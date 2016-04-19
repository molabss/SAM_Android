package br.com.constran.mobile.persistence.vo.rae;

import br.com.constran.mobile.persistence.vo.AbstractVO;
import br.com.constran.mobile.persistence.vo.rae.abs.AbastecimentoVO;
import br.com.constran.mobile.persistence.vo.rae.abs.PostoVO;

import java.util.ArrayList;
import java.util.List;

public class RaeVO extends AbstractVO {

    private static final long serialVersionUID = -7655796984402299296L;
    private List<AbastecimentoVO> abastecimentos;

    private String data;
    private PostoVO posto;

    private String totalizadorInicial;
    private String totalizadorFinal;

    public RaeVO() {

    }

    public RaeVO(Integer id) {

        super(id);

        abastecimentos = new ArrayList<AbastecimentoVO>();
    }

    public RaeVO(Integer id, String data) {

        super(id);

        this.data = data;

        abastecimentos = new ArrayList<AbastecimentoVO>();
    }

    public RaeVO(String data) {

        this.data = data;

    }

    @Override
    public String toString() {

        return getPosto().getDescricao() + " " + getData();
    }

    public List<AbastecimentoVO> getAbastecimentos() {
        return abastecimentos;
    }

    public String getData() {
        return data;
    }

    public void setAbastecimentos(List<AbastecimentoVO> abastecimentos) {
        this.abastecimentos = abastecimentos;
    }

    public void setData(String data) {
        this.data = data;
    }

    public PostoVO getPosto() {
        return posto;
    }

    public void setPosto(PostoVO posto) {
        this.posto = posto;
    }

    public String getTotalizadorInicial() {
        return totalizadorInicial;
    }

    public String getTotalizadorFinal() {
        return totalizadorFinal;
    }

    public void setTotalizadorInicial(String totalizadorInicial) {
        this.totalizadorInicial = totalizadorInicial;
    }

    public void setTotalizadorFinal(String totalizadorFinal) {
        this.totalizadorFinal = totalizadorFinal;
    }
}
