package br.com.constran.mobile.persistence.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.vo.ObraVO;

public class ObraDAO extends AbstractDAO {

    private static final String OBRA_COL_ID_OBRA = "idObra";
    private static final String OBRA_COL_DESC = "descricao";
    private static final String OBRA_COL_EXIBIR_HOR = "exibirHorimetro";
    private static final String OBRA_COL_HOR_OBRIGATORIO = "horimetroObrigatorio";
    private static final String OBRA_COL_USA_QRCODE = "usaQRCode";
    private static final String OBRA_COL_USA_QRCODE_PES = "usaQRCodePessoal";
    private static final String OBRA_COL_USA_ORIGEM_DEST = "usaOrigemDestino";
    private static final String OBRA_USA_PLAN_SERVICO = "usaPlanServico";

    private static ObraDAO instance;

    private ObraDAO(Context context) {
        super(context, TBL_OBRA);
    }

    public static ObraDAO getInstance(Context context) {
        if (instance == null) {
            instance = new ObraDAO(context);
        }

        return instance;
    }

    public ObraVO[] getAllObraVO() {

        Cursor cursor =  super.findAll();

        ObraVO[] dados = new ObraVO[cursor.getCount()];

        int i = 0;

        while (cursor.moveToNext()) {
            dados[i++] = new ObraVO(
                    cursor.getInt(cursor.getColumnIndex(OBRA_COL_ID_OBRA)),
                    cursor.getString(cursor.getColumnIndex(OBRA_COL_DESC)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public ObraVO[] getArrayObraVO(Integer[] ccObraArray) {

        Query query = new Query(true);

        query.setColumns(new String[]{OBRA_COL_ID_OBRA, OBRA_COL_DESC});

        query.setTableJoin(TBL_OBRA);

        int j = 0;

        if (ccObraArray != null) {

            StringBuilder conditions = new StringBuilder("[idObra] in (");

            for (Integer id : ccObraArray) {

                if (j != 0)
                    conditions.append(",");

                conditions.append(id);

                j++;
            }
            conditions.append(")");

            query.setConditions(conditions.toString());
        }

        query.setOrderBy("[idObra] asc , [descricao] asc");

        Cursor cursor = getCursor(query);

        ObraVO[] dados = new ObraVO[cursor.getCount()];

        int i = 0;

        while (cursor.moveToNext()) {
            dados[i++] = new ObraVO(
                    cursor.getInt(cursor.getColumnIndex(OBRA_COL_ID_OBRA)),
                    cursor.getString(cursor.getColumnIndex(OBRA_COL_DESC)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }


    public void save(ObraVO pVO) {
        insert(getContentValues(pVO));
    }

    public ObraVO getById(Integer idObra) {

        Query query = new Query(true);

        query.setColumns(new String[]{OBRA_COL_ID_OBRA, OBRA_COL_DESC, OBRA_COL_EXIBIR_HOR, OBRA_COL_HOR_OBRIGATORIO,
                OBRA_COL_USA_QRCODE, OBRA_COL_USA_ORIGEM_DEST, OBRA_COL_USA_QRCODE_PES,OBRA_USA_PLAN_SERVICO});

        query.setTableJoin("[obras]");

        query.setConditions("[idObra] = ? ");

        query.setConditionsArgs(new String[]{idObra.toString()});

        Cursor cursor = getCursor(query);

        ObraVO obra = null;

        if (cursor.moveToNext()) {
            obra = new ObraVO(getInt(cursor, OBRA_COL_ID_OBRA),
                    getString(cursor, OBRA_COL_DESC),
                    getString(cursor, OBRA_COL_EXIBIR_HOR),
                    getString(cursor, OBRA_COL_HOR_OBRIGATORIO));
            obra.setUsaQRCode(getString(cursor, OBRA_COL_USA_QRCODE));
            obra.setUsaOrigemDestino(getString(cursor, OBRA_COL_USA_ORIGEM_DEST));
            obra.setUsaQRCodePessoal(getString(cursor, OBRA_COL_USA_QRCODE_PES));
            obra.setUsaPlanServico(getString(cursor,OBRA_USA_PLAN_SERVICO));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return obra;
    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        ObraVO obraVO = (ObraVO) abstractVO;
        ContentValues contentValues = new ContentValues();
        contentValues.put(OBRA_COL_ID_OBRA, obraVO.getId());
        contentValues.put(OBRA_COL_DESC, obraVO.getDescricao());
        contentValues.put(OBRA_COL_EXIBIR_HOR, obraVO.getExibirHorimetro());
        contentValues.put(OBRA_COL_HOR_OBRIGATORIO, obraVO.getHorimetroObrigatorio());
        contentValues.put(OBRA_COL_USA_QRCODE, obraVO.getUsaQRCode());
        contentValues.put(OBRA_COL_USA_ORIGEM_DEST, obraVO.getUsaOrigemDestino());
        contentValues.put(OBRA_COL_USA_QRCODE_PES, obraVO.getUsaQRCodePessoal());
        contentValues.put(OBRA_USA_PLAN_SERVICO, obraVO.getUsaPlanServico());
        return contentValues;
    }
}
