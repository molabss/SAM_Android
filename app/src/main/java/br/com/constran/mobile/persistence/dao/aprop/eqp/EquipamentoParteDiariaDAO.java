package br.com.constran.mobile.persistence.dao.aprop.eqp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.aprop.eqp.EquipamentoParteDiariaVO;
import br.com.constran.mobile.view.util.Util;

public class EquipamentoParteDiariaDAO extends AbstractDAO {

    private static final String EQUIP_PARTE_DIA_COL_ID_EQUIP_PARTE_DIA = "idEquipamentosParteDiaria";
    private static final String EQUIP_PARTE_DIA_COL_OPERADOR1 = "operador1";
    private static final String EQUIP_PARTE_DIA_COL_OPERADOR2 = "operador2";
    private static final String EQUIP_PARTE_DIA_COL_HOR_INI = "horimetroInicial";
    private static final String EQUIP_PARTE_DIA_COL_HOR_FIM = "horimetroFinal";
    private static final String EQUIP_PARTE_DIA_COL_PRODUCAO = "producao";
    private static final String EQUIP_PARTE_DIA_DATA_HORA = "dataHora";
    private static final String EQUIP_PARTE_DIA_COL_OBS = "observacoes";
    private static final String EQUIP_PARTE_DIA_COL_EQUIP = "equipamento";
    private static final String EQUIP_PARTE_DIA_COL_DATA_HORA = "dataHora";
    private static final String EQUIP_PARTE_DIA_COL_APROPRIACAO = "apropriacao";

    private static EquipamentoParteDiariaDAO instance;

    private EquipamentoParteDiariaDAO(Context context) {
        super(context, TBL_EQUIPAMENTO_PARTE_DIARIA);
    }


    public static EquipamentoParteDiariaDAO getInstance(Context context) {
        if (instance == null) {
            instance = new EquipamentoParteDiariaDAO(context);
        }

        return instance;
    }

    public String[] getValues(Integer idEquipamento) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{
                "pd.[idEquipamentosParteDiaria]",
                "e.[prefixo] || ' - ' || e.[descricao] " + ALIAS_EQUIPAMENTO,
                "ae.[operador1]",
                "ae.[operador2]",
                "ae.[horimetroInicial]",
                "ae.[horimetroFinal]",
                "ae.[producao]",
                "pd.[dataHora]",
                "ae.[observacoes]"
        };

        tableJoin = " 	[equipamentosParteDiaria] pd ";
        tableJoin += "  join [equipamentos] e on pd.[equipamento]  =  e.[idEquipamento]  ";
        tableJoin += "  left join [apropriacoes] a on a.[idApropriacao]  =  pd.[apropriacao] ";
        tableJoin += "  left join [apropriacoesEquipamento] ae on a.[idApropriacao]  =  ae.[apropriacao] ";
        tableJoin += "  and pd.[equipamento]  =  ae.[equipamento]";
        tableJoin += "  and pd.[dataHora]  =  ae.[dataHora]";

        conditions = " pd.[equipamento] = " + idEquipamento;
        conditions += " and substr(pd.[dataHora],0, 11) = '" + Util.getToday() + "'";

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

    public void save(EquipamentoParteDiariaVO pVO) {

        StringBuilder builder = new StringBuilder("");

        if (pVO.getId() == null) {
            insert(getContentValues(pVO));
        } else {

            builder.append(" update [equipamentosParteDiaria] set ");

            builder.append(" [apropriacao] = ");
            if (pVO.getIdApropriacao() == null) {
                builder.append(" null ");
            } else {
                builder.append("");
                builder.append(pVO.getIdApropriacao());
                builder.append("");
            }

            builder.append(" where [idEquipamentosParteDiaria] = ");
            builder.append(pVO.getId());
            builder.append(";");

            execute(builder);
        }

    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        EquipamentoParteDiariaVO pVO = (EquipamentoParteDiariaVO) abstractVO;

        ContentValues contentValues = new ContentValues();
        contentValues.put(EQUIP_PARTE_DIA_COL_EQUIP, pVO.getEquipamento().getId());
        contentValues.put(EQUIP_PARTE_DIA_COL_DATA_HORA, pVO.getDataHora());
        contentValues.put(EQUIP_PARTE_DIA_COL_APROPRIACAO, pVO.getIdApropriacao());

        return contentValues;
    }

    @Override
    protected String[] getColunas() {
        return new String[]{
                EQUIP_PARTE_DIA_COL_ID_EQUIP_PARTE_DIA,
                ALIAS_EQUIPAMENTO,
                EQUIP_PARTE_DIA_COL_OPERADOR1,
                EQUIP_PARTE_DIA_COL_OPERADOR2,
                EQUIP_PARTE_DIA_COL_HOR_INI,
                EQUIP_PARTE_DIA_COL_HOR_FIM,
                EQUIP_PARTE_DIA_COL_PRODUCAO,
                EQUIP_PARTE_DIA_DATA_HORA,
                EQUIP_PARTE_DIA_COL_OBS
        };
    }

}
