package com.supermercado.productos.domain.model;

import com.supermercado.productos.domain.exception.StockInsuficienteException;

import java.util.Objects;

public final class Stock {

    private final int cantidad;

    private Stock(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo: " + cantidad);
        }
        this.cantidad = cantidad;
    }

    public static Stock de(int cantidad) {
        return new Stock(cantidad);
    }

    public static Stock cero() {
        return new Stock(0);
    }

    public Stock incrementar(int unidades) {
        if (unidades <= 0) {
            throw new IllegalArgumentException("Las unidades a incrementar deben ser positivas: " + unidades);
        }
        return new Stock(this.cantidad + unidades);
    }

    public Stock decrementar(int unidades) {
        if (unidades <= 0) {
            throw new IllegalArgumentException("Las unidades a decrementar deben ser positivas: " + unidades);
        }
        if (unidades > this.cantidad) {
            throw new StockInsuficienteException(this.cantidad, unidades);
        }
        return new Stock(this.cantidad - unidades);
    }

    public boolean esSuficientePara(int unidadesRequeridas) {
        return this.cantidad >= unidadesRequeridas;
    }

    public int getCantidad() {
        return cantidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return cantidad == stock.cantidad;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cantidad);
    }

    @Override
    public String toString() {
        return String.valueOf(cantidad);
    }
}
