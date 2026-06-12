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
import com.gestion.deportiva.dto.SancionTipoDTO;
import com.gestion.deportiva.dto.filter.SancionTipoFilter;
import com.gestion.deportiva.dto.specifications.SancionTipoSpecifications;
import com.gestion.deportiva.model.SancionTipo;
import com.gestion.deportiva.repository.SancionTipoRepository;
import com.gestion.deportiva.service.SancionTipoService;
import com.gestion.deportiva.util.SancionTipoUtil;
import com.gestion.deportiva.util.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class SancionTipoServiceImpl implements SancionTipoService {

	private static final Logger logger = LoggerFactory.getLogger(SancionTipoServiceImpl.class);

	@Autowired
	private SancionTipoRepository sancionTipoRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public SancionTipoDTO findById(Long id) {
		logger.info("Buscando SancionTipo por ID: {}", id);
		return SancionTipoUtil.modelToDTO(sancionTipoRepository.findByActivoTrueAndId(id));
	}

	@Override
	public SancionTipoDTO findByUuid(String uuid) {
		logger.info("Buscando SancionTipo por UUID: {}", uuid);
		return SancionTipoUtil
				.modelToDTO(sancionTipoRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	@CacheEvict(value = "comunidades", allEntries = true)
	public Long guardar(SancionTipoDTO dto) {
		logger.info("Guardando SancionTipo");
		SancionTipo model = sancionTipoRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo SancionTipo");
			model = new SancionTipo();
		}
		model = SancionTipoUtil.dtoToModel(dto, model);
		sancionTipoRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public Page<SancionTipoDTO> getPageByFilter(SancionTipoFilter filter, Pageable pageable) {
		return SancionTipoUtil.pageToPageDTO(
				sancionTipoRepository.findAll(SancionTipoSpecifications.filter(filter), pageable));
	}

	@Override
	@CacheEvict(value = "comunidades", allEntries = true)
	public void eliminar(Long id) {
		logger.info("Eliminando SancionTipo por ID: {}");
		SancionTipo model = sancionTipoRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		sancionTipoRepository.saveAndFlush(model);
	}

	@Override
	@CacheEvict(value = "comunidades", allEntries = true)
	public void eliminar(String uuid) {
		logger.info("Eliminando SancionTipo por ID: {}");
		SancionTipo model = sancionTipoRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		sancionTipoRepository.saveAndFlush(model);
	}

	@Override
	public SancionTipoDTO findByNombreEqualsIgnoreCase(String nombre) {
		return SancionTipoUtil
				.modelToDTO(sancionTipoRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre));
	}

	@Override
	@Cacheable("comunidades")
	public List<ComboDTO> getListComboDTO() {
		return SancionTipoUtil.listModelToListComboDTO(sancionTipoRepository.findByActivoTrue());
	}

	@Override
	public List<SancionTipoDTO> getListDTO() {
		return Utils
				.sortByNombre(SancionTipoUtil.listModelToListDTO(sancionTipoRepository.findByActivoTrue()));
	}

	@Override
	public List<SancionTipoDTO> getListDTO(SancionTipoFilter filter) {
		return Utils.sortByNombre(SancionTipoUtil.listModelToListDTO(
				sancionTipoRepository.findAll(SancionTipoSpecifications.filter(filter))));
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
	public byte[] exportarExcel(SancionTipoFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}
}
