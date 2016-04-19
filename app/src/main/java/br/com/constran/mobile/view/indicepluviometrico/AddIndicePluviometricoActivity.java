package br.com.constran.mobile.view.indicepluviometrico;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.constran.mobile.*;
import br.com.constran.mobile.exception.AlertException;
import br.com.constran.mobile.persistence.dao.DAOFactory;
import br.com.constran.mobile.persistence.vo.aprop.indpluv.IndicePluviometricoVO;
import br.com.constran.mobile.persistence.vo.menu.ConfiguracoesVO;
import br.com.constran.mobile.view.params.IntentParameters;
import br.com.constran.mobile.view.util.Util;

/**
 * @author Mateus Vitali <mateus.vitali@constran.com.br>
 * @version 1.0
 * @since 2014-09-01
 */
public class AddIndicePluviometricoActivity extends Activity {

    private Button btnSalvar;
    private Button btnCancelar;
    private TextView dataHoraTextView;
    private EditText estacaInicialEditText;
    private EditText estacaFinalEditText;
    private EditText pluviometroEditText;
    private EditText volumeChuvaEditText;

    private boolean novoRegistro;

    //usado para integrar com modulos antigos na navegacao do menu
    protected IntentParameters intentParameters;

    private IndicePluviometricoVO indicePluviometricoSelecionado;

