package br.com.constran.mobile;

import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;
import br.com.constran.mobile.view.AbstractActivity;
import br.com.constran.mobile.view.interfaces.InterfaceEditActivity;
import br.com.constran.mobile.view.interfaces.InterfaceListActivity;
import br.com.constran.mobile.view.screens.Detail;
import br.com.constran.mobile.view.screens.GridBody;
import br.com.constran.mobile.view.screens.GridFooter;
import br.com.constran.mobile.view.screens.GridHeader;
import br.com.constran.mobile.view.util.References;
import br.com.constran.mobile.view.util.Util;

public final class EventosSearchActivity extends AbstractActivity implements InterfaceEditActivity, InterfaceListActivity {

    private AutoCompleteTextView equipamentoList;
    private AutoCompleteTextView dataList;
    private Button btSearch;
    private TextView total;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.eventos_search_body);

        currentClass = EventosSearchActivity.class;
        currentContext = EventosSearchActivity.this;

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

        equipamentoList = (AutoCompleteTextView) findViewById(R.id.evnCmbEqp);
        dataList = (AutoCompleteTextView) findViewById(R.id.evnCmbDt);
        btSearch = (Button) findViewById(R.id.btSearch);
        total = (TextView) findViewById(R.id.evnTotal);
    }

    @Override
    public void editValues() throws Exception {

        idEquipamento = intentParameters.getFiltroEquipamento();

        dataHora = (intentParameters.getFiltroData() != null) ? intentParameters.getFiltroData() : Util.getToday();

        equipamentoArrayVO = getDAO().getEquipamentoDAO().getArrayEquipamentosParteDiariaConsulta(dataHora);

        ArrayAdapter<EquipamentoVO> adp = new ArrayAdapter<EquipamentoVO>(this, android.R.layout.select_dialog_singlechoice, equipamentoArrayVO);
        adp.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        equipamentoList.setAdapter(adp);

        if (idEquipamento != null) {
            equipamentoList.setText(getDAO().getEquipamentoDAO().getDescricao(idEquipamento, false, true));
        }

        dataArray = getDAO().getEventoDAO().getArrayDatas();

        ArrayAdapter<String> adD = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, dataArray);
        adD.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        dataList.setAdapter(adD);

        dataList.setText(dataHora);

        cursor = getCursor();
    }

    @Override
    public void initEvents() throws Exception {

        equipamentoList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EquipamentoVO e = (EquipamentoVO) parent.getItemAtPosition(position);
                intentParameters.setFiltroEquipamento(e.getId());
            }

        });

        equipamentoList.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                intentParameters.setFiltroEquipamento(null);
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                equipamentoList.setTextColor(getColor(R.color.BLACK));
                return false;
            }
        });

        equipamentoList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                intentParameters.setFiltroEquipamento(null);
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                equipamentoList.setTextColor(getColor(R.color.BLACK));
            }
        });

        equipamentoList.setOnKeyListener(equipamentoListener());


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

                if (intentParameters.getFiltroData() == null || intentParameters.getFiltroData().equals(getStr(R.string.EMPTY))) {
                    intentParameters.setFiltroData(Util.getToday());
                }

                refreshIntentParameters(intentParameters);

                redirect(currentClass, false);

            }
        });
    }

    protected OnKeyListener equipamentoListener() {
        return new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                intentParameters.setFiltroEquipamento(null);
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                return false;
            }
        };
    }

    @Override
    public Cursor getCursor() throws Exception {

        cursor = getDAO().getEventoDAO().getCursor(new Object[]{dataHora, idEquipamento});

        total.setText(Util.getMessage(currentContext, String.valueOf(cursor.getCount()), R.string.totalConsulta));

        return cursor;
    }

    @Override
    public GridBody getGridBodyValues() throws Exception {

        Integer noContent = null;

        Integer indexCursorPrefixo = 0;
        Integer indexCursorEvento = 1;
        Integer indexCursorPeriodo = 2;
        Integer indexCursorApropriar = 3;

        Integer indexColumnNoRepeatPrefixo = 0;
        Integer indexColumnNoRepeatRowNum = 1;

        GridBody grid = new GridBody(this);

        grid.setColorTXT(getColor(R.color.BLACK));
        grid.setColorsBKG(new Integer[]{getColor(R.color.WHITE), getColor(R.color.GRAY3)});

        grid.setCursor(getCursor());
        grid.setColumnsTxt(new Integer[]{indexCursorPrefixo, noContent, indexCursorEvento, indexCursorPeriodo, indexCursorApropriar});
        grid.setIndexColumnsNoRepeat(new Integer[]{indexColumnNoRepeatPrefixo, indexColumnNoRepeatRowNum});

        grid.setIdTable(References.GRID_ID_ESC);
        grid.setFileLayoutRow(References.GRID_LAYOUT_ESC);
        grid.setIdsXmlColumn(References.GRID_ID_COLUMNS_ESC);
        grid.setTypesColumn(new String[]{getStr(R.string.TYPE_VIEW), getStr(R.string.TYPE_ROWNUM), getStr(R.string.TYPE_VIEW), getStr(R.string.TYPE_VIEW), getStr(R.string.TYPE_VIEW)});

        return grid;

    }

    @Override
    public GridHeader getGridHeaderValues() throws Exception {

        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.GRAY));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.HEADER_LAYOUT_ESC); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.HEADER_ID_COLUMNS_ESC);// Ids (xml) das colunas
        header.setIdTable(References.HEADER_ID_ESC); //Id do TableLayout);
        header.setNameColumns(getStrArray(R.array.HEADER_NAME_COLUMNS_ESC));//Nomes das Colunas

        return header;


    }

    public GridHeader getGridHeaderTopValues() {

        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.TOP_HEADER_LAYOUT_ESC); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.TOP_HEADER_ID_COLUMN);// Ids (xml) das colunas
        header.setIdTable(References.TOP_HEADER_ID_ESC); //Id do TableLayout);
        header.setNameColumns(getStrArray(R.array.TOP_HEADER_NAME_COLUMNS_ESC));//Nomes das Colunas

        return header;

    }

    @Override
    public GridFooter getGridFooterValues() throws Exception {

        GridFooter footer = new GridFooter(this);
        footer.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        footer.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        footer.setFileLayoutRow(References.FOOTER_LAYOUT_ESC); //arquivo xml - layout (TableRow)
        footer.setIdColumns(References.FOOTER_ID_COLUMNS);// Ids (xml) das colunas
        footer.setIdTable(References.FOOTER_ID_ESC); //Id do TableLayout);

        return footer;

    }


    @Override
    public Detail getDetailValues() throws Exception {

        //Texto do detail
        String strDetail = getStr(R.string.DETAIL_EVTS);

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

        redirect(EquipamentosParteDiariaActivity.class, true);
    }

    @Override
    public void validateFields() throws Exception {
    }

    @Override
    public void editScreen() throws Exception {
    }
}
