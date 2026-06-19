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
import com.gestion.deportiva.dto.ReservaEstadoDTO;
import com.gestion.deportiva.dto.filter.ReservaEstadoFilter;
import com.gestion.deportiva.dto.specifications.ReservaEstadoSpecifications;
import com.gestion.deportiva.mapper.ReservaEstadoMapper;
import com.gestion.deportiva.model.ReservaEstado;
import com.gestion.deportiva.repository.ReservaEstadoRepository;
import com.gestion.deportiva.service.ReservaEstadoService;
import com.gestion.deportiva.util.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class ReservaEstadoServiceImpl implements ReservaEstadoService {

	private static final Logger logger = LoggerFactory.getLogger(ReservaEstadoServiceImpl.class);

	@Autowired
	private ReservaEstadoRepository reservaEstadoRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ReservaEstadoMapper reservaEstadoMapper;

	@Override
	public ReservaEstadoDTO findById(Long id) {
		logger.info("Buscando ReservaEstado por ID: {}", id);
		return reservaEstadoMapper.modelToDTO(reservaEstadoRepository.findByActivoTrueAndId(id));
	}

	@Override
	public ReservaEstadoDTO findByUuid(String uuid) {
		logger.info("Buscando ReservaEstado por UUID: {}", uuid);
		return reservaEstadoMapper.modelToDTO(reservaEstadoRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	@CacheEvict(value = "comunidades", allEntries = true)
	public Long guardar(ReservaEstadoDTO dto) {
		logger.info("Guardando ReservaEstado");
		ReservaEstado model = reservaEstadoRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo ReservaEstado");
			model = new ReservaEstado();
		}
		model = reservaEstadoMapper.dtoToModel(dto, model);
		reservaEstadoRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public Page<ReservaEstadoDTO> getPageByFilter(ReservaEstadoFilter filter, Pageable pageable) {
		return reservaEstadoMapper
				.pageToPageDTO(reservaEstadoRepository.findAll(ReservaEstadoSpecifications.filter(filter), pageable));
	}

	@Override
	@CacheEvict(value = "comunidades", allEntries = true)
	public void eliminar(Long id) {
		logger.info("Eliminando ReservaEstado por ID: {}");
		ReservaEstado model = reservaEstadoRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		reservaEstadoRepository.saveAndFlush(model);
	}

	@Override
	@CacheEvict(value = "comunidades", allEntries = true)
	public void eliminar(String uuid) {
		logger.info("Eliminando ReservaEstado por ID: {}");
		ReservaEstado model = reservaEstadoRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		reservaEstadoRepository.saveAndFlush(model);
	}

	@Override
	public ReservaEstadoDTO findByNombreEqualsIgnoreCase(String nombre) {
		return reservaEstadoMapper
				.modelToDTO(reservaEstadoRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre));
	}

	@Override
	@Cacheable("comunidades")
	public List<ComboDTO> getListComboDTO() {
		return reservaEstadoMapper.listModelToListComboDTO(reservaEstadoRepository.findByActivoTrue());
	}

	@Override
	public List<ReservaEstadoDTO> getListDTO() {
		return Utils.sortByNombre(reservaEstadoMapper.listModelToListDTO(reservaEstadoRepository.findByActivoTrue()));
	}

	@Override
	public List<ReservaEstadoDTO> getListDTO(ReservaEstadoFilter filter) {
		return Utils.sortByNombre(reservaEstadoMapper
				.listModelToListDTO(reservaEstadoRepository.findAll(ReservaEstadoSpecifications.filter(filter))));
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
	public byte[] exportarExcel(ReservaEstadoFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}
}
