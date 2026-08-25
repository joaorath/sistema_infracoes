package com.transito.sistema.service;

import com.transito.sistema.dto.InfracaoRequest;
import com.transito.sistema.dto.InfracaoResponse;
import com.transito.sistema.entity.Condutor;
import com.transito.sistema.entity.Infracao;
import com.transito.sistema.entity.TipoInfracao;
import com.transito.sistema.entity.Veiculo;
import com.transito.sistema.repository.CondutorRepository;
import com.transito.sistema.repository.InfracaoRepository;
import com.transito.sistema.repository.TipoInfracaoRepository;
import com.transito.sistema.repository.VeiculoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.transito.sistema.enums.GravidadeInfracao;
import com.transito.sistema.exception.ResourceNotFoundException;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InfracaoServiceTest {

    @Mock
    private InfracaoRepository infracaoRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private CondutorRepository condutorRepository;

    @Mock
    private TipoInfracaoRepository tipoInfracaoRepository;

    @InjectMocks
    private InfracaoService infracaoService;

    @Test
    void deveRegistrarInfracaoEAdicionarPontosAoCondutor() {

        // Arrange
        Condutor condutor = new Condutor();
        condutor.setNome("Joao da Silva");
        condutor.setCpf("12345678900");
        condutor.setNumeroCnh("12345678901");
        condutor.setPontuacaoCnh(0);

        Veiculo veiculo = new Veiculo();

        TipoInfracao tipoInfracao = new TipoInfracao();
        tipoInfracao.setCodigo("001");
        tipoInfracao.setDescricao("Avançar sinal vermelho");
        tipoInfracao.setGravidade(GravidadeInfracao.GRAVISSIMA);
        tipoInfracao.setPontos(7);
        tipoInfracao.setValor(BigDecimal.valueOf(293.47));

        InfracaoRequest request = new InfracaoRequest();
        request.setVeiculoId(1L);
        request.setCondutorId(1L);
        request.setTipoInfracaoId(1L);
        request.setDataHora(LocalDateTime.of(2026, 8, 24, 16, 0));

        Infracao infracaoSalva = new Infracao();
        infracaoSalva.setVeiculo(veiculo);
        infracaoSalva.setCondutor(condutor);
        infracaoSalva.setTipoInfracao(tipoInfracao);
        infracaoSalva.setDataHora(request.getDataHora());

        when(veiculoRepository.findById(1L))
                .thenReturn(Optional.of(veiculo));

        when(condutorRepository.findById(1L))
                .thenReturn(Optional.of(condutor));

        when(tipoInfracaoRepository.findById(1L))
                .thenReturn(Optional.of(tipoInfracao));

        when(infracaoRepository.save(any(Infracao.class)))
                .thenReturn(infracaoSalva);

        // Act
        InfracaoResponse resultado = infracaoService.salvar(request);

        // Assert
        assertNotNull(resultado);

        assertEquals(7, condutor.getPontuacaoCnh());

        verify(condutorRepository, times(1))
                .save(condutor);

        verify(infracaoRepository, times(1))
                .save(any(Infracao.class));
    }

    @Test
    void deveLancarExcecaoQuandoVeiculoNaoExiste() {

        InfracaoRequest request = new InfracaoRequest();

        request.setVeiculoId(999L);
        request.setCondutorId(1L);
        request.setTipoInfracaoId(1L);
        request.setDataHora(LocalDateTime.now());

        when(veiculoRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> infracaoService.salvar(request));

        verify(veiculoRepository, times(1))
                .findById(999L);

        verifyNoInteractions(
                condutorRepository,
                tipoInfracaoRepository,
                infracaoRepository);
    }

    @Test
    void deveLancarExcecaoQuandoCondutorNaoExiste() {

        InfracaoRequest request = new InfracaoRequest();

        request.setVeiculoId(1L);
        request.setCondutorId(999L);
        request.setTipoInfracaoId(1L);
        request.setDataHora(LocalDateTime.now());

        Veiculo veiculo = new Veiculo();

        when(veiculoRepository.findById(1L))
                .thenReturn(Optional.of(veiculo));

        when(condutorRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> infracaoService.salvar(request));

        verify(veiculoRepository, times(1))
                .findById(1L);

        verify(condutorRepository, times(1))
                .findById(999L);

        verifyNoInteractions(
                tipoInfracaoRepository,
                infracaoRepository);
    }

    @Test
    void deveLancarExcecaoQuandoTipoInfracaoNaoExiste() {

        InfracaoRequest request = new InfracaoRequest();

        request.setVeiculoId(1L);
        request.setCondutorId(1L);
        request.setTipoInfracaoId(999L);
        request.setDataHora(LocalDateTime.now());

        Veiculo veiculo = new Veiculo();

        Condutor condutor = new Condutor();

        when(veiculoRepository.findById(1L))
                .thenReturn(Optional.of(veiculo));

        when(condutorRepository.findById(1L))
                .thenReturn(Optional.of(condutor));

        when(tipoInfracaoRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> infracaoService.salvar(request));

        verify(veiculoRepository, times(1))
                .findById(1L);

        verify(condutorRepository, times(1))
                .findById(1L);

        verify(tipoInfracaoRepository, times(1))
                .findById(999L);

        verifyNoInteractions(infracaoRepository);
    }

    @Test
    void deveSomarPontosAosPontosJaExistentesDoCondutor() {

        Condutor condutor = new Condutor();
        condutor.setNome("Joao da Silva");
        condutor.setCpf("12345678900");
        condutor.setNumeroCnh("12345678901");
        condutor.setPontuacaoCnh(7);

        Veiculo veiculo = new Veiculo();

        TipoInfracao tipoInfracao = new TipoInfracao();
        tipoInfracao.setCodigo("002");
        tipoInfracao.setDescricao("Estacionar em local proibido");
        tipoInfracao.setGravidade(GravidadeInfracao.MEDIA);
        tipoInfracao.setPontos(4);
        tipoInfracao.setValor(BigDecimal.valueOf(130.16));

        InfracaoRequest request = new InfracaoRequest();

        request.setVeiculoId(1L);
        request.setCondutorId(1L);
        request.setTipoInfracaoId(2L);
        request.setDataHora(
                LocalDateTime.of(2026, 8, 24, 17, 0));

        Infracao infracaoSalva = new Infracao();

        infracaoSalva.setVeiculo(veiculo);
        infracaoSalva.setCondutor(condutor);
        infracaoSalva.setTipoInfracao(tipoInfracao);
        infracaoSalva.setDataHora(request.getDataHora());

        when(veiculoRepository.findById(1L))
                .thenReturn(Optional.of(veiculo));

        when(condutorRepository.findById(1L))
                .thenReturn(Optional.of(condutor));

        when(tipoInfracaoRepository.findById(2L))
                .thenReturn(Optional.of(tipoInfracao));

        when(infracaoRepository.save(any(Infracao.class)))
                .thenReturn(infracaoSalva);

        infracaoService.salvar(request);

        assertEquals(11, condutor.getPontuacaoCnh());

        verify(condutorRepository, times(1))
                .save(condutor);

        verify(infracaoRepository, times(1))
                .save(any(Infracao.class));
    }
}