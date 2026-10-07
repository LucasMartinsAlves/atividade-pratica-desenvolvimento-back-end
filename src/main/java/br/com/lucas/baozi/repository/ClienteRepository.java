package br.com.lucas.baozi.repository;

import br.com.lucas.baozi.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
