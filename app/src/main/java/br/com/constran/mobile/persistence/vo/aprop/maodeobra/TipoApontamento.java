package br.com.constran.mobile.persistence.vo.aprop.maodeobra;

import com.google.gson.annotations.SerializedName;

/**
 * Criado em 24/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public enum TipoApontamento {

    @SerializedName("1")
    MANUAL(1, "Manual"),

    @SerializedName("2")
    AUTOMATICO(2, "Automático");

    private final int codigo;
    private final String descricao;

    private TipoApontamento(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoApontamento findByCodigo(Integer codigo) {
        if (codigo != null) {
            for (TipoApontamento ts : values()) {
                if (codigo == ts.getCodigo()) {
                    return ts;
                }
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
