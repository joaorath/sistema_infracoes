package com.transito.sistema.dto;

public class VeiculoResponse {

    private Long id;
    private String placa;
    private String renavam;
    private String marca;
    private String modelo;
    private Integer ano;
    private Long condutorId;
    private String nomeCondutor;

    public VeiculoResponse() {
    }

    public VeiculoResponse(
            Long id,
            String placa,
            String renavam,
            String marca,
            String modelo,
            Integer ano,
            Long condutorId,
            String nomeCondutor) {

        this.id = id;
        this.placa = placa;
        this.renavam = renavam;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.condutorId = condutorId;
        this.nomeCondutor = nomeCondutor;
    }

    public Long getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getRenavam() {
        return renavam;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public Integer getAno() {
        return ano;
    }

    public Long getCondutorId() {
        return condutorId;
    }

    public String getNomeCondutor() {
        return nomeCondutor;
    }
}