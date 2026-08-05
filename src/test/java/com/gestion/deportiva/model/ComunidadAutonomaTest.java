package com.gestion.deportiva.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ComunidadAutonomaTest {

    @Test
    void testConstructorVacio() {
        ComunidadAutonoma comunidad = new ComunidadAutonoma();

        assertNotNull(comunidad);
        assertNotNull(comunidad.getListProvincia());
        assertTrue(comunidad.getListProvincia().isEmpty());
    }

    @Test
    void testConstructorConId() {
        Long id = 1L;

        ComunidadAutonoma comunidad = new ComunidadAutonoma(id);

        assertEquals(id, comunidad.getId());
    }

    @Test
    void testGettersAndSetters() {
        ComunidadAutonoma comunidad = new ComunidadAutonoma();

        comunidad.setCodigoIne(28L);

        assertEquals(28L, comunidad.getCodigoIne());
    }

    @Test
    void testEqualsAndHashCode() {
        ComunidadAutonoma comunidad1 = new ComunidadAutonoma();
        comunidad1.setId(1L);

        ComunidadAutonoma comunidad2 = new ComunidadAutonoma();
        comunidad2.setId(1L);

        assertEquals(comunidad1, comunidad2);
        assertEquals(comunidad1.hashCode(), comunidad2.hashCode());
    }

    @Test
    void testToString() {
        ComunidadAutonoma comunidad = new ComunidadAutonoma();
        comunidad.setCodigoIne(28L);

        String result = comunidad.toString();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}