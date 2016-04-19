package br.com.constran.mobile.persistence.vo.aprop.eqp;

import br.com.constran.mobile.persistence.vo.AbstractVO;
import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

public class ApropriacaoEquipamentoVO extends AbstractVO {

    //PK
    private Integer idApropriacao;
    private EquipamentoVO equipamento;
    private String dataHora;

    private String horimetroIni;
    private String horimetroFim;
    private String operador1;
    private String producao;
    private String operador2;
    private String observacoes;

    private Collection<EventoVO> eventos;

    public ApropriacaoEquipamentoVO() {

    }

    public ApropriacaoEquipamentoVO(String id, String token) {

        if (id != null) {

            StringTokenizer st = new StringTokenizer(id, token);

            this.idApropriacao = Integer.parseInt(st.nextToken());
            this.equipamento = new EquipamentoVO(Integer.parseInt(st.nextToken()));
            this.dataHora = st.nextToken();
        }

        this.strId = id;

        this.eventos = new ArrayList<EventoVO>();

    }

    @Override
    public String toString() {
        return getDescricao();
    }

    public Integer getIdApropriacao() {
        return idApropriacao;
    }

    public void setIdApropriacao(Integer idApropriacao) {
        this.idApropriacao = idApropriacao;
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

    public String getHorimetroIni() {
        return horimetroIni;
    }

    public void setHorimetroIni(String horimetroIni) {
        this.horimetroIni = horimetroIni;
    }

    public String getHorimetroFim() {
        return horimetroFim;
    }

    public void setHorimetroFim(String horimetroFim) {
        this.horimetroFim = horimetroFim;
    }

    public String getOperador1() {
        return operador1;
    }

    public void setOperador1(String operador1) {
        this.operador1 = operador1;
    }

    public String getProducao() {
        return producao;
    }

    public void setProducao(String producao) {
        this.producao = producao;
    }

    public String getOperador2() {
        return operador2;
    }

    public void setOperador2(String operador2) {
        this.operador2 = operador2;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Collection<EventoVO> getEventos() {
        return eventos;
    }

    public void setEventos(Collection<EventoVO> eventos) {
        this.eventos = eventos;
    }

}
