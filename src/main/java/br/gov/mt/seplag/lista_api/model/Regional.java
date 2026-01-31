package br.gov.mt.seplag.lista_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Representa uma Regional.
 * Estrutura definida no requisito para sincronização com API externa.
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "regional")
public class Regional {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_regional", nullable = false)
    private Integer idRegional; // ID vindo da API Externa

    @Column(length = 200)
    private String nome;

    private Boolean ativo;
}