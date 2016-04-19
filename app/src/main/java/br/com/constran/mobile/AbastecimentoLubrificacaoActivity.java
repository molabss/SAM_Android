package br.com.constran.mobile;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;
import br.com.constran.mobile.persistence.vo.imp.json.AbastecimentoJson;
import br.com.constran.mobile.view.AbstractActivity;
import br.com.constran.mobile.view.interfaces.InterfaceListActivity;
import br.com.constran.mobile.view.screens.Detail;
import br.com.constran.mobile.view.screens.GridBody;
import br.com.constran.mobile.view.screens.GridFooter;
import br.com.constran.mobile.view.screens.GridHeader;
import br.com.constran.mobile.view.util.References;

public final class AbastecimentoLubrificacaoActivity extends AbstractActivity implements InterfaceListActivity {

    private TextView prefixo;
    private TextView horimetroKm;
    private TextView unidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.abastecimento_lubrificacao_body);

        currentClass = AbastecimentoLubrificacaoActivity.class;
        currentContext = AbastecimentoLubrificacaoActivity.this;

        try {
            init();

            editValues();

            createDetail(getDetailValues());

            createGridHeader(getGridHeaderValues());
            createGridBody(getGridBodyValues());
            createGridFooter(getGridFooterValues());

            createGridHeader(getGridHeaderTopValues2());
            createGridHeader(getGridHeaderValues2());
            createGridBody(getGridBodyValues2());
            createGridFooter(getGridFooterValues2());

        } catch (Exception e) {
            tratarExcecao(e);
        }
    }

    @Override
    public void editValues() throws Exception {
        AbastecimentoJson temp = getAbsTempVO();

        idEquipamento = temp.getCabecalho().getEquipamento().getId();

        prefixo = (TextView) findViewById(R.id.absPrefixo);
        horimetroKm = (TextView) findViewById(R.id.absHorKm);
        unidade = (TextView) findViewById(R.id.absUnd);

        Typeface font = Typeface.createFromAsset(getAssets(), getStr(R.string.FONT_MONACO));

        prefixo.setTypeface(font, Typeface.BOLD);
        horimetroKm.setTypeface(font, Typeface.BOLD);
        unidade.setTypeface(font, Typeface.BOLD);

        EquipamentoVO eqp = getDAO().getEquipamentoDAO().getById(temp.getCabecalho().getEquipamento().getId());

        String valor = eqp.getTipo() != null && eqp.getTipo().trim().equalsIgnoreCase("H") ? eqp.getHorimetro() : eqp.getQuilometragem();

        prefixo.setText(eqp.getPrefixo().trim());
        horimetroKm.setText(valor == null || valor.equals(getStr(R.string.EMPTY)) ? "0" : valor);
        unidade.setText(eqp.getTipo() != null && eqp.getTipo().trim().equalsIgnoreCase("H") ? "h" : "Km");

        prefixo.setPadding(0, 0, 25, 0);
        horimetroKm.setPadding(0, 0, 5, 0);
        unidade.setPadding(0, 0, 5, 0);
    }

    @Override
    public Cursor getCursor() throws Exception {
        return getDAO().getPreventivaEquipamentoDAO().getCursor(idEquipamento);
    }

    public Cursor getCursor2() throws Exception {
        return getDAO().getLubrificacaoEquipamentoDAO().getCursor(idEquipamento);
    }

    @Override
    public GridBody getGridBodyValues() throws Exception {
        Integer indexCursorData = 0;
        Integer indexCursorHorimetro = 1;

        GridBody grid = new GridBody(this);
        grid.setCursor(getCursor());
        grid.setColumnsTxt(new Integer[]{indexCursorData, indexCursorHorimetro});
        grid.setColorTXT(getColor(R.color.BLACK));
        grid.setColorsBKG(new Integer[]{getColor(R.color.WHITE), getColor(R.color.GRAY3)});
        grid.setIdTable(References.GRID_ID_ABS5);
        grid.setFileLayoutRow(References.GRID_LAYOUT_ABS5);
        grid.setIdsXmlColumn(References.GRID_ID_COLUMNS_ABS5);
        grid.setTypesColumn(new String[]{getStr(R.string.TYPE_VIEW), getStr(R.string.TYPE_VIEW)});
        return grid;
    }


    public GridBody getGridBodyValues2() throws Exception {
        Integer indexCursorCompartimento = 0;
        Integer indexCursorHodometro = 1;
        Integer indexCursorHorimetro = 2;
        Integer indexCursorDiferenca = 3;

        GridBody grid = new GridBody(this);
        grid.setCursor(getCursor2());
        grid.setColumnsTxt(new Integer[]{indexCursorCompartimento, indexCursorHodometro, indexCursorHorimetro, indexCursorDiferenca});
        grid.setColorTXT(getColor(R.color.BLACK));
        grid.setColorsBKG(new Integer[]{getColor(R.color.WHITE), getColor(R.color.GRAY3)});
        grid.setFileLayoutRow(References.GRID_LAYOUT_ABS6);
        grid.setIdsXmlColumn(References.GRID_ID_COLUMNS_ABS6);
        grid.setIdTable(References.GRID_ID_ABS6);
        grid.setTypesColumn(new String[]{getStr(R.string.TYPE_VIEW), getStr(R.string.TYPE_VIEW), getStr(R.string.TYPE_VIEW), getStr(R.string.TYPE_VIEW)});
        return grid;
    }

    @Override
    public GridHeader getGridHeaderValues() throws Exception {
        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.HEADER_LAYOUT_ABS5); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.HEADER_ID_COLUMNS_ABS5);// Ids (xml) das colunas
        header.setIdTable(References.HEADER_ID_ABS5); //Id do TableLayout);
        header.setNameColumns(getStrArray(R.array.HEADER_NAME_COLUMNS_ABS5));//Nomes das Colunas
        return header;
    }

    public GridHeader getGridHeaderValues2() throws Exception {
        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.GRAY));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.HEADER_LAYOUT_ABS6); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.HEADER_ID_COLUMNS_ABS6);// Ids (xml) das colunas
        header.setIdTable(References.HEADER_ID_ABS6); //Id do TableLayout);
        header.setNameColumns(getStrArray(R.array.HEADER_NAME_COLUMNS_ABS6));//Nomes das Colunas
        return header;
    }

    public GridHeader getGridHeaderTopValues2() {
        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.TOP_HEADER_LAYOUT_ABS6); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.TOP_HEADER_ID_COLUMN);// Ids (xml) das colunas
        header.setIdTable(References.TOP_HEADER_ID_ABS6); //Id do TableLayout);
        header.setNameColumns(getStrArray(R.array.TOP_HEADER_NAME_COLUMNS_ABS6));//Nomes das Colunas
        return header;
    }

    @Override
    public GridFooter getGridFooterValues() throws Exception {
        GridFooter footer = new GridFooter(this);
        footer.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        footer.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        footer.setFileLayoutRow(References.FOOTER_LAYOUT_ABS); //arquivo xml - layout (TableRow)
        footer.setIdColumns(References.FOOTER_ID_COLUMNS);// Ids (xml) das colunas
        footer.setIdTable(References.FOOTER_ID_ABS5); //Id do TableLayout);
        return footer;
    }

    public GridFooter getGridFooterValues2() throws Exception {
        GridFooter footer = new GridFooter(this);
        footer.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        footer.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        footer.setFileLayoutRow(References.FOOTER_LAYOUT_ABS); //arquivo xml - layout (TableRow)
        footer.setIdColumns(References.FOOTER_ID_COLUMNS);// Ids (xml) das colunas
        footer.setIdTable(References.FOOTER_ID_ABS6); //Id do TableLayout);
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
        redirect(AbastecimentoActivity.class, false);
    }

    @Override
    public GridHeader getGridHeaderTopValues() throws Exception {
        return null;
    }
}
