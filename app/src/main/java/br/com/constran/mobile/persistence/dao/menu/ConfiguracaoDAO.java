package br.com.constran.mobile.persistence.dao.menu;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.R;
import br.com.constran.mobile.exception.AlertException;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.menu.ConfiguracoesVO;
import br.com.constran.mobile.system.SharedPreferencesHelper;
import br.com.constran.mobile.view.util.Util;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

public class ConfiguracaoDAO extends AbstractDAO {

    //Colunas da tabela CONFIGURACAO
    private static final String CONFIG_COL_OBRA = "obra";
    private static final String CONFIG_COL_OBRA2 = "obra2";
    private static final String CONFIG_COL_ID_POSTO = "idPosto";
    private static final String CONFIG_COL_ID_USUARIO = "idUsuario";
    private static final String CONFIG_COL_ID_USUARIO_PESSOAL = "idUsuarioPessoal";
    private static final String CONFIG_COL_DISPOSITIVO = "dispositivo";
    private static final String CONFIG_COL_SERVIDOR = "servidor";
    private static final String CONFIG_COL_PORTA_WEB = "portaweb";
    private static final String CONFIG_COL_PORTA_FTP = "portaftp";
    private static final String CONFIG_COL_ETICKET = "eticket";
    private static final String CONFIG_COL_DURACAO = "duracao";
    private static final String CONFIG_COL_TOLERANCIA = "tolerancia";
    private static final String CONFIG_COL_REFERENCIA = "referencia";
    private static final String CONFIG_COL_DATA_HORA = "datahora";
    private static final String CONFIG_COL_POSTO = "posto";
    private static final String CONFIG_COL_COD_USUARIO = "codUsuario";
    private static final String CONFIG_COL_ATUAL = "atual";

    private static ConfiguracaoDAO instance;
    private static Context context;

    private ConfiguracaoDAO(Context context) {
        super(context, TBL_CONFIGURACAO);
        this.context = context;
    }

    public static ConfiguracaoDAO getInstance(Context context) {
        if (instance == null) {
            instance = new ConfiguracaoDAO(context);
        }

        return instance;
    }

    public boolean isExistRecords() {
        Query query = new Query(true);

        query.setColumns(new String[]{
                " count(*) "
        });

        query.setTableJoin(TBL_CONFIGURACAO);

        Cursor cursor = getCursor(query);
        cursor.moveToNext();

        boolean value = cursor.getInt(0) > 0;

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return value;
    }


    public void save(ConfiguracoesVO pVO) {

        StringBuilder builder = new StringBuilder("");
        builder.append(" update " + TBL_CONFIGURACAO + " set [atual] = 'N'; ");
        execute(builder);

        insert(getContentValues(pVO));
    }

    public void loadConfig() throws Exception {

        ConfiguracoesVO vo = new ConfiguracoesVO();

        SharedPreferencesHelper.Configuracao.CONTEXT = context;

        vo.setDispositivo(SharedPreferencesHelper.Configuracao.getDispositivo());
        vo.setIdObra(SharedPreferencesHelper.Configuracao.getIdObra());
        vo.setPortaFtp(SharedPreferencesHelper.Configuracao.getPortaFtp());
        vo.setPortaWeb(SharedPreferencesHelper.Configuracao.getPortaWeb());
        vo.setEticket(SharedPreferencesHelper.Configuracao.getEtiket());
        vo.setTolerancia(SharedPreferencesHelper.Configuracao.getTolerancia());
        vo.setReferencia(SharedPreferencesHelper.Configuracao.getReferencia());
        vo.setServidor(SharedPreferencesHelper.Configuracao.getServidor());

        vo.setIdUsuario(SharedPreferencesHelper.Configuracao.getIdUsuario());
        vo.setIdUsuarioPessoal(SharedPreferencesHelper.Configuracao.getIdUsuarioPessoal());
        vo.setIdPosto(SharedPreferencesHelper.Configuracao.getIdPosto());
        vo.setDuracao(Double.parseDouble(SharedPreferencesHelper.Configuracao.getDuracao()));

        if (vo == null ||
                vo.getDispositivo() == null ||
                vo.getIdObra()      == null ||
                vo.getPortaFtp()    == null ||
                vo.getPortaWeb()    == null ||
                vo.getEticket()     == null ||
                vo.getTolerancia()  == null ||
                vo.getReferencia()  == null ||
                vo.getServidor()    == null
            )
        {
            throw new AlertException(getStr(R.string.ALERT_CONFIG_FILE_INVALID));
        }
        else {
            vo.setAtual("Y");
            save(vo);
        }
    }

