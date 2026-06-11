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
import com.gestion.deportiva.dto.EmpresaDTO;
import com.gestion.deportiva.dto.filter.EmpresaFilter;
import com.gestion.deportiva.dto.specifications.EmpresaSpecifications;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.repository.EmpresaRepository;
import com.gestion.deportiva.service.EmpresaService;
import com.gestion.deportiva.util.EmpresaUtil;
import com.gestion.deportiva.util.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class EmpresaServiceImpl implements EmpresaService {

	private static final Logger logger = LoggerFactory.getLogger(EmpresaServiceImpl.class);

	@Autowired
	private EmpresaRepository empresaRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public EmpresaDTO findById(Long id) {
		logger.info("Buscando Empresa por ID: {}", id);
		return EmpresaUtil.modelToDTO(empresaRepository.findByActivoTrueAndId(id));
	}

	@Override
	public EmpresaDTO findByUuid(String uuid) {
		logger.info("Buscando Empresa por UUID: {}", uuid);
		return EmpresaUtil.modelToDTO(empresaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	@CacheEvict(value = "empresas", allEntries = true)
	public String guardar(EmpresaDTO dto) {
		logger.info("Guardando Empresa");
		Empresa model = empresaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo Empresa");
			model = new Empresa();
		}
		model = EmpresaUtil.dtoToModel(dto, model);
		empresaRepository.saveAndFlush(model);
		return model.getUuid();
	}

	@Override
	public Page<EmpresaDTO> getPageByFilter(EmpresaFilter filter, Pageable pageable) {
		return EmpresaUtil.pageToPageDTO(empresaRepository.findAll(EmpresaSpecifications.filter(filter), pageable));
	}

	@Override
	@CacheEvict(value = "empresas", allEntries = true)
	public void eliminar(Long id) {
		logger.info("Eliminando Empresa por ID: {}");
		Empresa model = empresaRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		empresaRepository.saveAndFlush(model);
	}

	@Override
	@CacheEvict(value = "empresas", allEntries = true)
	public void eliminar(String uuid) {
		logger.info("Eliminando Empresa por ID: {}");
		Empresa model = empresaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		empresaRepository.saveAndFlush(model);
	}

	@Override
	public EmpresaDTO findByNombreEqualsIgnoreCase(String nombre) {
		return EmpresaUtil.modelToDTO(empresaRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre));
	}

	@Override
	@Cacheable("empresas")
	public List<ComboDTO> getListComboDTO() {
		return EmpresaUtil.listModelToListComboDTO(empresaRepository.findByActivoTrue());
	}

	@Override
	public List<EmpresaDTO> getListDTO() {
		return Utils.sortByNombre(EmpresaUtil.listModelToListDTO(empresaRepository.findByActivoTrue()));
	}

	@Override
	public List<EmpresaDTO> getListDTO(EmpresaFilter filter) {
		return Utils.sortByNombre(
				EmpresaUtil.listModelToListDTO(empresaRepository.findAll(EmpresaSpecifications.filter(filter))));
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
	public byte[] exportarExcel(EmpresaFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}
}
