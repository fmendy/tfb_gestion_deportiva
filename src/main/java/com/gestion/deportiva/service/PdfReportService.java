package com.gestion.deportiva.service;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

public interface PdfReportService {

	 void exportarDatosArcoUsuarioPdf(Long usuarioId, HttpServletResponse response) throws IOException;
}
