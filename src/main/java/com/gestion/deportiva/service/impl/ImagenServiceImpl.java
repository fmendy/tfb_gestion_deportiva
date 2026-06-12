package com.gestion.deportiva.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.dto.ImagenDTO;
import com.gestion.deportiva.dto.filter.ImagenFilter;
import com.gestion.deportiva.dto.specifications.ImagenSpecifications;
import com.gestion.deportiva.model.Imagen;
import com.gestion.deportiva.repository.ImagenRepository;
import com.gestion.deportiva.service.ImagenService;
import com.gestion.deportiva.util.ImagenUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class ImagenServiceImpl implements ImagenService {

	private static final Logger logger = LoggerFactory.getLogger(ImagenServiceImpl.class);

	@Autowired
	private ImagenRepository imagenRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public ImagenDTO findById(Long id) {
		logger.info("Buscando Imagen por ID: {}", id);
		return ImagenUtil.modelToDTO(imagenRepository.findByActivoTrueAndId(id));
	}

	@Override
	public ImagenDTO findByUuid(String uuid) {
		logger.info("Buscando Imagen por UUID: {}", uuid);
		return ImagenUtil.modelToDTO(imagenRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	public Long guardar(ImagenDTO dto) {
		logger.info("Guardando Imagen");
		Imagen model = imagenRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo Imagen");
			model = new Imagen();
		}
		model = ImagenUtil.dtoToModel(dto, model);
		imagenRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public Page<ImagenDTO> getPageByFilter(ImagenFilter filter, Pageable pageable) {
		return ImagenUtil.pageToPageDTO(imagenRepository.findAll(ImagenSpecifications.filter(filter), pageable));
	}

	@Override
	public void eliminar(Long id) {
		logger.info("Eliminando Imagen por ID: {}");
		Imagen model = imagenRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		imagenRepository.saveAndFlush(model);
	}

	@Override
	public void eliminar(String uuid) {
		logger.info("Eliminando Imagen por ID: {}");
		Imagen model = imagenRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		imagenRepository.saveAndFlush(model);
	}

	@Override
	public List<ImagenDTO> getListDTO() {
		return ImagenUtil.listModelToListDTO(imagenRepository.findByActivoTrue());
	}

	@Override
	public List<ImagenDTO> getListDTO(ImagenFilter filter) {
		return ImagenUtil.listModelToListDTO(imagenRepository.findAll(ImagenSpecifications.filter(filter)));
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
	public byte[] exportarExcel(ImagenFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}
}
