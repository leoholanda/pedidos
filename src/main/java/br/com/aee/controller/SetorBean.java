package br.com.aee.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import br.com.aee.model.Setor;
import br.com.aee.repository.SetorRepository;
import br.com.aee.util.JsfUtil;

@Named("setorMB")
@ViewScoped
public class SetorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private SetorRepository repository;

	private Setor setor;

	private List<Setor> listaSetores;

	private String searchValue;

	// Actions

	/**
	 * Salva setor
	 */
	public void save(Setor setor) {
		try {
			if (validacaoNomeSetor()) {
				repository.save(setor);
				setor = new Setor();

				JsfUtil.info("Salvo com sucesso!");
				System.out.println("Salvo com sucesso");

			} else {
				JsfUtil.error("Setor existente");
			}

		} catch (Exception e) {
			e.getMessage();
		}
	}

	/**
	 * Pesquisa setor pelo nome
	 */
	public void pesquisar() {
		listaSetores = repository.findByNomeLikeOrderByNomeAsc("%"+searchValue+"%");
		
		if (listaSetores.isEmpty()) {
			JsfUtil.error("Nenhum registro encontrado");
		}
	}

	/**
	 * Seleciona setor na tabela
	 * 
	 * @param event
	 * @return
	 */
	public Setor selecionaSetorNaTabela(SelectEvent event) {
		return setor;
	}

	// Listing

	/**
	 * Lista setor por ordem alfabetica
	 * 
	 * @return
	 */
	public List<Setor> getListaSetores() {
		if (listaSetores == null) {
			listaSetores = repository.findOrderByNome();
		}
		return listaSetores;
	}

	// Validations

	/**
	 * Valida existencia de registro nome setor
	 * 
	 * @return
	 */
	public Boolean validacaoNomeSetor() {
		return repository.findAllByName(setor.getNome()).isEmpty();
	}
	
	/**
	 * Verifica se a lista findByNomeLikeOrderByNomeAsc é vazia
	 * @return
	 */
	public Boolean getListEmpty() {
		return !repository.findByNomeLikeOrderByNomeAsc("%"+searchValue+"%").isEmpty();
		
	}
	

	/**
	 * Instancia objeto setor
	 * 
	 * @return
	 */
	public Setor getSetor() {
		if (setor == null) {
			setor = new Setor();
		}
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue.toUpperCase();
	}

}
