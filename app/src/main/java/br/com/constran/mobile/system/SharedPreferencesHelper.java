package br.com.constran.mobile.system;

import android.content.Context;
import android.content.SharedPreferences;



public class SharedPreferencesHelper {


   public static class Configuracao{

        public static Context CONTEXT = ApplicationInit.CONTEXT;

        private static final String PREFS_NAME = "Cofiguracao";
        private static final String COD_USUARIO = "COD_USUARIO";
        private static final String ID_USUARIO = "ID_USUARIO";
        private static final String ID_USUARIO_PESSOAL = "ID_USUARIO_PESSOAL";
        private static final String NOME_USUARIO = "NOME_USUARIO";
        private static final String NOME_POSTO = "NOME_POSTO";
        private static final String ID_OBRA = "ID_OBRA";
        private static final String ID_OBRA_2 = "ID_OBRA_2";
        private static final String DISPOSITIVO = "DISPOSITIVO";
        private static final String SERVIDOR = "SERVIDOR";
        private static final String PORTA_WEB = "PORTA_WEB";
        private static final String PORTA_FTP = "PORTA_FTP";
        private static final String ETIKET = "ETIKET";
        private static final String TOLERANCIA = "TOLERANCIA";
        private static final String REFERENCIA = "REFERENCIA";
        private static final String ID_POSTO = "ID_POSTO";
        private static final String DURACAO = "DURACAO";
        private static final String ATUAL = "ATUAL";

        //COD_USUARIO----------------------------------------------------------------------------------
        public static Integer getCodUsuario () {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            return settings.getInt(COD_USUARIO, 0);
        }

        public static void setCodUsuario(Integer codUsuario) {
            if(codUsuario == null){
                codUsuario = 0;
            }
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(COD_USUARIO, codUsuario);
            editor.commit();
        }
        //--------------------------------------------------------------------------------------------


        //ID_USUARIO----------------------------------------------------------------------------------
        public static Integer getIdUsuario () {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            return settings.getInt(ID_USUARIO, 0);
        }

        public static void setIdUsuario(Integer idUsuario) {
            if(idUsuario == null){
                idUsuario = 0;
            }
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(ID_USUARIO, idUsuario);
            editor.commit();
        }
        //--------------------------------------------------------------------------------------------


        //ID_USUARIO_PESSOAL--------------------------------------------------------------------------
        public static Integer getIdUsuarioPessoal () {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            return settings.getInt(ID_USUARIO_PESSOAL, 0);
        }

        public static void setIdUsuarioPessoal(Integer idUsuarioPessoal) {
            if(idUsuarioPessoal == null){
                idUsuarioPessoal = 0;
            }
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(ID_USUARIO_PESSOAL, idUsuarioPessoal);
            editor.commit();
        }
        //--------------------------------------------------------------------------------------------

        //NOME_USUARIO--------------------------------------------------------------------------------
        public static String getNomeUsuario () {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            return settings.getString(NOME_USUARIO, "");
        }

        public static void setNomeUsuario(String nomeUsuario) {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(NOME_USUARIO, nomeUsuario);
            editor.commit();
        }
        //--------------------------------------------------------------------------------------------


        //NOME_POSTO----------------------------------------------------------------------------------
        public static String getNomePosto() {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            return settings.getString(NOME_POSTO,"");
        }

        public static void setNomePosto(String nomePosto) {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(NOME_POSTO, nomePosto);
            editor.commit();
        }
        //--------------------------------------------------------------------------------------------


        //ID_OBRA-------------------------------------------------------------------------------------
        public static Integer getIdObra () {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            return settings.getInt(ID_OBRA, 000000);
        }

        public static void setIdObra(Integer idObra) {
            if(idObra == null){
                idObra = 000000;
            }
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(ID_OBRA, idObra);
            editor.commit();
        }
        //----------------------------------------------------------------------------------------------


        //ID_OBRA_2-------------------------------------------------------------------------------------
        public static Integer getIdObra2 () {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            return settings.getInt(ID_OBRA_2, 000000);
        }

        public static void setIdObra2(Integer idObra2) {
            if(idObra2 == null){
                idObra2 = 0;
            }
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(ID_OBRA_2, idObra2);
            editor.commit();
        }
        //--------------------------------------------------------------------------------------------


        //DISPOSITIVO---------------------------------------------------------------------------------
        public static String getDispositivo () {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            return settings.getString(DISPOSITIVO,"T00");
        }

        public static void setDispositivo(String dispositivo) {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(DISPOSITIVO, dispositivo);
            editor.commit();
        }
        //---------------------------------------------------------------------------------------------


        //SERVIDOR-------------------------------------------------------------------------------------
        public static String getServidor () {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            return settings.getString(SERVIDOR,"mobile.constran.com.br");
        }

        public static void setServidor(String servidor) {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(SERVIDOR, servidor);
            editor.commit();
        }
        //----------------------------------------------------------------------------------------------


