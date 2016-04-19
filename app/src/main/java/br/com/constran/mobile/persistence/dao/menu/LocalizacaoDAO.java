package br.com.constran.mobile.persistence.dao.menu;

import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.imp.AtividadeVO;
import br.com.constran.mobile.persistence.vo.imp.FrenteObraVO;
import br.com.constran.mobile.persistence.vo.imp.OrigemDestinoVO;
import br.com.constran.mobile.persistence.vo.menu.LocalizacaoVO;
import br.com.constran.mobile.view.util.Util;

import java.util.ArrayList;
import java.util.List;

public class LocalizacaoDAO extends AbstractDAO {

    //Colunas da tabela LOCALIZACAO
    private static final String LOCALIZACAO_COL_ID = "idLocalizacao";
    private static final String LOCALIZACAO_COL_FRENTE_OBRA = "frentesObra";
    private static final String LOCALIZACAO_COL_ATIVIDADE = "atividade";
    private static final String LOCALIZACAO_COL_ESTACA_INI = "estacaInicial";
    private static final String LOCALIZACAO_COL_ESTACA_FIM = "estacaFinal";
    private static final String LOCALIZACAO_COL_TIPO = "tipo";
    private static final String LOCALIZACAO_COL_DATA_HORA = "dataHora";
    private static final String LOCALIZACAO_COL_ORIGEM = "origem";
    private static final String LOCALIZACAO_COL_DESTINO = "destino";
    private static final String LOCALIZACAO_COL_OBRA = "obra";

    private static LocalizacaoDAO instance;

    private LocalizacaoDAO(Context context) {
        super(context, TBL_LOCALIZACAO);
    }

    public static LocalizacaoDAO getInstance(Context context) {
        if (instance == null) {
            instance = new LocalizacaoDAO(context);
        }

        return instance;
    }

    public void save(LocalizacaoVO pVO) {
        StringBuilder builder = new StringBuilder("");
        builder.append(" update " + TBL_LOCALIZACAO + " set [atual] = 'N'; ");
        execute(builder);

        builder = new StringBuilder("");
        builder.append("insert into localizacao ( ");
        builder.append("      frentesObra,        ");
        builder.append("      atividade,          ");
        builder.append("      origem,             ");
        builder.append("      destino,            ");
        builder.append("      dataHora,           ");
        builder.append("      dataAtualizacao,    ");
        builder.append("      estacaInicial,      ");
        builder.append("	  estacaFinal,        ");
        builder.append("	  atual, 		      ");
        builder.append("      tipo)               ");
        builder.append(" values (  ");
        builder.append(pVO.getAtividade().getFrenteObra().getId());
        builder.append(",");
        builder.append(pVO.getAtividade().getIdAtividade());
        builder.append(",");


        if (pVO.getOrigem() == null) {
            builder.append("null,");
        } else {
            builder.append(pVO.getOrigem().getId());
            builder.append(",");
        }

        if (pVO.getDestino() == null) {
            builder.append("null,");
        } else {
            builder.append(pVO.getDestino().getId());
            builder.append(",");
        }

        if (pVO.getDataHora() == null) {
            builder.append("null,");
        } else {
            builder.append("'");
            builder.append(pVO.getDataHora());
            builder.append("',");
        }

        if (pVO.getDataAtualizacao() == null) {
            builder.append("null,");
        } else {
            builder.append("'");
            builder.append(pVO.getDataAtualizacao());
            builder.append("',");
        }

        if (pVO.getEstacaInicial() == null) {
            builder.append("null,");
        } else {
            builder.append("'");
            builder.append(pVO.getEstacaInicial());
            builder.append("',");
        }

        if (pVO.getEstacaFinal() == null) {
            builder.append("null,");
        } else {
            builder.append("'");
            builder.append(pVO.getEstacaFinal());
            builder.append("',");
        }

        builder.append("'Y',");

        if (pVO.getTipo() == null) {
            builder.append("null");
        } else {
            builder.append("'");
            builder.append(pVO.getTipo());
            builder.append("'");
        }

        builder.append(");");

        execute(builder);
    }

    public LocalizacaoVO findLocalizacaoById(Integer id) {
        LocalizacaoVO local = null;

        Query query = buildQuery(id, false, false);

        Cursor cursor = getCursor(query);

        if (cursor != null && cursor.moveToNext()) {
            local = popularLocal(cursor);
        }

        closeCursor(cursor);

        return local;
    }

