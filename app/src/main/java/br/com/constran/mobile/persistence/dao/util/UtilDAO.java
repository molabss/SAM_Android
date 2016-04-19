package br.com.constran.mobile.persistence.dao.util;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import br.com.constran.mobile.R;
import br.com.constran.mobile.model.LogAuditoria;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.dao.DAOFactory;
import br.com.constran.mobile.persistence.dao.LogAuditoriaDAO;
import br.com.constran.mobile.persistence.vo.AbstractVO;
import br.com.constran.mobile.persistence.vo.ManutencaoEquipamentoServicosVO;
import br.com.constran.mobile.persistence.vo.ManutencaoEquipamentoVO;
import br.com.constran.mobile.persistence.vo.aprop.ApropriacaoVO;
import br.com.constran.mobile.persistence.vo.aprop.eqp.ApropriacaoEquipamentoVO;
import br.com.constran.mobile.persistence.vo.aprop.eqp.EquipamentoParteDiariaVO;
import br.com.constran.mobile.persistence.vo.aprop.eqp.EventoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.ApropriacaoMaoObraVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.ApropriacaoServicoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.ParalisacaoEquipeVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.ParalisacaoMaoObraVO;
import br.com.constran.mobile.persistence.vo.aprop.mov.ApropriacaoMovimentacaoVO;
import br.com.constran.mobile.persistence.vo.aprop.mov.EquipamentoMovimentacaoDiariaVO;
import br.com.constran.mobile.persistence.vo.aprop.mov.ViagemVO;
import br.com.constran.mobile.persistence.vo.imp.*;
import br.com.constran.mobile.persistence.vo.imp.json.ExportMobile;
import br.com.constran.mobile.persistence.vo.imp.json.ExportMobileDate;
import br.com.constran.mobile.persistence.vo.rae.RaeVO;
import br.com.constran.mobile.persistence.vo.rae.abs.*;
import br.com.constran.mobile.system.ApplicationInit;
import br.com.constran.mobile.system.SharedPreferencesHelper;
import br.com.constran.mobile.view.util.Eticket;
import br.com.constran.mobile.view.util.Util;

import java.lang.reflect.Array;
import java.util.*;

public class UtilDAO extends AbstractDAO {

    private static final String TOKEN = "#";
    //	private static String[] SCRIPT_DDL = new String[31];
    private static List<String> SCRIPT_DELETE_CARGA = new ArrayList<String>();//sql server
    private static List<String> SCRIPT_DELETE_APONTAMENTOS = new ArrayList<String>();//tabela local
    private static List<String> SCRIPT_DELETE_CONFIGURACOES = new ArrayList<String>();//arquivo json

    private static UtilDAO instance;


    LogAuditoriaDAO logDAO;
    LogAuditoria log;


    public UtilDAO(Context context) {
        super(context);
    }

    public static UtilDAO getInstance(Context context) {
        if (instance == null) {
            instance = new UtilDAO(context);
        }

        return instance;
    }

    public void clearTables(boolean dados, boolean carga, boolean config) {

        createDelete();

        if (dados)
            execute(SCRIPT_DELETE_APONTAMENTOS.toArray(new String[0]));

        if (carga)
            execute(SCRIPT_DELETE_CARGA.toArray(new String[0]));

        if (config)
            execute(SCRIPT_DELETE_CONFIGURACOES.toArray(new String[0]));

    }

    /*
    public void clearTablesByDate(boolean dados, boolean carga, boolean config, String date) {

        createDelete(date);

        if (dados)
            execute(SCRIPT_DELETE_APONTAMENTOS.toArray(new String[0]));

        if (carga)
            execute(SCRIPT_DELETE_CARGA.toArray(new String[0]));

        if (config)
            execute(SCRIPT_DELETE_CONFIGURACOES.toArray(new String[0]));

    }
    */

    private void createDelete() {

        if (!isOpen()) {
            return;
        }

        int i = 0;

        /*** Tabelas de Apontamento ***/
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM " + TBL_EVENTO_EQUIPE);
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM " + TBL_APROPRIACOES_MAO_OBRA);
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM " + TBL_PARALISACOES_MAO_OBRA);
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM " + TBL_PARALISACOES_EQUIPE);
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM " + TBL_APROPRIACAO_SERVICO);
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM " + TBL_PREVISAO_SERVICO);

        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [viagensMovimentacoes]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [eventosEquipamento]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [apropriacoesMovimentacao]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [apropriacoesEquipamento]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [apropriacoes]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [localizacao]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [equipamentosMovimentacaoDiaria]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [equipamentosParteDiaria]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [lubrificacoesDetalhes]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [abastecimentosTemp]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [abastecimentos]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [abastecimentosPosto]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [rae]");

        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [manutencaoEquipamentoServicos]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [manutencaoEquipamentos]");

        i = 0;

        /*** Tabelas de Carga ***/
        SCRIPT_DELETE_CARGA.add("DELETE FROM [manutencaoServicoPorCategoriaEquipamento]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [manutencaoServicos]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [equipamentoCategorias]");

        SCRIPT_DELETE_CARGA.add("DELETE FROM [compartimentos]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [combustiveisPostos]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [combustiveisLubrificantes]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [postos]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [componentes]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [equipamentos]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [lubrificacoesEquipamento]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [preventivasEquipamento]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [justificativasOperador]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [materiais]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [servicos]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [paralisacoes]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [origensDestinos]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [frentesObraAtividade]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [frentesObra]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [usuarios]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [obras]");

        SCRIPT_DELETE_CARGA.add("DELETE FROM " + TBL_PESSOAL);
        SCRIPT_DELETE_CARGA.add("DELETE FROM " + TBL_PERIODOS_HORA_TRABALHO);
        SCRIPT_DELETE_CARGA.add("DELETE FROM " + TBL_HORARIOS_TRABALHO);
        SCRIPT_DELETE_CARGA.add("DELETE FROM " + TBL_INTEGRANTES_EQUIPE);
        SCRIPT_DELETE_CARGA.add("DELETE FROM " + TBL_INTEGRANTES_TEMP);
        SCRIPT_DELETE_CARGA.add("DELETE FROM " + TBL_EQUIPES_TRABALHO);
        SCRIPT_DELETE_CARGA.add("DELETE FROM " + TBL_LOCALIZACAO_EQUIPE);
        SCRIPT_DELETE_CARGA.add("DELETE FROM " + TBL_AUSENCIA);

        i = 0;

        SCRIPT_DELETE_CONFIGURACOES.add("DELETE FROM [configuracoes]");
    }

    /*
    private void createDelete(String date) {

        if (!isOpen()) {
            return;
        }

        int i = 0;

        Tabelas de Apontamento
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM " + TBL_EVENTO_EQUIPE);
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM " + TBL_APROPRIACOES_MAO_OBRA);
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM " + TBL_PARALISACOES_MAO_OBRA);
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM " + TBL_PARALISACOES_EQUIPE);
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM " + TBL_APROPRIACAO_SERVICO);
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM " + TBL_PREVISAO_SERVICO);

        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [viagensMovimentacoes]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [eventosEquipamento]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [apropriacoesMovimentacao]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [apropriacoesEquipamento]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [apropriacoes]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [localizacao]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [equipamentosMovimentacaoDiaria]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [equipamentosParteDiaria]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [lubrificacoesDetalhes]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [abastecimentosTemp]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [abastecimentos]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [abastecimentosPosto]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [rae]");

        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [manutencaoEquipamentoServicos]");
        SCRIPT_DELETE_APONTAMENTOS.add("DELETE FROM [manutencaoEquipamentos]");

        i = 0;

        Tabelas de Carga
        SCRIPT_DELETE_CARGA.add("DELETE FROM [manutencaoServicoPorCategoriaEquipamento]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [manutencaoServicos]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [equipamentoCategorias]");

        SCRIPT_DELETE_CARGA.add("DELETE FROM [compartimentos]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [combustiveisPostos]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [combustiveisLubrificantes]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [postos]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [componentes]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [equipamentos]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [lubrificacoesEquipamento]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [preventivasEquipamento]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [justificativasOperador]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [materiais]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [servicos]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [paralisacoes]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [origensDestinos]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [frentesObraAtividade]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [frentesObra]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [usuarios]");
        SCRIPT_DELETE_CARGA.add("DELETE FROM [obras]");

        SCRIPT_DELETE_CARGA.add("DELETE FROM " + TBL_PESSOAL);
        SCRIPT_DELETE_CARGA.add("DELETE FROM " + TBL_PERIODOS_HORA_TRABALHO);
        SCRIPT_DELETE_CARGA.add("DELETE FROM " + TBL_HORARIOS_TRABALHO);
        SCRIPT_DELETE_CARGA.add("DELETE FROM " + TBL_INTEGRANTES_EQUIPE);
        SCRIPT_DELETE_CARGA.add("DELETE FROM " + TBL_INTEGRANTES_TEMP);
        SCRIPT_DELETE_CARGA.add("DELETE FROM " + TBL_EQUIPES_TRABALHO);
        SCRIPT_DELETE_CARGA.add("DELETE FROM " + TBL_LOCALIZACAO_EQUIPE);
        SCRIPT_DELETE_CARGA.add("DELETE FROM " + TBL_AUSENCIA);

        i = 0;

        SCRIPT_DELETE_CONFIGURACOES.add("DELETE FROM [configuracoes]");
    }
    */

