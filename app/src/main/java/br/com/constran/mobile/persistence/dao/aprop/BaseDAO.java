package br.com.constran.mobile.persistence.dao.aprop;

import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.aprop.BaseVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Criado em 09/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public abstract class BaseDAO<T extends BaseVO> extends AbstractDAO implements IBaseDAO<T> {
    protected static final Integer COD_AUSENTE = 19;
    protected static final Integer COD_PRODUZINDO = 16;
    protected final String AND = " and ";
    protected final String EQ = " = ";
    protected final String QM = " ? ";

    public BaseDAO(Context context, String tableName) {
        super(context, tableName);
    }

    @Override
    public void save(T t) {
        if (isNewEntity(t)) {
            insert(bindContentValues(t));
        } else {
            update(t);
        }
    }

    public void insert(T t) {
        insert(bindContentValues(t));
    }

    public void update(T t) {
        String whereClause = concatClauses(getPkColumns());
        String[] whereArgs = concatArgs(getPkArgs(t));
        update(bindContentValues(t), whereClause, whereArgs);
    }

    @Override
    public void delete(T t) {
        String whereClause = concatClauses(getPkColumns());
        String[] whereArgs = concatArgs(getPkArgs(t));

        //so permite excluir se o numero de parametros for igual ao numero de argumentos
        if (getPkColumns().length == whereArgs.length) {
            delete(whereClause, whereArgs);
        }
    }

    @Override
    public T findById(T t) {
        T t1 = null;

        Cursor cursor = super.findByCompositeId(getPkArgs(t));

        if (cursor != null && cursor.moveToNext()) {
            t1 = popularEntidade(cursor);
        }

        closeCursor(cursor);

        return t1;
    }

    @Override
    public List<T> findAllItems() {
        return bindList(super.findAll());
    }

    @Override
    public List<T> findList(String orderBy) {
        return bindList(super.findAllOrderedBy(orderBy));
    }

    /**
     * Busca items a partir dos dados informados no objeto Query
     *
     * @param q
     * @return
     */
    @Override
    public List<T> findList(Query q) {
        return bindList(super.findAll(q.isDistinct(), q.getColumns(), q.getConditions(), q.getConditionsArgs(), q.getGroupBy(), q.getHaving(), q.getOrderBy(), q.getLimit()));
    }

    @Override
    public List<T> findList(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return bindList(super.findAll(columns, selection, selectionArgs, groupBy, having, orderBy));
    }

    @Override
    public List<T> findList(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return bindList(super.findAll(columns, selection, selectionArgs, groupBy, having, orderBy, limit));
    }

    @Override
    public List<T> findList(boolean distinct, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return bindList(super.findAll(distinct, columns, selection, selectionArgs, groupBy, having, orderBy, limit));
    }

    @Override
    public List<T> newList() {
        return new ArrayList<T>();
    }

    /**
     * Verifica se uma entidade é nova antes de inserir em BD
     *
     * @param t
     * @return
     */
    public abstract boolean isNewEntity(T t);

    /**
     * @param t
     * @return Retorna os valores da PK
     */
    public abstract Object[] getPkArgs(T t);

    protected List<T> bindList(Cursor cursor) {
        List<T> list = newList();

        while (cursor != null && cursor.moveToNext()) {
            list.add(popularEntidade(cursor));
        }

        closeCursor(cursor);

        return list;
    }

}
