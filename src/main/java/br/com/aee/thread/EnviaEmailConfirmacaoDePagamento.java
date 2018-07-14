package br.com.aee.thread;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.aee.controller.EnvioFaturaConfirmacaoPagamento;
import br.com.aee.controller.FaturaBean;
import br.com.aee.model.Fatura;

public class EnviaEmailConfirmacaoDePagamento extends Thread {

	List<Fatura> listaFaturas = new ArrayList<>();

	public EnviaEmailConfirmacaoDePagamento(List<Fatura> faturas) {
		listaFaturas = faturas;
	}

	@Inject
	FaturaBean faturaBean;

	@Override
	public void run() {
		for (Fatura fatura : listaFaturas) {
			new EnvioFaturaConfirmacaoPagamento().enviaEmailDeFatura(fatura);
		}
	}

}
