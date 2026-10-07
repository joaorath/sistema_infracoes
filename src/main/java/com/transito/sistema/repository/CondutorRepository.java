package com.transito.sistema.repository;

import com.transito.sistema.entity.Condutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CondutorRepository
        extends JpaRepository<Condutor, Long> {

    boolean existsByCpf(String cpf);

    boolean existsByNumeroCnh(String numeroCnh);

    boolean existsByCpfAndIdNot(
            String cpf,
            Long id
    );

    boolean existsByNumeroCnhAndIdNot(
            String numeroCnh,
            Long id
    );
}