package br.com.constran.mobile.persistence.vo.imp;

import br.com.constran.mobile.persistence.vo.AbstractVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.ParalisacaoMaoObraVO;

import java.util.List;

public class ParalisacaoVO extends AbstractVO {

    private static final long serialVersionUID = -8270302736141974170L;
    private String codigo;
    private String requerEstaca;
    private String aplicacao;

    private List<ParalisacaoMaoObraVO> paralisacoesMaoObra;

    public ParalisacaoVO(String descricao) {
        super(descricao);
    }

    public ParalisacaoVO(Integer id, String descricao) {
        super(id, descricao);
    }

    public ParalisacaoVO(Integer id, String descricao, String requerEstaca, String codigo) {
        super(id, descricao);
        this.codigo = codigo;
        this.requerEstaca = requerEstaca;
    }

    public ParalisacaoVO(Integer id) {
        super(id);
    }

    @Override
    public String toString() {

        return getDescricao();
    }

    public String getRequerEstaca() {
        return requerEstaca;
    }

    public void setRequerEstaca(String requerEstaca) {
        this.requerEstaca = requerEstaca;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public List<ParalisacaoMaoObraVO> getParalisacoesMaoObra() {
        return paralisacoesMaoObra;
    }

    public void setParalisacoesMaoObra(List<ParalisacaoMaoObraVO> paralisacoesMaoObra) {
        this.paralisacoesMaoObra = paralisacoesMaoObra;
    }

    public String getAplicacao() {
        return aplicacao;
    }

    public void setAplicacao(String aplicacao) {
        this.aplicacao = aplicacao;
    }

    public String getDescricaoFormatada() {
        final Integer COD_PRODUZINDO = 16;
        final String PRODUZINDO = " 16 - Produzindo";

        if (COD_PRODUZINDO.equals(getId())) {
            return PRODUZINDO;
        }

        String codigo = getId() < 10 ? " 0" : " ";

        return codigo + getId() + " - " + getDescricao();
    }
}
