package com.supermercado.productos.application.service;

import com.supermercado.productos.application.port.in.CrearProductoUseCase.CrearProductoComando;
import com.supermercado.productos.application.port.out.ProductoRepositoryPort;
import com.supermercado.productos.domain.exception.ProductoNoEncontradoException;
import com.supermercado.productos.domain.exception.SkuDuplicadoException;
import com.supermercado.productos.domain.model.CategoriaId;
import com.supermercado.productos.domain.model.Precio;
import com.supermercado.productos.domain.model.Producto;
import com.supermercado.productos.domain.model.ProductoId;
import com.supermercado.productos.domain.model.Sku;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductoServiceTest {

    // Doble de prueba en memoria — sin frameworks de mocking
    static class RepositorioEnMemoria implements ProductoRepositoryPort {

        private final Map<ProductoId, Producto> almacen = new HashMap<>();

        @Override
        public Producto guardar(Producto producto) {
            almacen.put(producto.getId(), producto);
            return producto;
        }

        @Override
        public Optional<Producto> buscarPorId(ProductoId id) {
            return Optional.ofNullable(almacen.get(id));
        }

        @Override
        public Optional<Producto> buscarPorSku(Sku sku) {
            return almacen.values().stream()
                    .filter(p -> p.getSku().equals(sku))
                    .findFirst();
        }

        @Override
        public List<Producto> listarTodos() {
            return new ArrayList<>(almacen.values());
        }

        @Override
        public boolean existePorSku(Sku sku) {
            return almacen.values().stream().anyMatch(p -> p.getSku().equals(sku));
        }

        @Override
        public void eliminar(ProductoId id) {
            almacen.remove(id);
        }
    }

    private ProductoService servicio;
    private CategoriaId categoriaId;

    @BeforeEach
    void setUp() {
        servicio = new ProductoService(new RepositorioEnMemoria());
        categoriaId = CategoriaId.de(UUID.randomUUID());
    }

    private CrearProductoComando comandoDeEjemplo(String sku) {
        return new CrearProductoComando(
                sku,
                "Leche entera 1L",
                "Leche pasteurizada entera",
                Precio.deClp(new BigDecimal("1500")),
                categoriaId
        );
    }

    @Test
    void crear_productoValido_loGuardaYRetorna() {
        Producto creado = servicio.crear(comandoDeEjemplo("LACT-001"));

        assertNotNull(creado.getId());
        assertEquals("LACT-001", creado.getSku().getValor());
        assertTrue(creado.isActivo());
        assertEquals(0, creado.getStock().getCantidad());
    }

    @Test
    void crear_conSkuDuplicado_lanzaSkuDuplicadoException() {
        servicio.crear(comandoDeEjemplo("LACT-001"));

        assertThrows(SkuDuplicadoException.class,
                () -> servicio.crear(comandoDeEjemplo("LACT-001")));
    }

    @Test
    void buscarPorId_productoExistente_loRetorna() {
        Producto creado = servicio.crear(comandoDeEjemplo("LACT-002"));

        Producto encontrado = servicio.buscarPorId(creado.getId());

        assertEquals(creado.getId(), encontrado.getId());
        assertEquals("LACT-002", encontrado.getSku().getValor());
    }

    @Test
    void buscarPorId_productoInexistente_lanzaProductoNoEncontradoException() {
        assertThrows(ProductoNoEncontradoException.class,
                () -> servicio.buscarPorId(ProductoId.de(UUID.randomUUID())));
    }

    @Test
    void listarTodos_sinProductos_retornaListaVacia() {
        assertTrue(servicio.listarTodos().isEmpty());
    }

    @Test
    void listarTodos_conVariosProductos_retornaTodos() {
        servicio.crear(comandoDeEjemplo("LACT-001"));
        servicio.crear(comandoDeEjemplo("LACT-002"));
        servicio.crear(comandoDeEjemplo("LACT-003"));

        assertEquals(3, servicio.listarTodos().size());
    }

    @Test
    void actualizarPrecio_productoExistente_cambiaElPrecio() {
        Producto creado = servicio.crear(comandoDeEjemplo("LACT-004"));
        Precio nuevoPrecio = Precio.deClp(new BigDecimal("2000"));

        servicio.actualizarPrecio(creado.getId(), nuevoPrecio);

        assertEquals(nuevoPrecio, servicio.buscarPorId(creado.getId()).getPrecio());
    }

    @Test
    void actualizarPrecio_productoInexistente_lanzaExcepcion() {
        assertThrows(ProductoNoEncontradoException.class,
                () -> servicio.actualizarPrecio(ProductoId.de(UUID.randomUUID()),
                        Precio.deClp(new BigDecimal("500"))));
    }

    @Test
    void aumentarStock_productoExistente_incrementaStock() {
        Producto creado = servicio.crear(comandoDeEjemplo("LACT-005"));

        servicio.aumentarStock(creado.getId(), 10);

        assertEquals(10, servicio.buscarPorId(creado.getId()).getStock().getCantidad());
    }

    @Test
    void reducirStock_conStockSuficiente_decrementaStock() {
        Producto creado = servicio.crear(comandoDeEjemplo("LACT-006"));
        servicio.aumentarStock(creado.getId(), 10);

        servicio.reducirStock(creado.getId(), 4);

        assertEquals(6, servicio.buscarPorId(creado.getId()).getStock().getCantidad());
    }

    @Test
    void reducirStock_productoInexistente_lanzaExcepcion() {
        assertThrows(ProductoNoEncontradoException.class,
                () -> servicio.reducirStock(ProductoId.de(UUID.randomUUID()), 1));
    }

    @Test
    void eliminar_productoExistente_loElimina() {
        Producto creado = servicio.crear(comandoDeEjemplo("LACT-007"));

        servicio.eliminar(creado.getId());

        assertThrows(ProductoNoEncontradoException.class,
                () -> servicio.buscarPorId(creado.getId()));
    }

    @Test
    void eliminar_productoInexistente_lanzaExcepcion() {
        assertThrows(ProductoNoEncontradoException.class,
                () -> servicio.eliminar(ProductoId.de(UUID.randomUUID())));
    }
}
