package br.com.constran.mobile.system;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import br.com.constran.mobile.LocalizacaoActivity;
import br.com.constran.mobile.R;
import br.com.constran.mobile.backup.BackupService;

/**
 * Created by moises_santana on 07/05/2015.
 */
public class ApplicationInit extends Application{

    public static Context CONTEXT;


    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = getApplicationContext();

        AppDirectory.ckeckDirectory();

        //SOMENTE SE O BANCO DE DADOS FOR CRIADO NO ARMAZENAMENTO INTERNO
        if(Build.VERSION.SDK_INT > 17) {
            startService(new Intent(CONTEXT, BackupService.class));
        }else{
            Log.i("BACKUP_DB","O backup do banco de dados não está disponível neste dispositivo");

            //new File(Environment.getExternalStorageDirectory().getPath()+"/Constran/Backup_DB/").mkdirs();

        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }





}
