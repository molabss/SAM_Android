package br.com.constran.mobile.persistence.dao.imp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import br.com.constran.mobile.R;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.imp.CategoriaVO;
import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;
import br.com.constran.mobile.view.util.Util;

public class EquipamentoDAO extends AbstractDAO {

    private static final String EQUIP_COL_ID_EQUIP = "idEquipamento";
    private static final String EQUIP_COL_PREFIXO = "prefixo";
    private static final String EQUIP_COL_DESC = "descricao";
    private static final String EQUIP_COL_MOVIMENTACAO = "movimentacao";
    private static final String EQUIP_COL_QRCODE = "qrcode";
    private static final String EQUIP_COL_ID_CATEGORIA = "idCategoria";
    private static final String EQUIP_COL_TIPO = "tipo";
    private static final String EQUIP_COL_EXIGE_JUSTIF = "exigeJustificativa";
    private static final String EQUIP_COL_HORIMETRO = "horimetro";
    private static final String EQUIP_COL_QUILOMETRAGEM = "quilometragem";

    private static EquipamentoDAO instance;

    private EquipamentoDAO(Context context) {
        super(context, TBL_EQUIPAMENTO);
    }

    public static EquipamentoDAO getInstance(Context context) {
        if (instance == null) {
            instance = new EquipamentoDAO(context);
        }

        return instance;
    }

    public EquipamentoVO[] getArrayEquipamentoParteDiariaVO() {

        Query query = new Query(true);

        query.setColumns(new String[]{"[idEquipamento]", "' ' || [prefixo] " + ALIAS_PREFIXO, "[descricao]", "[movimentacao]"});

        query.setTableJoin(TBL_EQUIPAMENTO);

        query.setOrderBy("[prefixo] ASC");

        String conditions = " not exists (select 1 from [equipamentosParteDiaria] where [equipamentosParteDiaria].[equipamento] = [equipamentos].[idEquipamento]";
        conditions += " and substr([equipamentosParteDiaria].[dataHora],0, 11) = '" + Util.getToday() + "')";

        query.setConditions(conditions);

        Cursor cursor = getCursor(query);

        EquipamentoVO[] dados = new EquipamentoVO[cursor.getCount()];

        preencherDados(cursor, dados);

        return dados;
    }

