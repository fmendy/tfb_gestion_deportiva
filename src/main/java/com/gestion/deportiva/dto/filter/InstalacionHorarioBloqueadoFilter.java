package com.gestion.deportiva.dto.filter;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InstalacionHorarioBloqueadoFilter extends BaseEntityFilter {

	private static final long serialVersionUID = 8841187476288722100L;

	private Long empresaId;

	private Long sedeId;

	private Long instalacionId;

	private LocalDate fechaDesde;

	private LocalDate fechaHasta;

}
