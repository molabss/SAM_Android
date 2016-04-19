package br.com.constran.mobile.persistence.vo.aprop.eqp;

import br.com.constran.mobile.persistence.vo.AbstractVO;
import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;


public class EquipamentoParteDiariaVO extends AbstractVO {

    private static final long serialVersionUID = -465080183041424175L;
    private EquipamentoVO equipamento;
    private String dataHora;
    private Integer idApropriacao;

    public EquipamentoParteDiariaVO() {

    }

    public EquipamentoParteDiariaVO(Integer id) {
        super(id);
    }


    public EquipamentoVO getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(EquipamentoVO equipamento) {
        this.equipamento = equipamento;
    }


    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public Integer getIdApropriacao() {
        return idApropriacao;
    }

    public void setIdApropriacao(Integer idApropriacao) {
        this.idApropriacao = idApropriacao;
    }

    @Override
    public String toString() {
        return getDescricao();
    }
}


