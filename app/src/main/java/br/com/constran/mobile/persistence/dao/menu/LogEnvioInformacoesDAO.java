package br.com.constran.mobile.persistence.dao.menu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.menu.LogEnvioInformacoesVO;

/**
 * Created by mateus_vitali on 05/11/2014.
 */
public class LogEnvioInformacoesDAO extends AbstractDAO {

    private static final String OBRA_COL_ID = "id";
    private static final String OBRA_COL_ARQUIVO = "arquivo";
    private static final String OBRA_COL_DATA = "data";
    private static final String OBRA_COL_OBRA = "obra";
    private static final String OBRA_COL_DATA_HORA_ENVIO = "dataHoraEnvio";

    private static LogEnvioInformacoesDAO instance;

    private LogEnvioInformacoesDAO(Context context) {
        super(context, TBL_LOG_ENVIO_INFORMACOES);
    }

    public static LogEnvioInformacoesDAO getInstance(Context context) {
        if (instance == null) {
            instance = new LogEnvioInformacoesDAO(context);
        }
        return instance;
    }

    public LogEnvioInformacoesVO[] getAllLogEnvioInformacoesVO() {

        Cursor cursor =  super.findAll();

        LogEnvioInformacoesVO[] dados = new LogEnvioInformacoesVO[cursor.getCount()];

        int i = 0;

        while (cursor.moveToNext()) {
            dados[i++] = new LogEnvioInformacoesVO(
                    cursor.getInt(cursor.getColumnIndex(OBRA_COL_ID)),
                    cursor.getString(cursor.getColumnIndex(OBRA_COL_ARQUIVO)),
                    cursor.getString(cursor.getColumnIndex(OBRA_COL_DATA)),
                    cursor.getString(cursor.getColumnIndex(OBRA_COL_OBRA)),
                    cursor.getString(cursor.getColumnIndex(OBRA_COL_DATA_HORA_ENVIO)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public LogEnvioInformacoesVO[] getAllLogEnvioInformacoesVODistinct() {

        Cursor cursor = super.executeSQLGroupBy("data");

        LogEnvioInformacoesVO[] dados = new LogEnvioInformacoesVO[cursor.getCount()];

        int i = 0;

        while (cursor.moveToNext()) {
            dados[i++] = new LogEnvioInformacoesVO(
                    cursor.getInt(cursor.getColumnIndex(OBRA_COL_ID)),
                    cursor.getString(cursor.getColumnIndex(OBRA_COL_ARQUIVO)),
                    cursor.getString(cursor.getColumnIndex(OBRA_COL_DATA)),
                    cursor.getString(cursor.getColumnIndex(OBRA_COL_OBRA)),
                    cursor.getString(cursor.getColumnIndex(OBRA_COL_DATA_HORA_ENVIO)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public void save(LogEnvioInformacoesVO pVO) {
        insert(getContentValues(pVO));
    }

    public LogEnvioInformacoesVO getById(Integer idObra) {

        Query query = new Query(true);

        query.setColumns(new String[]{OBRA_COL_ID, OBRA_COL_ARQUIVO, OBRA_COL_DATA, OBRA_COL_OBRA,
                OBRA_COL_DATA_HORA_ENVIO});

        query.setTableJoin("[" + TBL_LOG_ENVIO_INFORMACOES + "]");

        query.setConditions("[id] = ? ");

        query.setConditionsArgs(new String[]{idObra.toString()});

        Cursor cursor = getCursor(query);

        LogEnvioInformacoesVO logEnvio = null;

        if (cursor.moveToNext()) {
            logEnvio = new LogEnvioInformacoesVO(
                    cursor.getInt(cursor.getColumnIndex(OBRA_COL_ID)),
                    cursor.getString(cursor.getColumnIndex(OBRA_COL_ARQUIVO)),
                    cursor.getString(cursor.getColumnIndex(OBRA_COL_DATA)),
                    cursor.getString(cursor.getColumnIndex(OBRA_COL_OBRA)),
                    cursor.getString(cursor.getColumnIndex(OBRA_COL_DATA_HORA_ENVIO)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return logEnvio;
    }

    public LogEnvioInformacoesVO[] getLogEnvioInformacoesVOByDate(String date) {

        Query query = new Query(true);

        query.setColumns(new String[]{OBRA_COL_ID, OBRA_COL_ARQUIVO, OBRA_COL_DATA, OBRA_COL_OBRA,
                OBRA_COL_DATA_HORA_ENVIO});

        query.setTableJoin("[" + TBL_LOG_ENVIO_INFORMACOES + "]");

        query.setConditions("[data] = ? ");

        query.setConditionsArgs(new String[]{date});

        Cursor cursor = getCursor(query);

        LogEnvioInformacoesVO[] dados = new LogEnvioInformacoesVO[cursor.getCount()];

        int i = 0;

        while (cursor.moveToNext()) {
            dados[i++] = new LogEnvioInformacoesVO(
                    cursor.getInt(cursor.getColumnIndex(OBRA_COL_ID)),
                    cursor.getString(cursor.getColumnIndex(OBRA_COL_ARQUIVO)),
                    cursor.getString(cursor.getColumnIndex(OBRA_COL_DATA)),
                    cursor.getString(cursor.getColumnIndex(OBRA_COL_OBRA)),
                    cursor.getString(cursor.getColumnIndex(OBRA_COL_DATA_HORA_ENVIO)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        LogEnvioInformacoesVO logEnvioInformacoesVO = (LogEnvioInformacoesVO) abstractVO;
        ContentValues contentValues = new ContentValues();
        contentValues.put(OBRA_COL_ID, logEnvioInformacoesVO.getId());
        contentValues.put(OBRA_COL_ARQUIVO, logEnvioInformacoesVO.getArquivo());
        contentValues.put(OBRA_COL_DATA, logEnvioInformacoesVO.getData());
        contentValues.put(OBRA_COL_OBRA, logEnvioInformacoesVO.getObra());
        contentValues.put(OBRA_COL_DATA_HORA_ENVIO, logEnvioInformacoesVO.getDataHoraEnvio());

        return contentValues;
    }
}
