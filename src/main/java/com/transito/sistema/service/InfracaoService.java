package com.transito.sistema.service;

import com.transito.sistema.dto.InfracaoRequest;
import com.transito.sistema.dto.InfracaoResponse;
import com.transito.sistema.entity.Condutor;
import com.transito.sistema.entity.Infracao;
import com.transito.sistema.entity.TipoInfracao;
import com.transito.sistema.entity.Veiculo;
import com.transito.sistema.exception.ResourceNotFoundException;
import com.transito.sistema.repository.CondutorRepository;
import com.transito.sistema.repository.InfracaoRepository;
import com.transito.sistema.repository.TipoInfracaoRepository;
import com.transito.sistema.repository.VeiculoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfracaoService {

    private final InfracaoRepository infracaoRepository;
    private final VeiculoRepository veiculoRepository;
    private final CondutorRepository condutorRepository;
    private final TipoInfracaoRepository tipoInfracaoRepository;

    public InfracaoService(
            InfracaoRepository infracaoRepository,
            VeiculoRepository veiculoRepository,
            CondutorRepository condutorRepository,
            TipoInfracaoRepository tipoInfracaoRepository) {

        this.infracaoRepository = infracaoRepository;
        this.veiculoRepository = veiculoRepository;
        this.condutorRepository = condutorRepository;
        this.tipoInfracaoRepository = tipoInfracaoRepository;
    }

    public InfracaoResponse salvar(InfracaoRequest request) {

        Veiculo veiculo = veiculoRepository.findById(request.getVeiculoId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Veículo não encontrado"));

        Condutor condutor = condutorRepository.findById(request.getCondutorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                             "Condutor não encontrado"));

        TipoInfracao tipoInfracao =
                tipoInfracaoRepository.findById(request.getTipoInfracaoId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Tipo de infração não encontrado"));

        Infracao infracao = new Infracao();

        infracao.setVeiculo(veiculo);
        infracao.setCondutor(condutor);
        infracao.setTipoInfracao(tipoInfracao);
        infracao.setDataHora(request.getDataHora());

        // Adiciona os pontos da infração à CNH do condutor
        int pontos = tipoInfracao.getPontos();

        condutor.setPontuacaoCnh(
                condutor.getPontuacaoCnh() + pontos
        );

        condutorRepository.save(condutor);

        Infracao salva = infracaoRepository.save(infracao);

        return converterParaResponse(salva);
    }

    public List<InfracaoResponse> listarTodos() {

        return infracaoRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public List<InfracaoResponse> listarPorCondutor(Long condutorId) {
        return infracaoRepository.findByCondutorId(condutorId)
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public List<InfracaoResponse> listarPorVeiculo(Long veiculoId) {
        return infracaoRepository.findByVeiculoId(veiculoId)
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    private InfracaoResponse converterParaResponse(Infracao infracao) {

        return new InfracaoResponse(
                infracao.getId(),

                infracao.getVeiculo().getId(),
                infracao.getVeiculo().getPlaca(),

                infracao.getCondutor().getId(),
                infracao.getCondutor().getNome(),

                infracao.getTipoInfracao().getId(),
                infracao.getTipoInfracao().getCodigo(),
                infracao.getTipoInfracao().getDescricao(),

                infracao.getTipoInfracao().getPontos(),
                infracao.getTipoInfracao().getValor(),

                infracao.getDataHora()
        );
    }
}