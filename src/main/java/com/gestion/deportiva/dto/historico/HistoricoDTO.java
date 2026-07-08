package com.gestion.deportiva.dto.historico;

import java.util.Date;

import com.gestion.deportiva.dto.BaseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HistoricoDTO extends BaseDTO{

	private static final long serialVersionUID = 3874367788250417381L;

	private String usuarioModificacion;
	
	private Date fechaModificacion;
}
