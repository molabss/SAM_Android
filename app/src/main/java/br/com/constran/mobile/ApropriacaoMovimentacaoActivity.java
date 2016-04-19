package br.com.constran.mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.NumberKeyListener;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;
import br.com.constran.mobile.enums.TipoModulo;
import br.com.constran.mobile.exception.AlertException;
import br.com.constran.mobile.persistence.vo.ObraVO;
import br.com.constran.mobile.persistence.vo.aprop.ApropriacaoVO;
import br.com.constran.mobile.persistence.vo.aprop.mov.ApropriacaoMovimentacaoVO;
import br.com.constran.mobile.persistence.vo.aprop.mov.EquipamentoMovimentacaoDiariaVO;
import br.com.constran.mobile.persistence.vo.aprop.mov.FormMovimentacaoVO;
import br.com.constran.mobile.persistence.vo.aprop.mov.ViagemVO;
import br.com.constran.mobile.persistence.vo.imp.AtividadeVO;
import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;
import br.com.constran.mobile.persistence.vo.imp.MaterialVO;
import br.com.constran.mobile.persistence.vo.imp.OrigemDestinoVO;
import br.com.constran.mobile.qrcode.ZBarScannerActivity;
import br.com.constran.mobile.view.AbstractActivity;
import br.com.constran.mobile.view.interfaces.InterfaceEditActivity;
import br.com.constran.mobile.view.interfaces.InterfaceListActivity;
import br.com.constran.mobile.view.screens.Detail;
import br.com.constran.mobile.view.screens.GridBody;
import br.com.constran.mobile.view.screens.GridFooter;
import br.com.constran.mobile.view.screens.GridHeader;
import br.com.constran.mobile.view.util.Eticket;
import br.com.constran.mobile.view.util.References;
import br.com.constran.mobile.view.util.SaveVariables;
import br.com.constran.mobile.view.util.Util;
//import com.dm.zbar.android.scanner.ZBarScannerActivity;

import java.util.ArrayList;
import java.util.Calendar;

public final class ApropriacaoMovimentacaoActivity extends AbstractActivity implements InterfaceEditActivity, InterfaceListActivity {

    private TextView equipamentoView;
    private TextView equipamentoCargaView;
    private TextView horaView;
    private Spinner materialSpn;
    private AutoCompleteTextView equipamentoCargaList;
    private EditText prcCarga;
    private EditText observacoes;
    private EditText pesoEditText;
    private EditText estacaIni;
    private EditText estacaFimEditText;
    private EditText horimetroEditText;
    private EditText hodometroEditText;

    private TableRow rowCarga;
    private TableRow rowPeso;
    private TableRow rowTipo;
    private TableRow rowEticket;
    private TableRow rowObservacoes;
    private TableRow rowHorimetro;
    private TableRow rowHodometro;

    private EditText editEticket;
    private TextView txtEticketView;
    private CheckBox checkApropriar;
    private RadioButton radioOrg;
    private RadioButton radioDst;
    private Eticket eTicketVO;
    private TextView dots;
    private EditText minutoViagem;
    private EditText horaViagemEditText;

    private Integer nroFormularioEdicao;
    private Integer nroQRCodeEdicao;
    private Integer codSeguracaEdicao;

    private EquipamentoVO equipamentoVO;
    private ObraVO obraVO;
    private String horimetro;
    private String hodometro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentClass = ApropriacaoMovimentacaoActivity.class;
        currentContext = ApropriacaoMovimentacaoActivity.this;

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

