package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

class InstalacionHorarioTest {

    @Test
    void gettersYSetters() {
        InstalacionHorario horario = new InstalacionHorario();
        Instalacion instalacion = new Instalacion(1L);
        LocalTime horaInicio = LocalTime.of(8, 0);
        LocalTime horaFin = LocalTime.of(22, 0);
        
        horario.setInstalacion(instalacion);
        horario.setDiaSemana(1L);
        horario.setHoraInicio(horaInicio);
        horario.setHoraFin(horaFin);

        assertThat(horario.getInstalacion()).isEqualTo(instalacion);
        assertThat(horario.getInstalacion().getId()).isEqualTo(1L);
        assertThat(horario.getDiaSemana()).isEqualTo(1L);
        assertThat(horario.getHoraInicio()).isEqualTo(horaInicio);
        assertThat(horario.getHoraFin()).isEqualTo(horaFin);
    }

    @Test
    void constructorConId() {
        InstalacionHorario horario = new InstalacionHorario(7L);
        assertThat(horario.getId()).isEqualTo(7L);
    }
}