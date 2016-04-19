package br.com.constran.mobile.persistence.vo.imp;

import br.com.constran.mobile.persistence.vo.AbstractVO;

public class MaterialVO extends AbstractVO {

    private static final long serialVersionUID = -6822093449687722517L;
    private CategoriaVO categoria;

    public MaterialVO(String descricao) {
        super(descricao);
    }

    public MaterialVO(Integer id, String descricao) {
        super(id, descricao);
    }

    public MaterialVO(Integer id, String descricao, Integer idCategoria, String descricaoCategoria) {
        super(id, descricao);
        this.categoria = new CategoriaVO(idCategoria, descricaoCategoria);
    }

    public MaterialVO(Integer id) {
        super(id);
    }

    public CategoriaVO getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaVO categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {

        return getDescricao();
    }
}
