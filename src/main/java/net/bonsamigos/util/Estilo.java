package net.bonsamigos.util;

import net.bonsamigos.enums.Status;

/**
 * Estilização para efeito css e js
 * 
 * @author lholanda
 *
 */
public class Estilo {

	private static String icon = "";
	private static String cor = "";

	public static final String corParaStatus(Status status) {
		switch (status) {
		case ATIVADO:
			return cor = "Green";
		case DESATIVADO:
			return cor = "Red";
		default:
			return cor = "";
		}
	}

	public static final String iconeParaStatus(Status status) {
		switch (status) {
		case ATIVADO:
			return icon = "fa fa-check";
		case DESATIVADO:
			return icon = "fa fa-close";
		default:
			return icon = "";
		}
	}

	public static String getIcon() {
		return icon;
	}

	public static void setIcon(String icon) {
		Estilo.icon = icon;
	}

	public static String getCor() {
		return cor;
	}

	public static void setCor(String cor) {
		Estilo.cor = cor;
	}
	
}
