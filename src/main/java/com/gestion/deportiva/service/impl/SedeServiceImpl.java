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
import com.gestion.deportiva.dto.SedeDTO;
import com.gestion.deportiva.dto.filter.SedeFilter;
import com.gestion.deportiva.dto.specifications.SedeSpecifications;
import com.gestion.deportiva.model.Sede;
import com.gestion.deportiva.repository.SedeRepository;
import com.gestion.deportiva.service.SedeService;
import com.gestion.deportiva.util.SedeUtil;
import com.gestion.deportiva.util.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class SedeServiceImpl implements SedeService {

	private static final Logger logger = LoggerFactory.getLogger(SedeServiceImpl.class);

	@Autowired
	private SedeRepository sedeRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public SedeDTO findById(Long id) {
		logger.info("Buscando Sede por ID: {}", id);
		return SedeUtil.modelToDTO(sedeRepository.findByActivoTrueAndId(id));
	}

	@Override
	public SedeDTO findByUuid(String uuid) {
		logger.info("Buscando Sede por UUID: {}", uuid);
		return SedeUtil.modelToDTO(sedeRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	@CacheEvict(value = "sedes", allEntries = true)
	public Long guardar(SedeDTO dto) {
		logger.info("Guardando Sede");
		Sede model = sedeRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo Sede");
			model = new Sede();
		}
		model = SedeUtil.dtoToModel(dto, model);
		sedeRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public Page<SedeDTO> getPageByFilter(SedeFilter filter, Pageable pageable) {
		return SedeUtil.pageToPageDTO(sedeRepository.findAll(SedeSpecifications.filter(filter), pageable));
	}

	@Override
	@CacheEvict(value = "sedes", allEntries = true)
	public void eliminar(Long id) {
		logger.info("Eliminando Sede por ID: {}");
		Sede model = sedeRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		sedeRepository.saveAndFlush(model);
	}

	@Override
	@CacheEvict(value = "sedes", allEntries = true)
	public void eliminar(String uuid) {
		logger.info("Eliminando Sede por ID: {}");
		Sede model = sedeRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		sedeRepository.saveAndFlush(model);
	}

	@Override
	public SedeDTO findByNombreEqualsIgnoreCase(String nombre) {
		return SedeUtil.modelToDTO(sedeRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre));
	}

	@Override
	@Cacheable("sedes")
	public List<ComboDTO> getListComboDTO() {
		return SedeUtil.listModelToListComboDTO(sedeRepository.findByActivoTrue());
	}

	@Override
	public List<SedeDTO> getListDTO() {
		return Utils.sortByNombre(SedeUtil.listModelToListDTO(sedeRepository.findByActivoTrue()));
	}

	@Override
	public List<SedeDTO> getListDTO(SedeFilter filter) {
		return Utils.sortByNombre(
				SedeUtil.listModelToListDTO(sedeRepository.findAll(SedeSpecifications.filter(filter))));
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
	public byte[] exportarExcel(SedeFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}
}
