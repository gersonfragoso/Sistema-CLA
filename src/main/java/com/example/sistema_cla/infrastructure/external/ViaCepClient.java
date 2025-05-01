package com.example.sistema_cla.infrastructure.external;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

/**
 * Cliente para a API ViaCEP
 * https://viacep.com.br/
 */
@Component
public class ViaCepClient {
    private static final String BASE_URL = "https://viacep.com.br/ws/";
    private final HttpClient httpClient;

    public ViaCepClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Consulta um CEP na API ViaCEP
     * @param cep o CEP a ser consultado (apenas números ou com hífen)
     * @return JSONObject com os dados do endereço ou null se não encontrado
     */
    public JSONObject consultarCep(String cep) {
        if (cep == null || cep.isEmpty()) {
            return null;
        }

        // Remove caracteres não numéricos
        String cepNumerico = cep.replaceAll("\\D", "");

        if (cepNumerico.length() != 8) {
            return null;
        }

        try {
            String url = BASE_URL + cepNumerico + "/json";
            System.out.println("Consultando CEP: " + url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.err.println("Erro na consulta de CEP. Status: " + response.statusCode());
                return null;
            }

            JSONObject jsonResponse = new JSONObject(response.body());
            System.out.println("Resposta: " + jsonResponse.toString());

            // Verifica se o CEP existe
            if (jsonResponse.has("erro") && jsonResponse.getBoolean("erro")) {
                System.err.println("CEP não encontrado: " + cep);
                return null;
            }

            return jsonResponse;

        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao consultar ViaCEP: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao consultar ViaCEP: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Consulta endereços por logradouro, cidade e estado
     * @param uf UF do estado (2 letras)
     * @param cidade Nome da cidade
     * @param logradouro Nome do logradouro
     * @return Array de JSONObject com os endereços encontrados ou null se erro
     */
    public JSONObject[] consultarEndereco(String uf, String cidade, String logradouro) {
        if (uf == null || cidade == null || logradouro == null) {
            return null;
        }

        try {
            String url = BASE_URL +
                    URLEncoder.encode(uf, StandardCharsets.UTF_8) + "/" +
                    URLEncoder.encode(cidade, StandardCharsets.UTF_8) + "/" +
                    URLEncoder.encode(logradouro, StandardCharsets.UTF_8) + "/json";

            System.out.println("Consultando endereço: " + url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.err.println("Erro na consulta de endereço. Status: " + response.statusCode());
                return null;
            }

            String responseBody = response.body();
            System.out.println("Resposta: " + responseBody);

            // Verifica se a resposta é vazia ou inválida
            if (responseBody == null || responseBody.trim().isEmpty() || responseBody.equals("[]")) {
                System.err.println("Nenhum endereço encontrado");
                return null;
            }

            // Converte o JSON array para array de JSONObject
            org.json.JSONArray jsonArray = new org.json.JSONArray(responseBody);

            if (jsonArray.length() == 0) {
                return null;
            }

            JSONObject[] result = new JSONObject[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                result[i] = jsonArray.getJSONObject(i);
            }

            return result;

        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao consultar ViaCEP: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao consultar ViaCEP: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}