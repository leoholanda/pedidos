package br.com.aee.thread;

import javax.inject.Inject;

import br.com.aee.controller.EnvioFaturaIndividualEmailBean;
import br.com.aee.controller.FaturaBean;
import br.com.aee.model.Fatura;

public class EnviaFaturaIndividualThread extends Thread {

	private Fatura fatura = new Fatura();

	public EnviaFaturaIndividualThread(Fatura destino) {
		fatura = destino;
	}

	@Inject
	FaturaBean faturaBean;

	@Override
	public void run() {
		new EnvioFaturaIndividualEmailBean().enviaEmailDeFatura(fatura);
	}

}
