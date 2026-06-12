package com.gestion.deportiva.exception;

import com.gestion.deportiva.util.Utils;

public class RecursoNoEncontradoException extends Exception {

	private static final long serialVersionUID = 9105619061083854876L;

	public RecursoNoEncontradoException() {
        super(Utils.getMessage("error.recurso.no.encontrado"));
    }

    public RecursoNoEncontradoException(String message) {
        super(message);
    }
}