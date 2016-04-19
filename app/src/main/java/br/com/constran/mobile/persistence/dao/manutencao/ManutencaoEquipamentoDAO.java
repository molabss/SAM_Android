package br.com.constran.mobile.persistence.dao.manutencao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.ManutencaoEquipamentoVO;
import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;
import br.com.constran.mobile.view.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moises_santana on 02/03/2015.
 */
public class ManutencaoEquipamentoDAO extends AbstractDAO {

    private static ManutencaoEquipamentoDAO instance;

    public ManutencaoEquipamentoDAO(Context context) {
        super(context, TBL_MANUTENCAO_EQUIPAMENTOS);
    }

    public static ManutencaoEquipamentoDAO getInstance(Context context){

        if(instance == null){
            instance = new ManutencaoEquipamentoDAO(context);
        }
        return instance;
    }

    public Long cadastrarNova(ManutencaoEquipamentoVO manutencaoVO){
        ContentValues values = new ContentValues();
        values.put("idEquipamento",manutencaoVO.getEquipamento().getId());
        values.put("data", manutencaoVO.getData());
        values.put("horaInicio",manutencaoVO.getHoraInicio());
        values.put("horaTermino",manutencaoVO.getHoraTermino());
        values.put("horimetro",manutencaoVO.getHorimetro());
        values.put("hodometro",manutencaoVO.getHodometro());
        values.put("observacao",manutencaoVO.getObservacao());
        return insert(TBL_MANUTENCAO_EQUIPAMENTOS,values);
    }

    public void atualizarAtual(ManutencaoEquipamentoVO manutencao){
        ContentValues values = new ContentValues();
        values.put("horaInicio",manutencao.getHoraInicio());
        values.put("horaTermino",manutencao.getHoraTermino());
        values.put("horaTermino",manutencao.getHoraTermino());
        values.put("horimetro",manutencao.getHorimetro());
        values.put("hodometro",manutencao.getHodometro());
        values.put("observacao",manutencao.getObservacao());
        update(TBL_MANUTENCAO_EQUIPAMENTOS,values,"idEquipamento = ? and data = ?",
                new String[]{manutencao.getEquipamento().getId().toString(),manutencao.getData()}
        );
    }

    public boolean jaCadastrado(Integer idEquipamento, String data){

        boolean cadastrado = false;

        StringBuilder select = new StringBuilder();
        String[] condicoes = new String[]{idEquipamento.toString(),data};
        select.append("select count(*) as cadastrado from manutencaoEquipamentos where idEquipamento = ? and data = ?");

        Cursor cursor = getCursorRawParams(select.toString(),condicoes);

        if(cursor.moveToNext()){
            cadastrado = (cursor.getInt(cursor.getColumnIndex("cadastrado")) > 0);
        }

        return cadastrado;
    }



    public List<ManutencaoEquipamentoVO> listarTodos(String data, String prefixo, String manutencaoServico){

        if(data == null || data.length() == 0)data = Util.getToday();

        String[] condicoes = null;
        StringBuilder where = new StringBuilder("where me.data = ?");

        if(prefixo != null){
            condicoes = new String[]{data,prefixo};
            where.append(" and eq.prefixo = ?");
        }
        else if(manutencaoServico != null){
            condicoes = new String[]{data,manutencaoServico};
            where.append(" and ms.descricao = ?");
        }
        else{
            condicoes = new String[]{data};
        }

        List<ManutencaoEquipamentoVO> lista = new ArrayList<ManutencaoEquipamentoVO>();
        ManutencaoEquipamentoVO manutencao = null;
        EquipamentoVO equipamento = null;

        StringBuilder select = new StringBuilder();

        select.append("select distinct ");
        select.append("me.idEquipamento,eq.descricao,eq.prefixo,me.data as dataME, ");
        select.append("me.horaInicio,me.horaTermino,me.horimetro,me.hodometro,me.observacao ");
        select.append("from manutencaoEquipamentos me ");
        select.append("left join equipamentos eq ");
        select.append("on me.idEquipamento = eq.idEquipamento ");
        select.append("left join manutencaoEquipamentoServicos mes ");
        select.append("on mes.idEquipamento = me.idEquipamento and mes.data = me.data ");
        select.append("left join manutencaoServicoPorCategoriaEquipamento msce ");
        select.append("on mes.idServicoCategoriaEquipamento = msce.idServicoCategoriaEquipamento ");
        select.append("left join manutencaoServicos ms ");
        select.append("on msce.idManutencaoServico = ms.idManutencaoServico ");
        select.append(where);

        Cursor cursor = getCursorRawParams(select.toString(),condicoes);

        while(cursor.moveToNext()){

            equipamento = new EquipamentoVO();
            equipamento.setId(cursor.getInt(cursor.getColumnIndex("idEquipamento")));
            equipamento.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            equipamento.setPrefixo(cursor.getString(cursor.getColumnIndex("prefixo")));

            manutencao = new ManutencaoEquipamentoVO();
            manutencao.setEquipamento(equipamento);
            manutencao.setData(cursor.getString(cursor.getColumnIndex("dataME")));
            manutencao.setHoraInicio(cursor.getString(cursor.getColumnIndex("horaInicio")));
            manutencao.setHoraTermino(cursor.getString(cursor.getColumnIndex("horaTermino")));
            manutencao.setHorimetro(cursor.getString(cursor.getColumnIndex("horimetro")));
            manutencao.setHodometro(cursor.getString(cursor.getColumnIndex("hodometro")));
            manutencao.setObservacao(cursor.getString(cursor.getColumnIndex("observacao")));
            lista.add(manutencao);
        }
        return lista;
    }

