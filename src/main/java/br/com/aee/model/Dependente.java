package br.com.aee.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.br.CPF;

@Entity
@Table(name = "dependente")
public class Dependente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String nome;

    @NotEmpty
    @CPF
    @Column(name = "cpf")
    private String cpf;

    @NotNull
    @Column(name = "numero_sus")
    private Long sus;

    @NotNull
    @Column(name = "data_nascimento")
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    @NotEmpty
    @Column(name = "sexo")
    private String sexo;

    // APARTAMENTO, ENFERMARIA
    @Column(name = "acomodacao")
    private String acomodacao;

    @NotNull
    @Column(name = "data_registro")
    private Calendar dataRegistro = Calendar.getInstance();

    @ManyToOne
    @JoinColumn(name = "faixa_etaria")
    private FaixaEtaria faixaEtaria;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_dependencia")
    private TipoDependente tipoDependencia;

    @ManyToOne
    @JoinColumn(name = "titular")
    private Beneficiario beneficiario;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

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
        return beneficiario.getStatus().equals(Status.DESATIVADO);
    }
    
    public boolean isEnfermaria() {
        return acomodacao.equalsIgnoreCase("ENFERMARIA");
    }

    public boolean isApartamento() {
        return acomodacao.equalsIgnoreCase("APARTAMENTO");
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
    
    public String getConverterValor() {
        Locale ptBR = new Locale("pt", "BR");
        if (isApartamento()) {
            return NumberFormat.getCurrencyInstance(ptBR).format(faixaEtaria.getValorApartamento());
        } else {
            return NumberFormat.getCurrencyInstance(ptBR).format(faixaEtaria.getValorEnfermaria());
        }
    }
    
    public String getNomeComIniciaisMaiuscula() {

		String sNova = "";
		for (String sNome : nome.toLowerCase().split(" ")) {
			if (!"".equals(sNome)) {
				if (!"".equals(sNova))
					sNova += " ";
				if (sNome.length() > 2) {
					sNova += sNome.substring(0, 1).toUpperCase() + sNome.substring(1);
				} else {
					sNova += sNome;
				}
			}
		}
		return sNova;
	}

    // Getters and Setters
    public Beneficiario getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(Beneficiario beneficiario) {
        this.beneficiario = beneficiario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome.toUpperCase();
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Long getSus() {
        return sus;
    }

    public void setSus(Long sus) {
        this.sus = sus;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getSexo() {
        return sexo;
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

    public TipoDependente getTipoDependencia() {
        return tipoDependencia;
    }

    public void setTipoDependencia(TipoDependente tipoDependencia) {
        this.tipoDependencia = tipoDependencia;
    }

    public String getAcomodacao() {
        return acomodacao;
    }

    public void setAcomodacao(String acomodacao) {
        this.acomodacao = acomodacao;
    }
    
    public Status getStatus() {
		return status;
	}
    
    public void setStatus(Status status) {
		this.status = status;
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
        Dependente other = (Dependente) obj;
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