    public Cursor getCursorEquipamentosMovimentacaoDiaria(boolean descricaoCompleta) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        if (descricaoCompleta) {
            columns = new String[]{"e.[idEquipamento]", " e.[prefixo] || ' - ' || e.[descricao]"};
        } else {
            columns = new String[]{"e.[idEquipamento]", " e.[prefixo]"};
        }
        tableJoin = TBL_EQUIPAMENTO + " e join [equipamentosMovimentacaoDiaria] ed on e.[idEquipamento] = ed.[equipamento] ";
        orderBy = "e.[prefixo] asc";
        conditions = "substr(ed.[dataHora],0, 11) = '" + Util.getToday() + "'";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        return getCursor(query);
    }

    public Cursor getCursorEquipamentosParteDiaria(boolean descricaoCompleta) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        tableJoin = TBL_EQUIPAMENTO + " e join [equipamentosParteDiaria] ed on e.[idEquipamento] = ed.[equipamento] ";
        conditions = "substr(ed.[dataHora],0, 11) = '" + Util.getToday() + "'";

        if (descricaoCompleta) {
            columns = new String[]{"e.[idEquipamento]", " e.[prefixo] || ' - ' || e.[descricao]", "ed.[apropriacao]"};
        } else {
            columns = new String[]{"e.[idEquipamento]", " e.[prefixo]", "ed.[apropriacao]"};
            tableJoin += "join [apropriacoes] a on a.[idApropriacao] = ed.[apropriacao]";
        }

        orderBy = "e.[prefixo] asc";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        return getCursor(query);
    }

    public boolean isEquipamentoParteDiariaCadastrado(Integer idEquipamento, boolean isApropriacaoNull) {

        String columns[] = new String[]{"e.[idEquipamento]"};
        String tableJoin = TBL_EQUIPAMENTO + " e join [equipamentosParteDiaria] ed on e.[idEquipamento] = ed.[equipamento] ";
        String conditions = "substr(ed.[dataHora],0, 11) = '" + Util.getToday() + "' and e.[idEquipamento] = " + idEquipamento;
        String orderBy = null;

        if (isApropriacaoNull) {
            conditions += " and ed.apropriacao is null ";
        }

        Query query = new Query(false);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        boolean jaCadastrado = false;

        if (cursor.moveToNext()) {
            jaCadastrado = true;
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return jaCadastrado;
    }

    public EquipamentoVO[] getArrayEquipamentoVO() {

        Query query = new Query(true);

        query.setColumns(new String[]{"[idEquipamento]", "' ' || [prefixo] " + ALIAS_PREFIXO, "[descricao]", "[movimentacao]", "[idCategoria]",
                "[tipo]", "[exigeJustificativa]", "[horimetro]", "[quilometragem]"});

        query.setTableJoin("[equipamentos]");

        query.setOrderBy("[prefixo] ASC");

        Cursor cursor = getCursor(query);

        EquipamentoVO[] dados = new EquipamentoVO[cursor.getCount()];

        int i = 0;

        EquipamentoVO equipaVO = null;

        while (cursor.moveToNext()) {

            equipaVO = new EquipamentoVO(cursor.getInt(cursor.getColumnIndex(EQUIP_COL_ID_EQUIP)),
                    cursor.getString(cursor.getColumnIndex(ALIAS_PREFIXO)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_DESC)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_MOVIMENTACAO)),
                    cursor.getInt(cursor.getColumnIndex(EQUIP_COL_ID_CATEGORIA)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_TIPO)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_EXIGE_JUSTIF)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_HORIMETRO)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_QUILOMETRAGEM)));

            //atrubuindo uma simples scring que nao sera verificada conteudo, apenas para saber se deve concatenar
            //no array adapter o prefixo do equipamento com a descricao
            equipaVO.setPrefixoComDescricao("concatenar prefixo com descricao...");

            dados[i++] = equipaVO;
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public EquipamentoVO[] getArrayEquipamentosParteDiariaConsulta(String dataFiltro) {

        Query query = new Query(true);

        query.setColumns(new String[]{"e.[idEquipamento]", "' ' || e.[prefixo] " + ALIAS_PREFIXO, "e.[descricao]", "e.[movimentacao]"});

        String tableJoin = TBL_EQUIPAMENTO + " e inner join ";
        tableJoin += TBL_EVENTOS_EQUIPAMENTO + " ee on ee.equipamento = e.idEquipamento";

        query.setTableJoin(tableJoin);

        query.setOrderBy("e.[prefixo] ASC");

//		String conditions = " (e.[movimentacao] = '1' or  e.[movimentacao] = '2' or e. [movimentacao] = '3' )";

//		conditions += " and exists (select 1 from [eventosEquipamento] ee where ee.[equipamento] = e.[idEquipamento])";
        String conditions = " substr(ee.[dataHora],0, 11) = '" + dataFiltro + "'";

        query.setConditions(conditions);

        Cursor cursor = getCursor(query);

        EquipamentoVO[] dados = new EquipamentoVO[cursor.getCount()];

        preencherDados(cursor, dados);

        return dados;
    }

    public EquipamentoVO[] getArrayEquipamentoCarga() {

        Query query = new Query(true);

        query.setColumns(new String[]{"[idEquipamento]", "' ' ||  [prefixo] " + ALIAS_PREFIXO, "[descricao]", "[movimentacao]"});

        query.setTableJoin("[equipamentos]");

        query.setOrderBy("[prefixo] ASC");

        query.setConditions("[movimentacao] = '3'");

        Cursor cursor = getCursor(query);

        EquipamentoVO[] dados = new EquipamentoVO[cursor.getCount()];

        preencherDados(cursor, dados);

        return dados;
    }

    public EquipamentoVO[] getArrayEquipamentoMovimentacaoDiariaVO() {

        Query query = new Query(true);

        query.setColumns(new String[]{"[idEquipamento]", " ' ' || [prefixo] " + ALIAS_PREFIXO, "[descricao]", "[movimentacao]"});
        query.setTableJoin("[equipamentos]");
        query.setOrderBy("[prefixo] ASC");
        String conditions = " not exists (select 1 from [equipamentosMovimentacaoDiaria] where [equipamentosMovimentacaoDiaria].[equipamento] = [equipamentos].[idEquipamento]";
        conditions += " and substr([equipamentosMovimentacaoDiaria].[dataHora],0, 11) = '" + Util.getToday() + "')";
        conditions += " and ([movimentacao] = '1' or  [movimentacao] = '2' )";

        query.setConditions(conditions);

        Cursor cursor = getCursor(query);

        EquipamentoVO[] dados = new EquipamentoVO[cursor.getCount()];

        preencherDados(cursor, dados);

        return dados;
    }

    public EquipamentoVO[] getArrayEquipamentosMovimentacaoConsulta(String dataFiltro) {

        Query query = new Query(true);

        query.setColumns(new String[]{"e.[idEquipamento]", "' ' ||  e.[prefixo] " + ALIAS_PREFIXO, "e.[descricao]", "e.[movimentacao]"});

        query.setTableJoin("[equipamentos] e");

        query.setOrderBy("e.[prefixo] ASC");

        String conditions = " exists (select 1 from [viagensMovimentacoes] ve where ve.[equipamento] = e.[idEquipamento]";

        conditions += " and substr(ve.[dataHoraCadastro],0, 11) = '" + dataFiltro + "')";

        query.setConditions(conditions);

        Cursor cursor = getCursor(query);

        EquipamentoVO[] dados = new EquipamentoVO[cursor.getCount()];

        preencherDados(cursor, dados);

        return dados;
    }


    public EquipamentoVO[] getArrayEquipamentosAbastecimentoConsulta(String dataFiltro) {

        Query query = new Query(true);

        query.setColumns(new String[]{"e.[idEquipamento]", "' ' ||  e.[prefixo] " + ALIAS_PREFIXO, "e.[descricao]", "e.[movimentacao]"});

        query.setTableJoin("[equipamentos] e");

        query.setOrderBy("e.[prefixo] ASC");

        String conditions = " exists (select 1 from [abastecimentos] a join [rae] r where a.[rae] =  r.[idRae] and a.[equipamento] = e.[idEquipamento] ";

        conditions += " and TRIM(r.data) = '" + dataFiltro + "')";

        query.setConditions(conditions);

        Cursor cursor = getCursor(query);

        EquipamentoVO[] dados = new EquipamentoVO[cursor.getCount()];

        preencherDados(cursor, dados);

        return dados;
    }

    public void save(EquipamentoVO pVO) {
        insert(getContentValues(pVO));
    }

    public String getDescricao(Integer idEquipamento, boolean descricaoCompleta, boolean prefixo) {

        if (idEquipamento == null)
            return getStr(R.string.EMPTY);

        Query query = new Query(true);

        String columns[] = null;

        if (descricaoCompleta) {
            columns = new String[]{" [prefixo] || ' - ' || [descricao]"};
        } else if (prefixo) {
            columns = new String[]{" [prefixo]"};
        } else {
            columns = new String[]{" [descricao]"};
        }
        query.setColumns(columns);
        query.setTableJoin(" [equipamentos] ");
        query.setConditions(" [idEquipamento] = ? ");
        query.setConditionsArgs(new String[]{String.valueOf(idEquipamento)});

        Cursor cursor = getCursor(query);

        cursor.moveToNext();

        String value = cursor.getString(0).trim();

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return value;
    }

    public String getTipoMovimentacao(Integer idEquipamento) {

        Query query = new Query(true);

        String columns[] = new String[]{" [movimentacao]"};

        query.setColumns(columns);
        query.setTableJoin(" [equipamentos] ");
        query.setConditions(" [idEquipamento] = ? ");
        query.setConditionsArgs(new String[]{String.valueOf(idEquipamento)});

        Cursor cursor = getCursor(query);

        cursor.moveToNext();

        String value = cursor.getString(0).trim();

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return value;
    }

    public EquipamentoVO findByPrefixo(String prefixo) {
        Query query = new Query(true);

        query.setColumns(new String[]{EQUIP_COL_ID_EQUIP, EQUIP_COL_PREFIXO, EQUIP_COL_DESC, EQUIP_COL_MOVIMENTACAO,
                EQUIP_COL_ID_CATEGORIA, EQUIP_COL_TIPO, EQUIP_COL_EXIGE_JUSTIF, EQUIP_COL_HORIMETRO, EQUIP_COL_QUILOMETRAGEM});

        query.setTableJoin(TBL_EQUIPAMENTO);

        query.setConditions("prefixo = " + prefixo);

        Cursor cursor = getCursor(query);

        EquipamentoVO eqp = null;

        if (cursor.moveToNext()) {
            eqp = new EquipamentoVO(cursor.getInt(cursor.getColumnIndex(EQUIP_COL_ID_EQUIP)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_PREFIXO)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_DESC)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_MOVIMENTACAO)),
                    cursor.getInt(cursor.getColumnIndex(EQUIP_COL_ID_CATEGORIA)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_TIPO)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_EXIGE_JUSTIF)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_HORIMETRO)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_QUILOMETRAGEM)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return eqp;
    }

    public EquipamentoVO getByQRCode(Integer codigoQRCode) {

        Query query = new Query(true);

        query.setColumns(new String[]{EQUIP_COL_ID_EQUIP, EQUIP_COL_PREFIXO, EQUIP_COL_DESC, EQUIP_COL_MOVIMENTACAO,
                EQUIP_COL_ID_CATEGORIA, EQUIP_COL_TIPO, EQUIP_COL_EXIGE_JUSTIF, EQUIP_COL_HORIMETRO, EQUIP_COL_QUILOMETRAGEM});

        query.setTableJoin(TBL_EQUIPAMENTO);

        query.setConditions("qrCode LIKE '%" + codigoQRCode + "%' ");

        Cursor cursor = getCursor(query);

        EquipamentoVO eqp = null;

        if (cursor.moveToNext()) {
            eqp = new EquipamentoVO(cursor.getInt(cursor.getColumnIndex(EQUIP_COL_ID_EQUIP)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_PREFIXO)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_DESC)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_MOVIMENTACAO)),
                    cursor.getInt(cursor.getColumnIndex(EQUIP_COL_ID_CATEGORIA)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_TIPO)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_EXIGE_JUSTIF)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_HORIMETRO)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_QUILOMETRAGEM)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return eqp;
    }

    public EquipamentoVO getById(Integer idEquipamento) {

        Query query = new Query(true);

        query.setColumns(new String[]{EQUIP_COL_ID_EQUIP, EQUIP_COL_PREFIXO, EQUIP_COL_DESC, EQUIP_COL_MOVIMENTACAO,
                EQUIP_COL_ID_CATEGORIA, EQUIP_COL_TIPO, EQUIP_COL_EXIGE_JUSTIF, EQUIP_COL_HORIMETRO, EQUIP_COL_QUILOMETRAGEM});

        query.setTableJoin(TBL_EQUIPAMENTO);

        query.setConditions("idEquipamento = " + idEquipamento);

        Cursor cursor = getCursor(query);

        EquipamentoVO eqp = null;

        if (cursor.moveToNext()) {
            eqp = new EquipamentoVO(cursor.getInt(cursor.getColumnIndex(EQUIP_COL_ID_EQUIP)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_PREFIXO)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_DESC)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_MOVIMENTACAO)),
                    cursor.getInt(cursor.getColumnIndex(EQUIP_COL_ID_CATEGORIA)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_TIPO)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_EXIGE_JUSTIF)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_HORIMETRO)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_QUILOMETRAGEM)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return eqp;
    }

    private void preencherDados(Cursor cursor, EquipamentoVO[] dados) {
        int i = 0;

        while (cursor.moveToNext()) {
            dados[i++] = new EquipamentoVO(cursor.getInt(cursor.getColumnIndex(EQUIP_COL_ID_EQUIP)),
                    cursor.getString(cursor.getColumnIndex(ALIAS_PREFIXO)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_DESC)),
                    cursor.getString(cursor.getColumnIndex(EQUIP_COL_MOVIMENTACAO)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        EquipamentoVO pVO = (EquipamentoVO) abstractVO;

        ContentValues contentValues = new ContentValues();
        contentValues.put(EQUIP_COL_ID_EQUIP, pVO.getId());
        contentValues.put(EQUIP_COL_DESC, pVO.getDescricao());
        contentValues.put(EQUIP_COL_PREFIXO, pVO.getPrefixo());
        contentValues.put(EQUIP_COL_ID_CATEGORIA, pVO.getCategoria().getId());
      //contentValues.put(EQUIP_COL_TIPO, pVO.getTipo() != null ? pVO.getTipo() : getStr(R.string.EMPTY));
        contentValues.put(EQUIP_COL_TIPO, pVO.getTipo());
        contentValues.put(EQUIP_COL_EXIGE_JUSTIF, pVO.getExigeJustificativa());
        contentValues.put(EQUIP_COL_HORIMETRO, pVO.getHorimetro() != null ? pVO.getHorimetro() : getStr(R.string.EMPTY));
        contentValues.put(EQUIP_COL_QUILOMETRAGEM, pVO.getQuilometragem() != null ? pVO.getQuilometragem() : getStr(R.string.EMPTY));
        contentValues.put(EQUIP_COL_MOVIMENTACAO, pVO.getMovimentacao() != null ? pVO.getMovimentacao() : getStr(R.string.EMPTY));
        contentValues.put(EQUIP_COL_QRCODE, pVO.getQrcode() != null ? pVO.getQrcode() : getStr(R.string.EMPTY));

        return contentValues;
    }


    public EquipamentoVO [] getArrayEquipamentosManutencaoServicos(){

        StringBuilder select = new StringBuilder();
        select.append("select distinct(eqp.idEquipamento), ");
        select.append("eqp.descricao, eqp.movimentacao, eqp.tipo, eqp.horimetro, ");
        select.append("eqp.exigeJustificativa, eqp.quilometragem, eqp.idCategoria, ");
        select.append("eqp.prefixo, eqp.qrcode from equipamentos eqp ");
        select.append("inner join manutencaoServicoPorCategoriaEquipamento msp ");
        select.append("on msp.[idCategoriaEquipamento] = eqp.idCategoria order by eqp.prefixo");

        Cursor cursor = getCursorRaw(select.toString());
        EquipamentoVO[] equipamentosArray = new EquipamentoVO[cursor.getCount()];
        EquipamentoVO equipamento = null;
        CategoriaVO categoriaEqp = null;

        while(cursor.moveToNext()){

            equipamento = new EquipamentoVO();
            equipamento.setId(cursor.getInt(cursor.getColumnIndex("idEquipamento")));
            equipamento.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            equipamento.setMovimentacao(cursor.getString(cursor.getColumnIndex("movimentacao")));
            equipamento.setTipo(cursor.getString(cursor.getColumnIndex("tipo")));
            equipamento.setHorimetro(cursor.getString(cursor.getColumnIndex("horimetro")));
            equipamento.setExigeJustificativa(cursor.getString(cursor.getColumnIndex("exigeJustificativa")));
            equipamento.setQuilometragem(cursor.getString(cursor.getColumnIndex("quilometragem")));
            equipamento.setCategoria(new CategoriaVO(cursor.getInt(cursor.getColumnIndex("idCategoria"))));
            equipamento.setPrefixo(" ".concat(cursor.getString(cursor.getColumnIndex("prefixo"))));
            equipamento.setQrcode(cursor.getString(cursor.getColumnIndex("qrcode")));

            equipamento.setPrefixoComDescricao("concatenar prefixo com descricao...");

            equipamentosArray[cursor.getPosition()] = equipamento;

        }

        return equipamentosArray;
    }

}