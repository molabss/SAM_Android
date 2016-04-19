package br.com.constran.mobile.persistence.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import br.com.constran.mobile.system.AppDirectory;
import br.com.constran.mobile.view.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Criado em 17/04/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class AbstractDatabaseDAO {

    //46 - Mateus Vitali (criado o campo ccObra na tabela Usuario)
    //47 - Mateus Vitali (criado o campo qrcode na tabela Equipamento)
    //48 - Mateus Vitali (criado os campos codSeguranca, nroQRCode, nroFormulario na tabela viagensMovimentacoes)
    //49 - Mateus Vitali (criado a tabela logEnvioInformacoes para armanezar as informações/dias ja enviados)
    //50 - Mateus Vitali (criado o campo qrCode para a tabela de usuarios (usado no abatecimento))
    //51 - Mateus Vitali (criado o campo nroFicha para a tabela viagensMovimentacoes (usado na ficha de 3 vias))

    //53 - Moises Santana (criada tabelas manutencaoEquipamentos, equipamentoCategorias, manutencaoServicos, manutencaoServicoPorCategoriaEquipamento, manutencaoEquipamentoServicos)
    //para novo modulo de Manutencoes

    //54 - Moises Santana (adicionado campo usaPlanServico na tabela de Obras)
    //55 - Moises Santana (adicionada nova tabela logAuditoria)
    //56 - Moises Santana (adicionada campo idDispositivo em logAuditoria)
    //57 - Moises Santana (campos da tabela logAutidoria podem ser null, frenteObra e atividade)
    //58 - Moises Santana (removido frenteObra e atividade e idUsuario de logAuditoria)

    private static final int DATABASE_VERSION = 58;
    public static final String DATABASE_NAME = "constrandb.db";
    private static final String ABSTRACT_DATABASE_TAG = "DataBaseManager";

    /**
     * Nome das Tabelas
     */
    //Tabelas
    protected static final String TBL_OBRA = " [obras] ";
    protected static final String TBL_CONFIGURACAO = " [configuracoes] ";
    protected static final String TBL_LOCALIZACAO = " [localizacao] ";
    protected static final String TBL_LOCALIZACAO_EQUIPE = " [localizacaoEquipe] ";

    protected static final String TBL_APROPRIACAO = " [apropriacoes] ";
    protected static final String TBL_APROPRIACAO_EQUIPAMENTO = " [apropriacoesEquipamento] ";
    protected static final String TBL_APROPRIACAO_MOVIMENTACAO = " [apropriacoesMovimentacao] ";
    protected static final String TBL_EQUIPAMENTO_PARTE_DIARIA = " [equipamentosParteDiaria] ";
    protected static final String TBL_EVENTOS_EQUIPAMENTO = " [eventosEquipamento] ";
    protected static final String TBL_EQUIPAMENTO_MOVIM_DIA = " [equipamentosMovimentacaoDiaria] ";
    protected static final String TBL_VIAGEM_MOVIMENTACAO = " [viagensMovimentacoes] ";

    protected static final String TBL_ATIVIDADE = " [frentesObraAtividade] ";
    protected static final String TBL_COMPONENTE = " [componentes] ";
    protected static final String TBL_EQUIPAMENTO = " [equipamentos] ";
    protected static final String TBL_FRENTE_OBRA = " [frentesObra]  ";
    protected static final String TBL_JUSTIFICATIVA_OPERADOR = " [justificativasOperador]  ";
    protected static final String TBL_LUBRIFICACAO_EQUIP = " [lubrificacoesEquipamento]  ";
    protected static final String TBL_MATERIAL = " [materiais] ";
    protected static final String TBL_ORIGEM_DESTINO = " [origensDestinos] ";
    protected static final String TBL_PARALISACAO = " [paralisacoes] ";
    protected static final String TBL_PREVENTIVA_EQUIP = " [preventivasEquipamento] ";
    protected static final String TBL_SERVICO = " [servicos] ";
    protected static final String TBL_PREVISAO_SERVICO = " previsaoServico ";
    protected static final String TBL_USUARIO = " [usuarios]  ";

    protected static final String TBL_RAE = " [rae]  ";
    protected static final String TBL_ABASTECIMENTO = " [abastecimentos] ";
    protected static final String TBL_ABASTECIM_POSTO = " [abastecimentosPosto]  ";
    protected static final String TBL_ABASTECIM_TEMP = " [abastecimentosTemp]  ";
    protected static final String TBL_COMBUSTIVEL_LUBRIF = " [combustiveisLubrificantes]  ";
    protected static final String TBL_COMBUSTIVEL_POSTO = " [combustiveisPostos]  ";
    protected static final String TBL_COMPARTIMENTO = " [compartimentos]  ";
    protected static final String TBL_LUBRIFICANTE_DET = " [lubrificacoesDetalhes]  ";
    protected static final String TBL_POSTO = " [postos]  ";

    protected static final String TBL_PESSOAL = " [pessoal] ";
    protected static final String TBL_CONTROLE_FREQUENCIA = " [controleFrequencia] ";
    protected static final String TBL_PERIODOS_HORA_TRABALHO = " [periodosHorariosTrabalho] ";
    protected static final String TBL_HORARIOS_TRABALHO = " [horariosTrabalho] ";
    protected static final String TBL_EQUIPES_TRABALHO = " [equipesTrabalho] ";
    protected static final String TBL_EQUIPAMENTOS_EQUIPE = " [equipamentosEquipe] ";
    protected static final String TBL_INTEGRANTES_TEMP = " [integrantesTemp] ";
    protected static final String TBL_INTEGRANTES_EQUIPE = " [integrantesEquipe] ";
    protected static final String TBL_APROPRIACAO_SERVICO = " [apropriacaoServico] ";
    protected static final String TBL_PARALISACOES_EQUIPE = " [paralisacoesEquipe] ";
    protected static final String TBL_ATIVIDADES = " [atividades] ";
    protected static final String TBL_ATIVIDADES_SERVICOS = " [atividadesServicos] ";
    protected static final String TBL_APROPRIACOES_MAO_OBRA = " [apropriacoesMaoObra] ";
    protected static final String TBL_PARALISACOES_MAO_OBRA = " [paralisacoesMaoObra] ";

    protected static final String TBL_INDICES_PLUVIOMETRICO = " [indicesPluviometrico] ";

    protected static final String TBL_EVENTO_EQUIPE = " [eventoEquipe] ";
    protected static final String TBL_AUSENCIA = " [ausencia] ";

    protected static final String TBL_LOG_ENVIO_INFORMACOES = " [logEnvioInformacoes] ";

    protected static final String TBL_MANUTENCAO_EQUIPAMENTOS = " [manutencaoEquipamentos] ";
    protected static final String TBL_MANUTENCAO_EQUIPAMENTO_SERVICOS = " [manutencaoEquipamentoServicos] ";
    protected static final String TBL_MANUTENCAO_SERVICOS = " [manutencaoServicos] ";
    protected static final String TBL_EQUIPAMENTO_CATEGORIAS = " [equipamentoCategorias] ";
    protected static final String TBL_MANUTENCAO_SERVICO_POR_CATEGORIA_EQUIPAMENTO = " [manutencaoServicoPorCategoriaEquipamento] ";

    protected static final String TBL_LOG_AUDITORIA = " [logAuditoria] ";

    /**
     * Alias comuns a todas as queries
     */
    protected static final String ALIAS_ID_MATERIAL = "idMaterial";
    protected static final String ALIAS_EQUIPAMENTO = "equipamento";
    protected static final String ALIAS_HORA_VIAGEM = "horaViagem";
    protected static final String ALIAS_MATERIAL = "material";
    protected static final String ALIAS_ESTACA_INI = "estacaInicial";
    protected static final String ALIAS_ESTACA_FIM = "estacaFinal";
    protected static final String ALIAS_EDIT_ETICKET = "editEticket";
    protected static final String ALIAS_PERC = "percentual";
    protected static final String ALIAS_APROPRIAR = "apropriar";
    protected static final String ALIAS_OBS = "observacoes";
    protected static final String ALIAS_PESO = "peso";
    protected static final String ALIAS_TIPO = "tipo";
    protected static final String ALIAS_EQUIP_CARGA = "equipamentoCarga";
    protected static final String ALIAS_DESCRICAO = "descricao";
    protected static final String ALIAS_DESCRICAO2 = "descricao2";
    protected static final String ALIAS_DESCRICAO3 = "descricao3";
    protected static final String ALIAS_DESCRICAO4 = "descricao4";
    protected static final String ALIAS_DESCRICAO5 = "descricao5";
    protected static final String ALIAS_DESCRICAO6 = "descricao6";
    protected static final String ALIAS_SERVICO = "servico";
    protected static final String ALIAS_PARALISACAO = "paralisacao";
    protected static final String ALIAS_COMPONENTE = "componente";
    protected static final String ALIAS_HORA_INI = "horaInicio";
    protected static final String ALIAS_HORA_FIM = "horaTermino";
    protected static final String ALIAS_ESTACA = "estaca";
    protected static final String ALIAS_ID_PARALISACAO = "idParalisacao";
    protected static final String ALIAS_REQUER_ESTACA = "requerEstaca";
    protected static final String ALIAS_PREFIXO = "prefixo";
    protected static final String ALIAS_ID_USUARIO = "idUsuario";
    protected static final String ALIAS_ID_USUARIO_PES = "idUsuarioPessoal";
    protected static final String ALIAS_NOME = "nome";
    protected static final String ALIAS_COD_USUARIO = "codUsuario";
    protected static final String ALIAS_COD_OPERADOR = "codOperador";
    protected static final String ALIAS_DATA_HORA = "dataHora";
    protected static final String ALIAS_QTE_MEDIDA = "qteMedida";

    protected class DataBaseOpenHelper extends SQLiteOpenHelper {

        DataBaseOpenHelper(Context context) {
            super(context,AppDirectory.PATH_DATABASE+"/"+DATABASE_NAME,null,DATABASE_VERSION);
        }

        /**
         * Autor: Rafael Takashima
         * Executado sempre que uma nova versao do banco for gerada @DATABASE_VERSION
         */
        public void onCreate(SQLiteDatabase database) {

            Log.i("DATABASE", "BANCO DE DADOS SENDO CRIADO....");

            try {
                for (String sql : generateDLL()) {
                    Log.i("DATABASE ",sql+"\n");
                    database.execSQL(sql);

                }

            } catch (Exception e) {
                Log.e(ABSTRACT_DATABASE_TAG, e.getMessage());
            }
        }

        /**
         * Autor: Rafael Takashima
         * Executado sempre que uma nova versao do banco for gerada @DATABASE_VERSION
         * Recria as tabelas
         */
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

            Log.i("DATABASE", "BANCO DE DADOS SOFRENDO UPGRADE...");

            try {
                for (String sql : dropTables()) {
                    Log.i("DATABASE ",sql+"\n");
                    database.execSQL(sql);
                }

            } catch (Exception e) {
                Log.e(ABSTRACT_DATABASE_TAG, e.getMessage());
            }

            onCreate(database);
        }

        private List<String> generateDLL() {
            List<String> ddlScript = new ArrayList<String>();

            ddlScript.add(createTableObras());
            ddlScript.add(createTableUsuarios());
            ddlScript.add(createTableFrentesObra());
            ddlScript.add(createTableConfiguracoes());
            ddlScript.add(createTableMateriais());
            ddlScript.add(createTableComponentes());
            ddlScript.add(createTableServicos());
            ddlScript.add(createTableParalisacoes());
            ddlScript.add(createTableEquipamentos());
            ddlScript.add(createTableLubricoesEquipamento());
            ddlScript.add(createTablePreventivasEquipamento());
            ddlScript.add(createTableFrentesObraAtividade());
            ddlScript.add(createTableApropriacoes());
            ddlScript.add(createTableOrigensDestinos());
            ddlScript.add(createTableApropriacoesMovimentacao());
            ddlScript.add(createTableViagensMovimentacoes());
            ddlScript.add(createTableEquipamentosMovimentacaoDiaria());
            ddlScript.add(createTableLocalizacao());
            ddlScript.add(createTableLocalizacaoEquipe());
            ddlScript.add(createTableEquipamentoParteDiaria());
            ddlScript.add(createTableApropriacoesEquipamento());
            ddlScript.add(createTableEventosEquipamento());
            ddlScript.add(createTableCombustiveisLubrificantes());
            ddlScript.add(createTablePostos());
            ddlScript.add(createTableCombustiveisPostos());
            ddlScript.add(createTableCompartimentos());
            ddlScript.add(createTableRAE());
            ddlScript.add(createTableAbastecimentos());
            ddlScript.add(createTableAbastecimentosTemp());
            ddlScript.add(createTableAbastecimenosPosto());
            ddlScript.add(createTableLubrificacoesDetalhes());
            ddlScript.add(createTableJustificavasOperador());

            //DDLs de apropriação serviço/Mão-de-obra
            ddlScript.add(createTablePessoal());
            ddlScript.add(createTableControleFrequencia());
            ddlScript.add(createTablePeriodosHorariosTrabalho());
            ddlScript.add(createTableHorariosTrabalho());
            ddlScript.add(createTableEquipesTrabalho());
            ddlScript.add(createTableEquipamentosEquipe());
            ddlScript.add(createTableIntegrantesTemp());
            ddlScript.add(createTableIntegrantesEquipe());
            ddlScript.add(createTableApropriacaoServico());
            ddlScript.add(createTableParalisacoesEquipe());
            ddlScript.add(createTableAtividades());
            ddlScript.add(createTableAtividadesServicos());
            ddlScript.add(createTableApropriacoesMaoObra());
            ddlScript.add(createTableParalisacoesMaoObra());
            ddlScript.add(createTableEventoEquipe());
            ddlScript.add(createTableAusencia());

            ddlScript.add(createTablePrevisaoServico());

            //DDLs de Indice Pluviometrico
            ddlScript.add(createTableIndicesPluviometrico());

            //DDLs de tabelas de LOG
            ddlScript.add(createTableLogEnvioInformacoes());

            //DDLs de Tabela de Manutencao
            ddlScript.add(createTableEquipamentoCategorias());
            ddlScript.add(createTableManutencaoEquipamentos());
            ddlScript.add(createTableManutencaoServicos());
            ddlScript.add(createTableManutencaoEquipamentoServicos());
            ddlScript.add(createTableManutencaoServicoPorCategoriaEquipamento());

            //DDLs de Tabela de Log Auditoria
            ddlScript.add(createTableLog());

            //indexes
            ddlScript.add(createIndexes());


            return ddlScript;
        }

        private List<String> dropTables() {
            final String DROP_TABLE = "DROP TABLE IF EXISTS ";
            List<String> script = new ArrayList<String>();

            script.add(DROP_TABLE + TBL_OBRA);
            script.add(DROP_TABLE + TBL_CONFIGURACAO);
            script.add(DROP_TABLE + TBL_LOCALIZACAO);
            script.add(DROP_TABLE + TBL_APROPRIACAO);
            script.add(DROP_TABLE + TBL_APROPRIACAO_EQUIPAMENTO);
            script.add(DROP_TABLE + TBL_APROPRIACAO_MOVIMENTACAO);
            script.add(DROP_TABLE + TBL_EQUIPAMENTO_PARTE_DIARIA);
            script.add(DROP_TABLE + TBL_EVENTOS_EQUIPAMENTO);
            script.add(DROP_TABLE + TBL_EQUIPAMENTO_MOVIM_DIA);
            script.add(DROP_TABLE + TBL_VIAGEM_MOVIMENTACAO);
            script.add(DROP_TABLE + TBL_ATIVIDADE);
            script.add(DROP_TABLE + TBL_COMPONENTE);
            script.add(DROP_TABLE + TBL_EQUIPAMENTO);
            script.add(DROP_TABLE + TBL_FRENTE_OBRA);
            script.add(DROP_TABLE + TBL_JUSTIFICATIVA_OPERADOR);
            script.add(DROP_TABLE + TBL_LUBRIFICACAO_EQUIP);
            script.add(DROP_TABLE + TBL_MATERIAL);
            script.add(DROP_TABLE + TBL_ORIGEM_DESTINO);
            script.add(DROP_TABLE + TBL_PARALISACAO);
            script.add(DROP_TABLE + TBL_PREVENTIVA_EQUIP);
            script.add(DROP_TABLE + TBL_PREVISAO_SERVICO);
            script.add(DROP_TABLE + TBL_SERVICO);
            script.add(DROP_TABLE + TBL_USUARIO);
            script.add(DROP_TABLE + TBL_RAE);
            script.add(DROP_TABLE + TBL_ABASTECIMENTO);
            script.add(DROP_TABLE + TBL_ABASTECIM_POSTO);
            script.add(DROP_TABLE + TBL_ABASTECIM_TEMP);
            script.add(DROP_TABLE + TBL_COMBUSTIVEL_LUBRIF);
            script.add(DROP_TABLE + TBL_COMBUSTIVEL_POSTO);
            script.add(DROP_TABLE + TBL_COMPARTIMENTO);
            script.add(DROP_TABLE + TBL_LUBRIFICANTE_DET);
            script.add(DROP_TABLE + TBL_POSTO);
            script.add(DROP_TABLE + TBL_AUSENCIA);
            script.add(DROP_TABLE + TBL_PESSOAL);
            script.add(DROP_TABLE + TBL_CONTROLE_FREQUENCIA);
            script.add(DROP_TABLE + TBL_LOCALIZACAO_EQUIPE);
            script.add(DROP_TABLE + TBL_PERIODOS_HORA_TRABALHO);
            script.add(DROP_TABLE + TBL_HORARIOS_TRABALHO);
            script.add(DROP_TABLE + TBL_EVENTO_EQUIPE);
            script.add(DROP_TABLE + TBL_EQUIPES_TRABALHO);
            script.add(DROP_TABLE + TBL_EQUIPAMENTOS_EQUIPE);
            script.add(DROP_TABLE + TBL_INTEGRANTES_TEMP);
            script.add(DROP_TABLE + TBL_INTEGRANTES_EQUIPE);
            script.add(DROP_TABLE + TBL_APROPRIACAO_SERVICO);
            script.add(DROP_TABLE + TBL_PARALISACOES_EQUIPE);
            script.add(DROP_TABLE + TBL_ATIVIDADES);
            script.add(DROP_TABLE + TBL_ATIVIDADES_SERVICOS);
            script.add(DROP_TABLE + TBL_APROPRIACOES_MAO_OBRA);
            script.add(DROP_TABLE + TBL_PARALISACOES_MAO_OBRA);
            script.add(DROP_TABLE + TBL_INDICES_PLUVIOMETRICO);
            script.add(DROP_TABLE + TBL_LOG_ENVIO_INFORMACOES);

            script.add(DROP_TABLE + TBL_MANUTENCAO_EQUIPAMENTO_SERVICOS);
            script.add(DROP_TABLE + TBL_MANUTENCAO_SERVICO_POR_CATEGORIA_EQUIPAMENTO);
            script.add(DROP_TABLE + TBL_MANUTENCAO_EQUIPAMENTOS);

            script.add(DROP_TABLE + TBL_LOG_AUDITORIA);

            return script;
        }

        private String createTableObras() {
            return " CREATE TABLE IF NOT EXISTS obras "
                    + "   ( "
                    + "      idObra               INTEGER PRIMARY KEY, "
                    + "      descricao            TEXT NOT NULL, "
                    + "      exibirHorimetro      TEXT, "
                    + "      horimetroObrigatorio TEXT, "
                    + "      usaQRCode            TEXT, "
                    + "      usaOrigemDestino     TEXT, "
                    + "      usaPlanServico       TEXT, "
                    + "      usaQRCodePessoal     TEXT "
                    + "   ); ";
        }

        private String createTableUsuarios() {
            return " CREATE TABLE IF NOT EXISTS usuarios "
                    + "   ( "
                    + "      codUsuario       INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "      idUsuario        INTEGER NULL, "
                    + "      idUsuarioPessoal INTEGER NULL, "
                    + "      grupo            TEXT NULL, "
                    + "      funcao           TEXT NULL, "
                    + "      nome             TEXT NOT NULL, "
                    + "      senha            TEXT NULL, "
                    + "      qrcode           TEXT NULL, "
                    + "      ccObra           TEXT NULL "
                    + "   ); ";
        }

        private String createTableIndicesPluviometrico() {
            return " CREATE TABLE IF NOT EXISTS indicesPluviometrico "
                    + "   ( "
                    + "      id            INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "      data          TEXT NULL, "
                    + "      estacaInicial TEXT NULL, "
                    + "      estacaFinal   TEXT NULL, "
                    + "      pluviometro   TEXT NULL, "
                    + "      volumeChuva   INTEGER NULL "
                    + "   ); ";
        }

        private String createTableFrentesObra() {
            return " CREATE TABLE IF NOT EXISTS frentesObra "
                    + "   ( "
                    + "      idFrentesObra INTEGER PRIMARY KEY, "
                    + "      obra          INTEGER NOT NULL, "
                    + "      descricao     TEXT NOT NULL "
                    + "   ); ";
        }

        private String createTableConfiguracoes() {
            return " CREATE TABLE IF NOT EXISTS configuracoes "
                    + "   ( "
                    + "      obra             TEXT NOT NULL, "
                    + "      obra2            TEXT NULL, "
                    + "      codUsuario       INTEGER NULL, "
                    + "      idUsuario        INTEGER NULL, "
                    + "      idUsuarioPessoal INTEGER NULL, "
                    + "      dataHora         TEXT NOT NULL, "
                    + "      dispositivo      TEXT NOT NULL, "
                    + "      servidor         TEXT NOT NULL, "
                    + "      portaweb         TEXT NOT NULL, "
                    + "      portaftp         TEXT NOT NULL, "
                    + "      eticket          TEXT NOT NULL, "
                    + "      atual            TEXT NULL, "
                    + "      posto            INTEGER NULL, "
                    + "      referencia       TEXT NOT NULL, "
                    + "      tolerancia       TEXT NOT NULL, "
                    + "      duracao          TEXT NULL, "
                    + "      FOREIGN KEY (posto) REFERENCES postos (idPosto), "
                    + "      FOREIGN KEY (codUsuario) REFERENCES usuarios(codUsuario) "
                    + "   ); ";
        }

        private String createTableMateriais() {
            return " CREATE TABLE IF NOT EXISTS materiais "
                    + "   ( "
                    + "      idMaterial         INTEGER PRIMARY KEY, "
                    + "      descricao          TEXT NOT NULL, "
                    + "      idCategoria        INTEGER NULL, "
                    + "      descricaoCategoria TEXT NULL "
                    + "   ); ";
        }

        private String createTableComponentes() {
            return " CREATE TABLE IF NOT EXISTS componentes "
                    + "   ( "
                    + "      idComponente INTEGER NOT NULL, "
                    + "      descricao    TEXT NOT NULL, "
                    + "      idCategoria  INTEGER NULL, "
                    + "      PRIMARY KEY(idComponente, idCategoria) "
                    + "   ); ";
        }

        private String createTableServicos() {
            return " CREATE TABLE IF NOT EXISTS servicos "
                    + "   ( "
                    + "      idServico     INTEGER, "
                    + "      descricao     TEXT NOT NULL, "
                    + "      idCategoria   INTEGER NULL, "
                    + "      tipo          INTEGER NULL, "
                    + "      unidadeMedida TEXT NULL, "
                    + "      PRIMARY KEY(idServico, idCategoria) "
                    + "   ); ";
        }

        private String createTableParalisacoes() {
            return " CREATE TABLE IF NOT EXISTS paralisacoes "
                    + "   ( "
                    + "      idParalisacao INTEGER PRIMARY KEY, "
                    + "      codigo        TEXT NOT NULL, "
                    + "      requerEstaca  TEXT NULL, "
                    + "      aplicacao     TEXT NULL, "
                    + "      descricao     TEXT NOT NULL "
                    + "   ); ";
        }

        private String createTableEquipamentos() {
            return " CREATE TABLE IF NOT EXISTS equipamentos "
                    + "   ( "
                    + "      idEquipamento      INTEGER PRIMARY KEY, "
                    + "      descricao          TEXT NOT NULL, "
                    + "      movimentacao       TEXT NOT NULL, "
                    + "      tipo               TEXT, "
                    + "      horimetro          TEXT NULL, "
                    + "      exigeJustificativa TEXT NULL, "
                    + "      quilometragem      TEXT NULL, "
                    + "      idCategoria        INTEGER NULL, "
                    + "      prefixo            TEXT NOT NULL, "
                    + "      qrcode             TEXT NULL "
                    + "   ); ";
        }

        private String createTableLubricoesEquipamento() {
            return " CREATE TABLE IF NOT EXISTS lubrificacoesEquipamento "
                    + "   ( "
                    + "      equipamento   INTEGER NOT NULL, "
                    + "      data          TEXT NOT NULL, "
                    + "      compartimento INTEGER NOT NULL, "
                    + "      horimetro     TEXT NULL, "
                    + "      quilometragem TEXT NULL, "
                    + "      categoria     INTEGER NOT NULL, "
                    + "      PRIMARY KEY (equipamento, data, compartimento, categoria), "
                    + "      FOREIGN KEY (compartimento, categoria) REFERENCES compartimentos (idCompartimento, idCategoria), "
                    + "      FOREIGN KEY (equipamento) REFERENCES equipamentos (idEquipamento) "
                    + "   ); ";
        }

        private String createTablePreventivasEquipamento() {
            return " CREATE TABLE IF NOT EXISTS preventivasEquipamento "
                    + "   ( "
                    + "      equipamento INTEGER NOT NULL, "
                    + "      data        TEXT NOT NULL, "
                    + "      horimetro   TEXT NULL, "
                    + "      PRIMARY KEY (equipamento, data), "
                    + "      FOREIGN KEY (equipamento) REFERENCES equipamentos (idEquipamento) "
                    + "   ); ";
        }

        //Atributo idAtividade foi removido para manter a compatibilidade com a versao anterior
        private String createTableFrentesObraAtividade() {
            return " CREATE TABLE IF NOT EXISTS frentesObraAtividade "
                    + "   ( "
                    + "      frentesobra INTEGER NOT NULL, "
                    + "      atividade   INTEGER NOT NULL, "
                    + "      descricao   TEXT NOT NULL, "
                    + "      PRIMARY KEY(frentesobra, atividade), "
                    + "      FOREIGN KEY(frentesobra) REFERENCES frentesobra(idFrentesObra) "
                    + "   ); ";
        }

        private String createTableApropriacoes() {
            return " CREATE TABLE IF NOT EXISTS apropriacoes "
                    + "   ( "
                    + "      idApropriacao       INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "      frentesObra         INTEGER NULL, "
                    + "      atividade           INTEGER NULL, "
                    + "      tipoApropriacao     TEXT NOT NULL, "
                    + "      dataHoraApontamento TEXT NOT NULL, "
                    + "      observacoes         TEXT NULL, "
                    + "      FOREIGN KEY(frentesObra, atividade) REFERENCES frentesObraAtividade(frentesObra, atividade) "
                    + "   ); ";
        }

        private String createTableOrigensDestinos() {
            return " CREATE TABLE IF NOT EXISTS origensDestinos "
                    + "   ( "
                    + "      idOrigensDestinos INTEGER PRIMARY KEY, "
                    + "      descricao         TEXT NOT NULL, "
                    + "      estacaInicial     TEXT NULL, "
                    + "      estacaFinal       TEXT NULL, "
                    + "      tipo              INTEGER NULL, "
                    + "      descricaoTipo     TEXT NULL "
                    + "   ); ";
        }

        private String createTableApropriacoesMovimentacao() {
            return " CREATE TABLE IF NOT EXISTS apropriacoesMovimentacao "
                    + "   ( "
                    + "      apropriacao          INTEGER NOT NULL, "
                    + "      equipamento          INTEGER NOT NULL, "
                    + "      horaInicio           TEXT NOT NULL, "
                    + "      horaTermino          TEXT NULL, "
                    + "      horimetroInicial     TEXT NULL, "
                    + "      horimetroFinal       TEXT NULL, "
                    + "      tipoApropriacao      TEXT NULL, "
                    + "      estacaOrigemInicial  TEXT NULL, "
                    + "      estacaOrigemFinal    TEXT NULL, "
                    + "      estacaDestinoInicial TEXT NULL, "
                    + "      estacaDestinoFinal   TEXT NULL, "
                    + "      origem               INTEGER NULL, "
                    + "      destino              INTEGER NULL, "
                    + "      qtdViagens           INTEGER NULL, "
                    + "      percentualCarga      TEXT NULL, "
                    + "      material             INTEGER NULL, "
                    + "      dataHoraCadastro     TEXT NOT NULL, "
                    + "      dataHoraAtualizacao  TEXT NOT NULL, "
                    + "      FOREIGN KEY(apropriacao) REFERENCES apropriacoes(idApropriacao) ON DELETE CASCADE, "
                    + "      FOREIGN KEY(equipamento) REFERENCES equipamentos(idEquipamento), "
                    + "      FOREIGN KEY(material) REFERENCES materiais(idMaterial), "
                    + "      FOREIGN KEY(origem) REFERENCES origensdestinos(idOrigemDestino), "
                    + "      FOREIGN KEY(destino) REFERENCES origensdestinos(idOrigemDestino), "
                    + "      PRIMARY KEY(apropriacao, equipamento, horaInicio) "
                    + "   ); ";
        }

        private String createTableViagensMovimentacoes() {
            return " CREATE TABLE IF NOT EXISTS viagensMovimentacoes "
                    + "   ( "
                    + "      apropriacao         INTEGER NOT NULL, "
                    + "      equipamento         INTEGER NOT NULL, "
                    + "      horaInicio          INTEGER NOT NULL, "
                    + "      horaViagem          INTEGER NOT NULL, "
                    + "      equipamentoCarga    INTEGER NULL, "
                    + "      peso                TEXT NULL, "
                    + "      material            INTEGER NULL, "
                    + "      estacaInicial       TEXT NULL, "
                    + "      estacaFinal         TEXT NULL, "
                    + "      eticket             TEXT NULL, "
                    + "      codSeguranca        INTEGER NULL, "
                    + "      nroQRCode           INTEGER NULL, "
                    + "      nroFormulario       INTEGER NULL, "
                    + "      nroFicha            TEXT NULL, "
                    + "      percentualCarga     TEXT NULL, "
                    + "      tipo                TEXT NULL, "
                    + "      horimetro           TEXT NULL, "
                    + "      hodometro           TEXT NULL, "
                    + "      apropriar           TEXT NULL, "
                    + "      observacoes         TEXT NULL, "
                    + "      dataHoraCadastro    TEXT NOT NULL, "
                    + "      dataHoraAtualizacao TEXT NOT NULL, "
                    + "      FOREIGN KEY(equipamentocarga) REFERENCES equipamentos(idEquipamento), "
                    + "      FOREIGN KEY(material) REFERENCES materiais(idMaterial), "
                    + "      FOREIGN KEY(apropriacao, equipamento, horaInicio) REFERENCES apropriacoesMovimentacao(apropriacao, equipamento, horaInicio) ON DELETE CASCADE, "
                    + "      PRIMARY KEY(apropriacao, equipamento, horaInicio, horaViagem) "
                    + "   ); ";
        }

        private String createTableEquipamentosMovimentacaoDiaria() {
            return " CREATE TABLE IF NOT EXISTS equipamentosMovimentacaoDiaria "
                    + "   ( "
                    + "      idMovimentacoesDiarias INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "      equipamento            TEXT NOT NULL, "
                    + "      dataHora               TEXT NOT NULL, "
                    + "      FOREIGN KEY(equipamento) REFERENCES equipamentos(idEquipamento ) "
                    + "   ); ";
        }

        private String createTableLocalizacao() {
            return " CREATE TABLE IF NOT EXISTS localizacao "
                    + "   ( "
                    + "      idLocalizacao   INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + "      frentesObra     INTEGER NOT NULL, "
                    + "      atividade       INTEGER NOT NULL, "
                    + "      estacaInicial   TEXT NOT NULL, "
                    + "      estacaFinal     TEXT NULL, "
                    + "      tipo            TEXT NULL, "
                    + "      dataHora        TEXT NOT NULL, "
                    + "      dataAtualizacao TEXT NOT NULL, "
                    + "      origem          INTEGER NULL, "
                    + "      atual           TEXT NULL, "
                    + "      destino         INTEGER NULL, "
                    + "      FOREIGN KEY(frentesObra, atividade) REFERENCES frentesObraAtividade(frentesObra, atividade), "
                    + "      FOREIGN KEY(origem) REFERENCES origensDestinos(idOrigemDestino), "
                    + "      FOREIGN KEY(destino) REFERENCES origensDestinos(idOrigemDestino) "
                    + "   ); ";
        }

        private String createTableLocalizacaoEquipe() {
            return " CREATE TABLE IF NOT EXISTS localizacaoEquipe "
                    + "   ( "
                    + "      localizacao INTEGER NOT NULL, "
                    + "      equipe      INTEGER NOT NULL, "
                    + "      dataHora    TEXT NOT NULL, "
                    + "      PRIMARY KEY(localizacao, equipe, datahora) "
                    + "   ); ";
        }

        private String createTableEquipamentoParteDiaria() {
            return " CREATE TABLE IF NOT EXISTS equipamentosParteDiaria "
                    + "   ( "
                    + "      idEquipamentosParteDiaria INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "      equipamento               TEXT NOT NULL, "
                    + "      dataHora                  TEXT NOT NULL, "
                    + "      apropriacao               INTEGER NULL, "
                    + "      FOREIGN KEY(apropriacao) REFERENCES apropriacoes(idApropriacao) ON DELETE CASCADE, "
                    + "      FOREIGN KEY(equipamento) REFERENCES equipamentos(idEquipamento) "
                    + "   ); ";
        }

        private String createTableApropriacoesEquipamento() {
            return " CREATE TABLE IF NOT EXISTS apropriacoesEquipamento "
                    + "   ( "
                    + "      apropriacao      INTEGER NOT NULL, "
                    + "      equipamento      INTEGER NOT NULL, "
                    + "      dataHora         TEXT NOT NULL, "
                    + "      horimetroInicial TEXT NULL, "
                    + "      horimetroFinal   TEXT NULL, "
                    + "      producao         TEXT NULL, "
                    + "      operador1        TEXT NULL, "
                    + "      operador2        TEXT NULL, "
                    + "      observacoes      TEXT NULL, "
                    + "      FOREIGN KEY(apropriacao) REFERENCES apropriacoes(idApropriacao) ON DELETE CASCADE, "
                    + "      FOREIGN KEY(equipamento) REFERENCES equipamentos(idEquipamento), "
                    + "      PRIMARY KEY(apropriacao, equipamento, dataHora) "
                    + "   ); ";
        }

        private String createTableEventosEquipamento() {
            return " CREATE TABLE IF NOT EXISTS eventosEquipamento "
                    + "   ( "
                    + "      apropriacao         INTEGER NOT NULL, "
                    + "      equipamento         INTEGER NOT NULL, "
                    + "      dataHora            TEXT NOT NULL, "
                    + "      horaInicio          TEXT NOT NULL, "
                    + "      apropriar           TEXT NULL, "
                    + "      horaTermino         TEXT NULL, "
                    + "      servico             INTEGER NULL, "
                    + "      paralisacao         TEXT NULL, "
                    + "      componente          INTEGER NULL, "
                    + "      categoria           INTEGER NULL, "
                    + "      estaca              TEXT NULL, "
                    + "      observacoes         TEXT NULL, "
                    + "      dataHoraCadastro    TEXT NOT NULL, "
                    + "      dataHoraAtualizacao TEXT NOT NULL, "
                    + "      FOREIGN KEY(apropriacao, equipamento, dataHora, horaInicio) REFERENCES apropriacoesEquipamento(apropriacao, equipamento, dataHora, horaInicio) ON DELETE CASCADE,  "
                    + "      FOREIGN KEY(servico) REFERENCES servicos(idservico), "
                    + "      FOREIGN KEY(paralisacao) REFERENCES paralisacoes(idparalisacao), "
                    + "      FOREIGN KEY(componente) REFERENCES componentes(idcomponente), "
                    + "      PRIMARY KEY(apropriacao, equipamento, dataHora, horaInicio) "
                    + "   ); ";
        }

        private String createTableCombustiveisLubrificantes() {
            return " CREATE TABLE IF NOT EXISTS combustiveisLubrificantes "
                    + "   ( "
                    + "      idCombustivelLubrificante INTEGER NOT NULL, "
                    + "      descricao                 TEXT NOT NULL, "
                    + "      unidadeMedida             TEXT NOT NULL, "
                    + "      tipo                      TEXT NULL, "
                    + "      PRIMARY KEY (idCombustivelLubrificante) "
                    + "   ); ";
        }

        private String createTablePostos() {
            return " CREATE TABLE IF NOT EXISTS postos "
                    + "   ( "
                    + "      idPosto     INTEGER NOT NULL, "
                    + "      equipamento INTEGER NOT NULL, "
                    + "      descricao   TEXT NOT NULL, "
                    + "      tipo        TEXT NULL, "
                    + "      PRIMARY KEY (idPosto), "
                    + "      FOREIGN KEY (equipamento) REFERENCES equipamentos (idEquipamento) "
                    + "   ); ";
        }

        private String createTableCombustiveisPostos() {
            return " CREATE TABLE IF NOT EXISTS combustiveisPostos "
                    + "   ( "
                    + "      posto       INTEGER NOT NULL, "
                    + "      combustivel INTEGER NOT NULL, "
                    + "      PRIMARY KEY (posto, combustivel), "
                    + "      FOREIGN KEY (posto) REFERENCES postos (idPosto), "
                    + "      FOREIGN KEY (combustivel) REFERENCES combustiveisLubrificantes (idCombustivelLubrificante) "
                    + "   ); ";
        }

        private String createTableCompartimentos() {
            return " CREATE TABLE IF NOT EXISTS compartimentos "
                    + "   ( "
                    + "      idCompartimento INTEGER NOT NULL, "
                    + "      idCategoria     INTEGER NOT NULL, "
                    + "      descricao       TEXT NOT NULL, "
                    + "      tipo            TEXT NOT NULL, "
                    + "      PRIMARY KEY (idCategoria, idCompartimento) "
                    + "   ); ";
        }

        private String createTableRAE() {
            return " CREATE TABLE IF NOT EXISTS RAE "
                    + "   ( "
                    + "      idRAE              INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + "      data               TEXT NOT NULL, "
                    + "      posto              INTEGER NOT NULL, "
                    + "      totalizadorInicial TEXT NULL, "
                    + "      totalizadorFinal   TEXT NULL, "
                    + "      FOREIGN KEY (posto) REFERENCES postos (idPosto) "
                    + "   ); ";
        }

        private String createTableAbastecimentos() {
            return " CREATE TABLE IF NOT EXISTS abastecimentos "
                    + "   ( "
                    + "      RAE                  INTEGER NOT NULL, "
                    + "      equipamento          INTEGER NOT NULL, "
                    + "      combustivel          INTEGER NOT NULL, "
                    + "      horaInicio           TEXT NOT NULL, "
                    + "      codabastecedor       INTEGER NOT NULL, "
                    + "      codoperador          INTEGER NULL, "
                    + "      tipo                 TEXT NOT NULL, "
                    + "      ccObra               INTEGER NOT NULL, "
                    + "      frentesObra          INTEGER NULL, "
                    + "      atividade            INTEGER NULL, "
                    + "      idPessoalAbastecedor INTEGER NOT NULL, "
                    + "      idPessoalOperador    INTEGER NULL, "
                    + "      horaTermino          TEXT NULL, "
                    + "      horimetro            TEXT NULL, "
                    + "      quilometragem        TEXT NULL, "
                    + "      quantidade           TEXT NULL, "
                    + "      observacao           TEXT NULL, "
                    + "      justificativa        TEXT NULL, "
                    + "      obsJustificativa     TEXT NULL, "
                    + "      PRIMARY KEY (RAE, equipamento, combustivel, horaInicio), "
                    + "      FOREIGN KEY (RAE) REFERENCES RAE (idRAE), "
                    + "      FOREIGN KEY (equipamento) REFERENCES equipamentos (idEquipamento), "
                    + "      FOREIGN KEY (codOperador ) REFERENCES usuarios (codUsuario), "
                    + "      FOREIGN KEY (codAbastecedor) REFERENCES usuarios (codUsuario), "
                    + "      FOREIGN KEY (frentesObra, atividade) REFERENCES frentesobraatividade(frentesObra, atividade), "
                    + "      FOREIGN KEY (justificativa) REFERENCES justificativasoperador(idJustificativaOperador), "
                    + "      FOREIGN KEY (ccObra) REFERENCES obras(idobra), "
                    + "      FOREIGN KEY (combustivel) REFERENCES combustiveislubrificantes (idCombustivelLubrificante) "
                    + "   ); ";
        }

        private String createTableAbastecimentosTemp() {
            return " CREATE TABLE IF NOT EXISTS abastecimentosTemp "
                    + "   ( "
                    + "      equipamento   INTEGER NOT NULL, "
                    + "      dataHora      TEXT NOT NULL, "
                    + "      codOperador   INTEGER NULL, "
                    + "      horimetro     TEXT NULL, "
                    + "      quilometragem TEXT NULL, "
                    + "      PRIMARY KEY (equipamento, dataHora), "
                    + "      FOREIGN KEY (equipamento) REFERENCES equipamentos (idEquipamento), "
                    + "      FOREIGN KEY (codOperador) REFERENCES usuarios (codUsuario) "
                    + "   ); ";
        }

        private String createTableAbastecimenosPosto() {
            return " CREATE TABLE IF NOT EXISTS abastecimentosPosto "
                    + "   ( "
                    + "      combustivel INTEGER NOT NULL, "
                    + "      posto       INTEGER NOT NULL, "
                    + "      data        TEXT NOT NULL, "
                    + "      hora        TEXT NOT NULL, "
                    + "      quantidade  TEXT NOT NULL, "
                    + "      posto2      INTEGER NULL, "
                    + "      tipo        TEXT NULL, "
                    + "      PRIMARY KEY (combustivel, posto, data, hora), "
                    + "      FOREIGN KEY (posto) REFERENCES postos (idPosto), "
                    + "      FOREIGN KEY (combustivel) REFERENCES combustiveisLubrificantes (idCombustivelLubrificante) "
                    + "   ); ";
        }

        private String createTableLubrificacoesDetalhes() {
            return " CREATE TABLE IF NOT EXISTS lubrificacoesDetalhes "
                    + "   ( "
                    + "      RAE           INTEGER NOT NULL, "
                    + "      equipamento   INTEGER NOT NULL, "
                    + "      horainicio    TEXT NOT NULL, "
                    + "      lubrificante  INTEGER NOT NULL, "
                    + "      compartimento INTEGER NOT NULL, "
                    + "      categoria     INTEGER NOT NULL, "
                    + "      quantidade    TEXT NULL, "
                    + "      observacoes   TEXT NULL, "
                    + "      PRIMARY KEY (RAE, equipamento, horaInicio, lubrificante, compartimento, categoria), "
                    + "      FOREIGN KEY (compartimento, categoria) REFERENCES compartimentos (idCompartimento , idCategoria), "
                    + "      FOREIGN KEY (RAE , equipamento , horaInicio , lubrificante) "
                    + "      REFERENCES abastecimentos (RAE , equipamento , horaInicio , combustivel) ON DELETE CASCADE "
                    + "   ); ";
        }

        private String createTableJustificavasOperador() {
            return " CREATE TABLE IF NOT EXISTS justificativasOperador "
                    + "   ( "
                    + "      idJustificativaOperador INTEGER PRIMARY KEY, "
                    + "      descricao               TEXT NOT NULL "
                    + "   ); ";
        }

        /**
         * Tabelas de Apropriação de Serviço/Mão-de-obra **
         */

        private String createTablePessoal() {
            return " CREATE TABLE IF NOT EXISTS pessoal "
                    + "   ( "
                    + "      idPessoal INTEGER NOT NULL, "
                    + "      nome      TEXT NOT NULL, "
                    + "      matricula TEXT NOT NULL, "
                    + "      PRIMARY KEY (idPessoal) "
                    + "   ); ";
        }

        private String createTableControleFrequencia() {
            return " CREATE TABLE IF NOT EXISTS controleFrequencia "
                    + "   ( "
                    + "      ccObra      INTEGER NOT NULL, "
                    + "      pessoa      INTEGER NOT NULL, "
                    + "      horaEntrada TEXT NOT NULL, "
                    + "      horaSaida   TEXT NULL, "
                    + "      equipe      INTEGER NULL, "
                    + "      observacoes TEXT NULL, "
                    + "      PRIMARY KEY (ccObra, pessoa, horaEntrada), "
                    + "      CONSTRAINT fk_controleFrequencia_obras1 FOREIGN KEY (ccObra) REFERENCES obras (idObra), "
                    + "      CONSTRAINT fk_controleFrequencia_pessoal1 FOREIGN KEY (pessoa) REFERENCES pessoal (idPessoal), "
                    + "      CONSTRAINT fk_controleFrequencia_equipesTrabalho1 FOREIGN KEY (equipe) REFERENCES equipestrabalho (idEquipe) "
                    + "   ); ";
        }

        private String createTablePeriodosHorariosTrabalho() {
            return " CREATE TABLE IF NOT EXISTS periodosHorariosTrabalho "
                    + "   ( "
                    + "      horario           INTEGER NOT NULL, "
                    + "      diaSemana         TEXT NOT NULL, "
                    + "      horaInicio        TEXT NOT NULL, "
                    + "      horaTermino       TEXT NOT NULL, "
                    + "      produtivo         TEXT NULL, "
                    + "      cobraHoraExtra    TEXT NULL, "
                    + "      codigoParalisacao TEXT NULL, "
                    + "      PRIMARY KEY (horario, diaSemana, horaInicio), "
                    + "      CONSTRAINT fk_periodosHorariosTrabalho_horariosTrabalho1 FOREIGN KEY (horario) REFERENCES horariostrabalho (idHorario) "
                    + "   ); ";
        }

        private String createTableHorariosTrabalho() {
            return " CREATE TABLE IF NOT EXISTS horariosTrabalho "
                    + "   ( "
                    + "      idHorario   INTEGER NOT NULL, "
                    + "      ccObra      INTEGER NOT NULL, "
                    + "      descricao   TEXT NULL, "
                    + "      horaInicio  TEXT NULL, "
                    + "      horaTermino TEXT NULL, "
                    + "      PRIMARY KEY (idHorario), "
                    + "      CONSTRAINT fk_horariosTrabalho_obras1 FOREIGN KEY (ccObra) REFERENCES obras (idObra) "
                    + "   ); ";
        }

        private String createTableEquipesTrabalho() {
            return " CREATE TABLE IF NOT EXISTS equipesTrabalho "
                    + "   ( "
                    + "      idEquipe        INTEGER NOT NULL, "
                    + "      ccObra          INTEGER NOT NULL, "
                    + "      nomeEquipe      TEXT NOT NULL, "
                    + "      apelido         TEXT NULL, "
                    + "      formacao        INTEGER NULL, "
                    + "      datacriacao     TEXT NULL, "
                    + "      responsavel     INTEGER NULL, "
                    + "      ativa           TEXT NOT NULL, "
                    + "      apropriavel     TEXT NOT NULL, "
                    + "      horarioTrabalho INTEGER NULL, "
                    + "      PRIMARY KEY (idEquipe), "
                    + "      CONSTRAINT fk_equipesTrabalho_pessoal1 FOREIGN KEY (responsavel) REFERENCES pessoal (idPessoal), "
                    + "      CONSTRAINT fk_equipesTrabalho_horariosTrabalho1 FOREIGN KEY (horarioTrabalho) REFERENCES horariosTrabalho (idHorario), "
                    + "      CONSTRAINT fk_equipesTrabalho_obras1 FOREIGN KEY (ccObra) REFERENCES obras (idObra) "
                    + "   ); ";
        }

        private String createTableEquipamentosEquipe() {
            return " CREATE TABLE IF NOT EXISTS equipamentosEquipe "
                    + "   ( "
                    + "      equipe        INTEGER NOT NULL, "
                    + "      equipamento   INTEGER NOT NULL, "
                    + "      dataIngresso  TEXT NOT NULL, "
                    + "      dataSaida     TEXT NULL, "
                    + "      servicoPadrao INTEGER NULL, "
                    + "      PRIMARY KEY (equipe, equipamento, dataIngresso), "
                    + "      CONSTRAINT fk_equipamentosEquipe_equipesTrabalho1 FOREIGN KEY (equipe) REFERENCES equipesTrabalho (idEquipe), "
                    + "      CONSTRAINT fk_equipamentosEquipe_equipamentos1 FOREIGN KEY (equipamento) REFERENCES equipamentos (idequipamento), "
                    + "      CONSTRAINT fk_equipamentosEquipe_servicos1 FOREIGN KEY (servicoPadrao) REFERENCES servicos (idServico) "
                    + "   ); ";
        }

        private String createTableIntegrantesTemp() {
            return " CREATE TABLE IF NOT EXISTS integrantesTemp "
                    + "   ( "
                    + "      equipe       INTEGER NOT NULL, "
                    + "      pessoa       INTEGER NOT NULL, "
                    + "      dataIngresso TEXT NOT NULL, "
                    + "      dataSaida    TEXT NULL, "
                    + "      PRIMARY KEY (equipe, pessoa, dataIngresso), "
                    + "      CONSTRAINT fk_integranteTemp_equipesTrabalho1 FOREIGN KEY (equipe) REFERENCES equipestrabalho (idEquipe), "
                    + "      CONSTRAINT fk_integranteTemp_pessoal1 FOREIGN KEY (pessoa) REFERENCES pessoal (idPessoal) "
                    + "   ); ";
        }

        private String createTableIntegrantesEquipe() {
            return " CREATE TABLE IF NOT EXISTS integrantesequipe "
                    + "   ( "
                    + "      equipe       INTEGER NOT NULL, "
                    + "      pessoa       INTEGER NOT NULL, "
                    + "      dataIngresso TEXT NOT NULL, "
                    + "      dataSaida    TEXT NULL, "
                    + "      PRIMARY KEY (equipe, pessoa, dataIngresso), "
                    + "      CONSTRAINT fk_integrantesEquipe_equipesTrabalho1 FOREIGN KEY (equipe) REFERENCES equipestrabalho (idEquipe), "
                    + "      CONSTRAINT fk_integrantesEquipe_pessoal1 FOREIGN KEY (pessoa) REFERENCES pessoal (idPessoal) "
                    + "   ); ";
        }

        private String createTableApropriacaoServico() {
            return " CREATE TABLE IF NOT EXISTS apropriacaoServico "
                    + "   ( "
                    + "      idApropriacao       INTEGER NOT NULL, "
                    + "      equipe              INTEGER NOT NULL, "
                    + "      idServico           INTEGER NOT NULL, "
                    + "      quantidadeProduzida DECIMAL(8, 2) NOT NULL, "
                    + "      horaIni             TEXT NOT NULL, "
                    + "      horaFim             TEXT NULL, "
                    + "      observacoes         TEXT NULL, "
                    + "      PRIMARY KEY (idApropriacao, equipe, idServico, horaIni), "
                    + "      CONSTRAINT fk_ApropriacaoServico_apropriacoes1 FOREIGN KEY (idApropriacao) REFERENCES apropriacoes (idApropriacao) ON DELETE CASCADE, "
                    + "      CONSTRAINT fk_apropriacaoServico_equipesTrabalho1 FOREIGN KEY (equipe) REFERENCES equipesTrabalho (idEquipe), "
                    + "      CONSTRAINT fk_apropriacaoServico_servicos1 FOREIGN KEY (idServico) REFERENCES servicos (idServico) "
                    + "   ); ";
        }

        private String createTableParalisacoesEquipe() {
            return " CREATE TABLE IF NOT EXISTS paralisacoesEquipe "
                    + "   ( "
                    + "      apropriacao   INTEGER NOT NULL, "
                    + "      equipe        INTEGER NOT NULL, "
                    + "      idParalisacao INTEGER NOT NULL, "
                    + "      servico       INTEGER NULL, "
                    + "      horaInicio    TEXT NOT NULL, "
                    + "      horaTermino   TEXT NOT NULL, "
                    + "      observacoes   TEXT NULL, "
                    + "      PRIMARY KEY (apropriacao, equipe, idParalisacao, horaInicio), "
                    + "      CONSTRAINT fk_paralisacoesEquipe_apropriacoes1 FOREIGN KEY (apropriacao) REFERENCES apropriacoes (idapropriacao), "
                    + "      CONSTRAINT fk_paralisacoesEquipe_equipesTrabalho1 FOREIGN KEY (equipe) REFERENCES equipesTrabalho (idEquipe), "
                    + "      CONSTRAINT fk_paralisacoesEquipe_paralisacoes1 FOREIGN KEY (idParalisacao) REFERENCES paralisacoes (idParalisacao) "
                    + "   ); ";
        }

        private String createTableAtividades() {
            return " CREATE TABLE IF NOT EXISTS atividades "
                    + "   ( "
                    + "      idAtividade INTEGER NOT NULL, "
                    + "      descricao   TEXT NULL, "
                    + "      PRIMARY KEY (idAtividade) "
                    + "   ); ";
        }

        private String createTableAtividadesServicos() {
            return " CREATE TABLE IF NOT EXISTS atividadesServicos "
                    + "   ( "
                    + "      idAtividadeServico INTEGER NOT NULL, "
                    + "      atividade          INTEGER NULL, "
                    + "      servico            INTEGER NULL, "
                    + "      descricao          TEXT NULL, "
                    + "      ordem              INTEGER NULL, "
                    + "      codigoPrevix       TEXT NULL, "
                    + "      atividadePai       INTEGER NULL, "
                    + "      PRIMARY KEY (idAtividadeServico), "
                    + "      CONSTRAINT fk_atividadesServicos_atividades1 FOREIGN KEY (atividade) REFERENCES atividades (idAtividade), "
                    + "      CONSTRAINT fk_atividadesServicos_servicos1 FOREIGN KEY (servico) REFERENCES servicos (idServico), "
                    + "      CONSTRAINT fk_atividadesServicos_atividadesServicos1 FOREIGN KEY (atividadePai) REFERENCES atividadesservicos (idAtividadeServico) "
                    + "   ); ";
        }

        private String createTableApropriacoesMaoObra() {
            return " CREATE TABLE IF NOT EXISTS apropriacoesMaoObra "
                    + "   ( "
                    + "      apropriacao INTEGER NOT NULL, "
                    + "      pessoa      INTEGER NOT NULL, "
                    + "      horaInicio  TEXT NOT NULL, "
                    + "      horaTermino TEXT NULL, "
                    + "      equipe      INTEGER NOT NULL, "
                    + "      servico     INTEGER NOT NULL, "
                    + "      observacoes TEXT NULL, "
                    + "      PRIMARY KEY (apropriacao, pessoa, horaInicio), "
                    + "      CONSTRAINT fk_apropriacoesMaoObra_apropriacoes1 FOREIGN KEY (apropriacao) REFERENCES apropriacoes (idApropriacao) ON DELETE CASCADE, "
                    + "      CONSTRAINT fk_apropriacoesMaoObra_pessoal1 FOREIGN KEY (pessoa) REFERENCES pessoal (idPessoal), "
                    + "      CONSTRAINT fk_apropriacoesMaoObra_equipesTrabalho1 FOREIGN KEY (equipe) REFERENCES equipesTrabalho (idEquipe) "
                    + "   ); ";
        }

        private String createTableParalisacoesMaoObra() {
            return " CREATE TABLE IF NOT EXISTS paralisacoesMaoObra "
                    + "   ( "
                    + "      ccObra      INTEGER NOT NULL, "
                    + "      pessoa      INTEGER NOT NULL, "
                    + "      horaInicio  TEXT NOT NULL, "
                    + "      horaTermino TEXT NULL, "
                    + "      apropriacao INTEGER NOT NULL, "
                    + "      paralisacao INTEGER NOT NULL, "
                    + "      equipe      INTEGER NOT NULL, "
                    + "      servico     INTEGER NULL, "
                    + "      observacoes TEXT NULL, "
                    + "      PRIMARY KEY (ccObra, pessoa, horaInicio), "
                    + "      CONSTRAINT fk_paralisacoesMaoObra_obras1 FOREIGN KEY (ccobra) REFERENCES obras (idObra), "
                    + "      CONSTRAINT fk_paralisacoesMaoObra_pessoal1 FOREIGN KEY (pessoa) REFERENCES pessoal (idPessoal), "
                    + "      CONSTRAINT fk_paralisacoesMaoObra_paralisacoes1 FOREIGN KEY (paralisacao) REFERENCES paralisacoes (idParalisacao), "
                    + "      CONSTRAINT fk_paralisacoesMaoObra_equipesTrabalho1 FOREIGN KEY (equipe) REFERENCES equipestrabalho (idEquipe) "
                    + "   ); ";
        }

        /**
         * Tabela auxiliar
         * So existe no Tablet
         *
         * @return
         */
        private String createTableLogEnvioInformacoes() {
            return " CREATE TABLE IF NOT EXISTS logEnvioInformacoes "
                    + "   ( "
                    + "      id            INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "      arquivo       TEXT NULL, "
                    + "      data          TEXT NULL, "
                    + "      obra          TEXT NULL, "
                    + "      dataHoraEnvio TEXT NULL "
                    + "   ); ";

        }

        private String createTableEventoEquipe() {
            return " CREATE TABLE IF NOT EXISTS eventoEquipe "
                    + "   ( "
                    + "      idEventoEquipe INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "      localizacao    INTEGER NOT NULL, "
                    + "      equipe         INTEGER NOT NULL, "
                    + "      paralisacao    INTEGER NOT NULL, "
                    + "      tipoHorario    INTEGER NOT NULL, "
                    + "      data           TEXT NOT NULL, "
                    + "      horaIni        TEXT NOT NULL, "
                    + "      horaFim        TEXT NULL, "
                    + "      observacao     TEXT NULL, "
                    + "      servico        INTEGER NULL, "
                    + "      apropriacao    INTEGER NULL "
                    + "   ); ";
        }

        private String createTableAusencia() {
            return " CREATE TABLE IF NOT EXISTS ausencia "
                    + "   ( "
                    + "      equipe INTEGER NOT NULL, "
                    + "      pessoa INTEGER NOT NULL, "
                    + "      data   TEXT NOT NULL, "
                    + "      PRIMARY KEY (equipe, pessoa, data), "
                    + "      CONSTRAINT fk_ausencia_pessoal1 FOREIGN KEY (pessoa) REFERENCES pessoal (idPessoal) ON DELETE no action ON UPDATE no action, "
                    + "      CONSTRAINT fk_ausencia_equipesTrabalho1 FOREIGN KEY (equipe) REFERENCES equipesTrabalho (idEquipe) ON DELETE no action ON UPDATE no action "
                    + "   ); ";
        }

        private String createTablePrevisaoServico() {
            return " CREATE TABLE IF NOT EXISTS previsaoServico "
                    + "   ( "
                    + "      ccObra     INTEGER NOT NULL, "
                    + "      frenteObra INTEGER NOT NULL, "
                    + "      atividade  INTEGER NOT NULL, "
                    + "      servico    INTEGER NOT NULL, "
                    + "      PRIMARY KEY (ccObra, frenteObra, atividade, servico), "
                    + "      CONSTRAINT fk_previsaoServico_atividades1 FOREIGN KEY (atividade) REFERENCES atividades (idAtividade) ON DELETE no action ON UPDATE no action, "
                    + "      CONSTRAINT fk_previsaoServico_servicos1 FOREIGN KEY (servico) REFERENCES servicos (idServico) ON DELETE no action ON UPDATE no action, "
                    + "      CONSTRAINT fk_previsaoServico_frentesObra1 FOREIGN KEY (frenteObra) REFERENCES frentesobra (idFrentesObra) ON DELETE no action ON UPDATE no action, "
                    + "      CONSTRAINT fk_previsaoServico_obras1 FOREIGN KEY (ccObra) REFERENCES obras (idObra) ON DELETE no action ON UPDATE no action "
                    + "   ); ";
        }


        // Moises - modulo de manutencao --

        private String createTableManutencaoEquipamentos(){
            return "CREATE TABLE IF NOT EXISTS manutencaoEquipamentos "
                    +"  ("
                    +"      idEquipamento INTEGER NOT NULL, "
                    +"      data TEXT NOT NULL, "
                    +"      horaInicio TEXT NOT NULL, "
                    +"      horaTermino TEXT NOT NULL, "
                    +"      horimetro TEXT NULL, "
                    +"      hodometro TEXT NULL, "
                    +"      observacao TEXT NULL, "
                    +"      PRIMARY KEY(idEquipamento, data) "
                    +"  ); ";
        }


        private String createTableEquipamentoCategorias(){
            return "CREATE TABLE IF NOT EXISTS equipamentoCategorias "
                    +"  ("
                    +"      idCategoria INTEGER PRIMARY KEY, "
                    +"      descricao TEXT NOT NULL "
                    +"  ); ";
        }


        private String createTableManutencaoServicos(){
            return "CREATE TABLE IF NOT EXISTS manutencaoServicos "
                    +"  ("
                    +"      idManutencaoServico INTEGER PRIMARY KEY, "
                    +"      descricao TEXT NOT NULL "
                    +"  ); ";
        }

        private String createTableManutencaoServicoPorCategoriaEquipamento(){
            return "CREATE TABLE IF NOT EXISTS manutencaoServicoPorCategoriaEquipamento "
                    +"  ("
                    +"      idServicoCategoriaEquipamento INTEGER NOT NULL, "
                    +"      idManutencaoServico INTEGER NOT NULL, "
                    +"      idCategoriaEquipamento INTEGER NOT NULL "
                    +"  ); ";
        }

        private String createTableManutencaoEquipamentoServicos(){
            return "CREATE TABLE IF NOT EXISTS manutencaoEquipamentoServicos "
                    +"  ("
                    +"      idRegistro INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +"      idEquipamento INTEGER NOT NULL, "
                    +"      data TEXT NOT NULL, "
                    +"      idServicoCategoriaEquipamento INTEGER NOT NULL, "
                    +"      horaInicio TEXT NOT NULL, "
                    +"      horaTermino TEXT NOT NULL, "
                    +"      observacao TEXT NULL, "
                    +"      FOREIGN KEY(idEquipamento, data) REFERENCES manutencaoEquipamentos (idEquipamento, data) ON DELETE cascade ON UPDATE cascade "
                    +"      FOREIGN KEY(idServicoCategoriaEquipamento) REFERENCES manutencaoServicoPorCategoriaEquipamento (idServicoCategoriaEquipamento) ON DELETE cascade ON UPDATE cascade "
                    +"  ); ";
        }

        private String createTableLog(){
            return "CREATE TABLE IF NOT EXISTS logAuditoria "
                    +"  ("
                    +"      idLog INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +"      modulo TEXT NOT NULL, "
                    +"      dataHora TEXT NOT NULL, "
                    +"      dispositivo TEXT NOT NULL, "
                    +"      acao TEXT NOT NULL "
                    +"  ); ";
        }

        private String createIndexes() {
            return "CREATE INDEX fk_controleFrequencia_pessoal1_idx ON controleFrequencia (pessoa ASC);" +
                    "CREATE INDEX fk_controleFrequencia_equipesTrabalho1_idx ON controleFrequencia (equipe ASC);" +
                    "CREATE INDEX fk_horariosTrabalho_obras1_idx ON horariosTrabalho (ccObra ASC);" +
                    "CREATE INDEX fk_equipesTrabalho_pessoal1_idx ON equipesTrabalho (responsavel ASC);" +
                    "CREATE INDEX fk_equipesTrabalho_horariosTrabalho1_idx ON equipesTrabalho (horarioTrabalho ASC);" +
                    "CREATE INDEX fk_equipesTrabalho_obras1_idx ON equipesTrabalho (ccObra ASC);" +
                    "CREATE INDEX fk_integranteTemp_pessoal1_idx ON integrantesTemp (pessoa ASC);" +
                    "CREATE INDEX fk_integrantesEquipe_pessoal1_idx ON integrantesEquipe (pessoa ASC);" +
                    "CREATE INDEX fk_ApropriacaoServico_apropriacoes1_idx ON apropriacaoServico (idApropriacao ASC);" +
                    "CREATE INDEX fk_apropriacaoServico_equipesTrabalho1_idx ON apropriacaoServico (equipe ASC);" +
                    "CREATE INDEX fk_apropriacaoServico_servicos1_idx ON apropriacaoServico (idServico ASC);" +
                    "CREATE INDEX fk_paralisacoesEquipe_equipesTrabalho1_idx ON paralisacoesEquipe (equipe ASC);" +
                    "CREATE INDEX fk_paralisacoesEquipe_paralisacoes1_idx ON paralisacoesEquipe (idParalisacao ASC);" +
                    "CREATE INDEX fk_atividadesServicos_atividades1_idx ON atividadesServicos (atividade ASC);" +
                    "CREATE INDEX fk_atividadesServicos_servicos1_idx ON atividadesServicos (servico ASC);" +
                    "CREATE INDEX fk_atividadesServicos_atividadesServicos1_idx ON atividadesServicos (atividadePai ASC);" +
                    "CREATE INDEX fk_apropriacoesMaoObra_pessoal1_idx ON apropriacoesMaoObra (pessoa ASC);" +
                    "CREATE INDEX fk_paralisacoesMaoObra_pessoal1_idx ON paralisacoesMaoObra (pessoa ASC);" +
                    "CREATE INDEX fk_apropriacoesMaoObra_equipesTrabalho1_idx ON apropriacoesMaoObra (equipe ASC);" +
                    "CREATE INDEX fk_paralisacoesMaoObra_paralisacoes1_idx ON paralisacoesMaoObra (paralisacao ASC);" +
                    "CREATE INDEX fk_paralisacoesMaoObra_equipesTrabalho1_idx ON paralisacoesMaoObra (equipe ASC);" +
                    "CREATE INDEX fk_ausencia_pessoal1_idx ON ausencia (pessoa ASC);" +
                    "CREATE INDEX fk_ausencia_equipesTrabalho1_idx ON ausencia (equipe ASC);" +
                    "CREATE INDEX fk_previsaoServico_servicos1_idx ON previsaoServico (servico ASC);" +
                    "CREATE INDEX fk_previsaoServico_frentesObra1_idx ON previsaoServico (frenteObra ASC);" +
                    "CREATE INDEX fk_previsaoServico_obras1_idx ON previsaoServico (ccObra ASC);";
        }

    }

}
