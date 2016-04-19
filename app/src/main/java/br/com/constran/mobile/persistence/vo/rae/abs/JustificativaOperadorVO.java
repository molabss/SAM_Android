package br.com.constran.mobile.persistence.vo.rae.abs;

import br.com.constran.mobile.persistence.vo.AbstractVO;

public class JustificativaOperadorVO extends AbstractVO {

    private static final long serialVersionUID = 3971735025182634304L;

    public JustificativaOperadorVO(String descricao) {
        super(descricao);
    }

    public JustificativaOperadorVO(Integer id, String descricao) {
        super(id, descricao);
    }

    public JustificativaOperadorVO(Integer id) {
        super(id);
    }

    @Override
    public String toString() {

        return getDescricao();
    }
}
