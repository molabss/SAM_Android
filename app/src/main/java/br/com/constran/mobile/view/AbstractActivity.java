package br.com.constran.mobile.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

//import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.constran.mobile.AbastecimentoActivity;
import br.com.constran.mobile.AbastecimentoAuthActivity;
import br.com.constran.mobile.AbastecimentoTempActivity;
import br.com.constran.mobile.AbastecimentosSearchActivity;
import br.com.constran.mobile.EquipamentosMovimentacaoDiariaActivity;
import br.com.constran.mobile.EquipamentosParteDiariaActivity;
import br.com.constran.mobile.LocalizacaoActivity;
import br.com.constran.mobile.LoginActivity;
import br.com.constran.mobile.PrincipalActivity;
import br.com.constran.mobile.R;
import br.com.constran.mobile.enums.TipoModulo;
import br.com.constran.mobile.exception.AlertException;
import br.com.constran.mobile.persistence.dao.DAOFactory;
import br.com.constran.mobile.persistence.vo.AbstractVO;
import br.com.constran.mobile.persistence.vo.ObraVO;
import br.com.constran.mobile.persistence.vo.aprop.ApropriacaoVO;
import br.com.constran.mobile.persistence.vo.aprop.eqp.ApropriacaoEquipamentoVO;
import br.com.constran.mobile.persistence.vo.aprop.eqp.EquipamentoParteDiariaVO;
import br.com.constran.mobile.persistence.vo.aprop.eqp.EventoVO;
import br.com.constran.mobile.persistence.vo.aprop.mov.ApropriacaoMovimentacaoVO;
import br.com.constran.mobile.persistence.vo.aprop.mov.EquipamentoMovimentacaoDiariaVO;
import br.com.constran.mobile.persistence.vo.aprop.mov.FormMovimentacaoVO;
import br.com.constran.mobile.persistence.vo.aprop.mov.ViagemVO;
import br.com.constran.mobile.persistence.vo.imp.AtividadeVO;
import br.com.constran.mobile.persistence.vo.imp.ComponenteVO;
import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;
import br.com.constran.mobile.persistence.vo.imp.FrenteObraVO;
import br.com.constran.mobile.persistence.vo.imp.MaterialVO;
import br.com.constran.mobile.persistence.vo.imp.OrigemDestinoVO;
import br.com.constran.mobile.persistence.vo.imp.ParalisacaoVO;
import br.com.constran.mobile.persistence.vo.imp.ServicoVO;
import br.com.constran.mobile.persistence.vo.imp.UsuarioVO;
import br.com.constran.mobile.persistence.vo.imp.json.AbastecimentoJson;
import br.com.constran.mobile.persistence.vo.imp.json.ExportMobile;
import br.com.constran.mobile.persistence.vo.imp.json.ExportMobileDate;
import br.com.constran.mobile.persistence.vo.menu.ConfiguracoesVO;
import br.com.constran.mobile.persistence.vo.menu.LocalizacaoVO;
import br.com.constran.mobile.persistence.vo.rae.abs.AbastecimentoPostoVO;
import br.com.constran.mobile.persistence.vo.rae.abs.AbastecimentoTempVO;
import br.com.constran.mobile.persistence.vo.rae.abs.CombustivelLubrificanteVO;
import br.com.constran.mobile.persistence.vo.rae.abs.JustificativaOperadorVO;
import br.com.constran.mobile.persistence.vo.rae.abs.PostoVO;
import br.com.constran.mobile.qrcode.ZBarScannerActivity;
import br.com.constran.mobile.system.AppDirectory;
import br.com.constran.mobile.system.SharedPreferencesHelper;
import br.com.constran.mobile.view.indicepluviometrico.IndicePluviometricoServicoGridActivity;
import br.com.constran.mobile.view.maodeobra.MaoObraServicoGridActivity;
import br.com.constran.mobile.view.params.IntentParameters;
import br.com.constran.mobile.view.screens.Detail;
import br.com.constran.mobile.view.screens.GridBody;
import br.com.constran.mobile.view.screens.GridFooter;
import br.com.constran.mobile.view.screens.GridHeader;
import br.com.constran.mobile.view.util.QRCodeData;
import br.com.constran.mobile.view.util.Util;

public abstract class AbstractActivity extends Activity {

    protected final int QR_CODE_REQUEST = 999;
    protected final int QR_CODE_FORM_REQUEST = 996;

    protected Button btEquipamentos;
    protected Button btMovimentacoes;
    protected Button btAbastecimento;
    protected Button btIndicePluviometrico;
    protected Button btMaoDeObra;
    protected Button btSalvar;
    protected Button btCancelar;
    protected Button btNovo;
    protected Button btManutencoes;

    protected IntentParameters intentParameters;

    protected static String intentDesc;
    protected static Context currentContext;
    protected static String[] dataArray;
    protected static Class<?> currentClass;
    protected static UsuarioVO userRequest;
    protected static ConfiguracoesVO config;

    protected UsuarioVO[] usuarioArrayVO;
    protected ServicoVO[] servicoArrayVO;
    protected MaterialVO[] materialArrayVO;
    protected AtividadeVO[] atividadeArrayVO;
    protected FrenteObraVO[] frenteObraArrayVO;
    protected ComponenteVO[] componenteArrayVO;
    protected EquipamentoVO[] equipamentoArrayVO;
    protected EquipamentoVO[] equipamentoCargaArrayVO;
    protected ParalisacaoVO[] paralisacaoArrayVO;
    protected OrigemDestinoVO[] origemDestinoArrayVO;

    protected ObraVO[] obraArrayVO;
    protected PostoVO[] postoArrayVO;
    protected UsuarioVO[] abastecedorArrayVO;
    protected UsuarioVO[] operadorArrayVO;
    protected JustificativaOperadorVO[] justificativaOperadorArrayVO;
    protected CombustivelLubrificanteVO[] combustivelArrayVO;

    protected Integer ccObra;
    protected Integer ccObra2;

    protected PostoVO posto;
    protected UsuarioVO operador;
    protected UsuarioVO abastecedor;
    protected AtividadeVO atividade;
    protected FrenteObraVO frenteObra;
    protected EquipamentoVO equipamento;
    protected AbastecimentoTempVO abastecimentoTemp;
    protected AbastecimentoPostoVO abastecimentoPosto;
    protected CombustivelLubrificanteVO combustivelLubrificante;
    protected JustificativaOperadorVO justificativaOperador;
    protected ObraVO obra;

    protected Integer idLocalizacao;
    protected Integer idMaterial;
    protected Integer idOrigem;
    protected Integer idDestino;
    protected Integer idAtividade;
    protected Integer idFrenteObra;
    protected Integer idApropriacao;
    protected Integer idEquipamento;
    protected Integer idEquipamentoCarga;
    protected Integer idServico;
    protected Integer idParalisacao;
    protected Integer idComponente;
    protected Integer idCategoria;
    protected Integer idSelected;
    protected Integer idCombustivelLubrificante;

    protected Integer indexOrigemDestino;
    protected Integer indexFrenteObra;
    protected Integer indexAtividade;

    protected String codParalisacao;
    protected String descEquipamento;
    protected String descServico;
    protected String descParalisacao;
    protected String reqeParalisacao;
    protected String descComponente;
    protected String descEquipamentoCarga;
    protected String tipoEquipamento;
    protected String tipoOrigem;
    protected String horaInicio;
    protected String horaTermino;
    protected String horaViagem;
    protected String eTicket;
    protected String peso;
    protected String strId;
    protected String idParteDiaria;
    protected String operador1;
    protected String operador2;
    protected String producao;
    protected String horimetroInicial;
    protected String horimetroFinal;
    protected String estaca;
    protected String estacaInicioView;
    protected String estacaFimView;
    protected String dataHora;
    protected String tipoLocalizacao;
    protected String tipoViagem;
    protected String estacaInicio;
    protected String estacaFim;

    protected String descLocal;
    protected String estacaInicioLocal;
    protected String estacaFimLocal;

    //protected boolean viraVira;
    protected boolean apropriar;
    protected boolean novoRegistro;

    private int numRow;
    private int contRows;
    private int indexAux;
    private int numRowAux;
    private TableLayout table;
    private LayoutInflater inflater;
    private String[] currentGridValues;
    private String[] beforeColumnsValues;

    protected List<String[]> values;

    protected Map<String, String[]> rowsColumnsMap;

