package com.gestion.deportiva.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class MaestraDTO extends BaseDTO implements Serializable {


	private static final long serialVersionUID = -8338998915852392550L;
	

	@NotBlank
	@Size(max = 255)
	private String nombre;
}
