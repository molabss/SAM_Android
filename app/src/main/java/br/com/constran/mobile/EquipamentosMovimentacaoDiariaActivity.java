package br.com.constran.mobile;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import br.com.constran.mobile.exception.AlertException;
import br.com.constran.mobile.persistence.vo.ObraVO;
import br.com.constran.mobile.persistence.vo.aprop.mov.EquipamentoMovimentacaoDiariaVO;
import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;
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

public final class EquipamentosMovimentacaoDiariaActivity extends AbstractActivity implements InterfaceEditActivity, InterfaceListActivity {

    private AutoCompleteTextView equipamentoList;
    private Button btAddEquipamento, btAddEquipQRCode, btConsulta;
    private ObraVO obraVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.movimentacoes_diarias_body);

        currentClass = EquipamentosMovimentacaoDiariaActivity.class;
        currentContext = EquipamentosMovimentacaoDiariaActivity.this;

        try {
            init();
            initAtributes();
            editValues();
            initEvents();

            createDetail(getDetailValues());
            createGridHeader(getGridHeaderValues());
            createGridBody(getGridBodyValues());
            createGridFooter(getGridFooterValues());

            if ("S".equalsIgnoreCase(obraVO.getUsaQRCode())) {
                equipamentoList.setEnabled(false);
            }

        } catch (Exception e) {
            tratarExcecao(e);
        }

    }

    @Override
    public void initAtributes() throws Exception {
        equipamentoList = (AutoCompleteTextView) findViewById(R.id.movActEqp);
        btAddEquipamento = (Button) findViewById(R.id.btAddEqp);
        btAddEquipQRCode = (Button) findViewById(R.id.btAddEqpQRCode);
        btConsulta = (Button) findViewById(R.id.btConsVgs);
    }

    @Override
    public void editValues() throws Exception {
        obraVO = getDAO().getObraDAO().getById(config.getIdObra());

        equipamentoArrayVO = getDAO().getEquipamentoDAO().getArrayEquipamentoMovimentacaoDiariaVO();

        ArrayAdapter<EquipamentoVO> adp = new ArrayAdapter<EquipamentoVO>(this, android.R.layout.select_dialog_singlechoice, equipamentoArrayVO);
        adp.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        equipamentoList.setAdapter(adp);

        exibirBotaoAdd();
    }

    private void exibirBotaoAdd() {
        if ("S".equalsIgnoreCase(obraVO.getUsaQRCode())) {
            btAddEquipamento.setVisibility(View.GONE);
            btAddEquipQRCode.setVisibility(View.VISIBLE);
        } else {
            btAddEquipamento.setVisibility(View.VISIBLE);
            btAddEquipQRCode.setVisibility(View.GONE);
        }
    }

    @Override
    public void initEvents() throws Exception {

        equipamentoList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EquipamentoVO e = (EquipamentoVO) parent.getItemAtPosition(position);
                idEquipamento = e.getId();
            }
        });

        equipamentoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                idEquipamento = null;
            }
        });

        equipamentoList.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                idEquipamento = null;
                return false;
            }
        });


        btAddEquipamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {

                    if (idEquipamento == null) {
                        throw new AlertException(getStr(R.string.ALERT_SELECT_EQP));
                    }

                    EquipamentoMovimentacaoDiariaVO vo = new EquipamentoMovimentacaoDiariaVO();
                    vo.setIdEquipamento(idEquipamento);
                    vo.setDataHora(Util.getNow());

                    saveOrUpdate(vo);
                    redirect(currentClass, false);
                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }
        });

        btAddEquipQRCode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(EquipamentosMovimentacaoDiariaActivity.this, ZBarScannerActivity.class);
                    startActivityForResult(intent, QR_CODE_REQUEST);
                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }
        });

        btConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    redirect(ViagensSearchActivity.class, false);
                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }
        });

        equipamentoList.setOnKeyListener(equipamentoListener());
    }

    protected OnKeyListener equipamentoListener() {
        return new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                idEquipamento = null;
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                return false;
            }
        };
    }

    @Override
    public Cursor getCursor() throws Exception {
        return getDAO().getEquipamentoDAO().getCursorEquipamentosMovimentacaoDiaria(true);
    }

    @Override
    public GridBody getGridBodyValues() throws Exception {
        Integer noContent = null;
        Integer indexCursorIdEquipamento = 0;
        Integer indexCursorDescricao = 1;

        GridBody grid = new GridBody(this);
        grid.setClassVO(EquipamentoMovimentacaoDiariaVO.class);
        grid.setClassRedirect(ApropriacaoMovimentacaoActivity.class);
        grid.setClassRefresh(EquipamentosMovimentacaoDiariaActivity.class);
        grid.setColorTXT(getColor(R.color.BLACK));
        grid.setColorsBKG(new Integer[]{getColor(R.color.WHITE), getColor(R.color.GRAY3)});
        grid.setMsgNotRemove(getStr(R.string.ALERT_EXISTS_CHILDS_MOV));
        grid.setColumnsTxt(new Integer[]{indexCursorDescricao, noContent});
        grid.setCursor(getCursor());
        grid.setIndexsPKRow(new Integer[]{indexCursorIdEquipamento});
        grid.setReferencesImage(new Integer[]{noContent, R.drawable.delete});
        grid.setIdTable(References.GRID_ID_MOV);
        grid.setFileLayoutRow(References.GRID_LAYOUT_MOV);
        grid.setIdsXmlColumn(References.GRID_ID_COLUMNS_MOV);
        grid.setTypesColumn(new String[]{getStr(R.string.TYPE_NEXT_PAGE), getStr(R.string.TYPE_REMOVE)});

        return grid;
    }

    @Override
    public GridHeader getGridHeaderValues() throws Exception {
        GridHeader header = new GridHeader(this);
        header.setColorBKG(getColor(R.color.BLACK));// Cor de fundo
        header.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        header.setFileLayoutRow(References.HEADER_LAYOUT_MOV); //arquivo xml - layout (TableRow)
        header.setIdColumns(References.HEADER_ID_COLUMNS_MOV);// Ids (xml) das colunas
        header.setIdTable(References.HEADER_ID_MOV); //Id do TableLayout);
        header.setNameColumns(getStrArray(R.array.HEADER_NAME_COLUMNS_MOV));//Nomes das Colunas

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
        redirectPrincipal();
    }

    @Override
    public GridHeader getGridHeaderTopValues() {
        return null;
    }

    @Override
    public void validateFields() throws Exception {
    }

    @Override
    public void editScreen() throws Exception {
    }

    @Override
    protected void trataDadosQRCode() throws AlertException {
        EquipamentoMovimentacaoDiariaVO vo = new EquipamentoMovimentacaoDiariaVO();

        // Primeira implentação foi usado o idEquipamento no QRCode, agora passa a ser o campo QRCode da tabela Equipamento
        // porém é buscado o idEquipamento na tabela atravez do campo QRCode para dar continuidade ao processamento
        // ja desenvolvido nas outras telas (usando o idEquipamento)
        int codigoQrCode = idEquipamento;
        EquipamentoVO equip = getDAO().getEquipamentoDAO().getByQRCode(codigoQrCode);

        if(equip != null) {
            idEquipamento = equip.getId();
            intentParameters.setIdRegistroPai(String.valueOf(idEquipamento));
            intentParameters.setIdEquipamento(idEquipamento);
            intentParameters.setFromQRCode(true);

            vo.setIdEquipamento(idEquipamento);
            vo.setDataHora(Util.getNow());
            saveOrUpdate(vo);
            redirect(ApropriacaoMovimentacaoActivity.class, false);
        }else {
            throw new AlertException(getStr(R.string.ERROR_QRCODE_EQUIPAMENTO_INVALIDO));
        }

    }
}

