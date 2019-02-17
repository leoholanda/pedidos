package br.com.aee.controller;

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
			email.setCharset("UTF-8");
			email.setFrom("fatura@aeeroraima.com.br", "Fatura AEE Roraima");
			email.setSubject("Confirmação de Pagamento");
			email.setSSLOnConnect(true);
			email.setAuthentication("fatura@aeeroraima.com.br", "leo12@3qwARe123");
			email.setHostName("server28.integrator.com.br");
			email.setSmtpPort(465);
			email.addTo(fatura.getPlano().getBeneficiario().getEmail(), fatura.getPlano().getBeneficiario().getNomeComIniciaisMaiuscula());		

			StringBuilder builder = new StringBuilder();
			builder.append("<h4>Olá, " + nomeBeneficiario + "</h4>");
			builder.append("<strong>Recebemos a confirmação do pagamento referente a fatura<br />"
					+ " do mês de " + mes + "</strong><p />" + " <table> <tr>"
					+ " <td style='width: 100px'>Nome:</td>" + " <td>" + nomeBeneficiario + "</td>" + " </tr>"
					+ " <tr>" + " <td>Valor pago:</td>" + " <td>" + valorPago + "</td>" + " </tr>" + " <tr>"
					+ " <td>Valor cobrado:</td>" + " <td>" + valorTotal + "</td>" + " </tr>" + " <tr>"
					+ " <td>Data do Pagamento:</td>" + " <td>" + dataPagamento + "</td>" + " </tr>" + " </table> <br />"
					
					+ " Consulte sua fatura em nosso sistema: http://www.aeeroraima.com.br/ <p />"
					+ " Duvidas e sugestões atráves do email: diretoria@aeeroraima.com.br <br />");
			
			builder.append("<img src=\"http://www.aeeroraima.com.br/javax.faces.resource/logo-aee-sem-nome.png.xhtml?ln=images\"> <p />");
			builder.append("Favor não responder este email");

			email.setHtmlMsg(builder.toString());
			email.send();
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
