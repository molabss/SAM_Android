package br.com.constran.mobile.persistence.dao.aprop.eqp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.aprop.eqp.ApropriacaoEquipamentoVO;
import br.com.constran.mobile.view.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ApropriacaoEquipamentoDAO extends AbstractDAO {

    private static final String APROPRIACAO_EQUIP_COL_ID_APROPRIACAO = "idApropriacao";
    private static final String APROPRIACAO_EQUIP_COL_EQUIPAMENTO = "equipamento";
    private static final String APROPRIACAO_EQUIP_COL_DATA_HORA = "dataHora";
    private static final String APROPRIACAO_EQUIP_COL_APROPRIACAO = "apropriacao";
    private static final String APROPRIACAO_EQUIP_COL_OPERADOR1 = "operador1";
    private static final String APROPRIACAO_EQUIP_COL_OPERADOR2 = "operador2";
    private static final String APROPRIACAO_EQUIP_COL_HORIM_INI = "horimetroInicial";
    private static final String APROPRIACAO_EQUIP_COL_HORIM_FIM = "horimetroFinal";
    private static final String APROPRIACAO_EQUIP_COL_PRODUCAO = "producao";
    private static final String APROPRIACAO_EQUIP_COL_OBS = "observacoes";

    private static ApropriacaoEquipamentoDAO instance;

    private ApropriacaoEquipamentoDAO(Context context) {
        super(context, TBL_APROPRIACAO_EQUIPAMENTO);
    }

    public static ApropriacaoEquipamentoDAO getInstance(Context context) {
        if (instance == null) {
            instance = new ApropriacaoEquipamentoDAO(context);
        }

        return instance;
    }

    public void save(ApropriacaoEquipamentoVO pVO) {

        StringBuilder builder = new StringBuilder("");

        if (pVO.getStrId() == null) {
            insert(getContentValues(pVO));
        } else {

            builder.append(" update [apropriacoesEquipamento] set ");

            builder.append(" [horimetroInicial] = ");
            if (pVO.getHorimetroIni() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getHorimetroIni());
                builder.append("'");
            }

            builder.append(" , [horimetroFinal] = ");
            if (pVO.getHorimetroFim() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getHorimetroFim());
                builder.append("'");
            }

            builder.append(" , [operador1] = ");
            if (pVO.getHorimetroIni() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getOperador1());
                builder.append("'");
            }

            builder.append(" , [operador2] = ");
            if (pVO.getHorimetroFim() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getOperador2());
                builder.append("'");
            }

            builder.append(", [producao] = ");
            if (pVO.getHorimetroFim() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getProducao());
                builder.append("'");
            }

            builder.append(", [observacoes] = ");
            if (pVO.getObservacoes() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getObservacoes());
                builder.append("'");
            }

            builder.append(" where [apropriacao] = ");
            builder.append(pVO.getIdApropriacao());
            builder.append(" and [equipamento]  = ");
            builder.append(pVO.getEquipamento().getId());
            builder.append(" and [dataHora]  = ");
            builder.append("'");
            builder.append(pVO.getDataHora());
            builder.append("';");

            execute(builder);
        }

    }

    public String[] getPk(Integer idEquipamento) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{"a.[idApropriacao], pd.[equipamento], pd.[dataHora]"};

        tableJoin = " [apropriacoesEquipamento] pd ";
        tableJoin += " join [apropriacoes] a on pd.[apropriacao] =  a.[idApropriacao]  ";

        conditions = "  substr(a.[dataHoraApontamento],0, 11) =  '" + Util.getToday() + "'";
        conditions += " and a.[dataHoraApontamento] =  pd.[dataHora]";
        conditions += " and pd.[equipamento] = " + idEquipamento;

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        List<String[]> listIds = new ArrayList<String[]>();

        String[] ids = null;

        if (cursor.moveToNext()) {
            ids = new String[]{cursor.getString(cursor.getColumnIndex(APROPRIACAO_EQUIP_COL_ID_APROPRIACAO)),
                    cursor.getString(cursor.getColumnIndex(APROPRIACAO_EQUIP_COL_EQUIPAMENTO)),
                    cursor.getString(cursor.getColumnIndex(APROPRIACAO_EQUIP_COL_DATA_HORA))};
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return ids;
    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        ApropriacaoEquipamentoVO pVO = (ApropriacaoEquipamentoVO) abstractVO;

        ContentValues contentValues = new ContentValues();
        contentValues.put(APROPRIACAO_EQUIP_COL_EQUIPAMENTO, pVO.getEquipamento().getId());
        contentValues.put(APROPRIACAO_EQUIP_COL_DATA_HORA, pVO.getDataHora());
        contentValues.put(APROPRIACAO_EQUIP_COL_APROPRIACAO, pVO.getIdApropriacao());
        contentValues.put(APROPRIACAO_EQUIP_COL_OPERADOR1, pVO.getOperador1());
        contentValues.put(APROPRIACAO_EQUIP_COL_OPERADOR2, pVO.getOperador2());
        contentValues.put(APROPRIACAO_EQUIP_COL_HORIM_INI, pVO.getHorimetroIni());
        contentValues.put(APROPRIACAO_EQUIP_COL_HORIM_FIM, pVO.getHorimetroFim());
        contentValues.put(APROPRIACAO_EQUIP_COL_PRODUCAO, pVO.getProducao());
        contentValues.put(APROPRIACAO_EQUIP_COL_OBS, pVO.getObservacoes());

        return contentValues;
    }
}
