package com.gestion.deportiva.dto.filter;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SancionFilter extends BaseEntityFilter {

	private static final long serialVersionUID = 9221019637732455446L;

	private Long reservaId;

	private Long usuarioId;

	private String usuarioNombre;

	private Long sancionTipoId;
	
	private LocalDate fechaInicioDesde;
	
	private LocalDate fechaInicioHasta;
	
	private LocalDate fechaFinDesde;
	
	private LocalDate fechaFinHasta;

}
