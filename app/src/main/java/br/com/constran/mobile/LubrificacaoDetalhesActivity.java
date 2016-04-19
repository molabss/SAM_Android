package br.com.constran.mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import br.com.constran.mobile.exception.AlertException;
import br.com.constran.mobile.persistence.vo.imp.json.AbastecimentoJson;
import br.com.constran.mobile.persistence.vo.rae.abs.AbastecimentoVO;
import br.com.constran.mobile.persistence.vo.rae.abs.CompartimentoVO;
import br.com.constran.mobile.persistence.vo.rae.abs.LubrificacaoDetalheVO;
import br.com.constran.mobile.view.AbstractActivity;
import br.com.constran.mobile.view.interfaces.InterfaceEditActivity;
import br.com.constran.mobile.view.interfaces.InterfaceListActivity;
import br.com.constran.mobile.view.screens.Detail;
import br.com.constran.mobile.view.screens.GridBody;
import br.com.constran.mobile.view.screens.GridFooter;
import br.com.constran.mobile.view.screens.GridHeader;
import br.com.constran.mobile.view.util.References;
import br.com.constran.mobile.view.util.Util;
import org.apache.commons.io.output.FileWriterWithEncoding;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

public final class LubrificacaoDetalhesActivity extends AbstractActivity implements InterfaceEditActivity, InterfaceListActivity {

