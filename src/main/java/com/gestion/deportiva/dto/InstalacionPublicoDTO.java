package com.gestion.deportiva.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InstalacionPublicoDTO extends MaestraDTO {

	private static final long serialVersionUID = -2598188018260874895L;
	
	private String descripcion;
	
    private String instalacionTipoNombre;
    
    private String empresaNombre;
    
    private String sedeLatitud;
    
    private String sedeLongitud;
    
    private Long sedeId;
    
    private String sedeNombre;
    
    private String sedeDireccion;
    
    private String sedeMunicipioNombre;
    
    private String sedeMunicipioProvinciaNombre;
    
    private String sedeMunicipioProvinciaComunidadAutonomaNombre;

    // Horario base semanal
    private List<InstalacionHorarioPublicoDTO> horarioSemanal;

    // Horario final con especiales aplicados (clave = fecha)
    private Map<LocalDate, List<InstalacionHorarioPublicoDTO>> horarioCalculado;
}