package com.padaria.cachoeiro.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data   
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;
    private Double preco;
    private Integer estoque;
}
