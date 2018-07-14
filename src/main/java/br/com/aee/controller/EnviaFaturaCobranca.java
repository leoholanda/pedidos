package br.com.aee.controller;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import br.com.aee.model.Fatura;

public class EnviaFaturaCobranca {

	/**
	 * config lembrete de email
	 *
	 * @param fatura
	 * @throws EmailException
	 */
	public void configEmail(Fatura fatura) throws EmailException {
		if (fatura.getPlano().getBeneficiario().getEmail() != null) {
			System.out.println("Enviando cobrança para: " + fatura.getPlano().getBeneficiario().getNome());

			String nomeBeneficiario = fatura.getPlano().getBeneficiario().getNomeComIniciaisMaiuscula();
			String valorTotal = fatura.getConverterValorTotal();
			String valorGerado = fatura.getConverterValorTotalGerado();
			String dataVencimento = fatura.getConverterDataVencimento();
			String multaPorAtraso = fatura.getMultaPorAtraso();
			String jurosAoDia = fatura.getJurosAoDia();

			HtmlEmail email = new HtmlEmail();
			email.setHostName("aeeroraima.com.br");
			email.setSslSmtpPort("25");
			email.setAuthenticator(new DefaultAuthenticator("fatura@aeeroraima.com.br", "leo12@3qwARe123"));
			try {
				email.addTo(fatura.getPlano().getBeneficiario().getEmail());
				email.setFrom("fatura@aeeroraima.com.br");
				email.setSubject("Aviso de Cobranca");

				StringBuilder builder = new StringBuilder();
				builder.append("<h4>Ola, " + nomeBeneficiario + "</h4>");
				builder.append("<strong>Percebemos ate o momento que nao houve confirmacao do pagamento</strong><p />" + " <table> <tr>"
						+ " <td style='width: 100px'>Nome:</td>" + " <td>" + nomeBeneficiario + "</td>" + " </tr>"
						+ " <tr>" + " <td>Vencimento:</td>" + " <td>" + dataVencimento + "</td>" + " </tr>" + " <tr>"
						+ " <tr>" + " <td>Valor Gerado:</td>" + " <td>" + valorGerado + "</td>" + " </tr>" + " <tr>"
						+ " <tr>" + " <td>Multa:</td>" + " <td>" + multaPorAtraso + "</td>" + " </tr>" + " <tr>"
						+ " <tr>" + " <td>Juros:</td>" + " <td>" + jurosAoDia + "</td>" + " </tr>" + " <tr>"
						+ " <td>Valor Total:</td>" + " <td>" + valorTotal + "</td>" + " </tr>" + " </table> <br />"
						
						+ " <strong>Forma de Pagamento: Trasferencia ou deposito bancario</strong><p />"
						+ " <strong>Banco do Brasil</strong><br /> "
						+ " <strong>Associacao dos Empregados da Embrapa Roraima</strong><br />"
						+ " <strong>CNPJ:</strong> 04.650.370/0001-25<br />"
						+ " <strong>Agencia:</strong> 2617-4<br />"
						+ " <strong>Conta Corrente:</strong> 34344-7<p />"
						
						+ " <strong>Bradesco</strong><br /> "
						+ " <strong>Associacao dos Empregados da Embrapa Roraima</strong><br />"
						+ " <strong>CNPJ:</strong> 04.650.370/0001-25<br />"
						+ " <strong>Agencia:</strong> 0522<br />"
						+ " <strong>Conta Corrente:</strong> 20.229-0<p />"
						
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
	 * Metodo utilizado para enviar email de fatura disponivel para beneficiario
	 * 
	 * @param fatura
	 */
	public void enviaEmailDeCobranca(Fatura f) {
		try {
			configEmail(f);
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
