package br.com.constran.mobile.persistence.vo.imp;

import br.com.constran.mobile.persistence.vo.AbstractVO;

public class CategoriaVO extends AbstractVO {

    private static final long serialVersionUID = 8772642371653889843L;

    public CategoriaVO(String descricao) {
        super(descricao);
    }

    public CategoriaVO(Integer id, String descricao) {
        super(id, descricao);
    }

    public CategoriaVO(Integer id) {
        super(id);
    }

    @Override
    public String toString() {

        return getDescricao();
    }
}
