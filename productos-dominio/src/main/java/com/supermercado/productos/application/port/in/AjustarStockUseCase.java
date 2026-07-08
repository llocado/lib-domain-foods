package com.supermercado.productos.application.port.in;

import com.supermercado.productos.domain.model.ProductoId;

public interface AjustarStockUseCase {

    void aumentarStock(ProductoId productoId, int unidades);

    void reducirStock(ProductoId productoId, int unidades);
}
