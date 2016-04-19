package br.com.constran.mobile.persistence.dao;

import android.content.Context;
import br.com.constran.mobile.persistence.dao.aprop.ApropriacaoDAO;
import br.com.constran.mobile.persistence.dao.aprop.eqp.ApropriacaoEquipamentoDAO;
import br.com.constran.mobile.persistence.dao.aprop.eqp.EquipamentoParteDiariaDAO;
import br.com.constran.mobile.persistence.dao.aprop.eqp.EventoDAO;
import br.com.constran.mobile.persistence.dao.aprop.indicepluviometrico.IndicePluviometricoDAO;
import br.com.constran.mobile.persistence.dao.aprop.maodeobra.*;
import br.com.constran.mobile.persistence.dao.aprop.mov.ApropriacaoMovimentacaoDAO;
import br.com.constran.mobile.persistence.dao.aprop.mov.EquipamentoMovimentacaoDiariaDAO;
import br.com.constran.mobile.persistence.dao.aprop.mov.ViagemDAO;
import br.com.constran.mobile.persistence.dao.imp.*;
import br.com.constran.mobile.persistence.dao.imp.rae.RaeDAO;
import br.com.constran.mobile.persistence.dao.imp.rae.abs.*;
import br.com.constran.mobile.persistence.dao.manutencao.EquipamentoCategoriaDAO;
import br.com.constran.mobile.persistence.dao.manutencao.ManutencaoEquipamentoDAO;
import br.com.constran.mobile.persistence.dao.manutencao.ManutencaoEquipamentoServicoDAO;
import br.com.constran.mobile.persistence.dao.manutencao.ManutencaoServicoPorCategoriaEquipamentoDAO;
import br.com.constran.mobile.persistence.dao.manutencao.ManutencaoServicosDAO;
import br.com.constran.mobile.persistence.dao.menu.ConfiguracaoDAO;
import br.com.constran.mobile.persistence.dao.menu.LocalizacaoDAO;
import br.com.constran.mobile.persistence.dao.menu.LogEnvioInformacoesDAO;
import br.com.constran.mobile.persistence.dao.util.UtilDAO;

public class DAOFactory {

    private static DAOFactory instance;
    private static Context context;

    private DAOFactory() {
    }

    public static DAOFactory getInstance(Context pContext) {
        if (instance == null) {
            instance = new DAOFactory();
        }
        context = pContext;

        return instance;
    }

    public ConfiguracaoDAO getConfiguracoesDAO() {
        return ConfiguracaoDAO.getInstance(context);
    }

    public LocalizacaoDAO getLocalizacaoDAO() {
        return LocalizacaoDAO.getInstance(context);
    }

    public ViagemDAO getViagemDAO() {
        return ViagemDAO.getInstance(context);
    }

    public ApropriacaoMovimentacaoDAO getApropriacaoMovimentacaoDAO() {
        return ApropriacaoMovimentacaoDAO.getInstance(context);
    }

    public ApropriacaoDAO getApropriacaoDAO() {
        return ApropriacaoDAO.getInstance(context);
    }

    public ApropriacaoEquipamentoDAO getApropriacaoEquipamentoDAO() {
        return ApropriacaoEquipamentoDAO.getInstance(context);
    }

    public EquipamentoMovimentacaoDiariaDAO getMovimentacaoDiariaDAO() {
        return EquipamentoMovimentacaoDiariaDAO.getInstance(context);
    }

    public EventoDAO getEventoDAO() {
        return EventoDAO.getInstance(context);
    }

    public EquipamentoParteDiariaDAO getParteDiariaDAO() {
        return EquipamentoParteDiariaDAO.getInstance(context);
    }

    public FrenteObraDAO getFrenteObraDAO() {
        return FrenteObraDAO.getInstance(context);
    }

    public AtividadeDAO getAtividadeDAO() {
        return AtividadeDAO.getInstance(context);
    }

    public ServicoDAO getServicoDAO() {
        return ServicoDAO.getInstance(context);
    }

    public ParalisacaoDAO getParalisacaoDAO() {
        return ParalisacaoDAO.getInstance(context);
    }

    public ComponenteDAO getComponenteDAO() {
        return ComponenteDAO.getInstance(context);
    }

    public UtilDAO getUtilDAO() {
        return UtilDAO.getInstance(context);
    }

    public ObraDAO getObraDAO() {
        return ObraDAO.getInstance(context);
    }

    public LogEnvioInformacoesDAO getLogEnvioInformacoesDAO() { return LogEnvioInformacoesDAO.getInstance(context); }

    public MaterialDAO getMaterialDAO() {
        return MaterialDAO.getInstance(context);
    }

    public UsuarioDAO getUsuarioDAO() {
        return UsuarioDAO.getInstance(context);
    }

    public EquipamentoDAO getEquipamentoDAO() {
        return EquipamentoDAO.getInstance(context);
    }

    public OrigemDestinoDAO getOrigemDestinoDAO() {
        return OrigemDestinoDAO.getInstance(context);
    }

    public CompartimentoDAO getCompartimentoDAO() {
        return CompartimentoDAO.getInstance(context);
    }

    public PostoDAO getPostoDAO() {
        return PostoDAO.getInstance(context);
    }

