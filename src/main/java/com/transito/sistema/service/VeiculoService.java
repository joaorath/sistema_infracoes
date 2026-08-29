package com.transito.sistema.service;

import com.transito.sistema.dto.VeiculoRequest;
import com.transito.sistema.dto.VeiculoResponse;
import com.transito.sistema.entity.Condutor;
import com.transito.sistema.entity.Veiculo;
import com.transito.sistema.exception.BusinessException;
import com.transito.sistema.exception.ResourceNotFoundException;
import com.transito.sistema.repository.CondutorRepository;
import com.transito.sistema.repository.VeiculoRepository;
import org.springframework.stereotype.Service;
import com.transito.sistema.repository.InfracaoRepository;
import com.transito.sistema.exception.BusinessException;

import java.util.List;

@Service
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final CondutorRepository condutorRepository;
    private final InfracaoRepository infracaoRepository;

    public VeiculoService(
        VeiculoRepository veiculoRepository,
        CondutorRepository condutorRepository,
        InfracaoRepository infracaoRepository) {

    this.veiculoRepository = veiculoRepository;
    this.condutorRepository = condutorRepository;
    this.infracaoRepository = infracaoRepository;
}

    public VeiculoResponse salvar(VeiculoRequest request) {

        Condutor condutor = condutorRepository.findById(request.getCondutorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Condutor não encontrado"));

        Veiculo veiculo = new Veiculo();

        veiculo.setPlaca(request.getPlaca());
        veiculo.setRenavam(request.getRenavam());
        veiculo.setMarca(request.getMarca());
        veiculo.setModelo(request.getModelo());
        veiculo.setAno(request.getAno());
        veiculo.setCondutor(condutor);

        Veiculo salvo = veiculoRepository.save(veiculo);

        return converterParaResponse(salvo);
    }

    public List<VeiculoResponse> listarTodos() {

        return veiculoRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public Veiculo buscarPorId(Long id) {

        return veiculoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Veículo não encontrado"));
    }

    public VeiculoResponse atualizar(Long id, VeiculoRequest request) {

        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Veículo não encontrado"));

        Condutor condutor = condutorRepository.findById(request.getCondutorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Condutor não encontrado"));

        veiculo.setPlaca(request.getPlaca());
        veiculo.setRenavam(request.getRenavam());
        veiculo.setMarca(request.getMarca());
        veiculo.setModelo(request.getModelo());
        veiculo.setAno(request.getAno());
        veiculo.setCondutor(condutor);

        Veiculo atualizado = veiculoRepository.save(veiculo);

        return converterParaResponse(atualizado);
    }

    private VeiculoResponse converterParaResponse(Veiculo veiculo) {

        return new VeiculoResponse(
                veiculo.getId(),
                veiculo.getPlaca(),
                veiculo.getRenavam(),
                veiculo.getMarca(),
                veiculo.getModelo(),
                veiculo.getAno(),
                veiculo.getCondutor().getId(),
                veiculo.getCondutor().getNome()
        );
    }

    public void excluir(Long id) {

    Veiculo veiculo = veiculoRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Veículo não encontrado"));

    if (!infracaoRepository.findByVeiculoId(id).isEmpty()) {
        throw new BusinessException(
        "Não é possível excluir o veículo porque existem infrações vinculadas a ele.");
    }

    veiculoRepository.delete(veiculo);
    }
}