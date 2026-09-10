package com.foodstore.domain.productos.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CriterioPaginacionTest {

    @Test
    void criterio_valido_seCreaCorrectamente() {
        CriterioPaginacion criterio = CriterioPaginacion.de(0, 20);

        assertEquals(0, criterio.getPagina());
        assertEquals(20, criterio.getTamano());
    }

    @Test
    void criterio_conPaginaNegativa_lanzaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> CriterioPaginacion.de(-1, 20));
    }

    @Test
    void criterio_conTamanoCero_lanzaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> CriterioPaginacion.de(0, 0));
    }

    @Test
    void criterio_conTamanoNegativo_lanzaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> CriterioPaginacion.de(0, -5));
    }

    @Test
    void criterio_igualdadPorValor() {
        assertEquals(CriterioPaginacion.de(1, 10), CriterioPaginacion.de(1, 10));
        assertEquals(CriterioPaginacion.de(1, 10).hashCode(), CriterioPaginacion.de(1, 10).hashCode());
    }

    @Test
    void criterio_distintosValores_noSonIguales() {
        assertNotEquals(CriterioPaginacion.de(0, 10), CriterioPaginacion.de(1, 10));
    }
}
