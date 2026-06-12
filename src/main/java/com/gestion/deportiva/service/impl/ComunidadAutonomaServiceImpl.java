package com.gestion.deportiva.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.ComunidadAutonomaDTO;
import com.gestion.deportiva.dto.filter.ComunidadAutonomaFilter;
import com.gestion.deportiva.dto.specifications.ComunidadAutonomaSpecifications;
import com.gestion.deportiva.model.ComunidadAutonoma;
import com.gestion.deportiva.repository.ComunidadAutonomaRepository;
import com.gestion.deportiva.service.ComunidadAutonomaService;
import com.gestion.deportiva.util.ComunidadAutonomaUtil;
import com.gestion.deportiva.util.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class ComunidadAutonomaServiceImpl implements ComunidadAutonomaService {

	private static final Logger logger = LoggerFactory.getLogger(ComunidadAutonomaServiceImpl.class);

	@Autowired
	private ComunidadAutonomaRepository comunidadAutonomaRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public ComunidadAutonomaDTO findById(Long id) {
		logger.info("Buscando ComunidadAutonoma por ID: {}", id);
		return ComunidadAutonomaUtil.modelToDTO(comunidadAutonomaRepository.findByActivoTrueAndId(id));
	}

	@Override
	public ComunidadAutonomaDTO findByUuid(String uuid) {
		logger.info("Buscando ComunidadAutonoma por UUID: {}", uuid);
		return ComunidadAutonomaUtil
				.modelToDTO(comunidadAutonomaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	@CacheEvict(value = "comunidades", allEntries = true)
	public Long guardar(ComunidadAutonomaDTO dto) {
		logger.info("Guardando ComunidadAutonoma");
		ComunidadAutonoma model = comunidadAutonomaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo ComunidadAutonoma");
			model = new ComunidadAutonoma();
		}
		model = ComunidadAutonomaUtil.dtoToModel(dto, model);
		comunidadAutonomaRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public Page<ComunidadAutonomaDTO> getPageByFilter(ComunidadAutonomaFilter filter, Pageable pageable) {
		return ComunidadAutonomaUtil.pageToPageDTO(
				comunidadAutonomaRepository.findAll(ComunidadAutonomaSpecifications.filter(filter), pageable));
	}

	@Override
	@CacheEvict(value = "comunidades", allEntries = true)
	public void eliminar(Long id) {
		logger.info("Eliminando ComunidadAutonoma por ID: {}");
		ComunidadAutonoma model = comunidadAutonomaRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		comunidadAutonomaRepository.saveAndFlush(model);
	}

	@Override
	@CacheEvict(value = "comunidades", allEntries = true)
	public void eliminar(String uuid) {
		logger.info("Eliminando ComunidadAutonoma por ID: {}");
		ComunidadAutonoma model = comunidadAutonomaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		comunidadAutonomaRepository.saveAndFlush(model);
	}

	@Override
	public ComunidadAutonomaDTO findByNombreEqualsIgnoreCase(String nombre) {
		return ComunidadAutonomaUtil
				.modelToDTO(comunidadAutonomaRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre));
	}

	@Override
	@Cacheable("comunidades")
	public List<ComboDTO> getListComboDTO() {
		return ComunidadAutonomaUtil.listModelToListComboDTO(comunidadAutonomaRepository.findByActivoTrue());
	}

	@Override
	public List<ComunidadAutonomaDTO> getListDTO() {
		return Utils
				.sortByNombre(ComunidadAutonomaUtil.listModelToListDTO(comunidadAutonomaRepository.findByActivoTrue()));
	}

	@Override
	public List<ComunidadAutonomaDTO> getListDTO(ComunidadAutonomaFilter filter) {
		return Utils.sortByNombre(ComunidadAutonomaUtil.listModelToListDTO(
				comunidadAutonomaRepository.findAll(ComunidadAutonomaSpecifications.filter(filter))));
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
	public byte[] exportarExcel(ComunidadAutonomaFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}
}
