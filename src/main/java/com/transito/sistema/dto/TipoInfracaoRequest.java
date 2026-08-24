package com.transito.sistema.dto;

import com.transito.sistema.enums.GravidadeInfracao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class TipoInfracaoRequest {

    @NotBlank(message = "O código é obrigatório")
    @Size(max = 20, message = "O código deve possuir no máximo 20 caracteres")
    private String codigo;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(min = 5, max = 255, message = "A descrição deve ter entre 5 e 255 caracteres")
    private String descricao;

    @NotNull(message = "A gravidade é obrigatória")
    private GravidadeInfracao gravidade;

    @NotNull(message = "O valor é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor deve ser maior que zero")
    private BigDecimal valor;

    public TipoInfracaoRequest() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public GravidadeInfracao getGravidade() {
        return gravidade;
    }

    public void setGravidade(GravidadeInfracao gravidade) {
        this.gravidade = gravidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}