package com.example.sistema_cla.presentation.facade;

import com.example.sistema_cla.application.service.AvaliacaoService;
import com.example.sistema_cla.application.service.LocalService;
import com.example.sistema_cla.application.service.RelatorioService;
import com.example.sistema_cla.application.service.UsuarioService;
import com.example.sistema_cla.presentation.dto.request.AvaliacaoRequest;
import com.example.sistema_cla.presentation.dto.request.LocalRequest;
import com.example.sistema_cla.presentation.dto.request.RelatorioEstatisticaRequest;
import com.example.sistema_cla.presentation.dto.request.UsuarioRequest;
import com.example.sistema_cla.presentation.dto.response.AvaliacaoResponse;
import com.example.sistema_cla.presentation.dto.response.LocalComAvaliacoesResponse;
import com.example.sistema_cla.presentation.dto.response.LocalResponse;
import com.example.sistema_cla.presentation.dto.response.RelatorioResponse;
import com.example.sistema_cla.presentation.dto.response.UsuarioResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
                      RelatorioService relatorioService) {
        this.usuarioService = usuarioService;
        this.localService = localService;
        this.avaliacaoService = avaliacaoService;
        this.relatorioService = relatorioService;
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
}