    public CombustivelLubrificanteDAO getCombustivelLubrificanteDAO() {
        return CombustivelLubrificanteDAO.getInstance(context);
    }

    public CombustivelPostoDAO getCombustivelPostoDAO() {
        return CombustivelPostoDAO.getInstance(context);
    }

    public AbastecimentoDAO getAbastecimentoDAO() {
        return AbastecimentoDAO.getInstance(context);
    }

    public AbastecimentoPostoDAO getAbastecimentoPostoDAO() {
        return AbastecimentoPostoDAO.getInstance(context);
    }

    public AbastecimentoTempDAO getAbastecimentoTempDAO() {
        return AbastecimentoTempDAO.getInstance(context);
    }

    public RaeDAO getRaeDAO() {
        return RaeDAO.getInstance(context);
    }

    public LubrificacaoDetalhesDAO getLubrificacaoDetalhesDAO() {
        return LubrificacaoDetalhesDAO.getInstance(context);
    }

    public LubrificacaoEquipamentoDAO getLubrificacaoEquipamentoDAO() {
        return LubrificacaoEquipamentoDAO.getInstance(context);
    }

    public PreventivaEquipamentoDAO getPreventivaEquipamentoDAO() {
        return PreventivaEquipamentoDAO.getInstance(context);
    }

    public JustificativaOperadorDAO getJustificativaOperadorDAO() {
        return JustificativaOperadorDAO.getInstance(context);
    }

    /**
     * Métodos de apropriação serviço mao-de-obra **
     */

    public PessoalDAO getPessoalDAO() {
        return PessoalDAO.getInstance(context);
    }

    public EquipeTrabalhoDAO getEquipeTrabalhoDAO() {
        return EquipeTrabalhoDAO.getInstance(context);
    }

    public EquipamentoEquipeDAO getEquipamentoEquipeDAO() {
        return EquipamentoEquipeDAO.getInstance(context);
    }

    public IntegranteTempDAO getIntegranteTempDAO() {
        return IntegranteTempDAO.getInstance(context);
    }

    public IntegranteEquipeDAO getIntegranteEquipeDAO() {
        return IntegranteEquipeDAO.getInstance(context);
    }

    public PeriodoHorarioTrabalhoDAO getPeriodoHorarioTrabalhoDAO() {
        return PeriodoHorarioTrabalhoDAO.getInstance(context);
    }

    public HorarioTrabalhoDAO getHorarioTrabalhoDAO() {
        return HorarioTrabalhoDAO.getInstance(context);
    }

    public ApropriacaoServicoDAO getApropriacaoServicoDAO() {
        return ApropriacaoServicoDAO.getInstance(context);
    }

    public ParalisacaoEquipeDAO getParalisacaoEquipeDAO() {
        return ParalisacaoEquipeDAO.getInstance(context);
    }

    public AtividadeServicoDAO getAtividadeServicoDAO() {
        return AtividadeServicoDAO.getInstance(context);
    }

    public ApropriacaoMaoObraDAO getApropriacaoMaoObraDAO() {
        return ApropriacaoMaoObraDAO.getInstance(context);
    }

    public ParalisacaoMaoObraDAO getParalisacaoMaoObraDAO() {
        return ParalisacaoMaoObraDAO.getInstance(context);
    }

    public ControleFrequenciaDAO getControleFrequenciaDAO() {
        return ControleFrequenciaDAO.getInstance(context);
    }

    public EventoEquipeDAO getEventoEquipeDAO() {
        return EventoEquipeDAO.getInstance(context);
    }

    public IndicePluviometricoDAO getIndicePluviometricoDAO() {
        return IndicePluviometricoDAO.getInstance(context);
    }

    public LocalizacaoEquipeDAO getLocalizacaoEquipeDAO() {
        return LocalizacaoEquipeDAO.getInstance(context);
    }

    public AusenciaDAO getAusenciaDAO() {
        return AusenciaDAO.getInstance(context);
    }

    public PrevisaoServicoDAO getPrevisaoServicoDAO() {
        return PrevisaoServicoDAO.getInstance(context);
    }


    // Modulo de manutencao
    public EquipamentoCategoriaDAO getEquipamentoCategoriaDAO(){
        return EquipamentoCategoriaDAO.getInstance(context);
    }

    public ManutencaoServicosDAO getManutencaoServicoDAO(){
        return ManutencaoServicosDAO.getInstance(context);
    }

    public ManutencaoServicoPorCategoriaEquipamentoDAO getManutencaoServicoPorCategoriaEquipamentoDAO(){
        return ManutencaoServicoPorCategoriaEquipamentoDAO.getInstance(context);
    }

    public ManutencaoEquipamentoDAO getManutencaoEquipamentoDAO(){
        return ManutencaoEquipamentoDAO.getInstance(context);
    }

    public ManutencaoEquipamentoServicoDAO getManutencaoEquipamentoServicoDAO(){
        return ManutencaoEquipamentoServicoDAO.getInstance(context);
    }
    //------------------------------------------------------------------


    public LogAuditoriaDAO getLogAuditoriaDAO(){
        return LogAuditoriaDAO.getInstance(context);
    }

}
