package net.bonsamigos.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.bonsamigos.enums.Area;
import net.bonsamigos.enums.Status;
import net.bonsamigos.model.Unidade;
import net.bonsamigos.service.UnidadeService;
import net.bonsamigos.util.FacesUtil;
import net.bonsamigos.util.NegocioException;

@Named
@ViewScoped
public class PesquisaUnidadeController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UnidadeService unidadeService;

	private Unidade unidade;

	private List<Unidade> unidades;

	private String valorDePesquisa;

	public final static int ROW = 10; // Quantidade de linhas para paginacao

	@PostConstruct
	public void init() {
		unidade = new Unidade();
		unidades = unidadeService.findAll();
	}

	/**
	 * Pesquisa unidade pelo codigo
	 * 
	 * @throws NegocioException
	 * @throws NumberFormatException
	 */
	public void pesquisar() {
//		unidades = unidadeService.findByCodigo(valorDePesquisa);
	}

	/**
	 * Desativa unidade
	 */
	public void desativar() {
		unidadeService.remove(unidade);
		FacesUtil.info("Unidade " + unidade.getNomeInicialMaiuscula() + " desativada!");
	}
	
	/**
	 * Lista de areas
	 * @return
	 */
	public List<Area> getAreas() {
		return Arrays.asList(Area.values());
	}
	
	public List<Status> getListaStatus() {
		return Arrays.asList(Status.ATIVADO, Status.DESATIVADO);
	}
	
	@Override
	public String toString() {
		return unidade.getCodigo() + " | " + unidade.getNome();
	}

	/**
	 * Verifica necessidade de paginação
	 * 
	 */
	public boolean isPaginator() {
		return unidades.size() > 10 ? true : false;
	}

	public List<Unidade> getListaTodos() {
		return unidadeService.findAll();
	}

	public Unidade buscaPorCodigo(Long codigo) {
		return unidadeService.findByCodigo(codigo);
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	public List<Unidade> getUnidades() {
		return unidades;
	}

	public void setUnidades(List<Unidade> unidades) {
		this.unidades = unidades;
	}

	public String getValorDePesquisa() {
		return valorDePesquisa;
	}

	public void setValorDePesquisa(String valorDePesquisa) {
		this.valorDePesquisa = valorDePesquisa;
	}

}
