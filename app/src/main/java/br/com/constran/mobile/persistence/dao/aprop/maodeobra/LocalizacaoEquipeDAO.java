package br.com.constran.mobile.persistence.dao.aprop.maodeobra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.dao.aprop.BaseDAO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.EquipeTrabalhoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.LocalizacaoEquipeVO;
import br.com.constran.mobile.persistence.vo.menu.LocalizacaoVO;
import br.com.constran.mobile.view.util.Util;

import java.util.List;

/**
 * Criado em 18/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class LocalizacaoEquipeDAO extends BaseDAO<LocalizacaoEquipeVO> {

    private static final String LOCAL_EQUIPE_COL_LOCALIZACAO = "localizacao";
    private static final String LOCAL_EQUIPE_COL_EQUIPE = "equipe";
    private static final String LOCAL_EQUIPE_COL_DATA_HORA = "dataHora";
    private static final String EQUIPE_COL_APELIDO = "apelido";

    private static LocalizacaoEquipeDAO instance;
    private static IntegranteEquipeDAO integranteEquipeDAO;

    private LocalizacaoEquipeDAO(Context context) {
        super(context, TBL_LOCALIZACAO_EQUIPE);
    }

    public static LocalizacaoEquipeDAO getInstance(Context context) {
        integranteEquipeDAO = integranteEquipeDAO == null ? IntegranteEquipeDAO.getInstance(context) : integranteEquipeDAO;
        return instance == null ? instance = new LocalizacaoEquipeDAO(context) : instance;
    }

    @Override
    public boolean isNewEntity(LocalizacaoEquipeVO le) {
        return findById(le) == null;
    }

    public List<LocalizacaoEquipeVO> findListByLocalizacao(LocalizacaoVO localizacao) {
        StringBuilder query = new StringBuilder();
        query.append("select leq.*, eqt.apelido from ")
                .append(TBL_LOCALIZACAO_EQUIPE).append(" leq ")
                .append(" inner join ").append(TBL_EQUIPES_TRABALHO).append(" eqt ")
                .append(" on leq.equipe = eqt.idEquipe ")
                .append(" where leq.localizacao = ? ")
                .append(" and leq.dataHora = ? ")
                .append("order by UPPER(eqt.apelido), leq.dataHora ");

        Cursor cursor = findByQuery(query.toString(), concatArgs(localizacao.getId(), Util.getToday()));

        List<LocalizacaoEquipeVO> localizacaoEquipes = bindList(cursor);

        bindIntegrantesEquipe(localizacaoEquipes);

        return localizacaoEquipes;
    }

    private void bindIntegrantesEquipe(List<LocalizacaoEquipeVO> localizacaoEquipes) {
        if (localizacaoEquipes != null) {
            for (LocalizacaoEquipeVO localEquipe : localizacaoEquipes) {
                EquipeTrabalhoVO equipe = localEquipe.getEquipe();
                equipe.setIntegrantesFixos(integranteEquipeDAO.findByEquipe(equipe.getId()));
            }
        }
    }

    public List<LocalizacaoEquipeVO> findListByLocalizacaoAndData(LocalizacaoVO localizacao, String data) {
        StringBuilder query = new StringBuilder();
        query.append("select leq.*, eqt.apelido from ")
                .append(TBL_LOCALIZACAO_EQUIPE).append(" leq ")
                .append(" inner join ").append(TBL_EQUIPES_TRABALHO).append(" eqt ")
                .append(" on leq.equipe = eqt.idEquipe ")
                .append(" where leq.localizacao = ? ")
                .append(" and leq.dataHora = ? ")
                .append("order by UPPER(eqt.apelido), leq.dataHora ");

        Cursor cursor = findByQuery(query.toString(), concatArgs(localizacao.getId(), data));

        return bindList(cursor);
    }

    public List<LocalizacaoEquipeVO> findAllItems() {
        StringBuilder query = new StringBuilder();
        query.append("select leq.*, eqt.apelido from ")
                .append(TBL_LOCALIZACAO_EQUIPE).append(" leq ")
                .append(" inner join ").append(TBL_EQUIPES_TRABALHO).append(" eqt ")
                .append(" on leq.equipe = eqt.idEquipe ")
                .append(" where leq.dataHora = ? ")
                .append("order by leq.dataHora, eqt.apelido ");

        Cursor cursor = findByQuery(query.toString(), concatArgs(Util.getToday()));

        return bindList(cursor);

    }

    @Override
    public LocalizacaoEquipeVO popularEntidade(Cursor cursor) {
        LocalizacaoEquipeVO le = new LocalizacaoEquipeVO();
        LocalizacaoVO localizacao = new LocalizacaoVO(cursor.getInt(cursor.getColumnIndex(LOCAL_EQUIPE_COL_LOCALIZACAO)));
        EquipeTrabalhoVO equipe = new EquipeTrabalhoVO(cursor.getInt(cursor.getColumnIndex(LOCAL_EQUIPE_COL_EQUIPE)));

        int index = cursor.getColumnIndex(EQUIPE_COL_APELIDO);
        equipe.setApelido(index > -1 ? cursor.getString(index) : "");

        le.setDataHora(cursor.getString(cursor.getColumnIndex(LOCAL_EQUIPE_COL_DATA_HORA)));
        le.setLocalizacao(localizacao);
        le.setEquipe(equipe);

        return le;
    }

    @Override
    public ContentValues bindContentValues(LocalizacaoEquipeVO le) {
        ContentValues contentValues = new ContentValues();
        LocalizacaoVO l = le.getLocalizacao();
        EquipeTrabalhoVO e = le.getEquipe();

        contentValues.put(LOCAL_EQUIPE_COL_LOCALIZACAO, l != null && l.getId() != null ? l.getId() : null);
        contentValues.put(LOCAL_EQUIPE_COL_EQUIPE, e != null && e.getId() != null ? e.getId() : null);
        contentValues.put(LOCAL_EQUIPE_COL_DATA_HORA, le.getDataHora());

        return contentValues;
    }

    @Override
    public String[] getPkColumns() {
        return new String[]{LOCAL_EQUIPE_COL_LOCALIZACAO, LOCAL_EQUIPE_COL_EQUIPE, LOCAL_EQUIPE_COL_DATA_HORA};
    }

    @Override
    public Object[] getPkArgs(LocalizacaoEquipeVO le) {
        return new Object[]{le.getLocalizacao().getId(), le.getEquipe().getId(), le.getDataHora()};
    }

}
