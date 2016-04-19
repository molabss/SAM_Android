package br.com.constran.mobile.persistence.vo;

import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;

import java.util.List;

/**
 * Created by moises_santana on 12/03/2015.
 */
public class ManutencaoEquipamentoServicosVO extends AbstractVO{

    private EquipamentoVO equipamento;
    private String data;
    private ManutencaoServicoPorCategoriaEquipamentoVO manutencaoServicoPorCategoriaEquipamento;
    private Integer idServicoCategoriaEquipamento;

    //Para simplificacao do objeto JSON gerado
    private Integer idEquipamento;
    private String descricaoEquipamento;
    private String descricaoServico;
    //----------------------------------------

    private String horaInicio;
    private String horaTermino;
    private String observacao;


    public Integer getIdServicoCategoriaEquipamento() {
        return idServicoCategoriaEquipamento;
    }

    public void setIdServicoCategoriaEquipamento(Integer idServicoCategoriaEquipamento) {
        this.idServicoCategoriaEquipamento = idServicoCategoriaEquipamento;
    }

    public ManutencaoServicoPorCategoriaEquipamentoVO getManutencaoServicoPorCategoriaEquipamento() {
        return manutencaoServicoPorCategoriaEquipamento;
    }

    public void setManutencaoServicoPorCategoriaEquipamento(ManutencaoServicoPorCategoriaEquipamentoVO manutencaoServicoPorCategoriaEquipamento) {
        this.manutencaoServicoPorCategoriaEquipamento = manutencaoServicoPorCategoriaEquipamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getHoraTermino() {
        return horaTermino;
    }

    public void setHoraTermino(String horaTermino) {
        this.horaTermino = horaTermino;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public EquipamentoVO getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(EquipamentoVO equipamento) {
        this.equipamento = equipamento;
    }

    public Integer getIdEquipamento() {
        return idEquipamento;
    }

    public void setIdEquipamento(Integer idEquipamento) {
        this.idEquipamento = idEquipamento;
    }

    public String getDescricaoEquipamento() {
        return descricaoEquipamento;
    }

    public void setDescricaoEquipamento(String descricaoEquipamento) {
        this.descricaoEquipamento = descricaoEquipamento;
    }

    public String getDescricaoServico() {
        return descricaoServico;
    }

    public void setDescricaoServico(String descricaoServico) {
        this.descricaoServico = descricaoServico;
    }
}
