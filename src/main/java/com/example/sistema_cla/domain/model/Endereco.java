package com.example.sistema_cla.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {
    private Long id;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String pais;
    private boolean validado;

    // Método para obter o endereço formatado

    public String getEnderecoFormatado() {
        StringBuilder sb = new StringBuilder();

        sb.append(logradouro != null ? logradouro : "");

        if (numero != null && !numero.isEmpty()) {
            sb.append(", ").append(numero);
        }

        if (complemento != null && !complemento.isEmpty()) {
            sb.append(" - ").append(complemento);
        }

        if (bairro != null && !bairro.isEmpty()) {
            sb.append(", ").append(bairro);
        }

        if (cidade != null && !cidade.isEmpty()) {
            sb.append(", ").append(cidade);
        }

        if (estado != null && !estado.isEmpty()) {
            sb.append(" - ").append(estado);
        }

        if (cep != null && !cep.isEmpty()) {
            sb.append(", CEP: ").append(cep);
        }

        return sb.toString();
    }
}