package br.gov.mt.seplag.lista_api.dto.album;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class AlbumRequestDTO {

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    @NotNull(message = "O ano de lançamento é obrigatório")
    private Integer anoLancamento;

    @NotEmpty(message = "O álbum deve ter pelo menos um artista vinculado")
    private Set<Long> artistasIds;
}