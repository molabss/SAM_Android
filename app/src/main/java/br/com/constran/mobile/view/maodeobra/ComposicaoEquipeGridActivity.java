package br.com.constran.mobile.view.maodeobra;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.*;
import br.com.constran.mobile.R;
import br.com.constran.mobile.adapter.maodeobra.ComposicaoEquipeAdapter;
import br.com.constran.mobile.adapter.maodeobra.TemporarioEquipeAdapter;
import br.com.constran.mobile.exception.AlertException;
import br.com.constran.mobile.persistence.vo.ObraVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.*;
import br.com.constran.mobile.persistence.vo.menu.LocalizacaoVO;
import br.com.constran.mobile.qrcode.ZBarScannerActivity;
import br.com.constran.mobile.view.util.Util;
//import com.dm.zbar.android.scanner.ZBarScannerActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Criado em 11/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class ComposicaoEquipeGridActivity extends GridActivity<ComposicaoEquipeAdapter> {

    private static final String ULTIMA_HORA = " 23:59:59";
    private static final int COMPOSICAO_CODE = 1;

    private AutoCompleteTextView equipeAutoComplete;
    private GridView integranteEquipeGridView;
    private GridView temporarioEquipeGridView;

    private Button btnSalvar;
    private Button btnAddTemporario;
    private Button qrCodeAddIntegrante;
    private Button qrCodeAddTemporario;

    private CheckBox checkAll;

    private LocalizacaoVO localSelecionado;
    private EquipeTrabalhoVO equipeSelecionada;

    private TextView temporarioTextView;
    private int selectedIndex;

    private List<EquipeTrabalhoVO> equipes;
    private List<IntegranteEquipeVO> integranteList;
    private List<IntegranteTempVO> temporarioList;
    private List<AusenciaVO> ausencias;

    private ArrayAdapter equipeAdapter;

    private IntegranteEquipeVO integranteQrCode;

    private ComposicaoEquipeAdapter adapter;

    private boolean usaQRCodePessoal;
    private boolean closeAfterSave;

    @Override
    protected int getContentView() {
        return R.layout.composicao_equipe;
    }

    @Override
    public void initAttributes() {

        integranteEquipeGridView = (GridView) findViewById(R.id.integranteEquipeGridView);
        temporarioEquipeGridView = (GridView) findViewById(R.id.temporarioEquipeGridView);
        equipeAutoComplete = (AutoCompleteTextView) findViewById(R.id.equipeAutoComplete);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnAddTemporario = (Button) findViewById(R.id.btnAddTemporario);
        qrCodeAddIntegrante = (Button) findViewById(R.id.qrCodeAddIntegrante);
        qrCodeAddTemporario = (Button) findViewById(R.id.qrCodeAddTemporario);
        gridVazioTextView = (TextView) findViewById(R.id.empty_list_view);
        temporarioTextView = (TextView) findViewById(R.id.empty_list_temporario_view);
        checkAll = (CheckBox) findViewById(R.id.integrantesCheckBox);
    }

    @Override
    public void bindComponents() {
        localSelecionado = (LocalizacaoVO) getIntent().getSerializableExtra(PARAM_LOCAL_SELECIONADO);
        equipeSelecionada = (EquipeTrabalhoVO) getIntent().getSerializableExtra(PARAM_EQUIPE_SELECIONADA);

        if (equipeSelecionada != null) {
            equipeAutoComplete.setAdapter(null);
            equipeAutoComplete.setText(equipeSelecionada.getApelido());
            equipeAutoComplete.setAdapter(equipeAdapter);
        }

        Integer idObra = dao.getConfiguracoesDAO().getConfiguracaoVO().getIdObra();
        ObraVO obraVO = dao.getObraDAO().getById(idObra);

        usaQRCodePessoal = obraVO.getUsaQRCodePessoal() != null && "S".equalsIgnoreCase(obraVO.getUsaQRCodePessoal());

        showHideComponents();
    }

    private void showHideComponents() {
        if (!usaQRCodePessoal) {
            qrCodeAddIntegrante.setVisibility(View.GONE);
            qrCodeAddTemporario.setVisibility(View.GONE);
        } else {
            btnAddTemporario.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initAdapter() {
        equipes = dao.getEquipeTrabalhoDAO().findByLocalizacao(localSelecionado);
        ausencias = dao.getAusenciaDAO().findByEquipe(equipeSelecionada);

        if (equipeSelecionada != null) {
            integranteList = (List<IntegranteEquipeVO>) dao.getIntegranteEquipeDAO().findByEquipe(equipeSelecionada.getId());
            temporarioList = dao.getIntegranteTempDAO().findByEquipe(equipeSelecionada);
        } else {
            integranteList = new ArrayList<IntegranteEquipeVO>();
            temporarioList = new ArrayList<IntegranteTempVO>();
        }

        adapter = new ComposicaoEquipeAdapter(this, integranteList, ausencias, usaQRCodePessoal);
        setAdapter(adapter);

        equipeAdapter = new ArrayAdapter<EquipeTrabalhoVO>(this, android.R.layout.select_dialog_singlechoice, equipes);
        equipeAutoComplete.setAdapter(equipeAdapter);

        integranteEquipeGridView.setAdapter(getAdapter());
        temporarioEquipeGridView.setAdapter(new TemporarioEquipeAdapter(this, temporarioList));

        if (!usaQRCodePessoal) {
            checkAll.setChecked(adapter.isAllItemsChecked());
        } else {
            checkAll.setVisibility(View.INVISIBLE);
        }

        showHideEmptyText(integranteList);
        showHideEmptyText(temporarioList, temporarioTextView);

        showHideBtnSalvar();
    }

    private void showHideBtnSalvar() {
        if (integranteList == null || integranteList.isEmpty() || usaQRCodePessoal) {
            btnSalvar.setVisibility(View.GONE);
        } else {
            btnSalvar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        adapter = (ComposicaoEquipeAdapter) integranteEquipeGridView.getAdapter();
        boolean mudancaNaoSalva = !adapter.getCheckedItems().isEmpty() || !adapter.getUncheckedItems().isEmpty();

        if (mudancaNaoSalva && !usaQRCodePessoal) {
            AlertDialog.Builder dialogBuider = new AlertDialog.Builder(this);
            dialogBuider.setTitle(getStr(R.string.alerta))
                    .setMessage(getStr(R.string.ALERT_SALVAR_MUDANCAS))
                    .setPositiveButton(getStr(R.string.SIM), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                            closeAfterSave = true;
                            salvar();
                        }
                    })
                    .setNegativeButton(getStr(R.string.NAO), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .create().show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void addListeners() {
        addBtnSalvarListener();
        addBtnAddTemporarioListener();
        addEquipeAutoCompleteListener();
        addIntegranteEquipeGridViewListener();
        addTemporarioEquipeGridViewListener();
        addQRCodeListener();
        addCheckAllListener();
    }

    private void addQRCodeListener() {
        if (usaQRCodePessoal) {
            qrCodeAddIntegrante.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(ComposicaoEquipeGridActivity.this, ZBarScannerActivity.class);
                        intent.putExtra(getStr(R.string.STRING_INTENT_PARAMS), intentParameters);
                        startActivityForResult(intent, INTEGRANTE_QR_CODE_REQUEST);

                    } catch (Exception e) {
                        Util.viewErrorMessage(ComposicaoEquipeGridActivity.this, getStr(R.string.ERROR_LEITURA_QR_CODE));
                    }
                }
            });

            qrCodeAddTemporario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(ComposicaoEquipeGridActivity.this, ZBarScannerActivity.class);
                        intent.putExtra(getStr(R.string.STRING_INTENT_PARAMS), intentParameters);
                        startActivityForResult(intent, TEMPORARIO_QR_CODE_REQUEST);

                    } catch (Exception e) {
                        Util.viewErrorMessage(ComposicaoEquipeGridActivity.this, getStr(R.string.ERROR_LEITURA_QR_CODE));
                    }
                }
            });
        }
    }

    private void addCheckAllListener() {
        checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                adapter = (ComposicaoEquipeAdapter) integranteEquipeGridView.getAdapter();

                if (checked) {
                    adapter.setCheckedItems(new HashSet<IntegranteEquipeVO>(integranteList));
                    adapter.setUncheckedItems(new HashSet<IntegranteEquipeVO>());
                } else {
                    adapter.setCheckedItems(new HashSet<IntegranteEquipeVO>());
                    adapter.setUncheckedItems(new HashSet<IntegranteEquipeVO>(integranteList));
                }

                int childCount = integranteEquipeGridView.getChildCount();

                for (int i = 0; i < childCount; i++) {
                    LinearLayout itemLayout = (LinearLayout) integranteEquipeGridView.getChildAt(i);
                    CheckBox cb = (CheckBox) itemLayout.findViewById(R.id.integranteCheckBox);
                    cb.setChecked(checked);
                }

                adapter.setAllItemsChecked(checked);
//                itemCheckedListener(adapter, checked);
            }
        });
    }

    private void addIntegranteEquipeGridViewListener() {
        integranteEquipeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                gridClickListener(adapterView, view, i, l);
            }
        });
    }

    /**
     * Salva os items checados
     *
     * @param adapter
     * @param limparSelecao informa se os campos selecionados devem ser limpos
     */
    private void itemCheckedListener(final ComposicaoEquipeAdapter adapter, final boolean limparSelecao) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setMessage(getStr(R.string.ALERT_COPIAR_APONTAMENTOS_PRESENTE));
        dialogo.setPositiveButton(getStr(R.string.SIM), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface di, int arg) {
                boolean ok = false;

                //quando nao for preciso limpar a selecao significa que foi chamado apos leitura do QR Code
                if (!limparSelecao && integranteQrCode != null) {
                    AusenciaVO ausencia = new AusenciaVO(integranteQrCode.getEquipe(), integranteQrCode.getPessoa(), Util.getToday());
                    dao.getAusenciaDAO().delete(ausencia);
                    ausencias.remove(ausencia);

                    ok = copiarApontamentos(integranteQrCode);
                } else {
                    for (IntegranteEquipeVO integranteEquipe : adapter.getCheckedItems()) {
                        AusenciaVO ausencia = new AusenciaVO(integranteEquipe.getEquipe(), integranteEquipe.getPessoa(), Util.getToday());
                        dao.getAusenciaDAO().delete(ausencia);
                        ausencias.remove(ausencia);

                        ok = copiarApontamentos(integranteEquipe);
                    }
                }


                afterClick(ok, limparSelecao, adapter);
            }

        });
        dialogo.setNegativeButton(getStr(R.string.NAO), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface di, int arg) {
                for (IntegranteEquipeVO integranteEquipe : adapter.getCheckedItems()) {
                    AusenciaVO ausencia = new AusenciaVO(integranteEquipe.getEquipe(), integranteEquipe.getPessoa(), Util.getToday());
                    dao.getAusenciaDAO().delete(ausencia);
                    ausencias.remove(ausencia);
                }

                afterClick(true, limparSelecao, adapter);
            }
        });

        dialogo.setTitle(getStr(R.string.AVISO));
        dialogo.show();

    }

    /**
     * @param showResultMessage
     * @param limparSelecao     informa se os campos selecionados devem ser limpos
     * @param adapter
     */
    private void afterClick(boolean showResultMessage, boolean limparSelecao, ComposicaoEquipeAdapter adapter) {
        //limpar os items checados que devem ser tratados
        if (limparSelecao) {
            adapter.setCheckedItems(new HashSet<IntegranteEquipeVO>());
        } else {
            adapter.notifyDataSetChanged();
        }

        //se tiver items desmarcados, entao os processa
        if (adapter.getUncheckedItems() != null && !adapter.getUncheckedItems().isEmpty()) {
            itemUncheckedListener(adapter, limparSelecao);
        } else {
            Toast.makeText(ComposicaoEquipeGridActivity.this, getStr(R.string.ALERT_SUCESS_UPDATE), Toast.LENGTH_SHORT).show();

            if (closeAfterSave) {
                finish();
            }
        }

    }

    /**
     * @param adapter
     * @param limparSelecao informa se os campos selecionados devem ser limpos
     */
    private void itemUncheckedListener(final ComposicaoEquipeAdapter adapter, boolean limparSelecao) {

        for (IntegranteEquipeVO integranteEquipe : adapter.getUncheckedItems()) {
            AusenciaVO ausencia = new AusenciaVO(integranteEquipe.getEquipe(), integranteEquipe.getPessoa(), Util.getToday());
            dao.getAusenciaDAO().save(ausencia);

            if (ausencias == null) {
                ausencias = new ArrayList<AusenciaVO>();
            }

            ausencias.add(ausencia);

            //exclui apropriacao mao-obra
            dao.getApropriacaoMaoObraDAO().deleteByIntegrante(equipeSelecionada, integranteEquipe);

            //exclui paralisacoes mao-obra
            dao.getParalisacaoMaoObraDAO().deleteByIntegrante(equipeSelecionada, integranteEquipe);

        }

        if (limparSelecao) {
            adapter.setUncheckedItems(new HashSet<IntegranteEquipeVO>());
        }

        Toast.makeText(ComposicaoEquipeGridActivity.this, getStr(R.string.ALERT_SUCESS_UPDATE), Toast.LENGTH_SHORT).show();

        if (closeAfterSave) {
            finish();
        }

    }

    private boolean copiarApontamentos(IntegranteEquipeVO integranteEquipe) {
        return dao.getEventoEquipeDAO().replicarApontamentosEquipe(localSelecionado, equipeSelecionada, integranteEquipe);
    }

    private void addTemporarioEquipeGridViewListener() {
        temporarioEquipeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                TemporarioEquipeAdapter adapter = (TemporarioEquipeAdapter) parent.getAdapter();

                if (adapter.getOperacao() != null)
                    switch (adapter.getOperacao()) {
                        case EXCLUSAO:
                            confirmaExclusao(i);
                            break;
                        default:
                            break;
                    }
            }
        });
    }

    /**
     * Este método esta sendo utilizado pelo == temporarioEquipeGridView ==
     *
     * @param i indice da linha do registro
     */
    @Override
    protected void excluirGridItem(int i) {
        selectedIndex = i;
        confirmarDialog(R.string.ALERT_EXCLUIR_APONTAMENTOS_TEMPORARIO);
    }

    /**
     * Caso exclusao de integrante temporário for confirmada, executa este método
     */
    @Override
    protected void positiveClick() {
        IntegranteTempVO temporario = temporarioList.get(selectedIndex);

        //exclui apropriacao mao-obra
        dao.getApropriacaoMaoObraDAO().deleteByIntegrante(equipeSelecionada, temporario);

        //exclui paralisacoes mao-obra
        dao.getParalisacaoMaoObraDAO().deleteByIntegrante(equipeSelecionada, temporario);

        //exclui integrante temporario
        dao.getIntegranteTempDAO().delete(temporario);

        temporarioList = dao.getIntegranteTempDAO().findByEquipe(equipeSelecionada);
        temporarioEquipeGridView.setAdapter(new TemporarioEquipeAdapter(ComposicaoEquipeGridActivity.this, temporarioList));

        showHideEmptyText(temporarioList, temporarioTextView);
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
        adapter = (ComposicaoEquipeAdapter) integranteEquipeGridView.getAdapter();

        if (adapter.getCheckedItems() != null && !adapter.getCheckedItems().isEmpty()) {
            itemCheckedListener(adapter, true);
        } else if (adapter.getUncheckedItems() != null && !adapter.getUncheckedItems().isEmpty()) {
            itemUncheckedListener(adapter, true);
        }
    }

    private void addBtnAddTemporarioListener() {
        btnAddTemporario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (equipeSelecionada == null) {
                    Util.viewMessage(ComposicaoEquipeGridActivity.this, Util.getMessage(ComposicaoEquipeGridActivity.this, getStr(R.string.equipe), R.string.ALERT_REQUIRED));
                } else {
                    Intent intent = new Intent(getBaseContext(), AddTemporarioActivity.class);
                    intent.putExtra(PARAM_LOCAL_SELECIONADO, localSelecionado);
                    intent.putExtra(PARAM_EQUIPE_SELECIONADA, equipeSelecionada);
                    intent.putExtra(getStr(R.string.STRING_INTENT_PARAMS), intentParameters);

                    startActivityForResult(intent, COMPOSICAO_CODE);
                }
            }
        });
    }

    private void addEquipeAutoCompleteListener() {
        equipeAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                equipeSelecionada = (EquipeTrabalhoVO) parent.getItemAtPosition(i);
                integranteList = (List<IntegranteEquipeVO>) dao.getIntegranteEquipeDAO().findByEquipe(equipeSelecionada.getId());
                ausencias = dao.getAusenciaDAO().findByEquipe(equipeSelecionada);
                setAdapter(new ComposicaoEquipeAdapter(ComposicaoEquipeGridActivity.this, integranteList, ausencias, usaQRCodePessoal));

                integranteEquipeGridView.setAdapter(getAdapter());

                temporarioList = dao.getIntegranteTempDAO().findByEquipe(equipeSelecionada);
                temporarioEquipeGridView.setAdapter(new TemporarioEquipeAdapter(ComposicaoEquipeGridActivity.this, temporarioList));

                showHideEmptyText(integranteList);
                showHideEmptyText(temporarioList, temporarioTextView);
            }
        });

        equipeAutoComplete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                equipeAutoComplete.setText("");
                equipeSelecionada = null;
                integranteList = new ArrayList<IntegranteEquipeVO>();
                temporarioList = new ArrayList<IntegranteTempVO>();
                setAdapter(new ComposicaoEquipeAdapter(ComposicaoEquipeGridActivity.this, integranteList, ausencias, usaQRCodePessoal));
                integranteEquipeGridView.setAdapter(getAdapter());

                temporarioEquipeGridView.setAdapter(new TemporarioEquipeAdapter(ComposicaoEquipeGridActivity.this, temporarioList));

                showHideEmptyText(integranteList);
                showHideEmptyText(temporarioList, temporarioTextView);

                return false;
            }
        });

        equipeAutoComplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focusOn) {
                if (!focusOn && equipeSelecionada != null) {
                    equipeAutoComplete.setAdapter(null);
                    equipeAutoComplete.setText(equipeSelecionada.getApelido());
                    equipeAutoComplete.setAdapter(equipeAdapter);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == COMPOSICAO_CODE) {
            temporarioList = dao.getIntegranteTempDAO().findByEquipe(equipeSelecionada);
            temporarioEquipeGridView.setAdapter(new TemporarioEquipeAdapter(ComposicaoEquipeGridActivity.this, temporarioList));

            showHideEmptyText(integranteList);
            showHideEmptyText(temporarioList, temporarioTextView);
        }

        try {
            //se estiver usando QR Code, adiciona integrante fixo
            if (usaQRCodePessoal && requestCode == INTEGRANTE_QR_CODE_REQUEST) {
                adicionarIntegranteFixo(intent);
            }

            //se estiver usando QR Code, adiciona integrante temporario
            if (usaQRCodePessoal && requestCode == TEMPORARIO_QR_CODE_REQUEST) {
                adicionarIntegranteTemporario(intent);
            }

        } catch (Exception e) {
            tratarExcecao(e);
        }
    }

    /**
     * @param intent
     */
    private void adicionarIntegranteFixo(Intent intent) throws Exception {
        if (integranteList != null && !integranteList.isEmpty()) {
            // o idPessoa passa a ser na verdade o campo QRCode da tabela de pessoas que vem do arquivo do SAM
            Integer idPessoa = Util.getQRCodeId(this, intent);

            if (idPessoa == null)
                throw new AlertException(getStr(R.string.ALERT_SELECT_INTEGRANTE_QRCODE));

            // para manter a estrutura ja feita, retornaremos o idPessoa buscando pelo codigo QRCode lido
            idPessoa = getIdPessoalByQRCode(idPessoa.toString());

            if (idPessoa == 0)
                throw new AlertException(getStr(R.string.ALERT_SELECT_INTEGRANTE_QRCODE));

            if (idPessoa != null) {
                integranteQrCode = (IntegranteEquipeVO) findIntegranteByPessoa(integranteList, idPessoa);

                if (integranteQrCode != null) {
                    adapter = (ComposicaoEquipeAdapter) integranteEquipeGridView.getAdapter();
                    adapter.getCheckedItems().add(integranteQrCode);
                    adapter.updateItemsChecked(integranteQrCode);

                    itemCheckedListener(adapter, false);
                } else {
                    Util.viewErrorMessage(ComposicaoEquipeGridActivity.this, getStr(R.string.ALERT_INTEGRANTE_EQUIPE_INVALIDO));
                }
            }
        }
    }

    /**
     * Adiciona integrante a lista de temporarios apos ler QR Code e redireciona para tela de ajuste de saida integrante temporario
     *
     * @param intent
     */
    private void adicionarIntegranteTemporario(Intent intent) throws Exception {
        // o idPessoa passa a ser na verdade o campo QRCode da tabela de pessoas que vem do arquivo do SAM
        Integer pessoaId = Util.getQRCodeId(this, intent);

        if (pessoaId == null)
            throw new AlertException(getStr(R.string.ALERT_SELECT_INTEGRANTE_QRCODE));

        // para manter a estrutura ja feita, retornaremos o idPessoa buscando pelo codigo QRCode lido
        pessoaId = getIdPessoalByQRCode(pessoaId.toString());

        if (pessoaId == 0)
            throw new AlertException(getStr(R.string.ALERT_SELECT_INTEGRANTE_QRCODE));

        if (isTemporarioOk(pessoaId)) {

            IntegranteTempVO integranteTemp = adicionaTemporario(pessoaId);

            if (integranteTemp != null) {
                intent = new Intent(getBaseContext(), AddTemporarioActivity.class);
                intent.putExtra(PARAM_LOCAL_SELECIONADO, localSelecionado);
                intent.putExtra(PARAM_EQUIPE_SELECIONADA, equipeSelecionada);
                intent.putExtra(PARAM_INTEGRANTE_SELECIONADO, IntegranteEquipeVO.toIntegranteEquipe(integranteTemp));
                intent.putExtra(getStr(R.string.STRING_INTENT_PARAMS), intentParameters);

                startActivityForResult(intent, COMPOSICAO_CODE);
            }
        }
    }

    private IntegranteTempVO adicionaTemporario(Integer pessoaId) {
        PessoalVO pessoa = new PessoalVO(pessoaId);
        pessoa = dao.getPessoalDAO().findById(pessoa);

        if (pessoa != null) {
            IntegranteTempVO integranteTemp = new IntegranteTempVO();
            integranteTemp.setEquipe(equipeSelecionada);
            integranteTemp.setDataIngresso(Util.getToday() + ULTIMA_HORA);
            integranteTemp.setPessoa(pessoa);
            integranteTemp.setDataSaida(Util.getToday());
//            dao.getIntegranteTempDAO().save(integranteTemp);

            return integranteTemp;
        }

        return null;
    }

    private Integer getIdPessoalByQRCode(String qrCode) {
        return dao.getUsuarioDAO().getIdPessoalByQRCode(qrCode);
    }

    /**
     * O integrante é valido como temporário se ele ainda não estiver na lista de temporários
     * e se ele não for um integrante fixo da mesma equipe
     *
     * @return
     */
    private boolean isTemporarioOk(Integer pessoaId) {
        if (pessoaId == null) {
            return false;
        }

        IntegranteVO integrante = findIntegranteByPessoa(temporarioList, pessoaId);

        //verifica se o integrante adicionado já pertence a equipe como temporario
        if (integrante != null) {
            Toast.makeText(this, getStr(R.string.ALERT_INTEGRANTE_EXISTENTE), Toast.LENGTH_SHORT).show();
            return false;
        }

        integrante = findIntegranteByPessoa(integranteList, pessoaId);

        //verifica se o integrante adicionado já pertence a equipe como fixo
        if (integrante != null) {
            Util.viewErrorMessage(ComposicaoEquipeGridActivity.this, getStr(R.string.ALERT_FALHA_AO_ADD_TEMP));
            return false;
        }

        //verifica se o integrante esta alocado em outra equipe no mesmo período
        if (!dao.getIntegranteEquipeDAO().isIntegranteDisponivel(pessoaId, equipeSelecionada.getId())) {
            Util.viewErrorMessage(ComposicaoEquipeGridActivity.this, getStr(R.string.ALERT_INTEGRANTE_INDISPONIVEL));
            return false;
        }

        return true;
    }

    private IntegranteVO findIntegranteByPessoa(List<? extends IntegranteVO> lista, Integer idPessoa) {
        if (lista != null) {
            for (IntegranteVO integrante : lista) {
                if (idPessoa.equals(integrante.getPessoa().getId())) {
                    integrante.setEquipe(equipeSelecionada);

                    return integrante;
                }
            }
        }
        return null;
    }
}
