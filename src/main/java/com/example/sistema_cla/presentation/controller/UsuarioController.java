package com.example.sistema_cla.presentation.controller;

import com.example.sistema_cla.presentation.dto.request.UsuarioRequest;
import com.example.sistema_cla.presentation.dto.response.UsuarioResponse;
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
@RequestMapping("/api/usuarios")
public class UsuarioController {

    // Não injetamos dependências via construtor, usamos a fachada singleton

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        return ResponseEntity.ok(APIFacade.getInstance().listarUsuarios());
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> criarUsuario(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(APIFacade.getInstance().criarUsuario(request));
    }
}