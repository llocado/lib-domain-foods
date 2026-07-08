package com.supermercado.productos.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

public final class Precio {

    private final BigDecimal monto;
    private final Currency moneda;

    private Precio(BigDecimal monto, Currency moneda) {
        Objects.requireNonNull(monto, "El monto no puede ser nulo");
        Objects.requireNonNull(moneda, "La moneda no puede ser nula");
        if (monto.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo: " + monto);
        }
        this.monto = monto.setScale(2, RoundingMode.HALF_UP);
        this.moneda = moneda;
    }

    public static Precio de(BigDecimal monto, Currency moneda) {
        return new Precio(monto, moneda);
    }

    public static Precio deClp(BigDecimal monto) {
        return new Precio(monto, Currency.getInstance("CLP"));
    }

    public Precio aplicarDescuento(BigDecimal porcentaje) {
        Objects.requireNonNull(porcentaje, "El porcentaje de descuento no puede ser nulo");
        if (porcentaje.compareTo(BigDecimal.ZERO) < 0 || porcentaje.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException(
                    "El porcentaje de descuento debe estar entre 0 y 100: " + porcentaje);
        }
        BigDecimal factor = BigDecimal.ONE.subtract(
                porcentaje.divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP));
        BigDecimal nuevoMonto = monto.multiply(factor).setScale(2, RoundingMode.HALF_UP);
        return new Precio(nuevoMonto, moneda);
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public Currency getMoneda() {
        return moneda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Precio precio = (Precio) o;
        return Objects.equals(monto, precio.monto) && Objects.equals(moneda, precio.moneda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(monto, moneda);
    }

    @Override
    public String toString() {
        return monto + " " + moneda.getCurrencyCode();
    }
}
