package com.transito.sistema.service;

import com.transito.sistema.dto.TipoInfracaoRequest;
import com.transito.sistema.dto.TipoInfracaoResponse;
import com.transito.sistema.entity.TipoInfracao;
import com.transito.sistema.exception.ResourceNotFoundException;
import com.transito.sistema.repository.TipoInfracaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoInfracaoService {

    private final TipoInfracaoRepository tipoInfracaoRepository;

    public TipoInfracaoService(
            TipoInfracaoRepository tipoInfracaoRepository) {
        this.tipoInfracaoRepository = tipoInfracaoRepository;
    }

    public TipoInfracaoResponse salvar(TipoInfracaoRequest request) {

        TipoInfracao tipoInfracao = new TipoInfracao();

        tipoInfracao.setCodigo(request.getCodigo());
        tipoInfracao.setDescricao(request.getDescricao());
        tipoInfracao.setGravidade(request.getGravidade());
        tipoInfracao.setPontos(request.getPontos());
        tipoInfracao.setValor(request.getValor());

        TipoInfracao salvo = tipoInfracaoRepository.save(tipoInfracao);

        return converterParaResponse(salvo);
    }

    public List<TipoInfracaoResponse> listarTodos() {

        return tipoInfracaoRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public TipoInfracao buscarPorId(Long id) {

        return tipoInfracaoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Tipo de infração não encontrado"
                        ));
    }

    public TipoInfracaoResponse atualizar(
            Long id,
            TipoInfracaoRequest request) {

        TipoInfracao tipoInfracao = tipoInfracaoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Tipo de infração não encontrado"
                        ));

        tipoInfracao.setCodigo(request.getCodigo());
        tipoInfracao.setDescricao(request.getDescricao());
        tipoInfracao.setGravidade(request.getGravidade());
        tipoInfracao.setPontos(request.getPontos());
        tipoInfracao.setValor(request.getValor());

        TipoInfracao atualizado =
                tipoInfracaoRepository.save(tipoInfracao);

        return converterParaResponse(atualizado);
    }

    public void excluir(Long id) {

        TipoInfracao tipoInfracao =
                tipoInfracaoRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Tipo de infração não encontrado"
                                ));

        tipoInfracaoRepository.delete(tipoInfracao);
    }

    private TipoInfracaoResponse converterParaResponse(
            TipoInfracao tipoInfracao) {

        return new TipoInfracaoResponse(
                tipoInfracao.getId(),
                tipoInfracao.getCodigo(),
                tipoInfracao.getDescricao(),
                tipoInfracao.getGravidade(),
                tipoInfracao.getPontos(),
                tipoInfracao.getValor()
        );
    }
}