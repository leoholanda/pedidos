/** 
 * Constant class
 * Utilizada apenas para descrever o endereco das views
 */
package br.com.aee.util;

/**
 * @author holanda
 *
 */
public class Adress {

	public static final String REDIRECT = "?faces-redirect=true";

	public static final String INDEX = "/pages/public/index" + REDIRECT;

	public static final String LOGIN = "/pages/public/login" + REDIRECT;

	public static final String DASHBOARD = "/pages/protected/dashboard/dashboard" + REDIRECT;
	
	public static final String DASHBOARD_FINANCAS = "/pages/protected/dashboard/dashboard-financas" + REDIRECT;

	public static final String NOVO_SETOR = "/pages/protected/setor/novo-setor" + REDIRECT;

	public static final String PESQUISA_SETOR = "/pages/protected/setor/pesquisa-setor" + REDIRECT;

	public static final String NOVO_BENEFICIARIO = "/pages/protected/beneficiario/novo-beneficiario" + REDIRECT;

	public static final String PESQUISA_BENEFICIARIO = "/pages/protected/beneficiario/pesquisa-beneficiario" + REDIRECT;
	
	public static final String INDEX_BENEFICIARIO = "/pages/protected/beneficiario/index-beneficiario" + REDIRECT;

	public static final String PESQUISA_FATURA = "/pages/protected/fatura/pesquisa-fatura" + REDIRECT;

	public static final String NOVA_FAIXA = "/pages/protected/faixa/nova-faixa" + REDIRECT;

	public static final String PESQUISA_FAIXA = "/pages/protected/faixa/pesquisa-faixa" + REDIRECT;

	public static final String PESQUISA_USUARIO = "/pages/protected/usuario/pesquisa-usuario" + REDIRECT;

	public static final String AREA_BENEFICIARIO = "/pages/public/beneficiario/beneficiario" + REDIRECT;

}
