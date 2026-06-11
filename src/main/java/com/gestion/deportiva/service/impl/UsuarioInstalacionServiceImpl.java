package com.gestion.deportiva.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.dto.UsuarioInstalacionDTO;
import com.gestion.deportiva.dto.filter.UsuarioInstalacionFilter;
import com.gestion.deportiva.dto.specifications.UsuarioInstalacionSpecifications;
import com.gestion.deportiva.model.UsuarioInstalacion;
import com.gestion.deportiva.repository.UsuarioInstalacionRepository;
import com.gestion.deportiva.service.UsuarioInstalacionService;
import com.gestion.deportiva.util.UsuarioInstalacionUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class UsuarioInstalacionServiceImpl implements UsuarioInstalacionService {

	private static final Logger logger = LoggerFactory.getLogger(UsuarioInstalacionServiceImpl.class);

	@Autowired
	private UsuarioInstalacionRepository usuarioInstalacionRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public UsuarioInstalacionDTO findById(Long id) {
		logger.info("Buscando UsuarioInstalacion por ID: {}", id);
		return UsuarioInstalacionUtil.modelToDTO(usuarioInstalacionRepository.findByActivoTrueAndId(id));
	}

	@Override
	public UsuarioInstalacionDTO findByUuid(String uuid) {
		logger.info("Buscando UsuarioInstalacion por UUID: {}", uuid);
		return UsuarioInstalacionUtil.modelToDTO(usuarioInstalacionRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	public String guardar(UsuarioInstalacionDTO dto) {
		logger.info("Guardando UsuarioInstalacion");
		UsuarioInstalacion model = usuarioInstalacionRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo UsuarioInstalacion");
			model = new UsuarioInstalacion();
		}
		model = UsuarioInstalacionUtil.dtoToModel(dto, model);
		usuarioInstalacionRepository.saveAndFlush(model);
		return model.getUuid();
	}

	@Override
	public Page<UsuarioInstalacionDTO> getPageByFilter(UsuarioInstalacionFilter filter, Pageable pageable) {
		return UsuarioInstalacionUtil.pageToPageDTO(usuarioInstalacionRepository.findAll(UsuarioInstalacionSpecifications.filter(filter), pageable));
	}

	@Override
	public void eliminar(Long id) {
		logger.info("Eliminando UsuarioInstalacion por ID: {}");
		UsuarioInstalacion model = usuarioInstalacionRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		usuarioInstalacionRepository.saveAndFlush(model);
	}

	@Override
	public void eliminar(String uuid) {
		logger.info("Eliminando UsuarioInstalacion por ID: {}");
		UsuarioInstalacion model = usuarioInstalacionRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		usuarioInstalacionRepository.saveAndFlush(model);
	}

	@Override
	public List<UsuarioInstalacionDTO> getListDTO() {
		return UsuarioInstalacionUtil.listModelToListDTO(usuarioInstalacionRepository.findByActivoTrue());
	}

	@Override
	public List<UsuarioInstalacionDTO> getListDTO(UsuarioInstalacionFilter filter) {
		return UsuarioInstalacionUtil.listModelToListDTO(usuarioInstalacionRepository.findAll(UsuarioInstalacionSpecifications.filter(filter)));
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
	public byte[] exportarExcel(UsuarioInstalacionFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}
}
