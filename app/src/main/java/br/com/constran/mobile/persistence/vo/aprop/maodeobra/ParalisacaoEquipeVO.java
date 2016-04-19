package br.com.constran.mobile.persistence.vo.aprop.maodeobra;

import br.com.constran.mobile.persistence.vo.aprop.ApropriacaoVO;
import br.com.constran.mobile.persistence.vo.aprop.BaseVO;
import br.com.constran.mobile.persistence.vo.imp.ParalisacaoVO;
import br.com.constran.mobile.persistence.vo.imp.ServicoVO;

/**
 * Criado em 05/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class ParalisacaoEquipeVO extends BaseVO {

    private ApropriacaoVO apropriacao;
    private EquipeTrabalhoVO equipe;
    private ParalisacaoVO paralisacao;
    private String horaInicio;
    private String horaTermino;
    private String observacoes;

    private transient ApropriacaoServicoVO apropriacaoServico;
    private ServicoVO servico;

    public ApropriacaoVO getApropriacao() {
        return apropriacao;
    }

    public void setApropriacao(ApropriacaoVO apropriacao) {
        this.apropriacao = apropriacao;
    }

    public EquipeTrabalhoVO getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeTrabalhoVO equipe) {
        this.equipe = equipe;
    }

    public ParalisacaoVO getParalisacao() {
        return paralisacao;
    }

    public void setParalisacao(ParalisacaoVO paralisacao) {
        this.paralisacao = paralisacao;
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

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public ApropriacaoServicoVO getApropriacaoServico() {
        return apropriacaoServico;
    }

    public void setApropriacaoServico(ApropriacaoServicoVO apropriacaoServico) {
        if (apropriacaoServico != null && apropriacaoServico.getServico() != null) {
            servico = apropriacaoServico.getServico();
        }
        this.apropriacaoServico = apropriacaoServico;
    }

    /**
     * Usado para gerar dados do JSON
     *
     * @return
     */
    public ServicoVO getServico() {
        return servico;
    }

}
