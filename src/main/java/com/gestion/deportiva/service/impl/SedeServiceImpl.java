package com.gestion.deportiva.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.InstalacionDTO;
import com.gestion.deportiva.dto.SedeDTO;
import com.gestion.deportiva.dto.SedePublicoDTO;
import com.gestion.deportiva.dto.filter.SedeFilter;
import com.gestion.deportiva.dto.filter.SedePublicoFilter;
import com.gestion.deportiva.dto.specifications.SedeMapaSpecifications;
import com.gestion.deportiva.dto.specifications.SedeSpecifications;
import com.gestion.deportiva.mapper.InstalacionMapper;
import com.gestion.deportiva.mapper.SedeMapper;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.Sede;
import com.gestion.deportiva.repository.InstalacionRepository;
import com.gestion.deportiva.repository.SedeRepository;
import com.gestion.deportiva.service.ImageStoreService;
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

	@Autowired
	private InstalacionRepository instalacionRepository;

	@Autowired
	private InstalacionMapper instalacionMapper;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SedeMapper sedeMapper;

	@Autowired
	private ImageStoreService imageStoreService;

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
		if (dto.getLogoBorrar() != null && dto.getLogoBorrar()) {
			dto.setLogo(null);
		}

		if (dto.getLogoFile() != null && !dto.getLogoFile().isEmpty()) {
			try {
				Map<String, String> result = imageStoreService.uploadImage(dto.getLogoFile());
				dto.setLogo(result.get("url"));
			} catch (IOException e) {
				logger.error("Error al guardar el logo", e);
			}
		}
		model = sedeMapper.dtoToModel(dto, model);
		sedeRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public Page<SedeDTO> getPageByFilter(SedeFilter filter, Pageable pageable) {
		return sedeMapper.pageToPageDTO(
				sedeRepository.findAll(SedeSpecifications.filter(limitacionesPermisos(filter)), pageable));
	}

	private SedeFilter limitacionesPermisos(SedeFilter filter) {
		if (SecurityUtil.hasAuthority(Constantes.Permiso.GESTION_GLOBAL)) {
			return filter;
		}
		if (SecurityUtil.hasAuthority(Constantes.Permiso.Localizacion.GESTION_EMPRESA)) {
			filter.setListEmpresaIds(SecurityUtil.getCurrentUserListEmpresaId());
			return filter;
		}

		if (SecurityUtil.hasAuthority(Constantes.Permiso.Localizacion.GESTION_SEDE)) {
			filter.setListIds(SecurityUtil.getCurrentUserListSedeId());
		} else {
			filter.setListIds(List.of(-1L));
		}
		return filter;
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
		return Utils.sortByNombre(sedeMapper
				.listModelToListDTO(sedeRepository.findAll(SedeSpecifications.filter(limitacionesPermisos(filter)))));
	}

	@Override
	public boolean canWrite(Long id) {

		if (SecurityUtil.hasAuthority(Constantes.Permiso.GESTION_GLOBAL)) {
			return true;
		}

		if (id == null && SecurityUtil.hasAuthority(Constantes.Permiso.Localizacion.GESTION_EMPRESA)) {
			return true;
		}
		if (id == null) {
			return false;
		}
		if (SecurityUtil.hasAuthority(Constantes.Permiso.Localizacion.GESTION_EMPRESA)) {
			Sede sede = sedeRepository.findByActivoTrueAndId(id);
			if (sede != null) {
				return SecurityUtil.getCurrentUserListEmpresaId().contains(sede.getEmpresa().getId());
			}

		}
		if (SecurityUtil.hasAuthority(Constantes.Permiso.Localizacion.GESTION_SEDE)) {
			logger.info("Sedes del usuario {}, sede trando de ver {}", SecurityUtil.getCurrentUserListSedeId(), id);
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

	@Override
	public List<SedeDTO> getListDTOParaInstalacion(Long empresaId) {
		List<SedeDTO> retVal = new ArrayList<>();
		if (SecurityUtil.hasAuthority(Constantes.Permiso.GESTION_GLOBAL)) {
			retVal = getListDTO();
		} else if (SecurityUtil.hasAuthority(Constantes.Permiso.Localizacion.GESTION_EMPRESA)) {
			retVal = sedeMapper.listModelToListDTO(
					sedeRepository.findByActivoTrueAndEmpresaIdIn(SecurityUtil.getCurrentUserListEmpresaId()));
		} else if (SecurityUtil.hasAuthority(Constantes.Permiso.Localizacion.GESTION_SEDE)) {
			retVal = sedeMapper.listModelToListDTO(
					sedeRepository.findByActivoTrueAndIdIn(SecurityUtil.getCurrentUserListSedeId()).stream().toList());
		} else if (SecurityUtil.hasAuthority(Constantes.Permiso.Localizacion.GESTION_INSTALACION)) {
			retVal = sedeMapper.listModelToListDTO(
					instalacionRepository.findByActivoTrueAndIdIn(SecurityUtil.getCurrentUserListSedeId()).stream()
							.map(Instalacion::getSede).toList());
		}

		if (empresaId != null) {
			retVal.removeIf(s -> !s.getEmpresaId().equals(empresaId));
		}
		return Utils.addEmptyOptionIfMoreThanOneOption(retVal, SedeDTO.class);
	}

	@Override
	public List<SedeDTO> getListDTOParaEmpleado(Long empresaId) {
		List<SedeDTO> retVal = new ArrayList<>();
		if (SecurityUtil.hasAuthority(Constantes.Permiso.GESTION_GLOBAL)
				|| SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_GLOBAL)) {
			retVal = getListDTO();
		} else if (SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_EMPRESA)) {
			retVal = sedeMapper.listModelToListDTO(
					sedeRepository.findByActivoTrueAndEmpresaIdIn(SecurityUtil.getCurrentUserListEmpresaId()));
		} else if (SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_SEDE)) {
			retVal = sedeMapper.listModelToListDTO(
					sedeRepository.findByActivoTrueAndIdIn(SecurityUtil.getCurrentUserListSedeId()).stream().toList());
		} else if (SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_INSTALACION)) {
			retVal = sedeMapper.listModelToListDTO(
					instalacionRepository.findByActivoTrueAndIdIn(SecurityUtil.getCurrentUserListSedeId()).stream()
							.map(Instalacion::getSede).toList());
		}

		if (empresaId != null) {
			retVal.removeIf(s -> !s.getEmpresaId().equals(empresaId));
		}
		Utils.sortByCampo(retVal, SedeDTO::getEmpresaSedeNombre);
		return Utils.addEmptyOption(retVal, SedeDTO.class);
	}

	@Override
	public List<SedePublicoDTO> getListSedePublicoDTO(SedePublicoFilter filter) {
		List<SedePublicoDTO> retVal = new ArrayList<>();
		List<Sede> listSede = sedeRepository.findAll(SedeMapaSpecifications.filter(filter), Pageable.unpaged())
				.getContent();
		for (Sede sede : listSede) {
			List<InstalacionDTO> listInstalacion = instalacionMapper
					.listModelToListDTO(instalacionRepository.findByActivoTrueAndSedeId(sede.getId()));
			retVal.add(sedeMapper.modelToMapaDTO(sede, listInstalacion));
		}

		return retVal;
	}

	@Override
	public SedePublicoDTO getSedePublicoDTOById(Long id) {
		logger.info("Buscando Sede por ID: {}", id);
		Sede sede = sedeRepository.findByActivoTrueAndId(id);
		List<InstalacionDTO> listInstalacion = instalacionMapper
				.listModelToListDTO(instalacionRepository.findByActivoTrueAndSedeId(sede.getId()));
		return sedeMapper.modelToMapaDTO(sede, listInstalacion);
	}
}
