package com.gestion.deportiva.dto.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioEmpresaFilter extends BaseEntityFilter {

	private static final long serialVersionUID = -1444599947630179869L;

	private Long empresaId;

	private Long usuarioId;

}
