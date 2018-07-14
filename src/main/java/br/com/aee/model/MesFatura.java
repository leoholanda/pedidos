package br.com.aee.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Checa o mes que ja foi gerada fatura
 * 
 * @author holanda
 *
 */
@Entity
@Table(name = "mes_fatura")
public class MesFatura implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_processo")
	private Calendar dataProcesso = Calendar.getInstance();
	
	private String evento;
	
	/**
	 * Nome por extenso mês da fatura
	 * @return
	 */
	public String getMesDaFatura() {
		int mes = dataProcesso.get(Calendar.MONTH);
		int ano = dataProcesso.get(Calendar.YEAR);
		
	    String meses[] = {"Janeiro", "Fevereiro", "Março", "Abril",
	      "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro",
	      "Novembro", "Dezembro"};
	    
	    return(meses[mes]) + "/" + ano;
	}

	// getters and setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Calendar getDataProcesso() {
		return dataProcesso;
	}

	public void setDataProcesso(Calendar dataProcesso) {
		this.dataProcesso = dataProcesso;
	}
	
	public String getEvento() {
		return evento;
	}
	
	public void setEvento(String evento) {
		this.evento = evento.toUpperCase();
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MesFatura other = (MesFatura) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
