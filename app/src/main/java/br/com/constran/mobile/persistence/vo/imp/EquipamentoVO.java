package br.com.constran.mobile.persistence.vo.imp;

import android.util.Log;
import br.com.constran.mobile.persistence.vo.AbstractVO;

public class EquipamentoVO extends AbstractVO {

    private static final long serialVersionUID = -1247287717543989303L;

    private String prefixoComDescricao;

    private String prefixo;
    private CategoriaVO categoria;

    private String movimentacao;
    private String tipo;
    private String horimetro;
    private String quilometragem;
    private String exigeJustificativa;



    private String qrcode;

    public EquipamentoVO(Integer id, String prefixo, String descricao, String movimentacao, Integer idCategoria, String tipo, String exigeJustificativa, String horimetro, String quilometragem) {
        super(id, descricao);
        this.prefixo = prefixo;
        this.movimentacao = movimentacao;
        this.tipo = tipo;
        this.horimetro = horimetro;
        this.quilometragem = quilometragem;
        this.categoria = new CategoriaVO(idCategoria);
        this.exigeJustificativa = exigeJustificativa;
    }

    public EquipamentoVO(Integer id, String prefixo, String descricao, String movimentacao, Integer idCategoria, String tipo, String exigeJustificativa, String horimetro, String quilometragem, String qrcode) {
        super(id, descricao);
        this.prefixo = prefixo;
        this.movimentacao = movimentacao;
        this.tipo = tipo;
        this.horimetro = horimetro;
        this.quilometragem = quilometragem;
        this.categoria = new CategoriaVO(idCategoria);
        this.exigeJustificativa = exigeJustificativa;
        this.qrcode = qrcode;
    }

    public EquipamentoVO(Integer id, String prefixo, String descricao, String movimentacao) {
        super(id, descricao);
        this.prefixo = prefixo;
        this.movimentacao = movimentacao;
    }

    public EquipamentoVO(Integer id, String descricao) {
        super(id, descricao);
    }


    public EquipamentoVO(Integer id) {
        super(id);
    }

    public EquipamentoVO(String descricao) {
        super(descricao);
    }


    public EquipamentoVO() {

    }


    public String toString() {

        if (prefixo != null) {

            if(prefixoComDescricao != null){

                return getPrefixo()+" - "+getDescricao().substring(0, Math.min(getDescricao().length(), 8)).concat("...");

            }else{

                return getPrefixo();
            }
        }

        return getDescricao();
    }

    public String getPrefixo() {
        return prefixo;
    }

    public void setPrefixo(String prefixo) {
        this.prefixo = prefixo;
    }

    public String getMovimentacao() {
        return movimentacao;
    }

    public void setMovimentacao(String movimentacao) {
        this.movimentacao = movimentacao;
    }

    public CategoriaVO getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaVO categoria) {
        this.categoria = categoria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getHorimetro() {
        return horimetro;
    }

    public void setHorimetro(String horimetro) {
        this.horimetro = horimetro;
    }

    public String getExigeJustificativa() {
        return exigeJustificativa;
    }

    public void setExigeJustificativa(String exigeJustificativa) {
        this.exigeJustificativa = exigeJustificativa;
    }

    /**
     * @return the quilometragem
     */
    public String getQuilometragem() {
        return quilometragem;
    }

    /**
     * @param quilometragem the quilometragem to set
     */
    public void setQuilometragem(String quilometragem) {
        this.quilometragem = quilometragem;
    }


    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getPrefixoComDescricao() {
        return prefixo+" - "+getDescricao().substring(0, Math.min(getDescricao().length(), 5));
    }

    public void setPrefixoComDescricao(String prefixoComDescricao) {
        this.prefixoComDescricao = prefixoComDescricao;
    }
}
