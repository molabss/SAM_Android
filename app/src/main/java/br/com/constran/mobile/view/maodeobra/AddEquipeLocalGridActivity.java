package br.com.constran.mobile.view.maodeobra;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.*;
import br.com.constran.mobile.R;
import br.com.constran.mobile.adapter.maodeobra.LocalEquipeAdapter;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.EquipeTrabalhoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.EventoEquipeVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.IntegranteVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.LocalizacaoEquipeVO;
import br.com.constran.mobile.persistence.vo.menu.LocalizacaoVO;
import br.com.constran.mobile.view.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Criado em 10/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class AddEquipeLocalGridActivity extends GridActivity<LocalEquipeAdapter> {

    private GridView equipeGridView;
    private AutoCompleteTextView localizacaoAutoComplete;
    private AutoCompleteTextView equipeAutoComplete;

    private ImageView saveImage;

    private EventoEquipeVO eventoEquipeSelecionado;
    private LocalizacaoVO localSelecionado;
    private EquipeTrabalhoVO equipeSelecionada;
    private EquipeTrabalhoVO equipeAntiga;

    private List<LocalizacaoVO> localizacoes;
    private List<LocalizacaoEquipeVO> localizacaoEquipes;
    private List<EquipeTrabalhoVO> equipes;

    private ArrayAdapter localizacaoAdapter;
    private ArrayAdapter<EquipeTrabalhoVO> equipeAdapter;

    @Override
    protected int getContentView() {
        return R.layout.add_equipe_local;
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshComponents(localSelecionado);

        localizacaoAutoComplete.setAdapter(null);
        localizacaoAutoComplete.setText(localSelecionado != null ? localSelecionado.getDescricao() : "");
        localizacaoAutoComplete.setAdapter(localizacaoAdapter);
    }

    @Override
    public void initAttributes() {
        equipeGridView = (GridView) findViewById(R.id.equipeGridView);
        localizacaoAutoComplete = (AutoCompleteTextView) findViewById(R.id.localizacaoAutoComplete);
        equipeAutoComplete = (AutoCompleteTextView) findViewById(R.id.equipeAutoComplete);
        saveImage = (ImageView) findViewById(R.id.saveImage);
        gridVazioTextView = (TextView) findViewById(R.id.empty_list_view);
    }

    @Override
    public void bindComponents() {
        eventoEquipeSelecionado = (EventoEquipeVO) getIntent().getSerializableExtra(PARAM_EVENTO_EQUIPE_SELECIONADA);
        localSelecionado = (LocalizacaoVO) getIntent().getSerializableExtra(PARAM_LOCAL_SELECIONADO);
        equipeSelecionada = (EquipeTrabalhoVO) getIntent().getSerializableExtra(PARAM_EQUIPE_SELECIONADA);

        if (eventoEquipeSelecionado != null && (localSelecionado == null || equipeSelecionada == null)) {
            localSelecionado = eventoEquipeSelecionado.getLocalizacao();
            equipeSelecionada = eventoEquipeSelecionado.getEquipe();
        }

        equipeAntiga = equipeSelecionada;
        localizacaoEquipes = new ArrayList<LocalizacaoEquipeVO>();
        localizacoes = dao.getLocalizacaoDAO().findList(true);

        equipes = new ArrayList<EquipeTrabalhoVO>();

        localizacaoAdapter = new ArrayAdapter<LocalizacaoVO>(this, android.R.layout.select_dialog_singlechoice, localizacoes);
        equipeAdapter = new ArrayAdapter<EquipeTrabalhoVO>(this, android.R.layout.select_dialog_singlechoice, equipes);

        localizacaoAutoComplete.setAdapter(localizacaoAdapter);
        equipeAutoComplete.setAdapter(equipeAdapter);

        showHideEmptyText(localizacaoEquipes);
    }

    @Override
    public void initAdapter() {
        setAdapter(new LocalEquipeAdapter(this, localizacaoEquipes));
        equipeGridView.setAdapter(getAdapter());

        showHideEmptyText(localizacaoEquipes);
    }

    @Override
    public void addListeners() {
        addLocalizacaoAutoCompleteListener();
        addEquipeAutocompleteListener();
        addEquipeGridViewListener();
        addSaveImageListener();
    }

    private void addEquipeGridViewListener() {
        equipeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                gridClickListener(adapterView, view, i, l);
            }
        });
    }

    private void addEquipeAutocompleteListener() {
        equipeAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                equipeSelecionada = (EquipeTrabalhoVO) parent.getItemAtPosition(i);
            }
        });

        equipeAutoComplete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                equipeAutoComplete.setText("");
                equipeSelecionada = null;
                return false;
            }
        });
    }

    private void addLocalizacaoAutoCompleteListener() {
        localizacaoAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                localSelecionado = (LocalizacaoVO) parent.getItemAtPosition(i);

                refreshComponents(localSelecionado);
            }
        });

        localizacaoAutoComplete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                localizacaoAutoComplete.setText("");
                localSelecionado = null;
                refreshComponents(localSelecionado);

                return false;
            }
        });
    }

    private void addSaveImageListener() {
        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvar();
            }
        });
    }

    private boolean isValidationOk() {
        if (localSelecionado == null || localizacaoAutoComplete.getText().toString().isEmpty()) {
            Util.viewMessage(this, Util.getMessage(this, getStr(R.string.menu_localizacao), R.string.ALERT_REQUIRED));
            return false;
        }

        if (equipes == null || equipes.isEmpty() || equipeSelecionada == null || equipeAutoComplete.getText().toString().trim().isEmpty()) {
            Util.viewMessage(this, Util.getMessage(this, getStr(R.string.equipe), R.string.ALERT_REQUIRED));
            return false;
        }

        return true;
    }

    protected void confirmaExclusao(final int i) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setMessage(getStr(R.string.ALERT_CONFIRM_EQUIPE_REMOVE));
        dialogo.setPositiveButton(getStr(R.string.SIM), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface di, int arg) {
                excluirGridItem(i);
            }
        });
        dialogo.setNegativeButton(getStr(R.string.NAO), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface di, int arg) {
                getAdapter().setOperacao(null);
            }
        });
        dialogo.setTitle(getStr(R.string.AVISO));
        dialogo.show();
    }


    @Override
    protected void editarGridItem(int i) {
        if (localizacaoEquipes == null || i >= localizacaoEquipes.size()) {
            return;
        }

        LocalizacaoEquipeVO localizacaoEquipeSelecionada = localizacaoEquipes.get(i);

        Intent intent = new Intent(this, ComposicaoEquipeGridActivity.class);
        intent.putExtra(PARAM_EQUIPE_SELECIONADA, localizacaoEquipeSelecionada.getEquipe());
        intent.putExtra(PARAM_LOCAL_SELECIONADO, localSelecionado);
        intent.putExtra(getStr(R.string.STRING_INTENT_PARAMS), intentParameters);

        startActivity(intent);
    }

    @Override
    protected void excluirGridItem(int i) {
        if (localizacaoEquipes == null || i >= localizacaoEquipes.size()) {
            return;
        }
        LocalizacaoEquipeVO localizacaoEquipe = localizacaoEquipes.get(i);

        //remove equipe local
        dao.getLocalizacaoEquipeDAO().delete(localizacaoEquipe);
        localizacaoEquipes.remove(i);

        //remove faltas dos integrantes da equipe
        dao.getAusenciaDAO().deleteByEquipe(localizacaoEquipe.getEquipe(), Util.getToday());

        //remove eventos/apropriacoes/paralisacoes
        dao.getEventoEquipeDAO().deleteByLocalAndEquipe(localSelecionado, localizacaoEquipe.getEquipe());

        //remove integrantes temporarios da equipe
        dao.getIntegranteTempDAO().delete(localizacaoEquipe.getEquipe(), Util.getToday());

        setAdapter(new LocalEquipeAdapter(this, localizacaoEquipes));
        equipeGridView.setAdapter(getAdapter());

        refreshEquipeAutoComplete(localSelecionado);
        showHideEmptyText(localizacaoEquipes);

        if (localizacaoEquipe.getEquipe() != null && equipeAntiga != null && localizacaoEquipe.getEquipe().getId().equals(equipeAntiga.getId())) {
            equipeAntiga = null;
        }
    }

    private void refreshComponents(LocalizacaoVO local) {
        localizacaoEquipes = local != null ? dao.getLocalizacaoEquipeDAO().findListByLocalizacao(local) : new ArrayList<LocalizacaoEquipeVO>();
        setAdapter(new LocalEquipeAdapter(this, localizacaoEquipes));
        equipeGridView.setAdapter(getAdapter());

        refreshEquipeAutoComplete(local);

        showHideEmptyText(localizacaoEquipes);
    }

    private void refreshEquipeAutoComplete(LocalizacaoVO localSelecionado) {
        equipes = localSelecionado != null ? dao.getEquipeTrabalhoDAO().findNotInLocalEquipe(localSelecionado) : new ArrayList<EquipeTrabalhoVO>();
        equipeAutoComplete.setText("");

        equipeAdapter = new ArrayAdapter<EquipeTrabalhoVO>(this, android.R.layout.select_dialog_singlechoice, equipes);
        equipeAutoComplete.setAdapter(equipeAdapter);
        getAdapter().notifyDataSetChanged();
    }

    private void salvar() {
        if (isValidationOk()) {
            LocalizacaoEquipeVO localizacaoEquipe = new LocalizacaoEquipeVO();
            localizacaoEquipe.setLocalizacao(localSelecionado);
            localizacaoEquipe.setEquipe(equipeSelecionada);
            localizacaoEquipe.setDataHora(Util.getToday());

            //salva equipe no local selecionado
            dao.getLocalizacaoEquipeDAO().save(localizacaoEquipe);

            //inicialmente marca os integrantes como não presentes (o responsável pelo apontamento deve marcar aqueles que estão presentes)
            List<? extends IntegranteVO> integrantes = dao.getIntegranteEquipeDAO().findByEquipe(equipeSelecionada.getId());
            dao.getAusenciaDAO().salvar(integrantes);

            equipeSelecionada = null;
            refreshComponents(localizacaoEquipe.getLocalizacao());
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(PARAM_EQUIPE_SELECIONADA, getEquipeSelecionada());
        setResult(EQUIPE_LOCAL_RESULT_CODE, intent);

        super.onBackPressed();
    }

    /**
     * @return equipe selecionada anteriormente (equipe antiga) ou null se nao existia equipe selecionada ou foi excluida
     */
    private EquipeTrabalhoVO getEquipeSelecionada() {
        //se havia uma equipe selecionada
        if (equipeAntiga != null) {
            //se a equipe estiver dentre as equipes disponiveis, entao foi excluida, senao retorna a equipe antiga
            return (equipes != null && equipes.contains(equipeAntiga)) ? null : equipeAntiga;
        }

        return equipeAntiga;
    }
}
