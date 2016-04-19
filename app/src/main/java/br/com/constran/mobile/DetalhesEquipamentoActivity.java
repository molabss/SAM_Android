package br.com.constran.mobile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import br.com.constran.mobile.exception.AlertException;
import br.com.constran.mobile.persistence.vo.aprop.ApropriacaoVO;
import br.com.constran.mobile.persistence.vo.aprop.eqp.ApropriacaoEquipamentoVO;
import br.com.constran.mobile.persistence.vo.aprop.eqp.EquipamentoParteDiariaVO;
import br.com.constran.mobile.persistence.vo.imp.AtividadeVO;
import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;
import br.com.constran.mobile.view.AbstractActivity;
import br.com.constran.mobile.view.interfaces.InterfaceEditActivity;
import br.com.constran.mobile.view.screens.Detail;
import br.com.constran.mobile.view.util.References;
import br.com.constran.mobile.view.util.Util;

public final class DetalhesEquipamentoActivity extends AbstractActivity implements InterfaceEditActivity {

    private TextView dataView;
    private TextView equipamentoLabel;
    private EditText operador1EditText;
    private EditText operador2EditText;
    private EditText horimetroIni;
    private EditText horimetroFim;
    private EditText producaoEditText;
    private EditText observacoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detalhes_equipamento_edit);

        currentContext = DetalhesEquipamentoActivity.this;
        currentClass = DetalhesEquipamentoActivity.class;

        try {

            init();

            editValues();

            initAtributes();

            createDetail(getDetailValues());

            editScreen();

            initEvents();

        } catch (Exception e) {
            tratarExcecao(e);
        }
    }


    @Override
    public void validateFields() throws Exception {

        if (horimetroInicial == null || horimetroInicial.trim().equals(getStr(R.string.EMPTY))) {

            throw new AlertException(Util.getMessage(currentContext, getStr(R.string.horimetro_inicial), R.string.ALERT_REQUIRED));

        } else if (horimetroFinal != null && !horimetroFinal.trim().equals(getStr(R.string.EMPTY)) && Integer.valueOf(horimetroInicial.trim()) > Integer.valueOf(horimetroFinal.trim())) {

            throw new AlertException(getStr(R.string.ALERT_KM_INVALID));
        }
        if (operador1 == null || operador1.trim().equals(getStr(R.string.EMPTY))) {

            throw new AlertException(Util.getMessage(currentContext, getStr(R.string.operador1), R.string.ALERT_REQUIRED));

        }
    }


    @Override
    public void editValues() {
        idEquipamento = Integer.valueOf(intentParameters.getIdRegistroAtual());

        intentDesc = getStr(R.string.DETAIL_EVTS_EQUIPAMENTO);
    }

    @Override
    public void initAtributes() {

        dataView = (TextView) findViewById(R.id.eqpViewDt);
        equipamentoLabel = (TextView) findViewById(R.id.lblEqp);
        operador1EditText = (EditText) findViewById(R.id.eqpOp1);
        operador2EditText = (EditText) findViewById(R.id.eqpOp2);
        horimetroIni = (EditText) findViewById(R.id.eqpEditHorimIni);
        horimetroFim = (EditText) findViewById(R.id.eqpEditHorimFim);
        producaoEditText = (EditText) findViewById(R.id.eqpEditProd);
        observacoes = (EditText) findViewById(R.id.eqpEditObs);

        Util.addMascaraNumerica(producaoEditText, 5, 2);

    }

    @Override
    public void initEvents() {

        btSalvar = (Button) findViewById(R.id.btEqpSave);
        btSalvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {

                    ApropriacaoEquipamentoVO vo = new ApropriacaoEquipamentoVO();

                    operador1 = operador1EditText.getText().toString();
                    operador2 = operador2EditText.getText().toString();
                    horimetroInicial = horimetroIni.getText().toString();
                    horimetroFinal = horimetroFim.getText().toString();
                    producao = producaoEditText.getText().toString();

                    validateFields();

                    if (changePkParteDiaria()) {

                        String strId = idApropriacao + getStr(R.string.TOKEN) + idEquipamento + getStr(R.string.TOKEN) + dataHora;

                        vo.setStrId(strId);//update

                    } else {

                        ApropriacaoVO apropriacao = new ApropriacaoVO();
                        apropriacao.setAtividade(new AtividadeVO(idAtividade, idFrenteObra));
                        apropriacao.setDataHoraApontamento(dataHora);
                        apropriacao.setTipoApropriacao(getStr(R.string.OPTION_MENU_EQP));
                        apropriacao.setObservacoes(getStr(R.string.EMPTY));

                        saveOrUpdate(apropriacao);

                        idApropriacao = getDAO().getApropriacaoDAO().getMaxId();

                        EquipamentoParteDiariaVO parteDiaria = novoRegistro ? new EquipamentoParteDiariaVO() : new EquipamentoParteDiariaVO(Integer.valueOf(idParteDiaria));
                        parteDiaria.setIdApropriacao(idApropriacao);
                        parteDiaria.setEquipamento(new EquipamentoVO(idEquipamento));
                        parteDiaria.setDataHora(dataHora);

                        saveOrUpdate(parteDiaria);
                    }

                    vo.setIdApropriacao(idApropriacao);
                    vo.setEquipamento(new EquipamentoVO(idEquipamento));
                    vo.setDataHora(dataHora);
                    vo.setHorimetroFim(horimetroFinal);
                    vo.setHorimetroIni(horimetroInicial);
                    vo.setOperador1(operador1);
                    vo.setOperador2(operador2);
                    vo.setProducao(producao);
                    vo.setObservacoes(observacoes.getText().toString());

                    saveOrUpdate(vo);

                    onBackPressed();

                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }

        });

        btCancelar = (Button) findViewById(R.id.btEqpCancel);
        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
    }

    @Override
    public Detail getDetailValues() {

        //Texto do Detail
        String strDetail = intentDesc;

        Detail detail = new Detail(this);
        detail.setDetail(strDetail);
        detail.setColorBKG(getColor(R.color.GRAY));// Cor de fundo
        detail.setColorTXT(getColor(R.color.WHITE));// Cor da fonte
        detail.setFileLayoutRow(References.DETAIL_LAYOUT); //arquivo xml - layout (TableRow)
        detail.setIdColumns(References.DETAIL_ID_COLUMNS);// Ids (xml) das colunas
        detail.setIdTable(References.DETAIL_ID_EDT_EQP); //Id do TableLayout);
        return detail;
    }

    @Override
    public void onBackPressed() {

        redirect(EquipamentosParteDiariaActivity.class, true);
    }

    @Override
    public void editScreen() throws Exception {

        String[] dados = null;

        dados = getDAO().getParteDiariaDAO().getValues(idEquipamento);

        if (dados == null) {
            novoRegistro = true;
            EquipamentoVO equipamento = getDAO().getEquipamentoDAO().getById(idEquipamento);
            descEquipamento = equipamento.getDescricao();
            equipamentoLabel.setText(descEquipamento);

            dados = getDAO().getLocalizacaoDAO().getValues();

            int i = 0;
            idFrenteObra = (dados[i] != null) ? Integer.valueOf(dados[i]) : null;
            idAtividade = (dados[++i] != null) ? Integer.valueOf(dados[i]) : null;
            dataHora = Util.getNow();
            dataView.setText(dataHora);

            return;
//            throw new AlertException(getStr(R.string.ERROR_EQUIPAMENTO_INVALIDO));
        }

        int i = 0;

        idParteDiaria = dados[i++];
        descEquipamento = dados[i++];
        operador1 = dados[i++];
        operador2 = dados[i++];
        horimetroInicial = dados[i++];
        horimetroFinal = dados[i++];
        producao = dados[i++];
        dataHora = dados[i++];

        equipamentoLabel.setText(descEquipamento);
        operador1EditText.setText(operador1);
        operador2EditText.setText(operador2);
        horimetroIni.setText(horimetroInicial);
        horimetroFim.setText(horimetroFinal);
        producaoEditText.setText(producao);
        dataView.setText(dataHora);
        observacoes.setText(dados[i++]);

        i = 0;

        dados = getDAO().getLocalizacaoDAO().getValues();

        idFrenteObra = (dados[i] != null) ? Integer.valueOf(dados[i]) : null;
        idAtividade = (dados[++i] != null) ? Integer.valueOf(dados[i]) : null;

        novoRegistro = idParteDiaria == null;

    }
}
