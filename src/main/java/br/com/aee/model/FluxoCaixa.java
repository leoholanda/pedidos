package br.com.aee.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by holanda on 08/11/17.
 */
@Entity
@Table(name = "caixa")
public class FluxoCaixa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="tipo_fluxo")
    private TipoFluxo tipoFluxo;
    
    private String descricao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_evento")
    private Calendar dataEvento = Calendar.getInstance();
    
    private Double valor;

    @ManyToOne
    @JoinColumn(name = "fatura_paga")
    private Fatura faturaPaga;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoFluxo getTipoFluxo() {
        return tipoFluxo;
    }

    public void setTipoFluxo(TipoFluxo tipoFluxo) {
        this.tipoFluxo = tipoFluxo;
    }

    public Calendar getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(Calendar dataEvento) {
        this.dataEvento = dataEvento;
    }

    public Fatura getFaturaPaga() {
        return faturaPaga;
    }

    public void setFaturaPaga(Fatura faturaPaga) {
        this.faturaPaga = faturaPaga;
    }
    
    public Double getValor() {
		return valor;
	}
    
    public void setValor(Double valor) {
		this.valor = valor;
	}
    
    public String getDescricao() {
		return descricao;
	}
    
    public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FluxoCaixa that = (FluxoCaixa) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
