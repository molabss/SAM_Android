package br.com.constran.mobile.persistence.vo.aprop;

import br.com.constran.mobile.persistence.vo.AbstractVO;
import br.com.constran.mobile.persistence.vo.aprop.eqp.ApropriacaoEquipamentoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.ApropriacaoMaoObraVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.ApropriacaoServicoVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.ParalisacaoEquipeVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.ParalisacaoMaoObraVO;
import br.com.constran.mobile.persistence.vo.aprop.mov.ApropriacaoMovimentacaoVO;
import br.com.constran.mobile.persistence.vo.imp.AtividadeVO;

import java.util.ArrayList;
import java.util.List;


public class ApropriacaoVO extends AbstractVO {

    private static final long serialVersionUID = -1088157682615086225L;
    private AtividadeVO atividade;

    private String tipoApropriacao;
    private String dataHoraApontamento;
    private String observacoes;


    private List<ApropriacaoMovimentacaoVO> movimentacoes;
    private List<ApropriacaoEquipamentoVO> partesDiarias;

    private List<ApropriacaoServicoVO> servicos;
    private List<ApropriacaoMaoObraVO> maosObras;
    private List<ParalisacaoEquipeVO> paralisacoesEquipe;
    private List<ParalisacaoMaoObraVO> paralisacoesMaoObra;

    public ApropriacaoVO() {
    }

    public ApropriacaoVO(Integer id, String tipoApropriacao) {
        super(id);

        this.tipoApropriacao = tipoApropriacao;

        movimentacoes = new ArrayList<ApropriacaoMovimentacaoVO>();
        partesDiarias = new ArrayList<ApropriacaoEquipamentoVO>();

    }

    @Override
    public String toString() {
        return getDescricao();
    }

    public AtividadeVO getAtividade() {
        return atividade;
    }


    public void setAtividade(AtividadeVO atividade) {
        this.atividade = atividade;
    }


    public String getTipoApropriacao() {
        return tipoApropriacao;
    }


    public void setTipoApropriacao(String tipoApropriacao) {
        this.tipoApropriacao = tipoApropriacao;
    }


    public String getDataHoraApontamento() {
        return dataHoraApontamento;
    }


    public void setDataHoraApontamento(String dataHoraApontamento) {
        this.dataHoraApontamento = dataHoraApontamento;
    }


    public String getObservacoes() {
        return observacoes == null ? "" : observacoes;
    }


    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }


    public List<ApropriacaoMovimentacaoVO> getMovimentacoes() {
        return movimentacoes;
    }


    public void setMovimentacoes(List<ApropriacaoMovimentacaoVO> movimentacoes) {
        this.movimentacoes = movimentacoes;
    }

    public List<ApropriacaoEquipamentoVO> getPartesDiarias() {
        return partesDiarias;
    }

    public void setPartesDiarias(List<ApropriacaoEquipamentoVO> partesDiarias) {
        this.partesDiarias = partesDiarias;
    }

    public List<ApropriacaoServicoVO> getServicos() {
        return servicos;
    }

    public void setServicos(List<ApropriacaoServicoVO> servicos) {
        this.servicos = servicos;
    }

    public List<ApropriacaoMaoObraVO> getMaosObras() {
        return maosObras;
    }

    public void setMaosObras(List<ApropriacaoMaoObraVO> maosObras) {
        this.maosObras = maosObras;
    }

    public List<ParalisacaoEquipeVO> getParalisacoesEquipe() {
        return paralisacoesEquipe;
    }

    public void setParalisacoesEquipe(List<ParalisacaoEquipeVO> paralisacoesEquipe) {
        this.paralisacoesEquipe = paralisacoesEquipe;
    }

    public List<ParalisacaoMaoObraVO> getParalisacoesMaoObra() {
        return paralisacoesMaoObra;
    }

    public void setParalisacoesMaoObra(List<ParalisacaoMaoObraVO> paralisacoesMaoObra) {
        this.paralisacoesMaoObra = paralisacoesMaoObra;
    }
}
