package br.com.constran.mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import br.com.constran.mobile.exception.AlertException;
import br.com.constran.mobile.persistence.vo.ManutencaoEquipamentoVO;
import br.com.constran.mobile.view.AbstractActivity;
import br.com.constran.mobile.view.util.Util;
import org.w3c.dom.Text;

import java.util.Calendar;

/**
 * Created by moises_santana on 17/03/2015.
 */
public class ManutencaoEquipamentoActivity extends AbstractActivity{

    private EditText man_etHoraInicio;
    private EditText man_etMinutoInicio;
    private EditText man_etHorimetro;
    private EditText man_etHodometro;
    private EditText man_etHoraTermino;
    private EditText man_etMinutoTermino;
    private EditText man_etObservacao;
    private Button man_btnSalvar;
    private Button man_btnCancelar;
    private TextView man_tvEquipamentoDesc;
    private ManutencaoEquipamentoVO manutencao;

    private String horaInicio = null;
    private String minutoInicio = null;
    private String horaTermino = null;
    private String minutoTermino = null;

    private boolean validacaoFalhou = false;
    private String msgAlertaUsr = "Não definida";

    private String horimetro = null;
    private String hodometro = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.manutencao_equipamento);
        this.setTitle("Manutenções: Informações do Equipamento");

        currentClass = ManutencaoEquipamentoActivity.class;
        currentContext = ManutencaoEquipamentoActivity.this;

        initAttributes();
        initEvents();
    }


    void initAttributes(){

        man_etHoraInicio = (EditText)findViewById(R.id.man_etHoraInicio);
        man_etMinutoInicio = (EditText)findViewById(R.id.man_etMinutoInicio);
        man_etHorimetro = (EditText)findViewById(R.id.man_etHorimetro);
        man_etHodometro = (EditText)findViewById(R.id.man_etHodometro);
        man_etHoraTermino = (EditText)findViewById(R.id.man_etHoraTermino);
        man_etMinutoTermino = (EditText)findViewById(R.id.man_etMinutoTermino);
        man_etObservacao = (EditText)findViewById(R.id.man_etObservacao);
        man_btnSalvar = (Button)findViewById(R.id.man_btnSalvar);
        man_btnCancelar = (Button)findViewById(R.id.man_btnCancelar);
        man_tvEquipamentoDesc = (TextView)findViewById(R.id.man_tvEquipamentoDesc);

        Intent intent = getIntent();
        manutencao = (ManutencaoEquipamentoVO)intent.getSerializableExtra("manutencao");

        man_tvEquipamentoDesc.setText(manutencao.getEquipamento().getPrefixo()+" - "+manutencao.getEquipamento().getDescricao());

        //SE HORA DE INICIO NAO FOI PREENCHIDA ANTERIORMENTE ENTAO SOFTWARE PREENCHE
        if(manutencao.getHoraInicio().length() == 0){
            man_etHoraInicio.setText(Util.getHoraDoDia());
            man_etMinutoInicio.setText(Util.getMinutoDaHora());

        //SE JA PREEENCHIDA ANTERIOMENTE ENTAO CARREGA NOS CAMPOS A HORA E MINUTO
        }else{
            man_etHoraInicio.setText(manutencao.getHoraInicio().split(":")[0]);
            man_etMinutoInicio.setText(manutencao.getHoraInicio().split(":")[1]);
        }

        //SE A HORA DE TERMINO FOI PREENCHIDO ANTERIORMENTE ENTAO CARREGA NOS CAMPOS A HORA E MINUTO
        if(manutencao.getHoraTermino().length() > 0){
            man_etHoraTermino.setText(manutencao.getHoraTermino().split(":")[0]);
            man_etMinutoTermino.setText(manutencao.getHoraTermino().split(":")[1]);
        }

        man_etHorimetro.setText(manutencao.getHorimetro());
        man_etHodometro.setText(manutencao.getHodometro());
        man_etObservacao.setText(manutencao.getObservacao());
   }

    void initEvents(){


        man_etHoraInicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {     }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (text.length() == 2) {
                    man_etMinutoInicio.requestFocus();
                    man_etHoraInicio.clearFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {     }
        });


        man_etHoraTermino.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {    }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (text.length() == 2) {
                    man_etMinutoTermino.requestFocus();
                    man_etHoraTermino.clearFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {    }
        });

        man_btnSalvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                validacaoFalhou = false;

                horaInicio = man_etHoraInicio.getText().toString();
                minutoInicio = man_etMinutoInicio.getText().toString();
                horaTermino = man_etHoraTermino.getText().toString();
                minutoTermino = man_etMinutoTermino.getText().toString();
                horimetro = man_etHorimetro.getText().toString();
                hodometro = man_etHodometro.getText().toString();

                //HORA DE INICIO TEM QUE SER PREENCHIDA
                if(!Util.isHoraValidaM(getHoraInicio())){
                    validacaoFalhou = true;
                    msgAlertaUsr ="Hora e/ ou minuto de início são inválidos.";
                }
                else //HORA DE TERMINO, QUANDO PREENCHIDA, TEM QUE SER VALIDA E MAIOR QUE HORA DE INICIO E MAIOR QUE A HORA DE TERMINO DO ULTIMO SERVICO EFETUADO
                if(horaTermino.length() > 0 || minutoTermino.length() > 0) {

                    if(naoTemNenhumServico()){
                        validacaoFalhou = true; man_etHoraTermino.setText(""); man_etMinutoTermino.setText("");
                        msgAlertaUsr ="Você não pode finalizar a manutenção deste equipamento. Nenhum serviço foi realizado nele.";
                    }
                    else
                    if( !Util.isHoraValidaM(getHoraTermino())){
                        validacaoFalhou = true;
                        msgAlertaUsr ="Hora e/ ou minuto de término são inválidos.";
                    }
                    else
                    if(!Util.isPeriodoHorasValido(getHoraInicio(), getHoraTermino())){
                        validacaoFalhou = true;
                        msgAlertaUsr ="Hora de término precisa ser maior que hora de início.";
                    }
                    else
                    if(!horaEHposteriorAoUtimoServico()){
                        validacaoFalhou = true;
                        msgAlertaUsr ="Hora de término precisa ser maior que a hora de término do último serviço efetuado.";
                    }
                }
                else //HORIMETRO OU HODOMETRO PRECISAM SER INFORMADOS
                if(horimetro.length() == 0 && hodometro.length() == 0) {
                    validacaoFalhou = true;
                    msgAlertaUsr ="Hodômetro ou Horímetro precisam ser informados.";
                }

                if(validacaoFalhou) {

                    AlertDialog.Builder dialogoD = new AlertDialog.Builder(currentContext);
                    dialogoD.setMessage(msgAlertaUsr);
                    dialogoD.setPositiveButton(getStr(R.string.OK), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface di, int arg) {
                        }
                    });
                    dialogoD.setTitle(getStr(R.string.AVISO));
                    dialogoD.show();
                    return;
                }

                manutencao.setHodometro(man_etHodometro.getText().toString());
                manutencao.setHorimetro(man_etHorimetro.getText().toString());
                manutencao.setHoraInicio(getHoraInicio());
                manutencao.setHoraTermino(getHoraTermino());
                manutencao.setObservacao(man_etObservacao.getText().toString());

                if(manutencao.getEquipamento().getId() > 0 && manutencao.getData().length() > 0){

                    //FAZ UPDATE
                    getDAO().getManutencaoEquipamentoDAO().atualizarAtual(manutencao);
                    msgAlertaUsr = getStr(R.string.ALERT_ATUALIZADO_SUCESSO);

                }else{

                    //FAZ SALVAR NOVO
                    manutencao.setData(Util.getToday());

                    if(getDAO().getManutencaoEquipamentoDAO().jaCadastrado(manutencao.getEquipamento().getId(),manutencao.getData()))
                    {
                        msgAlertaUsr = getStr(R.string.ALERT_EQUIPAMENTO_JA_CADASTRADO_MANUTENCAO);
                    }
                    else
                    {
                        getDAO().getManutencaoEquipamentoDAO().cadastrarNova(manutencao);
                        msgAlertaUsr = getStr(R.string.ALERT_NOVO_FOI_SALVO);
                    }
                }

                AlertDialog.Builder dialogoD = new AlertDialog.Builder(currentContext);
                dialogoD.setMessage(msgAlertaUsr);
                dialogoD.setPositiveButton(getStr(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface di, int arg) {
                        sair();
                    }
                });
                dialogoD.setTitle(getStr(R.string.AVISO));
                dialogoD.show();
            }

        });

        man_btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sair();
            }
        });
    }

    public void sair(){
        ManutencaoEquipamentoActivity.this.finish();
    }

    public String getHoraInicio(){

        Integer hora = null;
        Integer minuto = null;

        try{
            hora = Integer.valueOf(man_etHoraInicio.getText().toString());
            minuto = Integer.valueOf(man_etMinutoInicio.getText().toString());
        }catch (NumberFormatException e){
            return "-1:00";
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

        if (man_etHoraTermino.getText().toString().length() == 0 && man_etMinutoTermino.getText().toString().length() == 0){
            return strHora;
        }

        try{
            hora = Integer.valueOf(man_etHoraTermino.getText().toString());
            minuto = Integer.valueOf(man_etMinutoTermino.getText().toString());
        }catch (NumberFormatException e){
            return "-1:00";
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

    public boolean horaEHposteriorAoUtimoServico(){
        return Util.isPeriodoHorasValido(getDAO().getManutencaoEquipamentoServicoDAO().obterHoraTerminoDoUltimoServico(manutencao.getEquipamento().getId(),Util.getToday()),getHoraTermino());
    }


    public boolean naoTemNenhumServico(){
        return getDAO().getManutencaoEquipamentoServicoDAO().getTotalItemServicos(manutencao.getEquipamento().getId(),Util.getToday()) == 0;
    }

}
