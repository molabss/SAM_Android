package br.com.constran.mobile.system;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by moises_santana on 26/05/2015.
 */
public class NetworkUtils {

   static Context context = ApplicationInit.CONTEXT;

    public static boolean isNetworkUnavailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return !(activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }

}
