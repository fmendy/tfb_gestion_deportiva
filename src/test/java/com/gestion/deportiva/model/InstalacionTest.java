package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class InstalacionTest {

    @Test
    void gettersYSetters() {
        Instalacion instalacion = new Instalacion();
        Sede sede = new Sede(1L);
        InstalacionTipo instalacionTipo = new InstalacionTipo(2L);
        
        instalacion.setDescripcion("Instalación deportiva principal");
        instalacion.setSede(sede);
        instalacion.setInstalacionTipo(instalacionTipo);

        assertThat(instalacion.getDescripcion()).isEqualTo("Instalación deportiva principal");
        assertThat(instalacion.getSede()).isEqualTo(sede);
        assertThat(instalacion.getSede().getId()).isEqualTo(1L);
        assertThat(instalacion.getInstalacionTipo()).isEqualTo(instalacionTipo);
        assertThat(instalacion.getInstalacionTipo().getId()).isEqualTo(2L);
    }

    @Test
    void constructorConId() {
        Instalacion instalacion = new Instalacion(10L);

        assertThat(instalacion.getId()).isEqualTo(10L);
    }
}