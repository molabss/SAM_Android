package br.com.constran.mobile.persistence.vo.aprop.maodeobra;

import br.com.constran.mobile.persistence.vo.aprop.BaseVO;

/**
 * Criado em 05/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class PeriodoHorarioTrabalhoVO extends BaseVO {

    private HorarioTrabalhoVO horario;
    private String diaSemana;
    private String horaInicio;
    private String horaTermino;
    private String produtivo;
    private String cobraHoraExtra;
    private String codigoParalisacao;

    public HorarioTrabalhoVO getHorario() {
        return horario;
    }

    public void setHorario(HorarioTrabalhoVO horario) {
        this.horario = horario;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraTermino() {
        return horaTermino;
    }

    public void setHoraTermino(String horaTermino) {
        this.horaTermino = horaTermino;
    }

    public String getProdutivo() {
        return produtivo;
    }

    public void setProdutivo(String produtivo) {
        this.produtivo = produtivo;
    }

    public String getCobraHoraExtra() {
        return cobraHoraExtra;
    }

    public void setCobraHoraExtra(String cobraHoraExtra) {
        this.cobraHoraExtra = cobraHoraExtra;
    }

    public String getCodigoParalisacao() {
        return codigoParalisacao;
    }

    public void setCodigoParalisacao(String codigoParalisacao) {
        this.codigoParalisacao = codigoParalisacao;
    }
}
