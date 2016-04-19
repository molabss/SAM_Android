package br.com.constran.mobile.persistence.vo.aprop.mov;

import br.com.constran.mobile.enums.TipoModulo;

/**
 * Created by mateus_vitali on 16/10/2014.
 */
public class FormMovimentacaoVO {

    private TipoModulo tipoModulo;
    private String eTicketCarga;
    private Integer codSegurancaCarga;
    private String eTicketDescarga;
    private Integer codSegurancaDescarga;
    private Integer nroQRCode;
    private Integer nroFormulario;
    private String nroFicha;

    public TipoModulo getTipoModulo() {
        return tipoModulo;
    }

    public void setTipoModulo(TipoModulo tipoModulo) {
        this.tipoModulo = tipoModulo;
    }


    public String geteTicketCarga() {
        return eTicketCarga;
    }

    public void seteTicketCarga(String eTicketCarga) {
        this.eTicketCarga = eTicketCarga;
    }

    public Integer getCodSegurancaCarga() {
        return codSegurancaCarga;
    }

    public void setCodSegurancaCarga(Integer codSegurancaCarga) {
        this.codSegurancaCarga = codSegurancaCarga;
    }

    public String geteTicketDescarga() {
        return eTicketDescarga;
    }

    public void seteTicketDescarga(String eTicketDescarga) {
        this.eTicketDescarga = eTicketDescarga;
    }

    public Integer getCodSegurancaDescarga() {
        return codSegurancaDescarga;
    }

    public void setCodSegurancaDescarga(Integer codSegurancaDescarga) {
        this.codSegurancaDescarga = codSegurancaDescarga;
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

    public String getNroFicha() {
        return nroFicha;
    }

    public void setNroFicha(String nroFicha) {
        this.nroFicha = nroFicha;
    }
}
