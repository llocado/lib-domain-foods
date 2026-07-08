package com.foodstore.domain.productos.domain.model;

import java.util.Objects;
import java.util.regex.Pattern;

public final class Sku {

    private static final Pattern PATRON_SKU = Pattern.compile("^[A-Z0-9][A-Z0-9\\-]{2,19}$");

    private final String valor;

    private Sku(String valor) {
        Objects.requireNonNull(valor, "El SKU no puede ser nulo");
        String normalizado = valor.trim().toUpperCase();
        if (!PATRON_SKU.matcher(normalizado).matches()) {
            throw new IllegalArgumentException(
                    "SKU inválido: '" + valor + "'. Debe tener entre 3 y 20 caracteres (letras, dígitos o guiones) y comenzar con letra o dígito.");
        }
        this.valor = normalizado;
    }

    public static Sku de(String valor) {
        return new Sku(valor);
    }

    public String getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sku sku = (Sku) o;
        return Objects.equals(valor, sku.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor;
    }
}
