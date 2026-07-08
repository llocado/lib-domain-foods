package com.supermercado.productos.application.service;

import com.supermercado.productos.application.port.in.ActualizarPrecioUseCase;
import com.supermercado.productos.application.port.in.AjustarStockUseCase;
import com.supermercado.productos.application.port.in.BuscarProductoUseCase;
import com.supermercado.productos.application.port.in.CrearProductoUseCase;
import com.supermercado.productos.application.port.in.EliminarProductoUseCase;
import com.supermercado.productos.application.port.out.ProductoRepositoryPort;
import com.supermercado.productos.domain.exception.ProductoNoEncontradoException;
import com.supermercado.productos.domain.exception.SkuDuplicadoException;
import com.supermercado.productos.domain.model.Precio;
import com.supermercado.productos.domain.model.Producto;
import com.supermercado.productos.domain.model.ProductoId;
import com.supermercado.productos.domain.model.Sku;

import java.util.List;
import java.util.Objects;

public class ProductoService implements
        CrearProductoUseCase,
        ActualizarPrecioUseCase,
        AjustarStockUseCase,
        BuscarProductoUseCase,
        EliminarProductoUseCase {

    private final ProductoRepositoryPort repositorio;

    public ProductoService(ProductoRepositoryPort repositorio) {
        this.repositorio = Objects.requireNonNull(repositorio, "El repositorio no puede ser nulo");
    }

    @Override
    public Producto crear(CrearProductoComando comando) {
        Sku sku = Sku.de(comando.sku());
        if (repositorio.existePorSku(sku)) {
            throw new SkuDuplicadoException(sku.getValor());
        }
        Producto producto = Producto.crear(
                sku,
                comando.nombre(),
                comando.descripcion(),
                comando.precio(),
                comando.categoriaId()
        );
        return repositorio.guardar(producto);
    }

    @Override
    public void actualizarPrecio(ProductoId productoId, Precio nuevoPrecio) {
        Producto producto = buscarOFallar(productoId);
        producto.cambiarPrecio(nuevoPrecio);
        repositorio.guardar(producto);
    }

    @Override
    public void aumentarStock(ProductoId productoId, int unidades) {
        Producto producto = buscarOFallar(productoId);
        producto.aumentarStock(unidades);
        repositorio.guardar(producto);
    }

    @Override
    public void reducirStock(ProductoId productoId, int unidades) {
        Producto producto = buscarOFallar(productoId);
        producto.reducirStock(unidades);
        repositorio.guardar(producto);
    }

    @Override
    public Producto buscarPorId(ProductoId productoId) {
        return buscarOFallar(productoId);
    }

    @Override
    public List<Producto> listarTodos() {
        return repositorio.listarTodos();
    }

    @Override
    public void eliminar(ProductoId productoId) {
        buscarOFallar(productoId);
        repositorio.eliminar(productoId);
    }

    private Producto buscarOFallar(ProductoId productoId) {
        return repositorio.buscarPorId(productoId)
                .orElseThrow(() -> new ProductoNoEncontradoException(
                        "Producto no encontrado con id: " + productoId));
    }
}
