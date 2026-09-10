package com.transito.sistema.dto;

import com.transito.sistema.enums.GravidadeInfracao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InfracaoResponse {

    private Long id;

    private Long condutorId;
    private String nomeCondutor;

    private Long veiculoId;
    private String placaVeiculo;

    private Long tipoInfracaoId;
    private String codigoInfracao;
    private String descricaoInfracao;

    private GravidadeInfracao gravidade;

    private Integer pontos;

    private BigDecimal valor;

    private LocalDateTime dataHora;

    public InfracaoResponse() {
    }

    public InfracaoResponse(
            Long id,
            Long condutorId,
            String nomeCondutor,
            Long veiculoId,
            String placaVeiculo,
            Long tipoInfracaoId,
            String codigoInfracao,
            String descricaoInfracao,
            GravidadeInfracao gravidade,
            Integer pontos,
            BigDecimal valor,
            LocalDateTime dataHora
    ) {
        this.id = id;
        this.condutorId = condutorId;
        this.nomeCondutor = nomeCondutor;
        this.veiculoId = veiculoId;
        this.placaVeiculo = placaVeiculo;
        this.tipoInfracaoId = tipoInfracaoId;
        this.codigoInfracao = codigoInfracao;
        this.descricaoInfracao = descricaoInfracao;
        this.gravidade = gravidade;
        this.pontos = pontos;
        this.valor = valor;
        this.dataHora = dataHora;
    }

    public Long getId() {
        return id;
    }

    public Long getCondutorId() {
        return condutorId;
    }

    public String getNomeCondutor() {
        return nomeCondutor;
    }

    public Long getVeiculoId() {
        return veiculoId;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public Long getTipoInfracaoId() {
        return tipoInfracaoId;
    }

    public String getCodigoInfracao() {
        return codigoInfracao;
    }

    public String getDescricaoInfracao() {
        return descricaoInfracao;
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

    public LocalDateTime getDataHora() {
        return dataHora;
    }
}