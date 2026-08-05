package com.gestion.deportiva.config;

import org.hibernate.envers.RevisionListener;

import com.gestion.deportiva.model.RevisionInfoEntity;
import com.gestion.deportiva.util.SecurityUtil;



public class CustomRevisionListener implements RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		RevisionInfoEntity rev = (RevisionInfoEntity) revisionEntity;
		try {
			Long userId = SecurityUtil.getCurrentUserId();
			rev.setUsuarioId(userId);
		} catch (IllegalStateException e) {
			// No hay usuario autenticado 
			rev.setUsuarioId(null);
		}

	}
}