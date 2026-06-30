package com.gestion.deportiva.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InstalacionDisponibilidadDTO extends MaestraDTO {

	private static final long serialVersionUID = -2712215987494087343L;

	private String descripcion;

	private String instalacionTipoNombre;

	private String empresaNombre;

	private String sedeNombre;

	private String sedeDireccion;

	private String sedeMunicipioNombre;

	private String sedeMunicipioProvinciaNombre;

	private String sedeMunicipioProvinciaComunidadAutonomaNombre;

	private String fecha;

	private Long instalacionConfiguracionReservaDuracionMin;

	private Long instalacionConfiguracionReservaDuracionMax;

	private Long instalacionConfiguracionReservaIntervaloHorario;
	
	private List<FranjaHorariaDTO> listFranjaHoraria;
}
