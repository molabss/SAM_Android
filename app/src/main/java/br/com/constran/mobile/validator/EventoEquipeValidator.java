package br.com.constran.mobile.validator;

import android.content.Context;
import br.com.constran.mobile.R;
import br.com.constran.mobile.persistence.vo.Intervalo;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.*;
import br.com.constran.mobile.persistence.vo.imp.ParalisacaoVO;
import br.com.constran.mobile.persistence.vo.imp.ServicoVO;
import br.com.constran.mobile.view.util.Error;
import br.com.constran.mobile.view.util.Util;

import java.util.*;

/**
 * Criado em 11/07/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class EventoEquipeValidator {
    private static final Integer COD_PRODUZINDO = 16;

    private Context context;

    public EventoEquipeValidator(Context context) {
        this.context = context;

    }

    public Error validate(List<EventoEquipeVO> eventoEquipeList, EventoEquipeVO eventoEquipe, EquipeTrabalhoVO equipeSelecionada,
                          ParalisacaoVO tipoEventoSelecionado, ServicoVO servicoSelecionado,
                          String horaIniStr, String horaFimStr, boolean servicoObrigatorio) {

        //valida se equipe vazia
        if (equipeSelecionada == null) {
            return new Error(Util.getMessage(context, getStr(R.string.equipe), R.string.ALERT_REQUIRED));
        }

        boolean apontamentoManual = TipoApontamento.MANUAL.equals(eventoEquipe.getTipoApontamento());

        //valida se tipo evento vazio
        if (tipoEventoSelecionado == null && apontamentoManual) {
            return new Error(Util.getMessage(context, getStr(R.string.tipo_evento), R.string.ALERT_REQUIRED));
        }

        if (servicoObrigatorio && servicoSelecionado == null) {
            return new Error(Util.getMessage(context, getStr(R.string.servico), R.string.ALERT_REQUIRED));
        }

        //quando o apontamento for automatico nao valida os dados pois estes vem do servidor
        if (!apontamentoManual) {
            return null;
        }

        Date horaIni = Util.toHourFormat(horaIniStr);
        Date horaFim = Util.toHourFormat(horaFimStr);

        //valida se hora inicio vazia
        if (horaIniStr.isEmpty()) {
            return new Error(Util.getMessage(context, getStr(R.string.hora_inicio), R.string.ALERT_REQUIRED));
        }

        if (!Util.isHoraValida(horaIniStr)) {
            return new Error(Util.getMessage(context, getStr(R.string.hora_inicio), R.string.ALERT_HOUR_INVALID));
        }

        if (!horaFimStr.isEmpty() && !Util.isHoraValida(horaFimStr)) {
            return new Error(Util.getMessage(context, getStr(R.string.hora_termino), R.string.ALERT_HOUR_INVALID));
        }

        if (horaIni != null && horaFim != null && !horaFim.after(horaIni)) {
            return new Error(Util.getMessage(context, getStr(R.string.hora_termino), R.string.ALERT_HOUR_INVALID));
        }

        Error error = dataTerminoInvalida(eventoEquipeList, eventoEquipe, horaIni, horaFim);

        if (error != null) {
            return error;
        }

        if (!periodoEventoOk(eventoEquipeList, eventoEquipe, horaIni, horaFim)) {
            return new Error(getStr(R.string.ALERT_EVENTO_PERIODO_INVALIDO));
        }

        return error;
    }

    public Error validate(List<EventoEquipeVO> eventoEquipeList, EventoEquipeVO eventoEquipe, EquipeTrabalhoVO equipeSelecionada,
                          ParalisacaoVO eventoSelecionado, ServicoVO servicoSelecionado, String horaIniStr, String horaFimStr) {

        boolean apontamentoAuto = TipoApontamento.AUTOMATICO.equals(eventoEquipe.getTipoApontamento());
        boolean servicoObrigatorio = apontamentoAuto || eventoSelecionado != null && COD_PRODUZINDO.equals(eventoSelecionado.getId());

        return validate(eventoEquipeList, eventoEquipe, equipeSelecionada, eventoSelecionado, servicoSelecionado, horaIniStr, horaFimStr, servicoObrigatorio);
    }

    /**
     * Verifica se é possível ajustar os eventos da equipe por individuo
     *
     * @param equipeSelecionada
     * @param eventoEquipes
     * @param eventoAutomaticoNaoSalvo
     * @return
     */
    public Error isValidationAjusteEquipeOk(EquipeTrabalhoVO equipeSelecionada, List<EventoEquipeVO> eventoEquipes, boolean eventoAutomaticoNaoSalvo) {
        if (equipeSelecionada == null) {
            return new Error(Util.getMessage(context, getStr(R.string.equipe), R.string.ALERT_REQUIRED));
        }

        if (eventoEquipes == null || eventoEquipes.isEmpty()) {
            return new Error(getStr(R.string.ALERT_SEM_EVENTOS));
        }

        if (eventoAutomaticoNaoSalvo) {
            return new Error(getStr(R.string.ALERT_EVENTOS_NAO_SALVOS));
        }

        return null;
    }

    public Error dataTerminoInvalida(List<EventoEquipeVO> eventoEquipeList, EventoEquipeVO eventoEquipe, Date dataIni, Date dataFim) {
        boolean dataFimVazia = eventoEquipe.getHoraFim() == null || eventoEquipe.getHoraFim().isEmpty();
        boolean existeDataFimVazia = false;
        Date ultimaHoraIni = null;

        //verifica eventos com data termino nao apontados
        if (!eventoEquipeList.contains(eventoEquipe)) {//inserindo novo
            for (EventoEquipeVO ee : eventoEquipeList) {
                Date dataIniItem = Util.toHourFormat(ee.getHoraIni());
                Date dataFimItem = Util.toHourFormat(ee.getHoraFim());

                if (ultimaHoraIni != null) {
                    ultimaHoraIni = dataIniItem.after(ultimaHoraIni) ? dataIniItem : ultimaHoraIni;
                } else {
                    ultimaHoraIni = dataIniItem;
                }

                if (ee.getHoraFim().isEmpty()) {
                    existeDataFimVazia = true;
                }

                //se existe um apontamento com hora fim vazia, entao nao permite um novo registro
                boolean hasBothEmpty = ee.getHoraFim().isEmpty() && dataFimVazia;

                if (hasBothEmpty || (ee.getHoraFim().isEmpty() && Util.isInInterval(dataIni, dataFim, dataIniItem, dataFimItem) && dataIni.before(dataIniItem))) {
                    return new Error(getStr(R.string.ALERT_EVENTO_SEM_DATA_FIM));
                }

                //se existe um registro X com hora fim vazia e o novo registro for posterior a X entao invalido
                if (existeDataFimVazia && dataIni.after(ultimaHoraIni)) {
                    return new Error(getStr(R.string.ALERT_EVENTO_SEM_DATA_FIM));
                }

                //verifica se a hora inicio esta no intervalo de um apontamento existente qdo data fim vazia
                if (dataFimVazia && Util.isInInterval(dataIni, dataFim, dataIniItem, dataFimItem)) {
                    return new Error(getStr(R.string.ALERT_EVENTO_PERIODO_INVALIDO));
                }

                //verifica se o novo horario de inicio é o mesmo de um jã existente
                if ((!dataIni.after(dataIniItem) && !dataIni.before(dataIniItem)) || Util.isInInterval(dataIni, dataFim, dataIniItem, dataFimItem)) {
                    return new Error(getStr(R.string.ALERT_EVENTO_PERIODO_INVALIDO));
                }

                //verifica se o horario final é vazio e a hora inicio é anterior a um horario ja existente
                if (dataFimVazia && !dataIni.after(dataIniItem) && !dataIni.after(dataFimItem)) {
                    return new Error(getStr(R.string.ALERT_EVENTO_HORA_FIM_INVALIDO));
                }

            }
        } else { //editando registro
            //se ja existir um registro com data termino vazio, nao permite outro
            if (dataFimVazia) {

                for (EventoEquipeVO ee : eventoEquipeList) {
                    Date dataIniAtual = Util.toHourFormat(ee.getHoraIni());

                    if (ultimaHoraIni != null) {
                        ultimaHoraIni = dataIniAtual.after(ultimaHoraIni) ? dataIniAtual : ultimaHoraIni;
                    } else {
                        ultimaHoraIni = dataIniAtual;
                    }

                    if (ee.getHoraFim().isEmpty() && !ee.getHoraIni().equals(eventoEquipe.getHoraIni())) {
                        return new Error(getStr(R.string.ALERT_EVENTO_SEM_DATA_FIM));
                    }
                }
            }
        }

        //se a data termino nao for apontada, verifica se este registro é o último apontado
        //senao informa que não é possivel salvar pq a data termino so pode ser vazia no ultimo registro do dia
        boolean horaIniAntesUltHoraIni = ultimaHoraIni != null ? Util.toHourFormat(eventoEquipe.getHoraIni()).before(ultimaHoraIni) : true;

        if (dataFimVazia && ultimaHoraIni != null && horaIniAntesUltHoraIni) {
            return new Error(getStr(R.string.ALERT_EVENTO_HORA_FIM_INVALIDO));
        }

        return null;
    }

    public boolean periodoEventoOk(List<EventoEquipeVO> eventoEquipeList, EventoEquipeVO eventoEquipe, Date horaIni, Date horaFim) {
        for (EventoEquipeVO ee : eventoEquipeList) {
            Date horaIniItem = Util.toHourFormat(ee.getHoraIni());
            Date horaFimItem = Util.toHourFormat(ee.getHoraFim());

            if (!ee.equals(eventoEquipe) && Util.isInInterval(horaIni, horaFim, horaIniItem, horaFimItem)) {
                return false;
            }

        }

        return true;
    }

    /**
     * Verifica se a apropriacao da mao de obra do integrante é igual ao evento que está sendo modificado para a equipe
     * considerando a hora fim antiga da equipe
     * Nota: Esta verificação só deve ser feita quando apenas a hora término da equipe for alterada,
     * quando o Tipo de Evento (paralisação/produzindo) ou o Serviço for modificado, a mudança deve ser
     * replicada (sobrescrever) o apontamento individual
     *
     * @param eventoEquipe
     * @param amo
     * @return
     */
    public static boolean existeApropriacaoMO(EventoEquipeVO eventoEquipe, ApropriacaoMaoObraVO amo) {
        try {
            return COD_PRODUZINDO.equals(eventoEquipe.getParalisacaoAntiga().getId())
                    && eventoEquipe.getServicoAntigo().getId().equals(amo.getServico().getId())
                    && eventoEquipe.getHoraIni().equals(amo.getHoraInicio())
                    && eventoEquipe.getHoraFimAntiga().equals(amo.getHoraTermino())
                    && !eventoEquipe.getHoraFim().equals(amo.getHoraTermino());
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica se a paralisacao da mao de obra do integrante é igual ao evento que está sendo modificado para a equipe
     * Nota: Esta verificação só deve ser feita quando apenas a hora término da equipe for alterada,
     * quando o Tipo de Evento (paralisação/produzindo) ou o Serviço for modificado, a mudança deve ser
     * replicada (sobrescrever) o apontamento individual
     *
     * @param eventoEquipe
     * @param pmo
     * @return
     */
    public static boolean existeParalisacaoMO(EventoEquipeVO eventoEquipe, ParalisacaoMaoObraVO pmo) {
        try {
            if (isServicoAntigoIgual(eventoEquipe, pmo.getServico()))
                return false;

            return !COD_PRODUZINDO.equals(eventoEquipe.getParalisacao().getId())
                    && eventoEquipe.getParalisacaoAntiga().getId().equals(pmo.getParalisacao().getId())
                    && eventoEquipe.getHoraIni().equals(pmo.getHoraInicio())
                    && eventoEquipe.getHoraFimAntiga().equals(pmo.getHoraTermino());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica se o codigo da paralisacao atual NAO corresponde a produzindo e se o codigo antigo era IGUAL a produzindo
     * Para ser considerado mudança para paralisado, a fim de atualizar os a paralisacao da MO, os seguintes atributos antigos
     * devem ser iguais aos existentes: tipoEvento (paralisacao), servico, hora inicio e hora fim
     *
     * @param eventoEquipe
     * @param amo
     * @return
     */
    public static boolean mudouParaParalisado(EventoEquipeVO eventoEquipe, ApropriacaoMaoObraVO amo) {
        try {
            //so considera mudança para os registros que possuiam o mesmo serviço antes da atualizacao
            if (isServicoAntigoIgual(eventoEquipe, amo.getServico()))
                return false;

            return COD_PRODUZINDO.equals(eventoEquipe.getParalisacaoAntiga().getId())
                    && !COD_PRODUZINDO.equals(eventoEquipe.getParalisacao().getId())
                    && eventoEquipe.getHoraIni().equals(amo.getHoraInicio())
                    && eventoEquipe.getHoraFimAntiga().equals(amo.getHoraTermino());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica se o codigo da paralisacao atual corresponde a produzindo e se o codigo antigo nao era produzindo
     * Para ser considerado mudança para produzindo, a fim de atualizar os a apropriacao da MO, os seguintes atributos antigos
     * devem ser iguais aos existentes: tipoEvento (paralisacao), servico, hora inicio e hora fim
     *
     * @param eventoEquipe
     * @param pmo
     * @return
     */
    public static boolean mudouParaProduzindo(EventoEquipeVO eventoEquipe, ParalisacaoMaoObraVO pmo) {
        try {
            //so considera mudança para os registros que possuiam o mesmo serviço antes da atualizacao
            if (isServicoAntigoIgual(eventoEquipe, pmo.getServico()))
                return false;

            return COD_PRODUZINDO.equals(eventoEquipe.getParalisacao().getId())
                    && !COD_PRODUZINDO.equals(eventoEquipe.getParalisacaoAntiga().getId())
                    && eventoEquipe.getHoraIni().equals(pmo.getHoraInicio())
                    && eventoEquipe.getHoraFimAntiga().equals(pmo.getHoraTermino());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isServicoAntigoIgual(EventoEquipeVO eventoEquipe, ServicoVO servico) {
        return eventoEquipe.getServicoAntigo() == null && servico != null
                || eventoEquipe.getServicoAntigo() != null && servico == null
                || eventoEquipe.getServicoAntigo().getId() == null && servico.getId() != null
                || eventoEquipe.getServicoAntigo().getId() != null && servico.getId() == null
                || !eventoEquipe.getServicoAntigo().getId().equals(servico.getId());
    }

    /**
     * Verifica se existe conflito de horarios dentre os eventos ja existentes e o novo sendo apontado/replicado
     *
     * @param eventosIntegrante
     * @param ee
     * @return
     */
    public static boolean hasConflitoHorario(List<EventoEquipeVO> eventosIntegrante, EventoEquipeVO ee) {
        if (eventosIntegrante != null) {
            for (EventoEquipeVO evento : eventosIntegrante) {
                Date horaIni = Util.toHourFormat(evento.getHoraIni());
                Date horaFim = evento.getHoraFim() != null ? Util.toHourFormat(evento.getHoraFim()) : null;
                Date newHoraIni = Util.toHourFormat(ee.getHoraIni());
                Date newHoraFim = ee.getHoraFim() != null ? Util.toHourFormat(ee.getHoraFim()) : null;

                //verifica se o novo horario de inicio nao conflitua com o horario existente
                if (!newHoraIni.before(horaIni) && (horaFim == null || !newHoraIni.after(horaFim))) {
                    return true;
                }

                //verifica se o novo horario de termino nao conflitua com o horario existente
                if (newHoraFim != null && !newHoraFim.before(horaIni) && (horaFim == null || !newHoraFim.after(horaFim))) {
                    return true;
                }

                //verifica se o horario de inicio nao conflitua com o horario existente
                if (!horaIni.before(newHoraIni) && (newHoraFim == null || !horaIni.after(newHoraFim))) {
                    return true;
                }

                //verifica se o horario de termino nao conflitua com o horario existente
                if (horaFim != null && !horaFim.before(newHoraIni) && (newHoraFim == null || !horaFim.after(newHoraFim))) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Busca os intervalos sem apontamentos realizados para o integrante
     *
     * @param eventosIntegrante
     * @param ee
     * @return
     */
    public static List<Intervalo> getIntervalosDisponiveis(List<EventoEquipeVO> eventosIntegrante, EventoEquipeVO ee) {
        List<Intervalo> intervalos = new ArrayList<Intervalo>();

        if (!hasConflitoHorario(eventosIntegrante, ee)) {
            intervalos.add(new Intervalo(ee.getHoraIni(), ee.getHoraFim()));
        } else {
            intervalos = getIntervalosDisponiveis(eventosIntegrante, ee.getHoraIni(), ee.getHoraFim());
        }

        return intervalos;
    }

    /**
     * Busca os intervalos disponiveis sem repetir resultados e ordenados
     *
     * @param eventosIntegrante
     * @param horaIni
     * @param horaFim
     * @return
     */
    private static List<Intervalo> getIntervalosDisponiveis(List<EventoEquipeVO> eventosIntegrante, String horaIni, String horaFim) {
        List<Intervalo> intervalos = new ArrayList<Intervalo>();
        Date horaIniApontada = Util.toHourFormat(horaIni);
        Date horaFimApontada = Util.toHourFormat(horaFim);

        if (eventosIntegrante != null && !eventosIntegrante.isEmpty()) {
            Set<String> horarioSet = new HashSet<String>();
            horarioSet.add(horaIni);

            if (horaFim != null && !horaFim.isEmpty()) {
                horarioSet.add(horaFim);
            }

            for (EventoEquipeVO evento : eventosIntegrante) {
                if (evento.getHoraIni() != null && !evento.getHoraIni().isEmpty()) {
                    horarioSet.add(evento.getHoraIni());
                }

                if (evento.getHoraFim() != null && !evento.getHoraFim().isEmpty()) {
                    horarioSet.add(evento.getHoraFim());
                }
            }

            List<String> horarios = new ArrayList<String>(horarioSet);
            Collections.sort(horarios);

            preencheIntervalos(horarios, intervalos, horaIniApontada, horaFimApontada);
        }

        return intervalos;
    }

    /**
     * Preenche os intervalos disponiveis na lista
     *
     * @param horarios
     * @param intervalos
     * @param horaIniApontada
     * @param horaFimApontada
     */
    private static void preencheIntervalos(List<String> horarios, List<Intervalo> intervalos, Date horaIniApontada, Date horaFimApontada) {
        String atual = null;
        String prox = null;
        String ultHora = null;

        for (String h : horarios) {
            if (atual == null) {
                atual = h;
            } else if (prox == null) {
                prox = h;

                Date h1 = Util.toHourFormat(atual);
                Date h2 = Util.toHourFormat(prox);

                if (!isIntervaloContido(h1, h2, horaIniApontada, horaFimApontada, false)
                        && isHoraContida(h1, horaIniApontada, horaFimApontada, true)
                        && isHoraContida(h2, horaIniApontada, horaFimApontada, true)) {
                    intervalos.add(new Intervalo(atual, prox));
                }

                atual = prox;
                ultHora = prox != null && !prox.isEmpty() ? prox : ultHora;
                prox = null;
            }
        }

        if (horaFimApontada == null && ultHora != null && !ultHora.isEmpty()) {
            intervalos.add(new Intervalo(ultHora, ""));
        }
    }

    /**
     * Verifica se a hora esta contida no intervalo informado por horaIni e horaFim
     *
     * @param hora
     * @param horaIni
     * @param horaFim
     * @return
     */
    private static boolean isHoraContida(Date hora, Date horaIni, Date horaFim, boolean intervaloFechado) {
        if (intervaloFechado) {
            return !hora.before(horaIni) && (horaFim == null || !hora.after(horaFim));
        }

        return hora.after(horaIni) && (horaFim == null || hora.before(horaFim));
    }

    private static boolean isIntervaloContido(Date hora1, Date hora2, Date horaIni, Date horaFim, boolean intervaloFechado) {
//        if(hora2 == null) {
//            return isHoraContida(hora1, horaIni, horaFim, true);
//        }
//
        if (hora1.equals(horaIni) && (hora2 == horaFim || (horaFim == null && isHoraContida(horaIni, hora1, hora2, false))
                || (hora2 != null && horaFim != null && hora2.equals(horaFim)))) {
            return true;
        }

        if (intervaloFechado) {
            //verifica se a hora 1 (inicio) esta contida no intervalo
            if (!hora1.before(horaIni) && (horaFim == null || !hora1.after(horaFim))) {
                //verifica se a hora 2 (fim) esta contida no intervalo
                if (hora2 != null && !hora2.before(horaIni) && (horaFim == null || !hora2.after(horaFim))) {
                    return true;
                }
            }
        }

        //verifica se a hora 1 (inicio) esta contida no intervalo
        if (hora1.after(horaIni) && (horaFim == null || hora1.before(horaFim))) {
            //verifica se a hora 2 (fim) esta contida no intervalo
            return hora2 != null && hora2.after(horaIni) && (horaFim == null || hora2.before(horaFim));
        }

        return false;
    }

    private String getStr(int id) {
        return context.getResources().getString(id);
    }

}
