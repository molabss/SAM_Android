package br.com.constran.mobile.view.screens;

import android.content.Context;


public class GridFooter extends GridHeader {

    private int numRecords;

    public GridFooter(Context context) {
        super(context);
    }

    public int getNumRecords() {
        return numRecords;
    }

    public void setNumRecords(int numRecords) {
        this.numRecords = numRecords;
    }
}