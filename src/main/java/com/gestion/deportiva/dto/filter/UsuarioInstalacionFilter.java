package com.gestion.deportiva.dto.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioInstalacionFilter extends BaseEntityFilter {
	
	private static final long serialVersionUID = -4233691865692618715L;

	private Long empresaId;

	private Long sedeId;

	private Long instalacionId;
	
	private Long usuarioId;

}
