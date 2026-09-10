package com.transito.sistema.entity;

import com.transito.sistema.enums.GravidadeInfracao;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class TipoInfracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GravidadeInfracao gravidade;

    @Column(nullable = false)
    private Integer pontos;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    public TipoInfracao() {
    }

    public Long getId() {
        return id;
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

    public Integer getPontos() {
        return pontos;
    }

    public void setPontos(Integer pontos) {
        this.pontos = pontos;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}