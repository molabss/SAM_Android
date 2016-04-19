package br.com.constran.mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Date;

import br.com.constran.mobile.connectivity.DownloadAPK;
import br.com.constran.mobile.connectivity.Exportador;
import br.com.constran.mobile.connectivity.Importador;
import br.com.constran.mobile.exception.AlertException;
import br.com.constran.mobile.system.AppDirectory;
import br.com.constran.mobile.system.AppSecurity;
import br.com.constran.mobile.system.SharedPreferencesHelper;
import br.com.constran.mobile.view.AbstractActivity;
import br.com.constran.mobile.view.indicepluviometrico.IndicePluviometricoServicoGridActivity;
import br.com.constran.mobile.view.interfaces.InterfaceEditActivity;
import br.com.constran.mobile.view.maodeobra.MaoObraServicoGridActivity;
import br.com.constran.mobile.view.params.IntentParameters;
import br.com.constran.mobile.view.screens.Detail;
import br.com.constran.mobile.view.util.References;
import br.com.constran.mobile.view.util.Util;

public final class PrincipalActivity extends AbstractActivity implements InterfaceEditActivity {

    private int action;
    //private String filename;

    private TextView rodape;
    private TextView rodape2;
    private TextView descServidor;
    Intent intentBkpService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        currentClass = PrincipalActivity.class;
        currentContext = PrincipalActivity.this;

