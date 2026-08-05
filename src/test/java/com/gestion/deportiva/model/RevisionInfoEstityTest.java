package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RevisionInfoEntityTest {

	@Test
	void gettersYSetters() {
		RevisionInfoEntity revisionInfo = new RevisionInfoEntity();
		int rev = 1;
		long timestamp = 1722854400000L;
		Long usuarioId = 42L;

		revisionInfo.setRev(rev);
		revisionInfo.setRevtstmp(timestamp);
		revisionInfo.setUsuarioId(usuarioId);

		assertThat(revisionInfo.getRev()).isEqualTo(rev);
		assertThat(revisionInfo.getRevtstmp()).isEqualTo(timestamp);
		assertThat(revisionInfo.getUsuarioId()).isEqualTo(usuarioId);
	}
}