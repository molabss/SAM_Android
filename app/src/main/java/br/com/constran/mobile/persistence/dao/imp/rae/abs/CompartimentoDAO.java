package br.com.constran.mobile.persistence.dao.imp.rae.abs;

import android.content.ContentValues;
import android.content.Context;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.rae.abs.CompartimentoVO;

public class CompartimentoDAO extends AbstractDAO {

    private static final String COMPARTIM_COL_ID_COMPARTIM = "idCompartimento";
    private static final String COMPARTIM_COL_DESC = "descricao";
    private static final String COMPARTIM_COL_TIPO = "tipo";
    private static final String COMPARTIM_COL_ID_CATEGORIA = "idCategoria";

    private static CompartimentoDAO instance;

    private CompartimentoDAO(Context context) {
        super(context, TBL_COMPARTIMENTO);
    }

    public static CompartimentoDAO getInstance(Context context) {
        if (instance == null) {
            instance = new CompartimentoDAO(context);
        }

        return instance;
    }

    public void save(CompartimentoVO pVO) {
        insert(getContentValues(pVO));
    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        CompartimentoVO pVO = (CompartimentoVO) abstractVO;

        ContentValues contentValues = new ContentValues();
        contentValues.put(COMPARTIM_COL_ID_COMPARTIM, pVO.getId());
        contentValues.put(COMPARTIM_COL_DESC, pVO.getDescricao());
        contentValues.put(COMPARTIM_COL_TIPO, pVO.getTipo());
        contentValues.put(COMPARTIM_COL_ID_CATEGORIA, pVO.getCategoria().getId());

        return contentValues;
    }
}
