package com.transito.sistema.service;

import com.transito.sistema.entity.Condutor;
import com.transito.sistema.repository.CondutorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CondutorService {

    private final CondutorRepository condutorRepository;

    public CondutorService(CondutorRepository condutorRepository) {
        this.condutorRepository = condutorRepository;
    }

    public List<Condutor> listarTodos() {
        return condutorRepository.findAll();
    }

    public Condutor salvar(Condutor condutor) {
        return condutorRepository.save(condutor);
    }
}