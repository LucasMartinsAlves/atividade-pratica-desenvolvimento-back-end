package br.com.lucas.baozi.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PedidoDados(
        @NotNull @Positive Long clienteId,
        @NotNull @Positive Long produtoId,
        @NotNull @Positive Integer quantidade) {
}
