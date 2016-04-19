package br.com.constran.mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.NumberKeyListener;
import android.text.method.TextKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;
import br.com.constran.mobile.exception.AlertException;
import br.com.constran.mobile.persistence.vo.aprop.eqp.EventoVO;
import br.com.constran.mobile.persistence.vo.imp.ComponenteVO;
import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;
import br.com.constran.mobile.persistence.vo.imp.ParalisacaoVO;
import br.com.constran.mobile.persistence.vo.imp.ServicoVO;
import br.com.constran.mobile.persistence.vo.menu.LocalizacaoVO;
import br.com.constran.mobile.system.SharedPreferencesHelper;
import br.com.constran.mobile.view.AbstractActivity;
import br.com.constran.mobile.view.interfaces.InterfaceEditActivity;
import br.com.constran.mobile.view.interfaces.InterfaceListActivity;
import br.com.constran.mobile.view.screens.Detail;
import br.com.constran.mobile.view.screens.GridBody;
import br.com.constran.mobile.view.screens.GridFooter;
import br.com.constran.mobile.view.screens.GridHeader;
import br.com.constran.mobile.view.util.References;
import br.com.constran.mobile.view.util.Util;

import java.util.ArrayList;
import java.util.List;

public final class ApropriacaoEquipamentoActivity extends AbstractActivity implements InterfaceEditActivity, InterfaceListActivity {

    private TextView equipamentoView;
    private TextView servicoView;
    private TextView paralisacaoView;
    private TextView componenteView;
    private TextView horaView;
    private Spinner paralisacaoSpn;
    private AutoCompleteTextView servicoList;
    private AutoCompleteTextView componenteList;
    private EditText observacoes;
    private EditText estacaEditText;
    private EditText horaEvento;
    private TextView dots;
    private EditText minutoEvento;
    private EditText horaTerminoEditText;
    private EditText minutoTermino;
    private TableRow rowObservacoes;
    private CheckBox checkApropriar;
    private LocalizacaoVO localizacao;

