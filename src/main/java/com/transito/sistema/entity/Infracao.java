package com.transito.sistema.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Infracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "condutor_id", nullable = false)
    private Condutor condutor;

    @ManyToOne
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    @ManyToOne
    @JoinColumn(name = "tipo_infracao_id", nullable = false)
    private TipoInfracao tipoInfracao;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    public Infracao() {
    }

    public Long getId() {
        return id;
    }

    public Condutor getCondutor() {
        return condutor;
    }

    public void setCondutor(Condutor condutor) {
        this.condutor = condutor;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public TipoInfracao getTipoInfracao() {
        return tipoInfracao;
    }

    public void setTipoInfracao(TipoInfracao tipoInfracao) {
        this.tipoInfracao = tipoInfracao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}