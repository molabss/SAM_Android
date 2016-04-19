package br.com.constran.mobile.persistence.dao.aprop.maodeobra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import br.com.constran.mobile.enums.TipoAplicacao;
import br.com.constran.mobile.persistence.dao.aprop.ApropriacaoDAO;
import br.com.constran.mobile.persistence.dao.aprop.BaseDAO;
import br.com.constran.mobile.persistence.dao.imp.ParalisacaoDAO;
import br.com.constran.mobile.persistence.dao.imp.ServicoDAO;
import br.com.constran.mobile.persistence.dao.menu.LocalizacaoDAO;
import br.com.constran.mobile.persistence.vo.ObraVO;
import br.com.constran.mobile.persistence.vo.aprop.ApropriacaoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.*;
import br.com.constran.mobile.persistence.vo.imp.FrenteObraVO;
import br.com.constran.mobile.persistence.vo.imp.ParalisacaoVO;
import br.com.constran.mobile.persistence.vo.imp.ServicoVO;
import br.com.constran.mobile.persistence.vo.menu.LocalizacaoVO;
import br.com.constran.mobile.view.util.Util;

import java.util.*;

/**
 * Criado em 10/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class EventoEquipeDAO extends BaseDAO<EventoEquipeVO> {

    private static final String PRODUZINDO_DESC = " Produzindo";

    private static final String TP_SERVICO = "SRV";
    private static final String EVENTO_EQUIPE_COL_ID = "idEventoEquipe";
    private static final String EVENTO_EQUIPE_COL_LOCALIZACAO = "localizacao";
    private static final String EVENTO_EQUIPE_COL_EQUIPE = "equipe";
    private static final String EVENTO_EQUIPE_COL_PARALISA = "paralisacao";
    private static final String EVENTO_EQUIPE_COL_APROPRIA = "apropriacao";
    private static final String EVENTO_EQUIPE_COL_TP_HORARI = "tipoHorario";
    private static final String EVENTO_EQUIPE_COL_DATA = "data";
    private static final String EVENTO_EQUIPE_COL_HORA_INI = "horaIni";
    private static final String EVENTO_EQUIPE_COL_HORA_FIM = "horaFim";
    private static final String EVENTO_EQUIPE_COL_OBS = "observacao";
    private static final String EVENTO_EQUIPE_COL_SERVICO = "servico";
    private static final String EQUIPE_COL_APELIDO = "apelido";

    private static EventoEquipeDAO instance;
    private static LocalizacaoDAO localizacaoDAO;
    private static ApropriacaoDAO apropriacaoDAO;
    private static ParalisacaoDAO paralisacaoDAO;
    private static ApropriacaoServicoDAO apropriacaoServicoDAO;
    private static ApropriacaoMaoObraDAO apropriacaoMaoObraDAO;
    private static ParalisacaoEquipeDAO paralisacaoEquipeDAO;
    private static ParalisacaoMaoObraDAO paralisacaoMaoObraDAO;
    private static EquipeTrabalhoDAO equipeTrabalhoDAO;
    private static ServicoDAO servicoDAO;
    private static AusenciaDAO ausenciaDAO;


    public EventoEquipeDAO(Context context) {
        super(context, TBL_EVENTO_EQUIPE);
    }

    public static EventoEquipeDAO getInstance(Context context) {
        localizacaoDAO = localizacaoDAO == null ? LocalizacaoDAO.getInstance(context) : localizacaoDAO;
        apropriacaoDAO = apropriacaoDAO == null ? ApropriacaoDAO.getInstance(context) : apropriacaoDAO;
        paralisacaoDAO = paralisacaoDAO == null ? ParalisacaoDAO.getInstance(context) : paralisacaoDAO;
        apropriacaoServicoDAO = apropriacaoServicoDAO == null ? ApropriacaoServicoDAO.getInstance(context) : apropriacaoServicoDAO;
        apropriacaoMaoObraDAO = apropriacaoMaoObraDAO == null ? ApropriacaoMaoObraDAO.getInstance(context) : apropriacaoMaoObraDAO;
        paralisacaoEquipeDAO = paralisacaoEquipeDAO == null ? ParalisacaoEquipeDAO.getInstance(context) : paralisacaoEquipeDAO;
        paralisacaoMaoObraDAO = paralisacaoMaoObraDAO == null ? ParalisacaoMaoObraDAO.getInstance(context) : paralisacaoMaoObraDAO;
        equipeTrabalhoDAO = equipeTrabalhoDAO == null ? EquipeTrabalhoDAO.getInstance(context) : equipeTrabalhoDAO;
        servicoDAO = servicoDAO == null ? ServicoDAO.getInstance(context) : servicoDAO;
        ausenciaDAO = ausenciaDAO == null ? AusenciaDAO.getInstance(context) : ausenciaDAO;

        return instance == null ? instance = new EventoEquipeDAO(context) : instance;
    }

    public static EventoEquipeVO popularEntidade(Context context, Cursor cursor) {
        return getInstance(context).popularEntidade(cursor);
    }

    /**
     * Salva apontamentos da equipe, podendo replicar os mesmos para os seus integrantes
     *
     * @param eventoEquipe
     * @param replicarParaMaoObra
     */
    public void save(EventoEquipeVO eventoEquipe, boolean replicarParaMaoObra) {

        //salvar apropriacao
        salvarApropriacao(eventoEquipe.getApropriacao());

        //salvar apropriacao servico
        salvarApropriacaoServico(eventoEquipe, true);

        if (replicarParaMaoObra) {
            //salvar apropriacao mao-de-obra
            salvarApropriacaoMaoObra(eventoEquipe, null);
        }

        //salvar Paralisacao Equipe
        salvarParalisacaoEquipe(eventoEquipe);

        if (replicarParaMaoObra) {
            //salvar Paralisacao Mao-de-Obra
            salvarParalisacaoMaoObra(eventoEquipe, null);
        }

        //salvar evento equipe
        super.save(eventoEquipe);
    }

    public void salvarApontamentoIndividual(EventoEquipeVO eventoEquipe, IntegranteVO integrante) {
        //salvar apropriacao
        salvarApropriacao(eventoEquipe.getApropriacao());

        //salvar apropriacao servico
        salvarApropriacaoServico(eventoEquipe, false);

        //salvar apropriacao mao-de-obra
        salvarApropriacaoMaoObra(eventoEquipe, integrante);

        //salvar Paralisacao Mao de Obra
        salvarParalisacaoMaoObra(eventoEquipe, integrante);

        //salvar evento equipe
//        super.save(eventoEquipe);
    }

    /**
     * Seleciona os eventos apontados para a equipe e replica para o integrante adicionado seguindo a regra
     * <p/>
     * Regra: caso o integrante adicionado seja temporario ele pode ter sido marcado como "ausente" em outra equipe
     * Neste caso:
     * 1- SE o evento não estiver com hora termino preenchida os eventos so devem ser replicados posteriormente a hora inicio
     * 2- OU devem ser replicados depois da hora termino apontada no evento ausente da equipe anterior
     * E em ambos os casos, a regra é válida SOMENTE quando o evento "ausente" foi o último do integrante na equipe antiga
     * Caso contrário, significaria que ele ainda está alocado a outra equipe
     *
     * @param local
     * @param equipe
     * @param integrante
     * @return
     */
    public boolean replicarApontamentosEquipe(LocalizacaoVO local, EquipeTrabalhoVO equipe, IntegranteVO integrante) {
        try {
            //busca os eventos apontados para a equipe no dia
            List<EventoEquipeVO> eventoEquipes = findByLocalizacaoAndEquipe(local, equipe, Util.getToday());

            if (eventoEquipes != null && !eventoEquipes.isEmpty()) {
                //busca eventos apontados para o integrante no dia atual
                List<EventoEquipeVO> eventosIntegrante = findApontamentos(integrante.getPessoa(), Util.getToday());

                //se o integrante nao possuir nenhum evento, entao pode copiar os eventos da equipe para este integrante sem restricoes
                if (eventosIntegrante == null || eventosIntegrante.isEmpty()) {
                    copiarEventos(local, equipe, integrante, eventoEquipes, null);
                } else {
                    if (isUltimoEventoAusencia(eventosIntegrante)) {
                        //copiar apontamentos a partir da hora inicio da ultima ausencia
                        copiarEventos(local, equipe, integrante, eventoEquipes, getUltimaHoraEvento(eventosIntegrante, true));
                    } else {
                        //alerta de erro: nao pode copiar apontamentos, pois o integrante esta alocado a outra equipe
                        return false;
                    }
                }
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica se o ultimo evento apontado para o integrante foi o de "ausencia"
     *
     * @param eventosIntegrante
     * @return
     */
    public boolean isUltimoEventoAusencia(List<EventoEquipeVO> eventosIntegrante) {
        if (eventosIntegrante != null && !eventosIntegrante.isEmpty()) {
            Date ultimaHora = null;
            Date horaIniAusencia = null;

            for (EventoEquipeVO evento : eventosIntegrante) {
                Date horaIni = Util.toHourFormat(evento.getHoraIni());

                if (ultimaHora == null || horaIni.after(ultimaHora)) {
                    ultimaHora = horaIni;
                }

                if (evento.getParalisacao() != null && COD_AUSENTE.equals(evento.getParalisacao().getId())) {
                    horaIniAusencia = horaIniAusencia == null || horaIniAusencia.before(horaIni) ? horaIni : horaIniAusencia;
                }
            }

            return horaIniAusencia != null && horaIniAusencia.equals(ultimaHora);
        }

        return false;
    }

    /**
     * Busca a ultima hora do evento apontado
     * Se o parametro checkHoraIni for true, a ultima hora informada sera a de inicio,
     * caso contrário, será a hora fim.
     *
     * @param eventosIntegrante
     * @param checkHoraIni
     * @return
     */
    public String getUltimaHoraEvento(List<EventoEquipeVO> eventosIntegrante, boolean checkHoraIni) {
        String ultimaHoraStr = null;

        if (eventosIntegrante != null && !eventosIntegrante.isEmpty()) {
            Date ultimaHora = null;

            for (EventoEquipeVO evento : eventosIntegrante) {
                Date hora = Util.toHourFormat(checkHoraIni ? evento.getHoraIni() : evento.getHoraFim());

                if (hora != null && (ultimaHora == null || hora.after(ultimaHora))) {
                    ultimaHora = hora;
                    ultimaHoraStr = checkHoraIni ? evento.getHoraIni() : evento.getHoraFim();
                }
            }
        }

        return ultimaHoraStr;
    }

    /**
     * Copia os eventos da equipe para o integrante na integra (caso horaIni nao informada)
     * OU copia os eventos a partir da horaIni informada (caso existam)
     *
     * @param local
     * @param equipe
     * @param integrante
     * @param eventoEquipes
     * @param horaIni
     */
    private void copiarEventos(LocalizacaoVO local, EquipeTrabalhoVO equipe, IntegranteVO integrante,
                               List<EventoEquipeVO> eventoEquipes, String horaIni) {
        for (EventoEquipeVO eeq : eventoEquipes) {
            //so replica os eventos se nao houver hora Ini informada OU se hora Ini posterior ao evento corrente
            if (horaIni == null || Util.isHoraPosteriorOuIgual(eeq.getHoraIni(), horaIni)) {
                ApropriacaoVO apropriacao = eeq.getApropriacao();
                PessoalVO pessoa = integrante.getPessoa();
                ServicoVO servico = eeq.getServico();
                ParalisacaoVO paralisacao = eeq.getParalisacao();
                FrenteObraVO frenteObra = local.getAtividade().getFrenteObra();
                ObraVO obra = new ObraVO(frenteObra.getIdObra());

                //replicar apropriacao mao obra
                if (servico != null && paralisacao != null && COD_PRODUZINDO.equals(paralisacao.getId())) {
                    salvarApropriacaoMaoObra(equipe, eeq, apropriacao, pessoa, servico, eeq.getHoraIni());
                }

                //replicar paralisacao mao obra
                if (paralisacao != null && !COD_PRODUZINDO.equals(paralisacao.getId())) {
                    salvarParalisacaoMaoObra(equipe, eeq, apropriacao, pessoa, paralisacao, obra, eeq.getHoraIni());
                }
            }
        }
    }

    private void salvarParalisacaoMaoObra(EquipeTrabalhoVO equipe, EventoEquipeVO eeq, ApropriacaoVO apropriacao, PessoalVO pessoa,
                                          ParalisacaoVO paralisacao, ObraVO obra, String horaIni) {
        ApropriacaoServicoVO apropriacaoServico = new ApropriacaoServicoVO();
        apropriacaoServico.setServico(eeq.getServico());

        ParalisacaoMaoObraVO pmo = new ParalisacaoMaoObraVO();
        pmo.setEquipe(equipe);
        pmo.setPessoa(pessoa);
        pmo.setParalisacao(paralisacao);
        pmo.setHoraInicio(horaIni == null ? eeq.getHoraIni() : horaIni);
        pmo.setHoraTermino(eeq.getHoraFim());
        pmo.setObra(obra);
        pmo.setObservacoes(eeq.getObservacao());
        pmo.setApropriacao(apropriacao);
        pmo.setApropriacaoServico(apropriacaoServico);

        AusenciaVO ausencia = new AusenciaVO(equipe, pessoa, Util.getToday());

        //salva registro apenas quando usuário não faltou
        if (ausenciaDAO.findById(ausencia) == null) {
            paralisacaoMaoObraDAO.save(pmo);
        }

    }

    /**
     * Salva as apropriacoes de mao-de-obra a partir da hora inicio especificada,
     * OU busca o horario a partir do evento quando hora ini nao for informados
     *
     * @param equipe
     * @param eeq
     * @param apropriacao
     * @param pessoa
     * @param servico
     * @param horaIni
     */
    private void salvarApropriacaoMaoObra(EquipeTrabalhoVO equipe, EventoEquipeVO eeq, ApropriacaoVO apropriacao,
                                          PessoalVO pessoa, ServicoVO servico, String horaIni) {
        ApropriacaoServicoVO aps = new ApropriacaoServicoVO();
        aps.setApropriacao(apropriacao);
        aps.setServico(servico);
        aps.setEquipe(eeq.getEquipe());
        aps.setHoraIni(eeq.getHoraIni());

        aps = apropriacaoServicoDAO.findByApropriacaoServico(aps);

        ApropriacaoMaoObraVO amo = new ApropriacaoMaoObraVO();
        amo.setApropriacao(apropriacao);
        amo.setEquipe(equipe);
        amo.setApropriacaoServico(aps);
        amo.setHoraInicio(horaIni == null ? eeq.getHoraIni() : horaIni);
        amo.setHoraTermino(eeq.getHoraFim());
        amo.setObservacoes(eeq.getObservacao());
        amo.setPessoa(pessoa);

        AusenciaVO ausencia = new AusenciaVO(equipe, pessoa, Util.getToday());

        //salva registro apenas quando usuário não faltou
        if (ausenciaDAO.findById(ausencia) == null) {
            apropriacaoMaoObraDAO.save(amo);
        }

    }

    private void salvarApropriacao(ApropriacaoVO apropriacao) {
        ApropriacaoVO apr = apropriacaoDAO.findByPK(apropriacao);

        if (apr != null) {
            apropriacao.setId(apr.getId());
        }

        apropriacao.setTipoApropriacao(TP_SERVICO);
        apropriacao.setId(apropriacaoDAO.save(apropriacao));
    }

    /**
     * A apropriacao servico so deve ser atualizada quando o tipo do evento for 'PRODUZINDO'
     *
     * @param eventoEquipe
     * @param atualizarObs
     */
    private void salvarApropriacaoServico(EventoEquipeVO eventoEquipe, boolean atualizarObs) {
        ServicoVO servico = eventoEquipe.getServico();
        ApropriacaoVO apropriacao = eventoEquipe.getApropriacao();
        ParalisacaoVO paralisacao = eventoEquipe.getParalisacao();

        if (apropriacao != null && apropriacao.getId() != null && paralisacao != null && COD_PRODUZINDO.equals(paralisacao.getId())
                && servico != null && servico.getId() != null) {

            ApropriacaoServicoVO aps = preencherApropriacaoServico(eventoEquipe);
            ApropriacaoServicoVO apropriacaoServicoBD = apropriacaoServicoDAO.findByApropriacaoServico(aps);

            //so atualiza o campo observacoes quando for apontado na tela de equipes
            if (apropriacaoServicoBD == null) {
                apropriacaoServicoDAO.insert(aps);
            } else {
                //no ajuste individual o campo obs nao deve modificar a obs salva pela equipe
                if (!atualizarObs) {
                    aps.setObservacoes(apropriacaoServicoBD.getObservacoes());
                }

                //na atualizacao, a quantidade é mantida
                aps.setQuantidadeProduzida(apropriacaoServicoBD.getQuantidadeProduzida());

                apropriacaoServicoDAO.update(aps);
            }
        }
    }

    /**
     * Salva a apropriacao da mao de obra para um integrante ou todos os integrantes da equipe
     * dependendo do parametro @integrante.
     * Se @integrante for informado (diferente de null) entao apenas este integrante tera a
     * apropriacao de mao de obra salva, senao todos os integrantes poderao ter a apropriacao salva
     * dependendo das regras de negocio.
     *
     * @param eventoEquipe
     * @param integrante
     */
    private void salvarApropriacaoMaoObra(EventoEquipeVO eventoEquipe, IntegranteVO integrante) {
        ApropriacaoVO apropriacao = eventoEquipe.getApropriacao();
        ParalisacaoVO paralisacao = eventoEquipe.getParalisacao();

        if (apropriacao != null && apropriacao.getId() != null && paralisacao != null && COD_PRODUZINDO.equals(paralisacao.getId())) {
            apropriacaoMaoObraDAO.save(eventoEquipe, integrante);
        }
    }

    private void salvarParalisacaoEquipe(EventoEquipeVO eventoEquipe) {
        ApropriacaoVO apropriacao = eventoEquipe.getApropriacao();
        EquipeTrabalhoVO equipe = eventoEquipe.getEquipe();
        ParalisacaoVO paralisacao = eventoEquipe.getParalisacao();
        String horaFim = eventoEquipe.getHoraFim();
        String horaIni = eventoEquipe.getHoraIni();

        //horaFim é obrigado
        if (horaFim == null || horaFim.isEmpty()) {
            horaFim = "";
        }

        if (paralisacao != null && equipe != null && apropriacao != null && horaIni != null && horaFim != null && !COD_PRODUZINDO.equals(paralisacao.getId())) {
            ParalisacaoEquipeVO paralisacaoEquipe = preencherParalisacaoEquipe(eventoEquipe);

            paralisacaoEquipeDAO.save(paralisacaoEquipe);
        }
    }

    /**
     * Salva a paralisacao da mao de obra para um integrante ou todos os integrantes da equipe
     * dependendo do parametro @integrante.
     * Se @integrante for informado (diferente de null) entao apenas este integrante tera a
     * paralisacao de mao de obra salva, senao todos os integrantes poderao ter a paralisacao salva
     * dependendo das regras de negocio.
     *
     * @param eventoEquipe
     * @param integrante
     */
    private void salvarParalisacaoMaoObra(EventoEquipeVO eventoEquipe, IntegranteVO integrante) {
        if (eventoEquipe.getParalisacao() != null && !COD_PRODUZINDO.equals(eventoEquipe.getParalisacao().getId())) {
            paralisacaoMaoObraDAO.save(eventoEquipe, integrante);
        }
    }

    @Override
    public void delete(EventoEquipeVO eventoEquipe) {
        ApropriacaoVO apropriacao = eventoEquipe.getApropriacao();

        ApropriacaoServicoVO apropriacaoServico = preencherApropriacaoServico(eventoEquipe);
        ParalisacaoEquipeVO paralisacaoEquipe = preencherParalisacaoEquipe(eventoEquipe);
        ParalisacaoVO paralisacao = paralisacaoEquipe.getParalisacao();

        //excluir apropriacao servico
        if (apropriacaoServico != null && apropriacaoServico.getServico() != null) {
            apropriacaoServicoDAO.delete(apropriacaoServico);
        }

        //excluir paralisacao equipe
        paralisacaoEquipeDAO.delete(paralisacaoEquipe);

        //excluir apropriacao mao de obra
        if (apropriacao != null && apropriacao.getId() != null && paralisacao != null && COD_PRODUZINDO.equals(paralisacao.getId())) {
            apropriacaoMaoObraDAO.delete(eventoEquipe);
        }

        //excluir paralisacao mao de obra
        if (eventoEquipe.getLocalizacao() != null && paralisacao != null && !COD_PRODUZINDO.equals(paralisacao.getId())) {
            paralisacaoMaoObraDAO.delete(eventoEquipe);
        }

        //excluir apropriacao
        apropriacaoDAO.deleteOrfan(apropriacao.getId(), Util.getToday());

        super.delete(eventoEquipe);
    }

    public void deleteApontamentoIndividual(EventoEquipeVO eventoEquipe, IntegranteVO integrante) {
        ApropriacaoVO apropriacao = eventoEquipe.getApropriacao();

        ParalisacaoEquipeVO paralisacaoEquipe = preencherParalisacaoEquipe(eventoEquipe);
        ParalisacaoVO paralisacao = paralisacaoEquipe.getParalisacao();

        //excluir apropriacao mao de obra
        if (apropriacao != null && apropriacao.getId() != null && paralisacao != null && COD_PRODUZINDO.equals(paralisacao.getId())) {
            apropriacaoMaoObraDAO.delete(eventoEquipe, integrante);
        }

        //excluir paralisacao mao de obra
        if (eventoEquipe.getLocalizacao() != null && paralisacao != null && !COD_PRODUZINDO.equals(paralisacao.getId())) {
            paralisacaoMaoObraDAO.delete(eventoEquipe, integrante);
        }

        //excluir apropriacao orfan
        if (apropriacao != null && apropriacao.getId() != null) {
            apropriacaoServicoDAO.deleteOrfan(eventoEquipe, Util.getToday());
            apropriacaoDAO.deleteOrfan(apropriacao.getId(), Util.getToday());
        }

    }

    /**
     * Exclui todos os eventos/apropriacoes/paralisacoes feitos para a equipe naquele local e data atual
     * a data utilizada é a atual
     */
    public void deleteByLocalAndEquipe(LocalizacaoVO local, EquipeTrabalhoVO equipe) {
        ApropriacaoVO apropriacao = new ApropriacaoVO();
        apropriacao.setAtividade(local.getAtividade());
        apropriacao.setTipoApropriacao(TP_SERVICO);
        apropriacao.setDataHoraApontamento(Util.getNow());

        apropriacao = apropriacaoDAO.findByPK(apropriacao);

        if (apropriacao != null) {
            //excluir apropriacao servico
            apropriacaoServicoDAO.delete(apropriacao, equipe);

            //excluir paralisacao equipe
            paralisacaoEquipeDAO.delete(apropriacao, equipe);

            //excluir apropriacao mao de obra
            apropriacaoMaoObraDAO.delete(apropriacao, equipe);

            //excluir paralisacao mao de obra
            paralisacaoMaoObraDAO.delete(apropriacao, equipe);

            //excluir apropriacao
            apropriacaoDAO.deleteOrfan(apropriacao.getId(), Util.getToday());

            //exclui eventos equipe
            delete(apropriacao, equipe);
        }
    }

    /**
     * Remove apropriacaoMaoObra a partir da apropriacao e da equipe
     *
     * @param apropriacao
     * @param equipe
     */
    public void delete(ApropriacaoVO apropriacao, EquipeTrabalhoVO equipe) {
        StringBuilder whereClause = new StringBuilder();

        whereClause.append(EVENTO_EQUIPE_COL_APROPRIA + EQ + apropriacao.getId())
                .append(AND + EVENTO_EQUIPE_COL_EQUIPE + EQ + equipe.getId());

        deleteWhere(whereClause.toString());
    }

    /**
     * Exclui o evento (apropriacaoMO ou paralisacaoMO) se mudar o tipo evento
     * PARALISACAO > PRODUCAO ou PRODUCAO > PARALISACAO, excluir o registro do primeiro antes
     *
     * @param eventoEquipeBD
     * @param local
     * @param tipoEvento
     * @param integrante
     */
    public void deleteWhenTipoEventoChanges(EventoEquipeVO eventoEquipeBD, LocalizacaoVO local, ParalisacaoVO tipoEvento, IntegranteVO integrante) {
        if (eventoEquipeBD != null) {
            ParalisacaoVO paralisacao = eventoEquipeBD.getParalisacao();
            eventoEquipeBD.setLocalizacao(local);

            //se o item estava como produzindo e foi mudado para paralisação
            if (COD_PRODUZINDO.equals(paralisacao.getId()) && !COD_PRODUZINDO.equals(tipoEvento.getId())) {
                apropriacaoMaoObraDAO.delete(eventoEquipeBD, integrante);
            }

            //se o item estava como paralisacao e foi mudado para produzindo
            if (!COD_PRODUZINDO.equals(paralisacao.getId()) && COD_PRODUZINDO.equals(tipoEvento.getId())) {
                paralisacaoMaoObraDAO.delete(eventoEquipeBD, integrante);
            }
        }

    }

    @Override
    public List<EventoEquipeVO> findAllItems() {
        StringBuilder query = new StringBuilder();
        query.append("select eeq.*,")
                .append("ifnull(par.descricao,\"Produzindo\") ").append(ALIAS_DESCRICAO2)
                .append(", eqp.apelido from ").append(TBL_EVENTO_EQUIPE).append(" eeq ")
                .append("left join ").append(TBL_PARALISACAO).append(" par on eeq.paralisacao = par.idParalisacao ")
                .append("inner join ").append(TBL_EQUIPES_TRABALHO).append(" eqp on eeq.equipe = eqp.idEquipe ")
                .append("left join ").append(TBL_APROPRIACAO).append(" apr on eeq.apropriacao = apr.idApropriacao ")
                .append("order by eeq.horaIni, eeq.horaFim ");

        Cursor cursor = super.findByQuery(query.toString());

        return bindList(cursor);
    }

    private List<EventoEquipeVO> preencherDescricaoServico(Cursor cursor, LocalizacaoVO local) {
        List<EventoEquipeVO> eventoEquipes = bindList(cursor);

        if (eventoEquipes != null) {

            for (EventoEquipeVO ee : eventoEquipes) {

                if (COD_PRODUZINDO.equals(ee.getParalisacao().getId())) {

                    //Log.i("SERVICO",ee.getServico().getId()+"");

                    ServicoVO servico = servicoDAO.findServicoById(ee.getServico().getId(), local);

                    if(servico != null) {
                        ee.getParalisacao().setDescricao(servico.getDescricao());
                    }else{
                        servico = servicoDAO.findAllServicosById(ee.getServico().getId());
                        ee.getParalisacao().setDescricao(servico.getDescricao());
                    }
                }
            }
        }
        return eventoEquipes;
    }

    public List<EventoEquipeVO> findApontamentos(LocalizacaoVO local, EquipeTrabalhoVO equipe, PessoalVO pessoa, ServicoVO servico, String data) {
        if (local == null || local.getId() == null || equipe == null || equipe.getId() == null || pessoa == null || pessoa.getId() == null) {
            return new LinkedList<EventoEquipeVO>();
        }

        if (data == null || data.isEmpty()) {
            data = Util.getToday();
        }

        List<ApropriacaoMaoObraVO> apropriacaoMaoObraVOs = apropriacaoMaoObraDAO.findByLocal(local, equipe, pessoa, data);
        List<ParalisacaoMaoObraVO> paralisacaoMaoObraVOs = paralisacaoMaoObraDAO.findByLocal(local, equipe, pessoa, data);

        List<EventoEquipeVO> eventoEquipes = new LinkedList<EventoEquipeVO>();

        amoToEventoEquipe(local, apropriacaoMaoObraVOs, eventoEquipes);
        pmoToEventoEquipe(local, paralisacaoMaoObraVOs, eventoEquipes);

        Collections.sort(eventoEquipes);

        return eventoEquipes;
    }

    public List<EventoEquipeVO> findApontamentos(PessoalVO pessoa, String data) {
        if (pessoa == null || pessoa.getId() == null) {
            return new ArrayList<EventoEquipeVO>();
        }

        if (data == null || data.isEmpty()) {
            data = Util.getToday();
        }

        List<ApropriacaoMaoObraVO> apropriacaoMaoObraVOs = apropriacaoMaoObraDAO.findByPessoa(pessoa, data);
        List<ParalisacaoMaoObraVO> paralisacaoMaoObraVOs = paralisacaoMaoObraDAO.findByPessoa(pessoa, data);

        List<EventoEquipeVO> eventoEquipes = new ArrayList<EventoEquipeVO>();

        amoToEventoEquipe(null, apropriacaoMaoObraVOs, eventoEquipes);
        pmoToEventoEquipe(null, paralisacaoMaoObraVOs, eventoEquipes);

        Collections.sort(eventoEquipes);

        return eventoEquipes;
    }

    private void pmoToEventoEquipe(LocalizacaoVO local, List<ParalisacaoMaoObraVO> paralisacaoMaoObraVOs, List<EventoEquipeVO> eventoEquipes) {
        for (ParalisacaoMaoObraVO pmo : paralisacaoMaoObraVOs) {
            EventoEquipeVO eventoEquipe = new EventoEquipeVO();
            eventoEquipe.setId(pmo.getApropriacao().getId());
            eventoEquipe.setLocalizacao(local);
            eventoEquipe.setParalisacao(pmo.getParalisacao());
            eventoEquipe.setEquipe(pmo.getEquipe());
            eventoEquipe.setHoraIni(pmo.getHoraInicio());
            eventoEquipe.setHoraFim(pmo.getHoraTermino());
            eventoEquipe.setObservacao(pmo.getObservacoes());

            eventoEquipe.setServico(pmo.getServico());

            eventoEquipes.add(eventoEquipe);
        }
    }

    private void amoToEventoEquipe(LocalizacaoVO local, List<ApropriacaoMaoObraVO> apropriacaoMaoObraVOs, List<EventoEquipeVO> eventoEquipes) {
        for (ApropriacaoMaoObraVO amo : apropriacaoMaoObraVOs) {
            EventoEquipeVO eventoEquipe = new EventoEquipeVO();
            eventoEquipe.setId(amo.getApropriacao().getId());
            eventoEquipe.setApropriacao(amo.getApropriacao());
            eventoEquipe.setLocalizacao(local);
            ServicoVO servico = amo.getApropriacaoServico().getServico();
            eventoEquipe.setParalisacao(new ParalisacaoVO(COD_PRODUZINDO, servico.getDescricao()));
            eventoEquipe.setEquipe(amo.getEquipe());
            eventoEquipe.setHoraIni(amo.getHoraInicio());
            eventoEquipe.setHoraFim(amo.getHoraTermino());
            eventoEquipe.setObservacao(amo.getObservacoes());
            eventoEquipe.setServico(servico);

            eventoEquipes.add(eventoEquipe);
        }
    }

    public List<EventoEquipeVO> findByLocalizacaoAndEquipe(LocalizacaoVO local, EquipeTrabalhoVO equipe) {
        if (local == null || local.getId() == null || equipe == null || equipe.getId() == null) {
            return new ArrayList<EventoEquipeVO>();
        }

        StringBuilder query = new StringBuilder();
        query.append("select eeq.*, ifnull(par.descricao,'").append(PRODUZINDO_DESC).append("') ").append(ALIAS_DESCRICAO2)
                .append(" from ").append(TBL_EVENTO_EQUIPE).append(" eeq ")
                .append("left join ").append(TBL_PARALISACAO).append(" par on eeq.paralisacao = par.idParalisacao ")
                .append("left join ").append(TBL_SERVICO).append(" ser on eeq.servico = ser.idServico ")
                .append("where eeq.localizacao = ? and eeq.equipe = ? ")
                .append(" group by eeq.idEventoEquipe, eeq.localizacao, eeq.equipe ")
                .append("order by eeq.horaIni, eeq.horaFim ");

        Cursor cursor = super.findByQuery(query.toString(), concatArgs(local.getId(), equipe.getId()));

        return preencherDescricaoServico(cursor, local);
    }

    public List<EventoEquipeVO> findByLocalizacaoAndEquipe(LocalizacaoVO local, EquipeTrabalhoVO equipe, String data) {
        if (local == null || local.getId() == null || equipe == null || equipe.getId() == null) {
            return new ArrayList<EventoEquipeVO>();
        }

        if (data == null || data.isEmpty()) {
            data = Util.getToday();
        }

        StringBuilder query = new StringBuilder();
        query.append("select eeq.*,")
                .append("ifnull(par.descricao,'").append(PRODUZINDO_DESC).append("') ").append(ALIAS_DESCRICAO2)
                .append(", eqp.apelido from ").append(TBL_EVENTO_EQUIPE).append(" eeq ")
                .append("left join ").append(TBL_PARALISACAO).append(" par on eeq.paralisacao = par.idParalisacao ")
                .append("inner join ").append(TBL_EQUIPES_TRABALHO).append(" eqp on eeq.equipe = eqp.idEquipe ")
                .append("left join ").append(TBL_APROPRIACAO).append(" apr on eeq.apropriacao = apr.idApropriacao ")
                .append("where eeq.localizacao = ? and eeq.equipe = ? and substr(eeq.data,0,11) = ? ")
                .append("order by eeq.horaIni, eeq.horaFim, eqp.apelido ");

        Cursor cursor = super.findByQuery(query.toString(), concatArgs(local.getId(), equipe.getId(), Util.toSimpleDateFormat(data)));

        return preencherDescricaoServico(cursor, local);
    }

    public List<EventoEquipeVO> findDistinctList() {
        StringBuilder query = new StringBuilder();
        query.append("select distinct substr(eeq.data,0,11) 'data', eeq.equipe, eqp.apelido, eeq.apropriacao, ser.idServico, eeq.localizacao from ")
                .append(TBL_EVENTO_EQUIPE).append(" eeq ")
                .append("left join ").append(TBL_PARALISACAO).append(" par on eeq.paralisacao = par.idParalisacao ")
                .append("inner join ").append(TBL_APROPRIACAO).append(" apr on apr.idApropriacao = eeq.apropriacao ")
                .append("inner join ").append(TBL_EQUIPES_TRABALHO).append(" eqp on eeq.equipe = eqp.idEquipe ")
                .append("left join ").append(TBL_SERVICO).append(" ser on eeq.servico = ser.idServico ")
                .append("group by 'data', eeq.equipe, eqp.apelido, eeq.apropriacao ")
                .append("order by date(substr(eeq.data,7,4)||'-'||substr(eeq.data,4,2)||'-'||substr(eeq.data,1,2)), eqp.apelido");

        Cursor cursor = super.findByQuery(query.toString());

        List<EventoEquipeVO> eventoEquipes = bindList(cursor);

        for (EventoEquipeVO eventoEquipeVO : eventoEquipes) {
            ParalisacaoVO paralisacao = eventoEquipeVO.getParalisacao();

            if (paralisacao != null && paralisacao.getId() != null && COD_PRODUZINDO.equals(paralisacao.getId())) {
                eventoEquipeVO.getParalisacao().setDescricao(eventoEquipeVO.getServico().getDescricao());
            }

            LocalizacaoVO localizacao = localizacaoDAO.findLocalizacaoById(eventoEquipeVO.getLocalizacao().getId());
            eventoEquipeVO.setLocalizacao(localizacao);

            //utiliza o VO para listar a descricao na tela
            if (eventoEquipeVO.getApropriacao().getDescricao() == null) {
                eventoEquipeVO.getApropriacao().setDescricao(localizacao.getDescricao());
            }
        }

        return eventoEquipes;
    }

    private ParalisacaoEquipeVO preencherParalisacaoEquipe(EventoEquipeVO eventoEquipe) {
        ParalisacaoEquipeVO paralisacaoEquipe = new ParalisacaoEquipeVO();
        paralisacaoEquipe.setApropriacao(eventoEquipe.getApropriacao());
        paralisacaoEquipe.setEquipe(eventoEquipe.getEquipe());
        paralisacaoEquipe.setParalisacao(eventoEquipe.getParalisacao());
        paralisacaoEquipe.setHoraInicio(eventoEquipe.getHoraIni());
        paralisacaoEquipe.setHoraTermino(eventoEquipe.getHoraFim());
//        paralisacaoEquipe.setObservacoes(eventoEquipe.getObservacao());

        ApropriacaoServicoVO apropriacaoServico = new ApropriacaoServicoVO();
        apropriacaoServico.setApropriacao(eventoEquipe.getApropriacao());
        apropriacaoServico.setServico(eventoEquipe.getServico());
        paralisacaoEquipe.setApropriacaoServico(apropriacaoServico);

        return paralisacaoEquipe;
    }

    private ApropriacaoServicoVO preencherApropriacaoServico(EventoEquipeVO eventoEquipe) {
        ApropriacaoVO apropriacao = eventoEquipe.getApropriacao();
        EquipeTrabalhoVO equipe = eventoEquipe.getEquipe();

        ApropriacaoServicoVO apropriacaoServico = new ApropriacaoServicoVO();
        apropriacaoServico.setApropriacao(apropriacao);
        apropriacaoServico.setServico(eventoEquipe.getServico());
        apropriacaoServico.setEquipe(equipe);
        apropriacaoServico.setHoraIni(eventoEquipe.getHoraIni());
        apropriacaoServico.setHoraFim(eventoEquipe.getHoraFim());
        apropriacaoServico.setObservacoes(eventoEquipe.getObservacao());
        apropriacaoServico.setQuantidadeProduzida(0.0);

        return apropriacaoServico;
    }

    /**
     * Converte dados do periodo horario trabalho em eventos equipe
     *
     * @param periodosTrabalho
     * @param eventoEquipePadrao
     * @param servico
     * @return
     */
    public List<EventoEquipeVO> toEventosEquipe(List<PeriodoHorarioTrabalhoVO> periodosTrabalho, EventoEquipeVO eventoEquipePadrao, ServicoVO servico) {
        List<EventoEquipeVO> eventosEquipe = new ArrayList<EventoEquipeVO>();

        if (periodosTrabalho != null) {
            List<ParalisacaoVO> paralisacoes = paralisacaoDAO.findSimpleList(TipoAplicacao.MAO_DE_OBRA);

            for (PeriodoHorarioTrabalhoVO pht : periodosTrabalho) {
                EventoEquipeVO eventoEquipe = eventoEquipePadrao.clone();
                eventoEquipe.setHoraIni(pht.getHoraInicio());
                eventoEquipe.setHoraFim(pht.getHoraTermino());

                //paralisado
                if (pht.getCodigoParalisacao() != null && !pht.getCodigoParalisacao().isEmpty()) {
                    ParalisacaoVO paralisacao = getParalisacaoByCodigo(paralisacoes, Integer.parseInt(pht.getCodigoParalisacao()));
                    eventoEquipe.setParalisacao(paralisacao);
                } else {//produzindo
                    eventoEquipe.setServico(servico);
                    ParalisacaoVO paralisacao = getParalisacaoByCodigo(paralisacoes, COD_PRODUZINDO);
                    paralisacao.setDescricao(servico != null ? servico.getDescricao() : PRODUZINDO_DESC);
                    eventoEquipe.setParalisacao(paralisacao);
                }

                eventosEquipe.add(eventoEquipe);
            }
        }

        return eventosEquipe;
    }

    /**
     * Busca a paralisacao a partir de uma lista, informando o seu codigo
     *
     * @param paralisacoes
     * @param codigo
     * @return
     */
    private ParalisacaoVO getParalisacaoByCodigo(List<ParalisacaoVO> paralisacoes, Integer codigo) {
        for (ParalisacaoVO p : paralisacoes) {
            if (p.getId().equals(codigo)) {
                return p;
            }
        }

        return new ParalisacaoVO("");
    }

    @Override
    public EventoEquipeVO popularEntidade(Cursor cursor) {
        EventoEquipeVO eventoEquipe = new EventoEquipeVO(getInt(cursor, EVENTO_EQUIPE_COL_ID));
        EquipeTrabalhoVO equipe = new EquipeTrabalhoVO(getInt(cursor, EVENTO_EQUIPE_COL_EQUIPE));
        ApropriacaoVO apropriacao = new ApropriacaoVO(getInt(cursor, EVENTO_EQUIPE_COL_APROPRIA), "");
        ParalisacaoVO paralisacao = new ParalisacaoVO(getInt(cursor, EVENTO_EQUIPE_COL_PARALISA));
        ServicoVO servico = new ServicoVO(getInt(cursor, EVENTO_EQUIPE_COL_SERVICO));
        servico.setDescricao(getString(cursor, ALIAS_DESCRICAO));
        LocalizacaoVO localizacao = new LocalizacaoVO(getInt(cursor, EVENTO_EQUIPE_COL_LOCALIZACAO));

        equipe.setApelido(getString(cursor, EQUIPE_COL_APELIDO));

        Integer indexDesc = getInt(cursor, ALIAS_DESCRICAO2);

        if (indexDesc != null && indexDesc > -1) {
            paralisacao.setDescricao(getString(cursor, ALIAS_DESCRICAO2));
        }

        eventoEquipe.setLocalizacao(localizacao);
        eventoEquipe.setData(getString(cursor, EVENTO_EQUIPE_COL_DATA));
        eventoEquipe.setHoraIni(getString(cursor, EVENTO_EQUIPE_COL_HORA_INI));
        eventoEquipe.setHoraFim(getString(cursor, EVENTO_EQUIPE_COL_HORA_FIM));
        eventoEquipe.setObservacao(getString(cursor, EVENTO_EQUIPE_COL_OBS));
        eventoEquipe.setTipoApontamento(TipoApontamento.findByCodigo(getInt(cursor, EVENTO_EQUIPE_COL_TP_HORARI)));
        eventoEquipe.setApropriacao(apropriacao);
        eventoEquipe.setParalisacao(paralisacao);
        eventoEquipe.setEquipe(equipe);

        eventoEquipe.setServico(servico);

        return eventoEquipe;
    }

    @Override
    public ContentValues bindContentValues(EventoEquipeVO ee) {
        ContentValues contentValues = new ContentValues();
        EquipeTrabalhoVO equipe = ee.getEquipe();
        ServicoVO servico = ee.getServico();
        ApropriacaoVO apropriacao = ee.getApropriacao();
        ParalisacaoVO paralisacao = ee.getParalisacao();
        TipoApontamento tipoApontamento = ee.getTipoApontamento();
        LocalizacaoVO localizacao = ee.getLocalizacao();

        contentValues.put(EVENTO_EQUIPE_COL_ID, ee.getId());
        contentValues.put(EVENTO_EQUIPE_COL_EQUIPE, equipe != null ? equipe.getId() : null);
        contentValues.put(EVENTO_EQUIPE_COL_LOCALIZACAO, localizacao != null ? localizacao.getId() : null);
        contentValues.put(EVENTO_EQUIPE_COL_SERVICO, servico != null ? servico.getId() : null);
        contentValues.put(EVENTO_EQUIPE_COL_APROPRIA, apropriacao != null ? apropriacao.getId() : null);
        contentValues.put(EVENTO_EQUIPE_COL_PARALISA, paralisacao != null ? paralisacao.getId() : null);
        contentValues.put(EVENTO_EQUIPE_COL_TP_HORARI, tipoApontamento != null ? tipoApontamento.getCodigo() : TipoApontamento.MANUAL.getCodigo());
        contentValues.put(EVENTO_EQUIPE_COL_HORA_INI, ee.getHoraIni());
        contentValues.put(EVENTO_EQUIPE_COL_HORA_FIM, ee.getHoraFim());
        contentValues.put(EVENTO_EQUIPE_COL_OBS, ee.getObservacao());
        contentValues.put(EVENTO_EQUIPE_COL_DATA, ee.getData());

        return contentValues;
    }

    @Override
    public boolean isNewEntity(EventoEquipeVO ee) {
        return ee == null || ee.getId() == null;
    }

    @Override
    public String[] getPkColumns() {
        return new String[]{EVENTO_EQUIPE_COL_ID};
    }

    @Override
    public Object[] getPkArgs(EventoEquipeVO ee) {
        return new Object[]{ee.getId()};
    }
}
