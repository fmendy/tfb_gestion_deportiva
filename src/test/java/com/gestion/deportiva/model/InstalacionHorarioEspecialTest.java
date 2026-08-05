package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

class InstalacionHorarioEspecialTest {

    @Test
    void gettersYSetters() {
        InstalacionHorarioEspecial horarioEspecial = new InstalacionHorarioEspecial();
        Instalacion instalacion = new Instalacion(1L);
        LocalDate fecha = LocalDate.of(2026, 12, 25);
        LocalTime horaInicio = LocalTime.of(9, 0);
        LocalTime horaFin = LocalTime.of(14, 0);
        Boolean cerrado = false;
        
        horarioEspecial.setInstalacion(instalacion);
        horarioEspecial.setFecha(fecha);
        horarioEspecial.setHoraInicio(horaInicio);
        horarioEspecial.setHoraFin(horaFin);
        horarioEspecial.setCerrado(cerrado);

        assertThat(horarioEspecial.getInstalacion()).isEqualTo(instalacion);
        assertThat(horarioEspecial.getInstalacion().getId()).isEqualTo(1L);
        assertThat(horarioEspecial.getFecha()).isEqualTo(fecha);
        assertThat(horarioEspecial.getHoraInicio()).isEqualTo(horaInicio);
        assertThat(horarioEspecial.getHoraFin()).isEqualTo(horaFin);
        assertThat(horarioEspecial.getCerrado()).isFalse();
    }

    @Test
    void constructorConId() {
        InstalacionHorarioEspecial horarioEspecial = new InstalacionHorarioEspecial(9L);

        assertThat(horarioEspecial.getId()).isEqualTo(9L);
    }
}