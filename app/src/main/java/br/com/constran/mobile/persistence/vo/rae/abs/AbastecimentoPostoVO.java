package br.com.constran.mobile.persistence.vo.rae.abs;

import br.com.constran.mobile.persistence.vo.AbstractVO;

import java.util.StringTokenizer;

public class AbastecimentoPostoVO extends AbstractVO {

    private static final long serialVersionUID = -6878253040745922616L;

    //PK
    private CombustivelLubrificanteVO combustivelLubrificante;
    private PostoVO posto;
    private String data;
    private String hora;

    private String qtd;

    private PostoVO posto2;

    private String tipo;

    public AbastecimentoPostoVO() {

    }

    public AbastecimentoPostoVO(String id, String token) {

        if (id != null) {

            StringTokenizer st = new StringTokenizer(id, token);

            this.posto = new PostoVO(Integer.parseInt(st.nextToken()));
            this.combustivelLubrificante = new CombustivelLubrificanteVO(Integer.parseInt(st.nextToken()));
            this.data = st.nextToken();
            this.hora = st.nextToken();
        }

        this.strId = id;

    }

    public CombustivelLubrificanteVO getCombustivelLubrificante() {
        return combustivelLubrificante;
    }

    public void setCombustivelLubrificante(
            CombustivelLubrificanteVO combustivelLubrificante) {
        this.combustivelLubrificante = combustivelLubrificante;
    }

    public PostoVO getPosto() {
        return posto;
    }

    public void setPosto(PostoVO posto) {
        this.posto = posto;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getQtd() {
        return qtd;
    }

    public void setQtd(String qtd) {
        this.qtd = qtd;
    }

    public PostoVO getPosto2() {
        return posto2;
    }

    public void setPosto2(PostoVO posto2) {
        this.posto2 = posto2;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


}