    public List<LocalizacaoVO> findList(boolean todayOnly) {
        List<LocalizacaoVO> localizacaoList = new ArrayList<LocalizacaoVO>();

        Query query = buildQuery(null, todayOnly, false);

        Cursor cursor = getCursor(query);

        while (cursor.moveToNext()) {
            LocalizacaoVO local = popularLocal(cursor);

            localizacaoList.add(local);
        }

        return localizacaoList;
    }

    private LocalizacaoVO popularLocal(Cursor cursor) {
        LocalizacaoVO local = new LocalizacaoVO();
        AtividadeVO atividade = new AtividadeVO();
        FrenteObraVO frenteObra = new FrenteObraVO();
        local.setId(getInt(cursor, LOCALIZACAO_COL_ID));
        frenteObra.setId(getInt(cursor, LOCALIZACAO_COL_FRENTE_OBRA));
        frenteObra.setIdObra(getInt(cursor, LOCALIZACAO_COL_OBRA));
        frenteObra.setDescricao(" " + getString(cursor, ALIAS_DESCRICAO));
        atividade.setIdAtividade(getInt(cursor, LOCALIZACAO_COL_ATIVIDADE));
        atividade.setDescricao(getString(cursor, ALIAS_DESCRICAO2));
        atividade.setFrenteObra(frenteObra);
        local.setAtividade(atividade);

        String d1 = frenteObra.getDescricao();
        String d2 = atividade.getDescricao();

        d1 = d1.length() > LIMITE ? d1.substring(0, LIMITE) : d1;
        d2 = d2.length() > LIMITE ? d2.substring(0, LIMITE) : d2;

        local.setDescricao(d1 + "/" + d2);
        return local;
    }

    /**
     * @param id
     * @param todayOnly
     * @return
     */
    private Query buildQuery(Integer id, boolean todayOnly, boolean atualOnly) {
        String conditions = " 1 = 1 ";
        String orderBy = null;

        String[] columns = new String[]{
                "l.idLocalizacao",
                "l.frentesObra ",
                "l.atividade ",
                "f.obra",
                "f.descricao " + ALIAS_DESCRICAO,
                "a.descricao " + ALIAS_DESCRICAO2};

        String tableJoin = "[localizacao] l";
        tableJoin += " join [frentesObra] f on f.[idFrentesObra] = l.[frentesObra]";
        tableJoin += " join [frentesObraAtividade] a on a.[atividade] = l.[atividade]";
        tableJoin += " and f.[idFrentesObra] = a.[frentesObra]";

        if (todayOnly) {
            conditions = " substr(l.[dataHora],0, 11) = '" + Util.getToday() + "'";
        }

        if (id != null) {
            conditions += " and " + LOCALIZACAO_COL_ID + " = " + id;
        }

        if (atualOnly) {
            conditions += " and l.atual = 'Y'";
        }

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        return query;
    }

    public Cursor getCursor() {
        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{
                "l.[idLocalizacao]",
                "substr(l.[dataHora],12, 5)",
                " case when o.[descricao] is not null then " +
                        "o.[descricao] || ' (' || trim(o.[estacaInicial]) || ' ' || trim(o.[estacaFinal]) || ')'" +
                        " when o.[descricao] is null then " +
                        "d.[descricao] || ' (' || trim(d.[estacaInicial]) || ' ' || trim(d.[estacaFinal]) || ')' end ",
                "l.[tipo]"};

        tableJoin = "[localizacao] l";
        tableJoin += " left join [origensDestinos] d on l.[destino] = d.[idOrigensDestinos]";
        tableJoin += " left join [origensDestinos] o on l.[origem] = o.[idOrigensDestinos]";

        conditions = " substr(l.[dataHora],0, 11) = '" + Util.getToday() + "'";
        conditions += " and atual = 'N'";

        orderBy = " datetime(substr(l.[dataHora],12, 8)) desc ";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        return getCursor(query);
    }

