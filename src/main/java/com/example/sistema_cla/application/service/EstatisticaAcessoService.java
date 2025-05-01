package com.example.sistema_cla.application.service;

import com.example.sistema_cla.application.business.relatorio.RelatorioEstatisticaHTML;
import com.example.sistema_cla.application.business.relatorio.RelatorioEstatisticaPDF;
import com.example.sistema_cla.application.business.relatorio.RelatorioEstatisticaTemplate;
import com.example.sistema_cla.domain.model.EstatisticaAcesso;
import com.example.sistema_cla.domain.model.Relatorio;
import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.infrastructure.dao.interfaces.RegistroAcessoDAO;
import com.example.sistema_cla.infrastructure.dao.interfaces.RelatorioDAO;
import com.example.sistema_cla.presentation.dto.request.RelatorioEstatisticaRequest;
import com.example.sistema_cla.presentation.dto.response.RelatorioResponse;
import com.example.sistema_cla.infrastructure.utils.RelatorioMapper;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EstatisticaAcessoService {

    private final RegistroAcessoDAO registroAcessoDAO;
    private final RelatorioDAO relatorioDAO;
    private final BuscarEntidadeService buscarEntidadeService;

    public EstatisticaAcessoService(
            RegistroAcessoDAO registroAcessoDAO,
            RelatorioDAO relatorioDAO,
            BuscarEntidadeService buscarEntidadeService) {
        this.registroAcessoDAO = registroAcessoDAO;
        this.relatorioDAO = relatorioDAO;
        this.buscarEntidadeService = buscarEntidadeService;
    }

    /**
     * Gerar relatório de estatísticas de acesso
     */
    public RelatorioResponse gerarRelatorioEstatisticas(RelatorioEstatisticaRequest request) {
        // Buscar usuário solicitante
        Usuario solicitante = buscarEntidadeService.buscarUsuario(request.usuarioId());

        // Obter estatísticas para o período especificado
        Map<String, Object> estatisticas = coletarEstatisticasAcesso(
                request.dataInicio(),
                request.dataFim());

        // Selecionar template apropriado
        RelatorioEstatisticaTemplate template = selecionarTemplate(request.formato());

        // Gerar o relatório usando o template
        Relatorio relatorio = template.gerarRelatorio(
                request.titulo(),
                estatisticas,
                solicitante);

        // Persistir o relatório
        Relatorio salvo = relatorioDAO.save(relatorio);

        // Converter para DTO de resposta
        return RelatorioMapper.toResponse(salvo);
    }

    /**
     * Coletar estatísticas de acesso para o período especificado
     */
    private Map<String, Object> coletarEstatisticasAcesso(LocalDate dataInicio, LocalDate dataFim) {
        // Em uma implementação real, você buscaria esses dados do banco
        // Aqui estamos simulando dados para demonstração

        Map<String, Object> estatisticas = new HashMap<>();

        // Dados do período
        estatisticas.put("dataInicio", dataInicio);
        estatisticas.put("dataFim", dataFim);

        // Dados gerais
        estatisticas.put("totalAcessos", 1250);
        estatisticas.put("usuariosAtivos", 45);
        estatisticas.put("mediaAcessosPorUsuario", 27.8);
        estatisticas.put("tempoMedioSessaoMinutos", 32L);

        // Dados por usuário (normalmente viriam do banco de dados)
        List<EstatisticaAcesso> estatisticasPorUsuario = gerarDadosDeExemplo();
        estatisticas.put("estatisticasPorUsuario", estatisticasPorUsuario);

        return estatisticas;
    }

    /**
     * Selecionar o template apropriado com base no formato solicitado
     */
    private RelatorioEstatisticaTemplate selecionarTemplate(String formato) {
        if ("PDF".equalsIgnoreCase(formato)) {
            return new RelatorioEstatisticaPDF();
        } else {
            return new RelatorioEstatisticaHTML();
        }
    }

    /**
     * Gerar dados de exemplo para demonstração
     * Em uma implementação real, estes dados viriam do DAO
     */
    private List<EstatisticaAcesso> gerarDadosDeExemplo() {
        return List.of(
                new EstatisticaAcesso(1L, "João Silva", 45,
                        LocalDateTime.now().minusHours(2), 120L, 78),
                new EstatisticaAcesso(2L, "Maria Oliveira", 32,
                        LocalDateTime.now().minusDays(1), 95L, 53),
                new EstatisticaAcesso(3L, "Carlos Santos", 67,
                        LocalDateTime.now().minusHours(5), 210L, 112),
                new EstatisticaAcesso(4L, "Ana Costa", 28,
                        LocalDateTime.now().minusDays(2), 72L, 41),
                new EstatisticaAcesso(5L, "Paulo Mendes", 19,
                        LocalDateTime.now().minusHours(12), 47L, 23)
        );
    }
}