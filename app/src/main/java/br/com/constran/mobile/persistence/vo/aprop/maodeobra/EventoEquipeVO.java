package br.com.constran.mobile.persistence.vo.aprop.maodeobra;

import br.com.constran.mobile.persistence.vo.aprop.ApropriacaoVO;
import br.com.constran.mobile.persistence.vo.aprop.BaseVO;
import br.com.constran.mobile.persistence.vo.imp.ParalisacaoVO;
import br.com.constran.mobile.persistence.vo.imp.ServicoVO;
import br.com.constran.mobile.persistence.vo.menu.LocalizacaoVO;
import br.com.constran.mobile.view.util.Util;

import java.util.Date;

/**
 * Criado em 10/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class EventoEquipeVO extends BaseVO implements Comparable<EventoEquipeVO> {

    private static final long serialVersionUID = 7699952798766616573L;

    private Integer id;
    private String data;
    private String horaIni;
    private String horaFim;
    private String observacao;
    private LocalizacaoVO localizacao;
    private TipoApontamento tipoApontamento;
    private ApropriacaoVO apropriacao;
    private ParalisacaoVO paralisacao;//tipo de evento é a paralisacao
    private EquipeTrabalhoVO equipe;
    private ServicoVO servico;

    private transient String horaFimAntiga;
    private transient ServicoVO servicoAntigo;
    private transient ParalisacaoVO paralisacaoAntiga;

    public EventoEquipeVO() {
    }

    public EventoEquipeVO(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHoraIni() {
        return horaIni;
    }

    public void setHoraIni(String horaIni) {
        this.horaIni = horaIni;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao != null && observacao.isEmpty() ? null : observacao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public LocalizacaoVO getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(LocalizacaoVO localizacao) {
        this.localizacao = localizacao;
    }

    public ParalisacaoVO getParalisacao() {
        return paralisacao;
    }

    public void setParalisacao(ParalisacaoVO paralisacao) {
        this.paralisacao = paralisacao;
    }

    public EquipeTrabalhoVO getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeTrabalhoVO equipe) {
        this.equipe = equipe;
    }

    public ServicoVO getServico() {
        return servico;
    }

    public void setServico(ServicoVO servico) {
        this.servico = servico;
    }

    public ApropriacaoVO getApropriacao() {
        return apropriacao;
    }

    public void setApropriacao(ApropriacaoVO apropriacao) {
        this.apropriacao = apropriacao;
    }

    public TipoApontamento getTipoApontamento() {
        return tipoApontamento;
    }

    public void setTipoApontamento(TipoApontamento tipoApontamento) {
        this.tipoApontamento = tipoApontamento;
    }

    public String getHoraFimAntiga() {
        return horaFimAntiga;
    }

    public void setHoraFimAntiga(String horaFimAntiga) {
        this.horaFimAntiga = horaFimAntiga;
    }

    public ServicoVO getServicoAntigo() {
        return servicoAntigo;
    }

    public void setServicoAntigo(ServicoVO servicoAntigo) {
        this.servicoAntigo = servicoAntigo;
    }

    public ParalisacaoVO getParalisacaoAntiga() {
        return paralisacaoAntiga;
    }

    public void setParalisacaoAntiga(ParalisacaoVO paralisacaoAntiga) {
        this.paralisacaoAntiga = paralisacaoAntiga;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventoEquipeVO that = (EventoEquipeVO) o;

        if (id != null && that.id == null) return false;
        if (id == null && that.id != null) return false;
        if (id != null && !id.equals(that.id)) return false;
        if (!horaIni.equals(that.horaIni)) return false;
        if (!equipe.equals(that.equipe)) return false;
//        if (!paralisacao.equals(that.paralisacao)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = horaIni != null ? horaIni.hashCode() : 31;
        result = 31 * result + (paralisacao != null ? paralisacao.hashCode() : 0);
        result = 31 * result + (equipe != null ? equipe.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(EventoEquipeVO eventoEquipeVO) {
        if (eventoEquipeVO == null || eventoEquipeVO.getHoraIni() == null) {
            return 0;
        }

        Date dataIni = Util.toDateFormat(data);
        Date dataIni2 = Util.toDateFormat(eventoEquipeVO.getData());

        if (dataIni.compareTo(dataIni2) == 0) {
            Date hIni = Util.toHourFormat(horaIni);
            Date hIni2 = Util.toHourFormat(eventoEquipeVO.getHoraIni());

            if (hIni.after(hIni2)) {
                return 1;
            }

            return hIni.before(hIni2) ? -1 : 0;
        }

        return dataIni.compareTo(dataIni2);
    }

    @Override
    public String toString() {
        if (equipe == null || apropriacao == null) {
            return super.toString();
        }

        if (apropriacao != null) {
            String descricao = apropriacao.getDescricao() != null ? apropriacao.getDescricao() : "";

            if (data != null && equipe.getApelido() != null) {
                return " " + data + " " + Util.toCamelCase(equipe.getApelido()) + " " + descricao;
            }
        }

        return super.toString();
    }

    public EventoEquipeVO clone() {
        EventoEquipeVO ee = new EventoEquipeVO();
        ee.setId(id);
        ee.setApropriacao(apropriacao);
        ee.setObservacao(observacao);
        ee.setServico(servico);
        ee.setEquipe(equipe);
        ee.setHoraIni(horaIni);
        ee.setHoraFim(horaFim);
        ee.setData(data);
        ee.setLocalizacao(localizacao);
        ee.setParalisacao(paralisacao);
        ee.setTipoApontamento(tipoApontamento);

        ee.setHoraFimAntiga(horaFimAntiga);
        ee.setServicoAntigo(servicoAntigo);
        ee.setParalisacaoAntiga(paralisacaoAntiga);

        return ee;
    }

}
