package br.com.constran.mobile;

import android.database.Cursor;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.text.method.TextKeyListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;
import br.com.constran.mobile.persistence.vo.imp.MaterialVO;
import br.com.constran.mobile.view.AbstractActivity;
import br.com.constran.mobile.view.interfaces.InterfaceEditActivity;
import br.com.constran.mobile.view.interfaces.InterfaceListActivity;
import br.com.constran.mobile.view.screens.Detail;
import br.com.constran.mobile.view.screens.GridBody;
import br.com.constran.mobile.view.screens.GridFooter;
import br.com.constran.mobile.view.screens.GridHeader;
import br.com.constran.mobile.view.util.References;
import br.com.constran.mobile.view.util.Util;

public final class ViagensSearchActivity extends AbstractActivity implements InterfaceEditActivity, InterfaceListActivity {

    private static final String FILTRO_PREFIXO = "Prefixo";
    private static final String FILTRO_MATERIAL = "Material";
    private static final String FILTRO_ESTACA = "Estaca";
    private static final int INDEX_PREFIXO = 0;
    private static final int INDEX_ESTACA = 1;
    private static final int INDEX_MATERIAL = 2;

    private AutoCompleteTextView equipamentoList;
    private AutoCompleteTextView dataList;
    private Button btSearch;
    private TextView total;
    private Cursor cursor;

