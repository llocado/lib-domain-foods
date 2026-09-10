package com.foodstore.domain.productos.domain.model;

import java.util.List;
import java.util.Objects;

/**
 * Resultado de una consulta paginada. No depende de ningun framework:
 * los adaptadores de persistencia son responsables de traducir su propio
 * mecanismo de paginacion (ej. Pageable de Spring Data) hacia y desde este tipo.
 */
public final class Pagina<T> {

    private final List<T> contenido;
    private final int numero;
    private final int tamano;
    private final long totalElementos;

    private Pagina(List<T> contenido, int numero, int tamano, long totalElementos) {
        this.contenido = List.copyOf(Objects.requireNonNull(contenido, "El contenido no puede ser nulo"));
        this.numero = numero;
        this.tamano = tamano;
        this.totalElementos = totalElementos;
    }

    public static <T> Pagina<T> de(List<T> contenido, int numero, int tamano, long totalElementos) {
        return new Pagina<>(contenido, numero, tamano, totalElementos);
    }

    public List<T> getContenido() {
        return contenido;
    }

    public int getNumero() {
        return numero;
    }

    public int getTamano() {
        return tamano;
    }

    public long getTotalElementos() {
        return totalElementos;
    }

    public int getTotalPaginas() {
        if (tamano == 0) {
            return 0;
        }
        return (int) Math.ceil((double) totalElementos / tamano);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pagina<?> pagina = (Pagina<?>) o;
        return numero == pagina.numero
                && tamano == pagina.tamano
                && totalElementos == pagina.totalElementos
                && contenido.equals(pagina.contenido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contenido, numero, tamano, totalElementos);
    }

    @Override
    public String toString() {
        return "Pagina{numero=" + numero + ", tamano=" + tamano
                + ", totalElementos=" + totalElementos + ", totalPaginas=" + getTotalPaginas() + "}";
    }
}
