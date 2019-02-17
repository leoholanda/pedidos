package br.com.aee.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.aee.converter.BaseEntity;
import br.com.aee.converter.StringExtended;

@Entity
@Table(name = "beneficiario")
public class Beneficiario implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	private String nome;

	@NotEmpty
//	@CPF
	@Column(name = "cpf")
	private String cpf;

	@Column(name = "matricula")
	private String matricula;

	@NotNull
	@Column(name = "data_nascimento")
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;

	@NotEmpty
	@Column(name = "sexo")
	private String sexo;

	@NotNull
	@Column(name = "data_registro")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Calendar dataRegistro = Calendar.getInstance();

	@Email
	private String email;

	// @NotEmpty
	private String telefone;

	@Column(name = "numero_sus")
	private Long sus;

	@Column(name = "servidor")
	@NotNull
	private Boolean confirmaServidor = true;

	@Column(name = "salario")
	private Double salario = 0.00;

	// APARTAMENTO, ENFERMARIA
	@Column(name = "acomodacao")
	private String acomodacao;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status;

	@Column(name = "mensalidade_consignado")
	private Boolean mensalidadeConsignado;

	@NotNull
	@Column(name = "tem_plano_de_saude")
	private Boolean temPlanoDeSaude;

	@ManyToOne
	@JoinColumn(name = "faixa_etaria")
	private FaixaEtaria faixaEtaria;

	@OneToMany(mappedBy = "beneficiario", cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<Dependente> dependentes = new ArrayList<Dependente>();

	@OneToMany(mappedBy = "beneficiario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Plano> planos = new ArrayList<Plano>();

	@ManyToOne
	@JoinColumn(name = "fiador")
	private Beneficiario fiador;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "endereco")
	private Endereco endereco;

	/**
	 * Beneficiario do sexo masculino
	 *
	 * @return
	 */
	public boolean isMasculino() {
		return sexo.equalsIgnoreCase("Masculino");
	}

	/**
	 * Beneficiario do sexo feminino
	 *
	 * @return
	 */
	public boolean isFeminino() {
		return sexo.equalsIgnoreCase("Feminino");
	}

	public boolean isDesativado() {
		return Status.DESATIVADO.equals(this.status);
	}

	public boolean isAtivado() {
		return Status.ATIVADO.equals(this.status);
	}

	public boolean isEnfermaria() {
		if (acomodacao != null) {
			return acomodacao.equalsIgnoreCase("ENFERMARIA");
		}
		return false;
	}

	public boolean isApartamento() {
		if (acomodacao != null) {
			return acomodacao.equalsIgnoreCase("APARTAMENTO");
		}
		return false;
	}

	public String getConverterValor() {
		Locale ptBR = new Locale("pt", "BR");
		if (isApartamento()) {
			return NumberFormat.getCurrencyInstance(ptBR).format(faixaEtaria.getValorApartamento());
		} else {
			return NumberFormat.getCurrencyInstance(ptBR).format(faixaEtaria.getValorEnfermaria());
		}
	}

	public String getMensalidade() {
		Locale ptBR = new Locale("pt", "BR");
		return NumberFormat.getCurrencyInstance(ptBR).format(calculoMensalidade());
	}

	/**
	 * Pagamento consignado
	 *
	 * @return
	 */
	public boolean isConsignado() {
		return mensalidadeConsignado.equals(true);
	}

	public String getPagamentoConsignado() {
		return isConsignado() ? "Sim" : "Não";
	}

	/**
	 * Tem fiador
	 *
	 * @return
	 */
	public boolean isTemFiador() {
		return fiador != null;
	}

	public boolean isBeneficiarioServidor() {
		return confirmaServidor.equals(true);
	}

	public String getServidor() {
		if (confirmaServidor.equals(false)) {
			return "Contribuinte";
		} else {
			return matricula;
		}
	}

	public String getAssociadoServidor() {
		return confirmaServidor ? "Servidor" : "Não é Servidor";
	}

	// Formatacao para SUS
	public String getFormatacaoSus() {
		return String.format("%015d", sus);
	}

	public String getNomeComIniciaisMaiuscula() {
		return StringExtended.getNomeComIniciaisMaiuscula(nome);
	}

	/**
	 * Converte para idade atual do beneficiario
	 *
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Integer getIdade() {
		Calendar dataAtual = Calendar.getInstance();

		Integer diferencaMes = dataAtual.get(Calendar.MONTH) - dataNascimento.getMonth();
		Integer diferencaDia = dataAtual.get(Calendar.DAY_OF_MONTH) - dataNascimento.getDay();
		Integer idade = (dataAtual.get(Calendar.YEAR) - dataNascimento.getYear());

		if (diferencaMes < 0 || (diferencaMes == 0 && diferencaDia < 0)) {
			idade--;
		}
		Integer calculaIdade = idade - 1900;

		return calculaIdade;
	}

	/**
	 * Calculo da mensalidade
	 * 
	 * @return
	 */
	public Double calculoMensalidade() {
		Double mensalidade = 0.00;
		if (isBeneficiarioServidor()) {
			if (salario != null) {

				// Mensalidade é 0,8% do salário
				mensalidade = salario * 0.008;
			}
		} else {
			mensalidade = 70.00;
		}
		return mensalidade;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = StringExtended.toASCII(nome.toUpperCase());
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public Calendar getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(Calendar dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public FaixaEtaria getFaixaEtaria() {
		return faixaEtaria;
	}

	public void setFaixaEtaria(FaixaEtaria faixaEtaria) {
		this.faixaEtaria = faixaEtaria;
	}

	public List<Plano> getPlanos() {
		return planos;
	}

	public void setPlanos(List<Plano> planos) {
		this.planos = planos;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSexo() {
		return sexo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getAcomodacao() {
		return acomodacao;
	}

	public void setAcomodacao(String acomodacao) {
		this.acomodacao = acomodacao;
	}

	public List<Dependente> getDependentes() {
		return dependentes;
	}

	public void setDependentes(List<Dependente> dependentes) {
		this.dependentes = dependentes;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula.toUpperCase();
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Beneficiario getFiador() {
		return fiador;
	}

	public void setFiador(Beneficiario fiador) {
		this.fiador = fiador;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Long getSus() {
		return sus;
	}

	public void setSus(Long sus) {
		this.sus = sus;
	}

	public Boolean getConfirmaServidor() {
		return confirmaServidor;
	}

	public void setConfirmaServidor(Boolean confirmaServidor) {
		this.confirmaServidor = confirmaServidor;
	}

	public Double getSalario() {
		return salario;
	}

	public void setSalario(Double salario) {
		this.salario = salario;
	}

	public Boolean getMensalidadeConsignado() {
		return mensalidadeConsignado;
	}

	public void setMensalidadeConsignado(Boolean mensalidadeConsignado) {
		this.mensalidadeConsignado = mensalidadeConsignado;
	}

	public Boolean getTemPlanoDeSaude() {
		return temPlanoDeSaude;
	}

	public void setTemPlanoDeSaude(Boolean temPlanoDeSaude) {
		this.temPlanoDeSaude = temPlanoDeSaude;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Beneficiario other = (Beneficiario) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
