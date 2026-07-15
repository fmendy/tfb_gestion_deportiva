package com.gestion.deportiva.service.impl;

import java.util.List;


import com.gestion.deportiva.dto.BaseDTO;
import com.gestion.deportiva.dto.filter.BaseEntityFilter;
import com.gestion.deportiva.model.BaseEntity;
import com.gestion.deportiva.service.BaseService;

public abstract class BaseServiceImpl<T extends BaseDTO, F extends BaseEntityFilter>
        implements BaseService<T, F> {

    protected <E extends BaseEntity> void desactivar(List<E> entidades) {
        entidades.forEach(e -> e.setActivo(false));
    }
}