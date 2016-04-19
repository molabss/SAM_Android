package br.com.constran.mobile.view.screens;

import android.content.Context;
import android.database.Cursor;

import java.util.List;

public class GridBody {

    protected Context context;

    private boolean paintSelected, validate;

    private Class<?> classEdit, classRefresh, classRedirect, classList, classVO;

    private Cursor cursor;

    private Integer idTable, fileLayoutRow, colorTXT, indexValidate, indexColumnNoShow, indexValueNoShow;

    private String[] typesColumn;

    private List<String[]> values;

    private String typeValidate, msgNotRemove, msgAuthRemove, valueNoShow;

    private Integer[] columnsTxt, referencesImage, idsXmlColumn, indexsPKRow, colorsBKG, indexColumnsNoRepeat;

    public GridBody(Context context) {
        this.context = context;
    }

    public Integer[] getIndexColumnsNoRepeat() {
        return indexColumnsNoRepeat;
    }

    public void setIndexColumnsNoRepeat(Integer[] indexColumnsNoRepeat) {
        this.indexColumnsNoRepeat = indexColumnsNoRepeat;
    }

    public Context getContext() {
        return context;
    }

    public Integer getIndexColumnNoShow() {
        return indexColumnNoShow;
    }

    public void setIndexColumnNoShow(Integer indexColumnNoShow) {
        this.indexColumnNoShow = indexColumnNoShow;
    }

    public Integer getIndexValueNoShow() {
        return indexValueNoShow;
    }

    public void setIndexValueNoShow(Integer indexValueNoShow) {
        this.indexValueNoShow = indexValueNoShow;
    }

    public String getValueNoShow() {
        return valueNoShow;
    }

    public void setValueNoShow(String valueNoShow) {
        this.valueNoShow = valueNoShow;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Class<?> getClassRedirect() {
        return classRedirect;
    }

    public void setClassRedirect(Class<?> classRedirect) {
        this.classRedirect = classRedirect;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public String[] getTypesColumn() {
        return typesColumn;
    }

    public void setTypesColumn(String[] typesColumn) {
        this.typesColumn = typesColumn;
    }

    public Integer[] getColumnsTxt() {
        return columnsTxt;
    }

    public void setColumnsTxt(Integer[] columnsTxt) {
        this.columnsTxt = columnsTxt;
    }

    public Integer[] getReferencesImage() {
        return referencesImage;
    }

    public void setReferencesImage(Integer[] referencesImage) {
        this.referencesImage = referencesImage;
    }

    public Integer[] getIdsXmlColumn() {
        return idsXmlColumn;
    }

    public void setIdsXmlColumn(Integer[] idsXmlColumn) {
        this.idsXmlColumn = idsXmlColumn;
    }

    public Integer[] getIndexsPKRow() {
        return indexsPKRow;
    }

    public String getMsgAuthRemove() {
        return msgAuthRemove;
    }

    public void setMsgAuthRemove(String msgAuthRemove) {
        this.msgAuthRemove = msgAuthRemove;
    }

    public Class<?> getClassRefresh() {
        return classRefresh;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public void setClassRefresh(Class<?> classRefresh) {
        this.classRefresh = classRefresh;
    }

    public void setIndexsPKRow(Integer[] indexsPKRow) {
        this.indexsPKRow = indexsPKRow;
    }

    public Integer[] getColorsBKG() {
        return colorsBKG;
    }

    public List<String[]> getValues() {
        return values;
    }

    public void setValues(List<String[]> values) {
        this.values = values;
    }

    public void setColorsBKG(Integer[] colorsBKG) {
        this.colorsBKG = colorsBKG;
    }

    public Integer getColorTXT() {
        return colorTXT;
    }

    public String getMsgNotRemove() {
        return msgNotRemove;
    }

    public void setMsgNotRemove(String msgNotRemove) {
        this.msgNotRemove = msgNotRemove;
    }

    public String getTypeValidate() {
        return typeValidate;
    }

    public void setTypeValidate(String typeValidate) {
        this.typeValidate = typeValidate;
    }

    public Integer getIdTable() {
        return idTable;
    }


    public void setIdTable(Integer idTable) {
        this.idTable = idTable;
    }

    public Integer getFileLayoutRow() {
        return fileLayoutRow;
    }

    public void setFileLayoutRow(Integer fileLayoutRow) {
        this.fileLayoutRow = fileLayoutRow;
    }

    public void setColorTXT(Integer colorTXT) {
        this.colorTXT = colorTXT;
    }

    public Class<?> getClassList() {
        return classList;
    }

    public Integer getIndexValidate() {
        return indexValidate;
    }

    public void setIndexValidate(Integer indexValidate) {
        this.indexValidate = indexValidate;
    }

    public void setClassList(Class<?> classList) {
        this.classList = classList;
    }

    public Class<?> getClassEdit() {
        return classEdit;
    }

    public Class<?> getClassVO() {
        return classVO;
    }

    public void setClassEdit(Class<?> classEdit) {
        this.classEdit = classEdit;
    }

    public void setClassVO(Class<?> classVO) {
        this.classVO = classVO;
    }

    public boolean isPaintSelected() {
        return paintSelected;
    }

    public void setPaintSelected(boolean paintSelected) {
        this.paintSelected = paintSelected;
    }


}