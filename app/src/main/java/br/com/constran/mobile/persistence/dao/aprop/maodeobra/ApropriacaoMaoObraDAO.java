package br.com.constran.mobile.persistence.dao.aprop.maodeobra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.dao.DAOFactory;
import br.com.constran.mobile.persistence.dao.aprop.ApropriacaoDAO;
import br.com.constran.mobile.persistence.dao.aprop.BaseDAO;
import br.com.constran.mobile.persistence.vo.Intervalo;
import br.com.constran.mobile.persistence.vo.aprop.ApropriacaoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.*;
import br.com.constran.mobile.persistence.vo.imp.AtividadeVO;
import br.com.constran.mobile.persistence.vo.imp.FrenteObraVO;
import br.com.constran.mobile.persistence.vo.imp.ServicoVO;
import br.com.constran.mobile.persistence.vo.menu.LocalizacaoVO;
import br.com.constran.mobile.validator.EventoEquipeValidator;
import br.com.constran.mobile.view.util.Util;

import java.util.List;

/**
 * Criado em 06/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class ApropriacaoMaoObraDAO extends BaseDAO<ApropriacaoMaoObraVO> {

    private static final String APROPR_MAO_OBRA_COL_APROPR = "apropriacao";
    private static final String APROPR_MAO_OBRA_COL_PESSOA = "pessoa";
    private static final String APROPR_MAO_OBRA_COL_HORA_INI = "horaInicio";
    private static final String APROPR_MAO_OBRA_COL_HORA_TERM = "horaTermino";
    private static final String APROPR_MAO_OBRA_COL_EQUIPE = "equipe";
    private static final String APROPR_MAO_OBRA_COL_SERVICO = "servico";
    private static final String APROPR_MAO_OBRA_COL_OBS = "observacoes";
    private static final String APROPR_SERVICO_COL_ID_SERVICO = "idServico";

    private static ApropriacaoMaoObraDAO instance;
    private static ApropriacaoDAO apropriacaoDAO;
    private static AusenciaDAO ausenciaDAO;
    private static PessoalDAO pessoalDAO;

    private ApropriacaoMaoObraDAO(Context context) {
        super(context, TBL_APROPRIACOES_MAO_OBRA);
    }

    public static ApropriacaoMaoObraDAO getInstance(Context context) {
        apropriacaoDAO = apropriacaoDAO == null ? ApropriacaoDAO.getInstance(context) : apropriacaoDAO;
        ausenciaDAO = ausenciaDAO == null ? AusenciaDAO.getInstance(context) : ausenciaDAO;
        pessoalDAO = pessoalDAO == null ? PessoalDAO.getInstance(context) : pessoalDAO;

        return instance == null ? instance = new ApropriacaoMaoObraDAO(context) : instance;
    }

    @Override
    public List<ApropriacaoMaoObraVO> findAllItems() {
        StringBuilder query = new StringBuilder();
        query.append("select amo.*, apr.dataHoraApontamento, apr.tipoApropriacao, apr.atividade from ")
                .append(TBL_APROPRIACOES_MAO_OBRA).append(" amo ")
                .append("inner join ").append(TBL_APROPRIACAO).append(" apr on amo.apropriacao = apr.idApropriacao ")
                .append("order by apr.dataHoraApontamento ");

        Cursor cursor = super.findByQuery(query.toString());

        List<ApropriacaoMaoObraVO> apropriacaoMaoObraVOs = bindList(cursor);

        if (apropriacaoMaoObraVOs != null) {
            ApropriacaoVO apropriacao = null;

            for (ApropriacaoMaoObraVO apropriacaoMaoObra : apropriacaoMaoObraVOs) {
                if (apropriacao == null || apropriacaoMaoObra.getApropriacao() == null || !apropriacao.getId().equals(apropriacaoMaoObra.getApropriacao().getId())) {
                    apropriacao = apropriacaoDAO.findByIdApropriacao(apropriacaoMaoObra.getApropriacao().getId());
                }
                apropriacaoMaoObra.setApropriacao(apropriacao);
            }
        }

        return apropriacaoMaoObraVOs;
    }

    public List<ApropriacaoMaoObraVO> findByLocal(LocalizacaoVO local, EquipeTrabalhoVO equipe, PessoalVO pessoal, String data) {
        AtividadeVO atividade = local.getAtividade();
        FrenteObraVO frenteObra = atividade.getFrenteObra();

        StringBuilder query = new StringBuilder();
        query.append("select amo.servico, amo.pessoa, amo.apropriacao, amo.horaInicio, amo.horaTermino, amo.equipe, amo.observacoes, ")
                .append("srv.descricao descricao2 from ").append(TBL_APROPRIACOES_MAO_OBRA).append(" amo ")
                .append("inner join servicos srv on srv.idServico = amo.servico ")
                .append("inner join ").append(TBL_APROPRIACAO).append(" apr on amo.apropriacao = apr.idApropriacao ")
                .append("where apr.frentesObra = ? and apr.atividade = ? and amo.equipe = ? and amo.pessoa = ? ")
                .append(" and substr(apr.dataHoraApontamento,0,11)= ? ")
                .append("group by amo.servico, amo.pessoa, amo.apropriacao, amo.horaInicio, amo.horaTermino, amo.equipe, amo.observacoes, descricao2 ")
                .append("order by amo.servico, amo.pessoa, amo.apropriacao, amo.horaInicio");

        String[] args = concatArgs(frenteObra.getId(), atividade.getIdAtividade(), equipe.getId(), pessoal.getId(), Util.toSimpleDateFormat(data));
        Cursor cursor = super.findByQuery(query.toString(), args);

        List<ApropriacaoMaoObraVO> apropriacaoMaoObraVOs = bindList(cursor);

        if (apropriacaoMaoObraVOs != null) {
            for (ApropriacaoMaoObraVO apropriacaoMaoObra : apropriacaoMaoObraVOs) {
                ApropriacaoVO apropriacao = apropriacaoDAO.findByIdApropriacao(apropriacaoMaoObra.getApropriacao().getId());
                apropriacaoMaoObra.setApropriacao(apropriacao);
            }
        }

        return apropriacaoMaoObraVOs;
    }

    public List<ApropriacaoMaoObraVO> findByPessoa(PessoalVO pessoal, String data) {
        StringBuilder query = new StringBuilder();
        query.append("select amo.servico, amo.pessoa, amo.apropriacao, amo.horaInicio, amo.horaTermino, amo.equipe, amo.observacoes, ")
                .append("srv.descricao descricao2 from ").append(TBL_APROPRIACOES_MAO_OBRA).append(" amo ")
                .append("inner join servicos srv on srv.idServico = amo.servico ")
                .append("inner join ").append(TBL_APROPRIACAO).append(" apr on amo.apropriacao = apr.idApropriacao ")
                .append("where amo.pessoa = ? ")
                .append(" and substr(apr.dataHoraApontamento,0,11)= ? ")
                .append("group by amo.servico, amo.pessoa, amo.apropriacao, amo.horaInicio, amo.horaTermino, amo.equipe, amo.observacoes, descricao2 ")
                .append("order by amo.servico, amo.pessoa, amo.apropriacao, amo.horaInicio");

        String[] args = concatArgs(pessoal.getId(), Util.toSimpleDateFormat(data));
        Cursor cursor = super.findByQuery(query.toString(), args);

        List<ApropriacaoMaoObraVO> apropriacaoMaoObraVOs = bindList(cursor);

        if (apropriacaoMaoObraVOs != null) {
            for (ApropriacaoMaoObraVO apropriacaoMaoObra : apropriacaoMaoObraVOs) {
                ApropriacaoVO apropriacao = apropriacaoDAO.findByIdApropriacao(apropriacaoMaoObra.getApropriacao().getId());
                apropriacaoMaoObra.setApropriacao(apropriacao);
            }
        }

        return apropriacaoMaoObraVOs;
    }

    /**
     * Busca uma apropriacao da mao de obra para o mesmo integrante, apropriacao e hora inicio
     *
     * @param pmo
     * @return
     */
    public ApropriacaoMaoObraVO findByParalisacaoMaoObra(ParalisacaoMaoObraVO pmo) {
        try {
            ApropriacaoMaoObraVO amo = new ApropriacaoMaoObraVO();
            amo.setApropriacao(pmo.getApropriacao());
            amo.setHoraInicio(pmo.getHoraInicio());
            amo.setPessoa(pmo.getPessoa());

            return findById(amo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<ApropriacaoMaoObraVO> findByLocalAndServico(LocalizacaoVO local, EquipeTrabalhoVO equipe, PessoalVO pessoal, ServicoVO servico) {
        AtividadeVO atividade = local.getAtividade();
        FrenteObraVO frenteObra = atividade.getFrenteObra();

        StringBuilder query = new StringBuilder();
        query.append("select distinct amo.servico, amo.apropriacao, amo.pessoa, amo.horaInicio, amo.horaTermino, amo.equipe, amo.observacoes,")
                .append("apr.dataHoraApontamento, apr.tipoApropriacao, apr.atividade, aps.idServico, srv.descricao ").append(ALIAS_DESCRICAO2)
                .append(" from ").append(TBL_APROPRIACOES_MAO_OBRA).append(" amo ")
                .append("inner join ").append(TBL_APROPRIACAO).append(" apr on amo.apropriacao = apr.idApropriacao ")
                .append("inner join ").append(TBL_APROPRIACAO_SERVICO).append(" aps on aps.idApropriacao = apr.idApropriacao and aps.equipe = amo.equipe ")
                .append("inner join ").append(TBL_SERVICO).append(" srv on srv.idServico = aps.idServico ")
                .append("where apr.frentesObra = ? and apr.atividade = ? and amo.equipe = ? and amo.pessoa = ? and and amo.servico = ?");

        query.append("group by amo.servico, amo.apropriacao, amo.pessoa, amo.horaInicio, amo.horaTermino, amo.equipe, amo.observacoes, ")
                .append("apr.dataHoraApontamento, apr.tipoApropriacao, apr.atividade ")
                .append("order by apr.dataHoraApontamento ");

        String[] args = concatArgs(frenteObra.getId(), atividade.getIdAtividade(), equipe.getId(), pessoal.getId(), servico.getId());
        Cursor cursor = super.findByQuery(query.toString(), args);

        List<ApropriacaoMaoObraVO> apropriacaoMaoObraVOs = bindList(cursor);

        if (apropriacaoMaoObraVOs != null) {
            for (ApropriacaoMaoObraVO apropriacaoMaoObra : apropriacaoMaoObraVOs) {
                ApropriacaoVO apropriacao = apropriacaoDAO.findByIdApropriacao(apropriacaoMaoObra.getApropriacao().getId());
                apropriacaoMaoObra.setApropriacao(apropriacao);
            }
        }

        return apropriacaoMaoObraVOs;
    }

    public void save(EventoEquipeVO eventoEquipe, IntegranteVO integranteEquipe) {
        ApropriacaoVO apropriacao = eventoEquipe.getApropriacao();
        EquipeTrabalhoVO equipe = eventoEquipe.getEquipe();

        //se integrante estiver preenchido salva dados apenas para ele, senao salva para toda equipe
        if (integranteEquipe == null) {
            salvarApropriacaoMaoObraEquipe(eventoEquipe, apropriacao, equipe);
        } else {
            PessoalVO pessoa = integranteEquipe.getPessoa();
            AusenciaVO ausencia = new AusenciaVO(equipe, pessoa, Util.getToday());

            //salva registro apenas quando usuário não faltou
            if (ausenciaDAO.findById(ausencia) == null) {
                save(preencherApropriacaoMaoObra(eventoEquipe, apropriacao, equipe, pessoa));
            }
        }
    }

    /**
     * Salva as apropriações da equipe para os seus integrantes
     *
     * @param eventoEquipe
     * @param apropriacao
     * @param equipe
     */
    private void salvarApropriacaoMaoObraEquipe(EventoEquipeVO eventoEquipe, ApropriacaoVO apropriacao, EquipeTrabalhoVO equipe) {
        //busca os integrantes que não estão na lista de faltosos
        List<PessoalVO> integrantes = pessoalDAO.findByIntegrantePresenteEquipe(equipe);

        if (integrantes != null) {
            //Apropria MO para cada membro da equipe
            for (PessoalVO pessoa : integrantes) {
                salvar(eventoEquipe, apropriacao, equipe, pessoa);
            }
        }
    }

    /**
     * Salva os dados de apropriacao considerando os eventos dos integrantes antes de replicar os dados
     * Caso o integrante esteja "ausente" a apropriacao so deve ser replicada se a hora termino da ausencia
     * for anterior a hora termino do evento sendo apropriado. caso a hora termino de ausencia nao seja informada
     * o evento nao deve ser replicado para o integrante, pois este estará ausente.
     *
     * @param eventoEquipe
     * @param apropriacao
     * @param equipe
     * @param pessoa
     */
    private void salvar(EventoEquipeVO eventoEquipe, ApropriacaoVO apropriacao, EquipeTrabalhoVO equipe, PessoalVO pessoa) {
        ApropriacaoMaoObraVO amo = preencherApropriacaoMaoObra(eventoEquipe, apropriacao, equipe, pessoa);

        //quando for apontar para equipe, a obs nao deve ser replicada
        ApropriacaoMaoObraVO amoBD = instance.findById(amo);

        //atualiza/sobrescreve a apropriacao MO individual apenas quando estiver editando a hora termino
        if (amoBD != null && EventoEquipeValidator.existeApropriacaoMO(eventoEquipe, amoBD)) {
            //se o tipo do evento nao for igual a produzindo (COD_PRODUZINDO) atualiza, senao exclui
            amo.setObservacoes(amoBD.getObservacoes() != null ? amoBD.getObservacoes() : null);
            update(amo);
        } else {
            salvarApropriacaoMaoObra(eventoEquipe, pessoa, amo);
        }
    }

    private void salvarApropriacaoMaoObra(EventoEquipeVO eventoEquipe, PessoalVO pessoa, ApropriacaoMaoObraVO amo) {
        EventoEquipeDAO eventoEquipeDAO = DAOFactory.getInstance(context).getEventoEquipeDAO();
        List<EventoEquipeVO> eventosIntegrante = eventoEquipeDAO.findApontamentos(pessoa, Util.getToday());

        //verifica se o integrante esta AUSENTE da equipe(replica se nao estiver ausente ou apos o periodo de ausencia)
        boolean ultimoEventoAusencia = eventoEquipeDAO.isUltimoEventoAusencia(eventosIntegrante);
        String ultimaHoraEvento = null;

        if (ultimoEventoAusencia) {
            ultimaHoraEvento = eventoEquipeDAO.getUltimaHoraEvento(eventosIntegrante, false);
        }

        boolean jaSalvou = false;

        //a hora de inicio do evento apontado deve ser posterior ao retorno do evento de ausente
        if (ultimoEventoAusencia && ultimaHoraEvento != null && amo.getHoraTermino() != null && Util.isHoraPosteriorOuIgual(amo.getHoraTermino(), ultimaHoraEvento)) {
            amo.setHoraInicio(ultimaHoraEvento);
        } else if (EventoEquipeValidator.hasConflitoHorario(eventosIntegrante, eventoEquipe)) {
            List<Intervalo> intervalos = EventoEquipeValidator.getIntervalosDisponiveis(eventosIntegrante, eventoEquipe);

            if (intervalos != null && !intervalos.isEmpty()) {

                //replicar apontamento para cada intervalo disponivel
                for (Intervalo intervalo : intervalos) {
                    amo.setHoraInicio(intervalo.getHoraIni());
                    amo.setHoraTermino(intervalo.getHoraFim());

                    checkAndSave(eventoEquipe, amo);
                }

                jaSalvou = true;
            }
        }

        if (!jaSalvou) {
            checkAndSave(eventoEquipe, amo);
        }
    }

    private void checkAndSave(EventoEquipeVO eventoEquipe, ApropriacaoMaoObraVO amo) {
        //verica se existe alguma paralisacao cadastrada para o mesmo horario de inicio, apropriacao
        //se existir entao, nao deve inserir
        ParalisacaoMaoObraVO pmo = DAOFactory.getInstance(context).getParalisacaoMaoObraDAO().findByApropriacaoMaoObra(amo);

        if (pmo == null) {
            amo.setObservacoes(amo.getObservacoes() == null || amo.getObservacoes().isEmpty() ? null : amo.getObservacoes());
            save(amo);
        } else if (EventoEquipeValidator.mudouParaProduzindo(eventoEquipe, pmo)) {
            DAOFactory.getInstance(context).getParalisacaoMaoObraDAO().delete(pmo);
            save(amo);
        }
    }

    private ApropriacaoMaoObraVO preencherApropriacaoMaoObra(EventoEquipeVO eventoEquipe, ApropriacaoVO apropriacao, EquipeTrabalhoVO equipe, PessoalVO pessoa) {
        ApropriacaoMaoObraVO amo = new ApropriacaoMaoObraVO();
        ApropriacaoServicoVO asv = new ApropriacaoServicoVO();
        asv.setApropriacao(apropriacao);
        asv.setEquipe(equipe);
        asv.setServico(eventoEquipe.getServico());

        amo.setApropriacao(apropriacao);
        amo.setEquipe(equipe);
        amo.setHoraInicio(eventoEquipe.getHoraIni());
        amo.setHoraTermino(eventoEquipe.getHoraFim());
        amo.setPessoa(pessoa);
        amo.setObservacoes(eventoEquipe.getObservacao());
        amo.setApropriacaoServico(asv);

        return amo;
    }

    /**
     * Exclui apropriacoes Mao de Obra da equipe
     * REGRA: so deve excluir a apropriacao da mao de obra
     * para os casos em que o serviço realizado pelo integrante seja o mesmo da equipe
     * e o horario de producao/trabalho do integrante esteja no intervalo de trabalho da equipe
     * do registro sendo excluido
     *
     * @param eventoEquipe
     */
    public void delete(EventoEquipeVO eventoEquipe) {
        ApropriacaoVO apropriacao = eventoEquipe.getApropriacao();
        String horaIni = eventoEquipe.getHoraIni();
        String horaFim = eventoEquipe.getHoraFim();
        Integer servico = eventoEquipe.getServico().getId();
        Integer equipe = eventoEquipe.getEquipe().getId();

        StringBuilder whereClause = new StringBuilder();
        whereClause.append(" apropriacao = " + apropriacao.getId());

        if (horaFim != null && !horaFim.isEmpty()) {
            whereClause.append(" and time(horaInicio) between time('" + horaIni + "') and time('" + horaFim + "')");
            whereClause.append(" and time(horaTermino) between time('" + horaIni + "') and time('" + horaFim + "')");
        } else {
            whereClause.append(" and time(horaInicio) >= time('" + horaIni + "') ");
            whereClause.append(" and case horaTermino when '' then 1=1 else time(horaTermino) >= time('" + horaIni + "') end ");
        }

        whereClause.append(" and equipe = " + equipe)
                .append(" and servico = " + servico);

        deleteWhere(whereClause.toString());
    }

    /**
     * Exclui apropriacao Mao de Obra do integrante
     *
     * @param eventoEquipe
     */
    public void delete(EventoEquipeVO eventoEquipe, IntegranteVO integrante) {
        ApropriacaoVO apropriacao = eventoEquipe.getApropriacao();
        PessoalVO pessoa = integrante.getPessoa();

        ApropriacaoMaoObraVO apropriacaoMaoObra = preencherApropriacaoMaoObra(eventoEquipe, apropriacao, null, pessoa);

        super.delete(apropriacaoMaoObra);
    }

    /**
     * Remove apropriacaoMaoObra a partir da apropriacao e da equipe
     *
     * @param apropriacao
     * @param equipe
     */
    public void delete(ApropriacaoVO apropriacao, EquipeTrabalhoVO equipe) {
        StringBuilder whereClause = new StringBuilder();

        whereClause.append(APROPR_MAO_OBRA_COL_APROPR + EQ + apropriacao.getId())
                .append(AND + APROPR_MAO_OBRA_COL_EQUIPE + EQ + equipe.getId());

        deleteWhere(whereClause.toString());
    }

    /**
     * Exclui apropriacoes por integrante da equipe
     *
     * @param equipe
     * @param integrante
     */
    public void deleteByIntegrante(EquipeTrabalhoVO equipe, IntegranteVO integrante) {
        if (equipe == null || integrante == null || integrante.getPessoa() == null) {
            return;
        }

        StringBuilder whereClause = new StringBuilder();
        whereClause.append(APROPR_MAO_OBRA_COL_EQUIPE).append(EQ).append(equipe.getId())
                .append(AND).append(APROPR_MAO_OBRA_COL_PESSOA).append(EQ).append(integrante.getPessoa().getId());

        deleteWhere(whereClause.toString());
    }

    @Override
    public ApropriacaoMaoObraVO popularEntidade(Cursor cursor) {
        ApropriacaoMaoObraVO apropriacaoMaoObra = new ApropriacaoMaoObraVO();
        ApropriacaoVO apropriacao = new ApropriacaoVO(cursor.getInt(cursor.getColumnIndex(APROPR_MAO_OBRA_COL_APROPR)), "");
        PessoalVO pessoa = new PessoalVO(cursor.getInt(cursor.getColumnIndex(APROPR_MAO_OBRA_COL_PESSOA)));
        EquipeTrabalhoVO equipe = new EquipeTrabalhoVO(cursor.getInt(cursor.getColumnIndex(APROPR_MAO_OBRA_COL_EQUIPE)));
        ServicoVO servico = new ServicoVO(getInt(cursor, APROPR_SERVICO_COL_ID_SERVICO), getString(cursor, ALIAS_DESCRICAO2), null);

        ApropriacaoServicoVO apropriacaoServico = new ApropriacaoServicoVO();

        if (servico.getId() == null) {
            servico.setId(getInt(cursor, APROPR_MAO_OBRA_COL_SERVICO));
        }

        apropriacaoServico.setServico(servico);
        apropriacaoMaoObra.setHoraInicio(cursor.getString(cursor.getColumnIndex(APROPR_MAO_OBRA_COL_HORA_INI)));
        apropriacaoMaoObra.setHoraTermino(cursor.getString(cursor.getColumnIndex(APROPR_MAO_OBRA_COL_HORA_TERM)));
        apropriacaoMaoObra.setObservacoes(cursor.getString(cursor.getColumnIndex(APROPR_MAO_OBRA_COL_OBS)));
        apropriacaoMaoObra.setApropriacao(apropriacao);
        apropriacaoMaoObra.setPessoa(pessoa);
        apropriacaoMaoObra.setEquipe(equipe);
        apropriacaoMaoObra.setApropriacaoServico(apropriacaoServico);

        return apropriacaoMaoObra;
    }

    @Override
    public ContentValues bindContentValues(ApropriacaoMaoObraVO amo) {
        ContentValues contentValues = new ContentValues();
        ApropriacaoVO apropriacao = amo.getApropriacao();
        PessoalVO pessoa = amo.getPessoa();
        EquipeTrabalhoVO equipe = amo.getEquipe();
        ServicoVO servico = amo.getApropriacaoServico().getServico();

        contentValues.put(APROPR_MAO_OBRA_COL_APROPR, apropriacao != null ? apropriacao.getId() : null);
        contentValues.put(APROPR_MAO_OBRA_COL_PESSOA, pessoa != null ? pessoa.getId() : null);
        contentValues.put(APROPR_MAO_OBRA_COL_EQUIPE, equipe != null ? equipe.getId() : null);
        contentValues.put(APROPR_MAO_OBRA_COL_SERVICO, servico != null ? servico.getId() : null);
        contentValues.put(APROPR_MAO_OBRA_COL_HORA_INI, amo.getHoraInicio());
        contentValues.put(APROPR_MAO_OBRA_COL_HORA_TERM, amo.getHoraTermino());
        contentValues.put(APROPR_MAO_OBRA_COL_OBS, amo.getObservacoes());

        return contentValues;
    }

    @Override
    public boolean isNewEntity(ApropriacaoMaoObraVO amo) {
        return findById(amo) == null;
    }

    @Override
    public Object[] getPkArgs(ApropriacaoMaoObraVO amo) {
        return new Object[]{amo.getApropriacao().getId(), amo.getPessoa().getId(), amo.getHoraInicio()};
    }

    @Override
    public String[] getPkColumns() {
        return new String[]{APROPR_MAO_OBRA_COL_APROPR, APROPR_MAO_OBRA_COL_PESSOA, APROPR_MAO_OBRA_COL_HORA_INI};
    }

}
