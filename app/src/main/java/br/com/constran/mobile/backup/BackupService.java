package br.com.constran.mobile.backup;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import br.com.constran.mobile.persistence.dao.AbstractDatabaseDAO;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by moises_santana on 06/05/2015.
 */
public class BackupService extends Service{

    private final String TAG = "BACKUP_SAM";
    private final File LOCAL_TO_BACKUP = new File(Environment.getExternalStorageDirectory().getPath()+"/Constran/Backup_DB/");
    private Handler handler;
    long fileSize = 0;
    private Runnable r;

    public BackupService(){
    }

    private void setLastFileSize(){
        fileSize = getDatabaseCurrentFile().length();
        Log.i(TAG, "Tamanho DB: " + new Long(fileSize).toString());
    }

    private long getDatabaseFileSize(){
        return getBaseContext().getDatabasePath(AbstractDatabaseDAO.DATABASE_NAME).length();
    }

    private File getDatabaseCurrentFile(){
        Log.i(TAG, getBaseContext().getDatabasePath(AbstractDatabaseDAO.DATABASE_NAME).getAbsolutePath());
        return getBaseContext().getDatabasePath(AbstractDatabaseDAO.DATABASE_NAME);
    }

    public boolean dirFailure() {
        boolean dirNaoExiste = !(new File(LOCAL_TO_BACKUP.getPath()).exists());
        boolean falhouAoCriarDir = true;
        boolean fail = true;

        if (dirNaoExiste) {
            falhouAoCriarDir = !(new File(LOCAL_TO_BACKUP.getPath()).mkdirs());//Criando o diretorio para backup

            fail = falhouAoCriarDir;

        } else {
            fail =  false;
        }
        Log.i(TAG, "DIRETORIO PARA BACKUP FOI CRIADO: "+fail);
        return fail;
    }

    public void doBackupDatabase() {

        if (dirFailure()) {
            //Toast.makeText(this, "Não será possível fazer o backup do banco de dados. Diretório não foi criado.", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Não será possível fazer o backup do banco de dados. Diretório não foi criado.");
        } else if (fileSize != getDatabaseFileSize()) {

            try {
                FileUtils.copyFileToDirectory(getDatabaseCurrentFile(), LOCAL_TO_BACKUP);
                Log.i(TAG, "FAZENDO O BACKUP!");
                setLastFileSize();
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, "Não foi possível fazer o backup do banco de dados."+e.getMessage());
                //Toast.makeText(this, "Não foi possível fazer o backup do banco de dados."+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else{
            setLastFileSize();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
         r = new Runnable() {
            public void run() {
                doBackupDatabase();
                handler.postDelayed(this, 60000);
            }
        };
        handler.postDelayed(r, 60000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(r);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}