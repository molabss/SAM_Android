package br.com.constran.mobile;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.constran.mobile.system.SharedPreferencesHelper;
import br.com.constran.mobile.view.AbstractActivity;

/**
 * Created by moises_santana on 12/05/2015.
 */
public class ModulosActivity extends Activity {

    private CheckBox mod_chbMovimentacoes;
    private CheckBox mod_chbEquipamentos;
    private CheckBox mod_chbMaoDeObra;
    private CheckBox mod_chbAbastecimentos;
    private CheckBox mod_chbIndicesPluviometricos;
    private CheckBox mod_chbManutencoes;
    private List<CheckBox> chkboxList = new ArrayList<CheckBox>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.modulos);
        this.setTitle("Módulos");
        SharedPreferencesHelper.AppModulo.CONTEXT = this;
        initAttributes();
        initEvents();
    }


    public void initAttributes(){
        mod_chbMovimentacoes = (CheckBox) findViewById(R.id.mod_chbMovimentacoes);
        mod_chbMovimentacoes.setChecked(SharedPreferencesHelper.AppModulo.estaMovimentacoesAtivado());

        mod_chbEquipamentos = (CheckBox) findViewById(R.id.mod_chbEquipamentos);
        mod_chbEquipamentos.setChecked(SharedPreferencesHelper.AppModulo.estaEquipamentosAtivado());

        mod_chbMaoDeObra = (CheckBox) findViewById(R.id.mod_chbMaoDeObra);
        mod_chbMaoDeObra.setChecked(SharedPreferencesHelper.AppModulo.estaMaoDeObraAtivado());

        mod_chbAbastecimentos = (CheckBox) findViewById(R.id.mod_chbAbastecimentos);
        mod_chbAbastecimentos.setChecked(SharedPreferencesHelper.AppModulo.estaAbastecimentosAtivado());

        mod_chbIndicesPluviometricos = (CheckBox) findViewById(R.id.mod_chbIndicesPluviometricos);
        mod_chbIndicesPluviometricos.setChecked(SharedPreferencesHelper.AppModulo.estaIndicesPluviometricosAtivado());

        mod_chbManutencoes = (CheckBox) findViewById(R.id.mod_chbManutencoes);
        mod_chbManutencoes.setChecked(SharedPreferencesHelper.AppModulo.estaManutencoesAtivado());


        Log.i("manutencao ativado",SharedPreferencesHelper.AppModulo.estaManutencoesAtivado()+"");


        atualizarListaCheckBox();
    }


    public void initEvents() {

        final Toast toast = Toast.makeText(getBaseContext(),"Pelo menos um módulo deve estar disponível.",Toast.LENGTH_LONG);

        mod_chbMovimentacoes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ativando) {

                if( (podeDesativar() && !ativando) || ativando){
                    SharedPreferencesHelper.AppModulo.setMovimentacoesAtivado(ativando);
                }
                else{
                    toast.show();
                    mod_chbMovimentacoes.setChecked(true);
                }
            }
        });


        mod_chbEquipamentos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ativando) {

                if( (podeDesativar() && !ativando) || ativando){
                    SharedPreferencesHelper.AppModulo.setEquipamentosAtivado(ativando);
                }
                else{
                    toast.show();
                    mod_chbEquipamentos.setChecked(true);
                }
            }
        });

        mod_chbMaoDeObra.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ativando) {

                if( (podeDesativar() && !ativando) || ativando){
                    SharedPreferencesHelper.AppModulo.setMaoDeObraAtivado(ativando);
                }
                else{
                    toast.show();
                    mod_chbMaoDeObra.setChecked(true);
                }
            }
        });

        mod_chbAbastecimentos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ativando) {

                if( (podeDesativar() && !ativando) || ativando){
                    SharedPreferencesHelper.AppModulo.setAbastecimentosAtivado(ativando);
                }
                else{
                    toast.show();
                    mod_chbAbastecimentos.setChecked(true);
                }
            }
        });

        mod_chbIndicesPluviometricos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ativando) {

                if( (podeDesativar() && !ativando) || ativando){
                    SharedPreferencesHelper.AppModulo.setIndicesPluviometricosAtivado(ativando);
                }
                else{
                    toast.show();
                    mod_chbIndicesPluviometricos.setChecked(true);
                }
            }
        });

        mod_chbManutencoes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ativando) {

                if( (podeDesativar() && !ativando) || ativando){
                    SharedPreferencesHelper.AppModulo.setManutencoesAtivado(ativando);
                }
                else{
                    toast.show();
                    mod_chbManutencoes.setChecked(true);
                }
            }
        });
    }


    private void atualizarListaCheckBox(){
        chkboxList.clear();
        chkboxList.add(mod_chbMovimentacoes);
        chkboxList.add(mod_chbEquipamentos);
        chkboxList.add(mod_chbMaoDeObra);
        chkboxList.add(mod_chbAbastecimentos);
        chkboxList.add(mod_chbIndicesPluviometricos);
        chkboxList.add(mod_chbManutencoes);
    }


    private boolean podeDesativar(){

        atualizarListaCheckBox();

        int ativado = 0;

        for(CheckBox checkBox : chkboxList){
            if(checkBox.isChecked()){
                ativado++;
            }
        }

        if(ativado > 0){
           return true;
        }else{
           return false;
        }
    }
}
