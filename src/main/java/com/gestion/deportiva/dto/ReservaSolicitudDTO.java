package com.gestion.deportiva.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.gestion.deportiva.validation.ReservaSolicitudDisponibilidadValid;
import com.gestion.deportiva.validation.ReservaSolicitudFechaValid;
import com.gestion.deportiva.validation.ReservaSolicitudInstalacionAbiertaValid;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ReservaSolicitudFechaValid
@ReservaSolicitudDisponibilidadValid
@ReservaSolicitudInstalacionAbiertaValid
public class ReservaSolicitudDTO extends BaseDTO {

	private static final long serialVersionUID = 572748819122752282L;

	@NotNull
	private Long instalacionId;

	private String instalacionNombre;

	private String instalacionSedeNombre;

	private String instalacionSedeEmpresaNombre;

	private String instalacionInstalacionTipoNombre;

	@NotNull
	private LocalDate fecha;

	@NotNull
	private LocalTime hora;

	private LocalTime horaFin;

	@NotNull
	private Long duracion;

}
