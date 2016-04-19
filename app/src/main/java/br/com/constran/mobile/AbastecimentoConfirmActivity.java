package br.com.constran.mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import android.widget.LinearLayout.LayoutParams;

import br.com.constran.mobile.model.LogAuditoria;
import br.com.constran.mobile.persistence.dao.LogAuditoriaDAO;
import br.com.constran.mobile.persistence.vo.imp.json.AbastecimentoJson;
import br.com.constran.mobile.persistence.vo.rae.RaeVO;
import br.com.constran.mobile.persistence.vo.rae.abs.AbastecimentoVO;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AbastecimentoConfirmActivity extends AbstractActivity implements InterfaceEditActivity, InterfaceListActivity {

    private TextView raeView;
    private TextView prefixoView;
    private TextView postoView;
    private TextView frenteObraView;
    private TextView atividadeView;
    private EditText observacoesEdit;
    private TextView inicioView;
    private TextView terminoView;
    private TextView horimetroView;
    private TextView quilometragemView;
    private TextView operadorView;
    private TextView justificativaView;

    private TableRow rowQuilometragem;
    private TableRow rowHorimetro;
    private TableRow rowOperador;
    private TableRow rowJustificativa;
    LogAuditoriaDAO logDAO;
    LogAuditoria log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.abastecimento_confirm);

        currentContext = AbastecimentoConfirmActivity.this;
        currentClass = AbastecimentoConfirmActivity.class;

        try {

            init();

            initAtributes();

            editValues();

            initEvents();

            editScreen();

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

        raeView = (TextView) findViewById(R.id.absConfirmRae);
        prefixoView = (TextView) findViewById(R.id.absConfirmPref);
        postoView = (TextView) findViewById(R.id.absConfirmPosto);
        frenteObraView = (TextView) findViewById(R.id.lclViewFob);
        atividadeView = (TextView) findViewById(R.id.lclViewAtv);
        observacoesEdit = (EditText) findViewById(R.id.absConfirmObs);
        inicioView = (TextView) findViewById(R.id.absConfirmHrIn);
        terminoView = (TextView) findViewById(R.id.absConfirmHrFim);
        horimetroView = (TextView) findViewById(R.id.absConfirmHorim);
        quilometragemView = (TextView) findViewById(R.id.absConfirmQuilm);
        operadorView = (TextView) findViewById(R.id.lclViewOperador);
        justificativaView = (TextView) findViewById(R.id.lclViewJustificativa);

        rowHorimetro = (TableRow) findViewById(R.id.tbRwabsHrm);
        rowQuilometragem = (TableRow) findViewById(R.id.tbRwabsKm);
        rowOperador = (TableRow) findViewById(R.id.tbRwOperador);
        rowJustificativa = (TableRow) findViewById(R.id.tbRwJustificativa);

        btSalvar = (Button) findViewById(R.id.btAbsConfirmSave);
        btCancelar = (Button) findViewById(R.id.btAbsConfirmCancel);

    }

    @Override
    public void editValues() throws Exception {

        AbastecimentoJson objJson = getAbsTempVO();
        AbastecimentoVO cabecalho = objJson.getCabecalho();

        logDAO = getDAO().getLogAuditoriaDAO();
        log = new LogAuditoria("ABASTECIMENTO",config.getDispositivo());
        logDAO.setLogPropriedades(log);

        raeView.setText(objJson.getRae().getData());
        prefixoView.setText(cabecalho.getEquipamento().getPrefixo() + " - " + cabecalho.getEquipamento().getDescricao());
        postoView.setText(objJson.getRae().getPosto().getDescricao());
        frenteObraView.setText(cabecalho.getAtividade().getFrenteObra().getDescricao());
        atividadeView.setText(cabecalho.getAtividade().getDescricao());
        inicioView.setText(cabecalho.getHoraInicio());
        terminoView.setText(Util.getHourFormated(new Date()));
        quilometragemView.setText(cabecalho.getQuilometragem());
        horimetroView.setText(cabecalho.getHorimetro());
        observacoesEdit.setText(cabecalho.getObservacao());

        horaTermino = Util.getHourFormated(new Date());

        operador = cabecalho.getOperador();

        values = getValues();

        if (cabecalho.getOperador() != null) {
            rowOperador.setVisibility(View.VISIBLE);
            rowJustificativa.setVisibility(View.GONE);
            operadorView.setText(cabecalho.getOperador().getDescricao());

        } else if (cabecalho.getJustificativa() != null){
                rowOperador.setVisibility(View.GONE);
                rowJustificativa.setVisibility(View.VISIBLE);
                justificativaView.setText(cabecalho.getJustificativa().getDescricao());
        }else{
            rowOperador.setVisibility(View.GONE);
            rowJustificativa.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        redirect(AbastecimentoActivity.class, true);
    }

    @Override
    public GridBody getGridBodyValues() throws Exception {

        Integer indexArrayCombustivel = 0;
        Integer indexArrayQuantidade = 1;

        GridBody grid = new GridBody(this);

        grid.setClassVO(AbastecimentoVO.class);
        grid.setClassRefresh(AbastecimentoActivity.class);
        grid.setClassList(LubrificacaoDetalhesActivity.class);

        grid.setColorsBKG(new Integer[]{getColor(R.color.WHITE), getColor(R.color.GRAY3)});
        grid.setColorTXT(getColor(R.color.BLACK));

        grid.setValues(values);
        grid.setColumnsTxt(new Integer[]{indexArrayCombustivel, indexArrayQuantidade});
        grid.setTypesColumn(new String[]{getStr(R.string.TYPE_VIEW), getStr(R.string.TYPE_VIEW)});

        grid.setFileLayoutRow(References.GRID_LAYOUT_ABS4);
        grid.setIdsXmlColumn(References.GRID_ID_COLUMNS_ABS4);
        grid.setIdTable(References.GRID_ID_ABS4);

        return grid;
    }

    @Override
    public GridHeader getGridHeaderValues() throws Exception {

        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.HEADER_LAYOUT_ABS4); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.HEADER_ID_COLUMNS_ABS4);// Ids (xml) das colunas
        header.setIdTable(References.HEADER_ID_ABS4); //Id do TableLayout);
        header.setNameColumns(getStrArray(R.array.HEADER_NAME_COLUMNS_ABS4));//Nomes das Colunas

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

        btSalvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (operador != null && operador.getId() != null) {

                    logDAO.insereLog("solicitando a senha do operador");

                    final EditText input2 = new EditText(currentContext);
                    AlertDialog.Builder editalert = new AlertDialog.Builder(currentContext);
                    editalert.setTitle(getStr(R.string.ALERT_INFORME_SENHA_OPERADOR));
                    input2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editalert.setView(input2);
                    editalert.setPositiveButton(getStr(R.string.OK), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            try {

                                saveAbastecimentos(input2.getText().toString().trim());

                                logDAO.insereLog("abastecimento/ lubri cadastrado na base com sucesso");

                            } catch (Exception e) {
                                tratarExcecao(e);
                                logDAO.insereLog("abastecimento/ lubri cadastramento na base falhou");
                            }

                        }
                    });
                    editalert.setNegativeButton(getStr(R.string.CANCELAR), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            logDAO.insereLog("cancelou");

                        }
                    });
                    editalert.show();
                } else {

                    try {

                        saveAbastecimentos();
                        logDAO.insereLog("abastecimento/ lubri cadastrado na base com sucesso");

                    } catch (Exception e) {
                        tratarExcecao(e);
                    }
                }
            }
        });


        btCancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                logDAO.insereLog("abastecimento/ lubri nao salvou na base, cancelou");
                onBackPressed();
            }
        });
    }


    @Override
    public void editScreen() throws Exception {

        hiddenKeyboard(observacoesEdit);

        if (horimetroView.getText().toString().trim().equals(getStr(R.string.EMPTY))) {
            rowHorimetro.setVisibility(View.GONE);
            rowHorimetro.setLayoutParams(new LayoutParams(0, 0));
        }
        if (quilometragemView.getText().toString().trim().equals(getStr(R.string.EMPTY))) {
            rowQuilometragem.setVisibility(View.GONE);
            rowQuilometragem.setLayoutParams(new LayoutParams(0, 0));
        }
    }


    @Override
    public void validateFields() throws Exception {

    }


    private void saveAbastecimentos() throws Exception {

        AbastecimentoJson tempVo = getAbsTempVO();

        tempVo.getCabecalho().setObservacao(observacoesEdit.getText().toString());

        RaeVO raeVO = tempVo.getRae();

        Integer idRae = getDAO().getRaeDAO().getIdRae(raeVO);

        if (idRae == null) {
            getDAO().getRaeDAO().save(raeVO);
            idRae = getDAO().getRaeDAO().getIdRae(raeVO);
        }

        raeVO.setId(idRae);

        tempVo.setRae(raeVO);
        tempVo.getCabecalho().setHoraTermino(horaTermino);

        AbastecimentoVO cabVo = tempVo.getCabecalho();

        for (AbastecimentoVO absVo : tempVo.getAbastecimentos()) {

            getDAO().getAbastecimentoDAO().save(raeVO, cabVo, absVo);

            if (absVo.getLubrificacaoDetalhes() != null && !absVo.getLubrificacaoDetalhes().isEmpty()) {

                for (LubrificacaoDetalheVO lubVo : absVo.getLubrificacaoDetalhes()) {

                    lubVo.setIdCombustivelLubrificante(absVo.getCombustivelLubrificante().getId());

                    if (Double.valueOf(lubVo.getQtd()) > 0 || (lubVo.getObservacao() != null && !lubVo.getObservacao().equals(getStr(R.string.EMPTY)))) {

                        getDAO().getLubrificacaoDetalhesDAO().save(raeVO, cabVo, lubVo);
                    }
                }
            }
        }

        getAbsTempFile().delete();

        //remove o abastecimento da previa de horimetros
        getDAO().getAbastecimentoTempDAO().delete(cabVo.getEquipamento().getId());

        redirect(AbastecimentosSearchActivity.class, true);
    }

    private void saveAbastecimentos(String senha) throws Exception {

        if (operador.getSenha() != null && operador.getSenha().trim().equals(senha.trim()))
            saveAbastecimentos();
        else
            Util.viewMessage(currentContext, getStr(R.string.ALERT_SENHA_INVALIDA));
    }

    public void createGridBody(final GridBody gridBody) {

        TableLayout table = (TableLayout) findViewById(gridBody.getIdTable());
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int row = 0;

        for (String[] values : gridBody.getValues()) {

            View newRow = inflater.inflate(gridBody.getFileLayoutRow(), null);

            int i = 0;

            for (int idColumn : gridBody.getIdsXmlColumn()) {

                Integer color = gridBody.getColorTXT();

                Button column = (Button) newRow.findViewById(idColumn);

                String txt = values[gridBody.getColumnsTxt()[i++]];
                column.setText(txt);
                column.setTextColor(color);
                column.setBackground(null);

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

            String[] arrayValues = new String[]{abs.getCombustivelLubrificante().getDescricao(),
                    abs.getQtd() + " " + abs.getCombustivelLubrificante().getUnidadeMedida()};

            values.add(arrayValues);

        }

        return values;
    }


    @Override
    public GridHeader getGridHeaderTopValues() throws Exception {
        return null;
    }


}
