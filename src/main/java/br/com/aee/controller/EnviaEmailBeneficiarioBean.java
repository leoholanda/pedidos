package br.com.aee.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.aee.model.Beneficiario;
import br.com.aee.repository.BeneficiarioRepository;
import br.com.aee.thread.EnviaEmailIndividualAssociadoThread;
import br.com.aee.thread.EnviaEmailParaBeneficiarioThread;
import br.com.aee.util.JsfUtil;

@Named("enviaEmailBeneficiarioMB")
@ViewScoped
public class EnviaEmailBeneficiarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private BeneficiarioRepository repository;

	@NotEmpty
	private String textoDeEmail;

	private Beneficiario beneficiario = new Beneficiario();

	/**
	 * Envia email para todos
	 * 
	 * @return
	 */
	public void enviarEmailParaBeneficiario() {
		EnviaEmailParaBeneficiarioThread email = new EnviaEmailParaBeneficiarioThread(
				repository.findBeneficiarioAtivado(), textoDeEmail);
		email.start();

		JsfUtil.info("Email enviado com sucesso!");
		textoDeEmail = null;
	}

	/**
	 * Envia email de forma individual
	 */
	public void enviarEmailParaBeneficiarioEscolhido() {
		System.out.println(">> Enviando email individual");
		EnviaEmailIndividualAssociadoThread email = new EnviaEmailIndividualAssociadoThread(
				repository.findBeneficiarioAtivado(), textoDeEmail, beneficiario);
		email.start();

		JsfUtil.info("Email enviado com sucesso!");
		textoDeEmail = null;
		beneficiario = new Beneficiario();
	}

	/**
	 * config lembrete de email
	 *
	 * @param fatura
	 * @throws EmailException
	 */
	public void configEmail(Beneficiario beneficiario, String texto) throws EmailException {
		if (beneficiario.getEmail() == null) {
			System.out.println("Email não enviado para: " + beneficiario.getNome());
		} else {

			System.out.println("Enviando email para: " + beneficiario.getNome());

			HtmlEmail email = new HtmlEmail();
			email.setHostName("aeeroraima.com.br");
			email.setSslSmtpPort("25");
			email.setAuthenticator(new DefaultAuthenticator("diretoria@aeeroraima.com.br", "zopic80a6h@"));
			try {
				email.addTo(beneficiario.getEmail());
				email.setFrom("diretoria@aeeroraima.com.br");
				email.setSubject("Associacao dos Empregados da Embrapa Roraima");

				StringBuilder builder = new StringBuilder();
				builder.append(" <p> " + texto + " </p>");
				builder.append("<img src=\"http://www.faee.org.br/aees/logomarcas/AEERR.jpg\"> <br />");

				email.setHtmlMsg(builder.toString());
				email.send();
			} catch (EmailException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Metodo utilizado para enviar email de fatura disponivel para beneficiario
	 * 
	 * @param fatura
	 */
	public void enviarEmail(Beneficiario b, String texto) {
		try {
			configEmail(b, texto);
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getTextoDeEmail() {
		return textoDeEmail;
	}

	public void setTextoDeEmail(String textoDeEmail) {
		this.textoDeEmail = textoDeEmail;
	}
	
	public Beneficiario getBeneficiario() {
		return beneficiario;
	}
	
	public void setBeneficiario(Beneficiario beneficiario) {
		this.beneficiario = beneficiario;
	}

}
