package com.gestion.deportiva.service;

import java.util.List;

import com.gestion.deportiva.dto.BaseDTO;
import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.filter.BaseEntityFilter;

public interface MaestraService<T extends BaseDTO, F extends BaseEntityFilter> extends BaseService<T, F> {

	T findByNombreEqualsIgnoreCase(String nombre);

	List<ComboDTO> getListComboDTO();

}
