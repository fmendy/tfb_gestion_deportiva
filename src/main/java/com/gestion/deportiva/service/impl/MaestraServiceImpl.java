package com.gestion.deportiva.service.impl;

import com.gestion.deportiva.dto.BaseDTO;
import com.gestion.deportiva.dto.filter.BaseEntityFilter;
import com.gestion.deportiva.service.MaestraService;

public abstract class MaestraServiceImpl<T extends BaseDTO, F extends BaseEntityFilter> extends BaseServiceImpl<T, F>
		implements MaestraService<T, F> {

}