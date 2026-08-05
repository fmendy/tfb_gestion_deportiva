package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SedTest {

    @Test
    void gettersYSetters() {
        Sede sede = new Sede();
        Municipio municipio = new Municipio(1L);
        Empresa empresa = new Empresa(2L);
        String direccion = "Calle Principal 123";
        String latitud = "40.4168";
        String longitud = "-3.7038";
        String email = "contacto@sede.com";
        String logo = "logo.png";
        String url = "https://www.sede.com";
        String descripcion = "Sede central de gestión deportiva";
        
        sede.setMunicipio(municipio);
        sede.setEmpresa(empresa);
        sede.setDireccion(direccion);
        sede.setLatitud(latitud);
        sede.setLongitud(longitud);
        sede.setEmail(email);
        sede.setLogo(logo);
        sede.setUrl(url);
        sede.setDescripcion(descripcion);

        assertThat(sede.getMunicipio()).isEqualTo(municipio);
        assertThat(sede.getMunicipio().getId()).isEqualTo(1L);
        assertThat(sede.getEmpresa()).isEqualTo(empresa);
        assertThat(sede.getEmpresa().getId()).isEqualTo(2L);
        assertThat(sede.getDireccion()).isEqualTo(direccion);
        assertThat(sede.getLatitud()).isEqualTo(latitud);
        assertThat(sede.getLongitud()).isEqualTo(longitud);
        assertThat(sede.getEmail()).isEqualTo(email);
        assertThat(sede.getLogo()).isEqualTo(logo);
        assertThat(sede.getUrl()).isEqualTo(url);
        assertThat(sede.getDescripcion()).isEqualTo(descripcion);
    }

    @Test
    void constructorConId() {
        Sede sede = new Sede(7L);

        assertThat(sede.getId()).isEqualTo(7L);
    }
}