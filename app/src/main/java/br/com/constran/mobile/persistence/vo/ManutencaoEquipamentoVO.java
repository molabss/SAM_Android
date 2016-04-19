package br.com.constran.mobile.persistence.vo;

import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;

import java.util.List;

/**
 * Created by moises_santana on 12/03/2015.
 */
public class ManutencaoEquipamentoVO extends AbstractVO {

    private EquipamentoVO equipamento;
    private String data;

    //Para simplificacao do objeto JSON gerado
    private Integer idEquipamento;
    private String descricaoEquipamento;
    //----------------------------------------

    private String horaInicio;
    private String horaTermino;
    private String horimetro;
    private String hodometro;
    private String observacao;
    private List<ManutencaoEquipamentoServicosVO> servicos;


    @Override
    public String getStrId() {
        return String.valueOf(equipamento.getId())+" "+data;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }




    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }


    public EquipamentoVO getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(EquipamentoVO equipamento) {
        this.equipamento = equipamento;
    }


    public String getHoraTermino() {
        return horaTermino;
    }

    public void setHoraTermino(String horaTermino) {
        this.horaTermino = horaTermino;
    }

    public Integer getIdEquipamento() {
        return idEquipamento;
    }

    public void setIdEquipamento(Integer idEquipamento) {
        this.idEquipamento = idEquipamento;
    }

    public List<ManutencaoEquipamentoServicosVO> getServicos() {
        return servicos;
    }

    public void setServicos(List<ManutencaoEquipamentoServicosVO> servicos) {
        this.servicos = servicos;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescricaoEquipamento() {
        return descricaoEquipamento;
    }

    public void setDescricaoEquipamento(String descricaoEquipamento) {
        this.descricaoEquipamento = descricaoEquipamento;
    }

    public String getHorimetro() {
        return horimetro;
    }

    public void setHorimetro(String horimetro) {
        this.horimetro = horimetro;
    }

    public String getHodometro() {
        return hodometro;
    }

    public void setHodometro(String hodometro) {
        this.hodometro = hodometro;
    }
}
