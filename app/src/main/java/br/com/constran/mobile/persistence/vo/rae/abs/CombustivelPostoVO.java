package br.com.constran.mobile.persistence.vo.rae.abs;

import br.com.constran.mobile.persistence.vo.AbstractVO;

public class CombustivelPostoVO extends AbstractVO {

    private static final long serialVersionUID = 6675449449922609858L;

    private CombustivelLubrificanteVO combustivel;

    private PostoVO posto;

    public CombustivelPostoVO(Integer idPosto, String pDescricao, Integer idEquipamento, String pTipo, Integer idCombustivel, String cDescricao, String unidadeMedida, String cTipo) {
        posto = new PostoVO(idPosto, pDescricao, idEquipamento, pTipo);
        combustivel = new CombustivelLubrificanteVO(idCombustivel, cDescricao, unidadeMedida, cTipo);
    }

    public CombustivelPostoVO(Integer idPosto, Integer idCombustivel) {
        posto = new PostoVO(idPosto);
        combustivel = new CombustivelLubrificanteVO(idCombustivel);
    }

    public CombustivelLubrificanteVO getCombustivel() {
        return combustivel;
    }

    public PostoVO getPosto() {
        return posto;
    }

    public void setCombustivel(CombustivelLubrificanteVO combustivel) {
        this.combustivel = combustivel;
    }

    public void setPosto(PostoVO posto) {
        this.posto = posto;
    }

    @Override
    public String toString() {

        return getDescricao();
    }
}