        try {
            //verificaSDcard();
            verificarDiretorios();
            initNetworkPermissions();
            init();
            initAtributes();
            disponibilizarBotoesAcessoModulos();
            initEvents();
            initSQL();
            editValues();
            //createDetail(getDetailValues());

        } catch (Exception e) {
            tratarExcecao(e);
        }
    }

    @Override
    protected void onResume() {
        disponibilizarBotoesAcessoModulos();
        initEvents();
        try {
            validarDataTablet();
        } catch (AlertException e) {
            tratarExcecao(e);
        }
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (intentBkpService != null) stopService(intentBkpService);
    }

    public void initNetworkPermissions() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


    public void initSQL() throws Exception {
        //getDAO().getConfiguracoesDAO().loadConfig(false);
        getDAO().getConfiguracoesDAO().loadConfig();
    }


    public void disponibilizarBotoesAcessoModulos() {

        SharedPreferencesHelper.AppModulo.CONTEXT = currentContext;

        //MOVIMENTACOES
        if (!SharedPreferencesHelper.AppModulo.estaMovimentacoesAtivado()) {
            btMovimentacoes = (Button) findViewById(R.id.buttonMovimentacoes_destivado);
            findViewById(R.id.buttonMovimentacoes).setVisibility(View.GONE);
            //btMovimentacoes.setText("Movimentações");
            //btMovimentacoes.setTextColor(Color.GRAY);
        } else {
            btMovimentacoes = (Button) findViewById(R.id.buttonMovimentacoes);
            findViewById(R.id.buttonMovimentacoes_destivado).setVisibility(View.GONE);
           // btMovimentacoes.setText("Movimentações");
            //btMovimentacoes.setTextColor(Color.BLACK);
        }
        btMovimentacoes.setVisibility(View.VISIBLE);
        //--------------


        //EQUIPAMENTOS
        if (!SharedPreferencesHelper.AppModulo.estaEquipamentosAtivado()) {
            btEquipamentos = (Button) findViewById(R.id.buttonEquipamentos_desativado);
            findViewById(R.id.buttonEquipamentos).setVisibility(View.GONE);
            //btEquipamentos.setText("Equipamentos");
            //btEquipamentos.setTextColor(Color.GRAY);
        } else {
            btEquipamentos = (Button) findViewById(R.id.buttonEquipamentos);
            findViewById(R.id.buttonEquipamentos_desativado).setVisibility(View.GONE);
            //btEquipamentos.setText("Equipamentos");
            //btEquipamentos.setTextColor(Color.BLACK);
        }
        btEquipamentos.setVisibility(View.VISIBLE);
        //--------------


        //MAO DE OBRA
        if (!SharedPreferencesHelper.AppModulo.estaMaoDeObraAtivado()) {
            btMaoDeObra = (Button) findViewById(R.id.buttonMaoObra_desativado);
            findViewById(R.id.buttonMaoObra).setVisibility(View.GONE);
            //btMaoDeObra.setText("Mão de Obra");
            //btMaoDeObra.setTextColor(Color.GRAY);
        } else {
            btMaoDeObra = (Button) findViewById(R.id.buttonMaoObra);
            findViewById(R.id.buttonMaoObra_desativado).setVisibility(View.GONE);
            //.setText("Mão de Obra");
            //btMaoDeObra.setTextColor(Color.BLACK);
        }
        btMaoDeObra.setVisibility(View.VISIBLE);
        //--------------


        //ABASTECIMENTOS
        if (!SharedPreferencesHelper.AppModulo.estaAbastecimentosAtivado()) {
            btAbastecimento = (Button) findViewById(R.id.buttonAbastecimento_desativado);
            findViewById(R.id.buttonAbastecimento).setVisibility(View.GONE);
            //btAbastecimento.setText("Abastecimentos");
            //btAbastecimento.setTextColor(Color.GRAY);
        } else {
            btAbastecimento = (Button) findViewById(R.id.buttonAbastecimento);
            findViewById(R.id.buttonAbastecimento_desativado).setVisibility(View.GONE);
            //btAbastecimento.setText("Abastecimentos");
            //btAbastecimento.setTextColor(Color.BLACK);
        }
        btAbastecimento.setVisibility(View.VISIBLE);
        //--------------


        //INDICE PLUVIOMETRICO
        if (!SharedPreferencesHelper.AppModulo.estaIndicesPluviometricosAtivado()) {
            btIndicePluviometrico = (Button) findViewById(R.id.buttonIndicePluviometrico_desativado);
            findViewById(R.id.buttonIndicePluviometrico).setVisibility(View.GONE);
            //btIndicePluviometrico.setText("Índice Pluviométrico");
            //btIndicePluviometrico.setTextColor(Color.GRAY);
        } else {
            btIndicePluviometrico = (Button) findViewById(R.id.buttonIndicePluviometrico);
            findViewById(R.id.buttonIndicePluviometrico_desativado).setVisibility(View.GONE);
           // btIndicePluviometrico.setText("Índice Pluviométrico");
            //btIndicePluviometrico.setTextColor(Color.BLACK);
        }
        btIndicePluviometrico.setVisibility(View.VISIBLE);
        //-------------------


        //MANUTENCOES
        if (!SharedPreferencesHelper.AppModulo.estaManutencoesAtivado()) {
            btManutencoes = (Button) findViewById(R.id.buttonManutencoes_desativado);
            findViewById(R.id.buttonManutencoes).setVisibility(View.GONE);
            //btManutencoes.setText("Manutenções");
            //btManutencoes.setTextColor(Color.GRAY);
        } else {
            btManutencoes = (Button) findViewById(R.id.buttonManutencoes);
            findViewById(R.id.buttonManutencoes_desativado).setVisibility(View.GONE);
            //btManutencoes.setText("Manutenções");
            //btManutencoes.setTextColor(Color.BLACK);
        }
        btManutencoes.setVisibility(View.VISIBLE);
        //-------------------
    }

    public void initEvents() {

        SharedPreferencesHelper.AppModulo.CONTEXT = currentContext;

        final Toast toastMenu = Toast.makeText(currentContext, "Este módulo está desativado neste tablet.", Toast.LENGTH_SHORT);

        btMovimentacoes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (!SharedPreferencesHelper.AppModulo.estaMovimentacoesAtivado()) {
                    toastMenu.show();
                    return;
                }

                try {
                    checkUserLogged(); // Verifica se o usuário foi informado.
                    checkLocal(); // Verifica se a localização foi informada.

                    IntentParameters objJson = new IntentParameters(getStr(R.string.OPTION_MENU_MOV), intentParameters.getUserSession());
                    refreshIntentParameters(objJson);

                    redirect(EquipamentosMovimentacaoDiariaActivity.class, false);
                } catch (AlertException e) {
                    tratarExcecaoRedirecionando(e);
                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }
        });

        btEquipamentos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (!SharedPreferencesHelper.AppModulo.estaEquipamentosAtivado()) {
                    toastMenu.show();
                    return;
                }

                try {
                    checkUserLogged(); // Verifica se o usuário foi informado.
                    checkLocal(); // Verifica se a localização foi informada.

                    IntentParameters objJson = new IntentParameters(getStr(R.string.OPTION_MENU_EQP), intentParameters.getUserSession());
                    refreshIntentParameters(objJson);

                    redirect(EquipamentosParteDiariaActivity.class, false);
                } catch (AlertException e) {
                    tratarExcecaoRedirecionando(e);
                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }
        });

        btAbastecimento.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (!SharedPreferencesHelper.AppModulo.estaAbastecimentosAtivado()) {
                    toastMenu.show();
                    return;
                }

                try {
                    checkPosto();

                    IntentParameters objJson = new IntentParameters(getStr(R.string.OPTION_MENU_ABS), intentParameters.getUserSession());
                    refreshIntentParameters(objJson);

                    verifyLogin(AbastecimentosSearchActivity.class);
                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }
        });

        btMaoDeObra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!SharedPreferencesHelper.AppModulo.estaMaoDeObraAtivado()) {
                    toastMenu.show();
                    return;
                }

                try {
                    checkUserLogged(); // Verifica se o usuário foi informado.

                    IntentParameters intent = new IntentParameters(getStr(R.string.OPTION_MENU_MAO), intentParameters.getUserSession());
                    refreshIntentParameters(intent);

                    Intent intent1 = new Intent(getBaseContext(), MaoObraServicoGridActivity.class);
                    intent1.putExtra(getStr(R.string.STRING_INTENT_PARAMS), intentParameters);
                    startActivity(intent1);
                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }
        });

        btIndicePluviometrico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!SharedPreferencesHelper.AppModulo.estaIndicesPluviometricosAtivado()) {
                    toastMenu.show();
                    return;
                }

                try {
                    checkUserLogged(); // Verifica se o usuário foi informado.
                    checkLocal(); // Verifica se a localização foi informada.

                    IntentParameters intent = new IntentParameters(getStr(R.string.OPTION_MENU_PLU), intentParameters.getUserSession());
                    refreshIntentParameters(intent);

                    Intent intent1 = new Intent(getBaseContext(), IndicePluviometricoServicoGridActivity.class);
                    intent1.putExtra(getStr(R.string.STRING_INTENT_PARAMS), intentParameters);
                    startActivity(intent1);
                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }
        });

        btManutencoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!SharedPreferencesHelper.AppModulo.estaManutencoesAtivado()) {
                    toastMenu.show();
                    return;
                }

                startActivity(new Intent(getBaseContext(), ManutencaoListaEquipamentoActivity.class));
            }
        });
    }

    @Override
    public Detail getDetailValues() throws Exception {
        //Texto do Detail
        String strDetail = getStr(R.string.TITLE_MNU);
        Detail detail = new Detail(this);
        detail.setDetail(strDetail);
        detail.setColorBKG(getColor(R.color.GRAY));// Cor de fundo
        detail.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        detail.setFileLayoutRow(References.DETAIL_LAYOUT); //arquivo xml - layout (TableRow)
        detail.setIdColumns(References.DETAIL_ID_COLUMNS);// Ids (xml) das colunas
        detail.setIdTable(References.DETAIL_ID_MENU); //Id do TableLayout);
        return detail;
    }


    private void validarDataTablet() throws AlertException {
        Date dataVersao = getStr(R.string.DATA_ULT_VERSAO) != null ? Util.toDateFormat(getStr(R.string.DATA_ULT_VERSAO)) : new Date();
        Date dataTablet = new Date();

        if (dataTablet.before(dataVersao)) {
            throw new AlertException(getStr(R.string.ALERT_DATA_TABLET_INVALIDA));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        action = item.getItemId();
        AlertDialog.Builder dialog = new AlertDialog.Builder(currentContext);
        dialog.setTitle(getStr(R.string.AVISO));

        switch (action) {

            //LOCALIZACAO----------------------------------------------------------------------------------------------------------
            case R.id.local:
                redirect(LocalizacaoActivity.class, false);
                break;
            //---------------------------------------------------------------------------------------------------------------------


            //RELATORIOS DE ENVIO DE ARQUIVOS---------------------------------------------------------------------------------------
            case R.id.relatorio:
                Intent intent = new Intent(getBaseContext(), RelatorioEnvioActivity.class);
                intent.putExtra(getStr(R.string.STRING_INTENT_PARAMS), intentParameters);
                startActivity(intent);
                break;
            //----------------------------------------------------------------------------------------------------------------------


            //SOLICITACAO DE SENHA PARA ACESSAR:
            // -CONFIGURACOES/
            // -DOWNLOAD DE NOVA VERSAO
            // -ATIVACAO E/OU DESATIVACAO DE MODULOS
            // ---------------------------------------------------------------------------------------------------------------------
            case R.id.about:
            case R.id.app:
            case R.id.modulos:
                final EditText input = new EditText(this);
                AlertDialog.Builder dialogPassw = new AlertDialog.Builder(this);
                dialogPassw.setTitle(getStr(R.string.informe_senha));
                input.setTransformationMethod(PasswordTransformationMethod.getInstance());
                dialogPassw.setView(input);
                dialogPassw.setPositiveButton(getStr(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        //SENHA DEFINIDA PELO USUARIO ADM OU SENHA PADRAO DA TI 210419 PODE SER USADA CASO A OBRA ESQUEÇA A SENHA.
                        if (input.getText().toString().equals(config.getReferencia()) || input.getText().toString().equals(AppSecurity.DEFAULT_REF)) {

                            switch (action) {
                                case R.id.about:
                                    redirect(ConfiguracoesActivity.class, false);
                                break;
                                case R.id.app:
                                    DownloadAPK.VersaoMaisNova download = new DownloadAPK(currentContext).new VersaoMaisNova();
                                    download.execute(PrincipalActivity.this);
                                break;
                                case R.id.modulos:
                                    startActivity(new Intent(currentContext, ModulosActivity.class));
                                break;
                                default:
                                break;
                            }
                        } else {
                            Util.viewMessage(currentContext, getStr(R.string.ALERT_SENHA_INVALIDA));
                        }
                    }
                });
                dialogPassw.setNegativeButton(getStr(R.string.CANCELAR), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                dialogPassw.show();
                break;
            //----------------------------------------------------------------------------------------------

            //IMPORTACAO DE DADOS PARA O TABLET-------------------------------------------------------------
            case R.id.importa:
                dialog.setMessage(getStr(R.string.ALERT_UPDATE));
                dialog.setPositiveButton(getStr(R.string.SIM), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface di, int arg) {
                        Importador.DadosParaOtablet importador = new Importador(currentContext).new DadosParaOtablet();
                        importador.execute();
                    }
                });
                dialog.setNegativeButton(getStr(R.string.NAO), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface di, int arg) {
                    }
                });
                dialog.show();
                break;
            //---------------------------------------------------------------------------------------------

            //EXPORTACAO DE DADOS PARA SERVIDOR------------------------------------------------------------
            case R.id.exporta:
                dialog.setMessage(getStr(R.string.ALERT_DESCARREGAR_INFO));
                dialog.setPositiveButton(getStr(R.string.SIM), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface di, int arg) {

                        Exportador.ArquivoParaServidor exportador = new Exportador(currentContext).new ArquivoParaServidor();
                        exportador.execute();

                        Exportador.LogAuditoriaParaServidor exportadorLog = new Exportador(currentContext).new LogAuditoriaParaServidor();
                        exportadorLog.execute();
                    }
                });
                dialog.setNegativeButton(getStr(R.string.NAO), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface di, int arg) {
                    }
                });
                dialog.show();
                break;
            default:
                break;
            ///--------------------------------------------------------------------------------------------
        }
        return true;
    }

    @Override
    public void editValues() throws Exception {

        config = getDAO().getConfiguracoesDAO().getConfiguracaoVO();

        String version = Util.getNumVersaoApp(currentContext);
        String tablet = config.getDispositivo();
        String obra = config.getIdObra().toString();
        String user = config.getNomeUsuario();

        rodape.setText(Util.getString(currentContext, new String[]{tablet, version, obra}, R.string.DESC_RODAPE_PRINCIPAL));
        rodape2.setText(Util.getString(currentContext, user, R.string.DESC_RODAPE_AUXILIAR));
        descServidor.setText("Base: "+config.getServidor());

    }

    @Override
    public void editScreen() throws Exception {
    }

    @Override
    public void initAtributes() throws Exception {
        rodape = (TextView) findViewById(R.id.descRodape);
        rodape2 = (TextView) findViewById(R.id.descVersao);
        descServidor = (TextView)findViewById(R.id.descServidor);
    }

    @Override
    public void validateFields() throws Exception {
    }


    public void verificaSDcard(){
        if(!AppDirectory.isSDPresent()){
            AlertDialog.Builder dialogo = new AlertDialog.Builder(currentContext);
            dialogo.setMessage("Um cartão de memória não está presente. O aplicativo não poderá ser utilizado.");
            dialogo.setPositiveButton((R.string.OK), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface di, int arg) {
                    finish();
                }
            });
            dialogo.setTitle(R.string.AVISO);
            dialogo.show();
        }
    }


    public void verificarDiretorios() {

        String msg = AppDirectory.getDirectoryCheckMessage();
        if (!"".equals(msg)) {
            AlertDialog.Builder dialogo = new AlertDialog.Builder(currentContext);
            dialogo.setMessage(msg);
            dialogo.setPositiveButton((R.string.OK), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface di, int arg) {
                    if (AppDirectory.dirCreationHasError()) {
                        finish();
                    }
                }
            });
            dialogo.setTitle(R.string.AVISO);
            dialogo.show();
        }
    }

    /*
    protected void downloadAPK_FTP(String nomeArquivo) throws Exception {

        FTPClient ftp = new FTPClient();
        ftp.connect(config.getServidor(), Integer.parseInt(config.getPortaFtp()));
        ftp.login(getStr(R.string.FTP_USER), getStr(R.string.FTP_PASS));
        ftp.changeWorkingDirectory(getStr(R.string.PATH_APK_FTP));
        FileOutputStream stream = new FileOutputStream(AppDirectory.PATH_INSTALL + "/" + nomeArquivo);
        ftp.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);//FTPClient.BINARY_FILE_TYPE
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);//FTPClient.BINARY_FILE_TYPE

        boolean sucess = ftp.retrieveFile(nomeArquivo, stream);

        ftp.logout();
        ftp.disconnect();

        //INICIANDO INSTALAÇÃO----------------------------------------------------------------------------------------------------------
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(AppDirectory.PATH_INSTALL + "/" + nomeArquivo)), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //-----------------------------------------------------------------------------------------------------------------------------

        if (!sucess) {
            throw new AlertException(Util.getMessage(currentContext, nomeArquivo, R.string.ERROR_DOWNLOAD_FILE));
        }
    }
    */

    /*
    class DownloadAPP extends AsyncTask {

        public DownloadAPP(Context context) {
            super(context);
        }

        @Override
        protected String doInBackground(Void... params) {

            filename = "Constran.apk";

            try {
                if (isOnline())
                    downloadAPK_FTP(filename);
                else
                    return getStr(R.string.ALERT_OFFLINE);

            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }

            return Util.getMessage(currentContext, filename, R.string.ALERT_SUCESS_COPY);
        }
    }
    */

    /*
    public boolean isExiststApontamentos() throws Exception {
        boolean existsApontamentos = getDAO().getUtilDAO().existsApontamentos();
        initSQL();
        return existsApontamentos;
    }

    public boolean isConnectSucessfulServer() throws Exception {
        String server = getStr(R.string.URL_SERVER).replaceAll(getStr(R.string.TOKEN), config.getServidor());
        return ping(server, 5000);
    }
    */

    /*
    class DescarregarInfos extends AsyncTaskBar {
        public DescarregarInfos(Context context) {
            super(context);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                if (isOnline()) {
                    if (!isConnectSucessfulServer()) {
                        return getStr(R.string.ALERT_SERVER_OFFLINE);
                    }
                } else {
                    return getStr(R.string.ALERT_OFFLINE);
                }

                if (!isExiststApontamentos()) {
                    return getStr(R.string.ALERT_BLANK_APROP_CREATE);
                }

                ProgressDialog progress = getProgress();
                progress.setProgress(progress.getProgress() + 10);

                List<String> datas = getDAO().getUtilDAO().getDatesToExport();

                // resta 90% da progressbar para preencher...
                // 90 / qtdDias = % de cada dia
                int porcentDia = 90 / datas.size();

                for (String date : datas) {
                    //progress.setMessage("Enviando apontamentos do dia " + date);
                    ExportMobileDate exmd = exportInfos(date);

                    if (!sendInfos(exmd)) {
                        return "Erro ao descarregar apontamentos do dia " + date + ", tente novamente! ";
                    }

                    LogEnvioInformacoesVO log =
                    new LogEnvioInformacoesVO(Util.getFileExportFormated(exmd.getCcObra(), exmd.getDate(), exmd.getDispositivo()),exmd.getDate(), exmd.getCcObra(), Util.getNow());
                    getDAO().getLogEnvioInformacoesDAO().save(log);

                    progress.setProgress(progress.getProgress() + porcentDia);
                }

                getDAO().getUtilDAO().clearTables(true, false, false);

            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
            return getStr(R.string.ALERT_SUCESS_DESCARREGAMENTO);
        }
    }
    */


    /*
    protected boolean validaArquivoEnviado(File file) throws Exception {
        String urlPath1 = config.getUrlExport(currentContext).concat(Util.getFoldersByDate()).concat(getStr(R.string.BAR)).concat(file.getName());
        String urlPath2 = config.getUrlExportRoot(currentContext).concat(file.getName());

        //verifica se existe arquivo no servidor usando HTTP
        boolean exists = Util.isExistsRemoteFile(urlPath1);

        //verifica se existe arquivo no servidor usando FTP
        if (!exists) {
            String pathFiles = getStr(R.string.DIR_FTP_LIST).replaceAll(getStr(R.string.TOKEN), String.valueOf(config.getIdObra()));
            pathFiles = pathFiles.concat(Util.getFoldersByDate()).concat(getStr(R.string.BAR));

            FTPFile[] ftpFiles = getFtpClient().listFiles(pathFiles);
            exists = existeArquivoNoServidor(file.getName(), ftpFiles);
        }

        //verifica se existe arquivo na RAIZ do servidor usando HTTP
        if (!exists) {
            exists = Util.isExistsRemoteFile(urlPath2);
        }

        //apaga o arquivo json do tablet
        if (exists && file.exists()) {
            file.delete();
        }

        return exists;
    }
    */

        /*
    class ChangeBD extends AsyncTaskBar {
        public ChangeBD(Context context) {
            super(context);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {


                if (isExiststApontamentos()) {

                    file = Util.getFileExport(currentContext, config);

                    if (isOnline()) {

                        if (!validaArquivoEnviado(file)) {

                            return Util.getMessage(currentContext, file.getName(), R.string.ALERT_FILE_NOT_SEND);
                        }

                    } else {

                        return getStr(R.string.ALERT_OFFLINE);
                    }
                }

                ProgressDialog progress = getProgress();
                progress.setProgress(progress.getProgress() + 3);

                ImportMobile vo = Util.getImportVO(currentContext, config);

                progress.setProgress(progress.getProgress() + 3);
                getDAO().getUtilDAO().clearTables(true, true, false);

                //removeUserSession();

                //rodape2.setText(Util.getString(currentContext, "", R.string.DESC_RODAPE_AUXILIAR));

                saveImport(vo, getProgress());

                file = Util.getFileImport(currentContext, config);

                file.delete();

            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
            return getStr(R.string.ALERT_SUCESS_UPDATE);
        }
    }
    */


    /*
    class SendFile extends AsyncTask {
        public SendFile(Context context) {
            super(context);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                if (isOnline()) {
                    file = getFileExport();

                    boolean ok = envioFTP(file, false);
                    if (!ok)
                        ok = envioFTP(file, true);
                    if (!ok)
                        return getStr(R.string.ERROR_SEND_FILE);
                    filename = file.getName();

                } else {
                    return getStr(R.string.ALERT_OFFLINE);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
            return Util.getMessage(currentContext, filename, R.string.ALERT_SUCESS_SEND);
        }
    }
    */


    /*
    class DownloadAPKFile extends AsyncTask {
        public DownloadAPKFile(Context context) {
            super(context);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                if (isOnline()) {
                    filename = Util.getNameFileImport(config.getIdObra());
                    downloadAPK_FTP(filename);
                } else
                    return getStr(R.string.ALERT_OFFLINE);

            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }

            return Util.getMessage(currentContext, filename, R.string.ALERT_SUCESS_COPY);
        }
    }
    */


    /*
    protected void downloadFTP(String nomeArquivo, int tipoArquivo) throws Exception {

        FTPClient ftp = new FTPClient();

        String diretorioServidor = "";
        String dirSdCard = "";

        if (tipoArquivo == FTPClient.ASCII_FILE_TYPE) {
            diretorioServidor = getStr(R.string.PATH_IMPORT_FTP).replaceAll(getStr(R.string.TOKEN), getStr(R.string.DIR_FILES).concat(String.valueOf(config.getIdObra())));
            dirSdCard = getStr(R.string.DIR_IMPORT);
        } else {
            dirSdCard = getStr(R.string.DIR_APK);
            diretorioServidor = getStr(R.string.PATH_APK_FTP);
        }

        dirSdCard += "/";

        ftp.connect(config.getServidor(), Integer.parseInt(config.getPortaFtp()));
        ftp.login(getStr(R.string.FTP_USER), getStr(R.string.FTP_PASS));
        ftp.changeWorkingDirectory(diretorioServidor);
        FileOutputStream stream = new FileOutputStream(SD_CARD + dirSdCard + nomeArquivo);
        ftp.setFileTransferMode(tipoArquivo);//FTPClient.BINARY_FILE_TYPE
        ftp.setFileType(tipoArquivo);//FTPClient.BINARY_FILE_TYPE

        if (tipoArquivo == FTPClient.ASCII_FILE_TYPE) {
            ftp.setAutodetectUTF8(false);
            ftp.setControlEncoding(getStr(R.string.UTF_8));
            ftp.setCharset(Charset.forName(getStr(R.string.UTF_8)));
        }

        boolean sucess = ftp.retrieveFile(nomeArquivo, stream);

        ftp.logout();
        ftp.disconnect();

        if (!sucess) {
            throw new AlertException(Util.getMessage(currentContext, nomeArquivo, R.string.ERROR_DOWNLOAD_FILE));
        }
    }
    */

    /*
    class CreateFile extends AsyncTask {
        public CreateFile(Context context) {
            super(context);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                if (!isExiststApontamentos()) {
                    return getStr(R.string.ALERT_BLANK_APROP_CREATE);
                }

                filename = exportJson();

            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
            return Util.getMessage(currentContext, filename, R.string.ALERT_SUCESS_GENERATION);
        }
    }
    */


    /*
    protected boolean envioFTP(File localFile, boolean raiz) throws Exception {

        if (!localFile.exists()) {
            throw new AlertException(getStr(R.string.ALERT_GENERATE_FILE));
        }

        Reader reader = new FileReader(localFile);
        ExportMobile vo = Util.GSON_API.fromJson(reader, ExportMobile.class);

        if (vo.getApropriacoes() == null && vo.getApropriacoes().isEmpty() && vo.getRaes() == null && vo.getRaes().isEmpty()) {
            localFile.delete();
            throw new AlertException(getStr(R.string.ALERT_EXPORT_FILE_INVALID));
        }

        String diretorioServidor = getStr(R.string.PATH_EXPORT_FTP).replaceAll(getStr(R.string.TOKEN), getStr(R.string.DIR_FILES).concat(String.valueOf(config.getIdObra())));

        String urlPath = config.getUrlExport(currentContext);

        String pathFiles = getStr(R.string.DIR_FTP_LIST).replaceAll(getStr(R.string.TOKEN), String.valueOf(config.getIdObra()));

        if (!raiz) {
            diretorioServidor = diretorioServidor.concat(getStr(R.string.BAR)).concat(Util.getFoldersByDate());
            urlPath = urlPath.concat(Util.getFoldersByDate()).concat(getStr(R.string.BAR));
            pathFiles = pathFiles.concat(Util.getFoldersByDate()).concat(getStr(R.string.BAR));
        }

        String nomeArquivoLocal = localFile.getName();
        String nomeArquivoEnviar = nomeArquivoLocal;

        FTPClient ftp = getFtpClient();

        boolean exists = false;

        FTPFile[] ftpFiles = ftp.listFiles(pathFiles);

        //verifica se ja existe um arquivo no servidor com esse nome
        exists = existeArquivoNoServidor(nomeArquivoLocal, ftpFiles);

        //se ja existir, renomeia o arquivo que vai ser enviado
        if (exists)
            nomeArquivoEnviar = nomeArquivoLocal.replace(".json", "_".concat(Util.getNamePartCopyByDate()).concat(".json"));

        urlPath = urlPath.concat(nomeArquivoEnviar);

        //conecta via ftp ao servidor e envia/armazzena o arquivo json
        FileInputStream stream = new FileInputStream(localFile.getCanonicalPath());
        ftp.setFileTransferMode(FTPClient.ASCII_FILE_TYPE);
        ftp.setFileType(FTPClient.ASCII_FILE_TYPE);
        ftp.changeWorkingDirectory(diretorioServidor);
        ftp.storeFile(nomeArquivoEnviar, stream);

        //verifica se o arquivo enviado foi salvo (se foi salvo, ele vai ser listado no diretorio)
        boolean sucess = !raiz ? existeArquivoNoServidor(nomeArquivoLocal, ftp.listFiles()) : Util.isExistsRemoteFile(urlPath);

        ftp.logout();
        ftp.disconnect();

        if (sucess)
            localFile.delete();

        return sucess;

    }
    */

        /*
    private boolean existeArquivoNoServidor(String nomeArquivoLocal, FTPFile[] ftpFiles) {
        for (FTPFile ftpFile : ftpFiles) {
            if (nomeArquivoLocal.equals(ftpFile.getName())) {
                return true;
            }
        }
        return false;
    }
    */

        /*
    private void configureLog4j() {
        String fileName = Util.getPathLog() + "log.log";
        //String filePattern = "%d - [%c] - %p : %m%n";
        String filePattern = "%d %-5p [%C:%M] - %m%n";
        int maxBackupSize = 10;
        long maxFileSize = 1024 * 1024;
    }
    */

        /*
    private FTPClient getFtpClient() throws Exception {
        FTPClient ftp = new FTPClient();
        ftp.connect(config.getServidor(), Integer.parseInt(config.getPortaFtp()));
        ftp.login(getStr(R.string.FTP_USER), getStr(R.string.FTP_PASS));
        ftp.setAutodetectUTF8(false);
        ftp.setControlEncoding(getStr(R.string.UTF_8));
        ftp.setCharset(Charset.forName(getStr(R.string.UTF_8)));
        return ftp;
    }
    */

