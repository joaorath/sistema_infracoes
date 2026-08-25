package com.transito.sistema.controller;

import com.transito.sistema.dto.CondutorRequest;
import com.transito.sistema.dto.CondutorResponse;
import com.transito.sistema.entity.Condutor;
import com.transito.sistema.service.CondutorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/condutores")
public class CondutorController {

    private final CondutorService condutorService;

    public CondutorController(CondutorService condutorService) {
        this.condutorService = condutorService;
    }

    @GetMapping
    public List<CondutorResponse> listarTodos() {
        return condutorService.listarTodos();
    }

    @GetMapping("/{id}")
    public Condutor buscarPorId(@PathVariable Long id) {
        return condutorService.buscarPorId(id);
    }

    @PostMapping
    public CondutorResponse salvar(
            @Valid @RequestBody CondutorRequest request) {

        return condutorService.salvar(request);
    }
}