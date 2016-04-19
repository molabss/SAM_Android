package br.com.constran.mobile.persistence.vo.rae.abs;

import br.com.constran.mobile.persistence.vo.AbstractVO;

import java.util.StringTokenizer;

public class LubrificacaoDetalheVO extends AbstractVO {

    private static final long serialVersionUID = -3287305694789776554L;
    //PK
    private Integer idRae;
    private Integer idEquipamento;
    private Integer idCombustivelLubrificante;
    private String horaInicio;
    private CompartimentoVO compartimento;

    private String qtd;
    private String observacao;

    public LubrificacaoDetalheVO() {

    }

    public LubrificacaoDetalheVO(String id, String token) {

        if (id != null) {

            StringTokenizer st = new StringTokenizer(id, token);

            this.idRae = Integer.parseInt(st.nextToken());
            this.idEquipamento = Integer.parseInt(st.nextToken());
            this.idCombustivelLubrificante = Integer.parseInt(st.nextToken());
            this.horaInicio = st.nextToken();
            this.compartimento = new CompartimentoVO(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));

        }

        this.strId = id;
    }

    @Override
    public String toString() {
        return getDescricao();
    }

    public Integer getIdRae() {
        return idRae;
    }

    public Integer getIdEquipamento() {
        return idEquipamento;
    }

    public Integer getIdCombustivelLubrificante() {
        return idCombustivelLubrificante;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public CompartimentoVO getCompartimento() {
        return compartimento;
    }

    public String getQtd() {
        return qtd;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setIdRae(Integer idRae) {
        this.idRae = idRae;
    }

    public void setIdEquipamento(Integer idEquipamento) {
        this.idEquipamento = idEquipamento;
    }

    public void setIdCombustivelLubrificante(Integer idCombustivelLubrificante) {
        this.idCombustivelLubrificante = idCombustivelLubrificante;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setCompartimento(CompartimentoVO compartimento) {
        this.compartimento = compartimento;
    }

    public void setQtd(String qtd) {
        this.qtd = qtd;
    }


    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

}
