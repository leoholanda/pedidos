package br.com.aee.controller;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import br.com.aee.model.Fatura;

public class EnvioFaturaEmailBean {

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
			String dataVencimento = fatura.getConverterDataVencimento();

			HtmlEmail email = new HtmlEmail();
			email.setCharset("UTF-8");
			email.setFrom("fatura@aeeroraima.com.br", "Fatura AEE Roraima");
			email.setSubject("AEE Roraima");
			email.setSSLOnConnect(true);
			email.setAuthentication("fatura@aeeroraima.com.br", "leo12@3qwARe123");
			email.setHostName("server28.integrator.com.br");
			email.setSmtpPort(465);
			email.addTo(fatura.getPlano().getBeneficiario().getEmail());	

			StringBuilder builder = new StringBuilder();
			builder.append("<h4>Olá, " + nomeBeneficiario + "</h4>");
			builder.append("<strong>Fatura disponível para pagamento</strong><p />" + " <table> <tr>"
					+ " <td style='width: 100px'>Nome:</td>" + " <td>" + nomeBeneficiario + "</td>" + " </tr>"
					+ " <tr>" + " <td>Valor total:</td>" + " <td>" + valorTotal + "</td>" + " </tr>" + " <tr>"
					+ " <td>Vencimento:</td>" + " <td>" + dataVencimento + "</td>" + " </tr>" + " </table> <br />"
					+ " <strong>Forma de Pagamento: Trasferência ou depósito bancário</strong><p />"
					
					+ " <strong>Banco do Brasil</strong><br /> "
					+ " <strong>Associação dos Empregados da Embrapa Roraima</strong><br />"
					+ " <strong>CNPJ:</strong> 04.650.370/0001-25<br />"
					+ " <strong>Agência:</strong> 2617-4<br />"
					+ " <strong>Conta Corrente:</strong> 34344-7<p />"
					
					+ " <strong>Bradesco</strong><br /> "
					+ " <strong>Associação dos Empregados da Embrapa Roraima</strong><br />"
					+ " <strong>CNPJ:</strong> 04.650.370/0001-25<br />"
					+ " <strong>Agência:</strong> 0522<br />"
					+ " <strong>Conta Corrente:</strong> 20.229-0<p />"
					
					+ " Consulte sua fatura em nosso sistema: http://www.aeeroraima.com.br/ <p />"
					+ " Duvidas e sugestões atráves do email: diretoria@aeeroraima.com.br <br />");
			
			builder.append("<img src=\"http://www.faee.org.br/aees/logomarcas/AEERR.jpg\"> <p />");
			builder.append("Favor não responder este email");

			email.setHtmlMsg(builder.toString());
			email.send();
		}

	}

	/**
	 * Metodo utilizado para enviar email de fatura disponivel para beneficiario
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
