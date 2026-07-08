package com.gestion.deportiva.dto.historico;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HistoricoReservaDTO extends HistoricoDTO {

	private static final long serialVersionUID = -393596421072202948L;

	private String instalacionSedeEmpresaNombre;

	private String instalacionSedeNombre;

	private String instalacionInstalacionTipoNombre;

	private String instalacionNombre;

	private LocalTime horaInicio;

	private LocalTime horaFin;

	private LocalDate fecha;

	private String reservaEstadoNombre;

	private String usuarioNombre;

}
