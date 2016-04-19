package br.com.constran.mobile.persistence.dao.imp.rae.abs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.rae.abs.CombustivelLubrificanteVO;

public class CombustivelLubrificanteDAO extends AbstractDAO {

    private static final String COMBUSTIVEL_LUBR_COL_ID_LUBRIF = "idCombustivelLubrificante";
    private static final String COMBUSTIVEL_LUBR_COL_DESC = "descricao";
    private static final String COMBUSTIVEL_LUBR_COL_UNID_MEDIDA = "unidadeMedida";
    private static final String COMBUSTIVEL_LUBR_COL_TIPO = "tipo";

    private static CombustivelLubrificanteDAO instance;

    private CombustivelLubrificanteDAO(Context context) {
        super(context, TBL_COMBUSTIVEL_LUBRIF);
    }

    public static CombustivelLubrificanteDAO getInstance(Context context) {
        if (instance == null) {
            instance = new CombustivelLubrificanteDAO(context);
        }

        return instance;
    }

    public CombustivelLubrificanteVO[] getArrayCombustivelLubrificanteVO(Integer idPosto, String[] combustiveis) {

        Query query = new Query(true);

        query.setColumns(new String[]{"c.[idCombustivelLubrificante]",
                "' ' || c.[descricao] " + ALIAS_DESCRICAO, "c.[unidadeMedida]", "c.[tipo]"});

        query.setTableJoin(" [combustiveisLubrificantes] c join [combustiveisPostos] cp on cp.[combustivel] = c.[idCombustivelLubrificante] ");

        StringBuilder conditions = new StringBuilder("cp.[posto] = ?");

        int j = 0;

        if (combustiveis != null) {
            conditions.append(" and c.[idCombustivelLubrificante] not in (");

            for (String id : combustiveis) {
                if (j != 0)
                    conditions.append(",");

                conditions.append(id);

                j++;
            }
            conditions.append(")");
        }

        query.setConditions(conditions.toString());

        query.setConditionsArgs(new String[]{idPosto.toString()});

        query.setOrderBy("[descricao]");

        Cursor cursor = getCursor(query);

        CombustivelLubrificanteVO[] dados = new CombustivelLubrificanteVO[cursor.getCount()];

        int i = 0;

        while (cursor.moveToNext()) {
            dados[i++] = new CombustivelLubrificanteVO(
                    cursor.getInt(cursor.getColumnIndex(COMBUSTIVEL_LUBR_COL_ID_LUBRIF)),
                    cursor.getString(cursor.getColumnIndex(ALIAS_DESCRICAO)),
                    cursor.getString(cursor.getColumnIndex(COMBUSTIVEL_LUBR_COL_UNID_MEDIDA)),
                    cursor.getString(cursor.getColumnIndex(COMBUSTIVEL_LUBR_COL_TIPO)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public CombustivelLubrificanteVO[] getArrayCombustivelLubrificanteVO(Integer idPosto) {

        Query query = new Query(true);

        query.setColumns(new String[]{"c.[idCombustivelLubrificante]",
                "' ' || c.[descricao] " + ALIAS_DESCRICAO, "c.[unidadeMedida]", "c.[tipo]"});

        query.setTableJoin(" [combustiveisLubrificantes] c join [combustiveisPostos] cp on cp.[combustivel] = c.[idCombustivelLubrificante] ");

        StringBuilder conditions = new StringBuilder("cp.[posto] = ?");

        query.setConditions(conditions.toString());

        query.setConditionsArgs(new String[]{idPosto.toString()});

        query.setOrderBy("[descricao]");

        Cursor cursor = getCursor(query);

        CombustivelLubrificanteVO[] dados = new CombustivelLubrificanteVO[cursor.getCount()];

        int i = 0;

        while (cursor.moveToNext()) {
            dados[i++] = new CombustivelLubrificanteVO(
                    cursor.getInt(cursor.getColumnIndex(COMBUSTIVEL_LUBR_COL_ID_LUBRIF)),
                    cursor.getString(cursor.getColumnIndex(ALIAS_DESCRICAO)),
                    cursor.getString(cursor.getColumnIndex(COMBUSTIVEL_LUBR_COL_UNID_MEDIDA)),
                    cursor.getString(cursor.getColumnIndex(COMBUSTIVEL_LUBR_COL_TIPO)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public CombustivelLubrificanteVO getById(Integer idCombustivelLubrificante) {

        Query query = new Query(true);

        query.setColumns(new String[]{COMBUSTIVEL_LUBR_COL_ID_LUBRIF, COMBUSTIVEL_LUBR_COL_DESC,
                COMBUSTIVEL_LUBR_COL_UNID_MEDIDA, COMBUSTIVEL_LUBR_COL_TIPO});

        query.setTableJoin(TBL_COMBUSTIVEL_LUBRIF);

        query.setConditions("[idCombustivelLubrificante] = ? ");

        query.setConditionsArgs(new String[]{idCombustivelLubrificante.toString()});

        Cursor cursor = getCursor(query);

        CombustivelLubrificanteVO combustivelLubrificante = null;

        if (cursor.moveToNext()) {
            combustivelLubrificante = new CombustivelLubrificanteVO(
                    cursor.getInt(cursor.getColumnIndex(COMBUSTIVEL_LUBR_COL_ID_LUBRIF)),
                    cursor.getString(cursor.getColumnIndex(COMBUSTIVEL_LUBR_COL_DESC)),
                    cursor.getString(cursor.getColumnIndex(COMBUSTIVEL_LUBR_COL_UNID_MEDIDA)),
                    cursor.getString(cursor.getColumnIndex(COMBUSTIVEL_LUBR_COL_TIPO)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return combustivelLubrificante;
    }

    public void save(CombustivelLubrificanteVO pVO) {
        insert(getContentValues(pVO));
    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        CombustivelLubrificanteVO pVO = (CombustivelLubrificanteVO) abstractVO;

        ContentValues contentValues = new ContentValues();
        contentValues.put(COMBUSTIVEL_LUBR_COL_ID_LUBRIF, pVO.getId());
        contentValues.put(COMBUSTIVEL_LUBR_COL_DESC, pVO.getDescricao());
        contentValues.put(COMBUSTIVEL_LUBR_COL_UNID_MEDIDA, pVO.getUnidadeMedida());
        contentValues.put(COMBUSTIVEL_LUBR_COL_TIPO, pVO.getTipo());

        return contentValues;
    }
}
