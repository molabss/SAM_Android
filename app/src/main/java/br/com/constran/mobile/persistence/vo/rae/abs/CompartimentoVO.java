package br.com.constran.mobile.persistence.vo.rae.abs;

import br.com.constran.mobile.persistence.vo.AbstractVO;
import br.com.constran.mobile.persistence.vo.imp.CategoriaVO;

public class CompartimentoVO extends AbstractVO {

    private static final long serialVersionUID = -6869187083496539308L;
    private CategoriaVO categoria;
    private String tipo;

    public CompartimentoVO(Integer idCompartimento, String descricao, Integer idCategoria, String tipo) {

        super(idCompartimento, descricao);

        categoria = new CategoriaVO(idCategoria);

        this.tipo = tipo;
    }

    public CompartimentoVO(Integer idCompartimento, Integer idCategoria) {

        super(idCompartimento);

        categoria = new CategoriaVO(idCategoria);

    }

    public CategoriaVO getCategoria() {
        return categoria;
    }


    public String getTipo() {
        return tipo;
    }


    public void setCategoria(CategoriaVO categoria) {
        this.categoria = categoria;
    }


    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    @Override
    public String toString() {

        return getDescricao();
    }
}
