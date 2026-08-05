package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

class InstalacionHorarioBloqueadoTest {

    @Test
    void gettersYSetters() {
        InstalacionHorarioBloqueado horarioBloqueado = new InstalacionHorarioBloqueado();
        Instalacion instalacion = new Instalacion(1L);
        LocalDate fecha = LocalDate.of(2026, 8, 5);
        LocalTime horaInicio = LocalTime.of(10, 0);
        LocalTime horaFin = LocalTime.of(12, 0);
        
        horarioBloqueado.setInstalacion(instalacion);
        horarioBloqueado.setFecha(fecha);
        horarioBloqueado.setHoraInicio(horaInicio);
        horarioBloqueado.setHoraFin(horaFin);

        assertThat(horarioBloqueado.getInstalacion()).isEqualTo(instalacion);
        assertThat(horarioBloqueado.getInstalacion().getId()).isEqualTo(1L);
        assertThat(horarioBloqueado.getFecha()).isEqualTo(fecha);
        assertThat(horarioBloqueado.getHoraInicio()).isEqualTo(horaInicio);
        assertThat(horarioBloqueado.getHoraFin()).isEqualTo(horaFin);
    }

    @Test
    void constructorConId() {
        InstalacionHorarioBloqueado horarioBloqueado = new InstalacionHorarioBloqueado(3L);
        assertThat(horarioBloqueado.getId()).isEqualTo(3L);
    }
}