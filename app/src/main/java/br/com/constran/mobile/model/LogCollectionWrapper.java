package br.com.constran.mobile.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moises_santana on 02/09/2015.
 */
public class LogCollectionWrapper {

    private  List<LogAuditoria> listaDeLogs = null;
    private  String ccObra;

    public  void add(LogAuditoria log){
        if(listaDeLogs == null)listaDeLogs = new ArrayList<LogAuditoria>();
        listaDeLogs.add(log);
    }


    public  List<LogAuditoria> getListaDeLogs(boolean clear) {
        if(clear){
            List<LogAuditoria> tempListaDeLogs = new ArrayList<LogAuditoria>(listaDeLogs);
            listaDeLogs.clear();
            return tempListaDeLogs;
        }
        return listaDeLogs;
    }

    public  void setListaDeLogs(List<LogAuditoria> listaDeLogs) {
        this.listaDeLogs = listaDeLogs;
    }

    public  String getCcObra() {
        return ccObra;
    }

    public void setCcObra(String ccObra) {
        this.ccObra = ccObra;
    }
}
