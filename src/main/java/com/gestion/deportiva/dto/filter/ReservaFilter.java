package com.gestion.deportiva.dto.filter;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservaFilter extends BaseEntityFilter {

	private static final long serialVersionUID = -5645711373965396587L;

	private Long empresaId;

	private Long sedeId;

	private Long instalacionId;
	
	private Long instalacionTipoId;

	private LocalDate fechaDesde;

	private LocalDate fechaHasta;

	private Long reservaEstadoId;

	private Long usuarioCreacionId;

	private List<Long> listEmpresaIds;

	private List<Long> listSedeIds;

	private List<Long> listInstalacionIds;

}
