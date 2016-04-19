package br.com.constran.mobile.view.interfaces;

import android.database.Cursor;
import br.com.constran.mobile.view.screens.GridBody;
import br.com.constran.mobile.view.screens.GridFooter;
import br.com.constran.mobile.view.screens.GridHeader;


public interface InterfaceListActivity extends InterfaceActivity {

    GridBody getGridBodyValues() throws Exception;

    GridHeader getGridHeaderValues() throws Exception;

    GridHeader getGridHeaderTopValues() throws Exception;

    GridFooter getGridFooterValues() throws Exception;

    Cursor getCursor() throws Exception;

}
