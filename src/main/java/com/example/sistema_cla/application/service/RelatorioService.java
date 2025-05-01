package com.example.sistema_cla.application.service;

import com.example.sistema_cla.application.business.relatorio.RelatorioEstatisticaTemplate;
import com.example.sistema_cla.application.business.relatorio.RelatorioTemplateFactory;
import com.example.sistema_cla.domain.model.Relatorio;
import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.infrastructure.dao.interfaces.RelatorioDAO;
import com.example.sistema_cla.infrastructure.exceptions.ValidationException;
import com.example.sistema_cla.infrastructure.utils.RelatorioMapper;
import com.example.sistema_cla.presentation.dto.request.RelatorioEstatisticaRequest;
import com.example.sistema_cla.presentation.dto.response.RelatorioResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    private final RelatorioDAO relatorioDAO;
    private final BuscarEntidadeService buscarEntidadeService;

    @Autowired
    public RelatorioService(RelatorioDAO relatorioDAO,
                            BuscarEntidadeService buscarEntidadeService) {
        this.relatorioDAO = relatorioDAO;
        this.buscarEntidadeService = buscarEntidadeService;
    }

    /**
     * Gera um relatório de estatísticas de acesso
     */
    public RelatorioResponse gerarRelatorioEstatisticas(RelatorioEstatisticaRequest request) {
        validarCamposEstatistica(request);

        // Buscar usuário solicitante
        Usuario solicitante = buscarEntidadeService.buscarUsuario(request.usuarioId());

        // Obter dados para o relatório
        Map<String, Object> estatisticas = coletarEstatisticasAcesso(
                request.dataInicio(),
                request.dataFim());

        // Criar template apropriado usando a Factory
        RelatorioEstatisticaTemplate template = RelatorioTemplateFactory.criarTemplate(request.formato());

        // Gerar o relatório usando o template
        Relatorio relatorio = template.gerarRelatorio(
                request.titulo(),
                estatisticas,
                solicitante);

        // Adicionar metadados específicos ao relatório
        relatorio.setCategoria("ESTATISTICA_ACESSO");
        relatorio.setPeriodoInicio(request.dataInicio());
        relatorio.setPeriodoFim(request.dataFim());

        // Persistir o relatório
        Relatorio salvo = relatorioDAO.save(relatorio);

        // Converter para DTO de resposta
        return RelatorioMapper.toResponse(salvo);
    }

    /**
     * Listar todos os relatórios
     */
    public List<RelatorioResponse> listarRelatorios() {
        return relatorioDAO.findAll()
                .stream()
                .map(RelatorioMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Buscar relatório por ID
     */
    public RelatorioResponse buscarRelatorioPorId(Long id) {
        Relatorio relatorio = relatorioDAO.findById(id)
                .orElseThrow(() -> new ValidationException("Relatório não encontrado com ID: " + id));
        return RelatorioMapper.toResponse(relatorio);
    }

    /**
     * Buscar relatórios por usuário
     */
    public List<RelatorioResponse> buscarRelatoriosPorUsuario(Long usuarioId) {
        return relatorioDAO.findByUsuarioId(usuarioId)
                .stream()
                .map(RelatorioMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Buscar relatórios por categoria
     */
    public List<RelatorioResponse> buscarRelatoriosPorCategoria(String categoria) {
        return relatorioDAO.findByCategoria(categoria)
                .stream()
                .map(RelatorioMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Excluir um relatório
     */
    public void excluirRelatorio(Long id) {
        relatorioDAO.deleteById(id);
    }

    /**
     * Gerar relatório com período do último mês
     */
    public RelatorioResponse gerarRelatorioUltimoMes(String titulo, String formato, Long usuarioId) {
        LocalDate hoje = LocalDate.now();
        LocalDate mesPassado = hoje.minusMonths(1);

        RelatorioEstatisticaRequest request = new RelatorioEstatisticaRequest(
                titulo != null ? titulo : "Relatório de Acessos - Último Mês",
                formato != null ? formato : "HTML",
                mesPassado,
                hoje,
                usuarioId
        );

        return gerarRelatorioEstatisticas(request);
    }

    /**
     * Coletar estatísticas de acesso para o período especificado
     * (método mockado para demonstração)
     */
    private Map<String, Object> coletarEstatisticasAcesso(LocalDate dataInicio, LocalDate dataFim) {
        Map<String, Object> estatisticas = new HashMap<>();

        // Dados do período
        estatisticas.put("dataInicio", dataInicio);
        estatisticas.put("dataFim", dataFim);

        // Dados gerais (mock)
        estatisticas.put("totalAcessos", 1250);
        estatisticas.put("usuariosAtivos", 45);
        estatisticas.put("mediaAcessosPorUsuario", 27.8);
        estatisticas.put("tempoMedioSessaoMinutos", 32L);

        // Dados detalhados por usuário (mock)
        List<Map<String, Object>> estatisticasPorUsuario = new ArrayList<>();

        Map<String, Object> usuario1 = new HashMap<>();
        usuario1.put("usuarioId", 1L);
        usuario1.put("nomeUsuario", "João Silva");
        usuario1.put("quantidadeAcessos", 45);
        usuario1.put("ultimoAcesso", LocalDate.now().atTime(10, 30));
        usuario1.put("tempoTotalMinutos", 120L);
        usuario1.put("paginasVisitadas", 78);

        Map<String, Object> usuario2 = new HashMap<>();
        usuario2.put("usuarioId", 2L);
        usuario2.put("nomeUsuario", "Maria Oliveira");
        usuario2.put("quantidadeAcessos", 32);
        usuario2.put("ultimoAcesso", LocalDate.now().minusDays(1).atTime(15, 45));
        usuario2.put("tempoTotalMinutos", 95L);
        usuario2.put("paginasVisitadas", 53);

        estatisticasPorUsuario.add(usuario1);
        estatisticasPorUsuario.add(usuario2);

        estatisticas.put("estatisticasPorUsuario", estatisticasPorUsuario);

        return estatisticas;
    }

    /**
     * Validar campos do request
     */
    private void validarCamposEstatistica(RelatorioEstatisticaRequest request) {
        List<String> erros = new ArrayList<>();

        if (request.titulo() == null || request.titulo().trim().isEmpty()) {
            erros.add("O título do relatório é obrigatório");
        }

        if (request.formato() == null || request.formato().trim().isEmpty()) {
            erros.add("O formato do relatório é obrigatório");
        } else if (!isFormatoValido(request.formato())) {
            erros.add("Formato de relatório inválido. Formatos suportados: HTML, PDF");
        }

        if (request.dataInicio() == null) {
            erros.add("A data de início é obrigatória");
        }

        if (request.dataFim() == null) {
            erros.add("A data de fim é obrigatória");
        }

        if (request.dataInicio() != null && request.dataFim() != null &&
                request.dataInicio().isAfter(request.dataFim())) {
            erros.add("A data de início não pode ser posterior à data de fim");
        }

        if (request.usuarioId() == null) {
            erros.add("O ID do usuário é obrigatório");
        }

        if (!erros.isEmpty()) {
            throw new ValidationException(String.join("; ", erros));
        }
    }

    private boolean isFormatoValido(String formato) {
        return "HTML".equalsIgnoreCase(formato) || "PDF".equalsIgnoreCase(formato);
    }
}