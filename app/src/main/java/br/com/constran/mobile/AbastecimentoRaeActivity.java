package br.com.constran.mobile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import br.com.constran.mobile.persistence.vo.rae.RaeVO;
import br.com.constran.mobile.persistence.vo.rae.abs.PostoVO;
import br.com.constran.mobile.view.AbstractActivity;
import br.com.constran.mobile.view.interfaces.InterfaceEditActivity;
import br.com.constran.mobile.view.screens.Detail;
import br.com.constran.mobile.view.util.References;
import br.com.constran.mobile.view.util.Util;

import java.util.Date;

public final class AbastecimentoRaeActivity extends AbstractActivity implements InterfaceEditActivity {

    private EditText totalizadorInicial;
    private EditText totalizadorFinal;

    private TextView raeView;
    private TextView postoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.abastecimento_rae);

        currentContext = AbastecimentoRaeActivity.this;
        currentClass = AbastecimentoRaeActivity.class;

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
        raeView = (TextView) findViewById(R.id.absAuthRae);
        postoView = (TextView) findViewById(R.id.absAuthPosto);

        totalizadorInicial = (EditText) findViewById(R.id.absAuthHorim);
        totalizadorFinal = (EditText) findViewById(R.id.absAuthQuilm);

        btSalvar = (Button) findViewById(R.id.btAbsAuthSave);
        btCancelar = (Button) findViewById(R.id.btAbsAuthCancel);
    }

    @Override
    public void editValues() throws Exception {

        config = getDAO().getConfiguracoesDAO().getConfiguracaoVO();

        posto = new PostoVO(config.getIdPosto(), config.getNomePosto());

        postoView.setText(posto.getDescricao());

        dataHora = Util.getDateFormated(new Date());

        raeView.setText(dataHora);

        String[] values = getDAO().getRaeDAO().getValues(new String[]{dataHora, String.valueOf(posto.getId())});

        if (values != null) {
            totalizadorInicial.setText(values[0] == null ? getStr(R.string.EMPTY) : values[0]);
            totalizadorFinal.setText(values[1] == null ? getStr(R.string.EMPTY) : values[1]);
        }
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
        btSalvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    validateFields();
                    saveRae();
                    redirect(AbastecimentosSearchActivity.class, false);
                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }
        });

        btCancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
    }

    @Override
    public void editScreen() throws Exception {
        hiddenKeyboard(totalizadorInicial, totalizadorFinal);
    }

    @Override
    public void validateFields() throws Exception {
    }

    public void saveRae() throws Exception {
        RaeVO raeVO = new RaeVO(dataHora);
        raeVO.setPosto(posto);
        raeVO.setTotalizadorInicial(totalizadorInicial.getText().toString());
        raeVO.setTotalizadorFinal(totalizadorFinal.getText().toString());
        Integer idRae = getDAO().getRaeDAO().getIdRae(raeVO);
        raeVO.setId(idRae);
        getDAO().getRaeDAO().save(raeVO);
    }

}