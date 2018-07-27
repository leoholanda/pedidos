package br.com.aee.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import br.com.aee.model.*;
import br.com.aee.repository.*;
import org.hibernate.Session;
import org.primefaces.event.SelectEvent;

import br.com.aee.report.ExecutorRelatorio;
import br.com.aee.util.Adress;
import br.com.aee.util.JsfUtil;
import br.com.aee.util.Message;

@Named("beneficiarioMB")
@ViewScoped
public class BeneficiarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletResponse response;

	@Inject
	private EntityManager manager;

	@Inject
	private BeneficiarioRepository repository;

	@Inject
	private DependenteRepository dependenteRepository;

	@Inject
	private ConvenioRepository convenioRepository;

	@Inject
	private PlanoRepository planoRepository;

	@Inject
	private FaturaRepository faturaRepository;

	@Inject
	private MesFaturaRepository mesFaturaRepository;

	@Inject
	private FaixaEtariaBean faixaEtariaBean;

	@Inject
	private DependenteBean dependenteBean;

	@Inject
	private FaturaBean faturaBean;

	private Beneficiario beneficiario;

	private Dependente dependente;

	private Endereco endereco;

	private Plano plano = new Plano();

	private MesFatura mesFatura;

	private List<Beneficiario> listaBeneficiarios;

	private List<Plano> listaDePlanosParaRemover;

	private List<Plano> listaPlanosAtivos;

	private String searchValue;

	private Long idConvenio;

	@PostConstruct
	public void init() {
		beneficiario = new Beneficiario();
		dependente = new Dependente();
		plano = new Plano();
		endereco = new Endereco();
		mesFatura = new MesFatura();
		listaPlanosAtivos = new ArrayList<>();

		listaBeneficiarios = repository.findAllOrderByNomeAsc();
		listaDePlanosParaRemover = new ArrayList<>();
		dependenteBean.excluirDependente();
	}

	// Actions
	/**
	 * Salva beneficiario
	 */
	public void save() {
		try {
			if (validaBeneficiario()) {
				beneficiario.setStatus(Status.ATIVADO);
				beneficiario.setEndereco(endereco);
				temPlanoDeSaude();

				repository.save(beneficiario);

				// Adiciona faixa etiaria ao beneficiario
				checaFaixaEtaria(beneficiario);

				beneficiario = new Beneficiario();
				endereco = new Endereco();
				plano = new Plano();

				JsfUtil.info(Message.MSG_SAVE);
			} else {
				JsfUtil.error(Message.BENEFICIARIO + Message.MSG_EXISTING);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			JsfUtil.fatal(Message.MSG_CATCH);
		}
	}

	public void temPlanoDeSaude() {
		Convenio convenio = new Convenio();
		if (beneficiario.getTemPlanoDeSaude()) {
			System.out.println(">> Beneficiario com plano de saude");
			convenio = convenioRepository.findBy(1l);
		} else {
			System.out.println(">> Somente mensalidade na fatura");
			convenio = convenioRepository.findBy(2l);
			beneficiario.setAcomodacao(null);
		}
		beneficiario.getPlanos().add(plano);
		plano.setBeneficiario(beneficiario);
		plano.setConvenio(convenio);
	}

	/**
	 * Atualiza dados do beneficiario
	 */
	public void update() {
		try {
			if (!listaDePlanosParaRemover.isEmpty()) {
				for (Plano plano : listaDePlanosParaRemover) {
					System.out.println(">>> Removendo Plano " + plano.getConvenio().getNome());
					plano = planoRepository.findBy(plano.getId());
					planoRepository.remove(plano);
					planoRepository.flush();
					plano = new Plano();
				}
			}
			
			for (Dependente dependente : beneficiario.getDependentes()) {
				dependente.setAcomodacao(beneficiario.getAcomodacao());
				
			}
			
			repository.save(beneficiario);

			JsfUtil.info(Message.MSG_UPDATE);
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

			String context = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
			try {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(context + "/novo-beneficiario.xhtml?id=" + beneficiario.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			JsfUtil.fatal(Message.MSG_CATCH);
		}
	}

	public String indexBeneficiario() {
		buscar();
		return Adress.INDEX_BENEFICIARIO + "id=" + beneficiario.getId();
	}

	public void novoDependente() {
		dependente = new Dependente();
	}

	/**
	 * Desativa beneficiário
	 *
	 */
	public void desativar() {
		Calendar hoje = Calendar.getInstance();
		if (beneficiario != null) {
			beneficiario.setStatus(Status.DESATIVADO);
			repository.save(beneficiario);

			/**
			 * Fecha fatura em aberto para não correr juros
			 */
			for (Fatura f : faturaRepository.findFaturaPendenteBeneficiario(beneficiario)) {
				System.out.println("Fatura " + f.getId() + " do " + f.getPlano().getBeneficiario().getNome());
				System.out.println("hoje: " + hoje.get(Calendar.DAY_OF_MONTH));
				f.setDataPagamento(hoje);
				faturaRepository.save(f);
			}

			JsfUtil.info(Message.MSG_SAVE);
		}
	}

	/**
	 * Reativa beneficiário
	 *
	 */
	public void ativar() {
		if (beneficiario != null) {
			beneficiario.setStatus(Status.ATIVADO);
			repository.save(beneficiario);

			JsfUtil.info(Message.MSG_SAVE);
		}
	}

	/**
	 * Adiciona dependente a lista
	 */
	public void adicionarDependente() {
		if (dependenteBean.validaDependente(dependente.getCpf())) {
			dependente.setAcomodacao(beneficiario.getAcomodacao());
			beneficiario.getDependentes().add(dependente);
			dependente.setBeneficiario(beneficiario);

			// Adiciona faixa etaria ao dependente
			dependenteBean.checaFaixaEtariaDependente(dependente);
		} else {
			JsfUtil.info("CPF existente");
		}
	}

	/**
	 * Remove apenas o dependente adicionado a lista mas nao persistido
	 */
	public void removerDependente() {
		beneficiario.getDependentes().remove(dependente);
	}

	/**
	 * Remove dependente populado -> Exclui do banco
	 */
	public String removerDependentePopulado() {
		dependente.setStatus(Status.DESATIVADO);
		dependenteRepository.save(dependente);
		JsfUtil.info("Dependente Removido!");

		return Adress.NOVO_BENEFICIARIO + "id=" + beneficiario.getId();
	}

	/**
	 * Guarda plano ao beneficiario
	 */
	public void adicionaPlano() {
		Convenio convenio = new Convenio();
		convenio = convenioRepository.findBy(idConvenio);
		System.out.println(">> Adicionando plano: " + convenio.getNome());

		if (contemPlanoAdicionado(beneficiario.getPlanos(), idConvenio)) {
			JsfUtil.error("Plano já adicionado!");
		} else {
			// this.getListaServicos().add(plano);
			beneficiario.getPlanos().add(plano);
			plano.setBeneficiario(beneficiario);
			plano.setConvenio(convenio);
		}
		plano = new Plano();
	}

	// verifica se a lista ja contem plano adicionado para nao duplicar
	public Boolean contemPlanoAdicionado(List<Plano> planos, Long convenio) {
		for (Plano p : planos) {
			if (p.getConvenio().getId().equals(convenio)) {
				return true;
			}
		}
		return false;
	}

	public void removerServico(Plano plano) {
		beneficiario.getPlanos().remove(plano);
		plano = new Plano();
	}

	public void removerPlano(Plano plano) {
		if(beneficiario != null) {
			plano.setAtivo(false);
			planoRepository.save(plano);
			System.out.println(">> Plano desativado!");
		}else {
			beneficiario.getPlanos().remove(plano);
			listaDePlanosParaRemover.add(plano);
		}
		this.getListaPlanosAtivos();

		plano = new Plano();
		JsfUtil.info("Serviço removido!");
	}

	public void ativaPlanoDeSaude() {
//		boolean resultado = false;
//		if(beneficiario.getTemPlanoDeSaude()){
//			resultado = true;
//		}
//		plano.setAtivo(resultado);
//		planoRepository.save(plano);
//		String msg = resultado ? "Plano habilitado!" : "Plano desativado!";
//
//		System.out.println(msg);
	}

	/**
	 * SelectEvent para selecionar via ajax linha na tabela
	 *
	 * @param event
	 * @return
	 */
	public Beneficiario selecionaBeneficiarioNaTabela(SelectEvent event) {
		return beneficiario;
	}

	public void gerarFaturaParaBeneficiarioSelecionado() {
		System.out.println(">>> Gerando fatura para " + beneficiario.getNomeComIniciaisMaiuscula());
		faturaBean.geraFaturaIndividual(beneficiario);
	}

	/**
	 * Busca id do beneficiario
	 */
	public void buscar() {
		beneficiario = repository.findBy(beneficiario.getId());
		listaPlanosAtivos = planoRepository.findByPlanoAtivo(beneficiario);
	}

	/**
	 * Busca cpf do beneficiario
	 */
	public void buscaCpf() {
		beneficiario = repository.findByCpfParaBeneficiario(beneficiario.getCpf());
	}

	/**
	 * Exporta para pdf declaracao IPFR
	 */
	public void emitirDeclaracaoImpostoDeRendaPDF() {
		if (beneficiario != null) {
			Map<String, Object> parametros = new HashMap<>();
			parametros.put("p_beneficiario_id", beneficiario.getId());

			ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/declaracao_irpf.jasper", this.response,
					parametros, "Declaração Imposto de Renda.pdf");

			Session session = manager.unwrap(Session.class);
			session.doWork(executor);

			if (executor.isRelatorioGerado()) {
				facesContext.responseComplete();
			} else {
				JsfUtil.error("A execução do relatório não retornou dados");
			}
		}
	}

	// Listing

	/**
	 * Autocomplete beneficiario nome
	 *
	 * @param nome
	 * @return
	 */
	public List<Beneficiario> autocompleteFiador(String nome) {
		nome = nome.toUpperCase();
		return repository.findByNome(nome + "%");
	}

	/**
	 * Lista servicos do associado
	 */
	public List<Plano> getListaServicos() {
		return planoRepository.findByServico(beneficiario);
	}

	/**
	 * Pesquisa beneficiario com 3 parametros CPF, Matricula e Nome
	 */
	public void pesquisar() {
		listaBeneficiarios = repository.findByCpfOrMatriculaOrNomeLikeOrderByNomeAsc(searchValue, searchValue,
				"%" + searchValue + "%");

		if (listaBeneficiarios.isEmpty()) {
			JsfUtil.error("Nenhum registro encontrado");
		}
	}

	public void servidores() {
		listaBeneficiarios = repository.findByServidor();
	}

	public Long getTotalAssociados() {
		return repository.countBeneficiarioAtivo();
	}

	public Long getTotalBeneficiarios() {
		return repository.countBeneficiarioAtivo2();
	}

	/**
	 * Lista beneficiarioes em ordem alfabetica
	 *
	 * @return
	 */
	public List<Beneficiario> getListaBeneficiarios() {
		return listaBeneficiarios;
	}


	// Validations

	/**
	 * Checa se existe fatura para o beneficiario
	 */
	public void existeFaturaDoMesAtual() {
		faturaBean.checaExisteFaturaDesteMes(beneficiario);
	}

	/**
	 * Checa se existe fatura para o beneficiario
	 */
	public boolean isExisteFaturaDoMesAtual() {
		return faturaBean.isExisteFatura(beneficiario);
	}

	/**
	 * Checa existencia do beneficiario
	 *
	 * @return
	 */
	public boolean isBeneficiarioExiste() {
		return beneficiario.getId() != null;
	}

	public boolean isDependenteExiste() {
		return this.getDependente().getId() != null;
	}

	/**
	 * Checa se a lista de dependetes é vazia
	 *
	 * @return
	 */
	public boolean isListaDependentesVazia() {
		return beneficiario.getDependentes().isEmpty();
	}

	/**
	 * Integridade do beneficiario - verifica se o CPF digitado ja existe
	 *
	 * @return
	 */
	public boolean validaBeneficiario() {
		return repository.findByCpf(beneficiario.getCpf()).isEmpty();
	}

	public boolean verificaCPFDependenteEmBeneficiario(String cpf) {
		return repository.findByCpfAndStatus(cpf).isEmpty();
	}

	public boolean checaSeCpfExisteEmDependente() {
		return dependenteBean.validaDependente(beneficiario.getCpf());
	}

	/**
	 * Checa cpf no ato do cadastro
	 */
	public void verificaCpfExistente() {
		if (!validaBeneficiario()) {
			JsfUtil.error("CPF existente");
		} else if (!checaSeCpfExisteEmDependente()) {
			JsfUtil.error("CPF existente para dependente");
		}
	}

	/**
	 * Verifica cpf para dependente no ato do cadastro
	 */
	public void checaCpfDependente() {
		System.out.println(">>> checaCpfDependente");
		if (!dependenteBean.validaDependente(dependente.getCpf())) {
			JsfUtil.error("CPF existente");
		} else if (!verificaCPFDependenteEmBeneficiario(dependente.getCpf())) {
			JsfUtil.error("CPF existente para titular");
		}
	}

	/**
	 * Para acesso de beneficiario no sistema
	 * 
	 * @return
	 */
	public String autorizarBeneficiario() {
		if (!validaAreaBeneficiario(beneficiario.getCpf())) {
			return Adress.AREA_BENEFICIARIO + "id=" + beneficiario.getCpf();
		} else {
			JsfUtil.error("CPF não encontrado");
			return null;
		}
	}

	public boolean validaAreaBeneficiario(String cpf) {
		return repository.findByCpfAndStatus(cpf).isEmpty();
	}

	/**
	 * Verifica lista de plano
	 * 
	 * @return
	 */
	public Boolean listaDePlanoNaoPodeSerVazio() {
		return !beneficiario.getPlanos().isEmpty();
	}

	/**
	 * Checa se ha servicos adicionados
	 */
	public boolean isServicos() {
		return planoRepository.findByServico(beneficiario).isEmpty();
	}

	/**
	 * Checa faixa etaria de todos os beneficiario
	 */
	public void checaFaixaEtariaDosBeneficiarios() {
		 if (isMudancaDeFaixaEtaria() && diaDoMes() >= 20 && diaDoMes() <= 31) {
			// Carrega lista dos beneficiarios
			for (Beneficiario beneficiario : this.getListaBeneficiarios()) {

				// Carrega lista de faixa etaria existentes
				for (FaixaEtaria faixaEtaria : faixaEtariaBean.getListaFaixaEtaria()) {

					// Faz a magica para setar a faixa etaria de acordo com a
					// idade
					if (beneficiario.getIdade() >= faixaEtaria.getPeriodoInicial()
							&& beneficiario.getIdade() <= faixaEtaria.getPeriodoFinal()) {
						beneficiario.setFaixaEtaria(faixaEtaria);
						repository.save(beneficiario);
					}
				}
			}

			// Invoca faixa etária do dependente
			dependenteBean.checaFaixaEtariaDependente();

			mesFatura.setEvento("Aniversario");
			mesFaturaRepository.save(mesFatura);
		}
	}

	/**
	 * Checa faixa etaria do beneficiario
	 */
	public void checaFaixaEtaria(Beneficiario beneficiario) {
		System.out.println(">>> adicionando faixa etaria do beneficiario");

		// Carrega lista de faixa etaria existentes
		for (FaixaEtaria faixaEtaria : faixaEtariaBean.getListaFaixaEtaria()) {

			// Faz a magica para setar a faixa etaria de acordo com a
			// idade
			if (beneficiario.getIdade() >= faixaEtaria.getPeriodoInicial()
					&& beneficiario.getIdade() <= faixaEtaria.getPeriodoFinal()) {
				beneficiario.setFaixaEtaria(faixaEtaria);
				repository.save(beneficiario);
			}
		}
	}

	/**
	 * Informa o dia do mes
	 *
	 * @return
	 */
	public int diaDoMes() {
		Calendar hoje = Calendar.getInstance();
		hoje.get(Calendar.DAY_OF_MONTH);
		int diaMes = hoje.get(Calendar.DAY_OF_MONTH);

		return diaMes;
	}

	public boolean isMudancaDeFaixaEtaria() {
		Calendar hoje = Calendar.getInstance();
		return mesFaturaRepository.findByBeneficiarioAniversario(hoje).isEmpty();
	}

	public Beneficiario getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(Beneficiario beneficiario) {
		this.beneficiario = beneficiario;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public Dependente getDependente() {
		return dependente;
	}

	public Plano getPlano() {
		return plano;
	}

	public void setDependente(Dependente dependente) {
		this.dependente = dependente;
	}

	public void setPlano(Plano plano) {
		this.plano = plano;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue.toUpperCase();
	}

	public Long getIdConvenio() {
		return idConvenio;
	}

	public void setIdConvenio(Long idConvenio) {
		this.idConvenio = idConvenio;
	}

	private Boolean celular = true;

	public Boolean getCelular() {
		return celular;
	}

	public void setCelular(Boolean celular) {
		this.celular = celular;
	}

	public List<Plano> getListaPlanosAtivos() {
		return listaPlanosAtivos;
	}
}
