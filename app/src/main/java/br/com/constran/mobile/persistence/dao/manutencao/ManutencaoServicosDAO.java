package br.com.constran.mobile.persistence.dao.manutencao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.ManutencaoServicosVO;
import br.com.constran.mobile.view.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moises_santana on 10/03/2015.
 */
public class ManutencaoServicosDAO extends AbstractDAO{

    private static ManutencaoServicosDAO instance;

    public ManutencaoServicosDAO(Context context) {
        super(context, TBL_MANUTENCAO_SERVICOS);
    }


    public static ManutencaoServicosDAO getInstance(Context context) {
        if (instance == null) {
            instance = new ManutencaoServicosDAO(context);
        }
        return instance;
    }


    public void save(ManutencaoServicosVO manutServVO){

        ContentValues values = new ContentValues();
        values.put("idManutencaoServico",manutServVO.getId());
        values.put("descricao",manutServVO.getDescricao());
        insert(TBL_MANUTENCAO_SERVICOS,values);
    }


    public ManutencaoServicosVO [] listarPorCategoriaDeEquipamento(String idEquipamento){

        String[] condicoes = new String[]{idEquipamento};
        ManutencaoServicosVO servico = null;

        StringBuilder select = new StringBuilder();
        select.append("select ms.idManutencaoServico, ms.descricao, eq.idCategoria ");
        select.append("from manutencaoServicos ms ");
        select.append("inner join manutencaoServicoPorCategoriaEquipamento msce ");
        select.append("on msce.idManutencaoServico = ms.idManutencaoServico ");
        select.append("inner join equipamentos eq ");
        select.append("on eq.idCategoria = msce.idCategoriaEquipamento ");
        select.append("where eq.idEquipamento = ?");

        Cursor cursor = getCursorRawParams(select.toString(),condicoes);
        ManutencaoServicosVO [] arrayServicos = new ManutencaoServicosVO[cursor.getCount()];

        for(int i = 0; cursor.moveToNext(); i++){

            servico = new ManutencaoServicosVO();
            servico.setId(cursor.getInt(cursor.getColumnIndex("idManutencaoServico")));
            servico.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            arrayServicos[i] = servico;
        }
        return arrayServicos;
    }

    //para colocar no autocomplete os servicos de manutencao que podem ser buscados nesta data momento
    public String[] listarServicosDeManutencao(){

        StringBuilder select = new StringBuilder();
        select.append("select distinct ms.descricao from manutencaoServicos ms ");
        select.append("inner join manutencaoServicoPorCategoriaEquipamento msce ");
        select.append("on ms.idManutencaoServico = msce.idManutencaoServico ");
        select.append("inner join manutencaoEquipamentoServicos mes ");
        select.append("on msce.idServicoCategoriaEquipamento = mes.idServicoCategoriaEquipamento ");
        select.append("inner join manutencaoEquipamentos me ");
        select.append("on me.idEquipamento = mes.idEquipamento and me.data = mes.data");

        Cursor cursor = getCursorRawParams(select.toString(),null);

        String[] servicos = new String[cursor.getCount()];

        //adicionando um espaco no inicio para mostrar a lista completa quando o usuario tocar sobre a tecla de espaco
        while(cursor.moveToNext()){
            servicos[cursor.getPosition()] = " ".concat(cursor.getString(cursor.getColumnIndex("descricao")));
        }

        return servicos;
    }




}
