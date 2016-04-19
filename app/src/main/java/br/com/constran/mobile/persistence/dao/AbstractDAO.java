package br.com.constran.mobile.persistence.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import br.com.constran.mobile.R;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.system.AppDirectory;
import br.com.constran.mobile.view.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class AbstractDAO extends AbstractDatabaseDAO {

    protected final int LIMITE = 45;

    public static SQLiteDatabase database;

    public static Context context;

    private static final String BASE_TAG = "DataBaseManager";
    private final String tableName;
    private DataBaseOpenHelper openHelper;

    public AbstractDAO(Context context) {
        AbstractDAO.context = context;
        tableName = null;

        if (!isOpen()) {
            try {
                openHelper = new DataBaseOpenHelper(context);
                database = openHelper.getWritableDatabase();
            } catch (Exception e) {
                Log.e(BASE_TAG, "Erro ao conectar ao banco de dados");
            }

        }
    }

    public AbstractDAO(Context context, String tableName) {
        AbstractDAO.context = context;
        this.tableName = tableName;

        try {
            if (!isOpen()) {
                openHelper = new DataBaseOpenHelper(context);
                database = openHelper.getWritableDatabase();
            }
        } catch (Exception e) {
            Log.e(BASE_TAG, "Erro ao conectar ao banco de dados");
        }
    }

    public AbstractDAO(Context context, String tableName, boolean readOnly) {
        AbstractDAO.context = context;
        this.tableName = tableName;

        try {
            if (!isOpen()) {
                openHelper = new DataBaseOpenHelper(context);
                database = readOnly ? openHelper.getReadableDatabase() : openHelper.getWritableDatabase();
            }
        } catch (Exception e) {
            Log.e(BASE_TAG, "Erro ao conectar ao banco de dados");
        }
    }

    public void close() {
        database.close();
        openHelper.close();
    }

    public boolean isOpen() {

        File dbFile = new File(AppDirectory.PATH_DATABASE+"/"+DATABASE_NAME);

        if (dbFile.exists()) {
            return database != null && database.isOpen();
        }

        if (database != null && database.isOpen()) {
            database.close();
        }

        return false;
    }

    public Long insert(ContentValues contentValues) {
        return insert(tableName, contentValues);
    }

    public long insert(String tableName, ContentValues contentValues) {
        try {
            database.beginTransaction();
            long id = database.insert(tableName, null, contentValues);
            database.setTransactionSuccessful();
            return id;
        } catch (Exception e) {
            Log.e(BASE_TAG, "Erro ao inserir no banco de dados");
            return -1L;
        } finally {
            database.endTransaction();
        }
    }

    public void update(ContentValues contentValues, String whereClause, String[] whereArgs) {
        update(tableName, contentValues, whereClause, whereArgs);
    }

    public void update(String tableName, ContentValues contentValues, String whereClause, String[] whereArgs) {
        try {
            database.beginTransaction();
            database.update(tableName, contentValues, whereClause, whereArgs);
            database.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(BASE_TAG, getStr(R.string.ERROR_DE_CONEXAO));
        } finally {
            database.endTransaction();
        }
    }

    public int delete(String whereClause, String[] whereArgs) {
        return delete(whereClause, whereArgs, tableName);
    }

    public int delete(String whereClause, String[] whereArgs, String tableName) {
        try {
            database.beginTransaction();
            int deleted = database.delete(tableName, whereClause, whereArgs);
            database.setTransactionSuccessful();
            return deleted;
        } catch (Exception e) {
            Log.e(BASE_TAG, getStr(R.string.ERROR_DE_CONEXAO));
            return 0;
        } finally {
            database.endTransaction();
        }
    }

    public int deleteAll() {
        try {
            database.beginTransaction();
            int deleted = database.delete(tableName, null, null);
            database.setTransactionSuccessful();
            return deleted;
        } catch (Exception e) {
            Log.e(BASE_TAG, getStr(R.string.ERROR_DE_CONEXAO));
            return 0;
        } finally {
            database.endTransaction();
        }
    }

    public int deleteById(Integer id) {
        return delete(concatClauses(getPkColumns()), concatArgs(id));
    }

    public int delete(List<Integer> ids) {
        String whereClause = getPkColumns() + " in (" + toParamsArgs(ids) + ")";
        String[] whereArgs = toParamsValues(ids);
        return delete(whereClause, whereArgs);
    }

    public int delete(String tableName, List<Integer> ids) {
        String whereClause = getPkColumns() + " in (" + toParamsArgs(ids) + ")";
        String[] whereArgs = toParamsValues(ids);
        return delete(whereClause, whereArgs, tableName);
    }

    public int delete(List<Integer> ids, String idColumn) {
        String whereClause = idColumn + " in (" + toParamsArgs(ids) + ")";
        String[] whereArgs = toParamsValues(ids);
        return delete(whereClause, whereArgs);
    }

    public int count() {
        Cursor cursor = findByQuery("select count(1) from " + tableName);
        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }

        return count;
    }

    public long maxId() {
        Cursor cursor = findByQuery("select max(" + getPkColumns() + ") from " + tableName);
        long maxId = 1;

        if (cursor.moveToFirst()) {
            maxId = cursor.getLong(0);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }

        return maxId;
    }

    public Cursor findById(Integer id) {
        StringBuilder query = new StringBuilder();
        query.append("select * from ")
                .append(tableName)
                .append(" where ")
                .append(getPkColumns())
                .append(" = ")
                .append(id)
                .append(" order by ")
                .append(getPkColumns());

        return findByQuery(query.toString());
    }

    public Cursor findByCompositeId(Object... ids) {
        StringBuilder query = new StringBuilder();
        query.append("select * from ")
                .append(tableName)
                .append(" where 1=1 ");

        for (String column : getPkColumns()) {
            query.append(" and ").append(column).append(" = ? ");
        }

        return findByQuery(query.toString(), concatArgs(ids));
    }

    protected void executeSQL(String sql) {
        database.execSQL(sql);
    }

    protected void executeSQL(String sql, Object[] args) {
        database.execSQL(sql, args);
    }

    protected void deleteWhere(String whereClause) {
        String sql = "delete from " + tableName + " where " + whereClause;
        database.execSQL(sql);
    }

    protected void deleteIn(Collection<Integer> ids, String idColumn) {
        String sql = "delete from " + tableName + " where " + idColumn + " in(" + toStringValues(ids) + ")";
        database.execSQL(sql);
    }

    protected void deleteNotIn(Collection<Integer> ids, String idColumn) {
        String sql = "delete from " + tableName + " where " + idColumn + " not in(" + toStringValues(ids) + ")";
        database.execSQL(sql);
    }

    public Cursor find() {
        return database.rawQuery("select * from " + tableName, null);
    }

    public Cursor findAll() {
        return database.rawQuery("select * from " + tableName, null);
    }

    public Cursor findAllNotOrdered() {
        return database.rawQuery("select * from " + tableName, null);
    }

    public Cursor findAll(String tableName) {
        return database.rawQuery("select * from " + tableName, null);
    }

    public Cursor findAllOrderedBy(String orderBy) {
        return database.rawQuery("select * from " + tableName + " order by " + orderBy, null);
    }


    public Cursor executeSQLGroupBy(String field) {
        return database.rawQuery("select * from " + tableName + " group by " + field, null);
    }

    /**
     * Busca registros passando todos os parametros de selecao e ordenacao
     *
     * @param columns       A list of which columns to return. Passing null will return all columns, which is discouraged to prevent reading data from storage that isn't going to be used.
     * @param selection     A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself). Passing null will return all rows for the given table.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values from selectionArgs, in order that they appear in the selection. The values will be bound as Strings.
     * @param groupBy       A filter declaring how to group rows, formatted as an SQL GROUP BY clause (excluding the GROUP BY itself). Passing null will cause the rows to not be grouped.
     * @param having        A filter declare which row groups to include in the cursor, if row grouping is being used, formatted as an SQL HAVING clause (excluding the HAVING itself). Passing null will cause all row groups to be included, and is required when row grouping is not being used.
     * @param orderBy       How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort order, which may be unordered.
     */
    public Cursor findAll(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return database.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    /**
     * Busca registros passando todos os parametros de selecao e ordenacao e limite
     *
     * @param columns       A list of which columns to return. Passing null will return all columns, which is discouraged to prevent reading data from storage that isn't going to be used.
     * @param selection     A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself). Passing null will return all rows for the given table.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values from selectionArgs, in order that they appear in the selection. The values will be bound as Strings.
     * @param groupBy       A filter declaring how to group rows, formatted as an SQL GROUP BY clause (excluding the GROUP BY itself). Passing null will cause the rows to not be grouped.
     * @param having        A filter declare which row groups to include in the cursor, if row grouping is being used, formatted as an SQL HAVING clause (excluding the HAVING itself). Passing null will cause all row groups to be included, and is required when row grouping is not being used.
     * @param orderBy       How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort order, which may be unordered.
     * @param limit         Limits the number of rows returned by the query, formatted as LIMIT clause. Passing null denotes no LIMIT clause.
     */
    public Cursor findAll(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return database.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    /**
     * Busca registros passando todos os parametros de selecao e ordenacao
     * Podendo trazer apenas items distintos e limitando os resultados
     *
     * @param distinct      true if you want each row to be unique, false otherwise.
     * @param columns       A list of which columns to return. Passing null will return all columns, which is discouraged to prevent reading data from storage that isn't going to be used.
     * @param selection     A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself). Passing null will return all rows for the given table.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values from selectionArgs, in order that they appear in the selection. The values will be bound as Strings.
     * @param groupBy       A filter declaring how to group rows, formatted as an SQL GROUP BY clause (excluding the GROUP BY itself). Passing null will cause the rows to not be grouped.
     * @param having        A filter declare which row groups to include in the cursor, if row grouping is being used, formatted as an SQL HAVING clause (excluding the HAVING itself). Passing null will cause all row groups to be included, and is required when row grouping is not being used.
     * @param orderBy       How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort order, which may be unordered.
     * @param limit         Limits the number of rows returned by the query, formatted as LIMIT clause. Passing null denotes no LIMIT clause.
     */
    public Cursor findAll(boolean distinct, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return database.query(distinct, tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public Cursor findByQuery(String query) {
        return database.rawQuery(query, null);
    }

    public Cursor findByQuery(String query, String[] args) {
        try {
            return database.rawQuery(query, args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected String toParamsArgs(Collection<Integer> ids) {
        StringBuilder params = new StringBuilder();

        for (@SuppressWarnings("unused") Integer id : ids) {
            params.append("?,");
        }

        return params.substring(0, params.length() - 1);
    }

    protected String[] toParamsValues(Collection<Integer> ids) {
        String[] params = new String[ids.size()];

        int i = 0;

        for (Iterator<Integer> it = ids.iterator(); it.hasNext(); ) {
            params[i++] = String.valueOf(it.next());
        }

        return params;
    }

    protected String toStringValues(Collection<Integer> ids) {
        StringBuilder params = new StringBuilder();

        for (Integer id : ids) {
            params.append(id + ",");
        }

        return params.substring(0, params.length() - 1);
    }

    /**
     * remover este método quando a passagem de dados para a activity deixar de ser por array
     * e passar a ser por lista de objetos
     */
    protected String[] preencherDados(Cursor cursor) {
        String[] columns = getColunas();
        String[] dados = new String[columns.length];

        if (cursor.moveToNext() && columns != null && dados.length > 0) {
            for (int i = 0; i < dados.length; i++) {
                dados[i] = cursor.getString(cursor.getColumnIndex(columns[i]));
            }
        } else {
            dados = null;
        }

        return dados;
    }

    protected String[] getColunas() {
        return null;
    }

    /**
     * Deve ser sobrescrito pelas subclasses para preencher o ContentValue específico
     *
     * @param abstractVO
     * @return ContentValues
     */
    protected ContentValues getContentValues(Object abstractVO) {
        return new ContentValues();
    }

    //************ métodos da classe antiga a partir deste ponto **********************//

    public Cursor getCursor(Query query) {
        return database.query(query.isDistinct(), query.getTableJoin(), query.getColumns(),
                query.getConditions(), query.getConditionsArgs(), query.getGroupBy(), query.getHaving(), query.getOrderBy(), query.getLimit());
    }

    public Cursor getCursorRaw(String query) {
        return database.rawQuery(query, null);
    }

    public void execute(StringBuilder builder) {

        execute(builder.toString());
    }




    /*
* Criado por Moises Santana
*
* 04-03-15
* */
    public Cursor getCursorRawParams(String query, String[] selectionArgs) {
        return database.rawQuery(query, selectionArgs);
    }


    public void execute(String sql) {

        database.execSQL(sql);

    }


    public void execute(String[] sqlArray) {

        for (String sql : sqlArray) {

            execute(sql);
        }
    }

    public static String getStr(int id) {

        return context.getResources().getString(id);
    }

    protected void closeCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    protected String concatClauses(String... clauses) {
        StringBuilder list = new StringBuilder();
        list.append("1 = 1");

        for (String c : clauses) {
            list.append(" and ");
            list.append(c);
            list.append(" = ? ");
        }

        return list.toString();
    }

    protected String[] concatArgs(Object... args) {
        if (args == null) {
            return null;
        }

        List<String> list = new ArrayList<String>(args.length);

        for (Object a : args) {
            list.add(a != null ? a.toString() : null);
        }

        return list.toArray(new String[0]);
    }

    /**
     * @return retorna o nome da(s) coluna(s) que compoem a PK
     */
    public String[] getPkColumns() {
        return new String[]{};
    }

    protected Integer getInt(Cursor cursor, String column) {
        int index = cursor.getColumnIndex(column);
        return index > -1 ? cursor.getInt(index) : null;
    }

    protected String getString(Cursor cursor, String column) {
        int index = cursor.getColumnIndex(column);
        return index > -1 ? cursor.getString(index) : null;
    }

}
