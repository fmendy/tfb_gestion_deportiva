package com.gestion.deportiva.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gestion.deportiva.dto.BaseDTO;
import com.gestion.deportiva.dto.filter.BaseEntityFilter;

public interface BaseService<T extends BaseDTO, F extends BaseEntityFilter> {

	T findById(Long id);

	T findByUuid(String uuid);

	String guardar(T dto);

	Page<T> getPageByFilter(F filter, Pageable pageable);

	void eliminar(Long id);

	void eliminar(String uuid);

	List<T> getListDTO();

	List<T> getListDTO(F filter);
	
	boolean canWrite(String uuid);
	
	boolean canRead(String uuid);
	
	byte[] exportarExcel(F filter) throws IOException;
}
