package com.supermercado.productos.application.port.in;

import com.supermercado.productos.domain.model.ProductoId;

public interface EliminarProductoUseCase {

    void eliminar(ProductoId productoId);
}
