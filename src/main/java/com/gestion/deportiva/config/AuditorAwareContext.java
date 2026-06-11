package com.gestion.deportiva.config;

public class AuditorAwareContext {

	private static final ThreadLocal<Boolean> auditingDisabled = new ThreadLocal<>();

	public static void disableAuditing() {
		auditingDisabled.set(true);
	}

	public static void enableAuditing() {
		auditingDisabled.set(false);
	}

	public static boolean isAuditingDisabled() {
		return auditingDisabled.get() != null && auditingDisabled.get();
	}
}
