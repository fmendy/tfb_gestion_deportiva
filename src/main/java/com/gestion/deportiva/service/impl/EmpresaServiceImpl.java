package com.gestion.deportiva.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.EmpresaDTO;
import com.gestion.deportiva.dto.RegistroEmpresaDTO;
import com.gestion.deportiva.dto.filter.EmpresaFilter;
import com.gestion.deportiva.dto.specifications.EmpresaSpecifications;
import com.gestion.deportiva.mapper.EmpresaMapper;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.repository.EmpresaRepository;
import com.gestion.deportiva.service.EmpresaService;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;
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

	@Autowired
	private EmpresaMapper empresaMapper;

	@Override
	public EmpresaDTO findById(Long id) {
		logger.info("Buscando Empresa por ID: {}", id);
		return empresaMapper.modelToDTO(empresaRepository.findByActivoTrueAndId(id));
	}

	@Override
	public EmpresaDTO findByUuid(String uuid) {
		logger.info("Buscando Empresa por UUID: {}", uuid);
		return empresaMapper.modelToDTO(empresaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	public Long guardar(EmpresaDTO dto) {
		logger.info("Guardando Empresa");
		Empresa model = empresaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo Empresa");
			model = new Empresa();
		}
		model = empresaMapper.dtoToModel(dto, model);
		empresaRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public Page<EmpresaDTO> getPageByFilter(EmpresaFilter filter, Pageable pageable) {
		return empresaMapper.pageToPageDTO(
				empresaRepository.findAll(EmpresaSpecifications.filter(limitacionesPermisos(filter)), pageable));
	}

	private EmpresaFilter limitacionesPermisos(EmpresaFilter filter) {
		if (SecurityUtil.hasAuthority(Constantes.Permiso.GESTION_GLOBAL)) {
			return filter;
		}

		if (SecurityUtil.hasAuthority(Constantes.Permiso.Localizacion.GESTION_EMPRESA)) {
			filter.setListIds(SecurityUtil.getCurrentUserListEmpresaId());
		} else {
			filter.setListIds(List.of(-1L));
		}
		return filter;
	}

	@Override
	public void eliminar(Long id) {
		logger.info("Eliminando Empresa por ID: {}");
		Empresa model = empresaRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		empresaRepository.saveAndFlush(model);
	}

	@Override
	public void eliminar(String uuid) {
		logger.info("Eliminando Empresa por ID: {}");
		Empresa model = empresaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		empresaRepository.saveAndFlush(model);
	}

	@Override
	public EmpresaDTO findByNombreEqualsIgnoreCase(String nombre) {
		return empresaMapper.modelToDTO(empresaRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre));
	}

	@Override
	public List<ComboDTO> getListComboDTO() {
		return empresaMapper.listModelToListComboDTO(empresaRepository.findByActivoTrue());
	}

	@Override
	public List<EmpresaDTO> getListDTO() {
		return Utils.sortByNombre(empresaMapper.listModelToListDTO(empresaRepository.findByActivoTrue()));
	}

	@Override
	public List<EmpresaDTO> getListDTO(EmpresaFilter filter) {
		return Utils.sortByNombre(empresaMapper.listModelToListDTO(
				empresaRepository.findAll(EmpresaSpecifications.filter(limitacionesPermisos(filter)))));
	}

	@Override
	public boolean canWrite(Long id) {
		return (SecurityUtil.hasAuthority(Constantes.Permiso.GESTION_GLOBAL)
				|| (SecurityUtil.hasAuthority(Constantes.Permiso.Localizacion.GESTION_EMPRESA)
						&& SecurityUtil.getCurrentUserListEmpresaId().contains(id)));
	}

	@Override
	public boolean canRead(Long id) {
		return (SecurityUtil.hasAuthority(Constantes.Permiso.GESTION_GLOBAL)
				|| (SecurityUtil.hasAuthority(Constantes.Permiso.Localizacion.GESTION_EMPRESA)
						&& SecurityUtil.getCurrentUserListEmpresaId().contains(id)));
	}

	@Override
	public byte[] exportarExcel(EmpresaFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long registrarEmpresa(RegistroEmpresaDTO dto) {
		logger.info("Registrando nueva empresa nombre: {}", dto.getNombre());
		Empresa model = empresaMapper.registroEmpresaDTOToModel(dto);
		model = empresaRepository.saveAndFlush(model);
		return model.getId();
	}
}
