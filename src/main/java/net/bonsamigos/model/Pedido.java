package net.bonsamigos.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
import net.bonsamigos.util.NomeComInicialMaiscula;

@Entity
public class Pedido extends Auditoria implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private Long codigo;

	@NotNull
	@Column(length = 30)
	@Enumerated(EnumType.STRING)
	private Status status;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "usuario", foreignKey = @ForeignKey(name = "usuario"))
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name = "autorizado_por", foreignKey = @ForeignKey(name = "autorizado_por"))
	private Usuario autorizadoPor;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "unidade", foreignKey = @ForeignKey(name = "unidade"))
	private Unidade unidade;

	@Column(name = "data_entrega")
	private Calendar dataEntrega = Calendar.getInstance();

	private String entregador; // Nome do entregador

	private String nomeResponsavel; // Responsável da unidade que recebeu os produtos

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
	private List<Item> itens = new ArrayList<Item>();

	@Transient
	public String getTitulo() {
		return id == null ? "Fazer" : "Editar";
	}
	
	public String getCodigoCompleto() {
		String format = String.format ("%02d", codigo);
		return format;
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
	
	/**
	 * Codigo e mes do pedido
	 * @return
	 */
	public String getCodigoMes() {
		return this.getCodigoCompleto() + "/" + this.getMes(); 
	}
	
	public String getNomeResponsavelMaiuscula() {
		return NomeComInicialMaiscula.iniciaisMaiuscula(nomeResponsavel);
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

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}
	
	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel.toUpperCase();
	}
	
	public Usuario getAutorizadoPor() {
		return autorizadoPor;
	}
	
	public void setAutorizadoPor(Usuario autorizadoPor) {
		this.autorizadoPor = autorizadoPor;
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}
	
	public Long getCodigo() {
		return codigo;
	}
	
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
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
