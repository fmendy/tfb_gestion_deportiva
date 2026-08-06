package com.gestion.deportiva.config;

import org.hibernate.envers.RevisionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gestion.deportiva.model.RevisionInfoEntity;
import com.gestion.deportiva.util.SecurityUtil;

public class CustomRevisionListener implements RevisionListener {

	private static final Logger logger = LoggerFactory.getLogger(CustomRevisionListener.class);

	@Override
	public void newRevision(Object revisionEntity) {
		RevisionInfoEntity rev = (RevisionInfoEntity) revisionEntity;
		try {
			Long userId = SecurityUtil.getCurrentUserId();
			rev.setUsuarioId(userId);
		} catch (IllegalStateException e) {
			logger.error("Error al obtener el usuario actual: " + e.getMessage());
			rev.setUsuarioId(null);
		}

	}
}