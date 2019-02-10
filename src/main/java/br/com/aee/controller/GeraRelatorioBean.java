package br.com.aee.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import br.com.aee.model.Fatura;
import br.com.aee.report.ExecutorRelatorio;
import br.com.aee.repository.FaturaRepository;
import br.com.aee.util.JsfUtil;

@Named("geraRelatorioMB")
@ViewScoped
public class GeraRelatorioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletResponse response;

	@Inject
	private EntityManager manager;

	@Inject
	private FaturaRepository faturaRepository;

	private Long idBeneficiario;

	private Integer searchAno;
	
	private Double valor;

	/**
	 * Lista fatura anual por beneficiário
	 *
	 * @return
	 */
	public List<Fatura> getListaFaturaAnualDoBeneficiario() {
		List<Fatura> listEmpty = new ArrayList<>();
		if (idBeneficiario == null || searchAno == null) {
			return listEmpty;
		} else {
			return faturaRepository.findByFaturaBeneficiarioAno(idBeneficiario, searchAno);
		}
	}

	/**
	 * Emite declaracao imposto de renda pf
	 */
	public void emitirDeclaracao() {
		List<Fatura> faturas = this.getListaFaturaAnualDoBeneficiario();
		faturas = faturaRepository.findByFaturaBeneficiarioAno(idBeneficiario, searchAno);
		System.out.println(">>> " + idBeneficiario + " " + searchAno);

		if (faturas.isEmpty()) {
			JsfUtil.error("Nenhum registro encontrado");
		}
	}

	/**
	 * Exporta para pdf
	 */
	public void emitirDeclaracaoImpostoDeRendaPDF() {
		if (idBeneficiario != null) {
			Map<String, Object> parametros = new HashMap<>();
			parametros.put("p_beneficiario_id", this.idBeneficiario);
			parametros.put("p_ano", this.searchAno);

			ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/declaracao_irpf_3.jasper", this.response,
					parametros, "Declaração Imposto de Renda.pdf");

			Session session = manager.unwrap(Session.class);
			session.doWork(executor);

			if (executor.isRelatorioGerado()) {
				facesContext.responseComplete();
			} else {
				JsfUtil.error("A execução do relatório não retornou dados");
			}
		} else {
			JsfUtil.error("A execução do relatório não retornou dados");
		}
	}
	
	/**
	 * Exporta para pdf
	 */
	public void emitirDeclaracaoImpostoDeRendaComValorInformadoPDF() {
		if (idBeneficiario != null) {
			Map<String, Object> parametros = new HashMap<>();
			parametros.put("p_beneficiario_id", this.idBeneficiario);
			parametros.put("p_valor", this.valor);

			ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/declaracao_irpf_valor_editado.jasper", this.response,
					parametros, "Declaração Imposto de Renda.pdf");

			Session session = manager.unwrap(Session.class);
			session.doWork(executor);

			if (executor.isRelatorioGerado()) {
				facesContext.responseComplete();
			} else {
				JsfUtil.error("A execução do relatório não retornou dados");
			}
		} else {
			JsfUtil.error("A execução do relatório não retornou dados");
		}
	}

	public Long getIdBeneficiario() {
		return idBeneficiario;
	}

	public void setIdBeneficiario(Long idBeneficiario) {
		this.idBeneficiario = idBeneficiario;
	}

	public Integer getSearchAno() {
		return searchAno;
	}

	public void setSearchAno(Integer searchAno) {
		this.searchAno = searchAno;
	}
	
	public Double getValor() {
		return valor;
	}
	
	public void setValor(Double valor) {
		this.valor = valor;
	}
}
