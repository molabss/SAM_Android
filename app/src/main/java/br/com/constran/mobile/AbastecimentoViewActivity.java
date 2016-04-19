package br.com.constran.mobile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import br.com.constran.mobile.view.AbstractActivity;
import br.com.constran.mobile.view.interfaces.InterfaceEditActivity;
import br.com.constran.mobile.view.screens.Detail;
import br.com.constran.mobile.view.util.References;
import br.com.constran.mobile.view.util.Util;
//import org.apache.log4j.Logger;

public final class AbastecimentoViewActivity extends AbstractActivity implements InterfaceEditActivity {

    private TextView raeView;
    private TextView prefixoView;
    private TextView postoView;
    private TextView combustivelView;
    private TextView frenteObraView;
    private TextView atividadeView;
    private TextView operadorView;
    private TextView justificativaView;
    private TextView observacoesView;
    private TextView inicioView;
    private TextView terminoView;
    private TextView horimetroView;
    private TextView quantidadeView;
    private TextView quilometragemView;

    private TableRow rowQuilometragem;
    private TableRow rowJustificativa;
    private TableRow rowHorimetro;
    private TableRow rowOperador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.abastecimento_view);

        currentContext = AbastecimentoViewActivity.this;
        currentClass = AbastecimentoViewActivity.class;

        try {

            init();

            initAtributes();

            editValues();

            initEvents();

            editScreen();

            createDetail(getDetailValues());

        } catch (Exception e) {
            tratarExcecao(e);
        }
    }


    @Override
    public void initAtributes() throws Exception {

        raeView = (TextView) findViewById(R.id.absViewRae);
        prefixoView = (TextView) findViewById(R.id.absViewPref);
        postoView = (TextView) findViewById(R.id.absViewPosto);
        combustivelView = (TextView) findViewById(R.id.absViewComb);
        frenteObraView = (TextView) findViewById(R.id.lclViewFob);
        atividadeView = (TextView) findViewById(R.id.lclViewAtv);
        operadorView = (TextView) findViewById(R.id.absViewOper);
        justificativaView = (TextView) findViewById(R.id.absViewJus);
        observacoesView = (TextView) findViewById(R.id.absViewObs);
        inicioView = (TextView) findViewById(R.id.absViewHrIn);
        terminoView = (TextView) findViewById(R.id.absViewHrFim);
        horimetroView = (TextView) findViewById(R.id.absViewHorim);
        quantidadeView = (TextView) findViewById(R.id.absViewQtd);
        quilometragemView = (TextView) findViewById(R.id.absViewQuilm);

        rowOperador = (TableRow) findViewById(R.id.tbRwabsOperVw);
        rowHorimetro = (TableRow) findViewById(R.id.tbRwabsHrmView);
        rowQuilometragem = (TableRow) findViewById(R.id.tbRwabsKmView);
        rowJustificativa = (TableRow) findViewById(R.id.tbRwabsOperJus);
    }

    @Override
    public void editValues() {

        String[] arrayPK = Util.getArrayPK(intentParameters.getIdRegistroAtual(), getStr(R.string.TOKEN));
        String[] dados = getDAO().getAbastecimentoDAO().getViewValues(arrayPK);

        if(dados == null)return;

        raeView.setText(dados[0]);
        prefixoView.setText(dados[1]+" - "+(dados[14] == null ? "" : dados[14]));
        combustivelView.setText(dados[3]);
        postoView.setText(dados[2]);
        operadorView.setText(dados[4] == null ? "" : dados[4]);
        frenteObraView.setText(dados[5] == null ? "" : dados[5]);
        atividadeView.setText(dados[6] == null ? "" : dados[6]);
        inicioView.setText(dados[7]);
        terminoView.setText(dados[8]);
        quilometragemView.setText(dados[9]);
        horimetroView.setText(dados[10]);
        quantidadeView.setText(dados[11]);
        justificativaView.setText(dados[12] == null ? "" : dados[12]);
        observacoesView.setText(dados[13] == null ? "" : dados[13]);

        /*
        postoView.setText(dados[++i] == null ? getStr(R.string.EMPTY) : dados[i]);
        combustivelView.setText(dados[++i] == null ? getStr(R.string.EMPTY) : dados[i]);
        operadorView.setText(dados[++i] == null ? getStr(R.string.EMPTY) : dados[i]);
        frenteObraView.setText(dados[++i] == null ? getStr(R.string.EMPTY) : dados[i]);
        atividadeView.setText(dados[++i] == null ? getStr(R.string.EMPTY) : dados[i]);
        inicioView.setText(dados[++i] == null ? getStr(R.string.EMPTY) : dados[i]);
        terminoView.setText(dados[++i] == null ? getStr(R.string.EMPTY) : dados[i]);
        quilometragemView.setText(dados[++i] == null ? getStr(R.string.EMPTY) : dados[i]);
        horimetroView.setText(dados[++i] == null ? getStr(R.string.EMPTY) : dados[i]);
        quantidadeView.setText(dados[++i] == null ? getStr(R.string.EMPTY) : dados[i]);
        justificativaView.setText(dados[++i] == null ? getStr(R.string.EMPTY) : dados[i]);
        observacoesView.setText(dados[++i] == null ? getStr(R.string.EMPTY) : dados[i]);
        String descricaoEquipamento = dados[++i] == null ? getStr(R.string.EMPTY) : dados[i];
        prefixoView.setText(prefixo + " - " + descricaoEquipamento);
        */
    }


    @Override
    public void onBackPressed() {

        redirect(AbastecimentosSearchActivity.class, false);
    }


    @Override

    public Detail getDetailValues() throws Exception {

        //Texto do Detail

        String strDetail = getStr(R.string.DETAIL_ABS);

        Detail detail = new Detail(this);
        detail.setDetail(strDetail);
        detail.setColorBKG(getColor(R.color.GRAY));// Cor de fundo
        detail.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        detail.setFileLayoutRow(References.DETAIL_LAYOUT); //arquivo xml - layout (TableRow)
        detail.setIdColumns(References.DETAIL_ID_COLUMNS);// Ids (xml) das colunas
        detail.setIdTable(References.DETAIL_ID_ABS); //Id do TableLayout);

        return detail;
    }


    @Override
    public void initEvents() throws Exception {
    }


    @Override
    public void editScreen() throws Exception {

        if (horimetroView.getText().toString().trim().equals(getStr(R.string.EMPTY))) {
            rowHorimetro.setVisibility(View.GONE);
            rowHorimetro.setLayoutParams(new LayoutParams(0, 0));
        }
        if (quilometragemView.getText().toString().trim().equals(getStr(R.string.EMPTY))) {
            rowQuilometragem.setVisibility(View.GONE);
            rowQuilometragem.setLayoutParams(new LayoutParams(0, 0));
        }
        if (operadorView.getText().toString().trim().equals(getStr(R.string.EMPTY))) {
            rowOperador.setVisibility(View.GONE);
            rowOperador.setLayoutParams(new LayoutParams(0, 0));
        }
        if (justificativaView.getText().toString().trim().equals(getStr(R.string.EMPTY))) {
            rowJustificativa.setVisibility(View.GONE);
            rowJustificativa.setLayoutParams(new LayoutParams(0, 0));
        }
    }


    @Override
    public void validateFields() throws Exception {


    }


}
