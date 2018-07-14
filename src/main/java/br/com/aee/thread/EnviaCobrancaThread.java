package br.com.aee.thread;

import java.util.ArrayList;
import java.util.List;

import br.com.aee.controller.EnviaFaturaCobranca;
import br.com.aee.model.Fatura;

public class EnviaCobrancaThread extends Thread {

	List<Fatura> listaFaturas = new ArrayList<>();

	public EnviaCobrancaThread(List<Fatura> faturas) {
		listaFaturas = faturas;
	}

	@Override
	public void run() {
		for (Fatura fatura : listaFaturas) {
			new EnviaFaturaCobranca().enviaEmailDeCobranca(fatura);
		}
	}

}
