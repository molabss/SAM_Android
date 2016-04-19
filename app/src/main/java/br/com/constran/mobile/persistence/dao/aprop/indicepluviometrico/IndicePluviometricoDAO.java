package br.com.constran.mobile.persistence.dao.aprop.indicepluviometrico;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.dao.aprop.BaseDAO;
import br.com.constran.mobile.persistence.vo.aprop.indpluv.IndicePluviometricoVO;

import java.util.List;

/**
 * Created by mateus_vitali on 01/09/2014.
 */
public class IndicePluviometricoDAO extends BaseDAO<IndicePluviometricoVO> {

    private static final String TP_SERVICO = "PLU";
    private static final String INDICE_PLUVIOMETRICO_COL_ID = "id";
    private static final String INDICE_PLUVIOMETRICO_COL_DATA = "data";
    private static final String INDICE_PLUVIOMETRICO_COL_ESTACA_INICIAL = "estacaInicial";
    private static final String INDICE_PLUVIOMETRICO_COL_ESTACA_FINAL = "estacaFinal";
    private static final String INDICE_PLUVIOMETRICO_COL_PLUVIOMETRO = "pluviometro";
    private static final String INDICE_PLUVIOMETRICO_COL_VOLUME_CHUVA = "volumeChuva";

    private static IndicePluviometricoDAO instance;

    public IndicePluviometricoDAO(Context context) {
        super(context, TBL_INDICES_PLUVIOMETRICO);
    }

    public static IndicePluviometricoDAO getInstance(Context context) {
        return instance == null ? instance = new IndicePluviometricoDAO(context) : instance;
    }

    public static IndicePluviometricoVO popularEntidade(Context context, Cursor cursor) {
        return getInstance(context).popularEntidade(cursor);
    }

    @Override
    public IndicePluviometricoVO popularEntidade(Cursor cursor) {
        IndicePluviometricoVO indicePluviometrico = new IndicePluviometricoVO();

        indicePluviometrico.setId(getInt(cursor, INDICE_PLUVIOMETRICO_COL_ID));
        indicePluviometrico.setData(getString(cursor, INDICE_PLUVIOMETRICO_COL_DATA));
        indicePluviometrico.setEstacaInicial(getString(cursor, INDICE_PLUVIOMETRICO_COL_ESTACA_INICIAL));
        indicePluviometrico.setEstacaFinal(getString(cursor, INDICE_PLUVIOMETRICO_COL_ESTACA_FINAL));
        indicePluviometrico.setPluviometro(getString(cursor, INDICE_PLUVIOMETRICO_COL_PLUVIOMETRO));
        indicePluviometrico.setVolumeChuva(getInt(cursor, INDICE_PLUVIOMETRICO_COL_VOLUME_CHUVA));

        return indicePluviometrico;
    }

    @Override
    public ContentValues bindContentValues(IndicePluviometricoVO ip) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(INDICE_PLUVIOMETRICO_COL_ID, ip.getId());
        contentValues.put(INDICE_PLUVIOMETRICO_COL_DATA, ip.getData());
        contentValues.put(INDICE_PLUVIOMETRICO_COL_ESTACA_INICIAL, ip.getEstacaInicial());
        contentValues.put(INDICE_PLUVIOMETRICO_COL_ESTACA_FINAL, ip.getEstacaFinal());
        contentValues.put(INDICE_PLUVIOMETRICO_COL_PLUVIOMETRO, ip.getPluviometro());
        contentValues.put(INDICE_PLUVIOMETRICO_COL_VOLUME_CHUVA, ip.getVolumeChuva());

        return contentValues;
    }

    @Override
    public boolean isNewEntity(IndicePluviometricoVO ip) {
        return ip == null || ip.getId() == null;
    }

    @Override
    public String[] getPkColumns() {
        return new String[]{INDICE_PLUVIOMETRICO_COL_ID};
    }

    @Override
    public Object[] getPkArgs(IndicePluviometricoVO ip) {
        return new Object[]{ip.getId()};
    }

    public List<IndicePluviometricoVO> findDistinctList() {
        StringBuilder query = new StringBuilder();
        query.append("select distinct tip.id, tip.data, tip.estacaInicial, tip.estacaFinal, tip.pluviometro, tip.volumeChuva from ")
                .append(TBL_INDICES_PLUVIOMETRICO).append(" tip ")
                .append("order by date(substr(tip.data,7,4)||'-'||substr(tip.data,4,2)||'-'||substr(tip.data,1,2))");

        Cursor cursor = super.findByQuery(query.toString());

        return bindList(cursor);
    }

}
