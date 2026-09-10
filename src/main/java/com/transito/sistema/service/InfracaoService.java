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
    private final CondutorRepository condutorRepository;
    private final VeiculoRepository veiculoRepository;
    private final TipoInfracaoRepository tipoInfracaoRepository;

    public InfracaoService(
            InfracaoRepository infracaoRepository,
            CondutorRepository condutorRepository,
            VeiculoRepository veiculoRepository,
            TipoInfracaoRepository tipoInfracaoRepository
    ) {
        this.infracaoRepository = infracaoRepository;
        this.condutorRepository = condutorRepository;
        this.veiculoRepository = veiculoRepository;
        this.tipoInfracaoRepository = tipoInfracaoRepository;
    }

    public InfracaoResponse salvar(InfracaoRequest request) {

        Veiculo veiculo = buscarVeiculo(request.getVeiculoId());

        Condutor condutor = buscarCondutor(request.getCondutorId());

        TipoInfracao tipoInfracao =
                buscarTipoInfracao(request.getTipoInfracaoId());

        validarVeiculoDoCondutor(veiculo, condutor);

        Infracao infracao = new Infracao();

        infracao.setCondutor(condutor);
        infracao.setVeiculo(veiculo);
        infracao.setTipoInfracao(tipoInfracao);
        infracao.setDataHora(request.getDataHora());

        adicionarPontos(
                condutor,
                tipoInfracao.getPontos()
        );

        Infracao salvo = infracaoRepository.save(infracao);

        condutorRepository.save(condutor);

        return converterParaResponse(salvo);
    }

    public List<InfracaoResponse> listarTodos() {

        return infracaoRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public InfracaoResponse buscarPorId(Long id) {

        Infracao infracao = infracaoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Infração não encontrada"
                        )
                );

        return converterParaResponse(infracao);
    }

    public List<InfracaoResponse> listarPorCondutor(Long condutorId) {

        buscarCondutor(condutorId);

        return infracaoRepository.findByCondutorId(condutorId)
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public List<InfracaoResponse> listarPorVeiculo(Long veiculoId) {

        buscarVeiculo(veiculoId);

        return infracaoRepository.findByVeiculoId(veiculoId)
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public InfracaoResponse atualizar(
            Long id,
            InfracaoRequest request
    ) {

        Infracao infracao = infracaoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Infração não encontrada"
                        )
                );

        Condutor novoCondutor =
                buscarCondutor(request.getCondutorId());

        Veiculo novoVeiculo =
                buscarVeiculo(request.getVeiculoId());

        TipoInfracao novoTipo =
                buscarTipoInfracao(request.getTipoInfracaoId());

        validarVeiculoDoCondutor(
                novoVeiculo,
                novoCondutor
        );

        Condutor condutorAnterior =
                infracao.getCondutor();

        TipoInfracao tipoAnterior =
                infracao.getTipoInfracao();

        removerPontos(
                condutorAnterior,
                tipoAnterior.getPontos()
        );

        infracao.setCondutor(novoCondutor);
        infracao.setVeiculo(novoVeiculo);
        infracao.setTipoInfracao(novoTipo);
        infracao.setDataHora(request.getDataHora());

        adicionarPontos(
                novoCondutor,
                novoTipo.getPontos()
        );

        Infracao atualizado =
                infracaoRepository.save(infracao);

        condutorRepository.save(condutorAnterior);

        /*
         * Se for um condutor diferente, também precisamos
         * salvar a pontuação do novo condutor.
         */
        if (!mesmoCondutor(condutorAnterior, novoCondutor)) {
            condutorRepository.save(novoCondutor);
        }

        return converterParaResponse(atualizado);
    }

    public void excluir(Long id) {

        Infracao infracao = infracaoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Infração não encontrada"
                        )
                );

        Condutor condutor = infracao.getCondutor();

        removerPontos(
                condutor,
                infracao.getTipoInfracao().getPontos()
        );

        infracaoRepository.delete(infracao);

        condutorRepository.save(condutor);
    }

    private Condutor buscarCondutor(Long id) {

        return condutorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Condutor não encontrado"
                        )
                );
    }

    private Veiculo buscarVeiculo(Long id) {

        return veiculoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Veículo não encontrado"
                        )
                );
    }

    private TipoInfracao buscarTipoInfracao(Long id) {

        return tipoInfracaoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Tipo de infração não encontrado"
                        )
                );
    }

    private void validarVeiculoDoCondutor(
            Veiculo veiculo,
            Condutor condutor
    ) {

        if (veiculo.getCondutor() == null) {
            throw new IllegalArgumentException(
                    "O veículo não possui um condutor associado."
            );
        }

        Condutor condutorDoVeiculo = veiculo.getCondutor();

        /*
         * Quando os objetos possuem ID, usamos o ID para verificar
         * se representam o mesmo registro no banco.
         */
        if (condutorDoVeiculo.getId() != null
                && condutor.getId() != null) {

            if (!condutorDoVeiculo.getId()
                    .equals(condutor.getId())) {

                throw new IllegalArgumentException(
                        "O veículo informado não pertence ao condutor."
                );
            }

            return;
        }

        /*
         * Nos testes unitários os objetos podem ainda não possuir ID.
         * Nesse caso, verificamos se é exatamente o mesmo objeto.
         */
        if (condutorDoVeiculo != condutor) {
            throw new IllegalArgumentException(
                    "O veículo informado não pertence ao condutor."
            );
        }
    }

    private boolean mesmoCondutor(
            Condutor primeiro,
            Condutor segundo
    ) {

        if (primeiro == segundo) {
            return true;
        }

        if (primeiro == null || segundo == null) {
            return false;
        }

        if (primeiro.getId() == null || segundo.getId() == null) {
            return false;
        }

        return primeiro.getId().equals(segundo.getId());
    }

    private void adicionarPontos(
            Condutor condutor,
            Integer pontos
    ) {

        int pontuacaoAtual =
                condutor.getPontuacaoCnh();

        condutor.setPontuacaoCnh(
                pontuacaoAtual + pontos
        );
    }

    private void removerPontos(
            Condutor condutor,
            Integer pontos
    ) {

        int pontuacaoAtual =
                condutor.getPontuacaoCnh();

        int novaPontuacao =
                pontuacaoAtual - pontos;

        condutor.setPontuacaoCnh(
                Math.max(novaPontuacao, 0)
        );
    }

    private InfracaoResponse converterParaResponse(
            Infracao infracao
    ) {

        return new InfracaoResponse(
                infracao.getId(),

                infracao.getCondutor().getId(),
                infracao.getCondutor().getNome(),

                infracao.getVeiculo().getId(),
                infracao.getVeiculo().getPlaca(),

                infracao.getTipoInfracao().getId(),
                infracao.getTipoInfracao().getCodigo(),
                infracao.getTipoInfracao().getDescricao(),

                infracao.getTipoInfracao().getGravidade(),
                infracao.getTipoInfracao().getPontos(),
                infracao.getTipoInfracao().getValor(),

                infracao.getDataHora()
        );
    }
}