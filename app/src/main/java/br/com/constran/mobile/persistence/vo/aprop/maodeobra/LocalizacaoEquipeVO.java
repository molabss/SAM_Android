package br.com.constran.mobile.persistence.vo.aprop.maodeobra;

import br.com.constran.mobile.persistence.vo.aprop.BaseVO;
import br.com.constran.mobile.persistence.vo.menu.LocalizacaoVO;

/**
 * Criado em 18/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class LocalizacaoEquipeVO extends BaseVO {

    private LocalizacaoVO localizacao;
    private EquipeTrabalhoVO equipe;
    private String dataHora;

    public LocalizacaoVO getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(LocalizacaoVO localizacao) {
        this.localizacao = localizacao;
    }

    public EquipeTrabalhoVO getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeTrabalhoVO equipe) {
        this.equipe = equipe;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }
}
