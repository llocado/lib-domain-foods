package com.supermercado.productos.domain.exception;

public class StockInsuficienteException extends RuntimeException {

    private final int disponible;
    private final int solicitado;

    public StockInsuficienteException(int disponible, int solicitado) {
        super("Stock insuficiente: disponible=" + disponible + ", solicitado=" + solicitado);
        this.disponible = disponible;
        this.solicitado = solicitado;
    }

    public int getDisponible() {
        return disponible;
    }

    public int getSolicitado() {
        return solicitado;
    }
}
