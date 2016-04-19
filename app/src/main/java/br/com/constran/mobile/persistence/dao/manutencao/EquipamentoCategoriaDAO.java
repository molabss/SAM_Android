package br.com.constran.mobile.persistence.dao.manutencao;

import android.content.ContentValues;
import android.content.Context;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.EquipamentoCategoriaVO;

/**
 * Created by moises_santana on 11/03/2015.
 */
public class EquipamentoCategoriaDAO extends AbstractDAO{

    private static EquipamentoCategoriaDAO instance;

    public EquipamentoCategoriaDAO(Context context) {
        super(context, TBL_EQUIPAMENTO_CATEGORIAS);
    }


    public static EquipamentoCategoriaDAO getInstance(Context context) {
        if (instance == null) {
            instance = new EquipamentoCategoriaDAO(context);
        }
        return instance;
    }



    public void save(EquipamentoCategoriaVO equipaCategVO){
        ContentValues values = new ContentValues();
        values.put("idCategoria",equipaCategVO.getId());
        values.put("descricao",equipaCategVO.getDescricao());
        insert(TBL_EQUIPAMENTO_CATEGORIAS,values);
    }




}
