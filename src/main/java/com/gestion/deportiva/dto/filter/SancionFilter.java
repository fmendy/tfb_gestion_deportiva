package com.gestion.deportiva.dto.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SancionFilter extends BaseEntityFilter {

	private static final long serialVersionUID = 9221019637732455446L;

	private Long empresaId;

	private Long usuarioId;

	private Long sancionTipoId;

}
