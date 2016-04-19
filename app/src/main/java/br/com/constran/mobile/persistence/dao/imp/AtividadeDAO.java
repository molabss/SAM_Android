package br.com.constran.mobile.persistence.dao.imp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.R;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.imp.AtividadeVO;

public class AtividadeDAO extends AbstractDAO {

    private static final String ATIV_COL_FRENTES_OBRA = "frentesObra";
    private static final String ATIV_COL_ATIVIDADE = "atividade";
    private static final String ATIV_COL_DESCRICAO = "descricao";

    private static AtividadeDAO instance;

    private AtividadeDAO(Context context) {
        super(context, TBL_ATIVIDADE);
    }

    public static AtividadeDAO getInstance(Context context) {
        if (instance == null) {
            instance = new AtividadeDAO(context);
        }

        return instance;
    }

    public AtividadeVO[] getArrayAtividadeVO(Integer idFrenteObra) {

        AtividadeVO[] dados = new AtividadeVO[]{new AtividadeVO(getStr(R.string.SELECT))};

        if (idFrenteObra == null)
            return dados;

        String[] columns = new String[]{"at.[atividade]", "at.[descricao]"};
        String tableJoin = " [frentesObraAtividade] at ";
        tableJoin += " join [frentesObra]  f on at.[frentesObra] = f.[idFrentesObra]  ";
        String conditions = " f.[idFrentesObra] = " + idFrenteObra;
        String orderBy = " at.[descricao] asc";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        dados = new AtividadeVO[cursor.getCount() + 1];

        int i = 0;

        dados[i++] = new AtividadeVO(getStr(R.string.SELECT));

        while (cursor.moveToNext()) {
            dados[i++] = new AtividadeVO(cursor.getInt(cursor.getColumnIndex(ATIV_COL_ATIVIDADE)),
                    cursor.getString(cursor.getColumnIndex(ATIV_COL_DESCRICAO)));

        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public void save(AtividadeVO pVO) {
        insert(getContentValues(pVO));
    }

    public String getDescricao(Integer idFrenteObra, Integer idAtividade) {

        Query query = new Query(true);

        query.setColumns(new String[]{ATIV_COL_DESCRICAO});
        query.setTableJoin(" [frentesObraAtividade] ");
        query.setConditions(" [frentesObra] = ?  and [atividade] = ?");
        query.setConditionsArgs(new String[]{String.valueOf(idFrenteObra), String.valueOf(idAtividade)});

        Cursor cursor = getCursor(query);

        cursor.moveToNext();

        String value = cursor.getString(0).trim();

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return value;
    }

    public String getDescricao(Integer idApropriacao) {

        Query query = new Query(true);

        query.setColumns(new String[]{" FA.[descricao] "});

        String tableJoin = " [apropriacoes] A ";
        tableJoin += " join [frentesObraAtividade] FA on FA.[atividade] = A.[atividade] ";
        query.setTableJoin(tableJoin);

        query.setConditions(" A.[idApropriacao] = ? ");

        query.setConditionsArgs(new String[]{String.valueOf(idApropriacao)});

        Cursor cursor = getCursor(query);

        cursor.moveToNext();

        String value = cursor.getString(0).trim();

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return value;

    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        AtividadeVO pVO = (AtividadeVO) abstractVO;
        ContentValues contentValues = new ContentValues();
        contentValues.put(ATIV_COL_FRENTES_OBRA, pVO.getFrenteObra().getId());
        contentValues.put(ATIV_COL_ATIVIDADE, pVO.getIdAtividade());
        contentValues.put(ATIV_COL_DESCRICAO, pVO.getDescricao());

        return contentValues;
    }


}