    protected void init() {

        intentParameters = (IntentParameters) getIntent().getSerializableExtra(getStr(R.string.STRING_INTENT_PARAMS));

        if (intentParameters == null) {
            intentParameters = new IntentParameters();
        }

        refreshUserRequest();

    }


    /*
    @Override
    protected void onResume() {
        try {
            currentContext = this;

            if (currentContext != null && !currentClass.equals(PrincipalActivity.class) &&
                    (currentClass.getName().contains("Movimentacao") || currentClass.getName().contains("ParteDiaria")
                            || currentClass.getName().contains("Equipamento"))) {
                checkLocal();
            }
        } catch (Exception e) {
            tratarExcecao(e);
        } finally {
            super.onResume();
        }
    }
    */


    @Override
    protected void onResume() {

        try {
            currentContext = this;

            if (currentContext != null &&
                    !currentClass.equals(PrincipalActivity.class) &&
                    !currentClass.getName().contains("Manutencao") &&
                    (
                            currentClass.getName().contains("Movimentacao") ||
                                    currentClass.getName().contains("ParteDiaria") ||
                                        currentClass.getName().contains("Equipamento")
                    )
                    )
            {
                checkLocal();
            }
        } catch (Exception e) {
            tratarExcecao(e);
        } finally {
            super.onResume();
        }
    }


