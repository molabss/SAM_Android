package br.com.constran.mobile.persistence.vo.aprop.maodeobra;

import br.com.constran.mobile.persistence.vo.aprop.BaseVO;

/**
 * Criado em 05/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class PessoalVO extends BaseVO {

    private Integer id;
    //    private String origem;
    private String nome;
    private String matricula;
    private String senha;

//    private FornecedorVO fornecedor;

    public PessoalVO() {
    }

    public PessoalVO(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

//    public String getOrigem() {
//        return origem;
//    }
//
//    public void setOrigem(String origem) {
//        this.origem = origem;
//    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
//
//    public FornecedorVO getFornecedor() {
//        return fornecedor;
//    }
//
//    public void setFornecedor(FornecedorVO fornecedor) {
//        this.fornecedor = fornecedor;
//    }
}
