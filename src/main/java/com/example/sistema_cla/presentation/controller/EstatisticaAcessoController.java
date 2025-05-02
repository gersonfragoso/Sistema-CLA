package com.example.sistema_cla.presentation.controller;

import com.example.sistema_cla.domain.model.EstatisticaAcesso;
import com.example.sistema_cla.presentation.facade.APIFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller que utiliza o padrão Facade Singleton para acessar estatísticas de acesso.
 */
@RestController
@RequestMapping("/api/estatisticas")
public class EstatisticaAcessoController {

    @GetMapping("/usuario/{id}")
    @Operation(summary = "Buscar estatísticas de acesso de um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estatísticas encontradas"),
            @ApiResponse(responseCode = "404", description = "Estatísticas não encontradas")
    })
    public ResponseEntity<EstatisticaAcesso> buscarEstatisticaPorUsuario(@PathVariable Long id) {
        return APIFacade.getInstance().buscarEstatisticaPorUsuario(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/top/{limit}")
    @Operation(summary = "Buscar os usuários mais ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estatísticas encontradas")
    })
    public ResponseEntity<List<EstatisticaAcesso>> buscarTopUsuariosAtivos(@PathVariable int limit) {
        return ResponseEntity.ok(APIFacade.getInstance().buscarTopUsuariosAtivos(limit));
    }
}
