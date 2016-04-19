package br.com.constran.mobile.persistence.dao.aprop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.aprop.ApropriacaoVO;
import br.com.constran.mobile.persistence.vo.imp.AtividadeVO;
import br.com.constran.mobile.persistence.vo.imp.FrenteObraVO;

public class ApropriacaoDAO extends AbstractDAO {

    private static final String APROPRIACAO_COL_ID = "idApropriacao";
    private static final String APROPRIACAO_COL_DATA_HORA_APONT = "dataHoraApontamento";
    private static final String APROPRIACAO_COL_ATIVIDADE = "atividade";
    private static final String APROPRIACAO_COL_FRENTES_OBRA = "frentesObra";
    private static final String APROPRIACAO_COL_OBS = "observacoes";
    private static final String APROPRIACAO_COL_TIPO_APROP = "tipoApropriacao";

    private static ApropriacaoDAO instance;

    private ApropriacaoDAO(Context context) {
        super(context, TBL_APROPRIACAO);
    }

    public static ApropriacaoDAO getInstance(Context context) {
        if (instance == null) {
            instance = new ApropriacaoDAO(context);
        }

        return instance;
    }

    public Integer save(ApropriacaoVO pVO) {

        StringBuilder builder = new StringBuilder("");

        if (pVO.getId() == null) {
            return insert(getContentValues(pVO)).intValue();
        } else {

            builder.append(" update [apropriacoes] set [observacoes] = '");
            builder.append(pVO.getObservacoes());
            builder.append("' where [idApropriacao] = ");
            builder.append(pVO.getId());
            builder.append(";");

            execute(builder);

            return pVO.getId();
        }
    }

    /**
     * Busca a apropriacao pela chave alternativa (frenteObra, atividade, dataHoraApontamento e tipoApropriacao)
     *
     * @param apropriacao
     * @return
     */
    public ApropriacaoVO findByPK(ApropriacaoVO apropriacao) {
        if (apropriacao == null) {
            return null;
        }

        AtividadeVO atividade = apropriacao.getAtividade();
        Integer idAtividade = atividade.getIdAtividade();
        Integer frenteObra = atividade.getFrenteObra().getId();
        String data = apropriacao.getDataHoraApontamento();
        String tipo = apropriacao.getTipoApropriacao();
        data = data != null ? data.substring(0, 10) : data;

        StringBuilder query = new StringBuilder();
        query.append(" select * from ").append(TBL_APROPRIACAO)
                .append(" where frentesObra = ? and atividade = ? and substr(dataHoraApontamento,0, 11) = ? and tipoApropriacao = ? ");

        Cursor cursor = super.findByQuery(query.toString(), concatArgs(frenteObra, idAtividade, data, tipo));

        ApropriacaoVO apropriacaoVO = null;

        if (cursor != null && cursor.moveToNext()) {
            apropriacaoVO = popularEntidade(cursor);
        }

        closeCursor(cursor);

        return apropriacaoVO;
    }

    public ApropriacaoVO findByIdApropriacao(Integer idApropriacao) {
        StringBuilder query = new StringBuilder();
        query.append(" select apr.*, foa.*, fob.descricao ").append(ALIAS_DESCRICAO2).append(" from ")
                .append(TBL_APROPRIACAO).append(" apr ")
                .append(" inner join ").append(TBL_ATIVIDADE).append(" foa ")
                .append("on foa.frentesObra = apr.frentesObra and foa.atividade = apr.atividade")
                .append(" inner join ").append(TBL_FRENTE_OBRA).append(" fob ")
                .append("on fob.idFrentesObra = apr.frentesObra ")
                .append(" where apr.idApropriacao = ?");

        Cursor cursor = super.findByQuery(query.toString(), concatArgs(idApropriacao));

        ApropriacaoVO apropriacaoVO = null;

        if (cursor != null && cursor.moveToNext()) {
            apropriacaoVO = popularEntidade(cursor);

            AtividadeVO atividade = apropriacaoVO.getAtividade();
            FrenteObraVO frenteObra = atividade.getFrenteObra();

            String d1 = frenteObra.getDescricao();
            String d2 = atividade.getDescricao();

            d1 = d1.length() > LIMITE ? d1.substring(0, LIMITE) : d1;
            d2 = d2.length() > LIMITE ? d2.substring(0, LIMITE) : d2;
            apropriacaoVO.setDescricao(d1 + "/" + d2);
        }

        closeCursor(cursor);

        return apropriacaoVO;
    }

    /**
     * Exclui apropriacoes orfans dos módulos de servico/mao-de-obra
     *
     * @return
     */
    public void deleteOrfan(Integer id, String data) {
        StringBuilder whereClause = new StringBuilder();
        whereClause.append(" idApropriacao not in (select distinct apropriacao from apropriacoesMaoObra) ")
                .append(" and idApropriacao not in (select distinct apropriacao from paralisacoesEquipe) ")
                .append(" and idApropriacao not in (select distinct idApropriacao from apropriacaoServico) ")
                .append(" and tipoApropriacao = 'SRV' and idApropriacao = ? and substr(dataHoraApontamento,0, 11) = ?");

        delete(whereClause.toString(), concatArgs(id, data));
    }

    public Integer getMaxId() {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{"max(a.[idApropriacao])"};
        tableJoin = " [apropriacoes] a ";
        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        cursor.moveToNext();

        int maxId = cursor.getInt(0);

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return maxId;
    }

    private ApropriacaoVO popularEntidade(Cursor cursor) {
        if (!cursor.isClosed() && cursor.getCount() > 0) {
            ApropriacaoVO apropriacao = new ApropriacaoVO();
            FrenteObraVO frenteObra = new FrenteObraVO();
            AtividadeVO atividade = new AtividadeVO(getInt(cursor, APROPRIACAO_COL_ATIVIDADE), getString(cursor, ALIAS_DESCRICAO));
            apropriacao.setId(getInt(cursor, APROPRIACAO_COL_ID));
            apropriacao.setDataHoraApontamento(getString(cursor, APROPRIACAO_COL_DATA_HORA_APONT));
            apropriacao.setTipoApropriacao(getString(cursor, APROPRIACAO_COL_TIPO_APROP));
            apropriacao.setObservacoes(getString(cursor, APROPRIACAO_COL_OBS));
            frenteObra.setId(getInt(cursor, APROPRIACAO_COL_FRENTES_OBRA));
            frenteObra.setDescricao(getString(cursor, ALIAS_DESCRICAO2));
            atividade.setFrenteObra(frenteObra);
            apropriacao.setAtividade(atividade);

            return apropriacao;
        }

        return null;
    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        ApropriacaoVO pVO = (ApropriacaoVO) abstractVO;

        ContentValues contentValues = new ContentValues();
        contentValues.put(APROPRIACAO_COL_DATA_HORA_APONT, pVO.getDataHoraApontamento());
        contentValues.put(APROPRIACAO_COL_ATIVIDADE, pVO.getAtividade().getIdAtividade());
        contentValues.put(APROPRIACAO_COL_FRENTES_OBRA, pVO.getAtividade().getFrenteObra().getId());
        contentValues.put(APROPRIACAO_COL_OBS, pVO.getObservacoes());
        contentValues.put(APROPRIACAO_COL_TIPO_APROP, pVO.getTipoApropriacao());

        return contentValues;
    }

    @Override
    public int deleteById(Integer id) {
        return delete(concatClauses(new String[]{APROPRIACAO_COL_ID}), concatArgs(id));
    }

}
