package com.foodstore.domain.productos.domain.model;

import com.foodstore.domain.productos.domain.exception.StockInsuficienteException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductoTest {

    private Producto productoDeEjemplo() {
        return Producto.crear(
                Sku.de("PROD-001"),
                "Leche entera 1L",
                "Leche pasteurizada",
                Precio.deClp(new BigDecimal("1500")),
                CategoriaId.de(UUID.randomUUID())
        );
    }

    @Test
    void crear_generaIdYArrancaConStockCeroYActivo() {
        Producto p = productoDeEjemplo();

        assertNotNull(p.getId());
        assertEquals(0, p.getStock().getCantidad());
        assertTrue(p.isActivo());
    }

    @Test
    void crear_conNombreVacio_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                Producto.crear(Sku.de("PROD-002"), "  ", null,
                        Precio.deClp(BigDecimal.TEN), CategoriaId.de(UUID.randomUUID())));
    }

    @Test
    void crear_conNombreMayorA150Caracteres_lanzaExcepcion() {
        String nombreLargo = "A".repeat(151);

        assertThrows(IllegalArgumentException.class, () ->
                Producto.crear(Sku.de("PROD-003"), nombreLargo, null,
                        Precio.deClp(BigDecimal.TEN), CategoriaId.de(UUID.randomUUID())));
    }

    @Test
    void crear_conNombreExacto150Caracteres_esValido() {
        String nombreExacto = "A".repeat(150);

        assertDoesNotThrow(() ->
                Producto.crear(Sku.de("PROD-004"), nombreExacto, null,
                        Precio.deClp(BigDecimal.TEN), CategoriaId.de(UUID.randomUUID())));
    }

    @Test
    void aumentarStock_incrementaCorrectamente() {
        Producto p = productoDeEjemplo();
        p.aumentarStock(10);

        assertEquals(10, p.getStock().getCantidad());
    }

    @Test
    void reducirStock_conStockSuficiente_decrementa() {
        Producto p = productoDeEjemplo();
        p.aumentarStock(10);
        p.reducirStock(3);

        assertEquals(7, p.getStock().getCantidad());
    }

    @Test
    void reducirStock_conStockInsuficiente_lanzaExcepcion() {
        Producto p = productoDeEjemplo();
        p.aumentarStock(5);

        assertThrows(StockInsuficienteException.class, () -> p.reducirStock(6));
    }

    @Test
    void cambiarPrecio_actualizaElPrecio() {
        Producto p = productoDeEjemplo();
        Precio nuevoPrecio = Precio.deClp(new BigDecimal("2000"));
        p.cambiarPrecio(nuevoPrecio);

        assertEquals(nuevoPrecio, p.getPrecio());
    }

    @Test
    void renombrar_conNombreValido_actualizaNombre() {
        Producto p = productoDeEjemplo();
        p.renombrar("Leche descremada 1L");

        assertEquals("Leche descremada 1L", p.getNombre());
    }

    @Test
    void actualizarDescripcion_actualizaDescripcion() {
        Producto p = productoDeEjemplo();
        p.actualizarDescripcion("Nueva descripción");

        assertEquals("Nueva descripción", p.getDescripcion());
    }

    @Test
    void reclasificar_actualizaLaCategoria() {
        Producto p = productoDeEjemplo();
        CategoriaId nuevaCategoria = CategoriaId.de(UUID.randomUUID());
        p.reclasificar(nuevaCategoria);

        assertEquals(nuevaCategoria, p.getCategoriaId());
    }

    @Test
    void desactivar_yLuegoActivar_cambiaEstado() {
        Producto p = productoDeEjemplo();
        p.desactivar();
        assertFalse(p.isActivo());

        p.activar();
        assertTrue(p.isActivo());
    }

    @Test
    void tieneDisponibilidad_activoYStockSuficiente_retornaTrue() {
        Producto p = productoDeEjemplo();
        p.aumentarStock(5);

        assertTrue(p.tieneDisponibilidad(5));
        assertTrue(p.tieneDisponibilidad(3));
    }

    @Test
    void tieneDisponibilidad_desactivado_retornaFalse() {
        Producto p = productoDeEjemplo();
        p.aumentarStock(10);
        p.desactivar();

        assertFalse(p.tieneDisponibilidad(1));
    }

    @Test
    void tieneDisponibilidad_stockInsuficiente_retornaFalse() {
        Producto p = productoDeEjemplo();
        p.aumentarStock(2);

        assertFalse(p.tieneDisponibilidad(3));
    }

    @Test
    void reconstruir_restauraEstadoCompleto() {
        ProductoId id = ProductoId.de(UUID.randomUUID());
        Sku sku = Sku.de("PROD-REC");
        Precio precio = Precio.deClp(new BigDecimal("999"));
        Stock stock = Stock.de(7);
        CategoriaId categoriaId = CategoriaId.de(UUID.randomUUID());

        Producto p = Producto.reconstruir(id, sku, "Nombre", "Desc", precio, stock, categoriaId, false);

        assertEquals(id, p.getId());
        assertEquals(sku, p.getSku());
        assertEquals(7, p.getStock().getCantidad());
        assertFalse(p.isActivo());
        assertEquals("Desc", p.getDescripcion());
    }
}
