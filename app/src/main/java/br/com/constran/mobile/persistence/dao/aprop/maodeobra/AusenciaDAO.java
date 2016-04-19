package br.com.constran.mobile.persistence.dao.aprop.maodeobra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.dao.aprop.BaseDAO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.AusenciaVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.EquipeTrabalhoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.IntegranteVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.PessoalVO;
import br.com.constran.mobile.view.util.Util;

import java.util.List;

/**
 * Criado em 22/07/2014
 * Classe de persistencia/consulta de dados de Ausencia de integrantes
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class AusenciaDAO extends BaseDAO<AusenciaVO> {

    private static final String AUSENCIA_COL_EQUIPE = "equipe";
    private static final String AUSENCIA_COL_PESSOA = "pessoa";
    private static final String AUSENCIA_COL_DATA = "data";

    private static AusenciaDAO instance;

    public AusenciaDAO(Context context) {
        super(context, TBL_AUSENCIA);
    }

    public static AusenciaDAO getInstance(Context context) {
        return instance == null ? instance = new AusenciaDAO(context) : instance;
    }

    public List<AusenciaVO> findByEquipe(EquipeTrabalhoVO equipe) {
        StringBuilder query = new StringBuilder();
        query.append("select * from ")
                .append(TBL_AUSENCIA)
                .append(" where equipe = ? and data = ? ")
                .append("order by ")
                .append(AUSENCIA_COL_PESSOA);

        return bindList(findByQuery(query.toString(), concatArgs(equipe.getId(), Util.getToday())));
    }

    /**
     * Registra integrantes da lista como faltosos para equipe
     *
     * @param integrantes
     */
    public void salvar(List<? extends IntegranteVO> integrantes) {
        if (integrantes != null) {
            for (IntegranteVO integrante : integrantes) {
                AusenciaVO ausencia = new AusenciaVO(integrante.getEquipe(), integrante.getPessoa(), Util.getToday());
                save(ausencia);
            }
        }
    }

    public void deleteByEquipe(EquipeTrabalhoVO equipe, String data) {
        String whereClause = "equipe = ? and data = ?";
        String[] whereArgs = new String[]{equipe.getId().toString(), data};
        delete(whereClause, whereArgs);
    }

    public void deleteByPessoa(Integer idPessoa) {
        String whereClause = "pessoa = ? and data = ?";
        String[] whereArgs = new String[]{idPessoa.toString(), Util.getToday()};
        delete(whereClause, whereArgs);
    }

    @Override
    public boolean isNewEntity(AusenciaVO ausenciaVO) {
        return findById(ausenciaVO) == null;
    }

    @Override
    public Object[] getPkArgs(AusenciaVO ausenciaVO) {
        return new Object[]{ausenciaVO.getEquipe().getId(), ausenciaVO.getPessoa().getId(), ausenciaVO.getData()};
    }

    @Override
    public String[] getPkColumns() {
        return new String[]{AUSENCIA_COL_EQUIPE, AUSENCIA_COL_PESSOA, AUSENCIA_COL_DATA};
    }

    @Override
    public AusenciaVO popularEntidade(Cursor cursor) {
        AusenciaVO ausencia = new AusenciaVO();
        EquipeTrabalhoVO equipe = new EquipeTrabalhoVO(getInt(cursor, AUSENCIA_COL_EQUIPE));
        PessoalVO pessoa = new PessoalVO(getInt(cursor, AUSENCIA_COL_PESSOA));

        ausencia.setEquipe(equipe);
        ausencia.setPessoa(pessoa);
        ausencia.setData(getString(cursor, AUSENCIA_COL_DATA));

        return ausencia;
    }

    @Override
    public ContentValues bindContentValues(AusenciaVO ausencia) {
        EquipeTrabalhoVO equipe = ausencia.getEquipe();
        PessoalVO pessoa = ausencia.getPessoa();
        ContentValues contentValues = new ContentValues();

        contentValues.put(AUSENCIA_COL_EQUIPE, equipe != null ? equipe.getId() : null);
        contentValues.put(AUSENCIA_COL_PESSOA, pessoa != null ? pessoa.getId() : null);
        contentValues.put(AUSENCIA_COL_DATA, ausencia.getData());

        return contentValues;
    }
}
