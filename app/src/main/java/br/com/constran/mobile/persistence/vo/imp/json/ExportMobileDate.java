package br.com.constran.mobile.persistence.vo.imp.json;

import br.com.constran.mobile.persistence.vo.ManutencaoEquipamentoVO;
import br.com.constran.mobile.persistence.vo.aprop.ApropriacaoVO;
import br.com.constran.mobile.persistence.vo.aprop.indpluv.IndicePluviometricoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.ParalisacaoMaoObraVO;
import br.com.constran.mobile.persistence.vo.menu.ConfiguracoesVO;
import br.com.constran.mobile.persistence.vo.rae.RaeVO;
import br.com.constran.mobile.persistence.vo.rae.abs.SaldoPostoVO;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mateus_vitali on 31/10/2014.
 */
public class ExportMobileDate {

    private String ccObra;

    private String date;

    private String dispositivo;

    private ConfiguracoesVO configuracoes;

    private SaldoPostoVO saldoPosto;

    private List<ApropriacaoVO> apropriacoes;

    private List<ParalisacaoMaoObraVO> paralisacoesMaoObra;

    private List<IndicePluviometricoVO> indicesPluviometricos;


    private List<ManutencaoEquipamentoVO> manutencaoEquipamentos;

    private List<RaeVO> raes;

    public ExportMobileDate() {
        apropriacoes = new ArrayList<ApropriacaoVO>();
        paralisacoesMaoObra = new ArrayList<ParalisacaoMaoObraVO>();
        indicesPluviometricos = new ArrayList<IndicePluviometricoVO>();
        raes = new ArrayList<RaeVO>();
        manutencaoEquipamentos = new ArrayList<ManutencaoEquipamentoVO>();
    }

    public List<ManutencaoEquipamentoVO> getManutencaoEquipamentos() {
        return manutencaoEquipamentos;
    }

    public void setManutencaoEquipamentos(List<ManutencaoEquipamentoVO> manutencaoEquipamentos) {
        this.manutencaoEquipamentos = manutencaoEquipamentos;
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

    public String getCcObra() {
        return ccObra;
    }

    public void setCcObra(String ccObra) {
        this.ccObra = ccObra;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }
}
