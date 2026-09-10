package com.foodstore.domain.productos.domain.model;

import java.util.Objects;

public final class CriterioPaginacion {

    private final int pagina;
    private final int tamano;

    private CriterioPaginacion(int pagina, int tamano) {
        if (pagina < 0) {
            throw new IllegalArgumentException("La página no puede ser negativa");
        }
        if (tamano < 1) {
            throw new IllegalArgumentException("El tamaño de página debe ser mayor a 0");
        }
        this.pagina = pagina;
        this.tamano = tamano;
    }

    public static CriterioPaginacion de(int pagina, int tamano) {
        return new CriterioPaginacion(pagina, tamano);
    }

    public int getPagina() {
        return pagina;
    }

    public int getTamano() {
        return tamano;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CriterioPaginacion that = (CriterioPaginacion) o;
        return pagina == that.pagina && tamano == that.tamano;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pagina, tamano);
    }

    @Override
    public String toString() {
        return "CriterioPaginacion{pagina=" + pagina + ", tamano=" + tamano + "}";
    }
}
