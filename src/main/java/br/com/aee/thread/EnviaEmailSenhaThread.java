package br.com.aee.thread;

import org.apache.commons.mail.DefaultAuthenticator;
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
				// TODO Auto-generated catch block
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

		String nome = usuario.getNome();

		HtmlEmail email = new HtmlEmail();
		email.setHostName("aeeroraima.com.br");
		email.setSslSmtpPort("25");
		email.setAuthenticator(new DefaultAuthenticator("suporte@aeeroraima.com.br", "leo123qwe123151123"));
		try {
			email.addTo(usuario.getEmail());
			email.setFrom("diretoria@aeeroraima.com.br");
			email.setSubject("Solicitação de senha");

			StringBuilder builder = new StringBuilder();
			builder.append("<h4>Ola, " + nome + "</h4>");
			builder.append("<strong>Nova senha: " + senha + "</strong><p />");
			builder.append("<img src=\"http://www.faee.org.br/aees/logomarcas/AEERR.jpg\"> <br />");
			builder.append("Favor nao responder este email");

			email.setHtmlMsg(builder.toString());
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}

	}
	
	

}
