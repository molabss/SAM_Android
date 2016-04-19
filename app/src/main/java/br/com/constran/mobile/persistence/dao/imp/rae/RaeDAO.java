package br.com.constran.mobile.persistence.dao.imp.rae;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.R;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.rae.RaeVO;
import br.com.constran.mobile.view.util.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RaeDAO extends AbstractDAO {

    private static final String RAE_COL_ID_RAE = "idRAE";
    private static final String RAE_COL_TOTALIZ_INI = "totalizadorInicial";
    private static final String RAE_COL_TOTALIZ_FIM = "totalizadorFinal";
    private static final String RAE_COL_POSTO = "posto";
    private static final String RAE_COL_DATA = "data";

    private static RaeDAO instance;

    private RaeDAO(Context context) {
        super(context, TBL_RAE);
    }

    public static RaeDAO getInstance(Context context) {
        if (instance == null) {
            instance = new RaeDAO(context);
        }
        return instance;
    }

    public Integer getIdRae(RaeVO rae) {
        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{RAE_COL_ID_RAE};

        tableJoin = TBL_RAE;

        conditions = " data = '" + rae.getData() + "'";
        conditions += " and posto = " + rae.getPosto().getId();

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);
        Integer idRAE = null;

        if (cursor.moveToNext()) {
            idRAE = cursor.getInt(cursor.getColumnIndex(RAE_COL_ID_RAE));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return idRAE;
    }

    public List<RaeVO> findRaesByData(Date data) {
        String[] columns = new String[]{RAE_COL_ID_RAE, RAE_COL_DATA, RAE_COL_POSTO};

        String tableJoin = TBL_RAE + " r ";
        tableJoin += " inner join abastecimentos a on r.[idRae] = a.[rae]";

        String conditions = " data = '" + Util.getDateFormated(data) + "'";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(null);

        Cursor cursor = getCursor(query);

        List<RaeVO> raes = new ArrayList<RaeVO>();

        while (cursor.moveToNext()) {
            raes.add(new RaeVO(cursor.getInt(cursor.getColumnIndex(RAE_COL_ID_RAE)),
                    cursor.getString(cursor.getColumnIndex(RAE_COL_DATA))));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return raes;

    }

    public String[] getValues(String[] params) {

        String[] columns = new String[]{RAE_COL_TOTALIZ_INI, RAE_COL_TOTALIZ_FIM};

        String tableJoin = TBL_RAE;

        String conditions = " data = '" + params[0] + "'";
        conditions += " and posto = " + params[1];

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(null);

        Cursor cursor = getCursor(query);

        String[] dados = null;

        if (cursor.moveToNext()) {
            dados = new String[]{cursor.getString(cursor.getColumnIndex(RAE_COL_TOTALIZ_INI)),
                    cursor.getString(cursor.getColumnIndex(RAE_COL_TOTALIZ_FIM))};
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public void save(RaeVO pVO) {
        if (pVO.getId() == null) {
            insert(getContentValues(pVO));
        } else {
            String whereClause = RAE_COL_DATA + "= ? and " + RAE_COL_POSTO + "= ?";
            String[] whereArgs = new String[]{pVO.getData(), pVO.getPosto().getId().toString()};
            update(getContentValues(pVO), whereClause, whereArgs);
        }

    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        RaeVO pVO = (RaeVO) abstractVO;
        ContentValues contentValues = new ContentValues();
        contentValues.put(RAE_COL_POSTO, pVO.getPosto().getId());
        contentValues.put(RAE_COL_DATA, pVO.getData());
        contentValues.put(RAE_COL_TOTALIZ_INI, pVO.getTotalizadorInicial() == null ? getStr(R.string.EMPTY) : pVO.getTotalizadorInicial());
        contentValues.put(RAE_COL_TOTALIZ_FIM, pVO.getTotalizadorFinal() == null ? getStr(R.string.EMPTY) : pVO.getTotalizadorFinal());

        return contentValues;
    }

}
