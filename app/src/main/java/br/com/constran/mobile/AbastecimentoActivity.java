package br.com.constran.mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;

import br.com.constran.mobile.exception.AlertException;
import br.com.constran.mobile.model.LogAuditoria;
import br.com.constran.mobile.persistence.dao.DAOFactory;
import br.com.constran.mobile.persistence.dao.LogAuditoriaDAO;
import br.com.constran.mobile.persistence.dao.menu.ConfiguracaoDAO;
import br.com.constran.mobile.persistence.vo.imp.json.AbastecimentoJson;
import br.com.constran.mobile.persistence.vo.rae.abs.AbastecimentoVO;
import br.com.constran.mobile.persistence.vo.rae.abs.CombustivelLubrificanteVO;
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
import java.util.ArrayList;
import java.util.List;

public final class AbastecimentoActivity extends AbstractActivity implements InterfaceEditActivity, InterfaceListActivity {

    private AutoCompleteTextView combustivelList;
    private EditText quantidade;
    private Button btAdd, btLub, btConfirmar;
    private TextView unidadeMedida;
    private TextView prefixo;
    LogAuditoriaDAO logDAO;
    LogAuditoria log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.abastecimento_body);

        currentClass = AbastecimentoActivity.class;
        currentContext = AbastecimentoActivity.this;

        try {

            init();

            initAtributes();

            editValues();

            initEvents();

            createDetail(getDetailValues());

            createGridHeader(getGridHeaderValues());

            createGridBody(getGridBodyValues());

            createGridFooter(getGridFooterValues());

            editScreen();

        } catch (Exception e) {
            tratarExcecao(e);
        }
    }

    @Override
    public void initAtributes() throws Exception {

        combustivelList = (AutoCompleteTextView) findViewById(R.id.absComLubr);
        quantidade = (EditText) findViewById(R.id.absQtd);
        prefixo = (TextView) findViewById(R.id.absPrefixo);
        unidadeMedida = (TextView) findViewById(R.id.absUN);
        btAdd = (Button) findViewById(R.id.btAdd);
        btLub = (Button) findViewById(R.id.btLub);
        btConfirmar = (Button) findViewById(R.id.btConfirmar);

    }

    @Override
    public void editValues() throws Exception {

        DAOFactory dao = getDAO();
        ConfiguracaoDAO configuracaoDAO = dao.getConfiguracoesDAO();
        config = configuracaoDAO.getConfiguracaoVO();
        logDAO = getDAO().getLogAuditoriaDAO();
        log = new LogAuditoria("ABASTECIMENTO",config.getDispositivo());
        logDAO.setLogPropriedades(log);

        AbastecimentoJson temp = getAbsTempVO();

        String[] combustiveis = null;

        int tam = temp.getAbastecimentos().size();

        if (tam > 0) {

            combustiveis = new String[tam];

            int i = 0;

            for (AbastecimentoVO abs : temp.getAbastecimentos()) {

                combustiveis[i++] = abs.getCombustivelLubrificante().getId().toString();
            }
        }

        combustivelArrayVO = getDAO().getCombustivelLubrificanteDAO().getArrayCombustivelLubrificanteVO(config.getIdPosto(), combustiveis);

        ArrayAdapter<CombustivelLubrificanteVO> adp = new ArrayAdapter<CombustivelLubrificanteVO>(this, android.R.layout.select_dialog_singlechoice, combustivelArrayVO);
        adp.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        combustivelList.setAdapter(adp);

        values = getValues();

        Typeface font = Typeface.createFromAsset(getAssets(), getStr(R.string.FONT_MONACO));

        prefixo.setTypeface(font, Typeface.BOLD);
        unidadeMedida.setTypeface(font, Typeface.BOLD);

        prefixo.setText(temp.getCabecalho().getEquipamento().getPrefixo().trim());
        prefixo.setPadding(0, 0, 0, 0);
        unidadeMedida.setGravity(Gravity.CENTER);

    }

    @Override
    public void initEvents() throws Exception {

        combustivelList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                combustivelLubrificante = (CombustivelLubrificanteVO) parent.getItemAtPosition(position);
                unidadeMedida.setText(combustivelLubrificante.getUnidadeMedida());

            }

        });

        combustivelList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                combustivelLubrificante = null;
                unidadeMedida.setText(getStr(R.string.EMPTY));
            }
        });

        combustivelList.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                combustivelLubrificante = null;
                unidadeMedida.setText(getStr(R.string.EMPTY));
                return false;
            }
        });


        btAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {

                    validateFields();

                    boolean isCombustivel = combustivelLubrificante.getTipo().trim().equalsIgnoreCase("C");

                    if (isCombustivel) {

                        logDAO.insereLog("inserindo combustivel");

                        double qtd = Double.valueOf(quantidade.getText().toString().trim());
                        double saldo = getDAO().getAbastecimentoPostoDAO().getSaldo(config.getIdPosto(), combustivelLubrificante.getId());

                        if (saldo < qtd) {

                            AlertDialog.Builder dialogo = new AlertDialog.Builder(currentContext);

                            dialogo.setMessage(Util.getMessage(currentContext, String.valueOf(saldo), R.string.ALERT_BALANCE));

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

                        } else
                            saveTempFile();


                    } else
                        saveTempFile();

                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }
        });

        btLub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                logDAO.insereLog("detalhe de lubrificacao");

                try {

                    redirect(AbastecimentoLubrificacaoActivity.class, false);

                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }
        });

        btConfirmar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                logDAO.insereLog("confirmando abastecimento/Lubrificacao");

                if (values == null || values.isEmpty())
                    Util.viewMessage(currentContext, getStr(R.string.ALERT_ABS_NOT_FOUND));
                else
                    redirect(AbastecimentoConfirmActivity.class, false);
            }
        });


        combustivelList.setOnKeyListener(combustivelListener());

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
        Integer indexArrayId = 0;
        Integer indexArrayCombustivel = 1;
        Integer indexArrayQuantidade = 2;

        GridBody grid = new GridBody(this);

        grid.setClassVO(AbastecimentoVO.class);
        grid.setClassList(LubrificacaoDetalhesActivity.class);
        grid.setClassRefresh(AbastecimentoActivity.class);

        grid.setColorsBKG(new Integer[]{getColor(R.color.WHITE), getColor(R.color.GRAY3)});
        grid.setColorTXT(getColor(R.color.BLACK));

        grid.setValues(values);
        grid.setIndexsPKRow(new Integer[]{indexArrayId});
        grid.setColumnsTxt(new Integer[]{indexArrayCombustivel, indexArrayQuantidade, noContent, noContent});
        grid.setReferencesImage(new Integer[]{noContent, noContent, R.drawable.form, R.drawable.delete});

        grid.setIdTable(References.GRID_ID_ABS2);
        grid.setFileLayoutRow(References.GRID_LAYOUT_ABS2);
        grid.setIdsXmlColumn(References.GRID_ID_COLUMNS_ABS2);
        grid.setTypesColumn(new String[]{getStr(R.string.TYPE_VIEW), getStr(R.string.TYPE_VIEW), getStr(R.string.TYPE_LIST_PAGE), getStr(R.string.TYPE_REMOVE)});


        return grid;

    }

    @Override
    public GridHeader getGridHeaderValues() throws Exception {

        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.HEADER_LAYOUT_ABS2); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.HEADER_ID_COLUMNS_ABS2);// Ids (xml) das colunas
        header.setIdTable(References.HEADER_ID_ABS2); //Id do TableLayout);
        header.setNameColumns(getStrArray(R.array.HEADER_NAME_COLUMNS_ABS2));//Nomes das Colunas

        return header;

    }


    @Override
    public GridFooter getGridFooterValues() throws Exception {

        GridFooter footer = new GridFooter(this);
        footer.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        footer.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        footer.setFileLayoutRow(References.FOOTER_LAYOUT_DEFAULT); //arquivo xml - layout (TableRow)
        footer.setIdColumns(References.FOOTER_ID_COLUMNS);// Ids (xml) das colunas
        footer.setIdTable(References.FOOTER_ID_ABS); //Id do TableLayout);

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

        redirect(AbastecimentosSearchActivity.class, true);
    }


    @Override
    public GridHeader getGridHeaderTopValues() {
        return null;
    }

    @Override
    public void validateFields() throws Exception {


        if (combustivelLubrificante == null) {
            throw new AlertException(getStr(R.string.ALERT_SELECT_ABS));
        } else if (quantidade.getText().toString().equals(getStr(R.string.EMPTY))) {
            throw new AlertException(getStr(R.string.ALERT_SELECT_QTD));
        } else if (Double.valueOf(quantidade.getText().toString()) == 0.0) {
            throw new AlertException(getStr(R.string.ALERT_QTD_EMPTY));
        }
    }

    @Override
    public void editScreen() throws Exception {

        hiddenKeyboard(combustivelList);

    }

    public void saveTempFile() throws Exception {

        AbastecimentoJson objJson = getAbsTempVO();

        File file = getAbsTempFile();

        AbastecimentoVO registro = new AbastecimentoVO();

        registro.setCombustivelLubrificante(combustivelLubrificante);

        if (combustivelLubrificante.getTipo().equals("L")) {

            List<LubrificacaoDetalheVO> detalhes = getDAO().getLubrificacaoDetalhesDAO().getDetalhes(objJson.getCabecalho().getEquipamento().getId());

            registro.setLubrificacaoDetalhes(detalhes);
        }

        BigDecimal qtd = quantidade.getText().toString().trim().equals(getStr(R.string.EMPTY)) ? new BigDecimal("0") : new BigDecimal(quantidade.getText().toString());

        registro.setQtd(qtd.toString());

        List<AbastecimentoVO> lista = objJson.getAbastecimentos();

        lista.add(registro);

        objJson.setAbastecimentos(lista);

        String strJson = new Gson().toJson(objJson);

        FileWriterWithEncoding fw = new FileWriterWithEncoding(file, getStr(R.string.UTF_8));

        strJson = strJson.replaceAll("'", "");

        //grava no arquivo
        fw.write(strJson);
        fw.flush();
        fw.close();

        redirect(currentClass, false);


    }

    public void createGridBody(final GridBody gridBody) {

        TableLayout table = (TableLayout) findViewById(gridBody.getIdTable());
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int row = 0;

        for (String[] values : gridBody.getValues()) {

            View newRow = inflater.inflate(gridBody.getFileLayoutRow(), null);

            String rowId = getStr(R.string.EMPTY);
            String tipo = getStr(R.string.EMPTY);

            String[] arrayPK = Util.getArrayPK(values[0], getStr(R.string.TOKEN));
            rowId = arrayPK[0];
            tipo = arrayPK[1];

            int i = 0;

            for (int idColumn : gridBody.getIdsXmlColumn()) {

                final String typeColumn = gridBody.getTypesColumn()[i];

                boolean image = !typeColumn.equals(getStr(R.string.TYPE_VIEW));
                boolean remove = typeColumn.equals(getStr(R.string.TYPE_REMOVE));
                boolean noDetails = (i == 2 && tipo != null && tipo.equals("C"));

                Button column;

                Integer color = gridBody.getColorTXT();

                column = (Button) newRow.findViewById(idColumn);

                if (image) {

                    column.setText(getStr(R.string.EMPTY));

                    if (noDetails)
                        column.setBackground(null);
                    else
                        column.setBackgroundResource(gridBody.getReferencesImage()[i]);

                } else {

                    String txt = values[gridBody.getColumnsTxt()[i]].trim();
                    column.setText(txt);
                    column.setTextColor(color);
                    column.setBackground(null);
                }

                i++;

                column.setTag(rowId);//armazena o id para evento

                if (image && !noDetails) {

                    if (remove) {

                        column.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                final String objId = ((Button) arg0).getTag().toString();
                                AlertDialog.Builder dialogo = new AlertDialog.Builder(gridBody.getContext());
                                dialogo.setMessage(getStr(R.string.ALERT_CONFIRM_REMOVE));
                                dialogo.setPositiveButton(getStr(R.string.SIM), new
                                        DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface di, int arg) {
                                                logDAO.insereLog("deletando item de abastecimento");
                                                refreshAbsTempFile(Integer.valueOf(objId));
                                                redirect(gridBody.getClassRefresh(), true);
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
                        });

                    } else {//imagem

                        column.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {

                                Object obj = ((Button) arg0).getTag();

                                final String objId = String.valueOf(obj);

                                Class<?> objClass = null;

                                intentParameters.setIdRegistroPai(objId);
                                intentParameters.setIdRegistroAtual(null);

                                objClass = gridBody.getClassList();

                                refreshIntentParameters(intentParameters);

                                redirect(objClass, false);
                            }
                        });
                    }
                }
            }

            if (row % 2 == 0) {

                newRow.setBackgroundColor(gridBody.getColorsBKG()[0]);

            } else {

                newRow.setBackgroundColor(gridBody.getColorsBKG()[1]);
            }

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

            String[] arrayValues = new String[]{abs.getCombustivelLubrificante().getId().toString() + getStr(R.string.TOKEN) + abs.getCombustivelLubrificante().getTipo(),
                    abs.getCombustivelLubrificante().getDescricao().trim(),
                    abs.getQtd() + " " + abs.getCombustivelLubrificante().getUnidadeMedida(), null, null};

            values.add(arrayValues);

        }

        return values;
    }


    private void refreshAbsTempFile(Integer idCombustivel) {

        try {

            List<AbastecimentoVO> lista = new ArrayList<AbastecimentoVO>();

            AbastecimentoJson objJson = getAbsTempVO();

            File file = getAbsTempFile();

            for (AbastecimentoVO vo : objJson.getAbastecimentos()) {

                if (!(vo.getCombustivelLubrificante().getId().intValue() == idCombustivel.intValue())) {

                    lista.add(vo);
                }
            }

            objJson.setAbastecimentos(lista);

            String strJson = new Gson().toJson(objJson);

            FileWriterWithEncoding fw = new FileWriterWithEncoding(file, getStr(R.string.UTF_8));

            fw.write(strJson);
            fw.flush();
            fw.close();

        } catch (Exception e) {
            tratarExcecao(e);
        }
    }
}


