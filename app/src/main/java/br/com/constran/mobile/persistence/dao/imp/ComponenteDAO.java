package br.com.constran.mobile.persistence.dao.imp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.imp.ComponenteVO;

public class ComponenteDAO extends AbstractDAO {

    private static final String COMPONENTE_COL_ID_COMP = "idComponente";
    private static final String COMPONENTE_COL_DESC = "descricao";
    private static final String COMPONENTE_COL_ID_CATEG = "idCategoria";

    private static ComponenteDAO instance;

    private ComponenteDAO(Context context) {
        super(context, TBL_COMPONENTE);
    }

    public static ComponenteDAO getInstance(Context context) {
        if (instance == null) {
            instance = new ComponenteDAO(context);
        }

        return instance;
    }

    public ComponenteVO[] getArrayComponenteVO(Integer idEquipamento) {

        Query query = new Query(true);

        query.setColumns(new String[]{"c.[idComponente]", "' ' || c.[descricao] " + ALIAS_DESCRICAO, "c.[idCategoria]"});

        query.setTableJoin(TBL_COMPONENTE + " c join [equipamentos] e on e.[idCategoria] = c.[idCategoria]");

        query.setOrderBy("c.[descricao] ASC");

        query.setConditions("e.[idEquipamento] = " + idEquipamento);

        Cursor cursor = getCursor(query);

        ComponenteVO[] dados = new ComponenteVO[cursor.getCount()];

        int i = 0;

        while (cursor.moveToNext()) {
            dados[i++] = new ComponenteVO(cursor.getInt(cursor.getColumnIndex(COMPONENTE_COL_ID_COMP)),
                    cursor.getString(cursor.getColumnIndex(ALIAS_DESCRICAO)),
                    cursor.getInt(cursor.getColumnIndex(COMPONENTE_COL_ID_CATEG)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public void save(ComponenteVO pVO) {
        insert(getContentValues(pVO));
    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        ComponenteVO pVO = (ComponenteVO) abstractVO;
        ContentValues contentValues = new ContentValues();
        contentValues.put(COMPONENTE_COL_ID_COMP, pVO.getId());
        contentValues.put(COMPONENTE_COL_DESC, pVO.getDescricao());
        contentValues.put(COMPONENTE_COL_ID_CATEG, pVO.getCategoria().getId());

        return contentValues;
    }

}
