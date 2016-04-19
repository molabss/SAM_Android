package br.com.constran.mobile.persistence.vo.menu;

import android.content.Context;
import br.com.constran.mobile.R;
import br.com.constran.mobile.persistence.vo.AbstractVO;

import java.text.DecimalFormat;

public class ConfiguracoesVO extends AbstractVO {

    private static final long serialVersionUID = 291783959151140339L;
    //private Context context;
    private Integer codUsuario;
    private Integer idUsuario;
    private Integer idUsuarioPessoal;
    private String nomeUsuario;
    private String nomePosto;
    private Integer idObra;
    private Integer idObra2;
    private String dispositivo;
    private String servidor;
    private String portaWeb;
    private String portaFtp;
    private String eticket;
    private Integer tolerancia;
    private String referencia;
    private Integer idPosto;
    private Double duracao;
    private String atual;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getCodUsuario() {
        return codUsuario;
    }

    public Integer getIdUsuarioPessoal() {
        return idUsuarioPessoal;
    }

    public void setCodUsuario(Integer codUsuario) {
        this.codUsuario = codUsuario;
    }

    public void setIdUsuarioPessoal(Integer idUsuarioPessoal) {
        this.idUsuarioPessoal = idUsuarioPessoal;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public Integer getIdObra() {
        return idObra;
    }

    public void setIdObra(Integer idObra) {
        this.idObra = idObra;
    }

    public String getDispositivo() {
        return dispositivo;
    }

    public Integer getIdPosto() {
        return idPosto;
    }

    public void setIdPosto(Integer idPosto) {
        this.idPosto = idPosto;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }

    public String getServidor() {
        return servidor;
    }

    public void setServidor(String servidor) {
        this.servidor = servidor;
    }

    public String getPortaWeb() {
        return portaWeb;
    }

    public void setPortaWeb(String portaWeb) {
        this.portaWeb = portaWeb;
    }

    public String getPortaFtp() {
        return portaFtp;
    }

    public void setPortaFtp(String portaFtp) {
        this.portaFtp = portaFtp;
    }

    public String getNomePosto() {
        return nomePosto;
    }

    public void setNomePosto(String nomePosto) {
        this.nomePosto = nomePosto;
    }

    public String getUrlImport(Context context) {
        return "http://" + this.servidor + ":" + this.portaWeb + context.getResources().getString(R.string.DIR_FILES) + this.idObra + context.getResources().getString(R.string.DIR_MOBILE);
    }

    public String getUrlExport(Context context) {
        return "http://" + this.servidor + ":" + this.portaWeb + context.getResources().getString(R.string.DIR_FILES) + this.idObra + context.getResources().getString(R.string.DIR_WEB);
    }

    public String getUrlExportRoot(Context context) {
        return "http://" + this.servidor + ":" + this.portaWeb + context.getResources().getString(R.string.DIR_FILES);
    }

    public String getEticket() {
        return eticket;
    }

    public void setEticket(String eticket) {
        this.eticket = eticket;
    }

    public Integer getTolerancia() {
        return tolerancia;
    }

    public void setTolerancia(Integer tolerancia) {
        this.tolerancia = tolerancia;
    }

    public String getReferencia() {
        return referencia;
    }

    public Double getDuracao() {
        return duracao;
    }

    public void setDuracao(Double duracao) {
        String format = new DecimalFormat("#0.0").format(duracao);
        this.duracao = Double.valueOf(format.replace(",", "."));
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Integer getIdObra2() {
        return idObra2;
    }

    public void setIdObra2(Integer idObra2) {
        this.idObra2 = idObra2;
    }


    public String getAtual() {
        return atual;
    }

    public void setAtual(String atual) {
        this.atual = atual;
    }
}