        //PORTA_WEB-------------------------------------------------------------------------------------
        public static String getPortaWeb () {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            return settings.getString(PORTA_WEB,"8080");
        }

        public static void setPortaWeb(String portaWeb) {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(PORTA_WEB, portaWeb);
            editor.commit();
        }
        //----------------------------------------------------------------------------------------------


        //PORTA_FTP-------------------------------------------------------------------------------------
        public static String getPortaFtp () {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            return settings.getString(PORTA_FTP,"21");
        }

        public static void setPortaFtp(String portaFtp) {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(PORTA_FTP, portaFtp);
            editor.commit();
        }
        //---------------------------------------------------------------------------------------------


        //ETIKET----------------------------------------------------------------------------------------
        public static String getEtiket () {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            return settings.getString(ETIKET,"Origem");
        }

        public static void setEtiket(String etiket) {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(ETIKET, etiket);
            editor.commit();
        }
        //-----------------------------------------------------------------------------------------------


        //TOLERANCIA-------------------------------------------------------------------------------------
        public static Integer getTolerancia () {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            return settings.getInt(TOLERANCIA, 100);
        }

        public static void setTolerancia(Integer tolerancia) {
            if(tolerancia == null){
                tolerancia = 100;
            }
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(TOLERANCIA, tolerancia);
            editor.commit();
        }
        //-----------------------------------------------------------------------------------------------


        //REFERENCIA-------------------------------------------------------------------------------------
        public static String getReferencia () {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            return settings.getString(REFERENCIA, "210419");
        }

        public static void setReferencia(String referencia) {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(REFERENCIA, referencia);
            editor.commit();
        }
        //----------------------------------------------------------------------------------------------


        //ID_POSTO--------------------------------------------------------------------------------------
        public static Integer getIdPosto () {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            return settings.getInt(ID_POSTO, 0);
        }

        public static void setIdPosto(Integer idPosto) {
            if(idPosto == null){
                idPosto = 0;
            }
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(ID_POSTO, idPosto);
            editor.commit();
        }
        //----------------------------------------------------------------------------------------------

        //DURACAO---------------------------------------------------------------------------------------
        public static String getDuracao () {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            return settings.getString(DURACAO,"0");
        }

        public static void setDuracao(String duracao) {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(DURACAO, duracao);
            editor.commit();
        }
        //----------------------------------------------------------------------------------------------

        //ATUAL-----------------------------------------------------------------------------------------
        public static String getAtual () {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            return settings.getString(ATUAL,"");
        }

        public static void setAtual(String atual) {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(ATUAL, atual);
            editor.commit();
        }
        //----------------------------------------------------------------------------------------------
    }

    public static class AppModulo{

        private static final String MOVIMENTACOES = "MOVIMENTACOES";
        private static final String EQUIPAMENTOS = "EQUIPAMENTOS";
        private static final String MAO_DE_OBRA = "MAO_DE_OBRA";
        private static final String ABASTECIMENTOS = "ABASTECIMENTOS";
        private static final String INDICES_PLUVIOMETRICOS = "INDICES_PLUVIOMETRICOS";
        private static final String MANUTENCOES = "MANUTENCOES";
        public static Context CONTEXT = ApplicationInit.CONTEXT;
        private static final String PREFS_NAME = "Modulo";

        public static boolean estaMovimentacoesAtivado () {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            return settings.getBoolean(MOVIMENTACOES,true);
        }

        public static void setMovimentacoesAtivado(boolean ativado) {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(MOVIMENTACOES, ativado);
            editor.commit();
        }

        public static boolean estaEquipamentosAtivado () {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            return settings.getBoolean(EQUIPAMENTOS,true);
        }

        public static void setEquipamentosAtivado(boolean ativado) {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(EQUIPAMENTOS, ativado);
            editor.commit();
        }

        public static boolean estaMaoDeObraAtivado () {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            return settings.getBoolean(MAO_DE_OBRA,true);
        }

        public static void setMaoDeObraAtivado(boolean ativado) {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(MAO_DE_OBRA, ativado);
            editor.commit();
        }

        public static boolean estaAbastecimentosAtivado () {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            return settings.getBoolean(ABASTECIMENTOS,true);
        }

        public static void setAbastecimentosAtivado(boolean ativado) {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(ABASTECIMENTOS, ativado);
            editor.commit();
        }

        public static boolean estaIndicesPluviometricosAtivado () {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            return settings.getBoolean(INDICES_PLUVIOMETRICOS,true);
        }

        public static void setIndicesPluviometricosAtivado(boolean ativado) {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(INDICES_PLUVIOMETRICOS, ativado);
            editor.commit();
        }

        public static boolean estaManutencoesAtivado () {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            return settings.getBoolean(MANUTENCOES,false);
        }

        public static void setManutencoesAtivado(boolean ativado) {
            SharedPreferences settings = CONTEXT.getSharedPreferences(PREFS_NAME, CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(MANUTENCOES, ativado);
            editor.commit();
        }
        //----------------------------------------------------------------------------------------------
    }
}
