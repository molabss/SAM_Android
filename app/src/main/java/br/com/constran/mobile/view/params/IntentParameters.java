package br.com.constran.mobile.view.params;

import br.com.constran.mobile.persistence.vo.imp.UsuarioVO;

import java.io.Serializable;

/**
 * Classe que encapsula os parametros utilizados durante o redirecionamento das telas
 */
public class IntentParameters implements Serializable {

    private static final long serialVersionUID = -1251072046737835535L;

    private String menu;
    private String idRegistroPai;
    private String idRegistroAtual;
    private String idRegistroCopia;

    private UsuarioVO userSession;

    private Integer idEquipamento;

    private String filtroData;
    private Integer filtroEquipamento;
    private Integer filtroMaterial;
    private String filtroEstaca;

    private boolean fromQRCode;

    public IntentParameters() {

    }

    public IntentParameters(String menu, UsuarioVO userSession) {
        this.menu = menu;
        this.userSession = userSession;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getIdRegistroPai() {
        return idRegistroPai;
    }

    public void setIdRegistroPai(String idRegistroPai) {
        this.idRegistroPai = idRegistroPai;
    }

    public String getIdRegistroAtual() {
        return idRegistroAtual;
    }

    public void setIdRegistroAtual(String idRegistroAtual) {
        this.idRegistroAtual = idRegistroAtual;
    }

    public String getIdRegistroCopia() {
        return idRegistroCopia;
    }

    public void setIdRegistroCopia(String idRegistroCopia) {
        this.idRegistroCopia = idRegistroCopia;
    }

    public UsuarioVO getUserSession() {
        return userSession;
    }

    public void setUserSession(UsuarioVO userSession) {
        this.userSession = userSession;
    }

    public Integer getIdEquipamento() {
        return idEquipamento;
    }

    public void setIdEquipamento(Integer idEquipamento) {
        this.idEquipamento = idEquipamento;
    }

    public String getFiltroData() {
        return filtroData;
    }

    public void setFiltroData(String filtroData) {
        this.filtroData = filtroData;
    }

    public Integer getFiltroEquipamento() {
        return filtroEquipamento;
    }

    public void setFiltroEquipamento(Integer filtroEquipamento) {
        this.filtroEquipamento = filtroEquipamento;
    }

    public Integer getFiltroMaterial() {
        return filtroMaterial;
    }

    public void setFiltroMaterial(Integer filtroMaterial) {
        this.filtroMaterial = filtroMaterial;
    }

    public String getFiltroEstaca() {
        return filtroEstaca;
    }

    public void setFiltroEstaca(String filtroEstaca) {
        this.filtroEstaca = filtroEstaca;
    }

    public boolean isFromQRCode() {
        return fromQRCode;
    }

    public void setFromQRCode(boolean fromQRCode) {
        this.fromQRCode = fromQRCode;
    }

}
