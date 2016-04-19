package br.com.constran.mobile.persistence.dao.imp.rae.abs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.R;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;
import br.com.constran.mobile.persistence.vo.imp.UsuarioVO;
import br.com.constran.mobile.persistence.vo.rae.abs.AbastecimentoTempVO;
import br.com.constran.mobile.view.util.Util;

import java.util.ArrayList;
import java.util.List;

public class AbastecimentoTempDAO extends AbstractDAO {

    private static final String ABASTEC_TMP_COL_EQUIP = "equipamento";
    private static final String ABASTEC_TMP_COL_PREF = "prefixo";
    private static final String ABASTEC_TMP_COL_DATA_HORA = "dataHora";
    private static final String ABASTEC_TMP_COL_HORIMETRO = "horimetro";
    private static final String ABASTEC_TMP_COL_QUILOMET = "quilometragem";
    private static final String ABASTEC_TMP_COL_NOME = "nome";
    private static final String ABASTEC_TMP_COL_ID_USUARIO_PES = "idUsuarioPessoal";
    private static final String ABASTEC_TMP_COL_SENHA = "senha";
    private static final String ABASTEC_TMP_COL_TIPO = "tipo";
    private static final String ABASTEC_TMP_COL_COD_OPERADOR = "codOperador";

    private final String separador = "|| '-' ||";
    private final String espaco = "|| ' ' ||";

    private static AbastecimentoTempDAO instance;

    private AbastecimentoTempDAO(Context context) {
        super(context, TBL_ABASTECIM_TEMP);
    }

    public static AbastecimentoTempDAO getInstance(Context context) {
        if (instance == null) {
            instance = new AbastecimentoTempDAO(context);
        }

        return instance;
    }

    public Cursor getCursor(Double duracao) {
        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        String[] values = getArrayDateTime(duracao);

        columns = new String[]{"a.[equipamento]", "a.[dataHora]", "e.[prefixo]",
                "ifnull(u.[nome], '" + getStr(R.string.NAO_INFORMADO) + "')", "ifnull(a.[horimetro], a.[quilometragem])"};

        tableJoin = "[abastecimentosTemp] a  ";
        tableJoin += "join[equipamentos] e on e.[idEquipamento] = a.[equipamento] ";
        tableJoin += "left join[usuarios] u on u.[codUsuario] = a.[codOperador] ";

        //se duracao for maior que zero, a validação é feita
        if (duracao > 0)
            conditions = values[0] + " >= " + values[1];

        orderBy = "e.[prefixo] asc, " + values[0] + " desc ";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        return getCursor(query);
    }

