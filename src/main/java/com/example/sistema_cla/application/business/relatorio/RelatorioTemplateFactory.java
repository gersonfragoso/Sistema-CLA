package com.example.sistema_cla.application.business.relatorio;

/**
 * Factory para criação de instâncias de templates de relatório
 * implementando o padrão Factory Method
 */
public class RelatorioTemplateFactory {

    /**
     * Cria uma instância do template adequado com base no formato solicitado
     * @param formato o formato do relatório ("HTML", "PDF")
     * @return a instância de RelatorioEstatisticaTemplate apropriada
     * @throws IllegalArgumentException se o formato não for suportado
     */
    public static RelatorioEstatisticaTemplate criarTemplate(String formato) {
        switch(formato.toUpperCase()) {
            case "HTML":
                return new RelatorioEstatisticaHTML();
            case "PDF":
                return new RelatorioEstatisticaPDF();
            default:
                throw new IllegalArgumentException("Formato de relatório não suportado: " + formato);
        }
    }
}