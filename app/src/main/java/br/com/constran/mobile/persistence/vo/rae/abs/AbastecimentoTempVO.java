package br.com.constran.mobile.persistence.vo.rae.abs;

import br.com.constran.mobile.persistence.vo.AbstractVO;
import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;
import br.com.constran.mobile.persistence.vo.imp.UsuarioVO;

public class AbastecimentoTempVO extends AbstractVO {

    private static final long serialVersionUID = 6473590562242603590L;

    private EquipamentoVO equipamento;
    private UsuarioVO operador;

    private String dataHora;
    private String horimetro;
    private String quilometragem;

    public AbastecimentoTempVO() {

    }

    @Override
    public String toString() {
        return getDescricao();
    }

    public EquipamentoVO getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(EquipamentoVO equipamento) {
        this.equipamento = equipamento;
    }

    public UsuarioVO getOperador() {
        return operador;
    }

    public void setOperador(UsuarioVO operador) {
        this.operador = operador;
    }

    public String getHorimetro() {
        return horimetro;
    }

    public void setHorimetro(String horimetro) {
        this.horimetro = horimetro;
    }

    public String getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(String quilometragem) {
        this.quilometragem = quilometragem;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }


}
