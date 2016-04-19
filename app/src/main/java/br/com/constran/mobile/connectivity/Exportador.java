package br.com.constran.mobile.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import br.com.constran.mobile.R;
import br.com.constran.mobile.exception.ExportacaoApontamentosException;
import br.com.constran.mobile.exception.UsuarioNaoInformadoException;
import br.com.constran.mobile.model.LogCollectionWrapper;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.DAOFactory;
import br.com.constran.mobile.persistence.vo.imp.json.ExportMobileDate;
import br.com.constran.mobile.persistence.vo.menu.ConfiguracoesVO;
import br.com.constran.mobile.persistence.vo.menu.LogEnvioInformacoesVO;
import br.com.constran.mobile.system.AppDirectory;
import br.com.constran.mobile.system.NetworkUtils;
import br.com.constran.mobile.system.SharedPreferencesHelper;
import br.com.constran.mobile.view.util.Util;

/**
 * Created by moises_santana on 22/05/2015.
 */
public class Exportador {

    Context context = null;
    private ProgressDialog progressE = null;
    private final Integer PROGRESS_INCREMENT = 10;
    private ExportMobileDate exportMobileDateVO = null;
    private StringBuilder msgDialog = new StringBuilder();
    private boolean houveErroHttp = false;
    private boolean faltaDados = false;
    private ConfiguracoesVO config = null;
    private LogEnvioInformacoesVO log = null;
    boolean somenteLOG = false;

    public Exportador(Context context){
        this.context = context;
    }

    public DAOFactory getDAO(){
        return DAOFactory.getInstance(context);
    }

    public class ArquivoParaServidor extends AsyncTask <Void, String, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressE = new ProgressDialog(context);
            progressE.setTitle(context.getResources().getString(R.string.AGUARDE));
            progressE.setMessage(context.getResources().getString(R.string.EM_ANDAMENTO));
            progressE.setCancelable(false);
            progressE.setIndeterminate(false);
            progressE.setProgress(0);
            progressE.setMax(100);
            progressE.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressE.show();

