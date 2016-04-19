package br.com.constran.mobile.persistence.vo.aprop.maodeobra;

import br.com.constran.mobile.persistence.vo.aprop.BaseVO;

/**
 * Criado em 14/07/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class IntegranteVO extends BaseVO implements Comparable<IntegranteVO> {

    private EquipeTrabalhoVO equipe;
    private PessoalVO pessoa;
    private String dataIngresso;
    private String dataSaida;

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

    public String getDataIngresso() {
        return dataIngresso;
    }

    public void setDataIngresso(String dataIngresso) {
        this.dataIngresso = dataIngresso;
    }

    public String getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(String dataSaida) {
        this.dataSaida = dataSaida;
    }

    @Override
    public int compareTo(IntegranteVO integranteVO) {
        if (pessoa != null && pessoa.getNome() != null && integranteVO.getPessoa() != null && integranteVO.getPessoa().getNome() != null) {
            return pessoa.getNome().compareTo(integranteVO.getPessoa().getNome());
        }

        return 0;
    }
}