    private List<EventoVO> eventosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.apropriacoes_equipamento_edit);

        currentClass = ApropriacaoEquipamentoActivity.class;
        currentContext = ApropriacaoEquipamentoActivity.this;

        try {

            init();

            initAtributes();

            editValues();

            initEvents();

            createGridHeader(getGridHeaderValues());
            createGridBody(getGridBodyValues());
            createGridFooter(getGridFooterValues());

            createGridHeader(getGridHeaderTopValues());
            createGridHeader(getGridHeaderValues2());
            createGridBody(getGridBodyValues2());
            createGridFooter(getGridFooterValues2());

            editScreen();

            createDetail(getDetailValues());

        } catch (Exception e) {
            tratarExcecao(e);
        }
    }

    @Override
    public void validateFields() throws Exception {

        String horaInicio = getStr(R.string.EMPTY);
        String minutosInicio = getStr(R.string.EMPTY);

        String horaFim = horaTerminoEditText.getText().toString().trim();
        String minutosFim = minutoTermino.getText().toString().trim();

        if (novoRegistro) {
            horaInicio = horaEvento.getText().toString().trim();
            minutosInicio = minutoEvento.getText().toString().trim();
        } else {
            horaInicio = this.horaInicio.substring(0, 2);
            minutosInicio = this.horaInicio.substring(3, 5);
        }

        if (novoRegistro) {

            if (Util.isHorarioConflitando(eventosList, horaInicio + ":" + minutosInicio, horaFim + ":" + minutosFim)) {
                throw new AlertException(getStr(R.string.ALERT_HOUR_INTERVAL));
            }

            if (idServico == null && idParalisacao == null) {
                throw new AlertException(getStr(R.string.ALERT_SERV_OR_PARALIZ));
            } else if (horaInicio.equals(getStr(R.string.EMPTY)) || minutosInicio.equals(getStr(R.string.EMPTY))) {
                throw new AlertException(Util.getMessage(currentContext, getStr(R.string.hora_evento), R.string.ALERT_REQUIRED));
            } else if (horaInicio.length() != 2 || minutosInicio.length() != 2) {
                throw new AlertException(Util.getMessage(currentContext, getStr(R.string.hora_evento), R.string.ALERT_HOUR_INVALID));
            } else if (Integer.valueOf(horaInicio) > 23 || Integer.valueOf(minutosInicio) > 59) {
                throw new AlertException(Util.getMessage(currentContext, getStr(R.string.hora_evento), R.string.ALERT_HOUR_INVALID));
            }
        }

        if (horaFim.equals(getStr(R.string.EMPTY)) || minutosFim.equals(getStr(R.string.EMPTY))) {
            throw new AlertException(Util.getMessage(currentContext, getStr(R.string.hora_termino), R.string.ALERT_REQUIRED));
        } else if (horaFim.length() != 2 || minutosFim.length() != 2) {
            throw new AlertException(Util.getMessage(currentContext, getStr(R.string.hora_termino), R.string.ALERT_HOUR_INVALID));
        } else if (Integer.valueOf(horaFim) > 23 || Integer.valueOf(minutosFim) > 59) {
            throw new AlertException(Util.getMessage(currentContext, getStr(R.string.hora_termino), R.string.ALERT_HOUR_INVALID));
        } else if (Integer.valueOf(horaFim) < Integer.valueOf(horaInicio)) {
            throw new AlertException(Util.getMessage(currentContext, new String[]{getStr(R.string.hora_evento), getStr(R.string.hora_termino)}, R.string.ALERT_PERIOD_INVALID));
        } else if (Integer.valueOf(horaFim).intValue() == Integer.valueOf(horaInicio).intValue()
                && Integer.valueOf(minutosFim) < Integer.valueOf(minutosInicio)) {
            throw new AlertException(Util.getMessage(currentContext, new String[]{getStr(R.string.hora_evento), getStr(R.string.hora_termino)}, R.string.ALERT_PERIOD_INVALID));
        }

        boolean estacaVazia = estaca.trim().equals(getStr(R.string.EMPTY));

        boolean estacaObrigatoria = true;

        if (estacaVazia) {

            if (idParalisacao != null && reqeParalisacao != null && reqeParalisacao.equals(getStr(R.string.N)))
                estacaObrigatoria = false;

            if (estacaObrigatoria)
                throw new AlertException(Util.getMessage(currentContext, getStr(R.string.estaca), R.string.ALERT_REQUIRED));

        } else {

            Long estacaInicialLocalizacao = Long.valueOf(estacaInicioLocal);
            Long estacaFinalLocalizacao = Long.valueOf(estacaFimLocal);
            Long estaca = Long.valueOf(this.estaca);

            if (estaca < estacaInicialLocalizacao || estaca > estacaFinalLocalizacao) {
                throw new AlertException(Util.getMessage(currentContext, new String[]{descLocal}, R.string.ERROR_VALIDATE_CUTTING));
            }
        }
    }

    @Override
    public void initAtributes() {

        obra = getDAO().getObraDAO().getById(getDAO().getConfiguracoesDAO().getConfiguracaoVO().getIdObra());

        eventosList = new ArrayList<EventoVO>();

        btCancelar = (Button) findViewById(R.id.btEvtEqpCancel);
        btSalvar = (Button) findViewById(R.id.btEvtEqpSave);
        btNovo = (Button) findViewById(R.id.btEvtEqpNew);

        equipamentoView = (TextView) findViewById(R.id.EvtEqpViewEqp);
        servicoView = (TextView) findViewById(R.id.EvtEqpViewSrvc);
        componenteView = (TextView) findViewById(R.id.EvtEqpViewComp);
        horaView = (TextView) findViewById(R.id.horaView);
        paralisacaoView = (TextView) findViewById(R.id.EvtEqpViewParalisacao);
        servicoList = (AutoCompleteTextView) findViewById(R.id.EvtEqpCmbSrvc);
        paralisacaoSpn = (Spinner) findViewById(R.id.EvtEqpSpnParal);
        componenteList = (AutoCompleteTextView) findViewById(R.id.EvtEqpCmbComp);
        rowObservacoes = (TableRow) findViewById(R.id.tbRwEvtEqpObs);
        observacoes = (EditText) findViewById(R.id.EvtEqpEditObs);
        estacaEditText = (EditText) findViewById(R.id.EvtEqpEditEstaca);
        dots = (TextView) findViewById(R.id.twoDots);
        horaEvento = (EditText) findViewById(R.id.eqpEditHorIni);
        minutoEvento = (EditText) findViewById(R.id.eqpEditMinIni);
        horaTerminoEditText = (EditText) findViewById(R.id.eqpEditHorFim);
        minutoTermino = (EditText) findViewById(R.id.eqpEditMinFim);
        checkApropriar = (CheckBox) findViewById(R.id.EvtEqpChkApropriar);


    }

    @Override
    public void editValues() {

        novoRegistro = (intentParameters.getIdRegistroAtual() == null);

        intentParameters.setIdEquipamento((intentParameters.getIdRegistroPai() != null) ? Integer.valueOf(intentParameters.getIdRegistroPai()) : intentParameters.getIdEquipamento());

        idEquipamento = intentParameters.getIdEquipamento();

        //GERA STRING JSON A PARTIR DO OBJETO MODIFICADO
        refreshIntentParameters(intentParameters);


        localizacao = getDAO().getLocalizacaoDAO().findLocalizacaoAtual();

        carregarListaServicos();

        //servicoArrayVO = getDAO().getServicoDAO().getArrayServicoVO(localizacao);

        //20-05-2015 -- adicionado para evitar que tentem usar modulo sem nenhum servico para selecionar.
        if(servicoArrayVO.length == 0){
            AlertDialog.Builder dialogoD = new AlertDialog.Builder(currentContext);
            dialogoD.setMessage("Nenhum serviço está cadastrado para a localização "+localizacao.getDescricao());
            dialogoD.setPositiveButton(getStr(R.string.OK), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface di, int arg) {
                    redirectPrincipal();
                }
            });
            dialogoD.setTitle(getStr(R.string.AVISO));
            dialogoD.show();
            return;
        }


        for (ServicoVO vo : servicoArrayVO) {
            vo.setDescricao(" " + vo.getDescricao());
        }

        componenteArrayVO = getDAO().getComponenteDAO().getArrayComponenteVO(idEquipamento);

        paralisacaoArrayVO = getDAO().getParalisacaoDAO().getArrayParalisacaoVO();

        ArrayAdapter<ServicoVO> adpS = new ArrayAdapter<ServicoVO>(this, android.R.layout.select_dialog_singlechoice, servicoArrayVO);
        adpS.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        servicoList.setAdapter(adpS);

        ArrayAdapter<ParalisacaoVO> adpP = new ArrayAdapter<ParalisacaoVO>(this, android.R.layout.select_dialog_singlechoice, paralisacaoArrayVO);
        adpP.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        paralisacaoSpn.setAdapter(adpP);

        ArrayAdapter<ComponenteVO> adpC = new ArrayAdapter<ComponenteVO>(this, android.R.layout.select_dialog_singlechoice, componenteArrayVO);
        adpC.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        componenteList.setAdapter(adpC);

        String[] dados = getDAO().getLocalizacaoDAO().getValues();

        idFrenteObra = (dados[0] != null) ? Integer.valueOf(dados[0]) : null;
        idAtividade = (dados[1] != null) ? Integer.valueOf(dados[1]) : null;
        estacaInicioView = dados[2];
        estacaFimView = dados[3];
        tipoLocalizacao = (dados[4] != null) ? dados[4] : getStr(R.string.ORIGEM);
        dataHora = dados[5];
        idOrigem = (dados[6] != null) ? Integer.valueOf(dados[6]) : null;
        idDestino = (dados[7] != null) ? Integer.valueOf(dados[7]) : null;
        descLocal = (dados[10] != null) ? dados[10] : dados[11];
        estacaInicioLocal = dados[12].trim();
        estacaFimLocal = dados[13].trim();

        config = getDAO().getConfiguracoesDAO().getConfiguracaoVO();
    }


    /*
    * Caso a obra esteja parametrizada para não usar Planejamento de Serviços faça a busca somente na tabela
    * Servicos e traga todos os serviços cadastrados, independente de Frente de Obra e Atividade.
    *
    * Moisés Santana 16/07/2015
    *
    * */
    private void carregarListaServicos(){
        if(obra.getUsaPlanServico().equals("N")){
            servicoArrayVO = getDAO().getServicoDAO().getArrayAllServicos();
        } else {
            servicoArrayVO = getDAO().getServicoDAO().getArrayServicoVO(localizacao);
        }
    }


    @Override
    public Cursor getCursor() {

        return getDAO().getEquipamentoDAO().getCursorEquipamentosParteDiaria(false);

    }


    @Override
    public GridBody getGridBodyValues() {

        Integer indexCursorIdEquipamento = 0;
        Integer indexCursorPrefixo = 1;

        GridBody grid = new GridBody(this);

        grid.setClassRedirect(ApropriacaoEquipamentoActivity.class);

        grid.setColorTXT(getColor(R.color.BLACK));
        grid.setColorsBKG(new Integer[]{getColor(R.color.GRAY3), getColor(R.color.GRAY4)});

        grid.setCursor(getCursor());
        grid.setColumnsTxt(new Integer[]{indexCursorPrefixo});
        grid.setIndexsPKRow(new Integer[]{indexCursorIdEquipamento});

        grid.setPaintSelected(true);

        grid.setIdTable(References.GRID_ID_PFX);
        grid.setFileLayoutRow(References.GRID_LAYOUT_PFX);
        grid.setIdsXmlColumn(References.GRID_ID_COLUMNS_PFX);
        grid.setTypesColumn(new String[]{getStr(R.string.TYPE_NEXT_PAGE)});


        return grid;

    }

    @Override
    public GridHeader getGridHeaderValues() {

        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.HEADER_LAYOUT_PFX); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.HEADER_ID_COLUMNS_PFX);// Ids (xml) das colunas
        header.setIdTable(References.HEADER_ID_PFX); //Id do TableLayout);
        header.setNameColumns(getStrArray(R.array.HEADER_NAME_COLUMNS_PFX));//Nomes das Colunas

        header.setClassRedirect(EquipamentosParteDiariaActivity.class);

        return header;

    }


    @Override
    public GridFooter getGridFooterValues() {

        GridFooter footer = new GridFooter(this);
        footer.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        footer.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        footer.setFileLayoutRow(References.FOOTER_LAYOUT_PFX); //arquivo xml - layout (TableRow)
        footer.setIdColumns(References.FOOTER_ID_COLUMNS);// Ids (xml) das colunas
        footer.setIdTable(References.FOOTER_ID_PFX); //Id do TableLayout);

        return footer;

    }

    public GridBody getGridBodyValues2() {

        Integer noContent = null;

        Integer indexCursorIdApropriacao = 0;
        Integer indexCursorIdEquipamento = 1;
        Integer indexCursorDataHora = 2;
        Integer indexCursorHoraInicio = 3;
        Integer indexCursorPeriodo = 4;
        Integer indexCursorDescricaoEventos = 5;

        GridBody grid = new GridBody(this);
        grid.setClassRedirect(ApropriacaoEquipamentoActivity.class);

        grid.setCursor(getCursor2());
        grid.setColumnsTxt(new Integer[]{noContent, indexCursorPeriodo, indexCursorDescricaoEventos});
        grid.setIndexsPKRow(new Integer[]{indexCursorIdApropriacao, indexCursorIdEquipamento, indexCursorDataHora, indexCursorHoraInicio});

        grid.setColorTXT(getColor(R.color.BLACK));
        grid.setColorsBKG(new Integer[]{getColor(R.color.WHITE), getColor(R.color.GRAY3)});

        grid.setIdTable(References.GRID_ID_EVT);
        grid.setFileLayoutRow(References.GRID_LAYOUT_EVT);
        grid.setIdsXmlColumn(References.GRID_ID_COLUMNS_EVT);
        grid.setTypesColumn(new String[]{getStr(R.string.TYPE_ROWNUM), getStr(R.string.TYPE_SELECT_EDIT), getStr(R.string.TYPE_SELECT_EDIT)});


        return grid;

    }

    public GridHeader getGridHeaderTopValues() {

        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.TOP_HEADER_LAYOUT_EVT); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.TOP_HEADER_ID_COLUMN);// Ids (xml) das colunas
        header.setIdTable(References.TOP_HEADER_ID_EVT); //Id do TableLayout);
        header.setNameColumns(getStrArray(R.array.TOP_HEADER_NAME_COLUMNS_EVT));//Nomes das Colunas

        return header;

    }

    public GridHeader getGridHeaderValues2() {

        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.GRAY));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.HEADER_LAYOUT_EVT); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.HEADER_ID_COLUMNS_EVT);// Ids (xml) das colunas
        header.setIdTable(References.HEADER_ID_EVT); //Id do TableLayout);
        header.setNameColumns(getStrArray(R.array.HEADER_NAME_COLUMNS_EVT));//Nomes das Colunas

        return header;

    }

    public GridFooter getGridFooterValues2() {

        GridFooter footer = new GridFooter(this);
        footer.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        footer.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        footer.setFileLayoutRow(References.FOOTER_LAYOUT_EVT); //arquivo xml - layout (TableRow)
        footer.setIdColumns(References.FOOTER_ID_COLUMNS);// Ids (xml) das colunas
        footer.setIdTable(References.FOOTER_ID_EVT); //Id do TableLayout);

        return footer;

    }

    private void changeLayout() {

        String[] arrayPK = (!(novoRegistro)) ? Util.getArrayPK(intentParameters.getIdRegistroAtual(), getStr(R.string.TOKEN)) : null;

        if (arrayPK != null) {

            idApropriacao = Integer.valueOf(arrayPK[0]);
            idEquipamento = Integer.valueOf(arrayPK[1]);
            dataHora = arrayPK[2];
            horaInicio = arrayPK[3];
        }

        //GERA STRING JSON A PARTIR DO OBJETO MODIFICADO
        refreshIntentParameters(intentParameters);

        if (novoRegistro) {

            rowObservacoes.setVisibility(View.GONE);
            rowObservacoes.setLayoutParams(new LayoutParams(0, 0));

            checkApropriar.setVisibility(View.GONE);
            checkApropriar.setLayoutParams(new LayoutParams(0, 0));

            servicoList.setVisibility(View.VISIBLE);
            servicoList.setLayoutParams(new LayoutParams(250, 45));

            componenteList.setVisibility(View.VISIBLE);
            componenteList.setLayoutParams(new LayoutParams(250, 45));

            paralisacaoSpn.setVisibility(View.VISIBLE);
            paralisacaoSpn.setLayoutParams(new LayoutParams(250, 45));

            horaEvento.setVisibility(View.VISIBLE);
            horaEvento.setLayoutParams(new LayoutParams(70, 45));

            minutoEvento.setVisibility(View.VISIBLE);
            minutoEvento.setLayoutParams(new LayoutParams(70, 45));

            dots.setVisibility(View.VISIBLE);
            dots.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            servicoView.setVisibility(View.GONE);
            servicoView.setLayoutParams(new LayoutParams(0, 0));

            paralisacaoView.setVisibility(View.GONE);
            paralisacaoView.setLayoutParams(new LayoutParams(0, 0));

            horaView.setVisibility(View.GONE);
            horaView.setLayoutParams(new LayoutParams(0, 0));

            componenteView.setVisibility(View.GONE);
            componenteView.setLayoutParams(new LayoutParams(0, 0));

            checkApropriar.setVisibility(View.GONE);
            checkApropriar.setLayoutParams(new LayoutParams(0, 0));

            rowObservacoes.setVisibility(View.GONE);
            rowObservacoes.setLayoutParams(new LayoutParams(0, 0));

            btNovo.setVisibility(View.GONE);
            btNovo.setLayoutParams(new LayoutParams(0, 0));

            estaca = getStr(R.string.EMPTY);
            estacaEditText.setText(estaca);

            horaEvento.setText(getStr(R.string.EMPTY));
            minutoEvento.setText(getStr(R.string.EMPTY));

            horaTerminoEditText.setText(getStr(R.string.EMPTY));
            minutoTermino.setText(getStr(R.string.EMPTY));

            idServico = null;
            idCategoria = null;
            idComponente = null;
            idParalisacao = null;
            descComponente = null;
            codParalisacao = null;
            descParalisacao = null;
            reqeParalisacao = null;
            descServico = null;

            servicoView.setText(descServico);
            paralisacaoView.setText(codParalisacao);

            paralisacaoView.setText(descParalisacao);
            componenteView.setText(descComponente);

        } else {

            rowObservacoes.setVisibility(View.VISIBLE);
            rowObservacoes.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            checkApropriar.setVisibility(View.VISIBLE);
            checkApropriar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            servicoList.setVisibility(View.GONE);
            servicoList.setLayoutParams(new LayoutParams(0, 0));

            componenteList.setVisibility(View.GONE);
            componenteList.setLayoutParams(new LayoutParams(0, 0));

            paralisacaoSpn.setVisibility(View.GONE);
            paralisacaoSpn.setLayoutParams(new LayoutParams(0, 0));

            horaEvento.setVisibility(View.GONE);
            horaEvento.setLayoutParams(new LayoutParams(0, 0));

            minutoEvento.setVisibility(View.GONE);
            minutoEvento.setLayoutParams(new LayoutParams(0, 0));

            dots.setVisibility(View.GONE);
            dots.setLayoutParams(new LayoutParams(0, 0));

            servicoView.setVisibility(View.VISIBLE);
            servicoView.setLayoutParams(new LayoutParams(250, 45));

			/*			paralisacaoView.setVisibility(View.VISIBLE);
            paralisacaoView.setLayoutParams(new LayoutParams(250,45));
			 */
            horaView.setVisibility(View.VISIBLE);
            horaView.setLayoutParams(new LayoutParams(250, 45));

            componenteView.setVisibility(View.VISIBLE);
            componenteView.setLayoutParams(new LayoutParams(250, 45));


            String[] dados = getDAO().getEventoDAO().getValues(new String[]{String.valueOf(idApropriacao),
                    String.valueOf(idEquipamento), dataHora, horaInicio});

            equipamentoView.setText(dados[0]);
            servicoView.setText(dados[1]);
            paralisacaoView.setText(dados[2]);
            componenteView.setText(dados[3]);
            horaView.setText(dados[4]);

            if (dados[5] != null) {
                horaTerminoEditText.setText(dados[5].substring(0, 2));
                minutoTermino.setText(dados[5].substring(3, 5));
            }
            apropriar = (dados[6] != null && dados[6].equalsIgnoreCase(getStr(R.string.Y)));
            observacoes.setText(dados[7]);
            estacaEditText.setText(dados[8]);

            idParalisacao = (dados[9] != null) ? Integer.valueOf(dados[9]) : null;

            reqeParalisacao = dados[10];
            idServico = dados[11] != null ? Integer.valueOf(dados[11]) : null;

            checkApropriar.setChecked(apropriar);

            btNovo.setVisibility(View.VISIBLE);
            btNovo.setLayoutParams(new LayoutParams(120, LayoutParams.WRAP_CONTENT));
        }

        descEquipamento = getDAO().getEquipamentoDAO().getDescricao(idEquipamento, true, false);

        equipamentoView.setText(descEquipamento);

    }

    @Override
    public void initEvents() {

        paralisacaoSpn.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                ParalisacaoVO p = (ParalisacaoVO) parent.getSelectedItem();
                idParalisacao = p.getId();
                codParalisacao = p.getCodigo();
                descParalisacao = p.getDescricao();
                reqeParalisacao = p.getRequerEstaca();

                validaParalisacao(idParalisacao);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        servicoList.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                clearServico(v);
                return false;
            }
        });

        componenteList.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                clearComponente(v);
                return false;
            }
        });

        horaTerminoEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 2) {
                    minutoTermino.requestFocus();
                    horaTerminoEditText.clearFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        horaEvento.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 2) {
                    minutoEvento.requestFocus();
                    horaEvento.clearFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        minutoEvento.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 2) {
                    horaTerminoEditText.requestFocus();
                    minutoEvento.clearFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        minutoTermino.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 2) {
                    estacaEditText.requestFocus();
                    minutoTermino.clearFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        servicoList.setOnKeyListener(servicoOnKeyListener());

        servicoList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ServicoVO s = (ServicoVO) parent.getItemAtPosition(position);
                idServico = s.getId();
                idParalisacao = null;
                codParalisacao = null;
                idComponente = null;
                idCategoria = null;
                descParalisacao = getStr(R.string.EMPTY);
                reqeParalisacao = getStr(R.string.EMPTY);
                descComponente = getStr(R.string.EMPTY);

                paralisacaoView.setText(descParalisacao);
                componenteList.setText(descComponente);
                componenteView.setText(descComponente);

                componenteList.setVisibility(View.GONE);
                componenteList.setLayoutParams(new LayoutParams(0, 0));

                paralisacaoView.setVisibility(View.VISIBLE);
                paralisacaoView.setLayoutParams(new LayoutParams(250, 45));

                paralisacaoSpn.setVisibility(View.GONE);
                paralisacaoSpn.setLayoutParams(new LayoutParams(0, 0));

                componenteView.setVisibility(View.VISIBLE);
                componenteView.setLayoutParams(new LayoutParams(250, 45));
            }

        });

        servicoList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clearServico(v);
            }
        });

        componenteList.setOnKeyListener(componenteListener());

        componenteList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ComponenteVO c = (ComponenteVO) parent.getItemAtPosition(position);
                idComponente = c.getId();
                idCategoria = c.getCategoria().getId();
            }

        });

        componenteList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clearComponente(v);
            }
        });


        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });


        btSalvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {

                    salvarEvento();

                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }
        });


        btNovo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {

                    novoRegistro = true;
                    intentParameters.setIdRegistroAtual(null);
                    refreshIntentParameters(intentParameters);

                    changeLayout();
                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }
        });
    }

    protected OnKeyListener servicoOnKeyListener() {
        return new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                clearServico(v);
                return false;
            }
        };
    }

    protected OnKeyListener componenteListener() {
        return new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                clearComponente(v);
                return false;
            }
        };
    }

    void clearComponente(View v) {

        ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
        idComponente = null;
        idCategoria = null;
        descComponente = getStr(R.string.EMPTY);

        componenteList.setTextColor(getColor(R.color.BLACK));

    }


    void clearServico(View v) {

        ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
        idServico = null;
        descServico = getStr(R.string.EMPTY);
        servicoList.setTextColor(getColor(R.color.BLACK));

        if (idParalisacao != null) {
            componenteList.setVisibility(View.VISIBLE);
            componenteList.setLayoutParams(new LayoutParams(250, 45));
        }

        paralisacaoSpn.setVisibility(View.VISIBLE);
        paralisacaoSpn.setLayoutParams(new LayoutParams(250, 45));
    }


    void validaParalisacao(Integer id) {

        idServico = null;
        idComponente = null;
        descServico = getStr(R.string.EMPTY);
        descComponente = getStr(R.string.EMPTY);

        componenteView.setText(getStr(R.string.EMPTY));
        componenteList.setText(getStr(R.string.EMPTY));

        if (id != null) {

            servicoList.setVisibility(View.GONE);
            servicoList.setLayoutParams(new LayoutParams(0, 0));
            servicoView.setVisibility(View.VISIBLE);
            servicoView.setLayoutParams(new LayoutParams(250, 45));

            if (codParalisacao.equals("04") || codParalisacao.equals("10")) {

                componenteView.setVisibility(View.GONE);
                componenteView.setLayoutParams(new LayoutParams(0, 0));
                componenteList.setVisibility(View.VISIBLE);
                componenteList.setLayoutParams(new LayoutParams(250, 45));
                componenteList.requestFocus();

            } else {
                componenteList.setVisibility(View.GONE);
                componenteList.setLayoutParams(new LayoutParams(0, 0));
                componenteView.setVisibility(View.VISIBLE);
                componenteView.setLayoutParams(new LayoutParams(250, 45));
            }


        } else {
//			servicoView.setVisibility(View.GONE);
//			servicoView.setLayoutParams(new LayoutParams(0,0));
//			servicoList.setVisibility(View.VISIBLE);
//			servicoList.setLayoutParams(new LayoutParams(250,45));
//			componenteView.setVisibility(View.GONE);
//			componenteView.setLayoutParams(new LayoutParams(0,0));
//			componenteList.setVisibility(View.VISIBLE);
//			componenteList.setLayoutParams(new LayoutParams(250,45));
            servicoView.setVisibility(View.VISIBLE);
            servicoView.setLayoutParams(new LayoutParams(250, 45));
            servicoList.setVisibility(View.VISIBLE);
            servicoList.setLayoutParams(new LayoutParams(250, 45));
            componenteView.setVisibility(View.VISIBLE);
            componenteView.setLayoutParams(new LayoutParams(250, 45));
            componenteList.setVisibility(View.GONE);
            componenteList.setLayoutParams(new LayoutParams(0, 0));

        }

    }


    @Override
    public Detail getDetailValues() {

        //Texto do Detail

        String strDetail = getStr(R.string.DETAIL_EVTS);

        Detail detail = new Detail(this);
        detail.setDetail(strDetail);
        detail.setColorBKG(getColor(R.color.GRAY));// Cor de fundo
        detail.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        detail.setFileLayoutRow(References.DETAIL_LAYOUT); //arquivo xml - layout (TableRow)
        detail.setIdColumns(References.DETAIL_ID_COLUMNS);// Ids (xml) das colunas
        detail.setIdTable(References.DETAIL_ID_EVT_EQP); //Id do TableLayout);

        return detail;
    }

    @Override
    public void onBackPressed() {

        redirect(EquipamentosParteDiariaActivity.class, true);
    }


    public void onLocation() {
        redirect(LocalizacaoActivity.class, false);
    }


    private void salvarEvento() throws Exception {

        estaca = estacaEditText.getText().toString();

        if (novoRegistro) {

            horaInicio = horaEvento.getText().toString().concat(getStr(R.string.TWO_DOTS)).concat(minutoEvento.getText().toString());
            changePkParteDiaria();
        }

        validateFields();

        horaTermino = horaTerminoEditText.getText().toString().concat(getStr(R.string.TWO_DOTS)).concat(minutoTermino.getText().toString());

        EventoVO vo = new EventoVO();
        vo.setIdEquipamento(idEquipamento);
        vo.setIdApropriacao(idApropriacao);
        vo.setDataHora(dataHora);
        vo.setHoraInicio(horaInicio);
        vo.setHoraTermino(horaTermino);
        vo.setEstaca(estaca);

        if (servicoList.getVisibility() == View.VISIBLE || !"".equals(servicoView.getText())) {
            vo.setServico(new ServicoVO(idServico));
        } else {
            vo.setComponente(new ComponenteVO(idComponente));
            vo.setParalisacao(new ParalisacaoVO(idParalisacao));
        }

        if (novoRegistro) {

            String value = getDAO().getUtilDAO().getExist(vo);//Verfica se existe um registro com a mesma chave e retorna o campo apropriar

            if (value.equals(getStr(R.string.Y))) {//Verifica se o registro existente é válido

                throw new AlertException(getStr(R.string.ERROR_EQUAL_TIME_EVN));

            } else if (value.equals(getStr(R.string.N))) {//Verifica se o registro existente não é válido

                String strId = vo.getIdApropriacao().toString().concat(getStr(R.string.TOKEN))
                        .concat(vo.getIdEquipamento().toString()).concat(getStr(R.string.TOKEN))
                        .concat(vo.getDataHora()).concat(getStr(R.string.TOKEN))
                        .concat(vo.getHoraInicio());

                vo.setStrId(strId);//habilita edição para subscrever o registro

            }
            vo.setApropriar(getStr(R.string.Y));
            vo.setObservacoes(getStr(R.string.EMPTY));

        } else {

            vo.setStrId(intentParameters.getIdRegistroAtual());
            vo.setObservacoes(observacoes.getText().toString());

            intentParameters.setIdRegistroAtual(null);
            refreshIntentParameters(intentParameters);

            if (checkApropriar.isChecked()) {
                vo.setApropriar(getStr(R.string.Y));
            } else {
                vo.setApropriar(getStr(R.string.N));
            }
        }

        saveOrUpdate(vo);

        redirect(ApropriacaoEquipamentoActivity.class, false);

    }

    public Cursor getCursor2() {
        if (eventosList == null)
            eventosList = new ArrayList<EventoVO>();

        eventosList = getDAO().getEventoDAO().getListEventosDia(idEquipamento);

        return getDAO().getEventoDAO().getCursorEventosDia(idEquipamento);
    }

    @Override
    public void editScreen() throws Exception {
        changeLayout();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }


    public boolean changePK(Context context) {
        String[] dados = getDAO().getApropriacaoEquipamentoDAO().getPk(idEquipamento);

        boolean exists = (dados != null);

        if (exists) {

            idApropriacao = (dados[0] != null) ? Integer.valueOf(dados[0]) : null;
            idEquipamento = (dados[1] != null) ? Integer.valueOf(dados[1]) : null;
            dataHora = dados[2];

        }

        return exists;
    }

    @Override
    protected void trataDadosQRCode() throws AlertException {
//        EquipamentoParteDiariaVO vo = new EquipamentoParteDiariaVO();
//        vo.setEquipamento(new EquipamentoVO(idEquipamento));
//        vo.setDataHora(Util.getNow());
        int codigoQrCode = idEquipamento;
        EquipamentoVO equip = getDAO().getEquipamentoDAO().getByQRCode(codigoQrCode);


        if(equip != null) {
            idEquipamento = equip.getId();
            intentParameters.setIdRegistroPai(String.valueOf(idEquipamento));
            intentParameters.setIdEquipamento(idEquipamento);
            intentParameters.setFromQRCode(true);

            //so cadastra o equipamento caso ele ainda nao tenha sido cadastrado anteriormente
            if (!getDAO().getEquipamentoDAO().isEquipamentoParteDiariaCadastrado(idEquipamento, false)) {
//            saveOrUpdate(vo);
                intentParameters.setIdRegistroAtual(String.valueOf(idEquipamento));

                //se nao encontrar equipamento apropriado, desabilita campos
//            if(getDAO().getParteDiariaDAO().getValues(idEquipamento) == null) {
//                disableFields();
//            } else {
                enableFields();
//            }

                redirect(DetalhesEquipamentoActivity.class, false);
            } else {
                enableFields();
                //se o equipamento nao foi apropriado (sem autorizacao) entao redireciona para tela de autorizacao
                //senao recarrega tela de apropriacao
                if (getDAO().getEquipamentoDAO().isEquipamentoParteDiariaCadastrado(idEquipamento, true)) {
                    intentParameters.setIdRegistroAtual(String.valueOf(idEquipamento));
                    redirect(DetalhesEquipamentoActivity.class, false);
                } else {
                    //quando ler qrCode deve exibir tela com campos para inserir novo evento para o equipamento
                    //ou seja, o evento do registro atual deve ser nulo
                    intentParameters.setIdRegistroAtual(null);

                    redirect(ApropriacaoEquipamentoActivity.class, false);
                }
            }
        } else {
            throw new AlertException(getStr(R.string.ERROR_QRCODE_EQUIPAMENTO_INVALIDO));
        }

    }

    /**
     * Usado apenas quando QR Code Habilitado
     *
     * @throws AlertException
     */
    private void disableFields() throws AlertException {
        enableFields(false, null, null, null);
        throw new AlertException(getStr(R.string.ERROR_EQUIPAMENTO_INVALIDO));
    }

    /**
     * Habilita/desabilita campos
     * Usado apenas quando QR Code Habilitado
     */
    private void enableFields() {
        TextKeyListener textKeyListener = new TextKeyListener(TextKeyListener.Capitalize.NONE, true);
        DigitsKeyListener digitsKeyListener = new DigitsKeyListener();
        NumberKeyListener nkl = DigitsKeyListener.getInstance(false, true);

        enableFields(true, textKeyListener, digitsKeyListener, nkl);
    }

    /**
     * Usado apenas quando QR Code Habilitado
     *
     * @param enabled
     * @param textKeyListener
     * @param digitsKeyListener
     * @param nkl
     */
    private void enableFields(boolean enabled, TextKeyListener textKeyListener, DigitsKeyListener digitsKeyListener, NumberKeyListener nkl) {
        servicoList.setKeyListener(textKeyListener);
        paralisacaoSpn.setClickable(enabled);
        componenteList.setKeyListener(textKeyListener);
        horaEvento.setKeyListener(digitsKeyListener);
        minutoEvento.setKeyListener(digitsKeyListener);
        horaTerminoEditText.setKeyListener(digitsKeyListener);
        minutoTermino.setKeyListener(digitsKeyListener);
        estacaEditText.setKeyListener(digitsKeyListener);
        btNovo.setEnabled(enabled);
        btSalvar.setEnabled(enabled);
    }
}
