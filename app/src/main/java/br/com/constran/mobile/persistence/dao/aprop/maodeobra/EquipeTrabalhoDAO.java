package br.com.constran.mobile.persistence.dao.aprop.maodeobra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.dao.DAOFactory;
import br.com.constran.mobile.persistence.dao.aprop.BaseDAO;
import br.com.constran.mobile.persistence.vo.ObraVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.EquipeTrabalhoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.HorarioTrabalhoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.PeriodoHorarioTrabalhoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.PessoalVO;
import br.com.constran.mobile.persistence.vo.menu.LocalizacaoVO;
import br.com.constran.mobile.view.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Criado em 06/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class EquipeTrabalhoDAO extends BaseDAO<EquipeTrabalhoVO> {

    private static final String EQUIPE_TRABALHO_COL_ID = "idEquipe";
    private static final String EQUIPE_TRABALHO_COL_CCOBRA = "ccObra";
    private static final String EQUIPE_TRABALHO_COL_NOME_EQUIPE = "nomeEquipe";
    private static final String EQUIPE_TRABALHO_COL_APELIDO = "apelido";
    private static final String EQUIPE_TRABALHO_COL_FORMACAO = "formacao";
    private static final String EQUIPE_TRABALHO_COL_DATA_CRIACAO = "dataCriacao";
    private static final String EQUIPE_TRABALHO_COL_RESPONSAVEL = "responsavel";
    private static final String EQUIPE_TRABALHO_COL_ATIVA = "ativa";
    private static final String EQUIPE_TRABALHO_COL_APROPRIAVEL = "apropriavel";
    private static final String EQUIPE_TRABALHO_COL_HORARIO_TRAB = "horarioTrabalho";
//    private static final String EQUIPE_TRABALHO_COL_EQUPE_ASSOC  = "equipeAssociada";

    private static EquipeTrabalhoDAO instance;

    private EquipeTrabalhoDAO(Context context) {
        super(context, TBL_EQUIPES_TRABALHO);
    }

    public static EquipeTrabalhoDAO getInstance(Context context) {
        return instance == null ? instance = new EquipeTrabalhoDAO(context) : instance;
    }

    public static EquipeTrabalhoVO popularEntidade(Context context, Cursor cursor) {
        return getInstance(context).popularEntidade(cursor);
    }

    @Override
    public EquipeTrabalhoVO findById(EquipeTrabalhoVO equipe) {
        StringBuilder query = new StringBuilder();
        query.append("select * from ").append(TBL_EQUIPES_TRABALHO).append(" et ")
                .append("inner join ").append(TBL_HORARIOS_TRABALHO).append(" ht ")
                .append("on et.horarioTrabalho = ht.idHorario ")
                .append("where ").append(EQUIPE_TRABALHO_COL_ID).append(EQ).append(QM);

        Cursor cursor = findByQuery(query.toString(), concatArgs(equipe.getId()));

        DAOFactory daoFactory = DAOFactory.getInstance(context);

        if (cursor != null && cursor.moveToNext()) {
            equipe = popularEntidade(cursor);
            List<PeriodoHorarioTrabalhoVO> periodosTrabalho = daoFactory.getPeriodoHorarioTrabalhoDAO().findAllByHorario(equipe.getHorarioTrabalho());
            equipe.getHorarioTrabalho().setPeriodosHorariosTrabalhos(periodosTrabalho);
        }

        closeCursor(cursor);

        return equipe;
    }

    public List<EquipeTrabalhoVO> findByLocalizacao(LocalizacaoVO local) {
        if (local == null || local.getId() == null) {
            return new ArrayList<EquipeTrabalhoVO>();
        }

        StringBuilder query = new StringBuilder();
        query.append("select eqt.* from ").append(TBL_EQUIPES_TRABALHO).append(" eqt ")
                .append(" inner join ").append(TBL_LOCALIZACAO_EQUIPE).append(" leq ")
                .append(" on eqt.idEquipe = leq.equipe ")
                .append(" left join ").append(TBL_HORARIOS_TRABALHO).append(" hot ")
                .append(" on eqt.horarioTrabalho = hot.idHorario ")
                .append(" where leq.localizacao = ? ")
                .append(" and leq.dataHora = ? ")
                .append(" order by eqt.apelido ");

        return bindList(findByQuery(query.toString(), concatArgs(local.getId(), Util.getToday())));
    }

    public List<EquipeTrabalhoVO> findByLocalizacaoAndData(LocalizacaoVO local, String data) {
        if (local == null || local.getId() == null) {
            return new ArrayList<EquipeTrabalhoVO>();
        }

        StringBuilder query = new StringBuilder();
        query.append("select eqt.* from ").append(TBL_EQUIPES_TRABALHO).append(" eqt ")
                .append(" inner join ").append(TBL_LOCALIZACAO_EQUIPE).append(" leq ")
                .append(" on eqt.idEquipe = leq.equipe ")
                .append(" left join ").append(TBL_HORARIOS_TRABALHO).append(" hot ")
                .append(" on eqt.horarioTrabalho = hot.idHorario ")
                .append(" where leq.localizacao = ? ")
                .append(" and leq.dataHora = ? ")
                .append(" order by eqt.apelido ");

        return bindList(findByQuery(query.toString(), concatArgs(local.getId(), data)));
    }

    public List<EquipeTrabalhoVO> findNotInLocalEquipe(LocalizacaoVO l) {
        StringBuilder query = new StringBuilder();
        query.append("select eqt.* from ").append(TBL_EQUIPES_TRABALHO).append(" eqt ")
                .append(" where eqt.idEquipe not in (")
                .append(" select leq.equipe from ").append(TBL_LOCALIZACAO_EQUIPE).append(" leq ")
                .append(" where leq.localizacao = ? and leq.dataHora = ? )")
                .append(" order by eqt.apelido ");

        Cursor cursor = super.findByQuery(query.toString(), concatArgs(l.getId(), Util.getToday()));

        return bindList(cursor);
    }

    public EquipeTrabalhoVO popularEntidade(Cursor cursor) {
        ObraVO obra = new ObraVO(getInt(cursor, EQUIPE_TRABALHO_COL_CCOBRA));
        PessoalVO responsavel = new PessoalVO(getInt(cursor, EQUIPE_TRABALHO_COL_RESPONSAVEL));
        EquipeTrabalhoVO equipeTrabalho = new EquipeTrabalhoVO(getInt(cursor, EQUIPE_TRABALHO_COL_ID));
//        HorarioTrabalhoVO horarioTrabalho = new HorarioTrabalhoVO(cursor.getInt(cursor.getColumnIndex(EQUIPE_TRABALHO_COL_HORARIO_TRAB)));

        HorarioTrabalhoVO horarioTrabalho = HorarioTrabalhoDAO.popularEntidade(context, cursor);
        horarioTrabalho.setId(getInt(cursor, EQUIPE_TRABALHO_COL_HORARIO_TRAB));

//        equipeTrabalho.setId(getInt(cursor, EQUIPE_TRABALHO_COL_ID));
        equipeTrabalho.setNomeEquipe(getString(cursor, EQUIPE_TRABALHO_COL_NOME_EQUIPE));
        equipeTrabalho.setApelido(getString(cursor, EQUIPE_TRABALHO_COL_APELIDO));
        equipeTrabalho.setFormacao(getInt(cursor, EQUIPE_TRABALHO_COL_FORMACAO));
        equipeTrabalho.setDataCriacao(getString(cursor, EQUIPE_TRABALHO_COL_DATA_CRIACAO));
        equipeTrabalho.setAtiva(getString(cursor, EQUIPE_TRABALHO_COL_ATIVA));
        equipeTrabalho.setApropriavel(getString(cursor, EQUIPE_TRABALHO_COL_APROPRIAVEL));
        equipeTrabalho.setObra(obra);
        equipeTrabalho.setResponsavel(responsavel);
        equipeTrabalho.setHorarioTrabalho(horarioTrabalho);

        return equipeTrabalho;
    }

    public ContentValues bindContentValues(EquipeTrabalhoVO equipeTrabalho) {
        ContentValues contentValues = new ContentValues();
        ObraVO obra = equipeTrabalho.getObra();
        PessoalVO responsavel = equipeTrabalho.getResponsavel();
        HorarioTrabalhoVO horarioTrabalho = equipeTrabalho.getHorarioTrabalho();
//        EquipeTrabalhoVO equipeAssociada = equipeTrabalho.getEquipeAssociada();

        contentValues.put(EQUIPE_TRABALHO_COL_ID, equipeTrabalho.getId());
        contentValues.put(EQUIPE_TRABALHO_COL_CCOBRA, obra != null ? obra.getId() : null);
        contentValues.put(EQUIPE_TRABALHO_COL_NOME_EQUIPE, equipeTrabalho.getNomeEquipe());
        contentValues.put(EQUIPE_TRABALHO_COL_APELIDO, equipeTrabalho.getApelido());
        contentValues.put(EQUIPE_TRABALHO_COL_FORMACAO, equipeTrabalho.getFormacao());
        contentValues.put(EQUIPE_TRABALHO_COL_DATA_CRIACAO, equipeTrabalho.getDataCriacao());
        contentValues.put(EQUIPE_TRABALHO_COL_RESPONSAVEL, responsavel != null ? responsavel.getId() : null);
        contentValues.put(EQUIPE_TRABALHO_COL_ATIVA, equipeTrabalho.getAtiva());
        contentValues.put(EQUIPE_TRABALHO_COL_APROPRIAVEL, equipeTrabalho.getApropriavel());
        contentValues.put(EQUIPE_TRABALHO_COL_HORARIO_TRAB, horarioTrabalho != null ? horarioTrabalho.getId() : null);
//        contentValues.put(EQUIPE_TRABALHO_COL_EQUPE_ASSOC, equipeAssociada != null ? equipeAssociada.getId() : null);

        return contentValues;
    }

    @Override
    public boolean isNewEntity(EquipeTrabalhoVO et) {
        return et != null && et.getId() == null;
    }

    @Override
    public Object[] getPkArgs(EquipeTrabalhoVO et) {
        return new Object[]{et.getId()};
    }

    @Override
    public String[] getPkColumns() {
        return new String[]{EQUIPE_TRABALHO_COL_ID};
    }
}
