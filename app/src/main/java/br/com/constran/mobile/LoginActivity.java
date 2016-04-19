package br.com.constran.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import br.com.constran.mobile.exception.AlertException;
import br.com.constran.mobile.persistence.vo.ObraVO;
import br.com.constran.mobile.persistence.vo.imp.UsuarioVO;
import br.com.constran.mobile.qrcode.ZBarScannerActivity;
import br.com.constran.mobile.view.AbstractActivity;
import br.com.constran.mobile.view.interfaces.InterfaceEditActivity;
import br.com.constran.mobile.view.screens.Detail;
import br.com.constran.mobile.view.util.Util;
//import com.dm.zbar.android.scanner.ZBarScannerActivity;

public final class LoginActivity extends AbstractActivity implements InterfaceEditActivity {

    protected final int QR_CODE_REQUEST_ABS = 998;
    
    private AutoCompleteTextView usuarioList;
    private EditText senha;
    private Button btnQRCode;
    private TextView labelUsuario;
    private Integer idAbastecedor;
    private EditText prefAbsQRCode;
    private String[] dados;

    private ObraVO obraVO;

    protected LinearLayout conteudoLinearLayout;
    protected LinearLayout conteudoLinearLayout2;
    protected LinearLayout headerLinearLayout;
    protected LinearLayout headerLinearLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        currentContext = LoginActivity.this;
        currentClass = LoginActivity.class;