    static final String PARAM_PLUVIOMETRO_SELECIONADO = "indicePluviometricoSelecionda";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_indice_pluviometrico);
        initAttributes();
        bindComponents();
        addListeners();
        initAdapters();

        ajustaCorBackground();
        intentParameters = (IntentParameters) getIntent().getSerializableExtra(getStr(R.string.STRING_INTENT_PARAMS));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu_aux, menu);
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.plu).setVisible(false);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.mov: //movimentacao
                    checkLocal(); // Verifica se a localização foi informada.
                    checkUserLogged(); // Verifica se o usuário foi informado.
                    redirect(EquipamentosMovimentacaoDiariaActivity.class, R.string.OPTION_MENU_MOV);
                    break;
                case R.id.eqp: //equipamento
                    checkLocal(); // Verifica se a localização foi informada.
                    checkUserLogged(); // Verifica se o usuário foi informado.
                    redirect(EquipamentosParteDiariaActivity.class, R.string.OPTION_MENU_EQP);
                    break;
                case R.id.abs://abastecimento
                    checkPosto();
                    verifyLogin(AbastecimentosSearchActivity.class, R.string.OPTION_MENU_ABS);
                    break;
                case R.id.absTemp:
                    redirect(AbastecimentoTempActivity.class, R.string.EMPTY);
                    break;
                case R.id.plu:
                    checkLocal(); // Verifica se a localização foi informada.
                    redirect(IndicePluviometricoServicoGridActivity.class, R.string.OPTION_MENU_PLU);
                    break;
                case R.id.local:
                    redirect(LocalizacaoActivity.class, R.string.EMPTY);
                    break;
                default:
                    break;
            }
        } catch (AlertException e) {
            tratarExcecaoRedirecionando(e);
        } catch (Exception e) {
            tratarExcecao(e);
        }

        return super.onOptionsItemSelected(item);
    }

    private void initAttributes() {
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        dataHoraTextView = (TextView) findViewById(R.id.dataHoraTextView);
        estacaInicialEditText = (EditText) findViewById(R.id.estacaInicialEditText);
        estacaFinalEditText = (EditText) findViewById(R.id.estacaFinalEditText);
        pluviometroEditText = (EditText) findViewById(R.id.pluviometroEditText);
        volumeChuvaEditText = (EditText) findViewById(R.id.volumeChuvaEditText);

        novoRegistro = false;
    }

    private void ajustaCorBackground() {
        LinearLayout conteudoLinearLayout = (LinearLayout) findViewById(R.id.conteudoLinearLayout);
        LinearLayout conteudoLinearLayout2 = (LinearLayout) findViewById(R.id.conteudoLinearLayout2);
        LinearLayout conteudoLinearLayout3 = (LinearLayout) findViewById(R.id.conteudoLinearLayout3);
        LinearLayout conteudoLinearLayout4 = (LinearLayout) findViewById(R.id.conteudoLinearLayout4);

        if (conteudoLinearLayout != null) {
            conteudoLinearLayout.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        }

        if (conteudoLinearLayout2 != null) {
            conteudoLinearLayout2.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        }

        if (conteudoLinearLayout3 != null) {
            conteudoLinearLayout3.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        }

        if (conteudoLinearLayout4 != null) {
            conteudoLinearLayout4.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        }
    }

    private void bindComponents() {

        indicePluviometricoSelecionado = (IndicePluviometricoVO) getIntent().getSerializableExtra(PARAM_PLUVIOMETRO_SELECIONADO);

        if (indicePluviometricoSelecionado != null) {
            dataHoraTextView.setText(indicePluviometricoSelecionado.getData().equals(null) ? "" : indicePluviometricoSelecionado.getData());
            estacaInicialEditText.setText(indicePluviometricoSelecionado.getEstacaInicial().equals(null) ? "" : indicePluviometricoSelecionado.getEstacaInicial());
            estacaFinalEditText.setText(indicePluviometricoSelecionado.getEstacaFinal().equals(null) ? "" : indicePluviometricoSelecionado.getEstacaFinal());
            pluviometroEditText.setText(indicePluviometricoSelecionado.getPluviometro().equals(null) ? "" : indicePluviometricoSelecionado.getPluviometro());
            volumeChuvaEditText.setText(indicePluviometricoSelecionado.getVolumeChuva().equals(null) ? "0" : indicePluviometricoSelecionado.getVolumeChuva().toString());
            novoRegistro = false;
        } else {
            dataHoraTextView.setText(Util.getNow());
            novoRegistro = true;
        }

    }

    private void addListeners() {
        addBtnSalvarListener();
        addBtnCancelarListener();

        addEstacaInicialEditTextListener();
        addEstacaFinalEditTextListener();
        addPluviometroEditTextListener();
        addVolumeChuvaEditTextListener();
    }

    private void initAdapters() {
    }

    private void addBtnCancelarListener() {
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void addBtnSalvarListener() {
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvar();
            }
        });
    }

    private void addEstacaInicialEditTextListener() {
        estacaInicialEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                estacaInicialEditText.setText("");
                return false;
            }
        });
    }

    private void addEstacaFinalEditTextListener() {
        estacaFinalEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                estacaFinalEditText.setText("");
                return false;
            }
        });
    }

    private void addPluviometroEditTextListener() {
        pluviometroEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                pluviometroEditText.setText("");
                return false;
            }
        });
    }

    private void addVolumeChuvaEditTextListener() {
        volumeChuvaEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                volumeChuvaEditText.setText("");
                return false;
            }
        });
    }

    private void salvar() {
        if (isValidationOk()) {
            if (novoRegistro) {
                IndicePluviometricoVO indicePluviometricoVO = new IndicePluviometricoVO();
                indicePluviometricoVO.setData(dataHoraTextView.getText().toString());
                indicePluviometricoVO.setPluviometro(pluviometroEditText.getText().toString());
                indicePluviometricoVO.setEstacaInicial(estacaInicialEditText.getText().toString());
                indicePluviometricoVO.setEstacaFinal(estacaFinalEditText.getText().toString());
                indicePluviometricoVO.setVolumeChuva(Integer.parseInt(volumeChuvaEditText.getText().toString()));
                getDAO().getIndicePluviometricoDAO().save(indicePluviometricoVO);

                //Util.viewMessage(this, Util.getMessage(this, "", R.string.INFO_ADD_INDICE_PLUVIOMETRICO));
                finish();
            } else {
                IndicePluviometricoVO indicePluviometricoVO = new IndicePluviometricoVO();
                indicePluviometricoVO.setId(indicePluviometricoSelecionado.getId());
                indicePluviometricoVO.setData(dataHoraTextView.getText().toString());
                indicePluviometricoVO.setPluviometro(pluviometroEditText.getText().toString());
                indicePluviometricoVO.setEstacaInicial(estacaInicialEditText.getText().toString());
                indicePluviometricoVO.setEstacaFinal(estacaFinalEditText.getText().toString());
                indicePluviometricoVO.setVolumeChuva(Integer.parseInt(volumeChuvaEditText.getText().toString()));

                getDAO().getIndicePluviometricoDAO().update(indicePluviometricoVO);
                //Util.viewMessage(this, Util.getMessage(this, "", R.string.INFO_CHANGE_INDICE_PLUVIOMETRICO));
                finish();
            }
        }
    }

    private boolean isValidationOk() {
        boolean estacaIncialVazia = estacaInicialEditText.getText().toString().trim().equals(getStr(R.string.EMPTY));
        boolean estacaFinalVazia = estacaFinalEditText.getText().toString().trim().equals(getStr(R.string.EMPTY));
        boolean pluviometroVazio = pluviometroEditText.getText().toString().trim().equals(getStr(R.string.EMPTY));
        boolean volumeChuvaVazio = volumeChuvaEditText.getText().toString().trim().equals(getStr(R.string.EMPTY));

        if (pluviometroVazio && volumeChuvaVazio) {
            Util.viewMessage(this, Util.getMessage(this, "", R.string.ALERT_EXIST_REQUIRED));
        }

        if (estacaIncialVazia) {
            Util.viewMessage(this, Util.getMessage(this, getStr(R.string.estaca_inicial), R.string.ALERT_REQUIRED));
            return false;
        } else {

            String[] dados = getDAO().getLocalizacaoDAO().getValues();
            if (dados != null) {
                String descLocal = (dados[10] != null) ? dados[10] : dados[11];
                String estacaInicioLocal = dados[12].trim();
                String estacaFimLocal = dados[13].trim();

                Long estacaInicial = Long.valueOf(estacaInicialEditText.getText().toString());

                if (estacaFinalVazia) {
                    if (estacaInicial.longValue() < Long.parseLong(estacaInicioLocal) || estacaInicial.longValue() > Long.parseLong(estacaFimLocal)) {
                        Util.viewMessage(this, Util.getMessage(this, new String[]{descLocal}, R.string.ERROR_VALIDATE_CUTTING));
                        return false;
                    }
                } else {
                    Long estacaFinal = Long.valueOf(estacaFinalEditText.getText().toString());

                    if (estacaInicial.longValue() < Long.parseLong(estacaInicioLocal) && estacaFinal.longValue() > Long.parseLong(estacaFimLocal)) {
                        Util.viewMessage(this, Util.getMessage(this, new String[]{descLocal}, R.string.ERROR_VALIDATE_CUTTING));
                        return false;
                    }
                }
            } else {
                Util.viewMessage(this, Util.getMessage(this, "", R.string.ALERT_LOCATION));
            }
        }

        return true;
    }

    private String getStr(int id) {
        return getResources().getString(id);
    }

    public DAOFactory getDAO() {
        return DAOFactory.getInstance(this);
    }

    public void verifyLogin(Class<?> classRedirect, int optionStr) {
        if (intentParameters == null || intentParameters.getUserSession() == null) {
            redirect(LoginActivity.class, optionStr);
        } else {
            redirect(classRedirect, optionStr);
        }
    }

    public void checkLocal() throws Exception {
        if (getDAO().getLocalizacaoDAO().getValues() == null) {
            throw new AlertException(getStr(R.string.ALERT_LOCATION));
        }
    }

    public void checkUserLogged() throws Exception {
        ConfiguracoesVO configTemp = getDAO().getConfiguracoesDAO().getConfiguracaoVO();
        if (configTemp.getNomeUsuario() == null || configTemp.getNomeUsuario().isEmpty() || configTemp.getNomeUsuario().equals("Não informado")) {
            throw new AlertException(getStr(R.string.ALERT_USER_NOT_LOGGED));
        }
    }

    public void checkPosto() throws Exception {
        ConfiguracoesVO config = getDAO().getConfiguracoesDAO().getConfiguracaoVO();

        if (config.getIdPosto() == null || config.getIdPosto().intValue() == 0)
            throw new AlertException(getStr(R.string.ALERT_STATION));
    }

    public void tratarExcecao(Exception e) {
        e.printStackTrace();

        String errorMsg = e.getMessage() != null && !e.getMessage().isEmpty() ? e.getMessage() : getStr(R.string.ERROR_INESPERADO);

        if (e instanceof AlertException)
            Util.viewMessage(this, errorMsg);
        else
            Util.viewErrorMessage(this, errorMsg);
    }

    public void tratarExcecaoRedirecionando(Exception e) {
        e.printStackTrace();

        String errorMsg = e.getMessage() != null && !e.getMessage().isEmpty() ? e.getMessage() : getStr(R.string.ERROR_INESPERADO);

        if (e instanceof AlertException) {
            if (!e.getMessage().equals(getStr(R.string.ALERT_USER_NOT_LOGGED))) {
                AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
                dialogo.setTitle(this.getResources().getString(R.string.msg_aviso));
                dialogo.setMessage(errorMsg);
                dialogo.setPositiveButton(this.getResources().getString(R.string.msg_ok), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        redirect(LocalizacaoActivity.class, R.string.EMPTY);
                    }
                });
                dialogo.show();
            } else if (e.getMessage().equals(getStr(R.string.ALERT_USER_NOT_LOGGED)) || e.getMessage().equals(getStr(R.string.ALERT_STATION))) {
                Util.viewMessage(this, errorMsg);
            }
        }
    }

    private void redirect(Class<?> pClass, int optionStr) {
        //usado para integrar com os modulos antigos - parte de navegacao menu
        intentParameters.setMenu(getStr(optionStr));

        Intent intent = new Intent(this, pClass);
        intent.putExtra(getStr(R.string.STRING_INTENT_PARAMS), intentParameters);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);

        finish();
    }
}
