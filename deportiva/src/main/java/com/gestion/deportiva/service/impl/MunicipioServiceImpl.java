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
import com.gestion.deportiva.dto.MunicipioDTO;
import com.gestion.deportiva.dto.filter.MunicipioFilter;
import com.gestion.deportiva.dto.specifications.MunicipioSpecifications;
import com.gestion.deportiva.model.Municipio;
import com.gestion.deportiva.repository.MunicipioRepository;
import com.gestion.deportiva.repository.ProvinciaRepository;
import com.gestion.deportiva.service.MunicipioService;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.MunicipioUtil;
import com.gestion.deportiva.util.SecurityUtil;
import com.gestion.deportiva.util.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class MunicipioServiceImpl implements MunicipioService {

	private static final Logger logger = LoggerFactory.getLogger(MunicipioServiceImpl.class);

	@Autowired
	private MunicipioRepository municipioRepository;

	@Autowired
	private ProvinciaRepository provinciaRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public MunicipioDTO findById(Long id) {
		logger.info("Buscando Municipio por ID: {}", id);
		return MunicipioUtil.modelToDTO(municipioRepository.findByActivoTrueAndId(id));
	}

	@Override
	public MunicipioDTO findByUuid(String uuid) {
		logger.info("Buscando Municipio por UUID: {}", uuid);
		return MunicipioUtil.modelToDTO(municipioRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	@CacheEvict(value = "municipios", allEntries = true)
	public String guardar(MunicipioDTO dto) {
		logger.info("Guardando Municipio");
		Municipio model = municipioRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo Municipio");
			model = new Municipio();
		}
		model = MunicipioUtil.dtoToModel(dto, model,
				provinciaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getProvinciaUuid()));
		municipioRepository.saveAndFlush(model);
		return model.getUuid();
	}

	@Override
	public Page<MunicipioDTO> getPageByFilter(MunicipioFilter filter, Pageable pageable) {
		return MunicipioUtil
				.pageToPageDTO(municipioRepository.findAll(MunicipioSpecifications.filter(filter), pageable));
	}

	@Override
	@CacheEvict(value = "municipios", allEntries = true)
	public void eliminar(Long id) {
		logger.info("Eliminando Municipio por ID: {}");
		Municipio model = municipioRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		municipioRepository.saveAndFlush(model);
	}

	@Override
	@CacheEvict(value = "municipios", allEntries = true)
	public void eliminar(String uuid) {
		logger.info("Eliminando Municipio por ID: {}");
		Municipio model = municipioRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		municipioRepository.saveAndFlush(model);
	}

	@Override
	public MunicipioDTO findByNombreEqualsIgnoreCase(String nombre) {
		return MunicipioUtil.modelToDTO(municipioRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre));
	}

	@Override
	@Cacheable("municipios")
	public List<ComboDTO> getListComboDTO() {
		return MunicipioUtil.listModelToListComboDTO(municipioRepository.findByActivoTrue());
	}

	@Override
	public List<MunicipioDTO> getListDTO() {
		return Utils.sortByNombre(MunicipioUtil.listModelToListDTO(municipioRepository.findByActivoTrue()));
	}

	@Override
	public List<MunicipioDTO> getListDTO(MunicipioFilter filter) {
		return Utils.sortByNombre(
				MunicipioUtil.listModelToListDTO(municipioRepository.findAll(MunicipioSpecifications.filter(filter))));
	}

	@Override
	public boolean canWrite(String uuid) {
		return SecurityUtil.hasAuthority(Constantes.Permiso.Municipio.WRITE);
	}

	@Override
	public boolean canRead(String uuid) {
		return SecurityUtil.hasAuthority(Constantes.Permiso.Municipio.READ);
	}

	@Override
	public byte[] exportarExcel(MunicipioFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

}