    public void createGridHeader(final GridHeader pHParameters) {

        TableLayout table = (TableLayout) findViewById(pHParameters.getIdTable());
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newRow = inflater.inflate(pHParameters.getFileLayoutRow(), null);

        int i = 0;

        obra = getDAO().getObraDAO().getById(config.getIdObra());
        final boolean usaQRCode = obra != null ? "S".equalsIgnoreCase(obra.getUsaQRCode()) : false;

        for (int id : pHParameters.getIdColumns()) {

            Button column = (Button) newRow.findViewById(id);
            column.setTextColor(pHParameters.getColorTXT());
            column.setText(pHParameters.getNameColumns()[i++]);
            column.setBackground(null);

            //se a coluna tiver o "+", adiciona o icone e configura o listener
            if (column.getText().equals(getStr(R.string.ADD))) {

                //exibe o botão de adicao normal ou o do QR code
                if (!usaQRCode) {
                    column.setBackgroundResource(R.drawable.add);
                } else {
                    if (!intentParameters.getMenu().equals(getStr(R.string.OPTION_MENU_ABS))) {
                        column.setText("");
                        column.setBackgroundResource(R.drawable.camera);
                    } else {
                        column.setBackgroundResource(R.drawable.add);
                    }
                }

                column.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        if (intentParameters.getMenu().equals(getStr(R.string.OPTION_MENU_ABS)))
                            try {
                                verificaAbastecimentoTempFile();
                            } catch (Exception e) {
                                tratarExcecao(e);
                            }
                        else {
                            if (!usaQRCode) {
                                redirect(pHParameters.getClassRedirect(), false);
                            } else {
                                Intent intent = new Intent(AbstractActivity.this, ZBarScannerActivity.class);
                                startActivityForResult(intent, QR_CODE_REQUEST);
                            }
                        }
                    }
                });
            }
        }

        newRow.setBackgroundColor(pHParameters.getColorBKG());

        table.addView(newRow, 0);

    }

    public void createGridFooter(GridFooter pFParameters) {

        TableLayout table = (TableLayout) findViewById(pFParameters.getIdTable());
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newRow = inflater.inflate(pFParameters.getFileLayoutRow(), null);

        for (int id : pFParameters.getIdColumns()) {

            Button column = (Button) newRow.findViewById(id);
            column.setTextColor(pFParameters.getColorTXT());
            column.setBackground(null);
        }

        newRow.setBackgroundColor(pFParameters.getColorBKG());

        table.addView(newRow, 0);

    }

    /**
     * Método responsável pela criação do corpo da grid
     */

    public void createGridBody(GridBody gridBody) {

        startElementsGrid(gridBody);
        startNoRepeatValues(gridBody);

        while (gridBody.getCursor().moveToNext()) {

            int numColumn = 0;
            String strId = getStrIdRecord(gridBody);
            String valueValidate = getValueValidate(gridBody);
            View newRow = inflater.inflate(gridBody.getFileLayoutRow(), null);

            for (int idColumn : gridBody.getIdsXmlColumn())
                setColumn(gridBody, newRow, strId, valueValidate, idColumn, numColumn++);

            newRow.setBackgroundColor(getColorRow(gridBody, strId, numRow));
            table.addView(newRow, numRow++);
        }
    }

    /**
     * Método responsável pela geração do evento de uma coluna da grid
     */
    private void createEvent(final GridBody gridBody, View column, final String valueValidate, final int numColumn) {

        column.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeClickEvent(gridBody, view, valueValidate, numColumn);
            }
        });
    }

    /**
     * Método que inicia os arrays que mantém os valores das colunas que não deverão ser repetidos na grid (se houver);
     */

    private void startNoRepeatValues(GridBody gridBody) {

        if (gridBody.getIndexColumnsNoRepeat() != null) {
            beforeColumnsValues = new String[gridBody.getIndexColumnsNoRepeat().length];
            currentGridValues = new String[gridBody.getIndexColumnsNoRepeat().length];
        }
    }

    private void startElementsGrid(GridBody gridBody) {

        numRow = 0;
        indexAux = 0;
        numRowAux = 1;

        contRows = gridBody.getCursor().getCount();

        table = (TableLayout) findViewById(gridBody.getIdTable());
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /**
     * Método responsável pela definição do evento de uma coluna da grid
     */
    private void executeClickEvent(GridBody gridBody, View view, String valueValidate, int numColumn) {
        intentParameters.setFromQRCode(false);

        String typeColumn = getTypeColumn(gridBody, numColumn);

        boolean needAlert = false;
        boolean needConfirm = false;
        boolean isRemove = typeColumn.equals(getStr(R.string.TYPE_REMOVE));
        boolean isRemoveAuth = typeColumn.equals(getStr(R.string.TYPE_AUTH_REMOVE));
        boolean isCopy = typeColumn.equals(getStr(R.string.TYPE_SELECT_COPY));
        boolean isNotValid = isNotValueValid(gridBody, typeColumn, valueValidate);

        if (isNotValid)
            needAlert = true;
        else if (isCopy)
            needConfirm = true;

        if (isRemove)
            showAlertRemove(gridBody, view);
        else if (isRemoveAuth)
            showAlertAuthRemove(gridBody, view, valueValidate);
        else if (needAlert)
            showAlertIncompleteValues(gridBody);
        else if (needConfirm)
            showAlertConfrmCopy(gridBody, view);
        else
            preencherIntentParams(gridBody, view, typeColumn);
    }


    private boolean isNotValueValid(GridBody gridBody, String typeColumn, String valueValidate) {
        return gridBody.isValidate() && typeColumn.equals(gridBody.getTypeValidate()) &&
                (valueValidate == null || valueValidate.trim().equals(getStr(R.string.EMPTY)));
    }

    private String getValueValidate(GridBody gridBody) {

        if (gridBody.getIndexValidate() != null)
            return gridBody.getCursor().getString(gridBody.getIndexValidate());

        return null;
    }

    /**
     * Método que retorna se a coluna deve ser visível na grid
     */
    protected boolean isColumnNoShow(GridBody gridBody, int numColumn) {

        return (gridBody.getValueNoShow() != null && gridBody.getIndexColumnNoShow() != null &&
                gridBody.getValueNoShow().equals(gridBody.getCursor().getString(gridBody.getIndexValueNoShow())) &&
                gridBody.getIndexColumnNoShow() == numColumn);
    }


    /**
     * Método responsável por definir o(s) valor(es) que compõe(m) o id do registro em uma única String
     */
    private String getStrIdRecord(GridBody gridBody) {

        String rowId = "";

        if (gridBody.getIndexsPKRow() != null && gridBody.getCursor().getCount() > 0) {
            for (int i = 0; i < gridBody.getIndexsPKRow().length; i++) {
                rowId += gridBody.getCursor().getString(gridBody.getIndexsPKRow()[i]);
                if (gridBody.getIndexsPKRow().length > 1 && i < gridBody.getIndexsPKRow().length - 1) {
                    rowId += getStr(R.string.TOKEN);
                }
            }
        }
        return rowId;
    }

    /**
     * Método que retorna o tipo da coluna
     */
    private String getTypeColumn(GridBody gridBody, int numColumn) {
        return gridBody.getTypesColumn()[numColumn];
    }


    /**
     * Método que retorna um Dialog que permite copiar os dados do registro selecionado
     */
    private void showAlertConfrmCopy(final GridBody gridBody, final View view) {

        AlertDialog.Builder dialogo = new AlertDialog.Builder(gridBody.getContext());
        dialogo.setMessage(getStr(R.string.ALERT_CONFIRM_COPY));
        dialogo.setPositiveButton(getStr(R.string.SIM), new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface di, int arg) {
                        Class<?> objClass = null;
                        intentParameters.setIdRegistroCopia(String.valueOf(view.getTag()));
                        objClass = gridBody.getClassRefresh();
                        refreshIntentParameters(intentParameters);
                        redirect(objClass, false);
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

    /**
     * Método que retorna um Dialog que informa a necessidade de incluir dados ao registro
     */
    private void showAlertIncompleteValues(GridBody gridBody) {

        AlertDialog.Builder dialogo = new AlertDialog.Builder(gridBody.getContext());
        dialogo.setMessage(getStr(R.string.ALERT_INCOMPLETE_VALUES));
        dialogo.setNeutralButton(getStr(R.string.OK), new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface di, int arg) {
                    }
                });
        dialogo.setTitle(getStr(R.string.AVISO));
        dialogo.show();
    }

    /**
     * Método que retorna um Dialog que permite remover o registro após autenticação
     */
    private void showAlertAuthRemove(final GridBody gridBody, final View view, final String password) {

        if (password != null && !password.trim().equals(getStr(R.string.EMPTY))) {

            final EditText input2 = new EditText(currentContext);

            AlertDialog.Builder editAlert = new AlertDialog.Builder(currentContext);
            editAlert.setTitle(gridBody.getMsgAuthRemove());
            input2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editAlert.setView(input2);
            editAlert.setPositiveButton(getStr(R.string.OK), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (password != null && input2.getText().toString().trim().equals(password.trim())) {
                        getDAO().getUtilDAO().excluir(gridBody.getClassVO(), String.valueOf(view.getTag()));
                        redirect(gridBody.getClassRefresh(), false);
                    } else
                        Util.viewMessage(currentContext, getStr(R.string.ALERT_SENHA_INVALIDA));
                }
            });
            editAlert.setNegativeButton(getStr(R.string.CANCELAR), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            editAlert.show();
        } else {
            showAlertRemove(gridBody, view);

        }
    }

    /**
     * Método que retorna um Dialog que permite remover o registro após alerta
     */
    private void showAlertRemove(final GridBody gridBody, final View view) {

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder dialogo = new AlertDialog.Builder(gridBody.getContext());
                int childrens = getDAO().getUtilDAO().getCountChildrens(gridBody.getClassVO(), String.valueOf(view.getTag()));

                if (childrens > 0) {
                    dialogo.setMessage(gridBody.getMsgNotRemove());
                    dialogo.setNegativeButton(getStr(R.string.OK), new
                            DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface di, int arg) {
                                }
                            });
                } else {

                    dialogo.setMessage(getStr(R.string.ALERT_CONFIRM_REMOVE));
                    dialogo.setPositiveButton(getStr(R.string.SIM), new
                            DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface di, int arg) {
                                    getDAO().getUtilDAO().excluir(gridBody.getClassVO(), String.valueOf(view.getTag()));
                                    redirect(gridBody.getClassRefresh(), true);
                                }
                            });

                    dialogo.setNegativeButton(getStr(R.string.NAO), new
                            DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface di, int arg) {
                                }
                            });
                }
                dialogo.setTitle(getStr(R.string.AVISO));
                dialogo.show();
            }
        });
    }

    /**
     * Método padrão que preenche os dados e características de uma coluna da grid
     */
    private void setColumn(GridBody gridBody, View newRow, String idRecord, String valueValidate, int idColumn, int numColumn) {

        String typeColumn = getTypeColumn(gridBody, numColumn);
        View column = newRow.findViewById(idColumn);
        column.setTag(idRecord);

        boolean isView = typeColumn.equals(getStr(R.string.TYPE_VIEW));
        boolean isRowNum = typeColumn.equals(getStr(R.string.TYPE_ROWNUM));
        boolean noShowColumn = isColumnNoShow(gridBody, numColumn);
        boolean showColumn = !noShowColumn;
        boolean noRepeatColumn = isColumnNoRepeat(gridBody, numColumn, isView, isRowNum);
        boolean isImage = gridBody.getReferencesImage() != null && gridBody.getReferencesImage()[numColumn] != null;
        boolean isEvent = showColumn && !(isView || isRowNum);

        if (isImage && showColumn)
            column.setBackgroundResource(gridBody.getReferencesImage()[numColumn]);
        else {
            column.setBackground(null);
            if (showColumn) {
                ((Button) column).setTextColor(gridBody.getColorTXT());
                if (noRepeatColumn)
                    setColumnsNoRepeat(gridBody, column, numColumn, isView, isRowNum);
                else
                    setColumns(gridBody, column, numColumn, isRowNum);
            }
        }
        if (isEvent)
            createEvent(gridBody, column, valueValidate, numColumn);
    }

    private void setColumns(GridBody gridBody, View column, int numColumn, boolean isRowNum) {

        if (isRowNum)
            ((Button) column).setText(String.valueOf(contRows--));
        else {
            Integer indexCursor = gridBody.getColumnsTxt()[numColumn];
            String txt = gridBody.getCursor().getString(indexCursor);
            ((Button) column).setText(txt);
        }
    }

    private void setColumnsNoRepeat(GridBody gridBody, View column, int numColumn, boolean isView, boolean isRowNum) {

        if (isView) {

            Integer indexCursor = gridBody.getColumnsTxt()[numColumn];
            String txt = gridBody.getCursor().getString(indexCursor);
            Integer cIndex = getCurrentIndex(gridBody, indexCursor);

            currentGridValues[cIndex] = txt;

            String beforeValue = beforeColumnsValues[cIndex];

            boolean repeat = beforeValue != null && beforeColumnsValues[cIndex].equalsIgnoreCase(currentGridValues[cIndex]);

            if (repeat)
                ((Button) column).setText(getStr(R.string.EMPTY));
            else
                ((Button) column).setText(txt);

            if (cIndex > 0) {
                beforeColumnsValues[cIndex] = txt;
                indexAux = cIndex - 1;
                beforeColumnsValues[indexAux] = currentGridValues[indexAux];
            }

        } else if (isRowNum) {

            if (beforeColumnsValues[0] == null || !(beforeColumnsValues[0].equalsIgnoreCase(currentGridValues[0]))) {
                numRowAux = 1;
            }

            ((Button) column).setText(String.valueOf(numRowAux++));
        }
    }

    private boolean isColumnNoRepeat(GridBody gridBody, int numColumn, boolean typeView, boolean typeRowNum) {

        Integer[] array = gridBody.getIndexColumnsNoRepeat();
        boolean valid = array != null;

        if (typeRowNum)
            return valid;

        if (typeView && valid) {

            int indexCursor = gridBody.getColumnsTxt()[numColumn];

            for (int value : array) {
                if (value == indexCursor)
                    return true;
            }
        }
        return false;
    }

    private Integer getCurrentIndex(GridBody gridBody, int indexCursor) {

        Integer[] indexArray = gridBody.getIndexColumnsNoRepeat();

        int i = 0;

        for (int value : indexArray) {
            if (value == indexCursor)
                return i;
            i++;
        }

        return null;
    }

    /**
     * Método que preenche os parâmetros necessários para encaminhar à nova/atual Activity
     */
    private void preencherIntentParams(GridBody gridBody, View view, String typeColumn) {

        Class<?> objClass = null;

        intentParameters.setIdRegistroPai(null);
        intentParameters.setIdRegistroAtual(null);
        intentParameters.setIdRegistroCopia(null);

        if (typeColumn.equals(getStr(R.string.TYPE_NEXT_PAGE))) {

            intentParameters.setIdRegistroPai(String.valueOf(view.getTag()));
            objClass = gridBody.getClassRedirect();

        } else if (typeColumn.equals(getStr(R.string.TYPE_LIST_PAGE))) {

            intentParameters.setIdRegistroPai(String.valueOf(view.getTag()));
            objClass = gridBody.getClassList();

        } else if (typeColumn.equals(getStr(R.string.TYPE_EDIT_PAGE))) {

            intentParameters.setIdRegistroAtual(String.valueOf(view.getTag()));
            objClass = gridBody.getClassEdit();

        } else if (typeColumn.equals(getStr(R.string.TYPE_SELECT_NEW))) {

            intentParameters.setIdRegistroPai(String.valueOf(view.getTag()));
            objClass = gridBody.getClassRedirect();

        } else if (typeColumn.equals(getStr(R.string.TYPE_EDIT)) ||
                typeColumn.equals(getStr(R.string.TYPE_SELECT_EDIT)) ||
                typeColumn.equals(getStr(R.string.TYPE_SELECT_VIEW))) {

            intentParameters.setIdRegistroAtual(String.valueOf(view.getTag()));
            objClass = gridBody.getClassRedirect();
        }

        refreshIntentParameters(intentParameters);

        redirect(objClass, false);
    }

    private int getColorRow(GridBody gridBody, String idRecord, int numRow) {

        String idSelected = null;

        if (gridBody.isPaintSelected()) {
            String idTelaAnterior = intentParameters.getIdRegistroPai();
            String idTelaAtual = String.valueOf(intentParameters.getIdEquipamento());
            idSelected = idTelaAnterior != null ? idTelaAnterior : idTelaAtual;
        }
        if (idSelected != null && idRecord.equalsIgnoreCase(idSelected))
            return getColor(R.color.SELECTED);
        else
            return gridBody.getColorsBKG()[numRow % 2];

    }

    public void refreshIntentParameters(IntentParameters intentParameters) {
        this.intentParameters = intentParameters;
    }

    public void refreshUserRequest() {

        userRequest = intentParameters.getUserSession();

        if (userRequest == null)
            userRequest = new UsuarioVO();
    }

    public void removeUserSession() {
        intentParameters.setUserSession(null);
        refreshUserRequest();
    }

    public void createDetail(final Detail pDParameters) {

        TableLayout table = (TableLayout) findViewById(pDParameters.getIdTable());
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newRow = inflater.inflate(pDParameters.getFileLayoutRow(), null);

        for (int id : pDParameters.getIdColumns()) {

            Button column = (Button) newRow.findViewById(id);
            column.setTextColor(pDParameters.getColorTXT());
            column.setText(pDParameters.getDetail());
            column.setBackground(null);

        }

        newRow.setBackgroundColor(pDParameters.getColorBKG());
        table.addView(newRow, 0);
    }

    public void redirectPrincipal() {
        redirect(PrincipalActivity.class, true);
    }

    public void redirect(Class<?> pClass, boolean clear) {

        if (clear) {
            intentParameters = new IntentParameters(intentParameters.getMenu(), intentParameters.getUserSession());
        }

        Intent intent = new Intent(currentContext, pClass);
        intent.putExtra(getStr(R.string.STRING_INTENT_PARAMS), intentParameters);

        //quando o redirecionamento é feito no intuito de fazer o refresh dos dados da tela, da erro no caso de localizacao se a flag estiver sendo passada
        if (!LocalizacaoActivity.class.equals(pClass)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        putExtraParams(intent);

        startActivity(intent);

        finish();
    }

    public void putExtraParams(Intent intent) {

    }

    private void incrementProgressBar(ProgressDialog progress) {
        progress.setProgress(progress.getProgress() + 4);
    }

    public void saveOrUpdate(AbstractVO pVO) {

        if (pVO instanceof ApropriacaoVO) {
            getDAO().getApropriacaoDAO().save((ApropriacaoVO) pVO);
        } else if (pVO instanceof ApropriacaoMovimentacaoVO) {
            getDAO().getApropriacaoMovimentacaoDAO().save((ApropriacaoMovimentacaoVO) pVO);
        } else if (pVO instanceof ViagemVO) {
            getDAO().getViagemDAO().save((ViagemVO) pVO);
        } else if (pVO instanceof EventoVO) {
            getDAO().getEventoDAO().save((EventoVO) pVO);
        } else if (pVO instanceof EquipamentoMovimentacaoDiariaVO) {
            getDAO().getMovimentacaoDiariaDAO().save((EquipamentoMovimentacaoDiariaVO) pVO);
        } else if (pVO instanceof EquipamentoParteDiariaVO) {
            getDAO().getParteDiariaDAO().save((EquipamentoParteDiariaVO) pVO);
        } else if (pVO instanceof ApropriacaoEquipamentoVO) {
            getDAO().getApropriacaoEquipamentoDAO().save((ApropriacaoEquipamentoVO) pVO);
        } else if (pVO instanceof LocalizacaoVO) {
            getDAO().getLocalizacaoDAO().save((LocalizacaoVO) pVO);
        }

    }

    public int getIndexById(Integer pId, AbstractVO[] pArray) {

        int index = 0;

        if (pId != null) {

            for (int i = 0; i < pArray.length; i++) {

                if (pArray[i].getId() != null && pArray[i].getId().intValue() == pId) {

                    index = i;
                }
            }
        }

        return index;
    }

    public int getIndexById(String pId, AbstractVO[] pArray) {

        if (pId == null) {

            return 0;

        }

        return getIndexById(Integer.valueOf(pId), pArray);
    }

    /**
     * Preenche ambos os IDs usuarios de configuração
     * idUsuarioPessoal é apontado quando existe abastecimentos (posto e RAEs)
     * idUsuario é apontando para as demais apropriacoes (Movimentacoes, Equipamentos, Mao de obra)
     * <p/>
     * Tratamento é feito para evitar que hava problemas se existir algum apontador que faça tanto abastecimento quanto apropriacao de viagens/equipamentos/MO
     * Ate a data 09/09/2014 não era possível na prática que um apontador de abastecimentos realizasse outro tipo de apontamento.
     */
    private void preencheUsuario() {
        if (config.getIdUsuarioPessoal() != null && config.getIdUsuario() == null) {
            config.setIdUsuario(config.getIdUsuarioPessoal());
        } else if (config.getIdUsuarioPessoal() == null && config.getIdUsuario() != null) {
            config.setIdUsuarioPessoal(config.getIdUsuario());
        }
    }

    /**
     * Valida se existe apropriacao e o usuario nao foi configurado no dispositivo, lancando excecao
     * Se nao existir abastecimento e o idUsuarioPessoal nao e nulo, entao o idUsuario = idUsuarioPessoal
     *
     * @param objJSon
     * @throws Exception
     */
    private void validaUsuarioConfig(ExportMobile objJSon) throws Exception {
        if (config.getIdUsuario() == null && config.getIdUsuarioPessoal() == null && objJSon.getApropriacoes() != null && !objJSon.getApropriacoes().isEmpty()) {
            throw new Exception(getStr(R.string.ALERT_USUARIO_NAO_INFORMADO));
        } else if (config.getIdUsuarioPessoal() != null && (objJSon.getRaes() == null || objJSon.getRaes().isEmpty())) {
            config.setIdUsuario(config.getIdUsuarioPessoal());
            config.setIdUsuarioPessoal(null);
        }
    }


    /**
     * Valida se existe apropriacao e o usuario nao foi configurado no dispositivo, lancando excecao
     * Se nao existir abastecimento e o idUsuarioPessoal nao e nulo, entao o idUsuario = idUsuarioPessoal
     *
     * @param objJSon
     * @throws Exception
     */
    private void validaUsuarioConfig(ExportMobileDate objJSon) throws Exception {
        if (config.getIdUsuario() == null && config.getIdUsuarioPessoal() == null && objJSon.getApropriacoes() != null && !objJSon.getApropriacoes().isEmpty()) {
            throw new Exception(getStr(R.string.ALERT_USUARIO_NAO_INFORMADO));
        } else if (config.getIdUsuarioPessoal() != null && (objJSon.getRaes() == null || objJSon.getRaes().isEmpty())) {
            config.setIdUsuario(config.getIdUsuarioPessoal());
            config.setIdUsuarioPessoal(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(intentParameters == null){
            return false;
        }

        int xml = (intentParameters.getUserSession() != null)
                ? R.menu.activity_menu_aux
                : R.menu.activity_menu_aux_no_logout;

        getMenuInflater().inflate(xml, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferencesHelper.AppModulo.CONTEXT = currentContext;
        final Toast toastMenu = Toast.makeText(currentContext, "Este módulo está desativado neste tablet.", Toast.LENGTH_SHORT);


        try {

            switch (item.getItemId()) {
                case R.id.mov: //movimentacao

                    if(!SharedPreferencesHelper.AppModulo.estaMovimentacoesAtivado()){
                        toastMenu.show();
                        break;
                    }

                    try {
                        checkLocal();

                        IntentParameters objJson = new IntentParameters(getStr(R.string.OPTION_MENU_MOV), intentParameters.getUserSession());
                        refreshIntentParameters(objJson);
                        redirect(EquipamentosMovimentacaoDiariaActivity.class, false);
                    } catch (Exception e) {
                        tratarExcecao(e);
                    }
                    break;
                case R.id.eqp: //equipamento

                    if(!SharedPreferencesHelper.AppModulo.estaEquipamentosAtivado()){
                        toastMenu.show();
                        break;
                    }


                    try {
                        checkLocal();

                        IntentParameters objJson = new IntentParameters(getStr(R.string.OPTION_MENU_EQP), intentParameters.getUserSession());
                        refreshIntentParameters(objJson);
                        redirect(EquipamentosParteDiariaActivity.class, true);
                    } catch (Exception e) {
                        tratarExcecao(e);
                    }
                    break;
                case R.id.abs://abastecimento


                    if(!SharedPreferencesHelper.AppModulo.estaAbastecimentosAtivado()){
                        toastMenu.show();
                        break;
                    }


                    try {
                        checkPosto();

                        IntentParameters objJson = new IntentParameters(getStr(R.string.OPTION_MENU_ABS), intentParameters.getUserSession());
                        refreshIntentParameters(objJson);
                        verifyLogin(AbastecimentosSearchActivity.class);
                    } catch (Exception e) {
                        tratarExcecao(e);
                    }
                    break;
                case R.id.mao://mão de obra


                    if(!SharedPreferencesHelper.AppModulo.estaMaoDeObraAtivado()){
                        toastMenu.show();
                        break;
                    }


                    try {
                        IntentParameters objJson = new IntentParameters(getStr(R.string.OPTION_MENU_MAO), intentParameters.getUserSession());
                        refreshIntentParameters(objJson);
                        verifyLogin(MaoObraServicoGridActivity.class);
                    } catch (Exception e) {
                        tratarExcecao(e);
                    }
                    break;
                case R.id.absTemp:
                    try {
                        IntentParameters objJson = new IntentParameters(getStr(R.string.EMPTY), intentParameters.getUserSession());
                        refreshIntentParameters(objJson);
                        redirect(AbastecimentoTempActivity.class, true);
                    } catch (Exception e) {
                        tratarExcecao(e);
                    }
                    break;

                case R.id.plu:


                    if(!SharedPreferencesHelper.AppModulo.estaIndicesPluviometricosAtivado()){
                        toastMenu.show();
                        break;
                    }


                    try {
                        checkLocal(); // Verifica se a localização foi informada.
                        IntentParameters objJson = new IntentParameters(getStr(R.string.OPTION_MENU_PLU), intentParameters.getUserSession());
                        refreshIntentParameters(objJson);
                        redirect(IndicePluviometricoServicoGridActivity.class, true);
                    } catch (Exception e) {
                        tratarExcecao(e);
                    }
                    break;
                case R.id.local:
                    try {
                        IntentParameters objJson = new IntentParameters(getStr(R.string.EMPTY), intentParameters.getUserSession());
                        refreshIntentParameters(objJson);
                        redirect(LocalizacaoActivity.class, true);
                    } catch (Exception e) {
                        tratarExcecao(e);
                    }
                    break;
                case R.id.logout:
                    try {
                        if (intentParameters.getUserSession() != null) {
                            logout();
                            refreshConfig();
                            redirectPrincipal();
                        } else {
                            throw new AlertException(getStr(R.string.ALERT_USUARIO_NAO_LOGADO));
                        }

                    } catch (Exception e) {
                        tratarExcecao(e);
                    }
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            tratarExcecao(e);
        }

        return super.onOptionsItemSelected(item);

    }

    public boolean changePkParteDiaria() {
        String[] dados = getDAO().getApropriacaoEquipamentoDAO().getPk(idEquipamento);
        boolean exists = (dados != null);

        if (exists) {
            idApropriacao = (dados[0] != null) ? Integer.valueOf(dados[0]) : null;
            idEquipamento = (dados[1] != null) ? Integer.valueOf(dados[1]) : null;
            dataHora = dados[2];
        }
        return exists;
    }

    public DAOFactory getDAO() {
        if (currentContext == null) {
            currentContext = this;
        }
        return DAOFactory.getInstance(currentContext);
    }


    public void checkLocal() throws Exception {
        String[] dados = getDAO().getLocalizacaoDAO().getValues();
        if (dados == null) {
            throw new AlertException(getStr(R.string.ALERT_LOCATION));
        }
    }

    public void checkUserLogged() throws Exception {
        ConfiguracoesVO configTemp = getDAO().getConfiguracoesDAO().getConfiguracaoVO();
        if (configTemp.getNomeUsuario() == null || configTemp.getNomeUsuario().isEmpty() || configTemp.getNomeUsuario().equals("Não informado")) {
            throw new AlertException(getStr(R.string.ALERT_USER_NOT_LOGGED));
        }
    }

    public void checkPosto() throws Exception {
        config = getDAO().getConfiguracoesDAO().getConfiguracaoVO();
        if (config.getIdPosto() == null || config.getIdPosto() == 0) {
            throw new AlertException(getStr(R.string.ALERT_STATION));
        }
    }

    public void verifyLogin(Class<?> classRedirect) {
        if (intentParameters.getUserSession() == null)
            redirect(LoginActivity.class, true);
        else
            redirect(classRedirect, false);
    }

    public void login() throws Exception {
        intentParameters.setUserSession(userRequest);
        refreshIntentParameters(intentParameters);
        refreshConfig();
    }

    public void logout() throws Exception {
        intentParameters.setUserSession(null);
        //intentParameters.setMenu(getStr(R.string.EMPTY));
        refreshIntentParameters(intentParameters);
        refreshUserRequest();
        refreshConfig();
    }

    public void refreshConfig() throws Exception {

        config = getDAO().getConfiguracoesDAO().getConfiguracaoVO();

        if (userRequest != null) {
            config.setCodUsuario(userRequest.getId());
            config.setIdUsuario(userRequest.getIdUsuario());
            config.setIdUsuarioPessoal(userRequest.getIdUsuarioPessoal());
        }

        SharedPreferencesHelper.Configuracao.setCodUsuario(config.getCodUsuario());
        SharedPreferencesHelper.Configuracao.setIdUsuario(config.getIdUsuario());
        SharedPreferencesHelper.Configuracao.setIdUsuarioPessoal(config.getIdUsuarioPessoal());

        //String strJson = Util.GSON_API.toJson(config);
        //String strPath = SD_CARD + getStr(R.string.DIR_CONFIG);
        //String nameFile = getStr(R.string.FILE_CONFIG);
        //File path = new File(strPath);
        //File file = new File(path, nameFile);
        //FileWriterWithEncoding fw = new FileWriterWithEncoding(file, getStr(R.string.UTF_8));
        //strJson = strJson.replaceAll("'", "");
        //fw.write(strJson);
        //fw.flush();
        //fw.close();

        //getDAO().getConfiguracoesDAO().loadConfig(true);
        getDAO().getConfiguracoesDAO().loadConfig();
    }



    //**********IMPORTACAO E EXPORTACAO DE DADOS*****************************************************

    /*
    public ExportMobileDate exportInfos(String date) throws Exception {

        Query queryMOV = getDAO().getUtilDAO().getQueryMovimentacoesExportByDate(date);
        Query queryEQP = getDAO().getUtilDAO().getQueryParteDiariaExportByDate(date);
        Query queryABS = getDAO().getUtilDAO().getQueryAbastecimentoLubrificacaoExportByDate(date);
        Query queryMAN = getDAO().getUtilDAO().getQueryManutencaoExportByDate(date);


        ExportMobileDate objJSon = getDAO().getUtilDAO().getObjJsonDate(
                new Query[]{queryMOV, queryEQP, queryABS,queryMAN},
                new String[]{getStr(R.string.OPTION_MENU_MOV), getStr(R.string.OPTION_MENU_EQP), getStr(R.string.OPTION_MENU_ABS),getStr(R.string.OPTION_MENU_MAN)}
        );

        //valida usuario preenchido para apropriacoes
        validaUsuarioConfig(objJSon);

        preencheUsuario();

        objJSon.setConfiguracoes(config);
        objJSon.setSaldoPosto(getDAO().getAbastecimentoPostoDAO().getSaldoPostoVO(config.getIdObra()));
        objJSon.setIndicesPluviometricos(getDAO().getIndicePluviometricoDAO().findAllItems());

        objJSon.setDispositivo(config.getDispositivo());
        objJSon.setCcObra(config.getIdObra().toString());
        objJSon.setDate(date);

        return objJSon;
    }
*/

    /*
    public boolean sendInfos(ExportMobileDate exmd) throws Exception {
        String json = new Gson().toJson(exmd);

        String server = getStr(R.string.URL_SERVER).replaceAll(getStr(R.string.TOKEN), config.getServidor());
        String response = makeRequest(server, json);

        if(response.equals("200")) {
            return true;
        } else {
            return false;
        }
    }
    */

    public boolean isOnline() {

        WifiManager wfManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if (wfManager.isWifiEnabled()) {

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }

    /*
    public static String makeRequest(String uri, String json) {
        try {
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();

            return String.valueOf(statusCode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    */

    /**
     * Pings a HTTP URL. This effectively sends a HEAD request and returns <code>true</code> if the response code is in
     * the 200-399 range.
     // @param url The HTTP URL to be pinged.
     // @param timeout The timeout in millis for both the connection timeout and the response read timeout. Note that
     * the total timeout is effectively two times the given timeout.
     * @return <code>true</code> if the given HTTP URL has returned response code 200-399 on a HEAD request within the
     * given timeout, otherwise <code>false</code>.
     */

    /*
    public static boolean ping(String url, int timeout) {
        url = url.replaceFirst("https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.

        try {
            HttpURLConnection connection = (HttpURLConnection) new java.net.URL(url).openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return (200 <= responseCode && responseCode <= 399);
        } catch (IOException e) {
            return false;
        }
    }
    */
    //***********************************************************************************************




    //*********TEMP DE ABASTECIMENTO*****************************************************************

    private void verificaAbastecimentoTempFile() throws Exception {

        File file = getAbsTempFile();
        //File file = new File(AppDirectory.PATH_TEMP+"/"+AppDirectory.TEMP_ABASTECIMENTO_NMARQUIVO);

        boolean exists = file.exists();

        if (intentParameters.getMenu().equals(getStr(R.string.OPTION_MENU_ABS))) {

            if (exists) {

                Reader reader = new FileReader(file);
                AbastecimentoJson vo = new Gson().fromJson(reader, AbastecimentoJson.class);

                if (vo != null) {

                    //se usuario que iniciou o abastecimento é o mesmo que esta logado e o posto é o mesmo
                    if (vo.getCabecalho().getAbastecedor().getId().intValue() == userRequest.getId().intValue()
                            && vo.getRae().getPosto().getId().intValue() == config.getIdPosto().intValue())
                        alertaAbastecimentoTempFile();
                    else
                        redirect(AbastecimentoAuthActivity.class, true);
                } else
                    redirect(AbastecimentoAuthActivity.class, true);
            } else
                redirect(AbastecimentoAuthActivity.class, true);
        }

    }

    private void alertaAbastecimentoTempFile() {

        AlertDialog.Builder dialogo = new AlertDialog.Builder(currentContext);
        dialogo.setMessage(getStr(R.string.ALERT_TEMP_FILE));
        dialogo.setPositiveButton(getStr(R.string.SIM), new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface di, int arg) {
                        redirect(AbastecimentoActivity.class, false);
                    }
                });
        dialogo.setNegativeButton(getStr(R.string.NAO), new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface di, int arg) {
                        redirect(AbastecimentoAuthActivity.class, false);
                    }
                });
        dialogo.setTitle(getStr(R.string.AVISO));
        dialogo.show();
    }

    public File getAbsTempFile() throws Exception {
        //Object[] dados = Util.getPathTemp(currentContext);
        //return new File((File) dados[0], (String) dados[1]);
        return new File(AppDirectory.PATH_TEMP+"/"+AppDirectory.TEMP_ABASTECIMENTO_NMARQUIVO);
    }


    public AbastecimentoJson getAbsTempVO() throws Exception {
        File file = getAbsTempFile();
        //File file = new File(AppDirectory.PATH_TEMP+"/"+"temp_abs.json");
        Reader reader = new FileReader(file);
        return new Gson().fromJson(reader, AbastecimentoJson.class);
    }



    //***********************************************************************************************

    public boolean validaCentroCusto(Integer ccObra) {

        return getDAO().getObraDAO().getById(ccObra) != null;
    }


    public void hiddenKeyboard(View... views) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        for (View view : views) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    public void tratarExcecao(Exception e) {

        e.printStackTrace();

        String errorMsg = e.getMessage() != null && !e.getMessage().isEmpty() ? e.getMessage() : getStr(R.string.ERROR_INESPERADO)+"\n"+getStackTrace(e.getStackTrace());

        if (e instanceof AlertException)
            Util.viewMessage(currentContext, errorMsg);
        else
            Util.viewErrorMessage(currentContext, errorMsg);

    }

    public String getStackTrace(StackTraceElement[] stack){
        StringBuilder stackContent = new StringBuilder();
        stackContent.append("\nTrecho inicial da pilha:\n");
        for(int i = 0; i < 3; i++){
            stackContent.append(stack[i]);
            stackContent.append("\n");
        }
        stackContent.append(".........");
        return stackContent.toString();
    }


    public void tratarExcecaoRedirecionando(Exception e) {

        e.printStackTrace();

        String errorMsg = e.getMessage() != null && !e.getMessage().isEmpty() ? e.getMessage() : getStr(R.string.ERROR_INESPERADO)+"\n\n "+e.getMessage()+" "+e.getCause();

        if (e instanceof AlertException) {
            if (!e.getMessage().equals(getStr(R.string.ALERT_USER_NOT_LOGGED))) {
                AlertDialog.Builder dialogo = new AlertDialog.Builder(currentContext);
                dialogo.setTitle(currentContext.getResources().getString(R.string.msg_aviso));
                dialogo.setMessage(errorMsg);
                dialogo.setPositiveButton(currentContext.getResources().getString(R.string.msg_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        redirect(LocalizacaoActivity.class, false);
                    }
                });
                dialogo.show();
            } else if (e.getMessage().equals(getStr(R.string.ALERT_USER_NOT_LOGGED))) {
                Util.viewMessage(currentContext, errorMsg);
            }
        }
    }

    public String getStr(int id) {

        return getResources().getString(id);
    }

    public String[] getStrArray(int id) {

        return getResources().getStringArray(id);
    }

    public int getColor(int id) {
        return getResources().getColor(id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == QR_CODE_REQUEST) {

            try {
                idEquipamento = Util.getQRCodeId(this, intent);

                if (idEquipamento == null) {
                    throw new AlertException(getStr(R.string.ALERT_SELECT_EQP));
                }

                intentParameters.setIdRegistroPai(String.valueOf(idEquipamento));
                intentParameters.setIdEquipamento(idEquipamento);
                intentParameters.setFromQRCode(true);

                trataDadosQRCode();

            } catch (Exception e) {
                //tratarExcecao(e);
                tratarExcecao(new Exception(getStr(R.string.ERROR_QRCODE_EQUIP_INVALIDO)));
            }

        } else if (requestCode == QR_CODE_FORM_REQUEST) {
            try {
                QRCodeData data = Util.getQRCodeData2(this, intent);

                if(data.getTipoModulo().equals(TipoModulo.FICHA_3_VIAS)) {
                    ArrayList<String> infos = Util.getQRCodeInfos(this, intent);

                    // E-ticket-carga ; E-ticket-descarga ; nroFicha (codigo 6 digitos atrelado ao qrcode)
                    FormMovimentacaoVO formMovVO = new FormMovimentacaoVO();
                    // Tipo de Modulo
                    formMovVO.setTipoModulo(data.getTipoModulo());
                    // Carga
                    formMovVO.seteTicketCarga(infos.get(2));
                    // Descarga
                    formMovVO.seteTicketDescarga(infos.get(3));
                    // Numero da Ficha
                    formMovVO.setNroFicha(infos.get(4));

                    trataDadosQRCodeForm(formMovVO);

                } else if(data.getTipoModulo().equals(TipoModulo.FICHA_MOTORISTA)) {
                    ArrayList<String> infos = Util.getQRCodeInfos(this, intent);

                    // E-ticket-carga ; Codigo-segurança-carga ; E-ticket-descarga ; Codigo-segurança-descarga ; numeroQrCode (0-30) ; nroForm
                    FormMovimentacaoVO formMovVO = new FormMovimentacaoVO();
                    // Tipo de Modulo
                    formMovVO.setTipoModulo(data.getTipoModulo());
                    // Carga
                    formMovVO.seteTicketCarga(infos.get(2));
                    formMovVO.setCodSegurancaCarga(Integer.parseInt(infos.get(3)));
                    // Descarga
                    formMovVO.seteTicketDescarga(infos.get(4));
                    formMovVO.setCodSegurancaDescarga(Integer.parseInt(infos.get(5)));

                    formMovVO.setNroFormulario(Integer.parseInt(infos.get(7)));
                    formMovVO.setNroQRCode(Integer.parseInt(infos.get(6)));

                    trataDadosQRCodeForm(formMovVO);
                }else{

                    AlertDialog.Builder dialogo = new AlertDialog.Builder(currentContext);
                    dialogo.setMessage("Este não é um QR CODE válido para Controle diário de Movimentação de Carga. Tente Novamente!");
                    dialogo.setPositiveButton(getStr(R.string.OK), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface di, int arg) {
                        }
                    });
                    dialogo.setTitle(getStr(R.string.AVISO));
                    dialogo.show();
                }
            } catch (Exception e) {
                tratarExcecao(new Exception(getStr(R.string.ERROR_QRCODE_FORM_INVALIDO)));
            }
        }
    }

    //metodo que deve ser sobrescrito pelas classes responsaveis por tratar QR Code
    protected void trataDadosQRCode() throws AlertException {
    }

    //metodo que deve ser sobrescrito pelas classes responsaveis por tratar QR Code do Formulario
    protected void trataDadosQRCodeForm(FormMovimentacaoVO formMovVO) throws AlertException {
    }




    //METODOS INUTEIS********************************************************************



    /*
    public static String getDataFromGetRequest(String url) {

        try {
            HttpParams httpParameters = new BasicHttpParams();

            // Set the timeout in milliseconds until a connection is established.
            // The default value is zero, that means the timeout is not used.
            int timeoutConnection = 5000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

            // Set the default socket timeout (SO_TIMEOUT)
            // in milliseconds which is the timeout for waiting for data.
            int timeoutSocket = 10000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpGet httpget = new HttpGet(url);

            System.out.println(httpget.getURI());

            // connect
            HttpResponse response = httpClient.execute(httpget);

            int statusCode = response.getStatusLine().getStatusCode();

            if(statusCode == 200) {
                // get response
                HttpEntity entity = response.getEntity();

                if (entity == null) {
                    return null;
                }

                // get response content and convert it to json string
                InputStream is = entity.getContent();

                return Util.inputStreamToString(is);

            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    */





    /*
       public String exportJson() throws Exception {

        Object[] dados = Util.getPathExport(currentContext, config);

        File file = new File((File) dados[0], (String) dados[1]);

        Query mov = getDAO().getUtilDAO().getQueryMovimentacoesExport();
        Query eqp = getDAO().getUtilDAO().getQueryParteDiariaExport();
        Query abs = getDAO().getUtilDAO().getQueryAbastecimentoLubrificacaoExport();

        ExportMobile objJSon = getDAO().getUtilDAO().getObjJson(new Query[]{mov, eqp, abs},
                new String[]{getStr(R.string.OPTION_MENU_MOV), getStr(R.string.OPTION_MENU_EQP), getStr(R.string.OPTION_MENU_ABS)});

        //valida usuario preenchido para apropriacoes
        validaUsuarioConfig(objJSon);

        preencheUsuario();

        objJSon.setConfiguracoes(config);
        objJSon.setSaldoPosto(getDAO().getAbastecimentoPostoDAO().getSaldoPostoVO(config.getIdObra()));
        objJSon.setIndicesPluviometricos(getDAO().getIndicePluviometricoDAO().findAllItems());

        String strJson = "";

        try {
            strJson = Util.GSON_API.toJson(objJSon);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FileWriterWithEncoding fw = new FileWriterWithEncoding(file, getStr(R.string.UTF_8));

        strJson = strJson.replaceAll("'", "");

        fw.write(strJson);
        fw.flush();
        fw.close();

        return file.getCanonicalPath();
    }
    */


    /*
    public File getFileExport() throws Exception {
        Object[] dados = Util.getPathExport(currentContext, config);
        return new File((File) dados[0], (String) dados[1]);
    }
    */


    /*
    public ImportMobile getInfos(Context pContext, String ccObra, ConfiguracoesVO conf) throws Exception {

        String server = getStr(R.string.URL_SERVER).replaceAll(getStr(R.string.TOKEN), config.getServidor());

        //String srtJson = getDataFromGetRequest(server + "?ccObra=" + ccObra);
        //ImportMobile vo = new ImportMobile();
        ImportMobile vo = null;


        Map<String,String> params = new HashMap<String, String>();
        params.put("ccObra", "303599");

        AppHTTP http = new AppHTTP();
        vo = http.connect(AppHTTP.GET,"http://mobile.constran.com.br:8080/ServerManagerMobile/Manager",ImportMobile.class,params);

        Log.i("http",vo.toString());



        if(srtJson == null) {
            throw new AlertException(getStr(R.string.ALERT_SERVER_OFFLINE));
        } else {
            vo = new Gson().fromJson(srtJson, ImportMobile.class);
        }


        if (vo.getFrentesObra() == null || vo.getFrentesObra().isEmpty()) {
            throw new AlertException(Util.getMessage(pContext, R.string.obra, R.string.ALERT_IMPORT_SEM_DADOS));
        }

        if (vo.getAtividades() == null || vo.getAtividades().isEmpty()) {
            throw new AlertException(Util.getMessage(pContext, R.string.atividade, R.string.ALERT_IMPORT_SEM_DADOS));
        }

        if (vo.getEquipamentos() == null || vo.getEquipamentos().isEmpty()) {
            throw new AlertException(Util.getMessage(pContext, R.string.equipamento, R.string.ALERT_IMPORT_SEM_DADOS));
        }

        if (vo.getMateriais() == null || vo.getMateriais().isEmpty()) {
            throw new AlertException(Util.getMessage(pContext, R.string.material, R.string.ALERT_IMPORT_SEM_DADOS));
        }

        if (vo.getUsuarios() == null || vo.getUsuarios().isEmpty()) {
            throw new AlertException(Util.getMessage(pContext, R.string.usuario, R.string.ALERT_IMPORT_SEM_DADOS));
        }

        if (vo.getServicos() == null || vo.getServicos().isEmpty()) {
            throw new AlertException(Util.getMessage(pContext, R.string.servico, R.string.ALERT_IMPORT_SEM_DADOS));
        }

        if (vo.getComponentes() == null || vo.getComponentes().isEmpty()) {
            throw new AlertException(Util.getMessage(pContext, R.string.componente, R.string.ALERT_IMPORT_SEM_DADOS));
        }

        if (vo.getParalisacoes() == null || vo.getParalisacoes().isEmpty()) {
            throw new AlertException(Util.getMessage(pContext, R.string.paralisacao, R.string.ALERT_IMPORT_SEM_DADOS));
        }

        //modulo manutencao
        if(SharedPreferencesHelper.AppModulo.estaManutencoesAtivado()){

            if(vo.getEquipamentoCategorias() == null || vo.getEquipamentoCategorias().isEmpty()){
                throw new AlertException(Util.getMessage(pContext, R.string.equipamento_categorias, R.string.ALERT_IMPORT_SEM_DADOS));
            }

            if(vo.getManutencaoServicos() == null || vo.getManutencaoServicos().isEmpty()){
                throw new AlertException(Util.getMessage(pContext, R.string.manutencao_servicos, R.string.ALERT_IMPORT_SEM_DADOS));
            }

            if(vo.getServicosPorCategoriaEquipamento() == null || vo.getServicosPorCategoriaEquipamento().isEmpty()){
                throw new AlertException(Util.getMessage(pContext, R.string.manutencao_servicos, R.string.ALERT_IMPORT_SEM_DADOS));
            }
        }
        //------------------


        for (ObraVO obra : vo.getObras()) {
            if (conf.getIdObra() != null && conf.getIdObra().equals(obra.getId()) || conf.getIdObra2() != null && conf.getIdObra2().equals(obra.getId())) {
                if ("S".equalsIgnoreCase(obra.getUsaOrigemDestino())) {
                    if (vo.getOrigensDestinos() == null || vo.getOrigensDestinos().isEmpty()) {
                        throw new AlertException(Util.getMessage(pContext, R.string.origemDestino, R.string.ALERT_IMPORT_SEM_DADOS));
                    }
                }
            }
        }

        return vo;
    }
    */

    /*
    public void saveImport(ImportMobile pVO, ProgressDialog progress) {

        //6
        incrementProgressBar(progress);
        for (ObraVO vo : pVO.getObras()) {
            getDAO().getObraDAO().save(vo);
        }
        //10
        incrementProgressBar(progress);
        for (FrenteObraVO vo : pVO.getFrentesObra()) {
            getDAO().getFrenteObraDAO().save(vo);
        }
        //14
        incrementProgressBar(progress);
        for (AtividadeVO vo : pVO.getAtividades()) {
            getDAO().getAtividadeDAO().save(vo);
        }

        incrementProgressBar(progress);
        for (MaterialVO vo : pVO.getMateriais()) {
            getDAO().getMaterialDAO().save(vo);
        }

        incrementProgressBar(progress);
        for (UsuarioVO vo : pVO.getUsuarios()) {
            getDAO().getUsuarioDAO().save(vo);
        }

        incrementProgressBar(progress);
        if (pVO.getUsuariosPessoal() != null) {
            for (UsuarioVO vo : pVO.getUsuariosPessoal()) {
                getDAO().getUsuarioDAO().save(vo);
                getDAO().getPessoalDAO().salvar(vo);
            }
        }
        //30
        incrementProgressBar(progress);
        for (ParalisacaoVO vo : pVO.getParalisacoes()) {
            getDAO().getParalisacaoDAO().save(vo);
        }

        incrementProgressBar(progress);
        for (ServicoVO vo : pVO.getServicos()) {
            getDAO().getServicoDAO().save(vo);
        }

        incrementProgressBar(progress);
        for (ComponenteVO vo : pVO.getComponentes()) {
            getDAO().getComponenteDAO().save(vo);
        }

        incrementProgressBar(progress);
        for (EquipamentoVO vo : pVO.getEquipamentos()) {
            getDAO().getEquipamentoDAO().save(vo);
        }

        incrementProgressBar(progress);
        for (OrigemDestinoVO vo : pVO.getOrigensDestinos()) {
            getDAO().getOrigemDestinoDAO().save(vo);
        }
        //50
        incrementProgressBar(progress);
        if (pVO.getCompartimentos() != null) {
            for (CompartimentoVO vo : pVO.getCompartimentos()) {
                getDAO().getCompartimentoDAO().save(vo);
            }
        }

        incrementProgressBar(progress);
        if (pVO.getPostos() != null) {
            for (PostoVO vo : pVO.getPostos()) {
                getDAO().getPostoDAO().save(vo);
            }
        }

        incrementProgressBar(progress);
        if (pVO.getCombustiveis() != null) {
            for (CombustivelLubrificanteVO vo : pVO.getCombustiveis()) {
                getDAO().getCombustivelLubrificanteDAO().save(vo);
            }
        }

        incrementProgressBar(progress);
        if (pVO.getCombustiveisPostos() != null) {
            for (CombustivelPostoVO vo : pVO.getCombustiveisPostos()) {
                getDAO().getCombustivelPostoDAO().save(vo);
            }
        }

        incrementProgressBar(progress);
        if (pVO.getLubrificacoesEquipamento() != null) {

            //DESLIGADO POR ESTAR CAUSANDO ERRO DEVIDO A UMA DIVISAO POR ZERO, ISTO NAO EH NECESSARIO PARA O FUNCIONAMENTO
            //int size = pVO.getLubrificacoesEquipamento().size();
            //int c = size / 10;
            //int maxInc = 0;
            //int i = 0;

            for (LubrificacaoEquipamentoVO vo : pVO.getLubrificacoesEquipamento()) {

                //DESLIGADO POR ESTAR CAUSANDO ERRO DEVIDO A UMA DIVISAO POR ZERO, ISTO NAO EH NECESSARIO PARA O FUNCIONAMENTO
                //i++;
                //if ((i % c == 0) && maxInc < 10) {
                //progress.setProgress(progress.getProgress() + 1);
                //++maxInc;
                //}

                getDAO().getLubrificacaoEquipamentoDAO().save(vo);
            }
        }

        //70
        incrementProgressBar(progress);
        if (pVO.getJustificativasOperador() != null) {
            for (JustificativaOperadorVO vo : pVO.getJustificativasOperador()) {
                getDAO().getJustificativaOperadorDAO().save(vo);
            }
        }

        incrementProgressBar(progress);
        if (pVO.getPreventivas() != null) {
            for (PreventivaEquipamentoVO vo : pVO.getPreventivas()) {
                getDAO().getPreventivaEquipamentoDAO().save(vo);
            }
        }

        // dados modulo servico e mao de obra

        incrementProgressBar(progress);
        if (pVO.getPeriodoHorarioTrabalhos() != null) {
            for (PeriodoHorarioTrabalhoVO vo : pVO.getPeriodoHorarioTrabalhos()) {
                getDAO().getPeriodoHorarioTrabalhoDAO().insert(vo);
            }
        }

        if (pVO.getPrevisaoServicos() != null) {
            for (PrevisaoServicoVO vo : pVO.getPrevisaoServicos()) {
                getDAO().getPrevisaoServicoDAO().insert(vo);
            }
        }

        incrementProgressBar(progress);
        if (pVO.getHorariosTrabalhos() != null) {
            for (HorarioTrabalhoVO vo : pVO.getHorariosTrabalhos()) {
                getDAO().getHorarioTrabalhoDAO().insert(vo);
            }
        }

        if (pVO.getIntegrantesEquipe() != null) {
            for (IntegranteEquipeVO vo : pVO.getIntegrantesEquipe()) {
                Date date = Util.extractSimpleDateFromDB(vo.getDataIngresso());
                vo.setDataIngresso(Util.getDateFormated(date));
                getDAO().getIntegranteEquipeDAO().insert(vo);
            }
        }

        incrementProgressBar(progress);
        if (pVO.getEquipesTrabalhos() != null) {
            for (EquipeTrabalhoVO vo : pVO.getEquipesTrabalhos()) {
                vo.setApropriavel("S");
                getDAO().getEquipeTrabalhoDAO().insert(vo);
            }
        }


        // ------ dados modulo manutencao --------

        //incrementProgressBar(progress);
        if(pVO.getEquipamentoCategorias() != null){
            for(EquipamentoCategoriaVO vo : pVO.getEquipamentoCategorias()){
                getDAO().getEquipamentoCategoriaDAO().save(vo);
            }
        }

        //incrementProgressBar(progress);
        if(pVO.getManutencaoServicos() != null){
            for(ManutencaoServicosVO vo : pVO.getManutencaoServicos()){
                getDAO().getManutencaoServicoDAO().save(vo);
            }
        }

        //incrementProgressBar(progress);
        if(pVO.getServicosPorCategoriaEquipamento() != null){
            for(ManutencaoServicoPorCategoriaEquipamentoVO vo : pVO.getServicosPorCategoriaEquipamento()){
                getDAO().getManutencaoServicoPorCategoriaEquipamentoDAO().save(vo);
            }
        }

        //---------------------------------------------------------

    }
    */


        /*
    public ExportMobileDate exportInfos(String date) throws Exception {

        Query mov = getDAO().getUtilDAO().getQueryMovimentacoesExportByDate(date);
        Query eqp = getDAO().getUtilDAO().getQueryParteDiariaExportByDate(date);
        Query abs = getDAO().getUtilDAO().getQueryAbastecimentoLubrificacaoExportByDate(date);

        ExportMobileDate objJSon = getDAO().getUtilDAO().getObjJsonDate(new Query[]{mov, eqp, abs},
                new String[]{getStr(R.string.OPTION_MENU_MOV), getStr(R.string.OPTION_MENU_EQP), getStr(R.string.OPTION_MENU_ABS)});

        //valida usuario preenchido para apropriacoes
        validaUsuarioConfig(objJSon);

        preencheUsuario();

        objJSon.setConfiguracoes(config);
        objJSon.setSaldoPosto(getDAO().getAbastecimentoPostoDAO().getSaldoPostoVO(config.getIdObra()));
        objJSon.setIndicesPluviometricos(getDAO().getIndicePluviometricoDAO().findAllItems());

        objJSon.setDispositivo(config.getDispositivo());
        objJSon.setCcObra(config.getIdObra().toString());
        objJSon.setDate(date);

        return objJSon;
    }
    */
}
