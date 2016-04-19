package br.com.constran.mobile.persistence.vo.imp;

import br.com.constran.mobile.persistence.vo.AbstractVO;

public class ComponenteVO extends AbstractVO {

    private static final long serialVersionUID = -8966214226716906604L;

    private CategoriaVO categoria;

    public ComponenteVO(String descricao) {
        super(descricao);
    }

    public ComponenteVO(Integer id, String descricao, Integer idCategoria) {
        super(id, descricao);
        this.categoria = new CategoriaVO(idCategoria);
    }

    public ComponenteVO(Integer id) {
        super(id);
    }

    @Override
    public String toString() {

        return getDescricao();
    }

    public CategoriaVO getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaVO categoria) {
        this.categoria = categoria;
    }
}
