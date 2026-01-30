package com.padaria.cachoeiro.repository;

import com.padaria.cachoeiro.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByNomeContainingIgnoreCaseOrCpfContaining(String nome, String cpf);
}