package br.com.constran.mobile.persistence.vo.rae.abs;

import br.com.constran.mobile.persistence.vo.AbstractVO;

public class CombustivelLubrificanteVO extends AbstractVO {

    private static final long serialVersionUID = -3201643427697434100L;
    private String tipo;
    private String unidadeMedida;


    public CombustivelLubrificanteVO(Integer id, String descricao, String unidadeMedida, String tipo) {
        super(id, descricao);

        this.tipo = tipo;
        this.unidadeMedida = unidadeMedida;
    }

    public CombustivelLubrificanteVO(Integer id) {
        super(id);
    }

    public String getTipo() {
        return tipo;
    }


    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    @Override
    public String toString() {

        return getDescricao();
    }
}
