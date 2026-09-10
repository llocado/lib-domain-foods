package com.foodstore.domain.productos.application.port;

import com.foodstore.domain.productos.domain.model.CriterioPaginacion;
import com.foodstore.domain.productos.domain.model.Pagina;
import com.foodstore.domain.productos.domain.model.Producto;
import com.foodstore.domain.productos.domain.model.ProductoId;
import com.foodstore.domain.productos.domain.model.Sku;

import java.util.List;
import java.util.Optional;

public interface ProductoRepositoryPort {

    Producto guardar(Producto producto);

    Optional<Producto> buscarPorId(ProductoId id);

    Optional<Producto> buscarPorSku(Sku sku);

    List<Producto> listarTodos();

    Pagina<Producto> listarPaginado(CriterioPaginacion criterio);

    boolean existePorSku(Sku sku);

    void eliminar(ProductoId id);
}
