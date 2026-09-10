package com.transito.sistema.controller;

import com.transito.sistema.dto.TipoInfracaoRequest;
import com.transito.sistema.dto.TipoInfracaoResponse;
import com.transito.sistema.entity.TipoInfracao;
import com.transito.sistema.service.TipoInfracaoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipos-infracao")
public class TipoInfracaoController {

    private final TipoInfracaoService tipoInfracaoService;

    public TipoInfracaoController(
            TipoInfracaoService tipoInfracaoService) {
        this.tipoInfracaoService = tipoInfracaoService;
    }

    @GetMapping
    public List<TipoInfracaoResponse> listarTodos() {
        return tipoInfracaoService.listarTodos();
    }

    @GetMapping("/{id}")
    public TipoInfracao buscarPorId(@PathVariable Long id) {
        return tipoInfracaoService.buscarPorId(id);
    }

    @PostMapping
    public TipoInfracaoResponse salvar(
            @Valid @RequestBody TipoInfracaoRequest request) {

        return tipoInfracaoService.salvar(request);
    }

    @PutMapping("/{id}")
    public TipoInfracaoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TipoInfracaoRequest request) {

        return tipoInfracaoService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        tipoInfracaoService.excluir(id);
    }
}