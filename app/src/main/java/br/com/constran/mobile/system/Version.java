package br.com.constran.mobile.system;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

/**
 * Created by moises_santana on 27/05/2015.
 */
public class Version {

      public static void get(){

          Context context = ApplicationInit.CONTEXT;
          PackageManager manager = context.getPackageManager();
          PackageInfo info = null;

          try {

              info = manager.getPackageInfo(context.getPackageName(), 0);

              Toast.makeText(context,
                      "PackageName = "+info.packageName
                       + "\nVersionCode = "+info.versionCode
                       + "\nVersionName = "+info.versionName
                       + "\nPermissions = "+info.permissions,Toast.LENGTH_LONG).show();


          } catch (PackageManager.NameNotFoundException e) {
              e.printStackTrace();
          }
      }
}
