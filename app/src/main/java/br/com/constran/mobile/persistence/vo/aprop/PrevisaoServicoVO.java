package br.com.constran.mobile.persistence.vo.aprop;

import br.com.constran.mobile.persistence.vo.ObraVO;
import br.com.constran.mobile.persistence.vo.imp.AtividadeVO;
import br.com.constran.mobile.persistence.vo.imp.FrenteObraVO;
import br.com.constran.mobile.persistence.vo.imp.ServicoVO;

/**
 * Criado em 19/08/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class PrevisaoServicoVO extends BaseVO {

    private ObraVO obra;
    private FrenteObraVO frenteObra;
    private AtividadeVO atividade;
    private ServicoVO servico;

    public ObraVO getObra() {
        return obra;
    }

    public void setObra(ObraVO obra) {
        this.obra = obra;
    }

    public FrenteObraVO getFrenteObra() {
        return frenteObra;
    }

    public void setFrenteObra(FrenteObraVO frenteObra) {
        this.frenteObra = frenteObra;
    }

    public AtividadeVO getAtividade() {
        return atividade;
    }

    public void setAtividade(AtividadeVO atividade) {
        this.atividade = atividade;
    }

    public ServicoVO getServico() {
        return servico;
    }

    public void setServico(ServicoVO servico) {
        this.servico = servico;
    }

}
