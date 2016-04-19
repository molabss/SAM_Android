package br.com.constran.mobile.view.util;


import android.app.ProgressDialog;
import android.content.Context;
import br.com.constran.mobile.R;

public class AsyncTask extends android.os.AsyncTask<Void, Void, String> {

    private ProgressDialog progress;
    private Context context;

    public AsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setTitle(context.getResources().getString(R.string.AGUARDE));
        progress.setMessage(context.getResources().getString(R.string.EM_ANDAMENTO));
        progress.setCancelable(false);
        progress.setIndeterminate(false);
        progress.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        progress.dismiss();
        Util.viewMessage(this.context, result);
    }

    @Override
    protected void onProgressUpdate(Void... values) {


    }

}
