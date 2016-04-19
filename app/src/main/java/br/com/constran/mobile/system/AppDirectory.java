package br.com.constran.mobile.system;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import br.com.constran.mobile.R;

/**
 * Created by moises_santana on 07/05/2015.
 */
public class AppDirectory {

    private static final String EXTERNAL_STORAGE_AVAILABLE = getExternalStorageAvailable();

    public static final String PATH_DATABASE = EXTERNAL_STORAGE_AVAILABLE.concat("/Constran/database");
    public static final String PATH_INSTALL = EXTERNAL_STORAGE_AVAILABLE.concat("/Constran/install");
    public static final String PATH_LOG = EXTERNAL_STORAGE_AVAILABLE.concat("/Constran/log");
    public static final String PATH_TEMP = EXTERNAL_STORAGE_AVAILABLE.concat("/Constran/temp");
    public static final String TEMP_ABASTECIMENTO_NMARQUIVO = "temp_abs.json";

    public static final String KEY_APK_NAME = "final";

    private static StringBuilder message = new StringBuilder("");
    private static boolean error = false;

    public AppDirectory(){}

    protected static void ckeckDirectory(){
        createForDatabase();
        createForAPK();
        createForLog();
        createForTempFiles();
    }

    public static boolean dirCreationHasError(){
        boolean errorReturn = error;
        error = false;
        return errorReturn;
    }

    public static String getDirectoryCheckMessage(){
        String msgReturn = message.toString();
        message.delete(0,message.length());
        return msgReturn;
    }

    private static void createForDatabase(){
        if(directotyNotExists(PATH_DATABASE)){
            File path = new File(PATH_DATABASE); path.setReadable(true);path.setExecutable(true);path.setWritable(true);
            boolean created = path.mkdirs();
            if(created){
                message.append("Diretório database criado com sucesso!");
            }else{
                message.append("Diretório database não pôde ser criado!");
                error = true;
            }
        }
    }

    private static void createForAPK(){
        if(directotyNotExists(PATH_INSTALL)){
            boolean created = new File(PATH_INSTALL).mkdirs();
            if(created){
                message.append("\nDiretório install criado com sucesso!");
            }else{
                message.append("\nDiretório install não pôde ser criado!");
                error = true;
            }
        }
    }

    private static void createForLog(){
        if(directotyNotExists(PATH_LOG)){
            boolean created = new File(PATH_LOG).mkdirs();
            if(created){
                message.append("\nDiretório log criado com sucesso!");
            }else{
                message.append("\nDiretório log não pôde ser criado!");
                error = true;
            }
        }
    }

    private static void createForTempFiles(){
        if(directotyNotExists(PATH_TEMP)){
            boolean created = new File(PATH_TEMP).mkdirs();
            if(created){
                message.append("\nDiretório temp criado com sucesso!");
            }else{
                message.append("\nDiretório temp não pôde ser criado!");
                error = true;
            }
        }
    }

    private static boolean directotyNotExists(String path){
        return !(new File(path).exists());
    }

    private static String getExternalStorageAvailable() {

        if(Build.VERSION.SDK_INT > 17){
            return Environment.getExternalStorageDirectory().getPath();
        }
        String[] SD_CARD_PATHS = {"/mnt/extsd/", "/storage/extSdCard/", "/Removable/MicroSD/"};
        String sdPath = "/mnt/extsd/";

        for (int i = 0 ; i < SD_CARD_PATHS.length; i++) {

            if (new File(SD_CARD_PATHS[i]).exists()) {
                sdPath = SD_CARD_PATHS[i];
                break;
            }
        }
        return sdPath;
    }

    //desativar-------------------------|----|----|----|
    public static String getURLserver(){

        SharedPreferencesHelper.Configuracao.CONTEXT = ApplicationInit.CONTEXT;
        String porta = SharedPreferencesHelper.Configuracao.getPortaWeb();
        String servidor = SharedPreferencesHelper.Configuracao.getServidor();

        StringBuilder url = new StringBuilder();
        url.append("http://").append(servidor).append(":").append(porta).append("/").append(ApplicationInit.CONTEXT.getResources().getString(R.string.SAM_CENTRAL_CONTEXT));
        return url.toString();
    }
    //----------------------------------|----|----|----|-


    private static StringBuilder getServerContextRoot(){

        SharedPreferencesHelper.Configuracao.CONTEXT = ApplicationInit.CONTEXT;
        String porta = SharedPreferencesHelper.Configuracao.getPortaWeb();
        String servidor = SharedPreferencesHelper.Configuracao.getServidor();

        StringBuilder url = new StringBuilder();

        return url.append("http://")
                .append(servidor)
                .append(":")
                .append(porta)
                .append("/")
                .append(ApplicationInit.CONTEXT.getResources().getString(R.string.SAM_CENTRAL_CONTEXT_NEW))
                .append("/");

    }


    public static String getImportaDadosRESTpath(){
        return getServerContextRoot()
                .append(ApplicationInit.CONTEXT.getResources()
                        .getString(R.string.SAM_CENTRAL_CONTEXT_IMPORTA_ARQUIVO))
                .toString();
    }


    public static String getExportaDadosRESTpath(){
        return getServerContextRoot()
                .append(ApplicationInit.CONTEXT.getResources()
                        .getString(R.string.SAM_CENTRAL_CONTEXT_EXPORTA_ARQUIVO))
                .toString();
    }


    public static String getURLauditoriaServer() {

        SharedPreferencesHelper.Configuracao.CONTEXT = ApplicationInit.CONTEXT;
        String porta = SharedPreferencesHelper.Configuracao.getPortaWeb();
        String servidor = SharedPreferencesHelper.Configuracao.getServidor();

        StringBuilder url = new StringBuilder();
        url.append("http://")
                .append(servidor)
                .append(":")
                .append(porta)
                .append("/")
                .append(ApplicationInit.CONTEXT.getResources()
                        .getString(R.string.SAM_LOG_AUDITOR_CONTEXT));
        return url.toString();
    }


    public static boolean isSDPresent (){

        if(Build.VERSION.SDK_INT > 17) {
            return true;
        }
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

}
