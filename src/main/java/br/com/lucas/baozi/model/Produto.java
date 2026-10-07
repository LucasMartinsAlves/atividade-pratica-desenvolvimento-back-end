package br.com.lucas.baozi.model;

import java.math.BigDecimal;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Produto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Long id;

    @NotBlank @Size(max = 150) @Column(nullable = false, length = 150)
    public String nome;

    @NotNull @DecimalMin("0.01") @Digits(integer = 10, fraction = 2)
    @Column(nullable = false, precision = 12, scale = 2)
    public BigDecimal preco;

    // O roteiro usa um booleano para indicar a disponibilidade do produto.
    @NotNull @Column(nullable = false)
    public Boolean estoque;
}
