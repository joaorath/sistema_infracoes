package com.transito.sistema.service;

import com.transito.sistema.dto.TipoInfracaoRequest;
import com.transito.sistema.dto.TipoInfracaoResponse;
import com.transito.sistema.entity.TipoInfracao;
import com.transito.sistema.exception.ResourceNotFoundException;
import com.transito.sistema.repository.TipoInfracaoRepository;
import org.springframework.stereotype.Service;
import com.transito.sistema.exception.BusinessException;
import com.transito.sistema.repository.InfracaoRepository;

import java.util.List;

@Service
public class TipoInfracaoService {

    private final TipoInfracaoRepository tipoInfracaoRepository;
    private final InfracaoRepository infracaoRepository;

    public TipoInfracaoService(
                TipoInfracaoRepository tipoInfracaoRepository,
                InfracaoRepository infracaoRepository) {

        this.tipoInfracaoRepository = tipoInfracaoRepository;
        this.infracaoRepository = infracaoRepository;
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

        boolean possuiInfracoes =
                !infracaoRepository.findByTipoInfracaoId(id).isEmpty();

        if (possuiInfracoes
                && !tipoInfracao.getPontos().equals(request.getPontos())) {

                throw new BusinessException(
                        "Não é possível alterar os pontos deste tipo de infração porque existem infrações vinculadas a ele."
                );
        }

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

        if (!infracaoRepository.findByTipoInfracaoId(id).isEmpty()) {

                throw new BusinessException(
                        "Não é possível excluir o tipo de infração porque existem infrações vinculadas a ele."
                );
        }

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