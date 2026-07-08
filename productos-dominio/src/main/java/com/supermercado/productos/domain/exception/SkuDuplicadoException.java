package com.supermercado.productos.domain.exception;

public class SkuDuplicadoException extends RuntimeException {

    public SkuDuplicadoException(String sku) {
        super("Ya existe un producto con el SKU: " + sku);
    }
}
