package br.com.constran.mobile.persistence.vo.imp;

import br.com.constran.mobile.persistence.vo.AbstractVO;

public class ServicoVO extends AbstractVO {

    private static final long serialVersionUID = -3856662916964487555L;
    private CategoriaVO categoria;
    private Integer tipoServico;
    private String unidadeMedida;

    public ServicoVO(String descricao) {
        super(descricao);
    }

    public ServicoVO(Integer id, String descricao, Integer idCategoria) {
        super(id, descricao);
        this.categoria = new CategoriaVO(idCategoria);
    }

    public ServicoVO(Integer id) {
        super(id);
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

    public CategoriaVO getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaVO categoria) {
        this.categoria = categoria;
    }

    public Integer getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(Integer tipoServico) {
        this.tipoServico = tipoServico;
    }
}
