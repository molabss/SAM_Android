package br.com.constran.mobile.persistence.vo;

/**
 * Criado em 17/09/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class Intervalo {

    private String horaIni;
    private String horaFim;

    public Intervalo(String horaIni, String horaFim) {
        this.horaIni = horaIni;
        this.horaFim = horaFim;
    }

    public String getHoraIni() {
        return horaIni;
    }

    public void setHoraIni(String horaIni) {
        this.horaIni = horaIni;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }
}
