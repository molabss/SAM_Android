package br.com.constran.mobile.persistence.vo.aprop.mov;

import br.com.constran.mobile.persistence.vo.AbstractVO;
import br.com.constran.mobile.persistence.vo.imp.MaterialVO;

import java.util.StringTokenizer;

public class ViagemVO extends AbstractVO {

    private static final long serialVersionUID = -943230670974420470L;
    //PK
    private Integer idApropriacao;
    private Integer idEquipamento;
    private String horaInicio;
    private String horaViagem;

    //FK
    private MaterialVO material;

    private Integer idEquipamentoCarga;
    private String dataHoraAtualizacao;

    //Novos campos para o Formulario com QRCode - FICHA_MOTORISTA
    private String eticket;
    private Integer codSeguranca;
    private Integer nroQRCode;
    private Integer nroFormulario;

    //Novos campos para o Formulario com QRCode - FICHA_3_VIAS
    private String nroFicha;

    private String horimetro;
    private String hodometro;
    private String apropriar;
    private String observacoes;
    private String dataHoraCadastro;
    private String peso;
    private String prcCarga;
    private String estacaIni;
    private String estacaFim;
    private String tipo;

    public ViagemVO() {

    }

    public ViagemVO(String id, String token) {

        if (id != null) {

            StringTokenizer st = new StringTokenizer(id, token);

            this.idApropriacao = Integer.parseInt(st.nextToken());
            this.idEquipamento = Integer.parseInt(st.nextToken());
            this.horaInicio = st.nextToken();
            this.horaViagem = st.nextToken();
        }

        this.strId = id;
    }

    @Override
    public String toString() {
        return getDescricao();
    }

    public String getNroFicha() {
        return nroFicha;
    }

    public void setNroFicha(String nroFicha) {
        this.nroFicha = nroFicha;
    }

    public Integer getIdApropriacao() {
        return idApropriacao;
    }

    public void setIdApropriacao(Integer idApropriacao) {
        this.idApropriacao = idApropriacao;
    }

    public Integer getIdEquipamento() {
        return idEquipamento;
    }

    public void setIdEquipamento(Integer idEquipamento) {
        this.idEquipamento = idEquipamento;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public MaterialVO getMaterial() {
        return material;
    }

    public void setMaterial(MaterialVO material) {
        this.material = material;
    }

    public String getPrcCarga() {
        return prcCarga;
    }

    public void setPrcCarga(String prcCarga) {
        this.prcCarga = prcCarga;
    }

    public String getEstacaIni() {
        return estacaIni;
    }

    public void setEstacaIni(String estacaIni) {
        this.estacaIni = estacaIni;
    }

    public String getEstacaFim() {
        return estacaFim;
    }

    public void setEstacaFim(String estacaFim) {
        this.estacaFim = estacaFim;
    }

    public String getHoraViagem() {
        return horaViagem;
    }

    public void setHoraViagem(String horaViagem) {
        this.horaViagem = horaViagem;
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

    public String getDataHoraAtualizacao() {
        return dataHoraAtualizacao;
    }

    public void setDataHoraAtualizacao(String dataHoraAtualizacao) {
        this.dataHoraAtualizacao = dataHoraAtualizacao;
    }

    public String getDataHoraCadastro() {
        return dataHoraCadastro;
    }

    public void setDataHoraCadastro(String dataHoraCadastro) {
        this.dataHoraCadastro = dataHoraCadastro;
    }

    public String getEticket() {
        return eticket;
    }

    public void setEticket(String eticket) {
        this.eticket = eticket;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getApropriar() {
        return apropriar;
    }

    public Integer getIdEquipamentoCarga() {
        return idEquipamentoCarga;
    }

    public void setIdEquipamentoCarga(Integer idEquipamentoCarga) {
        this.idEquipamentoCarga = idEquipamentoCarga;
    }

    public void setApropriar(String apropriar) {
        this.apropriar = apropriar;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Integer getNroQRCode() {
        return nroQRCode;
    }

    public void setNroQRCode(Integer nroQRCode) {
        this.nroQRCode = nroQRCode;
    }

    public Integer getNroFormulario() {
        return nroFormulario;
    }

    public void setNroFormulario(Integer nroFormulario) {
        this.nroFormulario = nroFormulario;
    }

    public Integer getCodSeguranca() {
        return codSeguranca;
    }

    public void setCodSeguranca(Integer codSeguranca) {
        this.codSeguranca = codSeguranca;
    }
}
