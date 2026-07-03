package com.gestion.deportiva.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservaListadoDTO extends ReservaDTO {

	private static final long serialVersionUID = 6379839239499903284L;

	private String reservaEstadoCss;

	private boolean mostrarEliminar;

	private boolean mostrarCancelarPorUsuario;
	
	private boolean mostrarAprobar;
	
	private boolean mostrarDenegar;
	
	private boolean mostrarCompletar;
	
	private boolean mostrarIncompletar;
	
	private boolean mostrarCancelarPorEmpresa;
	
	private boolean mostrarSancionar;

}
