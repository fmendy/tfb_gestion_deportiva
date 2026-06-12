package com.gestion.deportiva.exception;

import com.gestion.deportiva.util.Utils;

public class PermisoException extends Exception {


	/**
	 * 
	 */
	private static final long serialVersionUID = -343462676720158029L;

	public PermisoException() {
        super(Utils.getMessage("error.no.permisos"));
    }

    public PermisoException(String message) {
        super(message);
    }
}