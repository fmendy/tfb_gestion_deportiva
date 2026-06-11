package com.gestion.deportiva.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BreadcrumbDTO implements Serializable {

	private static final long serialVersionUID = 166526959094387876L;

	private String label;

	private String url;

	public BreadcrumbDTO(String label, String url) {
		super();
		this.label = label;
		this.url = url;
	}
	
	
}
