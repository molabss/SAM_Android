package br.com.constran.mobile.view.util;


import android.app.ProgressDialog;
import android.content.Context;
import br.com.constran.mobile.R;

public class AsyncTaskBar extends android.os.AsyncTask<Void, Void, String> {

    private ProgressDialog progress;
    private Context context;

    public AsyncTaskBar(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setTitle(context.getResources().getString(R.string.AGUARDE));
        progress.setMessage(context.getResources().getString(R.string.EM_ANDAMENTO));
        progress.setCancelable(true);
        progress.setIndeterminate(false);
        progress.setProgress(0);
        progress.setMax(100);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
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

    public ProgressDialog getProgress() {
        return progress;
    }

    public void setProgress(ProgressDialog progress) {
        this.progress = progress;
    }
}
