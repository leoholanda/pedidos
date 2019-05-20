package net.bonsamigos.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Mes {
	
	/**
	 * Nome por extenso do mês atual
	 * @return
	 */
	public final static String mesAtualPorExtenso() {
		Date hoje = new Date();
		GregorianCalendar dataCal = new GregorianCalendar();
		dataCal.setTime(hoje);
		int mes = dataCal.get(Calendar.MONTH);

		String meses[] = { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro",
				"Outubro", "Novembro", "Dezembro" };

		return (meses[mes]);
	}
	
	/**
	 * Nome por extenso do mês atual
	 * @return
	 */
	public final static String mesPorExtenso(Calendar data) {
		GregorianCalendar dataCal = new GregorianCalendar();
		dataCal.setTime(data.getTime());
		int mes = dataCal.get(Calendar.MONTH);

		String meses[] = { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro",
				"Outubro", "Novembro", "Dezembro" };

		return (meses[mes]);
	}
	
	public final static String[] listaMeses() {
		String[] meses = { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro",
				"Outubro", "Novembro", "Dezembro" };
		return meses;
	}

}
