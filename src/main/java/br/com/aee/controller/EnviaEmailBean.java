package br.com.aee.controller;

import java.util.Calendar;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.outjected.email.api.MailMessage;
import com.outjected.email.impl.templating.velocity.VelocityTemplate;

import br.com.aee.mail.Mailer;
import br.com.aee.model.Fatura;
import br.com.aee.repository.FaturaRepository;
import br.com.aee.util.JsfUtil;

@Named("enviaEmailMB")
@RequestScoped
public class EnviaEmailBean {

	@Inject
	private Mailer mailer;

	@Inject
	private FaturaRepository faturaRepository;

	/**
	 * config lembrete de email
	 *
	 * @param fatura
	 */
	public void configEmail(Fatura fatura) {
		System.out.println(">> Entrou...");
		MailMessage message = mailer.novaMensagem();

		message.to(fatura.getPlano().getBeneficiario().getEmail())
				.subject("Associação dos Empregados da Embrapa Roraima")
//				.bodyHtml(new VelocityTemplate(getClass().getResourceAsStream("/emails/notificacao.template")))
				.put("fatura", fatura).send();
	}

	/**
	 * Metodo utilizado para enviar email de fatura disponivel para beneficiario
	 * 
	 * @param fatura
	 */
	public void enviaEmailDeFatura2() {
		System.out.println(">> Entrando no metodo para enviar email");

		Calendar hoje = Calendar.getInstance();
		for (Fatura f : faturaRepository.findByFaturaBeneficiarioAtivo(hoje)) {
			if (f.getPlano().getBeneficiario().getEmail() != null) {
				configEmail(f);
				System.out.println("Enviando email para: " + f.getPlano().getBeneficiario().getNome());
			}
		}

		JsfUtil.info("Terminou de enviar...");
	}

	/**
	 * Metodo utilizado para enviar email de fatura disponivel para beneficiario
	 * 
	 * @param fatura
	 */
	public void enviaEmailDeFatura(Fatura f) {
		configEmail(f);
	}
}
