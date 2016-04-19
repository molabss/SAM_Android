package br.com.constran.mobile.persistence.dao.imp.rae.abs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import br.com.constran.mobile.R;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.rae.RaeVO;
import br.com.constran.mobile.persistence.vo.rae.abs.AbastecimentoVO;

public class AbastecimentoDAO extends AbstractDAO {

    private static final String ABASTECIM_COL_DATA = "data";
    private static final String ABASTECIM_COL_PREF = "prefixo";
    private static final String ABASTECIM_COL_NOME = "nome";
    private static final String ABASTECIM_COL_HORA_INI = "horaInicio";
    private static final String ABASTECIM_COL_HORA_FIM = "horaTermino";
    private static final String ABASTECIM_COL_QUILOM = "quilometragem";
    private static final String ABASTECIM_COL_HORIMETRO = "horimetro";
    private static final String ABASTECIM_COL_QTE = "quantidade";
    private static final String ABASTECIM_COL_OBS = "observacao";
    private static final String ABASTECIM_COL_TIPO = "tipo";
    private static final String ABASTECIM_COL_OBS_JUST = "obsJustificativa";
    private static final String ABASTECIM_COL_RAE = "rae";
    private static final String ABASTECIM_COL_CC_OBRA = "ccObra";
    private static final String ABASTECIM_COL_FRENTE_OBRA = "frentesObra";
    private static final String ABASTECIM_COL_ATIVIDADE = "atividade";
    private static final String ABASTECIM_COL_COD_ABAST = "codAbastecedor";
    private static final String ABASTECIM_COL_ID_PES_ABAST = "idPessoalAbastecedor";
    private static final String ABASTECIM_COL_COD_OPERADOR = "codOperador";
    private static final String ABASTECIM_COL_ID_PES_OPER = "idPessoalOperador";
    private static final String ABASTECIM_COL_JUSTIFICATIVA = "justificativa";
    private static final String ABASTECIM_COL_EQUIPAMENTO = "equipamento";
    private static final String ABASTECIM_COL_COMBUSTIVE = "combustivel";

    private static AbastecimentoDAO instance;

    private AbastecimentoDAO(Context context) {
        super(context, TBL_ABASTECIMENTO);
    }

    public static AbastecimentoDAO getInstance(Context context) {
        if (instance == null) {
            instance = new AbastecimentoDAO(context);
        }

        return instance;
    }

    public Cursor getCursorSearch(Object[] params) {

        //Log.i("SEARCH","getCursorSearch("+params[0]+","+params[1]+","+params[2]+")");

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{"r.[idRAE]", "c.[idCombustivelLubrificante]", "e.[idEquipamento]", "a.[horaInicio]", "e.[prefixo]", "a.[horaTermino]", "c.[descricao]", "u.[senha]"};

        tableJoin = "[abastecimentos] a  ";
        tableJoin += "join[rae] r on r.[idRAE] = a.[rae] ";
        tableJoin += "join[equipamentos] e on e.[idEquipamento] = a.[equipamento] ";
        tableJoin += "join[postos] p on p.[idPosto] = r.[posto] ";
        tableJoin += "left join[usuarios] u on u.[codUsuario] = a.[codOperador] ";
        tableJoin += "join [combustiveisLubrificantes] c on a.[combustivel] = c.[idCombustivelLubrificante] ";
        conditions = "r.data  = '" + String.valueOf(params[0]) + "'";
        conditions += " and r.posto = '" + String.valueOf(params[1]) + "'";
        conditions += " and a.[codAbastecedor] =" + Integer.valueOf(String.valueOf(params[2]));

        if (params[3] != null)
            conditions += " and a.[equipamento] = " + Integer.valueOf(String.valueOf(params[3]));

        orderBy = "e.[prefixo] asc, p.[descricao] asc, datetime(a.[horaInicio]) asc ";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        return getCursor(query);
    }

