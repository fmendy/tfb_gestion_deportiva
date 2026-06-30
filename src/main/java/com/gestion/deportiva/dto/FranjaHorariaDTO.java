package com.gestion.deportiva.dto;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FranjaHorariaDTO implements Serializable {

	public FranjaHorariaDTO(LocalTime inicio, List<FranjaHorariaDuracionDTO> opcionesValidas, String estado) {
		this.inicio = inicio;
		this.opciones = opcionesValidas;
		this.estado = estado;
	}

	private static final long serialVersionUID = -1653358986211621220L;

	private List<FranjaHorariaDuracionDTO> opciones;

	private LocalTime inicio;

	private LocalTime fin;

	private String estado;

}