    /*
    public void loadConfig(boolean manual) throws Exception {

        if (Util.getPathSdCard() != null) {

            boolean notBlank = isExistRecords();

            if (manual || !notBlank) {

                String strPath = Util.getPathSdCard() + getStr(R.string.DIR_CONFIG);

                String nameFile = getStr(R.string.FILE_CONFIG);

                File path = new File(strPath);

                if (!path.exists()) {
                    throw new AlertException(getStr(R.string.ALERT_CONFIG_FILE_NOT_FOUND));
                }

                File file = new File(path, nameFile);

                Reader reader = new FileReader(file);

                ConfiguracoesVO vo = Util.GSON_API.fromJson(reader, ConfiguracoesVO.class);

                if (vo == null ||
                        vo.getDispositivo() == null ||
                        vo.getIdObra() == null ||
                        vo.getPortaFtp() == null ||
                        vo.getPortaWeb() == null ||
                        vo.getEticket() == null ||
                        vo.getTolerancia() == null ||
                        vo.getReferencia() == null ||
                        vo.getServidor() == null) {

                    throw new AlertException(getStr(R.string.ALERT_CONFIG_FILE_INVALID));
                } else {
                    vo.setAtual("Y");
                    save(vo);
                }

            } else {
                String strPath = Util.getPathSdCard() + getStr(R.string.DIR_CONFIG);

                String nameFile = getStr(R.string.FILE_CONFIG);

                File path = new File(strPath);

                if (!path.exists()) {
                    throw new AlertException(getStr(R.string.ALERT_CONFIG_FILE_NOT_FOUND));
                }

                File file = new File(path, nameFile);

                Reader reader = new FileReader(file);

                ConfiguracoesVO vo = Util.GSON_API.fromJson(reader, ConfiguracoesVO.class);

                if (vo == null ||
                        vo.getDispositivo() == null ||
                        vo.getIdObra() == null ||
                        vo.getPortaFtp() == null ||
                        vo.getPortaWeb() == null ||
                        vo.getEticket() == null ||
                        vo.getTolerancia() == null ||
                        vo.getReferencia() == null ||
                        vo.getServidor() == null) {

                    throw new AlertException(getStr(R.string.ALERT_CONFIG_FILE_INVALID));
                }
            }
        }
    }
    */

    public ConfiguracoesVO getConfiguracaoVO() {

        String[] dados = getValues();

        ConfiguracoesVO vo = new ConfiguracoesVO();

        int i = 0;

        vo.setIdObra(Integer.valueOf(dados[i]));
        vo.setIdObra2((dados[++i] != null) ? Integer.valueOf(dados[i]) : null);
        vo.setNomeUsuario(dados[++i]);
        vo.setDispositivo(dados[++i]);
        vo.setServidor(dados[++i]);
        vo.setCodUsuario((dados[++i] != null) ? Integer.valueOf(dados[i]) : null);
        vo.setIdUsuario((dados[++i] != null) ? Integer.valueOf(dados[i]) : null);
        vo.setIdUsuarioPessoal((dados[++i] != null) ? Integer.valueOf(dados[i]) : null);
        vo.setIdPosto((dados[++i] != null) ? Integer.valueOf(dados[i]) : null);
        vo.setNomePosto(dados[++i]);
        vo.setPortaWeb(dados[++i]);
        vo.setPortaFtp(dados[++i]);
        vo.setEticket(dados[++i]);
        vo.setAtual(dados[++i]);
        vo.setDuracao((dados[++i] != null) ? Double.valueOf(dados[i]) : 0);
        vo.setTolerancia((dados[++i] != null) ? Integer.valueOf(dados[i]) : 0);
        vo.setReferencia(dados[++i]);

        return vo;
    }

