package br.com.aee.thread;

import br.com.aee.controller.BeneficiarioBean;
import br.com.aee.controller.DependenteBean;

public class ChecaFaixaEtariaThread extends Thread {
	
	@Override
	public void run() {
			new BeneficiarioBean().checaFaixaEtariaDosBeneficiarios();
			new DependenteBean().checaFaixaEtariaDependente();
	}

}
