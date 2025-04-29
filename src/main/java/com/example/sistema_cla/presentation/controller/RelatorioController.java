package controller;

import service.RelatorioService;
import strategy.EstatisticaPorUsuario;
import domain.acesso.Acesso;

import java.util.List;
import java.util.ArrayList;

public class RelatorioController {
    public static void main(String[] args) {
        RelatorioService service = new RelatorioService();
        service.setEstrategia(new EstatisticaPorUsuario());

        List<Acesso> acessos = new ArrayList<>();
        // Simulação de acessos
        acessos.add(new Acesso());
        acessos.add(new Acesso());

        service.gerarRelatorio("html", acessos);
    }
}