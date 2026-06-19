package com.gestion.deportiva.service.impl;

import java.util.ArrayList;
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
import com.gestion.deportiva.mapper.InstalacionMapper;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.repository.InstalacionRepository;
import com.gestion.deportiva.service.InstalacionService;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;
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

	@Autowired
	private InstalacionMapper instalacionMapper;

	@Override
	public InstalacionDTO findById(Long id) {
		logger.info("Buscando Instalacion por ID: {}", id);
		return instalacionMapper.modelToDTO(instalacionRepository.findByActivoTrueAndId(id));
	}

	@Override
	public InstalacionDTO findByUuid(String uuid) {
		logger.info("Buscando Instalacion por UUID: {}", uuid);
		return instalacionMapper.modelToDTO(instalacionRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	private InstalacionFilter limitacionesPermisos(InstalacionFilter filter) {
		if (SecurityUtil.hasAuthority(Constantes.Permiso.GESTION_GLOBAL)) {
			return filter;
		}

		if (SecurityUtil.hasAuthority(Constantes.Permiso.Localizacion.GESTION_EMPRESA)) {
			filter.setListEmpresaIds(SecurityUtil.getCurrentUserListEmpresaId());
		} else if (SecurityUtil.hasAuthority(Constantes.Permiso.Localizacion.GESTION_SEDE)) {
			filter.setListSedeIds(SecurityUtil.getCurrentUserListSedeId());
		} else if (SecurityUtil.hasAuthority(Constantes.Permiso.Localizacion.GESTION_INSTALACION)) {
			filter.setListInstalacionIds(SecurityUtil.getCurrentUserListInstalacionId());
		}

		else {
			filter.setListInstalacionIds(List.of(-1L));
		}
		return filter;
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
		model = instalacionMapper.dtoToModel(dto, model);
		instalacionRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public Page<InstalacionDTO> getPageByFilter(InstalacionFilter filter, Pageable pageable) {
		return instalacionMapper.pageToPageDTO(instalacionRepository
				.findAll(InstalacionSpecifications.filter(limitacionesPermisos(filter)), pageable));
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
		return instalacionMapper.modelToDTO(instalacionRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre));
	}

	@Override
	public List<ComboDTO> getListComboDTO() {
		return instalacionMapper.listModelToListComboDTO(instalacionRepository.findByActivoTrue());
	}

	@Override
	public List<InstalacionDTO> getListDTO() {
		return Utils.sortByNombre(instalacionMapper.listModelToListDTO(instalacionRepository.findByActivoTrue()));
	}

	@Override
	public List<InstalacionDTO> getListDTO(InstalacionFilter filter) {
		return Utils.sortByNombre(instalacionMapper.listModelToListDTO(
				instalacionRepository.findAll(InstalacionSpecifications.filter(limitacionesPermisos(filter)))));
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

	@Override
	public List<InstalacionDTO> getListDTOParaEmpleado(Long empresaId, Long sedeId) {
		List<InstalacionDTO> retVal = new ArrayList<>();
		if (SecurityUtil.hasAuthority(Constantes.Permiso.GESTION_GLOBAL)
				|| SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_GLOBAL)) {
			retVal = getListDTO();
		} else if (SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_EMPRESA)) {
			retVal = instalacionMapper.listModelToListDTO(instalacionRepository
					.findByActivoTrueAndSedeEmpresaIdIn(SecurityUtil.getCurrentUserListEmpresaId()));
		} else if (SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_SEDE)) {
			retVal = instalacionMapper.listModelToListDTO(instalacionRepository
					.findByActivoTrueAndSedeIdIn(SecurityUtil.getCurrentUserListSedeId()).stream().toList());
		} else if (SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_INSTALACION)) {
			retVal = instalacionMapper.listModelToListDTO(
					instalacionRepository.findByActivoTrueAndIdIn(SecurityUtil.getCurrentUserListInstalacionId()));
		}

		if (empresaId != null) {
			retVal.removeIf(s -> !s.getEmpresaId().equals(empresaId));
		}
		if (sedeId != null) {
			retVal.removeIf(s -> !s.getSedeId().equals(sedeId));
		}
		Utils.sortByCampo(retVal, InstalacionDTO::getEmpresaSedeInstalacionNombre);
		return Utils.addEmptyOption(retVal, InstalacionDTO.class);
	}
}
