package com.gestion.deportiva.dto.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImagenFilter extends BaseEntityFilter {
	
	private static final long serialVersionUID = -6080911578482040719L;
	
	private Long empresaId;
	
	private Long sedeId;
	
	private Long instalacionId;

}
