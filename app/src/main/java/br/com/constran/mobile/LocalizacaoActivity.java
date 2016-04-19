package br.com.constran.mobile;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;
import br.com.constran.mobile.exception.AlertException;
import br.com.constran.mobile.persistence.vo.ObraVO;
import br.com.constran.mobile.persistence.vo.imp.AtividadeVO;
import br.com.constran.mobile.persistence.vo.imp.FrenteObraVO;
import br.com.constran.mobile.persistence.vo.imp.OrigemDestinoVO;
import br.com.constran.mobile.persistence.vo.menu.LocalizacaoVO;
import br.com.constran.mobile.view.AbstractActivity;
import br.com.constran.mobile.view.interfaces.InterfaceEditActivity;
import br.com.constran.mobile.view.interfaces.InterfaceListActivity;
import br.com.constran.mobile.view.screens.Detail;
import br.com.constran.mobile.view.screens.GridBody;
import br.com.constran.mobile.view.screens.GridFooter;
import br.com.constran.mobile.view.screens.GridHeader;
import br.com.constran.mobile.view.util.References;
import br.com.constran.mobile.view.util.Util;

public final class LocalizacaoActivity extends AbstractActivity implements InterfaceListActivity, InterfaceEditActivity {
    private static final String PARAM_LOCAL_SELECIONADO = "localSelecionado";
    private static final String PARAM_FROM_MODULO_MAO_OBRA = "fromLocalizacaoEquipe";
    private static final String ESTACA_INI_DEFAULT = "000000";
    private static final String ESTACA_FIM_DEFAULT = "999999";

    private LinearLayout conteudoLinearLayout;
    private LinearLayout conteudoLinearLayout2;
    private LinearLayout conteudoLinearLayout3;
    private LinearLayout conteudoLinearLayout4;
    private LinearLayout conteudoLinearLayout5;
    private LinearLayout conteudoLinearLayout6;

    private LinearLayout headerLinearLayout;
    private LinearLayout headerLinearLayout2;
    private LinearLayout headerLinearLayout3;
    private LinearLayout headerLinearLayout4;
    private LinearLayout headerLinearLayout5;
    private LinearLayout headerLinearLayout6;

    private TableRow rowEstacas;

    private Spinner atividadeSpin;
    private Spinner frenteObraSpin;
    private Spinner tipoSpin;
    private TextView dataHoraTextView;
    private TextView frenteObraView;
    private TextView atividadeView;
    private TextView tipoView;
    private TextView labelTipo;
    private EditText estacaIni;
    private EditText estacaFimEditText;
    private TextView estacaIniView;
    private TextView estacaFimTextView;

    //linhas que podem ser ocultadas quando tela for chamada do modulo de mao-de-obra
    private TableRow tipoRow;
    private TableRow origemDestinoRow;

    private RadioButton radioOrgDst;
    private RadioButton radioOrg;
    private RadioButton radioDst;
    private RadioGroup tipoRadio;
    private TextView rodape;
    private String[] dados;

    private ObraVO obraVO;

