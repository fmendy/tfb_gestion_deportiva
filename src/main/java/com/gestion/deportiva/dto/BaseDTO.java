package com.gestion.deportiva.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class BaseDTO implements Serializable {

	private static final long serialVersionUID = -6055730925626631566L;

	private Long id;
	
	private String uuid;

	public BaseDTO(Long id, String uuid) {
		super();
		this.id = id;
		this.uuid = uuid;
	}

	public BaseDTO(Long id) {
		super();
		this.id = id;
	}

	public BaseDTO(String uuid) {
		super();
		this.uuid = uuid;
	}
	
	
}
