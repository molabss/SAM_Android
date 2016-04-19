package br.com.constran.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import br.com.constran.mobile.R;
import br.com.constran.mobile.enums.Operacao;
import br.com.constran.mobile.persistence.vo.ObraVO;
import br.com.constran.mobile.persistence.vo.aprop.BaseVO;

import java.util.List;

/**
 * Criado em 11/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public abstract class AbstractAdapter<T extends BaseVO> extends ArrayAdapter {

    protected Context context;
    protected int layout;
    protected List<T> list;
    protected Operacao operacao;
    protected ObraVO obraVO = null;

    public AbstractAdapter(Context context, int layout, List<T> list) {
        super(context, layout, list);
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // assign the view we are converting to a local variable
        View v = view;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            v = bindGridValues(i, inflater);
        }

        preencherItensGrid(v, i);
        setGridLineColor(v, i);

        return v;
    }

    protected View bindGridValues(int i, LayoutInflater inflater) {
        return inflater.inflate(layout, null);
    }

    protected abstract void preencherItensGrid(View view, int i);

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public void remove(Object item) {
        list.remove(item);
        notifyDataSetChanged();
    }

    public Operacao getOperacao() {
        return operacao;
    }

    public void setOperacao(Operacao operacao) {
        this.operacao = operacao;
    }

    protected void setGridLineColor(View view, int i) {
        int color = context.getResources().getColor(i % 2 == 0 ? R.color.WHITE : R.color.GRAY3);
        view.setBackgroundColor(color);
    }

}
