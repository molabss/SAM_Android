package br.com.constran.mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import br.com.constran.mobile.exception.AlertException;
import br.com.constran.mobile.persistence.vo.rae.abs.AbastecimentoPostoVO;
import br.com.constran.mobile.persistence.vo.rae.abs.PostoVO;
import br.com.constran.mobile.view.AbstractActivity;
import br.com.constran.mobile.view.interfaces.InterfaceEditActivity;
import br.com.constran.mobile.view.screens.Detail;
import br.com.constran.mobile.view.util.References;
import br.com.constran.mobile.view.util.Util;

public final class AbastecimentoPostoTransfActivity extends AbstractActivity implements InterfaceEditActivity {

    private AutoCompleteTextView postoList;

    private TextView dataHoraView, combustivelView, qtdView;

    private TextView lblOrigemDestino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.abastecimento_posto_transf);

        currentContext = AbastecimentoPostoTransfActivity.this;
        currentClass = AbastecimentoPostoTransfActivity.class;

        try {

            init();

            initAtributes();

            editValues();

            initEvents();

            editScreen();

        } catch (Exception e) {
            tratarExcecao(e);
        }
    }


    @Override
    public void initAtributes() throws Exception {

        dataHoraView = (TextView) findViewById(R.id.absTransfData);
        lblOrigemDestino = (TextView) findViewById(R.id.lblPst2);
        combustivelView = (TextView) findViewById(R.id.absTransfComb);
        qtdView = (TextView) findViewById(R.id.absTransfQtd);
        postoList = (AutoCompleteTextView) findViewById(R.id.absTransfPosto);
        btSalvar = (Button) findViewById(R.id.btAbsTransfSave);
        btCancelar = (Button) findViewById(R.id.btAbsTransfCancel);

    }

    @Override
    public void editValues() throws Exception {

        strId = intentParameters.getIdRegistroAtual();

        String[] arrayPK = Util.getArrayPK(strId, getStr(R.string.TOKEN));

        abastecimentoPosto = new AbastecimentoPostoVO(strId, getStr(R.string.TOKEN));

        postoArrayVO = getDAO().getPostoDAO().getArrayPostoVO(abastecimentoPosto.getPosto().getId());

        String[] values = getDAO().getAbastecimentoPostoDAO().getValues(arrayPK);

        dataHoraView.setText(values[0]);
        combustivelView.setText(values[1]);
        qtdView.setText(values[2]);

        if (values[3] != null) {

            abastecimentoPosto.setPosto2(new PostoVO(Integer.valueOf(values[3]), values[4]));

            postoList.setText(values[4]);
        }

        posto = abastecimentoPosto.getPosto2();

        abastecimentoPosto.setQtd(values[5]);
    }

    @Override
    public void onBackPressed() {

        redirect(AbastecimentoPostoActivity.class, true);
    }

    @Override

    public Detail getDetailValues() throws Exception {

        //Texto do Detail

        String strDetail = getStr(R.string.DETAIL_PRV);

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

        postoList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posto = (PostoVO) parent.getItemAtPosition(position);
            }

        });

        postoList.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                posto = null;
                return false;
            }
        });


        postoList.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                posto = null;
            }
        });

        btSalvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {


                    save();


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


        postoList.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                clear();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        postoList.removeTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                clear();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void clear() {

        if (posto != null) {

            posto = null;
            postoList.setText(getStr(R.string.EMPTY));
        }


    }


    @Override
    public void editScreen() throws Exception {

        hiddenKeyboard(postoList);

        ArrayAdapter<PostoVO> posto = new ArrayAdapter<PostoVO>(this, android.R.layout.select_dialog_singlechoice, postoArrayVO);
        posto.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        postoList.setAdapter(posto);

        int qtd = Integer.valueOf(abastecimentoPosto.getQtd());

        String tipo = null;

        if (this.posto != null) {

            if (qtd < 0)
                tipo = "S";
            else
                tipo = "E";
        }

        if (qtd < 0)
            lblOrigemDestino.setText(getStr(R.string.destino));

        abastecimentoPosto.setTipo(tipo);

    }

    public void save() throws Exception {

        validateFields();

        abastecimentoPosto.setPosto2(posto);

        getDAO().getAbastecimentoPostoDAO().save(abastecimentoPosto);

//		Util.viewMessage(currentContext, getStr(R.string.ALERT_SUCESS_UPDATE));
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle(getResources().getString(R.string.msg_aviso));
        dialogo.setMessage(getStr(R.string.ALERT_SUCESS_UPDATE));
        dialogo.setNeutralButton(getResources().getString(R.string.msg_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onBackPressed();
            }
        });
        dialogo.show();

    }


    @Override
    public void validateFields() throws Exception {

        if (posto == null && !getStr(R.string.EMPTY).equals(postoList.getText().toString().trim())) {

            throw new AlertException(getStr(R.string.ALERT_VALOR_INVALIDO));
        }

    }


}