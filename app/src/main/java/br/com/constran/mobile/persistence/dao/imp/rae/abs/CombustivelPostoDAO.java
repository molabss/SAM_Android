package br.com.constran.mobile.persistence.dao.imp.rae.abs;

import android.content.ContentValues;
import android.content.Context;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.rae.abs.CombustivelPostoVO;

public class CombustivelPostoDAO extends AbstractDAO {

    private static final String COMBUSTIVEL_POSTO_COL_COMBUSTIVEL = "combustivel";
    private static final String COMBUSTIVEL_POSTO_COL_POSTO = "posto";

    private static CombustivelPostoDAO instance;

    private CombustivelPostoDAO(Context context) {
        super(context, TBL_COMBUSTIVEL_POSTO);
    }

    public static CombustivelPostoDAO getInstance(Context context) {
        if (instance == null) {
            instance = new CombustivelPostoDAO(context);
        }

        return instance;
    }

    public void save(CombustivelPostoVO pVO) {
        insert(getContentValues(pVO));
    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        CombustivelPostoVO pVO = (CombustivelPostoVO) abstractVO;

        ContentValues contentValues = new ContentValues();
        contentValues.put(COMBUSTIVEL_POSTO_COL_COMBUSTIVEL, pVO.getCombustivel().getId());
        contentValues.put(COMBUSTIVEL_POSTO_COL_POSTO, pVO.getPosto().getId());

        return contentValues;
    }
}