    public int excluirRegistro(ManutencaoEquipamentoVO manutencao){
        return database.delete("manutencaoEquipamentos","idEquipamento = ? and data = ?",
                new String[] {manutencao.getEquipamento().getId().toString(),manutencao.getData()}
        );
    }

    public String[] listarPrefixosEquipamentos(){

        StringBuilder select = new StringBuilder();
        select.append("select distinct eqp.prefixo from equipamentos eqp ");
        select.append("inner join manutencaoEquipamentos me ");
        select.append("on me.idEquipamento = eqp.idEquipamento");

        Cursor cursor = getCursorRawParams(select.toString(),null);

        String[] prefixos = new String[cursor.getCount()];

        //adicionando um espaco no inicio para mostrar a lista completa quando o usuario tocar sobre a tecla de espaco
        while(cursor.moveToNext()){
            prefixos[cursor.getPosition()] = " ".concat(cursor.getString(cursor.getColumnIndex("prefixo")));
        }
        return prefixos;
    }

    public String[] listarDatas(){

        StringBuilder select = new StringBuilder();
        select.append("select distinct data from manutencaoEquipamentos order by data desc");

        Cursor cursor = getCursorRawParams(select.toString(),null);

        String[] datas = new String[cursor.getCount()];

        while(cursor.moveToNext()){
            datas[cursor.getPosition()] = " ".concat(cursor.getString(cursor.getColumnIndex("data")));
        }
        return datas;
    }

    public boolean faltaHoraDeTermino(Integer idEquipamento, String data){

        boolean faltando = false;
        String[] condicoes = new String[]{idEquipamento.toString(),data};
        StringBuilder select = new StringBuilder();
        select.append("select count(*) as encontrado from manutencaoEquipamentos where horaTermino = \"\" and (idEquipamento = ? and data = ?)");
        Cursor cursor = getCursorRawParams(select.toString(),condicoes);

        if(cursor.moveToNext()){
            faltando = (cursor.getInt(cursor.getColumnIndex("encontrado")) == 1);
        }
        return faltando;
    }

    public boolean existeItens(){

        boolean existe = false;
        StringBuilder select = new StringBuilder();
        select.append("select count(*) as encontrado from manutencaoEquipamentos");

        Cursor cursor = getCursorRawParams(select.toString(),null);

        if(cursor.moveToNext()){
            existe = (cursor.getInt(cursor.getColumnIndex("encontrado")) > 0);
        }
        return existe;
    }


    public String obterHoraQueEquipamentoEntrouNaOficina(Integer idEquipamento, String data){

        String horaInicio = "-1:00";

        String[] condicoes = new String[]{idEquipamento.toString(),data};
        StringBuilder select = new StringBuilder();
        select.append("select horaInicio from [manutencaoEquipamentos] where [idEquipamento] = ? and [data] = ?");

        Cursor cursor = getCursorRawParams(select.toString(),condicoes);

        if(cursor.moveToNext()){
            horaInicio = cursor.getString(cursor.getColumnIndex("horaInicio"));
        }
        return horaInicio;
    }


}
