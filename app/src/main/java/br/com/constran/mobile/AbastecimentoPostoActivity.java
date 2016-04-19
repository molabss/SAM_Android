package br.com.constran.mobile;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import br.com.constran.mobile.exception.AlertException;
import br.com.constran.mobile.model.LogAuditoria;
import br.com.constran.mobile.persistence.dao.LogAuditoriaDAO;
import br.com.constran.mobile.persistence.vo.rae.RaeVO;
import br.com.constran.mobile.persistence.vo.rae.abs.AbastecimentoPostoVO;
import br.com.constran.mobile.persistence.vo.rae.abs.CombustivelLubrificanteVO;
import br.com.constran.mobile.view.AbstractActivity;
import br.com.constran.mobile.view.interfaces.InterfaceEditActivity;
import br.com.constran.mobile.view.interfaces.InterfaceListActivity;
import br.com.constran.mobile.view.screens.Detail;
import br.com.constran.mobile.view.screens.GridBody;
import br.com.constran.mobile.view.screens.GridFooter;
import br.com.constran.mobile.view.screens.GridHeader;
import br.com.constran.mobile.view.util.References;
import br.com.constran.mobile.view.util.Util;

import java.util.List;

public final class AbastecimentoPostoActivity extends AbstractActivity implements InterfaceEditActivity, InterfaceListActivity {

    private static final String DIESEL = "Diesel";

    private AutoCompleteTextView combustivelList;
    private EditText quantidade;
    private EditText minutoEdit;
    private EditText horaEdit;
    private Button btAdd;
    private TextView unidadeMedida;

    private ScrollView scrollView;
    private ScrollView scrollViewAbs7;
    private ScrollView scrollViewAbs8;

    private TextView postoView;
    private EditText totalizaIniEdit;
    private EditText totalizaFimEdit;
    private ImageView btnSalvar;

