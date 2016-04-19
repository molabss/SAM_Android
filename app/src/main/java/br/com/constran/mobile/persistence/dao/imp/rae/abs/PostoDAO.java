package br.com.constran.mobile.persistence.dao.imp.rae.abs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.rae.abs.PostoVO;

public class PostoDAO extends AbstractDAO {

    private static final String POSTO_COL_ID_POSTO = "idPosto";
    private static final String POSTO_COL_EQUIP = "equipamento";
    private static final String POSTO_COL_TIPO = "tipo";
    private static final String POSTO_COL_DESC = "descricao";

    private static PostoDAO instance;

    private PostoDAO(Context context) {
        super(context, TBL_POSTO);
    }

    public static PostoDAO getInstance(Context context) {
        if (instance == null) {
            instance = new PostoDAO(context);
        }

        return instance;
    }

    public PostoVO[] getArrayPostoVO() {

        Query query = new Query(true);

        query.setColumns(new String[]{POSTO_COL_ID_POSTO, "' ' || p.[descricao] " + ALIAS_DESCRICAO,
                "p.[equipamento]", "p.[tipo]"});

        query.setTableJoin("[postos] p left join [equipamentos] e on p.[equipamento] = e.[idEquipamento]");

        query.setOrderBy(" p.[descricao] ASC");

        Cursor cursor = getCursor(query);

        PostoVO[] dados = new PostoVO[cursor.getCount()];

        int i = 0;

        while (cursor.moveToNext()) {
            dados[i++] = new PostoVO(
                    cursor.getInt(cursor.getColumnIndex(POSTO_COL_ID_POSTO)),
                    cursor.getString(cursor.getColumnIndex(ALIAS_DESCRICAO)),
                    cursor.getInt(cursor.getColumnIndex(POSTO_COL_EQUIP)),
                    cursor.getString(cursor.getColumnIndex(POSTO_COL_TIPO)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public PostoVO[] getArrayPostoVO(Integer idPosto) {

        Query query = new Query(true);

        query.setColumns(new String[]{"p.[idPosto]", "' ' || p.[descricao] " + ALIAS_DESCRICAO,
                "p.[equipamento]", "p.[tipo]"});

        query.setTableJoin("[postos] p left join [equipamentos] e on p.[equipamento] = e.[idEquipamento]");

        query.setConditions(" p.idPosto <> " + idPosto);

        query.setOrderBy(" p.[descricao] ASC");

        Cursor cursor = getCursor(query);

        PostoVO[] dados = new PostoVO[cursor.getCount()];

        int i = 0;

        while (cursor.moveToNext()) {
            dados[i++] = new PostoVO(
                    cursor.getInt(cursor.getColumnIndex(POSTO_COL_ID_POSTO)),
                    cursor.getString(cursor.getColumnIndex(ALIAS_DESCRICAO)),
                    cursor.getInt(cursor.getColumnIndex(POSTO_COL_EQUIP)),
                    cursor.getString(cursor.getColumnIndex(POSTO_COL_TIPO)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public void save(PostoVO pVO) {
        insert(getContentValues(pVO));
    }

    public PostoVO getById(Integer idPosto) {

        Query query = new Query(true);

        query.setColumns(new String[]{POSTO_COL_ID_POSTO, POSTO_COL_DESC});

        query.setTableJoin(TBL_POSTO);

        query.setConditions("[idPosto] = ? ");

        query.setConditionsArgs(new String[]{idPosto.toString()});

        Cursor cursor = getCursor(query);

        PostoVO posto = null;

        if (cursor.moveToNext()) {
            posto = new PostoVO(
                    cursor.getInt(cursor.getColumnIndex(POSTO_COL_ID_POSTO)),
                    cursor.getString(cursor.getColumnIndex(POSTO_COL_DESC)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return posto;
    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        PostoVO pVO = (PostoVO) abstractVO;

        ContentValues contentValues = new ContentValues();
        contentValues.put(POSTO_COL_ID_POSTO, pVO.getId());
        contentValues.put(POSTO_COL_EQUIP, pVO.getIdEquipamento());
        contentValues.put(POSTO_COL_DESC, pVO.getDescricao());
        contentValues.put(POSTO_COL_TIPO, pVO.getTipo());

        return contentValues;
    }

}
