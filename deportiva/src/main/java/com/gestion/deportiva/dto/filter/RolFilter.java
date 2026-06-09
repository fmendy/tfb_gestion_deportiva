package com.gestion.deportiva.dto.filter;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RolFilter extends MaestraFilter {

	private static final long serialVersionUID = 2672533785504574475L;

	private List<String> listNombre = new ArrayList<>();

}