    LogAuditoriaDAO logDAO;
    LogAuditoria log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.abastecimento_posto_body);

        currentClass = AbastecimentoPostoActivity.class;
        currentContext = AbastecimentoPostoActivity.this;

        try {
            init();

            initAtributes();

            editValues();

            initEvents();

            createDetail(getDetailValues());

            createGridHeader(getGridHeaderTopValues());
            createGridHeader(getGridHeaderValues());
            createGridFooter(getGridFooterValues());

            createGridHeader(getGridHeaderTopValues2());
            createGridHeader(getGridHeaderValues2());
            createGridFooter(getGridFooterValues2());

            this.createGridBody(getGridBodyValues());
            super.createGridBody(getGridBodyValues2());

            editScreen();

        } catch (Exception e) {
            tratarExcecao(e);
        }
    }

    @Override
    public void initAtributes() throws Exception {

        combustivelList = (AutoCompleteTextView) findViewById(R.id.absComLubr);
        quantidade = (EditText) findViewById(R.id.absQtd);
        postoView = (TextView) findViewById(R.id.absPrefixo);
        unidadeMedida = (TextView) findViewById(R.id.absUN);
        btAdd = (Button) findViewById(R.id.btAdd);
        horaEdit = (EditText) findViewById(R.id.hor);
        minutoEdit = (EditText) findViewById(R.id.min);
        totalizaIniEdit = (EditText) findViewById(R.id.totaliza_ini);
        totalizaFimEdit = (EditText) findViewById(R.id.totaliza_fim);
        btnSalvar = (ImageView) findViewById(R.id.saveTotalizadorImage);

        //cria os objetos do ScrollView usado
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollViewAbs7 = (ScrollView) findViewById(R.id.scrollViewAbs7);
        scrollViewAbs8 = (ScrollView) findViewById(R.id.scrollViewAbs8);

        //Forma de corrigir o bug do Android, quando um ScrollView está dentro de outro, deixando o filho não funcional
        //Solution found here: http://stackoverflow.com/questions/17671123/scrollview-inside-a-scrollview-in-android-issue
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                findViewById(R.id.scrollViewAbs7).getParent().requestDisallowInterceptTouchEvent(false);
                findViewById(R.id.scrollViewAbs8).getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        scrollViewAbs7.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of
                // child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        scrollViewAbs8.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of
                // child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }

    @Override
    public void editValues() throws Exception {

        config = getDAO().getConfiguracoesDAO().getConfiguracaoVO();


        log = new LogAuditoria("ABASTECIMENTO",config.getDispositivo());
        logDAO = getDAO().getLogAuditoriaDAO();
        logDAO.setLogPropriedades(log);

        combustivelArrayVO = getDAO().getCombustivelLubrificanteDAO().getArrayCombustivelLubrificanteVO(config.getIdPosto());

        ArrayAdapter<CombustivelLubrificanteVO> adp = new ArrayAdapter<CombustivelLubrificanteVO>(this, android.R.layout.select_dialog_singlechoice, combustivelArrayVO);
        adp.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        combustivelList.setAdapter(adp);

        Typeface font = Typeface.createFromAsset(getAssets(), getStr(R.string.FONT_MONACO));

        posto = getDAO().getPostoDAO().getById(config.getIdPosto());

        postoView.setTypeface(font, Typeface.BOLD);
        postoView.setText(posto.getDescricao());
        postoView.setPadding(0, 0, 25, 0);

        String[] values = getDAO().getRaeDAO().getValues(new String[]{Util.getToday(), String.valueOf(posto.getId())});

        if (values != null && values.length > 1) {
            totalizaIniEdit.setText(values[0] == null ? getStr(R.string.EMPTY) : values[0]);
            totalizaFimEdit.setText(values[1] == null ? getStr(R.string.EMPTY) : values[1]);
        }

    }

    @Override
    public void initEvents() throws Exception {

        combustivelList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                combustivelLubrificante = (CombustivelLubrificanteVO) parent.getItemAtPosition(position);
                unidadeMedida.setText(combustivelLubrificante.getUnidadeMedida());
                quantidade.requestFocus();

            }

        });

        combustivelList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                combustivelLubrificante = null;
                unidadeMedida.setText(getStr(R.string.EMPTY));
                combustivelList.requestFocus();
            }
        });

        combustivelList.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                combustivelLubrificante = null;
                unidadeMedida.setText(getStr(R.string.EMPTY));
                combustivelList.requestFocus();
                return false;
            }
        });


        btAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {

                    validateFields();

                    save();

                    redirect(currentClass, false);

                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }
        });


        combustivelList.setOnKeyListener(combustivelListener());

        horaEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 2) {
                    minutoEdit.requestFocus();
                    horaEdit.clearFocus();
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


        minutoEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 2) {
                    minutoEdit.clearFocus();
                    combustivelList.requestFocus();
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

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarTotalizador();

                logDAO.insereLog("totalizado foi salvo");
            }
        });
    }

    private void salvarTotalizador() {
        try {
            String totalizadorInicial = totalizaIniEdit.getText().toString().trim();
            String totalizadorFinal = totalizaFimEdit.getText().toString().trim();

            validaTotalizadores(totalizadorInicial, totalizadorFinal);

            RaeVO raeVO = new RaeVO();
            raeVO.setData(Util.getToday());
            raeVO.setPosto(posto);
            raeVO.setTotalizadorInicial(totalizadorInicial);
            raeVO.setTotalizadorFinal(totalizadorFinal);

            Integer idRae = getDAO().getRaeDAO().getIdRae(raeVO);

            if (idRae == null) {
                idRae = getDAO().getRaeDAO().getIdRae(raeVO);
            }

            raeVO.setId(idRae);
            getDAO().getRaeDAO().save(raeVO);

            Toast.makeText(this, getStr(R.string.ALERT_SUCESS_UPDATE), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            tratarExcecao(e);
        }
    }

    private void validaTotalizadores(String totalizadorInicial, String totalizadorFinal) throws AlertException {
        if (totalizadorInicial.isEmpty()) {
            throw new AlertException(Util.getMessage(currentContext, getStr(R.string.totaliz_inicial), R.string.ALERT_REQUIRED));
        }

        if (!totalizadorFinal.isEmpty()) {
            Double t1 = Double.parseDouble(totalizadorInicial);
            Double t2 = Double.parseDouble(totalizadorFinal);

            if (t1 > t2) {
                throw new AlertException(getStr(R.string.ALERT_TOTALIZADOR_FIM_INVALIDO));
            }
        }

    }

    protected OnKeyListener combustivelListener() {
        return new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                combustivelLubrificante = null;
                unidadeMedida.setText(getStr(R.string.EMPTY));
                return false;
            }
        };
    }


    @Override
    public GridBody getGridBodyValues() throws Exception {
        Integer noContent = null;

        Integer indexCursorIdPosto = 0;
        Integer indexCursorIdCombustivelLubrificante = 1;
        Integer indexCursorData = 2;
        Integer indexCursorHora = 3;
        Integer indexCursorDescricao = 4;
        Integer indexCursorQuantidade = 5;
        Integer indexCursorTipo = 6;

        Integer indexColumnNoShow = 3;
        String valueNoShow = "L";

        GridBody grid = new GridBody(this);
        grid.setIndexValueNoShow(indexCursorTipo);
        grid.setIndexColumnNoShow(indexColumnNoShow);
        grid.setValueNoShow(valueNoShow);
        grid.setClassRefresh(AbastecimentoPostoActivity.class);
        grid.setClassRedirect(AbastecimentoPostoTransfActivity.class);
        grid.setClassVO(AbastecimentoPostoVO.class);
        grid.setCursor(getCursor());
        grid.setColumnsTxt(new Integer[]{indexCursorHora, indexCursorDescricao, indexCursorQuantidade, noContent, noContent, noContent});
        grid.setReferencesImage(new Integer[]{noContent, noContent, noContent, R.drawable.edit, R.drawable.delete, R.drawable.transfer});
        grid.setIndexsPKRow(new Integer[]{indexCursorIdPosto, indexCursorIdCombustivelLubrificante, indexCursorData, indexCursorHora});
        grid.setColorTXT(getColor(R.color.BLACK));
        grid.setColorsBKG(new Integer[]{getColor(R.color.WHITE), getColor(R.color.GRAY3)});
        grid.setFileLayoutRow(References.GRID_LAYOUT_ABS7);
        grid.setIdsXmlColumn(References.GRID_ID_COLUMNS_ABS7);
        grid.setIdTable(References.GRID_ID_ABS7);
        grid.setTypesColumn(new String[]{getStr(R.string.TYPE_VIEW), getStr(R.string.TYPE_VIEW), getStr(R.string.TYPE_VIEW),
                getStr(R.string.TYPE_EDIT), getStr(R.string.TYPE_REMOVE), getStr(R.string.TYPE_VIEW)});
        return grid;
    }


    @Override
    protected boolean isColumnNoShow(GridBody gridBody, int numColumn) {
        if (gridBody.getIdsXmlColumn().length >= numColumn && gridBody.getIdsXmlColumn()[numColumn].equals(R.id.columnAbs6)) {
            String valorAbastecido = gridBody.getCursor().getString(numColumn);
            return !valorAbastecido.startsWith("-");

        } else {
            return super.isColumnNoShow(gridBody, numColumn);
        }
    }

    @Override
    public GridHeader getGridHeaderValues() throws Exception {
        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.GRAY));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.HEADER_LAYOUT_ABS7); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.HEADER_ID_COLUMNS_ABS7);// Ids (xml) das colunas
        header.setIdTable(References.HEADER_ID_ABS7); //Id do TableLayout);
        header.setNameColumns(getStrArray(R.array.HEADER_NAME_COLUMNS_ABS7));//Nomes das Colunas
        return header;
    }


    @Override
    public GridFooter getGridFooterValues() throws Exception {
        GridFooter footer = new GridFooter(this);
        footer.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        footer.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        footer.setFileLayoutRow(References.FOOTER_LAYOUT_DEFAULT); //arquivo xml - layout (TableRow)
        footer.setIdColumns(References.FOOTER_ID_COLUMNS);// Ids (xml) das colunas
        footer.setIdTable(References.FOOTER_ID_ABS7); //Id do TableLayout);
        return footer;
    }


    public GridBody getGridBodyValues2() throws Exception {
        Integer indexCursorDescricao = 0;
        Integer indexCursorAbastecido = 1;
        Integer indexCursorSaldo = 2;

        GridBody grid = new GridBody(this);
        grid.setColorsBKG(new Integer[]{getColor(R.color.WHITE), getColor(R.color.GRAY3)});
        grid.setColorTXT(getColor(R.color.BLACK));
        grid.setCursor(getCursor2());
        grid.setColumnsTxt(new Integer[]{indexCursorDescricao, indexCursorAbastecido, indexCursorSaldo});
        grid.setTypesColumn(new String[]{getStr(R.string.TYPE_VIEW), getStr(R.string.TYPE_VIEW), getStr(R.string.TYPE_VIEW)});
        grid.setFileLayoutRow(References.GRID_LAYOUT_ABS8);
        grid.setIdsXmlColumn(References.GRID_ID_COLUMNS_ABS8);
        grid.setIdTable(References.GRID_ID_ABS8);
        return grid;
    }

    public GridHeader getGridHeaderValues2() throws Exception {
        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.GRAY));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.HEADER_LAYOUT_ABS8); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.HEADER_ID_COLUMNS_ABS8);// Ids (xml) das colunas
        header.setIdTable(References.HEADER_ID_ABS8); //Id do TableLayout);
        header.setNameColumns(getStrArray(R.array.HEADER_NAME_COLUMNS_ABS8));//Nomes das Colunas

        return header;

    }


    public GridFooter getGridFooterValues2() throws Exception {
        GridFooter footer = new GridFooter(this);
        footer.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        footer.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        footer.setFileLayoutRow(References.FOOTER_LAYOUT_DEFAULT); //arquivo xml - layout (TableRow)
        footer.setIdColumns(References.FOOTER_ID_COLUMNS);// Ids (xml) das colunas
        footer.setIdTable(References.FOOTER_ID_ABS8); //Id do TableLayout);
        return footer;
    }


    @Override
    public Detail getDetailValues() throws Exception {

        //Texto do detail
        String strDetail = getStr(R.string.DETAIL_ABS);

        Detail detail = new Detail(this);
        detail.setDetail(strDetail);
        detail.setColorBKG(getColor(R.color.GRAY));// Cor de fundo
        detail.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        detail.setFileLayoutRow(References.DETAIL_LAYOUT); //arquivo xml - layout (TableRow)
        detail.setIdColumns(References.DETAIL_ID_COLUMNS);// Ids (xml) das colunas
        detail.setIdTable(References.DETAIL_ID_EQP); //Id do TableLayout);

        return detail;
    }


    @Override
    public void onBackPressed() {

        redirect(AbastecimentosSearchActivity.class, false);
    }


    @Override
    public GridHeader getGridHeaderTopValues() {

        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.TOP_HEADER_LAYOUT_ABS7); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.TOP_HEADER_ID_COLUMN);// Ids (xml) das colunas
        header.setIdTable(References.TOP_HEADER_ID_ABS7); //Id do TableLayout);
        header.setNameColumns(getStrArray(R.array.TOP_HEADER_NAME_COLUMNS_ABS7));//Nomes das Colunas


        return header;

    }

    @Override
    public void validateFields() throws Exception {

        String hora = horaEdit.getText().toString().trim();
        String minutos = minutoEdit.getText().toString().trim();

        if (hora.equals(getStr(R.string.EMPTY)) || minutos.equals(getStr(R.string.EMPTY))) {
            throw new AlertException(Util.getMessage(currentContext, getStr(R.string.hora), R.string.ALERT_REQUIRED));
        } else if (hora.length() != 2 || minutos.length() != 2) {
            throw new AlertException(Util.getMessage(currentContext, getStr(R.string.hora), R.string.ALERT_HOUR_INVALID));
        } else if (Integer.valueOf(hora) > 23 || Integer.valueOf(minutos) > 59) {
            throw new AlertException(Util.getMessage(currentContext, getStr(R.string.hora), R.string.ALERT_HOUR_INVALID));
        } else if (combustivelLubrificante == null) {
            throw new AlertException(getStr(R.string.ALERT_SELECT_ABS));
        } else if (quantidade.getText().toString().equals(getStr(R.string.EMPTY))) {
            throw new AlertException(getStr(R.string.ALERT_SELECT_QTD));
        } else if (Double.valueOf(quantidade.getText().toString()) == 0.0) {
            throw new AlertException(getStr(R.string.ALERT_QTD_EMPTY));
        } else {
            if (!DIESEL.equalsIgnoreCase(combustivelLubrificante.getDescricao().trim()) &&
                    Double.valueOf(quantidade.getText().toString()) < 0.0) {
                throw new AlertException(getStr(R.string.ALERT_VALOR_NEGATIVO));
            }
        }

        List<AbastecimentoPostoVO> abastecimentos = getDAO().getAbastecimentoPostoDAO().findAbastecimentosByPk(bindAbastecimentoVO());

        //Verifica se já existe um abastecimento neste horário com este combustível/lubrificante
        if (abastecimentos != null && !abastecimentos.isEmpty()) {
            throw new AlertException(getStr(R.string.ALERT_CONFLITO_ABASTECIMENTO));
        }
    }

    @Override
    public void editScreen() throws Exception {
        hiddenKeyboard(horaEdit);
    }

    public void save() throws Exception {
        AbastecimentoPostoVO abs = bindAbastecimentoVO();
        getDAO().getAbastecimentoPostoDAO().save(abs);
        redirect(currentClass, false);
    }

    private AbastecimentoPostoVO bindAbastecimentoVO() {
        horaInicio = horaEdit.getText().toString().concat(getStr(R.string.TWO_DOTS)).concat(minutoEdit.getText().toString());

        AbastecimentoPostoVO abs = new AbastecimentoPostoVO();

        abs.setCombustivelLubrificante(combustivelLubrificante);
        abs.setData(Util.getToday());
        abs.setHora(horaInicio);
        abs.setPosto(posto);
        abs.setQtd(quantidade.getText().toString().trim());
        return abs;
    }

    public GridHeader getGridHeaderTopValues2() {
        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.TOP_HEADER_LAYOUT_ABS8); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.TOP_HEADER_ID_COLUMN);// Ids (xml) das colunas
        header.setIdTable(References.TOP_HEADER_ID_ABS8); //Id do TableLayout);
        header.setNameColumns(getStrArray(R.array.TOP_HEADER_NAME_COLUMNS_ABS8));//Nomes das Colunas
        return header;
    }

    @Override
    public Cursor getCursor() throws Exception {
        return getDAO().getAbastecimentoPostoDAO().getCursor(config.getIdPosto());
    }

    public Cursor getCursor2() throws Exception {
        return getDAO().getAbastecimentoPostoDAO().getCursor2(config.getIdPosto());
    }
}


