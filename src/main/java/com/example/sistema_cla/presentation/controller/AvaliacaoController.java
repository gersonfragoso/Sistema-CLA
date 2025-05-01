package com.example.sistema_cla.presentation.controller;


import com.example.sistema_cla.presentation.dto.request.AvaliacaoRequest;
import com.example.sistema_cla.presentation.dto.response.AvaliacaoResponse;
import com.example.sistema_cla.presentation.facade.APIFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller que utiliza o padrão Facade Singleton para acessar os serviços do sistema.
 */
@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    @PostMapping
    @Operation(summary = "Criar uma avaliação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Usuário ou local não encontrado")
    })
    public ResponseEntity<AvaliacaoResponse> criarAvaliacao(@Valid @RequestBody AvaliacaoRequest request) {
        return ResponseEntity.ok(APIFacade.getInstance().criarAvaliacao(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar uma avaliação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação editada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada")
    })
    public ResponseEntity<AvaliacaoResponse> editarAvaliacao(
            @PathVariable Long id,
            @Valid @RequestBody AvaliacaoRequest request) {
        return ResponseEntity.ok(APIFacade.getInstance().editarAvaliacao(id, request));
    }

    @PostMapping("/{id}/desfazer")
    @Operation(summary = "Desfazer a última edição de uma avaliação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edição desfeita com sucesso"),
            @ApiResponse(responseCode = "400", description = "Não há edições para desfazer"),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada")
    })
    public ResponseEntity<AvaliacaoResponse> desfazerEdicao(@PathVariable Long id) {
        return ResponseEntity.ok(APIFacade.getInstance().desfazerAvaliacaoEdicao(id));
    }
}
