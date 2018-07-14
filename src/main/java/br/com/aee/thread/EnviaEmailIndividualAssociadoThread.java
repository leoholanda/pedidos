package br.com.aee.thread;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.aee.controller.EnviaEmailBeneficiarioBean;
import br.com.aee.controller.FaturaBean;
import br.com.aee.model.Beneficiario;

public class EnviaEmailIndividualAssociadoThread extends Thread {

	List<Beneficiario> listaDeBeneficiarios = new ArrayList<>();
	
	Beneficiario beneficiario;

	String textoDescricao;

	public EnviaEmailIndividualAssociadoThread(List<Beneficiario> beneficiarios, String texo,
			Beneficiario beneficiario) {
		listaDeBeneficiarios = beneficiarios;
		textoDescricao = texo;
		this.beneficiario = beneficiario;
	}

	@Inject
	FaturaBean faturaBean;

	@Override
	public void run() {
		new EnviaEmailBeneficiarioBean().enviarEmail(beneficiario, textoDescricao);
	}

}
