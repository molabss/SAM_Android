package br.com.constran.mobile.persistence.vo.aprop.maodeobra;

import br.com.constran.mobile.persistence.vo.ObraVO;
import br.com.constran.mobile.persistence.vo.aprop.BaseVO;

/**
 * Criado em 05/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class ControleFrequenciaVO extends BaseVO {

    private ObraVO obra;
    private PessoalVO pessoa;
    private String horaEntrada;
    private String horaSaida;
    private EquipeTrabalhoVO equipe;
    private String observacoes;

    public ObraVO getObra() {
        return obra;
    }

    public void setObra(ObraVO obra) {
        this.obra = obra;
    }

    public PessoalVO getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoalVO pessoa) {
        this.pessoa = pessoa;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(String horaSaida) {
        this.horaSaida = horaSaida;
    }

    public EquipeTrabalhoVO getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeTrabalhoVO equipe) {
        this.equipe = equipe;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
