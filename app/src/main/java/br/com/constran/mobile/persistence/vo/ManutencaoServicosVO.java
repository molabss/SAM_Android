package br.com.constran.mobile.persistence.vo;

/**
 * Created by moises_santana on 10/03/2015.
 */
public class ManutencaoServicosVO extends AbstractVO{


    public ManutencaoServicosVO(){

    }


    @Override
    public String toString() {
        return getDescricao();
    }
}
