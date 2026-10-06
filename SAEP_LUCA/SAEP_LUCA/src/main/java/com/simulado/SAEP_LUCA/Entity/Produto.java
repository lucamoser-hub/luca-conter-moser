package com.simulado.SAEP_LUCA.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "produto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O código do produto é obrigatório.")
    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @NotBlank(message = "O nome do produto é obrigatório.")
    @Column(nullable = false, length = 150)
    private String nome;

    @NotBlank(message = "A categoria é obrigatória.")
    @Column(length = 100)
    private String categoria;

    @Column(name = "material_cabo", length = 100)
    private String materialCabo;

    @Column(name = "material_cabeca", length = 100)
    private String materialCabeca;

    @Column(length = 255)
    private String especificacoes;

    @NotNull(message = "O peso é obrigatório.")
    @Min(value = 0, message = "O peso não pode ser negativo.")
    @Column(name = "peso_kg")
    private Double pesoKg;

    @NotNull(message = "O tamanho em mm é obrigatório.")
    @Min(value = 0, message = "O tamanho não pode ser negativo.")
    @Column(name = "tamanho_mm")
    private Double tamanhoMm;

    @NotNull(message = "A quantidade inicial em estoque é obrigatória.")
    @Min(value = 0, message = "A quantidade não pode ser negativa.")
    @Column(name = "quantidade_estoque", nullable = false)
    private Integer quantidadeEstoque;

    @NotNull(message = "O valor de estoque mínimo é obrigatório.")
    @Min(value = 0, message = "O estoque mínimo não pode ser negativo.")
    @Column(name = "estoque_minimo", nullable = false)
    private Integer estoqueMinimo;
}