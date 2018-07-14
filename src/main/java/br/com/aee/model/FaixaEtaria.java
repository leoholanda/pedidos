package br.com.aee.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.com.aee.converter.BaseEntity;
import java.text.NumberFormat;
import java.util.Locale;

@Entity
@Table(name = "faixa_etaria")
public class FaixaEtaria implements Serializable, BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "periodo_inicial")
    private Integer periodoInicial;

    @NotNull
    @Column(name = "periodo_final")
    private Integer periodoFinal;

    @NotNull
    @Column(name = "valor_enfermaria")
    private Double valorEnfermaria;

    @NotNull
    @Column(name = "valor_apartamento")
    private Double valorApartamento;

    @Column(name = "status")
    private Boolean status = true;

    public String getPeriodo() {
        return this.periodoInicial.toString() + "-" + this.periodoFinal.toString();
    }
    
    public String getConverterValorApartamento() {
        Locale ptBR = new Locale("pt", "BR");
        return NumberFormat.getCurrencyInstance(ptBR).format(valorApartamento);
    }
    
    public String getConverterValorEnfermaria() {
        Locale ptBR = new Locale("pt", "BR");
        return NumberFormat.getCurrencyInstance(ptBR).format(valorEnfermaria);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPeriodoInicial() {
        return periodoInicial;
    }

    public void setPeriodoInicial(Integer periodoInicial) {
        this.periodoInicial = periodoInicial;
    }

    public Integer getPeriodoFinal() {
        return periodoFinal;
    }

    public void setPeriodoFinal(Integer periodoFinal) {
        this.periodoFinal = periodoFinal;
    }

    public Double getValorEnfermaria() {
        return valorEnfermaria;
    }

    public void setValorEnfermaria(Double valorEnfermaria) {
        this.valorEnfermaria = valorEnfermaria;
    }

    public Double getValorApartamento() {
        return valorApartamento;
    }

    public void setValorApartamento(Double valorApartamento) {
        this.valorApartamento = valorApartamento;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
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
        FaixaEtaria other = (FaixaEtaria) obj;
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
