package com.gestion.deportiva.util;

import java.util.List;

import org.springframework.data.domain.Page;

import com.gestion.deportiva.dto.BaseDTO;
import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.filter.BaseEntityFilter;
import com.gestion.deportiva.model.BaseEntity;

public interface BaseUtil<T extends BaseDTO, F extends BaseEntity, K extends BaseEntityFilter> {

	public T modelToDTO(F model);

	public List<T> listModelToListDTO(List<F> list);

	public Page<T> pageToPageDTO(Page<F> page);

	public F dtoToModel(T dto, F model);

	public String cleanUrlPageFilter(K filter, String url);
	
	public List<ComboDTO> listModelToListComboDTO(List<F> list);

}
