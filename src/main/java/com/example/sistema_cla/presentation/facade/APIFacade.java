package com.example.sistema_cla.presentation.facade;

import com.example.sistema_cla.application.service.*;
import com.example.sistema_cla.domain.model.EstatisticaAcesso;
import com.example.sistema_cla.domain.model.RegistroAcesso;
import com.example.sistema_cla.domain.observer.AcessoObserver;
import com.example.sistema_cla.domain.observer.impl.EstatisticaAcessoObserver;
import com.example.sistema_cla.domain.observer.impl.LogAcessoObserver;
import com.example.sistema_cla.domain.observer.impl.SegurancaAcessoObserver;
import com.example.sistema_cla.infrastructure.dao.interfaces.EstatisticaAcessoDAO;
import com.example.sistema_cla.presentation.dto.request.*;
import com.example.sistema_cla.presentation.dto.response.AvaliacaoResponse;
import com.example.sistema_cla.presentation.dto.response.LocalComAvaliacoesResponse;
import com.example.sistema_cla.presentation.dto.response.LocalResponse;
import com.example.sistema_cla.presentation.dto.response.RelatorioResponse;
import com.example.sistema_cla.presentation.dto.response.UsuarioResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementação do padrão Facade combinado com Singleton.
 * Esta classe fornece uma interface unificada para os controllers do sistema,
 * garantindo que exista apenas uma instância em toda a aplicação.
 */
@Component
public class APIFacade {

    // Instância única (Singleton)
    private static APIFacade instance;

    // Serviços
    private UsuarioService usuarioService;
    private LocalService localService;
    private AvaliacaoService avaliacaoService;
    private RelatorioService relatorioService;
    private RegistroAcessoService registroAcessoService;
    private EstatisticaAcessoDAO estatisticaAcessoDAO;

    // Para acesso aos observers
    private ApplicationContext applicationContext;

    // Construtor privado para evitar instanciação externa
    private APIFacade() {
        // Construtor privado vazio
    }

    /**
     * Método de inicialização para injeção de dependências pelo Spring
     */
    @Autowired
    private void init(UsuarioService usuarioService,
                      LocalService localService,
                      AvaliacaoService avaliacaoService,
                      RelatorioService relatorioService,
                      RegistroAcessoService registroAcessoService,
                      EstatisticaAcessoDAO estatisticaAcessoDAO,
                      ApplicationContext applicationContext) {
        this.usuarioService = usuarioService;
        this.localService = localService;
        this.avaliacaoService = avaliacaoService;
        this.relatorioService = relatorioService;
        this.registroAcessoService = registroAcessoService;
        this.estatisticaAcessoDAO = estatisticaAcessoDAO;
        this.applicationContext = applicationContext;
        instance = this;
    }

    /**
     * Método para obter a instância única (Singleton)
     */
    public static synchronized APIFacade getInstance() {
        if (instance == null) {
            // Criação lazy (só quando necessário)
            instance = new APIFacade();
        }
        return instance;
    }

    // Métodos de Usuário
    public List<UsuarioResponse> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    public UsuarioResponse criarUsuario(UsuarioRequest request) {
        return usuarioService.criarUsuario(request);
    }

    // Métodos de Local
    public List<LocalResponse> listarLocais() {
        return localService.listarLocais();
    }

    public LocalResponse criarLocal(LocalRequest request) {
        return localService.criarLocal(request);
    }

    // Métodos de Avaliação
    public AvaliacaoResponse criarAvaliacao(AvaliacaoRequest request) {
        return avaliacaoService.criarAvaliacao(request);
    }

    public LocalComAvaliacoesResponse buscarLocalDetalhado(Long id){
        return localService.buscarLocalDetalhado(id);
    }

    // Métodos de Relatório
    public RelatorioResponse gerarRelatorioEstatisticas(RelatorioEstatisticaRequest request) {
        return relatorioService.gerarRelatorioEstatisticas(request);
    }

