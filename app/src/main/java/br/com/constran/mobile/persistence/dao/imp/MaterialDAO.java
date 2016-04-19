package br.com.constran.mobile.persistence.dao.imp;

import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.R;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.imp.MaterialVO;

public class MaterialDAO extends AbstractDAO {

    private static final String MATERIAL_COL_ID_MATERIAL = "idMaterial";
    private static final String MATERIAL_COL_DESCRICAO = "descricao";

    private static MaterialDAO instance;

    private MaterialDAO(Context context) {
        super(context, TBL_MATERIAL);
    }

    public static MaterialDAO getInstance(Context context) {
        if (instance == null) {
            instance = new MaterialDAO(context);
        }

        return instance;
    }

    public MaterialVO[] getArrayMaterialVO() {

        Query query = new Query(true);

        query.setColumns(new String[]{MATERIAL_COL_ID_MATERIAL, "' ' || [descricao] " + ALIAS_DESCRICAO});

        query.setTableJoin(TBL_MATERIAL);

        query.setConditions("[idCategoria] =  5");

        query.setOrderBy("[descricao] ASC");

        Cursor cursor = getCursor(query);

        MaterialVO[] dados = new MaterialVO[cursor.getCount() + 1];

        int i = 0;

        dados[i++] = new MaterialVO(getStr(R.string.SELECT));

        while (cursor.moveToNext()) {
            dados[i++] = new MaterialVO(cursor.getInt(cursor.getColumnIndex(MATERIAL_COL_ID_MATERIAL)),
                    cursor.getString(cursor.getColumnIndex(ALIAS_DESCRICAO)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public MaterialVO[] getArrayEquipamentosMovimentacaoConsulta(String dataFiltro) {

        Query query = new Query(true);

        query.setColumns(new String[]{"m.[idMaterial]", "' ' || m.[descricao]"});

        query.setTableJoin("[materiais] m");

        query.setOrderBy("m.[descricao] ASC");

        String conditions = " m.[idCategoria] =  5 and exists (select 1 from [viagensMovimentacoes] ve where ve.[material] = m.[idMaterial]";

        conditions += " and substr(ve.[dataHoraCadastro],0, 11) = '" + dataFiltro + "')";

        query.setConditions(conditions);

        Cursor cursor = getCursor(query);

        MaterialVO[] dados = new MaterialVO[cursor.getCount()];

        int i = 0;

        while (cursor.moveToNext()) {
            dados[i++] = new MaterialVO(cursor.getInt(0), cursor.getString(1));
        }

        return dados;

    }


    public void save(MaterialVO pVO) {

        StringBuilder builder = new StringBuilder();

        builder.append(" insert into ");
        builder.append(TBL_MATERIAL);
        builder.append(" ([idMaterial], [descricao],");
        builder.append("  [idCategoria], [descricaoCategoria])");
        builder.append(" values ");
        builder.append("(");
        builder.append(pVO.getId());
        builder.append(",'");
        builder.append(pVO.getDescricao());
        builder.append("',");
        builder.append(pVO.getCategoria().getId());
        builder.append(",'");
        builder.append(pVO.getCategoria().getDescricao());
        builder.append("');");

        execute(builder);
    }

    public String getDescricao(Integer idMaterial) {

        Query query = new Query(true);

        query.setColumns(new String[]{MATERIAL_COL_DESCRICAO});
        query.setTableJoin(TBL_MATERIAL);
        query.setConditions(" [idMaterial] = ? ");
        query.setConditionsArgs(new String[]{String.valueOf(idMaterial)});

        Cursor cursor = getCursor(query);

        cursor.moveToNext();

        String value = cursor.getString(cursor.getColumnIndex(MATERIAL_COL_DESCRICAO)).trim();

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return value;
    }

}