    public String[] getArrayDatas(Integer idPosto) {

        //Log.i("SEARCH","getArrayDatas(Integer idPosto)");

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{"r.[data]"};

        tableJoin = " [abastecimentos] a ";
        tableJoin += " join [rae] r on r.[idRae] = a.[rae] ";

        conditions = " r.posto = " + idPosto;

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        String[] dados = new String[cursor.getCount()];

        int i = 0;

        while (cursor.moveToNext()) {
            dados[i++] = cursor.getString(cursor.getColumnIndex(ABASTECIM_COL_DATA));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public String[] getViewValues(String[] params) {

        //Log.i("SEARCH", "getViewValues((" + params[0] + "," + params[1] + "," + params[2] + "," + params[3] + "))");

        //0 ID RAE
        //1 ID COMBUSTIVEL
        //2 ID EQUIPAMENTO
        //3 HORA INICIO

        /*
        String[] condicoes = new String[]{params[0],params[1],params[2],params[3]};

        StringBuilder select = new StringBuilder();
        select.append("select r.data,e.prefixo,p.descricao as descricao,c.descricao as descricao2,uo.nome"  );
        select.append(   ",f.descricao as descricao3,af.descricao as descricao4,a.horaInicio,a.horaTermino" );
        select.append(   ",a.quilometragem,a.horimetro,jo.descricao as descricao5,a.quantidade,a.observacao");
        select.append(   ",e.descricao as descricao6 "                                                      );
        select.append("from abastecimentos a "                                                              );
        select.append(    "inner join rae r "                                                               );
        select.append(       "on r.idRae = a.rae "                                                          );
        select.append(    "inner join combustiveisLubrificantes c "                                         );
        select.append(       "on a.combustivel = c.idCombustivelLubrificante "                              );
        select.append(    "inner join equipamentos e "                                                      );
        select.append(       "on e.idEquipamento = a.equipamento "                                          );
        select.append(    "inner join postos p "                                                            );
        select.append(       "on p.idPosto = r.posto "                                                      );
        select.append(    "inner join usuarios ua "                                                         );
        select.append(       "on a.codAbastecedor = ua.codUsuario "                                         );
        select.append(    "left join frentesObraAtividade af "                                              );
        select.append(       "on af.atividade = a.atividade "                                               );
        select.append(    "left join justificativasOperador jo "                                            );
        select.append(       "on jo. idJustificativaOperador = a. justificativa "                           );
        select.append("where  r.idRae = ? "                                                                 );
        select.append(      "and a.combustivel = ? "                                                        );
        select.append(          "and a.equipamento = ? "                                                    );
        select.append(              "and a.horaInicio = ? "                                                 );
        select.append("order by e.prefixo asc "                                                             );
        select.append(      ",p.descricao asc "                                                             );
        select.append(          ",c.descricao asc");

        Cursor cursor = getCursorRawParams(select.toString(),condicoes);
        */



        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{
                "r.[data]",
                "e.[prefixo]",
                "p.[descricao] " + ALIAS_DESCRICAO,
                "c.[descricao]"  + ALIAS_DESCRICAO2,
                "uo.[nome]",
                "f.[descricao]"  + ALIAS_DESCRICAO3,
                "af.[descricao]" + ALIAS_DESCRICAO4,
                "a.[horaInicio]",
                "a.[horaTermino]",
                "a.[quilometragem]",
                "a.[horimetro]",
                "jo.[descricao]" + ALIAS_DESCRICAO5,
                "a.[quantidade]",
                "a.[observacao]",
                "e.[descricao]"  + ALIAS_DESCRICAO6};

        tableJoin = "[abastecimentos] a  ";
        tableJoin += " join[rae] r on r.[idRae] = a.[rae] ";
        tableJoin += " LEFT join[frentesObraAtividade] af on af.[atividade] = a.[atividade]";
        tableJoin += " LEFT join[frentesObra] f on f.[idFrentesObra] = af.[frentesObra] ";
        tableJoin += " join[equipamentos] e on e.[idEquipamento] = a.[equipamento] ";
        tableJoin += " join[postos] p on p.[idPosto] = r.[posto] ";
        tableJoin += " join[usuarios] ua on a.[codAbastecedor] = ua.[codUsuario] ";
        tableJoin += " left join[usuarios] uo on a.[codOperador] = uo.[codUsuario] ";
        tableJoin += " left join[justificativasOperador] jo on jo.[idJustificativaOperador] = a.[justificativa] ";
        tableJoin += " join [combustiveisLubrificantes] c on a.[combustivel] = c.[idCombustivelLubrificante] ";

        conditions = " r.[idRae] = " + Integer.valueOf(String.valueOf(params[0]));
        conditions += " and a.[combustivel] = " + Integer.valueOf(String.valueOf(params[1]));
        conditions += " and a.[equipamento] = " + Integer.valueOf(String.valueOf(params[2]));
        conditions += " and a.[horaInicio] = '" + String.valueOf(params[3]) + "'";

        orderBy = "e.[prefixo] asc, p.[descricao] asc, c.[descricao] asc ";

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

    public void save(RaeVO pRae, AbastecimentoVO pCab, AbastecimentoVO pAbs) {
        insert(getContentValues(pRae, pCab, pAbs));
    }

    private ContentValues getContentValues(RaeVO pRae, AbastecimentoVO pCab, AbastecimentoVO pAbs) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ABASTECIM_COL_HORA_INI, pCab.getHoraInicio());
        contentValues.put(ABASTECIM_COL_HORA_FIM, pCab.getHoraTermino());
        contentValues.put(ABASTECIM_COL_HORIMETRO, pCab.getHorimetro() == null ? getStr(R.string.EMPTY) : pCab.getHorimetro());
        contentValues.put(ABASTECIM_COL_OBS, pCab.getObservacao() == null ? getStr(R.string.EMPTY) : pCab.getObservacao());
        contentValues.put(ABASTECIM_COL_QUILOM, pCab.getQuilometragem() == null ? getStr(R.string.EMPTY) : pCab.getQuilometragem());
        contentValues.put(ABASTECIM_COL_TIPO, pAbs.getCombustivelLubrificante().getTipo());
        contentValues.put(ABASTECIM_COL_OBS_JUST, pCab.getObservacaoJustificativa() == null ? getStr(R.string.EMPTY) : pCab.getObservacaoJustificativa());
        contentValues.put(ABASTECIM_COL_RAE, pRae.getId());
        contentValues.put(ABASTECIM_COL_CC_OBRA, pCab.getIdObra());
        contentValues.put(ABASTECIM_COL_FRENTE_OBRA, pCab.getAtividade().getFrenteObra().getId());
        contentValues.put(ABASTECIM_COL_ATIVIDADE, pCab.getAtividade().getIdAtividade());
        contentValues.put(ABASTECIM_COL_COD_ABAST, pCab.getAbastecedor().getId());
        contentValues.put(ABASTECIM_COL_ID_PES_ABAST, pCab.getAbastecedor().getIdUsuarioPessoal());
        contentValues.put(ABASTECIM_COL_COD_OPERADOR, pCab.getOperador() != null ? pCab.getOperador().getId() : null);
        contentValues.put(ABASTECIM_COL_ID_PES_OPER, pCab.getOperador() != null ? pCab.getOperador().getIdUsuarioPessoal() : null);
        contentValues.put(ABASTECIM_COL_JUSTIFICATIVA, pCab.getJustificativa() != null ? pCab.getJustificativa().getId() : null);
        contentValues.put(ABASTECIM_COL_EQUIPAMENTO, pCab.getEquipamento().getId());
        contentValues.put(ABASTECIM_COL_COMBUSTIVE, pAbs.getCombustivelLubrificante().getId());
        contentValues.put(ABASTECIM_COL_QTE, pAbs.getQtd());

        return contentValues;
    }

    @Override
    protected String[] getColunas() {
        return new String[]{
                ABASTECIM_COL_DATA,
                ABASTECIM_COL_PREF,
                ALIAS_DESCRICAO,
                ALIAS_DESCRICAO2,
                ABASTECIM_COL_NOME,
                ALIAS_DESCRICAO3,
                ALIAS_DESCRICAO4,
                ABASTECIM_COL_HORA_INI,
                ABASTECIM_COL_HORA_FIM,
                ABASTECIM_COL_QUILOM,
                ABASTECIM_COL_HORIMETRO,
                ABASTECIM_COL_QTE,
                ALIAS_DESCRICAO5,
                ABASTECIM_COL_OBS,
                ALIAS_DESCRICAO6
        };
    }
}
