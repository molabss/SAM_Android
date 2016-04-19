package br.com.constran.mobile.view.screens;

import android.content.Context;


public class GridHeader extends GridBody {

    private String[] nameColumns;
    private Integer[] idColumns;
    private Integer colorBKG;

    public GridHeader(Context context) {
        super(context);
    }

    public String[] getNameColumns() {
        return nameColumns;
    }

    public void setNameColumns(String[] nameColumns) {
        this.nameColumns = nameColumns;
    }

    public Integer[] getIdColumns() {
        return idColumns;
    }

    public void setIdColumns(Integer[] idColumns) {
        this.idColumns = idColumns;
    }

    public Integer getColorBKG() {
        return colorBKG;
    }

    public void setColorBKG(Integer colorBKG) {
        this.colorBKG = colorBKG;
    }


}