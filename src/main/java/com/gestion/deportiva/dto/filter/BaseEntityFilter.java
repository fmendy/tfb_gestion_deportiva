package com.gestion.deportiva.dto.filter;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BaseEntityFilter implements Serializable {
	
	private static final long serialVersionUID = -1818109955069842363L;
	
	private String uuid;

}
