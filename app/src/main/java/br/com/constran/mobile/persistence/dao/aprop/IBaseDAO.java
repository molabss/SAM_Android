package br.com.constran.mobile.persistence.dao.aprop;

import android.content.ContentValues;
import android.database.Cursor;
import br.com.constran.mobile.persistence.Query;

import java.util.List;

/**
 * Criado em 09/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public interface IBaseDAO<T> {

    void save(T t);

    void delete(T t);

    T findById(T t);

    List<T> findAllItems();

    List<T> findList(String orderBy);

    List<T> findList(Query query);

    List<T> findList(boolean distinct, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit);

    List<T> findList(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy);

    List<T> findList(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit);

    T popularEntidade(Cursor cursor);

    ContentValues bindContentValues(T t);

    List<T> newList();
}
