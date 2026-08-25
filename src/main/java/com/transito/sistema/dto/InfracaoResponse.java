package com.transito.sistema.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InfracaoResponse {

    private Long id;

    private Long veiculoId;
    private String placa;

    private Long condutorId;
    private String nomeCondutor;

    private Long tipoInfracaoId;
    private String codigoInfracao;
    private String descricaoInfracao;

    private Integer pontos;
    private BigDecimal valor;

    private LocalDateTime dataHora;

    public InfracaoResponse(
            Long id,
            Long veiculoId,
            String placa,
            Long condutorId,
            String nomeCondutor,
            Long tipoInfracaoId,
            String codigoInfracao,
            String descricaoInfracao,
            Integer pontos,
            BigDecimal valor,
            LocalDateTime dataHora) {

        this.id = id;
        this.veiculoId = veiculoId;
        this.placa = placa;
        this.condutorId = condutorId;
        this.nomeCondutor = nomeCondutor;
        this.tipoInfracaoId = tipoInfracaoId;
        this.codigoInfracao = codigoInfracao;
        this.descricaoInfracao = descricaoInfracao;
        this.pontos = pontos;
        this.valor = valor;
        this.dataHora = dataHora;
    }

    public Long getId() {
        return id;
    }

    public Long getVeiculoId() {
        return veiculoId;
    }

    public String getPlaca() {
        return placa;
    }

    public Long getCondutorId() {
        return condutorId;
    }

    public String getNomeCondutor() {
        return nomeCondutor;
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