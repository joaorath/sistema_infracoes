package com.transito.sistema.repository;

import com.transito.sistema.entity.Infracao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InfracaoRepository extends JpaRepository<Infracao, Long> {

    List<Infracao> findByCondutorId(Long condutorId);

    List<Infracao> findByVeiculoId(Long veiculoId);

    List<Infracao> findByTipoInfracaoId(Long tipoInfracaoId);
}