package com.padaria.cachoeiro.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "vendas")
@Data
public class Venda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Double total;
    private String formaPagamento;
}