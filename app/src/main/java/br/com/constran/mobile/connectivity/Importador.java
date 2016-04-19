package br.com.constran.mobile.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.constran.mobile.R;
import br.com.constran.mobile.persistence.dao.DAOFactory;
import br.com.constran.mobile.persistence.vo.EquipamentoCategoriaVO;
import br.com.constran.mobile.persistence.vo.ManutencaoServicoPorCategoriaEquipamentoVO;
import br.com.constran.mobile.persistence.vo.ManutencaoServicosVO;
import br.com.constran.mobile.persistence.vo.ObraVO;
import br.com.constran.mobile.persistence.vo.aprop.PrevisaoServicoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.EquipeTrabalhoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.HorarioTrabalhoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.IntegranteEquipeVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.PeriodoHorarioTrabalhoVO;
import br.com.constran.mobile.persistence.vo.imp.AtividadeVO;
import br.com.constran.mobile.persistence.vo.imp.ComponenteVO;
import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;
import br.com.constran.mobile.persistence.vo.imp.FrenteObraVO;
import br.com.constran.mobile.persistence.vo.imp.LubrificacaoEquipamentoVO;
import br.com.constran.mobile.persistence.vo.imp.MaterialVO;
import br.com.constran.mobile.persistence.vo.imp.OrigemDestinoVO;
import br.com.constran.mobile.persistence.vo.imp.ParalisacaoVO;
import br.com.constran.mobile.persistence.vo.imp.PreventivaEquipamentoVO;
import br.com.constran.mobile.persistence.vo.imp.ServicoVO;
import br.com.constran.mobile.persistence.vo.imp.UsuarioVO;
import br.com.constran.mobile.persistence.vo.imp.json.ImportMobile;
import br.com.constran.mobile.persistence.vo.rae.abs.CombustivelLubrificanteVO;
import br.com.constran.mobile.persistence.vo.rae.abs.CombustivelPostoVO;
import br.com.constran.mobile.persistence.vo.rae.abs.CompartimentoVO;
import br.com.constran.mobile.persistence.vo.rae.abs.JustificativaOperadorVO;
import br.com.constran.mobile.persistence.vo.rae.abs.PostoVO;
import br.com.constran.mobile.system.AppDirectory;
import br.com.constran.mobile.system.NetworkUtils;
import br.com.constran.mobile.system.SharedPreferencesHelper;
import br.com.constran.mobile.view.util.Util;

/**
 * Created by moises_santana on 22/05/2015.
 */
public class Importador {

    Context context;
    private ProgressDialog progressI;
    private final Integer PROGRESS_INCREMENT = 3;
    private ImportMobile importMobileVO = null;
    private StringBuilder msgDialog = new StringBuilder();
    private boolean houveErroHttp = false;
    private boolean faltaDados = false;

    public Importador(Context context){
        this.context = context;
    }

    public DAOFactory getDAO(){
        return DAOFactory.getInstance(context);
    }

    public boolean naoPodeImportar(){

        List<String> datas = getDAO().getUtilDAO().getDatesToExport();

        if (datas.size() > 0) {
            return true;
        }else{
            return false;
        }
    }

    public class DadosParaOtablet extends AsyncTask<Void, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressI = new ProgressDialog(context);
            progressI.setTitle(context.getResources().getString(R.string.AGUARDE));
            progressI.setMessage(context.getResources().getString(R.string.EM_ANDAMENTO));
            progressI.setCancelable(false);
            progressI.setIndeterminate(false);
            progressI.setProgress(0);
            progressI.setMax(100);
            progressI.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            progressI.show();

            addProgress("Verificando conexão com a internet...");
            if(NetworkUtils.isNetworkUnavailable()){
                msgDialog.append("Necessário uma conexão ativa com a internet.\n");
                cancel(true);
            }

