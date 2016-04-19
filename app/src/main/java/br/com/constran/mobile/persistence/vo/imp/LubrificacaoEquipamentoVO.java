package br.com.constran.mobile.persistence.vo.imp;

import br.com.constran.mobile.persistence.vo.AbstractVO;

public class LubrificacaoEquipamentoVO extends AbstractVO {

    private static final long serialVersionUID = 2291262520757720426L;

    public LubrificacaoEquipamentoVO(Integer idEquipamento, String data, Integer idCompartimento, Integer idCategoria, String horimetro, String quilometragem) {

        this.idEquipamento = idEquipamento;
        this.idCompartimento = idCompartimento;
        this.idCategoria = idCategoria;
        this.data = data;
        this.horimetro = horimetro;
        this.quilometragem = quilometragem;

    }

    //PK
    private Integer idEquipamento;
    private Integer idCompartimento;
    private Integer idCategoria;

    private String data;
    private String horimetro;
    private String quilometragem;


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


    public Integer getIdCompartimento() {
        return idCompartimento;
    }


    public void setIdCompartimento(Integer idCompartimento) {
        this.idCompartimento = idCompartimento;
    }


    public Integer getIdCategoria() {
        return idCategoria;
    }


    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
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


    public String getQuilometragem() {
        return quilometragem;
    }


    public void setQuilometragem(String quilometragem) {
        this.quilometragem = quilometragem;
    }


}
