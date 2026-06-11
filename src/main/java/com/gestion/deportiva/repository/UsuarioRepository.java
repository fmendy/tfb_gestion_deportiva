package com.gestion.deportiva.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.gestion.deportiva.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

	Usuario findByActivoTrueAndEmailIgnoreCaseAndIdNot(String email, Long id);

	Usuario findByActivoTrueAndEmailIgnoreCase(String email);

	Usuario findByActivoTrueAndNombreContainsIgnoreCase(String nombre);

	Usuario findByActivoTrueAndNombreEqualsIgnoreCase(String nombre);

	Usuario findByActivoTrueAndNombreIgnoreCase(String nombre);

	Usuario findByActivoTrueAndNombreEqualsIgnoreCaseAndIdNot(String nombre, Long id);
	
	Usuario findByActivoTrueAndEmailEqualsIgnoreCaseAndUuid(String email, String uuid);
	
	Usuario findByActivoTrueAndEmailEqualsIgnoreCaseAndUuidNot(String email, String uuid);

	List<Usuario> findByActivoTrueOrderByNombreAsc();

	List<Usuario> findByActivoTrueOrderByNombreDesc();

	List<Usuario> findByActivoTrue();

	Usuario findByActivoTrueAndId(Long id);

	Usuario findByActivoTrueAndUuidEqualsIgnoreCase(String uuid);
}
