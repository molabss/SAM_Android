package br.com.constran.mobile.persistence.vo.menu;

import br.com.constran.mobile.persistence.vo.AbstractVO;
import br.com.constran.mobile.persistence.vo.imp.AtividadeVO;
import br.com.constran.mobile.persistence.vo.imp.OrigemDestinoVO;


public class LocalizacaoVO extends AbstractVO {
    private static final long serialVersionUID = 4583316466814018939L;
    //FK
    private OrigemDestinoVO origem;
    private OrigemDestinoVO destino;
    private AtividadeVO atividade;

    private String tipo;

    private String dataHora;
    private String dataAtualizacao;

    private String estacaInicial;
    private String estacaFinal;

    public LocalizacaoVO() {

    }

    public LocalizacaoVO(Integer id) {
        super(id);
    }

    @Override
    public String toString() {
        return getDescricao();
    }

    public OrigemDestinoVO getOrigem() {
        return origem;
    }

    public void setOrigem(OrigemDestinoVO origem) {
        this.origem = origem;
    }

    public OrigemDestinoVO getDestino() {
        return destino;
    }

    public void setDestino(OrigemDestinoVO destino) {
        this.destino = destino;
    }

    public AtividadeVO getAtividade() {
        return atividade;
    }

    public void setAtividade(AtividadeVO atividade) {
        this.atividade = atividade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public String getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(String dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getEstacaInicial() {
        return estacaInicial;
    }

    public void setEstacaInicial(String estacaInicial) {
        this.estacaInicial = estacaInicial;
    }

    public String getEstacaFinal() {
        return estacaFinal;
    }

    public void setEstacaFinal(String estacaFinal) {
        this.estacaFinal = estacaFinal;
    }


}
