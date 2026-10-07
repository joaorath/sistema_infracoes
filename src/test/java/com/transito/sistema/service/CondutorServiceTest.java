package com.transito.sistema.service;

import com.transito.sistema.dto.CondutorRequest;
import com.transito.sistema.dto.CondutorResponse;
import com.transito.sistema.entity.Condutor;
import com.transito.sistema.exception.BusinessException;
import com.transito.sistema.exception.ResourceNotFoundException;
import com.transito.sistema.repository.CondutorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CondutorServiceTest {

    @Mock
    private CondutorRepository condutorRepository;

    @InjectMocks
    private CondutorService condutorService;

    @Test
    void deveListarTodosOsCondutores() {

        Condutor condutor =
                criarCondutor();

        when(condutorRepository.findAll())
                .thenReturn(
                        List.of(condutor)
                );

        List<CondutorResponse> resultado =
                condutorService.listarTodos();

        assertEquals(
                1,
                resultado.size()
        );

        assertEquals(
                "Joao da Silva",
                resultado.get(0).getNome()
        );
    }

    @Test
    void deveBuscarCondutorPorId() {

        Condutor condutor =
                criarCondutor();

        when(condutorRepository.findById(1L))
                .thenReturn(
                        Optional.of(condutor)
                );

        Condutor resultado =
                condutorService.buscarPorId(1L);

        assertNotNull(resultado);

        assertEquals(
                "Joao da Silva",
                resultado.getNome()
        );
    }

    @Test
    void deveLancarExcecaoQuandoCondutorNaoExiste() {

        when(condutorRepository.findById(999L))
                .thenReturn(
                        Optional.empty()
                );

        assertThrows(
                ResourceNotFoundException.class,
                () ->
                        condutorService.buscarPorId(
                                999L
                        )
        );
    }

    @Test
    void deveCadastrarCondutor() {

        CondutorRequest request =
                criarRequest();

        when(condutorRepository.existsByCpf(
                request.getCpf()))
                .thenReturn(false);

        when(condutorRepository.existsByNumeroCnh(
                request.getNumeroCnh()))
                .thenReturn(false);

        when(condutorRepository.save(
                any(Condutor.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        CondutorResponse response =
                condutorService.salvar(request);

        assertNotNull(response);

        assertEquals(
                "Joao da Silva",
                response.getNome()
        );

        assertEquals(
                0,
                response.getPontuacaoCnh()
        );
    }

    @Test
    void naoDeveCadastrarCpfDuplicado() {

        CondutorRequest request =
                criarRequest();

        when(condutorRepository.existsByCpf(
                request.getCpf()))
                .thenReturn(true);

        BusinessException exception =
                assertThrows(
                        BusinessException.class,
                        () ->
                                condutorService.salvar(
                                        request
                                )
                );

        assertEquals(
                "Já existe um condutor cadastrado com este CPF.",
                exception.getMessage()
        );

        verify(
                condutorRepository,
                never()
        ).save(any());
    }

    @Test
    void naoDeveCadastrarCnhDuplicada() {

        CondutorRequest request =
                criarRequest();

        when(condutorRepository.existsByCpf(
                request.getCpf()))
                .thenReturn(false);

        when(condutorRepository.existsByNumeroCnh(
                request.getNumeroCnh()))
                .thenReturn(true);

        assertThrows(
                BusinessException.class,
                () ->
                        condutorService.salvar(
                                request
                        )
        );

        verify(
                condutorRepository,
                never()
        ).save(any());
    }

    private Condutor criarCondutor() {

        Condutor condutor =
                new Condutor();

        condutor.setNome(
                "Joao da Silva"
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

        return condutor;
    }

    private CondutorRequest criarRequest() {

        CondutorRequest request =
                new CondutorRequest();

        request.setNome(
                "Joao da Silva"
        );

        request.setCpf(
                "12345678900"
        );

        request.setNumeroCnh(
                "12345678901"
        );

        return request;
    }
}