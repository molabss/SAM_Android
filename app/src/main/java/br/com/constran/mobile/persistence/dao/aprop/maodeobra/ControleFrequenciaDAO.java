package br.com.constran.mobile.persistence.dao.aprop.maodeobra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.dao.aprop.BaseDAO;
import br.com.constran.mobile.persistence.vo.ObraVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.ControleFrequenciaVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.EquipeTrabalhoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.PessoalVO;

/**
 * Criado em 06/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class ControleFrequenciaDAO extends BaseDAO<ControleFrequenciaVO> {

    private static final String CONTROLE_FREQ_COL_CCOBRA = "idFornecedor";
    private static final String CONTROLE_FREQ_COL_PESSOA = "pessoa";
    private static final String CONTROLE_FREQ_COL_HORA_ENTRADA = "horaEntrada";
    private static final String CONTROLE_FREQ_COL_HORA_SAIDA = "horaSaida";
    private static final String CONTROLE_FREQ_COL_EQUIPE = "equipe";
    private static final String CONTROLE_FREQ_COL_OBS = "observacoes";

    private static ControleFrequenciaDAO instance;

    private ControleFrequenciaDAO(Context context) {
        super(context, TBL_CONTROLE_FREQUENCIA);
    }

    public static ControleFrequenciaDAO getInstance(Context context) {
        return instance == null ? instance = new ControleFrequenciaDAO(context) : instance;
    }

    @Override
    public ControleFrequenciaVO popularEntidade(Cursor cursor) {
        ControleFrequenciaVO controleFrequencia = new ControleFrequenciaVO();
        ObraVO obra = new ObraVO(cursor.getInt(cursor.getColumnIndex(CONTROLE_FREQ_COL_CCOBRA)));
        PessoalVO pessoal = new PessoalVO(cursor.getInt(cursor.getColumnIndex(CONTROLE_FREQ_COL_PESSOA)));
        EquipeTrabalhoVO equipe = new EquipeTrabalhoVO(cursor.getInt(cursor.getColumnIndex(CONTROLE_FREQ_COL_EQUIPE)));

        controleFrequencia.setHoraEntrada(cursor.getString(cursor.getColumnIndex(CONTROLE_FREQ_COL_HORA_ENTRADA)));
        controleFrequencia.setHoraSaida(cursor.getString(cursor.getColumnIndex(CONTROLE_FREQ_COL_HORA_SAIDA)));
        controleFrequencia.setObservacoes(cursor.getString(cursor.getColumnIndex(CONTROLE_FREQ_COL_OBS)));
        controleFrequencia.setObra(obra);
        controleFrequencia.setPessoa(pessoal);
        controleFrequencia.setEquipe(equipe);

        return controleFrequencia;
    }

    @Override
    public ContentValues bindContentValues(ControleFrequenciaVO controleFrequencia) {
        ContentValues contentValues = new ContentValues();
        ObraVO obra = controleFrequencia.getObra();
        PessoalVO pessoa = controleFrequencia.getPessoa();
        EquipeTrabalhoVO equipe = controleFrequencia.getEquipe();

        contentValues.put(CONTROLE_FREQ_COL_CCOBRA, obra != null ? obra.getId() : null);
        contentValues.put(CONTROLE_FREQ_COL_PESSOA, pessoa != null ? pessoa.getId() : null);
        contentValues.put(CONTROLE_FREQ_COL_HORA_ENTRADA, controleFrequencia.getHoraEntrada());
        contentValues.put(CONTROLE_FREQ_COL_HORA_SAIDA, controleFrequencia.getHoraSaida());
        contentValues.put(CONTROLE_FREQ_COL_EQUIPE, equipe != null ? equipe.getId() : null);
        contentValues.put(CONTROLE_FREQ_COL_OBS, controleFrequencia.getObservacoes());

        return contentValues;
    }


    @Override
    public boolean isNewEntity(ControleFrequenciaVO cf) {
        return cf != null && cf.getObra() == null;
    }

    @Override
    public Object[] getPkArgs(ControleFrequenciaVO cf) {
        return new Object[]{cf.getObra().getId(), cf.getPessoa().getId()};
    }

    @Override
    public String[] getPkColumns() {
        return new String[]{CONTROLE_FREQ_COL_CCOBRA, CONTROLE_FREQ_COL_PESSOA};
    }

}
