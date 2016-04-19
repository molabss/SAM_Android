package br.com.constran.mobile.view.indicepluviometrico;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import br.com.constran.mobile.R;
import br.com.constran.mobile.adapter.indicepluviometrico.IndicePluviometricoAdapter;
import br.com.constran.mobile.persistence.vo.aprop.indpluv.IndicePluviometricoVO;
import br.com.constran.mobile.view.maodeobra.GridActivity;

import java.util.List;

/**
 * @author Mateus Vitali <mateus.vitali@constran.com.br>
 * @version 1.0
 * @since 2014-09-01
 */
public class IndicePluviometricoServicoGridActivity extends GridActivity<IndicePluviometricoAdapter> {

    /**
     * GridView para listagem dos Indices Pluviométricos
     */
    private GridView indicePluviometricoGridView;

    /**
     * Botão no cabeçalho do GridView para Adicionar um Indice Pluviométrico
     */
    private Button btnAddPluviometro;

    /**
     * Lista de Indices Pluviometricos para preencher a GridView
     */
    private List<IndicePluviometricoVO> indicesPluviometrico;

    /**
     * VO do Indice selecionado
     */
    private IndicePluviometricoVO indicePluviometricoSelecionado;

    /**
     * String chave para VO que é passado como parametro via intent
     */
    static final String PARAM_PLUVIOMETRO_SELECIONADO = "indicePluviometricoSelecionda";

    @Override
    protected void onResume() {
        super.onResume();

        if (indicePluviometricoSelecionado == null) {
            indicesPluviometrico = dao.getIndicePluviometricoDAO().findDistinctList();
            setAdapter(new IndicePluviometricoAdapter(this, indicesPluviometrico));
            indicePluviometricoGridView.setAdapter(getAdapter());
        }

        showHideEmptyText(indicesPluviometrico);
    }

    @Override
    protected int getContentView() {
        return R.layout.indice_pluviometrico_servico;
    }

    @Override
    public void initAttributes() {
        indicePluviometricoGridView = (GridView) findViewById(R.id.indicePluviometricoServicoGridView);
        btnAddPluviometro = (Button) findViewById(R.id.btnAddPluviometro);
        gridVazioTextView = (TextView) findViewById(R.id.empty_list_view);
    }

    @Override
    public void bindComponents() {
        indicesPluviometrico = dao.getIndicePluviometricoDAO().findDistinctList();
    }

    @Override
    protected void initAdapter() {
        setAdapter(new IndicePluviometricoAdapter(this, indicesPluviometrico));
        indicePluviometricoGridView.setAdapter(getAdapter());

        showHideEmptyText(indicesPluviometrico);
    }

    @Override
    public void addListeners() {
        addIndicePluviometricoGridView();
        addBtnAddPluviometroListener();
    }

    @Override
    protected void selecionarGridItem(int i) {
        IndicePluviometricoVO indicePluviometricoVO = indicesPluviometrico.get(i);

        Intent intent = new Intent(this, AddIndicePluviometricoActivity.class);
        intent.putExtra(PARAM_PLUVIOMETRO_SELECIONADO, indicePluviometricoVO);
        intent.putExtra(getStr(R.string.STRING_INTENT_PARAMS), intentParameters);

        startActivity(intent);
    }

    @Override
    protected void excluirGridItem(int i) {
        IndicePluviometricoVO indicePluviometricoVO = indicesPluviometrico.get(i);
        dao.getIndicePluviometricoDAO().delete(indicePluviometricoVO);
        indicesPluviometrico.remove(i);

        setAdapter(new IndicePluviometricoAdapter(this, indicesPluviometrico));
        indicePluviometricoGridView.setAdapter(getAdapter());
        indicePluviometricoSelecionado = null;
    }

    private void addIndicePluviometricoGridView() {
        indicePluviometricoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                gridClickListener(adapterView, view, i, l);
            }
        });
    }

    private void addBtnAddPluviometroListener() {
        btnAddPluviometro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IndicePluviometricoServicoGridActivity.this, AddIndicePluviometricoActivity.class);
                intent.putExtra(getStr(R.string.STRING_INTENT_PARAMS), intentParameters);
                startActivity(intent);
            }
        });
    }
}

