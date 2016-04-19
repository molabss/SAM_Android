package br.com.constran.mobile.persistence.vo.imp;

import br.com.constran.mobile.persistence.vo.AbstractVO;

public class OrigemDestinoVO extends AbstractVO {

    private static final long serialVersionUID = -4718999984301938936L;
    private String estacaInicial;
    private String estacaFinal;
    private Integer tipo;
    private String descricaoTipo;

    public OrigemDestinoVO(Integer id, String descricao) {
        super(id, descricao);
    }

    public OrigemDestinoVO(Integer id, String descricao, String estacaInicial, String estacaFinal, Integer tipo, String descricaoTipo) {
        super(id, descricao);
        this.estacaInicial = estacaInicial;
        this.estacaFinal = estacaFinal;
        this.tipo = tipo;
        this.descricaoTipo = descricaoTipo;
    }

    public OrigemDestinoVO(Integer id, String descricao, String estacaInicial, String estacaFinal) {
        super(id, descricao);
        this.estacaInicial = estacaInicial;
        this.estacaFinal = estacaFinal;
    }

    public OrigemDestinoVO(Integer id) {
        super(id);
    }

    public OrigemDestinoVO() {
        super();
    }

    public OrigemDestinoVO(String descricao) {
        super(descricao);
    }

    @Override
    public String toString() {

        return getDescricaoCompleta();
    }

    public String getDescricaoCompleta() {

        if (getEstacaFinal() == null || getEstacaInicial() == null)
            return getDescricao();

        return getDescricao() + " (" + getEstacaInicial().trim() + " a " + getEstacaFinal().trim() + ")";
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

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getDescricaoTipo() {
        return descricaoTipo;
    }

    public void setDescricaoTipo(String descricaoTipo) {
        this.descricaoTipo = descricaoTipo;
    }

}
