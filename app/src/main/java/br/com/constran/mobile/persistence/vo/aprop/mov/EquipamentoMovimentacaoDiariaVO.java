package br.com.constran.mobile.persistence.vo.aprop.mov;

import br.com.constran.mobile.persistence.vo.AbstractVO;


public class EquipamentoMovimentacaoDiariaVO extends AbstractVO {

    private static final long serialVersionUID = 7846685387796769064L;
    private Integer idEquipamento;
    private String dataHora;

    public EquipamentoMovimentacaoDiariaVO() {

    }

    public EquipamentoMovimentacaoDiariaVO(Integer id) {
        super(id);
    }

    public Integer getIdEquipamento() {
        return idEquipamento;
    }

    public void setIdEquipamento(Integer idEquipamento) {
        this.idEquipamento = idEquipamento;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public String toString() {
        return getDescricao();
    }
}


