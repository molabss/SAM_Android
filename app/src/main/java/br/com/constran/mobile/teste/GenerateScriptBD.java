package br.com.constran.mobile.teste;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mateus_vitali on 16/12/14.
 */
public class GenerateScriptBD {


    public static void main(String[] args) {
        try {

            for (String sql : generateDLL()) {
                System.out.println(sql);
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<String> generateDLL() {
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

        //indexes
        ddlScript.add(createIndexes());

        return ddlScript;
    }

    private static String createTableLogEnvioInformacoes() {
        return " create table if not exists logEnvioInformacoes ( "
                + " id integer primary key autoincrement,  "
                + " arquivo text null,"
                + " data text null,"
                + " obra text null,"
                + " dataHoraEnvio text null); ";
    }

    private static String createTableObras() {
        return " create table if not exists obras ( "
                + " idObra integer primary key,  "
                + " descricao text not null, "
                + " exibirHorimetro text, "
                + " horimetroObrigatorio text, "
                + " usaQRCode text, "
                + " usaOrigemDestino text, "
                + " usaQRCodePessoal text); ";
    }

    private static String createTableUsuarios() {
        return " create table if not exists usuarios ( "
                + " codUsuario integer primary key autoincrement,  "
                + " idUsuario integer null,  "
                + " idUsuarioPessoal integer null,  "
                + " grupo text  null,"
                + " funcao text  null,"
                + " nome text not null,"
                + " senha text null, "
                + " qrcode text null, "
                + "  ccObra text null ); ";
    }

    private static String createTableIndicesPluviometrico() {
        return "create table if not exists indicesPluviometrico ( " +
                "       id integer primary key autoincrement, " +
                "       data text null, " +
                "       estacaInicial text null, " +
                "       estacaFinal text null, " +
                "       pluviometro text null, " +
                "       volumeChuva integer null );";
    }

    private static String createTableFrentesObra() {
        return " create table if not exists frentesObra( "
                + " idFrentesObra integer primary key,  "
                + " obra integer not null, "
                + " descricao text not null ); ";
    }

    private static String createTableConfiguracoes() {
        return "create table if not exists configuracoes( "
                + " obra text not null, "
                + " obra2 text null, "
                + " codUsuario integer null, "
                + " idUsuario integer null, "
                + " idUsuarioPessoal integer null, "
                + " dataHora text not null, "
                + " dispositivo text not null, "
                + " servidor text not null, "
                + " portaWeb text not null, "
                + " portaFtp text not null, "
                + " eticket text not null, "
                + " atual text null,   "
                + " posto integer  null,"
                + " referencia text not null, "
                + " tolerancia text not null, "
                + " duracao text null, "
                + " FOREIGN KEY (posto) "
                + " REFERENCES postos (idPosto), "
                + " FOREIGN KEY(codUsuario) REFERENCES usuarios(codUsuario) "
                + "); ";
    }

    private static String createTableMateriais() {
        return " create table if not exists materiais( "
                + " idMaterial integer primary key,  "
                + " descricao text not null,  "
                + " idCategoria integer null, "
                + " descricaoCategoria text null  );";
    }

    private static String createTableComponentes() {
        return " create table if not exists componentes( "
                + " idComponente integer not null,  "
                + " descricao text not null,  "
                + " idCategoria integer null, "
                + " PRIMARY KEY(idComponente, idCategoria)); ";
    }

    private static String createTableServicos() {
        return " create table if not exists servicos( "
                + " idServico integer,  "
                + " descricao text not null, "
                + " idCategoria integer null, "
                + " tipo integer null, "
                + " unidadeMedida text null, "
                + " PRIMARY KEY(idServico, idCategoria)); ";
    }

    private static String createTableParalisacoes() {
        return " create table if not exists paralisacoes( "
                + " idParalisacao integer primary key,  "
                + " codigo text not null, "
                + " requerEstaca text null, "
                + " aplicacao text null, "
                + " descricao text not null  );";
    }

    private static String createTableEquipamentos() {
        return " create table if not exists equipamentos( "
                + " idEquipamento integer primary key,  "
                + " descricao text not null, "
                + " movimentacao text not null, "
                + " tipo text, "
                + " horimetro text null, "
                + " exigeJustificativa text null, "
                + " quilometragem text null, "
                + " idCategoria integer null, "
                + " prefixo text not null, "
                + " qrcode text null "
                + " ); ";
    }

    private static String createTableLubricoesEquipamento() {
        return " create table if not exists lubrificacoesEquipamento( "
                + " equipamento integer not null , "
                + " data text not null , "
                + " compartimento integer not null , "
                + " horimetro text  null , "
                + " quilometragem text  null , "
                + " categoria integer not null , "
                + " PRIMARY KEY (equipamento, data, compartimento, categoria) , "
                + " FOREIGN KEY (compartimento , categoria ) "
                + " REFERENCES compartimentos (idCompartimento , idCategoria ), "
                + " FOREIGN KEY (equipamento) "
                + " REFERENCES equipamentos (idEquipamento) ); ";
    }

    private static String createTablePreventivasEquipamento() {
        return " create table if not exists preventivasEquipamento( "
                + " equipamento integer not null , "
                + " data text not null , "
                + " horimetro text null , "
                + " PRIMARY KEY (equipamento, data) , "
                + " FOREIGN KEY (equipamento) "
                + " REFERENCES equipamentos (idEquipamento) ); ";
    }

    //Atributo idAtividade foi removido para manter a compatibilidade com a versao anterior
    private static String createTableFrentesObraAtividade() {
        return " create table if not exists frentesObraAtividade( "
                + " frentesObra integer not null , "
                + " atividade integer not null  , "
                + " descricao text not null, "
                + " PRIMARY KEY(frentesObra, atividade),"
                + " FOREIGN KEY(frentesObra) REFERENCES frentesObra(idFrentesObra)); ";
    }

    private static String createTableApropriacoes() {
        return " create table if not exists apropriacoes(idApropriacao integer primary key "
                + " autoincrement,  "
                + " frentesObra integer null, "
                + " atividade integer null, "
                + " tipoApropriacao text  not null , "
                + " dataHoraApontamento text  not null , "
                + " observacoes text null, "
                + "  FOREIGN KEY(frentesObra, atividade) REFERENCES frentesObraAtividade(frentesObra, atividade) ); ";
    }

    private static String createTableOrigensDestinos() {
        return " create table if not exists origensDestinos( "
                + " idOrigensDestinos integer primary key,  "
                + " descricao text not null,  "
                + " estacaInicial text null, "
                + " estacaFinal text null, "
                + " tipo integer null, "
                + " descricaoTipo text null ); ";
    }

    private static String createTableApropriacoesMovimentacao() {
        return " create table if not exists apropriacoesMovimentacao( "
                + " apropriacao integer not null, "
                + " equipamento integer not null, "
                + " horaInicio  text not null, "
                + " horaTermino text null, "
                + " horimetroInicial text null, "
                + " horimetroFinal text null, "
                + " tipoApropriacao text null, "
                + " estacaOrigemInicial text null, "
                + " estacaOrigemFinal text null, "
                + " estacaDestinoInicial text null, "
                + " estacaDestinoFinal text null, "
                + " origem integer null, "
                + " destino integer null, "
                + " qtdViagens integer null, "
                + " percentualCarga text null, "
                + " material integer null, "
                + " dataHoraCadastro text not null, "
                + " dataHoraAtualizacao text not null, "
                + " FOREIGN KEY(apropriacao) REFERENCES apropriacoes(idApropriacao) on delete cascade,  "
                + " FOREIGN KEY(equipamento) REFERENCES equipamentos(idEquipamento ), "
                + " FOREIGN KEY(material)    REFERENCES materiais(idMaterial), "
                + " FOREIGN KEY(origem) REFERENCES origensDestinos(idOrigemDestino), "
                + " FOREIGN KEY(destino) REFERENCES origensDestinos(idOrigemDestino), "
                + " PRIMARY KEY(apropriacao, equipamento, horaInicio)); ";
    }

    private static String createTableViagensMovimentacoes() {
        return "  create table if not exists viagensMovimentacoes( "
                + " apropriacao integer not null, "
                + " equipamento integer not null, "
                + " horaInicio integer not null, "
                + " horaViagem integer not null, "
                + " equipamentoCarga integer null, "
                + " peso text null, "
                + " material integer null,  "
                + " estacaInicial text null, "
                + " estacaFinal text null, "
                + " eticket text null, "
                + " codSeguranca integer null, "
                + " nroQRCode integer null, "
                + " nroFormulario integer null, "
                + " percentualCarga text null, "
                + " tipo text null,   "
                + " horimetro text null , "
                + " hodometro text null , "
                + " apropriar text  null, "
                + " observacoes text  null, "
                + " dataHoraCadastro text not null, "
                + " dataHoraAtualizacao text not null, "
                + " FOREIGN KEY(equipamentoCarga) REFERENCES equipamentos(idEquipamento ), "
                + " FOREIGN KEY(material) REFERENCES materiais(material ), "
                + " FOREIGN KEY(apropriacao, equipamento, horaInicio) REFERENCES apropriacoesMovimentacao(apropriacao, equipamento, horaInicio) on delete cascade,  "
                + " PRIMARY KEY(apropriacao, equipamento, horaInicio, horaViagem)); ";
    }

    private static String createTableEquipamentosMovimentacaoDiaria() {
        return " create table if not exists equipamentosMovimentacaoDiaria(   "
                + " idMovimentacoesDiarias integer primary key autoincrement,    "
                + " equipamento text not null,   "
                + " dataHora text not null,  "
                + " 		FOREIGN KEY(equipamento) REFERENCES equipamentos(idEquipamento ) ); ";
    }

    private static String createTableLocalizacao() {
        return "create table if not exists localizacao(  "
                + " idLocalizacao integer not null primary key autoincrement,  "
                + " frentesObra integer not null,   "
                + " atividade  integer not null,   "
                + " estacaInicial  text not null,   "
                + " estacaFinal text null,   "
                + " tipo text null,   "
                + " dataHora text not null,   "
                + " dataAtualizacao text not null,  "
                + " origem integer null,   "
                + " atual text null,   "
                + " destino integer null,  "
                + " FOREIGN KEY(frentesObra, atividade) REFERENCES frentesObraAtividade(frentesObra, atividade), "
                + " FOREIGN KEY(origem) REFERENCES origensDestinos(idOrigemDestino), "
                + " FOREIGN KEY(destino) REFERENCES origensDestinos(idOrigemDestino) "
                + "); ";
    }

    private static String createTableLocalizacaoEquipe() {
        return "create table if not exists localizacaoEquipe(  "
                + " localizacao integer not null,  "
                + " equipe integer not null,   "
                + " dataHora text not null, "
                + " PRIMARY KEY(localizacao, equipe, dataHora)); ";
    }

    private static String createTableEquipamentoParteDiaria() {
        return " create table if not exists equipamentosParteDiaria (   "
                + " idEquipamentosParteDiaria integer primary key autoincrement,    "
                + " equipamento text not null,   "
                + " dataHora text not null,   "
                + " apropriacao integer null, "
                + " FOREIGN KEY(apropriacao) REFERENCES apropriacoes(idApropriacao) on delete cascade,  "
                + " FOREIGN KEY(equipamento) REFERENCES equipamentos(idEquipamento) ); ";
    }

    private static String createTableApropriacoesEquipamento() {
        return " create table if not exists apropriacoesEquipamento(   "
                + " apropriacao integer not null, "
                + " equipamento integer not null, "
                + " dataHora  text not null, "
                + " horimetroInicial  text null,   "
                + " horimetroFinal text  null,   "
                + " producao text  null,   "
                + " operador1 text  null,   "
                + " operador2 text  null,  "
                + " observacoes text null, "
                + " FOREIGN KEY(apropriacao) REFERENCES apropriacoes(idApropriacao) on delete cascade,  "
                + " FOREIGN KEY(equipamento) REFERENCES equipamentos(idEquipamento), "
                + " PRIMARY KEY(apropriacao, equipamento, dataHora) ); ";
    }

    private static String createTableEventosEquipamento() {
        return " create table if not exists eventosEquipamento(   "
                + " apropriacao integer not null, "
                + " equipamento integer not null, "
                + " dataHora  text not null, "
                + "	horaInicio  text not null,   "
                + "	apropriar  text null,   "
                + "	horaTermino  text null,   "
                + "	servico integer  null,   "
                + "	paralisacao text  null,   "
                + "	componente integer  null,   "
                + "	categoria  integer  null,   "
                + "	estaca text  null,  "
                + "	observacoes text  null,  "
                + "  dataHoraCadastro text not null, "
                + "  dataHoraAtualizacao text not null, "
                + "  FOREIGN KEY(apropriacao, equipamento, dataHora, horaInicio) REFERENCES apropriacoesEquipamento(apropriacao, equipamento, dataHora, horaInicio) on delete cascade,  "
                + "  FOREIGN KEY(servico) REFERENCES servicos(idServico),  "
                + "  FOREIGN KEY(paralisacao) REFERENCES paralisacoes(idParalisacao),  "
                + "  FOREIGN KEY(componente) REFERENCES componentes(idComponente),  "
                + "  PRIMARY KEY(apropriacao, equipamento, dataHora, horaInicio) ); ";
    }

    private static String createTableCombustiveisLubrificantes() {
        return " create table if not exists combustiveisLubrificantes ( "
                + " idCombustivelLubrificante integer not null , "
                + " descricao text not null , "
                + " unidadeMedida text not null , "
                + " tipo text null , "
                + " PRIMARY KEY (idCombustivelLubrificante) ); ";
    }

    private static String createTablePostos() {
        return " create table if not exists postos ( "
                + " idPosto integer not null , "
                + " equipamento integer not null , "
                + " descricao text not null , "
                + " tipo text null , "
                + " PRIMARY KEY (idPosto) , "
                + " FOREIGN KEY (equipamento) "
                + " REFERENCES equipamentos (idEquipamento )); ";
    }

    private static String createTableCombustiveisPostos() {
        return " create table if not exists combustiveisPostos ( "
                + " posto integer not null ,  "
                + " combustivel integer not null ,   "
                + " PRIMARY KEY (posto, combustivel), "
                + " FOREIGN KEY (posto) "
                + " REFERENCES postos (idPosto ), "
                + " FOREIGN KEY (combustivel ) "
                + " REFERENCES combustiveisLubrificantes (idCombustivelLubrificante )); ";
    }

    private static String createTableCompartimentos() {
        return " create table if not exists compartimentos ( "
                + " idCompartimento integer not null , "
                + " idCategoria integer not null , "
                + " descricao text not null , "
                + " tipo text not null , "
                + " PRIMARY KEY (idCategoria, idCompartimento) ); ";
    }

    private static String createTableRAE() {
        return " create table if not exists RAE ( "
                + " idRAE integer not null  PRIMARY KEY autoincrement , "
                + " data text  not null,"
                + " posto integer  not null,"
                + " totalizadorInicial text null,"
                + " totalizadorFinal   text null,"
                + " FOREIGN KEY (posto) "
                + " REFERENCES postos (idPosto) ); ";
    }

    private static String createTableAbastecimentos() {
        return " create table if not exists abastecimentos ( "
                + " RAE integer not null , "
                + " equipamento integer not null , "
                + " combustivel integer not null , "
                + " horaInicio text not null , "
                + " codAbastecedor integer not null , "
                + " codOperador integer null , "
                + " tipo text not null , "
                + " ccObra integer not null, "
                + " frentesObra integer null,   "
                + " atividade  integer null,   "
                + " idPessoalAbastecedor integer not null , "
                + " idPessoalOperador integer null , "
                + " horaTermino text null , "
                + " horimetro text null , "
                + " quilometragem text null , "
                + " quantidade text null , "
                + " observacao text null , "
                + " justificativa text null , "
                + " obsJustificativa text null , "
                + " PRIMARY KEY (RAE, equipamento, combustivel, horaInicio) , "
                + " FOREIGN KEY (RAE ) "
                + " REFERENCES RAE (idRAE), "
                + " FOREIGN KEY (equipamento) "
                + " REFERENCES equipamentos (idEquipamento ), "
                + " FOREIGN KEY (codOperador ) "
                + " REFERENCES usuarios (codUsuario ), "
                + " FOREIGN KEY (codAbastecedor ) "
                + " REFERENCES usuarios (codUsuario ), "
                + " FOREIGN KEY(frentesObra, atividade) REFERENCES frentesObraAtividade(frentesObra, atividade),   "
                + " FOREIGN KEY(justificativa) REFERENCES justificativaOperador(idJustificativaOperador),   "
                + " FOREIGN KEY(ccObra) REFERENCES obras(idObra),   "
                + " FOREIGN KEY (combustivel ) "
                + " REFERENCES combustiveisLubrificantes (idCombustivelLubrificante )); ";

    }

    private static String createTableAbastecimentosTemp() {
        return " create table if not exists abastecimentosTemp ( "
                + " equipamento integer not null , "
                + " dataHora text not null , "
                + " codOperador integer null , "
                + " horimetro text null , "
                + " quilometragem text null , "
                + " PRIMARY KEY (equipamento, dataHora) , "
                + " FOREIGN KEY (equipamento) "
                + " REFERENCES equipamentos (idEquipamento ), "
                + " FOREIGN KEY (codOperador ) "
                + " REFERENCES usuarios (codUsuario ) ); ";
    }

    private static String createTableAbastecimenosPosto() {
        return " create table if not exists abastecimentosPosto ( "
                + " combustivel integer not null , "
                + " posto integer not null , "
                + " data text not null , "
                + " hora text not null , "
                + " quantidade text not null , "
                + " posto2 integer null , "
                + " tipo text null , "
                + " PRIMARY KEY (combustivel, posto, data, hora) , "
                + " FOREIGN KEY (posto) "
                + " REFERENCES postos (idPosto), "
                + " FOREIGN KEY (combustivel ) "
                + " REFERENCES combustiveisLubrificantes (idCombustivelLubrificante )); ";
    }

    private static String createTableLubrificacoesDetalhes() {
        return " create table if not exists lubrificacoesDetalhes ( "
                + " RAE integer not null , "
                + " equipamento integer not null , "
                + " horaInicio text not null , "
                + " lubrificante integer not null , "
                + " compartimento integer not null , "
                + " categoria integer not null , "
                + " quantidade text null , "
                + " observacoes text null , "
                + " PRIMARY KEY (RAE, equipamento, horaInicio, lubrificante, compartimento, categoria) , "
                + " FOREIGN KEY (compartimento , categoria ) "
                + " REFERENCES compartimentos (idCompartimento , idCategoria ), "
                + " FOREIGN KEY (RAE , equipamento , horaInicio , lubrificante ) "
                + " REFERENCES abastecimentos (RAE , equipamento , horaInicio , combustivel) on delete cascade); ";
    }

    private static String createTableJustificavasOperador() {
        return " create table if not exists justificativasOperador( "
                + " idJustificativaOperador integer primary key,  "
                + " descricao text not null ); ";
    }

    /**
     * Tabelas de Apropriação de Serviço/Mão-de-obra **
     */

    private static String createTablePessoal() {
        return "CREATE TABLE IF NOT EXISTS pessoal (" +
                "  idPessoal INTEGER NOT NULL," +
                "  nome TEXT NOT NULL," +
                "  matricula TEXT NOT NULL," +
                "  PRIMARY KEY (idPessoal)" +
                "  );";
    }

    private static String createTableControleFrequencia() {
        return "CREATE TABLE IF NOT EXISTS controleFrequencia (" +
                "  ccObra INTEGER NOT NULL," +
                "  pessoa INTEGER NOT NULL," +
                "  horaEntrada TEXT NOT NULL," +
                "  horaSaida TEXT NULL," +
                "  equipe INTEGER NULL," +
                "  observacoes TEXT NULL," +
                "  PRIMARY KEY (ccObra, pessoa, horaEntrada)," +
                "  CONSTRAINT fk_controleFrequencia_obras1" +
                "    FOREIGN KEY (ccObra)" +
                "    REFERENCES obras (idObra)" +
                "    ," +
                "  CONSTRAINT fk_controleFrequencia_pessoal1" +
                "    FOREIGN KEY (pessoa)" +
                "    REFERENCES pessoal (idPessoal)" +
                "    ," +
                "  CONSTRAINT fk_controleFrequencia_equipesTrabalho1" +
                "    FOREIGN KEY (equipe)" +
                "    REFERENCES equipesTrabalho (idEquipe)" +
                "    );";
    }

    private static String createTablePeriodosHorariosTrabalho() {
        return "CREATE TABLE IF NOT EXISTS periodosHorariosTrabalho (" +
                "  horario INTEGER NOT NULL," +
                "  diaSemana TEXT NOT NULL," +
                "  horaInicio TEXT NOT NULL," +
                "  horaTermino TEXT NOT NULL," +
                "  produtivo TEXT NULL," +
                "  cobraHoraExtra TEXT NULL," +
                "  codigoParalisacao TEXT NULL," +
                "  PRIMARY KEY (horario, diaSemana, horaInicio)," +
                "  CONSTRAINT fk_periodosHorariosTrabalho_horariosTrabalho1" +
                "    FOREIGN KEY (horario)" +
                "    REFERENCES horariosTrabalho (idHorario)" +
                "    );";
    }

    private static String createTableHorariosTrabalho() {
        return "CREATE TABLE IF NOT EXISTS horariosTrabalho (" +
                "  idHorario INTEGER NOT NULL," +
                "  ccObra INTEGER NOT NULL," +
                "  descricao TEXT NULL," +
                "  horaInicio TEXT NULL," +
                "  horaTermino TEXT NULL," +
                "  PRIMARY KEY (idHorario)," +
                "  CONSTRAINT fk_horariosTrabalho_obras1" +
                "    FOREIGN KEY (ccObra)" +
                "    REFERENCES obras (idObra)" +
                "    );";
    }

    private static String createTableEquipesTrabalho() {
        return "CREATE TABLE IF NOT EXISTS equipesTrabalho (" +
                "  idEquipe INTEGER NOT NULL," +
                "  ccObra INTEGER NOT NULL," +
                "  nomeEquipe TEXT NOT NULL," +
                "  apelido TEXT NULL," +
                "  formacao INTEGER NULL," +
                "  dataCriacao TEXT NULL," +
                "  responsavel INTEGER NULL," +
                "  ativa TEXT NOT NULL," +
                "  apropriavel TEXT NOT NULL," +
                "  horarioTrabalho INTEGER NULL," +
                "  PRIMARY KEY (idEquipe)," +
                "  CONSTRAINT fk_equipesTrabalho_pessoal1" +
                "    FOREIGN KEY (responsavel)" +
                "    REFERENCES pessoal (idPessoal)," +
                "  CONSTRAINT fk_equipesTrabalho_horariosTrabalho1" +
                "    FOREIGN KEY (horarioTrabalho)" +
                "    REFERENCES horariosTrabalho (idHorario)," +
                "  CONSTRAINT fk_equipesTrabalho_obras1" +
                "    FOREIGN KEY (ccObra)" +
                "    REFERENCES obras (idObra)" +
                "    );";
    }

    private static String createTableEquipamentosEquipe() {
        return "CREATE TABLE IF NOT EXISTS equipamentosEquipe (" +
                "  equipe INTEGER NOT NULL," +
                "  equipamento INTEGER NOT NULL," +
                "  dataIngresso TEXT NOT NULL," +
                "  dataSaida TEXT NULL," +
                "  servicoPadrao INTEGER NULL," +
                "  PRIMARY KEY (equipe, equipamento, dataIngresso)," +
                "  CONSTRAINT fk_equipamentosEquipe_equipesTrabalho1" +
                "    FOREIGN KEY (equipe)" +
                "    REFERENCES equipesTrabalho (idEquipe)," +
                "  CONSTRAINT fk_equipamentosEquipe_equipamentos1" +
                "    FOREIGN KEY (equipamento)" +
                "    REFERENCES equipamentos (idEquipamento)," +
                "  CONSTRAINT fk_equipamentosEquipe_servicos1" +
                "    FOREIGN KEY (servicoPadrao)" +
                "    REFERENCES servicos (idServico));";
    }

    private static String createTableIntegrantesTemp() {
        return "CREATE TABLE IF NOT EXISTS integrantesTemp (" +
                "  equipe INTEGER NOT NULL," +
                "  pessoa INTEGER NOT NULL," +
                "  dataIngresso TEXT NOT NULL," +
                "  dataSaida TEXT NULL," +
                "  PRIMARY KEY (equipe, pessoa, dataIngresso)," +
                "  CONSTRAINT fk_integranteTemp_equipesTrabalho1" +
                "    FOREIGN KEY (equipe)" +
                "    REFERENCES equipesTrabalho (idEquipe)" +
                "    ," +
                "  CONSTRAINT fk_integranteTemp_pessoal1" +
                "    FOREIGN KEY (pessoa)" +
                "    REFERENCES pessoal (idPessoal)" +
                "    );";
    }

    private static String createTableIntegrantesEquipe() {
        return "CREATE TABLE IF NOT EXISTS integrantesEquipe (" +
                "  equipe INTEGER NOT NULL," +
                "  pessoa INTEGER NOT NULL," +
                "  dataIngresso TEXT NOT NULL," +
                "  dataSaida TEXT NULL," +
                "  PRIMARY KEY (equipe, pessoa, dataIngresso)," +
                "  CONSTRAINT fk_integrantesEquipe_equipesTrabalho1" +
                "    FOREIGN KEY (equipe)" +
                "    REFERENCES equipesTrabalho (idEquipe)" +
                "    ," +
                "  CONSTRAINT fk_integrantesEquipe_pessoal1" +
                "    FOREIGN KEY (pessoa)" +
                "    REFERENCES pessoal (idPessoal)" +
                "    );";
    }

    private static String createTableApropriacaoServico() {
        return "CREATE TABLE IF NOT EXISTS apropriacaoServico (" +
                "  idApropriacao INTEGER NOT NULL," +
                "  equipe INTEGER NOT NULL," +
                "  idServico INTEGER NOT NULL," +
                "  quantidadeProduzida DECIMAL(8,2) NOT NULL," +
                "  horaIni TEXT NOT NULL," +
                "  horaFim TEXT NULL," +
                "  observacoes TEXT NULL," +
                "  PRIMARY KEY (idApropriacao, equipe, idServico, horaIni)," +
                "  CONSTRAINT fk_ApropriacaoServico_apropriacoes1" +
                "    FOREIGN KEY (idApropriacao)" +
                "    REFERENCES apropriacoes (idApropriacao) ON DELETE CASCADE" +
                "    ," +
                "  CONSTRAINT fk_apropriacaoServico_equipesTrabalho1" +
                "    FOREIGN KEY (equipe)" +
                "    REFERENCES equipesTrabalho (idEquipe)" +
                "    ," +
                "  CONSTRAINT fk_apropriacaoServico_servicos1" +
                "    FOREIGN KEY (idServico)" +
                "    REFERENCES servicos (idServico)" +
                "    );";
    }

    private static String createTableParalisacoesEquipe() {
        return "CREATE TABLE IF NOT EXISTS paralisacoesEquipe (" +
                "  apropriacao INTEGER NOT NULL," +
                "  equipe INTEGER NOT NULL," +
                "  idParalisacao INTEGER NOT NULL," +
                "  servico INTEGER NULL," +
                "  horaInicio TEXT NOT NULL," +
                "  horaTermino TEXT NOT NULL," +
                "  observacoes TEXT NULL," +
                "  PRIMARY KEY (apropriacao, equipe, idParalisacao, horaInicio)," +
                "  CONSTRAINT fk_paralisacoesEquipe_apropriacoes1" +
                "    FOREIGN KEY (apropriacao)" +
                "    REFERENCES apropriacoes (idApropriacao)" +
                "    ," +
                "  CONSTRAINT fk_paralisacoesEquipe_equipesTrabalho1" +
                "    FOREIGN KEY (equipe)" +
                "    REFERENCES equipesTrabalho (idEquipe)" +
                "    ," +
                "  CONSTRAINT fk_paralisacoesEquipe_paralisacoes1" +
                "    FOREIGN KEY (idParalisacao)" +
                "    REFERENCES paralisacoes (idParalisacao)" +
                "    );";
    }

    private static String createTableAtividades() {
        return "CREATE TABLE IF NOT EXISTS atividades (" +
                "  idAtividade INTEGER NOT NULL," +
                "  descricao TEXT NULL," +
                "  PRIMARY KEY (idAtividade));";
    }

    private static String createTableAtividadesServicos() {
        return "CREATE TABLE IF NOT EXISTS atividadesServicos (" +
                "  idAtividadeServico INTEGER NOT NULL," +
                "  atividade INTEGER NULL," +
                "  servico INTEGER NULL," +
                "  descricao TEXT NULL," +
                "  ordem INTEGER NULL," +
                "  codigoPrevix TEXT NULL," +
                "  atividadePai INTEGER NULL," +
                "  PRIMARY KEY (idAtividadeServico)," +
                "  CONSTRAINT fk_atividadesServicos_atividades1" +
                "    FOREIGN KEY (atividade)" +
                "    REFERENCES atividades (idAtividade)" +
                "    ," +
                "  CONSTRAINT fk_atividadesServicos_servicos1" +
                "    FOREIGN KEY (servico)" +
                "    REFERENCES servicos (idServico)" +
                "    ," +
                "  CONSTRAINT fk_atividadesServicos_atividadesServicos1" +
                "    FOREIGN KEY (atividadePai)" +
                "    REFERENCES atividadesServicos (idAtividadeServico)" +
                "    );";
    }

    private static String createTableApropriacoesMaoObra() {
        return "CREATE TABLE IF NOT EXISTS apropriacoesMaoObra (" +
                "  apropriacao INTEGER NOT NULL," +
                "  pessoa INTEGER NOT NULL," +
                "  horaInicio TEXT NOT NULL," +
                "  horaTermino TEXT NULL," +
                "  equipe INTEGER NOT NULL," +
                "  servico INTEGER NOT NULL," +
                "  observacoes TEXT NULL," +
                "  PRIMARY KEY (apropriacao, pessoa, horaInicio)," +
                "  CONSTRAINT fk_apropriacoesMaoObra_apropriacoes1" +
                "    FOREIGN KEY (apropriacao)" +
                "    REFERENCES apropriacoes (idApropriacao) on DELETE CASCADE," +
                "  CONSTRAINT fk_apropriacoesMaoObra_pessoal1" +
                "    FOREIGN KEY (pessoa)" +
                "    REFERENCES pessoal (idPessoal)," +
                "  CONSTRAINT fk_apropriacoesMaoObra_equipesTrabalho1" +
                "    FOREIGN KEY (equipe)" +
                "    REFERENCES equipesTrabalho (idEquipe)" +
                "    );";
    }

    private static String createTableParalisacoesMaoObra() {
        return "CREATE TABLE IF NOT EXISTS paralisacoesMaoObra (" +
                "  ccObra INTEGER NOT NULL," +
                "  pessoa INTEGER NOT NULL," +
                "  horaInicio TEXT NOT NULL," +
                "  horaTermino TEXT NULL," +
                "  apropriacao INTEGER NOT NULL," +
                "  paralisacao INTEGER NOT NULL," +
                "  equipe INTEGER NOT NULL," +
                "  servico INTEGER NULL," +
                "  observacoes TEXT NULL," +
                "  PRIMARY KEY (ccObra, pessoa, horaInicio)," +
                "  CONSTRAINT fk_paralisacoesMaoObra_obras1" +
                "    FOREIGN KEY (ccObra)" +
                "    REFERENCES obras (idObra)" +
                "    ," +
                "  CONSTRAINT fk_paralisacoesMaoObra_pessoal1" +
                "    FOREIGN KEY (pessoa)" +
                "    REFERENCES pessoal (idPessoal)" +
                "    ," +
                "  CONSTRAINT fk_paralisacoesMaoObra_paralisacoes1" +
                "    FOREIGN KEY (paralisacao)" +
                "    REFERENCES paralisacoes (idParalisacao)" +
                "    ," +
                "  CONSTRAINT fk_paralisacoesMaoObra_equipesTrabalho1" +
                "    FOREIGN KEY (equipe)" +
                "    REFERENCES equipesTrabalho (idEquipe)" +
                "    );";
    }

    /**
     * Tabela auxiliar
     * So existe no Tablet
     *
     * @return
     */
    private static String createTableEventoEquipe() {
        return "CREATE TABLE IF NOT EXISTS eventoEquipe (" +
                "  idEventoEquipe INTEGER PRIMARY KEY autoincrement," +
                "  localizacao INTEGER NOT NULL," +
                "  equipe INTEGER NOT NULL," +
                "  paralisacao INTEGER NOT NULL," +
                "  tipoHorario INTEGER NOT NULL," +
                "  data TEXT NOT NULL," +
                "  horaIni TEXT NOT NULL," +
                "  horaFim TEXT NULL," +
                "  observacao TEXT NULL," +
                "  servico INTEGER NULL," +
                "  apropriacao INTEGER NULL);";
    }

    private static String createTableAusencia() {
        return "CREATE TABLE IF NOT EXISTS ausencia (" +
                "  equipe INTEGER NOT NULL," +
                "  pessoa INTEGER NOT NULL," +
                "  data TEXT NOT NULL," +
                "  PRIMARY KEY (equipe, pessoa, data)," +
                "    CONSTRAINT fk_ausencia_pessoal1" +
                "    FOREIGN KEY (pessoa)" +
                "    REFERENCES pessoal (idPessoal)" +
                "    ON DELETE NO ACTION" +
                "    ON UPDATE NO ACTION," +
                "  CONSTRAINT fk_ausencia_equipesTrabalho1" +
                "    FOREIGN KEY (equipe)" +
                "    REFERENCES equipesTrabalho (idEquipe)" +
                "    ON DELETE NO ACTION" +
                "    ON UPDATE NO ACTION); ";
    }

    private static String createTablePrevisaoServico() {
        return "CREATE TABLE IF NOT EXISTS previsaoServico (" +
                "  ccObra INTEGER NOT NULL," +
                "  frenteObra INTEGER NOT NULL," +
                "  atividade INTEGER NOT NULL," +
                "  servico INTEGER NOT NULL," +
                "  PRIMARY KEY (ccObra, frenteObra, atividade, servico)," +
                "  CONSTRAINT fk_previsaoServico_atividades1" +
                "    FOREIGN KEY (atividade)" +
                "    REFERENCES atividades (idAtividade)" +
                "    ON DELETE NO ACTION" +
                "    ON UPDATE NO ACTION," +
                "  CONSTRAINT fk_previsaoServico_servicos1" +
                "    FOREIGN KEY (servico)" +
                "    REFERENCES servicos (idServico)" +
                "    ON DELETE NO ACTION" +
                "    ON UPDATE NO ACTION," +
                "  CONSTRAINT fk_previsaoServico_frentesObra1" +
                "    FOREIGN KEY (frenteObra)" +
                "    REFERENCES frentesObra (idFrentesObra)" +
                "    ON DELETE NO ACTION" +
                "    ON UPDATE NO ACTION," +
                "  CONSTRAINT fk_previsaoServico_obras1" +
                "    FOREIGN KEY (ccObra)" +
                "    REFERENCES obras (idObra)" +
                "    ON DELETE NO ACTION" +
                "    ON UPDATE NO ACTION);";
    }

    private static String createIndexes() {
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
