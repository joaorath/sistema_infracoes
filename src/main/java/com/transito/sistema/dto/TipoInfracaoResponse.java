package com.transito.sistema.dto;

import com.transito.sistema.enums.GravidadeInfracao;

import java.math.BigDecimal;

public class TipoInfracaoResponse {

    private Long id;
    private String codigo;
    private String descricao;
    private GravidadeInfracao gravidade;
    private Integer pontos;
    private BigDecimal valor;

    public TipoInfracaoResponse() {
    }

    public TipoInfracaoResponse(
            Long id,
            String codigo,
            String descricao,
            GravidadeInfracao gravidade,
            Integer pontos,
            BigDecimal valor) {

        this.id = id;
        this.codigo = codigo;
        this.descricao = descricao;
        this.gravidade = gravidade;
        this.pontos = pontos;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public GravidadeInfracao getGravidade() {
        return gravidade;
    }

    public Integer getPontos() {
        return pontos;
    }

    public BigDecimal getValor() {
        return valor;
    }
}