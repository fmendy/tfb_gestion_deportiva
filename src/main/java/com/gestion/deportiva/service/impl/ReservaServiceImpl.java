package com.gestion.deportiva.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.dto.ReservaDTO;
import com.gestion.deportiva.dto.ReservaInstalacionDTO;
import com.gestion.deportiva.dto.filter.ReservaFilter;
import com.gestion.deportiva.dto.specifications.ReservaSpecifications;
import com.gestion.deportiva.mapper.ReservaMapper;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.Reserva;
import com.gestion.deportiva.repository.InstalacionRepository;
import com.gestion.deportiva.repository.ReservaRepository;
import com.gestion.deportiva.service.ReservaService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class ReservaServiceImpl implements ReservaService {

	private static final Logger logger = LoggerFactory.getLogger(ReservaServiceImpl.class);

	@Autowired
	private ReservaRepository reservaRepository;

	@Autowired
	private InstalacionRepository instalacionRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ReservaMapper reservaMapper;

	@Override
	public ReservaDTO findById(Long id) {
		logger.info("Buscando Reserva por ID: {}", id);
		return reservaMapper.modelToDTO(reservaRepository.findByActivoTrueAndId(id));
	}

	@Override
	public ReservaDTO findByUuid(String uuid) {
		logger.info("Buscando Reserva por UUID: {}", uuid);
		return reservaMapper.modelToDTO(reservaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	public Long guardar(ReservaDTO dto) {
		logger.info("Guardando Reserva");
		Reserva model = reservaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo Reserva");
			model = new Reserva();
		}
		model = reservaMapper.dtoToModel(dto, model);
		reservaRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public Page<ReservaDTO> getPageByFilter(ReservaFilter filter, Pageable pageable) {
		return reservaMapper.pageToPageDTO(reservaRepository.findAll(ReservaSpecifications.filter(filter), pageable));
	}

	@Override
	public void eliminar(Long id) {
		logger.info("Eliminando Reserva por ID: {}");
		Reserva model = reservaRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		reservaRepository.saveAndFlush(model);
	}

	@Override
	public void eliminar(String uuid) {
		logger.info("Eliminando Reserva por ID: {}");
		Reserva model = reservaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		reservaRepository.saveAndFlush(model);
	}

	@Override
	public List<ReservaDTO> getListDTO() {
		return reservaMapper.listModelToListDTO(reservaRepository.findByActivoTrue());
	}

	@Override
	public List<ReservaDTO> getListDTO(ReservaFilter filter) {
		return reservaMapper.listModelToListDTO(reservaRepository.findAll(ReservaSpecifications.filter(filter)));
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
	public byte[] exportarExcel(ReservaFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReservaInstalacionDTO getReservaInstalacionDTOByInstalacionIdAndReservaInstalacionDTO(Long instalacionId,
			ReservaInstalacionDTO dto) {
		Instalacion instalacion = instalacionRepository.findByActivoTrueAndId(instalacionId);

		return reservaMapper.instalacionModelToReservaInstalacionDTO(instalacion, dto);
	}
}
