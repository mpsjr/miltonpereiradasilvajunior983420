package br.gov.mt.seplag.lista_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Representa uma Regional.
 * Estrutura definida no requisito para sincronização.
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "regional")
public class Regional {

    @EqualsAndHashCode.Include
    @Id
    private Integer id; // ID manual para sincronizar com API externa

    @Column(length = 200)
    private String nome;

    private Boolean ativo;
}