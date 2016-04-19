package br.com.constran.mobile.persistence.dao.imp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.imp.AtividadeVO;
import br.com.constran.mobile.persistence.vo.imp.ServicoVO;
import br.com.constran.mobile.persistence.vo.menu.LocalizacaoVO;
import br.com.constran.mobile.system.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

public class ServicoDAO extends AbstractDAO {

    private static final String SERVICO_COL_ID_SERVICO = "idServico";
    private static final String SERVICO_COL_DESC = "descricao";
    private static final String SERVICO_COL_TIPO = "tipo";
    private static final String SERVICO_COL_UNIDADE = "unidadeMedida";

    private static ServicoDAO instance;

    private ServicoDAO(Context context) {
        super(context, TBL_SERVICO);
    }

    public static ServicoDAO getInstance(Context context) {
        if (instance == null) {
            instance = new ServicoDAO(context);
        }
        return instance;
    }

    public ServicoVO[] getArrayServicoVO(LocalizacaoVO localizacao) {

        Query query = new Query(true);

        query.setColumns(new String[]{"s.[idServico]", "' ' || s.[descricao] " + ALIAS_DESCRICAO, "s.[idCategoria]"});

        query.setTableJoin(TBL_SERVICO + " s join " + TBL_PREVISAO_SERVICO + " ps on s.idServico = ps.servico ");

        query.setOrderBy("s.[descricao] ASC");

        query.setConditions("1 = 1 " + filtrarPorAtividadeLocal(localizacao));


        Cursor cursor = getCursor(query);

        ServicoVO[] dados = new ServicoVO[cursor.getCount()];

        int i = 0;

        while (cursor.moveToNext()) {
            ServicoVO servico = new ServicoVO(cursor.getInt(cursor.getColumnIndex(SERVICO_COL_ID_SERVICO)));
            servico.setDescricao(cursor.getString(cursor.getColumnIndex(ALIAS_DESCRICAO)));
            dados[i++] = servico;
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;

    }


    public ServicoVO[] getArrayAllServicos() {

        Query query = new Query(true);
        query.setColumns(new String[]{"[idServico]", "' ' || [descricao] " + ALIAS_DESCRICAO, "[idCategoria]"});
        query.setTableJoin(TBL_SERVICO);
        query.setOrderBy("[descricao]");

        Cursor cursor = getCursor(query);
        ServicoVO[] dados = new ServicoVO[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            ServicoVO servico = new ServicoVO(cursor.getInt(cursor.getColumnIndex(SERVICO_COL_ID_SERVICO)));
            servico.setDescricao(cursor.getString(cursor.getColumnIndex(ALIAS_DESCRICAO)));
            dados[i++] = servico;
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;

    }



    public List<ServicoVO> findServicosEquipe(LocalizacaoVO localizacao) {

        if (localizacao == null) {
            return new ArrayList<ServicoVO>();
        }

        StringBuilder query = new StringBuilder();
        query.append("select distinct(ser.idServico), ' ' || ser.descricao ").append(ALIAS_DESCRICAO).append(" from ")
                .append(TBL_SERVICO).append(" ser ")
                .append(" inner join " + TBL_PREVISAO_SERVICO + "ps on ser.idServico = ps.servico ")
                .append("where 1 = 1")
                .append(filtrarPorAtividadeLocal(localizacao))
                .append(" group by ser.idServico, ser.descricao ")
                .append(" order by ser.descricao");

        //Log.i("Query", query.toString());

        Cursor cursor = findByQuery(query.toString());

        return bindList(cursor);
    }


    public List<ServicoVO> findAllServicos() {

        StringBuilder query = new StringBuilder();
        query.append("select distinct(idServico), ' ' || descricao ").append(ALIAS_DESCRICAO).append(" from servicos order by descricao");

        Cursor cursor = findByQuery(query.toString());

        return bindList(cursor);
    }

    public ServicoVO findAllServicosById(Integer id) {

        StringBuilder query = new StringBuilder();
        query.append("select idServico, ' ' || descricao ")
                .append(ALIAS_DESCRICAO).append(" from servicos where idServico = ").append(id);

        Cursor cursor = getCursorRaw(query.toString());

        ServicoVO servicoVO = null;

        if(cursor.moveToNext()){
            servicoVO = new ServicoVO(cursor.getInt(cursor.getColumnIndex("idServico")));
            servicoVO.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
        }

        return servicoVO;
    }



    /**
     * Busca os servicos pelo id
     * Utilizado pelo modulo de mao-de-obra
     *
     * @param id
     * @return
     */
    public ServicoVO findServicoById(Integer id, LocalizacaoVO localizacao) {
        StringBuilder query = new StringBuilder();

        query.append("select distinct(ser.idServico), ser.descricao").append(" from ")
                .append(TBL_SERVICO).append(" ser ")
                .append(" inner join " + TBL_PREVISAO_SERVICO + "ps on ser.idServico = ps.servico ")
                .append("where ser.idServico = ? ")
                .append(filtrarPorAtividadeLocal(localizacao))
                .append(" order by ser.descricao");

        Cursor cursor = findByQuery(query.toString(), concatArgs(id));

        ServicoVO servico = null;

        if (cursor != null && cursor.moveToNext()) {
            servico = popularEntidade(cursor);
        }

        closeCursor(cursor);

        return servico;
    }


    public void save(ServicoVO pVO) {
        insert(getContentValues(pVO));
    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        ServicoVO pVO = (ServicoVO) abstractVO;

        ContentValues contentValues = new ContentValues();
        contentValues.put(SERVICO_COL_ID_SERVICO, pVO.getId());
        contentValues.put(SERVICO_COL_DESC, pVO.getDescricao());
        contentValues.put(SERVICO_COL_TIPO, pVO.getTipoServico());
        contentValues.put(SERVICO_COL_UNIDADE, pVO.getUnidadeMedida());

        return contentValues;
    }

    protected List<ServicoVO> bindList(Cursor cursor) {
        List<ServicoVO> list = new ArrayList<ServicoVO>();

        while (cursor != null && cursor.moveToNext()) {
            list.add(popularEntidade(cursor));
        }

        closeCursor(cursor);

        return list;
    }

    /**
     * Usado pelo módulo de Mao de obra
     *
     * @param cursor
     * @return
     */
    public ServicoVO popularEntidade(Cursor cursor) {
        ServicoVO servicoVO = new ServicoVO(cursor.getInt(cursor.getColumnIndex(SERVICO_COL_ID_SERVICO)),
                cursor.getString(cursor.getColumnIndex(ALIAS_DESCRICAO)), null);
        servicoVO.setUnidadeMedida(getString(cursor, SERVICO_COL_UNIDADE));

        return servicoVO;
    }

    private String filtrarPorAtividadeLocal(LocalizacaoVO localizacao) {
        AtividadeVO atividade = localizacao.getAtividade();

        return " and ps.ccObra = " + atividade.getFrenteObra().getIdObra()
                + " and ps.frenteObra =" + atividade.getFrenteObra().getId()
                + " and ps.atividade = " + atividade.getIdAtividade();
    }

}
