package com.gestion.deportiva.dto.filter;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioFilter extends MaestraFilter implements Serializable {

	private static final long serialVersionUID = 7282733907446780968L;

	private String email;
	
	private String demarcacionUuid; 
	
	private List<String> listDemarcacionUuid;
	
	private String rolUuid;

}
