package br.com.constran.mobile.persistence.vo.imp;

import br.com.constran.mobile.persistence.vo.AbstractVO;

public class PreventivaEquipamentoVO extends AbstractVO {

    private static final long serialVersionUID = 4426491266234818150L;
    //PK
    private Integer idEquipamento;
    private String data;

    private String horimetro;

    public PreventivaEquipamentoVO(Integer idEquipamento, String data, String horimetro) {

        super("Preventiva");

        this.idEquipamento = idEquipamento;
        this.horimetro = horimetro;
        this.data = data;
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


    public String getData() {
        return data;
    }


    public void setData(String data) {
        this.data = data;
    }


    public String getHorimetro() {
        return horimetro;
    }


    public void setHorimetro(String horimetro) {
        this.horimetro = horimetro;
    }


}
