package com.supermercado.productos.domain.model;

import com.supermercado.productos.domain.exception.StockInsuficienteException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    @Test
    void stock_cero_esValido() {
        Stock s = Stock.cero();

        assertEquals(0, s.getCantidad());
    }

    @Test
    void stock_positivo_esValido() {
        Stock s = Stock.de(100);

        assertEquals(100, s.getCantidad());
    }

    @Test
    void stock_negativo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> Stock.de(-1));
    }

    @Test
    void incrementar_aumentaCantidad() {
        Stock resultado = Stock.de(5).incrementar(3);

        assertEquals(8, resultado.getCantidad());
    }

    @Test
    void incrementar_cero_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> Stock.de(5).incrementar(0));
    }

    @Test
    void incrementar_negativo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> Stock.de(5).incrementar(-1));
    }

    @Test
    void decrementar_conStockSuficiente_reduce() {
        Stock resultado = Stock.de(10).decrementar(4);

        assertEquals(6, resultado.getCantidad());
    }

    @Test
    void decrementar_todoElStock_resultaCero() {
        Stock resultado = Stock.de(5).decrementar(5);

        assertEquals(0, resultado.getCantidad());
    }

    @Test
    void decrementar_conStockInsuficiente_lanzaExcepcionConDetalle() {
        StockInsuficienteException ex = assertThrows(StockInsuficienteException.class,
                () -> Stock.de(3).decrementar(5));

        assertEquals(3, ex.getDisponible());
        assertEquals(5, ex.getSolicitado());
    }

    @Test
    void decrementar_cero_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> Stock.de(5).decrementar(0));
    }

    @Test
    void decrementar_negativo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> Stock.de(5).decrementar(-2));
    }

    @Test
    void esSuficientePara_exactamenteLaCantidad_retornaTrue() {
        assertTrue(Stock.de(5).esSuficientePara(5));
    }

    @Test
    void esSuficientePara_menosDeLaCantidad_retornaTrue() {
        assertTrue(Stock.de(5).esSuficientePara(3));
    }

    @Test
    void esSuficientePara_masDeLaCantidad_retornaFalse() {
        assertFalse(Stock.de(5).esSuficientePara(6));
    }

    @Test
    void stock_esInmutable_incrementarDevuelveNuevoObjeto() {
        Stock original = Stock.de(5);
        Stock incrementado = original.incrementar(3);

        assertEquals(5, original.getCantidad());
        assertEquals(8, incrementado.getCantidad());
    }

    @Test
    void stock_esInmutable_decrementarDevuelveNuevoObjeto() {
        Stock original = Stock.de(10);
        Stock decrementado = original.decrementar(4);

        assertEquals(10, original.getCantidad());
        assertEquals(6, decrementado.getCantidad());
    }

    @Test
    void stock_igualdadPorValor() {
        assertEquals(Stock.de(5), Stock.de(5));
        assertEquals(Stock.de(5).hashCode(), Stock.de(5).hashCode());
    }
}
