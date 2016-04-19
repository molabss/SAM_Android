package br.com.constran.mobile.persistence.dao.manutencao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.EquipamentoCategoriaVO;
import br.com.constran.mobile.persistence.vo.ManutencaoServicoPorCategoriaEquipamentoVO;
import br.com.constran.mobile.persistence.vo.ManutencaoServicosVO;

/**
 * Created by moises_santana on 10/04/2015.
 */
public class ManutencaoServicoPorCategoriaEquipamentoDAO extends AbstractDAO{

    private static ManutencaoServicoPorCategoriaEquipamentoDAO instance;

    public ManutencaoServicoPorCategoriaEquipamentoDAO(Context context) {
        super(context, TBL_MANUTENCAO_SERVICO_POR_CATEGORIA_EQUIPAMENTO);
    }


    public static ManutencaoServicoPorCategoriaEquipamentoDAO getInstance(Context context) {
        if (instance == null) {
            instance = new ManutencaoServicoPorCategoriaEquipamentoDAO(context);
        }
        return instance;
    }


    public void save(ManutencaoServicoPorCategoriaEquipamentoVO manutServVO){

        ContentValues values = new ContentValues();
        values.put("idServicoCategoriaEquipamento",manutServVO.getId());
        values.put("idManutencaoServico",manutServVO.getManutencaoServico().getId());
        values.put("idCategoriaEquipamento",manutServVO.getEquipamentoCategoria().getId());
        insert(TBL_MANUTENCAO_SERVICO_POR_CATEGORIA_EQUIPAMENTO, values);
    }


    public ManutencaoServicoPorCategoriaEquipamentoVO [] listarPorCategoriaDeEquipamento(String idEquipamento){

        String[] condicoes = new String[]{idEquipamento};
        ManutencaoServicoPorCategoriaEquipamentoVO servicoPorCategoriaEquipamento = null;
        ManutencaoServicosVO servico = null;
        EquipamentoCategoriaVO equipamentoCategoria = null;

        StringBuilder select = new StringBuilder();
        select.append("select msce.idServicoCategoriaEquipamento, ms.idManutencaoServico, ms.descricao as descricaoServico, ec.idCategoria, ec.descricao as descricaoCatEquipamento ");
        select.append(  "from manutencaoServicoPorCategoriaEquipamento msce ");
        select.append(      "inner join manutencaoServicos ms ");
        select.append(          "on msce.idManutencaoServico = ms.idManutencaoServico ");
        select.append(      "inner join equipamentoCategorias ec ");
        select.append(          "on msce.idCategoriaEquipamento = ec.idCategoria ");
        select.append(      "inner join equipamentos eqp ");
        select.append(          "on eqp.idCategoria = ec.idCategoria ");
        select.append(  "where eqp.idEquipamento = ? order by descricaoServico");



        Cursor cursor = getCursorRawParams(select.toString(),condicoes);

        //getCount()+1 POR CONTA DO "SELECIONE"
        //QUE ENTROU POR ULTIMO
        ManutencaoServicoPorCategoriaEquipamentoVO [] arrayServicos = new ManutencaoServicoPorCategoriaEquipamentoVO[cursor.getCount()+1];

        for(int i = 0; cursor.moveToNext(); i++){

            servicoPorCategoriaEquipamento = new ManutencaoServicoPorCategoriaEquipamentoVO();
            servicoPorCategoriaEquipamento.setId(cursor.getInt(cursor.getColumnIndex("idServicoCategoriaEquipamento")));

            //manutencao servico
            servico = new ManutencaoServicosVO();
            servico.setId(cursor.getInt(cursor.getColumnIndex("idManutencaoServico")));
            servico.setDescricao(cursor.getString(cursor.getColumnIndex("descricaoServico")));

            //categoria de equipamento
            equipamentoCategoria = new EquipamentoCategoriaVO();
            equipamentoCategoria.setId(cursor.getInt(cursor.getColumnIndex("idCategoria")));
            equipamentoCategoria.setDescricao(cursor.getString(cursor.getColumnIndex("descricaoCatEquipamento")));

            servicoPorCategoriaEquipamento.setManutencaoServico(servico);
            servicoPorCategoriaEquipamento.setEquipamentoCategoria(equipamentoCategoria);

            arrayServicos[i] = servicoPorCategoriaEquipamento;
        }

        //ADICIONADO UM ITEM PADRAO PARA FORCAR O USUARIO A ESCOLHER COM ATENCAO QUALQUER UM DOS ITENS VALIDOS
        servicoPorCategoriaEquipamento = new ManutencaoServicoPorCategoriaEquipamentoVO();
        servicoPorCategoriaEquipamento.setId(-1);
        servico = new ManutencaoServicosVO();
        servico.setId(-1);
        servico.setDescricao("Selecione");
        servicoPorCategoriaEquipamento.setManutencaoServico(servico);
        arrayServicos[cursor.getCount()] = servicoPorCategoriaEquipamento;
        //----------------------------------------------------------------------------------------------------

        return arrayServicos;
    }
}
