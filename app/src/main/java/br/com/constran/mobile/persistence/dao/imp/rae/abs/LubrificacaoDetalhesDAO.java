package br.com.constran.mobile.persistence.dao.imp.rae.abs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.R;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.rae.RaeVO;
import br.com.constran.mobile.persistence.vo.rae.abs.AbastecimentoVO;
import br.com.constran.mobile.persistence.vo.rae.abs.CompartimentoVO;
import br.com.constran.mobile.persistence.vo.rae.abs.LubrificacaoDetalheVO;

import java.util.ArrayList;
import java.util.List;

public class LubrificacaoDetalhesDAO extends AbstractDAO {

    private static final String LUBRIF_DET_COL_ID_COMPARTIM = "idCompartimento";
    private static final String LUBRIF_DET_COL_DESC = "descricao";
    private static final String LUBRIF_DET_COL_ID_CATEGORIA = "idCategoria";
    private static final String LUBRIF_DET_COL_TIPO = "tipo";
    private static final String LUBRIF_DET_HORA_INI = "horaInicio";
    private static final String LUBRIF_DET_OBS = "observacoes";
    private static final String LUBRIF_DET_RAE = "rae";
    private static final String LUBRIF_DET_EQUIP = "equipamento";
    private static final String LUBRIF_DET_LUBRIF = "lubrificante";
    private static final String LUBRIF_DET_COMPARTIM = "compartimento";
    private static final String LUBRIF_DET_CATEGORIA = "categoria";
    private static final String LUBRIF_DET_QTE = "quantidade";

    private static LubrificacaoDetalhesDAO instance;

    private LubrificacaoDetalhesDAO(Context context) {
        super(context, TBL_LUBRIFICANTE_DET);
    }

    public static LubrificacaoDetalhesDAO getInstance(Context context) {
        if (instance == null) {
            instance = new LubrificacaoDetalhesDAO(context);
        }

        return instance;
    }

    public List<LubrificacaoDetalheVO> getDetalhes(Integer idEquipamento) {

        List<LubrificacaoDetalheVO> lista = new ArrayList<LubrificacaoDetalheVO>();

        Query query = new Query(true);

        query.setColumns(new String[]{"c.[idCompartimento]", "c.[descricao]", "c.[idCategoria]", "c.[tipo]"});

        query.setTableJoin("[compartimentos] c join [equipamentos] e on e.[idCategoria] = c.[idCategoria]");

        query.setConditions("c.[tipo] = 'L' and e.[idEquipamento] = " + idEquipamento);

        query.setOrderBy("c.[descricao] ASC");

        Cursor cursor = getCursor(query);

        while (cursor.moveToNext()) {

            CompartimentoVO cVO = new CompartimentoVO(
                    cursor.getInt(cursor.getColumnIndex(LUBRIF_DET_COL_ID_COMPARTIM)),
                    cursor.getString(cursor.getColumnIndex(LUBRIF_DET_COL_DESC)),
                    cursor.getInt(cursor.getColumnIndex(LUBRIF_DET_COL_ID_CATEGORIA)),
                    cursor.getString(cursor.getColumnIndex(LUBRIF_DET_COL_TIPO)));

            LubrificacaoDetalheVO lVO = new LubrificacaoDetalheVO();
            lVO.setCompartimento(cVO);
            lVO.setQtd("0");

            lista.add(lVO);
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return lista;
    }

    public void save(RaeVO pRae, AbastecimentoVO pCab, LubrificacaoDetalheVO pLub) {
        insert(getContentValues(pRae, pCab, pLub));
    }

    private ContentValues getContentValues(RaeVO pRae, AbastecimentoVO pCab, LubrificacaoDetalheVO pLub) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(LUBRIF_DET_HORA_INI, pCab.getHoraInicio());
        contentValues.put(LUBRIF_DET_OBS, pLub.getObservacao() == null ? getStr(R.string.EMPTY) : pLub.getObservacao());
        contentValues.put(LUBRIF_DET_RAE, pRae.getId());
        contentValues.put(LUBRIF_DET_EQUIP, pCab.getEquipamento().getId());
        contentValues.put(LUBRIF_DET_LUBRIF, pLub.getIdCombustivelLubrificante());
        contentValues.put(LUBRIF_DET_COMPARTIM, pLub.getCompartimento().getId());
        contentValues.put(LUBRIF_DET_CATEGORIA, pLub.getCompartimento().getCategoria().getId());
        contentValues.put(LUBRIF_DET_QTE, pLub.getQtd());

        return contentValues;
    }
}
