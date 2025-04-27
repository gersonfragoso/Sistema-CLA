package com.example.sistema_cla.presentation.controller;

import com.example.sistema_cla.presentation.dto.request.UsuarioRequest;
import com.example.sistema_cla.presentation.dto.response.UsuarioResponse;
import com.example.sistema_cla.presentation.facade.APIFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Lista todos os usuários",
            description = "Retorna uma lista de todos os usuários cadastrados"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        return ResponseEntity.ok(APIFacade.getInstance().listarUsuarios());
    }

    @Operation(
            summary = "Cria um novo usuário",
            description = "Cria um novo usuário com as informações fornecidas"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<UsuarioResponse> criarUsuario(@RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(APIFacade.getInstance().criarUsuario(request));
    }
}