package com.transito.sistema.service;

import com.transito.sistema.entity.Condutor;
import com.transito.sistema.entity.Infracao;
import com.transito.sistema.entity.Veiculo;
import com.transito.sistema.exception.BusinessException;
import com.transito.sistema.repository.CondutorRepository;
import com.transito.sistema.repository.InfracaoRepository;
import com.transito.sistema.repository.VeiculoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VeiculoServiceTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private CondutorRepository condutorRepository;

    @Mock
    private InfracaoRepository infracaoRepository;

    @InjectMocks
    private VeiculoService veiculoService;

    @Test
    void deveExcluirVeiculoSemInfracoes() {

        Veiculo veiculo =
                criarVeiculo();

        when(veiculoRepository.findById(1L))
                .thenReturn(
                        Optional.of(veiculo)
                );

        when(infracaoRepository.findByVeiculoId(1L))
                .thenReturn(
                        List.of()
                );

        veiculoService.excluir(1L);

        verify(
                veiculoRepository,
                times(1)
        ).delete(veiculo);
    }

    @Test
    void naoDeveExcluirVeiculoComInfracoes() {

        Veiculo veiculo =
                criarVeiculo();

        Infracao infracao =
                new Infracao();

        when(veiculoRepository.findById(1L))
                .thenReturn(
                        Optional.of(veiculo)
                );

        when(infracaoRepository.findByVeiculoId(1L))
                .thenReturn(
                        List.of(infracao)
                );

        assertThrows(
                BusinessException.class,
                () ->
                        veiculoService.excluir(
                                1L
                        )
        );

        verify(
                veiculoRepository,
                never()
        ).delete(any());
    }

    private Veiculo criarVeiculo() {

        Condutor condutor =
                new Condutor();

        condutor.setNome(
                "Joao"
        );

        condutor.setCpf(
                "12345678900"
        );

        condutor.setNumeroCnh(
                "12345678901"
        );

        condutor.setPontuacaoCnh(
                0
        );

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
}