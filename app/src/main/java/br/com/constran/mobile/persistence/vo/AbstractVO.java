package br.com.constran.mobile.persistence.vo;

import java.io.Serializable;

public abstract class AbstractVO implements Serializable {

    private static final long serialVersionUID = -3940076902701284050L;

    private Integer id;
    private String descricao;
    protected String strId;

    public AbstractVO() {
    }

    public AbstractVO(Integer id, String descricao) {

        this.descricao = descricao;
        this.id = id;
    }

    public AbstractVO(Integer id) {

        this.id = id;
    }

    public AbstractVO(String descricao) {

        this.descricao = descricao;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }


    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }
}
