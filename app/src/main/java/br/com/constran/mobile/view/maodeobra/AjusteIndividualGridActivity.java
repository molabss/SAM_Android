package br.com.constran.mobile.view.maodeobra;

import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.constran.mobile.R;
import br.com.constran.mobile.adapter.maodeobra.AjusteIntegranteAdapter;
import br.com.constran.mobile.enums.TipoAplicacao;
import br.com.constran.mobile.persistence.vo.aprop.ApropriacaoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.EquipeTrabalhoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.EventoEquipeVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.IntegranteTempVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.IntegranteVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.TipoApontamento;
import br.com.constran.mobile.persistence.vo.imp.ParalisacaoVO;
import br.com.constran.mobile.persistence.vo.imp.ServicoVO;
import br.com.constran.mobile.persistence.vo.menu.LocalizacaoVO;
import br.com.constran.mobile.validator.EventoEquipeValidator;
import br.com.constran.mobile.view.util.Error;
import br.com.constran.mobile.view.util.Util;

/**
 * Criado em 11/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class AjusteIndividualGridActivity extends GridActivity<AjusteIntegranteAdapter> {

    private GridView historicoIntegranteGridView;

    private Button btnSalvar;
    private Button btnAddEvento;

    private AutoCompleteTextView equipeAutoComplete;
    private AutoCompleteTextView paralisacaoAutoComplete;
    private AutoCompleteTextView servicoAutoComplete;

    private Spinner integranteSpinner;
    private EditText horaIniEditText;
    private EditText horaFimEditText;
    private EditText obsEditText;

    private List<EventoEquipeVO> eventoEquipes;
    private List<ServicoVO> servicos;
    private List<EquipeTrabalhoVO> equipes;
    private List<ParalisacaoVO> paralisacaoList;
    private List<IntegranteVO> integranteList;
    private List<? extends IntegranteVO> temporarioList;

    private ParalisacaoVO tipoEventoSelecionado;
    private LocalizacaoVO localSelecionado;
    private EventoEquipeVO eventoEquipeSelecionado;
    private EquipeTrabalhoVO equipeSelecionada;
    private IntegranteVO integranteSelecionado;
    private ServicoVO servicoSelecionado;

    //adapters
    private ArrayAdapter<EquipeTrabalhoVO> equipeAdapter;
    private ArrayAdapter<ServicoVO> servicoAdapter;
    private ArrayAdapter<ParalisacaoVO> paralisacaoAdapter;

    @Override
    protected int getContentView() {
        return R.layout.ajuste_individual;
    }

    @Override
    protected void onResume() {
        super.onResume();
        equipeAutoComplete.setAdapter(null);
        equipeAutoComplete.setText(equipeSelecionada.getApelido());
        equipeAutoComplete.setAdapter(equipeAdapter);

        for (int i = 0; i < integranteList.size(); i++) {
            if (integranteList.get(i).getPessoa().getId().equals(integranteSelecionado.getPessoa().getId())) {
                integranteSpinner.setSelection(i);
                break;
            }
        }
    }

    @Override
    protected void initAttributes() {

        obraVO = dao.getObraDAO().getById(dao.getConfiguracoesDAO().getConfiguracaoVO().getIdObra());

        historicoIntegranteGridView = (GridView) findViewById(R.id.historicoIntegranteGridView);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnAddEvento = (Button) findViewById(R.id.btnAddEvento);
        equipeAutoComplete = (AutoCompleteTextView) findViewById(R.id.equipeAutoComplete);
        servicoAutoComplete = (AutoCompleteTextView) findViewById(R.id.servicoAutoComplete);
        integranteSpinner = (Spinner) findViewById(R.id.integranteSpinner);
        paralisacaoAutoComplete = (AutoCompleteTextView) findViewById(R.id.paralisacaoAutoComplete);
        horaIniEditText = (EditText) findViewById(R.id.horaIniEditText);
        horaFimEditText = (EditText) findViewById(R.id.horaFimEditText);
        obsEditText = (EditText) findViewById(R.id.obsEditText);
        gridVazioTextView = (TextView) findViewById(R.id.empty_list_view);
    }

    @Override
    protected void bindComponents() {

        localSelecionado = (LocalizacaoVO) getIntent().getSerializableExtra(PARAM_LOCAL_SELECIONADO);

        equipeSelecionada = (EquipeTrabalhoVO) getIntent().getSerializableExtra(PARAM_EQUIPE_SELECIONADA);
        integranteSelecionado = (IntegranteVO) getIntent().getSerializableExtra(PARAM_INTEGRANTE_SELECIONADO);
        dataSelecionada = getIntent().getStringExtra(PARAM_DATA_SELECIONADA);


        //-----------------------------------------------------------------
        //servicos = dao.getServicoDAO().findServicosEquipe(localSelecionado);
        //-----------------------------------------------------------------

        carregarListaServicos();

        equipes = dao.getEquipeTrabalhoDAO().findAllItems();
        paralisacaoList = dao.getParalisacaoDAO().findList(TipoAplicacao.MAO_DE_OBRA);

        equipeAdapter = new ArrayAdapter<EquipeTrabalhoVO>(this, android.R.layout.select_dialog_singlechoice, equipes);
        servicoAdapter = new ArrayAdapter<ServicoVO>(this, android.R.layout.select_dialog_singlechoice, servicos);
        paralisacaoAdapter = new ArrayAdapter<ParalisacaoVO>(this, android.R.layout.select_dialog_singlechoice, paralisacaoList);

        equipeAutoComplete.setAdapter(equipeAdapter);
        servicoAutoComplete.setAdapter(servicoAdapter);
        paralisacaoAutoComplete.setAdapter(paralisacaoAdapter);

        updateIntegrantesSpinner();

        horaIniEditText.setKeyListener(new DigitsKeyListener());
        horaFimEditText.setKeyListener(new DigitsKeyListener());
        equipeAutoComplete.setKeyListener(null);
    }

    @Override
    protected void initAdapter() {
        updateDataSet();
    }

    @Override
    protected void addListeners() {
        addBtnSalvarListener();
        addBtnEventoListener();
        addIntegranteSpinnerListener();
        addGridViewListener();
        addParalisacaoAutoCompleteListener();
        addHoraEditTextListener();
        addServicoAutoCompleteListener();
        addHorarioEditTextListener();
        addObsEditTextListener();
    }

    private void addServicoAutoCompleteListener() {
        servicoAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                servicoSelecionado = (ServicoVO) parent.getItemAtPosition(i);
            }
        });

        servicoAutoComplete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                servicoAutoComplete.setText("");
                servicoSelecionado = null;
                return false;
            }
        });
    }

    private void addObsEditTextListener() {
        obsEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                obsEditText.setText("");
                return false;
            }
        });
    }

    private void updateIntegrantesSpinner() {

        if (equipeSelecionada != null) {
            integranteList = (List<IntegranteVO>) dao.getIntegranteEquipeDAO().findByLocalizacaoAndEquipe(eventoEquipeSelecionado, localSelecionado, equipeSelecionada);
            temporarioList = dao.getIntegranteTempDAO().findByLocalizacaoAndEquipe(localSelecionado, equipeSelecionada);
        }

        if (integranteList == null) {
            integranteList = new ArrayList<IntegranteVO>();
        }
        if (temporarioList == null) {
            temporarioList = new ArrayList<IntegranteTempVO>();
        }

        integranteList.addAll(temporarioList);
        //ordenando integrantes por nome
        Collections.sort(integranteList);

        integranteSpinner.setAdapter(new ArrayAdapter<IntegranteVO>(this, android.R.layout.select_dialog_singlechoice, integranteList));
    }

    private void addBtnEventoListener() {
        btnAddEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limparEvento();
            }
        });
    }

    private void limparEvento() {
        eventoEquipeSelecionado = null;
        paralisacaoAutoComplete.setText("");
        servicoAutoComplete.setText("");
        horaIniEditText.setText("");
        horaFimEditText.setText("");
        obsEditText.setText("");

        //habilitar edicao de hora ini
        horaIniEditText.setKeyListener(new DigitsKeyListener());
    }

    private void addParalisacaoAutoCompleteListener() {
        paralisacaoAutoComplete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                paralisacaoAutoComplete.setText("");
                servicoAutoComplete.setText("");
                servicoSelecionado = null;
                tipoEventoSelecionado = null;

                return false;
            }
        });

        paralisacaoAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                tipoEventoSelecionado = (ParalisacaoVO) parent.getItemAtPosition(i);
            }
        });
    }

    private void addBtnSalvarListener() {
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarEventoIntegrante();
            }
        });
    }

    private void salvarEventoIntegrante() {
        EventoEquipeVO eventoEquipeAntigo = eventoEquipeSelecionado;
        EventoEquipeVO eventoEquipeNovo = preencherEventoEquipe();

        if (isValidationOk(eventoEquipeNovo)) {
            //verifica se o tipo de evento é diferente do anterior e o exclui quando estiver diferente
            dao.getEventoEquipeDAO().deleteWhenTipoEventoChanges(eventoEquipeAntigo, localSelecionado, tipoEventoSelecionado, integranteSelecionado);

            integranteSelecionado = (IntegranteVO) integranteSpinner.getSelectedItem();
            dao.getEventoEquipeDAO().salvarApontamentoIndividual(eventoEquipeNovo, integranteSelecionado);

            updateDataSet();
            limparEvento();
        }
    }

    private void updateDataSet() {
        eventoEquipes = dao.getEventoEquipeDAO().findApontamentos(localSelecionado, equipeSelecionada, integranteSelecionado.getPessoa(), servicoSelecionado, dataSelecionada);
        setAdapter(new AjusteIntegranteAdapter(this, eventoEquipes));
        historicoIntegranteGridView.setAdapter(getAdapter());

        showHideEmptyText(eventoEquipes);
    }

    private boolean isValidationOk(EventoEquipeVO eventoEquipe) {
        String horaIniStr = horaIniEditText.getText().toString().trim();
        String horaFimStr = horaFimEditText.getText().toString().trim();

        EventoEquipeValidator validator = new EventoEquipeValidator(this);

        Error error = validator.validate(eventoEquipes, eventoEquipe, equipeSelecionada,
                tipoEventoSelecionado, servicoSelecionado, horaIniStr, horaFimStr, servicoSelecionado != null);

        if (error != null) {
            Util.viewMessage(this, error.getMessage());
            return false;
        }

        return true;
    }


    private void addIntegranteSpinnerListener() {
        //adiando listerner para evitar conflito com edicao
        integranteSpinner.post(new Runnable() {
            @Override
            public void run() {
                integranteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                        integranteSelecionado = (IntegranteVO) parent.getSelectedItem();
                        updateDataSet();

                        limparEvento();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });
    }

    private void addGridViewListener() {
        historicoIntegranteGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                gridClickListener(adapterView, view, i, l);
            }
        });
    }

    private void addHoraEditTextListener() {
        Util.addMascaraHora(horaIniEditText, horaFimEditText);
        Util.addMascaraHora(horaFimEditText, obsEditText);
    }

    private void addHorarioEditTextListener() {
        horaIniEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                horaIniEditText.setText("");
                return false;
            }
        });

        horaFimEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                horaFimEditText.setText("");
                return false;
            }
        });

    }

    @Override
    protected void editarGridItem(int i) {
        eventoEquipeSelecionado = eventoEquipes.get(i);
        tipoEventoSelecionado = eventoEquipeSelecionado.getParalisacao();

        ServicoVO servico = eventoEquipeSelecionado.getServico();

        servicoAutoComplete.setAdapter(null);
        servicoAutoComplete.setText(servico != null ? servico.getDescricao() : "");
        servicoAutoComplete.setAdapter(servicoAdapter);

        paralisacaoAutoComplete.setAdapter(null);
        paralisacaoAutoComplete.setText(tipoEventoSelecionado.getDescricaoFormatada());
        paralisacaoAutoComplete.setAdapter(paralisacaoAdapter);

        servicoSelecionado = eventoEquipeSelecionado.getServico();
        horaIniEditText.setText(eventoEquipeSelecionado.getHoraIni());
        horaFimEditText.setText(eventoEquipeSelecionado.getHoraFim());
        obsEditText.setText(eventoEquipeSelecionado.getObservacao());

        //desabilitar edicao de hora ini
        horaIniEditText.setKeyListener(null);
    }

    @Override
    protected void excluirGridItem(int i) {
        eventoEquipeSelecionado = eventoEquipes.get(i);

        if (eventoEquipeSelecionado.getParalisacao() != null) {
            dao.getEventoEquipeDAO().deleteApontamentoIndividual(eventoEquipeSelecionado, integranteSelecionado);
            limparEvento();
            updateDataSet();
        }
    }

    private EventoEquipeVO preencherEventoEquipe() {
        EventoEquipeVO eventoEquipe = new EventoEquipeVO();
        ApropriacaoVO apropriacao = null;

        if (eventoEquipeSelecionado != null) {
            eventoEquipe = eventoEquipeSelecionado.clone();
            apropriacao = eventoEquipe.getApropriacao();
        } else {
            eventoEquipe.setEquipe(equipeSelecionada);
            eventoEquipe.setServico(servicoSelecionado);
            eventoEquipe.setTipoApontamento(TipoApontamento.MANUAL);
        }

        String dataApontamento = dataSelecionada == null ? Util.getNow() : dataSelecionada;

        if (apropriacao == null) {
            apropriacao = new ApropriacaoVO();
            apropriacao.setDataHoraApontamento(dataApontamento);
        }

        apropriacao.setAtividade(localSelecionado.getAtividade());
        apropriacao.setTipoApropriacao(TIPO_APROPRIACAO_SERVICO);

        eventoEquipe.setApropriacao(apropriacao);
        eventoEquipe.setParalisacao(tipoEventoSelecionado);
        eventoEquipe.setData(dataApontamento);
        eventoEquipe.setLocalizacao(localSelecionado);
        eventoEquipe.setServico(servicoSelecionado);
        eventoEquipe.setHoraIni(horaIniEditText.getText().toString());
        eventoEquipe.setHoraFim(horaFimEditText.getText().toString());
        eventoEquipe.setObservacao(obsEditText.getText().toString());

        return eventoEquipe;
    }


    /*
    * Caso a obra esteja parametrizada para não usar Planejamento de Serviços faça a busca somente na tabela
    * Servicos e traga todos os serviços cadastrados, independente de Frente de Obra e Atividade.
    *
    * Moisés Santana 16/07/2015
    *
    * */
    private void carregarListaServicos(){
        if(obraVO.getUsaPlanServico().equals("N")){
            servicos = dao.getServicoDAO().findAllServicos();
        } else {
            servicos = dao.getServicoDAO().findServicosEquipe(localSelecionado);
        }
    }

}
