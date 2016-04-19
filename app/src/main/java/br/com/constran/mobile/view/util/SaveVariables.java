package br.com.constran.mobile.view.util;

import br.com.constran.mobile.enums.TipoModulo;

/**
 * Created by mateus_vitali on 14/01/15.
 */
public class SaveVariables {

    private TipoModulo tipoModulo;

    private static SaveVariables instance = null;

    private SaveVariables() {
        tipoModulo = TipoModulo.FICHA_MOTORISTA; //inicial
    }

    public static synchronized SaveVariables getInstance() {
        if (instance == null) {
            instance = new SaveVariables();
        }

        return instance;
    }


    public TipoModulo getTipoModulo() {
        return tipoModulo;
    }

    public void setTipoModulo(TipoModulo tipoModulo) {
        this.tipoModulo = tipoModulo;
    }

}
