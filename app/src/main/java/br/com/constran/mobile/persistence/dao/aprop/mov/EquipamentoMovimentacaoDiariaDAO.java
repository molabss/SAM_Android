package br.com.constran.mobile.persistence.dao.aprop.mov;

import android.content.ContentValues;
import android.content.Context;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.aprop.mov.EquipamentoMovimentacaoDiariaVO;

public class EquipamentoMovimentacaoDiariaDAO extends AbstractDAO {

    private static final String EQUIP_MOV_DIA_COL_EQUIP = "equipamento";
    private static final String EQUIP_MOV_DIA_COL_DATA_HORA = "dataHora";

    private static EquipamentoMovimentacaoDiariaDAO instance;

    private EquipamentoMovimentacaoDiariaDAO(Context context) {
        super(context, TBL_EQUIPAMENTO_MOVIM_DIA);
    }

    public static EquipamentoMovimentacaoDiariaDAO getInstance(Context context) {
        if (instance == null) {
            instance = new EquipamentoMovimentacaoDiariaDAO(context);
        }

        return instance;
    }

    public void save(EquipamentoMovimentacaoDiariaVO pVO) {
        insert(getContentValues(pVO));
    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        EquipamentoMovimentacaoDiariaVO pVO = (EquipamentoMovimentacaoDiariaVO) abstractVO;

        ContentValues contentValues = new ContentValues();
        contentValues.put(EQUIP_MOV_DIA_COL_EQUIP, pVO.getIdEquipamento());
        contentValues.put(EQUIP_MOV_DIA_COL_DATA_HORA, pVO.getDataHora());

        return contentValues;
    }
}
