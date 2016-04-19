package br.com.constran.mobile.persistence.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.constran.mobile.model.LogAuditoria;
import br.com.constran.mobile.view.util.Util;

/**
 * Created by moises_santana on 01/09/2015.
 */
public class LogAuditoriaDAO extends AbstractDAO {


    private static LogAuditoriaDAO instance;
    private LogAuditoria log;

    public LogAuditoriaDAO(Context context) {
        super(context, TBL_LOG_AUDITORIA);
    }

    public static LogAuditoriaDAO getInstance(Context context){

        if(instance == null){
            instance = new LogAuditoriaDAO(context);
        }
        return instance;
    }

    public void setLogPropriedades(LogAuditoria log){
        this.log = log;
    }

    public long insereLog(String acao){
        ContentValues values = new ContentValues();
        values.put("modulo",log.getModulo());
        values.put("dataHora", Util.getNow_EN());
        values.put("dispositivo",log.getDispositivo());
        values.put("acao",acao);

        return insert(TBL_LOG_AUDITORIA,values);
    }


    public List<LogAuditoria> listarTodos(){

        List<LogAuditoria> lista = new ArrayList<LogAuditoria>();
        LogAuditoria log;

        StringBuilder select = new StringBuilder();
        select.append("select * from logAuditoria");

        Cursor cursor = getCursorRawParams(select.toString(),null);

        while(cursor.moveToNext()) {

            log = new LogAuditoria();
            log.setModulo(cursor.getString(cursor.getColumnIndex("modulo")));
            log.setDataHora(cursor.getString(cursor.getColumnIndex("dataHora")));
            log.setDispositivo(cursor.getString(cursor.getColumnIndex("dispositivo")));
            log.setAcao(cursor.getString(cursor.getColumnIndex("acao")));
            lista.add(log);
        }
        return lista;
    }


    public int limparTabela(){
        return database.delete("logAuditoria",null,null);
    }

}
