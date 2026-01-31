package br.gov.mt.seplag.lista_api.repository;

import br.gov.mt.seplag.lista_api.model.Regional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionalRepository extends JpaRepository<Regional, Long> {
    
    // Busca todas as regionais ativas para comparação
    List<Regional> findByAtivoTrue();

    // Busca uma regional pelo ID externo
    Optional<Regional> findByIdRegionalAndAtivoTrue(Integer idRegional);
}