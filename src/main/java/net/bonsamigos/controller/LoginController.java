package net.bonsamigos.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.bonsamigos.util.FacesUtil;

@Named
@SessionScoped
public class LoginController implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletRequest request;

	@Inject
	private HttpServletResponse response;

	private String cpf;

	public void login() throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/j_spring_security_check");
		dispatcher.forward(request, response);

		facesContext.responseComplete();
	}

	public void preRender() {
		if ("true".equals(request.getParameter("invalid"))) {
			FacesUtil.error("Autenticação inválida!");
		}
	}
	
	public void logout() {
		try {
			String context = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
			FacesContext.getCurrentInstance().getExternalContext().redirect(context + "/logout");
		} catch (IOException e) {
			e.printStackTrace();
			FacesUtil.error("Ops! Não planejamos isso.");
		}
	}

	public String getCpf() {
		return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
}
