package br.com.constran.mobile.persistence.vo.imp;

import br.com.constran.mobile.persistence.vo.AbstractVO;

public class UsuarioVO extends AbstractVO {

    private static final long serialVersionUID = 5098939117794653807L;

    private String nome;
    private String senha;
    private String grupo;
    private String funcao;
    private String matricula;
    private Integer idUsuario;
    private Integer idUsuarioPessoal;
    private String ccObra;
    private String qrcode;

    public UsuarioVO() {

    }

    public UsuarioVO(Integer idUsuario, String nome, String senha, String tipo, String grupo, String funcao) {

        super(nome);//descricao

        this.nome = nome;
        this.senha = senha;
        this.grupo = grupo;
        this.funcao = funcao;

        if (tipo.equals("P"))
            this.idUsuarioPessoal = idUsuario;//D. PESSOAL
        else
            this.idUsuario = idUsuario;//WEB
    }

    public UsuarioVO(Integer codUsuario, Integer idUsuario, String nome, String senha, String tipo, String grupo, String funcao) {

        super(codUsuario, nome);//id, descricao

        this.nome = nome;
        this.senha = senha;
        this.grupo = grupo;
        this.funcao = funcao;

        if (tipo.equals("P"))
            this.idUsuarioPessoal = idUsuario;//D. PESSOAL
        else
            this.idUsuario = idUsuario;//WEB
    }

    public UsuarioVO(Integer codUsuario, Integer idUsuario, Integer idUsuarioPessoal, String nome, String senha) {

        super(codUsuario, nome);//id, descricao

        this.nome = nome;
        this.senha = senha;
        this.idUsuario = idUsuario;//WEB
        this.idUsuarioPessoal = idUsuarioPessoal;//D. PESSOAL
    }


    public UsuarioVO(Integer codUsuario, String nome) {
        super(codUsuario, nome);//id, descricao
        this.nome = nome;
    }

    public UsuarioVO(String nome) {
        super(nome);//descricao
        this.nome = nome;
    }

    public UsuarioVO(Integer id) {
        super(id);
    }

    @Override
    public String toString() {
        return getDescricao();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public Integer getIdUsuarioPessoal() {
        return idUsuarioPessoal;
    }

    public void setIdUsuarioPessoal(Integer idUsuarioPessoal) {
        this.idUsuarioPessoal = idUsuarioPessoal;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getGrupo() {
        return grupo;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getCcObra() {
        return ccObra;
    }

    public void setCcObra(String ccObra) {
        this.ccObra = ccObra;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
}
