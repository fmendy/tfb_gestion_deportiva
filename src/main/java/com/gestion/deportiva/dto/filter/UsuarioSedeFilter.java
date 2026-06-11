package com.gestion.deportiva.dto.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioSedeFilter extends BaseEntityFilter {

	private static final long serialVersionUID = -7914730514249738223L;

	private Long empresaId;

	private Long sedeId;

	private Long usuarioId;

}
