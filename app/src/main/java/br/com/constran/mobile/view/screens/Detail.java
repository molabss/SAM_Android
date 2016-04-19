package br.com.constran.mobile.view.screens;


import android.content.Context;


public class Detail extends GridHeader {

    private String detail;

    public Detail(Context context) {

        super(context);
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}