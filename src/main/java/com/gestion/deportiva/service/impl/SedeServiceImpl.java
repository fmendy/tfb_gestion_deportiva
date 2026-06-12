package com.gestion.deportiva.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.SedeDTO;
import com.gestion.deportiva.dto.filter.SedeFilter;
import com.gestion.deportiva.dto.specifications.SedeSpecifications;
import com.gestion.deportiva.mapper.SedeMapper;
import com.gestion.deportiva.model.Sede;
import com.gestion.deportiva.repository.SedeRepository;
import com.gestion.deportiva.service.SedeService;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;
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

	@Autowired
	private SedeMapper sedeMapper;

	@Override
	public SedeDTO findById(Long id) {
		logger.info("Buscando Sede por ID: {}", id);
		return sedeMapper.modelToDTO(sedeRepository.findByActivoTrueAndId(id));
	}

	@Override
	public SedeDTO findByUuid(String uuid) {
		logger.info("Buscando Sede por UUID: {}", uuid);
		return sedeMapper.modelToDTO(sedeRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	public Long guardar(SedeDTO dto) {
		logger.info("Guardando Sede");
		Sede model = sedeRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo Sede");
			model = new Sede();
		}
		model = sedeMapper.dtoToModel(dto, model);
		sedeRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public Page<SedeDTO> getPageByFilter(SedeFilter filter, Pageable pageable) {
		return sedeMapper.pageToPageDTO(sedeRepository.findAll(SedeSpecifications.filter(filter), pageable));
	}

	@Override
	public void eliminar(Long id) {
		logger.info("Eliminando Sede por ID: {}");
		Sede model = sedeRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		sedeRepository.saveAndFlush(model);
	}

	@Override
	public void eliminar(String uuid) {
		logger.info("Eliminando Sede por ID: {}");
		Sede model = sedeRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		sedeRepository.saveAndFlush(model);
	}

	@Override
	public SedeDTO findByNombreEqualsIgnoreCase(String nombre) {
		return sedeMapper.modelToDTO(sedeRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre));
	}

	@Override
	public List<ComboDTO> getListComboDTO() {
		return sedeMapper.listModelToListComboDTO(sedeRepository.findByActivoTrue());
	}

	@Override
	public List<SedeDTO> getListDTO() {
		return Utils.sortByNombre(sedeMapper.listModelToListDTO(sedeRepository.findByActivoTrue()));
	}

	@Override
	public List<SedeDTO> getListDTO(SedeFilter filter) {
		return Utils
				.sortByNombre(sedeMapper.listModelToListDTO(sedeRepository.findAll(SedeSpecifications.filter(filter))));
	}

	@Override
	public boolean canWrite(Long id) {
		if (id == null) {
			return false;
		}
		if (SecurityUtil.hasAuthority(Constantes.Permiso.GESTION_GLOBAL)) {
			return true;
		}
		if (SecurityUtil.hasAuthority(Constantes.Permiso.Localizacion.GESTION_EMPRESA)) {
			Sede sede = sedeRepository.findByActivoTrueAndId(id);
			if (sede != null) {
				return SecurityUtil.getCurrentUserListEmpresaId().contains(sede.getEmpresa().getId());
			}

		}
		if (SecurityUtil.hasAuthority(Constantes.Permiso.Localizacion.GESTION_SEDE)) {
			return SecurityUtil.getCurrentUserListSedeId().contains(id);
		}
		return false;
	}

	@Override
	public boolean canRead(Long id) {
		return canWrite(id);
	}

	@Override
	public byte[] exportarExcel(SedeFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}
}
