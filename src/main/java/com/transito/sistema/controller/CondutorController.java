package com.transito.sistema.controller;

import com.transito.sistema.entity.Condutor;
import com.transito.sistema.service.CondutorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/condutores")
public class CondutorController {

    private final CondutorService condutorService;

    public CondutorController(CondutorService condutorService) {
        this.condutorService = condutorService;
    }

    @GetMapping
    public List<Condutor> listarTodos() {
        return condutorService.listarTodos();
    }

    @PostMapping
    public Condutor salvar(@Valid @RequestBody Condutor condutor) {
        return condutorService.salvar(condutor);
    }
}