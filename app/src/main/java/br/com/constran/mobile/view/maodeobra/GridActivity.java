package br.com.constran.mobile.view.maodeobra;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.constran.mobile.*;
import br.com.constran.mobile.adapter.AbstractAdapter;
import br.com.constran.mobile.exception.AlertException;
import br.com.constran.mobile.persistence.dao.DAOFactory;
import br.com.constran.mobile.persistence.vo.ObraVO;
import br.com.constran.mobile.persistence.vo.menu.ConfiguracoesVO;
import br.com.constran.mobile.view.indicepluviometrico.IndicePluviometricoServicoGridActivity;
import br.com.constran.mobile.view.params.IntentParameters;
import br.com.constran.mobile.view.util.Util;

import java.util.Date;
import java.util.List;

/**
 * Criado em 11/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public abstract class GridActivity<T extends AbstractAdapter> extends Activity {

    protected static final Integer COD_PRODUZINDO = 16;
    protected final int INTEGRANTE_QR_CODE_REQUEST = 997;
    protected final int TEMPORARIO_QR_CODE_REQUEST = 998;

    protected LinearLayout conteudoLinearLayout;
    protected LinearLayout conteudoLinearLayout2;
    protected LinearLayout conteudoLinearLayout3;
    protected LinearLayout conteudoLinearLayout4;
    protected LinearLayout conteudoLinearLayout5;
    private LinearLayout conteudoLinearLayout6;
    private LinearLayout conteudoLinearLayout7;
    private TextView emptyListView;

    String dataSelecionada;
    boolean readOnly;

    static final int RESULT_CODE = 1;
    static final int REQUEST_LOCAL_CODE = 2;
    static final int EQUIPE_LOCAL_RESULT_CODE = 3;

    static final String TIPO_APROPRIACAO_SERVICO = "SRV";
    static final String PARAM_FROM_LOCAL_EQUIPE = "fromLocalizacaoEquipe";
    static final String PARAM_EQUIPE_SELECIONADA = "equipeSelecionada";
    static final String PARAM_LOCAL_SELECIONADO = "localSelecionado";
    static final String PARAM_READ_ONLY = "readOnly";
    static final String PARAM_DATA_SELECIONADA = "dataSelecionada";
    static final String PARAM_EVENTO_EQUIPE_SELECIONADA = "eventoEquipeSelecionada";
    static final String PARAM_INTEGRANTE_SELECIONADO = "integranteSelecionado";

    protected ObraVO obraVO = null;

    //usado para integrar com modulos antigos na navegacao do menu
    protected IntentParameters intentParameters;

    private T adapter;
    protected DAOFactory dao;

    protected TextView gridVazioTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = DAOFactory.getInstance(this);
        setContentView(getContentView());
        initAttributes();
        bindComponents();
        initAdapter();
        addListeners();

        ajustaCorBackground();
        intentParameters = (IntentParameters) getIntent().getSerializableExtra(getStr(R.string.STRING_INTENT_PARAMS));
    }

    /**
     * O android está com bug ao aplicar cor de fundo de alguns componentes
     * Algumas vezes os componentes que tem o background definido como DARK_GRAY não são renderizados corretamente
     * Assim, este método foi adicionado para corrigir a renderizacao dinamicamente
     */
    private void ajustaCorBackground() {
        conteudoLinearLayout = (LinearLayout) findViewById(R.id.conteudoLinearLayout);
        conteudoLinearLayout2 = (LinearLayout) findViewById(R.id.conteudoLinearLayout2);
        conteudoLinearLayout3 = (LinearLayout) findViewById(R.id.conteudoLinearLayout3);
        conteudoLinearLayout4 = (LinearLayout) findViewById(R.id.conteudoLinearLayout4);
        conteudoLinearLayout5 = (LinearLayout) findViewById(R.id.conteudoLinearLayout5);
        conteudoLinearLayout6 = (LinearLayout) findViewById(R.id.conteudoLinearLayout6);
        conteudoLinearLayout7 = (LinearLayout) findViewById(R.id.conteudoLinearLayout7);
        emptyListView = (TextView) findViewById(R.id.empty_list_view);

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

        if (conteudoLinearLayout5 != null) {
            conteudoLinearLayout5.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        }

        if (conteudoLinearLayout6 != null) {
            conteudoLinearLayout6.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        }

        if (conteudoLinearLayout7 != null) {
            conteudoLinearLayout7.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        }

        if (emptyListView != null) {
            emptyListView.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu_aux, menu);
        menu.findItem(R.id.logout).setVisible(false);
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

    private void redirect(Class<?> pClass, int optionStr) {
        //usado para integrar com os modulos antigos - parte de navegacao menu
        intentParameters.setMenu(getStr(optionStr));

        Intent intent = new Intent(this, pClass);
        intent.putExtra(getStr(R.string.STRING_INTENT_PARAMS), intentParameters);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);

        finish();
    }

    public void verifyLogin(Class<?> classRedirect, int optionStr) {
        if (intentParameters == null || intentParameters.getUserSession() == null) {
            redirect(LoginActivity.class, optionStr);
        } else {
            redirect(classRedirect, optionStr);
        }
    }

    public void checkLocal() throws Exception {
        if (dao.getLocalizacaoDAO().getValues() == null) {
            throw new AlertException(getStr(R.string.ALERT_LOCATION));
        }
    }

    public void checkUserLogged() throws Exception {
        ConfiguracoesVO configTemp = dao.getConfiguracoesDAO().getConfiguracaoVO();
        if (configTemp.getNomeUsuario() == null || configTemp.getNomeUsuario().isEmpty() || configTemp.getNomeUsuario().equals("Não informado")) {
            throw new AlertException(getStr(R.string.ALERT_USER_NOT_LOGGED));
        }
    }

    public void checkPosto() throws Exception {
        ConfiguracoesVO config = dao.getConfiguracoesDAO().getConfiguracaoVO();

        if (config.getIdPosto() == null || config.getIdPosto() == 0)
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

    protected T getAdapter() {
        return adapter;
    }

    protected void setAdapter(T adapter) {
        this.adapter = adapter;
    }

    protected abstract int getContentView();

    protected abstract void initAttributes();

    protected abstract void bindComponents();

    protected abstract void initAdapter();

    protected abstract void addListeners();

    /**
     * Grid Methods **
     */

    protected void editarGridItem(int i) {
    }

    protected void excluirGridItem(int i) {
    }

    protected void selecionarGridItem(int i) {
    }

    protected void positiveClick() {
    }

    protected void negativeClick() {
    }

    protected void afterClick() {
    }


    protected void gridClickListener(AdapterView<?> adapterView, View view, int i, long l) {
        if (getAdapter().getOperacao() != null)
            switch (getAdapter().getOperacao()) {
                case EXCLUSAO:
                    confirmaExclusao(i);
                    break;
                case EDICAO:
                    editarGridItem(i);
                    break;
                case SELECAO:
                    selecionarGridItem(i);
                    break;
                default:
                    break;
            }
    }

    protected void confirmaExclusao(final int i) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setMessage(getStr(R.string.ALERT_CONFIRM_REMOVE));
        dialogo.setPositiveButton(getStr(R.string.SIM), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface di, int arg) {
                excluirGridItem(i);
            }
        });
        dialogo.setNegativeButton(getStr(R.string.NAO), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface di, int arg) {
                getAdapter().setOperacao(null);
            }
        });
        dialogo.setTitle(getStr(R.string.AVISO));
        dialogo.show();
    }

    protected void confirmarDialog(final int messageId) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setMessage(getStr(messageId));
        dialogo.setPositiveButton(getStr(R.string.SIM), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface di, int arg) {
                positiveClick();
                afterClick();
            }
        });
        dialogo.setNegativeButton(getStr(R.string.NAO), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface di, int arg) {
                negativeClick();
                afterClick();
            }
        });

        dialogo.setTitle(getStr(R.string.AVISO));
        dialogo.show();
    }

    protected String getStr(int id) {
        return getResources().getString(id);
    }

    protected void showHideEmptyText(List<?> lista) {
        if (gridVazioTextView != null) {
            if (lista == null || lista.isEmpty()) {
                gridVazioTextView.setVisibility(View.VISIBLE);
            } else {
                gridVazioTextView.setVisibility(View.GONE);
            }
        }
    }

    protected void showHideEmptyText(List<?> lista, TextView gridVazioTextView) {
        if (gridVazioTextView != null) {
            if (lista == null || lista.isEmpty()) {
                gridVazioTextView.setVisibility(View.VISIBLE);
            } else {
                gridVazioTextView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Verifica se o registro editado é antigo ou do mesmo dia
     *
     * @param data data do registro editado
     * @return true ou false
     */
    protected boolean isEventoAntigo(String data) {
        Date dateEvento = Util.toDateFormat(data);
        Date hoje = Util.toDateFormat(Util.getToday());

        return dateEvento.before(hoje);
    }

}
