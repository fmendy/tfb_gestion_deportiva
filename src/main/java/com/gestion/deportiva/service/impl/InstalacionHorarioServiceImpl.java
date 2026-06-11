package com.gestion.deportiva.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.dto.InstalacionHorarioDTO;
import com.gestion.deportiva.dto.filter.InstalacionHorarioFilter;
import com.gestion.deportiva.dto.specifications.InstalacionHorarioSpecifications;
import com.gestion.deportiva.model.InstalacionHorario;
import com.gestion.deportiva.repository.InstalacionHorarioRepository;
import com.gestion.deportiva.service.InstalacionHorarioService;
import com.gestion.deportiva.util.InstalacionHorarioUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class InstalacionHorarioServiceImpl implements InstalacionHorarioService {

	private static final Logger logger = LoggerFactory.getLogger(InstalacionHorarioServiceImpl.class);

	@Autowired
	private InstalacionHorarioRepository instalacionHorarioRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public InstalacionHorarioDTO findById(Long id) {
		logger.info("Buscando InstalacionHorario por ID: {}", id);
		return InstalacionHorarioUtil.modelToDTO(instalacionHorarioRepository.findByActivoTrueAndId(id));
	}

	@Override
	public InstalacionHorarioDTO findByUuid(String uuid) {
		logger.info("Buscando InstalacionHorario por UUID: {}", uuid);
		return InstalacionHorarioUtil.modelToDTO(instalacionHorarioRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	public String guardar(InstalacionHorarioDTO dto) {
		logger.info("Guardando InstalacionHorario");
		InstalacionHorario model = instalacionHorarioRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo InstalacionHorario");
			model = new InstalacionHorario();
		}
		model = InstalacionHorarioUtil.dtoToModel(dto, model);
		instalacionHorarioRepository.saveAndFlush(model);
		return model.getUuid();
	}

	@Override
	public Page<InstalacionHorarioDTO> getPageByFilter(InstalacionHorarioFilter filter, Pageable pageable) {
		return InstalacionHorarioUtil.pageToPageDTO(instalacionHorarioRepository.findAll(InstalacionHorarioSpecifications.filter(filter), pageable));
	}

	@Override
	public void eliminar(Long id) {
		logger.info("Eliminando InstalacionHorario por ID: {}");
		InstalacionHorario model = instalacionHorarioRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		instalacionHorarioRepository.saveAndFlush(model);
	}

	@Override
	public void eliminar(String uuid) {
		logger.info("Eliminando InstalacionHorario por ID: {}");
		InstalacionHorario model = instalacionHorarioRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		instalacionHorarioRepository.saveAndFlush(model);
	}

	@Override
	public List<InstalacionHorarioDTO> getListDTO() {
		return InstalacionHorarioUtil.listModelToListDTO(instalacionHorarioRepository.findByActivoTrue());
	}

	@Override
	public List<InstalacionHorarioDTO> getListDTO(InstalacionHorarioFilter filter) {
		return InstalacionHorarioUtil.listModelToListDTO(instalacionHorarioRepository.findAll(InstalacionHorarioSpecifications.filter(filter)));
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
	public byte[] exportarExcel(InstalacionHorarioFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}
}
