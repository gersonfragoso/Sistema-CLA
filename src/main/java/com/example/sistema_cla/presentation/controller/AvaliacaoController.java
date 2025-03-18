package com.example.sistema_cla.presentation.controller;

import com.example.sistema_cla.application.service.AvaliacaoServiceImpl;
import com.example.sistema_cla.presentation.dto.request.AvaliacaoRequest;
import com.example.sistema_cla.presentation.dto.response.AvaliacaoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/avaliar")
public class AvaliacaoController {

    private final AvaliacaoServiceImpl avaliacaoService;

    public AvaliacaoController(AvaliacaoServiceImpl avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @Operation(
            summary = "Cria uma nova avaliação",
            description = "Cria uma nova avaliação para um local por um usuário"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Avaliação criada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor"
            )
    })
    @PostMapping
    public ResponseEntity<AvaliacaoResponse> criarAvaliacao(@RequestBody AvaliacaoRequest request) {
        return ResponseEntity.ok(avaliacaoService.criarAvaliacao(request));
    }
}

