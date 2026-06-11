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
import com.gestion.deportiva.model.InstalacionTipo;
import com.gestion.deportiva.repository.InstalacionTipoRepository;
import com.gestion.deportiva.service.InstalacionTipoService;
import com.gestion.deportiva.util.InstalacionTipoUtil;
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

	@Override
	public InstalacionTipoDTO findById(Long id) {
		logger.info("Buscando InstalacionTipo por ID: {}", id);
		return InstalacionTipoUtil.modelToDTO(instalacionTipoRepository.findByActivoTrueAndId(id));
	}

	@Override
	public InstalacionTipoDTO findByUuid(String uuid) {
		logger.info("Buscando InstalacionTipo por UUID: {}", uuid);
		return InstalacionTipoUtil.modelToDTO(instalacionTipoRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	@CacheEvict(value = "instalacionTipos", allEntries = true)
	public String guardar(InstalacionTipoDTO dto) {
		logger.info("Guardando InstalacionTipo");
		InstalacionTipo model = instalacionTipoRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo InstalacionTipo");
			model = new InstalacionTipo();
		}
		model = InstalacionTipoUtil.dtoToModel(dto, model);
		instalacionTipoRepository.saveAndFlush(model);
		return model.getUuid();
	}

	@Override
	public Page<InstalacionTipoDTO> getPageByFilter(InstalacionTipoFilter filter, Pageable pageable) {
		return InstalacionTipoUtil.pageToPageDTO(instalacionTipoRepository.findAll(InstalacionTipoSpecifications.filter(filter), pageable));
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
		return InstalacionTipoUtil.modelToDTO(instalacionTipoRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre));
	}

	@Override
	@Cacheable("instalacionTipos")
	public List<ComboDTO> getListComboDTO() {
		return InstalacionTipoUtil.listModelToListComboDTO(instalacionTipoRepository.findByActivoTrue());
	}

	@Override
	public List<InstalacionTipoDTO> getListDTO() {
		return Utils.sortByNombre(InstalacionTipoUtil.listModelToListDTO(instalacionTipoRepository.findByActivoTrue()));
	}

	@Override
	public List<InstalacionTipoDTO> getListDTO(InstalacionTipoFilter filter) {
		return Utils.sortByNombre(
				InstalacionTipoUtil.listModelToListDTO(instalacionTipoRepository.findAll(InstalacionTipoSpecifications.filter(filter))));
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
