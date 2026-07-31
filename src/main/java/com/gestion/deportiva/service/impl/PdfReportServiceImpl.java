package com.gestion.deportiva.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.repository.UsuarioRepository;
import com.gestion.deportiva.service.PdfReportService;
import com.gestion.deportiva.util.Utils;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class PdfReportServiceImpl implements PdfReportService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private EntityManager entityManager;

	@Override
	public void exportarDatosArcoUsuarioPdf(Long usuarioId, HttpServletResponse response) throws IOException {

		Usuario usuario = usuarioRepository.findByActivoTrueAndId(usuarioId);

		// Consultar el histórico de auditoría de Envers para la entidad Usuario
		AuditReader auditReader = AuditReaderFactory.get(entityManager);
		List<Object[]> revisions = auditReader.createQuery().forRevisionsOfEntity(Usuario.class, false, true)
				.add(AuditEntity.id().eq(usuarioId)).getResultList();

		// 1. Configurar la respuesta HTTP
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=informe-datos-personales-arco.pdf");

		// 2. Crear el documento PDF (Tamaño A4, márgenes de 36 puntos)
		Document document = new Document(PageSize.A4, 36, 36, 36, 36);
		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();

		// Fuentes
		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Font.NORMAL);
		Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Font.NORMAL);
		Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL);

		// Título del informe
		Paragraph title = new Paragraph(Utils.getMessage("usuario.arco.informe.title"), titleFont);
		title.setAlignment(Element.ALIGN_CENTER);
		title.setSpacingAfter(20);
		document.add(title);

		// Sección 1: Datos Actuales
		document.add(new Paragraph(Utils.getMessage("usuario.arco.informe.subtitle.datos.actuales"), subtitleFont));
		if (usuario != null) {
			document.add(new Paragraph(Utils.getMessage("usuario.arco.informe.nombre") + ": " + usuario.getNombre(),
					bodyFont));
			document.add(new Paragraph(Utils.getMessage("usuario.arco.informe.email") + ": " + usuario.getEmail(),
					bodyFont));
			document.add(new Paragraph(
					Utils.getMessage("usuario.arco.informe.fecha.registro") + ": " + usuario.getFechaCreacion(),
					bodyFont));
		} else {
			document.add(new Paragraph("Usuario no encontrado.", bodyFont));
		}
		document.add(new Paragraph("\n"));

		// Sección 2: Histórico de Cambios (Envers)
		document.add(new Paragraph(Utils.getMessage("usuario.arco.informe.subtitle.historico"), subtitleFont));

		if (revisions != null && !revisions.isEmpty()) {
			int contadorVersion = 1;
			for (Object[] revisionRow : revisions) {
				Usuario entidadRev = (Usuario) revisionRow[0];
				Object revisionEntity = revisionRow[1];

				// Título de la versión (ej. Versión 1)
				Paragraph versionHeader = new Paragraph(
						Utils.getMessage("usuario.arco.informe.version") + " " + contadorVersion, subtitleFont);
				versionHeader.setSpacingBefore(10);
				versionHeader.setSpacingAfter(2);
				document.add(versionHeader);

				// Datos formateados de forma limpia y bonita
				document.add(new Paragraph(
						"  • " + Utils.getMessage("usuario.arco.informe.nombre") + ": " + entidadRev.getNombre(),
						bodyFont));
				document.add(new Paragraph(
						"  • " + Utils.getMessage("usuario.arco.informe.email") + ": " + entidadRev.getEmail(),
						bodyFont));
				document.add(new Paragraph("  • " + Utils.getMessage("usuario.arco.informe.fecha.cambio") + ": "
						+ entidadRev.getFechaModificacion(), bodyFont));

				contadorVersion++;
			}
		}

		document.close();
	}
}