    private TextView lubrificante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detalhes_lubrificacao_body);

        currentClass = LubrificacaoDetalhesActivity.class;
        currentContext = LubrificacaoDetalhesActivity.this;

        try {

            init();

            initAtributes();

            editValues();

            initEvents();

            createDetail(getDetailValues());

            createGridHeader(getGridHeaderValues());

            createGridBody(getGridBodyValues());

            createGridFooter(getGridFooterValues());

        } catch (Exception e) {
            tratarExcecao(e);
        }
    }


    @Override
    public void initAtributes() throws Exception {

        lubrificante = (TextView) findViewById(R.id.absLub);

    }


    @Override
    public void editValues() throws Exception {

        idCombustivelLubrificante = Integer.parseInt(intentParameters.getIdRegistroPai());

        combustivelLubrificante = getDAO().getCombustivelLubrificanteDAO().getById(idCombustivelLubrificante);

        String qtd = "0";

        AbastecimentoJson temp = getAbsTempVO();

        for (AbastecimentoVO abs : temp.getAbastecimentos()) {

            if (abs.getCombustivelLubrificante().getId().intValue() == idCombustivelLubrificante.intValue())
                qtd = abs.getQtd();
        }

        values = getValues();

        Typeface font = Typeface.createFromAsset(getAssets(), getStr(R.string.FONT_MONACO));

        lubrificante.setTypeface(font, Typeface.BOLD);

        lubrificante.setText(combustivelLubrificante.getDescricao() + "  " + qtd + " " + combustivelLubrificante.getUnidadeMedida());

        lubrificante.setPadding(0, 0, 0, 0);

    }

    @Override
    public GridBody getGridBodyValues() throws Exception {

        Integer indexArrayIdCompartimento = 0;
        Integer indexArrayIdCategoria = 1;
        Integer indexArrayCompartimento = 2;
        Integer indexArrayQuantidade = 3;
        Integer indexArrayObservacao = 4;

        GridBody grid = new GridBody(this);

        grid.setValues(values);
        grid.setIndexsPKRow(new Integer[]{indexArrayIdCompartimento, indexArrayIdCategoria});
        grid.setColumnsTxt(new Integer[]{indexArrayCompartimento, indexArrayQuantidade, indexArrayObservacao});

        grid.setClassVO(AbastecimentoVO.class);
        grid.setClassRefresh(LubrificacaoDetalhesActivity.class);

        grid.setColorTXT(getColor(R.color.BLACK));
        grid.setColorsBKG(new Integer[]{getColor(R.color.WHITE), getColor(R.color.GRAY3)});

        grid.setIdTable(References.GRID_ID_ABS3);
        grid.setFileLayoutRow(References.GRID_LAYOUT_ABS3);
        grid.setIdsXmlColumn(References.GRID_ID_COLUMNS_ABS3);
        grid.setTypesColumn(new String[]{getStr(R.string.TYPE_VIEW), getStr(R.string.TYPE_EDIT), getStr(R.string.TYPE_EDIT)});

        return grid;


    }

    @Override
    public GridHeader getGridHeaderValues() throws Exception {

        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.HEADER_LAYOUT_ABS3); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.HEADER_ID_COLUMNS_ABS3);// Ids (xml) das colunas
        header.setIdTable(References.HEADER_ID_ABS3); //Id do TableLayout);
        header.setNameColumns(getStrArray(R.array.HEADER_NAME_COLUMNS_ABS3));//Nomes das Colunas

        return header;

    }


    @Override
    public GridFooter getGridFooterValues() throws Exception {

        GridFooter footer = new GridFooter(this);
        footer.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        footer.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        footer.setFileLayoutRow(References.FOOTER_LAYOUT_DEFAULT); //arquivo xml - layout (TableRow)
        footer.setIdColumns(References.FOOTER_ID_COLUMNS);// Ids (xml) das colunas
        footer.setIdTable(References.FOOTER_ID_MOV); //Id do TableLayout);

        return footer;

    }


    @Override
    public Detail getDetailValues() throws Exception {

        //Texto do detail
        String strDetail = getStr(R.string.DETAIL_ABS_LUBRIF);

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


    public void createGridHeader(final GridHeader pHParameters) {

        TableLayout table = (TableLayout) findViewById(pHParameters.getIdTable());
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newRow = inflater.inflate(pHParameters.getFileLayoutRow(), null);

        int i = 0;

        for (int id : pHParameters.getIdColumns()) {

            Button column = (Button) newRow.findViewById(id);
            column.setTextColor(pHParameters.getColorTXT());
            column.setText(pHParameters.getNameColumns()[i++]);
            column.setBackground(null);

            if (column.getText().equals(getStr(R.string.REFRESH))) {

                column.setBackgroundResource(R.drawable.refresh);
                column.setText(getStr(R.string.EMPTY));

                column.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        view.requestFocus();
                        try {
                            saveGrid();
                        } catch (Exception e) {
                            tratarExcecao(e);
                        }
                    }
                });

//			}else if(pHParameters.getValueReplaceToken() != null){
//
//				column.setText(column.getText().toString().replace(getStr(R.string.TOKEN), pHParameters.getValueReplaceToken()));
            }

        }

        newRow.setBackgroundColor(pHParameters.getColorBKG());

        table.addView(newRow, 0);

    }

    public void createGridBody(final GridBody gridBody) {

        TableLayout table = (TableLayout) findViewById(gridBody.getIdTable());
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        rowsColumnsMap = new HashMap<String, String[]>();

        int row = 0;

        int index = 2139999999;

        List<String[]> valueList = gridBody.getValues();

        Collections.sort(valueList, new Comparator<String[]>() {
            //ordena pelo nome da propriedade (index == 2)
            @Override
            public int compare(String[] a, String[] b) {
                return a[2].compareTo(b[2]);
            }
        });

        for (String[] values : valueList) {

            String[] columns = new String[4];

            View newRow = inflater.inflate(gridBody.getFileLayoutRow(), null);

            String rowId = getStr(R.string.EMPTY);

            for (int i = 0; i < gridBody.getIndexsPKRow().length; i++) {

                rowId += values[i];

                if (gridBody.getIndexsPKRow().length > 1 && i < gridBody.getIndexsPKRow().length - 1) {
                    rowId += getStr(R.string.TOKEN);
                }
            }

            int i = 0;

            int j = 1;


            for (int idColumn : gridBody.getIdsXmlColumn()) {

                final String typeColumn = gridBody.getTypesColumn()[i];

                boolean edit = typeColumn.equals(getStr(R.string.TYPE_EDIT));

                View column;

                String txt = values[gridBody.getColumnsTxt()[i++]].trim();
                Integer color = gridBody.getColorTXT();

                column = newRow.findViewById(idColumn);

                if (edit) {

                    ((EditText) column).setText(txt);
                    ((EditText) column).setTextColor(color);

                } else {

                    ((TextView) column).setText(txt);
                    ((TextView) column).setTextColor(color);

                }

                column.setId(index--);

                hiddenKeyboard(column);

                columns[j++] = String.valueOf(column.getId());

            }

            if (row % 2 == 0) {

                newRow.setBackgroundColor(gridBody.getColorsBKG()[0]);

            } else {

                newRow.setBackgroundColor(gridBody.getColorsBKG()[1]);
            }

            newRow.setId(index--);

            columns[0] = String.valueOf(newRow.getId());

            newRow.setTag(rowId);//armazena o id para evento

            rowsColumnsMap.put(rowId, columns);

            table.addView(newRow, row++);

        }
    }


    @Override
    public Cursor getCursor() throws Exception {
        return null;
    }


    public List<String[]> getValues() throws Exception {

        List<String[]> values = new ArrayList<String[]>();

        AbastecimentoJson objJson = getAbsTempVO();

        for (AbastecimentoVO abs : objJson.getAbastecimentos()) {

            if (abs.getCombustivelLubrificante().getId().intValue() == idCombustivelLubrificante.intValue()) {

                for (LubrificacaoDetalheVO vo : abs.getLubrificacaoDetalhes()) {

                    String[] arrayValues = new String[]{vo.getCompartimento().getId().toString(),
                            vo.getCompartimento().getCategoria().getId().toString(),
                            vo.getCompartimento().getDescricao(), vo.getQtd(),
                            vo.getObservacao() == null ? getStr(R.string.EMPTY) : vo.getObservacao()};

                    values.add(arrayValues);
                }
            }
        }

        return values;
    }


    public void saveGrid() throws Exception {

        if (values.isEmpty())

            Util.viewMessage(currentContext, getStr(R.string.ALERT_SEM_EQUIP_ASSOCIADOS_CATEGORIA));

        else {

            BigDecimal contQuantidade = new BigDecimal(0);

            List<LubrificacaoDetalheVO> lista = new ArrayList<LubrificacaoDetalheVO>();

            AbastecimentoJson tempVO = getAbsTempVO();

            Set<String> keys = rowsColumnsMap.keySet();

            for (String key : keys) {

                String[] columns = rowsColumnsMap.get(key);

                View row = findViewById(Integer.valueOf(columns[0]));

                LubrificacaoDetalheVO vo = new LubrificacaoDetalheVO();

                String descricao = ((TextView) row.findViewById(Integer.valueOf(columns[1]))).getText().toString();
                String quantidade = ((EditText) row.findViewById(Integer.valueOf(columns[2]))).getText().toString();
                String observacao = ((EditText) row.findViewById(Integer.valueOf(columns[3]))).getText().toString();

                contQuantidade = contQuantidade.add(new BigDecimal((quantidade == null || quantidade.trim().equals(getStr(R.string.EMPTY))) ? "0" : quantidade));

                BigDecimal qtd = new BigDecimal(quantidade.trim().equals(getStr(R.string.EMPTY)) ? "0" : quantidade.trim());

                vo.setQtd(qtd.toString());
                vo.setObservacao(observacao);

                String[] arrayPK = Util.getArrayPK(key, getStr(R.string.TOKEN));

                vo.setCompartimento(new CompartimentoVO(Integer.parseInt(arrayPK[0]), Integer.parseInt(arrayPK[1])));
                vo.getCompartimento().setDescricao(descricao);

                lista.add(vo);

            }

            for (AbastecimentoVO abs : tempVO.getAbastecimentos()) {

                if (abs.getCombustivelLubrificante().getId().intValue() == idCombustivelLubrificante.intValue()) {

                    if (contQuantidade.doubleValue() == Double.valueOf(abs.getQtd()))
                        abs.setLubrificacaoDetalhes(lista);
                    else
                        throw new AlertException(getStr(R.string.ERROR_SOMA_VALORES_QTE) + abs.getCombustivelLubrificante().getDescricao());
                }
            }

            File file = getAbsTempFile();

            String strJson = new Gson().toJson(tempVO);

            FileWriterWithEncoding fw = new FileWriterWithEncoding(file, getStr(R.string.UTF_8));

            fw.write(strJson);
            fw.flush();
            fw.close();

//			Util.viewMessage(currentContext, getStr(R.string.ALERT_SUCESS_UPDATE));
            AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
            dialogo.setTitle(getResources().getString(R.string.msg_aviso));
            dialogo.setMessage(getStr(R.string.ALERT_SUCESS_UPDATE));
            dialogo.setNeutralButton(getResources().getString(R.string.msg_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onBackPressed();
                }
            });
            dialogo.show();

        }

    }


    @Override
    public void initEvents() throws Exception {

    }

    @Override
    public GridHeader getGridHeaderTopValues() throws Exception {
        return null;
    }


    @Override
    public void editScreen() throws Exception {

    }


    @Override
    public void validateFields() throws Exception {

    }


}