    public Query getQueryMovimentacoesExportByDate(String date) {

        String conditions = null;

        String[] columns = new String[]{

                "APR.[idApropriacao] PK_APROPRIACAO",
                "ORG.[idOrigensDestinos] PK_ORIGEM",
                "DST.[idOrigensDestinos] PK_DESTINO",
                "MAT_MOV.[idMaterial] PK_MATERIAL_MOVIMENTACAO",
                "MAT_VGM.[idMaterial] PK_MATERIAL_VIAGEM",
                "ATV.[frentesObra]||'" + TOKEN + "'||ATV.[atividade] PK_ATIVIDADE",
                "MOV.[apropriacao]||'" + TOKEN + "'||MOV.[equipamento]||'" + TOKEN + "'||MOV.[horaInicio] PK_MOVIMENTACAO",
                "VGM.[apropriacao] ||'" + TOKEN + "'||  VGM.[equipamento] ||'" + TOKEN + "'||  VGM.[horaInicio] ||'" + TOKEN + "'||  VGM.[horaViagem] PK_VIAGEM",

                "FOB.[descricao] FRENTE_OBRA",
                "ATV.[descricao] ATIVIDADE",
                "EQP.[descricao] EQUIPAMENTO",
                "MAT_MOV.[descricao]  MATERIAL_MOVIMENTACAO",
                "MAT_MOV.[idCategoria]  MATERIAL_MOVIMENTACAO_CATEGORIA",
                "MAT_MOV.[descricaoCategoria]  MATERIAL_MOVIMENTACAO_DESC_CATEGORIA",
                "MAT_VGM.[descricao] MATERIAL_VIAGEM",
                "MAT_VGM.[idCategoria] MATERIAL_VIAGEM_CATEGORIA",
                "MAT_VGM.[descricaoCategoria] MATERIAL_VIAGEM_DESC_CATEGORIA",
                "ORG.[descricao] ORIGEM",
                "DST.[descricao] DESTINO",

                "APR.[dataHoraApontamento]",
                "APR.[observacoes]",
                "MOV.[horaTermino]",
                "MOV.[estacaOrigemInicial]",
                "MOV.[estacaOrigemFinal]",
                "MOV.[estacaDestinoInicial]",
                "MOV.[estacaDestinoFinal]",
                "MOV.[horimetroInicial]",
                "MOV.[horimetroFinal]",
                "MOV.[percentualCarga] CARGA_MOV",
                "MOV.[qtdViagens]",
                "EQP.[prefixo]",
                "VGM.[equipamentoCarga]",
                "VGM.[estacaInicial]",
                "VGM.[estacaFinal]",
                "VGM.[dataHoraCadastro]",
                "VGM.[dataHoraAtualizacao]",
                "VGM.[peso]",
//				"VGM.[viravira]",
                "VGM.[horimetro]",
                "VGM.[hodometro]",
                "VGM.[eticket]",
                "VGM.[codSeguranca]",
                "VGM.[nroQRCode]",
                "VGM.[nroFormulario]",
                "VGM.[nroFicha]",
                "VGM.[tipo]",
                "VGM.[apropriar]",
                "VGM.[observacoes]",
                "VGM.[percentualCarga] CARGA_VIAGEM"};


        StringBuilder tableJoins = new StringBuilder("[apropriacoes] APR");

        tableJoins.append(" join [frentesObra] FOB on APR.[frentesObra] = FOB.[idFrentesObra]");
        tableJoins.append(" join [frentesObraAtividade] ATV on FOB.[idFrentesObra] = ATV.[frentesObra] ");
        tableJoins.append(" and APR.[atividade] = ATV.[atividade]  ");
        tableJoins.append(" join [apropriacoesMovimentacao] MOV on MOV.[apropriacao] = APR.[idApropriacao]");
        tableJoins.append(" join [equipamentos] EQP on MOV.[equipamento] = EQP.[idEquipamento]  ");
        tableJoins.append(" left join [materiais] MAT_MOV on MOV.[material] = MAT_MOV.[idMaterial]");
        tableJoins.append(" left join [origensDestinos] ORG on MOV.[origem] = ORG.[idOrigensDestinos]");
        tableJoins.append(" left join [origensDestinos] DST on MOV.[destino] = DST.[idOrigensDestinos] ");
        tableJoins.append(" join [viagensMovimentacoes] VGM on VGM.[horaInicio] = MOV.[horaInicio]");
        tableJoins.append(" and MOV.[equipamento] =  VGM.[equipamento]");
        tableJoins.append(" and MOV.[apropriacao] =  VGM.[apropriacao]");
        tableJoins.append(" left join [materiais] MAT_VGM on MAT_VGM.[idMaterial] = VGM.[material]");

        conditions = "  substr(APR.[dataHoraApontamento],0, 11) =  '" + date + "' ";

        String orderBy = " DATE(substr(APR.[dataHoraApontamento],0,11)) desc, TIME(substr(APR.[dataHoraApontamento],12,16)) desc";
        orderBy += ", TIME(MOV.[horaInicio],12,16) desc, TIME(VGM.[horaViagem]) desc";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoins.toString());
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        return query;

    }

    public Query getQueryMovimentacoesExport() {

        String[] columns = new String[]{

                "APR.[idApropriacao] PK_APROPRIACAO",
                "ORG.[idOrigensDestinos] PK_ORIGEM",
                "DST.[idOrigensDestinos] PK_DESTINO",
                "MAT_MOV.[idMaterial] PK_MATERIAL_MOVIMENTACAO",
                "MAT_VGM.[idMaterial] PK_MATERIAL_VIAGEM",
                "ATV.[frentesObra]||'" + TOKEN + "'||ATV.[atividade] PK_ATIVIDADE",
                "MOV.[apropriacao]||'" + TOKEN + "'||MOV.[equipamento]||'" + TOKEN + "'||MOV.[horaInicio] PK_MOVIMENTACAO",
                "VGM.[apropriacao] ||'" + TOKEN + "'||  VGM.[equipamento] ||'" + TOKEN + "'||  VGM.[horaInicio] ||'" + TOKEN + "'||  VGM.[horaViagem] PK_VIAGEM",

                "FOB.[descricao] FRENTE_OBRA",
                "ATV.[descricao] ATIVIDADE",
                "EQP.[descricao] EQUIPAMENTO",
                "MAT_MOV.[descricao]  MATERIAL_MOVIMENTACAO",
                "MAT_MOV.[idCategoria]  MATERIAL_MOVIMENTACAO_CATEGORIA",
                "MAT_MOV.[descricaoCategoria]  MATERIAL_MOVIMENTACAO_DESC_CATEGORIA",
                "MAT_VGM.[descricao] MATERIAL_VIAGEM",
                "MAT_VGM.[idCategoria] MATERIAL_VIAGEM_CATEGORIA",
                "MAT_VGM.[descricaoCategoria] MATERIAL_VIAGEM_DESC_CATEGORIA",
                "ORG.[descricao] ORIGEM",
                "DST.[descricao] DESTINO",

                "APR.[dataHoraApontamento]",
                "APR.[observacoes]",
                "MOV.[horaTermino]",
                "MOV.[estacaOrigemInicial]",
                "MOV.[estacaOrigemFinal]",
                "MOV.[estacaDestinoInicial]",
                "MOV.[estacaDestinoFinal]",
                "MOV.[horimetroInicial]",
                "MOV.[horimetroFinal]",
                "MOV.[percentualCarga] CARGA_MOV",
                "MOV.[qtdViagens]",
                "EQP.[prefixo]",
                "VGM.[equipamentoCarga]",
                "VGM.[estacaInicial]",
                "VGM.[estacaFinal]",
                "VGM.[dataHoraCadastro]",
                "VGM.[dataHoraAtualizacao]",
                "VGM.[peso]",
//				"VGM.[viravira]",
                "VGM.[horimetro]",
                "VGM.[hodometro]",
                "VGM.[eticket]",
                "VGM.[codSeguranca]",
                "VGM.[nroQRCode]",
                "VGM.[nroFormulario]",
                "VGM.[tipo]",
                "VGM.[apropriar]",
                "VGM.[observacoes]",
                "VGM.[percentualCarga] CARGA_VIAGEM"};


        StringBuilder tableJoins = new StringBuilder("[apropriacoes] APR");

        tableJoins.append(" join [frentesObra] FOB on APR.[frentesObra] = FOB.[idFrentesObra]");
        tableJoins.append(" join [frentesObraAtividade] ATV on FOB.[idFrentesObra] = ATV.[frentesObra] ");
        tableJoins.append(" and APR.[atividade] = ATV.[atividade]  ");
        tableJoins.append(" join [apropriacoesMovimentacao] MOV on MOV.[apropriacao] = APR.[idApropriacao]");
        tableJoins.append(" join [equipamentos] EQP on MOV.[equipamento] = EQP.[idEquipamento]  ");
        tableJoins.append(" left join [materiais] MAT_MOV on MOV.[material] = MAT_MOV.[idMaterial]");
        tableJoins.append(" left join [origensDestinos] ORG on MOV.[origem] = ORG.[idOrigensDestinos]");
        tableJoins.append(" left join [origensDestinos] DST on MOV.[destino] = DST.[idOrigensDestinos] ");
        tableJoins.append(" join [viagensMovimentacoes] VGM on VGM.[horaInicio] = MOV.[horaInicio]");
        tableJoins.append(" and MOV.[equipamento] =  VGM.[equipamento]");
        tableJoins.append(" and MOV.[apropriacao] =  VGM.[apropriacao]");
        tableJoins.append(" left join [materiais] MAT_VGM on MAT_VGM.[idMaterial] = VGM.[material]");

        String orderBy = "DATE(substr(APR.[dataHoraApontamento],0,11)) desc, TIME(substr(APR.[dataHoraApontamento],12,16)) desc";
        orderBy += ", TIME(MOV.[horaInicio],12,16) desc, TIME(VGM.[horaViagem]) desc";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoins.toString());
        query.setOrderBy(orderBy);

        return query;

    }

    public Query getQueryParteDiariaExportByDate(String date) {

        String conditions = null;

        String[] columns = new String[]{

                "APR.[idApropriacao] PK_APROPRIACAO",
                "SRV.[idServico] PK_SERVICO",
                "COM.[idComponente] PK_COMPONENTE",
                "PAR.[idParalisacao] PK_PARALISACAO",
                "ATV.[frentesObra]||'" + TOKEN + "'||ATV.[atividade] PK_ATIVIDADE",
                "APE.[apropriacao]||'" + TOKEN + "'||APE.[equipamento]||'" + TOKEN + "'||APE.[dataHora] PK_PARTEDIARIA",
                "EVN.[apropriacao] ||'" + TOKEN + "'||  EVN.[equipamento] ||'" + TOKEN + "'||  EVN.[dataHora] ||'" + TOKEN + "'||  EVN.[horaInicio] PK_EVENTO",

                "FOB.[descricao] FRENTE_OBRA",
                "ATV.[descricao] ATIVIDADE",
                "EQP.[descricao] EQUIPAMENTO",
                "COM.[descricao] COMPONENTE",
                "SRV.[descricao] SERVICO",
                "PAR.[descricao] PARALISACAO",
                "PAR.[codigo] PARALISACAO_CODIGO",
                "PAR.[requerEstaca] PARALISACAO_REQUER_ESTACA",

                "APR.[dataHoraApontamento]",
                "APR.[observacoes]",
                "APE.[horimetroInicial]",
                "APE.[horimetroFinal]",
                "EQP.[prefixo]",
                "APE.[producao]",
                "APE.[operador1]",
                "APE.[operador2]",
                "APE.[observacoes] PARTE_DIARIA_OBS",

                "EVN.[estaca]",
                "EVN.[dataHoraCadastro]",
                "EVN.[dataHoraAtualizacao]",
                "EVN.[apropriar]",
                "EVN.[observacoes] EVENTO_OBS",
                "EVN.[horaTermino]"};


        StringBuilder tableJoins = new StringBuilder("[apropriacoes] APR");

        tableJoins.append(" join [frentesObra] FOB on APR.[frentesObra] = FOB.[idFrentesObra]");
        tableJoins.append(" join [frentesObraAtividade] ATV on FOB.[idFrentesObra] = ATV.[frentesObra] ");
        tableJoins.append(" and APR.[atividade] = ATV.[atividade]  ");
        tableJoins.append(" join [apropriacoesEquipamento] APE on APE.[apropriacao] = APR.[idApropriacao]");
        tableJoins.append(" join [equipamentos] EQP on APE.[equipamento] = EQP.[idEquipamento]  ");
        tableJoins.append(" join [eventosEquipamento] EVN on EVN.[dataHora] = APE.[dataHora]");
        tableJoins.append(" and APE.[equipamento] =  EVN.[equipamento]");
        tableJoins.append(" and APE.[apropriacao] =  EVN.[apropriacao]");
        tableJoins.append(" left join [servicos] SRV on SRV.[idServico] = EVN.[servico]");
        tableJoins.append(" left join [paralisacoes] PAR on PAR.[idParalisacao] = EVN.[paralisacao]");
        tableJoins.append(" left join [componentes] COM on COM.[idComponente] = EVN.[componente]");

        conditions = "  substr(APR.[dataHoraApontamento],0, 11) =  '" + date + "' ";

        String orderBy = "DATE(substr(APR.[dataHoraApontamento],0,11)) desc, TIME(substr(APR.[dataHoraApontamento],12,16)) desc";
        orderBy += ", TIME(EVN.[horaInicio]) desc";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoins.toString());
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        return query;
    }

    public Query getQueryParteDiariaExport() {

        String[] columns = new String[]{

                "APR.[idApropriacao] PK_APROPRIACAO",
                "SRV.[idServico] PK_SERVICO",
                "COM.[idComponente] PK_COMPONENTE",
                "PAR.[idParalisacao] PK_PARALISACAO",
                "ATV.[frentesObra]||'" + TOKEN + "'||ATV.[atividade] PK_ATIVIDADE",
                "APE.[apropriacao]||'" + TOKEN + "'||APE.[equipamento]||'" + TOKEN + "'||APE.[dataHora] PK_PARTEDIARIA",
                "EVN.[apropriacao] ||'" + TOKEN + "'||  EVN.[equipamento] ||'" + TOKEN + "'||  EVN.[dataHora] ||'" + TOKEN + "'||  EVN.[horaInicio] PK_EVENTO",

                "FOB.[descricao] FRENTE_OBRA",
                "ATV.[descricao] ATIVIDADE",
                "EQP.[descricao] EQUIPAMENTO",
                "COM.[descricao] COMPONENTE",
                "SRV.[descricao] SERVICO",
                "PAR.[descricao] PARALISACAO",
                "PAR.[codigo] PARALISACAO_CODIGO",
                "PAR.[requerEstaca] PARALISACAO_REQUER_ESTACA",

                "APR.[dataHoraApontamento]",
                "APR.[observacoes]",
                "APE.[horimetroInicial]",
                "APE.[horimetroFinal]",
                "EQP.[prefixo]",
                "APE.[producao]",
                "APE.[operador1]",
                "APE.[operador2]",
                "APE.[observacoes] PARTE_DIARIA_OBS",

                "EVN.[estaca]",
                "EVN.[dataHoraCadastro]",
                "EVN.[dataHoraAtualizacao]",
                "EVN.[apropriar]",
                "EVN.[observacoes] EVENTO_OBS",
                "EVN.[horaTermino]"};


        StringBuilder tableJoins = new StringBuilder("[apropriacoes] APR");

        tableJoins.append(" join [frentesObra] FOB on APR.[frentesObra] = FOB.[idFrentesObra]");
        tableJoins.append(" join [frentesObraAtividade] ATV on FOB.[idFrentesObra] = ATV.[frentesObra] ");
        tableJoins.append(" and APR.[atividade] = ATV.[atividade]  ");
        tableJoins.append(" join [apropriacoesEquipamento] APE on APE.[apropriacao] = APR.[idApropriacao]");
        tableJoins.append(" join [equipamentos] EQP on APE.[equipamento] = EQP.[idEquipamento]  ");
        tableJoins.append(" join [eventosEquipamento] EVN on EVN.[dataHora] = APE.[dataHora]");
        tableJoins.append(" and APE.[equipamento] =  EVN.[equipamento]");
        tableJoins.append(" and APE.[apropriacao] =  EVN.[apropriacao]");
        tableJoins.append(" left join [servicos] SRV on SRV.[idServico] = EVN.[servico]");
        tableJoins.append(" left join [paralisacoes] PAR on PAR.[idParalisacao] = EVN.[paralisacao]");
        tableJoins.append(" left join [componentes] COM on COM.[idComponente] = EVN.[componente]");


        String orderBy = "DATE(substr(APR.[dataHoraApontamento],0,11)) desc, TIME(substr(APR.[dataHoraApontamento],12,16)) desc";
        orderBy += ", TIME(EVN.[horaInicio]) desc";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoins.toString());
        query.setOrderBy(orderBy);

        return query;
    }

    public Query getQueryAbastecimentoLubrificacaoExportByDate(String date) {

        String conditions = null;

        String[] columns = new String[]{


                "r.[idRae] PK_RAE",
                "a.[Rae]||'" + TOKEN + "'||a.[equipamento]     ||'" + TOKEN + "'|| a.[combustivel]  ||'" + TOKEN + "'||a.[horaInicio] PK_ABASTECIMENTO",
                "l.[Rae] VALIDA_LUB_DET",
                "l.[Rae]||'" + TOKEN + "'||l.[equipamento]     ||'" + TOKEN + "'|| l.[lubrificante] ||'" + TOKEN + "'||l.[horaInicio] ||'" +
                        TOKEN + "'||l.[compartimento]   ||'" + TOKEN + "'|| l.[categoria] PK_DETALHES_LUB",
                "e.[idEquipamento] PK_EQP",
                "p.idPosto PK_POSTO",
                "cl.idCombustivelLubrificante PK_COMBUSTIVEL",
                "ua.idUsuarioPessoal PK_ABASTECEDOR",
                "uo.idUsuarioPessoal PK_OPERADOR",
                "c.[idCompartimento] ID_COMPARTIMENTO",
                "c.[idCategoria] ID_CATEGORIA",
                "r.[data]  RAE_DATA",
                "ifnull(r.[totalizadorInicial], 0)  RAE_TI",
                "ifnull(r.[totalizadorFinal], 0)  RAE_TF",
                "a.[horaTermino] ABS_FIM",
                "a.[tipo] ABS_TIPO",
                "a.[ccObra] OBRA",
                "a.[frentesObra] FROBRA",
                "a.[atividade] ATV",
                "ifnull(a.[horimetro], 0) ABS_HORIMETRO",
                "ifnull(a.[quantidade], 0) ABS_QTD",
                "ifnull(a.[quilometragem], 0) ABS_KM",
                "a.[observacao] ABS_OBS",
                "e.[prefixo] ABS_EQP",
                "e.[descricao] ABS_EQP_DESC",
                "p.[descricao] POST_DESC",
                "p.[tipo] POSTO_TIPO",
                "cl.[descricao] COMBUST_DESC",
                "cl.[unidadeMedida] COMBUST_UND",
                "cl.[tipo] COMBUST_TIPO",
                "ua.[nome] ABAST_NOME",
                "uo.[nome] OPER_NOME",
                "c.[descricao] CMP_DESC",
                "l.[quantidade]  CMP_QTD",
                "jo.[idJustificativaOperador] JUS_ID",
                "jo.[descricao] JUS_DESC",
                "a.[obsJustificativa] ABS_JUS_OBS",
                "l.[observacoes] CMP_OBS"};


        StringBuilder tableJoins = new StringBuilder(" RAE r ");
        tableJoins.append(" JOIN  abastecimentos a on a.rae = r.idRae ");
        tableJoins.append(" JOIN  equipamentos e on a.equipamento = e.idEquipamento ");
        tableJoins.append(" JOIN  combustiveisPostos cp on r.posto = cp.posto and a.combustivel = cp.combustivel  ");
        tableJoins.append(" JOIN  postos p on p.idPosto = cp.posto        ");
        tableJoins.append(" LEFT JOIN  equipamentos ep on p.equipamento = ep.idEquipamento ");
        tableJoins.append(" JOIN  combustiveisLubrificantes cl on cl.idCombustivelLubrificante = cp.combustivel ");
        tableJoins.append(" JOIN  usuarios ua on ua.codUsuario = a.codAbastecedor ");
        tableJoins.append(" LEFT JOIN usuarios uo on uo.codUsuario = a.codOperador ");
        tableJoins.append(" LEFT JOIN justificativasOperador jo on jo.idJustificativaOperador = a.justificativa");
        tableJoins.append(" LEFT JOIN lubrificacoesDetalhes l on l.[lubrificante] = a.[combustivel] and l.[equipamento] = a.[equipamento] and l.[horaInicio] = a.[horaInicio] and l.[RAE] = a.[RAE]");
        tableJoins.append(" LEFT JOIN [compartimentos] c on c.idCompartimento = l.compartimento and c.idCategoria = l.categoria ");

        conditions = "  substr(r.[data],0,11) =  '" + date + "' ";

        String orderBy = "DATE(substr(R.[data],0,11)) asc, TIME(A.horaInicio) asc";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoins.toString());
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        return query;

    }

    public Query getQueryAbastecimentoLubrificacaoExport() {

        String[] columns = new String[]{


                "r.[idRae] PK_RAE",
                "a.[Rae]||'" + TOKEN + "'||a.[equipamento]     ||'" + TOKEN + "'|| a.[combustivel]  ||'" + TOKEN + "'||a.[horaInicio] PK_ABASTECIMENTO",
                "l.[Rae] VALIDA_LUB_DET",
                "l.[Rae]||'" + TOKEN + "'||l.[equipamento]     ||'" + TOKEN + "'|| l.[lubrificante] ||'" + TOKEN + "'||l.[horaInicio] ||'" +
                        TOKEN + "'||l.[compartimento]   ||'" + TOKEN + "'|| l.[categoria] PK_DETALHES_LUB",
                "e.[idEquipamento] PK_EQP",
                "p.idPosto PK_POSTO",
                "cl.idCombustivelLubrificante PK_COMBUSTIVEL",
                "ua.idUsuarioPessoal PK_ABASTECEDOR",
                "uo.idUsuarioPessoal PK_OPERADOR",
                "c.[idCompartimento] ID_COMPARTIMENTO",
                "c.[idCategoria] ID_CATEGORIA",
                "r.[data]  RAE_DATA",
                "ifnull(r.[totalizadorInicial], 0)  RAE_TI",
                "ifnull(r.[totalizadorFinal], 0)  RAE_TF",
                "a.[horaTermino] ABS_FIM",
                "a.[tipo] ABS_TIPO",
                "a.[ccObra] OBRA",
                "a.[frentesObra] FROBRA",
                "a.[atividade] ATV",
                "ifnull(a.[horimetro], 0) ABS_HORIMETRO",
                "ifnull(a.[quantidade], 0) ABS_QTD",
                "ifnull(a.[quilometragem], 0) ABS_KM",
                "a.[observacao] ABS_OBS",
                "e.[prefixo] ABS_EQP",
                "e.[descricao] ABS_EQP_DESC",
                "p.[descricao] POST_DESC",
                "p.[tipo] POSTO_TIPO",
                "cl.[descricao] COMBUST_DESC",
                "cl.[unidadeMedida] COMBUST_UND",
                "cl.[tipo] COMBUST_TIPO",
                "ua.[nome] ABAST_NOME",
                "uo.[nome] OPER_NOME",
                "c.[descricao] CMP_DESC",
                "l.[quantidade]  CMP_QTD",
                "jo.[idJustificativaOperador] JUS_ID",
                "jo.[descricao] JUS_DESC",
                "a.[obsJustificativa] ABS_JUS_OBS",
                "l.[observacoes] CMP_OBS"};


        StringBuilder tableJoins = new StringBuilder(" RAE r ");
        tableJoins.append(" JOIN  abastecimentos a on a.rae = r.idRae ");
        tableJoins.append(" JOIN  equipamentos e on a.equipamento = e.idEquipamento ");
        tableJoins.append(" JOIN  combustiveisPostos cp on r.posto = cp.posto and a.combustivel = cp.combustivel  ");
        tableJoins.append(" JOIN  postos p on p.idPosto = cp.posto        ");
        tableJoins.append(" LEFT JOIN  equipamentos ep on p.equipamento = ep.idEquipamento ");
        tableJoins.append(" JOIN  combustiveisLubrificantes cl on cl.idCombustivelLubrificante = cp.combustivel ");
        tableJoins.append(" JOIN  usuarios ua on ua.codUsuario = a.codAbastecedor ");
        tableJoins.append(" LEFT JOIN usuarios uo on uo.codUsuario = a.codOperador ");
        tableJoins.append(" LEFT JOIN justificativasOperador jo on jo.idJustificativaOperador = a.justificativa");
        tableJoins.append(" LEFT JOIN lubrificacoesDetalhes l on l.[lubrificante] = a.[combustivel] and l.[equipamento] = a.[equipamento] and l.[horaInicio] = a.[horaInicio] and l.[RAE] = a.[RAE]");
        tableJoins.append(" LEFT JOIN [compartimentos] c on c.idCompartimento = l.compartimento and c.idCategoria = l.categoria ");


        String orderBy = "DATE(substr(R.[data],0,11)) asc, TIME(A.horaInicio) asc";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoins.toString());
        query.setOrderBy(orderBy);

        return query;

    }

    public void excluir(Class<?> pVOClass, String pId) {



        if (pVOClass.equals(EquipamentoParteDiariaVO.class)) {
            execute("delete from [eventosEquipamento] 		 where [equipamento] = " + pId + " and substr([dataHora],0, 11) = '" + Util.getToday() + "'");
            execute("delete from [apropriacoesEquipamento]  where [equipamento] = " + pId + " and substr([dataHora],0, 11) = '" + Util.getToday() + "'");
            execute("delete from [equipamentosParteDiaria]  where [equipamento] = " + pId + " and substr([dataHora],0, 11) = '" + Util.getToday() + "'");
        } else if (pVOClass.equals(EquipamentoMovimentacaoDiariaVO.class)) {
            execute("delete from [viagensMovimentacoes]     where [equipamento] = " + pId + " and substr([dataHoraCadastro],0, 11) = '" + Util.getToday() + "'");
            execute("delete from [apropriacoesMovimentacao] where [equipamento] = " + pId + " and substr([dataHoraCadastro],0, 11) = '" + Util.getToday() + "'");
            execute("delete from [equipamentosMovimentacaoDiaria]  where [equipamento] = " + pId + " and substr([dataHora],0, 11) = '" + Util.getToday() + "'");
        } else if (pVOClass.equals(ManutencaoEquipamentoVO.class)) {
            //execute("delete from [manutencaoEquipamentos] where [idEquipamento] = " + pId +" and substr([dataHora],0, 11) = '" + Util.getToday() + "'");
            execute("delete from [manutencaoEquipamentos] where [idEquipamento] = " + pId +" and [data] = '" + Util.getToday() + "'");
        } else {

            //ESTE TRECHO DE CODIGO TEVE DE SER COLOCADO AQUI DEVIDO A GRANDE FALTA DE
            //COESAO DA ARQUITETURA E PROJETO DO APLICATIVO QUE MISTURA RESPONSABILIDADES
            //E CARECE DE ORIENTACAO A OBJETOS
            log = new LogAuditoria();
            logDAO = DAOFactory.getInstance(ApplicationInit.CONTEXT).getLogAuditoriaDAO();
            log.setDispositivo(SharedPreferencesHelper.Configuracao.getDispositivo());
            log.setModulo("ABASTECIMENTO");
            logDAO.setLogPropriedades(log);
            //---------------------------------------------------------------------------

            String delete = "";

            String[] array = Util.getArrayPK(pId, getStr(R.string.TOKEN));

            if (pVOClass.equals(AbastecimentoVO.class)) {

                delete = "delete from [abastecimentos] where [rae] = " + array[0] + "  and [combustivel] = " + array[1] + " and [equipamento] = " + array[2] + " and [horaInicio] = '" + array[3] + "'";

                logDAO.insereLog("deletando abastecimento - [rae "+ array[0] +"] [combustivel " + array[1] +"] [equipamento " + array[2] + "] [horaInicio " + array[3]+"]");

            } else if (pVOClass.equals(AbastecimentoTempVO.class)) {

                delete = "delete from [abastecimentosTemp] where [equipamento] = " + array[0] + "  and [dataHora] = '" + array[1] + "'";

                logDAO.insereLog("deletando abastecimento temp");

            } else if (pVOClass.equals(AbastecimentoPostoVO.class)) {

                delete = "delete from [abastecimentosPosto] where [posto] = " + array[0] + "  and [combustivel] = " + array[1] + " and [data] = '" + array[2] + "' and [hora] = '" + array[3] + "'";

                logDAO.insereLog("deletando posto - [posto " + array[0] + "] [combustivel "+ array[1] +"]");
            }

            execute(delete);
        }
    }

    public int getCountChildrens(Class<?> pVOClass, String pId) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        if (pVOClass.equals(EquipamentoParteDiariaVO.class)) {
            tableJoin = " [eventosEquipamento] ";
            conditions = " [equipamento] =   " + pId;
            conditions += " and [apropriar] =  'Y'";
            conditions += " and substr([dataHoraCadastro],0, 11) = '" + Util.getToday() + "'";

        } else if (pVOClass.equals(EquipamentoMovimentacaoDiariaVO.class)) {
            tableJoin = " [viagensMovimentacoes] ";
            conditions = " [equipamento] =   " + pId;
            conditions += " and [apropriar] =  'Y'";
            conditions += " and substr([dataHoraCadastro],0, 11) = '" + Util.getToday() + "'";

        } else {

            return 0;
        }

        int total = 0;

        columns = new String[]{" count(*) "};

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        cursor.moveToNext();

        total = cursor.getInt(0);

        return total;
    }

    /*
    public ExportMobile getObjJson(final Query[] querys, final String[] menus) {

        ExportMobile objJson = new ExportMobile();

        List<ApropriacaoVO> listaApropriacoes = new ArrayList<ApropriacaoVO>();
        List<RaeVO> listaRAEs = new ArrayList<RaeVO>();

        for (int i = 0; i < menus.length; i++) {

            Query query = querys[i];

            String tipo = menus[i];

            if (tipo.equals(getStr(R.string.OPTION_MENU_MOV))) {

                Map<String, ApropriacaoVO> mapApropriacao = preencherMovimentacoes(query, tipo);

                listaApropriacoes.addAll(mapApropriacao.values());
            } else if (tipo.equals(getStr(R.string.OPTION_MENU_EQP))) {

                Map<String, ApropriacaoVO> mapApropriacao = preencherEquipamentos(query, tipo);

                listaApropriacoes.addAll(mapApropriacao.values());
            } else if (tipo.equals(getStr(R.string.OPTION_MENU_ABS))) {

                Map<String, RaeVO> mapRAE = preencherAbastecimentos(query);

                listaRAEs.addAll(mapRAE.values());
            }

            //insere dados apropriacao mao-de-obra / servico  na ultima iteracao
            if ((i + 1) >= menus.length) {
                preencherApropriacaoMaosObras(listaApropriacoes);
                preencherApropriacaoServicos(listaApropriacoes);
                preencherParalisacaoEquipe(listaApropriacoes);
                preencherParalisacaoMaoObra(listaApropriacoes);
            }

            objJson.setApropriacoes(listaApropriacoes);
            objJson.setRaes(listaRAEs);
        }

        return objJson;
    }
    */

    /*
    public List<String> getDatesToExport() {
        List<String> datas = new ArrayList<String>();

        String query =
                "SELECT DISTINCT data FROM (SELECT r.data as data FROM RAE r UNION ALL " +
                        " SELECT substr(APR.dataHoraApontamento,0, 11) as data FROM apropriacoes APR ) ORDER BY data asc ";
        Cursor cursor = getCursorRaw(query);

        while (cursor.moveToNext()) {
            int index = 0;

            String d = cursor.getString(index);
            if(d != null) {
                datas.add(d);
            }
        }

        return datas;
    }
    */

    public List<String> getDatesToExport() {
        List<String> datas = new ArrayList<String>();

        /*
        String query =
                "SELECT DISTINCT data FROM (SELECT r.data as data FROM RAE r UNION ALL " +
                        " SELECT substr(APR.dataHoraApontamento,0, 11) as data FROM apropriacoes APR ) ORDER BY data asc ";
        */

        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT data FROM ");
        query.append("( ");
        query.append(   "SELECT R.data as data FROM RAE R ");
        query.append(   "UNION ALL ");
        query.append(   "SELECT substr(APR.dataHoraApontamento,0, 11) as data FROM apropriacoes APR ");
        query.append(   "UNION ALL ");
        //query.append(   "SELECT substr(MAN.dataHora,0, 11) as dataHora FROM manutencaoEquipamentos MAN ");
        query.append(   "SELECT MAN.data as data FROM manutencaoEquipamentos MAN ");
        query.append(") ");
        query.append("ORDER BY data asc");

        Cursor cursor = getCursorRaw(query.toString());

        while (cursor.moveToNext()) {
            int index = 0;

            String d = cursor.getString(index);
            if(d != null) {
                datas.add(d);
            }
        }

        return datas;
    }

    /*
    public ExportMobileDate getObjJsonDate(final Query[] querys, final String[] menus) {

        ExportMobileDate objJson = new ExportMobileDate();

        List<ApropriacaoVO> listaApropriacoes = new ArrayList<ApropriacaoVO>();
        List<RaeVO> listaRAEs = new ArrayList<RaeVO>();

        for (int i = 0; i < menus.length; i++) {

            Query query = querys[i];

            String tipo = menus[i];

            if (tipo.equals(getStr(R.string.OPTION_MENU_MOV))) {

                Map<String, ApropriacaoVO> mapApropriacao = preencherMovimentacoes(query, tipo);

                listaApropriacoes.addAll(mapApropriacao.values());
            } else if (tipo.equals(getStr(R.string.OPTION_MENU_EQP))) {

                Map<String, ApropriacaoVO> mapApropriacao = preencherEquipamentos(query, tipo);

                listaApropriacoes.addAll(mapApropriacao.values());
            } else if (tipo.equals(getStr(R.string.OPTION_MENU_ABS))) {

                Map<String, RaeVO> mapRAE = preencherAbastecimentos(query);

                listaRAEs.addAll(mapRAE.values());
            }

            //insere dados apropriacao mao-de-obra / servico  na ultima iteracao
            if ((i + 1) >= menus.length) {
                preencherApropriacaoMaosObras(listaApropriacoes);
                preencherApropriacaoServicos(listaApropriacoes);
                preencherParalisacaoEquipe(listaApropriacoes);
                preencherParalisacaoMaoObra(listaApropriacoes);
            }

            objJson.setApropriacoes(listaApropriacoes);
            objJson.setRaes(listaRAEs);
        }

        return objJson;
    }
    */


    public ExportMobileDate getObjJsonDate(final Query[] querys, final String[] menus) {

        ExportMobileDate objJson = new ExportMobileDate();

        List<ApropriacaoVO> listaApropriacoes = new ArrayList<ApropriacaoVO>();
        List<RaeVO> listaRAEs = new ArrayList<RaeVO>();
        List<ManutencaoEquipamentoVO> listaManutencaoEquipamento = null;

        for (int i = 0; i < menus.length; i++) {

            Query query = querys[i];

            String tipo = menus[i];

            if (tipo.equals(getStr(R.string.OPTION_MENU_MOV))) {

                Map<String, ApropriacaoVO> mapApropriacao = preencherMovimentacoes(query, tipo);
                listaApropriacoes.addAll(mapApropriacao.values());

            } else if (tipo.equals(getStr(R.string.OPTION_MENU_EQP))) {

                Map<String, ApropriacaoVO> mapApropriacao = preencherEquipamentos(query, tipo);
                listaApropriacoes.addAll(mapApropriacao.values());

            } else if (tipo.equals(getStr(R.string.OPTION_MENU_ABS))) {

                Map<String, RaeVO> mapRAE = preencherAbastecimentos(query);
                listaRAEs.addAll(mapRAE.values());

            }else if (tipo.equals(getStr(R.string.OPTION_MENU_MAN))){

                //DESNECESSÁRIO USAR UMA ESTRUTURA COM HASHMAPS PARA ESTE CASO...
                listaManutencaoEquipamento = preencherManutencoes(query);

            }

            //insere dados apropriacao mao-de-obra / servico  na ultima iteracao
            if ((i + 1) >= menus.length) {
                preencherApropriacaoMaosObras(listaApropriacoes);
                preencherApropriacaoServicos(listaApropriacoes);
                preencherParalisacaoEquipe(listaApropriacoes);
                preencherParalisacaoMaoObra(listaApropriacoes);
            }

            objJson.setManutencaoEquipamentos(listaManutencaoEquipamento);
            objJson.setApropriacoes(listaApropriacoes);
            objJson.setRaes(listaRAEs);
        }

        return objJson;
    }


    private void preencherApropriacaoMaosObras(List<ApropriacaoVO> listaApropriacoes) {
        Map<String, ApropriacaoVO> mapApropriacao = new HashMap<String, ApropriacaoVO>();

        List<ApropriacaoMaoObraVO> maosObras = DAOFactory.getInstance(context).getApropriacaoMaoObraDAO().findAllItems();

        if (maosObras != null && !maosObras.isEmpty()) {
            for (ApropriacaoMaoObraVO maoObra : maosObras) {
                ApropriacaoVO apropriacaoVO = maoObra.getApropriacao();
                apropriacaoVO.setTipoApropriacao(getStr(R.string.OPTION_MENU_SRV));
                apropriacaoVO.setMovimentacoes(null);
                apropriacaoVO.setPartesDiarias(null);
                apropriacaoVO.setParalisacoesEquipe(null);
                apropriacaoVO.setParalisacoesMaoObra(null);
                apropriacaoVO.setServicos(null);

                String chaveMap = apropriacaoVO.getId() + "," + apropriacaoVO.getDataHoraApontamento() + "," + apropriacaoVO.getTipoApropriacao();

                if (mapApropriacao.containsKey(chaveMap)) {
                    apropriacaoVO = mapApropriacao.get(chaveMap);
                } else {
                    mapApropriacao.put(chaveMap, apropriacaoVO);
                }

                if (apropriacaoVO.getMaosObras() == null) {
                    apropriacaoVO.setMaosObras(new ArrayList<ApropriacaoMaoObraVO>());
                }

                maoObra.setApropriacao(null);
                apropriacaoVO.getMaosObras().add(maoObra);
                mapApropriacao.put(chaveMap, apropriacaoVO);
            }

            listaApropriacoes.addAll(mapApropriacao.values());
        }
    }

    private void preencherApropriacaoServicos(List<ApropriacaoVO> listaApropriacoes) {
        Map<String, ApropriacaoVO> servicosMap = new HashMap<String, ApropriacaoVO>();

        List<ApropriacaoServicoVO> servicos = DAOFactory.getInstance(context).getApropriacaoServicoDAO().findAllItems();

        if (servicos != null && !servicos.isEmpty()) {
            for (ApropriacaoServicoVO servico : servicos) {
                ApropriacaoVO apropriacaoVO = servico.getApropriacao();
                apropriacaoVO.setTipoApropriacao(getStr(R.string.OPTION_MENU_SRV));
                apropriacaoVO.setMovimentacoes(null);
                apropriacaoVO.setPartesDiarias(null);
                apropriacaoVO.setParalisacoesEquipe(null);
                apropriacaoVO.setParalisacoesMaoObra(null);
                apropriacaoVO.setMaosObras(null);

                String chaveMap = apropriacaoVO.getId() + "," + apropriacaoVO.getDataHoraApontamento() + "," + apropriacaoVO.getTipoApropriacao();

                if (servicosMap.containsKey(chaveMap)) {
                    apropriacaoVO = servicosMap.get(chaveMap);
                } else {
                    servicosMap.put(chaveMap, apropriacaoVO);
                }

                if (apropriacaoVO.getServicos() == null) {
                    apropriacaoVO.setServicos(new ArrayList<ApropriacaoServicoVO>());
                }

                servico.setApropriacao(null);
                apropriacaoVO.getServicos().add(servico);
                servicosMap.put(chaveMap, apropriacaoVO);
            }

            listaApropriacoes.addAll(servicosMap.values());
        }
    }

    private void preencherParalisacaoEquipe(List<ApropriacaoVO> listaApropriacoes) {
        Map<String, ApropriacaoVO> paralisacaoEquipeMap = new HashMap<String, ApropriacaoVO>();

        List<ParalisacaoEquipeVO> paralisacaoEquipes = DAOFactory.getInstance(context).getParalisacaoEquipeDAO().findAllItems();

        if (paralisacaoEquipes != null && !paralisacaoEquipes.isEmpty()) {
            for (ParalisacaoEquipeVO peq : paralisacaoEquipes) {
                ApropriacaoVO apropriacaoVO = peq.getApropriacao();
                apropriacaoVO.setTipoApropriacao(getStr(R.string.OPTION_MENU_SRV));
                apropriacaoVO.setMovimentacoes(null);
                apropriacaoVO.setPartesDiarias(null);
                apropriacaoVO.setParalisacoesMaoObra(null);
                apropriacaoVO.setServicos(null);
                apropriacaoVO.setMaosObras(null);

                String chaveMap = apropriacaoVO.getId() + "," + apropriacaoVO.getDataHoraApontamento() + "," + apropriacaoVO.getTipoApropriacao();

                if (paralisacaoEquipeMap.containsKey(chaveMap)) {
                    apropriacaoVO = paralisacaoEquipeMap.get(chaveMap);
                } else {
                    paralisacaoEquipeMap.put(chaveMap, apropriacaoVO);
                }

                if (apropriacaoVO.getParalisacoesEquipe() == null) {
                    apropriacaoVO.setParalisacoesEquipe(new ArrayList<ParalisacaoEquipeVO>());
                }

                peq.setApropriacao(null);
                apropriacaoVO.getParalisacoesEquipe().add(peq);

                preencherParalisacao(peq.getParalisacao());

                paralisacaoEquipeMap.put(chaveMap, apropriacaoVO);
            }

            listaApropriacoes.addAll(paralisacaoEquipeMap.values());
        }
    }

    private void preencherParalisacaoMaoObra(List<ApropriacaoVO> listaApropriacoes) {
        Map<String, ApropriacaoVO> paralisacaoMaoObraMap = new HashMap<String, ApropriacaoVO>();

        List<ParalisacaoMaoObraVO> paralisacaoMaoObras = DAOFactory.getInstance(context).getParalisacaoMaoObraDAO().findAllItems();

        if (paralisacaoMaoObras != null && !paralisacaoMaoObras.isEmpty()) {
            for (ParalisacaoMaoObraVO pmo : paralisacaoMaoObras) {
                ApropriacaoVO apropriacaoVO = pmo.getApropriacao();
                apropriacaoVO.setTipoApropriacao(getStr(R.string.OPTION_MENU_SRV));
                apropriacaoVO.setMovimentacoes(null);
                apropriacaoVO.setPartesDiarias(null);
                apropriacaoVO.setParalisacoesEquipe(null);
                apropriacaoVO.setServicos(null);
                apropriacaoVO.setMaosObras(null);

                String chaveMap = apropriacaoVO.getId() + "," + apropriacaoVO.getDataHoraApontamento() + "," + apropriacaoVO.getTipoApropriacao();

                if (paralisacaoMaoObraMap.containsKey(chaveMap)) {
                    apropriacaoVO = paralisacaoMaoObraMap.get(chaveMap);
                } else {
                    paralisacaoMaoObraMap.put(chaveMap, apropriacaoVO);
                }

                if (apropriacaoVO.getParalisacoesMaoObra() == null) {
                    apropriacaoVO.setParalisacoesMaoObra(new ArrayList<ParalisacaoMaoObraVO>());
                }

                pmo.setApropriacao(null);
                preencherParalisacao(pmo.getParalisacao());
                apropriacaoVO.getParalisacoesMaoObra().add(pmo);

                paralisacaoMaoObraMap.put(chaveMap, apropriacaoVO);
            }

            listaApropriacoes.addAll(paralisacaoMaoObraMap.values());
        }
    }

    private void preencherParalisacao(ParalisacaoVO paralisacao) {
        if (paralisacao != null && (paralisacao.getCodigo() == null || paralisacao.getCodigo().isEmpty())) {
            if (paralisacao.getId() != null) {
                paralisacao.setCodigo(paralisacao.getId() < 10 ? "0" + paralisacao.getId() : "" + paralisacao.getId());
            } else {
                paralisacao.setCodigo("16");
            }
        }
    }

    private Map<String, ApropriacaoVO> preencherEquipamentos(Query query, String tipo) {
        Cursor cursor = getCursor(query);

        Map<String, ApropriacaoVO> mapApropriacao = new HashMap<String, ApropriacaoVO>();
        Map<String, ApropriacaoEquipamentoVO> mapParteDiaria = new HashMap<String, ApropriacaoEquipamentoVO>();

        List<EventoVO> listEventos = new ArrayList<EventoVO>();

        while (cursor.moveToNext()) {

            int index = 0;

            Integer pk_apropriacao = cursor.getInt(index++);
            Integer pk_servico = cursor.getInt(index++);
            Integer pk_componente = cursor.getInt(index++);
            Integer pk_paralisacao = cursor.getInt(index++);

            String pk_atividade = cursor.getString(index++);
            String pk_parte_diaria = cursor.getString(index++);
            String pk_evento = cursor.getString(index++);

            ApropriacaoVO apropriacao = new ApropriacaoVO(pk_apropriacao, tipo);

            String descFrenteObra = cursor.getString(index++);
            String descAtividade = cursor.getString(index++);
            String descEquipamento = cursor.getString(index++);
            String descComponente = cursor.getString(index++);
            String descServico = cursor.getString(index++);
            String descParalisacao = cursor.getString(index++);
            String codParalisacao = cursor.getString(index++);
            String requerEstaca = cursor.getString(index++);

            AtividadeVO atividade = new AtividadeVO(pk_atividade, getStr(R.string.TOKEN));

            apropriacao.setDataHoraApontamento(cursor.getString(index++));
            apropriacao.setObservacoes(cursor.getString(index++));

            atividade.getFrenteObra().setDescricao(descFrenteObra);
            atividade.setDescricao(descAtividade);

            apropriacao.setAtividade(atividade);

            ApropriacaoEquipamentoVO parteDiaria = null;

            if (pk_parte_diaria != null) {

                parteDiaria = new ApropriacaoEquipamentoVO(pk_parte_diaria, getStr(R.string.TOKEN));

                parteDiaria.setHorimetroIni(cursor.getString(index++));
                parteDiaria.setHorimetroFim(cursor.getString(index++));
                parteDiaria.getEquipamento().setPrefixo(cursor.getString(index++));
                parteDiaria.getEquipamento().setDescricao(descEquipamento);
                parteDiaria.setProducao(cursor.getString(index++));
                parteDiaria.setOperador1(cursor.getString(index++));
                parteDiaria.setOperador2(cursor.getString(index++));
                parteDiaria.setObservacoes(cursor.getString(index++));

                mapParteDiaria.put(pk_parte_diaria, parteDiaria);
            }

            EventoVO evento = null;

            if (pk_evento != null) {

                evento = new EventoVO(pk_evento, getStr(R.string.TOKEN));

                if (pk_servico != null && pk_servico != 0) {
                    ServicoVO servico = new ServicoVO(pk_servico, descServico, null);
                    evento.setServico(servico);
                }

                if (pk_paralisacao != null && pk_paralisacao != 0) {
                    ParalisacaoVO paralisacao = new ParalisacaoVO(pk_servico, descParalisacao, requerEstaca, codParalisacao);
                    evento.setParalisacao(paralisacao);
                }

                if (pk_componente != null && pk_componente != 0) {
                    ComponenteVO componente = new ComponenteVO(pk_servico, descComponente, null);
                    evento.setComponente(componente);
                }

                evento.setEstaca(cursor.getString(index++));
                evento.setDataHoraCadastro(cursor.getString(index++));
                evento.setDataHoraAtualizacao(cursor.getString(index++));
                evento.setApropriar(cursor.getString(index++));
                evento.setObservacoes(cursor.getString(index++));
                evento.setHoraTermino(cursor.getString(index++));

                listEventos.add(evento);
            }

            mapApropriacao.put(pk_apropriacao.toString(), apropriacao);

        }

        Set<String> aKeys = mapApropriacao.keySet();
        Set<String> mKeys = mapParteDiaria.keySet();

        for (String key : mKeys) {

            StringTokenizer st = new StringTokenizer(key, getStr(R.string.TOKEN));

            Integer idApropriacao = Integer.parseInt(st.nextToken());
            Integer idEquipamento = Integer.parseInt(st.nextToken());
            String dataHora = st.nextToken();

            for (EventoVO evento : listEventos) {

                boolean isEqualApropriacao = evento.getIdApropriacao().intValue() == idApropriacao.intValue();
                boolean isEqualEquipamento = evento.getIdEquipamento().intValue() == idEquipamento.intValue();
                boolean isEqualDataHora = evento.getDataHora().equals(dataHora);

                if (isEqualApropriacao && isEqualEquipamento && isEqualDataHora) {
                    evento.setStrId(null);
                    mapParteDiaria.get(key).getEventos().add(evento);
                }
            }
        }

        Collection<ApropriacaoEquipamentoVO> listParteDiaria = (Collection<ApropriacaoEquipamentoVO>) mapParteDiaria.values();

        for (String idApropriacao : aKeys) {

            for (ApropriacaoEquipamentoVO parteDiaria : listParteDiaria) {

                boolean isEqualApropriacao = parteDiaria.getIdApropriacao().toString().equals(idApropriacao);

                if (isEqualApropriacao) {
                    parteDiaria.setStrId(null);
                    mapApropriacao.get(idApropriacao).getPartesDiarias().add(parteDiaria);
                }
            }
        }
        return mapApropriacao;
    }

    private Map<String, RaeVO> preencherAbastecimentos(Query query) {
        Cursor cursor = getCursor(query);

        Map<String, RaeVO> mapRAE = new HashMap<String, RaeVO>();
        Map<String, AbastecimentoVO> mapAbastecimento = new HashMap<String, AbastecimentoVO>();

        List<LubrificacaoDetalheVO> listLubrificacoes = new ArrayList<LubrificacaoDetalheVO>();

        while (cursor.moveToNext()) {

            int index = 0;

            Integer pk_rae = cursor.getInt(index++);

            String pk_abastecimento = cursor.getString(index++);

            String valida_lub_detalhe = cursor.getString(index++);

            String pk_lub_detalhe = cursor.getString(index++);

            Integer pk_equipamento = cursor.getInt(index++);
            Integer pk_posto = cursor.getInt(index++);
            Integer pk_combustivel = cursor.getInt(index++);
            Integer id_abastecedor = cursor.getInt(index++);
            Integer id_operador = cursor.getInt(index++);
            Integer id_compartimento = cursor.getInt(index++);
            Integer id_categoria = cursor.getInt(index++);

            String data = cursor.getString(index++);
            String totalIni = cursor.getString(index++);
            String totalFim = cursor.getString(index++);
            String horaTermino = cursor.getString(index++);
            String tipo_abs = cursor.getString(index++);
            Integer ccObra = cursor.getInt(index++);
            Integer frenteObra = cursor.getInt(index++);
            Integer atividade = cursor.getInt(index++);
            String horimetro = cursor.getString(index++);
            String quantidade = cursor.getString(index++);
            String quilometragem = cursor.getString(index++);
            String observacao = cursor.getString(index++);
            String eqp_prefixo = cursor.getString(index++);
            String eqp_desc = cursor.getString(index++);
            String posto_desc = cursor.getString(index++);
            String posto_tipo = cursor.getString(index++);
            String comb_desc = cursor.getString(index++);
            String comb_medida = cursor.getString(index++);
            String comb_tipo = cursor.getString(index++);
            String abs_nome = cursor.getString(index++);
            String opr_nome = cursor.getString(index++);
            String cprt_desc = cursor.getString(index++);
            String ld_quantidade = cursor.getString(index++);
            Integer pk_jus = cursor.getInt(index++);
            String jus_desc = cursor.getString(index++);
            String jus_abs_obs = cursor.getString(index++);
            String ld_observacao = cursor.getString(index++);

            RaeVO rae = new RaeVO(pk_rae, data);

            PostoVO posto = new PostoVO(pk_posto, posto_desc, posto_tipo);

            rae.setPosto(posto);

            rae.setTotalizadorInicial(totalIni);
            rae.setTotalizadorFinal(totalFim);

            CombustivelLubrificanteVO combustivel = new CombustivelLubrificanteVO(pk_combustivel, comb_desc, comb_medida, comb_tipo);
            EquipamentoVO equipamento = new EquipamentoVO(pk_equipamento, eqp_prefixo, eqp_desc, null);

            UsuarioVO abastecedor = new UsuarioVO(abs_nome);
            abastecedor.setIdUsuarioPessoal(id_abastecedor);

            UsuarioVO operador = new UsuarioVO(opr_nome);
            operador.setIdUsuarioPessoal(id_operador);

            if (pk_abastecimento != null) {

                AbastecimentoVO abs = new AbastecimentoVO(pk_abastecimento, TOKEN);

                abs.setAbastecedor(abastecedor);
                abs.setCombustivelLubrificante(combustivel);
                abs.setEquipamento(equipamento);
                abs.setOperador(operador);
                abs.setHoraTermino(horaTermino);
                abs.setHorimetro(horimetro);
                abs.setIdRae(rae.getId());
                abs.setObservacao(observacao);
                abs.setTipo(tipo_abs);
                abs.setIdObra(ccObra);
                abs.setAtividade(new AtividadeVO());
                abs.getAtividade().setIdAtividade(atividade);
                abs.getAtividade().setFrenteObra(new FrenteObraVO());
                abs.getAtividade().getFrenteObra().setId(frenteObra);
                abs.setQuilometragem(quilometragem);
                abs.setQtd(quantidade);
                abs.setCombustivelLubrificante(combustivel);
                abs.setJustificativa(new JustificativaOperadorVO(pk_jus, jus_desc));
                abs.setObservacaoJustificativa(jus_abs_obs);

                mapAbastecimento.put(pk_abastecimento, abs);
            }

            LubrificacaoDetalheVO lub_detalhe = null;

            if (valida_lub_detalhe != null) {

                lub_detalhe = new LubrificacaoDetalheVO(pk_lub_detalhe, getStr(R.string.TOKEN));

                lub_detalhe.setCompartimento(new CompartimentoVO(id_compartimento, id_categoria));
                lub_detalhe.getCompartimento().setDescricao(cprt_desc);

                lub_detalhe.setObservacao(ld_observacao);
                lub_detalhe.setQtd(ld_quantidade);

                listLubrificacoes.add(lub_detalhe);
            }

            mapRAE.put(pk_rae.toString(), rae);

        }

        Set<String> rKeys = mapRAE.keySet();
        Set<String> aKeys = mapAbastecimento.keySet();

        for (String key : aKeys) {

            StringTokenizer st = new StringTokenizer(key, getStr(R.string.TOKEN));

            Integer idRAE = Integer.parseInt(st.nextToken());
            Integer idEquipamento = Integer.parseInt(st.nextToken());
            Integer idCombustivel = Integer.parseInt(st.nextToken());
            String hora = st.nextToken();

            for (LubrificacaoDetalheVO lubrificacao : listLubrificacoes) {

                boolean isEqualRae = lubrificacao.getIdRae().intValue() == idRAE.intValue();
                boolean isEqualEquipamento = lubrificacao.getIdEquipamento().intValue() == idEquipamento.intValue();
                boolean isEqualCombustivel = lubrificacao.getIdCombustivelLubrificante().intValue() == idCombustivel.intValue();
                boolean isEqualHora = lubrificacao.getHoraInicio().equals(hora);

                if (isEqualRae && isEqualEquipamento && isEqualCombustivel && isEqualHora) {
                    lubrificacao.setStrId(null);
                    mapAbastecimento.get(key).getLubrificacaoDetalhes().add(lubrificacao);
                }
            }
        }

        Collection<AbastecimentoVO> listAbastecimentos = (Collection<AbastecimentoVO>) mapAbastecimento.values();

        for (String idRae : rKeys) {

            for (AbastecimentoVO abastecimento : listAbastecimentos) {

                boolean isEqualRae = abastecimento.getIdRae().toString().equals(idRae);

                if (isEqualRae) {
                    abastecimento.setStrId(null);
                    mapRAE.get(idRae).getAbastecimentos().add(abastecimento);
                }
            }
        }
        return mapRAE;
    }

    private Map<String, ApropriacaoVO> preencherMovimentacoes(Query query, String tipo) {
        Cursor cursor = getCursor(query);

        Map<String, ApropriacaoVO> mapApropriacao = new HashMap<String, ApropriacaoVO>();
        Map<String, ApropriacaoMovimentacaoVO> mapMovimentacoes = new HashMap<String, ApropriacaoMovimentacaoVO>();
        List<ViagemVO> listViagens = new ArrayList<ViagemVO>();

        while (cursor.moveToNext()) {

            int index = 0;

            Integer pk_apropriacao = cursor.getInt(index++);
            Integer pk_origem = cursor.getInt(index++);
            Integer pk_destino = cursor.getInt(index++);
            Integer pk_material_movimentacao = cursor.getInt(index++);
            Integer pk_material_viagem = cursor.getInt(index++);

            String pk_atividade = cursor.getString(index++);
            String pk_movimentacao = cursor.getString(index++);
            String pk_viagem = cursor.getString(index++);

            ApropriacaoVO apropriacao = new ApropriacaoVO(pk_apropriacao, tipo);

            String descFrenteObra = cursor.getString(index++);
            String descAtividade = cursor.getString(index++);
            String descEquipamento = cursor.getString(index++);
            String descMaterialMovimentacao = cursor.getString(index++);
            String idCategoriaMaterialMovimentacao = cursor.getString(index++);
            String descCategoriaMaterialMovimentacao = cursor.getString(index++);
            String descMaterialViagem = cursor.getString(index++);
            String idCategoriaMaterialViagem = cursor.getString(index++);
            String descCategoriaMaterialViagem = cursor.getString(index++);
            String descOrigem = cursor.getString(index++);
            String descDestino = cursor.getString(index++);

            AtividadeVO atividade = new AtividadeVO(pk_atividade, getStr(R.string.TOKEN));

            apropriacao.setDataHoraApontamento(cursor.getString(index++));
            apropriacao.setObservacoes(cursor.getString(index++));

            atividade.getFrenteObra().setDescricao(descFrenteObra);
            atividade.setDescricao(descAtividade);

            apropriacao.setAtividade(atividade);

            ApropriacaoMovimentacaoVO movimentacao = null;

            if (pk_movimentacao != null) {

                movimentacao = new ApropriacaoMovimentacaoVO(pk_movimentacao, getStr(R.string.TOKEN));

                if (pk_material_movimentacao != null && pk_material_movimentacao != 0) {
                    MaterialVO materialMovimentacao = new MaterialVO(pk_material_movimentacao);
                    movimentacao.setMaterial(materialMovimentacao);
                    movimentacao.getMaterial().setDescricao(descMaterialMovimentacao);
                    movimentacao.getMaterial().setCategoria(new CategoriaVO(Integer.valueOf(idCategoriaMaterialMovimentacao), descCategoriaMaterialMovimentacao));
                }
                if (pk_origem != null && pk_origem != 0) {
                    OrigemDestinoVO origem = new OrigemDestinoVO(pk_origem);
                    movimentacao.setOrigem(origem);
                    movimentacao.getOrigem().setDescricao(descOrigem);
                }
                if (pk_destino != null && pk_destino != 0) {
                    OrigemDestinoVO destino = new OrigemDestinoVO(pk_destino);
                    movimentacao.setDestino(destino);
                    movimentacao.getDestino().setDescricao(descDestino);
                }

                movimentacao.setHoraTermino(cursor.getString(index++));
                movimentacao.setEstacaOrigemInicial(cursor.getString(index++));
                movimentacao.setEstacaOrigemFinal(cursor.getString(index++));
                movimentacao.setEstacaDestinoInicial(cursor.getString(index++));
                movimentacao.setEstacaDestinoFinal(cursor.getString(index++));
                movimentacao.setHorimetroIni(cursor.getString(index++));
                movimentacao.setHorimetroFim(cursor.getString(index++));
                movimentacao.setPrcCarga(cursor.getString(index++));
                movimentacao.setQtdViagens(cursor.getString(index++));
                movimentacao.getEquipamento().setPrefixo(cursor.getString(index++));
                movimentacao.getEquipamento().setDescricao(descEquipamento);

                mapMovimentacoes.put(pk_movimentacao, movimentacao);
            }

            ViagemVO viagem = null;

            if (pk_viagem != null) {

                viagem = new ViagemVO(pk_viagem, getStr(R.string.TOKEN));

                if (pk_material_viagem != null && pk_material_viagem != 0) {
                    MaterialVO materialViagem = new MaterialVO(pk_material_viagem);
                    viagem.setMaterial(materialViagem);
                    viagem.getMaterial().setDescricao(descMaterialViagem);
                    viagem.getMaterial().setCategoria(new CategoriaVO(Integer.valueOf(idCategoriaMaterialViagem), descCategoriaMaterialViagem));

                }

                viagem.setIdEquipamentoCarga(cursor.getInt(index++));
                viagem.setEstacaIni(cursor.getString(index++));
                viagem.setEstacaFim(cursor.getString(index++));
                viagem.setDataHoraCadastro(cursor.getString(index++));
                viagem.setDataHoraAtualizacao(cursor.getString(index++));
                viagem.setPeso(cursor.getString(index++));
//						viagem.setViravira(cursor.getString(index++));
                viagem.setHorimetro(cursor.getString(index++));
                viagem.setHodometro(cursor.getString(index++));
                viagem.setEticket(cursor.getString(index++));

                viagem.setCodSeguranca(cursor.getInt(index++));

                viagem.setNroQRCode(cursor.getInt(index++));

                viagem.setNroFormulario(cursor.getInt(index++));

                viagem.setNroFicha(cursor.getString(index++));

                viagem.setTipo(cursor.getString(index++));
                viagem.setApropriar(cursor.getString(index++));
                viagem.setObservacoes(cursor.getString(index++));
                viagem.setPrcCarga(cursor.getString(index++));

                listViagens.add(viagem);
            }

            mapApropriacao.put(pk_apropriacao.toString(), apropriacao);
        }

        Set<String> aKeys = mapApropriacao.keySet();
        Set<String> mKeys = mapMovimentacoes.keySet();

        for (String key : mKeys) {

            StringTokenizer st = new StringTokenizer(key, getStr(R.string.TOKEN));

            Integer idApropriacao = Integer.parseInt(st.nextToken());
            Integer idEquipamento = Integer.parseInt(st.nextToken());
            String horaInicio = st.nextToken();

            for (ViagemVO viagem : listViagens) {

                boolean isEqualApropriacao = viagem.getIdApropriacao().intValue() == idApropriacao.intValue();
                boolean isEqualEquipamento = viagem.getIdEquipamento().intValue() == idEquipamento.intValue();
                boolean isEqualHoraInicio = viagem.getHoraInicio().equals(horaInicio);

                if (isEqualApropriacao && isEqualEquipamento && isEqualHoraInicio) {
                    viagem.setStrId(null);
                    mapMovimentacoes.get(key).getViagens().add(viagem);
                }
            }
        }

        Collection<ApropriacaoMovimentacaoVO> listMovimentacoes = (Collection<ApropriacaoMovimentacaoVO>) mapMovimentacoes.values();


        for (String idApropriacao : aKeys) {

            for (ApropriacaoMovimentacaoVO movimentacao : listMovimentacoes) {

                boolean isEqualApropriacao = movimentacao.getIdApropriacao().toString().equals(idApropriacao);

                if (isEqualApropriacao) {
                    movimentacao.setStrId(null);
                    mapApropriacao.get(idApropriacao).getMovimentacoes().add(movimentacao);
                }
            }
        }
        return mapApropriacao;
    }

    public String getExist(AbstractVO pVO) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        if (pVO instanceof ViagemVO) {
            tableJoin = " [viagensMovimentacoes] ";
            conditions = " [apropriacao] =   " + ((ViagemVO) pVO).getIdApropriacao();
            conditions += " and [equipamento] =   " + ((ViagemVO) pVO).getIdEquipamento();
            conditions += " and [horaInicio]  =  '" + ((ViagemVO) pVO).getHoraInicio() + "'";
            conditions += " and [horaViagem]  =  '" + ((ViagemVO) pVO).getHoraViagem() + "'";

        } else if (pVO instanceof EventoVO) {
            tableJoin = " [eventosEquipamento] ";
            conditions = " [apropriacao] =   " + ((EventoVO) pVO).getIdApropriacao();
            conditions += " and [equipamento] =   " + ((EventoVO) pVO).getIdEquipamento();
            conditions += " and [dataHora]  =  '" + ((EventoVO) pVO).getDataHora() + "'";
            conditions += " and [horaInicio]  =  '" + ((EventoVO) pVO).getHoraInicio() + "'";
        }

        columns = new String[]{"apropriar"};

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        String value = getStr(R.string.EMPTY);

        if (cursor.moveToNext())
            value = cursor.getString(0);


        return value;
    }

    public Eticket getETicketVO(String[] pParams) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{"substr(a.[dataHoraApontamento], 0, 11)"};

        tableJoin = "[apropriacoesMovimentacao] m";
        tableJoin += " join [apropriacoes] a on a.[idApropriacao] = m.[apropriacao]";
        tableJoin += " join [equipamentos] e on m.[equipamento] =  e.[idEquipamento] ";

        conditions = " m.[apropriacao] = " + pParams[0];
        conditions += " and m.[equipamento] = " + pParams[1];
        conditions += " and m.[horaInicio]  = '" + pParams[2] + "'";

        String[] dados = new String[columns.length];

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        while (cursor.moveToNext()) {

            for (int i = 0; i < dados.length; i++) {
                dados[i] = cursor.getString(i);
            }
        }

        return new Eticket(dados[0]);
    }

    public boolean validaEstacas(ViagemVO vo) {

        boolean retorno = false;

        Query query = new Query(true);

        StringBuilder conditions = new StringBuilder(getStr(R.string.EMPTY));

        conditions.append("m.[apropriacao] = ?  and m.[equipamento] = ?  and m.[horaInicio] = ? ");

        conditions.append(" and ");
        conditions.append(" cast(? as integer)  between cast(od.[estacaInicial] as integer) and  cast(od.[estacaFinal] as integer)");
        /*conditions.append(" and ");
        conditions.append(" cast(? as integer)  between cast(od.[estacaInicial] as integer) and  cast(od.[estacaFinal] as integer)");
		 */
        query.setConditions(conditions.toString());
        query.setConditionsArgs(new String[]{String.valueOf(vo.getIdApropriacao()), String.valueOf(vo.getIdEquipamento()), vo.getHoraInicio(),
                vo.getEstacaIni()/*, vo.getEstacaFim()*/});

        query.setColumns(new String[]{"od.[idOrigensDestinos]"});

        String table = "[apropriacoesMovimentacao] m ";

        if (vo.getTipo().equalsIgnoreCase(getStr(R.string.ORIGEM))) {
            table += "join [origensDestinos] od on m.[origem] = od.[idOrigensDestinos]";
        } else if (vo.getTipo().equalsIgnoreCase(getStr(R.string.DESTINO))) {
            table += "join [origensDestinos] od on m.[destino] = od.[idOrigensDestinos]";
        } else if (vo.getTipo().equalsIgnoreCase(getStr(R.string.ORIGEM_DESTINO))) {
            table += "join [origensDestinos] od on m.[origem] = od.[idOrigensDestinos]";
            table += "join [origensDestinos] od2 on m.[destino] = od2.[idOrigensDestinos]";
        }

        query.setTableJoin(table);

        Cursor cursor = getCursor(query);

        while (cursor.moveToNext()) {
            retorno = true;
        }


        return retorno;
    }

    public boolean existsApontamentos() {

        return (existsApropriacoes() || existsRAEs() || existsManutencoes());
    }

    public boolean existsApropriacoes() {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        tableJoin = " [apropriacoes] ";

        int total = 0;

        columns = new String[]{" count(*) "};

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        cursor.moveToNext();

        total = cursor.getInt(0);

        return total > 0;

    }


    public boolean existsRAEs() {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        tableJoin = " [RAE] ";

        int total = 0;

        columns = new String[]{" count(*) "};

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        cursor.moveToNext();

        total = cursor.getInt(0);

        return total > 0;

    }


    public boolean existsManutencoes() {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        tableJoin = " [manutencaoEquipamentos] ";

        int total = 0;

        columns = new String[]{" count(*) "};

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        cursor.moveToNext();

        total = cursor.getInt(0);

        return total > 0;

    }

    public Query getQueryManutencaoExportByDate(String date){

        Query query = new Query(true);
        String[] columns = new String[]{"me.[idEquipamento]","eqp.[descricao]","me.[data]","me.[horaInicio]","me.[horaTermino]","me.[horimetro]","me.[hodometro]","me.[observacao]"};
        String orderBy = null;
        String conditions = " me.[data] = "+ " '"+date+"' ";
        StringBuilder tableJoins = new StringBuilder(" [manutencaoEquipamentos] me");
        tableJoins.append(" inner join [equipamentos] eqp ");
        tableJoins.append(" on me.[idEquipamento] = eqp.[idEquipamento] ");

        query.setColumns(columns);
        query.setTableJoin(tableJoins.toString());
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        return query;
    }



    private List<ManutencaoEquipamentoVO> preencherManutencoes(Query query) {

        Cursor cursor = getCursor(query);

        List<ManutencaoEquipamentoVO> listaDeManutencoes = new ArrayList<ManutencaoEquipamentoVO>();

        List<ManutencaoEquipamentoServicosVO> listaDeServicos = null;

        ManutencaoEquipamentoVO manutencaoEquipamentoVO = null;
        ManutencaoEquipamentoServicosVO manutencaoServicoVO = null;

        while (cursor.moveToNext()) {

            manutencaoEquipamentoVO = new ManutencaoEquipamentoVO();
            manutencaoEquipamentoVO.setIdEquipamento(cursor.getInt(cursor.getColumnIndex("idEquipamento")));
            manutencaoEquipamentoVO.setDescricaoEquipamento(cursor.getString(cursor.getColumnIndex("descricao")));
            manutencaoEquipamentoVO.setData(cursor.getString(cursor.getColumnIndex("data")));
            manutencaoEquipamentoVO.setHoraInicio(cursor.getString(cursor.getColumnIndex("horaInicio")));
            manutencaoEquipamentoVO.setHoraTermino(cursor.getString(cursor.getColumnIndex("horaTermino")));
            manutencaoEquipamentoVO.setHorimetro(cursor.getString(cursor.getColumnIndex("horimetro")));
            manutencaoEquipamentoVO.setHodometro(cursor.getString(cursor.getColumnIndex("hodometro")));
            manutencaoEquipamentoVO.setObservacao(cursor.getString(cursor.getColumnIndex("observacao")));

            Cursor cursorServicos = getManutencaoServicosExecutados(manutencaoEquipamentoVO.getIdEquipamento(),manutencaoEquipamentoVO.getData());
            listaDeServicos = new ArrayList<ManutencaoEquipamentoServicosVO>();


            //A J U S T A R !!!!
            while(cursorServicos.moveToNext()){

                manutencaoServicoVO = new ManutencaoEquipamentoServicosVO();
                manutencaoServicoVO.setIdEquipamento(cursorServicos.getInt(cursorServicos.getColumnIndex("idEquipamento")));
                manutencaoServicoVO.setDescricaoEquipamento(cursorServicos.getString(cursorServicos.getColumnIndex("descricao_equipamento")));
                manutencaoServicoVO.setData(cursorServicos.getString(cursorServicos.getColumnIndex("data")));
                manutencaoServicoVO.setIdServicoCategoriaEquipamento(cursorServicos.getInt(cursorServicos.getColumnIndex("idServicoCategoriaEquipamento")));
                manutencaoServicoVO.setDescricaoServico(cursorServicos.getString(cursorServicos.getColumnIndex("descricao")));
                manutencaoServicoVO.setHoraInicio(cursorServicos.getString(cursorServicos.getColumnIndex("horaInicio")));
                manutencaoServicoVO.setHoraTermino(cursorServicos.getString(cursorServicos.getColumnIndex("horaTermino")));
                manutencaoServicoVO.setObservacao(cursorServicos.getString(cursorServicos.getColumnIndex("observacao")));
                listaDeServicos.add(manutencaoServicoVO);
            }
            manutencaoEquipamentoVO.setServicos(listaDeServicos);
            listaDeManutencoes.add(manutencaoEquipamentoVO);
        }
        return listaDeManutencoes;
    }

    private Cursor getManutencaoServicosExecutados(Integer idEquipamento, String data){
        StringBuilder query = new StringBuilder();
        query.append("select mes.idEquipamento, eqp.descricao as descricao_equipamento, ");
        query.append("mes.data, msce.idServicoCategoriaEquipamento, ms.descricao, mes.horaInicio, mes.horaTermino, mes.observacao ");
        query.append("from manutencaoEquipamentoServicos mes ");
        query.append("inner join equipamentos eqp ");
        query.append("on mes.idEquipamento = eqp.idEquipamento ");
        query.append("inner join manutencaoServicoPorCategoriaEquipamento msce ");
        query.append("on mes.idServicoCategoriaEquipamento = msce.idServicoCategoriaEquipamento ");
        query.append("inner join equipamentoCategorias ec ");
        query.append("on msce.idCategoriaEquipamento = ec.idCategoria ");
        query.append("inner join manutencaoServicos ms ");
        query.append("on msce.idManutencaoServico = ms.idManutencaoServico ");
        query.append("where mes.idEquipamento = ? and mes.data = ?");
        String[] params =  new String[]{String.valueOf(idEquipamento),data};
        Cursor cursor = getCursorRawParams(query.toString(),params);
        return cursor;
    }



}
