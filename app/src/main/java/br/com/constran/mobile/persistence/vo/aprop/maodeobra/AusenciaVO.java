package br.com.constran.mobile.persistence.vo.aprop.maodeobra;

import br.com.constran.mobile.persistence.vo.aprop.BaseVO;

/**
 * Criado em 22/07/2014
 * Classe que representa as faltas/ausencias do integrante/funcionario da equipe
 * === Cenário ===
 * Inicialmente, todos os integrantes da equipe serão apontados como faltosos.
 * Durante a formação da equipe (composição de equipe), o encarregado verifica se cada
 * integrante está presente. Os que estiverem presentes serão marcados com o checkbox
 * e os que não estiverem (faltosos) ficarão desmarcados.
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class AusenciaVO extends BaseVO {

    private EquipeTrabalhoVO equipe;
    private PessoalVO pessoa;
    private String data;

    public AusenciaVO() {

    }

    public AusenciaVO(EquipeTrabalhoVO equipe, PessoalVO pessoa, String data) {
        this.equipe = equipe;
        this.pessoa = pessoa;
        this.data = data;
    }

    public EquipeTrabalhoVO getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeTrabalhoVO equipe) {
        this.equipe = equipe;
    }

    public PessoalVO getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoalVO pessoa) {
        this.pessoa = pessoa;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AusenciaVO)) return false;

        AusenciaVO that = (AusenciaVO) o;

        if (data != null ? !data.equals(that.data) : that.data != null)
            return false;
        if (pessoa != null && pessoa.getId() != null ? !pessoa.getId().equals(that.pessoa.getId()) : that.pessoa.getId() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pessoa != null ? pessoa.hashCode() : 0;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }
}
