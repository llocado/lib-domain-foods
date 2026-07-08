package com.supermercado.productos.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class SkuTest {

    @Test
    void sku_valido_seCreaCorrectamente() {
        Sku sku = Sku.de("PROD-001");

        assertEquals("PROD-001", sku.getValor());
    }

    @Test
    void sku_seNormalizaAMayusculas() {
        Sku sku = Sku.de("abc-123");

        assertEquals("ABC-123", sku.getValor());
    }

    @Test
    void sku_conEspaciosAlrededor_seTrimea() {
        Sku sku = Sku.de("  ABC-123  ");

        assertEquals("ABC-123", sku.getValor());
    }

    @Test
    void sku_longitudMinima3_esValido() {
        assertDoesNotThrow(() -> Sku.de("A00"));
    }

    @Test
    void sku_longitudMaxima20_esValido() {
        assertDoesNotThrow(() -> Sku.de("A234567890123456789"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"AB", "", "  ", "SKU CON ESPACIOS", "SKU_INVALIDO!", "-EMPIEZA-GUION"})
    void sku_invalido_lanzaIllegalArgumentException(String skuInvalido) {
        assertThrows(IllegalArgumentException.class, () -> Sku.de(skuInvalido));
    }

    @Test
    void sku_nulo_lanzaNullPointerException() {
        assertThrows(NullPointerException.class, () -> Sku.de(null));
    }

    @Test
    void sku_igualdadPorValor_ignorandoMayusculas() {
        Sku sku1 = Sku.de("PROD-001");
        Sku sku2 = Sku.de("prod-001");

        assertEquals(sku1, sku2);
        assertEquals(sku1.hashCode(), sku2.hashCode());
    }

    @Test
    void sku_distintosValores_noSonIguales() {
        Sku sku1 = Sku.de("PROD-001");
        Sku sku2 = Sku.de("PROD-002");

        assertNotEquals(sku1, sku2);
    }

    @Test
    void sku_toString_retornaElValor() {
        Sku sku = Sku.de("PROD-001");

        assertEquals("PROD-001", sku.toString());
    }
}
