package br.com.constran.mobile.persistence.dao.aprop.maodeobra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.dao.aprop.BaseDAO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.EquipamentoEquipeVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.EquipeTrabalhoVO;
import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;
import br.com.constran.mobile.persistence.vo.imp.ServicoVO;

/**
 * Criado em 06/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class EquipamentoEquipeDAO extends BaseDAO<EquipamentoEquipeVO> {

    private static final String EQUIPAMENTO_EQUIPE_COL_EQUIPE = "equipe";
    private static final String EQUIPAMENTO_EQUIPE_COL_EQUIPAMENTO = "equipamento";
    private static final String EQUIPAMENTO_EQUIPE_COL_DATA_INGRESSO = "dataIngresso";
    private static final String EQUIPAMENTO_EQUIPE_COL_DATA_SAIDA = "dataSaida";
    private static final String EQUIPAMENTO_EQUIPE_COL_SERVICO_PADRAO = "servicoPadrao";

    private static EquipamentoEquipeDAO instance;

    private EquipamentoEquipeDAO(Context context) {
        super(context, TBL_EQUIPAMENTOS_EQUIPE);
    }

    public static EquipamentoEquipeDAO getInstance(Context context) {
        return instance == null ? instance = new EquipamentoEquipeDAO(context) : instance;
    }

    @Override
    public EquipamentoEquipeVO popularEntidade(Cursor cursor) {
        EquipamentoEquipeVO equipamentoEquipe = new EquipamentoEquipeVO();
        EquipeTrabalhoVO equipe = new EquipeTrabalhoVO(cursor.getInt(cursor.getColumnIndex(EQUIPAMENTO_EQUIPE_COL_EQUIPE)));
        EquipamentoVO equipamento = new EquipamentoVO(cursor.getInt(cursor.getColumnIndex(EQUIPAMENTO_EQUIPE_COL_EQUIPAMENTO)));
        ServicoVO servico = new ServicoVO(cursor.getString(cursor.getColumnIndex(EQUIPAMENTO_EQUIPE_COL_SERVICO_PADRAO)));

        equipamentoEquipe.setDataIngresso(cursor.getString(cursor.getColumnIndex(EQUIPAMENTO_EQUIPE_COL_DATA_INGRESSO)));
        equipamentoEquipe.setDataSaida(cursor.getString(cursor.getColumnIndex(EQUIPAMENTO_EQUIPE_COL_DATA_SAIDA)));
        equipamentoEquipe.setEquipe(equipe);
        equipamentoEquipe.setEquipamento(equipamento);
        equipamentoEquipe.setServicoPadrao(servico);

        return equipamentoEquipe;
    }

    @Override
    public ContentValues bindContentValues(EquipamentoEquipeVO equipamentoEquipe) {
        ContentValues contentValues = new ContentValues();

        EquipeTrabalhoVO equipe = equipamentoEquipe.getEquipe();
        EquipamentoVO equipamento = equipamentoEquipe.getEquipamento();
        ServicoVO servicoPadrao = equipamentoEquipe.getServicoPadrao();

        contentValues.put(EQUIPAMENTO_EQUIPE_COL_EQUIPE, equipe != null ? equipe.getId() : null);
        contentValues.put(EQUIPAMENTO_EQUIPE_COL_EQUIPAMENTO, equipamento != null ? equipamento.getId() : null);
        contentValues.put(EQUIPAMENTO_EQUIPE_COL_SERVICO_PADRAO, servicoPadrao != null ? servicoPadrao.getId() : null);
        contentValues.put(EQUIPAMENTO_EQUIPE_COL_DATA_INGRESSO, equipamentoEquipe.getDataIngresso());
        contentValues.put(EQUIPAMENTO_EQUIPE_COL_DATA_SAIDA, equipamentoEquipe.getDataSaida());

        return contentValues;
    }

    @Override
    public boolean isNewEntity(EquipamentoEquipeVO ee) {
        return ee != null && ee.getEquipe() == null || ee.getEquipe().getId() == null;
    }

    @Override
    public Object[] getPkArgs(EquipamentoEquipeVO ee) {
        return new Object[]{ee.getEquipe().getId(), ee.getEquipamento().getId(), ee.getDataIngresso()};
    }

    @Override
    public String[] getPkColumns() {
        return new String[]{EQUIPAMENTO_EQUIPE_COL_EQUIPE, EQUIPAMENTO_EQUIPE_COL_EQUIPAMENTO, EQUIPAMENTO_EQUIPE_COL_DATA_INGRESSO};
    }

}
