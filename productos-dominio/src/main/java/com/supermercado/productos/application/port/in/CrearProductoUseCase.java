package com.supermercado.productos.application.port.in;

import com.supermercado.productos.domain.model.CategoriaId;
import com.supermercado.productos.domain.model.Precio;
import com.supermercado.productos.domain.model.Producto;

public interface CrearProductoUseCase {

    Producto crear(CrearProductoComando comando);

    record CrearProductoComando(
            String sku,
            String nombre,
            String descripcion,
            Precio precio,
            CategoriaId categoriaId
    ) {}
}
