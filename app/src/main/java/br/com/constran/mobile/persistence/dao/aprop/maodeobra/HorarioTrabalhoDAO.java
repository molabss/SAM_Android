package br.com.constran.mobile.persistence.dao.aprop.maodeobra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.dao.DAOFactory;
import br.com.constran.mobile.persistence.dao.aprop.BaseDAO;
import br.com.constran.mobile.persistence.vo.ObraVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.HorarioTrabalhoVO;

/**
 * Criado em 06/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class HorarioTrabalhoDAO extends BaseDAO<HorarioTrabalhoVO> {

    private static final String HORARIO_TRAB_COL_ID_HORARIO = "idHorario";
    private static final String HORARIO_TRAB_COL_CCOBRA = "ccObra";
    private static final String HORARIO_TRAB_COL_DESCRICAO = "descricao";
    private static final String HORARIO_TRAB_COL_HORA_INI = "horaInicio";
    private static final String HORARIO_TRAB_COL_HORA_TERM = "horaTermino";

    private static HorarioTrabalhoDAO instance;

    private HorarioTrabalhoDAO(Context context) {
        super(context, TBL_HORARIOS_TRABALHO);
    }

    public static HorarioTrabalhoDAO getInstance(Context context) {
        return instance == null ? instance = new HorarioTrabalhoDAO(context) : instance;
    }

    protected static HorarioTrabalhoVO popularEntidade(Context context, Cursor cursor) {
        return getInstance(context).popularEntidade(cursor);
    }

    @Override
    public HorarioTrabalhoVO findById(HorarioTrabalhoVO horario) {
        StringBuilder query = new StringBuilder();
        query.append("select * from ").append(TBL_HORARIOS_TRABALHO).append(" ht ")
                .append("where ").append(HORARIO_TRAB_COL_ID_HORARIO).append(EQ).append(QM);

        Cursor cursor = findByQuery(query.toString(), concatArgs(horario.getId()));

        DAOFactory daoFactory = DAOFactory.getInstance(context);

        if (cursor != null && cursor.moveToNext()) {
            horario = popularEntidade(cursor);
            horario.setPeriodosHorariosTrabalhos(daoFactory.getPeriodoHorarioTrabalhoDAO().findAllByHorario(horario));
        }

        closeCursor(cursor);

        return horario;
    }

    @Override
    public HorarioTrabalhoVO popularEntidade(Cursor cursor) {
        HorarioTrabalhoVO horarioTrabalho = new HorarioTrabalhoVO();
        ObraVO obra = new ObraVO(getInt(cursor, HORARIO_TRAB_COL_CCOBRA));

        horarioTrabalho.setId(getInt(cursor, HORARIO_TRAB_COL_ID_HORARIO));
        horarioTrabalho.setObra(obra);
        horarioTrabalho.setDescricao(getString(cursor, HORARIO_TRAB_COL_DESCRICAO));
        horarioTrabalho.setHoraInicio(getString(cursor, HORARIO_TRAB_COL_HORA_INI));
        horarioTrabalho.setHoraTermino(getString(cursor, HORARIO_TRAB_COL_HORA_TERM));

        return horarioTrabalho;
    }

    @Override
    public ContentValues bindContentValues(HorarioTrabalhoVO horarioTrabalho) {
        ContentValues contentValues = new ContentValues();
        ObraVO obra = horarioTrabalho.getObra();

        contentValues.put(HORARIO_TRAB_COL_ID_HORARIO, horarioTrabalho.getId());
        contentValues.put(HORARIO_TRAB_COL_CCOBRA, obra != null ? obra.getId() : null);
        contentValues.put(HORARIO_TRAB_COL_DESCRICAO, horarioTrabalho.getDescricao());
        contentValues.put(HORARIO_TRAB_COL_HORA_INI, horarioTrabalho.getHoraInicio());
        contentValues.put(HORARIO_TRAB_COL_HORA_TERM, horarioTrabalho.getHoraTermino());

        return contentValues;
    }

    @Override
    public boolean isNewEntity(HorarioTrabalhoVO ht) {
        return ht != null && ht.getId() == null;
    }

    @Override
    public Object[] getPkArgs(HorarioTrabalhoVO ht) {
        return new Object[]{ht.getId()};
    }

    @Override
    public String[] getPkColumns() {
        return new String[]{HORARIO_TRAB_COL_ID_HORARIO};
    }

}
