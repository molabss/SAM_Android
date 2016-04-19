package br.com.constran.mobile.persistence.vo.imp;

import java.io.Serializable;
import java.util.StringTokenizer;


public class AtividadeVO implements Serializable {

    private static final long serialVersionUID = -8840964478818699577L;

    private FrenteObraVO frenteObra;
    private Integer idAtividade;
    private String descricao;
    private String strId;

    public AtividadeVO(String id, String token) {

        if (id != null) {

            StringTokenizer st = new StringTokenizer(id, token);

            this.frenteObra = new FrenteObraVO(Integer.parseInt(st.nextToken()));
            this.idAtividade = Integer.parseInt(st.nextToken());
        }

        this.strId = id;

    }

    public AtividadeVO(String descricao) {
        this.descricao = descricao;
    }

    public AtividadeVO() {

    }

    public AtividadeVO(Integer idAtividade, String descricao) {
        this.idAtividade = idAtividade;
        this.descricao = descricao;
    }

    public AtividadeVO(Integer idAtividade, Integer idFrenteObra) {
        this.idAtividade = idAtividade;
        this.frenteObra = new FrenteObraVO(idFrenteObra);
    }

    public AtividadeVO(Integer idAtividade, Integer idFrenteObra, String descricao) {
        this.idAtividade = idAtividade;
        this.frenteObra = new FrenteObraVO(idFrenteObra);
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return getDescricao();
    }

    public Integer getIdAtividade() {
        return idAtividade;
    }

    public void setIdAtividade(Integer idAtividade) {
        this.idAtividade = idAtividade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public FrenteObraVO getFrenteObra() {
        return frenteObra;
    }

    public void setFrenteObra(FrenteObraVO frenteObra) {
        this.frenteObra = frenteObra;
    }

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }


}
