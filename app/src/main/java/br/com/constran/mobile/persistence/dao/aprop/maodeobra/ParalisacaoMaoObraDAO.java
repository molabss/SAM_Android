package br.com.constran.mobile.persistence.dao.aprop.maodeobra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.dao.DAOFactory;
import br.com.constran.mobile.persistence.dao.aprop.ApropriacaoDAO;
import br.com.constran.mobile.persistence.dao.aprop.BaseDAO;
import br.com.constran.mobile.persistence.vo.Intervalo;
import br.com.constran.mobile.persistence.vo.ObraVO;
import br.com.constran.mobile.persistence.vo.aprop.ApropriacaoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.*;
import br.com.constran.mobile.persistence.vo.imp.AtividadeVO;
import br.com.constran.mobile.persistence.vo.imp.FrenteObraVO;
import br.com.constran.mobile.persistence.vo.imp.ParalisacaoVO;
import br.com.constran.mobile.persistence.vo.imp.ServicoVO;
import br.com.constran.mobile.persistence.vo.menu.LocalizacaoVO;
import br.com.constran.mobile.validator.EventoEquipeValidator;
import br.com.constran.mobile.view.util.Util;

import java.util.List;

/**
 * Criado em 06/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class ParalisacaoMaoObraDAO extends BaseDAO<ParalisacaoMaoObraVO> {

    private static final String PARALISACAO_MAO_OBRA_COL_CCOBRA = "ccObra";
    private static final String PARALISACAO_MAO_OBRA_COL_PESSOA = "pessoa";
    private static final String PARALISACAO_MAO_OBRA_COL_HORA_INI = "horaInicio";
    private static final String PARALISACAO_MAO_OBRA_COL_HORA_TERM = "horaTermino";
    private static final String PARALISACAO_MAO_OBRA_COL_APROPRIACAO = "apropriacao";
    private static final String PARALISACAO_MAO_OBRA_COL_PARALISACAO = "paralisacao";
    private static final String PARALISACAO_MAO_OBRA_COL_EQUIPE = "equipe";
    private static final String PARALISACAO_MAO_OBRA_COL_OBS = "observacoes";
    private static final String PARALISACAO_MAO_OBRA_COL_SERVICO = "servico";

    private static ApropriacaoDAO apropriacaoDAO;
    private static ParalisacaoMaoObraDAO instance;
    private static AusenciaDAO ausenciaDAO;
    private static PessoalDAO pessoalDAO;

    private ParalisacaoMaoObraDAO(Context context) {
        super(context, TBL_PARALISACOES_MAO_OBRA);
    }

    public static ParalisacaoMaoObraDAO getInstance(Context context) {
        apropriacaoDAO = apropriacaoDAO == null ? ApropriacaoDAO.getInstance(context) : apropriacaoDAO;
        ausenciaDAO = ausenciaDAO == null ? AusenciaDAO.getInstance(context) : ausenciaDAO;
        pessoalDAO = pessoalDAO == null ? PessoalDAO.getInstance(context) : pessoalDAO;

        return instance == null ? instance = new ParalisacaoMaoObraDAO(context) : instance;
    }

    @Override
    public List<ParalisacaoMaoObraVO> findAllItems() {
        StringBuilder query = new StringBuilder();
        query.append("select pmo.*, apr.dataHoraApontamento, apr.tipoApropriacao, apr.atividade from ")
                .append(TBL_PARALISACOES_MAO_OBRA).append(" pmo ")
                .append("inner join ").append(TBL_APROPRIACAO).append(" apr on pmo.apropriacao = apr.idApropriacao ")
                .append("order by apr.dataHoraApontamento ");

        Cursor cursor = super.findByQuery(query.toString());

        List<ParalisacaoMaoObraVO> paralisacaoMaoObraVOs = bindList(cursor);

        if (paralisacaoMaoObraVOs != null) {
            ApropriacaoVO apropriacao = null;

            for (ParalisacaoMaoObraVO paralisacaoMaoObra : paralisacaoMaoObraVOs) {
                if (apropriacao == null || paralisacaoMaoObra.getApropriacao() == null || !apropriacao.getId().equals(paralisacaoMaoObra.getApropriacao().getId())) {
                    apropriacao = apropriacaoDAO.findByIdApropriacao(paralisacaoMaoObra.getApropriacao().getId());
                }

                paralisacaoMaoObra.setApropriacao(apropriacao);
            }
        }

        return paralisacaoMaoObraVOs;


    }

    public void save(EventoEquipeVO eventoEquipe, IntegranteVO integranteEquipe) {
        FrenteObraVO frenteObra = eventoEquipe.getLocalizacao().getAtividade().getFrenteObra();

        //se integrante estiver preenchido salva dados apenas para ele, senao salva para toda equipe
        if (integranteEquipe == null) {
            salvarParalisacaoMaoObraEquipe(eventoEquipe, frenteObra);
        } else {
            PessoalVO pessoa = integranteEquipe.getPessoa();
            AusenciaVO ausencia = new AusenciaVO(eventoEquipe.getEquipe(), pessoa, Util.getToday());

            //salva registro apenas quando usuário não faltou
            if (ausenciaDAO.findById(ausencia) == null) {
                save(preencherParalisacaoMaoObra(eventoEquipe, frenteObra, pessoa));
            }
        }
    }

    public List<ParalisacaoMaoObraVO> findByLocal(LocalizacaoVO local, EquipeTrabalhoVO equipe, PessoalVO pessoal, String data) {
        AtividadeVO atividade = local.getAtividade();
        FrenteObraVO frenteObra = atividade.getFrenteObra();

        StringBuilder query = new StringBuilder();
        query.append("select distinct pmo.*, par.descricao, srv.descricao ").append(ALIAS_DESCRICAO2)
                .append(" from ").append(TBL_PARALISACOES_MAO_OBRA).append(" pmo ")
                .append("left join servicos srv on srv.idServico = pmo.servico ")
                .append("inner join ").append(TBL_PARALISACAO).append(" par on pmo.paralisacao = par.idParalisacao ")
                .append("inner join  [apropriacoes]  apr on pmo.apropriacao = apr.idApropriacao ")
                .append("where pmo.ccObra = ? and apr.atividade = ? and pmo.equipe = ? and pmo.pessoa = ? ")
                .append(" and substr(apr.dataHoraApontamento,0,11)= ? ")
                .append("order by pmo.horaInicio ");

        String[] args = concatArgs(frenteObra.getIdObra(), atividade.getIdAtividade(), equipe.getId(), pessoal.getId(), Util.toSimpleDateFormat(data));
        Cursor cursor = super.findByQuery(query.toString(), args);

        return bindList(cursor);
    }

    /**
     * Busca todas as paralisacoes apontadas para o integrante na data informada
     *
     * @param pessoal
     * @param data
     * @return
     */
    public List<ParalisacaoMaoObraVO> findByPessoa(PessoalVO pessoal, String data) {
        StringBuilder query = new StringBuilder();
        query.append("select distinct pmo.*, par.descricao, srv.descricao ").append(ALIAS_DESCRICAO2)
                .append(" from ").append(TBL_PARALISACOES_MAO_OBRA).append(" pmo ")
                .append("left join servicos srv on srv.idServico = pmo.servico ")
                .append("inner join ").append(TBL_PARALISACAO).append(" par on pmo.paralisacao = par.idParalisacao ")
                .append("inner join  [apropriacoes]  apr on pmo.apropriacao = apr.idApropriacao ")
                .append("where pmo.pessoa = ? ")
                .append(" and substr(apr.dataHoraApontamento,0,11)= ? ")
                .append("order by pmo.horaInicio ");

        String[] args = concatArgs(pessoal.getId(), Util.toSimpleDateFormat(data));
        Cursor cursor = super.findByQuery(query.toString(), args);

        return bindList(cursor);
    }

    /**
     * Busca uma paralisacao da mao de obra para o mesmo integrante, apropriacao, obra e hora inicio
     *
     * @param amo
     * @return
     */
    public ParalisacaoMaoObraVO findByApropriacaoMaoObra(ApropriacaoMaoObraVO amo) {
        try {
            ParalisacaoMaoObraVO pmo = new ParalisacaoMaoObraVO();
            FrenteObraVO frenteObra = amo.getApropriacao().getAtividade().getFrenteObra();
            pmo.setObra(new ObraVO(frenteObra.getIdObra()));
            pmo.setApropriacao(amo.getApropriacao());
            pmo.setHoraInicio(amo.getHoraInicio());
            pmo.setPessoa(amo.getPessoa());

            return findById(pmo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void salvarParalisacaoMaoObraEquipe(EventoEquipeVO eventoEquipe, FrenteObraVO frenteObra) {
        //busca os integrantes que não estão na lista de faltosos
        List<PessoalVO> integrantes = pessoalDAO.findByIntegrantePresenteEquipe(eventoEquipe.getEquipe());

        if (integrantes != null) {
            //Paralisacao MO para cada membro da equipe
            for (PessoalVO pessoa : integrantes) {
                salvar(eventoEquipe, frenteObra, pessoa);
            }
        }
    }

    /**
     * Salva os dados de paralisacao considerando os eventos dos integrantes antes de replicar os dados
     * Caso o integrante esteja "ausente" a paralisacao so deve ser replicada se a hora termino da ausencia
     * for anterior a hora termino do evento sendo apropriado. caso a hora termino de ausencia nao seja informada
     * o evento nao deve ser replicado para o integrante, pois este estará ausente.
     *
     * @param eventoEquipe
     * @param frenteObra
     * @param pessoa
     */
    private void salvar(EventoEquipeVO eventoEquipe, FrenteObraVO frenteObra, PessoalVO pessoa) {
        ParalisacaoMaoObraVO pmo = preencherParalisacaoMaoObra(eventoEquipe, frenteObra, pessoa);

        ParalisacaoMaoObraVO pmoBD = instance.findById(pmo);

        //atualiza/sobrescreve a paralisacao MO individual apenas quando estiver editando a hora termino
        if (pmoBD != null && EventoEquipeValidator.existeParalisacaoMO(eventoEquipe, pmoBD)) {
            pmo.setObservacoes(pmoBD.getObservacoes() != null ? pmoBD.getObservacoes() : null);
            update(pmo);
        } else {
            salvarParalisacaoMaoObra(eventoEquipe, pessoa, pmo);
        }
    }

    private void salvarParalisacaoMaoObra(EventoEquipeVO eventoEquipe, PessoalVO pessoa, ParalisacaoMaoObraVO pmo) {
        EventoEquipeDAO eventoEquipeDAO = DAOFactory.getInstance(context).getEventoEquipeDAO();
        List<EventoEquipeVO> eventosIntegrante = eventoEquipeDAO.findApontamentos(pessoa, Util.getToday());

        //verifica se o integrante esta AUSENTE da equipe(replica se nao estiver ausente, ou apos o periodo de ausencia)
        boolean ultimoEventoAusencia = eventoEquipeDAO.isUltimoEventoAusencia(eventosIntegrante);
        String ultimaHoraEvento = null;

        if (ultimoEventoAusencia) {
            ultimaHoraEvento = eventoEquipeDAO.getUltimaHoraEvento(eventosIntegrante, false);
        }

        boolean jaSalvou = false;

        //a hora de inicio do evento apontado deve ser posterior ao retorno do evento de ausente
        if (ultimoEventoAusencia && ultimaHoraEvento != null && Util.isHoraPosteriorOuIgual(pmo.getHoraTermino(), ultimaHoraEvento)) {
            pmo.setHoraInicio(ultimaHoraEvento);
        } else if (EventoEquipeValidator.hasConflitoHorario(eventosIntegrante, eventoEquipe)) {
            List<Intervalo> intervalos = EventoEquipeValidator.getIntervalosDisponiveis(eventosIntegrante, eventoEquipe);

            if (intervalos != null && !intervalos.isEmpty()) {

                //replicar apontamento para cada intervalo disponivel
                for (Intervalo intervalo : intervalos) {
                    pmo.setHoraInicio(intervalo.getHoraIni());
                    pmo.setHoraTermino(intervalo.getHoraFim());

                    checkAndSave(eventoEquipe, pmo);
                }

                jaSalvou = true;
            }
        }

        if (!jaSalvou) {
            checkAndSave(eventoEquipe, pmo);
        }

    }

    private void checkAndSave(EventoEquipeVO eventoEquipe, ParalisacaoMaoObraVO pmo) {
        //verica se existe alguma apropriacao MO cadastrada para o mesmo horario de inicio, apropriacao
        //se existir entao, nao deve inserir
        ApropriacaoMaoObraVO amo = DAOFactory.getInstance(context).getApropriacaoMaoObraDAO().findByParalisacaoMaoObra(pmo);

        if (amo == null) {
            pmo.setObservacoes(pmo.getObservacoes() == null || pmo.getObservacoes().isEmpty() ? null : pmo.getObservacoes());
            save(pmo);
        }
        //se mudou de paralisacao para produzindo
        else if (EventoEquipeValidator.mudouParaParalisado(eventoEquipe, amo)) {
            //apaga apropriacao MO que está sendo substituida por paralisacao MO
            DAOFactory.getInstance(context).getApropriacaoMaoObraDAO().delete(amo);
            save(pmo);
        }
    }

    private ParalisacaoMaoObraVO preencherParalisacaoMaoObra(EventoEquipeVO eventoEquipe, FrenteObraVO frenteObra, PessoalVO pessoa) {
        ApropriacaoServicoVO apropriacaoServico = new ApropriacaoServicoVO();
        apropriacaoServico.setServico(eventoEquipe.getServico());

        ParalisacaoMaoObraVO paralisacaoMaoObra = new ParalisacaoMaoObraVO();

        paralisacaoMaoObra.setHoraInicio(eventoEquipe.getHoraIni());
        paralisacaoMaoObra.setHoraTermino(eventoEquipe.getHoraFim());
        paralisacaoMaoObra.setApropriacao(eventoEquipe.getApropriacao());
        paralisacaoMaoObra.setParalisacao(eventoEquipe.getParalisacao());
        paralisacaoMaoObra.setEquipe(eventoEquipe.getEquipe());
        paralisacaoMaoObra.setObservacoes(eventoEquipe.getObservacao());
        paralisacaoMaoObra.setObra(new ObraVO(frenteObra.getIdObra()));
        paralisacaoMaoObra.setPessoa(pessoa);
        paralisacaoMaoObra.setApropriacaoServico(apropriacaoServico);


        return paralisacaoMaoObra;
    }

    /**
     * Exclui paralisacoes da equipe
     *
     * @param eventoEquipe
     */
    public void delete(EventoEquipeVO eventoEquipe) {
        FrenteObraVO frenteObra = eventoEquipe.getLocalizacao().getAtividade().getFrenteObra();
        ApropriacaoVO apropriacao = eventoEquipe.getApropriacao();
        String horaIni = eventoEquipe.getHoraIni();
        String horaFim = eventoEquipe.getHoraFim();
        Integer servico = eventoEquipe.getServico() != null ? eventoEquipe.getServico().getId() : null;
        Integer equipe = eventoEquipe.getEquipe().getId();

        StringBuilder whereClause = new StringBuilder();
        whereClause.append("ccObra = " + frenteObra.getIdObra())
                .append(" and apropriacao = " + apropriacao.getId())
                .append(" and paralisacao = " + eventoEquipe.getParalisacao().getId());

        if (horaFim != null && !horaFim.isEmpty()) {
            whereClause.append(" and time(horaInicio) between time('" + horaIni + "') and time('" + horaFim + "')");
            whereClause.append(" and time(horaTermino) between time('" + horaIni + "') and time('" + horaFim + "')");
        } else {
            whereClause.append(" and time(horaInicio) >= time('" + horaIni + "') ");
            whereClause.append(" and case horaTermino when '' then 1=1 else time(horaTermino) >= time('" + horaIni + "') end ");
        }

        whereClause.append(" and equipe = " + equipe);

        if (servico != null && servico > 0) {
            whereClause.append(" and servico = " + servico);
        }

        deleteWhere(whereClause.toString());
    }

    /**
     * Exclui paralisacoes do integrante
     *
     * @param eventoEquipe
     */
    public void delete(EventoEquipeVO eventoEquipe, IntegranteVO integrante) {
        FrenteObraVO frenteObra = eventoEquipe.getLocalizacao().getAtividade().getFrenteObra();
        ParalisacaoMaoObraVO paralisacaoMaoObra = preencherParalisacaoMaoObra(eventoEquipe, frenteObra, integrante.getPessoa());

        super.delete(paralisacaoMaoObra);
    }

    /**
     * Remove apropriacaoMaoObra a partir da apropriacao e da equipe
     *
     * @param apropriacao
     * @param equipe
     */
    public void delete(ApropriacaoVO apropriacao, EquipeTrabalhoVO equipe) {
        StringBuilder whereClause = new StringBuilder();

        whereClause.append(PARALISACAO_MAO_OBRA_COL_APROPRIACAO + EQ + apropriacao.getId())
                .append(AND + PARALISACAO_MAO_OBRA_COL_EQUIPE + EQ + equipe.getId());

        deleteWhere(whereClause.toString());
    }

    /**
     * Exclui paralisacoes por integrante da equipe
     *
     * @param equipe
     * @param integrante
     */
    public void deleteByIntegrante(EquipeTrabalhoVO equipe, IntegranteVO integrante) {
        if (equipe == null || integrante == null || integrante.getPessoa() == null) {
            return;
        }

        StringBuilder whereClause = new StringBuilder();
        whereClause.append(PARALISACAO_MAO_OBRA_COL_EQUIPE).append(EQ).append(equipe.getId())
                .append(AND).append(PARALISACAO_MAO_OBRA_COL_PESSOA).append(EQ).append(integrante.getPessoa().getId());

        deleteWhere(whereClause.toString());
    }

    @Override
    public ParalisacaoMaoObraVO popularEntidade(Cursor cursor) {
        ApropriacaoServicoVO apropriacaoServico = new ApropriacaoServicoVO();
        ParalisacaoMaoObraVO paralisacaoMaoObra = new ParalisacaoMaoObraVO();
        ObraVO obra = new ObraVO(getInt(cursor, PARALISACAO_MAO_OBRA_COL_CCOBRA));
        PessoalVO pessoa = new PessoalVO(getInt(cursor, PARALISACAO_MAO_OBRA_COL_PESSOA));
        ApropriacaoVO apropriacao = new ApropriacaoVO(getInt(cursor, PARALISACAO_MAO_OBRA_COL_APROPRIACAO), "");
        ParalisacaoVO paralisacao = new ParalisacaoVO(getInt(cursor, PARALISACAO_MAO_OBRA_COL_PARALISACAO), getString(cursor, ALIAS_DESCRICAO));
        EquipeTrabalhoVO equipe = new EquipeTrabalhoVO(getInt(cursor, PARALISACAO_MAO_OBRA_COL_EQUIPE));
        ServicoVO servico = new ServicoVO(getInt(cursor, PARALISACAO_MAO_OBRA_COL_SERVICO), getString(cursor, ALIAS_DESCRICAO2), null);
        apropriacaoServico.setServico(servico);

        paralisacaoMaoObra.setHoraInicio(getString(cursor, PARALISACAO_MAO_OBRA_COL_HORA_INI));
        paralisacaoMaoObra.setHoraTermino(getString(cursor, PARALISACAO_MAO_OBRA_COL_HORA_TERM));
        paralisacaoMaoObra.setObservacoes(getString(cursor, PARALISACAO_MAO_OBRA_COL_OBS));
        paralisacaoMaoObra.setObra(obra);
        paralisacaoMaoObra.setPessoa(pessoa);
        paralisacaoMaoObra.setApropriacao(apropriacao);
        paralisacaoMaoObra.setParalisacao(paralisacao);
        paralisacaoMaoObra.setEquipe(equipe);
        paralisacaoMaoObra.setApropriacaoServico(apropriacaoServico);

        return paralisacaoMaoObra;
    }

    @Override
    public ContentValues bindContentValues(ParalisacaoMaoObraVO pmo) {
        ContentValues contentValues = new ContentValues();
        ObraVO obra = pmo.getObra();
        PessoalVO pessoa = pmo.getPessoa();
        EquipeTrabalhoVO equipe = pmo.getEquipe();
        ParalisacaoVO paralisacao = pmo.getParalisacao();
        ApropriacaoVO apropriacao = pmo.getApropriacao();
        ApropriacaoServicoVO apropriacaoServico = pmo.getApropriacaoServico();
        ServicoVO servico = apropriacaoServico.getServico();

        contentValues.put(PARALISACAO_MAO_OBRA_COL_CCOBRA, obra != null ? obra.getId() : null);
        contentValues.put(PARALISACAO_MAO_OBRA_COL_PESSOA, pessoa != null ? pessoa.getId() : null);
        contentValues.put(PARALISACAO_MAO_OBRA_COL_EQUIPE, equipe != null ? equipe.getId() : null);
        contentValues.put(PARALISACAO_MAO_OBRA_COL_APROPRIACAO, apropriacao != null ? apropriacao.getId() : null);
        contentValues.put(PARALISACAO_MAO_OBRA_COL_PARALISACAO, paralisacao != null ? paralisacao.getId() : null);
        contentValues.put(PARALISACAO_MAO_OBRA_COL_SERVICO, servico != null ? servico.getId() : null);
        contentValues.put(PARALISACAO_MAO_OBRA_COL_HORA_INI, pmo.getHoraInicio());
        contentValues.put(PARALISACAO_MAO_OBRA_COL_HORA_TERM, pmo.getHoraTermino());
        contentValues.put(PARALISACAO_MAO_OBRA_COL_OBS, pmo.getObservacoes());

        return contentValues;
    }

    @Override
    public boolean isNewEntity(ParalisacaoMaoObraVO pmo) {
//        return pmo != null && pmo.getObra() == null || pmo.getObra().getId() == null;
        return findById(pmo) == null;
    }

    @Override
    public Object[] getPkArgs(ParalisacaoMaoObraVO pmo) {
        return new Object[]{pmo.getObra().getId(), pmo.getPessoa().getId(), pmo.getHoraInicio()};
    }

    @Override
    public String[] getPkColumns() {
        return new String[]{PARALISACAO_MAO_OBRA_COL_CCOBRA, PARALISACAO_MAO_OBRA_COL_PESSOA, PARALISACAO_MAO_OBRA_COL_HORA_INI};
    }

}