            //se QR code Obra estiver habilidado e nao vier da leitura, entao desabilita campos
            enableFields(intentParameters.isFromQRCode());

        } catch (AlertException e) {
            if (e.getMessage().equals(getStr(R.string.ERROR_EQUIPAMENTO_INVALIDO))) {
                tratarEquipamentoNaoEncontrado(e);
            }
        } catch (Exception e) {
            tratarExcecao(e);
        }
    }

    @Override
    public void validateFields() throws Exception {

        if (tipoViagem.equals(getStr(R.string.ORIGEM))) {

            if (idEquipamentoCarga == null) {
                throw new AlertException(Util.getMessage(currentContext, getStr(R.string.equipamentoCarga), R.string.ALERT_REQUIRED));
            }
        }

        if (idMaterial == null) {
            throw new AlertException(Util.getMessage(currentContext, getStr(R.string.material), R.string.ALERT_REQUIRED));
        }

        String horaInicio = getStr(R.string.EMPTY);
        String minutosInicio = getStr(R.string.EMPTY);

        if (novoRegistro) {

            horaInicio = horaViagemEditText.getText().toString().trim();
            minutosInicio = minutoViagem.getText().toString().trim();

            if (horaInicio.equals(getStr(R.string.EMPTY)) || minutosInicio.equals(getStr(R.string.EMPTY))) {
                throw new AlertException(Util.getMessage(currentContext, getStr(R.string.hora_viagem), R.string.ALERT_REQUIRED));
            } else if (horaInicio.length() != 2 || minutosInicio.length() != 2) {
                throw new AlertException(Util.getMessage(currentContext, getStr(R.string.hora_viagem), R.string.ALERT_HOUR_INVALID));
            } else if (Integer.valueOf(horaInicio) > 23 || Integer.valueOf(minutosInicio) > 59) {
                throw new AlertException(Util.getMessage(currentContext, getStr(R.string.hora_viagem), R.string.ALERT_HOUR_INVALID));
            }
        }

        if (estacaInicio.equals(getStr(R.string.EMPTY)) || estacaFim.equals(getStr(R.string.EMPTY))) {
            throw new AlertException(Util.getMessage(currentContext, getStr(R.string.estacas), R.string.ALERT_REQUIRED));
        }
        if (estacaInicio.length() < 6 || estacaFim.length() < 6) {
            throw new AlertException(Util.getMessage(currentContext, new String[]{getStr(R.string.estacas), "6"}, R.string.ALERT_LENGTH_NUMBER));
        }
        if (prcCarga.getText().toString().trim().equals(getStr(R.string.EMPTY))) {
            throw new AlertException(Util.getMessage(currentContext, getStr(R.string.percentual_carga), R.string.ALERT_REQUIRED));
        }
        if (Integer.parseInt(prcCarga.getText().toString()) > 100) {
            throw new AlertException(Util.getMessage(currentContext, getStr(R.string.percentual_carga), R.string.ALERT_INVALID_VALUE));
        }


        EquipamentoVO equipamentoVO = getDAO().getEquipamentoDAO().getById(idEquipamento);
        String tipo = equipamentoVO.getTipo();

        if ("".equals(tipo) && "S".equalsIgnoreCase(obraVO.getHorimetroObrigatorio()) && horimetroEditText.getText().toString().trim().isEmpty()
                && hodometroEditText.getText().toString().trim().isEmpty()) {
            throw new AlertException(getStr(R.string.ALERT_HOR_OR_KM_REQUIRED));
        }

        if (horimetroEditText.getText().toString().trim().isEmpty() && "S".equalsIgnoreCase(obraVO.getHorimetroObrigatorio()) && "H".equalsIgnoreCase(tipo)) {
            throw new AlertException(Util.getMessage(currentContext, getStr(R.string.horimetro), R.string.ALERT_REQUIRED));
        }

        if (hodometroEditText.getText().toString().trim().isEmpty() && "S".equalsIgnoreCase(obraVO.getHorimetroObrigatorio()) && "K".equalsIgnoreCase(tipo)) {
            throw new AlertException(Util.getMessage(currentContext, getStr(R.string.hodometro), R.string.ALERT_REQUIRED));
        }

    }

    @Override
    public void initAtributes() {

        //if(config.getIdObra().equals(302100)) {
            //SaveVariables.getInstance().setTipoModulo(TipoModulo.FICHA_3_VIAS);
        //}

        setContentView(R.layout.apropriacoes_movimentacao_edit);

        btCancelar = (Button) findViewById(R.id.btVgsEqpCancel);
        btSalvar = (Button) findViewById(R.id.btVgsEqpSave);
        btNovo = (Button) findViewById(R.id.btVgsEqpNew);

        equipamentoView = (TextView) findViewById(R.id.VgsEqpViewEqp);
        equipamentoCargaView = (TextView) findViewById(R.id.VgsEqpViewEqpCrg);
        horaView = (TextView) findViewById(R.id.horaView);
        dots = (TextView) findViewById(R.id.twoDots);
        horaViagemEditText = (EditText) findViewById(R.id.vgsEditHorIni);
        minutoViagem = (EditText) findViewById(R.id.vgsEditMinIni);
        materialSpn = (Spinner) findViewById(R.id.VgsEqpCmbMat);
        equipamentoCargaList = (AutoCompleteTextView) findViewById(R.id.VgsEqpCmbEqpCrg);
        editEticket = (EditText) findViewById(R.id.VgsEqpEditEticket);
        txtEticketView = (TextView) findViewById(R.id.VgsEqpViewNVale);
        rowEticket = (TableRow) findViewById(R.id.tbRwVgsEqpEticket);
        rowCarga = (TableRow) findViewById(R.id.tbRwVgsEqpCrg);
        rowTipo = (TableRow) findViewById(R.id.tbRwVgsEqpTp);
        rowPeso = (TableRow) findViewById(R.id.tbRwVgsEqpPso);
        rowObservacoes = (TableRow) findViewById(R.id.tbRwVgsEqpObs);
        prcCarga = (EditText) findViewById(R.id.VgsEqpEditPrcCrg);
        observacoes = (EditText) findViewById(R.id.VgsEqpEditObs);
        estacaIni = (EditText) findViewById(R.id.VgsEqpEditEstacaIni);
        estacaFimEditText = (EditText) findViewById(R.id.VgsEqpEditEstacaFim);
        pesoEditText = (EditText) findViewById(R.id.VgsEqpEditPeso);
        checkApropriar = (CheckBox) findViewById(R.id.VgsEqpChkApropriar);

        horimetroEditText = (EditText) findViewById(R.id.VgsEqpEditHorimetro);
        hodometroEditText = (EditText) findViewById(R.id.VgsEqpEditHodometro);

        rowHorimetro = (TableRow) findViewById(R.id.tbRwVgsEqpHorimetro);
        rowHodometro = (TableRow) findViewById(R.id.tbRwVgsEqpHodometro);

        radioOrg = (RadioButton) findViewById(R.id.radioOrg);
        radioDst = (RadioButton) findViewById(R.id.radioDst);

        Util.addMascaraNumerica(pesoEditText, 7, 2);
    }

    @Override
    public void editValues() {

        obraVO = getDAO().getObraDAO().getById(config.getIdObra());

        //SETANDO O TIPO DE MODULO PARA FICHA MOTORISTA
        //ISSO E NECESSARIO POIS OUTRO TIPO DE FORMULARIO DE REGISTRO DE VIAGEM FOI IMPLEMENTADO,
        //POREM TI DA CONSTRAN DECIDIU MANTER O FORMULARIO FICHA_MOTORISTA COMO UNICO E PADRAO EM TODAS
        //AS OBRAS.
        SaveVariables.getInstance().setTipoModulo(TipoModulo.FICHA_MOTORISTA);
        //---------------------------------------------------------------------------------------------


        /*
        //OBTENDO TIPO DE FOMULARIO DE MOVIMENTACAO DA OBRA ATUAL
        if("S".equalsIgnoreCase(obraVO.getUsaCVC())){
            SaveVariables.getInstance().setTipoModulo(TipoModulo.FICHA_3_VIAS);
        }
        */


        novoRegistro = (intentParameters.getIdRegistroAtual() == null);

        intentParameters.setIdEquipamento((intentParameters.getIdRegistroPai() != null) ? Integer.valueOf(intentParameters.getIdRegistroPai()) : intentParameters.getIdEquipamento());

        idEquipamento = intentParameters.getIdEquipamento();

        refreshIntentParameters(intentParameters);

        materialArrayVO = getDAO().getMaterialDAO().getArrayMaterialVO();

        equipamentoCargaArrayVO = getDAO().getEquipamentoDAO().getArrayEquipamentoCarga();

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

        tipoViagem = (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.ORIGEM_DESTINO))) ? getStr(R.string.ORIGEM) : tipoLocalizacao;
    }

    /**
     * Caso a obra utilize QR code e o dado não foi lido a partir de um QR code
     * os campos editaveis devem ser desabilitados
     */
    private void enableFields(boolean enabled) {
        //se nao estiver configurado para utilizar QR Code, entao os campos devem estar habilitados
        boolean naoUsaQRCode = !"S".equalsIgnoreCase(obraVO.getUsaQRCode());

        if (naoUsaQRCode) {
            enabled = true;
        }

        TextKeyListener textKeyListener = !enabled ? null : new TextKeyListener(TextKeyListener.Capitalize.NONE, true);
        DigitsKeyListener digitsKeyListener = !enabled ? null : new DigitsKeyListener();
        NumberKeyListener nkl = !enabled ? null : DigitsKeyListener.getInstance(false, true);

        enableFields(enabled, naoUsaQRCode, textKeyListener, digitsKeyListener, nkl);
    }

    private void enableFields(boolean enabled, boolean naoUsaQRCode, TextKeyListener textKeyListener, DigitsKeyListener digitsKeyListener, NumberKeyListener nkl) {
        equipamentoCargaList.setKeyListener(textKeyListener);
        materialSpn.setClickable(enabled);
        horaViagemEditText.setKeyListener(digitsKeyListener);
        minutoViagem.setKeyListener(digitsKeyListener);
        pesoEditText.setKeyListener(nkl);
        estacaIni.setKeyListener(digitsKeyListener);
        estacaFimEditText.setKeyListener(digitsKeyListener);
        prcCarga.setKeyListener(digitsKeyListener);
        horimetroEditText.setKeyListener(digitsKeyListener);
        hodometroEditText.setKeyListener(digitsKeyListener);
        radioOrg.setClickable(enabled);
        radioDst.setClickable(enabled);
        editEticket.setKeyListener(textKeyListener);
        btSalvar.setEnabled(enabled);
        btNovo.setEnabled(naoUsaQRCode ? enabled : false);

        if (checkApropriar.getVisibility() == View.VISIBLE) {
            btSalvar.setEnabled(true);
        }
    }

    @Override
    public Cursor getCursor() {
        return getDAO().getEquipamentoDAO().getCursorEquipamentosMovimentacaoDiaria(false);
    }

    @Override
    public GridBody getGridBodyValues() {

        Integer indexCursorIdEquipamento = 0;
        Integer indexCursorEquipamento = 1;

        GridBody grid = new GridBody(this);

        grid.setClassRedirect(ApropriacaoMovimentacaoActivity.class);

        grid.setCursor(getCursor());
        grid.setColumnsTxt(new Integer[]{indexCursorEquipamento});
        grid.setIndexsPKRow(new Integer[]{indexCursorIdEquipamento});

        grid.setColorsBKG(new Integer[]{getColor(R.color.GRAY3), getColor(R.color.GRAY4)});

        grid.setIdTable(References.GRID_ID_PFX);
        grid.setColorTXT(getColor(R.color.BLACK));
        grid.setFileLayoutRow(References.GRID_LAYOUT_PFX);
        grid.setIdsXmlColumn(References.GRID_ID_COLUMNS_PFX);
        grid.setTypesColumn(new String[]{getStr(R.string.TYPE_NEXT_PAGE)});

        grid.setPaintSelected(true);


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

        header.setClassRedirect(EquipamentosMovimentacaoDiariaActivity.class);

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
        Integer indexCursorHoraInicio = 2;
        Integer indexCursorHoraViagem = 3;
        Integer indexCursorEstacas = 4;
        Integer indexCursorEticket = 5;

        GridBody grid = new GridBody(this);
        grid.setClassRedirect(ApropriacaoMovimentacaoActivity.class);

        grid.setColorTXT(getColor(R.color.BLACK));
        grid.setColorsBKG(new Integer[]{getColor(R.color.WHITE), getColor(R.color.GRAY3)});

        grid.setCursor(getCursor2());
        grid.setColumnsTxt(new Integer[]{noContent, indexCursorHoraViagem, indexCursorEstacas, indexCursorEticket});
        grid.setIndexsPKRow(new Integer[]{indexCursorIdApropriacao, indexCursorIdEquipamento, indexCursorHoraInicio, indexCursorHoraViagem});

        grid.setIdTable(References.GRID_ID_VEQ);
        grid.setFileLayoutRow(References.GRID_LAYOUT_VEQ);
        grid.setIdsXmlColumn(References.GRID_ID_COLUMNS_VEQ);
        grid.setTypesColumn(new String[]{getStr(R.string.TYPE_ROWNUM), getStr(R.string.TYPE_SELECT_EDIT), getStr(R.string.TYPE_SELECT_EDIT), getStr(R.string.TYPE_SELECT_EDIT)});

        return grid;


    }

    public GridHeader getGridHeaderTopValues() {

        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.TOP_HEADER_LAYOUT_VEQ); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.TOP_HEADER_ID_COLUMN);// Ids (xml) das colunas
        header.setIdTable(References.TOP_HEADER_ID_VEQ); //Id do TableLayout);
        header.setNameColumns(getStrArray(R.array.TOP_HEADER_NAME_COLUMNS_VEQ));//Nomes das Colunas

        return header;

    }

    public GridHeader getGridHeaderValues2() {

        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.GRAY));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.HEADER_LAYOUT_VEQ); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.HEADER_ID_COLUMNS_VEQ);// Ids (xml) das colunas
        header.setIdTable(References.HEADER_ID_VEQ); //Id do TableLayout);

        TipoModulo tipoModuloQrCode = SaveVariables.getInstance().getTipoModulo();

        if("S".equalsIgnoreCase(obraVO.getUsaQRCode()) && tipoModuloQrCode.equals(TipoModulo.FICHA_MOTORISTA)) {
            header.setNameColumns(getStrArray(R.array.HEADER_NAME_COLUMNS_VEQ_QR_CODE));//Nomes das Colunas para QRCode
        } else {
            header.setNameColumns(getStrArray(R.array.HEADER_NAME_COLUMNS_VEQ));//Nomes das Colunas sem QRCode
        }
        return header;
    }

    public GridFooter getGridFooterValues2() {

        GridFooter footer = new GridFooter(this);
        footer.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        footer.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        footer.setFileLayoutRow(References.FOOTER_LAYOUT_VEQ); //arquivo xml - layout (TableRow)
        footer.setIdColumns(References.FOOTER_ID_COLUMNS);// Ids (xml) das colunas
        footer.setIdTable(References.FOOTER_ID_VEQ); //Id do TableLayout);

        return footer;
    }

    @SuppressWarnings("deprecation")
    private void changeLayout() throws AlertException {

        String[] arrayPK = (!(novoRegistro)) ? Util.getArrayPK(intentParameters.getIdRegistroAtual(), getStr(R.string.TOKEN)) : null;

        if (arrayPK != null) {

            idApropriacao = Integer.valueOf(arrayPK[0]);
            idEquipamento = Integer.valueOf(arrayPK[1]);
            horaInicio = arrayPK[2];
            horaViagem = arrayPK[3];
        }

        refreshIntentParameters(intentParameters);

        ArrayAdapter<MaterialVO> adpM = new ArrayAdapter<MaterialVO>(this, android.R.layout.select_dialog_singlechoice, materialArrayVO);
        adpM.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        materialSpn.setAdapter(adpM);

        if (novoRegistro) {

            if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.ORIGEM_DESTINO))) {
                rowTipo.setVisibility(View.VISIBLE);
                rowTipo.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            } else {
                rowTipo.setVisibility(View.GONE);
                rowTipo.setLayoutParams(new LayoutParams(0, 0));
            }

            rowObservacoes.setVisibility(View.GONE);
            rowObservacoes.setLayoutParams(new LayoutParams(0, 0));
            checkApropriar.setVisibility(View.GONE);
            checkApropriar.setLayoutParams(new LayoutParams(0, 0));

            rowEticket.setVisibility(View.GONE);
            rowEticket.setLayoutParams(new LayoutParams(0, 0));

            materialSpn.setSelection(0);
            editEticket.setText(getStr(R.string.EMPTY));

            btNovo.setVisibility(View.GONE);
            btNovo.setLayoutParams(new LayoutParams(0, 0));

            horaView.setVisibility(View.GONE);
            horaView.setLayoutParams(new LayoutParams(0, 0));

            horaViagemEditText.setVisibility(View.VISIBLE);
            horaViagemEditText.setLayoutParams(new LayoutParams(50, 45));

            minutoViagem.setVisibility(View.VISIBLE);
            minutoViagem.setLayoutParams(new LayoutParams(50, 45));

            dots.setVisibility(View.VISIBLE);
            dots.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            //materialView.setVisibility(View.GONE);
            //materialView.setLayoutParams(new LayoutParams(0,0));

            equipamentoCargaView.setVisibility(View.GONE);
            equipamentoCargaView.setLayoutParams(new LayoutParams(0, 0));

            //			materialSpn.setVisibility(View.VISIBLE);
            //			materialSpn.setLayoutParams(new LayoutParams(250,45));

            equipamentoCargaList.setVisibility(View.VISIBLE);
            equipamentoCargaList.setLayoutParams(new LayoutParams(250, 45));

            ArrayAdapter<EquipamentoVO> adpEC = new ArrayAdapter<EquipamentoVO>(this, android.R.layout.select_dialog_singlechoice, equipamentoCargaArrayVO);
            adpEC.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            equipamentoCargaList.setAdapter(adpEC);

            estacaInicio = getStr(R.string.EMPTY);
            estacaFim = getStr(R.string.EMPTY);

            String[] arrayDados = getDAO().getViagemDAO().getUltimaViagem(new Object[]{idFrenteObra, idAtividade, tipoLocalizacao, idOrigem, idDestino});

            if (arrayDados != null) {

                idMaterial = Integer.valueOf(arrayDados[0]);
                estacaInicio = arrayDados[1];
                estacaFim = arrayDados[2];
//				viraVira = (arrayDados[3] != null && arrayDados[3].equalsIgnoreCase(getStr(R.string.Y)));
//                horimetro = arrayDados[3];
//                hodometro = arrayDados[4];

                tipoViagem = (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.ORIGEM_DESTINO))) ? arrayDados[3] : tipoViagem;

                if (tipoViagem.equalsIgnoreCase(getStr(R.string.DESTINO))) {
                    radioDst.setChecked(true);
                } else {
                    radioOrg.setChecked(true);
                    idEquipamentoCarga = arrayDados[4] != null ? Integer.valueOf(arrayDados[4]) : null;
                }

                materialSpn.setSelection(getIndexById(idMaterial, materialArrayVO));
            }

            estacaIni.setText(estacaInicio);
            estacaFimEditText.setText(estacaFim);

