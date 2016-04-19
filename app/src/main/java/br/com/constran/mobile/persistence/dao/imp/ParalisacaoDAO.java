package br.com.constran.mobile.persistence.dao.imp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.R;
import br.com.constran.mobile.enums.TipoAplicacao;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.imp.ParalisacaoVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParalisacaoDAO extends AbstractDAO {
    private static final Integer COD_PRODUZINDO = 16;
    private static final Integer COD_SEGUINTE_PRODUZINDO = 17;

    private static final String PARALISACAO_COL_ID_PARALISACAO = "idParalisacao";
    private static final String PARALISACAO_COL_REQUER_ESTACA = "requerEstaca";
    private static final String PARALISACAO_COL_CODIGO = "codigo";
    private static final String PARALISACAO_COL_DESC = "descricao";
    private static final String PARALISACAO_COL_APLICACAO = "aplicacao";

    private static ParalisacaoDAO instance;

    private ParalisacaoDAO(Context context) {
        super(context, TBL_PARALISACAO);
    }

    public static ParalisacaoDAO getInstance(Context context) {
        if (instance == null) {
            instance = new ParalisacaoDAO(context);
        }

        return instance;
    }

    /**
     * Lista as paralisacoes, incluindo tipo produzindo (16) e sem concatenar com o codigo
     *
     * @param tipoAplicacao
     * @return
     */
    public List<ParalisacaoVO> findSimpleList(TipoAplicacao tipoAplicacao) {
        StringBuilder query = new StringBuilder();
        query.append("select * from ").append(TBL_PARALISACAO)
                .append(" where aplicacao is null or aplicacao = ? or aplicacao = ? ")
                .append("order by ").append(PARALISACAO_COL_CODIGO);

        List<ParalisacaoVO> paralisacoes = new ArrayList<ParalisacaoVO>();

        Cursor cursor = findByQuery(query.toString(), concatArgs(TipoAplicacao.AMBOS.getCodigo(), tipoAplicacao.getCodigo()));

        while (cursor != null && cursor.moveToNext()) {
            ParalisacaoVO paralisacao = new ParalisacaoVO(getInt(cursor, PARALISACAO_COL_ID_PARALISACAO),
                    " " + getString(cursor, ALIAS_DESCRICAO),
                    getString(cursor, PARALISACAO_COL_REQUER_ESTACA),
                    getString(cursor, PARALISACAO_COL_CODIGO));
            paralisacao.setAplicacao(getString(cursor, PARALISACAO_COL_APLICACAO));

            if (COD_SEGUINTE_PRODUZINDO.equals(paralisacao.getId())) {
                paralisacoes.add(getParalisacaoProducao(false));
            }

            paralisacoes.add(paralisacao);
        }

        return paralisacoes;
    }

    public List<ParalisacaoVO> findList(TipoAplicacao tipoAplicacao) {
        List<ParalisacaoVO> paralisacoes = Arrays.asList(getArrayParalisacaoVO());
        List<ParalisacaoVO> paralisacoesProducao = new ArrayList<ParalisacaoVO>();

        for (ParalisacaoVO p : paralisacoes) {
            TipoAplicacao aplicacao = TipoAplicacao.findByCodigo(p.getAplicacao());

            //lista as paralisacoes com aplicacao null ou do tipo informada
            if (p.getAplicacao() == null || TipoAplicacao.AMBOS.equals(aplicacao) || TipoAplicacao.findByCodigo(p.getAplicacao()).equals(tipoAplicacao)) {
                int codigo = p.getCodigo() != null ? Integer.parseInt(p.getCodigo()) : 0;

                if (codigo == COD_SEGUINTE_PRODUZINDO) {
                    ParalisacaoVO prod = getParalisacaoProducao(true);
                    paralisacoesProducao.add(prod);
                }
                p.setDescricao(" " + p.getDescricao());

                if (!p.toString().trim().isEmpty()) {
                    paralisacoesProducao.add(p);
                }
            }
        }

        return paralisacoesProducao;
    }

    private ParalisacaoVO getParalisacaoProducao(boolean concatenaCodigo) {
        ParalisacaoVO prod = new ParalisacaoVO(COD_PRODUZINDO);
        prod.setDescricao(concatenaCodigo ? " 16 - Produzindo" : " Produzindo");

        return prod;
    }

    public ParalisacaoVO[] getArrayParalisacaoVO() {

        Query query = new Query(true);

        String[] columns = {PARALISACAO_COL_ID_PARALISACAO, "[codigo] || ' - '  || [descricao] " + ALIAS_DESCRICAO,
                PARALISACAO_COL_REQUER_ESTACA, PARALISACAO_COL_CODIGO, PARALISACAO_COL_APLICACAO};

        query.setColumns(columns);

        query.setTableJoin(TBL_PARALISACAO);

        query.setOrderBy("[codigo] ASC");

        Cursor cursor = getCursor(query);

        ParalisacaoVO[] dados = new ParalisacaoVO[cursor.getCount() + 1];

        int i = 0;

        dados[i++] = new ParalisacaoVO(getStr(R.string.EMPTY));

        while (cursor.moveToNext()) {
            ParalisacaoVO paralisacaoVO = new ParalisacaoVO(cursor.getInt(cursor.getColumnIndex(PARALISACAO_COL_ID_PARALISACAO)),
                    getString(cursor, ALIAS_DESCRICAO),
                    getString(cursor, PARALISACAO_COL_REQUER_ESTACA),
                    getString(cursor, PARALISACAO_COL_CODIGO));
            paralisacaoVO.setAplicacao(getString(cursor, PARALISACAO_COL_APLICACAO));
            dados[i++] = paralisacaoVO;

        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public Integer save(ParalisacaoVO pVO) {
        try {
            return insert(getContentValues(pVO)).intValue();
        } catch (Exception e) {
            return -1;
        }
    }

    public void deleteOrfan(Integer id) {
        StringBuilder whereClause = new StringBuilder();
        whereClause.append(" idParalisacao not in (select distinct paralisacao from paralisacoesMaoObra) ")
                .append(" and idParalisacao not in (select distinct idParalisacao from paralisacoesEquipe) ")
                .append(" where idParalisacao = ? ");

        delete(whereClause.toString(), concatArgs(id));
    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        ParalisacaoVO pVO = (ParalisacaoVO) abstractVO;
        ContentValues contentValues = new ContentValues();
        contentValues.put(PARALISACAO_COL_ID_PARALISACAO, pVO.getId());
        contentValues.put(PARALISACAO_COL_DESC, pVO.getDescricao());
        contentValues.put(PARALISACAO_COL_REQUER_ESTACA, pVO.getRequerEstaca());
        contentValues.put(PARALISACAO_COL_APLICACAO, pVO.getAplicacao());
        contentValues.put(PARALISACAO_COL_CODIGO, pVO.getCodigo());

        return contentValues;
    }

    @Override
    public int deleteById(Integer id) {
        return delete(concatClauses(new String[]{PARALISACAO_COL_ID_PARALISACAO}), concatArgs(id));
    }

}

