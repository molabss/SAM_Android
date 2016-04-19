package br.com.constran.mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import br.com.constran.mobile.exception.AlertException;
import br.com.constran.mobile.persistence.vo.ObraVO;
import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;
import br.com.constran.mobile.persistence.vo.imp.UsuarioVO;
import br.com.constran.mobile.persistence.vo.rae.abs.AbastecimentoTempVO;
import br.com.constran.mobile.qrcode.ZBarScannerActivity;
import br.com.constran.mobile.view.AbstractActivity;
import br.com.constran.mobile.view.interfaces.InterfaceEditActivity;
import br.com.constran.mobile.view.interfaces.InterfaceListActivity;
import br.com.constran.mobile.view.screens.Detail;
import br.com.constran.mobile.view.screens.GridBody;
import br.com.constran.mobile.view.screens.GridFooter;
import br.com.constran.mobile.view.screens.GridHeader;
import br.com.constran.mobile.view.util.References;
import br.com.constran.mobile.view.util.Util;
//import com.dm.zbar.android.scanner.ZBarScannerActivity;

import java.util.ArrayList;
import java.util.List;

public final class AbastecimentoTempActivity extends AbstractActivity implements InterfaceEditActivity, InterfaceListActivity {

    private AutoCompleteTextView prefixoList;
    private AutoCompleteTextView operadorList;
    private EditText horimetro;
    private EditText quilometragem;
    private EditText prefEquipQRCode;
    private TextView dataHoraView, prefixoView;
    private TableRow horimetroRow, quilometragemRow;
    private Button btAddEquipQRCode;
    private ObraVO obraVO;
    private boolean horimetroExibe;
    private boolean quilometragemExibe;
    private boolean horimetroEmpty;
    private boolean quilometragemEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.abastecimento_temp);

        currentContext = AbastecimentoTempActivity.this;
        currentClass = AbastecimentoTempActivity.class;
        try {
            init();
            initAtributes();
            editValues();
            initEvents();
            editScreen();
            createDetail(getDetailValues());
            createGridHeader(getGridHeaderValues());
            createGridHeader(getGridHeaderTopValues());
            createGridBody(getGridBodyValues());
            createGridFooter(getGridFooterValues());
        } catch (Exception e) {
            tratarExcecao(e);
        }
    }

    @Override
    public void initAtributes() throws Exception {
        dataHoraView = (TextView) findViewById(R.id.absAuthRae);
        prefixoView = (TextView) findViewById(R.id.absAuthPrefView);
        prefixoList = (AutoCompleteTextView) findViewById(R.id.absAuthPref);
        prefEquipQRCode = (EditText) findViewById(R.id.editPrefQrCode);
        operadorList = (AutoCompleteTextView) findViewById(R.id.absAuthOper);
        horimetro = (EditText) findViewById(R.id.absAuthHorim);
        quilometragem = (EditText) findViewById(R.id.absAuthQuilm);
        quilometragemRow = (TableRow) findViewById(R.id.tbRwabsKm);
        horimetroRow = (TableRow) findViewById(R.id.tbRwabshorim);
        btAddEquipQRCode = (Button) findViewById(R.id.btAddEqpQRCode);
        btSalvar = (Button) findViewById(R.id.btAbsAuthSave);
        btNovo = (Button) findViewById(R.id.btAbsAuthNew);
        btCancelar = (Button) findViewById(R.id.btAbsAuthCancel);
    }

    @Override
    public void editValues() throws Exception {
        config = getDAO().getConfiguracoesDAO().getConfiguracaoVO();
        obraVO = getDAO().getObraDAO().getById(config.getIdObra());
        equipamentoArrayVO = getDAO().getEquipamentoDAO().getArrayEquipamentoVO();
        operadorArrayVO = getDAO().getUsuarioDAO().getArrayUsuarioOperador();
        strId = intentParameters.getIdRegistroAtual();
        novoRegistro = (strId == null);
        String[] arrayPK = (!(novoRegistro)) ? Util.getArrayPK(strId, getStr(R.string.TOKEN)) : null;

        if (arrayPK != null) {
            idEquipamento = Integer.valueOf(arrayPK[0]);
            dataHora = arrayPK[1];
            abastecimentoTemp = getDAO().getAbastecimentoTempDAO().getById(idEquipamento, dataHora);
        }
        exibirBotaoAdd();
    }

    private void exibirBotaoAdd() {
        //desabilita edicao
        prefEquipQRCode.setKeyListener(null);

        if ("S".equalsIgnoreCase(obraVO.getUsaQRCode())) {
            btAddEquipQRCode.setVisibility(View.VISIBLE);
            prefEquipQRCode.setVisibility(View.VISIBLE);
            prefixoList.setVisibility(View.GONE);
            prefixoView.setVisibility(View.GONE);
        } else {
            prefixoList.setVisibility(View.VISIBLE);
            btAddEquipQRCode.setVisibility(View.GONE);
            prefEquipQRCode.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        redirectPrincipal();
    }

    @Override
    public Detail getDetailValues() throws Exception {
        //Texto do Detail
        String strDetail = getStr(R.string.DETAIL_PRV);
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

        operadorList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                operador = (UsuarioVO) parent.getItemAtPosition(position);
            }
        });

        operadorList.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                operador = null;
                return false;
            }
        });

        operadorList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                operador = null;
            }
        });

        prefixoList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                equipamento = (EquipamentoVO) parent.getItemAtPosition(position);
                changeLayout(false);
            }
        });

        prefixoList.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                equipamento = null;
                changeLayout(false);
                return false;
            }
        });

        prefixoList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                equipamento = null;
                changeLayout(false);
            }
        });

        btAddEquipQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(AbastecimentoTempActivity.this, ZBarScannerActivity.class);
                    startActivityForResult(intent, QR_CODE_REQUEST);
                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }
        });

        btNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    clear();
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
                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }
        });

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
    }

    private void clear() {
        novoRegistro = true;
        strId = null;
        intentParameters.setIdRegistroAtual(strId);
        refreshIntentParameters(intentParameters);
        changeLayout(true);
    }

    @Override
    public void editScreen() throws Exception {

        hiddenKeyboard(prefixoList);

        frenteObraArrayVO = getDAO().getFrenteObraDAO().getArrayFrenteObraVO(ccObra);
        atividadeArrayVO = getDAO().getAtividadeDAO().getArrayAtividadeVO(idFrenteObra);

        ArrayAdapter<UsuarioVO> oper = new ArrayAdapter<UsuarioVO>(this, android.R.layout.select_dialog_singlechoice, operadorArrayVO);
        oper.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        operadorList.setAdapter(oper);

        //remover equipamento adicionado da lista de equipamentos disponiveis
        atualizarListaEquipDisponiveis();

        changeLayout(true);
    }


    private void changeLayout(boolean clean) {
        if (novoRegistro) {
            if (clean) {
                dataHora = Util.getNow();
                equipamento = null;
                operador = null;

                prefixoList.setText(getStr(R.string.EMPTY));
                operadorList.setText(getStr(R.string.EMPTY));
                horimetro.setText(getStr(R.string.EMPTY));
                quilometragem.setText(getStr(R.string.EMPTY));
            }

            btNovo.setVisibility(View.GONE);
            btNovo.setLayoutParams(new LayoutParams(0, 0));

            if (!"S".equalsIgnoreCase(obraVO.getUsaQRCode())) {
                prefixoList.setVisibility(View.VISIBLE);
                prefixoList.setLayoutParams(new LayoutParams(380, 50));
            } else {
                btAddEquipQRCode.setVisibility(View.VISIBLE);
                prefEquipQRCode.setVisibility(View.VISIBLE);
            }

            prefixoView.setVisibility(View.GONE);
            prefixoView.setLayoutParams(new LayoutParams(0, 0));

        } else {

            prefixoList.setVisibility(View.GONE);
            prefixoList.setLayoutParams(new LayoutParams(0, 0));

            prefixoView.setVisibility(View.VISIBLE);
            prefixoView.setLayoutParams(new LayoutParams(380, 50));

            if ("S".equalsIgnoreCase(obraVO.getUsaQRCode())) {
                btAddEquipQRCode.setVisibility(View.GONE);
                prefEquipQRCode.setVisibility(View.GONE);
            }

            equipamento = abastecimentoTemp.getEquipamento();
            operador = abastecimentoTemp.getOperador();

            prefixoView.setText(equipamento.getPrefixo());

            if (operador != null)
                operadorList.setText(operador.getNome());
            else
                operadorList.setText(getStr(R.string.EMPTY));

            horimetro.setText(abastecimentoTemp.getHorimetro());
            quilometragem.setText(abastecimentoTemp.getQuilometragem());
        }

        dataHoraView.setText(dataHora);


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

    @Override
    public void validateFields() throws Exception {
        if (equipamento == null)
            throw new AlertException(Util.getMessage(currentContext, getStr(R.string.equipamento), R.string.ALERT_REQUIRED));

        horimetroEmpty = horimetro.getText() == null || horimetro.getText().toString().equals(getStr(R.string.EMPTY));
        quilometragemEmpty = quilometragem.getText() == null || quilometragem.getText().toString().equals(getStr(R.string.EMPTY));

        if (horimetroExibe && quilometragemExibe) {
            if (horimetroEmpty && quilometragemEmpty)
                throw new AlertException(getStr(R.string.ALERT_HOR_OR_KM_REQUIRED));

        } else {
            if (horimetroExibe && horimetroEmpty)
                throw new AlertException(Util.getMessage(currentContext, getStr(R.string.horimetro), R.string.ALERT_REQUIRED));

            if (quilometragemExibe && quilometragemEmpty)
                throw new AlertException(Util.getMessage(currentContext, getStr(R.string.quilometragem), R.string.ALERT_REQUIRED));
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
                save();

        } else if (quilometragemExibe && !quilometragemEmpty) {
            boolean nulo = equipamento.getQuilometragem() == null || equipamento.getQuilometragem().trim().equals(getStr(R.string.EMPTY));

            String value = nulo ? "0" : equipamento.getQuilometragem();
            String value2 = quilometragem.getText().toString();

            if (Double.valueOf(value) >= Double.valueOf(value2))
                alertValue(Util.getMessage(currentContext, value, R.string.ALERT_KM));
            else
                save();
        }
    }


    public void alertValue(String msg) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(currentContext);
        dialogo.setMessage(msg);
        dialogo.setPositiveButton(getStr(R.string.SIM), new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface di, int arg) {
                        try {
                            save();
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


    public void save() throws Exception {
        AbastecimentoTempVO abs = new AbastecimentoTempVO();

        abs.setOperador(operador);
        abs.setEquipamento(equipamento);
        abs.setHorimetro(horimetro.getText().toString().trim());
        abs.setQuilometragem(quilometragem.getText().toString().trim());
        abs.setDataHora(dataHoraView.getText().toString().trim());
        abs.setStrId(strId);

        getDAO().getAbastecimentoTempDAO().save(abs);

        clear();

        redirect(AbastecimentoTempActivity.class, false);
    }

    private void atualizarListaEquipDisponiveis() {
        List<Integer> idsEquipamentos = getDAO().getAbastecimentoTempDAO().findIdsEquipamentos(config.getDuracao());
        List<EquipamentoVO> equipamentoList = new ArrayList<EquipamentoVO>();

        for (EquipamentoVO equipamentoVO : equipamentoArrayVO) {
            if (!idsEquipamentos.contains(equipamentoVO.getId())) {
                equipamentoList.add(equipamentoVO);
            }
        }

        ArrayAdapter<EquipamentoVO> eqp = new ArrayAdapter<EquipamentoVO>(this, android.R.layout.select_dialog_singlechoice, equipamentoList.toArray(new EquipamentoVO[0]));
        eqp.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        prefixoList.setAdapter(eqp);
    }

    @Override
    public GridBody getGridBodyValues() throws Exception {
        Integer noContent = null;

        Integer indexCursorIdEquipamento = 0;
        Integer indexCursorDataHora = 1;
        Integer indexCursorPrefixo = 2;
        Integer indexCursorNomeUsuario = 3;
        Integer indexCursorHorimetroHodometro = 4;

        Integer[] colorsRows = new Integer[]{getColor(R.color.WHITE), getColor(R.color.GRAY3)};

        GridBody grid = new GridBody(this);

        grid.setClassRefresh(AbastecimentoTempActivity.class);
        grid.setClassRedirect(AbastecimentoTempActivity.class);
        grid.setClassVO(AbastecimentoTempVO.class);

        grid.setIndexsPKRow(new Integer[]{indexCursorIdEquipamento, indexCursorDataHora});
        grid.setColumnsTxt(new Integer[]{indexCursorPrefixo, indexCursorNomeUsuario, indexCursorHorimetroHodometro, noContent});
        grid.setReferencesImage(new Integer[]{noContent, noContent, noContent, R.drawable.delete});

        grid.setCursor(getCursor());

        grid.setColorsBKG(colorsRows);
        grid.setColorTXT(getColor(R.color.BLACK));

        grid.setIdTable(References.GRID_ID_ABS9);
        grid.setFileLayoutRow(References.GRID_LAYOUT_ABS9);
        grid.setIdsXmlColumn(References.GRID_ID_COLUMNS_ABS9);
        grid.setTypesColumn(new String[]{getStr(R.string.TYPE_SELECT_EDIT), getStr(R.string.TYPE_SELECT_EDIT), getStr(R.string.TYPE_SELECT_EDIT), getStr(R.string.TYPE_REMOVE)});

        return grid;
    }

    @Override
    public GridHeader getGridHeaderValues() throws Exception {
        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.GRAY));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.HEADER_LAYOUT_ABS9); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.HEADER_ID_COLUMNS_ABS9);// Ids (xml) das colunas
        header.setIdTable(References.HEADER_ID_ABS9); //Id do TableLayout);
        header.setNameColumns(getStrArray(R.array.HEADER_NAME_COLUMNS_ABS9));//Nomes das Colunas

        return header;
    }


    @Override
    public GridFooter getGridFooterValues() throws Exception {
        GridFooter footer = new GridFooter(this);
        footer.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        footer.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        footer.setFileLayoutRow(References.FOOTER_LAYOUT_DEFAULT); //arquivo xml - layout (TableRow)
        footer.setIdColumns(References.FOOTER_ID_COLUMNS);// Ids (xml) das colunas
        footer.setIdTable(References.FOOTER_ID_ABS9); //Id do TableLayout);

        return footer;
    }

    @Override
    public GridHeader getGridHeaderTopValues() {
        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.TOP_HEADER_LAYOUT_ABS9); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.TOP_HEADER_ID_COLUMN);// Ids (xml) das colunas
        header.setIdTable(References.TOP_HEADER_ID_ABS9); //Id do TableLayout);
        header.setNameColumns(getStrArray(R.array.TOP_HEADER_NAME_COLUMNS_ABS9));//Nomes das Colunas

        return header;
    }

    @Override
    public Cursor getCursor() throws Exception {
        return getDAO().getAbastecimentoTempDAO().getCursor(config.getDuracao());
    }

    @Override
    protected void trataDadosQRCode() throws AlertException {
        int pos = 0;

        int codigoQrCode = idEquipamento;
        EquipamentoVO equip = getDAO().getEquipamentoDAO().getByQRCode(codigoQrCode);

        if (equip != null) {
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

            prefEquipQRCode.setText(equipamento.getPrefixo());

            abastecimentoTemp = getDAO().getAbastecimentoTempDAO().findAbastecimentoTemp(config.getDuracao(), idEquipamento);

            if (abastecimentoTemp != null) {
                ccObra = config.getIdObra();
                ccObra2 = config.getIdObra2();
                intentParameters.setIdRegistroAtual(String.valueOf(idEquipamento) + "#" + Util.getNow());
                strId = intentParameters.getIdRegistroAtual();
                dataHora = abastecimentoTemp.getDataHora();
                novoRegistro = false;
            }

            changeLayout(false);
        } else {
            throw new AlertException(getStr(R.string.ERROR_QRCODE_EQUIPAMENTO_INVALIDO));
        }
    }

}