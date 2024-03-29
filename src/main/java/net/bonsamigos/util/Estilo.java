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
		case AUTORIZADO: 
			return cor = "Green";
		case ATIVADO:
			return cor = "Green";
		case DESATIVADO:
			return cor = "Red";
		default:
			return cor = "Red";
		}
	}

	public static final String iconeParaStatus(Status status) {
		switch (status) {
		case AUTORIZADO:
			return icon = "fa fa-check";
		case ATIVADO:
			return icon = "fa fa-check";
		case DESATIVADO:
			return icon = "fa fa-close";
		default:
			return icon = "fa fa-close";
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
