package br.com.constran.mobile.persistence.dao.imp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.R;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.imp.FrenteObraVO;

public class FrenteObraDAO extends AbstractDAO {

    private static final String FRENTE_OBRA_ID_FRENTE_OBRA = "idFrentesObra";
    private static final String FRENTE_OBRA_OBRA = "obra";
    private static final String FRENTE_OBRA_DESCRICAO = "descricao";

    private static FrenteObraDAO instance;

    private FrenteObraDAO(Context context) {
        super(context, TBL_FRENTE_OBRA);
    }

    public static FrenteObraDAO getInstance(Context context) {
        if (instance == null) {
            instance = new FrenteObraDAO(context);
        }

        return instance;
    }

    public FrenteObraVO[] getArrayFrenteObraVO(Integer ccObra) {

        String[] columns = new String[]{"f.[idFrentesObra]", "f.[obra]", "f.[descricao]"};
        String tableJoin = TBL_FRENTE_OBRA + " f";
        tableJoin += " join [frentesObraAtividade] a on a.[frentesObra] = f.[idFrentesObra]";
        String conditions = "obra = " + ccObra;
        String orderBy = "f.[descricao] asc";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setConditions(conditions);
        query.setTableJoin(tableJoin);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        FrenteObraVO[] dados = new FrenteObraVO[cursor.getCount() + 1];

        int i = 0;

        dados[i++] = new FrenteObraVO(getStr(R.string.SELECT));

        while (cursor.moveToNext()) {
            dados[i++] = new FrenteObraVO(cursor.getInt(cursor.getColumnIndex(FRENTE_OBRA_ID_FRENTE_OBRA)),
                    cursor.getInt(cursor.getColumnIndex(FRENTE_OBRA_OBRA)),
                    cursor.getString(cursor.getColumnIndex(FRENTE_OBRA_DESCRICAO)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }


    public void save(FrenteObraVO pVO) {
        insert(getContentValues(pVO));
    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        FrenteObraVO pVO = (FrenteObraVO) abstractVO;
        ContentValues contentValues = new ContentValues();
        contentValues.put(FRENTE_OBRA_ID_FRENTE_OBRA, pVO.getId());
        contentValues.put(FRENTE_OBRA_OBRA, pVO.getIdObra());
        contentValues.put(FRENTE_OBRA_DESCRICAO, pVO.getDescricao());

        return contentValues;
    }

    public String getName(Integer idFrenteObra) {
        Query query = new Query(true);

        query.setTableJoin(TBL_FRENTE_OBRA);

        query.setConditions(" [idFrentesObra] =  ? ");

        query.setConditionsArgs(new String[]{String.valueOf(idFrenteObra)});

        query.setColumns(new String[]{FRENTE_OBRA_DESCRICAO});

        Cursor cursor = getCursor(query);

        cursor.moveToNext();

        String value = cursor.getString(cursor.getColumnIndex(FRENTE_OBRA_DESCRICAO)).trim();

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return value;
    }
}
