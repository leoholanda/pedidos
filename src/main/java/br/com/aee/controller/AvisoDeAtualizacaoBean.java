package br.com.aee.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named("avisoMB")
@SessionScoped
public class AvisoDeAtualizacaoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean exibirMensagem = true;
    
    public Boolean getExibirMensagem() {
		return exibirMensagem;
	}
    
    public void naoExibirMensagem() {
    	setExibirMensagem(false);
    }
    
    public void setExibirMensagem(Boolean exibirMensagem) {
		this.exibirMensagem = exibirMensagem;
	}
	
}