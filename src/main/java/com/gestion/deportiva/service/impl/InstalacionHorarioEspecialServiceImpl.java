package com.gestion.deportiva.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.dto.InstalacionHorarioEspecialDTO;
import com.gestion.deportiva.dto.filter.InstalacionHorarioEspecialFilter;
import com.gestion.deportiva.dto.specifications.InstalacionHorarioEspecialSpecifications;
import com.gestion.deportiva.mapper.InstalacionHorarioEspecialMapper;
import com.gestion.deportiva.model.InstalacionHorarioEspecial;
import com.gestion.deportiva.repository.InstalacionHorarioEspecialRepository;
import com.gestion.deportiva.service.InstalacionHorarioEspecialService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class InstalacionHorarioEspecialServiceImpl implements InstalacionHorarioEspecialService {

	private static final Logger logger = LoggerFactory.getLogger(InstalacionHorarioEspecialServiceImpl.class);

	@Autowired
	private InstalacionHorarioEspecialRepository instalacionHorarioEspecialRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private InstalacionHorarioEspecialMapper instalacionHorarioEspecialMapper;

	@Override
	public InstalacionHorarioEspecialDTO findById(Long id) {
		logger.info("Buscando InstalacionHorarioEspecial por ID: {}", id);
		return instalacionHorarioEspecialMapper
				.modelToDTO(instalacionHorarioEspecialRepository.findByActivoTrueAndId(id));
	}

	@Override
	public InstalacionHorarioEspecialDTO findByUuid(String uuid) {
		logger.info("Buscando InstalacionHorarioEspecial por UUID: {}", uuid);
		return instalacionHorarioEspecialMapper
				.modelToDTO(instalacionHorarioEspecialRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	public Long guardar(InstalacionHorarioEspecialDTO dto) {
		logger.info("Guardando InstalacionHorarioEspecial");
		InstalacionHorarioEspecial model = instalacionHorarioEspecialRepository
				.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo InstalacionHorarioEspecial");
			model = new InstalacionHorarioEspecial();
		}
		model = instalacionHorarioEspecialMapper.dtoToModel(dto, model);
		instalacionHorarioEspecialRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public Page<InstalacionHorarioEspecialDTO> getPageByFilter(InstalacionHorarioEspecialFilter filter,
			Pageable pageable) {
		return instalacionHorarioEspecialMapper.pageToPageDTO(instalacionHorarioEspecialRepository
				.findAll(InstalacionHorarioEspecialSpecifications.filter(filter), pageable));
	}

	@Override
	public void eliminar(Long id) {
		logger.info("Eliminando InstalacionHorarioEspecial por ID: {}");
		InstalacionHorarioEspecial model = instalacionHorarioEspecialRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		instalacionHorarioEspecialRepository.saveAndFlush(model);
	}

	@Override
	public void eliminar(String uuid) {
		logger.info("Eliminando InstalacionHorarioEspecial por ID: {}");
		InstalacionHorarioEspecial model = instalacionHorarioEspecialRepository
				.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		instalacionHorarioEspecialRepository.saveAndFlush(model);
	}

	@Override
	public List<InstalacionHorarioEspecialDTO> getListDTO() {
		return instalacionHorarioEspecialMapper
				.listModelToListDTO(instalacionHorarioEspecialRepository.findByActivoTrue());
	}

	@Override
	public List<InstalacionHorarioEspecialDTO> getListDTO(InstalacionHorarioEspecialFilter filter) {
		return instalacionHorarioEspecialMapper.listModelToListDTO(
				instalacionHorarioEspecialRepository.findAll(InstalacionHorarioEspecialSpecifications.filter(filter)));
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
	public byte[] exportarExcel(InstalacionHorarioEspecialFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InstalacionHorarioEspecialDTO findByIdOrNewEmpty(Long id, Long instalacionId) {
		InstalacionHorarioEspecialDTO retVal = null;
		if (id != null) {
			retVal = findById(instalacionId);
		}
		if (retVal == null) {
			retVal = new InstalacionHorarioEspecialDTO();
			retVal.setInstalacionId(instalacionId);
		}
		return retVal;
	}
}
