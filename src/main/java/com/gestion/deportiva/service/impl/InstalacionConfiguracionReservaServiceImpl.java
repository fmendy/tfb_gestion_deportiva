package com.gestion.deportiva.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.dto.InstalacionConfiguracionReservaDTO;
import com.gestion.deportiva.dto.filter.InstalacionConfiguracionReservaFilter;
import com.gestion.deportiva.dto.specifications.InstalacionConfiguracionReservaSpecifications;
import com.gestion.deportiva.mapper.InstalacionConfiguracionReservaMapper;
import com.gestion.deportiva.model.InstalacionConfiguracionReserva;
import com.gestion.deportiva.repository.InstalacionConfiguracionReservaRepository;
import com.gestion.deportiva.service.InstalacionConfiguracionReservaService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class InstalacionConfiguracionReservaServiceImpl implements InstalacionConfiguracionReservaService {

	private static final Logger logger = LoggerFactory.getLogger(InstalacionConfiguracionReservaServiceImpl.class);

	@Autowired
	private InstalacionConfiguracionReservaRepository instalacionConfiguracionReservaRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private InstalacionConfiguracionReservaMapper instalacionConfiguracionReservaMapper;

	@Override
	public InstalacionConfiguracionReservaDTO findById(Long id) {
		logger.info("Buscando InstalacionConfiguracionReserva por ID: {}", id);
		return instalacionConfiguracionReservaMapper
				.modelToDTO(instalacionConfiguracionReservaRepository.findByActivoTrueAndId(id));
	}

	@Override
	public InstalacionConfiguracionReservaDTO findByUuid(String uuid) {
		logger.info("Buscando InstalacionConfiguracionReserva por UUID: {}", uuid);
		return instalacionConfiguracionReservaMapper
				.modelToDTO(instalacionConfiguracionReservaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	public Long guardar(InstalacionConfiguracionReservaDTO dto) {
		logger.info("Guardando InstalacionConfiguracionReserva");
		InstalacionConfiguracionReserva model = instalacionConfiguracionReservaRepository
				.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo InstalacionConfiguracionReserva");
			model = new InstalacionConfiguracionReserva();
		}
		model = instalacionConfiguracionReservaMapper.dtoToModel(dto, model);
		instalacionConfiguracionReservaRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public Page<InstalacionConfiguracionReservaDTO> getPageByFilter(InstalacionConfiguracionReservaFilter filter,
			Pageable pageable) {
		return instalacionConfiguracionReservaMapper.pageToPageDTO(instalacionConfiguracionReservaRepository
				.findAll(InstalacionConfiguracionReservaSpecifications.filter(filter), pageable));
	}

	@Override
	public void eliminar(Long id) {
		logger.info("Eliminando InstalacionConfiguracionReserva por ID: {}");
		InstalacionConfiguracionReserva model = instalacionConfiguracionReservaRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		instalacionConfiguracionReservaRepository.saveAndFlush(model);
	}

	@Override
	public void eliminar(String uuid) {
		logger.info("Eliminando InstalacionConfiguracionReserva por ID: {}");
		InstalacionConfiguracionReserva model = instalacionConfiguracionReservaRepository
				.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		instalacionConfiguracionReservaRepository.saveAndFlush(model);
	}

	@Override
	public List<InstalacionConfiguracionReservaDTO> getListDTO() {
		return instalacionConfiguracionReservaMapper
				.listModelToListDTO(instalacionConfiguracionReservaRepository.findByActivoTrue());
	}

	@Override
	public List<InstalacionConfiguracionReservaDTO> getListDTO(InstalacionConfiguracionReservaFilter filter) {
		return instalacionConfiguracionReservaMapper.listModelToListDTO(instalacionConfiguracionReservaRepository
				.findAll(InstalacionConfiguracionReservaSpecifications.filter(filter)));
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
	public byte[] exportarExcel(InstalacionConfiguracionReservaFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InstalacionConfiguracionReservaDTO findDTOByInstalacionId(Long instalacionId) {
		logger.info("Buscando InstalacionConfiguracionReserva por instalacionID: {}", instalacionId);
		return instalacionConfiguracionReservaMapper
				.modelToDTO(instalacionConfiguracionReservaRepository.findByActivoTrueAndInstalacionId(instalacionId));
	}

	@Override
	public InstalacionConfiguracionReserva findByInstalacionId(Long instalacionId) {
		logger.info("Buscando InstalacionConfiguracionReserva por instalacionID: {}", instalacionId);
		return instalacionConfiguracionReservaRepository.findByActivoTrueAndInstalacionId(instalacionId);
	}

	@Override
	public InstalacionConfiguracionReservaDTO findDTOByInstalacionIdOrNewIfEmpty(Long instalacionId) {
		logger.info("Buscando InstalacionConfiguracionReserva por instalacionID: {}", instalacionId);

		InstalacionConfiguracionReserva bean = findByInstalacionId(instalacionId);
		if (bean == null) {
			InstalacionConfiguracionReservaDTO retVal = new InstalacionConfiguracionReservaDTO();
			retVal.setInstalacionId(instalacionId);
			return retVal;
		}
		return instalacionConfiguracionReservaMapper.modelToDTO(bean);
	}

}
