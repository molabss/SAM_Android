package br.com.constran.mobile.persistence.vo.aprop.maodeobra;

import br.com.constran.mobile.persistence.vo.aprop.ApropriacaoVO;
import br.com.constran.mobile.persistence.vo.aprop.BaseVO;
import br.com.constran.mobile.persistence.vo.imp.ServicoVO;

/**
 * Criado em 05/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class ApropriacaoServicoVO extends BaseVO {

    private ApropriacaoVO apropriacao;
    private EquipeTrabalhoVO equipe;
    private ServicoVO servico;
    private Double quantidadeProduzida;
    private String observacoes;
    private String horaIni;
    private String horaFim;

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

    public Double getQuantidadeProduzida() {
        return quantidadeProduzida;
    }

    public void setQuantidadeProduzida(Double quantidadeProduzida) {
        this.quantidadeProduzida = quantidadeProduzida;
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

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public ServicoVO getServico() {
        return servico;
    }

    public void setServico(ServicoVO servico) {
        this.servico = servico;
    }

}
