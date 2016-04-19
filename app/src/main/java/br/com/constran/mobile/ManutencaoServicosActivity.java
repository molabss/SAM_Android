package br.com.constran.mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.*;
import br.com.constran.mobile.persistence.vo.ManutencaoEquipamentoServicosVO;
import br.com.constran.mobile.persistence.vo.ManutencaoEquipamentoVO;
import br.com.constran.mobile.persistence.vo.ManutencaoServicoPorCategoriaEquipamentoVO;
import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;
import br.com.constran.mobile.view.AbstractActivity;
import br.com.constran.mobile.view.util.Util;

import java.util.List;

/**
 * Created by moises_santana on 02/04/2015.
 */
public class ManutencaoServicosActivity extends AbstractActivity{

    private EditText manSer_etHoraInicio;
    private EditText manSer_etMinutoInicio;
    private EditText manSer_etHoraTermino;
    private EditText manSer_etMinutoTermino;
    private EditText manSer_etObservacao;
    private Button manSer_btnNovo;
    private Button manSer_btnSalvar;
    private Button manSer_btnCancelar;
    private Button manSer_btnExcluir;

    private Spinner manSer_spnServicos;
    private GridLayout manSer_gridLayCadaServico;
    private ManutencaoEquipamentoVO manutencao;
    private TextView manSer_tvEquipamentoDesc;
    private ManutencaoServicoPorCategoriaEquipamentoVO[] servicosDisponiveisPorEquipamento;
    private ArrayAdapter<ManutencaoServicoPorCategoriaEquipamentoVO> arrAdpServicos = null;
    private ManutencaoEquipamentoServicosVO servicoRealizadoAgora = null;



    private boolean validacaoFalhou = false;
    private boolean registroNovo = true;
    private String msgAlertaUsr = "Não definida";

    private String horaInicio = null;
    private String minutoInicio = null;
    private String horaTermino = null;
    private String minutoTermino = null;

