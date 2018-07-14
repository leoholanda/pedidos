package br.com.aee.thread;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.aee.controller.EnviaEmailBeneficiarioBean;
import br.com.aee.controller.FaturaBean;
import br.com.aee.model.Beneficiario;

public class EnviaEmailParaBeneficiarioThread extends Thread {

	List<Beneficiario> listaDeBeneficiarios = new ArrayList<>();
	
	String textoDescricao;

	public EnviaEmailParaBeneficiarioThread(List<Beneficiario> beneficiarios, String texo) {
		listaDeBeneficiarios = beneficiarios;
		textoDescricao = texo;
	}

	@Inject
	FaturaBean faturaBean;

	@Override
	public void run() {
		for (Beneficiario beneficiario: listaDeBeneficiarios) {
			new EnviaEmailBeneficiarioBean().enviarEmail(beneficiario, textoDescricao);
		}
	}

}
