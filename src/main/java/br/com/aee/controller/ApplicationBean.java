package br.com.aee.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class ApplicationBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String getVersionApplication() throws IOException {
		Properties props = new Properties();
		props.load(getClass().getResourceAsStream("/application.properties"));
		
		return "Versão " + props.getProperty("versao");
	}
	
}
