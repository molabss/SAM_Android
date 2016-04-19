package br.com.constran.mobile.view.maodeobra;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;
import br.com.constran.mobile.R;
import br.com.constran.mobile.persistence.dao.DAOFactory;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.EquipeTrabalhoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.IntegranteEquipeVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.IntegranteTempVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.IntegranteVO;
import br.com.constran.mobile.persistence.vo.menu.LocalizacaoVO;
import br.com.constran.mobile.view.util.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Criado em 11/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class AddTemporarioActivity extends Activity {

    private static final String ULTIMA_HORA = " 23:59:59";
    private static final String PARAM_EQUIPE_SELECIONADA = "equipeSelecionada";
    private static final String PARAM_LOCAL_SELECIONADO = "localSelecionado";
    private static final String PARAM_INTEGRANTE_SELECIONADO = "integranteSelecionado";

    private int tamanhoData;
    private Button btnSalvar;
    private Button btnCancelar;

    private TextView equipeTextView;

    private EditText dataSaidaEditText;

    private AutoCompleteTextView integranteAutoComplete;

    private List<IntegranteVO> integranteEquipes;
    private IntegranteVO integranteEquipeSelecionado;
    private LocalizacaoVO localSelecionado;
    private EquipeTrabalhoVO equipeSelecionada;

    private ArrayAdapter<IntegranteVO> integranteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_integrante);
        initAttributes();
        bindComponents();
        addListeners();
        initAdapters();

        ajustaCorBackground();
    }

    @Override
    protected void onResume() {
        try {
            if (dataSaidaEditText != null) {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Date now = new Date();
                String dateNow = df.format(now);
                String dateSaida = dataSaidaEditText.getText().toString();

                if (!dateNow.equals(dateSaida)) {
                    Intent intent = new Intent(this, MaoObraServicoGridActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        } catch (Exception e) {
            Log.e(AddTemporarioActivity.class.toString(), e.getMessage());
        }
        super.onResume();
    }

    void initAttributes() {
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        equipeTextView = (TextView) findViewById(R.id.equipeTextView);
        integranteAutoComplete = (AutoCompleteTextView) findViewById(R.id.integranteAutoComplete);
        dataSaidaEditText = (EditText) findViewById(R.id.dataSaidaEditText);
    }

    private void ajustaCorBackground() {
        LinearLayout conteudoLinearLayout = (LinearLayout) findViewById(R.id.conteudoLinearLayout);
        LinearLayout conteudoLinearLayout2 = (LinearLayout) findViewById(R.id.conteudoLinearLayout2);
        LinearLayout conteudoLinearLayout3 = (LinearLayout) findViewById(R.id.conteudoLinearLayout3);

        if (conteudoLinearLayout != null) {
            conteudoLinearLayout.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        }

        if (conteudoLinearLayout2 != null) {
            conteudoLinearLayout2.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        }

        if (conteudoLinearLayout3 != null) {
            conteudoLinearLayout3.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        }
    }


    void bindComponents() {
        localSelecionado = (LocalizacaoVO) getIntent().getSerializableExtra(PARAM_LOCAL_SELECIONADO);
        equipeSelecionada = (EquipeTrabalhoVO) getIntent().getSerializableExtra(PARAM_EQUIPE_SELECIONADA);
        integranteEquipeSelecionado = (IntegranteEquipeVO) getIntent().getSerializableExtra(PARAM_INTEGRANTE_SELECIONADO);

        DAOFactory dao = DAOFactory.getInstance(this);
//        integranteEquipes = (List<IntegranteVO>) dao.getIntegranteEquipeDAO().findNotInEquipe(equipeSelecionada.getId());
        integranteEquipes = (List<IntegranteVO>) dao.getIntegranteEquipeDAO().findTemporariosDisponiveis(equipeSelecionada.getId());
        equipeTextView.setText(equipeSelecionada.getApelido());
        dataSaidaEditText.setText(Util.getToday());
    }

    void addListeners() {
        addBtnSalvarListener();
        addBtnCancelarListener();
        addIntegranteAutoCompleteListener();
        addDataSaidaEditTextListener();
    }

    private void addDataSaidaEditTextListener() {
        dataSaidaEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                tamanhoData = dataSaidaEditText.getText().length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Util.addMascaraDataListener(editable, dataSaidaEditText, tamanhoData);
            }
        });

        dataSaidaEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dataSaidaEditText.setText("");
                return false;
            }
        });
    }

    void initAdapters() {
        integranteAdapter = new ArrayAdapter<IntegranteVO>(this, android.R.layout.select_dialog_singlechoice, integranteEquipes);
        integranteAutoComplete.setAdapter(integranteAdapter);
    }

    private void addBtnCancelarListener() {
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void addBtnSalvarListener() {
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvar();
            }
        });
    }

    private void salvar() {
        if (isValidationOk()) {
            IntegranteTempVO temporario = IntegranteTempVO.toIntegranteTemp(integranteEquipeSelecionado);
            temporario.setEquipe(equipeSelecionada);
            String dataSaida = dataSaidaEditText.getText().toString();

            if (dataSaida == null || dataSaida.isEmpty()) {
                dataSaida = Util.getToday();
            }

            temporario.setDataIngresso(Util.getToday());
            temporario.setDataSaida(dataSaida + ULTIMA_HORA);
            DAOFactory.getInstance(this).getIntegranteTempDAO().save(temporario);

            integranteEquipeSelecionado = temporario;

            confirmarDialog(R.string.ALERT_COPIAR_APONTAMENTOS_TEMPORARIO);
        }
    }

    private boolean isValidationOk() {
        if (integranteEquipeSelecionado == null) {
            Util.viewMessage(this, Util.getMessage(this, getStr(R.string.integrante), R.string.ALERT_REQUIRED));
            return false;
        }

        String data = dataSaidaEditText.getText().toString();

        if (!Util.isDataValida(data)) {
            Util.viewMessage(this, Util.getMessage(this, getStr(R.string.data_saida), R.string.ALERT_HOUR_INVALID));
            return false;
        }

        return true;
    }

    void confirmarDialog(final int messageId) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setMessage(getStr(messageId));
        dialogo.setPositiveButton(getStr(R.string.SIM), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface di, int arg) {
                positiveClick();
            }
        });
        dialogo.setNegativeButton(getStr(R.string.NAO), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface di, int arg) {
                finish();
            }
        });

        dialogo.setTitle(getStr(R.string.AVISO));
        dialogo.show();
    }

    private void positiveClick() {
        boolean ok = DAOFactory.getInstance(this).getEventoEquipeDAO().replicarApontamentosEquipe(localSelecionado, equipeSelecionada, integranteEquipeSelecionado);

        if (ok) {
            AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
            dialogo.setTitle(getResources().getString(R.string.msg_aviso));
            dialogo.setMessage(getStr(R.string.ALERT_SUCESS_UPDATE));
            dialogo.setNeutralButton(getResources().getString(R.string.msg_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            dialogo.show();
        } else {
            AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
            dialogo.setTitle(getResources().getString(R.string.msg_aviso));
            dialogo.setMessage(getStr(R.string.ALERT_FAIL_UPDATE));
            dialogo.setNeutralButton(getResources().getString(R.string.msg_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            dialogo.show();
        }
    }

    private void addIntegranteAutoCompleteListener() {
        if (integranteEquipeSelecionado == null) {
            integranteAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                    integranteEquipeSelecionado = (IntegranteEquipeVO) parent.getItemAtPosition(i);

                }
            });

            integranteAutoComplete.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    integranteAutoComplete.setText("");
                    integranteEquipeSelecionado = null;
                    return false;
                }
            });

            integranteAutoComplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean focusOn) {
                    if (!focusOn && integranteEquipeSelecionado != null) {
                        integranteAutoComplete.setAdapter(null);
                        integranteAutoComplete.setText(integranteEquipeSelecionado.toString());
                        integranteAutoComplete.setAdapter(integranteAdapter);
                    }
                }
            });
        } else {
            integranteAutoComplete.setKeyListener(null);
            integranteAutoComplete.setText(integranteEquipeSelecionado.getPessoa().getNome());
        }
    }

    String getStr(int id) {
        return getResources().getString(id);
    }

}
