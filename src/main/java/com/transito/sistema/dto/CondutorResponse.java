package com.transito.sistema.dto;

public class CondutorResponse {

    private Long id;
    private String nome;
    private String cpf;
    private String numeroCnh;
    private Integer pontuacaoCnh;

    public CondutorResponse() {
    }

    public CondutorResponse(
            Long id,
            String nome,
            String cpf,
            String numeroCnh,
            Integer pontuacaoCnh) {

        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.numeroCnh = numeroCnh;
        this.pontuacaoCnh = pontuacaoCnh;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getNumeroCnh() {
        return numeroCnh;
    }

    public Integer getPontuacaoCnh() {
        return pontuacaoCnh;
    }
}