    private boolean fromLocalizacaoEquipe;
    private LocalizacaoVO localSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.localizacao_edit);

        currentClass = LocalizacaoActivity.class;
        currentContext = LocalizacaoActivity.this;

        try {

            init();

            initAtributes();

            editValues();

            initEvents();

            createDetail(getDetailValues());

            editScreen();

            createGridHeader(getGridHeaderValues());

            createGridHeader(getGridHeaderTopValues());

            createGridBody(getGridBodyValues());

            createGridFooter(getGridFooterValues());

        } catch (Exception e) {
            tratarExcecao(e);
        }
    }

    @Override
    public void initAtributes() {
        conteudoLinearLayout = (LinearLayout) findViewById(R.id.conteudoLinearLayout);
        conteudoLinearLayout2 = (LinearLayout) findViewById(R.id.conteudoLinearLayout2);
        conteudoLinearLayout3 = (LinearLayout) findViewById(R.id.conteudoLinearLayout3);
        conteudoLinearLayout4 = (LinearLayout) findViewById(R.id.conteudoLinearLayout4);
        conteudoLinearLayout5 = (LinearLayout) findViewById(R.id.conteudoLinearLayout5);
        conteudoLinearLayout6 = (LinearLayout) findViewById(R.id.conteudoLinearLayout6);

        rowEstacas = (TableRow) findViewById(R.id.tbRwLcl5);

        atividadeSpin = (Spinner) findViewById(R.id.LclCmbAtv);
        frenteObraSpin = (Spinner) findViewById(R.id.LclCmbFob);
        tipoSpin = (Spinner) findViewById(R.id.LclCmbOrgDst);
        dataHoraTextView = (TextView) findViewById(R.id.lclDtHr);
        frenteObraView = (TextView) findViewById(R.id.lclViewFob);
        tipoView = (TextView) findViewById(R.id.lclViewTipo);
        atividadeView = (TextView) findViewById(R.id.lclViewAtv);
        labelTipo = (TextView) findViewById(R.id.lblTipo);
        estacaIni = (EditText) findViewById(R.id.LclEditEstcIni);
        estacaFimEditText = (EditText) findViewById(R.id.LclEditEstcFim);
        estacaIniView = (TextView) findViewById(R.id.lclViewEi);
        estacaFimTextView = (TextView) findViewById(R.id.lclViewEf);
        radioOrg = (RadioButton) findViewById(R.id.radioOrg);
        radioOrgDst = (RadioButton) findViewById(R.id.radioOrgDst);
        radioDst = (RadioButton) findViewById(R.id.radioDst);
        tipoRadio = (RadioGroup) findViewById(R.id.lclRadioGroupTipo);
        btSalvar = (Button) findViewById(R.id.btLclSave);
        btCancelar = (Button) findViewById(R.id.btLclCancel);
        btNovo = (Button) findViewById(R.id.btLclNovo);
        tipoRow = (TableRow) findViewById(R.id.tbRwLclTp);
        origemDestinoRow = (TableRow) findViewById(R.id.tbRwLcl8);
        rodape = (TextView) findViewById(R.id.descRodape);

        fixBackgroundColor();
    }

    @Override
    public void editValues() {

        fromLocalizacaoEquipe = getIntent().getBooleanExtra(PARAM_FROM_MODULO_MAO_OBRA, false);

        idLocalizacao = (intentParameters.getIdRegistroCopia() != null) ? Integer.valueOf(intentParameters.getIdRegistroCopia()) : idLocalizacao;

        if (idLocalizacao != null) {
            LocalizacaoVO local = getDAO().getLocalizacaoDAO().getById(idLocalizacao);
            saveOrUpdate(local);
            localSelecionado = local;
        }

        config = getDAO().getConfiguracoesDAO().getConfiguracaoVO();
        obraVO = getDAO().getObraDAO().getById(config.getIdObra());

        ccObra = config.getIdObra();

        if (config.getTolerancia() != null)
            rodape.setText(Util.getMessage(currentContext, config.getTolerancia().toString(), R.string.DESC_RODAPE_LOCAL));
        else
            rodape.setText(Util.getMessage(currentContext, "0", R.string.DESC_RODAPE_LOCAL));

        dados = getDAO().getLocalizacaoDAO().getValues();

        novoRegistro = (dados == null);

        changeLayout();

        if (!(novoRegistro)) {

            idFrenteObra = (dados[0] != null) ? Integer.valueOf(dados[0]) : null;
            idAtividade = (dados[1] != null) ? Integer.valueOf(dados[1]) : null;
            estacaInicio = dados[2];
            estacaFim = dados[3];
            tipoLocalizacao = (dados[4] != null) ? dados[4] : getStr(R.string.ORIGEM);
            dataHora = dados[5];
            idOrigem = (dados[6] != null) ? Integer.valueOf(dados[6]) : null;
            idDestino = (dados[7] != null) ? Integer.valueOf(dados[7]) : null;
            idSelected = (idOrigem != null) ? idOrigem : idDestino;

        } else {

            clear();
        }

        labelTipo.setText(tipoLocalizacao);

        intentDesc = getStr(R.string.DETAIL_LOCATION);

        exibirOrigemDestino();
    }

    private void showHideRows() {
        if (fromLocalizacaoEquipe) {
            rowEstacas.setVisibility(View.GONE);
            tipoRow.setVisibility(View.GONE);
            origemDestinoRow.setVisibility(View.GONE);
        }
    }

    private void exibirOrigemDestino() {
        if (obraVO != null && "S".equalsIgnoreCase(obraVO.getUsaOrigemDestino())) {
            radioOrgDst.setVisibility(View.VISIBLE);
        } else {
            radioOrgDst.setVisibility(View.GONE);
        }
    }


    public void changeLayout() {

        if (novoRegistro) {

            tipoLocalizacao = getStr(R.string.ORIGEM);

            labelTipo.setText(tipoLocalizacao);

            frenteObraView.setVisibility(View.GONE);
            frenteObraView.setLayoutParams(new LayoutParams(0, 0));
            frenteObraSpin.setVisibility(View.VISIBLE);
            frenteObraSpin.setLayoutParams(new LayoutParams(380, 45));

            atividadeView.setVisibility(View.GONE);
            atividadeView.setLayoutParams(new LayoutParams(0, 0));
            atividadeSpin.setVisibility(View.VISIBLE);
            atividadeSpin.setLayoutParams(new LayoutParams(380, 60));

            tipoView.setVisibility(View.GONE);
            tipoView.setLayoutParams(new LayoutParams(0, 0));

            tipoSpin.setVisibility(View.VISIBLE);
            tipoSpin.setLayoutParams(new LayoutParams(380, 55));

            btNovo.setVisibility(View.GONE);
            btNovo.setLayoutParams(new LayoutParams(0, 0));

            btSalvar.setVisibility(View.VISIBLE);
            btSalvar.setLayoutParams(new LayoutParams(150, LayoutParams.WRAP_CONTENT));

            tipoRow.setVisibility(View.VISIBLE);
            tipoRow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            tipoRadio.setVisibility(View.VISIBLE);
            tipoRadio.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            estacaIni.setVisibility(View.VISIBLE);
            estacaIni.setLayoutParams(new LayoutParams(100, 45));
            estacaFimEditText.setVisibility(View.VISIBLE);
            estacaFimEditText.setLayoutParams(new LayoutParams(100, 45));

            estacaIniView.setVisibility(View.GONE);
            estacaIniView.setLayoutParams(new LayoutParams(0, 0));
            estacaFimTextView.setVisibility(View.GONE);
            estacaFimTextView.setLayoutParams(new LayoutParams(0, 0));


        } else {

            frenteObraSpin.setVisibility(View.GONE);
            frenteObraSpin.setLayoutParams(new LayoutParams(0, 0));
            frenteObraView.setVisibility(View.VISIBLE);
            frenteObraView.setLayoutParams(new LayoutParams(380, 45));

            atividadeSpin.setVisibility(View.GONE);
            atividadeSpin.setLayoutParams(new LayoutParams(0, 0));
            atividadeView.setVisibility(View.VISIBLE);
            atividadeView.setLayoutParams(new LayoutParams(380, 60));

            tipoSpin.setVisibility(View.GONE);
            tipoSpin.setLayoutParams(new LayoutParams(0, 0));
            tipoView.setVisibility(View.VISIBLE);
            tipoView.setLayoutParams(new LayoutParams(380, 55));

            tipoRow.setVisibility(View.GONE);
            tipoRow.setLayoutParams(new LayoutParams(0, 0));

            btSalvar.setVisibility(View.GONE);
            btSalvar.setLayoutParams(new LayoutParams(0, 0));

            btNovo.setVisibility(View.VISIBLE);
            btNovo.setLayoutParams(new LayoutParams(150, LayoutParams.WRAP_CONTENT));

            tipoRadio.setVisibility(View.GONE);
            tipoRadio.setLayoutParams(new LayoutParams(0, 0));

            estacaIniView.setVisibility(View.VISIBLE);
            estacaIniView.setLayoutParams(new LayoutParams(100, 45));
            estacaFimTextView.setVisibility(View.VISIBLE);
            estacaFimTextView.setLayoutParams(new LayoutParams(100, 45));

            estacaIni.setVisibility(View.GONE);
            estacaIni.setLayoutParams(new LayoutParams(0, 0));
            estacaFimEditText.setVisibility(View.GONE);
            estacaFimEditText.setLayoutParams(new LayoutParams(0, 0));

        }

        showHideRows();
    }

    @Override
    public void validateFields() throws Exception {

        if (idFrenteObra == null) {
            throw new AlertException(Util.getMessage(currentContext, getStr(R.string.frente_obra), R.string.ALERT_REQUIRED));
        }
        if (idAtividade == null) {
            throw new AlertException(Util.getMessage(currentContext, getStr(R.string.atividade), R.string.ALERT_REQUIRED));
        }
        if (estacaInicio == null || estacaFim == null || estacaInicio.isEmpty() || estacaFim.isEmpty()) {
            String origemDestino = labelTipo.getText().toString();
            throw new AlertException(Util.getMessage(currentContext, origemDestino, R.string.ALERT_REQUIRED_ESTACAS));
        }

        final int TAM_ESTACAS = 6;

        if (estacaInicio.length() < TAM_ESTACAS || estacaFim.length() < TAM_ESTACAS) {
            throw new AlertException(Util.getMessage(currentContext, new String[]{getStr(R.string.estacas), "6"}, R.string.ALERT_LENGTH_NUMBER));
        }

        if (idSelected == null && !fromLocalizacaoEquipe) {
            throw new AlertException(Util.getMessage(currentContext, tipoLocalizacao, R.string.ALERT_REQUIRED));
        }
    }

    @Override
    public void initEvents() {

        frenteObraSpin.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {


                FrenteObraVO a = (FrenteObraVO) parent.getSelectedItem();
                idFrenteObra = a.getId();

                atividadeArrayVO = getDAO().getAtividadeDAO().getArrayAtividadeVO(idFrenteObra);

                ArrayAdapter<AtividadeVO> adpA = new ArrayAdapter<AtividadeVO>(LocalizacaoActivity.this, android.R.layout.select_dialog_singlechoice, atividadeArrayVO);
                adpA.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                atividadeSpin.setAdapter(adpA);


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        atividadeSpin.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                AtividadeVO a = (AtividadeVO) parent.getSelectedItem();
                idAtividade = a.getIdAtividade();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        tipoSpin.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeSelectTipo();
                return false;
            }
        });

        tipoSpin.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                OrigemDestinoVO od = (OrigemDestinoVO) parent.getSelectedItem();

                if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.ORIGEM))) {
                    idSelected = od.getId();
                    idOrigem = idSelected;
                    idDestino = null;
                } else if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.DESTINO))) {
                    idSelected = od.getId();
                    idDestino = idSelected;
                    idOrigem = null;
                } else {
                    idSelected = od.getId();
                    idOrigem = idSelected;
                    idDestino = idSelected;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        radioOrg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean checked = ((RadioButton) v).isChecked();
                if (checked) {
                    tipoLocalizacao = getStr(R.string.ORIGEM);
                    labelTipo.setText(tipoLocalizacao);
                    idOrigem = null;
                    idDestino = null;
                    labelTipo.setText(tipoLocalizacao);


                    clearSelectTipo();

                }

            }
        });

        radioDst.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean checked = ((RadioButton) v).isChecked();
                if (checked) {
                    tipoLocalizacao = getStr(R.string.DESTINO);
                    labelTipo.setText(tipoLocalizacao);
                    idDestino = null;
                    idOrigem = null;
                    labelTipo.setText(tipoLocalizacao);


                    clearSelectTipo();

                }

            }
        });

        radioOrgDst.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean checked = ((RadioButton) v).isChecked();
                if (checked) {
                    tipoLocalizacao = getStr(R.string.ORIGEM_DESTINO);
                    labelTipo.setText(tipoLocalizacao);
                    idOrigem = null;
                    idDestino = null;
                    labelTipo.setText(tipoLocalizacao);


                    clearSelectTipo();
                }

            }
        });


        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                try {

                    dataHora = dataHoraTextView.getText().toString();

                    if (fromLocalizacaoEquipe) {
                        bindDefaultValues();
                    } else {
                        estacaInicio = estacaIni.getText().toString();
                        estacaFim = estacaFimEditText.getText().toString();
                    }

                    validateFields();

                    LocalizacaoVO vo = new LocalizacaoVO();

                    vo.setAtividade(new AtividadeVO(idAtividade, idFrenteObra));
                    vo.setDataAtualizacao(Util.getNow());
                    vo.setDestino(new OrigemDestinoVO(idDestino));
                    vo.setOrigem(new OrigemDestinoVO(idOrigem));
                    vo.setTipo(tipoLocalizacao);

                    vo.setDataHora(dataHora);
                    vo.setEstacaInicial(estacaInicio);
                    vo.setEstacaFinal(estacaFim);

                    saveOrUpdate(vo);

                    redirect(currentClass, false);
                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }
        });

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View argetDetailValuesg0) {
                onBackPressed();
            }
        });

        btNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View argetDetailValuesg0) {
                novoRegistro = true;

                changeLayout();

                clear();
            }
        });
    }

    /**
     * Preenche os valores default quando a localizacao for chamada a partir do modulo de mao-de-obra
     * Valores default:
     * -Tipo Localizacao: Origem
     * -id Origem: primeiro registro trazido da query de origens/destinos
     * -Estaca Inicio: ESTACA_INI_DEFAULT
     * -Estaca Fim: ESTACA_FIM_DEFAULT
     */
    private void bindDefaultValues() {
        int FIRST_VALID_INDEX = 1;
        estacaInicio = ESTACA_INI_DEFAULT;
        estacaFim = ESTACA_FIM_DEFAULT;
        tipoLocalizacao = getStr(R.string.ORIGEM);

        if (origemDestinoArrayVO == null || origemDestinoArrayVO.length <= FIRST_VALID_INDEX) {
            origemDestinoArrayVO = getDAO().getOrigemDestinoDAO().getArrayOrigemDestinoVO(tipoLocalizacao, estacaInicio, estacaFim);
        }

        idOrigem = origemDestinoArrayVO != null && origemDestinoArrayVO.length > FIRST_VALID_INDEX ? origemDestinoArrayVO[FIRST_VALID_INDEX].getId() : null;
        idSelected = idOrigem;
    }

    @Override
    public void putExtraParams(Intent intent) {
        super.putExtraParams(intent);
        intent.putExtra(PARAM_FROM_MODULO_MAO_OBRA, fromLocalizacaoEquipe);
    }

    public void changeSelectTipo() {
        String estacaInicio = estacaIni.getText().toString();
        String estacaFinal = estacaFimEditText.getText().toString();

        origemDestinoArrayVO = getDAO().getOrigemDestinoDAO().getArrayOrigemDestinoVO(tipoLocalizacao, estacaInicio, estacaFinal);

        ArrayAdapter<OrigemDestinoVO> adpO = new ArrayAdapter<OrigemDestinoVO>(LocalizacaoActivity.this, android.R.layout.select_dialog_singlechoice, origemDestinoArrayVO);
        adpO.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        tipoSpin.setAdapter(adpO);

    }

    public void clearSelectTipo() {

        origemDestinoArrayVO = new OrigemDestinoVO[]{new OrigemDestinoVO(getStr(R.string.SELECT))};

        ArrayAdapter<OrigemDestinoVO> adpO = new ArrayAdapter<OrigemDestinoVO>(LocalizacaoActivity.this, android.R.layout.select_dialog_singlechoice, origemDestinoArrayVO);
        adpO.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        tipoSpin.setAdapter(adpO);

    }


    public void clear() {

        frenteObraArrayVO = getDAO().getFrenteObraDAO().getArrayFrenteObraVO(ccObra);
        atividadeArrayVO = getDAO().getAtividadeDAO().getArrayAtividadeVO(idFrenteObra);
        origemDestinoArrayVO = getDAO().getOrigemDestinoDAO().getArrayOrigemDestinoVO(tipoLocalizacao, estacaInicio, estacaFim);

        ArrayAdapter<FrenteObraVO> adpF = new ArrayAdapter<FrenteObraVO>(this, android.R.layout.select_dialog_singlechoice, frenteObraArrayVO);
        adpF.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        frenteObraSpin.setAdapter(adpF);

        ArrayAdapter<OrigemDestinoVO> adpO = new ArrayAdapter<OrigemDestinoVO>(this, android.R.layout.select_dialog_singlechoice, origemDestinoArrayVO);
        adpO.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        tipoSpin.setAdapter(adpO);

        ArrayAdapter<AtividadeVO> adpA = new ArrayAdapter<AtividadeVO>(this, android.R.layout.select_dialog_singlechoice, atividadeArrayVO);
        adpA.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        atividadeSpin.setAdapter(adpA);

        btCancelar.setText(getStr(R.string.cancelar));

        dataHora = Util.getNow();

        indexFrenteObra = 0;
        indexOrigemDestino = 0;
        indexAtividade = 0;

        tipoLocalizacao = getStr(R.string.ORIGEM);

        estacaInicio = getStr(R.string.EMPTY);
        estacaFim = getStr(R.string.EMPTY);

        idFrenteObra = null;
        idAtividade = null;
        idOrigem = null;
        idDestino = null;
        idSelected = null;

        dataHoraTextView.setText(dataHora);
        tipoSpin.setSelection(indexOrigemDestino);
        frenteObraSpin.setSelection(indexFrenteObra);
        atividadeSpin.setSelection(indexAtividade);
        estacaIni.setText(estacaInicio);
        estacaFimEditText.setText(estacaFim);
        radioOrg.setChecked(true);
    }

    @Override
    public void onBackPressed() {

        if (fromLocalizacaoEquipe) {
            Intent intent = getIntent();
            intent.putExtra(PARAM_LOCAL_SELECIONADO, localSelecionado);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            redirectPrincipal();
        }

    }

    @Override
    public Detail getDetailValues() {

        //Texto do Detail

        String strDetail = getStr(R.string.TITLE_LCL);

        Detail detail = new Detail(this);
        detail.setDetail(strDetail);
        detail.setColorBKG(getColor(R.color.GRAY));// Cor de fundo
        detail.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        detail.setFileLayoutRow(References.DETAIL_LAYOUT); //arquivo xml - layout (TableRow)
        detail.setIdColumns(References.DETAIL_ID_COLUMNS);// Ids (xml) das colunas
        detail.setIdTable(References.DETAIL_ID_LOCAL); //Id do TableLayout);

        return detail;
    }


    @Override
    public void editScreen() throws Exception {

        if (novoRegistro) {

            clear();

        } else {

            btCancelar.setText(getStr(R.string.OK));
            dataHoraTextView.setText(dados[5]);
            frenteObraView.setText(dados[8]);
            atividadeView.setText(dados[9]);
            estacaIniView.setText(estacaInicio);
            estacaFimTextView.setText(estacaFim);

            String tipo = (dados[10] != null) ? dados[10] : dados[11];
            tipoView.setText(tipo);
        }
    }

    @Override
    public GridBody getGridBodyValues() throws Exception {

        Integer indexCursorId = 0;
        Integer indexCursorDataHora = 1;
        Integer indexCursorDescricao = 2;
        Integer indexCursorTipoLocal = 3;

        GridBody grid = new GridBody(this);

        grid.setClassRefresh(currentClass);

        grid.setCursor(getCursor());
        grid.setIndexsPKRow(new Integer[]{indexCursorId});
        grid.setColumnsTxt(new Integer[]{indexCursorDataHora, indexCursorDescricao, indexCursorTipoLocal});

        grid.setColorTXT(getColor(R.color.BLACK));
        grid.setColorsBKG(new Integer[]{getColor(R.color.WHITE), getColor(R.color.GRAY3)});

        grid.setIdTable(References.GRID_ID_LCL);
        grid.setFileLayoutRow(References.GRID_LAYOUT_LCL);
        grid.setIdsXmlColumn(References.GRID_ID_COLUMNS_LCL);
        grid.setTypesColumn(new String[]{getStr(R.string.TYPE_SELECT_COPY), getStr(R.string.TYPE_SELECT_COPY), getStr(R.string.TYPE_SELECT_COPY)});

        return grid;

    }

    public GridHeader getGridHeaderValues() throws Exception {

        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.GRAY));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.HEADER_LAYOUT_LCL); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.HEADER_ID_COLUMNS_LCL);// Ids (xml) das colunas
        header.setIdTable(References.HEADER_ID_LCL); //Id do TableLayout);
        header.setNameColumns(getStrArray(R.array.HEADER_NAME_COLUMNS_LCL));//Nomes das Colunas

        return header;
    }

    public GridHeader getGridHeaderTopValues() {

        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.TOP_HEADER_LAYOUT_LCL); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.TOP_HEADER_ID_COLUMN);// Ids (xml) das colunas
        header.setIdTable(References.TOP_HEADER_ID_LCL); //Id do TableLayout);
        header.setNameColumns(getStrArray(R.array.TOP_HEADER_NAME_COLUMNS_LCL));//Nomes das Colunas

        return header;

    }

    @Override
    public GridFooter getGridFooterValues() throws Exception {

        GridFooter footer = new GridFooter(this);
        footer.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        footer.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        footer.setFileLayoutRow(References.FOOTER_LAYOUT_LCL); //arquivo xml - layout (TableRow)
        footer.setIdColumns(References.FOOTER_ID_COLUMNS);// Ids (xml) das colunas
        footer.setIdTable(References.FOOTER_ID_LCL); //Id do TableLayout);

        return footer;

    }

    @Override
    public Cursor getCursor() throws Exception {

        return getDAO().getLocalizacaoDAO().getCursor();

    }

    private void fixBackgroundColor() {

        if (conteudoLinearLayout != null)
            conteudoLinearLayout.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        if (conteudoLinearLayout2 != null)
            conteudoLinearLayout2.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        if (conteudoLinearLayout3 != null)
            conteudoLinearLayout3.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        if (conteudoLinearLayout4 != null)
            conteudoLinearLayout4.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        if (conteudoLinearLayout5 != null)
            conteudoLinearLayout5.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        if (conteudoLinearLayout6 != null)
            conteudoLinearLayout6.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));

        if (headerLinearLayout != null)
            headerLinearLayout.setBackgroundColor(getResources().getColor(R.color.GRAY2));
        if (headerLinearLayout2 != null)
            headerLinearLayout2.setBackgroundColor(getResources().getColor(R.color.GRAY2));
        if (headerLinearLayout3 != null)
            headerLinearLayout3.setBackgroundColor(getResources().getColor(R.color.GRAY2));
        if (headerLinearLayout4 != null)
            headerLinearLayout4.setBackgroundColor(getResources().getColor(R.color.GRAY2));
        if (headerLinearLayout5 != null)
            headerLinearLayout5.setBackgroundColor(getResources().getColor(R.color.GRAY2));
        if (headerLinearLayout6 != null)
            headerLinearLayout6.setBackgroundColor(getResources().getColor(R.color.GRAY2));

    }

}