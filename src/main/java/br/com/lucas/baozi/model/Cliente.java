package br.com.lucas.baozi.model;

import java.time.LocalDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Long id;

    @NotBlank @Size(max = 150) @Column(nullable = false, length = 150)
    public String nome;

    @NotNull @PastOrPresent @Column(nullable = false)
    public LocalDate clienteDesde;
}
