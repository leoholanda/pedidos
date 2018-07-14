package br.com.aee.controller;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import br.com.aee.model.Fatura;

public class EnvioFaturaConfirmacaoPagamento {

	/**
	 * config lembrete de email
	 *
	 * @param fatura
	 * @throws EmailException
	 */
	public void configEmail(Fatura fatura) throws EmailException {
		if (fatura.getPlano().getBeneficiario().getEmail() == null) {
			System.out.println("Email não enviado para: " + fatura.getPlano().getBeneficiario().getNome());
		} else {
			
			System.out.println("Enviando fatura para: " + fatura.getPlano().getBeneficiario().getNome());

			String nomeBeneficiario = fatura.getPlano().getBeneficiario().getNomeComIniciaisMaiuscula();
			String valorTotal = fatura.getConverterValorTotal();
			String valorPago = fatura.getConverterValorPago();
			String dataPagamento = fatura.getConverterDataPagamento();
			String mes = fatura.getMesDaFatura();

			HtmlEmail email = new HtmlEmail();
			email.setHostName("aeeroraima.com.br");
			email.setSslSmtpPort("25");
			email.setAuthenticator(new DefaultAuthenticator("fatura@aeeroraima.com.br", "leo12@3qwARe123"));
			try {
				email.addTo(fatura.getPlano().getBeneficiario().getEmail());
				email.setFrom("fatura@aeeroraima.com.br");
				email.setSubject("Associacao dos Empregados da Embrapa Roraima");
				
				

				StringBuilder builder = new StringBuilder();
				builder.append("<h4>Ola, " + nomeBeneficiario + "</h4>");
				builder.append("<strong>Recebemos a confirmacao do seu pagamento referente a fatura<br />"
						+ " do mes de " + mes + "</strong><p />" + " <table> <tr>"
						+ " <td style='width: 100px'>Nome:</td>" + " <td>" + nomeBeneficiario + "</td>" + " </tr>"
						+ " <tr>" + " <td>Valor pago:</td>" + " <td>" + valorPago + "</td>" + " </tr>" + " <tr>"
						+ " <td>Valor cobrado:</td>" + " <td>" + valorTotal + "</td>" + " </tr>" + " <tr>"
						+ " <td>Data do Pagamento:</td>" + " <td>" + dataPagamento + "</td>" + " </tr>" + " </table> <br />"
						
						+ " Consulte sua fatura em nosso sistema: http://www.aeeroraima.com.br/ <p />"
						+ " Duvidas e sugestoes atraves do email: diretoria@aeeroraima.com.br <br />");
				
				builder.append("<img src=\"http://www.faee.org.br/aees/logomarcas/AEERR.jpg\"> <p />");
				builder.append("Favor nao responder este email");

				email.setHtmlMsg(builder.toString());
				email.send();
			} catch (EmailException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Metodo utilizado para enviar email de confirmacao
	 * 
	 * @param fatura
	 */
	public void enviaEmailDeFatura(Fatura f) {
		try {
			configEmail(f);
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