    public RelatorioResponse gerarRelatorioUltimoMes(String titulo, String formato, Long usuarioId) {
        return relatorioService.gerarRelatorioUltimoMes(titulo, formato, usuarioId);
    }

    public List<RelatorioResponse> listarRelatorios() {
        return relatorioService.listarRelatorios();
    }

    public RelatorioResponse buscarRelatorioPorId(Long id) {
        return relatorioService.buscarRelatorioPorId(id);
    }

    public List<RelatorioResponse> buscarRelatoriosPorUsuario(Long usuarioId) {
        return relatorioService.buscarRelatoriosPorUsuario(usuarioId);
    }

    public List<RelatorioResponse> buscarRelatoriosPorCategoria(String categoria) {
        return relatorioService.buscarRelatoriosPorCategoria(categoria);
    }

    public void excluirRelatorio(Long id) {
        relatorioService.excluirRelatorio(id);
    }

    public AvaliacaoResponse editarAvaliacao(Long id, AvaliacaoRequest request) {
        return avaliacaoService.editarAvaliacao(id, request);
    }

    public AvaliacaoResponse desfazerAvaliacaoEdicao(Long id) {
        return avaliacaoService.desfazerEdicao(id);
    }

    // Métodos de RegistroAcesso
    public RegistroAcesso registrarAcesso(RegistroAcessoRequest request) {
        return registroAcessoService.registrarAcesso(
                request.usuarioId(),
                request.ipAcesso(),
                request.dispositivo(),
                request.tempoSessaoMinutos(),
                request.paginasVisitadas()
        );
    }

    public List<RegistroAcesso> buscarRegistrosPorUsuario(Long usuarioId) {
        return registroAcessoService.buscarRegistrosPorUsuario(usuarioId);
    }

    public List<RegistroAcesso> buscarRegistrosPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return registroAcessoService.buscarRegistrosPorPeriodo(inicio, fim);
    }

    public List<RegistroAcesso> buscarRegistrosPorUsuarioEPeriodo(
            Long usuarioId, LocalDateTime inicio, LocalDateTime fim) {
        return registroAcessoService.buscarRegistrosPorUsuarioEPeriodo(usuarioId, inicio, fim);
    }

    // Métodos para gerenciar observers (para testes)
    public boolean adicionarObserver(String tipo) {
        try {
            AcessoObserver observer = null;

            switch (tipo.toLowerCase()) {
                case "estatistica":
                    observer = applicationContext.getBean(EstatisticaAcessoObserver.class);
                    break;
                case "log":
                    observer = applicationContext.getBean(LogAcessoObserver.class);
                    break;
                case "seguranca":
                    observer = applicationContext.getBean(SegurancaAcessoObserver.class);
                    break;
                default:
                    return false;
            }

            if (observer != null) {
                registroAcessoService.registrarObserver(observer);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean removerObserver(String tipo) {
        try {
            AcessoObserver observer = null;

            switch (tipo.toLowerCase()) {
                case "estatistica":
                    observer = applicationContext.getBean(EstatisticaAcessoObserver.class);
                    break;
                case "log":
                    observer = applicationContext.getBean(LogAcessoObserver.class);
                    break;
                case "seguranca":
                    observer = applicationContext.getBean(SegurancaAcessoObserver.class);
                    break;
                default:
                    return false;
            }

            if (observer != null) {
                registroAcessoService.removerObserver(observer);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // Métodos de Estatísticas
    public Optional<EstatisticaAcesso> buscarEstatisticaPorUsuario(Long usuarioId) {
        return estatisticaAcessoDAO.findByUsuarioId(usuarioId);
    }

    public List<EstatisticaAcesso> buscarTopUsuariosAtivos(int limit) {
        return estatisticaAcessoDAO.findTopUsuariosAtivos(limit);
    }

    public AvaliacaoResponse buscarAvaliacaoPorId(Long id) {
        return avaliacaoService.buscarPorId(id);
    }
}