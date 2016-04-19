package br.com.constran.mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.*;
import br.com.constran.mobile.persistence.vo.ManutencaoEquipamentoVO;
import br.com.constran.mobile.view.AbstractActivity;

import java.util.List;

/**
 * Created by moises_santana on 13/04/2015.
 */
public class ManutencaoBuscaActivity extends AbstractActivity{


    private AutoCompleteTextView manBus_actvEquipamento = null;
    private AutoCompleteTextView manBus_actvData = null;
    private TextView manBus_tvTotal = null;
    private Button manBus_btnBusca = null;
    private ScrollView manBus_scrvItensEncontrados = null;
    private Button manBus_btnLimpa = null;
    private Spinner manBus_spnTipoFiltro = null;
    private List<ManutencaoEquipamentoVO> listaEquipamentos = null;
    private LinearLayout layoutSrollItem = null;
    private ArrayAdapter<String> adapterAutoCompleteEqp = null;
    private String msgAlertaUsr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.manutencao_busca);
        this.setTitle("Manutenções: Buscar manutenções realizadas");

        currentClass = ManutencaoBuscaActivity.class;
        currentContext = ManutencaoBuscaActivity.this;
        initAttributes();
        initEvents();
    }


    public void initAttributes(){

        manBus_spnTipoFiltro = (Spinner)findViewById(R.id.manBus_spnTipoFiltro);
        ArrayAdapter<String> adapterTipoFiltro = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice, new String[]{"Prefixo Eqp.","Serviço efetuado"});
        adapterTipoFiltro.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        manBus_spnTipoFiltro.setAdapter(adapterTipoFiltro);


        manBus_actvData = (AutoCompleteTextView)findViewById(R.id.manBus_actvData);
        ArrayAdapter<String> adapterCampoData = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice, getDAO().getManutencaoEquipamentoDAO().listarDatas());
        adapterTipoFiltro.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        manBus_actvData.setAdapter(adapterCampoData);
        manBus_actvData.setText(adapterCampoData.getItem(0).trim());

        manBus_actvEquipamento = (AutoCompleteTextView)findViewById(R.id.manBus_actvEquipamento);

        manBus_tvTotal = (TextView)findViewById(R.id.manBus_tvTotal);
        manBus_btnBusca = (Button)findViewById(R.id.manBus_btnBusca);
        manBus_btnLimpa = (Button)findViewById((R.id.manBus_btnLimpa));
        manBus_scrvItensEncontrados = (ScrollView)findViewById(R.id.manBus_scrvItensEncontrados);

        layoutSrollItem = (LinearLayout)findViewById(R.id.manBus_scvListaItem);
    }

    public void initEvents(){

        manBus_btnBusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listarEquipamentosManutencionados();
            }
        });

        manBus_btnLimpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limparResultadoBusca();
            }
        });


        manBus_actvEquipamento.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                return false;
            }
        });

        manBus_actvEquipamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
            }
        });

        manBus_actvData.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                return false;
            }
        });


        manBus_actvData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
            }
        });


        manBus_spnTipoFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(parent.getSelectedItem().equals("Prefixo Eqp.")){

                    adapterAutoCompleteEqp = new ArrayAdapter<String>(currentContext,android.R.layout.select_dialog_singlechoice, getDAO().getManutencaoEquipamentoDAO().listarPrefixosEquipamentos());
                    adapterAutoCompleteEqp.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                    manBus_actvEquipamento.setAdapter(adapterAutoCompleteEqp);
                }
                else
                if(parent.getSelectedItem().equals("Serviço efetuado")){

                    adapterAutoCompleteEqp = new ArrayAdapter<String>(currentContext,android.R.layout.select_dialog_singlechoice, getDAO().getManutencaoServicoDAO().listarServicosDeManutencao());
                    adapterAutoCompleteEqp.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                    manBus_actvEquipamento.setAdapter(adapterAutoCompleteEqp);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void listarEquipamentosManutencionados(){

        limparResultadoBusca();

        String data = manBus_actvData.getText().toString().trim();
        String prefixo = null;
        String manutencaoServico = null;


        //SE ALGUM PREFIXO FOR INFORMADO...
        if(manBus_spnTipoFiltro.getSelectedItem().equals("Prefixo Eqp.") && manBus_actvEquipamento.getText().toString().trim().length() > 0 ){

            prefixo = manBus_actvEquipamento.getText().toString().trim();

        }//OU SE ALGUM SERFIVO FOR INFORMADO...
        else if(manBus_spnTipoFiltro.getSelectedItem().equals("Serviço efetuado") && manBus_actvEquipamento.getText().toString().trim().length() > 0){

            manutencaoServico = manBus_actvEquipamento.getText().toString().trim();
        }

        if(data.length() != 10 && (prefixo == null && manutencaoServico == null)){
            msgAlertaUsr = "Você precisa fornecer algum critério para realizar a busca: \"Prefixo do equipamento, Serviço efetuado, data.\"";
        }


        if(msgAlertaUsr.length() > 0){
            AlertDialog.Builder dialogo = new AlertDialog.Builder(currentContext);
            dialogo.setMessage(msgAlertaUsr);
            dialogo.setPositiveButton(getStr(R.string.OK), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface di, int arg) {
                }
            });
            dialogo.setTitle(getStr(R.string.AVISO));
            dialogo.show();
            msgAlertaUsr="";
            return;
        }

        listaEquipamentos = getDAO().getManutencaoEquipamentoDAO().listarTodos(data,prefixo,manutencaoServico);

        manBus_tvTotal.setText("Total ".concat(String.valueOf(listaEquipamentos.size())));

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
            if(descCompletaEquipamento.length() > 39){
                descCompletaEquipamento = descCompletaEquipamento.substring(0,39).concat("...");
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
            //----------------------

            //BOTAO EXLCUIR MANUTENCAO
            buttonParamsC = new LinearLayout.LayoutParams(50, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
            btC = new Button(getBaseContext());
            btC.setLayoutParams(buttonParamsC);
            btC.setBackgroundResource(R.drawable.delete_buttons);
            btC.setOnClickListener(handleOnClickExcluir(btC,item));
            //----------------------

            linLayoutFilho.addView(btA);
            linLayoutFilho.addView(btB);
            linLayoutFilho.addView(btC);

            linLayoutPai.addView(linLayoutFilho);
        }


        layoutSrollItem.addView(linLayoutPai);
    }


    public View.OnClickListener handleOnClickEditar(final Button button, final ManutencaoEquipamentoVO manutencao) {
        return new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(),ManutencaoEquipamentoActivity.class);
                intent.putExtra("manutencao",manutencao);
                startActivity(intent);
            }
        };
    }

    public View.OnClickListener handleOnClickServicos(final Button button, final ManutencaoEquipamentoVO manutencao) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),ManutencaoServicosActivity.class);
                intent.putExtra("manutencao",manutencao);
                startActivity(intent);
            }
        };
    }


    public View.OnClickListener handleOnClickExcluir(final Button button, final ManutencaoEquipamentoVO manutencao) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder dialogo = new AlertDialog.Builder(currentContext);
                dialogo.setMessage(getStr(R.string.ALERT_CONFIRM_REMOVE));
                dialogo.setPositiveButton(getStr(R.string.SIM), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface di, int arg) {

                        getDAO().getManutencaoEquipamentoDAO().excluirRegistro(manutencao);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        listarEquipamentosManutencionados();
    }

    void limparResultadoBusca(){
        layoutSrollItem.removeAllViews();
        manBus_tvTotal.setText("Total 0");
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
