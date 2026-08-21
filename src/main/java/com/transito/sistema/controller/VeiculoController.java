package com.transito.sistema.controller;

import com.transito.sistema.dto.VeiculoRequest;
import com.transito.sistema.dto.VeiculoResponse;
import com.transito.sistema.service.VeiculoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @GetMapping
    public List<VeiculoResponse> listarTodos() {
        return veiculoService.listarTodos();
    }

    @PostMapping
    public VeiculoResponse salvar(
            @Valid @RequestBody VeiculoRequest request) {

        return veiculoService.salvar(request);
    }
}