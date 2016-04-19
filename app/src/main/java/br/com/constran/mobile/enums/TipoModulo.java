package br.com.constran.mobile.enums;

/**
 * Criado em 04/09/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public enum TipoModulo {
    EQUIPAMENTO("1"),
    MAO_DE_OBRA("2"),
    FICHA_MOTORISTA("3"),
    FICHA_3_VIAS("4");

    private final String codigo;

    TipoModulo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public String toString() {
        return codigo;
    }

    public static TipoModulo findByCodigo(String codigo) {
        for (TipoModulo t : values()) {
            if (t.toString().equals(codigo)) {
                return t;
            }
        }

        return null;
    }
}
