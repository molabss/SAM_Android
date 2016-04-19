package br.com.constran.mobile.persistence.dao.imp.rae.abs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.rae.abs.AbastecimentoPostoVO;
import br.com.constran.mobile.persistence.vo.rae.abs.CombustivelLubrificanteVO;
import br.com.constran.mobile.persistence.vo.rae.abs.PostoVO;
import br.com.constran.mobile.persistence.vo.rae.abs.SaldoPostoVO;
import br.com.constran.mobile.view.util.Util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AbastecimentoPostoDAO extends AbstractDAO {

    private static final String ABASTEC_POSTO_COL_ID_POSTO = "idPosto";
    private static final String ABASTEC_POSTO_COL_QTE = "quantidade";
    private static final String ABASTEC_POSTO_COL_DATA = "data";
    private static final String ABASTEC_POSTO_COL_HORA = "hora";
    private static final String ABASTEC_POSTO_COL_POSTO = "posto";
    private static final String ABASTEC_POSTO_COL_POSTO2 = "posto2";
    private static final String ABASTEC_POSTO_COL_COMBUST = "combustivel";
    private static final String ABASTEC_POSTO_COL_TIPO = "tipo";

    private static AbastecimentoPostoDAO instance;

    private AbastecimentoPostoDAO(Context context) {
        super(context, TBL_ABASTECIM_POSTO);
    }

    public static AbastecimentoPostoDAO getInstance(Context context) {
        if (instance == null) {
            instance = new AbastecimentoPostoDAO(context);
        }

        return instance;
    }

    public Cursor getCursor(Integer idPosto) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{"a.[posto]", "a.[combustivel]", "a.[data]", "a.[hora]", "c.[descricao]", "a.[quantidade] || ' ' || c.[unidadeMedida]", "c.[tipo]"};

        tableJoin = "[abastecimentosPosto] a  ";
        tableJoin += " join[postos] p on p.[idPosto] = a.[posto] ";
        tableJoin += " join [combustiveisLubrificantes] c on a.[combustivel] = c.[idCombustivelLubrificante] ";
        conditions = " a.data  = '" + Util.getToday() + "'";
        conditions += " and a.posto = '" + idPosto + "'";

        orderBy = "datetime(a.[hora]) asc, c.[descricao] asc ";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        return getCursor(query);
    }

    public SaldoPostoVO getSaldoPostoVO(Integer idObra) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{"a.[posto]", "a.[posto2]", "a.[combustivel]", "a.[data]", "a.[hora]",
                "c.[descricao]", "a.[quantidade]", "c.[tipo]"};

        tableJoin = "[abastecimentosPosto] a  ";
        tableJoin += " join [postos] p on p.[idPosto] = a.[posto] ";
        tableJoin += " join [combustiveisLubrificantes] c on a.[combustivel] = c.[idCombustivelLubrificante] ";

        orderBy = "a.[posto], date(substr(a.[data],7)||substr(a.[data],4,2)||substr(a.[data],1,2)), datetime(a.[hora]) asc, a.[combustivel] ";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);


        List<AbastecimentoPostoVO> abastecimentoPostoVOs = new ArrayList<AbastecimentoPostoVO>();

        while (cursor.moveToNext()) {
            AbastecimentoPostoVO abastecimentoPostoVO = new AbastecimentoPostoVO();
            abastecimentoPostoVO.setPosto(new PostoVO(cursor.getInt(cursor.getColumnIndex(ABASTEC_POSTO_COL_POSTO))));
            abastecimentoPostoVO.setCombustivelLubrificante(new CombustivelLubrificanteVO(cursor.getInt(cursor.getColumnIndex(ABASTEC_POSTO_COL_COMBUST))));
            abastecimentoPostoVO.setData(cursor.getString(cursor.getColumnIndex(ABASTEC_POSTO_COL_DATA)));
            abastecimentoPostoVO.setHora(cursor.getString(cursor.getColumnIndex(ABASTEC_POSTO_COL_HORA)));
            abastecimentoPostoVO.setQtd(cursor.getString(cursor.getColumnIndex(ABASTEC_POSTO_COL_QTE)));
            abastecimentoPostoVO.setTipo(cursor.getString(cursor.getColumnIndex(ABASTEC_POSTO_COL_TIPO)));

            int idPosto2 = cursor.getInt(cursor.getColumnIndex(ABASTEC_POSTO_COL_POSTO2));

            //verifica se é transferencia
            if (idPosto2 != 0) {
                abastecimentoPostoVO.setPosto2(new PostoVO(idPosto2));
            }


            //ORIGINAL - VOLTAR LOGO
            //int qtd  = Integer.parseInt(abastecimentoPostoVO.getQtd());

            //DEBUG NITEROI - REMOVER
            //----------------------------------------------------------------------------------
            int qtd = 0;

            try{
                qtd  = Integer.parseInt(abastecimentoPostoVO.getQtd());
            } catch (NumberFormatException e){

                if(abastecimentoPostoVO.getQtd().contains(".")){
                    qtd = Integer.parseInt(abastecimentoPostoVO.getQtd().replaceAll("\\.0*$", ""));
                }
            }
            //-------------------------------------------------------------------------------



            //se nao for transferencia ou se for transferencia e não for qte negativa, entao adiciona a lista
            if (idPosto2 == 0 || (idPosto2 != 0 && qtd > 0)) {
                abastecimentoPostoVOs.add(abastecimentoPostoVO);
            }
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return new SaldoPostoVO(idObra, abastecimentoPostoVOs);
    }

    public List<AbastecimentoPostoVO> findAbastecimentosPostoByData(Date data) {
        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{ABASTEC_POSTO_COL_COMBUST, ABASTEC_POSTO_COL_POSTO, ABASTEC_POSTO_COL_DATA};

        tableJoin = "[abastecimentosPosto] a  ";
        conditions = " a.data  = '" + Util.getDateFormated(data) + "'";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        List<AbastecimentoPostoVO> abastecimentoPostoVOs = new ArrayList<AbastecimentoPostoVO>();

        while (cursor.moveToNext()) {
            AbastecimentoPostoVO abastecimentoPostoVO = new AbastecimentoPostoVO();
            abastecimentoPostoVO.setPosto(new PostoVO(cursor.getInt(cursor.getColumnIndex(ABASTEC_POSTO_COL_POSTO))));
            abastecimentoPostoVO.setCombustivelLubrificante(new CombustivelLubrificanteVO(cursor.getInt(cursor.getColumnIndex(ABASTEC_POSTO_COL_COMBUST))));
            abastecimentoPostoVO.setData(cursor.getString(cursor.getColumnIndex(ABASTEC_POSTO_COL_DATA)));

            abastecimentoPostoVOs.add(abastecimentoPostoVO);
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return abastecimentoPostoVOs;
    }

    public List<AbastecimentoPostoVO> findAbastecimentosByPk(AbastecimentoPostoVO abs) {
        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{ABASTEC_POSTO_COL_COMBUST, ABASTEC_POSTO_COL_POSTO, ABASTEC_POSTO_COL_DATA};

        tableJoin = "[abastecimentosPosto] a  ";
        conditions = " a.data  = '" + abs.getData() + "' AND a.hora = '" + abs.getHora() + "' AND a.combustivel = " + abs.getCombustivelLubrificante().getId();

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        List<AbastecimentoPostoVO> abastecimentoPostoVOs = new ArrayList<AbastecimentoPostoVO>();

        while (cursor.moveToNext()) {
            AbastecimentoPostoVO abastecimentoPostoVO = new AbastecimentoPostoVO();
            abastecimentoPostoVO.setPosto(new PostoVO(cursor.getInt(cursor.getColumnIndex(ABASTEC_POSTO_COL_POSTO))));
            abastecimentoPostoVO.setCombustivelLubrificante(new CombustivelLubrificanteVO(cursor.getInt(cursor.getColumnIndex(ABASTEC_POSTO_COL_COMBUST))));
            abastecimentoPostoVO.setData(cursor.getString(cursor.getColumnIndex(ABASTEC_POSTO_COL_DATA)));

            abastecimentoPostoVOs.add(abastecimentoPostoVO);
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return abastecimentoPostoVOs;
    }

    public String[] getValues(String[] arrayPK) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{"a.[data] || ' ' || a.[hora] " + ALIAS_DATA_HORA,
                "c.[descricao] " + ALIAS_DESCRICAO,
                "a.[quantidade] || ' ' || c.[unidadeMedida] " + ALIAS_QTE_MEDIDA, "p2.[idPosto]",
                "p2.[descricao] " + ALIAS_DESCRICAO2, "a.[quantidade]"};

        tableJoin = "[abastecimentosPosto] a  ";
        tableJoin += " join[postos] p on p.[idPosto] = a.[posto] ";
        tableJoin += " left join[postos] p2 on p2.[idPosto] = a.[posto2] ";
        tableJoin += " join [combustiveisLubrificantes] c on a.[combustivel] = c.[idCombustivelLubrificante] ";
        conditions = " a.posto  = " + arrayPK[0];
        conditions += " and a.combustivel  = " + arrayPK[1];
        conditions += " and a.data  = '" + arrayPK[2] + "'";
        conditions += " and a.hora  = '" + arrayPK[3] + "'";

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

    public Double getSaldo(Integer idPosto, Integer idCombustivel) {

        String saldo = "0";

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;

        StringBuilder sQuery1 = new StringBuilder();
        StringBuilder sQuery2 = new StringBuilder();
        StringBuilder sQuery3 = new StringBuilder();

        sQuery1.append(" (SELECT IFNULL(SUM(A.[quantidade]), 0)                            ");
        sQuery1.append("            FROM ABASTECIMENTOSPOSTO A                             ");
        sQuery1.append("           WHERE A.DATA = 				                          '");
        sQuery1.append(Util.getToday());
        sQuery1.append("'           AND A.POSTO =                                         '");
        sQuery1.append(idPosto);
        sQuery1.append("'           AND C.[idCombustivelLubrificante] = A.[combustivel]    ");
        sQuery1.append("   )                                     						   ");

        sQuery2.append(" (SELECT IFNULL(SUM(A.[quantidade]), 0)                            ");
        sQuery2.append("            FROM ABASTECIMENTOS A  JOIN RAE R ON R.IDRAE = A.RAE   ");
        sQuery2.append("           WHERE R.DATA = 				                       	  '");
        sQuery2.append(Util.getToday());
        sQuery2.append("'           AND R.POSTO =                                         '");
        sQuery2.append(idPosto);
        sQuery2.append("'           AND C.[idCombustivelLubrificante] = A.[combustivel]    ");
        sQuery2.append("   )                                     						   ");

        sQuery3.append(sQuery1.toString());
        sQuery3.append("                           -                        			   ");
        sQuery3.append(sQuery2.toString());


        columns = new String[]{sQuery3.toString()};

        tableJoin = " combustiveisLubrificantes C  ";
        tableJoin += " JOIN COMBUSTIVEISPOSTOS CP ON (CP.[combustivel] = C.[idCombustivelLubrificante]) ";
        tableJoin += " JOIN POSTOS P ON (P.[idPosto] = CP.[posto]) ";
        tableJoin += " LEFT JOIN  ABASTECIMENTOS A ON (A.[combustivel] = C.[idCombustivelLubrificante]) ";
        tableJoin += " LEFT JOIN  RAE R ON (R.[idRae] = A.[rae]) ";
        tableJoin += " LEFT JOIN ABASTECIMENTOSPOSTO AP ON (P.[idPosto] = AP.[posto]  and AP.[combustivel] = C.[idCombustivelLubrificante])";

        conditions = " IFNULL(AP.DATA, R.DATA) = '" + Util.getToday() + "'";
        conditions += " and IFNULL(AP.POSTO, R.POSTO) = " + idPosto;
        conditions += " and C.[idCombustivelLubrificante] = " + idCombustivel;

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);

        Cursor cursor = getCursor(query);

        if (cursor.moveToNext()) {
            saldo = cursor.getString(0);
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return new BigDecimal(saldo).doubleValue();
    }

    public Cursor getCursor2(Integer idPosto) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;
        String groupBy = null;


        StringBuilder sQuery1 = new StringBuilder();
        StringBuilder sQuery2 = new StringBuilder();
        StringBuilder sQuery3 = new StringBuilder();


        sQuery1.append(" 	(SELECT IFNULL(SUM(A.[quantidade]), 0)                            ");
        sQuery1.append("            FROM ABASTECIMENTOSPOSTO A                             ");
        sQuery1.append("           WHERE A.DATA = 				                          '");
        sQuery1.append(Util.getToday());
        sQuery1.append("'           AND A.POSTO =                                         '");
        sQuery1.append(idPosto);
        sQuery1.append("'           AND C.[idCombustivelLubrificante] = A.[combustivel]    ");
        sQuery1.append("   )                                     						   ");

        sQuery2.append(" 	(SELECT IFNULL(SUM(A.[quantidade]), 0)                            ");
        sQuery2.append("            FROM ABASTECIMENTOS A  JOIN RAE R ON R.IDRAE = A.RAE   ");
        sQuery2.append("           WHERE R.DATA = 				                       	  '");
        sQuery2.append(Util.getToday());
        sQuery2.append("'           AND R.POSTO =                                         '");
        sQuery2.append(idPosto);
        sQuery2.append("'           AND C.[idCombustivelLubrificante] = A.[combustivel]    ");
        sQuery2.append("   )                                     						   ");

        sQuery3.append(sQuery1.toString());
        sQuery3.append("                           -                        			   ");
        sQuery3.append(sQuery2.toString());


        columns = new String[]{"C.[descricao]",
                "cast(" + sQuery2.toString() + " as text) || ' ' || c.[unidadeMedida]",
                "cast(" + sQuery3.toString() + " as text) || ' ' || c.[unidadeMedida]"};

        tableJoin = " combustiveisLubrificantes C  ";
        tableJoin += " JOIN COMBUSTIVEISPOSTOS CP ON (CP.[combustivel] = C.[idCombustivelLubrificante]) ";
        tableJoin += " JOIN POSTOS P ON (P.[idPosto] = CP.[posto]) ";
        tableJoin += " LEFT JOIN  ABASTECIMENTOS A ON (A.[combustivel] = C.[idCombustivelLubrificante]) ";
        tableJoin += " LEFT JOIN  RAE R ON (R.[idRae] = A.[rae]) ";
        tableJoin += " LEFT JOIN ABASTECIMENTOSPOSTO AP ON (P.[idPosto] = AP.[posto]  and AP.[combustivel] = C.[idCombustivelLubrificante])";

        conditions = " IFNULL(AP.DATA, R.DATA) = '" + Util.getToday() + "'";
        conditions += " and IFNULL(AP.POSTO, R.POSTO) = " + idPosto;

        orderBy = "C.[descricao]";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);
        query.setGroupBy(groupBy);

        return getCursor(query);
    }

    public void save(AbastecimentoPostoVO pAbs) {

        StringBuilder builder = new StringBuilder();

        if (pAbs.getStrId() == null) {
            insert(getContentValues(pAbs));
        } else {
            builder.append(" update [abastecimentosPosto] set ");
            builder.append("     [posto2] =     ");
            builder.append(pAbs.getPosto2() == null ? pAbs.getPosto2() : pAbs.getPosto2().getId());
            builder.append("  ,  [tipo] = 		");
            builder.append(pAbs.getTipo() == null ? "''" : "'" + pAbs.getTipo() + "'");
            builder.append(" where [posto]  = 	 ");
            builder.append(pAbs.getPosto().getId());
            builder.append("  and [combustivel]  = ");
            builder.append(pAbs.getCombustivelLubrificante().getId());
            builder.append("  and [data]  = '");
            builder.append(pAbs.getData());
            builder.append("' and [hora]  = '");
            builder.append(pAbs.getHora());
            builder.append("';");

            execute(builder);
        }

    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        AbastecimentoPostoVO pAbs = (AbastecimentoPostoVO) abstractVO;

        ContentValues contentValues = new ContentValues();
        contentValues.put(ABASTEC_POSTO_COL_DATA, pAbs.getData());
        contentValues.put(ABASTEC_POSTO_COL_HORA, pAbs.getHora());
        contentValues.put(ABASTEC_POSTO_COL_POSTO, pAbs.getPosto().getId());
        contentValues.put(ABASTEC_POSTO_COL_COMBUST, pAbs.getCombustivelLubrificante().getId());
        contentValues.put(ABASTEC_POSTO_COL_QTE, pAbs.getQtd());

        return contentValues;
    }

    @Override
    protected String[] getColunas() {
        return new String[]{
                ALIAS_DATA_HORA,
                ALIAS_DESCRICAO,
                ALIAS_QTE_MEDIDA,
                ABASTEC_POSTO_COL_ID_POSTO,
                ALIAS_DESCRICAO2,
                ABASTEC_POSTO_COL_QTE
        };
    }


}
