package br.com.constran.mobile.persistence.dao.imp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.R;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.dao.DAOFactory;
import br.com.constran.mobile.persistence.vo.imp.OrigemDestinoVO;

public class OrigemDestinoDAO extends AbstractDAO {
    private static final String ORIG_DEST_COL_ID_ORIG_DEST = "idOrigensDestinos";
    private static final String ORIG_DEST_COL_DESC = "descricao";
    private static final String ORIG_DEST_COL_ESTACA_INI = "estacaInicial";
    private static final String ORIG_DEST_COL_ESTACA_FIM = "estacaFinal";
    private static final String ORIG_DEST_COL_TIPO = "tipo";
    private static final String ORIG_DEST_COL_DESC_TIPO = "descricaoTipo";

    private static OrigemDestinoDAO instance;

    private OrigemDestinoDAO(Context context) {
        super(context, TBL_ORIGEM_DESTINO);
    }

    public static OrigemDestinoDAO getInstance(Context context) {
        if (instance == null) {
            instance = new OrigemDestinoDAO(context);
        }

        return instance;
    }

    public OrigemDestinoVO[] getArrayOrigemDestinoVO(String tipo, String estacaIni, String estacaFim) {

        OrigemDestinoVO[] dados = new OrigemDestinoVO[]{new OrigemDestinoVO(getStr(R.string.SELECT))};

        Query query = new Query(true);

        StringBuilder conditions = new StringBuilder(getStr(R.string.EMPTY));

        if (estacaIni != null && estacaIni.length() == 6 && estacaFim != null && estacaFim.length() == 6) {

            Integer tolerancia = DAOFactory.getInstance(context).getConfiguracoesDAO().getConfiguracaoVO().getTolerancia();

            if (tolerancia == null) {
                tolerancia = 0;
            }

            int inicio = Integer.parseInt(estacaIni);
            int fim = Integer.parseInt(estacaFim);


            if (inicio > tolerancia) {
                inicio -= tolerancia;
            }

            fim += tolerancia;

            conditions.append("(( cast([estacaInicial] as integer) between " + inicio + "  and " + fim + ")");
            conditions.append(" or ");
            conditions.append("( cast([estacaFinal] as integer) between " + inicio + "  and " + fim + "))");

            if (tipo.equals(getStr(R.string.ORIGEM))) {
                conditions.append(" and ([tipo] = '0' or [tipo] = '2' or [tipo] = '3' or [tipo] = '5') ");
            } else if (tipo.equals(getStr(R.string.DESTINO))) {
                conditions.append(" and ([tipo] = '0' or [tipo] = '1' or [tipo] = '4' ) ");
            }

        } else
            return dados;

        query.setColumns(new String[]{ORIG_DEST_COL_ID_ORIG_DEST, ORIG_DEST_COL_DESC, ORIG_DEST_COL_ESTACA_INI,
                ORIG_DEST_COL_ESTACA_FIM, ORIG_DEST_COL_TIPO});
        query.setTableJoin(TBL_ORIGEM_DESTINO);
        query.setOrderBy(" [descricao] ASC ");
        query.setConditions(conditions.toString());

        Cursor cursor = getCursor(query);

        dados = new OrigemDestinoVO[cursor.getCount() + 1];

        int i = 0;

        dados[i++] = new OrigemDestinoVO(getStr(R.string.SELECT));

        while (cursor.moveToNext()) {
            dados[i++] = new OrigemDestinoVO(cursor.getInt(cursor.getColumnIndex(ORIG_DEST_COL_ID_ORIG_DEST)),
                    cursor.getString(cursor.getColumnIndex(ORIG_DEST_COL_DESC)),
                    cursor.getString(cursor.getColumnIndex(ORIG_DEST_COL_ESTACA_INI)),
                    cursor.getString(cursor.getColumnIndex(ORIG_DEST_COL_ESTACA_FIM)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public OrigemDestinoVO[] getArrayOrigemDestinoVO() {

        OrigemDestinoVO[] dados = new OrigemDestinoVO[]{new OrigemDestinoVO(getStr(R.string.SELECT))};

        Query query = new Query(true);

        query.setColumns(new String[]{ORIG_DEST_COL_ID_ORIG_DEST, ORIG_DEST_COL_DESC});
        query.setTableJoin(TBL_ORIGEM_DESTINO);
        query.setOrderBy("[descricao] ASC");

        Cursor cursor = getCursor(query);

        dados = new OrigemDestinoVO[cursor.getCount() + 1];

        int i = 0;

        dados[i++] = new OrigemDestinoVO(getStr(R.string.SELECT));

        while (cursor.moveToNext()) {
            dados[i++] = new OrigemDestinoVO(cursor.getInt(cursor.getColumnIndex(ORIG_DEST_COL_ID_ORIG_DEST)),
                    cursor.getString(cursor.getColumnIndex(ORIG_DEST_COL_DESC)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public void save(OrigemDestinoVO pVO) {
        insert(getContentValues(pVO));
    }

    public String getTipo(Integer idOrigem) {

        Query query = new Query(true);

        query.setColumns(new String[]{ORIG_DEST_COL_TIPO});
        query.setTableJoin(TBL_ORIGEM_DESTINO);
        query.setConditions(" [idOrigensDestinos] = ? ");
        query.setConditionsArgs(new String[]{String.valueOf(idOrigem)});

        Cursor cursor = getCursor(query);

        cursor.moveToNext();

        String value = cursor.getString(cursor.getColumnIndex(ORIG_DEST_COL_TIPO)).trim();

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return value;
    }

    public String getDescricao(String idOrigemDestino) {
        if (idOrigemDestino != null) {
            return getDescricao(Integer.valueOf(idOrigemDestino));
        }

        return getStr(R.string.EMPTY);
    }

    public String getDescricao(Integer idOrigemDestino) {
        Query query = new Query(true);

        query.setColumns(new String[]{ORIG_DEST_COL_DESC});
        query.setTableJoin(TBL_ORIGEM_DESTINO);
        query.setConditions(" [idOrigensDestinos] = ? ");
        query.setConditionsArgs(new String[]{String.valueOf(idOrigemDestino)});

        Cursor cursor = getCursor(query);

        cursor.moveToNext();

        String value = cursor.getString(cursor.getColumnIndex(ORIG_DEST_COL_DESC)).trim();

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return value;
    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        OrigemDestinoVO pVO = (OrigemDestinoVO) abstractVO;
        ContentValues contentValues = new ContentValues();
        contentValues.put(ORIG_DEST_COL_ID_ORIG_DEST, pVO.getId());
        contentValues.put(ORIG_DEST_COL_DESC, pVO.getDescricao());
        contentValues.put(ORIG_DEST_COL_ESTACA_INI, pVO.getEstacaInicial());
        contentValues.put(ORIG_DEST_COL_ESTACA_FIM, pVO.getEstacaFinal());
        contentValues.put(ORIG_DEST_COL_TIPO, pVO.getTipo());
        contentValues.put(ORIG_DEST_COL_DESC_TIPO, pVO.getDescricaoTipo());

        return contentValues;
    }

}
