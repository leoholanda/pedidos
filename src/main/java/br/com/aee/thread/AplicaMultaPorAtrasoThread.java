package br.com.aee.thread;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.aee.model.Fatura;
import br.com.aee.repository.FaturaRepository;

public class AplicaMultaPorAtrasoThread extends Thread {

	private List<Fatura> faturas = new ArrayList<>();

	@Inject
	private FaturaRepository repository;

	public AplicaMultaPorAtrasoThread(List<Fatura> lista, FaturaRepository r) {
		faturas = lista;
		repository = r;
	}

	@Override
	public void run() {
		System.out.println(">> Abriu a thread");
		try {
			Thread.sleep(10000l);
			System.out.println(">> Abriu a thread ++");
			Double multa = 0.00;
			for (Fatura f : this.faturas) {
				System.out.println(">>> Calculando multa para: " + f.getPlano().getBeneficiario().getNome());
				multa = f.getValorTotalGerado() * 0.02;
				Double calculo = multa + f.getValorTotalGerado();

				f.setMultaAplicada(true);
				f.setValorTotal(calculo);

				repository.save(f);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
