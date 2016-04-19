package br.com.constran.mobile.persistence.vo.aprop.maodeobra;

/**
 * Criado em 05/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class IntegranteEquipeVO extends IntegranteVO {

    public static IntegranteEquipeVO toIntegranteEquipe(IntegranteVO integranteEquipe) {
        if (integranteEquipe == null) {
            return null;
        }

        IntegranteEquipeVO integrante = new IntegranteEquipeVO();
        integrante.setPessoa(integranteEquipe.getPessoa());
        integrante.setEquipe(integranteEquipe.getEquipe());
        integrante.setDataIngresso(integranteEquipe.getDataIngresso());
        integrante.setDataSaida(integranteEquipe.getDataSaida());

        return integrante;
    }

    @Override
    public String toString() {
        return " " + (getPessoa() != null ? getPessoa().getNome() : "");
    }


}
