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
import com.gestion.deportiva.dto.InstalacionTipoDTO;
import com.gestion.deportiva.dto.filter.InstalacionTipoFilter;
import com.gestion.deportiva.dto.specifications.InstalacionTipoSpecifications;
import com.gestion.deportiva.mapper.InstalacionTipoMapper;
import com.gestion.deportiva.model.InstalacionTipo;
import com.gestion.deportiva.repository.InstalacionTipoRepository;
import com.gestion.deportiva.service.InstalacionTipoService;
import com.gestion.deportiva.util.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class InstalacionTipoServiceImpl implements InstalacionTipoService {

	private static final Logger logger = LoggerFactory.getLogger(InstalacionTipoServiceImpl.class);

	@Autowired
	private InstalacionTipoRepository instalacionTipoRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private InstalacionTipoMapper instalacionTipoMapper;

	@Override
	public InstalacionTipoDTO findById(Long id) {
		logger.info("Buscando InstalacionTipo por ID: {}", id);
		return instalacionTipoMapper.modelToDTO(instalacionTipoRepository.findByActivoTrueAndId(id));
	}

	@Override
	public InstalacionTipoDTO findByUuid(String uuid) {
		logger.info("Buscando InstalacionTipo por UUID: {}", uuid);
		return instalacionTipoMapper
				.modelToDTO(instalacionTipoRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	@CacheEvict(value = "instalacionTipos", allEntries = true)
	public Long guardar(InstalacionTipoDTO dto) {
		logger.info("Guardando InstalacionTipo");
		InstalacionTipo model = instalacionTipoRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo InstalacionTipo");
			model = new InstalacionTipo();
		}
		model = instalacionTipoMapper.dtoToModel(dto, model);
		instalacionTipoRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public Page<InstalacionTipoDTO> getPageByFilter(InstalacionTipoFilter filter, Pageable pageable) {
		return instalacionTipoMapper.pageToPageDTO(
				instalacionTipoRepository.findAll(InstalacionTipoSpecifications.filter(filter), pageable));
	}

	@Override
	@CacheEvict(value = "instalacionTipos", allEntries = true)
	public void eliminar(Long id) {
		logger.info("Eliminando InstalacionTipo por ID: {}");
		InstalacionTipo model = instalacionTipoRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		instalacionTipoRepository.saveAndFlush(model);
	}

	@Override
	@CacheEvict(value = "instalacionTipos", allEntries = true)
	public void eliminar(String uuid) {
		logger.info("Eliminando InstalacionTipo por ID: {}");
		InstalacionTipo model = instalacionTipoRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		instalacionTipoRepository.saveAndFlush(model);
	}

	@Override
	public InstalacionTipoDTO findByNombreEqualsIgnoreCase(String nombre) {
		return instalacionTipoMapper
				.modelToDTO(instalacionTipoRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre));
	}

	@Override
	@Cacheable("instalacionTipos")
	public List<ComboDTO> getListComboDTO() {
		return instalacionTipoMapper.listModelToListComboDTO(instalacionTipoRepository.findByActivoTrue());
	}

	@Override
	public List<InstalacionTipoDTO> getListDTO() {
		return Utils
				.sortByNombre(instalacionTipoMapper.listModelToListDTO(instalacionTipoRepository.findByActivoTrue()));

	}

	@Override
	public List<InstalacionTipoDTO> getListDTO(InstalacionTipoFilter filter) {
		return Utils.sortByNombre(instalacionTipoMapper
				.listModelToListDTO(instalacionTipoRepository.findAll(InstalacionTipoSpecifications.filter(filter))));
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
	public byte[] exportarExcel(InstalacionTipoFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}
}
