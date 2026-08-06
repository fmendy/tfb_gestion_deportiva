package com.gestion.deportiva.service.impl;

import java.io.IOException;
import java.util.List;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.model.Reserva;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.repository.ReservaEstadoRepository;
import com.gestion.deportiva.repository.ReservaRepository;
import com.gestion.deportiva.repository.UsuarioRepository;
import com.gestion.deportiva.service.PdfReportService;
import com.gestion.deportiva.util.Utils;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
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

	private final UsuarioRepository usuarioRepository;

	private final ReservaRepository reservaRepository;

	private final ReservaEstadoRepository reservaEstadoRepository;

	private final EntityManager entityManager;

	PdfReportServiceImpl(UsuarioRepository usuarioRepository, ReservaRepository reservaRepository,
			ReservaEstadoRepository reservaEstadoRepository, EntityManager entityManager) {
		this.usuarioRepository = usuarioRepository;
		this.reservaRepository = reservaRepository;
		this.reservaEstadoRepository = reservaEstadoRepository;
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void exportarDatosArcoUsuarioPdf(Long usuarioId, HttpServletResponse response) throws IOException {
		Usuario usuario = usuarioRepository.findByActivoTrueAndId(usuarioId);

		AuditReader auditReader = AuditReaderFactory.get(entityManager);
		List<Object[]> revisionsUsuario = auditReader.createQuery().forRevisionsOfEntity(Usuario.class, false, true)
				.add(AuditEntity.id().eq(usuarioId)).getResultList();

		configurarCabeceraResponse(response);

		try (Document document = new Document(PageSize.A4, 36, 36, 36, 36)) {
			PdfWriter.getInstance(document, response.getOutputStream());
			document.open();

			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Font.NORMAL);
			Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Font.NORMAL);
			Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL);

			agregarTituloInforme(document, titleFont);
			agregarDatosActualesUsuario(document, usuario, subtitleFont, bodyFont);
			agregarHistoricoUsuario(document, revisionsUsuario, subtitleFont, bodyFont);
			agregarSeccionReservas(document, usuarioId, auditReader, subtitleFont, bodyFont);

		} catch (DocumentException e) {
			throw new IOException("Error al generar el documento PDF", e);
		}
	}

	private void configurarCabeceraResponse(HttpServletResponse response) {
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=informe-datos-personales-arco.pdf");
	}

	private void agregarTituloInforme(Document document, Font titleFont) throws DocumentException {
		Paragraph title = new Paragraph(Utils.getMessage("usuario.arco.informe.title"), titleFont);
		title.setAlignment(Element.ALIGN_CENTER);
		title.setSpacingAfter(20);
		document.add(title);
	}

	private void agregarDatosActualesUsuario(Document document, Usuario usuario, Font subtitleFont, Font bodyFont)
			throws DocumentException {
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
	}

	private void agregarHistoricoUsuario(Document document, List<Object[]> revisionsUsuario, Font subtitleFont,
			Font bodyFont) throws DocumentException {
		document.add(new Paragraph(Utils.getMessage("usuario.arco.informe.subtitle.historico"), subtitleFont));

		if (revisionsUsuario == null || revisionsUsuario.isEmpty()) {
			document.add(new Paragraph("\n"));
			return;
		}

		int contadorVersionUsuario = 1;
		for (Object[] revisionRow : revisionsUsuario) {
			Usuario entidadRev = (Usuario) revisionRow[0];

			Paragraph versionHeader = new Paragraph(
					Utils.getMessage("usuario.arco.informe.version") + " " + contadorVersionUsuario, subtitleFont);
			versionHeader.setSpacingBefore(10);
			versionHeader.setSpacingAfter(2);
			document.add(versionHeader);

			document.add(new Paragraph(
					"  - " + Utils.getMessage("usuario.arco.informe.nombre") + ": " + entidadRev.getNombre(),
					bodyFont));
			document.add(new Paragraph(
					"  - " + Utils.getMessage("usuario.arco.informe.email") + ": " + entidadRev.getEmail(), bodyFont));
			document.add(new Paragraph("  - " + Utils.getMessage("usuario.arco.informe.fecha.cambio") + ": "
					+ entidadRev.getFechaModificacion(), bodyFont));

			contadorVersionUsuario++;
		}
		document.add(new Paragraph("\n"));
	}

	private void agregarSeccionReservas(Document document, Long usuarioId, AuditReader auditReader, Font subtitleFont,
			Font bodyFont) throws DocumentException {
		document.add(new Paragraph(Utils.getMessage("usuario.arco.informe.subtitle.reservas"), subtitleFont));

		List<Reserva> reservasUsuario = reservaRepository.findByActivoTrueAndUsuarioCreacionId(usuarioId);

		if (reservasUsuario == null || reservasUsuario.isEmpty()) {
			document.add(new Paragraph(Utils.getMessage("usuario.arco.informe.sin.reservas"), bodyFont));
			return;
		}

		for (Reserva reserva : reservasUsuario) {
			renderizarDetalleReserva(document, reserva, subtitleFont, bodyFont);
			renderizarHistoricoReserva(document, reserva, auditReader, subtitleFont, bodyFont);
		}
	}

	private void renderizarDetalleReserva(Document document, Reserva reserva, Font subtitleFont, Font bodyFont)
			throws DocumentException {
		Paragraph reservaHeader = new Paragraph(
				Utils.getMessage("usuario.arco.informe.reserva.id") + ": " + reserva.getId(), subtitleFont);
		reservaHeader.setSpacingBefore(8);
		reservaHeader.setSpacingAfter(2);
		document.add(reservaHeader);

		document.add(new Paragraph(
				"  - " + Utils.getMessage("usuario.arco.informe.reserva.fecha") + ": " + reserva.getFecha(), bodyFont));
		document.add(new Paragraph("  - " + Utils.getMessage("usuario.arco.informe.reserva.hora") + ": "
				+ reserva.getHoraInicio() + " - " + reserva.getHoraFin(), bodyFont));
		document.add(new Paragraph("  - " + Utils.getMessage("usuario.arco.informe.reserva.empresa") + ": "
				+ reserva.getInstalacion().getSede().getEmpresa().getNombre(), bodyFont));
		document.add(new Paragraph("  - " + Utils.getMessage("usuario.arco.informe.reserva.sede") + ": "
				+ reserva.getInstalacion().getSede().getNombre(), bodyFont));
		document.add(new Paragraph("  - " + Utils.getMessage("usuario.arco.informe.reserva.instalacion") + ": "
				+ reserva.getInstalacion().getNombre(), bodyFont));
		document.add(new Paragraph("  - " + Utils.getMessage("usuario.arco.informe.reserva.estado") + ": "
				+ reserva.getReservaEstado().getNombre(), bodyFont));
	}

	@SuppressWarnings("unchecked")
	private void renderizarHistoricoReserva(Document document, Reserva reserva, AuditReader auditReader,
			Font subtitleFont, Font bodyFont) throws DocumentException {
		List<Object[]> revisionsReserva = auditReader.createQuery().forRevisionsOfEntity(Reserva.class, false, true)
				.add(AuditEntity.id().eq(reserva.getId())).getResultList();

		if (revisionsReserva == null || revisionsReserva.isEmpty()) {
			return;
		}

		document.add(new Paragraph("    " + Utils.getMessage("usuario.arco.informe.reserva.historico"), bodyFont));
		int revNum = 1;
		for (Object[] resRevRow : revisionsReserva) {
			Reserva reservaRev = (Reserva) resRevRow[0];

			String estadoNombre = (reservaRev.getReservaEstado() != null
					&& reservaEstadoRepository.findById(reservaRev.getReservaEstado().getId()).isPresent())
							? reservaEstadoRepository.findById(reservaRev.getReservaEstado().getId()).get().getNombre()
							: "N/D";

			Paragraph versionHeader = new Paragraph(Utils.getMessage("usuario.arco.informe.version") + " " + revNum,
					subtitleFont);
			versionHeader.setSpacingBefore(10);
			versionHeader.setSpacingAfter(2);
			document.add(versionHeader);

			document.add(new Paragraph(
					"  - " + Utils.getMessage("usuario.arco.informe.reserva.estado") + ": " + estadoNombre, bodyFont));
			document.add(new Paragraph("  - " + Utils.getMessage("usuario.arco.informe.fecha.cambio") + ": "
					+ reservaRev.getFechaModificacion(), bodyFont));

			revNum++;
		}
	}
}