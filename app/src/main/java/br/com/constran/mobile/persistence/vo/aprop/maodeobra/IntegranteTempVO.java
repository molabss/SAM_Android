package br.com.constran.mobile.persistence.vo.aprop.maodeobra;

/**
 * Criado em 05/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class IntegranteTempVO extends IntegranteVO {

    public static IntegranteTempVO toIntegranteTemp(IntegranteVO integranteEquipe) {
        if (integranteEquipe == null) {
            return null;
        }

        IntegranteTempVO integranteTemp = new IntegranteTempVO();
        integranteTemp.setPessoa(integranteEquipe.getPessoa());
        integranteTemp.setEquipe(integranteEquipe.getEquipe());
        integranteTemp.setDataIngresso(integranteEquipe.getDataIngresso());
        integranteTemp.setDataSaida(integranteEquipe.getDataSaida());

        return integranteTemp;
    }

    @Override
    public String toString() {
        return " " + (getPessoa() != null ? getPessoa().getNome() : "") + "*";
    }
}
