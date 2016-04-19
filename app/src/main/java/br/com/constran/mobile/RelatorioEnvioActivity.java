package br.com.constran.mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TableLayout;
import br.com.constran.mobile.exception.AlertException;
import br.com.constran.mobile.persistence.dao.DAOFactory;
import br.com.constran.mobile.persistence.vo.menu.ConfiguracoesVO;
import br.com.constran.mobile.persistence.vo.menu.LogEnvioInformacoesVO;
import br.com.constran.mobile.view.indicepluviometrico.IndicePluviometricoServicoGridActivity;
import br.com.constran.mobile.view.params.IntentParameters;
import br.com.constran.mobile.view.screens.Detail;
import br.com.constran.mobile.view.util.ExpandableListAdapter;
import br.com.constran.mobile.view.util.References;
import br.com.constran.mobile.view.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mateus_vitali on 05/11/2014.
 */
public class RelatorioEnvioActivity extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    //usado para integrar com modulos antigos na navegacao do menu
    protected IntentParameters intentParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.relatorio_envio);
        initAttributes();
        loadData();
        bindComponents();
        addListeners();
        initAdapters();

        try {
            createDetail(getDetailValues());
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private void loadData() {
        try {
            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();

            LogEnvioInformacoesVO[] log = getDAO().getLogEnvioInformacoesDAO().getAllLogEnvioInformacoesVODistinct();
            List<String> listEnviados = new ArrayList<String>();
            for(LogEnvioInformacoesVO l : log) {
                listEnviados.add(l.getData());
            }

            List<String> datas = getDAO().getUtilDAO().getDatesToExport();

            // Adding header data
            listDataHeader.add("Enviados (" + listEnviados.size() + ")" );
            listDataHeader.add("Aguardando Envio (" + datas.size() + ")");

            listDataChild.put(listDataHeader.get(0), listEnviados); // Header, Child data
            listDataChild.put(listDataHeader.get(1), datas);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Detail getDetailValues() throws Exception {

        //Texto do Detail

        String strDetail = getStr(R.string.TITLE_REL);

        Detail detail = new Detail(this);
        detail.setDetail(strDetail);
        detail.setColorBKG(getColor(R.color.GRAY));// Cor de fundo
        detail.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        detail.setFileLayoutRow(References.DETAIL_LAYOUT); //arquivo xml - layout (TableRow)
        detail.setIdColumns(References.DETAIL_ID_COLUMNS);// Ids (xml) das colunas
        detail.setIdTable(References.DETAIL_ID_MENU); //Id do TableLayout);

        return detail;
    }

    public int getColor(int id) {
        return getResources().getColor(id);
    }

    public void createDetail(final Detail pDParameters) {

        TableLayout table = (TableLayout) findViewById(pDParameters.getIdTable());
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newRow = inflater.inflate(pDParameters.getFileLayoutRow(), null);

        for (int id : pDParameters.getIdColumns()) {

            Button column = (Button) newRow.findViewById(id);
            column.setTextColor(pDParameters.getColorTXT());
            column.setText(pDParameters.getDetail());
            column.setBackground(null);

        }

        newRow.setBackgroundColor(pDParameters.getColorBKG());
        table.addView(newRow, 0);
    }

    private void initAttributes() {
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
    }

    private void bindComponents() {
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    private void addListeners() {
        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                String tipo = listDataHeader.get(groupPosition);
                String item = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);

                if(tipo.contains("Enviados")) {
                    LogEnvioInformacoesVO[] logs = getDAO().getLogEnvioInformacoesDAO().getLogEnvioInformacoesVOByDate(item);

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(RelatorioEnvioActivity.this);
                    builder1.setTitle("Dia " + item);
                    String body = "";

                    int i = 1;
                    for(LogEnvioInformacoesVO log : logs) {
                        String[] infos = log.getDataHoraEnvio().split(" ");
                        body = body.concat(i + " - Enviado dia " + infos[0] + " às " + infos[1] + "\n");
                        i++;
                    }

                    builder1.setMessage(body);
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("OK",null);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

                return false;
            }
        });
    }

    private void initAdapters() {
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
