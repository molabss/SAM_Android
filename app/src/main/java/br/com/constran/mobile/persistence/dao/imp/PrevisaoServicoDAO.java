package br.com.constran.mobile.persistence.dao.imp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.dao.aprop.BaseDAO;
import br.com.constran.mobile.persistence.vo.ObraVO;
import br.com.constran.mobile.persistence.vo.aprop.PrevisaoServicoVO;
import br.com.constran.mobile.persistence.vo.imp.AtividadeVO;
import br.com.constran.mobile.persistence.vo.imp.FrenteObraVO;
import br.com.constran.mobile.persistence.vo.imp.ServicoVO;

/**
 * Criado em 19/08/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class PrevisaoServicoDAO extends BaseDAO<PrevisaoServicoVO> {

    private static final String COL_CC_OBRA = "ccObra";
    private static final String COL_FRENTE_OBRA = "frenteObra";
    private static final String COL_ATIVIDADE = "atividade";
    private static final String COL_SERVICO = "servico";

    private static PrevisaoServicoDAO instance;

    public PrevisaoServicoDAO(Context context) {
        super(context, TBL_PREVISAO_SERVICO);
    }

    public static PrevisaoServicoDAO getInstance(Context context) {
        return instance == null ? instance = new PrevisaoServicoDAO(context) : instance;
    }

    @Override
    public boolean isNewEntity(PrevisaoServicoVO previsaoServico) {
        return findById(previsaoServico) == null;
    }

    @Override
    public Object[] getPkArgs(PrevisaoServicoVO ps) {
        return new Object[]{ps.getObra().getId(), ps.getFrenteObra().getId(), ps.getAtividade().getIdAtividade(), ps.getServico().getId()};
    }

    @Override
    public PrevisaoServicoVO popularEntidade(Cursor cursor) {
        PrevisaoServicoVO previsaoServico = new PrevisaoServicoVO();
        previsaoServico.setObra(new ObraVO(getInt(cursor, COL_CC_OBRA)));
        previsaoServico.setFrenteObra(new FrenteObraVO(getInt(cursor, COL_FRENTE_OBRA)));
        previsaoServico.setAtividade(new AtividadeVO(getInt(cursor, COL_ATIVIDADE), ""));
        previsaoServico.setServico(new ServicoVO(getInt(cursor, COL_SERVICO)));

        return previsaoServico;
    }

    @Override
    public ContentValues bindContentValues(PrevisaoServicoVO previsaoServico) {
        ObraVO obra = previsaoServico.getObra();
        FrenteObraVO frenteObra = previsaoServico.getFrenteObra();
        AtividadeVO atividade = previsaoServico.getAtividade();
        ServicoVO servico = previsaoServico.getServico();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CC_OBRA, obra != null ? obra.getId() : null);
        contentValues.put(COL_FRENTE_OBRA, frenteObra != null ? frenteObra.getId() : null);
        contentValues.put(COL_ATIVIDADE, atividade != null ? atividade.getIdAtividade() : null);
        contentValues.put(COL_SERVICO, servico != null ? servico.getId() : null);

        return contentValues;
    }
}
