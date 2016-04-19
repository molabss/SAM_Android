package br.com.constran.mobile.view.util;

import br.com.constran.mobile.enums.TipoModulo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Criado em 04/09/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 * A estrutura generica dos dados do QRCode é formada por 3 parametros compostos da seguinte forma:
 * <p/>
 * [token][splitToken][tipoModulo][splitToken][dados]
 */
public class QRCodeData implements Serializable {

    private static final int INDEX_TOKEN = 0;
    private static final int INDEX_TIPO_MODULO = 1;
    private static final int INDEX_DATA = 2;
    private static final int QTE_PARAMS = 3;

    private String token;
    private String splitToken;
    private TipoModulo tipoModulo;
    private String dados;
    private ArrayList<String> infos;

    /**
     * Construtor usado para obter dados genericos do QR Code
     *
     * @param splitToken
     * @param dados
     */
    public QRCodeData(String splitToken, String dados) {
        this.splitToken = splitToken;

        String[] params = null;

        if (dados != null && !dados.isEmpty() && dados.contains(token)) {
            params = dados.split(splitToken);
        }

        if (params != null && params.length >= QTE_PARAMS) {
            this.token = params[INDEX_TOKEN];
            this.tipoModulo = TipoModulo.findByCodigo(params[INDEX_TIPO_MODULO]);
            this.dados = params[INDEX_DATA];
        }
    }

    /**
     * Construtor usado para obter dados genericos do QR Code
     *
     * @param token
     * @param splitToken
     * @param dados
     */
    public QRCodeData(String token, String splitToken, String dados) {
        this.token = token;
        this.splitToken = splitToken;

        String[] params = null;

        if (dados != null && !dados.isEmpty() && dados.contains(token)) {
            params = dados.split(splitToken);
        }

        if (params != null && params.length >= QTE_PARAMS) {
            this.tipoModulo = TipoModulo.findByCodigo(params[INDEX_TIPO_MODULO]);
            this.dados = params[INDEX_DATA];
        }
    }


    public QRCodeData(String token, String splitToken, ArrayList<String> infos) {
        this.token = token;
        this.splitToken = splitToken;
        this.infos = infos;
    }

    public QRCodeData(String token, TipoModulo tipoModulo, String splitToken, ArrayList<String> infos) {
        this.token = token;
        this.tipoModulo = tipoModulo;
        this.splitToken = splitToken;
        this.infos = infos;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSplitToken() {
        return splitToken;
    }

    public void setSplitToken(String splitToken) {
        this.splitToken = splitToken;
    }

    public TipoModulo getTipoModulo() {
        return tipoModulo;
    }

    public void setTipoModulo(TipoModulo tipoModulo) {
        this.tipoModulo = tipoModulo;
    }

    public String getDados() {
        return dados;
    }

    public void setDados(String dados) {
        this.dados = dados;
    }

    public ArrayList<String> getInfos() {
        return infos;
    }

    public void setInfos(ArrayList<String> infos) {
        this.infos = infos;
    }
}