        try {

            init();

            initAtributes();

            editValues();

            initEvents();

            editScreen();

        } catch (Exception e) {
            tratarExcecao(e);
        }
    }

    @Override
    public void initAtributes() throws Exception {

        senha = (EditText) findViewById(R.id.LgnPass);
        labelUsuario = (TextView) findViewById(R.id.lblUsr);
        usuarioList = (AutoCompleteTextView) findViewById(R.id.LgnUsr);
        btSalvar = (Button) findViewById(R.id.btLgnSave);
        btCancelar = (Button) findViewById(R.id.btLgnCancel);
        btnQRCode = (Button) findViewById(R.id.btnQRCode);

        prefAbsQRCode = (EditText) findViewById(R.id.editAbsPrefQrCode);

        conteudoLinearLayout = (LinearLayout) findViewById(R.id.contentLinearLayout);
        conteudoLinearLayout2 = (LinearLayout) findViewById(R.id.contentLinearLayout2);

        headerLinearLayout = (LinearLayout) findViewById(R.id.headerLinearLayout);
        headerLinearLayout2 = (LinearLayout) findViewById(R.id.headerLinearLayout2);

        if (conteudoLinearLayout != null)
            conteudoLinearLayout.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));
        if (conteudoLinearLayout2 != null)
            conteudoLinearLayout2.setBackgroundColor(getResources().getColor(R.color.DARK_GRAY));

        if (headerLinearLayout != null)
            headerLinearLayout.setBackgroundColor(getResources().getColor(R.color.GRAY2));
        if (headerLinearLayout2 != null)
            headerLinearLayout2.setBackgroundColor(getResources().getColor(R.color.GRAY2));
    }

    @Override
    public void editValues() throws Exception {

        obraVO = getDAO().getObraDAO().getById(config.getIdObra());

        if (intentParameters.getMenu().equals(getStr(R.string.OPTION_MENU_ABS))) {

            labelUsuario.setText(getStr(R.string.abastecedor));

            usuarioArrayVO = getDAO().getUsuarioDAO().getArrayUsuarioAbastecedor();

            config = getDAO().getConfiguracoesDAO().getConfiguracaoVO();

            for (UsuarioVO user : usuarioArrayVO) {

                if (config.getCodUsuario().toString().equals(user.getId().toString())) {
                    userRequest = user;
                    usuarioList.setText(user.getNome());
                    senha.requestFocus();
                    break;
                }
            }

        } else
            usuarioArrayVO = getDAO().getUsuarioDAO().getArrayUsuarioVO();

        senha.setTransformationMethod(PasswordTransformationMethod.getInstance());

        if ("S".equalsIgnoreCase(obraVO.getUsaQRCodePessoal())) {
            btnQRCode.setVisibility(View.VISIBLE);
            usuarioList.setVisibility(View.GONE);
            prefAbsQRCode.setVisibility(View.VISIBLE);
        } else {
            usuarioList.setVisibility(View.VISIBLE);
            btnQRCode.setVisibility(View.GONE);
        }
    }


    @Override
    public void onBackPressed() {

        redirectPrincipal();

    }


    @Override
    public void initEvents() throws Exception {

        usuarioList.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                userRequest = new UsuarioVO();
                return false;
            }
        });


        usuarioList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userRequest = (UsuarioVO) parent.getItemAtPosition(position);
            }

        });

        usuarioList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((AutoCompleteTextView) v).setText(getStr(R.string.EMPTY));
                userRequest = new UsuarioVO();
            }
        });


        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                try {
                    validateFields();

                    boolean validate = userRequest.getSenha().trim().equals(senha.getText().toString());

                    if (validate) {

                        login();

                        if (intentParameters.getMenu().equals(getStr(R.string.OPTION_MENU_ABS))) {

                            redirect(AbastecimentosSearchActivity.class, false);

                        }

                    } else
                        throw new AlertException(getStr(R.string.ALERT_SENHA_INVALIDA));


                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }
        });

        btnQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(LoginActivity.this, ZBarScannerActivity.class);
                    startActivityForResult(intent, QR_CODE_REQUEST_ABS);
                } catch (Exception e) {
                    tratarExcecao(e);
                }
            }
        });


        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View argetDetailValuesg0) {
                onBackPressed();
            }
        });

    }


    @Override
    public void editScreen() throws Exception {

        ArrayAdapter<UsuarioVO> adp = new ArrayAdapter<UsuarioVO>(this, android.R.layout.select_dialog_singlechoice, usuarioArrayVO);
        adp.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        usuarioList.setAdapter(adp);

        hiddenKeyboard(usuarioList, senha);

    }


    @Override
    public void validateFields() throws Exception {

        if (intentParameters.getMenu().equals(getStr(R.string.OPTION_MENU_ABS))) {
            if (userRequest.getIdUsuarioPessoal() == null)
                throw new AlertException(Util.getMessage(currentContext, R.string.abastecedor, R.string.ALERT_REQUIRED));
        } else if (userRequest.getId() == null) {
            throw new AlertException(Util.getMessage(currentContext, R.string.usuario, R.string.ALERT_REQUIRED));
        }

        if (senha.getText() == null || senha.getText().toString().equals(getStr(R.string.EMPTY))) {
            throw new AlertException(Util.getMessage(currentContext, R.string.senha, R.string.ALERT_REQUIRED));
        }

    }


    @Override
    public Detail getDetailValues() throws Exception {
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == QR_CODE_REQUEST_ABS) {
            try {
                // o idAbastecedor passa a ser na verdade o campo QRCode da tabela de pessoas que vem do arquivo do SAM
                idAbastecedor = Util.getQRCodeId(this, intent);

                if (idAbastecedor == null)
                    throw new AlertException(getStr(R.string.ALERT_SELECT_ABASTECEDOR));

                // para manter a estrutura ja feita, retornaremos o idAbastecedor buscando pelo codigo QRCode lido
                idAbastecedor = getIdAbastecedorByQRCode(idAbastecedor.toString());

                UsuarioVO userQRCode = this.findUsuarioArray(idAbastecedor);

                if (userQRCode != null) {
                    prefAbsQRCode.setText(userQRCode.getNome());
                    senha.requestFocus();
                    userRequest = userQRCode;
                } else {
                    throw new AlertException(getStr(R.string.ALERT_SELECT_ABASTECEDOR_VALIDO));
                }

            } catch (Exception e) {
                tratarExcecao(e);
            }
        }
    }

    private Integer getIdAbastecedorByQRCode(String qrcode) {
        return getDAO().getUsuarioDAO().getIdPessoalByQRCode(qrcode);
    }

    private UsuarioVO findUsuarioArray(int id) {
        for (UsuarioVO user : usuarioArrayVO) {
            if (user.getIdUsuarioPessoal() == id)
                return user;
        }
        return null;
    }

}
