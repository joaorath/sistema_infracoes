package com.transito.sistema.repository;

import com.transito.sistema.entity.TipoInfracao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoInfracaoRepository extends JpaRepository<TipoInfracao, Long> {
}