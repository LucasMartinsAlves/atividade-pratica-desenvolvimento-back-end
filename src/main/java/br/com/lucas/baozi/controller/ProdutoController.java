package br.com.lucas.baozi.controller;

import java.net.URI;
import java.util.List;
import br.com.lucas.baozi.model.Produto;
import br.com.lucas.baozi.repository.ProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    private final ProdutoRepository repository;

    public ProdutoController(ProdutoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Produto> criar(@Valid @RequestBody Produto dados) {
        var salvo = repository.save(dados);
        return ResponseEntity.created(URI.create("/produtos/" + salvo.id)).body(salvo);
    }

    @GetMapping
    public List<Produto> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Produto consultar(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto nao encontrado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        repository.delete(consultar(id));
        return ResponseEntity.noContent().build();
    }
}
