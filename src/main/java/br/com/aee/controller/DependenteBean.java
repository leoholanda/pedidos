package br.com.aee.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.ejb.Asynchronous;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import br.com.aee.model.Dependente;
import br.com.aee.model.FaixaEtaria;
import br.com.aee.model.TipoDependente;
import br.com.aee.repository.DependenteRepository;
import br.com.aee.util.JsfUtil;

@Named("dependenteMB")
@ViewScoped
public class DependenteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private DependenteRepository repository;

	@Inject
	private FaixaEtariaBean faixaEtariaBean;

	@Inject
	private BeneficiarioBean beneficiarioBean;

	private List<Dependente> listaDependentes;

	private Dependente dependente;

	private String searchValue;

	
	// Action
	
	
	/**
	 * Salva associado
	 */
	public void save() {
		if (validaDependente(dependente.getCpf())) {
			dependente.setAcomodacao(dependente.getBeneficiario().getAcomodacao());
			repository.save(dependente);
			dependente = new Dependente();
		}

		this.checaFaixaEtariaDependente();
	}

	/**
	 * Remove dependente populado
	 */
	public void removerDependente() {
		System.out.println("Dependente: " + dependente.getNome());
		repository.remove(dependente);
		JsfUtil.info("Dependente Removido!");
	}
	
	/**
	 * Outra forma para remover dependente
	 */
	@Asynchronous
	public void excluirDependente() {
		for (Dependente d : repository.findByStatusDesativado()) {
			System.out.println("Dependente Removido: " + d.getNome());
			repository.remove(d);
		}
	}

	/**
	 * Pesquisa dependente com 3 parametros CPF, Titular e Nome
	 */
	public void pesquisar() {
		listaDependentes = repository.findByCpfTitularNome(searchValue, "%" + searchValue + "%",
				"%" + searchValue + "%");

		if (listaDependentes.isEmpty()) {
			JsfUtil.error("Nenhum registro encontrado");
		}
	}

	/**
	 * Seleciona dependente via ajax na datatable
	 *
	 * @param event
	 * @return
	 */
	public Dependente selecionaDependenteNaTabela(SelectEvent event) {
		return dependente;
	}

	// Listing
	/**
	 * Carrega lista Tipo Dependente
	 *
	 * @return
	 */
	public List<TipoDependente> getListaTipoDependente() {
		return Arrays.asList(TipoDependente.values());
	}

	public List<Dependente> getListaDependentes() {
		if (listaDependentes == null) {
			listaDependentes = repository.findAllOrderByNomeAsc();
		}
		return listaDependentes;
	}

	/**
	 * Total dependente ativo
	 * 
	 * @return
	 */
	public Long getTotalDepentende() {
		return repository.countDependenteAtivo();
	}

	// Validations

	/**
	 * Checa faixa etaria
	 */
	@Asynchronous
	public void checaFaixaEtariaDependente() {
		System.out.println(">>> checando faixa etaria dependente");
		// Carrega lista dos dependentes do beneficiario
		for (Dependente d : this.getListaDependentes()) {

			// Carrega lista de faixa etaria existentes
			for (FaixaEtaria faixaEtaria : faixaEtariaBean.getListaFaixaEtaria()) {

				// Faz a magica para setar a faixa etaria de acordo com a
				// idade
				if (d.getIdade() >= faixaEtaria.getPeriodoInicial() && d.getIdade() <= faixaEtaria.getPeriodoFinal()) {
					d.setFaixaEtaria(faixaEtaria);
					repository.save(d);
				}
			}
		}
	}

	/**
	 * Checa faixa etaria do dependente cadastrado
	 */
	public void checaFaixaEtariaDependente(Dependente d) {
		System.out.println(">>> adicionando faixa etaria do dependente");

		// Carrega lista de faixa etaria existentes
		for (FaixaEtaria faixaEtaria : faixaEtariaBean.getListaFaixaEtaria()) {

			// Faz a magica para setar a faixa etaria de acordo com a
			// idade
			if (d.getIdade() >= faixaEtaria.getPeriodoInicial() && d.getIdade() <= faixaEtaria.getPeriodoFinal()) {
				d.setFaixaEtaria(faixaEtaria);
//				repository.save(d);
			}
		}
	}

	/**
	 * Checa se CPF ja existe em dependentes
	 *
	 * @return
	 */
	public boolean validaDependente(String cpf) {
		return repository.findByCpf(cpf).isEmpty();
	}

	/**
	 * Checa se CPF ja existe em beneficiarios
	 * 
	 * @return
	 */
	public boolean validaDependenteEmTitular() {
		return beneficiarioBean.verificaCPFDependenteEmBeneficiario(dependente.getCpf());
	}

	/**
	 * Verifica cpf para dependente no ato do cadastro
	 */
	public void checaCpfDependente() {
		if (!validaDependente(dependente.getCpf())) {
			JsfUtil.error("CPF existente");
		} else if (!beneficiarioBean.verificaCPFDependenteEmBeneficiario(dependente.getCpf())) {
			JsfUtil.error("CPF existente para titular");
		}
	}

	public Dependente getDependente() {
		if (dependente == null) {
			dependente = new Dependente();
		}
		return dependente;
	}

	public void setDependente(Dependente dependente) {
		this.dependente = dependente;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue.toUpperCase();
	}

}
