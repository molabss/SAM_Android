package br.com.constran.mobile.persistence.vo.imp;

import br.com.constran.mobile.persistence.vo.AbstractVO;

public class FrenteObraVO extends AbstractVO {

    private static final long serialVersionUID = 853846413830635767L;
    private Integer idObra;

    public FrenteObraVO(Integer id, Integer idObra, String name) {
        super(id, name);

        this.idObra = idObra;
    }

    public FrenteObraVO() {

    }

    public FrenteObraVO(Integer id) {
        super(id);
    }

    public FrenteObraVO(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return getDescricao();
    }

    public Integer getIdObra() {
        return idObra;
    }

    public void setIdObra(Integer idObra) {
        this.idObra = idObra;
    }


}
