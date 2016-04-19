package br.com.constran.mobile.persistence.vo.aprop.maodeobra;

import br.com.constran.mobile.persistence.vo.ObraVO;
import br.com.constran.mobile.persistence.vo.aprop.BaseVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Criado em 05/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class EquipeTrabalhoVO extends BaseVO {

    private static final long serialVersionUID = 2771871365347208024L;

    private Integer id;
    private ObraVO obra;
    private String nomeEquipe;
    private String apelido;
    private Integer formacao;
    private String dataCriacao;
    private PessoalVO responsavel;
    private String ativa;
    private String apropriavel;
    private HorarioTrabalhoVO horarioTrabalho;
//    private EquipeTrabalhoVO equipeAssociada;

    private transient List<? extends IntegranteVO> integrantesFixos;
    private transient List<? extends IntegranteVO> integrantesTemporarios;

    public EquipeTrabalhoVO() {

    }

    public EquipeTrabalhoVO(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ObraVO getObra() {
        return obra;
    }

    public void setObra(ObraVO obra) {
        this.obra = obra;
    }

    public String getNomeEquipe() {
        return nomeEquipe;
    }

    public void setNomeEquipe(String nomeEquipe) {
        this.nomeEquipe = nomeEquipe;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public Integer getFormacao() {
        return formacao;
    }

    public void setFormacao(Integer formacao) {
        this.formacao = formacao;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public PessoalVO getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(PessoalVO responsavel) {
        this.responsavel = responsavel;
    }

    public String getAtiva() {
        return ativa;
    }

    public void setAtiva(String ativa) {
        this.ativa = ativa;
    }

    public String getApropriavel() {
        return apropriavel;
    }

    public void setApropriavel(String apropriavel) {
        this.apropriavel = apropriavel;
    }

    public HorarioTrabalhoVO getHorarioTrabalho() {
        return horarioTrabalho;
    }

    public void setHorarioTrabalho(HorarioTrabalhoVO horarioTrabalho) {
        this.horarioTrabalho = horarioTrabalho;
    }

//    public EquipeTrabalhoVO getEquipeAssociada() {
//        return equipeAssociada;
//    }
//
//    public void setEquipeAssociada(EquipeTrabalhoVO equipeAssociada) {
//        this.equipeAssociada = equipeAssociada;
//    }

    public List<? extends IntegranteVO> getIntegrantesFixos() {
        return integrantesFixos;
    }

    public void setIntegrantesFixos(List<? extends IntegranteVO> integrantesFixos) {
        this.integrantesFixos = integrantesFixos;
    }

    public List<? extends IntegranteVO> getIntegrantesTemporarios() {
        return integrantesTemporarios;
    }

    public void setIntegrantesTemporarios(List<? extends IntegranteVO> integrantesTemporarios) {
        this.integrantesTemporarios = integrantesTemporarios;
    }

    public List<IntegranteVO> getIntegrantes() {
        List<IntegranteVO> integrantes = new ArrayList<IntegranteVO>();

        if (integrantesFixos != null) {
            integrantes.addAll(integrantesFixos);
        }

        if (integrantesTemporarios != null) {
            integrantes.addAll(integrantesTemporarios);
        }

        return integrantes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        EquipeTrabalhoVO that = (EquipeTrabalhoVO) o;

        if (!id.equals(that.id))
            return false;

        return true;
    }

    @Override
    public String toString() {
        if (apelido != null) {
            return " " + apelido;
        }

        return " (EQUIPE SEM APELIDO)";
    }
}
