package com.gestion.deportiva.dto.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MaestraFilter extends BaseEntityFilter {

	private static final long serialVersionUID = 4544319948461363414L;

	private String nombre;

}
