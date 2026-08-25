package com.transito.sistema.controller;

import com.transito.sistema.dto.InfracaoRequest;
import com.transito.sistema.dto.InfracaoResponse;
import com.transito.sistema.service.InfracaoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/infracoes")
public class InfracaoController {

    private final InfracaoService infracaoService;

    public InfracaoController(InfracaoService infracaoService) {
        this.infracaoService = infracaoService;
    }

    @GetMapping
    public List<InfracaoResponse> listarTodos() {
        return infracaoService.listarTodos();
    }

    @GetMapping("/condutor/{condutorId}")
    public List<InfracaoResponse> listarPorCondutor(
        @PathVariable Long condutorId) {

        return infracaoService.listarPorCondutor(condutorId);
    }

    @GetMapping("/veiculo/{veiculoId}")
    public List<InfracaoResponse> listarPorVeiculo(
        @PathVariable Long veiculoId) {

        return infracaoService.listarPorVeiculo(veiculoId);
    }

    @PostMapping
    public InfracaoResponse salvar(
            @Valid @RequestBody InfracaoRequest request) {

        return infracaoService.salvar(request);
    }
}