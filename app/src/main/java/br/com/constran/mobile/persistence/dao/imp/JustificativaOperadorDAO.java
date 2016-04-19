package br.com.constran.mobile.persistence.dao.imp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.rae.abs.JustificativaOperadorVO;

public class JustificativaOperadorDAO extends AbstractDAO {

    private static final String JUSTIFICATIVA_OP_COL_ID_JUST_OP = "idJustificativaOperador";
    private static final String JUSTIFICATIVA_OP_COL_DESC = "descricao";

    private static JustificativaOperadorDAO instance;

    private JustificativaOperadorDAO(Context context) {
        super(context, TBL_JUSTIFICATIVA_OPERADOR);
    }

    public static JustificativaOperadorDAO getInstance(Context context) {
        if (instance == null) {
            instance = new JustificativaOperadorDAO(context);
        }

        return instance;
    }

    public JustificativaOperadorVO[] getArrayJustificativaOperadorVO() {

        Query query = new Query(true);

        query.setColumns(new String[]{JUSTIFICATIVA_OP_COL_ID_JUST_OP, "' ' || [descricao] " + ALIAS_DESCRICAO});

        query.setTableJoin(TBL_JUSTIFICATIVA_OPERADOR);

        query.setOrderBy("[descricao] ASC");

        Cursor cursor = getCursor(query);

        JustificativaOperadorVO[] dados = new JustificativaOperadorVO[cursor.getCount()];

        int i = 0;

        while (cursor.moveToNext()) {
            dados[i++] = new JustificativaOperadorVO(
                    cursor.getInt(cursor.getColumnIndex(JUSTIFICATIVA_OP_COL_ID_JUST_OP)),
                    cursor.getString(cursor.getColumnIndex(ALIAS_DESCRICAO)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public void save(JustificativaOperadorVO pJus) {
        insert(getContentValues(pJus));
    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        JustificativaOperadorVO pJus = (JustificativaOperadorVO) abstractVO;
        ContentValues contentValues = new ContentValues();
        contentValues.put(JUSTIFICATIVA_OP_COL_DESC, pJus.getDescricao());
        contentValues.put(JUSTIFICATIVA_OP_COL_ID_JUST_OP, pJus.getId());

        return contentValues;
    }
}
