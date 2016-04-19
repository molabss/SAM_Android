package br.com.constran.mobile.persistence.dao.imp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.imp.PreventivaEquipamentoVO;

public class PreventivaEquipamentoDAO extends AbstractDAO {

    private static final String PREVENT_EQUIP_COL_DATA = "data";
    private static final String PREVENT_EQUIP_COL_HORIMETRO = "horimetro";
    private static final String PREVENT_EQUIP_COL_EQUIP = "equipamento";

    private static PreventivaEquipamentoDAO instance;

    private PreventivaEquipamentoDAO(Context context) {
        super(context, TBL_PREVENTIVA_EQUIP);
    }


    public static PreventivaEquipamentoDAO getInstance(Context context) {
        if (instance == null) {
            instance = new PreventivaEquipamentoDAO(context);
        }

        return instance;
    }

    public Cursor getCursor(Integer idEquipamento) {

        Query query = new Query(true);

        query.setColumns(new String[]{"[data]", "[horimetro]"});

        query.setTableJoin(TBL_PREVENTIVA_EQUIP);

        query.setConditions(" [equipamento] = ?");

        query.setConditionsArgs(new String[]{idEquipamento.toString()});

        return getCursor(query);
    }

    public void save(PreventivaEquipamentoVO pPrev) {
        insert(getContentValues(pPrev));
    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        PreventivaEquipamentoVO pPrev = (PreventivaEquipamentoVO) abstractVO;
        ContentValues ContentValues = new ContentValues();
        ContentValues.put(PREVENT_EQUIP_COL_DATA, pPrev.getData());
        ContentValues.put(PREVENT_EQUIP_COL_HORIMETRO, pPrev.getHorimetro());
        ContentValues.put(PREVENT_EQUIP_COL_EQUIP, pPrev.getIdEquipamento());

        return ContentValues;
    }
}
