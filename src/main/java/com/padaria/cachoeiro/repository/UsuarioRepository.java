package com.padaria.cachoeiro.repository;

import com.padaria.cachoeiro.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional; // Não esqueça desse import!

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    List<Usuario> findByNomeContainingIgnoreCaseOrLoginContainingIgnoreCase(String nome, String login);
    
    Optional<Usuario> findByLogin(String login);
}