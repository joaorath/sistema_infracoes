package com.transito.sistema.service;

import com.transito.sistema.dto.InfracaoRequest;
import com.transito.sistema.dto.InfracaoResponse;
import com.transito.sistema.entity.Condutor;
import com.transito.sistema.entity.Infracao;
import com.transito.sistema.entity.TipoInfracao;
import com.transito.sistema.entity.Veiculo;
import com.transito.sistema.enums.GravidadeInfracao;
import com.transito.sistema.repository.CondutorRepository;
import com.transito.sistema.repository.InfracaoRepository;
import com.transito.sistema.repository.TipoInfracaoRepository;
import com.transito.sistema.repository.VeiculoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InfracaoServiceTest {

    @Mock
    private InfracaoRepository infracaoRepository;

    @Mock
    private CondutorRepository condutorRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private TipoInfracaoRepository tipoInfracaoRepository;

    @InjectMocks
    private InfracaoService infracaoService;

    @Test
    void deveCadastrarInfracaoEAdicionarPontosAoCondutor() {

        Condutor condutor = criarCondutor(0);

        Veiculo veiculo = criarVeiculo(condutor);

        TipoInfracao tipo = criarTipoInfracao(5);

        InfracaoRequest request = criarRequest();

        when(condutorRepository.findById(1L))
                .thenReturn(Optional.of(condutor));

        when(veiculoRepository.findById(1L))
                .thenReturn(Optional.of(veiculo));

        when(tipoInfracaoRepository.findById(1L))
                .thenReturn(Optional.of(tipo));

        when(infracaoRepository.save(any(Infracao.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        InfracaoResponse response =
                infracaoService.salvar(request);

        assertNotNull(response);

        assertEquals(
                5,
                condutor.getPontuacaoCnh()
        );

        verify(infracaoRepository, times(1))
                .save(any(Infracao.class));

        verify(condutorRepository, times(1))
                .save(condutor);
    }

    @Test
    void deveExcluirInfracaoERemoverPontosDoCondutor() {

        Condutor condutor = criarCondutor(5);

        Veiculo veiculo = criarVeiculo(condutor);

        TipoInfracao tipo = criarTipoInfracao(5);

        Infracao infracao = new Infracao();

        infracao.setCondutor(condutor);
        infracao.setVeiculo(veiculo);
        infracao.setTipoInfracao(tipo);
        infracao.setDataHora(LocalDateTime.now());

        when(infracaoRepository.findById(1L))
                .thenReturn(Optional.of(infracao));

        infracaoService.excluir(1L);

        assertEquals(
                0,
                condutor.getPontuacaoCnh()
        );

        verify(infracaoRepository, times(1))
                .delete(infracao);

        verify(condutorRepository, times(1))
                .save(condutor);
    }

    @Test
    void deveAtualizarInfracaoERecalcularPontos() {

        Condutor condutor = criarCondutor(5);

        Veiculo veiculo = criarVeiculo(condutor);

        TipoInfracao tipoAnterior =
                criarTipoInfracao(5);

        TipoInfracao novoTipo =
                criarTipoInfracao(7);

        Infracao infracao = new Infracao();

        infracao.setCondutor(condutor);
        infracao.setVeiculo(veiculo);
        infracao.setTipoInfracao(tipoAnterior);
        infracao.setDataHora(LocalDateTime.now());

        InfracaoRequest request =
                criarRequest();

        when(infracaoRepository.findById(1L))
                .thenReturn(Optional.of(infracao));

        when(condutorRepository.findById(1L))
                .thenReturn(Optional.of(condutor));

        when(veiculoRepository.findById(1L))
                .thenReturn(Optional.of(veiculo));

        when(tipoInfracaoRepository.findById(1L))
                .thenReturn(Optional.of(novoTipo));

        when(infracaoRepository.save(any(Infracao.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        infracaoService.atualizar(
                1L,
                request
        );

        assertEquals(
                7,
                condutor.getPontuacaoCnh()
        );
    }

    @Test
    void naoDeveCadastrarInfracaoComVeiculoDeOutroCondutor() {

        Condutor condutor = criarCondutor(0);

        Condutor outroCondutor =
                criarCondutor(0);

        Veiculo veiculo =
                criarVeiculo(outroCondutor);

        TipoInfracao tipo =
                criarTipoInfracao(5);

        InfracaoRequest request =
                criarRequest();

        when(condutorRepository.findById(1L))
                .thenReturn(Optional.of(condutor));

        when(veiculoRepository.findById(1L))
                .thenReturn(Optional.of(veiculo));

        when(tipoInfracaoRepository.findById(1L))
                .thenReturn(Optional.of(tipo));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                infracaoService.salvar(
                                        request
                                )
                );

        assertEquals(
                "O veículo informado não pertence ao condutor.",
                exception.getMessage()
        );

        verify(
                infracaoRepository,
                never()
        ).save(any());
    }

    private Condutor criarCondutor(
            int pontos) {

        Condutor condutor =
                new Condutor();

        condutor.setNome(
                "João Teste"
        );

        condutor.setCpf(
                "12345678900"
        );

        condutor.setNumeroCnh(
                "12345678901"
        );

        condutor.setPontuacaoCnh(
                pontos
        );

        return condutor;
    }

    private Veiculo criarVeiculo(
            Condutor condutor) {

        Veiculo veiculo =
                new Veiculo();

        veiculo.setPlaca(
                "ABC1D23"
        );

        veiculo.setRenavam(
                "12345678901"
        );

        veiculo.setMarca(
                "Toyota"
        );

        veiculo.setModelo(
                "Corolla"
        );

        veiculo.setAno(
                2022
        );

        veiculo.setCondutor(
                condutor
        );

        return veiculo;
    }

    private TipoInfracao criarTipoInfracao(
            int pontos) {

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
                pontos
        );

        tipo.setValor(
                new BigDecimal("195.23")
        );

        return tipo;
    }

    private InfracaoRequest criarRequest() {

        InfracaoRequest request =
                new InfracaoRequest();

        request.setCondutorId(1L);
        request.setVeiculoId(1L);
        request.setTipoInfracaoId(1L);

        request.setDataHora(
                LocalDateTime.now()
        );

        return request;
    }
}