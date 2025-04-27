package com.example.sistema_cla.presentation.controller;


import com.example.sistema_cla.presentation.dto.request.AvaliacaoRequest;
import com.example.sistema_cla.presentation.dto.response.AvaliacaoResponse;
import com.example.sistema_cla.presentation.facade.APIFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller que utiliza o padrão Facade Singleton para acessar os serviços do sistema.
 */
@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    // Não injetamos dependências via construtor, usamos a fachada singleton

    @Operation(
            summary = "Cria uma nova avaliação",
            description = "Cria uma nova avaliação para um local por um usuário"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação criada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<AvaliacaoResponse> criarAvaliacao(@RequestBody AvaliacaoRequest request) {
        return ResponseEntity.ok(APIFacade.getInstance().criarAvaliacao(request));
    }
}