//DESATIVADOS POR MOTIVO DE MELHOR E MAIS ROBUSTA IMPLEMENTACAO

    /*
    public void executar() {

        AlertDialog.Builder dialogo = new AlertDialog.Builder(currentContext);

        if (action == R.id.local) {

            redirect(LocalizacaoActivity.class, false);
        }
        else if (action == R.id.relatorio) {

            Intent intent1 = new Intent(getBaseContext(), RelatorioEnvioActivity.class);
            intent1.putExtra(getStr(R.string.STRING_INTENT_PARAMS), intentParameters);
            startActivity(intent1);
        }
        else if (action == R.id.about || action == R.id.app || action == R.id.modulos) {

            final EditText input = new EditText(this);

            AlertDialog.Builder editalert = new AlertDialog.Builder(this);
            editalert.setTitle(getStr(R.string.informe_senha));
            input.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editalert.setView(input);
            editalert.setPositiveButton(getStr(R.string.OK), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    //SENHA DEFINIDA PELO USUARIO ADM OU SENHA PADRAO DA TI 210419 PODE SER USADA CASO A OBRA ESQUEÇA A SENHA.
                    if (input.getText().toString().equals(config.getReferencia()) || input.getText().toString().equals(AppSecurity.DEFAULT_REF)) {

                        if (action == R.id.about) {
                            redirect(ConfiguracoesActivity.class, false);
                        }
                        else if (action == R.id.app) {
                            DownloadAPP action = new DownloadAPP(currentContext);
                            action.execute();
                        }
                        else if(action == R.id.modulos){
                            startActivity(new Intent(currentContext,ModulosActivity.class));
                        }
                    }else{
                        Util.viewMessage(currentContext, getStr(R.string.ALERT_SENHA_INVALIDA));
                    }
                }
            });
            editalert.setNegativeButton(getStr(R.string.CANCELAR), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            editalert.show();
        }
        else if (action == R.id.importa) {

            dialogo.setMessage(getStr(R.string.ALERT_UPDATE));
            dialogo.setPositiveButton(getStr(R.string.SIM), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface di, int arg) {
                    //if (action == R.id.importa) {
                        //ImportarInfos action = new ImportarInfos(currentContext);
                        //action.execute();
                    //}
                }
            });
            dialogo.setNegativeButton(getStr(R.string.NAO), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface di, int arg) {
                }
            });
            dialogo.setTitle(getStr(R.string.AVISO));
            dialogo.show();
        }
        else if (action == R.id.exporta) {

            dialogo.setMessage(getStr(R.string.ALERT_DESCARREGAR_INFO));
            dialogo.setPositiveButton(getStr(R.string.SIM), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface di, int arg) {
                    //if (action == R.id.exporta) {
                        DescarregarInfos d = new DescarregarInfos(currentContext);
                        d.execute();
                    //}
                }
            });
            dialogo.setNegativeButton(getStr(R.string.NAO), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface di, int arg) {
                }
            });
            dialogo.setTitle(getStr(R.string.AVISO));
            dialogo.show();
        }
    }
    */

//IMPLEMENTACAO ORIGINAL
    /*
    class ImportarInfos extends AsyncTaskBar {

        public ImportarInfos(Context context) {
            super(context);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                if (isOnline()) {
                    if (!isConnectSucessfulServer()) {
                        return getStr(R.string.ALERT_SERVER_OFFLINE);
                    }
                } else {
                    return getStr(R.string.ALERT_OFFLINE);
                }

                List<String> datas = getDAO().getUtilDAO().getDatesToExport();


                if (datas.size() > 0) {
                    return getStr(R.string.ALERT_APONTAMENTOS_EXISTENTES);
                }
                else {


                    ProgressDialog progress = getProgress();
                    progress.setProgress(progress.getProgress() + 3);

                    ImportMobile vo = getInfos(currentContext, config.getIdObra().toString(), config);

                    progress.setProgress(progress.getProgress() + 3);

                    getDAO().getUtilDAO().clearTables(true, true, false);
                    saveImport(vo, getProgress());
                }

            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
            return getStr(R.string.ALERT_SUCESS_ATUALIZACAO);
        }
    }
    */
}