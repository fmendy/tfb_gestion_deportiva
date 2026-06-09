package com.gestion.deportiva.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ComboDTO implements Serializable {

	private static final long serialVersionUID = -1230850390210537332L;

	private String key;

	private String value;

	private String keyPadre;

	private String keyAbuelo;

	public ComboDTO(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public ComboDTO(String key, String value, String keyPadre) {
		super();
		this.key = key;
		this.value = value;
		this.keyPadre = keyPadre;
	}

	public ComboDTO(String key, String value, String keyPadre, String keyAbuelo) {
		super();
		this.key = key;
		this.value = value;
		this.keyPadre = keyPadre;
		this.keyAbuelo = keyAbuelo;
	}

}
