package br.com.constran.mobile.persistence.vo.aprop.mov;

import br.com.constran.mobile.persistence.vo.AbstractVO;
import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;
import br.com.constran.mobile.persistence.vo.imp.MaterialVO;
import br.com.constran.mobile.persistence.vo.imp.OrigemDestinoVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

public class ApropriacaoMovimentacaoVO extends AbstractVO {

    private static final long serialVersionUID = 8030435075076215282L;
    //PK
    private Integer idApropriacao;
    private EquipamentoVO equipamento;
    private String horaInicio;

    //FK
    private MaterialVO material;
    private OrigemDestinoVO origem;
    private OrigemDestinoVO destino;

    private String horaTermino;
    private String qtdViagens;
    private String prcCarga;
    private String horimetroIni;
    private String horimetroFim;

    private String estacaOrigemInicial;
    private String estacaOrigemFinal;
    private String estacaDestinoInicial;
    private String estacaDestinoFinal;

    private Collection<ViagemVO> viagens;

    public ApropriacaoMovimentacaoVO() {

    }

    public ApropriacaoMovimentacaoVO(String id, String token) {

        if (id != null) {

            StringTokenizer st = new StringTokenizer(id, token);

            this.idApropriacao = Integer.parseInt(st.nextToken());
            this.equipamento = new EquipamentoVO(Integer.parseInt(st.nextToken()));
            this.horaInicio = st.nextToken();
        }

        this.strId = id;

        this.viagens = new ArrayList<ViagemVO>();

    }

    @Override
    public String toString() {
        return getDescricao();
    }

    public Collection<ViagemVO> getViagens() {
        return viagens;
    }

    public void setViagens(Collection<ViagemVO> viagens) {
        this.viagens = viagens;
    }

    public Integer getIdApropriacao() {
        return idApropriacao;
    }

    public void setIdApropriacao(Integer idApropriacao) {
        this.idApropriacao = idApropriacao;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public MaterialVO getMaterial() {
        return material;
    }

    public void setMaterial(MaterialVO material) {
        this.material = material;
    }

    public OrigemDestinoVO getOrigem() {
        return origem;
    }

    public void setOrigem(OrigemDestinoVO origem) {
        this.origem = origem;
    }

    public OrigemDestinoVO getDestino() {
        return destino;
    }

    public void setDestino(OrigemDestinoVO destino) {
        this.destino = destino;
    }

    public String getHoraTermino() {
        return horaTermino;
    }

    public void setHoraTermino(String horaTermino) {
        this.horaTermino = horaTermino;
    }

    public String getQtdViagens() {
        return qtdViagens;
    }

    public void setQtdViagens(String qtdViagens) {
        this.qtdViagens = qtdViagens;
    }

    public String getPrcCarga() {
        return prcCarga;
    }

    public void setPrcCarga(String prcCarga) {
        this.prcCarga = prcCarga;
    }

    public String getHorimetroIni() {
        return horimetroIni;
    }

    public void setHorimetroIni(String horimetroIni) {
        this.horimetroIni = horimetroIni;
    }

    public String getHorimetroFim() {
        return horimetroFim;
    }

    public void setHorimetroFim(String horimetroFim) {
        this.horimetroFim = horimetroFim;
    }

    public String getEstacaOrigemInicial() {
        return estacaOrigemInicial;
    }

    public void setEstacaOrigemInicial(String estacaOrigemInicial) {
        this.estacaOrigemInicial = estacaOrigemInicial;
    }

    public String getEstacaOrigemFinal() {
        return estacaOrigemFinal;
    }

    public void setEstacaOrigemFinal(String estacaOrigemFinal) {
        this.estacaOrigemFinal = estacaOrigemFinal;
    }

    public String getEstacaDestinoInicial() {
        return estacaDestinoInicial;
    }

    public void setEstacaDestinoInicial(String estacaDestinoInicial) {
        this.estacaDestinoInicial = estacaDestinoInicial;
    }

    public String getEstacaDestinoFinal() {
        return estacaDestinoFinal;
    }

    public void setEstacaDestinoFinal(String estacaDestinoFinal) {
        this.estacaDestinoFinal = estacaDestinoFinal;
    }

    public EquipamentoVO getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(EquipamentoVO equipamento) {
        this.equipamento = equipamento;
    }


}
