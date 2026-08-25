package com.transito.sistema.service;

import com.transito.sistema.entity.Condutor;
import com.transito.sistema.exception.ResourceNotFoundException;
import com.transito.sistema.repository.CondutorRepository;
import org.springframework.stereotype.Service;
import com.transito.sistema.dto.CondutorRequest;
import com.transito.sistema.dto.CondutorResponse;
import java.util.List;

@Service
public class CondutorService {

    private final CondutorRepository condutorRepository;

    public CondutorService(CondutorRepository condutorRepository) {
        this.condutorRepository = condutorRepository;
    }

    public List<CondutorResponse> listarTodos() {

    return condutorRepository.findAll()
            .stream()
            .map(condutor -> new CondutorResponse(
                    condutor.getId(),
                    condutor.getNome(),
                    condutor.getCpf(),
                    condutor.getNumeroCnh(),
                    condutor.getPontuacaoCnh()
            ))
            .toList();
    }
    public Condutor buscarPorId(Long id) {
    return condutorRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Condutor não encontrado"));
    }

    public CondutorResponse salvar(CondutorRequest request) {

    Condutor condutor = new Condutor();

    condutor.setNome(request.getNome());
    condutor.setCpf(request.getCpf());
    condutor.setNumeroCnh(request.getNumeroCnh());
    condutor.setPontuacaoCnh(0);

    Condutor salvo = condutorRepository.save(condutor);

    return new CondutorResponse(
            salvo.getId(),
            salvo.getNome(),
            salvo.getCpf(),
            salvo.getNumeroCnh(),
            salvo.getPontuacaoCnh()
    );
}
}