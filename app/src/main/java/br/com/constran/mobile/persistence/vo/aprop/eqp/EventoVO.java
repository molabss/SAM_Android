package br.com.constran.mobile.persistence.vo.aprop.eqp;

import br.com.constran.mobile.persistence.vo.AbstractVO;
import br.com.constran.mobile.persistence.vo.imp.ComponenteVO;
import br.com.constran.mobile.persistence.vo.imp.ParalisacaoVO;
import br.com.constran.mobile.persistence.vo.imp.ServicoVO;

import java.util.StringTokenizer;

public class EventoVO extends AbstractVO {

    private static final long serialVersionUID = -3339328257764730815L;
    //PK
    private Integer idApropriacao;
    private Integer idEquipamento;
    private String dataHora;
    private String horaInicio;

    private String apropriar;
    private String observacoes;
    private String estaca;

    private ServicoVO servico;
    private ComponenteVO componente;
    private ParalisacaoVO paralisacao;
    private String horaTermino;

    private String dataHoraCadastro;
    private String dataHoraAtualizacao;

    public EventoVO() {

    }

    public EventoVO(String id, String token) {

        if (id != null) {

            StringTokenizer st = new StringTokenizer(id, token);

            this.idApropriacao = Integer.parseInt(st.nextToken());
            this.idEquipamento = Integer.parseInt(st.nextToken());
            this.dataHora = st.nextToken();
            this.horaInicio = st.nextToken();
        }

        this.strId = id;
    }


    @Override
    public String toString() {
        return getDescricao();
    }


    public Integer getIdApropriacao() {
        return idApropriacao;
    }

    public Integer getIdEquipamento() {
        return idEquipamento;
    }

    public String getDataHora() {
        return dataHora;
    }

    public String getApropriar() {
        return apropriar;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public String getEstaca() {
        return estaca;
    }

    public ServicoVO getServico() {
        return servico;
    }

    public ComponenteVO getComponente() {
        return componente;
    }

    public ParalisacaoVO getParalisacao() {
        return paralisacao;
    }

    public void setIdApropriacao(Integer idApropriacao) {
        this.idApropriacao = idApropriacao;
    }

    public void setIdEquipamento(Integer idEquipamento) {
        this.idEquipamento = idEquipamento;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public void setApropriar(String apropriar) {
        this.apropriar = apropriar;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public void setEstaca(String estaca) {
        this.estaca = estaca;
    }

    public void setServico(ServicoVO servico) {
        this.servico = servico;
    }

    public void setComponente(ComponenteVO componente) {
        this.componente = componente;
    }

    public void setParalisacao(ParalisacaoVO paralisacao) {
        this.paralisacao = paralisacao;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public String getHoraTermino() {
        return horaTermino;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setHoraTermino(String horaTermino) {
        this.horaTermino = horaTermino;
    }

    public String getDataHoraCadastro() {
        return dataHoraCadastro;
    }

    public String getDataHoraAtualizacao() {
        return dataHoraAtualizacao;
    }

    public void setDataHoraCadastro(String dataHoraCadastro) {
        this.dataHoraCadastro = dataHoraCadastro;
    }

    public void setDataHoraAtualizacao(String dataHoraAtualizacao) {
        this.dataHoraAtualizacao = dataHoraAtualizacao;
    }


}