            addProgress("Verificando conexão com a internet...");
            if(NetworkUtils.isNetworkUnavailable()){
                msgDialog.append("Necessário uma conexão ativa com a internet.\n");
                cancel(true);
            }
            else
            addProgress("Verificando se há dados para exportar...");
            if(naoExisteApotamentos()){
                msgDialog.append("Não existem apontamentos registrados no banco de dados !\n");
                cancel(true);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if(isCancelled()) {
                return null;
            }

            AppHTTP http = new AppHTTP();
            http.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

            config = getDAO().getConfiguracoesDAO().getConfiguracaoVO();

            addProgress("Obtendo datas de apontamentos...");
            List<String> datas = getDAO().getUtilDAO().getDatesToExport();

            try {

                for (String destaData : datas) {

                    exportMobileDateVO = obterObjetoParaExportacao(destaData);

                    addProgress("Exportando dados do dia " + destaData);

                    //sera desativado***
                   //http.connect(AppHTTP.POST, AppDirectory.getURLserver(), exportMobileDateVO);

                    //http.connect(AppHTTP.POST, "http://200.182.0.68:8080/samcentral/ws/exporta/arquivoParaServidor",exportMobileDateVO);

                    //este é o novo que sera posto em produção em breve
                    http.connect(AppHTTP.POST, AppDirectory.getExportaDadosRESTpath(),exportMobileDateVO);

                    if(http.getResponseCode() != 200) {
                        throw new ExportacaoApontamentosException("\n\nOs apontamentos do dia "+ destaData+" e os de damais datas (caso hajam) não foram exportados. HTTP "+http.getResponseCode());
                    }

                    addProgress("Gerando log de exportação...");
                    salvarLogEnvio();
                }

                addProgress("Limpando tabelas...");
                getDAO().getUtilDAO().clearTables(true, false, false);

                msgDialog.append("Apontamentos exportados com sucesso!");

            } catch (NullPointerException e) {

                houveErroHttp = true;
                e.printStackTrace();
                msgDialog.append("Objeto de importação está nulo.")
                        .append("\n").append("mantenha a calma e tente novamente.")
                        .append("\n\n").append("Detalhes/Causa:")
                        .append("\n").append(e.getMessage()).append("\n")
                        .append("HTTP Status: ").append(http.getResponseCode());

            } catch (SocketTimeoutException e) {

                    houveErroHttp = true;
                    e.printStackTrace();
                    msgDialog.append("O servidor não respondeu.")
                             .append("\n").append("Sua internet ou o serviço remoto estão apresentando lentidão. Tente novamente.")
                             .append("\n\n").append("Detalhes/Causa:")
                             .append("\n").append(e.getMessage());

            } catch (IOException e) {

                houveErroHttp = true;
                e.printStackTrace();
                msgDialog.append("Não foi possível processar a requisição.");
                msgDialog.append("\n");
                msgDialog.append("Sua conexão com a internet ou o servidor remoto podem estar com problemas.");
                msgDialog.append("\n");
                msgDialog.append("Falha de comunicação em rede. Tente novamente.");
                msgDialog.append("\n\n").append("Detalhes/Causa:").append("\n").append(e.getMessage());

            } catch (UsuarioNaoInformadoException e) {

                faltaDados = true;
                e.printStackTrace();
                msgDialog.append("Parâmetro requerido.").append("\n\n").append("Detalhes/Causa:").append("\n").append(e.getMessage());

            } catch (ExportacaoApontamentosException e) {

                houveErroHttp = true;
                e.printStackTrace();
                msgDialog.append("Não foi possível exportar os dados.").append("\n\n").append("Detalhes/Causa:").append("\n").append(e.getMessage());
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            onPostExecute(null);
        }

        public void addProgress(String newMessage) {
            if(progressE.getProgress() < progressE.getMax()){
                publishProgress(String.valueOf(progressE.getProgress()+PROGRESS_INCREMENT),newMessage);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            progressE.setMessage(values[1]);
            progressE.setProgress(Integer.valueOf(values[0]));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressE.dismiss();
            if(houveErroHttp || faltaDados){
                if(faltaDados)msgDialog.append("\n").append("Nenhum dado foi exportado. Por favor entre em contato com o suporte técnico.");
                Util.viewErrorMessage(context, msgDialog.toString());
            }
            else{
                Util.viewMessage(context,msgDialog.toString());
            }

            /*
            AlertDialog.Builder dialogo = new AlertDialog.Builder(context);
            dialogo.setMessage("Aproveitar e baixar os cadastros?");
            dialogo.setPositiveButton(context.getResources().getString(R.string.SIM), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface di, int arg) {
                    Importador.DadosParaOtablet importador = new Importador(context).new DadosParaOtablet();
                    importador.execute();
                }
            });
            dialogo.setNegativeButton(context.getResources().getString(R.string.NAO), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface di, int arg) {

                }
            });
            dialogo.setTitle("AVISO");
            dialogo.show();
            */
        }

        public boolean naoExisteApotamentos(){
            return !getDAO().getUtilDAO().existsApontamentos();
        }

        public ExportMobileDate obterObjetoParaExportacao(String estaData) throws UsuarioNaoInformadoException {

            Query queryMOV = getDAO().getUtilDAO().getQueryMovimentacoesExportByDate(estaData);
            Query queryEQP = getDAO().getUtilDAO().getQueryParteDiariaExportByDate(estaData);
            Query queryABS = getDAO().getUtilDAO().getQueryAbastecimentoLubrificacaoExportByDate(estaData);
            Query queryMAN = getDAO().getUtilDAO().getQueryManutencaoExportByDate(estaData);

            ExportMobileDate exportMobileDados = getDAO().getUtilDAO().getObjJsonDate(
                    new Query[]{queryMOV, queryEQP, queryABS,queryMAN},
                    new String[]{
                            context.getResources().getString(R.string.OPTION_MENU_MOV),
                            context.getResources().getString((R.string.OPTION_MENU_EQP)),
                            context.getResources().getString((R.string.OPTION_MENU_ABS)),
                            context.getResources().getString((R.string.OPTION_MENU_MAN))
                    }
            );

            //valida usuario preenchido para apropriacoes
            validaUsuarioConfig(exportMobileDados);

            preencheUsuario();

            exportMobileDados.setConfiguracoes(config);
            exportMobileDados.setSaldoPosto(getDAO().getAbastecimentoPostoDAO().getSaldoPostoVO(config.getIdObra()));
            exportMobileDados.setIndicesPluviometricos(getDAO().getIndicePluviometricoDAO().findAllItems());

            exportMobileDados.setDispositivo(config.getDispositivo());
            exportMobileDados.setCcObra(config.getIdObra().toString());
            exportMobileDados.setDate(estaData);

            return exportMobileDados;
        }

        /**
         * Valida se existe apropriacao e o usuario nao foi configurado no dispositivo, lancando excecao
         * Se nao existir abastecimento e o idUsuarioPessoal nao e nulo, entao o idUsuario = idUsuarioPessoal
         *
         * @param objJSon
         * @throws Exception
         */
        private void validaUsuarioConfig(ExportMobileDate objJSon) throws UsuarioNaoInformadoException {

            if (config.getIdUsuario() == null && config.getIdUsuarioPessoal() == null && objJSon.getApropriacoes() != null && !objJSon.getApropriacoes().isEmpty()) {
                throw new UsuarioNaoInformadoException(context.getResources().getString(R.string.ALERT_USUARIO_NAO_INFORMADO));
            }
            else if (config.getIdUsuarioPessoal() != null && (objJSon.getRaes() == null || objJSon.getRaes().isEmpty())) {
                config.setIdUsuario(config.getIdUsuarioPessoal());
                config.setIdUsuarioPessoal(null);
            }
        }

        /**
         * Preenche ambos os IDs usuarios de configuração
         * idUsuarioPessoal é apontado quando existe abastecimentos (posto e RAEs)
         * idUsuario é apontando para as demais apropriacoes (Movimentacoes, Equipamentos, Mao de obra)
         * <p/>
         * Tratamento é feito para evitar que hava problemas se existir algum apontador que faça tanto abastecimento quanto apropriacao de viagens/equipamentos/MO
         * Ate a data 09/09/2014 não era possível na prática que um apontador de abastecimentos realizasse outro tipo de apontamento.
         */
        private void preencheUsuario() {
            if (config.getIdUsuarioPessoal() != null && config.getIdUsuario() == null) {
                config.setIdUsuario(config.getIdUsuarioPessoal());
            } else if (config.getIdUsuarioPessoal() == null && config.getIdUsuario() != null) {
                config.setIdUsuarioPessoal(config.getIdUsuario());
            }
        }

        public void salvarLogEnvio(){
            log = new LogEnvioInformacoesVO(Util.getFileExportFormated(exportMobileDateVO.getCcObra(), exportMobileDateVO.getDate(), exportMobileDateVO.getDispositivo()),exportMobileDateVO.getDate(), exportMobileDateVO.getCcObra(), Util.getNow());
            getDAO().getLogEnvioInformacoesDAO().save(log);
        }
    }


    public class LogAuditoriaParaServidor extends AsyncTask <Void, String, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            if(isCancelled()) return null;
            exportarLogAuditoria();
            return null;
        }

        public void  exportarLogAuditoria(){

            AppHTTP http = null;

            try {

                LogCollectionWrapper logColl = new LogCollectionWrapper();
                logColl.setListaDeLogs(getDAO().getLogAuditoriaDAO().listarTodos());
                logColl.setCcObra(String.valueOf(SharedPreferencesHelper.Configuracao.getIdObra()));

                if(logColl.getListaDeLogs(false).size() == 0){
                    Log.i("LOG_AUDITOR", "NAO HA LOG AUDITORIA PARA ENVIAR");
                    return;
                }

                http = new AppHTTP();
                http.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                http.connect(AppHTTP.POST, AppDirectory.getURLauditoriaServer(), logColl);
                getDAO().getLogAuditoriaDAO().deleteAll();
                Log.i("LOG_AUDITOR", "HTTP RESPONSE CODE " + http.getResponseCode());

            } catch (Exception e) {
                Log.e("LOG_AUDITOR","HTTP RESPONSE CODE " + http.getResponseCode());
                e.printStackTrace();
            }
        }
    }
}