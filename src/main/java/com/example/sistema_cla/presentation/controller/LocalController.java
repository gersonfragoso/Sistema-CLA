package com.example.sistema_cla.presentation.controller;


import com.example.sistema_cla.presentation.dto.request.LocalRequest;
import com.example.sistema_cla.presentation.dto.response.LocalComAvaliacoesResponse;
import com.example.sistema_cla.presentation.dto.response.LocalResponse;
import com.example.sistema_cla.presentation.facade.APIFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller que utiliza o padrão Facade Singleton para acessar os serviços do sistema.
 */

@RestController
@RequestMapping("/api/locais")
public class LocalController {

    // Não injetamos dependências via construtor, usamos a fachada singleton

    @GetMapping
    public ResponseEntity<List<LocalResponse>> listarLocais() {
        return ResponseEntity.ok(APIFacade.getInstance().listarLocais());
    }

    @PostMapping
    public ResponseEntity<LocalResponse> criarLocal(@Valid @RequestBody LocalRequest request) {
        return ResponseEntity.ok(APIFacade.getInstance().criarLocal(request));
    }
    @GetMapping("/{id}")
    public ResponseEntity<LocalComAvaliacoesResponse> buscarLocalDetalhado(@PathVariable Long id) {
        return ResponseEntity.ok(APIFacade.getInstance().buscarLocalDetalhado(id));
    }
}