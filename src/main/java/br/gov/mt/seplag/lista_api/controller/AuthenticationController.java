package br.gov.mt.seplag.lista_api.controller;

import br.gov.mt.seplag.lista_api.dto.auth.AuthenticationDTO;
import br.gov.mt.seplag.lista_api.dto.auth.LoginResponseDTO;
import br.gov.mt.seplag.lista_api.infra.security.TokenService;
import br.gov.mt.seplag.lista_api.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@Tag(name = "Autenticação", description = "Endpoint de Login para obtenção de Token JWT")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Efetuar Login", description = "Recebe email/senha e retorna um Token JWT válido por 5 minutos.")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        // Cria o objeto de autenticação com os dados que chegaram
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        
        // Spring Security vai no banco, verifica o hash da senha e autentica
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // Senha correta - Gera o token
        var token = tokenService.generateToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @Operation(summary = "Renovar Token", description = "Gera um novo token de 5 minutos para o usuário já autenticado.")
    @PostMapping("/refresh")
    public ResponseEntity refresh(){
        // Recupera o usuário que já passou pelo filtro de segurança
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        
        // Gera um novo token 
        var newToken = tokenService.generateToken(usuario);
        
        return ResponseEntity.ok(new LoginResponseDTO(newToken));
    }
}