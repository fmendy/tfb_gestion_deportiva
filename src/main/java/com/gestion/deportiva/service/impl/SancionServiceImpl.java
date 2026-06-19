package com.gestion.deportiva.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.dto.SancionDTO;
import com.gestion.deportiva.dto.filter.SancionFilter;
import com.gestion.deportiva.dto.specifications.SancionSpecifications;
import com.gestion.deportiva.mapper.SancionMapper;
import com.gestion.deportiva.model.Sancion;
import com.gestion.deportiva.repository.SancionRepository;
import com.gestion.deportiva.service.SancionService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class SancionServiceImpl implements SancionService {

	private static final Logger logger = LoggerFactory.getLogger(SancionServiceImpl.class);

	@Autowired
	private SancionRepository sancionRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SancionMapper sancionMapper;

	@Override
	public SancionDTO findById(Long id) {
		logger.info("Buscando Sancion por ID: {}", id);
		return sancionMapper.modelToDTO(sancionRepository.findByActivoTrueAndId(id));
	}

	@Override
	public SancionDTO findByUuid(String uuid) {
		logger.info("Buscando Sancion por UUID: {}", uuid);
		return sancionMapper.modelToDTO(sancionRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	public Long guardar(SancionDTO dto) {
		logger.info("Guardando Sancion");
		Sancion model = sancionRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo Sancion");
			model = new Sancion();
		}
		model = sancionMapper.dtoToModel(dto, model);
		sancionRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public Page<SancionDTO> getPageByFilter(SancionFilter filter, Pageable pageable) {
		return sancionMapper.pageToPageDTO(sancionRepository.findAll(SancionSpecifications.filter(filter), pageable));
	}

	@Override
	public void eliminar(Long id) {
		logger.info("Eliminando Sancion por ID: {}");
		Sancion model = sancionRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		sancionRepository.saveAndFlush(model);
	}

	@Override
	public void eliminar(String uuid) {
		logger.info("Eliminando Sancion por ID: {}");
		Sancion model = sancionRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		sancionRepository.saveAndFlush(model);
	}

	@Override
	public List<SancionDTO> getListDTO() {
		return sancionMapper.listModelToListDTO(sancionRepository.findByActivoTrue());
	}

	@Override
	public List<SancionDTO> getListDTO(SancionFilter filter) {
		return sancionMapper.listModelToListDTO(sancionRepository.findAll(SancionSpecifications.filter(filter)));
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
	public byte[] exportarExcel(SancionFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}
}
