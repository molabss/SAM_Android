package br.com.constran.mobile.view.maodeobra;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.*;
import br.com.constran.mobile.R;
import br.com.constran.mobile.adapter.maodeobra.AjusteIndividualAdapter;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.EquipeTrabalhoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.IntegranteTempVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.IntegranteVO;
import br.com.constran.mobile.persistence.vo.menu.LocalizacaoVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Criado em 11/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class AjusteEquipeGridActivity extends GridActivity<AjusteIndividualAdapter> {
    private static final int REQUEST_CODE = 1;

    private GridView integranteEquipeGridView;
    private AutoCompleteTextView equipeAutoComplete;
    private Button btnSalvar;

    private List<EquipeTrabalhoVO> equipes;
    private List<IntegranteVO> integranteList;
    private List<? extends IntegranteVO> temporarioList;

    private LocalizacaoVO localSelecionado;
    private EquipeTrabalhoVO equipeSelecionada;
    private IntegranteVO integranteSelecionado;

    private ArrayAdapter<EquipeTrabalhoVO> equipeAdapter;

    @Override
    protected int getContentView() {
        return R.layout.ajuste_equipe;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (equipeSelecionada != null) {
            equipeAutoComplete.setAdapter(null);
            equipeAutoComplete.setText(equipeSelecionada.getApelido());
            equipeAutoComplete.setAdapter(equipeAdapter);
        }
    }

    @Override
    protected void initAttributes() {
        integranteEquipeGridView = (GridView) findViewById(R.id.integranteEquipeGridView);
        equipeAutoComplete = (AutoCompleteTextView) findViewById(R.id.equipeAutoComplete);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        gridVazioTextView = (TextView) findViewById(R.id.empty_list_view);
    }

    @Override
    protected void bindComponents() {
        btnSalvar.setVisibility(View.GONE);
        equipeSelecionada = (EquipeTrabalhoVO) getIntent().getSerializableExtra(PARAM_EQUIPE_SELECIONADA);
        localSelecionado = (LocalizacaoVO) getIntent().getSerializableExtra(PARAM_LOCAL_SELECIONADO);
        dataSelecionada = getIntent().getStringExtra(PARAM_DATA_SELECIONADA);

        //Log.i("LOCAL",localSelecionado.getDescricao());

        equipes = dao.getEquipeTrabalhoDAO().findByLocalizacao(localSelecionado);

        equipeAdapter = new ArrayAdapter<EquipeTrabalhoVO>(this, android.R.layout.select_dialog_singlechoice, equipes);
        equipeAutoComplete.setAdapter(equipeAdapter);
        equipeAutoComplete.setKeyListener(null);
    }

    @Override
    protected void initAdapter() {
        updateGrid();
    }

    private void updateGrid() {

        if (equipeSelecionada != null) {

            integranteList = (List<IntegranteVO>) dao.getIntegranteEquipeDAO().findPresentesByEquipe(equipeSelecionada, dataSelecionada);
            temporarioList = dao.getIntegranteTempDAO().findByEquipe(equipeSelecionada, dataSelecionada);

            //Log.i("EQUIPE_PRESENTES",equipeSelecionada+"");
            //Log.i("EQUIPE_PRESENTES",dataSelecionada+"");
            //Log.i("EQUIPE_PRESENTES",integranteList+"");
            //Log.i("EQUIPE_PRESENTES",temporarioList+"");
        }

        if (integranteList == null) {
            integranteList = new ArrayList<IntegranteVO>();
        }
        if (temporarioList == null) {
            temporarioList = new ArrayList<IntegranteTempVO>();
        }

        integranteList.addAll(temporarioList);
        //ordena por nome do integrante
        Collections.sort(integranteList);

        setAdapter(new AjusteIndividualAdapter(this, integranteList));
        integranteEquipeGridView.setAdapter(getAdapter());

        showHideEmptyText(integranteList);
    }

    @Override
    protected void addListeners() {
        addIntegranteGridViewListener();
//        addEquipeAutoCompleteListener();
    }

    private void addIntegranteGridViewListener() {
        integranteEquipeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                gridClickListener(adapterView, view, i, l);
            }
        });
    }

    @Override
    protected void editarGridItem(int i) {
        integranteSelecionado = integranteList.get(i);
        Intent intent = new Intent(this, AjusteIndividualGridActivity.class);
        intent.putExtra(PARAM_LOCAL_SELECIONADO, localSelecionado);
        intent.putExtra(PARAM_EQUIPE_SELECIONADA, equipeSelecionada);
        intent.putExtra(PARAM_INTEGRANTE_SELECIONADO, integranteSelecionado);
        intent.putExtra(PARAM_DATA_SELECIONADA, dataSelecionada);
        intent.putExtra(getStr(R.string.STRING_INTENT_PARAMS), intentParameters);

        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            updateGrid();
        }
    }
}
