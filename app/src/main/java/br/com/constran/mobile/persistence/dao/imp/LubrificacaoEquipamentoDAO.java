package br.com.constran.mobile.persistence.dao.imp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.R;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.imp.LubrificacaoEquipamentoVO;

public class LubrificacaoEquipamentoDAO extends AbstractDAO {

    private static final String LUBRIF_EQUIP_COL_DATA = "data";
    private static final String LUBRIF_EQUIP_COL_HORIMETRO = "horimetro";
    private static final String LUBRIF_EQUIP_COL_QUILOMET = "quilometragem";
    private static final String LUBRIF_EQUIP_COL_EQUIP = "equipamento";
    private static final String LUBRIF_EQUIP_COL_COMPARTIM = "compartimento";
    private static final String LUBRIF_EQUIP_COL_CATEG = "categoria";

    private static LubrificacaoEquipamentoDAO instance;

    private LubrificacaoEquipamentoDAO(Context context) {
        super(context, TBL_LUBRIFICACAO_EQUIP);
    }

    public static LubrificacaoEquipamentoDAO getInstance(Context context) {
        if (instance == null) {
            instance = new LubrificacaoEquipamentoDAO(context);
        }

        return instance;
    }

    public Cursor getCursor(Integer idEquipamento) {

        Query query = new Query(true);

        query.setColumns(new String[]{
                "c.[descricao]",
                "l.[quilometragem]",
                "l.[horimetro]",
                "ifnull(abs(e.[quilometragem] - l.quilometragem), abs(e.[horimetro] - l.[horimetro]))"
        });

        StringBuilder builder = new StringBuilder();

        builder.append("[lubrificacoesEquipamento] l join equipamentos e on l.equipamento = e.idEquipamento");

        builder.append(" join compartimentos c on c.idCompartimento = l.compartimento and c.idCategoria = l.categoria ");

        query.setTableJoin(builder.toString());

        query.setConditions("[equipamento] = ? ");

        query.setConditionsArgs(new String[]{idEquipamento.toString()});

        return getCursor(query);
    }

    public void save(LubrificacaoEquipamentoVO pLub) {
        insert(getContentValues(pLub));
    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        LubrificacaoEquipamentoVO pLub = (LubrificacaoEquipamentoVO) abstractVO;
        ContentValues contentValues = new ContentValues();
        contentValues.put(LUBRIF_EQUIP_COL_DATA, pLub.getData());
        contentValues.put(LUBRIF_EQUIP_COL_HORIMETRO, pLub.getHorimetro() != null ? pLub.getHorimetro() : getStr(R.string.EMPTY));
        contentValues.put(LUBRIF_EQUIP_COL_QUILOMET, pLub.getQuilometragem() != null ? pLub.getQuilometragem() : getStr(R.string.EMPTY));
        contentValues.put(LUBRIF_EQUIP_COL_EQUIP, pLub.getIdEquipamento());
        contentValues.put(LUBRIF_EQUIP_COL_COMPARTIM, pLub.getIdCompartimento());
        contentValues.put(LUBRIF_EQUIP_COL_CATEG, pLub.getIdCategoria());

        return contentValues;
    }
}
