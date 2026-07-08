package com.supermercado.productos.domain.model;

import java.util.Objects;
import java.util.UUID;

public final class CategoriaId {

    private final UUID valor;

    private CategoriaId(UUID valor) {
        this.valor = Objects.requireNonNull(valor, "El id de categoría no puede ser nulo");
    }

    public static CategoriaId de(UUID valor) {
        return new CategoriaId(valor);
    }

    public static CategoriaId de(String valor) {
        Objects.requireNonNull(valor, "El id de categoría no puede ser nulo");
        return new CategoriaId(UUID.fromString(valor));
    }

    public UUID getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoriaId that = (CategoriaId) o;
        return Objects.equals(valor, that.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
