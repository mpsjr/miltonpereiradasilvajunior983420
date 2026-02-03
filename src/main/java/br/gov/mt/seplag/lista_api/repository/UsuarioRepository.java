package br.gov.mt.seplag.lista_api.repository;

import br.gov.mt.seplag.lista_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Método usado para achar o usuário no login (email)
    UserDetails findByEmail(String email);
}