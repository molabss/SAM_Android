package br.com.constran.mobile.view.util;


public class Eticket {

    private String dataApontamento;
    private String horaViagem;

    public Eticket(String dataApontamento) {
        this.dataApontamento = dataApontamento;
    }

    public String getDataApontamento() {
        return dataApontamento;
    }

    public void setDataApontamento(String dataApontamento) {
        this.dataApontamento = dataApontamento;
    }

    public String getHoraViagem() {
        return horaViagem;
    }

    public void setHoraViagem(String horaViagem) {
        this.horaViagem = horaViagem;
    }


}
