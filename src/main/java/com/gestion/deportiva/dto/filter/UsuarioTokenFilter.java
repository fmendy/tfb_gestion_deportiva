package com.gestion.deportiva.dto.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioTokenFilter extends BaseEntityFilter {

	private static final long serialVersionUID = 6703885416229426131L;

	private Long usuarioId;

}
