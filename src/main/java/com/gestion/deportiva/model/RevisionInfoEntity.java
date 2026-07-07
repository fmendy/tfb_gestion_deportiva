package com.gestion.deportiva.model;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import com.gestion.deportiva.config.CustomRevisionListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@RevisionEntity(CustomRevisionListener.class)
@Table(name = "revision_info")
@Getter
@Setter
public class RevisionInfoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@RevisionNumber
	private int rev;

	@RevisionTimestamp
	private long revtstmp;

	@Column(name = "id_usuario", nullable = false)
	private Long usuarioId;

}
