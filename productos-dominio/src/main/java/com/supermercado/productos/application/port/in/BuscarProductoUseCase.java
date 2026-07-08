package com.supermercado.productos.application.port.in;

import com.supermercado.productos.domain.model.Producto;
import com.supermercado.productos.domain.model.ProductoId;

import java.util.List;

public interface BuscarProductoUseCase {

    Producto buscarPorId(ProductoId productoId);

    List<Producto> listarTodos();
}
