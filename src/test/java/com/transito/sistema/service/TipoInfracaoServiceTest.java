package com.transito.sistema.service;

import com.transito.sistema.dto.TipoInfracaoRequest;
import com.transito.sistema.entity.Infracao;
import com.transito.sistema.entity.TipoInfracao;
import com.transito.sistema.enums.GravidadeInfracao;
import com.transito.sistema.exception.BusinessException;
import com.transito.sistema.repository.InfracaoRepository;
import com.transito.sistema.repository.TipoInfracaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoInfracaoServiceTest {

    @Mock
    private TipoInfracaoRepository tipoInfracaoRepository;

    @Mock
    private InfracaoRepository infracaoRepository;

    @InjectMocks
    private TipoInfracaoService tipoInfracaoService;

    @Test
    void deveExcluirTipoSemInfracoes() {

        TipoInfracao tipo =
                criarTipo();

        when(tipoInfracaoRepository.findById(1L))
                .thenReturn(
                        Optional.of(tipo)
                );

        when(infracaoRepository.findByTipoInfracaoId(1L))
                .thenReturn(
                        List.of()
                );

        tipoInfracaoService.excluir(1L);

        verify(
                tipoInfracaoRepository,
                times(1)
        ).delete(tipo);
    }

    @Test
    void naoDeveExcluirTipoComInfracoes() {

        TipoInfracao tipo =
                criarTipo();

        when(tipoInfracaoRepository.findById(1L))
                .thenReturn(
                        Optional.of(tipo)
                );

        when(infracaoRepository.findByTipoInfracaoId(1L))
                .thenReturn(
                        List.of(new Infracao())
                );

        assertThrows(
                BusinessException.class,
                () ->
                        tipoInfracaoService.excluir(
                                1L
                        )
        );

        verify(
                tipoInfracaoRepository,
                never()
        ).delete(any());
    }

    @Test
    void naoDeveAlterarPontosDeTipoComInfracoes() {

        TipoInfracao tipo =
                criarTipo();

        TipoInfracaoRequest request =
                new TipoInfracaoRequest();

        request.setCodigo(
                "TESTE001"
        );

        request.setDescricao(
                "Infração de teste"
        );

        request.setGravidade(
                GravidadeInfracao.GRAVE
        );

        request.setPontos(
                7
        );

        request.setValor(
                new BigDecimal("195.23")
        );

        when(tipoInfracaoRepository.findById(1L))
                .thenReturn(
                        Optional.of(tipo)
                );

        when(infracaoRepository.findByTipoInfracaoId(1L))
                .thenReturn(
                        List.of(new Infracao())
                );

        assertThrows(
                BusinessException.class,
                () ->
                        tipoInfracaoService.atualizar(
                                1L,
                                request
                        )
        );

        verify(
                tipoInfracaoRepository,
                never()
        ).save(any());
    }

    private TipoInfracao criarTipo() {

        TipoInfracao tipo =
                new TipoInfracao();

        tipo.setCodigo(
                "TESTE001"
        );

        tipo.setDescricao(
                "Infração de teste"
        );

        tipo.setGravidade(
                GravidadeInfracao.GRAVE
        );

        tipo.setPontos(
                5
        );

        tipo.setValor(
                new BigDecimal("195.23")
        );

        return tipo;
    }
}