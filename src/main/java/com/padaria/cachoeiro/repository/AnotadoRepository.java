package com.padaria.cachoeiro.repository;

import com.padaria.cachoeiro.model.Anotado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AnotadoRepository extends JpaRepository<Anotado, Long> {
    Optional<Anotado> findByNomeCliente(String nomeCliente);
    
    List<Anotado> findByNomeClienteContainingIgnoreCase(String nome);
}