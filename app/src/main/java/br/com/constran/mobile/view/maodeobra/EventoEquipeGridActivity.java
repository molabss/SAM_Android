package br.com.constran.mobile.view.maodeobra;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.*;
import br.com.constran.mobile.LocalizacaoActivity;
import br.com.constran.mobile.R;
import br.com.constran.mobile.adapter.maodeobra.EventoEquipeAdapter;
import br.com.constran.mobile.enums.TipoAplicacao;
import br.com.constran.mobile.persistence.vo.aprop.ApropriacaoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.EquipeTrabalhoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.EventoEquipeVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.HorarioTrabalhoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.TipoApontamento;
import br.com.constran.mobile.persistence.vo.imp.ParalisacaoVO;
import br.com.constran.mobile.persistence.vo.imp.ServicoVO;
import br.com.constran.mobile.persistence.vo.menu.LocalizacaoVO;
import br.com.constran.mobile.validator.EventoEquipeValidator;
import br.com.constran.mobile.view.util.Error;
import br.com.constran.mobile.view.util.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Criado em 10/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class EventoEquipeGridActivity extends GridActivity<EventoEquipeAdapter> {

    private static final int REQUEST_CODE = 1;

    private GridView eventoGridView;
    private AutoCompleteTextView localizacaoAutoComplete;
    private AutoCompleteTextView equipeAutoComplete;
    private AutoCompleteTextView tipoEventoAutoComplete;
    private AutoCompleteTextView servicoAutoComplete;

    private EditText horaIniEditText;
    private EditText horaFimEditText;
    private EditText obsEditText;

    private Button btnAddLocalizacao;
    private Button btnAddEquipe;
    private Button btnAddEvento;
    private Button btnViewEquipe;

    private RadioGroup tipoApontamentoRadio;
    private RadioButton manualRadio;
    private RadioButton automaticoRadio;

    private ImageView saveEventoImage;

    private LocalizacaoVO localSelecionado;
    private EquipeTrabalhoVO equipeSelecionada;
    private ParalisacaoVO tipoEventoSelecionado;
    private ServicoVO servicoSelecionado;
    private EventoEquipeVO eventoEquipeSelecionado;

    private List<LocalizacaoVO> localizacoes;
    private List<EquipeTrabalhoVO> equipes;
    private List<EventoEquipeVO> eventoEquipes;
    private List<ServicoVO> servicos;
    private List<ParalisacaoVO> paralisacoes;

    //adapters
    private ArrayAdapter<LocalizacaoVO> localAdapter;
    private ArrayAdapter<EquipeTrabalhoVO> equipeAdapter;
    private ArrayAdapter<ParalisacaoVO> tipoEventoAdapter;
    private ArrayAdapter<ServicoVO> servicoAdapter;

    private boolean isNew;

    //usado para validar dados na edicao de registro
    private String horaFimAntiga;
    private ServicoVO servicoAntigo;
    private ParalisacaoVO paralisacaoAntiga;

    @Override
    protected void onResume() {
        super.onResume();
        refreshComponents();
        updateDataSet();

        try {
            if (eventoEquipes != null && eventoEquipes.size() > 0 && !readOnly) {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(eventoEquipes.get(0).getData());
                Date now = new Date();
                String dateEvent = df.format(date);
                String dateNow = df.format(now);

                if (!dateEvent.equals(dateNow)) {
                    Intent intent = new Intent(this, MaoObraServicoGridActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        } catch (Exception ex) {
            Log.e(AddTemporarioActivity.class.toString(), ex.getMessage());
        }

        localizacaoAutoComplete.setAdapter(null);
        localizacaoAutoComplete.setText(localSelecionado != null ? localSelecionado.getDescricao() : "");
        localizacaoAutoComplete.setAdapter(localAdapter);

        equipeAutoComplete.setAdapter(null);
        equipeAutoComplete.setText(equipeSelecionada != null ? equipeSelecionada.getApelido() : "");
        equipeAutoComplete.setAdapter(equipeAdapter);

        updateDefaultHorarioRadioChecked(horaIniEditText.getText().toString().isEmpty() && horaFimEditText.getText().toString().isEmpty());

        if (automaticoRadio.isChecked()) {
            preencherHorarioByEquipe();
            horaIniEditText.setKeyListener(null);
        } else {
            bindTipoEventoDefault();
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.evento_equipe;
    }

    @Override
    public void initAttributes() {

        obraVO = dao.getObraDAO().getById(dao.getConfiguracoesDAO().getConfiguracaoVO().getIdObra());

        eventoGridView = (GridView) findViewById(R.id.eventoEquipeGridView);
        localizacaoAutoComplete = (AutoCompleteTextView) findViewById(R.id.localizacaoAutoComplete);
        equipeAutoComplete = (AutoCompleteTextView) findViewById(R.id.equipeAutoComplete);
        tipoEventoAutoComplete = (AutoCompleteTextView) findViewById(R.id.tipoEventoAutoComplete);
        servicoAutoComplete = (AutoCompleteTextView) findViewById(R.id.servicoAutoComplete);

        manualRadio = (RadioButton) findViewById(R.id.manualRadio);
        automaticoRadio = (RadioButton) findViewById(R.id.automaticoRadio);

        horaIniEditText = (EditText) findViewById(R.id.horaIniEditText);
        horaFimEditText = (EditText) findViewById(R.id.horaFimEditText);

        btnAddLocalizacao = (Button) findViewById(R.id.btAddLocalizacao);
        btnAddEquipe = (Button) findViewById(R.id.btnAddEquipe);
        btnViewEquipe = (Button) findViewById(R.id.btnViewEquipe);
        btnAddEvento = (Button) findViewById(R.id.btnAddEvento);

        tipoApontamentoRadio = (RadioGroup) findViewById(R.id.tipoApontamentoRadio);
        saveEventoImage = (ImageView) findViewById(R.id.saveEventoImage);
        obsEditText = (EditText) findViewById(R.id.obsEditText);
        gridVazioTextView = (TextView) findViewById(R.id.empty_list_view);
    }

    @Override
    public void bindComponents() {
        isNew = true;
        localSelecionado = (LocalizacaoVO) getIntent().getSerializableExtra(PARAM_LOCAL_SELECIONADO);
        equipeSelecionada = (EquipeTrabalhoVO) getIntent().getSerializableExtra(PARAM_EQUIPE_SELECIONADA);
        eventoEquipeSelecionado = (EventoEquipeVO) getIntent().getSerializableExtra(PARAM_EVENTO_EQUIPE_SELECIONADA);
        readOnly = getIntent().getBooleanExtra(PARAM_READ_ONLY, false);
        isNew = eventoEquipeSelecionado == null;

        equipes = new ArrayList<EquipeTrabalhoVO>();
        localizacoes = dao.getLocalizacaoDAO().findList(true);

        carregarListaServicos();

        paralisacoes = dao.getParalisacaoDAO().findList(TipoAplicacao.MAO_DE_OBRA);

        localAdapter = new ArrayAdapter<LocalizacaoVO>(this, android.R.layout.select_dialog_singlechoice, localizacoes);
        equipeAdapter = new ArrayAdapter<EquipeTrabalhoVO>(this, android.R.layout.select_dialog_singlechoice, equipes);
        tipoEventoAdapter = new ArrayAdapter<ParalisacaoVO>(this, android.R.layout.select_dialog_singlechoice, paralisacoes);
        servicoAdapter = new ArrayAdapter<ServicoVO>(this, android.R.layout.select_dialog_singlechoice, servicos);

        localizacaoAutoComplete.setAdapter(localAdapter);
        equipeAutoComplete.setAdapter(equipeAdapter);
        tipoEventoAutoComplete.setAdapter(tipoEventoAdapter);
        servicoAutoComplete.setAdapter(servicoAdapter);

        atualizarEventosEquipe();
        tratarModoConsulta();
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

    private void atualizarEventosEquipe() {
        dataSelecionada = eventoEquipeSelecionado != null ? eventoEquipeSelecionado.getData() : dataSelecionada;
        eventoEquipes = dao.getEventoEquipeDAO().findByLocalizacaoAndEquipe(localSelecionado, equipeSelecionada, dataSelecionada);

        isNew = eventoEquipes == null || eventoEquipes.isEmpty();
    }

    @Override
    public void initAdapter() {
        setAdapter(new EventoEquipeAdapter(this, new ArrayList<EventoEquipeVO>(), readOnly));
    }

    @Override
    public void addListeners() {
        if (!readOnly) {
            addLocalizacaoAutoCompleteListener();
            addBtnAddLocalizacaoListener();
            addBtnAddEquipeListener();
            addBtnViewEquipeListener();
            addBtnAddEventoListener();
            addSaveEventoImageListener();
            addRadioTipoApontamentoListener();
            addTipoEventoAutoCompleteListener();
            addHorarioEditTextListener();
            addEquipeAutoCompleteListener();
            addServicoAutoCompleteListener();
            addHoraEditTextListener();
            addObsEditText();
        }

        addEventoGridViewListener();
    }

    private void addObsEditText() {
        obsEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                obsEditText.setText("");
                return false;
            }
        });
    }

    private void addEventoGridViewListener() {
        eventoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                gridClickListener(adapterView, view, i, l);
            }
        });
    }

    private void addEquipeAutoCompleteListener() {
        equipeAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                equipeSelecionada = (EquipeTrabalhoVO) parent.getItemAtPosition(i);
                afterEquipeAutoCompleteClick();
            }
        });

        equipeAutoComplete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                eventoEquipes = new ArrayList<EventoEquipeVO>();
                equipeSelecionada = null;
                tipoEventoAutoComplete.setText("");
                tipoEventoSelecionado = null;

                limparEventoForm(true);
                updateDataSet();

                return false;
            }
        });

        equipeAutoComplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focusOn) {
                if (!focusOn && equipeSelecionada != null) {
                    equipeAutoComplete.setAdapter(null);
                    equipeAutoComplete.setText(equipeSelecionada.getApelido());
                    equipeAutoComplete.setAdapter(equipeAdapter);
                }
            }
        });

    }

    private void afterEquipeAutoCompleteClick() {
        //atualiza grid de eventos
        atualizarEventosEquipe();
        servicoAutoComplete.setText("");
        servicoSelecionado = null;
        tipoEventoAutoComplete.setText("");
        tipoEventoSelecionado = null;

        if (!isNew) {
            limparEventoForm(false);
            //Quando estiver editando, nao deve exibir tipo Apropriacao Radio
            conteudoLinearLayout5.setVisibility(View.GONE);
        } else {
            HorarioTrabalhoVO horario = equipeSelecionada.getHorarioTrabalho();
            //busca horarios
            horario = horario != null && horario.getId() != null ? dao.getHorarioTrabalhoDAO().findById(horario) : horario;

            equipeSelecionada.setHorarioTrabalho(horario);

            horaIniEditText.setText(horario != null ? horario.getHoraInicio() : "");
            horaFimEditText.setText(horario != null ? horario.getHoraTermino() : "");
            conteudoLinearLayout5.setVisibility(View.VISIBLE);
            showHideTipoEventoRow();
        }

        carregarListaServicos();

        servicoAutoComplete.setAdapter(new ArrayAdapter<ServicoVO>(this, android.R.layout.select_dialog_singlechoice, servicos));

        updateDataSet();
        updateDefaultHorarioRadioChecked(horaIniEditText.getText().toString().isEmpty() && horaFimEditText.getText().toString().isEmpty());
    }

    private void tratarModoConsulta() {
        if (readOnly) {
            btnAddLocalizacao.setVisibility(View.GONE);
            btnAddEquipe.setVisibility(View.GONE);
            saveEventoImage.setVisibility(View.GONE);
            btnAddEvento.setVisibility(View.GONE);
            btnViewEquipe.setVisibility(View.GONE);
            localizacaoAutoComplete.setKeyListener(null);
            equipeAutoComplete.setKeyListener(null);
            tipoEventoAutoComplete.setKeyListener(null);
            servicoAutoComplete.setKeyListener(null);
            horaIniEditText.setKeyListener(null);
            horaFimEditText.setKeyListener(null);
            obsEditText.setKeyListener(null);

            tipoApontamentoRadio.setEnabled(false);
            automaticoRadio.setEnabled(false);
            manualRadio.setEnabled(false);
        }
    }

    private void updateDataSet() {
        if (eventoEquipes == null || eventoEquipes.isEmpty()) {
            eventoEquipes = new ArrayList<EventoEquipeVO>();
        } else {
            eventoEquipes = dao.getEventoEquipeDAO().findByLocalizacaoAndEquipe(localSelecionado, equipeSelecionada, dataSelecionada);

            if (eventoEquipes != null && !eventoEquipes.isEmpty()) {
                isNew = false;
            }
        }

        setAdapter(new EventoEquipeAdapter(this, eventoEquipes, readOnly));
        eventoGridView.setAdapter(getAdapter());
        showHideEmptyText(eventoEquipes);
    }

    private void addTipoEventoAutoCompleteListener() {
        tipoEventoAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                tipoEventoSelecionado = (ParalisacaoVO) parent.getItemAtPosition(i);
            }
        });

        tipoEventoAutoComplete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                tipoEventoAutoComplete.setText("");
                tipoEventoSelecionado = null;
                return false;
            }
        });

        tipoEventoAutoComplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focusOn) {
                if (!focusOn && tipoEventoSelecionado != null) {
                    tipoEventoAutoComplete.setAdapter(null);
                    tipoEventoAutoComplete.setText(tipoEventoSelecionado.getDescricao());
                    tipoEventoAutoComplete.setAdapter(tipoEventoAdapter);
                }
            }
        });
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

    private void addServicoAutoCompleteListener() {
        servicoAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                servicoSelecionado = (ServicoVO) parent.getItemAtPosition(i);
                updateDataSetWhenAutomatico();

            }
        });

        servicoAutoComplete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                servicoAutoComplete.setText("");
                servicoSelecionado = null;

                updateDataSetWhenAutomatico();

                return false;
            }
        });

        servicoAutoComplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focusOn) {
                if (!focusOn && servicoSelecionado != null) {
                    servicoAutoComplete.setAdapter(null);
                    servicoAutoComplete.setText(servicoSelecionado.getDescricao());
                    servicoAutoComplete.setAdapter(servicoAdapter);
                }
            }
        });
    }

    private void addRadioTipoApontamentoListener() {
        tipoApontamentoRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                boolean apontamentoManual = manualRadio.isChecked();

                horaIniEditText.setKeyListener(apontamentoManual ? new DigitsKeyListener() : null);
                horaFimEditText.setKeyListener(apontamentoManual ? new DigitsKeyListener() : null);

                if (!apontamentoManual && equipeSelecionada != null) {
                    showHideTipoApontamentoRow();
                    preencherHorarioByEquipe();
                } else {//apontamento manual
                    horaIniEditText.setText("");
                    horaFimEditText.setText("");

                    if (isNew) {
                        eventoEquipes = null;
                        updateDataSet();
                    } else {
                        showHideTipoApontamentoRow();
                    }
                    bindTipoEventoDefault();
                }

                showHideTipoEventoRow();
//                bindTipoEventoDefault();
            }
        });
    }

    /**
     * Regra:
     * 1 - quando for novo registro e automatico selecionado, esconder tipo evento quando nao houver periodo horario trabalho
     * 2 - quando for edicao, exibir linha tipo evento
     */
    private void showHideTipoEventoRow() {
        if (isNew) {
            HorarioTrabalhoVO horario = equipeSelecionada != null ? equipeSelecionada.getHorarioTrabalho() : null;

            if (automaticoRadio.isChecked() && horario != null && horario.getPeriodosHorariosTrabalhos() != null) {
                conteudoLinearLayout3.setVisibility(View.GONE);
                tipoEventoSelecionado = null;
            } else {
                bindTipoEventoDefault();
                conteudoLinearLayout3.setVisibility(View.VISIBLE);
            }
        } else {
            conteudoLinearLayout3.setVisibility(View.VISIBLE);
        }
    }

    private void showHideTipoApontamentoRow() {
        if (!isNew) {
            //desabilita edicao de tipo de evento, pois podem haver tipos de eventos distintos na lista
            conteudoLinearLayout5.setVisibility(View.GONE);
            tipoEventoAutoComplete.setText("");
            tipoEventoSelecionado = null;
        }
    }

    /**
     * Quando o radio automatico estiver selecionado, deve atualizar a lista de eventos
     * REGRA: busca eventos salvos na tabela eventoEquipe, se existir, entao os lista
     * senao, busca registros na tabela periodoHorarioTrabalho (eventos temporarios)
     */
    private void updateDataSetWhenAutomatico() {
        if (automaticoRadio.isChecked() && isNew) {
            HorarioTrabalhoVO horario = equipeSelecionada != null ? equipeSelecionada.getHorarioTrabalho() : null;

            if (horario != null && horario.getPeriodosHorariosTrabalhos() != null) {
                if (servicoSelecionado == null) {
                    eventoEquipes = dao.getEventoEquipeDAO().findByLocalizacaoAndEquipe(localSelecionado, equipeSelecionada);
                } else {
                    eventoEquipes = dao.getEventoEquipeDAO().toEventosEquipe(horario.getPeriodosHorariosTrabalhos(), preencherEventoEquipe(), servicoSelecionado);
                }

                setAdapter(new EventoEquipeAdapter(EventoEquipeGridActivity.this, eventoEquipes, TipoApontamento.AUTOMATICO));
                eventoGridView.setAdapter(getAdapter());
                showHideEmptyText(eventoEquipes);
            }
        }
    }

    /**
     * Busca os horarios de trabalho e periodos de horario trabalho associados a equipe
     */
    private void preencherHorarioByEquipe() {
        HorarioTrabalhoVO horario = equipeSelecionada.getHorarioTrabalho();

        if (horario == null || horario.getPeriodosHorariosTrabalhos() == null) {
            equipeSelecionada = dao.getEquipeTrabalhoDAO().findById(equipeSelecionada);
        }

        if (horario != null) {
            horaIniEditText.setText(horario.getHoraInicio());
            horaFimEditText.setText(horario.getHoraTermino());
        }

        updateDataSetWhenAutomatico();
    }

    private void addSaveEventoImageListener() {
        saveEventoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarEvento(false);
            }
        });
    }

    /**
     * Salva o(s) evento(s) informados
     * REGRA:
     * 1 - Quando o apontamento for manual, o evento é salvo individualmente
     * 2 - Quando o apontamento for automatico, uma lista de eventos pode ser salva
     * 3 - Quando um evento for editado, ele deve obrigatoriamente ser manual
     *
     * @param closeAfterSave indica se a activity deve ser finalizada apos salvar os dados
     */
    private void salvarEvento(boolean closeAfterSave) {
        if (isValidationOk()) {
            boolean salvou = true;

            if (TipoApontamento.MANUAL.equals(getTipoApontamentoSelecionado())) {
                salvarEventoManual();
            } else {
                salvou = salvarEventosAutomatico();
            }

            if (!closeAfterSave && salvou) {
                isNew = false;
                conteudoLinearLayout5.setVisibility(View.GONE);
                atualizarEventosEquipe();
                updateDataSet();
                limparEventoForm(false);
            } else if (closeAfterSave) {
                finish();
            }
        }
    }

    private void salvarEventoManual() {
        eventoEquipeSelecionado = preencherEventoEquipe();
        dao.getEventoEquipeDAO().save(eventoEquipeSelecionado, true);
    }

    /**
     * Salva evento apontado automaticamente quando possuir eventos,
     * caso contrario notifica o usuario que nao existem eventos automaticos associados
     * return true quando foi salvo com sucesso e false caso contrario
     */
    private boolean salvarEventosAutomatico() {
        HorarioTrabalhoVO horario = equipeSelecionada != null ? equipeSelecionada.getHorarioTrabalho() : null;

        if (horario != null && eventoEquipes != null && !eventoEquipes.isEmpty()) {
            for (EventoEquipeVO ee : eventoEquipes) {
                ee.setObservacao(obsEditText.getText().toString());
                dao.getEventoEquipeDAO().save(ee, true);
            }

            eventoEquipeSelecionado = null;
            dataSelecionada = Util.getNow();

            return true;
        } else {
            Util.viewMessage(this, getStr(R.string.ALERT_SEM_EVENTOS_AUTOMATICOS));
            return false;
        }
    }

    private boolean isValidationOk() {
        String horaIniStr = horaIniEditText.getText().toString().trim();
        String horaFimStr = horaFimEditText.getText().toString().trim();

        EventoEquipeValidator validator = new EventoEquipeValidator(this);
        Error error = validator.validate(eventoEquipes, preencherEventoEquipe(), equipeSelecionada,
                tipoEventoSelecionado, servicoSelecionado, horaIniStr, horaFimStr);

        if (error != null) {
            Util.viewMessage(this, error.getMessage());
            return false;
        }

        return true;
    }

    private void addBtnAddEventoListener() {
        btnAddEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasNoEmptyField()) {
                    AlertDialog.Builder dialogo = new AlertDialog.Builder(EventoEquipeGridActivity.this);
                    dialogo.setMessage(getStr(R.string.ALERT_LIMPAR_FORM));
                    dialogo.setPositiveButton(getStr(R.string.SIM), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface di, int arg) {
                            limparEventoForm(false);
                        }
                    });
                    dialogo.setNegativeButton(getStr(R.string.NAO), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface di, int arg) {
                        }
                    });

                    dialogo.setTitle(getStr(R.string.AVISO));
                    dialogo.show();

                } else {
                    limparEventoForm(false);
                }
            }
        });
    }

    private void addBtnViewEquipeListener() {
        btnViewEquipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidationAjusteEquipeOk()) {
                    Intent intent = new Intent(EventoEquipeGridActivity.this, AjusteEquipeGridActivity.class);
                    intent.putExtra(PARAM_EQUIPE_SELECIONADA, equipeSelecionada);
                    intent.putExtra(PARAM_LOCAL_SELECIONADO, localSelecionado);
                    intent.putExtra(PARAM_DATA_SELECIONADA, dataSelecionada);
                    intent.putExtra(getStr(R.string.STRING_INTENT_PARAMS), intentParameters);

                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });
    }

    private boolean isValidationAjusteEquipeOk() {
        EventoEquipeValidator validator = new EventoEquipeValidator(this);
        Error error = validator.isValidationAjusteEquipeOk(equipeSelecionada, eventoEquipes, hasEventoAutomaticoNaoSalvo());

        if (error != null) {
            Util.viewMessage(this, error.getMessage());
            return false;
        }

        return true;
    }

    private void addBtnAddEquipeListener() {
        btnAddEquipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventoEquipeGridActivity.this, AddEquipeLocalGridActivity.class);
                intent.putExtra(PARAM_EVENTO_EQUIPE_SELECIONADA, eventoEquipeSelecionado);
                intent.putExtra(PARAM_EQUIPE_SELECIONADA, equipeSelecionada);
                intent.putExtra(PARAM_LOCAL_SELECIONADO, localSelecionado);
                intent.putExtra(getStr(R.string.STRING_INTENT_PARAMS), intentParameters);

                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    private void addBtnAddLocalizacaoListener() {
        btnAddLocalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventoEquipeGridActivity.this, LocalizacaoActivity.class);
                intent.putExtra(PARAM_FROM_LOCAL_EQUIPE, true);
                intent.putExtra(getStr(R.string.STRING_INTENT_PARAMS), intentParameters);

                startActivityForResult(intent, REQUEST_LOCAL_CODE);
            }
        });
    }

    private void addLocalizacaoAutoCompleteListener() {
        localizacaoAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                localSelecionado = (LocalizacaoVO) parent.getItemAtPosition(i);
                afterLocalClick();
            }

        });

        localizacaoAutoComplete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                localSelecionado = null;
                localizacaoAutoComplete.setText("");
                afterLocalClick();

                return false;
            }
        });

        localizacaoAutoComplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focusOn) {
                if (!focusOn && localSelecionado != null) {
                    localizacaoAutoComplete.setAdapter(null);
                    localizacaoAutoComplete.setText(localSelecionado.getDescricao());
                    localizacaoAutoComplete.setAdapter(localAdapter);
                }
            }
        });

    }

    private void afterLocalClick() {
        eventoEquipes = new ArrayList<EventoEquipeVO>();

        limparEventoForm(true);
        updateDataSet();
        refreshComponents();
    }

    private void addHoraEditTextListener() {
        Util.addMascaraHora(horaIniEditText, horaFimEditText);
        Util.addMascaraHora(horaFimEditText, obsEditText);
    }

    @Override
    protected void editarGridItem(int i) {
        if (eventoEquipes == null || i >= eventoEquipes.size()) {
            return;
        }

        eventoEquipeSelecionado = eventoEquipes.get(i);
        eventoEquipeSelecionado.setTipoApontamento(TipoApontamento.MANUAL);

        //desabilita hora inicio na edicao, pois este faz parte da PK
        horaIniEditText.setKeyListener(null);

        String tipoEvento = eventoEquipeSelecionado.getParalisacao().getDescricaoFormatada();

        for (ServicoVO servico : servicos) {
            ServicoVO srv = eventoEquipeSelecionado.getServico();

            if (srv != null && srv.getId() != null && servico.getId().equals(srv.getId())) {
                eventoEquipeSelecionado.setServico(servico);
                break;
            }
        }

        ServicoVO servicoEquipe = eventoEquipeSelecionado.getServico();
        String servicoTxt = servicoEquipe != null ? servicoEquipe.getDescricao() : "";

        updateFields(tipoEvento, servicoTxt);

        //sempre que for edicao, nao deve permitir alterar o tipo de apontamento
        conteudoLinearLayout5.setVisibility(View.GONE);
    }

    private void updateFields(String tipoEvento, String servicoTxt) {
        updateDefaultHorarioRadioChecked(TipoApontamento.MANUAL.equals(eventoEquipeSelecionado.getTipoApontamento()));

        tipoEventoAutoComplete.setAdapter(null);
        tipoEventoAutoComplete.setText(tipoEvento);
        tipoEventoAutoComplete.setAdapter(tipoEventoAdapter);

        servicoAutoComplete.setAdapter(null);
        servicoAutoComplete.setText(servicoTxt);
        servicoAutoComplete.setAdapter(servicoAdapter);

        horaIniEditText.setText(eventoEquipeSelecionado.getHoraIni());
        horaFimEditText.setText(eventoEquipeSelecionado.getHoraFim());
        obsEditText.setText(eventoEquipeSelecionado.getObservacao());

        tipoEventoSelecionado = eventoEquipeSelecionado.getParalisacao();
        servicoSelecionado = eventoEquipeSelecionado.getServico();
        horaFimAntiga = eventoEquipeSelecionado.getHoraFim();
        servicoAntigo = eventoEquipeSelecionado.getServico();
        paralisacaoAntiga = eventoEquipeSelecionado.getParalisacao();
    }

    private void updateDefaultHorarioRadioChecked(boolean checkManualRadio) {
        if (checkManualRadio) {
            manualRadio.setChecked(true);
        } else {
            automaticoRadio.setChecked(true);
        }
    }

    @Override
    protected void excluirGridItem(int i) {
        EventoEquipeVO eventoEquipeVO = eventoEquipes.get(i);
        eventoEquipeVO.setLocalizacao(localSelecionado);
        dao.getEventoEquipeDAO().delete(eventoEquipeVO);

        atualizarEventosEquipe();
        updateDataSet();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            localSelecionado = (LocalizacaoVO) data.getSerializableExtra(PARAM_LOCAL_SELECIONADO);
            equipeSelecionada = (EquipeTrabalhoVO) data.getSerializableExtra(PARAM_EQUIPE_SELECIONADA);

            if (localSelecionado == null || equipeSelecionada == null) {
                eventoEquipes = new ArrayList<EventoEquipeVO>();
            }

            updateDataSet();
        }

        if (resultCode == EQUIPE_LOCAL_RESULT_CODE) {
            equipeSelecionada = (EquipeTrabalhoVO) data.getSerializableExtra(PARAM_EQUIPE_SELECIONADA);

            if (equipeSelecionada == null) {
                limparEventoForm(true);
            }
        }

        if (requestCode == REQUEST_LOCAL_CODE) {
            localizacoes = dao.getLocalizacaoDAO().findList(true);
            localAdapter = new ArrayAdapter<LocalizacaoVO>(this, android.R.layout.select_dialog_singlechoice, localizacoes);

            localizacaoAutoComplete.setAdapter(localAdapter);
        }
    }

    private EventoEquipeVO preencherEventoEquipe() {
        EditText horaIniEditText = (EditText) findViewById(R.id.horaIniEditText);
        EditText horaFimEditText = (EditText) findViewById(R.id.horaFimEditText);

        if (localSelecionado == null) {
            ListAdapter adapter = localizacaoAutoComplete.getAdapter();
            localSelecionado = adapter != null && adapter.getCount() > 0 ? (LocalizacaoVO) adapter.getItem(0) : null;
        }

        String dataHoraApontamento = eventoEquipeSelecionado != null && eventoEquipeSelecionado.getData() != null ? eventoEquipeSelecionado.getData() : Util.getNow();

        ApropriacaoVO apropriacao = preencherApropriacao(dataHoraApontamento);

        EventoEquipeVO eventoEquipe = new EventoEquipeVO();

        //qdo estiver editando, o id deve ser informado
        eventoEquipe.setId(eventoEquipeSelecionado != null ? eventoEquipeSelecionado.getId() : null);
        eventoEquipe.setHoraIni(horaIniEditText.getText().toString());
        eventoEquipe.setHoraFim(horaFimEditText.getText().toString());
        eventoEquipe.setData(dataHoraApontamento);
        eventoEquipe.setLocalizacao(localSelecionado);
        eventoEquipe.setEquipe(equipeSelecionada);
        eventoEquipe.setServico(servicoSelecionado);
        eventoEquipe.setParalisacao(tipoEventoSelecionado);
        eventoEquipe.setApropriacao(apropriacao);
        eventoEquipe.setTipoApontamento(getTipoApontamentoSelecionado());
        eventoEquipe.setObservacao(obsEditText.getText().toString());

        eventoEquipe.setHoraFimAntiga(horaFimAntiga);
        eventoEquipe.setServicoAntigo(servicoAntigo);
        eventoEquipe.setParalisacaoAntiga(paralisacaoAntiga);

        return eventoEquipe;
    }

    private ApropriacaoVO preencherApropriacao(String dataHoraApontamento) {
        ApropriacaoVO apropriacao = new ApropriacaoVO();
        apropriacao.setAtividade(localSelecionado != null ? localSelecionado.getAtividade() : null);
        apropriacao.setDataHoraApontamento(dataHoraApontamento);
        apropriacao.setTipoApropriacao(TIPO_APROPRIACAO_SERVICO);

        return apropriacao;
    }

    private void refreshComponents() {
        refreshEquipeAutoComplete();
    }

    private void refreshEquipeAutoComplete() {
        //so atualiza se nao estiver editando (vindo da tela de consulta)
        if (equipeSelecionada == null) {
            limparEventoForm(true);
        }

        if (localSelecionado != null) {
            equipes = dao.getEquipeTrabalhoDAO().findByLocalizacao(localSelecionado);

            carregarListaServicos();

        } else {
            equipes = new ArrayList<EquipeTrabalhoVO>();
            servicos = new ArrayList<ServicoVO>();
        }

        //atualiza dados equipes
        equipeAdapter = new ArrayAdapter<EquipeTrabalhoVO>(this, android.R.layout.select_dialog_singlechoice, equipes);
        equipeAutoComplete.setAdapter(equipeAdapter);
        servicoAdapter = new ArrayAdapter<ServicoVO>(this, android.R.layout.select_dialog_singlechoice, servicos);
        servicoAutoComplete.setAdapter(servicoAdapter);

        getAdapter().notifyDataSetChanged();
    }

    private void limparEventoForm(boolean limparEquipe) {
        if (limparEquipe) {
            equipeAutoComplete.setText("");
            equipeSelecionada = null;
            isNew = true;
        }

        manualRadio.setChecked(true);
        tipoEventoAutoComplete.setText("");
        servicoAutoComplete.setText("");
        horaIniEditText.setKeyListener(new DigitsKeyListener());
        horaIniEditText.setText("");
        horaFimEditText.setText("");
        obsEditText.setText("");

        tipoEventoSelecionado = null;
        servicoSelecionado = null;
        eventoEquipeSelecionado = null;

        if (isNew) { //so deve permitir escolher o tipo de apontamento quando for cadastro. edicao nao deve permitir
            conteudoLinearLayout5.setVisibility(View.VISIBLE);
        }
    }

    private void bindTipoEventoDefault() {
        if (!automaticoRadio.isChecked() && paralisacoes != null && paralisacoes.size() >= COD_PRODUZINDO
                && localSelecionado != null && equipeSelecionada != null) {
            ParalisacaoVO tipoProduzindo = getParalisacaoProduzindo();
            tipoEventoAutoComplete.setText(tipoProduzindo.getDescricao());
            tipoEventoSelecionado = tipoProduzindo;
        }
    }

    private ParalisacaoVO getParalisacaoProduzindo() {
        for (ParalisacaoVO p : paralisacoes) {
            if (COD_PRODUZINDO.equals(p.getId())) {
                return p;
            }
        }
        return new ParalisacaoVO("");
    }

    /**
     * Antes de retornar a tela anterior, verifica se os dados apontados automaticamente foram salvos
     */
    @Override
    public void onBackPressed() {
        if (hasEventoAutomaticoNaoSalvo()) {
            AlertDialog.Builder dialogBuider = new AlertDialog.Builder(this);
            dialogBuider.setTitle(getStr(R.string.alerta))
                    .setMessage(getStr(R.string.ALERT_SALVAR_EVENTOS_NAO_SALVOS))
                    .setPositiveButton(getStr(R.string.SIM), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                            salvarEvento(true);
                        }
                    })
                    .setNegativeButton(getStr(R.string.NAO), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .create().show();
        } else {
            super.onBackPressed();
        }
    }

    private TipoApontamento getTipoApontamentoSelecionado() {
        return tipoApontamentoRadio.getCheckedRadioButtonId() == manualRadio.getId() ? TipoApontamento.MANUAL : TipoApontamento.AUTOMATICO;
    }

    private boolean hasNoEmptyField() {
        return !tipoEventoAutoComplete.getText().toString().isEmpty()
                || !servicoAutoComplete.getText().toString().isEmpty()
                || !horaIniEditText.getText().toString().isEmpty()
                || !horaFimEditText.getText().toString().isEmpty()
                || !obsEditText.getText().toString().isEmpty();
    }

    /**
     * Verifica se os eventos trazidos automaticamente de Periodo Horas Trabalho ainda nao foram salvos
     *
     * @return
     */
    private boolean hasEventoAutomaticoNaoSalvo() {
        return TipoApontamento.AUTOMATICO.equals(getTipoApontamentoSelecionado()) && isNew && eventoEquipes != null && !eventoEquipes.isEmpty();
    }

}
