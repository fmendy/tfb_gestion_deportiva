package com.gestion.deportiva.dto.filter;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SedePublicoFilter extends MaestraFilter {

	private static final long serialVersionUID = 5901157233852247727L;

	private Long comunidadAutonomaId;

	private Long provinciaId;

	private Long municipioId;

	private List<Long> listInstalacionTipoIds;

}
