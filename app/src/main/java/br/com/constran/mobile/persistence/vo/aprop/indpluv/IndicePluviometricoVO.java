package br.com.constran.mobile.persistence.vo.aprop.indpluv;

import br.com.constran.mobile.persistence.vo.aprop.BaseVO;

/**
 * @author Mateus Vitali <mateus.vitali@constran.com.br>
 * @version 1.0
 * @since 2014-09-01
 */
public class IndicePluviometricoVO extends BaseVO implements Comparable<IndicePluviometricoVO> {

    private static final long serialVersionUID = -5165121780698634030L;

    private Integer id;
    private String data;
    private String estacaInicial;
    private String estacaFinal;
    private String pluviometro;
    private Integer volumeChuva;

    public IndicePluviometricoVO() {
        this.data = "";
        this.estacaInicial = "";
        this.estacaFinal = "";
        this.pluviometro = "";
        this.volumeChuva = 0;
    }

    @Override
    public boolean equals(Object o) {
        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public int compareTo(IndicePluviometricoVO indicePluviometricoVO) {
        return 0;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEstacaInicial() {
        return estacaInicial;
    }

    public void setEstacaInicial(String estacaInicial) {
        this.estacaInicial = estacaInicial;
    }

    public String getEstacaFinal() {
        return estacaFinal;
    }

    public void setEstacaFinal(String estacaFinal) {
        this.estacaFinal = estacaFinal;
    }

    public String getPluviometro() {
        return pluviometro;
    }

    public void setPluviometro(String pluviometro) {
        this.pluviometro = pluviometro;
    }

    public Integer getVolumeChuva() {
        return volumeChuva;
    }

    public void setVolumeChuva(Integer volumeChuva) {
        this.volumeChuva = volumeChuva;
    }
}

