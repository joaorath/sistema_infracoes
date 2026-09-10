package com.transito.sistema.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class InfracaoRequest {

    @NotNull(message = "O condutor é obrigatório")
    private Long condutorId;

    @NotNull(message = "O veículo é obrigatório")
    private Long veiculoId;

    @NotNull(message = "O tipo de infração é obrigatório")
    private Long tipoInfracaoId;

    @NotNull(message = "A data e hora são obrigatórias")
    private LocalDateTime dataHora;

    public InfracaoRequest() {
    }

    public Long getCondutorId() {
        return condutorId;
    }

    public void setCondutorId(Long condutorId) {
        this.condutorId = condutorId;
    }

    public Long getVeiculoId() {
        return veiculoId;
    }

    public void setVeiculoId(Long veiculoId) {
        this.veiculoId = veiculoId;
    }

    public Long getTipoInfracaoId() {
        return tipoInfracaoId;
    }

    public void setTipoInfracaoId(Long tipoInfracaoId) {
        this.tipoInfracaoId = tipoInfracaoId;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}