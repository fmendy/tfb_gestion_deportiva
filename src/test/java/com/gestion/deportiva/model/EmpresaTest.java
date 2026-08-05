package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EmpresaTest {

    @Test
    void gettersYSetters() {
        Empresa model = new Empresa();
        
        model.setEmail("test@empresa.com");
        model.setLogo("logo.png");
        model.setUrl("https://www.empresa.com");
        model.setDescripcion("Descripción de prueba");
        model.setCif("A12345678");

        assertThat(model.getEmail()).isEqualTo("test@empresa.com");
        assertThat(model.getLogo()).isEqualTo("logo.png");
        assertThat(model.getUrl()).isEqualTo("https://www.empresa.com");
        assertThat(model.getDescripcion()).isEqualTo("Descripción de prueba");
        assertThat(model.getCif()).isEqualTo("A12345678");
    }

    @Test
    void constructorConId() {
        Empresa empresa = new Empresa(5L);

        assertThat(empresa.getId()).isEqualTo(5L);
    }
}