    public String[] getValues() {

        String columns[] = null;
        String tableJoin = null;
        String conditions = null;

        columns = new String[]{
                "c.[obra]", "c.[obra2]",
                "ifnull(u.[nome],'" + getStr(R.string.NAO_INFORMADO) + "') " + ALIAS_NOME,
                "c.[dispositivo]",
                "c.[servidor]",
                "ifnull(c.[codUsuario], 0) " + ALIAS_COD_USUARIO,
                "c.[idUsuario]",
                "c.[idUsuarioPessoal]",
                "p.[idPosto]",
                "ifnull(p.[descricao],'" + getStr(R.string.NAO_INFORMADO) + "') " + ALIAS_DESCRICAO,
                "c.[portaWeb]",
                "c.[portaFtp]",
                "c.[eticket]",
                "c.[atual]",
                "c.[duracao]",
                "c.[tolerancia]", "c.[referencia]"};

        tableJoin = TBL_CONFIGURACAO + " c ";

        tableJoin += " left join [usuarios] u on u.idUsuario = c.idUsuario or u.idUsuarioPessoal = c.idUsuarioPessoal ";

        tableJoin += " left join [postos] p on p.idPosto = c.posto ";

        conditions = " c.atual = 'Y'";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);

        Cursor cursor = getCursor(query);

        String[] dados = preencherDados(cursor);

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    @Override
    protected String[] getColunas() {
        return new String[]{
                CONFIG_COL_OBRA,
                CONFIG_COL_OBRA2,
                ALIAS_NOME,
                CONFIG_COL_DISPOSITIVO,
                CONFIG_COL_SERVIDOR,
                ALIAS_COD_USUARIO,
                CONFIG_COL_ID_USUARIO,
                CONFIG_COL_ID_USUARIO_PESSOAL,
                CONFIG_COL_ID_POSTO,
                ALIAS_DESCRICAO,
                CONFIG_COL_PORTA_WEB,
                CONFIG_COL_PORTA_FTP,
                CONFIG_COL_ETICKET,
                CONFIG_COL_ATUAL,
                CONFIG_COL_DURACAO,
                CONFIG_COL_TOLERANCIA,
                CONFIG_COL_REFERENCIA
        };
    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        ConfiguracoesVO pVO = (ConfiguracoesVO) abstractVO;

        ContentValues contentValues = new ContentValues();
        contentValues.put(CONFIG_COL_DATA_HORA, Util.getNow());
        contentValues.put(CONFIG_COL_OBRA, pVO.getIdObra());
        contentValues.put(CONFIG_COL_OBRA2, pVO.getIdObra2());
        contentValues.put(CONFIG_COL_POSTO, pVO.getIdPosto());
        contentValues.put(CONFIG_COL_COD_USUARIO, pVO.getCodUsuario());
        contentValues.put(CONFIG_COL_ID_USUARIO, pVO.getIdUsuario());
        contentValues.put(CONFIG_COL_ID_USUARIO_PESSOAL, pVO.getIdUsuarioPessoal());
        contentValues.put(CONFIG_COL_DISPOSITIVO, pVO.getDispositivo());
        contentValues.put(CONFIG_COL_SERVIDOR, pVO.getServidor());
        contentValues.put(CONFIG_COL_PORTA_WEB, pVO.getPortaWeb());
        contentValues.put(CONFIG_COL_PORTA_FTP, pVO.getPortaFtp());
        contentValues.put(CONFIG_COL_ETICKET, pVO.getEticket());
        contentValues.put(CONFIG_COL_ATUAL, pVO.getAtual());
        contentValues.put(CONFIG_COL_DURACAO, pVO.getDuracao());
        contentValues.put(CONFIG_COL_TOLERANCIA, pVO.getTolerancia());
        contentValues.put(CONFIG_COL_REFERENCIA, pVO.getReferencia());

        return contentValues;
    }
}
