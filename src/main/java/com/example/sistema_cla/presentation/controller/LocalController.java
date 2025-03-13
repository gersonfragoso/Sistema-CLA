package com.example.sistema_cla.presentation.controller;

import com.example.sistema_cla.application.service.LocalService;
import com.example.sistema_cla.presentation.dto.request.LocalRequest;
import com.example.sistema_cla.presentation.dto.response.LocalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locais")
public class LocalController {
    private final LocalService localService;

    public LocalController(LocalService localService) {
        this.localService = localService;
    }

    @Operation(
            summary = "Lista todos os locais",
            description = "Retorna uma lista de todos os locais cadastrados"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor"
            )
    })
    @GetMapping
    public ResponseEntity<List<LocalResponse>> listarLocais() {
        return ResponseEntity.ok(localService.listarLocais());
    }


    @Operation(
            summary = "Cria um novo local",
            description = "Cria um novo local com as informações fornecidas"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Local criado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor"
            )
    })
    @PostMapping
    public ResponseEntity<LocalResponse> criarLocal(@RequestBody LocalRequest request) {
        return ResponseEntity.ok(localService.criarLocal(request));
    }
}