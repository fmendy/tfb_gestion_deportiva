package com.gestion.deportiva.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MiPerfilDTO extends MaestraDTO {

	private static final long serialVersionUID = 4324482952546323280L;

	private String email;
	
	private String rolNombre;
	
	private Date fechaAlta;

}
