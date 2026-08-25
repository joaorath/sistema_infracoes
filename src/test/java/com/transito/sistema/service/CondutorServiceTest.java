package com.transito.sistema.service;

import com.transito.sistema.dto.CondutorResponse;
import com.transito.sistema.entity.Condutor;
import com.transito.sistema.exception.ResourceNotFoundException;
import com.transito.sistema.repository.CondutorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CondutorServiceTest {

    @Mock
    private CondutorRepository condutorRepository;

    @InjectMocks
    private CondutorService condutorService;

    @Test
    void deveListarTodosOsCondutores() {

        Condutor condutor = new Condutor();

        condutor.setNome("Joao da Silva");
        condutor.setCpf("12345678900");
        condutor.setNumeroCnh("12345678901");
        condutor.setPontuacaoCnh(0);

        when(condutorRepository.findAll())
                .thenReturn(List.of(condutor));

        List<CondutorResponse> resultado = condutorService.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Joao da Silva", resultado.get(0).getNome());

        verify(condutorRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarCondutorPorId() {

        Condutor condutor = new Condutor();

        condutor.setNome("Maria Oliveira");
        condutor.setCpf("98765432100");
        condutor.setNumeroCnh("98765432101");
        condutor.setPontuacaoCnh(0);

        when(condutorRepository.findById(1L))
                .thenReturn(java.util.Optional.of(condutor));

        Condutor resultado = condutorService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Maria Oliveira", resultado.getNome());
        assertEquals("98765432101", resultado.getNumeroCnh());

        verify(condutorRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoCondutorNaoExiste() {

        when(condutorRepository.findById(999L))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> condutorService.buscarPorId(999L));

        verify(condutorRepository, times(1)).findById(999L);
    }
}