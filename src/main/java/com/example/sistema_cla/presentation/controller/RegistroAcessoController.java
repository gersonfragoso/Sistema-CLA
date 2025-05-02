package com.example.sistema_cla.presentation.controller;

import com.example.sistema_cla.domain.model.RegistroAcesso;
import com.example.sistema_cla.presentation.dto.request.RegistroAcessoRequest;
import com.example.sistema_cla.presentation.facade.APIFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller que utiliza o padrão Facade Singleton para gerenciar registros de acesso
 * e demonstrar o funcionamento do padrão Observer.
 */
@RestController
@RequestMapping("/api/acessos")
public class RegistroAcessoController {

    @PostMapping
    @Operation(summary = "Registrar um novo acesso ao sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Acesso registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<RegistroAcesso> registrarAcesso(@Valid @RequestBody RegistroAcessoRequest request) {
        return ResponseEntity.ok(APIFacade.getInstance().registrarAcesso(request));
    }

    @GetMapping("/usuario/{id}")
    @Operation(summary = "Buscar registros de acesso de um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registros encontrados")
    })
    public ResponseEntity<List<RegistroAcesso>> buscarRegistrosPorUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(APIFacade.getInstance().buscarRegistrosPorUsuario(id));
    }

    @GetMapping("/periodo")
    @Operation(summary = "Buscar registros de acesso por período")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registros encontrados")
    })
    public ResponseEntity<List<RegistroAcesso>> buscarRegistrosPorPeriodo(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim) {
        return ResponseEntity.ok(APIFacade.getInstance().buscarRegistrosPorPeriodo(inicio, fim));
    }

    @GetMapping("/usuario/{id}/periodo")
    @Operation(summary = "Buscar registros de acesso de um usuário em um período")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registros encontrados")
    })
    public ResponseEntity<List<RegistroAcesso>> buscarRegistrosPorUsuarioEPeriodo(
            @PathVariable Long id,
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim) {
        return ResponseEntity.ok(APIFacade.getInstance().buscarRegistrosPorUsuarioEPeriodo(id, inicio, fim));
    }

    @PostMapping("/observer/{tipo}")
    @Operation(summary = "Adicionar um observador de acesso específico (para testes)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Observador adicionado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Tipo de observador inválido")
    })
    public ResponseEntity<String> adicionarObserver(@PathVariable String tipo) {
        boolean sucesso = APIFacade.getInstance().adicionarObserver(tipo);
        if (sucesso) {
            return ResponseEntity.ok("Observador '" + tipo + "' adicionado com sucesso");
        } else {
            return ResponseEntity.badRequest().body("Tipo de observador inválido: " + tipo);
        }
    }

    @DeleteMapping("/observer/{tipo}")
    @Operation(summary = "Remover um observador de acesso específico (para testes)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Observador removido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Tipo de observador inválido")
    })
    public ResponseEntity<String> removerObserver(@PathVariable String tipo) {
        boolean sucesso = APIFacade.getInstance().removerObserver(tipo);
        if (sucesso) {
            return ResponseEntity.ok("Observador '" + tipo + "' removido com sucesso");
        } else {
            return ResponseEntity.badRequest().body("Tipo de observador inválido: " + tipo);
        }
    }
}