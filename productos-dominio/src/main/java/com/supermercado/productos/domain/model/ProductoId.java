package com.supermercado.productos.domain.model;

import java.util.Objects;
import java.util.UUID;

public final class ProductoId {

    private final UUID valor;

    private ProductoId(UUID valor) {
        this.valor = Objects.requireNonNull(valor, "El id de producto no puede ser nulo");
    }

    public static ProductoId nuevo() {
        return new ProductoId(UUID.randomUUID());
    }

    public static ProductoId de(UUID valor) {
        return new ProductoId(valor);
    }

    public static ProductoId de(String valor) {
        Objects.requireNonNull(valor, "El id de producto no puede ser nulo");
        return new ProductoId(UUID.fromString(valor));
    }

    public UUID getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductoId that = (ProductoId) o;
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
