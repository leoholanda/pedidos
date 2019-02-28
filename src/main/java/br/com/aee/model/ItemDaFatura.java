package br.com.aee.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "item_fatura")
public class ItemDaFatura implements Serializable {
	
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "fatura_id")
	private Fatura fatura;
	
	private String identificacao; //Nome do titular, dependente, mensalidade(identificação do valor do item para pagamento)
	
	@NotNull
	@Column(name = "valor")
	private Double valor;
	
	private Long ordem;
	
	public String getConverterValor() {
        Locale ptBR = new Locale("pt", "BR");
        return NumberFormat.getCurrencyInstance(ptBR).format(valor);
    }
	
	public String getNomeComIniciaisMaiuscula() {

		String sNova = "";
		for (String sNome : identificacao.toLowerCase().split(" ")) {
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

	public Fatura getFatura() {
		return fatura;
	}

	public void setFatura(Fatura fatura) {
		this.fatura = fatura;
	}

	public String getIdentificacao() {
		return identificacao;
	}

	public void setIdentificacao(String identificacao) {
		this.identificacao = identificacao;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
	
	public Long getOrdem() {
		return ordem;
	}
	
	public void setOrdem(Long ordem) {
		this.ordem = ordem;
	}
	

}
