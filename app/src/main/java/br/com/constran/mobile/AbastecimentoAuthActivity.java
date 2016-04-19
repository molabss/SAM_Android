package br.com.constran.mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;
import br.com.constran.mobile.exception.AlertException;
import br.com.constran.mobile.model.LogAuditoria;
import br.com.constran.mobile.persistence.dao.LogAuditoriaDAO;
import br.com.constran.mobile.persistence.vo.ObraVO;
import br.com.constran.mobile.persistence.vo.imp.AtividadeVO;
import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;
import br.com.constran.mobile.persistence.vo.imp.FrenteObraVO;
import br.com.constran.mobile.persistence.vo.imp.UsuarioVO;
import br.com.constran.mobile.persistence.vo.imp.json.AbastecimentoJson;
import br.com.constran.mobile.persistence.vo.rae.RaeVO;
import br.com.constran.mobile.persistence.vo.rae.abs.AbastecimentoTempVO;
import br.com.constran.mobile.persistence.vo.rae.abs.JustificativaOperadorVO;
import br.com.constran.mobile.persistence.vo.rae.abs.PostoVO;
import br.com.constran.mobile.qrcode.ZBarScannerActivity;
import br.com.constran.mobile.view.AbstractActivity;
import br.com.constran.mobile.view.interfaces.InterfaceEditActivity;
import br.com.constran.mobile.view.screens.Detail;
import br.com.constran.mobile.view.util.References;
import br.com.constran.mobile.view.util.Util;
//import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.google.gson.Gson;

import org.apache.commons.io.output.FileWriterWithEncoding;

import java.io.File;
import java.util.Date;

public final class AbastecimentoAuthActivity extends AbstractActivity implements InterfaceEditActivity {


    protected final int QR_CODE_REQUEST_ABS = 998;
    protected final int QR_CODE_REQUEST_OPER = 995;

    private AutoCompleteTextView prefixoList;
    private AutoCompleteTextView abastecedorList;
    private AutoCompleteTextView operadorList;
    private AutoCompleteTextView justificativaList;

    private Spinner obraSpn;
    private Spinner frenteObraSpin;
    private Spinner atividadeSpin;

    private TableRow obraRow, horimetroRow, quilometragemRow, justificativaRow, obsJustificativaRow;

    private EditText horimetro;
    private EditText quilometragem;
    private EditText observacoesJustificativa;
    private EditText prefEquipQRCode;
    private EditText prefAbsQRCode;
    private EditText prefOperQrCode;

    private TextView raeView;
    private TextView postoView;
    private TextView inicioView;

    private boolean executeEvent;

    private boolean horimetroExibe;
    private boolean quilometragemExibe;
    private boolean horimetroEmpty;
    private boolean quilometragemEmpty;

    private boolean justificativaExibe;

    private Button btAddEquipQRCode;
    private Button btAddAbsQRCode;
    private Button btAddOperQRCode;

    private ObraVO obraVO;

    private LinearLayout conteudoLinearLayout;
    private LinearLayout conteudoLinearLayout2;
    private LinearLayout conteudoLinearLayout3;
    private LinearLayout conteudoLinearLayout4;
    private LinearLayout conteudoLinearLayout5;
    private LinearLayout conteudoLinearLayout6;
    private LinearLayout conteudoLinearLayout7;
    private LinearLayout conteudoLinearLayout8;
    private LinearLayout conteudoLinearLayout9;
    private LinearLayout conteudoLinearLayout10;
    private LinearLayout conteudoLinearLayout11;
    private LinearLayout conteudoLinearLayout12;
    private LinearLayout conteudoLinearLayout13;

    private LinearLayout headerLinearLayout;
    private LinearLayout headerLinearLayout2;
    private LinearLayout headerLinearLayout3;
    private LinearLayout headerLinearLayout4;
    private LinearLayout headerLinearLayout5;
    private LinearLayout headerLinearLayout6;
    private LinearLayout headerLinearLayout7;
    private LinearLayout headerLinearLayout8;
    private LinearLayout headerLinearLayout9;
    private LinearLayout headerLinearLayout10;
    private LinearLayout headerLinearLayout11;
    private LinearLayout headerLinearLayout12;
    private LinearLayout headerLinearLayout13;


