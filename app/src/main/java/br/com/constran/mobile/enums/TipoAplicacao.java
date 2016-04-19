package br.com.constran.mobile.enums;

/**
 * Criado em 11/09/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public enum TipoAplicacao {
    EQUIPAMENTO("E"),
    AMBOS("A"),
    MAO_DE_OBRA("M");

    private final String codigo;

    TipoAplicacao(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public static TipoAplicacao findByCodigo(String codigo) {
        for (TipoAplicacao tipo : values()) {
            if (tipo.getCodigo().equalsIgnoreCase(codigo)) {
                return tipo;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return name();
    }
}
