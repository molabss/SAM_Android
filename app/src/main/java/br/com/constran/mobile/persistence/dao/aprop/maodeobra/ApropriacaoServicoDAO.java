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
import br.com.constran.mobile.persistence.vo.imp.AtividadeVO;
import br.com.constran.mobile.persistence.vo.imp.FrenteObraVO;
import br.com.constran.mobile.persistence.vo.imp.ServicoVO;
import br.com.constran.mobile.persistence.vo.menu.LocalizacaoVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Criado em 06/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class ApropriacaoServicoDAO extends BaseDAO<ApropriacaoServicoVO> {

    private static final String APROPR_SERVICO_COL_ID_APROPR = "idApropriacao";
    private static final String APROPR_SERVICO_COL_EQUIPE = "equipe";
    private static final String APROPR_SERVICO_COL_ID_SERVICO = "idServico";
    private static final String APROPR_SERVICO_COL_QTE_PRODUZIDA = "quantidadeProduzida";
    private static final String APROPR_SERVICO_COL_HORA_INI = "horaIni";
    private static final String APROPR_SERVICO_COL_HORA_FIM = "horaFim";
    private static final String APROPR_SERVICO_COL_OBS = "observacoes";
    private static final String APROPR_SERVICO_COL_UNIDADE = "unidadeMedida";

    private static ApropriacaoServicoDAO instance;
    private static ApropriacaoDAO apropriacaoDAO;

    private ApropriacaoServicoDAO(Context context) {
        super(context, TBL_APROPRIACAO_SERVICO);
    }

    public static ApropriacaoServicoDAO getInstance(Context context) {
        apropriacaoDAO = apropriacaoDAO == null ? ApropriacaoDAO.getInstance(context) : apropriacaoDAO;
        return instance == null ? instance = new ApropriacaoServicoDAO(context) : instance;
    }

    @Override
    public List<ApropriacaoServicoVO> findAllItems() {
        StringBuilder query = new StringBuilder();
        query.append("select distinct aps.*, srv.descricao from ").append(TBL_APROPRIACAO_SERVICO).append(" aps")
                .append(" inner join ").append(TBL_APROPRIACAO).append(" apr on aps.idApropriacao = apr.idApropriacao ")
                .append(" inner join ").append(TBL_EQUIPES_TRABALHO).append(" eqp on aps.equipe = eqp.idEquipe")
                .append(" inner join ").append(TBL_SERVICO).append(" srv on aps.idServico = srv.idServico")
                .append(" order by apr.dataHoraApontamento ");

        Cursor cursor = super.findByQuery(query.toString());

        List<ApropriacaoServicoVO> apropriacaoServicoVOs = bindList(cursor);

        if (apropriacaoServicoVOs != null) {
            for (ApropriacaoServicoVO apropriacaoServico : apropriacaoServicoVOs) {
                ApropriacaoVO apropriacao = apropriacaoDAO.findByIdApropriacao(apropriacaoServico.getApropriacao().getId());
                apropriacaoServico.setApropriacao(apropriacao);
            }
        }

        return apropriacaoServicoVOs;
    }

    public ApropriacaoServicoVO findByApropriacaoServico(ApropriacaoServicoVO aps) {
        if (aps == null || aps.getApropriacao() == null || aps.getEquipe() == null || aps.getServico() == null) {
            return null;
        }
        ApropriacaoServicoVO apropriacaoServico = null;
        Integer idApropriacao = aps.getApropriacao().getId();
        Integer idEquipe = aps.getEquipe().getId();
        Integer idServico = aps.getServico().getId();
        String horaIni = aps.getHoraIni();

        StringBuilder query = new StringBuilder();
        query.append("select aps.*, srv.descricao, srv.unidadeMedida from ").append(TBL_APROPRIACAO_SERVICO).append(" aps")
                .append(" inner join ").append(TBL_APROPRIACAO).append(" apr on aps.idApropriacao = apr.idApropriacao ")
                .append(" inner join ").append(TBL_EQUIPES_TRABALHO).append(" eqp on aps.equipe = eqp.idEquipe")
                .append(" inner join ").append(TBL_SERVICO).append(" srv on aps.idServico = srv.idServico")
                .append(" where aps.idApropriacao = ? and aps.equipe = ? and aps.idServico = ? and aps.horaIni = ? ")
                .append(" order by srv.descricao, aps.quantidadeProduzida ");


        Cursor cursor = findByQuery(query.toString(), concatArgs(idApropriacao, idEquipe, idServico, horaIni));

        if (cursor.moveToNext()) {
            apropriacaoServico = popularEntidade(cursor);
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return apropriacaoServico;
    }

    public List<ApropriacaoServicoVO> findByEventoEquipe(EventoEquipeVO eventoEquipe) {
        if (eventoEquipe == null || eventoEquipe.getApropriacao() == null) {
            return new ArrayList<ApropriacaoServicoVO>();
        }

        StringBuilder query = new StringBuilder();
        query.append("select distinct aps.*, srv.descricao, srv.unidadeMedida from ").append(TBL_APROPRIACAO_SERVICO).append(" aps")
                .append(" inner join ").append(TBL_APROPRIACAO).append(" apr on aps.idApropriacao = apr.idApropriacao ")
                .append(" inner join ").append(TBL_EQUIPES_TRABALHO).append(" eqp on aps.equipe = eqp.idEquipe")
                .append(" inner join ").append(TBL_SERVICO).append(" srv on aps.idServico = srv.idServico")
                .append(" where aps.idApropriacao = ? and aps.equipe = ? ")
                .append(" order by aps.horaIni, aps.horaFim, srv.descricao, aps.quantidadeProduzida ");

        Integer idApropriacao = eventoEquipe.getApropriacao().getId();
        Integer idEquipe = eventoEquipe.getEquipe().getId();

        Cursor cursor = super.findByQuery(query.toString(), concatArgs(idApropriacao, idEquipe));

        return bindList(cursor);
    }

    public List<ApropriacaoServicoVO> findByLocalAndEquipeAndData(LocalizacaoVO local, EquipeTrabalhoVO equipe, String data) {
        if (local == null || local.getAtividade() == null || equipe == null || data == null) {
            return new ArrayList<ApropriacaoServicoVO>();
        }

        AtividadeVO atividade = local.getAtividade();
        FrenteObraVO fob = atividade.getFrenteObra();

        StringBuilder query = new StringBuilder();
        query.append("select distinct aps.*, srv.descricao, srv.unidadeMedida from ").append(TBL_APROPRIACAO_SERVICO).append(" aps")
                .append(" inner join ").append(TBL_APROPRIACAO).append(" apr on aps.idApropriacao = apr.idApropriacao ")
                .append(" inner join ").append(TBL_FRENTE_OBRA).append(" fob on fob.idFrentesObra = apr.frentesObra ")
                .append(" inner join ").append(TBL_EQUIPES_TRABALHO).append(" eqp on aps.equipe = eqp.idEquipe")
                .append(" inner join ").append(TBL_SERVICO).append(" srv on aps.idServico = srv.idServico")
                .append(" where fob.obra = ? and apr.frentesObra = ? and apr.atividade = ? ")
                .append(" and aps.equipe = ? and substr(apr.dataHoraApontamento,0, 11) = ? ")
                .append(" order by srv.descricao, aps.quantidadeProduzida ");

        String[] args = concatArgs(fob.getIdObra(), fob.getId(), atividade.getIdAtividade(), equipe.getId(), data);
        Cursor cursor = super.findByQuery(query.toString(), args);

        return bindList(cursor);
    }

    public void delete(EventoEquipeVO eventoEquipe) {
        ApropriacaoVO apropriacao = eventoEquipe.getApropriacao();
        Integer apropriacaoId = apropriacao.getId();

        //exclui apropriacaoMaoObra
        deleteWhere(APROPR_SERVICO_COL_ID_APROPR + EQ + apropriacaoId);
    }

    /**
     * Remove apropriacaoServico a partir da apropriacao e da equipe
     *
     * @param apropriacao
     * @param equipe
     */
    public void delete(ApropriacaoVO apropriacao, EquipeTrabalhoVO equipe) {
        StringBuilder whereClause = new StringBuilder();
        whereClause.append(APROPR_SERVICO_COL_ID_APROPR + EQ + apropriacao.getId())
                .append(AND + APROPR_SERVICO_COL_EQUIPE + EQ + equipe.getId());

        deleteWhere(whereClause.toString());
    }

    /**
     * Exclui apropriacoes de servico que nao existam mais na equipe e em nenhum de seus integrantes
     *
     * @param ee
     * @param data
     */
    public void deleteOrfan(EventoEquipeVO ee, String data) {
        Integer idApropriacao = ee.getApropriacao().getId();
        Integer idEquipe = ee.getEquipe().getId();

        StringBuilder whereClause = new StringBuilder();
        whereClause.append(" idApropriacao not in (select distinct apropriacao from apropriacoesMaoObra amo where amo.equipe = equipe and amo.horaInicio = horaIni) ")
                .append(" and idApropriacao not in (select distinct apropriacao from paralisacoesEquipe pmo where pmo.equipe = equipe and pmo.horaInicio = horaIni) ")
                .append(" and idApropriacao = ? ")
                .append(" and equipe = ? and idApropriacao in ")
                .append(" (select idApropriacao from apropriacoes where tipoApropriacao = 'SRV' and substr(dataHoraApontamento,0, 11) = ? )");

        delete(whereClause.toString(), concatArgs(idApropriacao, idEquipe, data));
    }

    @Override
    public ApropriacaoServicoVO popularEntidade(Cursor cursor) {
        ApropriacaoServicoVO apropriacaoServico = new ApropriacaoServicoVO();
        ApropriacaoVO apropriacao = new ApropriacaoVO(getInt(cursor, APROPR_SERVICO_COL_ID_APROPR), null);
        EquipeTrabalhoVO equipe = new EquipeTrabalhoVO(getInt(cursor, APROPR_SERVICO_COL_EQUIPE));
        ServicoVO servico = new ServicoVO(getInt(cursor, APROPR_SERVICO_COL_ID_SERVICO));
        servico.setDescricao(getString(cursor, ALIAS_DESCRICAO));
        servico.setUnidadeMedida(getString(cursor, APROPR_SERVICO_COL_UNIDADE));

        apropriacaoServico.setQuantidadeProduzida(cursor.getDouble(cursor.getColumnIndex(APROPR_SERVICO_COL_QTE_PRODUZIDA)));
        apropriacaoServico.setHoraIni(cursor.getString(cursor.getColumnIndex(APROPR_SERVICO_COL_HORA_INI)));
        apropriacaoServico.setHoraFim(cursor.getString(cursor.getColumnIndex(APROPR_SERVICO_COL_HORA_FIM)));
        apropriacaoServico.setObservacoes(cursor.getString(cursor.getColumnIndex(APROPR_SERVICO_COL_OBS)));
        apropriacaoServico.setApropriacao(apropriacao);
        apropriacaoServico.setEquipe(equipe);
        apropriacaoServico.setServico(servico);

        return apropriacaoServico;
    }

    @Override
    public ContentValues bindContentValues(ApropriacaoServicoVO as) {
        ContentValues contentValues = new ContentValues();
        ApropriacaoVO apropriacao = as.getApropriacao();
        EquipeTrabalhoVO equipe = as.getEquipe();
        ServicoVO servico = as.getServico();

        contentValues.put(APROPR_SERVICO_COL_ID_APROPR, apropriacao != null ? apropriacao.getId() : null);
        contentValues.put(APROPR_SERVICO_COL_EQUIPE, equipe != null ? equipe.getId() : null);
        contentValues.put(APROPR_SERVICO_COL_ID_SERVICO, servico != null ? servico.getId() : null);
        contentValues.put(APROPR_SERVICO_COL_QTE_PRODUZIDA, as.getQuantidadeProduzida());
        contentValues.put(APROPR_SERVICO_COL_HORA_INI, as.getHoraIni());
        contentValues.put(APROPR_SERVICO_COL_HORA_FIM, as.getHoraFim());
        contentValues.put(APROPR_SERVICO_COL_OBS, as.getObservacoes());

        return contentValues;
    }

    @Override
    public boolean isNewEntity(ApropriacaoServicoVO as) {
        return findById(as) == null;
    }

    @Override
    public Object[] getPkArgs(ApropriacaoServicoVO as) {
        return new Object[]{as.getApropriacao().getId(), as.getEquipe().getId(), as.getServico().getId(), as.getHoraIni()};
    }

    @Override
    public String[] getPkColumns() {
        return new String[]{APROPR_SERVICO_COL_ID_APROPR, APROPR_SERVICO_COL_EQUIPE, APROPR_SERVICO_COL_ID_SERVICO, APROPR_SERVICO_COL_HORA_INI};
    }
}
