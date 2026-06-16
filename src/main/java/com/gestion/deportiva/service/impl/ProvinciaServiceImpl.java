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
import com.gestion.deportiva.dto.ProvinciaDTO;
import com.gestion.deportiva.dto.filter.ProvinciaFilter;
import com.gestion.deportiva.dto.specifications.ProvinciaSpecifications;
import com.gestion.deportiva.model.Provincia;
import com.gestion.deportiva.repository.ComunidadAutonomaRepository;
import com.gestion.deportiva.repository.ProvinciaRepository;
import com.gestion.deportiva.service.ProvinciaService;
import com.gestion.deportiva.util.ProvinciaUtil;
import com.gestion.deportiva.util.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class ProvinciaServiceImpl implements ProvinciaService {

	private static final Logger logger = LoggerFactory.getLogger(ProvinciaServiceImpl.class);

	@Autowired
	private ProvinciaRepository provinciaRepository;

	@Autowired
	private ComunidadAutonomaRepository comunidadAutonomaRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public ProvinciaDTO findById(Long id) {
		logger.info("Buscando Provincia por ID: {}", id);
		return ProvinciaUtil.modelToDTO(provinciaRepository.findByActivoTrueAndId(id));
	}

	@Override
	public ProvinciaDTO findByUuid(String uuid) {
		logger.info("Buscando Provincia por UUID: {}", uuid);
		return ProvinciaUtil.modelToDTO(provinciaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	@CacheEvict(value = "provincias", allEntries = true)
	public Long guardar(ProvinciaDTO dto) {
		logger.info("Guardando Provincia");
		Provincia model = provinciaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo Provincia");
			model = new Provincia();
		}
		model = ProvinciaUtil.dtoToModel(dto, model,
				comunidadAutonomaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getComunidadAutonomaUuid()));
		provinciaRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public Page<ProvinciaDTO> getPageByFilter(ProvinciaFilter filter, Pageable pageable) {
		return ProvinciaUtil
				.pageToPageDTO(provinciaRepository.findAll(ProvinciaSpecifications.filter(filter), pageable));
	}

	@Override
	@CacheEvict(value = "provincias", allEntries = true)
	public void eliminar(Long id) {
		logger.info("Eliminando Provincia por ID: {}");
		Provincia model = provinciaRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		provinciaRepository.saveAndFlush(model);
	}

	@Override
	@CacheEvict(value = "provincias", allEntries = true)
	public void eliminar(String uuid) {
		logger.info("Eliminando Provincia por ID: {}");
		Provincia model = provinciaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		provinciaRepository.saveAndFlush(model);
	}

	@Override
	public ProvinciaDTO findByNombreEqualsIgnoreCase(String nombre) {
		return ProvinciaUtil.modelToDTO(provinciaRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre));
	}

	@Override
	@Cacheable("provincias")
	public List<ComboDTO> getListComboDTO() {
		return ProvinciaUtil.listModelToListComboDTO(provinciaRepository.findByActivoTrue());
	}

	@Override
	public List<ProvinciaDTO> getListDTOByComunidadAutonomaId(Long comunidadAutonomaId) {
		if (comunidadAutonomaId == null) {
			return getListDTO();
		} else {
			return ProvinciaUtil.listModelToListDTO(
					provinciaRepository.findByActivoTrueAndComunidadAutonomaId(comunidadAutonomaId));
		}
	}

	@Override
	public List<ProvinciaDTO> getListDTO() {
		return Utils.sortByNombre(ProvinciaUtil.listModelToListDTO(provinciaRepository.findByActivoTrue()));
	}

	@Override
	public List<ProvinciaDTO> getListDTO(ProvinciaFilter filter) {
		return Utils.sortByNombre(
				ProvinciaUtil.listModelToListDTO(provinciaRepository.findAll(ProvinciaSpecifications.filter(filter))));
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
	public byte[] exportarExcel(ProvinciaFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

}
