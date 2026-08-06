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
import com.gestion.deportiva.model.Reserva;
import com.gestion.deportiva.model.Sancion;
import com.gestion.deportiva.repository.ReservaRepository;
import com.gestion.deportiva.repository.SancionRepository;
import com.gestion.deportiva.service.ReservaService;
import com.gestion.deportiva.service.SancionService;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;

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

	@Autowired
	private ReservaRepository reservaRepository;

	@Autowired
	private ReservaService reservaService;

	@Override
	public SancionDTO findById(Long id) {
		logger.info("Buscando Sancion por ID: {}", id);
		return sancionMapper.modelToDTO(sancionRepository.findByActivoTrueAndId(id));
	}

	@Override
	public SancionDTO findByDTO(SancionDTO dto) {
		logger.info("Buscando Sancion por ID: {}", dto.getId());
		if (dto.getId() != null) {
			return sancionMapper.modelToDTO(sancionRepository.findByActivoTrueAndId(dto.getId()));
		}
		Reserva reserva = reservaRepository.findByActivoTrueAndId(dto.getReservaId());
		return sancionMapper.dtoAndReservaToDTO(dto, reserva);
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
		reservaService.cancelarSancion(dto.getUsuarioId(), dto.getFechaInicio(), dto.getFechaFin());
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
	public List<SancionDTO> getListDTO() {
		return sancionMapper.listModelToListDTO(sancionRepository.findByActivoTrue());
	}

	@Override
	public List<SancionDTO> getListDTO(SancionFilter filter) {
		return sancionMapper.listModelToListDTO(sancionRepository.findAll(SancionSpecifications.filter(filter)));
	}

	@Override
	public boolean canWrite(Long id) {
		if (SecurityUtil.hasAnyAuthority(Constantes.Permiso.GESTION_GLOBAL,
				Constantes.Permiso.Sancion.GESTION_SANCION_GLOBAL)) {
			return true;
		}
		Sancion sancion = sancionRepository.findByActivoTrueAndId(id);
		if (sancion == null && (SecurityUtil.hasAnyAuthority(Constantes.Permiso.Sancion.GESTION_SANCION_EMPRESA,
				Constantes.Permiso.Sancion.GESTION_SANCION_SEDE,
				Constantes.Permiso.Sancion.GESTION_SANCION_INSTALACION))) {
			return true;
		}
		if (sancion == null) {
			return false;
		}
		if (SecurityUtil.hasAuthority(Constantes.Permiso.Sancion.GESTION_SANCION_EMPRESA)) {
			return SecurityUtil.getCurrentUserListEmpresaId()
					.contains(sancion.getReserva().getInstalacion().getSede().getEmpresa().getId());
		}
		if (SecurityUtil.hasAuthority(Constantes.Permiso.Sancion.GESTION_SANCION_SEDE)) {
			return SecurityUtil.getCurrentUserListEmpresaId()
					.contains(sancion.getReserva().getInstalacion().getSede().getId());
		}
		if (SecurityUtil.hasAuthority(Constantes.Permiso.Sancion.GESTION_SANCION_INSTALACION)) {
			return SecurityUtil.getCurrentUserListEmpresaId().contains(sancion.getReserva().getInstalacion().getId());
		}
		return false;
	}

	@Override
	public boolean canRead(Long id) {
		return canWrite(id);
	}

}
