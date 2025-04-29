package service;

import factory.RelatorioFactory;
import relatorio.RelatorioAcessoTemplate;
import strategy.EstrategiaEstatistica;
import domain.acesso.Acesso;

import java.util.List;

public class RelatorioService {
    private EstrategiaEstatistica estrategia;

    public void setEstrategia(EstrategiaEstatistica estrategia) {
        this.estrategia = estrategia;
    }

    public void gerarRelatorio(String tipo, List<Acesso> acessos) {
        RelatorioAcessoTemplate relatorio = RelatorioFactory.criar(tipo);
        estrategia.calcular(acessos);
        relatorio.gerar();
    }
}