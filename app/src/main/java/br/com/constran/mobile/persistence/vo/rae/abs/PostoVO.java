package br.com.constran.mobile.persistence.vo.rae.abs;

import br.com.constran.mobile.persistence.vo.AbstractVO;

public class PostoVO extends AbstractVO {

    private static final long serialVersionUID = 5276944889817251401L;
    private String tipo;
    private Integer idEquipamento;

    public PostoVO(Integer id, String descricao, Integer idEquipamento, String tipo) {
        super(id, descricao);
        this.tipo = tipo;
        this.idEquipamento = idEquipamento;
    }

    public PostoVO(Integer id, String descricao, String tipo) {
        super(id, descricao);
        this.tipo = tipo;
    }

    public PostoVO(Integer id, String descricao) {
        super(id, descricao);
    }

    public PostoVO(Integer id) {
        super(id);
    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {

        return getDescricao();
    }


    public Integer getIdEquipamento() {
        return idEquipamento;
    }


    public void setIdEquipamento(Integer idEquipamento) {
        this.idEquipamento = idEquipamento;
    }
}
