package com.example.sistema_cla.application.service;

import com.example.sistema_cla.domain.model.EstatisticaAcesso;
import com.example.sistema_cla.domain.model.Relatorio;
import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.domain.strategy.RelatorioGeracaoStrategy;
import com.example.sistema_cla.infrastructure.dao.interfaces.RegistroAcessoDAO;
import com.example.sistema_cla.infrastructure.dao.interfaces.RelatorioDAO;
import com.example.sistema_cla.infrastructure.dao.interfaces.UsuarioDAO;
import com.example.sistema_cla.infrastructure.exceptions.EntityNotFoundException;
import com.example.sistema_cla.infrastructure.exceptions.ValidationException;
import com.example.sistema_cla.infrastructure.utils.RelatorioMapper;
import com.example.sistema_cla.presentation.dto.request.RelatorioEstatisticaRequest;
import com.example.sistema_cla.presentation.dto.response.RelatorioResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatorioService {
    private final RelatorioDAO relatorioDAO;
    private final UsuarioDAO usuarioDAO;
    private final RegistroAcessoDAO registroAcessoDAO;
    private final Map<String, RelatorioGeracaoStrategy> estrategias;

    public RelatorioService(
            RelatorioDAO relatorioDAO,
            UsuarioDAO usuarioDAO,
            RegistroAcessoDAO registroAcessoDAO,
            List<RelatorioGeracaoStrategy> estrategiasList) {
        this.relatorioDAO = relatorioDAO;
        this.usuarioDAO = usuarioDAO;
        this.registroAcessoDAO = registroAcessoDAO;

        // Mapear estratégias por tipo
        this.estrategias = estrategiasList.stream()
                .collect(Collectors.toMap(
                        RelatorioGeracaoStrategy::getTipo,
                        strategy -> strategy));
    }

    /**
     * Gera um relatório de estatísticas de acesso
     */
    public RelatorioResponse gerarRelatorioEstatisticas(RelatorioEstatisticaRequest request) {
        validarCamposEstatistica(request);

        Usuario usuario = usuarioDAO.findById(request.usuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário", request.usuarioId()));

        // Converter para entity
        Relatorio relatorio = RelatorioMapper.toEntityFromEstatisticaRequest(request, usuario);

        // Buscar dados para o relatório
        List<EstatisticaAcesso> estatisticas = registroAcessoDAO.getEstatisticasAcessoPorPeriodo(
                request.dataInicio(), request.dataFim());

        Map<String, Object> resumoGeral = registroAcessoDAO.getResumoGeralAcessos(
                request.dataInicio(), request.dataFim());

        // Selecionar estratégia com base no formato
        RelatorioGeracaoStrategy estrategia = estrategias.get(request.formato().toUpperCase());
        if (estrategia == null) {
            estrategia = estrategias.get("HTML"); // Default
        }

        // Gerar conteúdo usando a estratégia
        String conteudo = estrategia.gerarConteudo(
                request.titulo(),
                request.dataInicio(),
                request.dataFim(),
                estatisticas,
                resumoGeral);

        relatorio.setConteudo(conteudo);
        relatorio.setCategoria("ESTATISTICA_ACESSO");
        relatorio.setPeriodoInicio(request.dataInicio());
        relatorio.setPeriodoFim(request.dataFim());

        // Salvar relatório
        Relatorio salvo = relatorioDAO.save(relatorio);

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
            erros.add("Formato de relatório inválido. Formatos suportados: HTML, TEXT");
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
        // Verificar se o formato está entre os disponíveis nas estratégias
        return estrategias.containsKey(formato.toUpperCase());
    }
}