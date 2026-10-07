package br.com.lucas.baozi.model;

import jakarta.persistence.*;

@Entity
public class Pedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    // As chaves estrangeiras garantem que o pedido tenha cliente e produto existentes.
    @ManyToOne(optional = false) @JoinColumn(name = "cliente_id", nullable = false)
    public Cliente cliente;

    @ManyToOne(optional = false) @JoinColumn(name = "produto_id", nullable = false)
    public Produto produto;

    @Column(nullable = false)
    public Integer quantidade;
}
