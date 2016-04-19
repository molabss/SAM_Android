package br.com.constran.mobile.persistence.vo.aprop.maodeobra;

import br.com.constran.mobile.persistence.vo.aprop.BaseVO;
import br.com.constran.mobile.persistence.vo.imp.AtividadeVO;
import br.com.constran.mobile.persistence.vo.imp.ServicoVO;

/**
 * Criado em 05/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class AtividadeServicoVO extends BaseVO {

    private Integer id;
    private AtividadeVO atividade;
    private ServicoVO servico;
    private String descricao;
    private Integer ordem;
    private String codigoPrevix;
    private AtividadeServicoVO atividadePai;

    public AtividadeServicoVO() {

    }

    public AtividadeServicoVO(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AtividadeVO getAtividade() {
        return atividade;
    }

    public void setAtividade(AtividadeVO atividade) {
        this.atividade = atividade;
    }

    public ServicoVO getServico() {
        return servico;
    }

    public void setServico(ServicoVO servico) {
        this.servico = servico;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public String getCodigoPrevix() {
        return codigoPrevix;
    }

    public void setCodigoPrevix(String codigoPrevix) {
        this.codigoPrevix = codigoPrevix;
    }

    public AtividadeServicoVO getAtividadePai() {
        return atividadePai;
    }

    public void setAtividadePai(AtividadeServicoVO atividadePai) {
        this.atividadePai = atividadePai;
    }
}
