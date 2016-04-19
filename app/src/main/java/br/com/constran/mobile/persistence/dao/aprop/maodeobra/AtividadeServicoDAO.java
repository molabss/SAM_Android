package br.com.constran.mobile.persistence.dao.aprop.maodeobra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.dao.aprop.BaseDAO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.AtividadeServicoVO;
import br.com.constran.mobile.persistence.vo.imp.AtividadeVO;
import br.com.constran.mobile.persistence.vo.imp.ServicoVO;

/**
 * Criado em 06/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class AtividadeServicoDAO extends BaseDAO<AtividadeServicoVO> {

    private static final String ATIVIDADE_SERVICO_COL_ID = "idAtividadeServico";
    private static final String ATIVIDADE_SERVICO_COL_ATIVIDADE = "atividade";
    private static final String ATIVIDADE_SERVICO_COL_SERVICO = "servico";
    private static final String ATIVIDADE_SERVICO_COL_DESC = "descricao";
    private static final String ATIVIDADE_SERVICO_COL_ORDEM = "ordem";
    private static final String ATIVIDADE_SERVICO_COL_COD_PREVIX = "codigoPrevix";
    private static final String ATIVIDADE_SERVICO_COL_ATIVIDADE_PAI = "atividadePai";

    private static AtividadeServicoDAO instance;

    private AtividadeServicoDAO(Context context) {
        super(context, TBL_ATIVIDADES_SERVICOS);
    }

    public static AtividadeServicoDAO getInstance(Context context) {
        return instance == null ? instance = new AtividadeServicoDAO(context) : instance;
    }

    @Override
    public AtividadeServicoVO popularEntidade(Cursor cursor) {
        AtividadeServicoVO atividadeServico = new AtividadeServicoVO();
        AtividadeServicoVO atividadePai = new AtividadeServicoVO(cursor.getInt(cursor.getColumnIndex(ATIVIDADE_SERVICO_COL_ATIVIDADE_PAI)));
        AtividadeVO atividade = new AtividadeVO(cursor.getInt(cursor.getColumnIndex(ATIVIDADE_SERVICO_COL_ATIVIDADE)), "");
        ServicoVO servico = new ServicoVO(cursor.getInt(cursor.getColumnIndex(ATIVIDADE_SERVICO_COL_SERVICO)));
        atividadeServico.setId(cursor.getInt(cursor.getColumnIndex(ATIVIDADE_SERVICO_COL_ID)));
        atividadeServico.setDescricao(cursor.getString(cursor.getColumnIndex(ATIVIDADE_SERVICO_COL_DESC)));
        atividadeServico.setOrdem(cursor.getInt(cursor.getColumnIndex(ATIVIDADE_SERVICO_COL_ORDEM)));
        atividadeServico.setCodigoPrevix(cursor.getString(cursor.getColumnIndex(ATIVIDADE_SERVICO_COL_COD_PREVIX)));
        atividadeServico.setAtividade(atividade);
        atividadeServico.setServico(servico);
        atividadeServico.setAtividadePai(atividadePai);

        return atividadeServico;
    }

    @Override
    public ContentValues bindContentValues(AtividadeServicoVO atividadeServico) {
        ContentValues contentValues = new ContentValues();
        AtividadeVO atividade = atividadeServico.getAtividade();
        ServicoVO servico = atividadeServico.getServico();
        AtividadeServicoVO atividadePai = atividadeServico.getAtividadePai();

        contentValues.put(ATIVIDADE_SERVICO_COL_ID, atividadeServico.getId());
        contentValues.put(ATIVIDADE_SERVICO_COL_DESC, atividadeServico.getDescricao());
        contentValues.put(ATIVIDADE_SERVICO_COL_ORDEM, atividadeServico.getOrdem());
        contentValues.put(ATIVIDADE_SERVICO_COL_COD_PREVIX, atividadeServico.getCodigoPrevix());
        contentValues.put(ATIVIDADE_SERVICO_COL_ATIVIDADE, atividade != null ? atividade.getIdAtividade() : null);
        contentValues.put(ATIVIDADE_SERVICO_COL_SERVICO, servico != null ? servico.getId() : null);
        contentValues.put(ATIVIDADE_SERVICO_COL_ATIVIDADE, atividadePai != null ? atividadePai.getId() : null);

        return contentValues;
    }

    @Override
    public boolean isNewEntity(AtividadeServicoVO as) {
        return as != null && as.getId() == null;
    }

    @Override
    public Object[] getPkArgs(AtividadeServicoVO as) {
        return new Object[]{as.getId()};
    }

    @Override
    public String[] getPkColumns() {
        return new String[]{ATIVIDADE_SERVICO_COL_ID};
    }

}
