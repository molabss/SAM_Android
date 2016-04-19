package br.com.constran.mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import br.com.constran.mobile.exception.AlertException;
import br.com.constran.mobile.persistence.vo.ObraVO;
import br.com.constran.mobile.persistence.vo.imp.UsuarioVO;
import br.com.constran.mobile.persistence.vo.menu.ConfiguracoesVO;
import br.com.constran.mobile.persistence.vo.rae.RaeVO;
import br.com.constran.mobile.persistence.vo.rae.abs.AbastecimentoPostoVO;
import br.com.constran.mobile.persistence.vo.rae.abs.PostoVO;
import br.com.constran.mobile.system.SharedPreferencesHelper;
import br.com.constran.mobile.view.AbstractActivity;
import br.com.constran.mobile.view.interfaces.InterfaceEditActivity;
import br.com.constran.mobile.view.screens.Detail;
import br.com.constran.mobile.view.util.References;
import br.com.constran.mobile.view.util.Util;
import org.apache.commons.io.output.FileWriterWithEncoding;

import java.io.File;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public final class ConfiguracoesActivity extends AbstractActivity implements InterfaceEditActivity {

    private EditText idObra, idObra2;
    private AutoCompleteTextView userList;
    private AutoCompleteTextView postoList;
    private EditText dispositivo;
    private EditText servidor;
    private Spinner eticket;
    private EditText tolerancia;
    private EditText portaWeb;
    private EditText referencia;
    private EditText duracao;
    private EditText portaFTP;
    private String[] dados;

    protected LinearLayout conteudoLinearLayout;
    protected LinearLayout conteudoLinearLayout2;
    protected LinearLayout conteudoLinearLayout3;
    protected LinearLayout conteudoLinearLayout4;
    protected LinearLayout conteudoLinearLayout5;
    protected LinearLayout conteudoLinearLayout6;
    protected LinearLayout conteudoLinearLayout7;
    protected LinearLayout conteudoLinearLayout8;
    protected LinearLayout conteudoLinearLayout9;
    protected LinearLayout conteudoLinearLayout10;
    protected LinearLayout conteudoLinearLayout11;
    protected LinearLayout conteudoLinearLayout12;

    protected LinearLayout headerLinearLayout;
    protected LinearLayout headerLinearLayout2;
    protected LinearLayout headerLinearLayout3;
    protected LinearLayout headerLinearLayout4;
    protected LinearLayout headerLinearLayout5;
    protected LinearLayout headerLinearLayout6;
    protected LinearLayout headerLinearLayout7;
    protected LinearLayout headerLinearLayout8;
    protected LinearLayout headerLinearLayout9;
    protected LinearLayout headerLinearLayout10;
    protected LinearLayout headerLinearLayout11;
    protected LinearLayout headerLinearLayout12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.configuracoes_edit);

        currentContext = ConfiguracoesActivity.this;
        currentClass = ConfiguracoesActivity.class;

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

        idObra = (EditText) findViewById(R.id.ConfViewCCObra);
        idObra2 = (EditText) findViewById(R.id.ConfViewCCObra2);
        eticket = (Spinner) findViewById(R.id.ConfViewEticket);
        dispositivo = (EditText) findViewById(R.id.ConfViewTablet);
        servidor = (EditText) findViewById(R.id.ConfViewSrv);
        portaWeb = (EditText) findViewById(R.id.ConfViewPWeb);
        portaFTP = (EditText) findViewById(R.id.ConfViewPFtp);
        referencia = (EditText) findViewById(R.id.ConfViewRef);
        userList = (AutoCompleteTextView) findViewById(R.id.ConfViewUsr);
        postoList = (AutoCompleteTextView) findViewById(R.id.ConfViewPst);
        duracao = (EditText) findViewById(R.id.ConfViewDur);
        tolerancia = (EditText) findViewById(R.id.ConfViewTol);
        btSalvar = (Button) findViewById(R.id.btConfSave);
        btCancelar = (Button) findViewById(R.id.btConfCancel);

        conteudoLinearLayout = (LinearLayout) findViewById(R.id.contentLinearLayout);
        conteudoLinearLayout2 = (LinearLayout) findViewById(R.id.contentLinearLayout2);
        conteudoLinearLayout3 = (LinearLayout) findViewById(R.id.contentLinearLayout3);
        conteudoLinearLayout4 = (LinearLayout) findViewById(R.id.contentLinearLayout4);
        conteudoLinearLayout5 = (LinearLayout) findViewById(R.id.contentLinearLayout5);
        conteudoLinearLayout6 = (LinearLayout) findViewById(R.id.contentLinearLayout6);
        conteudoLinearLayout7 = (LinearLayout) findViewById(R.id.contentLinearLayout7);
        conteudoLinearLayout8 = (LinearLayout) findViewById(R.id.contentLinearLayout8);
        conteudoLinearLayout9 = (LinearLayout) findViewById(R.id.contentLinearLayout9);
        conteudoLinearLayout10 = (LinearLayout) findViewById(R.id.contentLinearLayout10);
        conteudoLinearLayout11 = (LinearLayout) findViewById(R.id.contentLinearLayout11);
        conteudoLinearLayout12 = (LinearLayout) findViewById(R.id.contentLinearLayout12);

        headerLinearLayout = (LinearLayout) findViewById(R.id.headerLinearLayout);
        headerLinearLayout2 = (LinearLayout) findViewById(R.id.headerLinearLayout2);
        headerLinearLayout3 = (LinearLayout) findViewById(R.id.headerLinearLayout3);
        headerLinearLayout4 = (LinearLayout) findViewById(R.id.headerLinearLayout4);
        headerLinearLayout5 = (LinearLayout) findViewById(R.id.headerLinearLayout5);
        headerLinearLayout6 = (LinearLayout) findViewById(R.id.headerLinearLayout6);
        headerLinearLayout7 = (LinearLayout) findViewById(R.id.headerLinearLayout7);
        headerLinearLayout8 = (LinearLayout) findViewById(R.id.headerLinearLayout8);
        headerLinearLayout9 = (LinearLayout) findViewById(R.id.headerLinearLayout9);
        headerLinearLayout10 = (LinearLayout) findViewById(R.id.headerLinearLayout10);
        headerLinearLayout11 = (LinearLayout) findViewById(R.id.headerLinearLayout11);
        headerLinearLayout12 = (LinearLayout) findViewById(R.id.headerLinearLayout12);

        fixBackgroundColor();
    }

    @Override
    public void editValues() throws Exception {

        postoArrayVO = getDAO().getPostoDAO().getArrayPostoVO();

        dados = getDAO().getConfiguracoesDAO().getValues();

        int i = 0;

        idObra.setText(dados[i]);
        idObra2.setText(dados[++i]);
        userRequest.setNome(dados[++i]);
        dispositivo.setText(dados[++i]);
        servidor.setText(dados[++i]);
        userRequest.setId(dados[++i] != null ? Integer.valueOf(dados[i]) : null);
        userRequest.setIdUsuario(dados[++i] != null ? Integer.valueOf(dados[i]) : null);
        userRequest.setIdUsuarioPessoal(dados[++i] != null ? Integer.valueOf(dados[i]) : null);
        Integer ID_POSTO = dados[++i] != null ? Integer.valueOf(dados[i]) : null;
        String DESC_POSTO = dados[++i];
        portaWeb.setText(dados[++i]);
        portaFTP.setText(dados[++i]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(currentContext, android.R.layout.select_dialog_singlechoice, getResources().getStringArray(R.array.E_TICKET_SPINNER));
        eticket.setAdapter(adapter);
        adapter.sort(new Comparator<String>() {
            @Override
            public int compare(String s, String s2) {
                return 0;
            }
        });
        eticket.setSelection(adapter.getPosition(dados[++i]));

        String atual = dados[++i];

        duracao.setText(dados[++i]);
        tolerancia.setText(dados[++i]);
        referencia.setText(dados[++i]);

        posto = new PostoVO(ID_POSTO, DESC_POSTO);

        if (getDAO().getObraDAO().getAllObraVO().length > 0) {
            usuarioArrayVO = getDAO().getUsuarioDAO().getArrayUsuarioVOByObra(idObra.getText().toString(), idObra2.getText().toString());
        } else {
            usuarioArrayVO = getDAO().getUsuarioDAO().getArrayUsuarioVO();
        }
    }


    @Override
    public void onBackPressed() {
        redirectPrincipal();
    }

    @Override
    public Detail getDetailValues() throws Exception {
        //Texto do Detail
        String strDetail = getStr(R.string.DETAIL_CONF);

        Detail detail = new Detail(this);
        detail.setDetail(strDetail);
        detail.setColorBKG(getColor(R.color.GRAY));// Cor de fundo
        detail.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        detail.setFileLayoutRow(References.DETAIL_LAYOUT); //arquivo xml - layout (TableRow)
        detail.setIdColumns(References.DETAIL_ID_COLUMNS);// Ids (xml) das colunas
        detail.setIdTable(References.DETAIL_ID_CONF); //Id do TableLayout);

        return detail;
    }


    @Override
    public void initEvents() throws Exception {

        idObra.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        if (getDAO().getObraDAO().getAllObraVO().length > 0) {
                            if (!idObra.getText().toString().isEmpty()) {
                                ObraVO obra = getDAO().getObraDAO().getById(Integer.parseInt(idObra.getText().toString()));
                                if (obra != null) {
                                    usuarioArrayVO = getDAO().getUsuarioDAO().getArrayUsuarioVOByObra(idObra.getText().toString(), idObra2.getText().toString());
                                } else {
                                    throw new Exception(Util.getString(currentContext, new String[]{idObra.getText().toString()}, R.string.ALERT_OBRA_CADASTRADO));
                                }
                            }
                        } else {
                            usuarioArrayVO = getDAO().getUsuarioDAO().getArrayUsuarioVO();
                        }
                    } catch (Exception e) {
                        tratarExcecao(e);
                    }
                }
            }
        });

        idObra2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        if (getDAO().getObraDAO().getAllObraVO().length > 0) {
                            if (!idObra2.getText().toString().isEmpty()) {
                                ObraVO obra = getDAO().getObraDAO().getById(Integer.parseInt(idObra2.getText().toString()));
                                if (obra != null) {
                                    usuarioArrayVO = getDAO().getUsuarioDAO().getArrayUsuarioVOByObra(idObra.getText().toString(), idObra2.getText().toString());
                                } else {
                                    throw new Exception(Util.getString(currentContext, new String[]{idObra2.getText().toString()}, R.string.ALERT_OBRA_CADASTRADO));
                                }
                            }
                        } else {
                            usuarioArrayVO = getDAO().getUsuarioDAO().getArrayUsuarioVO();
                        }
                    } catch (Exception e) {
                        tratarExcecao(e);
                    }
                }
            }
        });

        userList.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                userRequest = new UsuarioVO();
                return false;
            }
        });

        userList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userRequest = (UsuarioVO) parent.getItemAtPosition(position);
            }

        });

        userList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                userRequest = new UsuarioVO();
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

        postoList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posto = (PostoVO) parent.getItemAtPosition(position);
            }

        });

        postoList.setOnClickListener(new View.OnClickListener() {
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
                    validate();
                    generateNewConfig();
                    //getDAO().getConfiguracoesDAO().loadConfig(true);
                    getDAO().getConfiguracoesDAO().loadConfig();


                    AlertDialog.Builder dialogo = new AlertDialog.Builder(currentContext);
                    dialogo.setMessage("Atualizado com sucesso!");
                    dialogo.setPositiveButton(getStr(R.string.OK), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface di, int arg) {
                            redirect(PrincipalActivity.class, false);
                        }
                    });
                    dialogo.setTitle(getStr(R.string.AVISO));
                    dialogo.show();

                } catch (Exception e) {
                    tratarExcecao(e);
                }

            }
        });

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View argetDetailValuesg0) {
                onBackPressed();
            }
        });
    }


    @Override
    public void editScreen() throws Exception {

        ArrayAdapter<UsuarioVO> adU = new ArrayAdapter<UsuarioVO>(this, android.R.layout.select_dialog_singlechoice, usuarioArrayVO);
        adU.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        userList.setAdapter(adU);

        userList.setText(userRequest.getNome());

        ArrayAdapter<PostoVO> adp = new ArrayAdapter<PostoVO>(this, android.R.layout.select_dialog_singlechoice, postoArrayVO);
        adp.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        postoList.setAdapter(adp);

        postoList.setText(posto == null ? getStr(R.string.EMPTY) : posto.getDescricao());
    }


    @Override
    public void validateFields() throws Exception {

        StringBuilder msgCamposObrigatorios = new StringBuilder("");

        if(config.getIdObra() == null || config.getIdObra() == 0){
            msgCamposObrigatorios.append("Campo Centro de custo é obrigatório.\n");
        }
        if(config.getDispositivo() == null || config.getDispositivo().length() == 0){
            msgCamposObrigatorios.append("Campo Dispositivo é obrigatório.\n");
        }
        if(config.getPortaFtp() == null || config.getPortaFtp().length() == 0){
            msgCamposObrigatorios.append("Campo Porta FTP é obrigatório.\n");
        }
        if(config.getPortaWeb() == null || config.getPortaWeb().length() == 0){
            msgCamposObrigatorios.append("Campo Porta Web é obrigatório.\n");
        }
        if(config.getEticket() == null || config.getEticket().length() == 0){
            msgCamposObrigatorios.append("Campo Geração E-Ticket é obrigatório.\n");
        }
        if(config.getTolerancia() == null || config.getTolerancia() == 0){
            msgCamposObrigatorios.append("Campo Geração Tolerância é obrigatório e não pode ser igual a 0.\n");
        }
        if(config.getServidor() == null || config.getServidor().length() == 0){
            msgCamposObrigatorios.append("Campo Servidor é obrigatório.\n");
        }
        if(config.getReferencia() == null || config.getReferencia().length() == 0){
            msgCamposObrigatorios.append("Campo Referência é obrigatório.\n");
        }

        if(msgCamposObrigatorios.length() > 0){
            throw new AlertException(msgCamposObrigatorios.toString());
        }

        /*
        if (config == null ||
                config.getDispositivo() == null ||
                config.getIdObra() == null ||
                config.getPortaFtp() == null ||
                config.getPortaWeb() == null ||
                config.getEticket() == null ||
                config.getTolerancia() == null ||
                config.getReferencia() == null ||
                config.getServidor() == null) {
        }
        */

    }

    private void generateNewConfig() throws Exception {

        config = new ConfiguracoesVO();
        config.setDispositivo(dispositivo.getText().toString());
        config.setEticket(eticket.getSelectedItem().toString());
        config.setPortaFtp(portaFTP.getText().toString());
        config.setPortaWeb(portaWeb.getText().toString());
        config.setServidor(servidor.getText().toString());
        config.setReferencia(referencia.getText().toString());
        config.setCodUsuario(userRequest.getId());
        config.setIdPosto(posto != null ? posto.getId() : null);
        config.setIdUsuario(userRequest.getIdUsuario() != null ? userRequest.getIdUsuario() : null);
        config.setIdUsuarioPessoal(userRequest.getIdUsuarioPessoal() != null ? userRequest.getIdUsuarioPessoal() : null);


        if (tolerancia.getText() != null && !tolerancia.getText().toString().equals(getStr(R.string.EMPTY))) {
            config.setTolerancia(Integer.valueOf(tolerancia.getText().toString()));
        }
        if (duracao.getText() != null && !duracao.getText().toString().equals(getStr(R.string.EMPTY))) {
            config.setDuracao(Double.valueOf(duracao.getText().toString()));
        }
        if (idObra.getText() != null && !idObra.getText().toString().equals(getStr(R.string.EMPTY))) {
            config.setIdObra(Integer.valueOf(idObra.getText().toString()));
        }
        if (idObra2.getText() != null && !idObra2.getText().toString().equals(getStr(R.string.EMPTY))) {
            config.setIdObra2(Integer.valueOf(idObra2.getText().toString()));
        }

        validateFields();

        SharedPreferencesHelper.Configuracao.CONTEXT = currentContext;

        SharedPreferencesHelper.Configuracao.setDispositivo(config.getDispositivo());
        SharedPreferencesHelper.Configuracao.setEtiket(config.getEticket());
        SharedPreferencesHelper.Configuracao.setPortaFtp(config.getPortaFtp());
        SharedPreferencesHelper.Configuracao.setPortaWeb(config.getPortaWeb());
        SharedPreferencesHelper.Configuracao.setServidor(config.getServidor());
        SharedPreferencesHelper.Configuracao.setReferencia(config.getReferencia());
        SharedPreferencesHelper.Configuracao.setCodUsuario(config.getCodUsuario());
        SharedPreferencesHelper.Configuracao.setIdPosto(config.getIdPosto());
        SharedPreferencesHelper.Configuracao.setIdUsuario(config.getIdUsuario());
        SharedPreferencesHelper.Configuracao.setIdUsuarioPessoal(config.getIdUsuarioPessoal());

        SharedPreferencesHelper.Configuracao.setIdObra(config.getIdObra());
        SharedPreferencesHelper.Configuracao.setTolerancia(config.getTolerancia());
        SharedPreferencesHelper.Configuracao.setIdObra2(config.getIdObra2());
        SharedPreferencesHelper.Configuracao.setDuracao(String.valueOf(config.getDuracao()));
        SharedPreferencesHelper.Configuracao.setNomeUsuario(userList.getText().toString());
        SharedPreferencesHelper.Configuracao.setNomePosto(postoList.getText().toString());

        //String strJson = Util.GSON_API.toJson(config);
        //String strPath = SD_CARD + getStr(R.string.DIR_CONFIG);
        //String nameFile = getStr(R.string.FILE_CONFIG);
        //File path = new File(strPath);
        //File file = new File(path, nameFile);
        //FileWriterWithEncoding fw = new FileWriterWithEncoding(file, getStr(R.string.UTF_8));
        //strJson = strJson.replaceAll("'", "");
        //fw.write(strJson);
        //fw.flush();
        //fw.close();

    }

    private void validate() throws Exception {
        List<RaeVO> raeVOs = getDAO().getRaeDAO().findRaesByData(new Date());

        if (getDAO().getObraDAO().getAllObraVO().length > 0) {
            if (!idObra.getText().toString().isEmpty()) {
                ObraVO obra = getDAO().getObraDAO().getById(Integer.parseInt(idObra.getText().toString()));
                if (obra == null) {
                    throw new Exception(Util.getString(currentContext, new String[]{idObra.getText().toString()}, R.string.ALERT_OBRA_CADASTRADO));
                }
            }
            if (!idObra2.getText().toString().isEmpty()) {
                ObraVO obra = getDAO().getObraDAO().getById(Integer.parseInt(idObra2.getText().toString()));
                if (obra == null) {
                    throw new Exception(Util.getString(currentContext, new String[]{idObra2.getText().toString()}, R.string.ALERT_OBRA_CADASTRADO));
                }
            }
        }

        if (raeVOs != null && !raeVOs.isEmpty()) {
            throw new AlertException(Util.getMessage(currentContext, R.string.abastecimentos, R.string.ALERT_POSTO_COM_ABASTECIMENTO_DIA));
        }

        List<AbastecimentoPostoVO> abastecimentoPostoVOs = getDAO().getAbastecimentoPostoDAO().findAbastecimentosPostoByData(new Date());

        if (abastecimentoPostoVOs != null && !abastecimentoPostoVOs.isEmpty()) {
            throw new AlertException(Util.getMessage(currentContext, R.string.saldos, R.string.ALERT_POSTO_COM_ABASTECIMENTO_DIA));
        }

    }


    private void fixBackgroundColor() {

        if (conteudoLinearLayout != null)
            conteudoLinearLayout.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        if (conteudoLinearLayout2 != null)
            conteudoLinearLayout2.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        if (conteudoLinearLayout3 != null)
            conteudoLinearLayout3.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        if (conteudoLinearLayout4 != null)
            conteudoLinearLayout4.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        if (conteudoLinearLayout5 != null)
            conteudoLinearLayout5.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        if (conteudoLinearLayout6 != null)
            conteudoLinearLayout6.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        if (conteudoLinearLayout7 != null)
            conteudoLinearLayout7.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        if (conteudoLinearLayout8 != null)
            conteudoLinearLayout8.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        if (conteudoLinearLayout9 != null)
            conteudoLinearLayout9.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        if (conteudoLinearLayout10 != null)
            conteudoLinearLayout10.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        if (conteudoLinearLayout11 != null)
            conteudoLinearLayout11.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        if (conteudoLinearLayout12 != null)
            conteudoLinearLayout12.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));

        if (headerLinearLayout != null)
            headerLinearLayout.setBackgroundColor(getResources().getColor(R.color.GRAY2));
        if (headerLinearLayout2 != null)
            headerLinearLayout2.setBackgroundColor(getResources().getColor(R.color.GRAY2));
        if (headerLinearLayout3 != null)
            headerLinearLayout3.setBackgroundColor(getResources().getColor(R.color.GRAY2));
        if (headerLinearLayout4 != null)
            headerLinearLayout4.setBackgroundColor(getResources().getColor(R.color.GRAY2));
        if (headerLinearLayout5 != null)
            headerLinearLayout5.setBackgroundColor(getResources().getColor(R.color.GRAY2));
        if (headerLinearLayout6 != null)
            headerLinearLayout6.setBackgroundColor(getResources().getColor(R.color.GRAY2));
        if (headerLinearLayout7 != null)
            headerLinearLayout7.setBackgroundColor(getResources().getColor(R.color.GRAY2));
        if (headerLinearLayout8 != null)
            headerLinearLayout8.setBackgroundColor(getResources().getColor(R.color.GRAY2));
        if (headerLinearLayout9 != null)
            headerLinearLayout9.setBackgroundColor(getResources().getColor(R.color.GRAY2));
        if (headerLinearLayout10 != null)
            headerLinearLayout10.setBackgroundColor(getResources().getColor(R.color.GRAY2));
        if (headerLinearLayout11 != null)
            headerLinearLayout11.setBackgroundColor(getResources().getColor(R.color.GRAY2));
        if (headerLinearLayout12 != null)
            headerLinearLayout12.setBackgroundColor(getResources().getColor(R.color.GRAY2));
    }
}
