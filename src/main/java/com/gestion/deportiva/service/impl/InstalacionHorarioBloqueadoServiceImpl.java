package com.gestion.deportiva.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.dto.InstalacionHorarioBloqueadoDTO;
import com.gestion.deportiva.dto.filter.InstalacionHorarioBloqueadoFilter;
import com.gestion.deportiva.dto.specifications.InstalacionHorarioBloqueadoSpecifications;
import com.gestion.deportiva.mapper.InstalacionHorarioBloqueadoMapper;
import com.gestion.deportiva.model.InstalacionHorarioBloqueado;
import com.gestion.deportiva.repository.InstalacionHorarioBloqueadoRepository;
import com.gestion.deportiva.service.InstalacionHorarioBloqueadoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class InstalacionHorarioBloqueadoServiceImpl implements InstalacionHorarioBloqueadoService {

	private static final Logger logger = LoggerFactory.getLogger(InstalacionHorarioBloqueadoServiceImpl.class);

	@Autowired
	private InstalacionHorarioBloqueadoRepository instalacionHorarioBloqueadoRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private InstalacionHorarioBloqueadoMapper instalacionHorarioBloqueadoMapper;

	@Override
	public InstalacionHorarioBloqueadoDTO findById(Long id) {
		logger.info("Buscando InstalacionHorarioBloqueado por ID: {}", id);
		return instalacionHorarioBloqueadoMapper
				.modelToDTO(instalacionHorarioBloqueadoRepository.findByActivoTrueAndId(id));
	}

	@Override
	public InstalacionHorarioBloqueadoDTO findByUuid(String uuid) {
		logger.info("Buscando InstalacionHorarioBloqueado por UUID: {}", uuid);
		return instalacionHorarioBloqueadoMapper
				.modelToDTO(instalacionHorarioBloqueadoRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	public Long guardar(InstalacionHorarioBloqueadoDTO dto) {
		logger.info("Guardando InstalacionHorarioBloqueado");
		InstalacionHorarioBloqueado model = instalacionHorarioBloqueadoRepository
				.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo InstalacionHorarioBloqueado");
			model = new InstalacionHorarioBloqueado();
		}
		model = instalacionHorarioBloqueadoMapper.dtoToModel(dto, model);
		instalacionHorarioBloqueadoRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public Page<InstalacionHorarioBloqueadoDTO> getPageByFilter(InstalacionHorarioBloqueadoFilter filter,
			Pageable pageable) {
		return instalacionHorarioBloqueadoMapper.pageToPageDTO(instalacionHorarioBloqueadoRepository
				.findAll(InstalacionHorarioBloqueadoSpecifications.filter(filter), pageable));
	}

	@Override
	public void eliminar(Long id) {
		logger.info("Eliminando InstalacionHorarioBloqueado por ID: {}");
		InstalacionHorarioBloqueado model = instalacionHorarioBloqueadoRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		instalacionHorarioBloqueadoRepository.saveAndFlush(model);
	}

	@Override
	public void eliminar(String uuid) {
		logger.info("Eliminando InstalacionHorarioBloqueado por ID: {}");
		InstalacionHorarioBloqueado model = instalacionHorarioBloqueadoRepository
				.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		instalacionHorarioBloqueadoRepository.saveAndFlush(model);
	}

	@Override
	public List<InstalacionHorarioBloqueadoDTO> getListDTO() {
		return instalacionHorarioBloqueadoMapper
				.listModelToListDTO(instalacionHorarioBloqueadoRepository.findByActivoTrue());
	}

	@Override
	public List<InstalacionHorarioBloqueadoDTO> getListDTO(InstalacionHorarioBloqueadoFilter filter) {
		return instalacionHorarioBloqueadoMapper.listModelToListDTO(instalacionHorarioBloqueadoRepository
				.findAll(InstalacionHorarioBloqueadoSpecifications.filter(filter)));
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
	public byte[] exportarExcel(InstalacionHorarioBloqueadoFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InstalacionHorarioBloqueadoDTO findByIdOrNewEmpty(Long id, Long instalacionId) {
		InstalacionHorarioBloqueadoDTO retVal = null;
		if (id != null) {
			retVal = findById(instalacionId);
		}
		if (retVal == null) {
			retVal = new InstalacionHorarioBloqueadoDTO();
			retVal.setInstalacionId(instalacionId);
		}
		return retVal;
	}
}
