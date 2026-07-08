package com.supermercado.productos.application.port.in;

import com.supermercado.productos.domain.model.Precio;
import com.supermercado.productos.domain.model.ProductoId;

public interface ActualizarPrecioUseCase {

    void actualizarPrecio(ProductoId productoId, Precio nuevoPrecio);
}
