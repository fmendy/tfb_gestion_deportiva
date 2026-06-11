package com.gestion.deportiva.dto.filter;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InstalacionHorarioEspecialFilter extends BaseEntityFilter {
	
	private static final long serialVersionUID = -6080911578482040719L;
	
	private Long empresaId;
	
	private Long sedeId;
	
	private Long instalacionId;
	
	private LocalDate fechaDesde;
	
	private LocalDate fechaHasta;
	
	private Boolean  cerrado;

}
