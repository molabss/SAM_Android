package br.com.constran.mobile.connectivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import br.com.constran.mobile.R;
import br.com.constran.mobile.exception.UltimaVersaoAPKnaoEncontradaException;
import br.com.constran.mobile.persistence.dao.DAOFactory;
import br.com.constran.mobile.persistence.vo.menu.ConfiguracoesVO;
import br.com.constran.mobile.system.AppDirectory;
import br.com.constran.mobile.system.NetworkUtils;
import br.com.constran.mobile.view.util.Util;

/**
 * Created by moises_santana on 22/05/2015.
 */
public class DownloadAPK {

    private Context context = null;
    private ProgressDialog progressA = null;
    private final Integer PROGRESS_INCREMENT = 10;
    private StringBuilder msgDialog = new StringBuilder();
    private ConfiguracoesVO config = null;
    private boolean houveErroFTP = false;
    private Activity activityCurrent = null;
    private String fileName = null;

    public DownloadAPK(Context context){
        this.context = context;
    }

    public DAOFactory getDAO(){
        return DAOFactory.getInstance(context);
    }

    public class VersaoMaisNova extends AsyncTask<Activity, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressA = new ProgressDialog(context);
            progressA.setTitle(context.getResources().getString(R.string.AGUARDE));
            progressA.setMessage(context.getResources().getString(R.string.EM_ANDAMENTO));
            progressA.setCancelable(true);
            progressA.setIndeterminate(false);
            progressA.setProgress(0);
            progressA.setMax(100);
            progressA.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressA.show();
        }

        @Override
        protected Void doInBackground(Activity... activities) {

            activityCurrent = activities[0];

            addProgress("Verificando conexão com a internet...");
            if(NetworkUtils.isNetworkUnavailable()){
                msgDialog.append("Necessário uma conexão ativa com a internet.");
                cancel(true);
                return null;
            }

            config = getDAO().getConfiguracoesDAO().getConfiguracaoVO();

            try {

                addProgress("Conectando ao servidor FTP...");

                fileName = downloadAPK_FTP();

                msgDialog.append("Download realizado com sucesso!");

            }
            catch (IOException e) {

                e.printStackTrace();
                houveErroFTP = true;
                msgDialog.append("Ocorreu um erro ao processar o download do arquivo.").append("\n\n").append("Detalhes/Causa:").append("\n").append(e.getMessage());

            } catch (UltimaVersaoAPKnaoEncontradaException e) {

                e.printStackTrace();
                msgDialog.append("A última versão do aplicativo não está diponível para download. Entre em contato com o suporte técnico.").append("\n\n").append("Detalhes/Causa:").append("\n").append(e.getMessage());
            }

            return null;
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
            onPostExecute(null);
        }

        public void addProgress(String newMessage) {
            if(progressA.getProgress() < progressA.getMax()){
                publishProgress(String.valueOf(progressA.getProgress()+PROGRESS_INCREMENT),newMessage);
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
            progressA.setMessage(values[1]);
            progressA.setProgress(Integer.valueOf(values[0]));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressA.dismiss();

            if(houveErroFTP){
                Util.viewErrorMessage(context,msgDialog.toString());
            }
            else{
                Util.viewMessage(context,msgDialog.toString());

                if(fileName != null && !fileName.equals("NOT_FOUND")){
                    sugerirInstalacao();
                }
            }
        }


        private void sugerirInstalacao(){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(AppDirectory.PATH_INSTALL + "/" + fileName)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activityCurrent.startActivity(intent);
        }

        protected String downloadAPK_FTP() throws IOException, UltimaVersaoAPKnaoEncontradaException {

            String ftpDir = context.getResources().getString(R.string.PATH_APK_FTP);

            FTPClient ftp = new FTPClient();
            ftp.connect(config.getServidor(), Integer.parseInt(config.getPortaFtp()));
            ftp.login(context.getResources().getString(R.string.FTP_USER), context.getResources().getString(R.string.FTP_PASS));
            ftp.changeWorkingDirectory(ftpDir);

            addProgress("Procurando pela última versão...");
            FTPFile[] filesArr = ftp.listFiles(ftpDir);
            String fileName = getLastVersionFileName(filesArr);

            if(fileName.equals("NOT_FOUND")){
                throw new UltimaVersaoAPKnaoEncontradaException("Pacote de instalação não encontrado no servidor.");
            }

            addProgress("Arquivo encontrado! "+fileName);

            FileOutputStream stream = new FileOutputStream(AppDirectory.PATH_INSTALL + "/" + fileName);
            ftp.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);//FTPClient.BINARY_FILE_TYPE
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);//FTPClient.BINARY_FILE_TYPE

            addProgress("Efetuando o download de " + fileName+"\n\n por favor aguarde...");
            ftp.retrieveFile(fileName, stream);

            addProgress("Desconectando...");
            ftp.logout();
            ftp.disconnect();

            return fileName;
        }


        private String getLastVersionFileName(FTPFile[] files) {
            //DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fileName = "NOT_FOUND";
            for (FTPFile file : files) {
                //fileName = file.getName().toLowerCase();
                if(file.getName().toLowerCase().contains(AppDirectory.KEY_APK_NAME.toLowerCase())){
                    fileName = file.getName();
                    break;
                }
            }
            return fileName;
        }
    }
}
