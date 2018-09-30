package br.com.aee.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import br.com.aee.model.Beneficiario;
import br.com.aee.model.Fatura;
import br.com.aee.model.FaturaParcelada;
import br.com.aee.model.Mes;
import br.com.aee.model.MesFatura;
import br.com.aee.model.Plano;
import br.com.aee.model.Status;
import br.com.aee.repository.DependenteRepository;
import br.com.aee.repository.FaturaParceladaRepository;
import br.com.aee.repository.FaturaRepository;
import br.com.aee.repository.MesFaturaRepository;
import br.com.aee.repository.PlanoRepository;
import br.com.aee.thread.EnviaCobrancaThread;
import br.com.aee.thread.EnviaEmailConfirmacaoDePagamento;
import br.com.aee.thread.EnviaEmailThread;
import br.com.aee.thread.EnviaFaturaIndividualThread;
import br.com.aee.util.Adress;
import br.com.aee.util.JsfUtil;

@Named("faturaMB")
@ViewScoped
public class FaturaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FaturaRepository repository;

	@Inject
	private FaturaParceladaRepository faturaParceladaRepository;

	@Inject
	private MesFaturaRepository mesFaturaRepository;

	@Inject
	private PlanoRepository planoRepository;

	@Inject
	private DependenteRepository dependenteRepository;

	@Inject
	private BeneficiarioBean beneficiarioBean;

	@Inject
	private FluxoCaixaBean fluxoCaixaBean;

	private Fatura fatura = new Fatura();

	private FaturaParcelada faturaParcelada = new FaturaParcelada();

	private MesFatura mesFatura = new MesFatura();

	private String codigoDeSegurancao;

	private List<Fatura> listaFaturas;
	private List<Fatura> listaFaturaAtrasada;
	private List<Fatura> listaFaturaPendente;
	private List<Fatura> listaFaturaPaga;

	private Date dataPagamento = new Date();
	private String searchValue;
	private Integer searchMes;
	private Integer searchAno;
	private Integer numeroDeParcelas = 2;

	// Actions

	@PostConstruct
	public void init() {
		listaFaturaAtrasada = repository.findByFaturaAtrasada();
		listaFaturaPendente = repository.findByFaturaPendenteAndFechada();

		// TODO Gera fatura para beneficiario com status ativo
		this.geraFatura();

//		enviaCobrancaDeFaturaAtrasada();

		// TODO Invoca o metodo para verificar fatura atrasada
		this.faturaAtrasada();

		// TODO Aplica multa por atraso
		this.aplicaMultaPorAtraso();

		// TODO Aplica juros ao dia
		this.aplicaJurosAoDia();

		// TODO Verifica data de aniversario para possivel mudança de faixa-etaria
		this.beneficiarioBean.checaFaixaEtariaDosBeneficiarios();
	}

	/**
	 * Cancela pagamento da fatura
	 */
	public void cancelarPagamento() {
		if (fatura.getStatus().equals(Status.PAGO)) {
			fatura.setStatus(Status.PENDENTE);
			fatura.setDataPagamento(null);
			fatura.setValorPago(null);
			fatura.setValorDoResiduo(null);
			repository.save(fatura);

			JsfUtil.info("Pagamento da fatura " + fatura.getId() + " cancelado com sucesso!");
		}
	}

	/**
	 * Remove fatura
	 */
	public void removerFatura() {
		try {
			fatura = repository.findBy(fatura.getId());
			repository.remove(fatura);
			repository.flush();

			JsfUtil.info("Fatura " + fatura.getId() + " excluída com sucesso!");

			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

			String context = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();

			try {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(context + "/pages/protected/fatura/pesquisa-fatura.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(">>>>> Fatura excluída com sucesso!");
		} catch (Exception e) {
			JsfUtil.error(
					"Há registro de pagamento dessa fatura no fluxo de caixa, para continuar é necessário remover o registro na área citada.");
		}
	}

	/**
	 * Checa fatura atrasada
	 */
	public void faturaAtrasada() {
		if (isFaturaAtrasada()) {
			Date hoje = new Date();

			for (Fatura fatura : repository.findByFaturaPendente()) {
				// TODO Através do Calendar, trabalhamos a data informada e adicionamos 1 dia
				Calendar vencimento = Calendar.getInstance();
				vencimento.setTime(fatura.getVencimento());
				vencimento.add(Calendar.DATE, +1);

				if (vencimento.getTime().before(hoje)) {
					fatura.setStatus(Status.ATRASADO);
					repository.save(fatura);
				}
			}

			mesFatura.setEvento("Atraso");
			mesFaturaRepository.save(mesFatura);
		}
	}

	/**
	 * Checa fatura atrasada
	 */
	public void faturaAtrasadaIndividual(Fatura fatura) {
		Date hoje = new Date();

		// TODO Através do Calendar, trabalhamos a data informada e adicionamos 1 dia
		Calendar vencimento = Calendar.getInstance();
		vencimento.setTime(fatura.getVencimento());
		vencimento.add(Calendar.DATE, +1);

		if (vencimento.getTime().before(hoje)) {
			System.out.println(">>>>> Atualizando Fatura Atrasada");
			fatura.setStatus(Status.ATRASADO);
			repository.save(fatura);
		}
	}

	public void geraFatura() {
		if (isFaturaParaEsseMes()) {

			// TODO Gera fatura automatica do dia 01 ao dia 7
			if (diaDoMes() >= 01 && diaDoMes() <= 29) {
				System.out.println(">> Gerando fatura...");
				this.geraValoresDaFatura();

				mesFatura.setEvento("Fatura");
				mesFaturaRepository.save(mesFatura);
			}
		}
	}

	/**
	 * Envia fatura individual por email
	 */
	public void enviaFaturaPorEmail() {
		if (fatura.getPlano().getBeneficiario().getEmail() != null) {
			System.out.println(">> Enviar fatura por email: codigo " + fatura.getId());
			EnviaEmailThread email = new EnviaEmailThread(repository.findByCodigo(fatura.getId()));
			email.start();

			String nomeDoBeneficiario = fatura.getPlano().getBeneficiario().getNomeComIniciaisMaiuscula();
			JsfUtil.info("Email enviado para " + nomeDoBeneficiario);
		} else {
			JsfUtil.error("Beneficiário sem email cadastrado");
		}
	}

	/**
	 * Envia email de cobrança somados 10,15,20 dias após o vencimento
	 */
	public void enviaCobrancaDeFaturaAtrasada() {
		if (diaDoMes() == 7 || diaDoMes() == 14 || diaDoMes() == 21 || diaDoMes() == 29) {
			if (isCobrancaParaEsseDia()) {
				EnviaCobrancaThread cobranca = new EnviaCobrancaThread(repository.findByFaturaAtrasada());
				cobranca.start();

				mesFatura.setEvento("Cobranca");
				mesFaturaRepository.save(mesFatura);
			}
		}
	}

	/**
	 * Calcula o plano de saude
	 * 
	 * @param plano
	 */
	public void geraFaturaComPlanoDeSaude(Plano plano) {

		// TODO Verifica se o plano do beneficiario é enfermaria
		if (plano.getBeneficiario().isEnfermaria()) {
			Double valorEnfermaria = this
					.valorAcomodacao(plano.getBeneficiario().getFaixaEtaria().getValorEnfermaria());

			// TODO Se o beneficiario tiver dependente soma todos
			Double valorDependente = this.valorDependente(plano.getBeneficiario());
			fatura.setValorPlanoDeSaude(valorEnfermaria + valorDependente);

			fatura.setValorTotalGerado((fatura.getValorPlanoDeSaude() + fatura.getValorMensalidade()
					+ fatura.getValorServicosAdicionais()));

			fatura.setValorTotal((fatura.getValorPlanoDeSaude() + fatura.getValorMensalidade()
					+ fatura.getResiduoDescontado() + fatura.getValorServicosAdicionais()));

			// TODO Acomodacao apartamento
		} else {
			Double valorApartamento = this
					.valorAcomodacao(plano.getBeneficiario().getFaixaEtaria().getValorApartamento());

			// TODO Se o beneficiario tiver dependente soma todos
			Double valorDependente = this.valorDependente(plano.getBeneficiario());
			fatura.setValorPlanoDeSaude(valorApartamento + valorDependente);

			fatura.setValorTotalGerado((fatura.getValorPlanoDeSaude() + fatura.getValorMensalidade()
					+ fatura.getValorServicosAdicionais()));
			fatura.setValorTotal((fatura.getValorPlanoDeSaude() + fatura.getValorMensalidade()
					+ fatura.getResiduoDescontado() + fatura.getValorServicosAdicionais()));

		}
	}

	/**
	 * Gera fatura somente com a taxa da mensalidade(sem plano de saude)
	 * 
	 * @param plano
	 */
	public void geraFaturaSemPlanoDeSaude(Plano plano) {
		fatura.setValorPlanoDeSaude(0.00);
		fatura.setValorTotalGerado((fatura.getValorMensalidade()) + fatura.getValorServicosAdicionais());
		fatura.setValorTotal(
				(fatura.getValorMensalidade()) + fatura.getResiduoDescontado() + fatura.getValorServicosAdicionais());
	}

	/**
	 * Adiciona servicos
	 * 
	 * @param lista
	 */
	public void servicosAdicionais(Beneficiario beneficiario) {
		Double total = 0.00;
		for (Plano plano : planoRepository.findByServico(beneficiario)) {
			total += plano.getConvenio().getValor();
		}
		// TODO seta total dos valores adicionais
		fatura.setValorServicosAdicionais(total);
	}

	/**
	 * Gera fatura
	 */
	public void geraValoresDaFatura() {
		for (Plano plano : planoRepository.findByPlanoBeneficiarioAtivado(Status.ATIVADO)) {
			Double mensalidade = 0.00;

			// TODO Calcula mensalidade do associado
			calculaMensalidade(plano, mensalidade);

			// TODO verifica se há servicos adicionais
			servicosAdicionais(plano.getBeneficiario());

			// TODO tras o residuo da fatura anterior e soma
			aplicaResiduoNaFatura(plano);
			fatura.setResiduoDescontado(aplicaResiduoNaFatura(plano));

			// TODO calcula valor do plano de saude
			if (plano.getBeneficiario().getTemPlanoDeSaude()) {
				this.geraFaturaComPlanoDeSaude(plano);

				// TODO se nao tiver plano gera somente mensalidade
			} else {
				this.geraFaturaSemPlanoDeSaude(plano);
			}

			fatura.setPlano(plano);
			fatura.setDataPagamento(null);

			this.dataDeVencimentoFatura();

			repository.save(fatura);

			/**
			 * Envia email de forma assincrona
			 */
			EnviaFaturaIndividualThread email = new EnviaFaturaIndividualThread(fatura);
			email.start();

			fatura = new Fatura();
		}
	}

	/**
	 * Gera fatura individual
	 */
	public void geraFaturaIndividual(Beneficiario beneficiario) {
		try {
			if (!planoRepository.findByPlanoBeneficiario(beneficiario).isEmpty()) {
				for (Plano plano : planoRepository.findByPlanoBeneficiario(beneficiario)) {
					System.out.println(">>>>> Gerando fatura individual para: " + beneficiario.getNome());
					Double mensalidade = 0.00;

					// TODO calcula mensalidade
					calculaMensalidade(plano, mensalidade);

					// TODO verifica se há servicos adicionais
					servicosAdicionais(plano.getBeneficiario());

					// TODO tras o residuo da fatura anterior e soma
					aplicaResiduoNaFatura(plano);
					fatura.setResiduoDescontado(aplicaResiduoNaFatura(plano));

					// TODO calcula valor do plano de saude
					if (plano.getBeneficiario().getTemPlanoDeSaude()) {
						this.geraFaturaComPlanoDeSaude(plano);
						System.out.println(">>>>> Fatura Com Plano de Saude");

						// TODO se nao tiver plano de saude gera somente mensalidade
					} else {
						this.geraFaturaSemPlanoDeSaude(plano);
						System.out.println(">>>>> Fatura Sem Plano de Saude");
					}

					// TODO pega a data de ontem para setar os juros em caso de atraso
					Calendar ontem = Calendar.getInstance();
					ontem.add(Calendar.DAY_OF_MONTH, -1);

					fatura.setPlano(plano);
					fatura.setDataPagamento(null);
					fatura.setDataJuros(ontem);

					this.dataDeVencimentoFaturaIndividual();

					repository.save(fatura);

					JsfUtil.info("Fatura gerada com sucesso!");

					this.aplicaMultaPorAtraso(fatura);
					this.aplicaJurosAoDia(fatura);

					fatura = new Fatura();
				}
			} else {
				JsfUtil.error("Desculpe. Nenhum serviço ou convênio esta vinculado ao associado!");
			}
		} catch (Exception e) {
			JsfUtil.error("Algo aconteceu. Não foi possível gerar fatura para o associado!");
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * Tras o residuo da fatura anterior
	 *
	 * @param plano
	 * @return
	 */
	public Double aplicaResiduoNaFatura(Plano plano) {
		Double residuo = 0.00;
		for (Fatura f : repository.findByFaturaMesAno(mesAtual(), anoAtual())) {
			if (f.getPlano() == plano && f.getValorDoResiduo() != null) {

				residuo = f.getValorDoResiduo();
				fatura.setResiduoDescontado(f.getValorDoResiduo());
			} else {
				fatura.setResiduoDescontado(0.01);
			}
		}
		return residuo;
	}

	/**
	 * Gera mensalidade para beneficiario
	 *
	 * @param plano
	 * @param mensalidade
	 */
	public void calculaMensalidade(Plano plano, Double mensalidade) {
		mensalidade = 0.00;
		if (plano.getBeneficiario().getSalario() != null) {

			// Gera mensalidade para servidor
			if (plano.getBeneficiario().isBeneficiarioServidor()) {

				if (plano.getBeneficiario().isConsignado()) {
					fatura.setValorMensalidade(mensalidade);

					// Calcula mensalidade para não consignado
				} else {
					// Mensalidade é 0,8% do salário
					mensalidade = plano.getBeneficiario().getSalario() * 0.008;
					fatura.setValorMensalidade(mensalidade);
				}
			} else {
				mensalidade = 70.00;
				fatura.setValorMensalidade(mensalidade);
			}

		} else {
			fatura.setValorMensalidade(mensalidade);
		}

	}

	/**
	 * Tras o valor do plano do dependente
	 *
	 * @param beneficiario
	 * @return
	 */
	public Double valorDependente(Beneficiario beneficiario) {
		if (beneficiario.getDependentes().isEmpty()) {
			return 0.00;
		} else {
			Double valorDependente = 0.00;
			if (beneficiario.isApartamento()) {
				valorDependente = dependenteRepository.sumAcomodacaoApartamento(beneficiario);
			} else {
				valorDependente = dependenteRepository.sumAcomodacaoEnfermaria(beneficiario);
			}
			return valorDependente;
		}
	}

	public Double valorAcomodacao(Double valor) {
		return valor;
	}

	/**
	 * Define o dia do vencimento da fatura
	 */
	public void dataDeVencimentoFatura() {
		int domingo = 1;
		int sabado = 7;
		Date dataHoje = new java.util.Date();

		Calendar c = Calendar.getInstance();
		c.setTime(dataHoje);

		// c.set(anoAtual(), mesAtual() + 1, 5);

		// TODO Dia da semana domingo, vencimento passa para dia 6
		if (c.get(Calendar.DAY_OF_WEEK) == domingo) {
			c.set(anoAtual(), mesAtual(), 6);

			// TODO Dia da semana sábado, vencimento passa para dia 7
		} else if (c.get(Calendar.DAY_OF_WEEK) == sabado) {
			c.set(anoAtual(), mesAtual(), 7);
		} else {
			c.set(anoAtual(), mesAtual(), 5);
		}

		Date vencimento = c.getTime();

		fatura.setVencimento(vencimento);
	}

	/**
	 * Define o dia do vencimento para fatura individual
	 */
	public void dataDeVencimentoFaturaIndividual() {
		Date dataHoje = new java.util.Date();

		Calendar c = Calendar.getInstance();
		c.setTime(dataHoje);

		c.set(anoAtual(), mesAtual(), 5);

		Date vencimento = c.getTime();

		fatura.setVencimento(vencimento);
	}

	/**
	 * Calculo da multa por atraso
	 */
	public void aplicaMultaPorAtraso() {
		if (!repository.findByCalculoDaMulta().isEmpty()) {
			System.out.println(">>>>> Aplicando multa por atraso");
			Double multa = 0.00;
			for (Fatura f : repository.findByCalculoDaMulta()) {
				System.out.println(">>>>> Calculando multa para: " + f.getPlano().getBeneficiario().getNome());
				multa = f.getValorTotalGerado() * 0.02;
				Double calculo = multa + f.getValorTotalGerado();

				f.setMultaAplicada(true);
				f.setValorTotal(calculo);

				repository.save(f);
			}
		}
	}

	/**
	 * Usado para fatura individual - apenas seta multa aplicada para true
	 */
	public void aplicaMultaPorAtraso(Fatura fatura) {
		Calendar hoje = Calendar.getInstance();
		int dia = hoje.get(Calendar.DAY_OF_MONTH);
		int mes = hoje.get(Calendar.MONTH);

		GregorianCalendar dataCal = new GregorianCalendar();
		dataCal.setTime(fatura.getVencimento());
		int diaDaFatura = dataCal.get(Calendar.DAY_OF_MONTH);
		int mesDaFatura = dataCal.get(Calendar.MONTH);

		if (dia > diaDaFatura && mes >= mesDaFatura) {
			System.out
					.println(">>>>> Aplicando multa por atraso para: " + fatura.getPlano().getBeneficiario().getNome());
			fatura.setMultaAplicada(true);

			repository.save(fatura);
		}
	}

	/**
	 * Calculo dos juros ao dia para fatura individual
	 */
	public void aplicaJurosAoDia(Fatura fatura) {
		Double juros = 0.00;
		Double multa = 0.00;
		Calendar hoje = Calendar.getInstance();
		System.out.println(">>>>> Calculando juros para: " + fatura.getPlano().getBeneficiario().getNome());
		multa = fatura.getValorTotalGerado() * 0.02;
		int ultimoDiaGerado = fatura.getDataJuros().get(Calendar.DAY_OF_MONTH);
		int dia = hoje.get(Calendar.DAY_OF_MONTH);

		if (dia != ultimoDiaGerado) {
			juros = fatura.getValorTotalGerado() * 0.00033 * fatura.getDiasAtrasados();

			System.out.println(">>>>> Juros: " + juros);
			System.out.println(">>>>> Multa: " + multa);
			System.out.println(">>>>> Residuo: " + fatura.getResiduoDescontado());

			Double calculo = juros + fatura.getValorTotalGerado() + multa + fatura.getResiduoDescontado();

			System.out.println(">>>>> Calculo: " + calculo);

			fatura.setValorTotal(calculo);
			fatura.setDataJuros(hoje);
			repository.save(fatura);
		}
	}

	/**
	 * Calculo dos juros ao dia
	 */
	public void aplicaJurosAoDia() {
		if (isJurosParaEsseDia()) {
			if (repository.findByJuros().isEmpty()) {
				Double juros = 0.00;
				Double multa = 0.00;
				Calendar hoje = Calendar.getInstance();
				for (Fatura f : repository.findByJuros()) {
					if (f.getPlano().getBeneficiario().getStatus() == Status.ATIVADO) {
						System.out.println(">>> Calculando juros para: " + f.getPlano().getBeneficiario().getNome());
						multa = f.getValorTotalGerado() * 0.02;
						int ultimoDiaGerado = f.getDataJuros().get(Calendar.DAY_OF_MONTH);
						int dia = hoje.get(Calendar.DAY_OF_MONTH);

						if (dia != ultimoDiaGerado) {
							juros = f.getValorTotalGerado() * 0.00033 * f.getDiasAtrasados();
							Double calculo = juros + f.getValorTotalGerado() + multa
									+ getResiduoAplicado(f.getResiduoDescontado());
							f.setValorTotal(calculo);
							f.setDataJuros(hoje);
							repository.save(f);
						}
					}
				}
				
				mesFatura.setEvento("Juros");
				mesFaturaRepository.save(mesFatura);
			}
		}
	}

	public Double getResiduoAplicado(Double residuo) {
		if (residuo != null) {
			return residuo;
		} else {
			return 0.00;
		}
	}

	public void fecharFatura() {
		fatura.setDataPagamento(Calendar.getInstance());
		fatura.setStatus(Status.FECHADO);
		repository.save(fatura);
		JsfUtil.info("Fatura fechada com sucesso!");
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

	public int mesAtual() {
		Calendar hoje = Calendar.getInstance();
		hoje.get(Calendar.MONTH);
		int mes = hoje.get(Calendar.MONTH);

		return mes;
	}

	public int anoAtual() {
		Calendar hoje = Calendar.getInstance();
		hoje.get(Calendar.YEAR);
		int ano = hoje.get(Calendar.YEAR);

		return ano;
	}

	/**
	 * Nome por extenso mês da fatura
	 *
	 * @return
	 */
	public String getMesAtualPorExtenso() {
		Date hoje = new Date();
		GregorianCalendar dataCal = new GregorianCalendar();
		dataCal.setTime(hoje);
		int mes = dataCal.get(Calendar.MONTH);

		String meses[] = { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro",
				"Outubro", "Novembro", "Dezembro" };

		return (meses[mes]);
	}

	public int getAnoAtual() {
		Calendar hoje = Calendar.getInstance();
		hoje.get(Calendar.YEAR);
		int ano = hoje.get(Calendar.YEAR);

		return ano;
	}

	/**
	 * Pesquisa fatura pelo nome beneficiario
	 */
	public void pesquisar() {
		listaFaturas = repository.findByBeneficiario("%" + searchValue + "%");

		if (listaFaturas.isEmpty()) {
			try {
				long codigo = Long.parseLong(searchValue);
				listaFaturas = repository.findByCodigo(codigo);
			} catch (Exception e) {
				JsfUtil.error("Nenhum registro encontrado");
			}
		}
	}

	/**
	 * Pesquisa fatura atrasada pelo nome beneficiario
	 */
	public void pesquisarFaturaAtrasada() {
		listaFaturaAtrasada = repository.findByBeneficiarioFaturaAtrasada("%" + searchValue + "%", Status.ATRASADO);

		if (listaFaturaAtrasada.isEmpty()) {
			try {
				long codigo = Long.parseLong(searchValue);
				listaFaturaAtrasada = repository.findByCodigoFaturaAtrasada(codigo);
			} catch (Exception e) {
				JsfUtil.error("Nenhum registro encontrado");
			}
		}

	}

	/**
	 * Pesquisa fatura pendente pelo codigo e nome beneficiario
	 */
	public void pesquisarFaturaPendente() {
		listaFaturaPendente = repository.findByBeneficiarioFaturaPendente("%" + searchValue + "%", Status.PENDENTE);

		if (listaFaturaPendente.isEmpty()) {
			try {
				long codigo = Long.parseLong(searchValue);
				listaFaturaPendente = repository.findByCodigoFaturaPendente(codigo);
			} catch (Exception e) {
				JsfUtil.error("Nenhum registro encontrado");
			}
		}
	}

	/**
	 * Pesquisa fatura pendente pelo codigo e nome beneficiario
	 */
	public void pesquisarFaturaPaga() {
		Integer mesAtual = mesAtual() + 1;

		listaFaturaPaga = repository.findByBeneficiarioFaturaPaga("%" + searchValue + "%", mesAtual, anoAtual());

		if (listaFaturaPaga.isEmpty()) {
			try {
				long codigo = Long.parseLong(searchValue);
				listaFaturaPaga = repository.findByCodigoFaturaPaga(mesAtual, anoAtual(), codigo);
			} catch (Exception e) {
				JsfUtil.error("Nenhum registro encontrado");
			}
		}
	}

	/**
	 * Pesquisa fatura mes ano
	 */
	public void pesquisarFaturaMesAno() {
		List<Fatura> faturas = this.getListaFaturaMesAno();
		faturas = repository.findByFaturaMesAno(searchMes, searchAno);
		System.out.println(">>> " + searchMes + " " + searchAno);

		if (faturas.isEmpty()) {
			JsfUtil.error("Nenhum registro encontrado");
		}
	}

	public void enviaEmailDeConfirmacaoDoPagamento() {
		if (fatura.getPlano().getBeneficiario().getEmail() != null) {
			System.out.println(">> Enviar fatura por email: codigo " + fatura.getId());
			EnviaEmailConfirmacaoDePagamento email = new EnviaEmailConfirmacaoDePagamento(
					repository.findByCodigo(fatura.getId()));
			email.start();

		} else {
			JsfUtil.warning(
					"Beneficiário não receberá email de confirmação por falta de informação no email cadastrado");
		}
	}

	/**
	 * Exibe o calculo
	 */
	public void mostraPagamentoCalculadoDoDia() {
		Double juros = 0.00;
		Double multa = 0.00;

		if (dataPagamento.before(fatura.getVencimento()) || dataPagamento.equals(fatura.getVencimento())) {
			fatura.setValorPago(fatura.getValorTotalGerado() + this.getResiduoAplicado(fatura.getResiduoDescontado()));
		} else {

			multa = fatura.getValorTotalGerado() * 0.02;
			juros = fatura.getValorTotalGerado() * 0.00033 * selecaoData();
			Double calculo = fatura.getValorTotalGerado() + multa + juros
					+ this.getResiduoAplicado(fatura.getResiduoDescontado());

			fatura.setValorPago(calculo);
		}
	}

	/**
	 * Seleção do dia ao informar pagamento
	 *
	 * @return
	 */
	public int selecaoData() {
		int diasAtrasados;
		diasAtrasados = (int) ((dataPagamento.getTime() - fatura.getVencimento().getTime()) / 86400000L);

		return diasAtrasados;
	}

	/**
	 * Confirma pagamento da fatura
	 */

	public void efetuarPagamento() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataPagamento);
		String nomeAssociado = fatura.getPlano().getBeneficiario().getNomeComIniciaisMaiuscula();

		if (dataPagamento.before(fatura.getVencimento()) || dataPagamento.equals(fatura.getVencimento())) {
			fatura.setMultaAplicada(false);
			fatura.setValorTotal(fatura.getValorTotalGerado());

			// TODO registro de entrada no caixa
			fluxoCaixaBean.entradaDeCaixa(fatura, fatura.getValorTotalGerado(), calendar, nomeAssociado);

		} else {
			fatura.setMultaAplicada(true);

			// Valor total
			Double multa = fatura.getValorTotalGerado() * 0.02;
			Double juros = fatura.getValorTotalGerado() * 0.00033 * selecaoData();
			Double calculo = fatura.getValorTotalGerado() + juros + multa;

			fatura.setValorTotal(calculo);

			if (fatura.getValorTotal() == fatura.getValorPago()) {
				fatura.setValorDoResiduo(0.00);
			}
			// TODO registro de entrada no caixa
			fluxoCaixaBean.entradaDeCaixa(fatura, calculo, calendar, nomeAssociado);

		}
		fatura.setDataPagamento(calendar);
		calculaResiduo();

		fatura.setStatus(Status.PAGO);
		repository.save(fatura);

		enviaEmailDeConfirmacaoDoPagamento();
		this.getListaFaturaAtrasada();

		JsfUtil.info("Fatura paga com sucesso!");

	}

	public void efetuarPagamento(Fatura fatura) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataPagamento);
		String nomeAssociado = fatura.getPlano().getBeneficiario().getNomeComIniciaisMaiuscula();

		if (dataPagamento.before(fatura.getVencimento()) || dataPagamento.equals(fatura.getVencimento())) {
			fatura.setMultaAplicada(false);
			fatura.setValorTotal(fatura.getValorTotalGerado());
			fluxoCaixaBean.entradaDeCaixa(fatura, fatura.getValorTotalGerado(), calendar, nomeAssociado);

		} else {
			fatura.setMultaAplicada(true);

			// Valor total
			Double multa = fatura.getValorTotalGerado() * 0.02;
			Double juros = fatura.getValorTotalGerado() * 0.00033 * selecaoData();
			Double calculo = fatura.getValorTotalGerado() + juros + multa;

			fatura.setValorTotal(calculo);
			fluxoCaixaBean.entradaDeCaixa(fatura, calculo, calendar, nomeAssociado);
		}

		fatura.setDataPagamento(calendar);
		fatura.setStatus(Status.PAGO);

		repository.save(fatura);
		this.getListaFaturaAtrasada();
	}

	public void validaDataPagamento() {
		if (dataPagamento.before(fatura.getVencimento()) || dataPagamento.equals(fatura.getVencimento())) {
			fatura.setMultaAplicada(false);
			fatura.setValorTotal(fatura.getValorTotalGerado());
			fatura.setStatus(Status.PAGO);
		}
	}

	/**
	 * Este metodo faz o calculo do valorDoPagamento ao totalPago
	 */
	public void calculaResiduo() {
		Double totalResiduo = fatura.getValorTotal() - fatura.getValorPago();
		fatura.setValorDoResiduo(totalResiduo);
		System.out.println(">> Residuo: " + totalResiduo);
	}

	/**
	 * Efetua pagamento de todas as faturas do mes atual
	 *
	 * @return
	 */
	public String efetuarPagamentoAll() {
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		for (Fatura f : repository.findByFaturaPendente()) {
			if (f.getDataRegistro().get(Calendar.MONTH) == mesAtual()
					&& f.getDataRegistro().get(Calendar.YEAR) == anoAtual()) {
				f.setStatus(Status.PAGO);
				repository.save(f);
			}
		}
		JsfUtil.info("Fatura com vencimento no mes de " + fatura.getMesDaFatura() + " foi paga com sucesso!");

		return Adress.PESQUISA_FATURA;
	}

	/**
	 * Seleciona fatura na tabela
	 *
	 * @param event
	 * @return
	 */
	public Fatura selecionaFaturaNaTabela(SelectEvent event) {
		return fatura;
	}

	/**
	 * Busca id do beneficiario
	 */
	public void buscar() {
		fatura = repository.findBy(fatura.getId());
	}

	/**
	 * Parcelar fatura
	 *
	 * @return
	 */
	public void parcelarFatura() {
		System.out.println(">>> Parcelando fatura... Parcelas: " + numeroDeParcelas);

		Calendar hoje = Calendar.getInstance();
		Date dataHoje = new java.util.Date();
		Date vencimentoFatura = null;

		Calendar c = Calendar.getInstance();
		c.setTime(dataHoje);

		for (int i = 1; i <= numeroDeParcelas; i++) {
			if (i == 1) {
				vencimentoFatura = c.getTime();
			} else {
				c.add(Calendar.DATE, +30);
				vencimentoFatura = c.getTime();
			}

			faturaParcelada.setFatura(fatura);
			faturaParcelada.setVencimento(vencimentoFatura);
			faturaParcelada.setNumeroDaParcela(i);
			faturaParcelada.setValorTotal(fatura.getValorTotalGerado() / numeroDeParcelas);

			fatura.setStatus(Status.PARCELADO);
			fatura.setParcelas(numeroDeParcelas);
			fatura.setParcelada(true);
			fatura.setDataPagamento(hoje);

			repository.save(fatura);
			faturaParceladaRepository.save(faturaParcelada);

			faturaParcelada = new FaturaParcelada();
		}

		JsfUtil.info("Fatura parcelada em " + numeroDeParcelas + "x");

	}

	/**
	 * Altera data de vencimento da fatura
	 */
	public void alterarVencimento() {
		repository.save(fatura);
		JsfUtil.info("Data de vencimento alterada!");
	}

	public void redirecionaParaFatura(int codigo) {
		String context = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(context + "/pages/protected/fatura/index-fatura.xhtml?id=" + codigo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Listing

	public List<Fatura> getListaFaturas() {
		if (listaFaturas == null) {
			listaFaturas = repository.findAllByBeneficiario();
		}
		return listaFaturas;
	}

	/**
	 * Lista faturas atrasadas
	 *
	 * @return
	 */
	public List<Fatura> getListaFaturaAtrasada() {
		return listaFaturaAtrasada;
	}

	/**
	 * Lista de faturas ainda nao pagas
	 *
	 * @return
	 */
	public List<Fatura> getListaFaturaPendente() {
		return listaFaturaPendente;
	}

	/**
	 * Lista faturas atrasadas
	 *
	 * @return
	 */
	public List<Fatura> getListaFaturaPaga() {
		if (listaFaturaPaga == null) {
			listaFaturaPaga = repository.findByFaturaPaga(mesAtual() + 1, anoAtual());
		}
		return listaFaturaPaga;
	}

	/**
	 * Lista fatura mes ano para relatorio mensal
	 *
	 * @return
	 */
	public List<Fatura> getListaFaturaMesAno() {
		List<Fatura> listEmpty = new ArrayList<>();
		if (searchMes == null || searchAno == null) {
			return listEmpty;
		} else {
			return repository.findByFaturaMesAno(searchMes, searchAno);
		}
	}

	/**
	 * Lista de meses
	 *
	 * @return
	 */
	public List<Mes> getListaMes() {
		return Arrays.asList(Mes.values());
	}

	/**
	 * Lista de faturas do beneficiario(somente a fatura para o mes selecionado)
	 *
	 * @return
	 */
	public List<Fatura> getTotalPlanoDeSaude() {
		List<Fatura> lista = new ArrayList<>();

		for (Fatura f : repository.findAll()) {
			if (f.getPlano().getBeneficiario() == fatura.getPlano().getBeneficiario()
					&& f.getDataRegistro().equals(fatura.getDataRegistro())
					&& f.getPlano().getConvenio().isPlanoDeSaude()) {
				lista.add(f);
			}
		}
		return lista;
	}

	public List<Fatura> getTotalMensalidade() {
		List<Fatura> lista = new ArrayList<>();

		for (Fatura f : repository.findAll()) {
			if (f.getPlano().getBeneficiario() == fatura.getPlano().getBeneficiario()
					&& f.getDataRegistro().equals(fatura.getDataRegistro())
					&& f.getPlano().getConvenio().isMensalidade()) {
				lista.add(f);
			}
		}
		return lista;
	}

	/**
	 * Soma as faturas geradas no mes
	 *
	 * @return
	 */
	public String getValorTotalDaFatura() {
		Locale ptBR = new Locale("pt", "BR");
		Double total = 0.00;
		for (Fatura f : repository.findAll()) {
			if (f.getPlano().getBeneficiario() == fatura.getPlano().getBeneficiario()
					&& f.getDataRegistro().equals(fatura.getDataRegistro())) {
				total += f.getValorPlanoDeSaude();
			}
		}
		return NumberFormat.getCurrencyInstance(ptBR).format(total);
	}

	public void getValorTotalDaFatura2() {
		Double total = 0.00;
		for (Fatura f : repository.findAll()) {
			if (f.getPlano().getBeneficiario() == fatura.getPlano().getBeneficiario()
					&& f.getDataRegistro().equals(fatura.getDataRegistro())) {
				total += f.getValorPlanoDeSaude();
			}
		}
	}

	/**
	 * Lista de contribuintes
	 *
	 * @return
	 */
	public List<Fatura> getTotalContribuinte() {
		List<Fatura> lista = new ArrayList<>();

		for (Fatura f : repository.findAll()) {
			if (f.getPlano().getBeneficiario() == fatura.getPlano().getBeneficiario()
					&& f.getDataRegistro().equals(fatura.getDataRegistro())
					&& f.getPlano().getConvenio().isContribuinte()) {
				lista.add(f);
			}
		}
		return lista;
	}

	/**
	 * Lista faturas do beneficiario
	 *
	 * @return
	 */
	public List<Fatura> getFaturasDoBeneficiario() {
		List<Fatura> faturasDoBeneficiario = new ArrayList<>();
		for (Fatura f : repository.findByFaturasDoMes(beneficiarioBean.getBeneficiario().getId())) {
			faturasDoBeneficiario.add(f);
		}
		return faturasDoBeneficiario;
	}

	/**
	 * Exibe as parcelas da fatura
	 *
	 * @return
	 */
	public List<FaturaParcelada> getParcelasDaFatura() {
		return faturaParceladaRepository.findByParcelasDaFatura(fatura);
	}

	/**
	 * Total de faturas pagas
	 *
	 * @return
	 */
	public Long getTotalFaturasPagas() {
		return repository.countFaturaPaga(mesAtual() + 1, anoAtual());
	}

	/**
	 * Conta quantidade faturas pendentes
	 *
	 * @return
	 */
	public Long getTotalFaturasPendentes() {
		return repository.countFaturaPendente();
	}

	/**
	 * Total de faturas atrasadas
	 *
	 * @return
	 */
	public Long getTotalFaturasAtrasadas() {
		return repository.countFaturaAtrasada();
	}

	// Validations

	/**
	 * Verifica se existe fatura do mes atual
	 */
	public void checaExisteFaturaDesteMes(Beneficiario beneficiario) {
		if (repository.findByFaturaMesBeneficiario(mesAtual() + 1, anoAtual(), beneficiario).isEmpty()) {
			JsfUtil.warning("Não foi gerada fatura do mês atual para o beneficiário "
					+ beneficiario.getNomeComIniciaisMaiuscula());
		}
	}

	public boolean isExisteFatura(Beneficiario beneficiario) {
		return repository.findByFaturaMesBeneficiario(mesAtual() + 1, anoAtual(), beneficiario).isEmpty();
	}

	/**
	 * Verifica se ha fatura do mes
	 *
	 * @return
	 */
	public boolean isFaturaParaEsseMes() {
		return mesFaturaRepository.findByFaturaDoMes(mesAtual() + 1, anoAtual()).isEmpty();
	}

	public boolean isCobrancaParaEsseDia() {
		Calendar hoje = Calendar.getInstance();
		return mesFaturaRepository.findByDataCobranca(hoje).isEmpty();
	}

	public boolean isJurosParaEsseDia() {
		Calendar hoje = Calendar.getInstance();
		return mesFaturaRepository.findByDataJuros(hoje).isEmpty();
	}

	/**
	 * Checa se o metodo ja foi invocado no dia
	 * 
	 * @return
	 */
	public boolean isFaturaAtrasada() {
		Calendar hoje = Calendar.getInstance();
		return mesFaturaRepository.findByFaturaAtrasada(hoje).isEmpty();
	}

	public boolean isFaturaExiste() {
		return this.getFatura().getId() != null;
	}

	public boolean isFaturaParcelada() {
		return fatura.getParcelada().equals(true);
	}

	/**
	 * Instancia objeto fatura
	 *
	 * @return
	 */
	public Fatura getFatura() {
		if (fatura == null) {
			fatura = new Fatura();
		}
		return fatura;
	}

	public List<Status> getSituacoes() {
		return Arrays.asList(Status.PENDENTE, Status.PAGO);
	}

	public void setFatura(Fatura fatura) {
		this.fatura = fatura;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue.toUpperCase();
	}

	public Integer getSearchMes() {
		return searchMes;
	}

	public void setSearchMes(Integer searchMes) {
		this.searchMes = searchMes;
	}

	public Integer getSearchAno() {
		return searchAno;
	}

	public void setSearchAno(Integer searchAno) {
		this.searchAno = searchAno;
	}

	public void setListaFaturaAtrasada(List<Fatura> listaFaturaAtrasada) {
		this.listaFaturaAtrasada = listaFaturaAtrasada;
	}

	public FaturaParcelada getParcelada() {
		return faturaParcelada;
	}

	public void setParcelada(FaturaParcelada parcelada) {
		this.faturaParcelada = parcelada;
	}

	public Integer getNumeroDeParcelas() {
		return numeroDeParcelas;
	}

	public void setNumeroDeParcelas(Integer numeroDeParcelas) {
		this.numeroDeParcelas = numeroDeParcelas;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public String getRequisicaoParaListarFaturasAtrasadas() {
		return Adress.DASHBOARD;
	}

	public String getCodigoDeSegurancao() {
		return codigoDeSegurancao;
	}

	public void setCodigoDeSegurancao(String codigoDeSegurancao) {
		this.codigoDeSegurancao = codigoDeSegurancao;
	}
}
