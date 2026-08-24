package com.transito.sistema.enums;

public enum GravidadeInfracao {

    LEVE(3),
    MEDIA(4),
    GRAVE(5),
    GRAVISSIMA(7);

    private final int pontos;

    GravidadeInfracao(int pontos) {
        this.pontos = pontos;
    }

    public int getPontos() {
        return pontos;
    }
}