    public AbastecimentoTempVO findAbastecimentoTemp(Double duracao, Integer idEquipamento) {
        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        String[] values = getArrayDateTime(duracao);

        columns = new String[]{"a.[equipamento]", "e.[prefixo]", "e.[tipo]", "a.[dataHora]", "a.[horimetro]", "a.[quilometragem]",
                "ifnull(a.[codOperador], 0) " + ALIAS_COD_OPERADOR, "u.[nome]", "u.[idUsuarioPessoal]", "u.[senha]"};

        tableJoin = "[abastecimentosTemp] a  ";
        tableJoin += "join[equipamentos] e on e.[idEquipamento] = a.[equipamento] ";
        tableJoin += "left join[usuarios] u on u.[codUsuario] = a.[codOperador] ";

        //se duracao for maior que zero, a validação é feita
        if (duracao > 0)
            conditions = values[0] + " >= " + values[1] + " and ";

        conditions += "e.idEquipamento = " + idEquipamento;

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        AbastecimentoTempVO abastecimentoTempVO = null;

        if (cursor.moveToNext()) {
            EquipamentoVO equipamento = new EquipamentoVO(cursor.getInt(cursor.getColumnIndex(ABASTEC_TMP_COL_EQUIP)));
            equipamento.setPrefixo(cursor.getString(cursor.getColumnIndex(ABASTEC_TMP_COL_PREF)));
            equipamento.setTipo(cursor.getString(cursor.getColumnIndex(ABASTEC_TMP_COL_TIPO)));

            abastecimentoTempVO = new AbastecimentoTempVO();
            abastecimentoTempVO.setEquipamento(equipamento);
            abastecimentoTempVO.setDataHora(cursor.getString(cursor.getColumnIndex(ABASTEC_TMP_COL_DATA_HORA)));
            abastecimentoTempVO.setHorimetro(cursor.getString(cursor.getColumnIndex(ABASTEC_TMP_COL_HORIMETRO)));
            abastecimentoTempVO.setQuilometragem(cursor.getString(cursor.getColumnIndex(ABASTEC_TMP_COL_QUILOMET)));

            if (cursor.getInt(cursor.getColumnIndex(ALIAS_COD_OPERADOR)) != 0) {
                abastecimentoTempVO.setOperador(new UsuarioVO(cursor.getInt(cursor.getColumnIndex(ALIAS_COD_OPERADOR)),
                        cursor.getString(cursor.getColumnIndex(ABASTEC_TMP_COL_NOME))));
                abastecimentoTempVO.getOperador().setIdUsuarioPessoal(cursor.getInt(cursor.getColumnIndex(ABASTEC_TMP_COL_ID_USUARIO_PES)));
                abastecimentoTempVO.getOperador().setSenha(cursor.getString(cursor.getColumnIndex(ABASTEC_TMP_COL_SENHA)));
            }

        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return abastecimentoTempVO;
    }

    public AbastecimentoTempVO getPreviaDados(Double duracao, Integer idEquipamento) {
        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        String[] values = getArrayDateTime(duracao);

        columns = new String[]{"a.[equipamento]", "e.[prefixo]", "a.[dataHora]", "a.[horimetro]", "a.[quilometragem]",
                "ifnull(a.[codOperador], 0) " + ALIAS_COD_OPERADOR, "u.[nome]", "u.[idUsuarioPessoal]", "u.[senha]"};

        tableJoin = "[abastecimentosTemp] a  ";
        tableJoin += "join[equipamentos] e on e.[idEquipamento] = a.[equipamento] ";
        tableJoin += "left join[usuarios] u on u.[codUsuario] = a.[codOperador] ";

        conditions = "a.[equipamento] = " + idEquipamento;

        if (duracao > 0)
            conditions += " and " + values[0] + " >= " + values[1];

        orderBy = values[0] + " desc ";

        Query query = new Query("1");

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        AbastecimentoTempVO dados = new AbastecimentoTempVO();

        if (cursor.moveToNext()) {

            dados.setDataHora(cursor.getString(cursor.getColumnIndex(ABASTEC_TMP_COL_DATA_HORA)));
            dados.setHorimetro(cursor.getString(cursor.getColumnIndex(ABASTEC_TMP_COL_HORIMETRO)));
            dados.setQuilometragem(cursor.getString(cursor.getColumnIndex(ABASTEC_TMP_COL_QUILOMET)));
            dados.setEquipamento(new EquipamentoVO(cursor.getInt(cursor.getColumnIndex(ABASTEC_TMP_COL_EQUIP))));
            dados.getEquipamento().setPrefixo(cursor.getString(cursor.getColumnIndex(ABASTEC_TMP_COL_PREF)));

            if (cursor.getInt(cursor.getColumnIndex(ALIAS_COD_OPERADOR)) != 0) {
                dados.setOperador(new UsuarioVO(cursor.getInt(cursor.getColumnIndex(ALIAS_COD_OPERADOR)),
                        cursor.getString(cursor.getColumnIndex(ABASTEC_TMP_COL_NOME))));
                dados.getOperador().setIdUsuarioPessoal(cursor.getInt(cursor.getColumnIndex(ABASTEC_TMP_COL_ID_USUARIO_PES)));
                dados.getOperador().setSenha(cursor.getString(cursor.getColumnIndex(ABASTEC_TMP_COL_SENHA)));
            }
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public AbastecimentoTempVO getById(Integer idEquipamento, String dataHora) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;

        columns = new String[]{"a.[equipamento]", "e.[prefixo]", "a.[dataHora]", "a.[horimetro]", "a.[quilometragem]",
                "ifnull(a.[codOperador], 0) " + ALIAS_COD_OPERADOR, "u.[nome]", "u.[idUsuarioPessoal]", "e.[tipo]"};

        tableJoin = "[abastecimentosTemp] a  ";
        tableJoin += "join[equipamentos] e on e.[idEquipamento] = a.[equipamento] ";
        tableJoin += "left join[usuarios] u on u.[codUsuario] = a.[codOperador] ";

        conditions = " a.[equipamento]  = " + idEquipamento;
        conditions += " and a.[dataHora] = '" + dataHora + "'";

        Query query = new Query();

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);

        Cursor cursor = getCursor(query);

        AbastecimentoTempVO dados = new AbastecimentoTempVO();

        if (cursor.moveToNext()) {

            dados.setEquipamento(new EquipamentoVO(cursor.getInt(cursor.getColumnIndex(ABASTEC_TMP_COL_EQUIP))));
            dados.getEquipamento().setPrefixo(cursor.getString(cursor.getColumnIndex(ABASTEC_TMP_COL_PREF)));
            dados.getEquipamento().setTipo(cursor.getString(cursor.getColumnIndex(ABASTEC_TMP_COL_TIPO)));
            dados.setDataHora(cursor.getString(cursor.getColumnIndex(ABASTEC_TMP_COL_DATA_HORA)));
            dados.setHorimetro(cursor.getString(cursor.getColumnIndex(ABASTEC_TMP_COL_HORIMETRO)));
            dados.setQuilometragem(cursor.getString(cursor.getColumnIndex(ABASTEC_TMP_COL_QUILOMET)));

            if (cursor.getInt(cursor.getColumnIndex(ALIAS_COD_OPERADOR)) != 0) {
                dados.setOperador(new UsuarioVO(cursor.getInt(cursor.getColumnIndex(ALIAS_COD_OPERADOR)),
                        cursor.getString(cursor.getColumnIndex(ABASTEC_TMP_COL_NOME))));
                dados.getOperador().setIdUsuarioPessoal(cursor.getInt(cursor.getColumnIndex(ABASTEC_TMP_COL_ID_USUARIO_PES)));
            }
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public void save(AbastecimentoTempVO pAbs) {

        if (pAbs.getStrId() == null) {
            insert(getContentValues(pAbs));
        } else {
            String whereClause = ABASTEC_TMP_COL_EQUIP + " = ? and " + ABASTEC_TMP_COL_DATA_HORA + " = ?";
            String[] whereArgs = new String[]{pAbs.getEquipamento().getId().toString(), pAbs.getDataHora()};
            update(getContentValues(pAbs), whereClause, whereArgs);
        }

    }

    private String[] getArrayDateTime(Double duracao) {

        String dataHoraColuna = "datetime(substr(substr(a.[dataHora],0,11),7)";
        dataHoraColuna += separador;
        dataHoraColuna += "substr(substr(a.[dataHora],0,11),4,2)";
        dataHoraColuna += separador;
        dataHoraColuna += "substr(substr(a.[dataHora],0,11),1,2)";
        dataHoraColuna += espaco;
        dataHoraColuna += "substr(a.[dataHora], 11), '+" + duracao + " hours')";

        String dataHoraAtual = "datetime(substr(substr('" + Util.getNow() + "',0,11),7)";
        dataHoraAtual += separador;
        dataHoraAtual += "substr(substr('" + Util.getNow() + "',0,11),4,2)";
        dataHoraAtual += separador;
        dataHoraAtual += "substr(substr('" + Util.getNow() + "',0,11),1,2)";
        dataHoraAtual += espaco;
        dataHoraAtual += "substr('" + Util.getNow() + "', 11))";

        return new String[]{dataHoraColuna, dataHoraAtual};
    }

    public List<Integer> findIdsEquipamentos(Double duracao) {
        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        String[] values = getArrayDateTime(duracao);

        columns = new String[]{"a.[equipamento]"};

        tableJoin = "[abastecimentosTemp] a  ";
        tableJoin += "join[equipamentos] e on e.[idEquipamento] = a.[equipamento] ";
        tableJoin += "left join[usuarios] u on u.[codUsuario] = a.[codOperador] ";

        //se duracao for maior que zero, a validação é feita
        if (duracao > 0)
            conditions = values[0] + " >= " + values[1];

        orderBy = "e.[prefixo] asc, " + values[0] + " desc ";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);


        Cursor cursor = getCursor(query);
        List<Integer> idsEquipamentos = new ArrayList<Integer>();

        while (cursor.moveToNext()) {
            idsEquipamentos.add(cursor.getInt(cursor.getColumnIndex(ABASTEC_TMP_COL_EQUIP)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return idsEquipamentos;
    }


    public void delete(final Integer idEquipamento) {
        List<Integer> ids = new ArrayList<Integer>();
        ids.add(idEquipamento);

        super.delete(ids, ABASTEC_TMP_COL_EQUIP);
    }


    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        AbastecimentoTempVO pAbs = (AbastecimentoTempVO) abstractVO;

        ContentValues contentValues = new ContentValues();
        contentValues.put(ABASTEC_TMP_COL_DATA_HORA, pAbs.getDataHora());
        contentValues.put(ABASTEC_TMP_COL_EQUIP, pAbs.getEquipamento().getId());
        contentValues.put(ABASTEC_TMP_COL_HORIMETRO, pAbs.getHorimetro() != null && pAbs.getHorimetro().isEmpty() ? null : pAbs.getHorimetro());
        contentValues.put(ABASTEC_TMP_COL_QUILOMET, pAbs.getQuilometragem() != null && pAbs.getQuilometragem().isEmpty() ? null : pAbs.getQuilometragem());
        contentValues.put(ABASTEC_TMP_COL_COD_OPERADOR, pAbs.getOperador() != null ? pAbs.getOperador().getId() : null);

        return contentValues;
    }
}
