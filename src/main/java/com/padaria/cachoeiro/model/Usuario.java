package com.padaria.cachoeiro.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;
    private String nome;
    private String nivel; // Administrador ou Funcionário
    private String senha;
}