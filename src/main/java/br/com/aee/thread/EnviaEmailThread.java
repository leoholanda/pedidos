package br.com.aee.thread;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.aee.controller.EnvioFaturaEmailBean;
import br.com.aee.controller.FaturaBean;
import br.com.aee.model.Fatura;

public class EnviaEmailThread extends Thread {

	List<Fatura> listaFaturas = new ArrayList<>();

	public EnviaEmailThread(List<Fatura> faturas) {
		listaFaturas = faturas;
	}

	@Inject
	FaturaBean faturaBean;

	@Override
	public void run() {
		for (Fatura fatura : listaFaturas) {
			new EnvioFaturaEmailBean().enviaEmailDeFatura(fatura);
		}
	}

}
