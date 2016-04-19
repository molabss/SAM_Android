package br.com.constran.mobile.persistence.dao.aprop.maodeobra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.dao.aprop.BaseDAO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.HorarioTrabalhoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.PeriodoHorarioTrabalhoVO;
import br.com.constran.mobile.view.util.Util;

import java.util.List;

/**
 * Criado em 06/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class PeriodoHorarioTrabalhoDAO extends BaseDAO<PeriodoHorarioTrabalhoVO> {

    private static final int TIPO_DIA_DE_SEMANA = 9;
    private static final int TIPO_FIM_DE_SEMANA = 7;

    private static final String PERIODO_HOR_TRAB_COL_HORARIO = "horario";
    private static final String PERIODO_HOR_TRAB_COL_DIA_SEMANA = "diaSemana";
    private static final String PERIODO_HOR_TRAB_COL_HORA_INI = "horaInicio";
    private static final String PERIODO_HOR_TRAB_COL_HORA_TERM = "horaTermino";
    private static final String PERIODO_HOR_TRAB_COL_PRODUTIVO = "produtivo";
    private static final String PERIODO_HOR_TRAB_COL_COBRA_HE = "cobraHoraExtra";
    private static final String PERIODO_HOR_TRAB_COL_COD_PARAL = "codigoParalisacao";

    private static PeriodoHorarioTrabalhoDAO instance;

    private PeriodoHorarioTrabalhoDAO(Context context) {
        super(context, TBL_PERIODOS_HORA_TRABALHO);
    }

    public static PeriodoHorarioTrabalhoDAO getInstance(Context context) {
        return instance == null ? instance = new PeriodoHorarioTrabalhoDAO(context) : instance;
    }

    /**
     * Busca os periodos de trabalho a partir do horario de trabalho
     *
     * @param horarioTrabalho
     * @return
     */
    public List<PeriodoHorarioTrabalhoVO> findAllByHorario(HorarioTrabalhoVO horarioTrabalho) {
        StringBuilder query = new StringBuilder();
        query.append("select * from ").append(TBL_PERIODOS_HORA_TRABALHO)
                .append(" where ").append(PERIODO_HOR_TRAB_COL_HORARIO).append(EQ).append(QM)
                .append(AND).append(PERIODO_HOR_TRAB_COL_DIA_SEMANA).append(EQ).append(QM);

        int diaSemanaBD = Util.isDiaSemana() ? TIPO_DIA_DE_SEMANA : TIPO_FIM_DE_SEMANA;

        return bindList(findByQuery(query.toString(), concatArgs(horarioTrabalho.getId(), diaSemanaBD)));
    }

    @Override
    public PeriodoHorarioTrabalhoVO popularEntidade(Cursor cursor) {
        PeriodoHorarioTrabalhoVO periodoTrabalho = new PeriodoHorarioTrabalhoVO();
        HorarioTrabalhoVO horario = new HorarioTrabalhoVO(cursor.getInt(cursor.getColumnIndex(PERIODO_HOR_TRAB_COL_HORARIO)));

        periodoTrabalho.setHorario(horario);
        periodoTrabalho.setDiaSemana(cursor.getString(cursor.getColumnIndex(PERIODO_HOR_TRAB_COL_DIA_SEMANA)));
        periodoTrabalho.setHoraInicio(cursor.getString(cursor.getColumnIndex(PERIODO_HOR_TRAB_COL_HORA_INI)));
        periodoTrabalho.setHoraTermino(cursor.getString(cursor.getColumnIndex(PERIODO_HOR_TRAB_COL_HORA_TERM)));
        periodoTrabalho.setProdutivo(cursor.getString(cursor.getColumnIndex(PERIODO_HOR_TRAB_COL_PRODUTIVO)));
        periodoTrabalho.setCobraHoraExtra(cursor.getString(cursor.getColumnIndex(PERIODO_HOR_TRAB_COL_COBRA_HE)));
        periodoTrabalho.setCodigoParalisacao(cursor.getString(cursor.getColumnIndex(PERIODO_HOR_TRAB_COL_COD_PARAL)));

        return periodoTrabalho;
    }

    @Override
    public ContentValues bindContentValues(PeriodoHorarioTrabalhoVO periodoTrabalho) {
        ContentValues contentValues = new ContentValues();
        HorarioTrabalhoVO horarioTrabalho = periodoTrabalho.getHorario();

        contentValues.put(PERIODO_HOR_TRAB_COL_HORARIO, horarioTrabalho != null ? horarioTrabalho.getId() : null);
        contentValues.put(PERIODO_HOR_TRAB_COL_DIA_SEMANA, periodoTrabalho.getDiaSemana());
        contentValues.put(PERIODO_HOR_TRAB_COL_HORA_INI, periodoTrabalho.getHoraInicio());
        contentValues.put(PERIODO_HOR_TRAB_COL_HORA_TERM, periodoTrabalho.getHoraTermino());
        contentValues.put(PERIODO_HOR_TRAB_COL_PRODUTIVO, periodoTrabalho.getProdutivo());
        contentValues.put(PERIODO_HOR_TRAB_COL_COBRA_HE, periodoTrabalho.getCobraHoraExtra());
        contentValues.put(PERIODO_HOR_TRAB_COL_COD_PARAL, periodoTrabalho.getCodigoParalisacao());

        return contentValues;
    }

    @Override
    public boolean isNewEntity(PeriodoHorarioTrabalhoVO periodo) {
        return periodo != null && periodo.getDiaSemana() == null;
    }

    @Override
    public Object[] getPkArgs(PeriodoHorarioTrabalhoVO periodo) {
        return new Object[]{periodo.getHorario().getId(), periodo.getDiaSemana(), periodo.getHoraInicio()};
    }

    @Override
    public String[] getPkColumns() {
        return new String[]{PERIODO_HOR_TRAB_COL_HORARIO, PERIODO_HOR_TRAB_COL_DIA_SEMANA, PERIODO_HOR_TRAB_COL_HORA_INI};
    }
}
