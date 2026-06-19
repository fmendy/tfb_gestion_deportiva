package com.gestion.deportiva.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.dto.UsuarioSedeDTO;
import com.gestion.deportiva.dto.filter.UsuarioSedeFilter;
import com.gestion.deportiva.dto.specifications.UsuarioSedeSpecifications;
import com.gestion.deportiva.mapper.UsuarioSedeMapper;
import com.gestion.deportiva.model.Sede;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.model.UsuarioSede;
import com.gestion.deportiva.repository.UsuarioSedeRepository;
import com.gestion.deportiva.service.UsuarioSedeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class UsuarioSedeServiceImpl implements UsuarioSedeService {

	private static final Logger logger = LoggerFactory.getLogger(UsuarioSedeServiceImpl.class);

	@Autowired
	private UsuarioSedeRepository usuarioSedeRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private UsuarioSedeMapper usuarioSedeMapper;

	@Override
	public UsuarioSedeDTO findById(Long id) {
		logger.info("Buscando UsuarioSede por ID: {}", id);
		return usuarioSedeMapper.modelToDTO(usuarioSedeRepository.findByActivoTrueAndId(id));
	}

	@Override
	public UsuarioSedeDTO findByUuid(String uuid) {
		logger.info("Buscando UsuarioSede por UUID: {}", uuid);
		return usuarioSedeMapper.modelToDTO(usuarioSedeRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	public Long guardar(UsuarioSedeDTO dto) {
		logger.info("Guardando UsuarioSede");
		UsuarioSede model = usuarioSedeRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo UsuarioSede");
			model = new UsuarioSede();
		}
		model = usuarioSedeMapper.dtoToModel(dto, model);
		usuarioSedeRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public Page<UsuarioSedeDTO> getPageByFilter(UsuarioSedeFilter filter, Pageable pageable) {
		return usuarioSedeMapper
				.pageToPageDTO(usuarioSedeRepository.findAll(UsuarioSedeSpecifications.filter(filter), pageable));
	}

	@Override
	public void eliminar(Long id) {
		logger.info("Eliminando UsuarioSede por ID: {}");
		UsuarioSede model = usuarioSedeRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		usuarioSedeRepository.saveAndFlush(model);
	}

	@Override
	public void eliminar(String uuid) {
		logger.info("Eliminando UsuarioSede por ID: {}");
		UsuarioSede model = usuarioSedeRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		usuarioSedeRepository.saveAndFlush(model);
	}

	@Override
	public List<UsuarioSedeDTO> getListDTO() {
		return usuarioSedeMapper.listModelToListDTO(usuarioSedeRepository.findByActivoTrue());
	}

	@Override
	public List<UsuarioSedeDTO> getListDTO(UsuarioSedeFilter filter) {
		return usuarioSedeMapper
				.listModelToListDTO(usuarioSedeRepository.findAll(UsuarioSedeSpecifications.filter(filter)));
	}

	@Override
	public boolean canWrite(Long id) {
		return true;
	}

	@Override
	public boolean canRead(Long id) {
		return true;
	}

	@Override
	public byte[] exportarExcel(UsuarioSedeFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long asociarUsuarioSede(Long usuarioId, Long sedeId) {
		UsuarioSede model = new UsuarioSede();
		model.setUsuario(new Usuario(usuarioId));
		model.setSede(new Sede(sedeId));
		logger.info("Asociando Usuario " + usuarioId + " con Sede " + sedeId);
		usuarioSedeRepository.saveAndFlush(model);
		return model.getId();
	}
}
