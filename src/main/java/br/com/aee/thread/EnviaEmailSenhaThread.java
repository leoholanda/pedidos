package br.com.aee.thread;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import br.com.aee.model.Usuario;

public class EnviaEmailSenhaThread extends Thread {

	Usuario usuario = new Usuario();
	String senha = null;

	public EnviaEmailSenhaThread(Usuario usuario, String senha) {
		this.usuario = usuario;
		this.senha = senha;
	}

	@Override
	public void run() {
		try {
			enviaEmailComNovaSenha(usuario, senha);
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Envia senha por email
	 *
	 * @param fatura
	 * @throws EmailException
	 */
	public void enviaEmailComNovaSenha(Usuario usuario, String senha) throws EmailException {
		System.out.println("Enviando senha para usuário: " + usuario.getNome());

		HtmlEmail email = new HtmlEmail();
		email.setCharset("UTF-8");
		email.setFrom("suporte@aeeroraima.com.br", "Suporte AEE Roraima");
		email.setSubject("Solicitação de Nova Senha");
		email.setSSLOnConnect(true);
		email.setAuthentication("suporte@aeeroraima.com.br", "leo12@3qwARe123");
		email.setHostName("server28.integrator.com.br");
		email.setSmtpPort(465);
		email.addTo(usuario.getEmail(), usuario.getNomeComIniciaisMaiuscula());

		StringBuilder builder = new StringBuilder();
		builder.append("<h4>Olá, " + usuario.getNomeComIniciaisMaiuscula() + "</h4>");
		builder.append("<strong>Nova senha: " + senha + "</strong><p />");
		builder.append("<img src=\"http://www.faee.org.br/aees/logomarcas/AEERR.jpg\"> <br />");
		builder.append("Favor não responder este email");

		email.setHtmlMsg(builder.toString());
		email.send();
	}
	
	

}
