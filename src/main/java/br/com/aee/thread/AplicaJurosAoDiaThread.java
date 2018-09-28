package br.com.aee.thread;

import java.util.Calendar;

import br.com.aee.model.Fatura;
import br.com.aee.model.Status;
import br.com.aee.repository.FaturaRepository;

public class AplicaJurosAoDiaThread extends Thread {

	private FaturaRepository repository;

	public AplicaJurosAoDiaThread(FaturaRepository r) {
		repository = r;
	}

	public Double getResiduoAplicado(Double residuo) {
		if (residuo != null) {
			return residuo;
		} else {
			return 0.00;
		}
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1000l);
			Double juros = 0.00;
			Double multa = 0.00;
			Calendar hoje = Calendar.getInstance();
			for (Fatura f : repository.findByJuros()) {
				if (f.getPlano().getBeneficiario().getStatus() == Status.ATIVADO) {
					System.out.println(">>> Calculando juros para: " + f.getPlano().getBeneficiario().getNome());
					multa = f.getValorTotalGerado() * 0.02;
					int ultimoDiaGerado = f.getDataJuros().get(Calendar.DAY_OF_MONTH);
					int dia = hoje.get(Calendar.DAY_OF_MONTH);

					if (dia != ultimoDiaGerado) {
						juros = f.getValorTotalGerado() * 0.00033 * f.getDiasAtrasados();
						Double calculo = juros + f.getValorTotalGerado() + multa
								+ getResiduoAplicado(f.getResiduoDescontado());
						f.setValorTotal(calculo);
						f.setDataJuros(hoje);
						repository.save(f);
					}
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
