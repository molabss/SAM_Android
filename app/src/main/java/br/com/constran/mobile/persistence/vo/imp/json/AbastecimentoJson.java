package br.com.constran.mobile.persistence.vo.imp.json;

import br.com.constran.mobile.persistence.vo.rae.RaeVO;
import br.com.constran.mobile.persistence.vo.rae.abs.AbastecimentoVO;

import java.util.ArrayList;
import java.util.List;

public class AbastecimentoJson {

    private RaeVO rae;

    private AbastecimentoVO cabecalho;

    private List<AbastecimentoVO> abastecimentos;

    public AbastecimentoJson() {

        cabecalho = new AbastecimentoVO();
        abastecimentos = new ArrayList<AbastecimentoVO>();
    }

    public AbastecimentoVO getCabecalho() {
        return cabecalho;
    }

    public List<AbastecimentoVO> getAbastecimentos() {
        return abastecimentos;
    }

    public void setCabecalho(AbastecimentoVO cabecalho) {
        this.cabecalho = cabecalho;
    }

    public void setAbastecimentos(List<AbastecimentoVO> abastecimentos) {
        this.abastecimentos = abastecimentos;
    }

    public RaeVO getRae() {
        return rae;
    }

    public void setRae(RaeVO rae) {
        this.rae = rae;
    }


}
