package com.gestion.deportiva.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstalacionHorarioSemanalDTO extends BaseDTO {

	private static final long serialVersionUID = -2630955013997587675L;
	
	public InstalacionHorarioSemanalDTO() {
        // Inicializar los 7 días
        for(int i = 1; i <= 7; i++) {
            horarios.put(i, new ArrayList<>());
        }
    }

	@NotNull
	private Long instalacionId;

	@NotEmpty
	@Valid
	private Map<Integer, List<InstalacionHorarioDTO>> horarios = new HashMap<>();
}
