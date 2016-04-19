package br.com.constran.mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.*;
import br.com.constran.mobile.exception.AlertException;
import br.com.constran.mobile.persistence.vo.ManutencaoEquipamentoVO;
import br.com.constran.mobile.persistence.vo.ObraVO;
import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;
import br.com.constran.mobile.qrcode.ZBarScannerActivity;
import br.com.constran.mobile.view.AbstractActivity;
//import com.dm.zbar.android.scanner.ZBarScannerActivity;

import java.util.List;

import static android.view.View.OnClickListener;

/**
 * Created by moises_santana on 18/03/2015.
 */
public class ManutencaoListaEquipamentoActivity extends AbstractActivity  {

    private AutoCompleteTextView man_ActvEquipamentos;
    private Button man_btnAddEquipamento;
    private Button man_btnBuscaEquipamentos;
    private Button man_btnAddEquipamentoQR;
    private ScrollView man_scvListaEquipamentos;
    private EquipamentoVO[] equipamentosPodemFazerManutencaoArray = null;
    private ObraVO obraVO = null;
    List<ManutencaoEquipamentoVO> listaEquipamentos = null;
    LinearLayout layoutSrollItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.manutencao_lista_equipamentos);
        this.setTitle("Manutenções: Equipamentos");


        currentClass = ManutencaoListaEquipamentoActivity.class;
        currentContext = ManutencaoListaEquipamentoActivity.this;

        try{
            init();
            initAttributes();
            initEvents();

            obraVO = getDAO().getObraDAO().getById(config.getIdObra());

            if(obraVO == null){
                AlertDialog.Builder dialogo = new AlertDialog.Builder(currentContext);
                dialogo.setMessage("Você precisa baixar os dados do servidor antes de continuar.");
                dialogo.setPositiveButton(getStr(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface di, int arg) {
                        ManutencaoListaEquipamentoActivity.this.finish();
                    }
                });

                dialogo.setTitle(getStr(R.string.AVISO));
                dialogo.show();
                return;
            }


            carregarEquipamentosDisponiveis();
            exibirBotaoAdd();
            listarEquipamentosManutencionados();

            if ("S".equalsIgnoreCase(obraVO.getUsaQRCode())) {
                man_ActvEquipamentos.setEnabled(false);
            }

        }catch (Exception e){
            tratarExcecao(e);
        }
    }

    void initAttributes(){
        man_ActvEquipamentos = (AutoCompleteTextView)findViewById(R.id.man_ActvEquipamentos) ;
        man_btnAddEquipamento = (Button)findViewById(R.id.man_btnAddEquipamento);
        man_btnBuscaEquipamentos = (Button)findViewById(R.id.man_btnBuscaEquipamentos);
        man_btnAddEquipamentoQR = (Button)findViewById(R.id.man_btnAddEquipamentoQR);
        man_scvListaEquipamentos = (ScrollView)findViewById(R.id.man_scvListaEquipamentos);
    }


    //OBTEM UMA LISTA DE QUIPAMENTOS CADASTRADOS NA TABELA manutencaoEquipamentos
    void listarEquipamentosManutencionados(){

        listaEquipamentos = getDAO().getManutencaoEquipamentoDAO().listarTodos(null,null,null);

        layoutSrollItem = (LinearLayout)findViewById(R.id.man_scvListaItem);

        LinearLayout linLayoutPai = new LinearLayout(getBaseContext());
        linLayoutPai.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams linLayouPaiParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linLayoutPai.setLayoutParams(linLayouPaiParams);
        linLayoutPai.setWeightSum(1);

        LinearLayout linLayoutFilho = null;
        LinearLayout.LayoutParams linLayouFilhoParams = null;

        LinearLayout.LayoutParams buttonParamsA = null;
        LinearLayout.LayoutParams buttonParamsB = null;
        LinearLayout.LayoutParams buttonParamsC = null;

        Button btA = null;
        Button btB = null;
        Button btC = null;

        int index = 0;
        int backColor = R.color.WHITE;
        String descCompletaEquipamento = new String();

        for(ManutencaoEquipamentoVO item : listaEquipamentos){

            index = listaEquipamentos.indexOf(item);

            if(index % 2 == 0){
                backColor = R.drawable.equipamento_buttons_par;
            }else{
                backColor = R.drawable.equipamento_buttons_impar;
            }

            //LINEAR LAYOUT FILHO - REPRESENTA UMA LINHA COM TRES BUTTONS
            linLayoutFilho = new LinearLayout(getBaseContext());
            linLayoutFilho.setOrientation(LinearLayout.HORIZONTAL);
            linLayouFilhoParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            linLayouFilhoParams.topMargin = 5;
            linLayoutFilho.setLayoutParams(linLayouFilhoParams);
            linLayoutFilho.setWeightSum(1);
            linLayoutFilho.setBackgroundResource(backColor);
            //-----------------------------------------------------------

            //AJUSTE PARA CABER DESCRICAO COMPLETA NA TELA DE 7 POLEGADAS
            descCompletaEquipamento = item.getEquipamento().getPrefixo() + " - " + item.getEquipamento().getDescricao();
            if(descCompletaEquipamento.length() > 42){
                descCompletaEquipamento = descCompletaEquipamento.substring(0,42).concat("...");
            }
            //-----------------------------------------------------------

            //BOTAO NOME EQUIPAMENTO
            buttonParamsA = new LinearLayout.LayoutParams(500, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            btA = new Button(getBaseContext());
            btA.setLayoutParams(buttonParamsA);
            btA.setText(descCompletaEquipamento);
            btA.setGravity(Gravity.LEFT);
            btA.setTextSize(18);
            btA.setBackgroundResource(backColor);
            btA.setTextColor(getTextColorBtnEquipamento(item));
            btA.setOnClickListener(handleOnClickEditar(btA,item));
            //-----------------------------------------------------------

            //BOTAO EDITAR SERVICOS
            buttonParamsB = new LinearLayout.LayoutParams(50, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
            btB = new Button(getBaseContext());
            btB.setLayoutParams(buttonParamsB);
            btB.setBackgroundResource(getImageBackBtnEditar(item));
            btB.setOnClickListener(handleOnClickServicos(btB,item));
            //-----------------------------------------------------------

            //BOTAO EXLCUIR MANUTENCAO
            buttonParamsC = new LinearLayout.LayoutParams(50, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
            btC = new Button(getBaseContext());
            btC.setLayoutParams(buttonParamsC);
            btC.setBackgroundResource(R.drawable.delete_buttons);
            btC.setOnClickListener(handleOnClickExcluir(btC,item));
            //-----------------------------------------------------------

            linLayoutFilho.addView(btA);
            linLayoutFilho.addView(btB);
            linLayoutFilho.addView(btC);

            linLayoutPai.addView(linLayoutFilho);
        }

        layoutSrollItem.addView(linLayoutPai);
    }

    public  OnClickListener handleOnClickEditar(final Button button, final ManutencaoEquipamentoVO manutencao) {
        return new OnClickListener() {
            public void onClick(View v) {

            Intent intent = new Intent(getBaseContext(),ManutencaoEquipamentoActivity.class);
            intent.putExtra("manutencao",manutencao);
            startActivity(intent);
            }
        };
    }

    public  OnClickListener handleOnClickServicos(final Button button, final ManutencaoEquipamentoVO manutencao) {
        return new OnClickListener() {
            public void onClick(View v) {
            Intent intent = new Intent(getBaseContext(),ManutencaoServicosActivity.class);
            intent.putExtra("manutencao",manutencao);
            startActivity(intent);
            }
        };
    }


    public  OnClickListener handleOnClickExcluir(final Button button, final ManutencaoEquipamentoVO manutencao) {
        return new OnClickListener() {
            public void onClick(View v) {

            //VERIFICAR ANTES SE HA REGISTROS DE ITENS DE MANUTENCAO
            int totalServicosExecutados = getDAO().getManutencaoEquipamentoServicoDAO().getTotalItemServicos(manutencao.getEquipamento().getId(),manutencao.getData());
            if(totalServicosExecutados > 0){

                AlertDialog.Builder dialogo = new AlertDialog.Builder(currentContext);
                dialogo.setMessage("Há serviços de manutenção realizadas neste equipamento, você não pode excluir este ítem.");
                dialogo.setPositiveButton(getStr(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface di, int arg) {
                    }
                });
                dialogo.setTitle(getStr(R.string.AVISO));
                dialogo.show();
                return;
            }

            AlertDialog.Builder dialogo = new AlertDialog.Builder(currentContext);
            dialogo.setMessage(getStr(R.string.ALERT_CONFIRM_REMOVE));
            dialogo.setPositiveButton(getStr(R.string.SIM), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface di, int arg) {
                    getDAO().getManutencaoEquipamentoDAO().excluirRegistro(manutencao);
                    layoutSrollItem.removeAllViews();
                    listarEquipamentosManutencionados();
                }
            });
            dialogo.setNegativeButton(getStr(R.string.NAO), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface di, int arg) {
                    //faz nada, apenas fecha AlertDialog
                }
            });
            dialogo.setTitle(getStr(R.string.AVISO));
            dialogo.show();
            }
        };
    }

    void initEvents(){

        man_ActvEquipamentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EquipamentoVO e = (EquipamentoVO) parent.getItemAtPosition(position);
                idEquipamento = e.getId();
                man_ActvEquipamentos.setText(man_ActvEquipamentos.getText().toString().trim());
            }
        });

        man_ActvEquipamentos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            ((AutoCompleteTextView) view).setText(getStr(R.string.EMPTY));
                idEquipamento = null;
            }
        });

        //REDIRECIONAR PARA A OUTRA TELA PARA INSERIR OS DEMAIS DADOS DE MANUTENCAO DO
        //EQUIPAMENTO
        man_btnAddEquipamento.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

            try {

                if (idEquipamento == null) {
                    throw new AlertException(getStr(R.string.ALERT_SELECT_EQP));
                }

                EquipamentoVO equipamento = getDAO().getEquipamentoDAO().getById(idEquipamento);
                ManutencaoEquipamentoVO manutencao = new ManutencaoEquipamentoVO();
                manutencao.setEquipamento(equipamento);
                manutencao.setHoraInicio(new String());
                manutencao.setHoraTermino(new String());
                manutencao.setHorimetro(new String());
                manutencao.setHodometro(new String());
                manutencao.setData(new String());

                Intent intentParams = new Intent(ManutencaoListaEquipamentoActivity.this,ManutencaoEquipamentoActivity.class);
                intentParams.putExtra("manutencao",manutencao);
                startActivity(intentParams);

            } catch (Exception e) {
                tratarExcecao(e);
            }
            }
        });

        man_btnAddEquipamentoQR.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
            try {
                Intent intent = new Intent(ManutencaoListaEquipamentoActivity.this, ZBarScannerActivity.class);
                startActivityForResult(intent, QR_CODE_REQUEST);
            } catch (Exception e) {
                tratarExcecao(e);
            }
            }
        });


        man_btnBuscaEquipamentos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

            if(getDAO().getManutencaoEquipamentoDAO().existeItens()){

                Intent intent = new Intent(ManutencaoListaEquipamentoActivity.this, ManutencaoBuscaActivity.class);
                startActivity(intent);
            }
            else{

                AlertDialog.Builder dialogo = new AlertDialog.Builder(currentContext);
                dialogo.setMessage("Não há nenhum equipamento para ser consultado");
                dialogo.setPositiveButton(getStr(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface di, int arg) {
                    }
                });
                dialogo.setTitle(getStr(R.string.AVISO));
                dialogo.show();
            }
            }
        });
    }


    void carregarEquipamentosDisponiveis(){
        //equipamentoArrayVO = getDAO().getEquipamentoDAO().getArrayEquipamentoMovimentacaoDiariaVO();
        equipamentosPodemFazerManutencaoArray = getDAO().getEquipamentoDAO().getArrayEquipamentosManutencaoServicos();
        ArrayAdapter<EquipamentoVO> adp = new ArrayAdapter<EquipamentoVO>(this, android.R.layout.select_dialog_singlechoice, equipamentosPodemFazerManutencaoArray);
        adp.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        man_ActvEquipamentos.setAdapter(adp);
    }

    private void exibirBotaoAdd() {

        if ("S".equalsIgnoreCase(obraVO.getUsaQRCode())) {
            man_btnAddEquipamento.setVisibility(View.GONE);
            man_btnAddEquipamentoQR.setVisibility(View.VISIBLE);
        } else {
            man_btnAddEquipamento.setVisibility(View.VISIBLE);
            man_btnAddEquipamentoQR.setVisibility(View.GONE);
        }
    }

    //REDIRECIONAR PARA A OUTRA TELA PARA INSERIR OS DEMAIS DADOS DE MANUTENCAO DO
    //EQUIPAMENTO
    @Override
    protected void trataDadosQRCode() throws AlertException {
        super.trataDadosQRCode();

        int codigoQrCode = idEquipamento;
        EquipamentoVO equipamento = getDAO().getEquipamentoDAO().getByQRCode(codigoQrCode);
        ManutencaoEquipamentoVO manutencao = new ManutencaoEquipamentoVO();

        if(equipamento != null) {

            idEquipamento = equipamento.getId();

            manutencao.setEquipamento(equipamento);
            manutencao.setHoraInicio(new String());
            manutencao.setHoraTermino(new String());
            manutencao.setHorimetro(new String());
            manutencao.setHodometro(new String());
            manutencao.setData(new String());

            Intent intentParams = new Intent(ManutencaoListaEquipamentoActivity.this,ManutencaoEquipamentoActivity.class);
            intentParams.putExtra("manutencao",manutencao);
            startActivity(intentParams);

        }else {
            throw new AlertException(getStr(R.string.ERROR_QRCODE_EQUIPAMENTO_INVALIDO));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        layoutSrollItem.removeAllViews();
        listarEquipamentosManutencionados();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(!"Selecione".equals(man_ActvEquipamentos.getText().toString())){
            man_ActvEquipamentos.setText("");
        }
    }


    public int getTextColorBtnEquipamento(ManutencaoEquipamentoVO item){

        if(getDAO().getManutencaoEquipamentoDAO().faltaHoraDeTermino(item.getEquipamento().getId(),item.getData())){
            return Color.RED;
        }
        return Color.BLACK;
    }


    public int getImageBackBtnEditar(ManutencaoEquipamentoVO item){

        if(getDAO().getManutencaoEquipamentoServicoDAO().getTotalItemServicos(item.getEquipamento().getId(),item.getData()) == 0){
            return R.drawable.form_buttons_zero_itens;
        }

        return R.drawable.form_buttons;
    }


}