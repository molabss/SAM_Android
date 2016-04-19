package br.com.constran.mobile.view.maodeobra;

import android.text.method.DigitsKeyListener;
import android.text.method.TextKeyListener;
import android.view.View;
import android.widget.*;
import br.com.constran.mobile.R;
import br.com.constran.mobile.adapter.maodeobra.ProducaoEquipeAdapter;
import br.com.constran.mobile.persistence.vo.aprop.ApropriacaoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.ApropriacaoServicoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.EquipeTrabalhoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.EventoEquipeVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.TipoApontamento;
import br.com.constran.mobile.persistence.vo.imp.ParalisacaoVO;
import br.com.constran.mobile.persistence.vo.imp.ServicoVO;
import br.com.constran.mobile.persistence.vo.menu.LocalizacaoVO;
import br.com.constran.mobile.validator.EventoEquipeValidator;
import br.com.constran.mobile.view.util.Error;
import br.com.constran.mobile.view.util.Util;

import java.util.List;

/**
 * Criado em 11/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class ProducaoEquipeGridActivity extends GridActivity<ProducaoEquipeAdapter> {

    private static DigitsKeyListener digitsKeyListener = new DigitsKeyListener();
    private static TextKeyListener textKeyListener = new TextKeyListener(TextKeyListener.Capitalize.NONE, true);

    private GridView servicoProducaoGridView;

    private Button btAddLocalizacao;
    private Button btnAddEquipe;

    private AutoCompleteTextView localizacaoAutoComplete;
    private AutoCompleteTextView equipeAutoComplete;

    private EditText servicoEditText;
    private EditText producaoEditText;
    private EditText horaIniEditText;
    private EditText horaFimEditText;
    private EditText obsEditText;

    private TextView unidadeTextView;

    private ImageView saveEventoImage;

    private List<ApropriacaoServicoVO> apropriacaoServicos;
    private List<LocalizacaoVO> localizacoes;
    private List<EquipeTrabalhoVO> equipes;

    private LocalizacaoVO localSelecionado;
    private EquipeTrabalhoVO equipeSelecionada;
    private ServicoVO servicoSelecionado;
    private ApropriacaoServicoVO apropriacaoServicoSelecionado;
    private EventoEquipeVO eventoEquipeSelecionado;

    private ArrayAdapter<LocalizacaoVO> localAdapter;
    private ArrayAdapter<EquipeTrabalhoVO> equipeAdapter;

    private boolean readOnly;

    @Override
    protected int getContentView() {
        return R.layout.producao_equipe;
    }

    @Override
    protected void onResume() {
        super.onResume();
        localSelecionado = (LocalizacaoVO) getIntent().getSerializableExtra(PARAM_LOCAL_SELECIONADO);
        equipeSelecionada = (EquipeTrabalhoVO) getIntent().getSerializableExtra(PARAM_EQUIPE_SELECIONADA);

        localizacaoAutoComplete.setAdapter(null);
        localizacaoAutoComplete.setText(localSelecionado != null ? localSelecionado.getDescricao() : "");
        localizacaoAutoComplete.setAdapter(localAdapter);

        equipeAutoComplete.setAdapter(null);
        equipeAutoComplete.setText(equipeSelecionada != null ? equipeSelecionada.getApelido() : "");
        equipeAutoComplete.setAdapter(equipeAdapter);

        showHideEmptyText(apropriacaoServicos);
    }

    @Override
    public void initAttributes() {
        btAddLocalizacao = (Button) findViewById(R.id.btAddLocalizacao);
        btnAddEquipe = (Button) findViewById(R.id.btnAddEquipe);
        servicoProducaoGridView = (GridView) findViewById(R.id.servicoProducaoGridView);
        localizacaoAutoComplete = (AutoCompleteTextView) findViewById(R.id.localizacaoAutoComplete);
        equipeAutoComplete = (AutoCompleteTextView) findViewById(R.id.equipeAutoComplete);
        servicoEditText = (EditText) findViewById(R.id.servicoEditText);
        producaoEditText = (EditText) findViewById(R.id.producaoEditText);
        horaIniEditText = (EditText) findViewById(R.id.horaIniEditText);
        horaFimEditText = (EditText) findViewById(R.id.horaFimEditText);
        obsEditText = (EditText) findViewById(R.id.obsEditText);
        saveEventoImage = (ImageView) findViewById(R.id.saveEventoImage);
        gridVazioTextView = (TextView) findViewById(R.id.empty_list_view);
        unidadeTextView = (TextView) findViewById(R.id.unidadeTextView);
    }

    @Override
    public void bindComponents() {
        btAddLocalizacao.setVisibility(View.GONE);
        btnAddEquipe.setVisibility(View.GONE);

        equipeSelecionada = (EquipeTrabalhoVO) getIntent().getSerializableExtra(PARAM_EQUIPE_SELECIONADA);
        eventoEquipeSelecionado = (EventoEquipeVO) getIntent().getSerializableExtra(PARAM_EVENTO_EQUIPE_SELECIONADA);

        if (eventoEquipeSelecionado != null) {
            apropriacaoServicos = dao.getApropriacaoServicoDAO().findByEventoEquipe(eventoEquipeSelecionado);
        } else {
            apropriacaoServicos = dao.getApropriacaoServicoDAO().findByLocalAndEquipeAndData(localSelecionado, equipeSelecionada, Util.getToday());
        }

        localizacoes = dao.getLocalizacaoDAO().findList(isTodaysEvent());
        equipes = dao.getEquipeTrabalhoDAO().findAllItems();

        localAdapter = new ArrayAdapter<LocalizacaoVO>(this, android.R.layout.select_dialog_singlechoice, localizacoes);
        equipeAdapter = new ArrayAdapter<EquipeTrabalhoVO>(this, android.R.layout.select_dialog_singlechoice, equipes);

        localizacaoAutoComplete.setAdapter(localAdapter);
        equipeAutoComplete.setAdapter(equipeAdapter);

        showHideEmptyText(apropriacaoServicos);
        unidadeTextView.setText("");

        //so deve permitir salvar apontamento de produção se existir serviço produzido
        //paralisacao nao deve permitir gerar uma nova entrada
        //so permite salvar os items já existentes, pois apenas a producao é apontada
        saveEventoImage.setVisibility(View.INVISIBLE);

        desabilitarEdicaoComponentes();

        //atualmente os autocompletes de localizacao e equipe sao somente leitura, caso mude, remover linha abaixo
        readOnly = true;
        tratarModoConsulta();
    }

    private void desabilitarEdicaoComponentes() {
        producaoEditText.setKeyListener(null);
        horaIniEditText.setKeyListener(null);
        horaFimEditText.setKeyListener(null);
        obsEditText.setKeyListener(null);
    }

    private void tratarModoConsulta() {
        if (readOnly) {
            localizacaoAutoComplete.setKeyListener(null);
            equipeAutoComplete.setKeyListener(null);
        }
    }

    /**
     * Verifica se o evento selecionado é do dia atual
     *
     * @return
     */
    private boolean isTodaysEvent() {
//        readOnly = !Util.getToday().equals(Util.toSimpleDateFormat(eventoEquipeSelecionado.getData()));
//        return !readOnly;
        return !Util.getToday().equals(Util.toSimpleDateFormat(eventoEquipeSelecionado.getData()));
    }

    @Override
    protected void initAdapter() {
        setAdapter(new ProducaoEquipeAdapter(this, apropriacaoServicos));
        servicoProducaoGridView.setAdapter(getAdapter());
    }

    @Override
    public void addListeners() {
        if (!readOnly) {
            addLocalizacaoAutoCompleteListener();
            addEquipeAutoCompleteListener();
        }

        addBtnSaveEventoListener();
        addServicoProducaoGridViewListener();
        addServicoAutoCompleteListener();
        addProducaoEditTextListener();
        addHorarioEditTextListener();
        addObsEditTextListener();
    }

    private void addHorarioEditTextListener() {
        Util.addMascaraHora(horaIniEditText, horaFimEditText);
        Util.addMascaraHora(horaFimEditText, obsEditText);

//        horaIniEditText.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                horaIniEditText.setText("");
//                return false;
//            }
//        });

        horaFimEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                horaFimEditText.setText("");
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

    private void addProducaoEditTextListener() {
        producaoEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                producaoEditText.setText("");
                return false;
            }
        });
    }

    private void addEquipeAutoCompleteListener() {
        equipeAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                equipeSelecionada = (EquipeTrabalhoVO) parent.getItemAtPosition(i);
                eventoEquipeSelecionado = null;

                limparForm();
                updateDataSet();
            }
        });

        equipeAutoComplete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                equipeAutoComplete.setText("");
                eventoEquipeSelecionado = null;
                equipeSelecionada = null;

                limparForm();
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

    private void addServicoAutoCompleteListener() {
        servicoEditText.setKeyListener(null);
    }

    private void addBtnSaveEventoListener() {
        saveEventoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidationOk()) {
                    salvarEventoEquipe();
                }
            }
        });
    }

    private void addServicoProducaoGridViewListener() {
        servicoProducaoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                gridClickListener(adapterView, view, i, l);
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
                localizacaoAutoComplete.setText("");
                localSelecionado = null;
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
        equipeAutoComplete.setText("");
        equipeSelecionada = null;
        eventoEquipeSelecionado = null;

        limparForm();
        updateDataSet();
    }

    private void limparForm() {
        saveEventoImage.setVisibility(View.INVISIBLE);
        servicoEditText.setText("");
        servicoSelecionado = null;
        producaoEditText.setText("");
        obsEditText.setText("");
        producaoEditText.setKeyListener(null);
        obsEditText.setKeyListener(null);
    }

    private void updateDataSet() {
        if (eventoEquipeSelecionado != null) {
            apropriacaoServicos = dao.getApropriacaoServicoDAO().findByEventoEquipe(eventoEquipeSelecionado);
        } else {
            apropriacaoServicos = dao.getApropriacaoServicoDAO().findByLocalAndEquipeAndData(localSelecionado, equipeSelecionada, Util.getToday());
        }

        setAdapter(new ProducaoEquipeAdapter(this, apropriacaoServicos));
        servicoProducaoGridView.setAdapter(getAdapter());

        showHideEmptyText(apropriacaoServicos);
    }

    private boolean isValidationOk() {
        if (servicoSelecionado == null) {
            Util.viewMessage(this, Util.getMessage(this, getStr(R.string.servico), R.string.ALERT_REQUIRED));
            return false;
        }

        String qte = producaoEditText.getText().toString().trim();

        if (qte.isEmpty()) {
            Util.viewMessage(this, Util.getMessage(this, getStr(R.string.producao), R.string.ALERT_REQUIRED));
            return false;
        }

        try {
            Double.parseDouble(qte);
        } catch (Exception e) {
            Util.viewMessage(this, getStr(R.string.producao), R.string.ALERT_INVALID_VALUE);
            return false;
        }

        String horaIni = horaIniEditText.getText().toString();
        String horaFim = horaFimEditText.getText().toString();

        if (!Util.isHoraValida(horaIni)) {
            Util.viewMessage(this, Util.getMessage(this, getStr(R.string.hora_inicio), R.string.ALERT_HOUR_INVALID));
            return false;
        }

        if (horaFim != null && !horaFim.isEmpty() && !Util.isHoraValida(horaFim)) {
            Util.viewMessage(this, Util.getMessage(this, getStr(R.string.hora_termino), R.string.ALERT_HOUR_INVALID));
            return false;
        }

        if (!Util.isPeriodoHorasValido(horaIni, horaFim)) {
            Util.viewMessage(this, Util.getMessage(this, getStr(R.string.hora_termino), R.string.ALERT_HOUR_INVALID));
            return false;
        }

        EventoEquipeValidator validator = new EventoEquipeValidator(this);
        ParalisacaoVO tipoEventoSelecionado = new ParalisacaoVO(COD_PRODUZINDO, "");
        String horaIniStr = horaIniEditText.getText().toString();
        String horaFimStr = horaFimEditText.getText().toString();

        List<EventoEquipeVO> eventosEquipe = getEventosEquipe();

        Error error = validator.validate(eventosEquipe, getEventoEquipeSelecionado(eventosEquipe), equipeSelecionada,
                tipoEventoSelecionado, servicoSelecionado, horaIniStr, horaFimStr);

        if (error != null) {
            Util.viewMessage(this, error.getMessage());
            return false;
        }

        return true;
    }

    private void salvarEventoEquipe() {
        ApropriacaoVO apropriacao = preencherApropriacao();

        ApropriacaoServicoVO apropriacaoServico = preencherApropriacaoServico(apropriacao);

        dao.getApropriacaoServicoDAO().save(apropriacaoServico);

        if (eventoEquipeSelecionado != null) {
            apropriacaoServicos = dao.getApropriacaoServicoDAO().findByEventoEquipe(eventoEquipeSelecionado);
        } else {
            apropriacaoServicos = dao.getApropriacaoServicoDAO().findByLocalAndEquipeAndData(localSelecionado, equipeSelecionada, Util.getToday());
        }

        setAdapter(new ProducaoEquipeAdapter(this, apropriacaoServicos));
        servicoProducaoGridView.setAdapter(getAdapter());

        //limpar
        limparCamposEditaveis();

        //o botao de salvar so deve estar disponivel quando um registro for selecionado para edicao
        saveEventoImage.setVisibility(View.INVISIBLE);

    }

    private void limparCamposEditaveis() {
        apropriacaoServicoSelecionado = null;
        servicoEditText.setText("");
        servicoSelecionado = null;
        producaoEditText.setText("");
        horaIniEditText.setText("");
        horaFimEditText.setText("");
        obsEditText.setText("");
        unidadeTextView.setText("");
        producaoEditText.setKeyListener(null);
        horaIniEditText.setKeyListener(null);
        horaFimEditText.setKeyListener(null);
        obsEditText.setKeyListener(null);
    }

    private ApropriacaoVO preencherApropriacao() {
        ApropriacaoVO apropriacao = eventoEquipeSelecionado != null ? eventoEquipeSelecionado.getApropriacao() : new ApropriacaoVO();

        if (apropriacao.getId() == null) {
            apropriacao.setAtividade(localSelecionado.getAtividade());
            apropriacao.setTipoApropriacao(TIPO_APROPRIACAO_SERVICO);
            apropriacao.setDataHoraApontamento(Util.getNow());
            apropriacao = dao.getApropriacaoDAO().findByPK(apropriacao);
        }

        return apropriacao;
    }

    private ApropriacaoServicoVO preencherApropriacaoServico(ApropriacaoVO apropriacao) {
        ApropriacaoServicoVO apropriacaoServico = new ApropriacaoServicoVO();
        apropriacaoServico.setApropriacao(apropriacao);
        apropriacaoServico.setEquipe(equipeSelecionada);
        apropriacaoServico.setServico(servicoSelecionado);
        apropriacaoServico.setQuantidadeProduzida(Double.parseDouble(producaoEditText.getText().toString()));
        apropriacaoServico.setHoraIni(horaIniEditText.getText().toString());
        apropriacaoServico.setHoraFim(horaFimEditText.getText().toString());
        apropriacaoServico.setObservacoes(obsEditText.getText().toString());

        return apropriacaoServico;
    }

    @Override
    protected void editarGridItem(int i) {
        apropriacaoServicoSelecionado = apropriacaoServicos.get(i);
        servicoSelecionado = apropriacaoServicoSelecionado.getServico();
        servicoEditText.setText(apropriacaoServicoSelecionado.getServico().getDescricao());
        producaoEditText.setText(String.valueOf(apropriacaoServicoSelecionado.getQuantidadeProduzida()));
        horaIniEditText.setText(apropriacaoServicoSelecionado.getHoraIni());
        horaFimEditText.setText(apropriacaoServicoSelecionado.getHoraFim());
        obsEditText.setText(apropriacaoServicoSelecionado.getObservacoes());
        unidadeTextView.setText(servicoSelecionado.getUnidadeMedida());

        producaoEditText.setKeyListener(digitsKeyListener);
//        horaIniEditText.setKeyListener(digitsKeyListener); faz parte da chave, entao nao deve permitir editar
        horaFimEditText.setKeyListener(digitsKeyListener);
        obsEditText.setKeyListener(textKeyListener);

        //so permite editar os items
        saveEventoImage.setVisibility(View.VISIBLE);
    }

    @Override
    protected void excluirGridItem(int i) {
        ApropriacaoServicoVO apropriacaoServicoVO = apropriacaoServicos.get(i);
        dao.getApropriacaoServicoDAO().delete(apropriacaoServicoVO);
        apropriacaoServicos.remove(i);

        setAdapter(new ProducaoEquipeAdapter(this, apropriacaoServicos));
        servicoProducaoGridView.setAdapter(getAdapter());
        apropriacaoServicoSelecionado = null;
    }

    private List<EventoEquipeVO> getEventosEquipe() {
        return dao.getEventoEquipeDAO().findByLocalizacaoAndEquipe(localSelecionado, equipeSelecionada);
    }

    private EventoEquipeVO getEventoEquipeSelecionado(List<EventoEquipeVO> eventosEquipe) {
        String horaIni = horaIniEditText.getText().toString();

        for (EventoEquipeVO ee : eventosEquipe) {
            if (equipeSelecionada.getId().equals(ee.getEquipe().getId()) && localSelecionado.getId().equals(ee.getLocalizacao().getId())
                    && servicoSelecionado.getId().equals(ee.getServico().getId()) && horaIni.equals(ee.getHoraIni())) {
                return ee;
            }
        }

        EventoEquipeVO ee = new EventoEquipeVO();
        ee.setHoraIni(horaIni);
        ee.setHoraFim(horaFimEditText.getText().toString());
        ee.setTipoApontamento(TipoApontamento.MANUAL);

        return ee;
    }

}
