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
import com.gestion.deportiva.model.InstalacionConfiguracionReserva;
import com.gestion.deportiva.repository.InstalacionConfiguracionReservaRepository;
import com.gestion.deportiva.service.InstalacionConfiguracionReservaService;
import com.gestion.deportiva.util.InstalacionConfiguracionReservaUtil;
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

	@Override
	public InstalacionConfiguracionReservaDTO findById(Long id) {
		logger.info("Buscando InstalacionConfiguracionReserva por ID: {}", id);
		return InstalacionConfiguracionReservaUtil.modelToDTO(instalacionConfiguracionReservaRepository.findByActivoTrueAndId(id));
	}

	@Override
	public InstalacionConfiguracionReservaDTO findByUuid(String uuid) {
		logger.info("Buscando InstalacionConfiguracionReserva por UUID: {}", uuid);
		return InstalacionConfiguracionReservaUtil.modelToDTO(instalacionConfiguracionReservaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	public String guardar(InstalacionConfiguracionReservaDTO dto) {
		logger.info("Guardando InstalacionConfiguracionReserva");
		InstalacionConfiguracionReserva model = instalacionConfiguracionReservaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo InstalacionConfiguracionReserva");
			model = new InstalacionConfiguracionReserva();
		}
		model = InstalacionConfiguracionReservaUtil.dtoToModel(dto, model);
		instalacionConfiguracionReservaRepository.saveAndFlush(model);
		return model.getUuid();
	}

	@Override
	public Page<InstalacionConfiguracionReservaDTO> getPageByFilter(InstalacionConfiguracionReservaFilter filter, Pageable pageable) {
		return InstalacionConfiguracionReservaUtil.pageToPageDTO(instalacionConfiguracionReservaRepository.findAll(InstalacionConfiguracionReservaSpecifications.filter(filter), pageable));
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
		InstalacionConfiguracionReserva model = instalacionConfiguracionReservaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		instalacionConfiguracionReservaRepository.saveAndFlush(model);
	}

	@Override
	public List<InstalacionConfiguracionReservaDTO> getListDTO() {
		return InstalacionConfiguracionReservaUtil.listModelToListDTO(instalacionConfiguracionReservaRepository.findByActivoTrue());
	}

	@Override
	public List<InstalacionConfiguracionReservaDTO> getListDTO(InstalacionConfiguracionReservaFilter filter) {
		return InstalacionConfiguracionReservaUtil.listModelToListDTO(instalacionConfiguracionReservaRepository.findAll(InstalacionConfiguracionReservaSpecifications.filter(filter)));
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
}
