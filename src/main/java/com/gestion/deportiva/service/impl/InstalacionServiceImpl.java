package com.gestion.deportiva.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.InstalacionDTO;
import com.gestion.deportiva.dto.filter.InstalacionFilter;
import com.gestion.deportiva.dto.specifications.InstalacionSpecifications;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.repository.InstalacionRepository;
import com.gestion.deportiva.service.InstalacionService;
import com.gestion.deportiva.util.InstalacionUtil;
import com.gestion.deportiva.util.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class InstalacionServiceImpl implements InstalacionService {

	private static final Logger logger = LoggerFactory.getLogger(InstalacionServiceImpl.class);

	@Autowired
	private InstalacionRepository instalacionRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public InstalacionDTO findById(Long id) {
		logger.info("Buscando Instalacion por ID: {}", id);
		return InstalacionUtil.modelToDTO(instalacionRepository.findByActivoTrueAndId(id));
	}

	@Override
	public InstalacionDTO findByUuid(String uuid) {
		logger.info("Buscando Instalacion por UUID: {}", uuid);
		return InstalacionUtil.modelToDTO(instalacionRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	public Long guardar(InstalacionDTO dto) {
		logger.info("Guardando Instalacion");
		Instalacion model = instalacionRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo Instalacion");
			model = new Instalacion();
		}
		model = InstalacionUtil.dtoToModel(dto, model);
		instalacionRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public Page<InstalacionDTO> getPageByFilter(InstalacionFilter filter, Pageable pageable) {
		return InstalacionUtil
				.pageToPageDTO(instalacionRepository.findAll(InstalacionSpecifications.filter(filter), pageable));
	}

	@Override
	public void eliminar(Long id) {
		logger.info("Eliminando Instalacion por ID: {}");
		Instalacion model = instalacionRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		instalacionRepository.saveAndFlush(model);
	}

	@Override
	public void eliminar(String uuid) {
		logger.info("Eliminando Instalacion por ID: {}");
		Instalacion model = instalacionRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		instalacionRepository.saveAndFlush(model);
	}

	@Override
	public InstalacionDTO findByNombreEqualsIgnoreCase(String nombre) {
		return InstalacionUtil.modelToDTO(instalacionRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre));
	}

	@Override
	public List<ComboDTO> getListComboDTO() {
		return InstalacionUtil.listModelToListComboDTO(instalacionRepository.findByActivoTrue());
	}

	@Override
	public List<InstalacionDTO> getListDTO() {
		return Utils.sortByNombre(InstalacionUtil.listModelToListDTO(instalacionRepository.findByActivoTrue()));
	}

	@Override
	public List<InstalacionDTO> getListDTO(InstalacionFilter filter) {
		return Utils.sortByNombre(InstalacionUtil
				.listModelToListDTO(instalacionRepository.findAll(InstalacionSpecifications.filter(filter))));
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
	public byte[] exportarExcel(InstalacionFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}
}
