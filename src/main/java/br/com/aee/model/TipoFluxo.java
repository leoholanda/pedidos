package br.com.aee.model;

/**
 * Created by holanda on 08/11/17.
 */
public enum TipoFluxo {
    ENTRADA("Entrada"),
    SAIDA("Saída");

    private String nome;

    TipoFluxo(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
