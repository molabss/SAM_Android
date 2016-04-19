package br.com.constran.mobile.persistence.dao.manutencao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.ManutencaoEquipamentoServicosVO;
import br.com.constran.mobile.persistence.vo.ManutencaoServicoPorCategoriaEquipamentoVO;
import br.com.constran.mobile.persistence.vo.ManutencaoServicosVO;
import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;
import br.com.constran.mobile.view.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moises_santana on 07/04/2015.
 */
public class ManutencaoEquipamentoServicoDAO extends AbstractDAO {

    private static ManutencaoEquipamentoServicoDAO instance;
    public ManutencaoEquipamentoServicoDAO(Context context) {
        super(context, TBL_MANUTENCAO_EQUIPAMENTO_SERVICOS);
    }

    public static ManutencaoEquipamentoServicoDAO getInstance(Context context){

        if(instance == null){
            instance = new ManutencaoEquipamentoServicoDAO(context);
        }
        return instance;
    }


    //carregar servicos que ja foram feitos neste equipamento na data de hoje
    public List<ManutencaoEquipamentoServicosVO> listarPorEquipamento(Integer idEquipamento,String data){

        List<ManutencaoEquipamentoServicosVO> listaDeServicosFeitos = new ArrayList<ManutencaoEquipamentoServicosVO>();

        ManutencaoEquipamentoServicosVO servicoFeitoEmEquipamento = null;
        ManutencaoServicoPorCategoriaEquipamentoVO manutencaoServicoPorEquipamento = null;
        ManutencaoServicosVO servico = null;
        EquipamentoVO equipamento = null;

        StringBuilder select = new StringBuilder();
        String[] condicoes = new String[]{String.valueOf(idEquipamento), (data != null ? data : Util.getToday()) };

        select.append("select mes.idRegistro, mes.idEquipamento, ms.idManutencaoServico, ");
        select.append("mes.data, mes.idServicoCategoriaEquipamento, ");
        select.append("mes.horaInicio, mes.horaTermino, mes.observacao, ms.descricao ");
        select.append("from manutencaoEquipamentoServicos mes ");
        select.append("inner join manutencaoServicoPorCategoriaEquipamento msce ");
        select.append("on mes.idServicoCategoriaEquipamento = msce.idServicoCategoriaEquipamento ");
        select.append("inner join manutencaoServicos ms ");
        select.append("on msce.idManutencaoServico = ms.idManutencaoServico ");
        select.append("where mes.idEquipamento = ? and mes.data = ? order by mes.horaTermino");

        Cursor cursor = getCursorRawParams(select.toString(),condicoes);

        while(cursor.moveToNext()){

            //tempo gasto e observacao
            servicoFeitoEmEquipamento = new ManutencaoEquipamentoServicosVO();
            servicoFeitoEmEquipamento.setId(cursor.getInt(cursor.getColumnIndex("idRegistro")));
            servicoFeitoEmEquipamento.setData(cursor.getString(cursor.getColumnIndex("data")));
            servicoFeitoEmEquipamento.setHoraInicio(cursor.getString(cursor.getColumnIndex("horaInicio")));
            servicoFeitoEmEquipamento.setHoraTermino(cursor.getString(cursor.getColumnIndex("horaTermino")));
            servicoFeitoEmEquipamento.setObservacao(cursor.getString(cursor.getColumnIndex("observacao")));

            //tipo de servico efetuado
            manutencaoServicoPorEquipamento = new ManutencaoServicoPorCategoriaEquipamentoVO();
            manutencaoServicoPorEquipamento.setId(cursor.getInt(cursor.getColumnIndex("idServicoCategoriaEquipamento")));

            //descricao do servico efetuado
            servico = new ManutencaoServicosVO();
            servico.setId(cursor.getInt(cursor.getColumnIndex("idManutencaoServico")));
            servico.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));

            //qual equipamento sofreu manutencao
            equipamento = new EquipamentoVO();
            equipamento.setId(cursor.getInt(cursor.getColumnIndex("idEquipamento")));

            servicoFeitoEmEquipamento.setEquipamento(equipamento);
            manutencaoServicoPorEquipamento.setManutencaoServico(servico);
            servicoFeitoEmEquipamento.setManutencaoServicoPorCategoriaEquipamento(manutencaoServicoPorEquipamento);

            listaDeServicosFeitos.add(servicoFeitoEmEquipamento);
        }

        return listaDeServicosFeitos;
    }


    public long cadastrarNovo(ManutencaoEquipamentoServicosVO servicoEfetuado){
        ContentValues values = new ContentValues();
        values.put("idEquipamento",servicoEfetuado.getIdEquipamento());
        values.put("idServicoCategoriaEquipamento",servicoEfetuado.getIdServicoCategoriaEquipamento());
        values.put("data", servicoEfetuado.getData());
        values.put("horaInicio",servicoEfetuado.getHoraInicio());
        values.put("horaTermino",servicoEfetuado.getHoraTermino());
        values.put("observacao",servicoEfetuado.getObservacao());
        return insert(TBL_MANUTENCAO_EQUIPAMENTO_SERVICOS,values);
    }

    public void atualizarAtual(ManutencaoEquipamentoServicosVO servicoEfetuado){
        ContentValues values = new ContentValues();
        values.put("horaInicio",servicoEfetuado.getHoraInicio());
        values.put("horaTermino",servicoEfetuado.getHoraTermino());
        values.put("idServicoCategoriaEquipamento",servicoEfetuado.getIdServicoCategoriaEquipamento());
        values.put("observacao",servicoEfetuado.getObservacao());
        update(TBL_MANUTENCAO_EQUIPAMENTO_SERVICOS,values,"idEquipamento = ? and data = ? and idRegistro = ?",
                new String[]{servicoEfetuado.getIdEquipamento().toString(),servicoEfetuado.getData(),String.valueOf(servicoEfetuado.getId())}
        );
    }

    public int excluir(ManutencaoEquipamentoServicosVO servicoEfetuado){

        return delete("idEquipamento = ? and data = ? and idRegistro = ?",
                new String[]{servicoEfetuado.getEquipamento().getId().toString(),servicoEfetuado.getData(),String.valueOf(servicoEfetuado.getId())},
                TBL_MANUTENCAO_EQUIPAMENTO_SERVICOS);
    }

    public int getTotalItemServicos(Integer idEquipamento, String data){

        String[] condicoes = new String[]{String.valueOf(idEquipamento),data};

        StringBuilder select = new StringBuilder();
        select.append("select count(*) as total from manutencaoEquipamentoServicos where idEquipamento = ? and data = ?");
        Cursor cursor = getCursorRawParams(select.toString(),condicoes);

        if(cursor.moveToNext())
            return cursor.getInt(cursor.getColumnIndex("total"));
        else
            return 0;
    }


    public String obterHoraTerminoDoUltimoServico(Integer idEquipamento, String data){
        String[] condicoes = new String[]{idEquipamento.toString(),data};
        String ultimaHoraTermino = "-1:00";
        StringBuilder select = new StringBuilder();
        select.append("select max(horaTermino) as horaTermino from [manutencaoEquipamentoServicos] where idEquipamento = ? and data = ?");
        Cursor cursor = getCursorRawParams(select.toString(),condicoes);

        if(cursor.moveToNext()){
            ultimaHoraTermino = cursor.getString(cursor.getColumnIndex("horaTermino"));
        }
        return ultimaHoraTermino;
    }
}
