package br.com.lucas.baozi.controller;

import java.net.URI;
import java.util.List;
import br.com.lucas.baozi.model.Pedido;
import br.com.lucas.baozi.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoRepository pedidos;
    private final ClienteRepository clientes;
    private final ProdutoRepository produtos;

    public PedidoController(PedidoRepository pedidos, ClienteRepository clientes, ProdutoRepository produtos) {
        this.pedidos = pedidos;
        this.clientes = clientes;
        this.produtos = produtos;
    }

    @PostMapping
    public ResponseEntity<Pedido> criar(@Valid @RequestBody PedidoDados dados) {
        var cliente = clientes.findById(dados.clienteId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente nao encontrado"));
        var produto = produtos.findById(dados.produtoId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto nao encontrado"));
        if (!Boolean.TRUE.equals(produto.estoque)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Produto indisponivel");
        }
        var pedido = new Pedido();
        pedido.cliente = cliente;
        pedido.produto = produto;
        pedido.quantidade = dados.quantidade();
        var salvo = pedidos.save(pedido);
        return ResponseEntity.created(URI.create("/pedidos/" + salvo.id)).body(salvo);
    }

    @GetMapping
    public List<Pedido> listar() {
        return pedidos.findAll();
    }

    @GetMapping("/{id}")
    public Pedido consultar(@PathVariable Long id) {
        return pedidos.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido nao encontrado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pedidos.delete(consultar(id));
        return ResponseEntity.noContent().build();
    }
}