    private TextView tvA = null;
    private TextView tvB = null;
    private TextView tvC = null;
    private Button btnEdit = null;
    private GridLayout.LayoutParams gridLayParams = null;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.manutencao_servicos);
        this.setTitle("Manutenções: Serviços realizados");

        currentClass = ManutencaoServicosActivity.class;
        currentContext = ManutencaoServicosActivity.this;
        initAttributes();
        initEvents();
        listarServicosEfetuados();
    }

    public void initAttributes(){

        manSer_etHoraInicio = (EditText)findViewById(R.id.manSer_etHoraInicio);         manSer_etHoraInicio.setText(new String());
        manSer_etMinutoInicio = (EditText)findViewById(R.id.manSer_etMinutoInicio);     manSer_etMinutoInicio.setText(new String());
        manSer_etHoraTermino = (EditText)findViewById(R.id.manSer_etHoraTermino);       manSer_etHoraTermino.setText(new String());
        manSer_etMinutoTermino = (EditText)findViewById(R.id.manSer_etMinutoTermino);   manSer_etMinutoTermino.setText(new String());

        manSer_spnServicos = (Spinner)findViewById(R.id.manSer_spnServicos);
        manSer_etObservacao = (EditText)findViewById(R.id.manSer_etObservacao);
        manSer_btnNovo = (Button)findViewById(R.id.manSer_btnNovo);
        manSer_btnSalvar = (Button)findViewById(R.id.manSer_btnSalvar);
        manSer_btnCancelar = (Button)findViewById(R.id.manSer_btnCancelar);
        manSer_btnExcluir = (Button)findViewById(R.id.manSer_btnExcluir);
        manSer_gridLayCadaServico = (GridLayout)findViewById(R.id.manSer_gridLayCadaServico);
        manSer_tvEquipamentoDesc = (TextView)findViewById(R.id.manSer_tvEquipamentoDesc);

        Intent intent = getIntent();
        manutencao = (ManutencaoEquipamentoVO)intent.getSerializableExtra("manutencao");

        servicosDisponiveisPorEquipamento = getDAO().getManutencaoServicoPorCategoriaEquipamentoDAO().listarPorCategoriaDeEquipamento(String.valueOf(manutencao.getEquipamento().getId()));
        arrAdpServicos = new ArrayAdapter<ManutencaoServicoPorCategoriaEquipamentoVO>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, servicosDisponiveisPorEquipamento);
        arrAdpServicos.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        manSer_spnServicos.setAdapter(arrAdpServicos);
        manSer_spnServicos.setSelection(selecioneItemSpinnerPadrao());

        manSer_tvEquipamentoDesc.setText(manutencao.getEquipamento().getPrefixo().concat(" - ").concat(manutencao.getEquipamento().getDescricao()));

        ligarBtn(manSer_btnNovo, true);
        ligarBtn(manSer_btnSalvar,false);
        ligarBtn(manSer_btnCancelar,false);
        ligarBtn(manSer_btnExcluir,false);
        habilitarForm(false);
    }

    public void carregarServicosJaExecutados(){
        List<ManutencaoEquipamentoServicosVO> listaDeServicosFeitos = getDAO().getManutencaoEquipamentoServicoDAO().listarPorEquipamento(manutencao.getEquipamento().getId(),manutencao.getData());
        manutencao.setServicos(listaDeServicosFeitos);
    }

    public void initEvents(){

        manSer_etHoraInicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {     }
            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (text.length() == 2) {
                    manSer_etMinutoInicio.requestFocus();
                    manSer_etHoraInicio.clearFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {        }
        });

        manSer_etHoraTermino.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {     }
            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (text.length() == 2) {
                    manSer_etMinutoTermino.requestFocus();
                    manSer_etHoraTermino.clearFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {        }
        });

        manSer_btnNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ligarBtn(manSer_btnNovo,false);
                ligarBtn(manSer_btnSalvar,true);
                ligarBtn(manSer_btnCancelar,true);
                limparFormulario();
                habilitarForm(true);
                registroNovo = true;
                servicoRealizadoAgora = new ManutencaoEquipamentoServicosVO();
                manSer_spnServicos.setSelection(selecioneItemSpinnerPadrao());
            }
        });

        manSer_btnSalvar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                horaInicio = manSer_etHoraInicio.getText().toString();
                minutoInicio = manSer_etMinutoInicio.getText().toString();
                horaTermino = manSer_etHoraTermino.getText().toString();
                minutoTermino = manSer_etMinutoTermino.getText().toString();

                if(horaInicio.length() == 0 || minutoInicio.length() == 0 || horaTermino.length() == 0 || minutoTermino.length() == 0){
                    validacaoFalhou = true;
                    msgAlertaUsr ="|Hora ou minuto de início| ou |hora ou minuto de término| não foram informados.";
                }
                else
                if(!Util.isHoraValida(getHoraInicio()) || !Util.isHoraValida(getHoraTermino())){
                   validacaoFalhou = true;
                   msgAlertaUsr ="Hora de início ou hora término são inválidas.";
                }
                else
                if(!Util.isPeriodoHorasValido(getHoraInicio(),getHoraTermino())){
                    validacaoFalhou = true;
                    msgAlertaUsr = "Hora de término precisa ser maior que hora de início.";
                }
                else
                if(!horaEHposteriorAentradaDoEquipamento(manutencao.getEquipamento().getId(),manutencao.getData(),getHoraInicio())){
                    validacaoFalhou = true;
                    msgAlertaUsr = "Hora de início é anterior ou igual à hora que o equipamento entrou em manutenção. Favor informar uma hora posterior.";
                }


                if(getIDitemItemSpinnerServicos() == -1){
                    validacaoFalhou = true;
                    msgAlertaUsr = "Selecione um dos serviços de manutenção disponíveis.";
                }


                if(validacaoFalhou){

                    AlertDialog.Builder dialogoC = new AlertDialog.Builder(currentContext);
                    dialogoC.setMessage(msgAlertaUsr);
                    dialogoC.setPositiveButton(getStr(R.string.OK), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface di, int arg) {
                        }
                    });
                    dialogoC.setTitle(getStr(R.string.AVISO));
                    dialogoC.show();
                    validacaoFalhou = false;
                    return;
                }

                servicoRealizadoAgora.setHoraInicio(getHoraInicio());
                servicoRealizadoAgora.setHoraTermino(getHoraTermino());
                servicoRealizadoAgora.setIdServicoCategoriaEquipamento(getIDitemItemSpinnerServicos());
                servicoRealizadoAgora.setObservacao(manSer_etObservacao.getText().toString());
                servicoRealizadoAgora.setIdEquipamento(manutencao.getEquipamento().getId());
                servicoRealizadoAgora.setData(manutencao.getData());

                if(registroNovo){
                    //salvar novo
                    getDAO().getManutencaoEquipamentoServicoDAO().cadastrarNovo(servicoRealizadoAgora);

                }else{
                    //atualizar
                    getDAO().getManutencaoEquipamentoServicoDAO().atualizarAtual(servicoRealizadoAgora);
                }

                ligarBtn(manSer_btnNovo,true);
                ligarBtn(manSer_btnSalvar,false);
                ligarBtn(manSer_btnCancelar,false);
                ligarBtn(manSer_btnExcluir,false);
                limparFormulario();
                habilitarForm(false);
                listarServicosEfetuados();
            }
        });

        manSer_btnCancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ligarBtn(manSer_btnNovo,true);
                ligarBtn(manSer_btnSalvar,false);
                ligarBtn(manSer_btnCancelar,false);
                ligarBtn(manSer_btnExcluir,false);
                limparFormulario();
                habilitarForm(false);
            }
        });

        manSer_btnExcluir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogo = new AlertDialog.Builder(currentContext);
                dialogo.setMessage(getStr(R.string.ALERT_CONFIRM_REMOVE));
                dialogo.setPositiveButton(getStr(R.string.SIM), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface di, int arg) {
                        getDAO().getManutencaoEquipamentoServicoDAO().excluir(servicoRealizadoAgora);
                        listarServicosEfetuados();
                        ligarBtn(manSer_btnNovo,true);
                        ligarBtn(manSer_btnSalvar,false);
                        ligarBtn(manSer_btnCancelar,false);
                        ligarBtn(manSer_btnExcluir,false);
                        limparFormulario();
                        habilitarForm(false);
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
        });
    }

    public void listarServicosEfetuados(){

        manSer_gridLayCadaServico.removeAllViews();
        carregarServicosJaExecutados();

        index = 0;

        for(ManutencaoEquipamentoServicosVO servico : manutencao.getServicos()){

            index = manutencao.getServicos().indexOf(servico);

            gridLayParams = new GridLayout.LayoutParams(GridLayout.spec(index),GridLayout.spec(0));
            gridLayParams.setGravity(Gravity.LEFT|Gravity.CENTER);
            tvA = new TextView(getBaseContext());
            tvA.setLayoutParams(gridLayParams);
            tvA.setText(servico.getHoraInicio());
            tvA.setTextSize(18);
            tvA.setTypeface(null, Typeface.BOLD);
            tvA.setTextColor(Color.BLACK);
            tvA.setPadding(5,10,5,10);
            manSer_gridLayCadaServico.addView(tvA);
            //-----------------x------------------------------------------x-----------------------

            gridLayParams = new GridLayout.LayoutParams(GridLayout.spec(index),GridLayout.spec(1));
            gridLayParams.setGravity(Gravity.LEFT|Gravity.CENTER);
            tvB = new TextView(getBaseContext());
            tvB.setLayoutParams(gridLayParams);
            tvB.setText(servico.getHoraTermino());
            tvB.setTextSize(18);
            tvB.setTypeface(null, Typeface.BOLD);
            tvB.setTextColor(Color.BLACK);
            tvB.setPadding(30, 10, 30, 10);
            manSer_gridLayCadaServico.addView(tvB);
            //------------------x-----------------------------------------x------------------------

            gridLayParams = new GridLayout.LayoutParams(GridLayout.spec(index),GridLayout.spec(2));
            gridLayParams.setGravity(Gravity.LEFT|Gravity.CENTER);
            tvC = new TextView(getBaseContext());
            tvC.setLayoutParams(gridLayParams);
            tvC.setText(servico.getManutencaoServicoPorCategoriaEquipamento().getManutencaoServico().getDescricao());
            tvC.setTextSize(18);
            tvC.setTypeface(null, Typeface.BOLD);
            tvC.setTextColor(Color.BLACK);
            tvC.setPadding(20, 10, 20, 10);
            manSer_gridLayCadaServico.addView(tvC);
            //-------------------x----------------------------------------x-------------------------

            gridLayParams = new GridLayout.LayoutParams(GridLayout.spec(index),GridLayout.spec(3));
            gridLayParams.setGravity(Gravity.RIGHT|Gravity.CENTER);
            btnEdit = new Button(getBaseContext());
            btnEdit.setLayoutParams(gridLayParams);
            btnEdit.setBackgroundResource(R.drawable.edit_buttons);
            btnEdit.setOnClickListener(handleOnClickEditar(btnEdit,servico));
            manSer_gridLayCadaServico.addView(btnEdit);
            //-------------------x----------------------------------------x--------------------------
        }
    }

    public View.OnClickListener handleOnClickEditar(final Button button, final ManutencaoEquipamentoServicosVO servico) {
        return new View.OnClickListener() {
            public void onClick(View v) {

                registroNovo = false;
                habilitarForm(true);

                servicoRealizadoAgora = new ManutencaoEquipamentoServicosVO();
                servicoRealizadoAgora.setIdEquipamento(servico.getIdEquipamento());
                servicoRealizadoAgora.setEquipamento(servico.getEquipamento());
                servicoRealizadoAgora.setData(servico.getData());
                servicoRealizadoAgora.setId(servico.getId());

                try{
                    manSer_etHoraInicio.setText(servico.getHoraInicio().split(":")[0]);
                    manSer_etMinutoInicio.setText(servico.getHoraInicio().split(":")[1]);

                    manSer_etHoraTermino.setText(servico.getHoraTermino().split(":")[0]);
                    manSer_etMinutoTermino.setText(servico.getHoraTermino().split(":")[1]);

                }catch (ArrayIndexOutOfBoundsException e){
                    Toast.makeText(currentContext,"Campos de hora início e hora termino são inconsistentes!",Toast.LENGTH_SHORT).show();
                }

                manSer_spnServicos.setSelection(selecioneItemSpinnerServicos(servico.getManutencaoServicoPorCategoriaEquipamento()));

                manSer_etObservacao.setText(servico.getObservacao());

                ligarBtn(manSer_btnNovo,false);
                ligarBtn(manSer_btnSalvar,true);
                ligarBtn(manSer_btnCancelar,true);
                ligarBtn(manSer_btnExcluir,true);
            }
        };
    }

    public void ligarBtn(Button btn, boolean ligue){
        if(ligue) {
            btn.setVisibility(View.VISIBLE);
        }else{
            btn.setVisibility(View.GONE);
        }
    }

    public void limparFormulario(){
        manSer_etHoraInicio.setText(null);
        manSer_etMinutoInicio.setText(null);
        manSer_etHoraTermino.setText(null);
        manSer_etMinutoTermino.setText(null);
        manSer_etObservacao.setText(null);
        manSer_spnServicos.setSelection(0);
    }

    public void habilitarForm(boolean habilitar){
        manSer_etHoraInicio.setEnabled(habilitar);
        manSer_etMinutoInicio.setEnabled(habilitar);
        manSer_etHoraTermino.setEnabled(habilitar);
        manSer_etMinutoTermino.setEnabled(habilitar);
        manSer_etObservacao.setEnabled(habilitar);
        manSer_spnServicos.setEnabled(habilitar);
    }

    public int selecioneItemSpinnerServicos(ManutencaoServicoPorCategoriaEquipamentoVO item){
        int posicao = -1;
        for(int i = 0; i < arrAdpServicos.getCount(); i++){
            if(arrAdpServicos.getItem(i).getManutencaoServico().getDescricao().equals(item.getManutencaoServico().getDescricao())){
                posicao = i;
                break;
            }
        }
        return posicao;
    }


    public int selecioneItemSpinnerPadrao(){
        int posicao = -1;
        for(int i = 0; i < arrAdpServicos.getCount(); i++){
            if(arrAdpServicos.getItem(i).getManutencaoServico().getDescricao().equals("Selecione")){
                posicao = i;
                break;
            }
        }
        return posicao;
    }


    public Integer getIDitemItemSpinnerServicos(){
        ManutencaoServicoPorCategoriaEquipamentoVO item = (ManutencaoServicoPorCategoriaEquipamentoVO)manSer_spnServicos.getSelectedItem();

        Integer idServicoCategoriaEquipamento = null;
        for(int i = 0; i < arrAdpServicos.getCount(); i++){

            if(arrAdpServicos.getItem(i).getManutencaoServico().getDescricao().equals(item.getManutencaoServico().getDescricao())){

                idServicoCategoriaEquipamento = item.getId();
                break;
            }
        }
        return idServicoCategoriaEquipamento;
    }


    public String getHoraInicio(){
        Integer hora = null;
        Integer minuto = null;

        try{
            hora = Integer.valueOf(manSer_etHoraInicio.getText().toString());
            minuto = Integer.valueOf(manSer_etMinutoInicio.getText().toString());
        }catch (NumberFormatException e){
            return "00:00";
        }

        String strHora = null;
        String strMinuto = null;

        if(hora<10){
            strHora = "0".concat(hora.toString());
        }else{
            strHora = hora.toString();
        }

        if(minuto<10){
            strMinuto = "0".concat(minuto.toString());
        }else{
            strMinuto = minuto.toString();
        }

        return strHora.concat(":").concat(strMinuto);
    }

    public String getHoraTermino() {
        String strHora = "";
        String strMinuto = "";
        Integer hora = null;
        Integer minuto = null;

        if (manSer_etHoraTermino.getText().toString().length() == 0 && manSer_etMinutoTermino.getText().toString().length() == 0){
            return strHora;
        }

        try{
            hora = Integer.valueOf(manSer_etHoraTermino.getText().toString());
            minuto = Integer.valueOf(manSer_etMinutoTermino.getText().toString());
        }catch (NumberFormatException e){
            return "00:00";
        }

        if(hora<10){
            strHora = "0".concat(hora.toString());
        }else{
            strHora = hora.toString();
        }

        if(minuto<10){
            strMinuto = "0".concat(minuto.toString());
        }else{
            strMinuto = minuto.toString();
        }

        return strHora.concat(":").concat(strMinuto);
    }


    boolean horaEHposteriorAentradaDoEquipamento(Integer idEquipamento, String data, String horaInicio){
        return Util.isPeriodoHorasValido(getDAO().getManutencaoEquipamentoDAO().obterHoraQueEquipamentoEntrouNaOficina(idEquipamento,data),horaInicio);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listarServicosEfetuados();
    }
}