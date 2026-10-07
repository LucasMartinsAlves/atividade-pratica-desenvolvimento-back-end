package br.com.lucas.baozi.repository;

import br.com.lucas.baozi.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