//			checkViraVira.setChecked(viraVira);
            horimetroEditText.setText(horimetro);
            hodometroEditText.setText(hodometro);

            prcCarga.setText("100");
            estacaIni.setText(estacaInicio);
            estacaFimEditText.setText(estacaFim);

            String hora = String.valueOf(Calendar.getInstance().getTime().getHours());
            String minutos = String.valueOf(Calendar.getInstance().getTime().getMinutes());

            if (hora.length() == 1) {
                hora = "0" + hora;
            }
            if (minutos.length() == 1) {
                minutos = "0" + minutos;
            }

            horaViagemEditText.setText(hora);
            minutoViagem.setText(minutos);


        } else {

            rowObservacoes.setVisibility(View.VISIBLE);
            rowObservacoes.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            checkApropriar.setVisibility(View.VISIBLE);
            checkApropriar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            rowEticket.setVisibility(View.VISIBLE);
            rowEticket.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            txtEticketView.setVisibility(View.VISIBLE);
            txtEticketView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            editEticket.setVisibility(View.GONE);
            editEticket.setLayoutParams(new LayoutParams(0, 0));

            rowTipo.setVisibility(View.GONE);
            rowTipo.setLayoutParams(new LayoutParams(0, 0));

            String[] dados = getDAO().getViagemDAO().getValues(new String[]{String.valueOf(idApropriacao),
                    String.valueOf(idEquipamento), horaInicio, horaViagem});

            idMaterial = Integer.valueOf(dados[0]);

            equipamentoView.setText(dados[1]);

            horaView.setText(dados[2]);

            //materialView.setText(dados[3]);

            estacaIni.setText(dados[4]);

            estacaFimEditText.setText(dados[5]);

            eTicket = dados[6];

            txtEticketView.setText(eTicket);
            rowEticket.setVisibility(eTicket != null ? View.VISIBLE : View.GONE);

            prcCarga.setText(dados[7]);

            materialSpn.setSelection(getIndexById(idMaterial, materialArrayVO));

            horimetroEditText.setText(dados[8]);

            hodometroEditText.setText(dados[9]);

            apropriar = (dados[10] != null && dados[10].equalsIgnoreCase(getStr(R.string.Y)));

            observacoes.setText(dados[11]);

            pesoEditText.setText(dados[12]);

            tipoViagem = dados[13];

            if (tipoViagem.equalsIgnoreCase(getStr(R.string.ORIGEM))) {
                idEquipamentoCarga = dados[14] != null ? Integer.valueOf(dados[14]) : null;
            }

            nroFormularioEdicao = dados[15] != null ? Integer.parseInt(dados[15]) : null;

            nroQRCodeEdicao = dados[16] != null ? Integer.parseInt(dados[16]) : null;

            codSeguracaEdicao = dados[17] != null ? Integer.parseInt(dados[17]) : null;

            checkApropriar.setChecked(apropriar);

            btNovo.setVisibility(View.VISIBLE);
            btNovo.setLayoutParams(new LayoutParams(120, LayoutParams.WRAP_CONTENT));

            horaViagemEditText.setVisibility(View.GONE);
            horaViagemEditText.setLayoutParams(new LayoutParams(0, 0));

            minutoViagem.setVisibility(View.GONE);
            minutoViagem.setLayoutParams(new LayoutParams(0, 0));

            dots.setVisibility(View.GONE);
            dots.setLayoutParams(new LayoutParams(0, 0));

            horaView.setLayoutParams(new LayoutParams(250, 45));

            //			materialSpn.setVisibility(View.GONE);
            //			materialSpn.setLayoutParams(new LayoutParams(0,0));

            equipamentoCargaList.setVisibility(View.GONE);
            equipamentoCargaList.setLayoutParams(new LayoutParams(0, 0));

            equipamentoCargaView.setText(getDAO().getMaterialDAO().getDescricao(idMaterial));
            equipamentoCargaView.setLayoutParams(new LayoutParams(250, 45));

        }

        //tipo é utilizado para tratar qual propriedade deve ser exibida na tela: horimetro/hodometro
        equipamentoVO = getDAO().getEquipamentoDAO().getById(idEquipamento);

        if (equipamentoVO == null) {
            throw new AlertException(getStr(R.string.ERROR_EQUIPAMENTO_INVALIDO));
        }

        tratarExibicaoHorimetroHodometro(equipamentoVO);

        descEquipamento = getDAO().getEquipamentoDAO().getDescricao(idEquipamento, true, false);
        descEquipamentoCarga = getDAO().getEquipamentoDAO().getDescricao(idEquipamentoCarga, false, true);

        equipamentoView.setText(descEquipamento);

        if (novoRegistro) {
            if (descEquipamentoCarga != null && !descEquipamentoCarga.equals(getStr(R.string.EMPTY))) {
                equipamentoCargaList.setText(descEquipamentoCarga);
            }
        } else
            equipamentoCargaView.setText(descEquipamentoCarga);

        tipoEquipamento = getDAO().getEquipamentoDAO().getTipoMovimentacao(idEquipamento);

        if (tipoEquipamento.equals("1")) {
            estacaFimEditText.setVisibility(View.GONE);
            estacaFimEditText.setLayoutParams(new LayoutParams(0, 0));
        }

        rowPeso.setVisibility(View.GONE);
        rowPeso.setLayoutParams(new LayoutParams(0, 0));

        if (tipoViagem.equalsIgnoreCase(getStr(R.string.ORIGEM)) && idOrigem != null) {

            tipoOrigem = getDAO().getOrigemDestinoDAO().getTipo(idOrigem);

            if (tipoOrigem.equalsIgnoreCase("5")) {
                rowPeso.setVisibility(View.VISIBLE);
                rowPeso.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            }
        } else {
            rowCarga.setVisibility(View.GONE);
            rowCarga.setLayoutParams(new LayoutParams(0, 0));
        }

    }

    private void tratarExibicaoHorimetroHodometro(EquipamentoVO equipamentoVO) {
        String tipo = equipamentoVO.getTipo();

        if (tipo != null && obraVO != null && "S".equalsIgnoreCase(obraVO.getExibirHorimetro())) {
            if (tipo.equalsIgnoreCase("H")) {
                rowHorimetro.setVisibility(View.VISIBLE);
                rowHodometro.setVisibility(View.GONE);
            } else if (tipo.equalsIgnoreCase("K")) {
                rowHorimetro.setVisibility(View.GONE);
                rowHodometro.setVisibility(View.VISIBLE);
            } else {
                rowHorimetro.setVisibility(View.VISIBLE);
                rowHodometro.setVisibility(View.VISIBLE);
            }
        } else {
            rowHorimetro.setVisibility(View.GONE);
            rowHodometro.setVisibility(View.GONE);
        }
    }

    @Override
    public void initEvents() {

        horaViagemEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 2) {
                    minutoViagem.requestFocus();
                    horaViagemEditText.clearFocus();
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


        minutoViagem.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 2) {
                    estacaIni.requestFocus();
                    minutoViagem.clearFocus();
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


        equipamentoCargaList.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (equipamentoCargaList.getKeyListener() != null) {
                    ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                    idEquipamentoCarga = null;
                    descEquipamentoCarga = getStr(R.string.EMPTY);
                }

                return false;
            }
        });


        equipamentoCargaList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EquipamentoVO e = (EquipamentoVO) parent.getItemAtPosition(position);
                idEquipamentoCarga = e.getId();
            }

        });

        equipamentoCargaList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (equipamentoCargaList.getKeyListener() != null) {
                    ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                    idEquipamentoCarga = null;
                    descEquipamentoCarga = getStr(R.string.EMPTY);
                }
            }
        });


        materialSpn.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                MaterialVO m = (MaterialVO) parent.getSelectedItem();
                idMaterial = m.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        estacaIni.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (estacaFimEditText.getText().toString().trim().equals(getStr(R.string.EMPTY))) {
                    estacaFimEditText.setText(((EditText) v).getText());
                }
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
                    boolean usaQRCode = "S".equalsIgnoreCase(obraVO.getUsaQRCode());

                    //Se a obra usar QRCode para equipamento, deverá ser feito a leitura do QRCode do Formulario antes de salvar
                    if (usaQRCode && novoRegistro) {
                        lerQRCodeForm();
                    } else if (usaQRCode && !novoRegistro) {
                        FormMovimentacaoVO f = new FormMovimentacaoVO();

                        f.setTipoModulo(SaveVariables.getInstance().getTipoModulo());

                        if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.ORIGEM))) {
                            f.seteTicketCarga(eTicket);
                            f.setNroFormulario(nroFormularioEdicao);
                            f.setNroQRCode(nroQRCodeEdicao);
                            f.setCodSegurancaCarga(codSeguracaEdicao);
                        } else if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.DESTINO))) {
                            f.seteTicketDescarga(eTicket);
                            f.setNroFormulario(nroFormularioEdicao);
                            f.setNroQRCode(nroQRCodeEdicao);
                            f.setCodSegurancaDescarga(codSeguracaEdicao);
                        } else if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.ORIGEM_DESTINO))) {
                            if (tipoViagem.equals(getStr(R.string.ORIGEM))) {
                                f.seteTicketCarga(eTicket);
                                f.setNroFormulario(nroFormularioEdicao);
                                f.setNroQRCode(nroQRCodeEdicao);
                                f.setCodSegurancaCarga(codSeguracaEdicao);
                            } else if (tipoViagem.equals(getStr(R.string.DESTINO))) {
                                f.seteTicketDescarga(eTicket);
                                f.setNroFormulario(nroFormularioEdicao);
                                f.setNroQRCode(nroQRCodeEdicao);
                                f.setCodSegurancaDescarga(codSeguracaEdicao);
                            }
                        }
                        salvarViagemQRCode(f);
                    } else {
                        salvarViagem();
                    }
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

        radioOrg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean checked = ((RadioButton) v).isChecked();
                if (checked) {

                    rowCarga.setVisibility(View.VISIBLE);
                    rowCarga.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                    tipoViagem = getStr(R.string.ORIGEM);


                    tipoOrigem = getDAO().getOrigemDestinoDAO().getTipo(idOrigem);


                    if (tipoOrigem.equalsIgnoreCase("5")) {
                        rowPeso.setVisibility(View.VISIBLE);
                        rowPeso.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    }
                }

            }
        });

        radioDst.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean checked = ((RadioButton) v).isChecked();
                if (checked) {

                    idEquipamentoCarga = null;
                    descEquipamentoCarga = getStr(R.string.EMPTY);
                    equipamentoCargaList.setText(descEquipamentoCarga);

                    rowCarga.setVisibility(View.GONE);
                    rowCarga.setLayoutParams(new LayoutParams(0, 0));

                    tipoViagem = getStr(R.string.DESTINO);

                    if (rowPeso.getVisibility() == View.VISIBLE) {
                        rowPeso.setVisibility(View.GONE);
                        rowPeso.setLayoutParams(new LayoutParams(0, 0));
                    }
                }

            }
        });

    }

    @Override
    public Detail getDetailValues() {

        //Texto do Detail

        String strDetail = getStr(R.string.DETAIL_VGS);

        Detail detail = new Detail(this);
        detail.setDetail(strDetail);
        detail.setColorBKG(getColor(R.color.GRAY));// Cor de fundo
        detail.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        detail.setFileLayoutRow(References.DETAIL_LAYOUT); //arquivo xml - layout (TableRow)
        detail.setIdColumns(References.DETAIL_ID_COLUMNS);// Ids (xml) das colunas
        detail.setIdTable(References.DETAIL_ID_VGS_EQP); //Id do TableLayout);

        return detail;
    }

    @Override
    public void onBackPressed() {
        redirect(EquipamentosMovimentacaoDiariaActivity.class, true);
    }

    private void tratarEquipamentoNaoEncontrado(Exception e) {
        try {
            Util.viewMessage(currentContext, e.getMessage());
            enableFields(false, true, null, null, null);
            createDetail(getDetailValues());
        } catch (Exception ex) {
            tratarExcecao(ex);
        }
    }

    public void onLocation() {
        redirect(LocalizacaoActivity.class, false);
    }

    private boolean changePkMovimentacao() {

        String[] dados = getDAO().getApropriacaoMovimentacaoDAO().getPk(new Object[]{idEquipamento, tipoLocalizacao, idOrigem, idDestino});

        boolean exists = (dados != null);

        if (exists) {

            idApropriacao = (dados[0] != null) ? Integer.valueOf(dados[0]) : null;
            idEquipamento = (dados[1] != null) ? Integer.valueOf(dados[1]) : null;
            horaInicio = dados[2];

        }

        return exists;
    }

    private void lerQRCodeForm() throws Exception {
        //Para verificar se a obra esta usando ou nao o QRCode para equipamentos e form do motorista com QRCode
        boolean usaQRCode = "S".equalsIgnoreCase(obraVO.getUsaQRCode());
        if (usaQRCode) {
            //Chama a leitura do QRCode usando o CODIGO QR_CODE_FORM_REQUEST
            Intent intent = new Intent(ApropriacaoMovimentacaoActivity.this, ZBarScannerActivity.class);
            startActivityForResult(intent, QR_CODE_FORM_REQUEST);
        }
    }

    private void salvarViagem() throws Exception {

        ApropriacaoVO apropriacao = new ApropriacaoVO();
        ApropriacaoMovimentacaoVO movimentacao = new ApropriacaoMovimentacaoVO();
        ViagemVO vo = new ViagemVO();

        estacaInicio = estacaIni.getText().toString();

        if (tipoEquipamento.equals("1")) {
            estacaFim = estacaInicio;
        } else {
            estacaFim = estacaFimEditText.getText().toString();
        }

        if (novoRegistro) {

            horaViagem = horaViagemEditText.getText().toString().concat(getStr(R.string.TWO_DOTS)).concat(minutoViagem.getText().toString());

            idMaterial = materialArrayVO[materialSpn.getSelectedItemPosition()].getId();

            validateFields();

            if (!changePkMovimentacao()) {

                apropriacao.setAtividade(new AtividadeVO(idAtividade, idFrenteObra));
                apropriacao.setDataHoraApontamento(Util.getNow());
                apropriacao.setTipoApropriacao(getStr(R.string.OPTION_MENU_MOV));
                apropriacao.setObservacoes(getStr(R.string.EMPTY));

                saveOrUpdate(apropriacao);

                horaInicio = horaViagem;

                movimentacao.setIdApropriacao(getDAO().getApropriacaoDAO().getMaxId());
                movimentacao.setEquipamento(new EquipamentoVO(idEquipamento));
                movimentacao.setHoraInicio(horaInicio);

                if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.ORIGEM))) {

                    movimentacao.setEstacaOrigemInicial(estacaInicioView);
                    movimentacao.setEstacaOrigemFinal(estacaFimView);
                    movimentacao.setOrigem(new OrigemDestinoVO(idOrigem));
                    movimentacao.setDestino(new OrigemDestinoVO());

                } else if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.DESTINO))) {

                    movimentacao.setEstacaDestinoInicial(estacaInicioView);
                    movimentacao.setEstacaDestinoFinal(estacaFimView);
                    movimentacao.setDestino(new OrigemDestinoVO(idDestino));
                    movimentacao.setOrigem(new OrigemDestinoVO());

                } else if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.ORIGEM_DESTINO))) {

                    movimentacao.setEstacaDestinoInicial(estacaInicioView);
                    movimentacao.setEstacaDestinoFinal(estacaFimView);
                    movimentacao.setOrigem(new OrigemDestinoVO(idOrigem));
                    movimentacao.setDestino(new OrigemDestinoVO(idDestino));
                }

                movimentacao.setPrcCarga(prcCarga.getText().toString());
                movimentacao.setMaterial(new MaterialVO(idMaterial));

                saveOrUpdate(movimentacao);

                changePkMovimentacao();

                //desabilita edicao
                intentParameters.setFromQRCode(false);
            }

            if (tipoViagem.equalsIgnoreCase(config.getEticket())) {

                eTicketVO = getDAO().getUtilDAO().getETicketVO(new String[]{String.valueOf(idApropriacao),
                        String.valueOf(idEquipamento), horaInicio});
                eTicketVO.setHoraViagem(horaViagem);

                eTicket = Util.gerarETicket(currentContext, eTicketVO, config);
            }

        } else {

            validateFields();

        }

        peso = pesoEditText.getText().toString();

        vo.setIdEquipamento(idEquipamento);
        vo.setIdEquipamentoCarga(idEquipamentoCarga);
        vo.setIdApropriacao(idApropriacao);
        vo.setHoraInicio(horaInicio);
        vo.setHoraViagem(horaViagem);
        vo.setEticket(eTicket);
        vo.setPrcCarga(prcCarga.getText().toString());
        vo.setEstacaIni(estacaInicio);
        vo.setEstacaFim(estacaFim);
        vo.setPeso(peso);
        vo.setMaterial(new MaterialVO(idMaterial));
        vo.setTipo(tipoViagem);


        if (!getDAO().getUtilDAO().validaEstacas(vo)) {
            throw new AlertException(Util.getMessage(currentContext, descLocal, R.string.ERROR_VALIDATE_CUTTING_INI));
        }

        vo.setHorimetro(horimetroEditText.getText().toString());
        vo.setHodometro(hodometroEditText.getText().toString());

        if (novoRegistro) {

            String value = getDAO().getUtilDAO().getExist(vo);//Verfica se existe um registro com a mesma chave e retorna o campo apropriar

            if (value.equals(getStr(R.string.Y))) {//Verifica se o registro existente é válido

                throw new AlertException(getStr(R.string.ERROR_EQUAL_TIME_VGM));

            } else if (value.equals(getStr(R.string.N))) {//Verifica se o registro existente não é válido

                String strId = vo.getIdApropriacao().toString().concat(getStr(R.string.TOKEN))
                        .concat(vo.getIdEquipamento().toString()).concat(getStr(R.string.TOKEN))
                        .concat(vo.getHoraInicio()).concat(getStr(R.string.TOKEN))
                        .concat(vo.getHoraViagem());

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

        //desabilita edicao
        intentParameters.setFromQRCode(false);

        movimentacao.setViagens(new ArrayList<ViagemVO>());
        movimentacao.getViagens().add(vo);
        apropriacao.setMovimentacoes(new ArrayList<ApropriacaoMovimentacaoVO>());
        apropriacao.getMovimentacoes().add(movimentacao);


        if (novoRegistro && (tipoLocalizacao.equalsIgnoreCase(config.getEticket()) || tipoViagem.equalsIgnoreCase(config.getEticket()))) {

            AlertDialog.Builder dialogo = new AlertDialog.Builder(ApropriacaoMovimentacaoActivity.this);

            TextView message = new TextView(getApplicationContext());

            Typeface font = Typeface.createFromAsset(getAssets(), getStr(R.string.FONT_MONACO));

            message.setTypeface(font, Typeface.BOLD);

            message.setText(eTicket);

            message.setTextSize(80);

            message.setPadding(5, 5, 5, 5);

            message.setGravity(Gravity.CENTER);

            message.setTextColor(getColor(R.color.BLACK));

            dialogo.setView(message);

            dialogo.setTitle(getStr(R.string.eticket));

            dialogo.setPositiveButton(getStr(R.string.OK), new

                    DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface di, int arg) {

                            redirect(ApropriacaoMovimentacaoActivity.class, false);
                        }
                    });

            dialogo.show();

        } else {
            redirect(ApropriacaoMovimentacaoActivity.class, false);
        }

    }

    private void salvarViagemQRCode(FormMovimentacaoVO formMovVO) throws Exception {

        // Seta o tipoModulo para saber como deve ser mostrado o Grid de Apropriacoes realizadas
        SaveVariables.getInstance().setTipoModulo(formMovVO.getTipoModulo());

        ApropriacaoVO apropriacao = new ApropriacaoVO();
        ApropriacaoMovimentacaoVO movimentacao = new ApropriacaoMovimentacaoVO();
        ViagemVO vo = new ViagemVO();

        estacaInicio = estacaIni.getText().toString();

        if (tipoEquipamento.equals("1")) {
            estacaFim = estacaInicio;
        } else {
            estacaFim = estacaFimEditText.getText().toString();
        }

        if (novoRegistro) {

            horaViagem = horaViagemEditText.getText().toString().concat(getStr(R.string.TWO_DOTS)).concat(minutoViagem.getText().toString());

            idMaterial = materialArrayVO[materialSpn.getSelectedItemPosition()].getId();

            validateFields();

            if (!changePkMovimentacao()) {

                apropriacao.setAtividade(new AtividadeVO(idAtividade, idFrenteObra));
                apropriacao.setDataHoraApontamento(Util.getNow());
                apropriacao.setTipoApropriacao(getStr(R.string.OPTION_MENU_MOV));
                apropriacao.setObservacoes(getStr(R.string.EMPTY));

                saveOrUpdate(apropriacao);

                horaInicio = horaViagem;

                movimentacao.setIdApropriacao(getDAO().getApropriacaoDAO().getMaxId());
                movimentacao.setEquipamento(new EquipamentoVO(idEquipamento));
                movimentacao.setHoraInicio(horaInicio);

                if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.ORIGEM))) {

                    movimentacao.setEstacaOrigemInicial(estacaInicioView);
                    movimentacao.setEstacaOrigemFinal(estacaFimView);
                    movimentacao.setOrigem(new OrigemDestinoVO(idOrigem));
                    movimentacao.setDestino(new OrigemDestinoVO());

                } else if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.DESTINO))) {

                    movimentacao.setEstacaDestinoInicial(estacaInicioView);
                    movimentacao.setEstacaDestinoFinal(estacaFimView);
                    movimentacao.setDestino(new OrigemDestinoVO(idDestino));
                    movimentacao.setOrigem(new OrigemDestinoVO());

                } else if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.ORIGEM_DESTINO))) {

                    movimentacao.setEstacaDestinoInicial(estacaInicioView);
                    movimentacao.setEstacaDestinoFinal(estacaFimView);
                    movimentacao.setOrigem(new OrigemDestinoVO(idOrigem));
                    movimentacao.setDestino(new OrigemDestinoVO(idDestino));
                }

                movimentacao.setPrcCarga(prcCarga.getText().toString());
                movimentacao.setMaterial(new MaterialVO(idMaterial));

                saveOrUpdate(movimentacao);

                changePkMovimentacao();

                //desabilita edicao
                intentParameters.setFromQRCode(false);
            }

            /*
            if (tipoViagem.equalsIgnoreCase(config.getEticket())) {

                eTicketVO = getDAO().getUtilDAO().getETicketVO(new String[]{String.valueOf(idApropriacao),
                        String.valueOf(idEquipamento), horaInicio});
                eTicketVO.setHoraViagem(horaViagem);

                eTicket = Util.gerarETicket(currentContext, eTicketVO, config);
            }
            */

        } else {
            validateFields();
        }

        peso = pesoEditText.getText().toString();

        vo.setIdEquipamento(idEquipamento);
        vo.setIdEquipamentoCarga(idEquipamentoCarga);
        vo.setIdApropriacao(idApropriacao);
        vo.setHoraInicio(horaInicio);
        vo.setHoraViagem(horaViagem);
        vo.setEticket(eTicket);
        vo.setPrcCarga(prcCarga.getText().toString());
        vo.setEstacaIni(estacaInicio);
        vo.setEstacaFim(estacaFim);
        vo.setPeso(peso);
        vo.setMaterial(new MaterialVO(idMaterial));
        vo.setTipo(tipoViagem);

        //Alteracoes feitas para o Formulario com QRCode
        if(novoRegistro) {
            if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.ORIGEM_DESTINO))) {

                if (tipoViagem.equals(getStr(R.string.ORIGEM))) {
                    eTicket = config.getDispositivo() + getStr(R.string.TRACE) + formMovVO.geteTicketCarga();
                    vo.setEticket(config.getDispositivo() + getStr(R.string.TRACE) + formMovVO.geteTicketCarga());

                    if(formMovVO.getTipoModulo().equals(TipoModulo.FICHA_MOTORISTA)) {
                        codSeguracaEdicao = formMovVO.getCodSegurancaCarga();
                        vo.setCodSeguranca(formMovVO.getCodSegurancaCarga());
                        vo.setNroFicha("");
                    } else if(formMovVO.getTipoModulo().equals(TipoModulo.FICHA_3_VIAS)) {
                        codSeguracaEdicao = 0;
                        vo.setCodSeguranca(0);
                    }
                } else if (tipoViagem.equals(getStr(R.string.DESTINO))) {
                    eTicket = config.getDispositivo() + getStr(R.string.TRACE) + formMovVO.geteTicketDescarga();
                    vo.setEticket(config.getDispositivo() + getStr(R.string.TRACE) + formMovVO.geteTicketDescarga());

                    if(formMovVO.getTipoModulo().equals(TipoModulo.FICHA_MOTORISTA)) {
                        codSeguracaEdicao = formMovVO.getCodSegurancaDescarga();
                        vo.setCodSeguranca(formMovVO.getCodSegurancaDescarga());
                        vo.setNroFicha("");
                    } else if(formMovVO.getTipoModulo().equals(TipoModulo.FICHA_3_VIAS)) {
                        codSeguracaEdicao = 0;
                        vo.setCodSeguranca(0);
                    }
                }

            } else if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.ORIGEM))) {
                eTicket = config.getDispositivo() + getStr(R.string.TRACE) + formMovVO.geteTicketCarga();
                vo.setEticket(config.getDispositivo() + getStr(R.string.TRACE) + formMovVO.geteTicketCarga());

                if(formMovVO.getTipoModulo().equals(TipoModulo.FICHA_MOTORISTA)) {
                    codSeguracaEdicao = formMovVO.getCodSegurancaCarga();
                    vo.setCodSeguranca(formMovVO.getCodSegurancaCarga());
                    vo.setNroFicha("");
                } else if(formMovVO.getTipoModulo().equals(TipoModulo.FICHA_3_VIAS)) {
                    codSeguracaEdicao = 0;
                    vo.setCodSeguranca(0);
                }
            } else if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.DESTINO))) {
                eTicket = config.getDispositivo() + getStr(R.string.TRACE) + formMovVO.geteTicketDescarga();
                vo.setEticket(config.getDispositivo() + getStr(R.string.TRACE) + formMovVO.geteTicketDescarga());

                if(formMovVO.getTipoModulo().equals(TipoModulo.FICHA_MOTORISTA)) {
                    codSeguracaEdicao = formMovVO.getCodSegurancaDescarga();
                    vo.setCodSeguranca(formMovVO.getCodSegurancaDescarga());
                    vo.setNroFicha("");
                } else if(formMovVO.getTipoModulo().equals(TipoModulo.FICHA_3_VIAS)) {
                    codSeguracaEdicao = 0;
                    vo.setCodSeguranca(0);
                }
            }
        } else {
            if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.ORIGEM_DESTINO))) {

                if (tipoViagem.equals(getStr(R.string.ORIGEM))) {
                    eTicket = formMovVO.geteTicketCarga();
                    vo.setEticket(formMovVO.geteTicketCarga());

                    if(formMovVO.getTipoModulo().equals(TipoModulo.FICHA_MOTORISTA)) {
                        codSeguracaEdicao = formMovVO.getCodSegurancaCarga();
                        vo.setCodSeguranca(formMovVO.getCodSegurancaCarga());
                        vo.setNroFicha("");
                    } else if(formMovVO.getTipoModulo().equals(TipoModulo.FICHA_3_VIAS)) {
                        codSeguracaEdicao = 0;
                        vo.setCodSeguranca(0);
                    }
                } else if (tipoViagem.equals(getStr(R.string.DESTINO))) {
                    eTicket = formMovVO.geteTicketDescarga();
                    vo.setEticket(formMovVO.geteTicketDescarga());

                    if(formMovVO.getTipoModulo().equals(TipoModulo.FICHA_MOTORISTA)) {
                        codSeguracaEdicao = formMovVO.getCodSegurancaDescarga();
                        vo.setCodSeguranca(formMovVO.getCodSegurancaDescarga());
                        vo.setNroFicha("");
                    } else if(formMovVO.getTipoModulo().equals(TipoModulo.FICHA_3_VIAS)) {
                        codSeguracaEdicao = 0;
                        vo.setCodSeguranca(0);
                    }
                }

            } else if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.ORIGEM))) {
                eTicket = formMovVO.geteTicketCarga();
                vo.setEticket(formMovVO.geteTicketCarga());

                //Log.i("TIPO_MODULO",formMovVO.getTipoModulo()+"");

                if(formMovVO.getTipoModulo().equals(TipoModulo.FICHA_MOTORISTA)) {
                    codSeguracaEdicao = formMovVO.getCodSegurancaCarga();
                    vo.setCodSeguranca(formMovVO.getCodSegurancaCarga());
                    vo.setNroFicha("");
                } else if(formMovVO.getTipoModulo().equals(TipoModulo.FICHA_3_VIAS)) {
                    codSeguracaEdicao = 0;
                    vo.setCodSeguranca(0);
                }
            } else if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.DESTINO))) {
                eTicket = formMovVO.geteTicketDescarga();
                vo.setEticket(formMovVO.geteTicketDescarga());

                if(formMovVO.getTipoModulo().equals(TipoModulo.FICHA_MOTORISTA)) {
                    codSeguracaEdicao = formMovVO.getCodSegurancaDescarga();
                    vo.setCodSeguranca(formMovVO.getCodSegurancaDescarga());
                    vo.setNroFicha("");
                } else if(formMovVO.getTipoModulo().equals(TipoModulo.FICHA_3_VIAS)) {
                    codSeguracaEdicao = 0;
                    vo.setCodSeguranca(0);
                }
            }
        }

        if(formMovVO.getTipoModulo().equals(TipoModulo.FICHA_MOTORISTA)) {
            vo.setNroQRCode(formMovVO.getNroQRCode());
            vo.setNroFormulario(formMovVO.getNroFormulario());
            vo.setNroFicha("");
        } else if(formMovVO.getTipoModulo().equals(TipoModulo.FICHA_3_VIAS)) {
            vo.setNroQRCode(0);
            vo.setNroFormulario(0);
            vo.setNroFicha(formMovVO.getNroFicha());
        }

        if (!getDAO().getUtilDAO().validaEstacas(vo)) {
            throw new AlertException(Util.getMessage(currentContext, descLocal, R.string.ERROR_VALIDATE_CUTTING_INI));
        }

        vo.setHorimetro(horimetroEditText.getText().toString());
        vo.setHodometro(hodometroEditText.getText().toString());

        if (novoRegistro) {

            String value = getDAO().getUtilDAO().getExist(vo);//Verfica se existe um registro com a mesma chave e retorna o campo apropriar

            if (value.equals(getStr(R.string.Y))) {//Verifica se o registro existente é válido
                throw new AlertException(getStr(R.string.ERROR_EQUAL_TIME_VGM));
            } else if (value.equals(getStr(R.string.N))) {//Verifica se o registro existente não é válido
                String strId = vo.getIdApropriacao().toString().concat(getStr(R.string.TOKEN))
                        .concat(vo.getIdEquipamento().toString()).concat(getStr(R.string.TOKEN))
                        .concat(vo.getHoraInicio()).concat(getStr(R.string.TOKEN))
                        .concat(vo.getHoraViagem());
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

        //desabilita edicao
        intentParameters.setFromQRCode(false);

        movimentacao.setViagens(new ArrayList<ViagemVO>());
        movimentacao.getViagens().add(vo);
        apropriacao.setMovimentacoes(new ArrayList<ApropriacaoMovimentacaoVO>());
        apropriacao.getMovimentacoes().add(movimentacao);

        TipoModulo tipoModuloQrCode =
                SaveVariables.getInstance().getTipoModulo();
        if (novoRegistro && tipoModuloQrCode.equals(TipoModulo.FICHA_MOTORISTA)) {

            AlertDialog.Builder dialogo = new AlertDialog.Builder(ApropriacaoMovimentacaoActivity.this);

            TextView message = new TextView(getApplicationContext());

            Typeface font = Typeface.createFromAsset(getAssets(), getStr(R.string.FONT_MONACO));

            message.setTypeface(font, Typeface.BOLD);

            message.setText(String.valueOf(vo.getCodSeguranca()));

            message.setTextSize(80);

            message.setPadding(5, 5, 5, 5);

            message.setGravity(Gravity.CENTER);

            message.setTextColor(getColor(R.color.BLACK));

            dialogo.setView(message);

            if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.ORIGEM_DESTINO))) {
                if (tipoViagem.equals(getStr(R.string.ORIGEM))) {
                    dialogo.setTitle("Código de Segurança de Carga");
                } else if (tipoViagem.equals(getStr(R.string.DESTINO))) {
                    dialogo.setTitle("Código de Segurança de Descarga");
                }
            } else  if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.ORIGEM))) {
                dialogo.setTitle("Código de Segurança de Carga");
            } else if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.DESTINO))) {
                dialogo.setTitle("Código de Segurança de Descarga");
            }

            dialogo.setPositiveButton(getStr(R.string.OK), new
                    DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface di, int arg) {
                            redirect(ApropriacaoMovimentacaoActivity.class, false);
                        }
                    });

            dialogo.show();

        } else {
            redirect(ApropriacaoMovimentacaoActivity.class, false);
        }

    }

    public Cursor getCursor2() {
        TipoModulo tipoModuloQrCode =
                SaveVariables.getInstance().getTipoModulo();
        if("S".equalsIgnoreCase(obraVO.getUsaQRCode()) && tipoModuloQrCode.equals(TipoModulo.FICHA_MOTORISTA)) {
            return getDAO().getViagemDAO().getCursorViagensDia2(new Object[]{idEquipamento, idFrenteObra, idAtividade, tipoLocalizacao, idOrigem, idDestino});
        } else {
            return getDAO().getViagemDAO().getCursorViagensDia(new Object[]{idEquipamento, idFrenteObra, idAtividade, tipoLocalizacao, idOrigem, idDestino});
        }
    }

    @Override
    public void editScreen() throws Exception {
        changeLayout();
    }

    @Override
    protected void trataDadosQRCodeForm(FormMovimentacaoVO formMovVO) throws AlertException {
        try {
            if (formMovVO != null && formMovVO.getTipoModulo().equals(TipoModulo.FICHA_MOTORISTA)) {
                // verifica se o QRCode ja foi usado
                boolean qrCodeUsado = getDAO().getViagemDAO().isQRCodeUsado(formMovVO, tipoLocalizacao, config.getDispositivo(), tipoViagem, false, TipoModulo.FICHA_MOTORISTA);
                //Faz a chamada de salvar a viagem se tudo ocorreu bem
                if(qrCodeUsado) {
                    throw new AlertException("QRCode já lido e utilizado pelo sistema!");
                } else {
                    salvarViagemQRCode(formMovVO);
                }
            } else if (formMovVO != null && formMovVO.getTipoModulo().equals(TipoModulo.FICHA_3_VIAS)) {
                // verifica se o QRCode ja foi usado
                boolean qrCodeUsado = getDAO().getViagemDAO().isQRCodeUsado(formMovVO, tipoLocalizacao, config.getDispositivo(), tipoViagem, false, TipoModulo.FICHA_3_VIAS);
                //Faz a chamada de salvar a viagem se tudo ocorreu bem
                if(qrCodeUsado) {
                    throw new AlertException("QRCode já lido e utilizado pelo sistema!");
                } else {
                    salvarViagemQRCode(formMovVO);
                }
            }else{
                throw new AlertException("QRCode inválido!");
            }
        } catch (Exception e) {
            tratarExcecao(e);
        }
    }

    @Override
    protected void trataDadosQRCode() throws AlertException {

        EquipamentoMovimentacaoDiariaVO vo = new EquipamentoMovimentacaoDiariaVO();

        int codigoQrCode = idEquipamento;
        EquipamentoVO equip = getDAO().getEquipamentoDAO().getByQRCode(codigoQrCode);

        if (equip != null) {
            idEquipamento = equip.getId();
            intentParameters.setIdRegistroPai(String.valueOf(idEquipamento));
            intentParameters.setIdEquipamento(idEquipamento);
            intentParameters.setFromQRCode(true);

            vo.setIdEquipamento(idEquipamento);
            vo.setDataHora(Util.getNow());

            intentParameters.setIdRegistroAtual(null);
            refreshIntentParameters(intentParameters);

            saveOrUpdate(vo);
            redirect(ApropriacaoMovimentacaoActivity.class, false);
        } else {
            throw new AlertException(getStr(R.string.ERROR_QRCODE_EQUIPAMENTO_INVALIDO));
        }

    }

}
