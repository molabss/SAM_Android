package br.com.constran.mobile.persistence.vo.aprop.maodeobra;

import br.com.constran.mobile.persistence.vo.ObraVO;
import br.com.constran.mobile.persistence.vo.aprop.BaseVO;

import java.util.List;

/**
 * Criado em 05/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class HorarioTrabalhoVO extends BaseVO {

    private Integer id;
    private String descricao;
    private ObraVO obra;
    private String horaInicio;
    private String horaTermino;

    private List<PeriodoHorarioTrabalhoVO> periodosHorariosTrabalhos;

    public HorarioTrabalhoVO() {

    }

    public HorarioTrabalhoVO(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ObraVO getObra() {
        return obra;
    }

    public void setObra(ObraVO obra) {
        this.obra = obra;
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

    public List<PeriodoHorarioTrabalhoVO> getPeriodosHorariosTrabalhos() {
        return periodosHorariosTrabalhos;
    }

    public void setPeriodosHorariosTrabalhos(List<PeriodoHorarioTrabalhoVO> periodosHorariosTrabalhos) {
        this.periodosHorariosTrabalhos = periodosHorariosTrabalhos;
    }
}
