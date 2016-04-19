package br.com.constran.mobile.persistence.dao.aprop.maodeobra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.aprop.BaseDAO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.EquipeTrabalhoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.PessoalVO;
import br.com.constran.mobile.persistence.vo.imp.UsuarioVO;
import br.com.constran.mobile.view.util.Util;

import java.util.List;

/**
 * Criado em 06/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class PessoalDAO extends BaseDAO<PessoalVO> {

    private static final String PESSOAL_COL_ID = "idPessoal";
    private static final String PESSOAL_COL_NOME = "nome";
    private static final String PESSOAL_COL_MATRICULA = "matricula";

    private static PessoalDAO instance;

    private PessoalDAO(Context context) {
        super(context, TBL_PESSOAL);
    }

    public static PessoalDAO getInstance(Context context) {
        return instance == null ? instance = new PessoalDAO(context) : instance;
    }

    public void salvar(UsuarioVO usuario) {
        PessoalVO pessoal = new PessoalVO();

        //o ID do usuário vai ser substituído pelo ID USUARIO que está salvo em configurações (usado para saber quem está apontando)
        //esta substituição do ID é feita na hora da exportação
        pessoal.setId(usuario.getIdUsuarioPessoal());
        pessoal.setNome(usuario.getNome());
        pessoal.setMatricula(usuario.getMatricula());

        super.insert(pessoal);
    }

    public static PessoalVO popularEntidade(Context context, Cursor cursor) {
        return getInstance(context).popularEntidade(cursor);
    }

    /**
     * busca os integrantes que não estão na lista de faltosos
     *
     * @param equipe
     * @return
     */
    public List<PessoalVO> findByIntegrantePresenteEquipe(EquipeTrabalhoVO equipe) {
        StringBuilder query = new StringBuilder();
        query.append("select pes.* from ").append(TBL_PESSOAL).append(" pes ")
                .append(" left join ").append(TBL_INTEGRANTES_EQUIPE).append(" ieq on pes.idPessoal = ieq.pessoa ")
                .append(" left join ").append(TBL_INTEGRANTES_TEMP).append(" itp on pes.idPessoal = itp.pessoa ")
                .append(" and date(substr(itp.dataSaida,7,4)||'-'||substr(itp.dataSaida,4,2)||'-'||substr(itp.dataSaida,1,2)) >= date('now')")
                .append(" where (ieq.equipe = ? or itp.equipe = ?) ")
                .append(" and pes.idPessoal not in ")
                .append(" (select distinct pessoa from ").append(TBL_AUSENCIA).append(" where data = ? and equipe = ?) ");

        Cursor cursor = super.findByQuery(query.toString(), concatArgs(equipe.getId(), equipe.getId(), Util.getToday(), equipe.getId()));

        return bindList(cursor);

    }

    @Override
    public PessoalVO popularEntidade(Cursor cursor) {
        PessoalVO pessoa = new PessoalVO();

        pessoa.setId(getInt(cursor, PESSOAL_COL_ID));
        pessoa.setNome(getString(cursor, PESSOAL_COL_NOME));
        pessoa.setMatricula(getString(cursor, PESSOAL_COL_MATRICULA));

        return pessoa;
    }

    @Override
    public ContentValues bindContentValues(PessoalVO pessoa) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(PESSOAL_COL_ID, pessoa.getId());
        contentValues.put(PESSOAL_COL_NOME, pessoa.getNome());
        contentValues.put(PESSOAL_COL_MATRICULA, pessoa.getMatricula());

        return contentValues;
    }

    @Override
    public boolean isNewEntity(PessoalVO pessoa) {
        return pessoa != null && pessoa.getId() == null;
    }

    @Override
    public Object[] getPkArgs(PessoalVO pessoa) {
        return new Object[]{pessoa.getId()};
    }

    @Override
    public String[] getPkColumns() {
        return new String[]{PESSOAL_COL_ID};
    }
}
