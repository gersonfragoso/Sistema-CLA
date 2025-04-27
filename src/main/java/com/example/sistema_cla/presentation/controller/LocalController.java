package com.example.sistema_cla.presentation.controller;


import com.example.sistema_cla.presentation.dto.request.LocalRequest;
import com.example.sistema_cla.presentation.dto.response.LocalResponse;
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
@RequestMapping("/api/locais")
public class LocalController {

    // Não injetamos dependências via construtor, usamos a fachada singleton

    @Operation(
            summary = "Lista todos os locais",
            description = "Retorna uma lista de todos os locais cadastrados"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<LocalResponse>> listarLocais() {
        return ResponseEntity.ok(APIFacade.getInstance().listarLocais());
    }

    @Operation(
            summary = "Cria um novo local",
            description = "Cria um novo local com as informações fornecidas"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Local criado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<LocalResponse> criarLocal(@RequestBody LocalRequest request) {
        return ResponseEntity.ok(APIFacade.getInstance().criarLocal(request));
    }
}