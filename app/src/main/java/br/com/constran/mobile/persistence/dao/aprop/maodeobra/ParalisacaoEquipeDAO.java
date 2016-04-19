package br.com.constran.mobile.persistence.dao.aprop.maodeobra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.dao.aprop.ApropriacaoDAO;
import br.com.constran.mobile.persistence.dao.aprop.BaseDAO;
import br.com.constran.mobile.persistence.vo.aprop.ApropriacaoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.ApropriacaoServicoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.EquipeTrabalhoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.EventoEquipeVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.ParalisacaoEquipeVO;
import br.com.constran.mobile.persistence.vo.imp.ParalisacaoVO;
import br.com.constran.mobile.persistence.vo.imp.ServicoVO;

import java.util.List;

/**
 * Criado em 06/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class ParalisacaoEquipeDAO extends BaseDAO<ParalisacaoEquipeVO> {

    private static final String PARALISACAO_EQ_COL_APROPRIACAO = "apropriacao";
    private static final String PARALISACAO_EQ_COL_EQUIPE = "equipe";
    private static final String PARALISACAO_EQ_COL_SERVICO = "servico";
    private static final String PARALISACAO_EQ_COL_ID_PARALISACAO = "idParalisacao";
    private static final String PARALISACAO_EQ_COL_HORA_INI = "horaInicio";
    private static final String PARALISACAO_EQ_COL_HORA_TERM = "horaTermino";
    private static final String PARALISACAO_EQ_COL_OBS = "observacoes";

    private static ParalisacaoEquipeDAO instance;
    private static ApropriacaoDAO apropriacaoDAO;

    private ParalisacaoEquipeDAO(Context context) {
        super(context, TBL_PARALISACOES_EQUIPE);
    }

    public static ParalisacaoEquipeDAO getInstance(Context context) {
        apropriacaoDAO = apropriacaoDAO == null ? ApropriacaoDAO.getInstance(context) : apropriacaoDAO;
        return instance == null ? instance = new ParalisacaoEquipeDAO(context) : instance;
    }

    @Override
    public List<ParalisacaoEquipeVO> findAllItems() {
        StringBuilder query = new StringBuilder();
        query.append("select peq.* from ").append(TBL_PARALISACOES_EQUIPE).append(" peq ")
                .append(" inner join ").append(TBL_APROPRIACAO).append(" apr on peq.apropriacao = apr.idApropriacao ")
                .append(" order by apr.dataHoraApontamento ");

        Cursor cursor = super.findByQuery(query.toString());

        List<ParalisacaoEquipeVO> paralisacaoEquipeVOs = bindList(cursor);

        if (paralisacaoEquipeVOs != null) {
            for (ParalisacaoEquipeVO paralisacaoEquipe : paralisacaoEquipeVOs) {
                ApropriacaoVO apropriacao = apropriacaoDAO.findByIdApropriacao(paralisacaoEquipe.getApropriacao().getId());
                paralisacaoEquipe.setApropriacao(apropriacao);
            }
        }

        return paralisacaoEquipeVOs;
    }

    public List<ParalisacaoEquipeVO> findByEventoEquipeAndData(EventoEquipeVO eventoEquipe, String data) {
        StringBuilder query = new StringBuilder();
        query.append("select peq.* from ").append(TBL_PARALISACOES_EQUIPE).append(" peq ")
                .append(" inner join ").append(TBL_APROPRIACAO).append(" apr on peq.apropriacao = apr.idApropriacao ")
                .append(" inner join ").append(TBL_EQUIPES_TRABALHO).append(" eqp on peq.equipe = eqp.idEquipe")
                .append(" where peq.apropriacao = ? and peq.equipe = ? and substr(apr.dataHoraApontamento,0, 11) = ? ")
                .append(" order by apr.dataHoraApontamento ");

        Integer idApropriacao = eventoEquipe.getApropriacao().getId();
        Integer idEquipe = eventoEquipe.getEquipe().getId();

        Cursor cursor = super.findByQuery(query.toString(), concatArgs(idApropriacao, idEquipe, data));

        return bindList(cursor);
    }

    /**
     * Remove paralisacaoEquipoe a partir da apropriacao e da equipe
     *
     * @param apropriacao
     * @param equipe
     */
    public void delete(ApropriacaoVO apropriacao, EquipeTrabalhoVO equipe) {
        StringBuilder whereClause = new StringBuilder();
        whereClause.append(PARALISACAO_EQ_COL_APROPRIACAO + EQ + apropriacao.getId())
                .append(AND + PARALISACAO_EQ_COL_EQUIPE + EQ + equipe.getId());

        deleteWhere(whereClause.toString());
    }

    @Override
    public ParalisacaoEquipeVO popularEntidade(Cursor cursor) {
        ParalisacaoEquipeVO paralisacaoEquipe = new ParalisacaoEquipeVO();
        ApropriacaoVO apropriacao = new ApropriacaoVO(getInt(cursor, PARALISACAO_EQ_COL_APROPRIACAO), "");
        EquipeTrabalhoVO equipe = new EquipeTrabalhoVO(getInt(cursor, PARALISACAO_EQ_COL_EQUIPE));
        ParalisacaoVO paralisacao = new ParalisacaoVO(getInt(cursor, PARALISACAO_EQ_COL_ID_PARALISACAO));
        ServicoVO servico = new ServicoVO(getInt(cursor, PARALISACAO_EQ_COL_SERVICO));

        ApropriacaoServicoVO apropriacaoServico = new ApropriacaoServicoVO();
        apropriacaoServico.setServico(servico);

        paralisacaoEquipe.setHoraInicio(cursor.getString(cursor.getColumnIndex(PARALISACAO_EQ_COL_HORA_INI)));
        paralisacaoEquipe.setHoraTermino(cursor.getString(cursor.getColumnIndex(PARALISACAO_EQ_COL_HORA_TERM)));
        paralisacaoEquipe.setObservacoes(cursor.getString(cursor.getColumnIndex(PARALISACAO_EQ_COL_OBS)));
        paralisacaoEquipe.setApropriacao(apropriacao);
        paralisacaoEquipe.setEquipe(equipe);
        paralisacaoEquipe.setParalisacao(paralisacao);
        paralisacaoEquipe.setApropriacaoServico(apropriacaoServico);

        return paralisacaoEquipe;
    }

    @Override
    public ContentValues bindContentValues(ParalisacaoEquipeVO paralisacaoEquipe) {
        ContentValues contentValues = new ContentValues();
        ApropriacaoVO apropriacao = paralisacaoEquipe.getApropriacao();
        EquipeTrabalhoVO equipe = paralisacaoEquipe.getEquipe();
        ParalisacaoVO paralisacao = paralisacaoEquipe.getParalisacao();
        ApropriacaoServicoVO apropriacaoServico = paralisacaoEquipe.getApropriacaoServico();
        ServicoVO servico = apropriacaoServico.getServico();

        contentValues.put(PARALISACAO_EQ_COL_APROPRIACAO, apropriacao != null ? apropriacao.getId() : null);
        contentValues.put(PARALISACAO_EQ_COL_EQUIPE, equipe != null ? equipe.getId() : null);
        contentValues.put(PARALISACAO_EQ_COL_SERVICO, servico != null ? servico.getId() : null);
        contentValues.put(PARALISACAO_EQ_COL_ID_PARALISACAO, paralisacao != null ? paralisacao.getId() : null);
        contentValues.put(PARALISACAO_EQ_COL_HORA_INI, paralisacaoEquipe.getHoraInicio());
        contentValues.put(PARALISACAO_EQ_COL_HORA_TERM, paralisacaoEquipe.getHoraTermino());
        contentValues.put(PARALISACAO_EQ_COL_OBS, paralisacaoEquipe.getObservacoes());

        return contentValues;
    }

    @Override
    public boolean isNewEntity(ParalisacaoEquipeVO pe) {
        return findById(pe) == null;
    }

    @Override
    public Object[] getPkArgs(ParalisacaoEquipeVO pe) {
        return new Object[]{pe.getApropriacao().getId(), pe.getEquipe().getId(), pe.getParalisacao().getId(), pe.getHoraInicio()};
    }

    @Override
    public String[] getPkColumns() {
        return new String[]{PARALISACAO_EQ_COL_APROPRIACAO, PARALISACAO_EQ_COL_EQUIPE, PARALISACAO_EQ_COL_ID_PARALISACAO, PARALISACAO_EQ_COL_HORA_INI};
    }

}
