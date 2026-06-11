package com.gestion.deportiva.repository;

import org.springframework.stereotype.Repository;

import com.gestion.deportiva.model.Sancion;

@Repository
public interface SancionRepository extends BaseEntityRepository<Sancion, Long> {

}
