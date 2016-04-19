package br.com.constran.mobile.persistence.vo.imp.json;

import br.com.constran.mobile.persistence.vo.aprop.ApropriacaoVO;
import br.com.constran.mobile.persistence.vo.aprop.indpluv.IndicePluviometricoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.ParalisacaoMaoObraVO;
import br.com.constran.mobile.persistence.vo.menu.ConfiguracoesVO;
import br.com.constran.mobile.persistence.vo.rae.RaeVO;
import br.com.constran.mobile.persistence.vo.rae.abs.SaldoPostoVO;

import java.util.ArrayList;
import java.util.List;

public class ExportMobile {

    private ConfiguracoesVO configuracoes;

    private SaldoPostoVO saldoPosto;

    private List<ApropriacaoVO> apropriacoes;

    private List<ParalisacaoMaoObraVO> paralisacoesMaoObra;

    private List<IndicePluviometricoVO> indicesPluviometricos;

    private List<RaeVO> raes;

    public ExportMobile() {

        apropriacoes = new ArrayList<ApropriacaoVO>();
        indicesPluviometricos = new ArrayList<IndicePluviometricoVO>();
    }

    public ConfiguracoesVO getConfiguracoes() {
        return configuracoes;
    }

    public List<ApropriacaoVO> getApropriacoes() {
        return apropriacoes;
    }

    public List<RaeVO> getRaes() {
        return raes;
    }

    public void setConfiguracoes(ConfiguracoesVO configuracoes) {
        this.configuracoes = configuracoes;
    }

    public void setApropriacoes(List<ApropriacaoVO> apropriacoes) {
        this.apropriacoes = apropriacoes;
    }

    public List<ParalisacaoMaoObraVO> getParalisacoesMaoObra() {
        return paralisacoesMaoObra;
    }

    public void setParalisacoesMaoObra(List<ParalisacaoMaoObraVO> paralisacoesMaoObra) {
        this.paralisacoesMaoObra = paralisacoesMaoObra;
    }

    public void setRaes(List<RaeVO> raes) {
        this.raes = raes;
    }

    public SaldoPostoVO getSaldoPosto() {
        return saldoPosto;
    }

    public void setSaldoPosto(SaldoPostoVO saldoPosto) {
        this.saldoPosto = saldoPosto;
    }

    public List<IndicePluviometricoVO> getIndicesPluviometricos() {
        return indicesPluviometricos;
    }

    public void setIndicesPluviometricos(List<IndicePluviometricoVO> indicesPluviometricos) {
        this.indicesPluviometricos = indicesPluviometricos;
    }
}