            addProgress("Verificando se há apontamentos pendentes para exportação...");
            if(naoPodeImportar()){
                msgDialog.append("Ainda há apontamentos para serem exportados.\nVocê não pode importar novos dados.\n");
                cancel(true);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if(isCancelled()) {
                return null;
            }

            AppHTTP http = new AppHTTP();

            Map<String, String> params = new HashMap<String, String>();
            params.put("ccObra", String.valueOf(SharedPreferencesHelper.Configuracao.getIdObra()));

            try {

                addProgress("Obtendo dados remotos...");

                //sera desativado***
                //importMobileVO = http.connect(AppHTTP.GET, AppDirectory.getURLserver(), ImportMobile.class, params);

                //importMobileVO = http.connect(AppHTTP.GET, "http://200.182.0.68:8080/samcentral/ws/importa/dadosParaOtablet?ccObra=303599",ImportMobile.class);
                //Log.i("LOG",AppDirectory.getImportaDadosRESTpath());

                //este é o novo que sera posto em produção em breve
                importMobileVO = http.connect(AppHTTP.GET, AppDirectory.getImportaDadosRESTpath(),ImportMobile.class,params);

                addProgress("Limpando tabelas...");
                getDAO().getUtilDAO().clearTables(true, true, false);

                salvarImportacao();


            } catch (NullPointerException e) {

                houveErroHttp = true;
                e.printStackTrace();
                msgDialog.append("Objeto de importação está nulo.")
                        .append("\n").append("Mantenha a calma e tente novamente.")
                        .append("\n\n").append("Detalhes/Causa:")
                        .append("\n").append(e.getMessage()).append("\n")
                        .append("HTTP Status: ").append(http.getResponseCode());
            } catch (SocketTimeoutException e) {

                houveErroHttp = true;
                e.printStackTrace();
                msgDialog.append("O servidor não respondeu.")
                        .append("\n").append("Sua internet ou o serviço remoto estão apresentando lentidão. Tente novamente.")
                        .append("\n\n").append("Detalhes/Causa:")
                        .append("\n").append(e.getMessage());
            } catch (IOException e) {

                houveErroHttp = true;
                e.printStackTrace();
                msgDialog.append("Não foi possível processar a requisição.");
                msgDialog.append("\n");
                msgDialog.append("Sua conexão com a internet ou o servidor remoto podem estar com problemas.");
                msgDialog.append("\n");
                msgDialog.append("Falha de comunicação em rede. Tente novamente.");
                msgDialog.append("\n\n").append("Detalhes/Causa:").append("\n").append(e.getMessage());
            }

            return null;

        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
            onPostExecute(null);
        }

