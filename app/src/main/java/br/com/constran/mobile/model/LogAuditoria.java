package br.com.constran.mobile.model;

/**
 * Created by moises_santana on 01/09/2015.
 */
public class LogAuditoria {

    private String id;
    private String modulo;
    private String acao;
    private String dataHora;
    private String dispositivo;


    
    
    
    public LogAuditoria(String modulo, String dispositivo){
        this.modulo = modulo;
        this.dispositivo = dispositivo;
    }

    public LogAuditoria(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }





    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }



    public String getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }


	
	
	
	
}
