package com.foodstore.domain.productos.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class PrecioTest {

    @Test
    void precio_valido_seCreaCorrectamente() {
        Precio p = Precio.deClp(new BigDecimal("1000"));

        assertEquals(new BigDecimal("1000.00"), p.getMonto());
        assertEquals(Currency.getInstance("CLP"), p.getMoneda());
    }

    @Test
    void precio_cero_esValido() {
        assertEquals(new BigDecimal("0.00"), Precio.deClp(BigDecimal.ZERO).getMonto());
    }

    @Test
    void precio_negativo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> Precio.deClp(new BigDecimal("-0.01")));
    }

    @Test
    void precio_montoNulo_lanzaExcepcion() {
        assertThrows(NullPointerException.class, () -> Precio.deClp(null));
    }

    @Test
    void aplicarDescuento_50porciento_reduceMitad() {
        Precio conDescuento = Precio.deClp(new BigDecimal("1000"))
                .aplicarDescuento(new BigDecimal("50"));

        assertEquals(new BigDecimal("500.00"), conDescuento.getMonto());
    }

    @Test
    void aplicarDescuento_100porciento_resultaCero() {
        Precio conDescuento = Precio.deClp(new BigDecimal("1000"))
                .aplicarDescuento(new BigDecimal("100"));

        assertEquals(new BigDecimal("0.00"), conDescuento.getMonto());
    }

    @Test
    void aplicarDescuento_0porciento_noCambiaMonto() {
        Precio conDescuento = Precio.deClp(new BigDecimal("1000"))
                .aplicarDescuento(BigDecimal.ZERO);

        assertEquals(new BigDecimal("1000.00"), conDescuento.getMonto());
    }

    @Test
    void aplicarDescuento_negativo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> Precio.deClp(new BigDecimal("1000")).aplicarDescuento(new BigDecimal("-1")));
    }

    @Test
    void aplicarDescuento_mayor100_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> Precio.deClp(new BigDecimal("1000")).aplicarDescuento(new BigDecimal("100.01")));
    }

    @Test
    void aplicarDescuento_esInmutable_noModificaOriginal() {
        Precio original = Precio.deClp(new BigDecimal("1000"));
        original.aplicarDescuento(new BigDecimal("50"));

        assertEquals(new BigDecimal("1000.00"), original.getMonto());
    }

    @Test
    void precio_igualdadPorValor() {
        Precio p1 = Precio.deClp(new BigDecimal("500"));
        Precio p2 = Precio.deClp(new BigDecimal("500"));

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void precio_distintosMonto_noSonIguales() {
        assertNotEquals(Precio.deClp(new BigDecimal("500")), Precio.deClp(new BigDecimal("600")));
    }

    @Test
    void precio_distintaMoneda_noSonIguales() {
        Precio clp = Precio.de(new BigDecimal("500"), Currency.getInstance("CLP"));
        Precio usd = Precio.de(new BigDecimal("500"), Currency.getInstance("USD"));

        assertNotEquals(clp, usd);
    }
}