        public void addProgress(String newMessage) {
            if(progressI.getProgress() < progressI.getMax()){
                publishProgress(String.valueOf(progressI.getProgress()+PROGRESS_INCREMENT),newMessage);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            progressI.setMessage(values[1]);
            progressI.setProgress(Integer.valueOf(values[0]));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressI.dismiss();

            if(houveErroHttp || faltaDados){

                if(faltaDados)msgDialog.append("\n\n").append("Nenhum dado foi importado. Por favor entre em contato com o suporte técnico.");

                Util.viewErrorMessage(context,msgDialog.toString());
            }
            else{
                Util.viewMessage(context,msgDialog.toString());
            }
        }

        public void salvarImportacao(){

            //EFETUANDO VALIDACAO...

           Integer idObra = SharedPreferencesHelper.Configuracao.getIdObra();
           Integer idObra2 = SharedPreferencesHelper.Configuracao.getIdObra2();


            boolean ativoMovimentacao = SharedPreferencesHelper.AppModulo.estaMovimentacoesAtivado();
            boolean ativoParteDiariaEquipa = SharedPreferencesHelper.AppModulo.estaEquipamentosAtivado();
            boolean ativoMaoDeObra = SharedPreferencesHelper.AppModulo.estaMaoDeObraAtivado();
            boolean ativoAbastecimento = SharedPreferencesHelper.AppModulo.estaAbastecimentosAtivado();
            boolean ativoManutencao = SharedPreferencesHelper.AppModulo.estaManutencoesAtivado();

            Log.i("manutencao ativo",ativoManutencao+"");
            Log.i("ativoParteDiariaEquipa",ativoParteDiariaEquipa+"");
            Log.i("ativoParteDiariaEquipa",ativoParteDiariaEquipa+"");



            //modulos de movimentacao ou parte diaria de equipamento ou mao de obra
            if(ativoMovimentacao || ativoParteDiariaEquipa || ativoMaoDeObra) {

                addProgress("Validando Origens e Destinos...");
                for (ObraVO obra : importMobileVO.getObras()) {
                    if (idObra.equals(obra.getId()) || idObra2 != 0 && idObra2.equals(obra.getId())) {
                        if ("S".equalsIgnoreCase(obra.getUsaOrigemDestino())) {
                            if (importMobileVO.getOrigensDestinos() == null || importMobileVO.getOrigensDestinos().isEmpty()) {
                                faltaDados = true;
                                msgDialog.append("\n- Origem/ Destinos sem registros");
                            }
                        }
                    }
                }

                addProgress("Validando frente obras...");
                if (importMobileVO.getFrentesObra() == null || importMobileVO.getFrentesObra().isEmpty()) {
                    faltaDados = true;
                    msgDialog.append("- Frente Obras sem registros");
                }

                addProgress("Validando Atividades...");
                if (importMobileVO.getAtividades() == null || importMobileVO.getAtividades().isEmpty()) {
                    faltaDados = true;
                    msgDialog.append("\n- Atividades sem registros");
                }


                addProgress("Validando Materiais...");
                if (importMobileVO.getMateriais() == null || importMobileVO.getMateriais().isEmpty()) {
                    faltaDados = true;
                    msgDialog.append("\n- Materiais sem registros");
                }


                addProgress("Validando Serviços...");
                if (importMobileVO.getServicos() == null || importMobileVO.getServicos().isEmpty()) {
                    faltaDados = true;
                    msgDialog.append("\n- Servicos sem registros");
                }

                addProgress("Validando Componentes...");
                if (importMobileVO.getComponentes() == null || importMobileVO.getComponentes().isEmpty()) {
                    faltaDados = true;
                    msgDialog.append("\n- Componentes sem registros");
                }

                addProgress("Validando Paralisações...");
                if (importMobileVO.getParalisacoes() == null || importMobileVO.getParalisacoes().isEmpty()) {
                    faltaDados = true;
                    msgDialog.append("\n- Paralisações sem registros");
                }

            }

            //modulos de abastecimento ou parte diaria de equipamento ou movimentacao
            if(ativoAbastecimento || ativoParteDiariaEquipa || ativoMovimentacao){

                addProgress("Validando equipamentos...");
                if (importMobileVO.getEquipamentos() == null || importMobileVO.getEquipamentos().isEmpty()) {
                    faltaDados = true;
                    msgDialog.append("\n- Equipamentos sem registros");
                }

            }


            //modulo de manutencao
            if(ativoManutencao){

                addProgress("Validandos Equipamentos Categorias...");
                if(importMobileVO.getEquipamentoCategorias() == null || importMobileVO.getEquipamentoCategorias().isEmpty()) {
                    faltaDados = true;
                    msgDialog.append("\n- Equipamento Categorias sem registros");
                }

                addProgress("Validando Manutenção Serviços...");
                if(importMobileVO.getManutencaoServicos() == null || importMobileVO.getManutencaoServicos().isEmpty()){
                    faltaDados = true;
                    msgDialog.append("\n- Manutenção Serviços sem registros");
                }

                addProgress("Validando Serviços por Categoria Equipamento...");
                if(importMobileVO.getServicosPorCategoriaEquipamento() == null || importMobileVO.getServicosPorCategoriaEquipamento().isEmpty()){
                    faltaDados = true;
                    msgDialog.append("\n- Serviços por Categoria Equipamento sem registros");
                }
            }
            //------------------


            addProgress("Validando Usuários...");
            if (importMobileVO.getUsuarios() == null || importMobileVO.getUsuarios().isEmpty()) {
                faltaDados = true;
                msgDialog.append("\n- Usuários sem registros");
            }


            if(faltaDados)return;

            //SALVANDO TUDO NO BANCO DE DADOS....

            addProgress("Importando Obras...");
            for (ObraVO vo : importMobileVO.getObras()) {
                getDAO().getObraDAO().save(vo);
            }

            addProgress("Importando Frente Obras...");
            for (FrenteObraVO vo : importMobileVO.getFrentesObra()) {
                getDAO().getFrenteObraDAO().save(vo);
            }

            addProgress("Importando Atividades...");
            for (AtividadeVO vo : importMobileVO.getAtividades()) {
                getDAO().getAtividadeDAO().save(vo);
            }

            addProgress("Importando Materiais...");
            for (MaterialVO vo : importMobileVO.getMateriais()) {
                getDAO().getMaterialDAO().save(vo);
            }

            addProgress("Importando Usuários...");
            for (UsuarioVO vo : importMobileVO.getUsuarios()) {
                getDAO().getUsuarioDAO().save(vo);
            }

            addProgress("Importando Usuário Pessoal...");
            if (importMobileVO.getUsuariosPessoal() != null) {
                for (UsuarioVO vo : importMobileVO.getUsuariosPessoal()) {
                    getDAO().getUsuarioDAO().save(vo);
                    getDAO().getPessoalDAO().salvar(vo);
                }
            }

            addProgress("Importando Paralisações...");
            for (ParalisacaoVO vo : importMobileVO.getParalisacoes()) {
                getDAO().getParalisacaoDAO().save(vo);
            }

            addProgress("Importando Serviços...");
            for (ServicoVO vo : importMobileVO.getServicos()) {
                getDAO().getServicoDAO().save(vo);
            }

            addProgress("Importando Componentes...");
            for (ComponenteVO vo : importMobileVO.getComponentes()) {
                getDAO().getComponenteDAO().save(vo);
            }

            addProgress("Importando Equipamentos...");
            for (EquipamentoVO vo : importMobileVO.getEquipamentos()) {
                getDAO().getEquipamentoDAO().save(vo);
            }

            addProgress("Importando Origens e Destinos...");
            for (OrigemDestinoVO vo : importMobileVO.getOrigensDestinos()) {
                getDAO().getOrigemDestinoDAO().save(vo);
            }

            addProgress("Importando Compartimentos...");
            if (importMobileVO.getCompartimentos() != null) {
                for (CompartimentoVO vo : importMobileVO.getCompartimentos()) {
                    getDAO().getCompartimentoDAO().save(vo);
                }
            }

            addProgress("Importando Postos...");
            if (importMobileVO.getPostos() != null) {
                for (PostoVO vo : importMobileVO.getPostos()) {
                    getDAO().getPostoDAO().save(vo);
                }
            }

            addProgress("Importando Combustíveis...");
            if (importMobileVO.getCombustiveis() != null) {
                for (CombustivelLubrificanteVO vo : importMobileVO.getCombustiveis()) {
                    getDAO().getCombustivelLubrificanteDAO().save(vo);
                }
            }

            addProgress("Importando Combustíveis-Postos...");
            if (importMobileVO.getCombustiveisPostos() != null) {
                for (CombustivelPostoVO vo : importMobileVO.getCombustiveisPostos()) {
                    getDAO().getCombustivelPostoDAO().save(vo);
                }
            }

            addProgress("Importando Lubrificações...");
            if (importMobileVO.getLubrificacoesEquipamento() != null) {

                for (LubrificacaoEquipamentoVO vo : importMobileVO.getLubrificacoesEquipamento()) {
                    getDAO().getLubrificacaoEquipamentoDAO().save(vo);
                }
            }

            addProgress("Importando Justificativas...");
            if (importMobileVO.getJustificativasOperador() != null) {
                for (JustificativaOperadorVO vo : importMobileVO.getJustificativasOperador()) {
                    getDAO().getJustificativaOperadorDAO().save(vo);
                }
            }

            addProgress("Importando Preventivas...");
            if (importMobileVO.getPreventivas() != null) {
                for (PreventivaEquipamentoVO vo : importMobileVO.getPreventivas()) {
                    getDAO().getPreventivaEquipamentoDAO().save(vo);
                }
            }

            /*** dados modulo servico e mao de obra ***/

            addProgress("Importando Períodos...");
            if (importMobileVO.getPeriodoHorarioTrabalhos() != null) {
                for (PeriodoHorarioTrabalhoVO vo : importMobileVO.getPeriodoHorarioTrabalhos()) {
                    getDAO().getPeriodoHorarioTrabalhoDAO().insert(vo);
                }
            }

            addProgress("Importando Previsão Serviços...");
            if (importMobileVO.getPrevisaoServicos() != null) {
                for (PrevisaoServicoVO vo : importMobileVO.getPrevisaoServicos()) {
                    getDAO().getPrevisaoServicoDAO().insert(vo);
                }
            }

            addProgress("Importando Horários Trabalhos...");
            if (importMobileVO.getHorariosTrabalhos() != null) {
                for (HorarioTrabalhoVO vo : importMobileVO.getHorariosTrabalhos()) {
                    getDAO().getHorarioTrabalhoDAO().insert(vo);
                }
            }

            addProgress("Importando Integrantes Equipe...");
            if (importMobileVO.getIntegrantesEquipe() != null) {
                for (IntegranteEquipeVO vo : importMobileVO.getIntegrantesEquipe()) {
                    Date date = Util.extractSimpleDateFromDB(vo.getDataIngresso());
                    vo.setDataIngresso(Util.getDateFormated(date));
                    getDAO().getIntegranteEquipeDAO().insert(vo);
                }
            }

            addProgress("Importando Equipes Trabalho...");
            if (importMobileVO.getEquipesTrabalhos() != null) {
                for (EquipeTrabalhoVO vo : importMobileVO.getEquipesTrabalhos()) {
                    vo.setApropriavel("S");
                    getDAO().getEquipeTrabalhoDAO().insert(vo);
                }
            }


            // ------ dados modulo manutencao ------------------------------------------------------------------

            addProgress("Importando Equipamentos Categorias...");
            if(importMobileVO.getEquipamentoCategorias() != null){
                for(EquipamentoCategoriaVO vo : importMobileVO.getEquipamentoCategorias()){
                    getDAO().getEquipamentoCategoriaDAO().save(vo);
                }
            }

            addProgress("Importando Manutenção Serviços...");
            if(importMobileVO.getManutencaoServicos() != null){
                for(ManutencaoServicosVO vo : importMobileVO.getManutencaoServicos()){
                    getDAO().getManutencaoServicoDAO().save(vo);
                }
            }

            addProgress("Importando Serviços Por Categoria Equipamento...");
            if(importMobileVO.getServicosPorCategoriaEquipamento() != null){
                for(ManutencaoServicoPorCategoriaEquipamentoVO vo : importMobileVO.getServicosPorCategoriaEquipamento()){
                    getDAO().getManutencaoServicoPorCategoriaEquipamentoDAO().save(vo);
                }
            }

            //----------------------------------------------------------------------------------------------------------------

            msgDialog.append("Importação realizada com sucesso!");
        }
    }
}
