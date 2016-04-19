package br.com.constran.mobile.persistence.dao.aprop.maodeobra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import br.com.constran.mobile.persistence.dao.aprop.BaseDAO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.EquipeTrabalhoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.IntegranteTempVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.IntegranteVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.PessoalVO;
import br.com.constran.mobile.persistence.vo.menu.LocalizacaoVO;
import br.com.constran.mobile.view.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Criado em 06/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class IntegranteTempDAO extends BaseDAO<IntegranteTempVO> {

    private static final String INTEGRANTE_TMP_COL_EQUIPE = "equipe";
    private static final String INTEGRANTE_TMP_COL_PESSOA = "pessoa";
    private static final String INTEGRANTE_TMP_COL_DATA_INGRESSO = "dataIngresso";
    private static final String INTEGRANTE_TMP_COL_DATA_SAIDA = "dataSaida";

    private static IntegranteTempDAO instance;

    private IntegranteTempDAO(Context context) {
        super(context, TBL_INTEGRANTES_TEMP);
    }

    public static IntegranteTempDAO getInstance(Context context) {
        return instance == null ? instance = new IntegranteTempDAO(context) : instance;
    }

    public void delete(EquipeTrabalhoVO equipe, String data) {
        String whereClause = INTEGRANTE_TMP_COL_EQUIPE + EQ + "?" + AND + INTEGRANTE_TMP_COL_DATA_INGRESSO + EQ + "?";
        String whereArgs[] = new String[]{equipe.getId().toString(), data};

        delete(whereClause, whereArgs);
    }

    /**
     * Busca temporararios cuja data de saida seja inferior a data atual
     *
     * @param equipe
     * @return
     */
    public List<IntegranteTempVO> findByEquipe(EquipeTrabalhoVO equipe) {
        StringBuilder query = new StringBuilder();
        query.append("select *, date(substr(ieq.dataSaida,7,4)||'-'||substr(ieq.dataSaida,4,2)||'-'||substr(ieq.dataSaida,1,2)) dtSaida from ")
                .append(TBL_INTEGRANTES_TEMP).append(" ieq ")
                .append(" inner join ").append(TBL_EQUIPES_TRABALHO).append(" eqt ")
                .append(" on ieq.equipe = eqt.idEquipe ")
                .append(" inner join ").append(TBL_PESSOAL).append(" pes ")
                .append(" on ieq.pessoa = pes.idPessoal ")
//                .append(" where eqt.idEquipe = ? and (ieq.dataSaida is null or ")
                .append(" where eqt.idEquipe = ? and ( ")
                .append(" dtSaida >= date('now')  )")
                .append(" order by pes.nome ");

        return bindList(findByQuery(query.toString(), concatArgs(equipe.getId())));
    }

    public List<IntegranteTempVO> findByEquipe(EquipeTrabalhoVO equipe, String data) {
        if (data == null) {
            data = Util.getToday();
        }

        StringBuilder query = new StringBuilder();
        query.append("select *, date(substr(itp.dataIngresso,7,4)||'-'||substr(itp.dataIngresso,4,2)||'-'||substr(itp.dataIngresso,1,2)) dtIngresso")
                .append(", date(substr(itp.dataSaida,7,4)||'-'||substr(itp.dataSaida,4,2)||'-'||substr(itp.dataSaida,1,2)) dtSaida")
                .append(" from ").append(TBL_INTEGRANTES_TEMP).append(" itp ")
                .append(" inner join ").append(TBL_EQUIPES_TRABALHO).append(" eqt ")
                .append(" on itp.equipe = eqt.idEquipe ")
                .append(" inner join ").append(TBL_PESSOAL).append(" pes ")
                .append(" on itp.pessoa = pes.idPessoal ")
                .append(" where eqt.idEquipe = ? and ( ")
                .append(" date(?) between dtIngresso and dtSaida )")
                .append(" order by pes.nome ");

        Log.i("SQL",query.toString());

        return bindList(findByQuery(query.toString(), concatArgs(equipe.getId(), Util.toDataBaseDateFormat(data))));
    }

    public List<? extends IntegranteVO> findByLocalizacaoAndEquipe(LocalizacaoVO local, EquipeTrabalhoVO equipe) {
        if (local == null || local.getId() == null || equipe == null || equipe.getId() == null) {
            return new ArrayList<IntegranteVO>();
        }

        StringBuilder query = new StringBuilder();
        query.append("select * from ").append(TBL_INTEGRANTES_TEMP).append(" itp ")
                .append("inner join ").append(TBL_EQUIPES_TRABALHO).append(" eqt ")
                .append("on itp.equipe = eqt.idEquipe ")
                .append("inner join ").append(TBL_PESSOAL).append(" pes ")
                .append("on itp.pessoa = pes.idPessoal ")
                .append("where eqt.idEquipe = ? and eqt.idEquipe in ")
                .append("(select eeq.equipe from EventoEquipe eeq where eeq.localizacao = ?) ")
                .append("and date(substr(itp.dataIngresso,7,4)||'-'||substr(itp.dataIngresso,4,2)||'-'||substr(itp.dataIngresso,1,2)) >= date('now') ")
                .append("order by pes.nome ");

        return bindList(findByQuery(query.toString(), concatArgs(equipe.getId(), local.getId())));
    }


    @Override
    public IntegranteTempVO popularEntidade(Cursor cursor) {
        IntegranteTempVO integranteTemp = new IntegranteTempVO();
        EquipeTrabalhoVO equipe = EquipeTrabalhoDAO.popularEntidade(context, cursor);
        PessoalVO pessoa = PessoalDAO.popularEntidade(context, cursor);

        equipe.setId(equipe.getId() == null ? getInt(cursor, INTEGRANTE_TMP_COL_EQUIPE) : equipe.getId());
        pessoa.setId(pessoa.getId() == null ? getInt(cursor, INTEGRANTE_TMP_COL_PESSOA) : pessoa.getId());

        integranteTemp.setDataIngresso(cursor.getString(cursor.getColumnIndex(INTEGRANTE_TMP_COL_DATA_INGRESSO)));
        integranteTemp.setDataSaida(cursor.getString(cursor.getColumnIndex(INTEGRANTE_TMP_COL_DATA_SAIDA)));
        integranteTemp.setEquipe(equipe);
        integranteTemp.setPessoa(pessoa);

        return integranteTemp;
    }

    @Override
    public ContentValues bindContentValues(IntegranteTempVO integranteTemp) {
        ContentValues contentValues = new ContentValues();
        EquipeTrabalhoVO equipe = integranteTemp.getEquipe();
        PessoalVO pessoa = integranteTemp.getPessoa();

        contentValues.put(INTEGRANTE_TMP_COL_EQUIPE, equipe != null ? equipe.getId() : null);
        contentValues.put(INTEGRANTE_TMP_COL_PESSOA, pessoa != null ? pessoa.getId() : null);
        contentValues.put(INTEGRANTE_TMP_COL_DATA_INGRESSO, integranteTemp.getDataIngresso());
        contentValues.put(INTEGRANTE_TMP_COL_DATA_SAIDA, integranteTemp.getDataSaida());

        return contentValues;
    }

    @Override
    public boolean isNewEntity(IntegranteTempVO it) {
//        return it != null && it.getEquipe() == null || it.getEquipe().getId() == null;
        return findById(it) == null;
    }

    @Override
    public Object[] getPkArgs(IntegranteTempVO it) {
        return new Object[]{it.getEquipe().getId(), it.getPessoa().getId()};
    }

    @Override
    public String[] getPkColumns() {
        return new String[]{INTEGRANTE_TMP_COL_EQUIPE, INTEGRANTE_TMP_COL_PESSOA};
    }
}