    LogAuditoriaDAO logDAO;
    LogAuditoria log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.abastecimento_auth);

        currentContext = AbastecimentoAuthActivity.this;
        currentClass = AbastecimentoAuthActivity.class;

        try {

            init();

            initAtributes();

            editValues();

            initEvents();

            editScreen();

            createDetail(getDetailValues());

        } catch (Exception e) {
            tratarExcecao(e);
        }
    }


    @Override
    public void initAtributes() throws Exception {

        raeView = (TextView) findViewById(R.id.absAuthRae);
        postoView = (TextView) findViewById(R.id.absAuthPosto);
        inicioView = (TextView) findViewById(R.id.absAuthHrIn);

        atividadeSpin = (Spinner) findViewById(R.id.LclCmbAtv);
        frenteObraSpin = (Spinner) findViewById(R.id.LclCmbFob);

        prefixoList = (AutoCompleteTextView) findViewById(R.id.absAuthPref);

        prefEquipQRCode = (EditText) findViewById(R.id.editPrefQrCode);
        prefAbsQRCode = (EditText) findViewById(R.id.editAbsPrefQrCode);
        prefOperQrCode = (EditText) findViewById(R.id.editOperPrefQrCode);

        abastecedorList = (AutoCompleteTextView) findViewById(R.id.absAuthAbast);
        operadorList = (AutoCompleteTextView) findViewById(R.id.absAuthOper);
        justificativaList = (AutoCompleteTextView) findViewById(R.id.absAuthJust);
        horimetro = (EditText) findViewById(R.id.absAuthHorim);
        quilometragem = (EditText) findViewById(R.id.absAuthQuilm);
        observacoesJustificativa = (EditText) findViewById(R.id.absObsJus);
        obraRow = (TableRow) findViewById(R.id.tbRwAbsCcobra);
        justificativaRow = (TableRow) findViewById(R.id.tbRwabsJust);
        obsJustificativaRow = (TableRow) findViewById(R.id.tbRwabsObsJus);
        quilometragemRow = (TableRow) findViewById(R.id.tbRwabsKm);
        horimetroRow = (TableRow) findViewById(R.id.tbRwabshorim);
        obraSpn = (Spinner) findViewById(R.id.SpnAbsCcobra);

        btAddEquipQRCode = (Button) findViewById(R.id.btAddEqpQRCode);
        btAddAbsQRCode = (Button) findViewById(R.id.btAddAbsQRCode);
        btAddOperQRCode = (Button) findViewById(R.id.btAddOperQRCode);

        btSalvar = (Button) findViewById(R.id.btAbsAuthSave);
        btCancelar = (Button) findViewById(R.id.btAbsAuthCancel);

        conteudoLinearLayout = (LinearLayout) findViewById(R.id.contentLinearLayout);
        conteudoLinearLayout2 = (LinearLayout) findViewById(R.id.contentLinearLayout2);
        conteudoLinearLayout3 = (LinearLayout) findViewById(R.id.contentLinearLayout3);
        conteudoLinearLayout4 = (LinearLayout) findViewById(R.id.contentLinearLayout4);
        conteudoLinearLayout5 = (LinearLayout) findViewById(R.id.contentLinearLayout5);
        conteudoLinearLayout6 = (LinearLayout) findViewById(R.id.contentLinearLayout6);
        conteudoLinearLayout7 = (LinearLayout) findViewById(R.id.contentLinearLayout7);
        conteudoLinearLayout8 = (LinearLayout) findViewById(R.id.contentLinearLayout8);
        conteudoLinearLayout9 = (LinearLayout) findViewById(R.id.contentLinearLayout9);
        conteudoLinearLayout10 = (LinearLayout) findViewById(R.id.contentLinearLayout10);
        conteudoLinearLayout11 = (LinearLayout) findViewById(R.id.contentLinearLayout11);
        conteudoLinearLayout12 = (LinearLayout) findViewById(R.id.contentLinearLayout12);
        conteudoLinearLayout13 = (LinearLayout) findViewById(R.id.contentLinearLayout13);

        headerLinearLayout = (LinearLayout) findViewById(R.id.headerLinearLayout);
        headerLinearLayout2 = (LinearLayout) findViewById(R.id.headerLinearLayout2);
        headerLinearLayout3 = (LinearLayout) findViewById(R.id.headerLinearLayout3);
        headerLinearLayout4 = (LinearLayout) findViewById(R.id.headerLinearLayout4);
        headerLinearLayout5 = (LinearLayout) findViewById(R.id.headerLinearLayout5);
        headerLinearLayout6 = (LinearLayout) findViewById(R.id.headerLinearLayout6);
        headerLinearLayout7 = (LinearLayout) findViewById(R.id.headerLinearLayout7);
        headerLinearLayout8 = (LinearLayout) findViewById(R.id.headerLinearLayout8);
        headerLinearLayout9 = (LinearLayout) findViewById(R.id.headerLinearLayout9);
        headerLinearLayout10 = (LinearLayout) findViewById(R.id.headerLinearLayout10);
        headerLinearLayout11 = (LinearLayout) findViewById(R.id.headerLinearLayout11);
        headerLinearLayout12 = (LinearLayout) findViewById(R.id.headerLinearLayout12);
        headerLinearLayout13 = (LinearLayout) findViewById(R.id.headerLinearLayout13);

        fixBackgroundColor();
    }

    @Override
    public void editValues() throws Exception {

        config = getDAO().getConfiguracoesDAO().getConfiguracaoVO();
        obraVO = getDAO().getObraDAO().getById(config.getIdObra());

        logDAO = getDAO().getLogAuditoriaDAO();
        log = new LogAuditoria("ABASTECIMENTO",config.getDispositivo());
        logDAO.setLogPropriedades(log);


        ccObra = config.getIdObra();
        ccObra2 = config.getIdObra2();

        posto = new PostoVO(config.getIdPosto(), config.getNomePosto());

        postoView.setText(posto.getDescricao());

        abastecedor = userRequest;

        dataHora = Util.getDateFormated(new Date());

        raeView.setText(dataHora);

        horaInicio = Util.getHourFormated(new Date());

        inicioView.setText(horaInicio);

        equipamentoArrayVO = getDAO().getEquipamentoDAO().getArrayEquipamentoVO();
        abastecedorArrayVO = getDAO().getUsuarioDAO().getArrayUsuarioAbastecedor();
        operadorArrayVO = getDAO().getUsuarioDAO().getArrayUsuarioOperador();
        justificativaOperadorArrayVO = getDAO().getJustificativaOperadorDAO().getArrayJustificativaOperadorVO();

        frenteObra = new FrenteObraVO();

        atividade = new AtividadeVO();

        exibirBotaoAdd();
    }

    private void exibirBotaoAdd() {
        //desabilita edicao
        prefEquipQRCode.setKeyListener(null);
        prefAbsQRCode.setKeyListener(null);


        if ("S".equalsIgnoreCase(obraVO.getUsaQRCode())) {
            btAddEquipQRCode.setVisibility(View.VISIBLE);
            prefixoList.setVisibility(View.GONE);
            prefEquipQRCode.setVisibility(View.VISIBLE);
        } else {
            prefixoList.setVisibility(View.VISIBLE);
            btAddEquipQRCode.setVisibility(View.GONE);
        }

        if ("S".equalsIgnoreCase(obraVO.getUsaQRCodePessoal())) {
            btAddAbsQRCode.setVisibility(View.VISIBLE);
            btAddOperQRCode.setVisibility(View.VISIBLE);
            abastecedorList.setVisibility(View.GONE);
            operadorList.setVisibility(View.GONE);
            prefAbsQRCode.setVisibility(View.VISIBLE);
            prefOperQrCode.setVisibility(View.VISIBLE);
        } else {
            abastecedorList.setVisibility(View.VISIBLE);
            operadorList.setVisibility(View.VISIBLE);
            btAddAbsQRCode.setVisibility(View.GONE);
            btAddOperQRCode.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (userRequest.getId() != null)
            redirect(AbastecimentosSearchActivity.class, true);
        else
            redirectPrincipal();
    }


    @Override

    public Detail getDetailValues() throws Exception {

        //Texto do Detail
        String strDetail = getStr(R.string.DETAIL_ABS);

        Detail detail = new Detail(this);
        detail.setDetail(strDetail);
        detail.setColorBKG(getColor(R.color.GRAY));// Cor de fundo
        detail.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        detail.setFileLayoutRow(References.DETAIL_LAYOUT); //arquivo xml - layout (TableRow)
        detail.setIdColumns(References.DETAIL_ID_COLUMNS);// Ids (xml) das colunas
        detail.setIdTable(References.DETAIL_ID_ABS); //Id do TableLayout);

        return detail;
    }


    @Override
    public void initEvents() throws Exception {


        obraSpn.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {

                ObraVO a = (ObraVO) parent.getSelectedItem();

                ccObra2 = ccObra;
                ccObra = a.getId();

                frenteObraArrayVO = getDAO().getFrenteObraDAO().getArrayFrenteObraVO(ccObra);

                ArrayAdapter<FrenteObraVO> adpF = new ArrayAdapter<FrenteObraVO>(currentContext, android.R.layout.select_dialog_singlechoice, frenteObraArrayVO);
                adpF.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                frenteObraSpin.setAdapter(adpF);


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        frenteObraSpin.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {

                frenteObra = (FrenteObraVO) parent.getSelectedItem();

                if (frenteObra.getId() == null) {
			    	frenteObra.setDescricao(getStr(R.string.EMPTY));
                }

                atividadeArrayVO = getDAO().getAtividadeDAO().getArrayAtividadeVO(frenteObra.getId());

                ArrayAdapter<AtividadeVO> adpA = new ArrayAdapter<AtividadeVO>(currentContext, android.R.layout.select_dialog_singlechoice, atividadeArrayVO);
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
                atividade = (AtividadeVO) parent.getSelectedItem();

                if (atividade.getIdAtividade() == null) {
					atividade.setDescricao(getStr(R.string.EMPTY));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        abastecedorList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UsuarioVO user = (UsuarioVO) parent.getItemAtPosition(position);
                alertConfirm(user, "A");
            }
        });

        abastecedorList.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                alertLogout();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        abastecedorList.removeTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                alertLogout();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        /////////////////////////////////////////////////////////////////////////////////

        operadorList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UsuarioVO user = (UsuarioVO) parent.getItemAtPosition(position);
                alertConfirm(user, "O");
            }
        });


        operadorList.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                operador = null;
                changeLayout(false, true);
                return false;
            }
        });


        operadorList.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                operador = null;
                changeLayout(false, true);
            }
        });

        justificativaList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                justificativaOperador = (JustificativaOperadorVO) parent.getItemAtPosition(position);
                operadorList.setEnabled(false);
            }
        });


        justificativaList.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                justificativaOperador = null;
                operadorList.setEnabled(true);
                return false;
            }
        });

        justificativaList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                justificativaOperador = null;
                operadorList.setEnabled(true);
            }
        });

        justificativaList.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focusOn) {
                if (!focusOn && !containsJustificativa(justificativaList.getText().toString())) {
                    justificativaOperador = null;
                    justificativaList.setText("");
                    operadorList.setEnabled(true);
                }
            }
        });

        prefixoList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                equipamento = (EquipamentoVO) parent.getItemAtPosition(position);
                changeValues(equipamento.getId());
                changeLayout(true, true);
            }

        });

        prefixoList.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                equipamento = null;
                changeLayout(true, true);
                return false;
            }
        });


        prefixoList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                equipamento = null;
                changeLayout(true, false);
            }
        });

        btAddEquipQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {

                    logDAO.insereLog("lendo qrcode do equipamento");

                    Intent intent = new Intent(AbastecimentoAuthActivity.this, ZBarScannerActivity.class);
                    startActivityForResult(intent, QR_CODE_REQUEST);
                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }
        });

        btAddAbsQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {

                    logDAO.insereLog("lendo qrcode do abastecedor");

                    alertLogoutQRCodeAbs();
                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }
        });

        btAddOperQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    logDAO.insereLog("lendo qrcode do operador");
                    readQRCodeOperador();
                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }
        });

        btSalvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {



                try {
                    validateFields();
                    validateSave();

                    logDAO.insereLog("abastecimento salvo");

                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }
        });


        btCancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                logDAO.insereLog("abastecimento cancelado");
                onBackPressed();
            }
        });

    }

    @Override
    public void editScreen() throws Exception {

        hiddenKeyboard(prefixoList);
        hiddenKeyboard(abastecedorList);

        frenteObraArrayVO = getDAO().getFrenteObraDAO().getArrayFrenteObraVO(ccObra);
        atividadeArrayVO = getDAO().getAtividadeDAO().getArrayAtividadeVO(idFrenteObra);

        ArrayAdapter<FrenteObraVO> adpF = new ArrayAdapter<FrenteObraVO>(this, android.R.layout.select_dialog_singlechoice, frenteObraArrayVO);
        adpF.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        frenteObraSpin.setAdapter(adpF);

        ArrayAdapter<AtividadeVO> adpA = new ArrayAdapter<AtividadeVO>(this, android.R.layout.select_dialog_singlechoice, atividadeArrayVO);
        adpA.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        atividadeSpin.setAdapter(adpA);

        ArrayAdapter<UsuarioVO> abs = new ArrayAdapter<UsuarioVO>(this, android.R.layout.select_dialog_singlechoice, abastecedorArrayVO);
        abs.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        abastecedorList.setAdapter(abs);

        executeEvent = false;
        abastecedorList.setText(abastecedor.getNome());
        prefAbsQRCode.setText(abastecedor.getNome());
        executeEvent = true;

        ArrayAdapter<UsuarioVO> oper = new ArrayAdapter<UsuarioVO>(this, android.R.layout.select_dialog_singlechoice, operadorArrayVO);
        oper.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        operadorList.setAdapter(oper);

        ArrayAdapter<JustificativaOperadorVO> jus = new ArrayAdapter<JustificativaOperadorVO>(this, android.R.layout.select_dialog_singlechoice, justificativaOperadorArrayVO);
        jus.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        justificativaList.setAdapter(jus);

        ArrayAdapter<EquipamentoVO> eqp = new ArrayAdapter<EquipamentoVO>(this, android.R.layout.select_dialog_singlechoice, equipamentoArrayVO);
        eqp.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        prefixoList.setAdapter(eqp);

        if (ccObra2 != null && validaCentroCusto(ccObra2)) {

            obraArrayVO = getDAO().getObraDAO().getArrayObraVO(new Integer[]{ccObra, ccObra2});

            ArrayAdapter<ObraVO> obra = new ArrayAdapter<ObraVO>(this, android.R.layout.select_dialog_singlechoice, obraArrayVO);
            obra.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            obraSpn.setAdapter(obra);
            obraSpn.setSelection(getIndexById(ccObra, obraArrayVO));

        } else {

            obraRow.setVisibility(View.GONE);
            obraRow.setLayoutParams(new LayoutParams(0, 0));
        }

        changeLayout(true, true);

    }


    private void changeLayout(boolean dadosEquipamento, boolean dadosOperador) {

        if (dadosEquipamento) {

            if (equipamento == null || equipamento.getTipo() == null || equipamento.getTipo().trim().equals(getStr(R.string.EMPTY))) {

                horimetroRow.setVisibility(View.VISIBLE);
                horimetroRow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                horimetroExibe = true;

                quilometragemRow.setVisibility(View.VISIBLE);
                quilometragemRow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                quilometragemExibe = true;

            } else if (equipamento.getTipo().trim().equalsIgnoreCase("H")) {

                horimetroRow.setVisibility(View.VISIBLE);
                horimetroRow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                horimetroExibe = true;

                quilometragemRow.setVisibility(View.GONE);
                quilometragemRow.setLayoutParams(new LayoutParams(0, 0));
                quilometragem.setText(getStr(R.string.EMPTY));
                quilometragemExibe = false;


            } else if (equipamento.getTipo().trim().equalsIgnoreCase("K")) {

                horimetroRow.setVisibility(View.GONE);
                horimetroRow.setLayoutParams(new LayoutParams(0, 0));
                horimetro.setText(getStr(R.string.EMPTY));
                horimetroExibe = false;

                quilometragemRow.setVisibility(View.VISIBLE);
                quilometragemRow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                quilometragemExibe = true;

            }
        }

        if (dadosOperador) {

            if (operador == null
                    && equipamento != null
                    && equipamento.getExigeJustificativa() != null
                    && equipamento.getExigeJustificativa().trim().equalsIgnoreCase("S")) {

                justificativaRow.setVisibility(View.VISIBLE);
                justificativaRow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                obsJustificativaRow.setVisibility(View.VISIBLE);
                obsJustificativaRow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                justificativaExibe = true;

            } else {

                justificativaRow.setVisibility(View.GONE);
                justificativaRow.setLayoutParams(new LayoutParams(0, 0));

                obsJustificativaRow.setVisibility(View.GONE);
                obsJustificativaRow.setLayoutParams(new LayoutParams(0, 0));

                justificativaList.setText(getStr(R.string.EMPTY));

                justificativaExibe = false;
            }
        }
    }

    private void changeValues(Integer idEquipamento) {

        AbastecimentoTempVO dados = getDAO().getAbastecimentoTempDAO().getPreviaDados(config.getDuracao(), idEquipamento);

        operador = dados.getOperador();

        horimetro.setText(dados.getHorimetro());
        quilometragem.setText(dados.getQuilometragem());


        if (operador != null) {
            operadorList.setText(operador.getNome());
            alertConfirm(operador, "O");
        } else
            operadorList.setText(getStr(R.string.EMPTY));


    }

    private boolean containsJustificativa(String text) {

        if (justificativaOperadorArrayVO != null) {
            for (JustificativaOperadorVO justificativa : justificativaOperadorArrayVO) {
                if (justificativa.getDescricao().equals(text)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void validateFields() throws Exception {

        horimetroEmpty = horimetro.getText() == null || horimetro.getText().toString().equals(getStr(R.string.EMPTY));
        quilometragemEmpty = quilometragem.getText() == null || quilometragem.getText().toString().equals(getStr(R.string.EMPTY));

        if (abastecedor == null || abastecedor.getId() == null)
            throw new AlertException(Util.getMessage(currentContext, getStr(R.string.abastecedor), R.string.ALERT_REQUIRED));

        if (equipamento == null)
            throw new AlertException(Util.getMessage(currentContext, R.string.equipamento, R.string.ALERT_REQUIRED));

        if (operador == null && justificativaExibe && justificativaOperador == null)
            throw new AlertException(Util.getMessage(currentContext, R.string.justificativa, R.string.ALERT_REQUIRED));


        if (horimetroExibe && quilometragemExibe) {

            if (horimetroEmpty && quilometragemEmpty)
                throw new AlertException(getStr(R.string.ALERT_HOR_OR_KM_REQUIRED));

        } else {

            if (horimetroExibe && horimetroEmpty)
                throw new AlertException(Util.getMessage(currentContext, R.string.horimetro, R.string.ALERT_REQUIRED));


            if (quilometragemExibe && quilometragemEmpty)
                throw new AlertException(Util.getMessage(currentContext, R.string.hodometro, R.string.ALERT_REQUIRED));
        }

    }

    public void validateSave() throws Exception {

        if (horimetroExibe && !horimetroEmpty) {

            boolean nulo = equipamento.getHorimetro() == null || equipamento.getHorimetro().trim().equals(getStr(R.string.EMPTY));

            String value = nulo ? "0" : equipamento.getHorimetro();
            String value2 = horimetro.getText().toString();

            if (Double.valueOf(value) >= Double.valueOf(value2))
                alertValue(Util.getMessage(currentContext, value, R.string.ALERT_HOURS));
            else
                saveTempFile();

        } else if (quilometragemExibe && !quilometragemEmpty) {

            boolean nulo = equipamento.getQuilometragem() == null || equipamento.getQuilometragem().trim().equals(getStr(R.string.EMPTY));

            String value = nulo ? "0" : equipamento.getQuilometragem();
            String value2 = quilometragem.getText().toString();

            if (Double.valueOf(value) >= Double.valueOf(value2))
                alertValue(Util.getMessage(currentContext, value, R.string.ALERT_KM));
            else
                saveTempFile();
        }
    }


    public void alertLogout() {

        if (executeEvent && userRequest.getId() != null) {

            AlertDialog.Builder dialogo = new AlertDialog.Builder(currentContext);
            dialogo.setMessage(getStr(R.string.ALERT_LOGOUT));
            dialogo.setPositiveButton(getStr(R.string.SIM), new
                    DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface di, int arg) {
                            try {
                                clearTextList("A");
                            } catch (Exception e) {
                                tratarExcecao(e);
                            }
                        }
                    });
            dialogo.setNegativeButton(getStr(R.string.NAO), new
                    DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface di, int arg) {
                            executeEvent = false;
                            abastecedorList.setText(userRequest.getNome());
                            executeEvent = true;
                        }
                    });
            dialogo.setTitle(getStr(R.string.AVISO));
            dialogo.show();
        }
    }

    public void readQRCodeOperador() throws Exception {
        clearTextList("O");
        Intent intent = new Intent(AbastecimentoAuthActivity.this, ZBarScannerActivity.class);
        startActivityForResult(intent, QR_CODE_REQUEST_OPER);
    }


    public void alertLogoutQRCodeAbs() {

        if (executeEvent && userRequest.getId() != null) {

            AlertDialog.Builder dialogo = new AlertDialog.Builder(currentContext);
            dialogo.setMessage(getStr(R.string.ALERT_LOGOUT));
            dialogo.setPositiveButton(getStr(R.string.SIM), new
                    DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface di, int arg) {
                            try {
                                clearTextList("A");
                                Intent intent = new Intent(AbastecimentoAuthActivity.this, ZBarScannerActivity.class);
                                startActivityForResult(intent, QR_CODE_REQUEST_ABS);
                            } catch (Exception e) {
                                tratarExcecao(e);
                            }
                        }
                    });
            dialogo.setNegativeButton(getStr(R.string.NAO), new
                    DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface di, int arg) {
                            executeEvent = false;
                            abastecedorList.setText(userRequest.getNome());
                            prefAbsQRCode.setText(userRequest.getNome());
                            executeEvent = true;
                        }
                    });
            dialogo.setTitle(getStr(R.string.AVISO));
            dialogo.show();
        } else {
            Intent intent = new Intent(AbastecimentoAuthActivity.this, ZBarScannerActivity.class);
            startActivityForResult(intent, QR_CODE_REQUEST_ABS);
        }
    }

    public void alertValue(String msg) {

        AlertDialog.Builder dialogo = new AlertDialog.Builder(currentContext);
        dialogo.setMessage(msg);
        dialogo.setPositiveButton(getStr(R.string.SIM), new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface di, int arg) {
                        try {
                            saveTempFile();
                        } catch (Exception e) {
                            tratarExcecao(e);
                        }
                    }
                });
        dialogo.setNegativeButton(getStr(R.string.NAO), new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface di, int arg) {

                    }
                });
        dialogo.setTitle(getStr(R.string.AVISO));
        dialogo.show();
    }

    public void alertConfirm(final UsuarioVO user, final String tipo) {

        final EditText input = new EditText(this);
        AlertDialog.Builder editalert = new AlertDialog.Builder(this);
        editalert.setTitle(getStr(R.string.informe_senha));
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        editalert.setView(input);
        editalert.setPositiveButton(getStr(R.string.OK), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (input.getText().toString().trim().equals(user.getSenha().trim())) {
                    refreshTextList(user, tipo);
                } else {
                    try {
                        clearTextList(tipo);
                    } catch (Exception e) {
                        tratarExcecao(e);
                    }
                    Util.viewMessage(currentContext, getStr(R.string.ALERT_SENHA_INVALIDA));
                }
            }
        });

        editalert.setNegativeButton(getStr(R.string.CANCELAR), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                try {
                    clearTextList(tipo);
                } catch (Exception e) {
                    tratarExcecao(e);
                }


            }
        });
        editalert.show();
    }


    private void clearTextList(String tipo) throws Exception {

        executeEvent = false;

        if (tipo.equals("A")) {
            logout();
            abastecedor = null;
            abastecedorList.setText(getStr(R.string.EMPTY));
            prefAbsQRCode.setText(getStr(R.string.EMPTY));
        } else {
            operador = null;
            operadorList.setText(getStr(R.string.EMPTY));
            prefOperQrCode.setText(getStr(R.string.EMPTY));
            changeLayout(false, true);
        }


        executeEvent = true;
    }

    private void refreshTextList(UsuarioVO user, String tipo) {

        if (tipo.equals("A")) {
            try {
                userRequest = user;
                abastecedor = userRequest;
                login();
            } catch (Exception e) {
                userRequest = new UsuarioVO();
                tratarExcecao(e);
            }
        } else {
            operador = user;
            changeLayout(false, true);

        }
    }

    public void saveTempFile() throws Exception {

        AbastecimentoJson objJSon = new AbastecimentoJson();

        File file = getAbsTempFile();

        objJSon.getCabecalho().setAbastecedor(abastecedor);
        objJSon.getCabecalho().setOperador(operador);
        objJSon.getCabecalho().setEquipamento(equipamento);
        objJSon.getCabecalho().setHoraInicio(horaInicio);
        objJSon.getCabecalho().setHorimetro(horimetro.getText().toString().trim());
        objJSon.getCabecalho().setQuilometragem(quilometragem.getText().toString().trim());
        objJSon.getCabecalho().setIdObra(ccObra);
        objJSon.getCabecalho().setJustificativa(justificativaOperador);
        objJSon.getCabecalho().setObservacaoJustificativa(observacoesJustificativa.getText().toString().trim());
        objJSon.getCabecalho().setAtividade(atividade);
        objJSon.getCabecalho().getAtividade().setFrenteObra(frenteObra);
        objJSon.setRae(new RaeVO(dataHora));
        objJSon.getRae().setPosto(posto);

        String strJson = new Gson().toJson(objJSon);

        if (file.exists())
            file.delete();

        FileWriterWithEncoding fw = new FileWriterWithEncoding(file, getStr(R.string.UTF_8));

        strJson = strJson.replaceAll("'", "");

        fw.write(strJson);
        fw.flush();
        fw.close();

        redirect(AbastecimentoActivity.class, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == QR_CODE_REQUEST) {
            super.onActivityResult(requestCode, resultCode, intent);
        } else if (requestCode == QR_CODE_REQUEST_ABS) {

            try {
                // o idAbastecedor passa a ser na verdade o campo QRCode da tabela de pessoas que vem do arquivo do SAM
                Integer idAbastecedor = Util.getQRCodeId(this, intent);

                if (idAbastecedor == null)
                    throw new AlertException(getStr(R.string.ALERT_SELECT_ABASTECEDOR_QRCODE));

                // para manter a estrutura ja feita, retornaremos o idAbastecedor buscando pelo codigo QRCode lido
                idAbastecedor = getIdAbastecedorByQRCode(idAbastecedor.toString());

                UsuarioVO user = this.findUsuarioArray(idAbastecedor);

                if (user != null) {
                    prefAbsQRCode.setText(user.getNome());
                    userRequest = user;
                } else {
                    throw new AlertException(getStr(R.string.ALERT_SELECT_ABASTECEDOR_VALIDO));
                }

                alertConfirm(user, "A");

            } catch (Exception e) {
                tratarExcecao(e);
            }
        } else if (requestCode == QR_CODE_REQUEST_OPER) {
            try {
                // o idAbastecedor passa a ser na verdade o campo QRCode da tabela de pessoas que vem do arquivo do SAM
                Integer idOperador = Util.getQRCodeId(this, intent);

                if (idOperador == null)
                    throw new AlertException(getStr(R.string.ALERT_SELECT_OPERADOR_QRCODE));

                // para manter a estrutura ja feita, retornaremos o idOperador buscando pelo codigo QRCode lido
                idOperador = getIdOperadorByQRCode(idOperador.toString());

                UsuarioVO user = this.findOperArray(idOperador);

                if (user != null) {
                    prefOperQrCode.setText(user.getNome());
                    userRequest = user;
                } else {
                    throw new AlertException(getStr(R.string.ALERT_SELECT_OPERADOR_QRCODE));
                }

                alertConfirm(user, "O");

            } catch (Exception e) {
                tratarExcecao(e);
            }
        }
    }

    private Integer getIdAbastecedorByQRCode(String qrcode) {
        return getDAO().getUsuarioDAO().getIdPessoalByQRCode(qrcode);
    }

    private Integer getIdOperadorByQRCode(String qrcode) {
        return getDAO().getUsuarioDAO().getIdPessoalByQRCode(qrcode);
    }

    private UsuarioVO findUsuarioArray(int id) {
        for (UsuarioVO user : abastecedorArrayVO) {
            if (user.getIdUsuarioPessoal() == id)
                return user;
        }
        return null;
    }

    private UsuarioVO findOperArray(int id) {
        for (UsuarioVO user : operadorArrayVO) {
            if (user.getIdUsuarioPessoal() == id)
                return user;
        }
        return null;
    }

    @Override
    protected void trataDadosQRCode() throws AlertException {
        int pos = 0;

        int codigoQrCode = idEquipamento;
        EquipamentoVO equip = getDAO().getEquipamentoDAO().getByQRCode(codigoQrCode);


        if(equip != null) {
            idEquipamento = equip.getId();
            intentParameters.setIdRegistroPai(String.valueOf(idEquipamento));
            intentParameters.setIdEquipamento(idEquipamento);
            intentParameters.setFromQRCode(true);

            for (EquipamentoVO equipamentoVO : equipamentoArrayVO) {
                if (equipamentoVO.getId().equals(idEquipamento)) {
                    equipamento = equipamentoVO;
                    break;
                }
                pos++;
            }

            if (equipamento == null) {
                throw new AlertException(getStr(R.string.ERROR_EQUIPAMENTO_INVALIDO));
            }

            prefEquipQRCode.setText(equipamento.getPrefixo());

            changeValues(equipamento.getId());

            changeLayout(true, true);
        } else {
            throw new AlertException(getStr(R.string.ERROR_QRCODE_EQUIPAMENTO_INVALIDO));
        }
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
        if (conteudoLinearLayout7 != null)
            conteudoLinearLayout7.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        if (conteudoLinearLayout8 != null)
            conteudoLinearLayout8.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        if (conteudoLinearLayout9 != null)
            conteudoLinearLayout9.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        if (conteudoLinearLayout10 != null)
            conteudoLinearLayout10.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        if (conteudoLinearLayout11 != null)
            conteudoLinearLayout11.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        if (conteudoLinearLayout12 != null)
            conteudoLinearLayout12.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        if (conteudoLinearLayout13 != null)
            conteudoLinearLayout13.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));

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
        if (headerLinearLayout7 != null)
            headerLinearLayout7.setBackgroundColor(getResources().getColor(R.color.GRAY2));
        if (headerLinearLayout8 != null)
            headerLinearLayout8.setBackgroundColor(getResources().getColor(R.color.GRAY2));
        if (headerLinearLayout9 != null)
            headerLinearLayout9.setBackgroundColor(getResources().getColor(R.color.GRAY2));
        if (headerLinearLayout10 != null)
            headerLinearLayout10.setBackgroundColor(getResources().getColor(R.color.GRAY2));
        if (headerLinearLayout11 != null)
            headerLinearLayout11.setBackgroundColor(getResources().getColor(R.color.GRAY2));
        if (headerLinearLayout12 != null)
            headerLinearLayout12.setBackgroundColor(getResources().getColor(R.color.GRAY2));
        if (headerLinearLayout13 != null)
            headerLinearLayout13.setBackgroundColor(getResources().getColor(R.color.GRAY2));
    }
}