    private Spinner tipoFiltroSpinner;
    private String[] tipoFiltros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.viagens_search_body);

        currentClass = ViagensSearchActivity.class;
        currentContext = ViagensSearchActivity.this;

        try {

            init();

            initAtributes();

            editValues();

            initEvents();

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

        equipamentoList = (AutoCompleteTextView) findViewById(R.id.vgsCmbEqp);
        dataList = (AutoCompleteTextView) findViewById(R.id.vgsCmbDt);
        btSearch = (Button) findViewById(R.id.btSearch);
        total = (TextView) findViewById(R.id.vgsTotal);
        tipoFiltroSpinner = (Spinner) findViewById(R.id.tipoFiltroSpinner);
        tipoFiltros = getResources().getStringArray(R.array.filtrosBuscaMovimentacao);

    }

    @Override
    public void editValues() throws Exception {

        idEquipamento = intentParameters.getFiltroEquipamento();
        idMaterial = intentParameters.getFiltroMaterial();
        estaca = intentParameters.getFiltroEstaca();

        dataHora = (intentParameters.getFiltroData() != null) ? intentParameters.getFiltroData() : Util.getToday();

        equipamentoArrayVO = getDAO().getEquipamentoDAO().getArrayEquipamentosMovimentacaoConsulta(dataHora);

        ArrayAdapter<EquipamentoVO> adp = new ArrayAdapter<EquipamentoVO>(this, android.R.layout.select_dialog_singlechoice, equipamentoArrayVO);
        adp.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        equipamentoList.setAdapter(adp);

        if (idEquipamento != null) {
            equipamentoList.setText(getDAO().getEquipamentoDAO().getDescricao(idEquipamento, false, true));
        }

        tipoFiltroSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, tipoFiltros));

        preencherCamposFiltrados(adp);

        dataArray = getDAO().getViagemDAO().getArrayDatas();

        ArrayAdapter<String> adD = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, dataArray);
        adD.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        dataList.setAdapter(adD);

        dataList.setText(dataHora);

        cursor = getCursor();
    }

    private void preencherCamposFiltrados(ArrayAdapter<EquipamentoVO> adp) {
        if (idEquipamento != null) {
            equipamentoList.setText(getDAO().getEquipamentoDAO().getDescricao(idEquipamento, false, true));
            tipoFiltroSpinner.setSelection(INDEX_PREFIXO);
        }

        if (idMaterial != null) {
            tipoFiltroSpinner.setSelection(INDEX_MATERIAL);
            materialArrayVO = getDAO().getMaterialDAO().getArrayEquipamentosMovimentacaoConsulta(dataHora);
            ArrayAdapter<MaterialVO> adp2 = new ArrayAdapter<MaterialVO>(ViagensSearchActivity.this, android.R.layout.select_dialog_singlechoice, materialArrayVO);
            adp.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            equipamentoList.setAdapter(adp2);

            for (MaterialVO material : materialArrayVO) {
                if (idMaterial.equals(material.getId())) {
                    equipamentoList.setText(material.getDescricao());
                    break;
                }
            }
        }

        if (estaca != null) {
            tipoFiltroSpinner.setSelection(INDEX_ESTACA);
            ArrayAdapter<String> adp2 = new ArrayAdapter<String>(ViagensSearchActivity.this, android.R.layout.select_dialog_singlechoice, new String[]{""});
            adp.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            equipamentoList.setAdapter(adp2);
            equipamentoList.setText(estaca);
        }

        atualizaTipoTeclado();
    }

    @Override
    public void initEvents() throws Exception {

        equipamentoList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filtroSelecionado = (String) tipoFiltroSpinner.getSelectedItem();

                if (FILTRO_PREFIXO.equals(filtroSelecionado)) {
                    EquipamentoVO e = (EquipamentoVO) parent.getItemAtPosition(position);
                    intentParameters.setFiltroEquipamento(e.getId());
                    intentParameters.setFiltroMaterial(null);
                    intentParameters.setFiltroEstaca(null);
                } else if (FILTRO_MATERIAL.equals(filtroSelecionado)) {
                    MaterialVO m = (MaterialVO) parent.getItemAtPosition(position);
                    intentParameters.setFiltroMaterial(m.getId());
                    intentParameters.setFiltroEquipamento(null);
                    intentParameters.setFiltroEstaca(null);
                } else {
                    intentParameters.setFiltroEstaca((String) parent.getItemAtPosition(position));
                    intentParameters.setFiltroEquipamento(null);
                    intentParameters.setFiltroMaterial(null);
                }
            }

        });

        equipamentoList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                intentParameters.setFiltroEquipamento(null);
                intentParameters.setFiltroMaterial(null);
                intentParameters.setFiltroEstaca(null);
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
            }
        });

        equipamentoList.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                intentParameters.setFiltroEquipamento(null);
                intentParameters.setFiltroMaterial(null);
                intentParameters.setFiltroEstaca(null);
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));

                atualizaTipoTeclado();

                return false;
            }
        });

        addTipoFiltroSpinner();

        dataList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intentParameters.setFiltroData((String) parent.getItemAtPosition(position));
            }

        });

        dataList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                intentParameters.setFiltroData(getStr(R.string.EMPTY));
                ((AutoCompleteTextView) v).setText(intentParameters.getFiltroData());
            }
        });

        btSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String filtroSelecionado = (String) tipoFiltroSpinner.getSelectedItem();

                if (FILTRO_ESTACA.equals(filtroSelecionado)) {
                    estaca = equipamentoList.getText().toString();
                    intentParameters.setFiltroEstaca("".equals(estaca.trim()) ? null : estaca);
                }

                if (intentParameters.getFiltroData() == null || intentParameters.getFiltroData().equals(getStr(R.string.EMPTY))) {
                    intentParameters.setFiltroData(Util.getToday());
                }
                refreshIntentParameters(intentParameters);
                redirect(currentClass, false);
            }
        });

        equipamentoList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                intentParameters.setFiltroEquipamento(null);
                intentParameters.setFiltroMaterial(null);
                intentParameters.setFiltroEstaca(null);
                equipamentoList.setText(getStr(R.string.EMPTY));

                return false;
            }
        });
    }

    private void atualizaTipoTeclado() {
        String tipoFiltro = (String) tipoFiltroSpinner.getSelectedItem();

        if (FILTRO_ESTACA.equals(tipoFiltro)) {
            equipamentoList.setKeyListener(new DigitsKeyListener());
        } else {
            equipamentoList.setKeyListener(new TextKeyListener(TextKeyListener.Capitalize.NONE, true));
        }
    }

    private void addTipoFiltroSpinner() {
        //atrasando o inicio do listener para nao atrapalhar o modo de edicao
        tipoFiltroSpinner.post(new Runnable() {
            @Override
            public void run() {
                tipoFiltroSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                        String item = (String) parent.getSelectedItem();

                        dataHora = (intentParameters.getFiltroData() != null) ? intentParameters.getFiltroData() : Util.getToday();

                        intentParameters.setFiltroEquipamento(null);
                        intentParameters.setFiltroMaterial(null);
                        intentParameters.setFiltroEstaca(null);

                        equipamentoList.setText("");

                        if (item.equals(FILTRO_PREFIXO)) {
                            equipamentoArrayVO = getDAO().getEquipamentoDAO().getArrayEquipamentosMovimentacaoConsulta(dataHora);
                            ArrayAdapter<EquipamentoVO> adp = new ArrayAdapter<EquipamentoVO>(ViagensSearchActivity.this, android.R.layout.select_dialog_singlechoice, equipamentoArrayVO);
                            adp.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                            equipamentoList.setAdapter(adp);
                        } else if (item.equals(FILTRO_MATERIAL)) {
                            materialArrayVO = getDAO().getMaterialDAO().getArrayEquipamentosMovimentacaoConsulta(dataHora);
                            ArrayAdapter<MaterialVO> adp = new ArrayAdapter<MaterialVO>(ViagensSearchActivity.this, android.R.layout.select_dialog_singlechoice, materialArrayVO);
                            adp.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                            equipamentoList.setAdapter(adp);
                        } else {
                            ArrayAdapter<String> adp = new ArrayAdapter<String>(ViagensSearchActivity.this, android.R.layout.select_dialog_singlechoice, new String[]{""});
                            adp.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                            equipamentoList.setAdapter(adp);
                        }

                        atualizaTipoTeclado();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }
        });
    }

    @Override
    public Cursor getCursor() throws Exception {

        return getDAO().getViagemDAO().getCursorSearch(new Object[]{dataHora, idEquipamento, idMaterial, estaca});
    }

    @Override
    public GridBody getGridBodyValues() throws Exception {

        Integer noContent = null;

        Integer indexCursorPrefixo = 0;
        Integer indexCursorEstaca = 1;
        Integer indexCursorHora = 2;
        Integer indexCursorEticket = 3;
        Integer indexCursorMaterial = 4;
        Integer indexCursorApropriar = 5;

        Integer indexColumnNoRepeatPrefixo = 0;
        Integer indexColumnNoRepeatEstaca = 1;

        GridBody grid = new GridBody(this);

        grid.setCursor(getCursor());
        grid.setIndexColumnsNoRepeat(new Integer[]{indexColumnNoRepeatPrefixo, indexColumnNoRepeatEstaca});
        grid.setColumnsTxt(new Integer[]{indexCursorPrefixo, noContent, indexCursorEstaca, indexCursorHora, indexCursorEticket, indexCursorMaterial, indexCursorApropriar});

        grid.setColorsBKG(new Integer[]{getColor(R.color.WHITE), getColor(R.color.GRAY3)});
        grid.setColorTXT(getColor(R.color.BLACK));

        grid.setIdTable(References.GRID_ID_VSC);
        grid.setFileLayoutRow(References.GRID_LAYOUT_VSC);
        grid.setIdsXmlColumn(References.GRID_ID_COLUMNS_VSC);
        grid.setTypesColumn(new String[]{getStr(R.string.TYPE_VIEW), getStr(R.string.TYPE_ROWNUM), getStr(R.string.TYPE_VIEW), getStr(R.string.TYPE_VIEW), getStr(R.string.TYPE_VIEW), getStr(R.string.TYPE_VIEW), getStr(R.string.TYPE_VIEW)});

        return grid;

    }

    @Override
    public GridHeader getGridHeaderValues() throws Exception {

        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.GRAY));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.HEADER_LAYOUT_VSC); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.HEADER_ID_COLUMNS_VSC);// Ids (xml) das colunas
        header.setIdTable(References.HEADER_ID_VSC); //Id do TableLayout);
        header.setNameColumns(getStrArray(R.array.HEADER_NAME_COLUMNS_VSC));//Nomes das Colunas

        return header;


    }

    public GridHeader getGridHeaderTopValues() {

        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.TOP_HEADER_LAYOUT_VSC); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.TOP_HEADER_ID_COLUMN);// Ids (xml) das colunas
        header.setIdTable(References.TOP_HEADER_ID_VSC); //Id do TableLayout);
        header.setNameColumns(getStrArray(R.array.TOP_HEADER_NAME_COLUMNS_VSC));//Nomes das Colunas

        return header;

    }

    @Override
    public GridFooter getGridFooterValues() throws Exception {

        GridFooter footer = new GridFooter(this);
        footer.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        footer.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        footer.setFileLayoutRow(References.FOOTER_LAYOUT_VSC); //arquivo xml - layout (TableRow)
        footer.setIdColumns(References.FOOTER_ID_COLUMNS);// Ids (xml) das colunas
        footer.setIdTable(References.FOOTER_ID_VSC); //Id do TableLayout);

        total.setText(Util.getMessage(currentContext, String.valueOf(cursor.getCount()), R.string.totalConsulta));

        return footer;

    }


    @Override
    public Detail getDetailValues() throws Exception {

        //Texto do detail
        String strDetail = getStr(R.string.DETAIL_MOV);

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

        redirect(EquipamentosMovimentacaoDiariaActivity.class, true);
    }

    @Override
    public void validateFields() throws Exception {
    }

    @Override
    public void editScreen() throws Exception {
    }
}
