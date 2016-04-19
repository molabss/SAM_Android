package br.com.constran.mobile.view.util;

import java.io.Serializable;

/**
 * Criado em 11/07/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class Error implements Serializable {

    private static final long serialVersionUID = -1432183745328297629L;

    private int codigo;//codigo da mensagem buscado de Resources do Android
    private String param1;
    private String param2;
    private String message;

    public Error(String message) {
        this.message = message;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
