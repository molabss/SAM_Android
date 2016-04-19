package br.com.constran.mobile.view.maodeobra;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import br.com.constran.mobile.R;
import br.com.constran.mobile.adapter.maodeobra.MaoObraServicoAdapter;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.EventoEquipeVO;
import br.com.constran.mobile.view.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Criado em 11/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class MaoObraServicoGridActivity extends GridActivity<MaoObraServicoAdapter> {

    private GridView eventoEquipeGridView;

    private AutoCompleteTextView eventoEquipeAutoComplete;

    private Button btnAddEquipe;

    private List<EventoEquipeVO> eventosEquipe;

    private EventoEquipeVO eventoEquipeSelecionda;

    @Override
    protected void onResume() {
        super.onResume();

        if (eventoEquipeSelecionda == null) {
            eventosEquipe = dao.getEventoEquipeDAO().findDistinctList();
            eventoEquipeAutoComplete.setAdapter(new ArrayAdapter<EventoEquipeVO>(this, android.R.layout.select_dialog_singlechoice, eventosEquipe));
            setAdapter(new MaoObraServicoAdapter(this, eventosEquipe));
            eventoEquipeGridView.setAdapter(getAdapter());
        }

        showHideEmptyText(eventosEquipe);
    }

    @Override
    protected int getContentView() {
        return R.layout.mao_obra_servico;
    }

    @Override
    public void initAttributes() {
        eventoEquipeGridView = (GridView) findViewById(R.id.apropriacaoServicoGridView);
        eventoEquipeAutoComplete = (AutoCompleteTextView) findViewById(R.id.eventoEquipeAutoComplete);
        btnAddEquipe = (Button) findViewById(R.id.btnAddEquipe);
        gridVazioTextView = (TextView) findViewById(R.id.empty_list_view);
    }

    @Override
    public void bindComponents() {
        eventosEquipe = dao.getEventoEquipeDAO().findDistinctList();
    }

    @Override
    protected void initAdapter() {
        eventoEquipeAutoComplete.setAdapter(new ArrayAdapter<EventoEquipeVO>(this, android.R.layout.select_dialog_singlechoice, eventosEquipe));
        setAdapter(new MaoObraServicoAdapter(this, eventosEquipe));
        eventoEquipeGridView.setAdapter(getAdapter());

        showHideEmptyText(eventosEquipe);
    }

    @Override
    public void addListeners() {
        addBtnAddEquipeListener();
        addEventoEquipeGridView();
        addEventoEquipeAutoComplete();
    }

    private void addBtnAddEquipeListener() {
        btnAddEquipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MaoObraServicoGridActivity.this, EventoEquipeGridActivity.class);
                intent.putExtra(getStr(R.string.STRING_INTENT_PARAMS), intentParameters);
                startActivity(intent);
            }
        });
    }

    private void addEventoEquipeGridView() {
        eventoEquipeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                gridClickListener(adapterView, view, i, l);
            }
        });
    }

    private void addEventoEquipeAutoComplete() {
        eventoEquipeAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                EventoEquipeVO eventoEquipeVO = (EventoEquipeVO) parent.getItemAtPosition(i);
                updateGrid(eventoEquipeVO);
            }
        });

        eventoEquipeAutoComplete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                eventoEquipeAutoComplete.setText("");
                eventosEquipe = dao.getEventoEquipeDAO().findDistinctList();
                setAdapter(new MaoObraServicoAdapter(MaoObraServicoGridActivity.this, eventosEquipe));
                eventoEquipeGridView.setAdapter(getAdapter());

                showHideEmptyText(eventosEquipe);
                return false;
            }
        });
    }

    @Override
    protected void editarGridItem(int i) {
        EventoEquipeVO eventoEquipeVO = eventosEquipe.get(i);

        Intent intent = new Intent(this, EventoEquipeGridActivity.class);
        intent.putExtra(PARAM_EVENTO_EQUIPE_SELECIONADA, eventoEquipeVO);
        intent.putExtra(PARAM_LOCAL_SELECIONADO, eventoEquipeVO.getLocalizacao());
        intent.putExtra(PARAM_EQUIPE_SELECIONADA, eventoEquipeVO.getEquipe());
        intent.putExtra(PARAM_READ_ONLY, isEventoAntigo(eventoEquipeVO.getData()));//quando for evento dia(s) anterior(es), modo read only
        intent.putExtra(getStr(R.string.STRING_INTENT_PARAMS), intentParameters);

        startActivity(intent);
    }

    @Override
    protected void selecionarGridItem(int i) {
        EventoEquipeVO eventoEquipeVO = eventosEquipe.get(i);
        dataSelecionada = eventoEquipeVO.getData() == null || eventoEquipeVO.getData().isEmpty() ? Util.getToday() : eventoEquipeVO.getData();

        Intent intent = new Intent(this, ProducaoEquipeGridActivity.class);
        intent.putExtra(PARAM_EVENTO_EQUIPE_SELECIONADA, eventoEquipeVO);
        intent.putExtra(PARAM_LOCAL_SELECIONADO, eventoEquipeVO.getLocalizacao());
        intent.putExtra(PARAM_EQUIPE_SELECIONADA, eventoEquipeVO.getEquipe());
        intent.putExtra(getStr(R.string.STRING_INTENT_PARAMS), intentParameters);

        startActivity(intent);
    }

    private void updateGrid(EventoEquipeVO eventoEquipeVO) {
        eventoEquipeSelecionda = eventoEquipeVO;
        eventosEquipe = new ArrayList<EventoEquipeVO>();
        eventosEquipe.add(eventoEquipeVO);
        setAdapter(new MaoObraServicoAdapter(MaoObraServicoGridActivity.this, eventosEquipe));
        eventoEquipeGridView.setAdapter(getAdapter());

        showHideEmptyText(eventosEquipe);
    }

}