    public String[] getValues() {
        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{
                "l.[frentesObra]",
                "l.[atividade]",
                "l.[estacaInicial]",
                "l.[estacaFinal]",
                "l.[tipo]",
                "l.[dataHora]",
                "l.[origem]",
                "l.[destino]",
                "f.[descricao]" + ALIAS_DESCRICAO,
                "a.[descricao]" + ALIAS_DESCRICAO2,
                "o.[descricao] || ' (' || trim(o.[estacaInicial]) || ' ' || trim(o.[estacaFinal]) || ')'" + ALIAS_DESCRICAO3,
                "d.[descricao] || ' (' || trim(d.[estacaInicial]) || ' ' || trim(d.[estacaFinal]) || ')'" + ALIAS_DESCRICAO4,
                "ifnull(o.[estacaInicial], d.[estacaInicial])" + ALIAS_ESTACA_INI,
                "ifnull(o.[estacaFinal], d.[estacaFinal])" + ALIAS_ESTACA_FIM};

        tableJoin = "[localizacao] l";
        tableJoin += " join [frentesObra] f on f.[idFrentesObra] = l.[frentesObra]";
        tableJoin += " join [frentesObraAtividade] a on a.[atividade] = l.[atividade]";
        tableJoin += " and f.[idFrentesObra] = a.[frentesObra]";
        tableJoin += " left join [origensDestinos] d on l.[destino] = d.[idOrigensDestinos]";
        tableJoin += " left join [origensDestinos] o on l.[origem] = o.[idOrigensDestinos]";

        conditions = " substr(l.[dataHora],0, 11) = '" + Util.getToday() + "'";

        conditions += " and atual = 'Y'";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        String[] dados = preencherDados(cursor);

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public LocalizacaoVO findLocalizacaoAtual() {
        LocalizacaoVO local = null;

        Query query = buildQuery(null, true, true);

        Cursor cursor = getCursor(query);

        if (cursor != null && cursor.moveToNext()) {
            local = popularLocal(cursor);
        }

        closeCursor(cursor);

        return local;
    }

    public LocalizacaoVO getById(Integer id) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{
                "[frentesObra]",
                "[atividade]",
                "[estacaInicial]",
                "[estacaFinal]",
                "[tipo]",
                "[origem]",
                "[destino]",};
        tableJoin = "[localizacao]";
        conditions = " idLocalizacao = " + id;

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        cursor.moveToNext();

        LocalizacaoVO vo = new LocalizacaoVO();

        vo.setAtividade(new AtividadeVO(cursor.getInt(cursor.getColumnIndex(LOCALIZACAO_COL_ATIVIDADE)),
                cursor.getInt(cursor.getColumnIndex(LOCALIZACAO_COL_FRENTE_OBRA))));
        vo.setEstacaInicial(cursor.getString(cursor.getColumnIndex(LOCALIZACAO_COL_ESTACA_INI)));
        vo.setEstacaFinal(cursor.getString(cursor.getColumnIndex(LOCALIZACAO_COL_ESTACA_FIM)));
        vo.setDataHora(Util.getNow());
        vo.setDataAtualizacao(vo.getDataHora());
        vo.setTipo(cursor.getString(cursor.getColumnIndex(LOCALIZACAO_COL_TIPO)));

        if (cursor.getInt(cursor.getColumnIndex(LOCALIZACAO_COL_ORIGEM)) != 0) {
            vo.setOrigem(new OrigemDestinoVO(cursor.getInt(cursor.getColumnIndex(LOCALIZACAO_COL_ORIGEM))));
        }

        if (cursor.getInt(cursor.getColumnIndex(LOCALIZACAO_COL_DESTINO)) != 0) {
            vo.setDestino(new OrigemDestinoVO(cursor.getInt(cursor.getColumnIndex(LOCALIZACAO_COL_DESTINO))));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return vo;
    }

    @Override
    protected String[] getColunas() {
        return new String[]{
                LOCALIZACAO_COL_FRENTE_OBRA,
                LOCALIZACAO_COL_ATIVIDADE,
                LOCALIZACAO_COL_ESTACA_INI,
                LOCALIZACAO_COL_ESTACA_FIM,
                LOCALIZACAO_COL_TIPO,
                LOCALIZACAO_COL_DATA_HORA,
                LOCALIZACAO_COL_ORIGEM,
                LOCALIZACAO_COL_DESTINO,
                ALIAS_DESCRICAO,
                ALIAS_DESCRICAO2,
                ALIAS_DESCRICAO3,
                ALIAS_DESCRICAO4,
                ALIAS_ESTACA_INI,
                ALIAS_ESTACA_FIM
        };
    }

}