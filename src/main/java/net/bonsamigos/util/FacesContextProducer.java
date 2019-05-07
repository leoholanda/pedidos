package net.bonsamigos.util;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

public class FacesContextProducer {

	@RequestScoped
	@Produces
	public FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}
	
	@Produces
	@RequestScoped
	public ExternalContext getExternalContext() {
		return getFacesContext().getExternalContext();
	}

	@Produces
	@RequestScoped
	public HttpServletResponse getHttpServletResponse() {
		return ((HttpServletResponse) getExternalContext().getResponse());
	}

}
