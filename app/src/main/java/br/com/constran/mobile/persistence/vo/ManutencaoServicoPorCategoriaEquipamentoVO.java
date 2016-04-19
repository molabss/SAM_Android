package br.com.constran.mobile.persistence.vo;

/**
 * Created by moises_santana on 10/04/2015.
 */
public class ManutencaoServicoPorCategoriaEquipamentoVO extends AbstractVO {

    private ManutencaoServicosVO manutencaoServico;
    private EquipamentoCategoriaVO equipamentoCategoria;


    public ManutencaoServicosVO getManutencaoServico() {
        return manutencaoServico;
    }

    public void setManutencaoServico(ManutencaoServicosVO manutencaoServico) {
        this.manutencaoServico = manutencaoServico;
    }

    public EquipamentoCategoriaVO getEquipamentoCategoria() {
        return equipamentoCategoria;
    }

    public void setEquipamentoCategoria(EquipamentoCategoriaVO equipamentoCategoria) {
        this.equipamentoCategoria = equipamentoCategoria;
    }


    @Override
    public String toString() {
        return getManutencaoServico().getDescricao();
    }
}
