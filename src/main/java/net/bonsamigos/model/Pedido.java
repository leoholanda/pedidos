package net.bonsamigos.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import net.bonsamigos.enums.Status;
import net.bonsamigos.util.Auditoria;
import net.bonsamigos.util.Mes;

@Entity
public class Pedido extends Auditoria implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(length = 30)
	@Enumerated(EnumType.STRING)
	private Status status;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "usuario", foreignKey = @ForeignKey(name = "usuario"))
	private Usuario usuario;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "unidade", foreignKey = @ForeignKey(name = "unidade"))
	private Unidade unidade;

	@Column(name = "data_entrega")
	private Calendar dataEntrega = Calendar.getInstance();

	private String entregador; // Nome do entregador

	private String responsavel; // Responsável pelo recebimento dos produtos

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
	private List<Item> itens = new ArrayList<Item>();

	@Transient
	public String getTitulo() {
		return id == null ? "Fazer" : "Editar";
	}
	
	public boolean isPedidoExistente() {
		return id == null ? false : true;
	}
	
	/**
	 * Nome do mês do pedido
	 * @return
	 */
	public String getMes() {
		return Mes.mesPorExtenso(this.getDataCriacao());
	}
	
	public String getMesDoPedido() {
		if (getDataCriacao() != null) {
			GregorianCalendar dataCal = new GregorianCalendar();
			dataCal.setTime(getDataCriacao().getTime());
			int mes = dataCal.get(Calendar.MONTH);
			String meses[] = { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro",
					"Outubro", "Novembro", "Dezembro" };

			return (meses[mes]);
		} else {
			return null;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Calendar getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(Calendar dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public String getEntregador() {
		return entregador;
	}

	public void setEntregador(String entregador) {
		this.entregador = entregador;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Item> getItens() {
		return itens;
	}

	public void setItens(List<Item> itens) {
		this.itens = itens;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
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
		Pedido other = (Pedido) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
