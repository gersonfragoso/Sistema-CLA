package com.example.sistema_cla.presentation.controller;

import com.example.sistema_cla.presentation.dto.request.RelatorioEstatisticaRequest;
import com.example.sistema_cla.presentation.dto.response.RelatorioResponse;
import com.example.sistema_cla.presentation.facade.APIFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller que utiliza o padrão Facade Singleton para relatórios do sistema.
 */
@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    @PostMapping("/estatisticas")
    @Operation(summary = "Gerar relatório de estatísticas de acesso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<RelatorioResponse> gerarRelatorioEstatisticas(
            @Valid @RequestBody RelatorioEstatisticaRequest request) {
        return ResponseEntity.ok(APIFacade.getInstance().gerarRelatorioEstatisticas(request));
    }

    @GetMapping("/estatisticas/ultimo-mes")
    @Operation(summary = "Gerar relatório de estatísticas do último mês")
    public ResponseEntity<RelatorioResponse> gerarRelatorioUltimoMes(
            @RequestParam Long usuarioId,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false, defaultValue = "HTML") String formato) {

        return ResponseEntity.ok(APIFacade.getInstance()
                .gerarRelatorioUltimoMes(titulo, formato, usuarioId));
    }

    @GetMapping
    @Operation(summary = "Listar todos os relatórios")
    public ResponseEntity<List<RelatorioResponse>> listarRelatorios() {
        return ResponseEntity.ok(APIFacade.getInstance().listarRelatorios());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar relatório por ID")
    public ResponseEntity<RelatorioResponse> buscarRelatorioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(APIFacade.getInstance().buscarRelatorioPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Buscar relatórios por usuário")
    public ResponseEntity<List<RelatorioResponse>> buscarRelatoriosPorUsuario(
            @PathVariable Long usuarioId) {
        return ResponseEntity.ok(APIFacade.getInstance().buscarRelatoriosPorUsuario(usuarioId));
    }

    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar relatórios por categoria")
    public ResponseEntity<List<RelatorioResponse>> buscarRelatoriosPorCategoria(
            @PathVariable String categoria) {
        return ResponseEntity.ok(APIFacade.getInstance().buscarRelatoriosPorCategoria(categoria));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir relatório")
    public ResponseEntity<Void> excluirRelatorio(@PathVariable Long id) {
        APIFacade.getInstance().excluirRelatorio(id);
        return ResponseEntity.noContent().build();
    }
}