package com.transito.sistema.service;

import com.transito.sistema.dto.CondutorRequest;
import com.transito.sistema.dto.CondutorResponse;
import com.transito.sistema.entity.Condutor;
import com.transito.sistema.exception.BusinessException;
import com.transito.sistema.exception.ResourceNotFoundException;
import com.transito.sistema.repository.CondutorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CondutorService {

    private final CondutorRepository condutorRepository;

    public CondutorService(CondutorRepository condutorRepository) {
        this.condutorRepository = condutorRepository;
    }

    private CondutorResponse converterParaResponse(Condutor condutor) {
        return new CondutorResponse(
                condutor.getId(),
                condutor.getNome(),
                condutor.getCpf(),
                condutor.getNumeroCnh(),
                condutor.getPontuacaoCnh()
        );
    }

    public List<CondutorResponse> listarTodos() {
        return condutorRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public Condutor buscarPorId(Long id) {
        return condutorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Condutor não encontrado"
                        )
                );
    }

    public CondutorResponse buscarResponsePorId(Long id) {
        Condutor condutor = buscarPorId(id);

        return converterParaResponse(condutor);
    }

    public CondutorResponse salvar(CondutorRequest request) {

        if (condutorRepository.existsByCpf(request.getCpf())) {
            throw new BusinessException(
                    "Já existe um condutor cadastrado com este CPF."
            );
        }

        if (condutorRepository.existsByNumeroCnh(request.getNumeroCnh())) {
            throw new BusinessException(
                    "Já existe um condutor cadastrado com este número de CNH."
            );
        }

        Condutor condutor = new Condutor();

        condutor.setNome(request.getNome());
        condutor.setCpf(request.getCpf());
        condutor.setNumeroCnh(request.getNumeroCnh());
        condutor.setPontuacaoCnh(0);

        Condutor salvo =
                condutorRepository.save(condutor);

        return converterParaResponse(salvo);
    }

    public CondutorResponse atualizar(
            Long id,
            CondutorRequest request) {

        Condutor condutor = buscarPorId(id);

        if (condutorRepository.existsByCpfAndIdNot(
                request.getCpf(),
                id)) {

            throw new BusinessException(
                    "Já existe outro condutor cadastrado com este CPF."
            );
        }

        if (condutorRepository.existsByNumeroCnhAndIdNot(
                request.getNumeroCnh(),
                id)) {

            throw new BusinessException(
                    "Já existe outro condutor cadastrado com este número de CNH."
            );
        }

        condutor.setNome(request.getNome());
        condutor.setCpf(request.getCpf());
        condutor.setNumeroCnh(request.getNumeroCnh());

        Condutor atualizado =
                condutorRepository.save(condutor);

        return converterParaResponse(atualizado);
    }
}