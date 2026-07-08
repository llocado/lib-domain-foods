package com.supermercado.productos.application.port.out;

import com.supermercado.productos.domain.model.Producto;
import com.supermercado.productos.domain.model.ProductoId;
import com.supermercado.productos.domain.model.Sku;

import java.util.List;
import java.util.Optional;

public interface ProductoRepositoryPort {

    Producto guardar(Producto producto);

    Optional<Producto> buscarPorId(ProductoId id);

    Optional<Producto> buscarPorSku(Sku sku);

    List<Producto> listarTodos();

    boolean existePorSku(Sku sku);

    void eliminar(ProductoId id);
}
