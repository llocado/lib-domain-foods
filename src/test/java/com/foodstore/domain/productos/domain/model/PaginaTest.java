package com.foodstore.domain.productos.domain.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaginaTest {

    @Test
    void pagina_calculaTotalPaginas_redondeandoHaciaArriba() {
        Pagina<String> pagina = Pagina.de(List.of("a", "b"), 0, 20, 45);

        assertEquals(3, pagina.getTotalPaginas());
    }

    @Test
    void pagina_conTotalElementosExactoAlTamano_calculaUnaSolaPagina() {
        Pagina<String> pagina = Pagina.de(List.of("a"), 0, 20, 20);

        assertEquals(1, pagina.getTotalPaginas());
    }

    @Test
    void pagina_vacia_tieneCeroPaginas() {
        Pagina<String> pagina = Pagina.de(List.of(), 0, 20, 0);

        assertEquals(0, pagina.getTotalPaginas());
        assertTrue(pagina.getContenido().isEmpty());
    }

    @Test
    void pagina_exponeContenidoInmutable() {
        Pagina<String> pagina = Pagina.de(List.of("a", "b"), 0, 20, 2);

        assertThrows(UnsupportedOperationException.class, () -> pagina.getContenido().add("c"));
    }

    @Test
    void pagina_contenidoNulo_lanzaNullPointerException() {
        assertThrows(NullPointerException.class, () -> Pagina.de(null, 0, 20, 0));
    }
}
