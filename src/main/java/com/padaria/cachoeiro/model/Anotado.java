package com.padaria.cachoeiro.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "anotados")
@Data
public class Anotado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nome_cliente")
    private String nomeCliente;
    
    @Column(name = "valor_devido") 
    private Double valorDevido;
    
    @Column(name = "data_lancamento")
    private LocalDate dataLancamento;
    
    private String status; 
}