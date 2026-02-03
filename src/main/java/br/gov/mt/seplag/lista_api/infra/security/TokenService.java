package br.gov.mt.seplag.lista_api.infra.security;

import br.gov.mt.seplag.lista_api.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // Lê a chave secreta definida no application.yaml
    @Value("${api.security.token.secret}")
    private String secret;

    // Método responsável por Gerar o Token
    public String generateToken(Usuario usuario){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("lista-api") // Quem emitiu o token
                    .withSubject(usuario.getEmail()) // Quem é o dono do token
                    .withExpiresAt(genExpirationDate()) // Define a data de validade (5 min)
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    // Método para validar o Token e retornar o email do usuário
    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("lista-api")
                    .build()
                    .verify(token) // Se expirou ou está inválido, lança exceção
                    .getSubject();
        } catch (JWTVerificationException exception){
            return ""; // Retorna vazio se o token não for válido
        }
    }
	
    // Regra de Negócio: Expiração em 5 minutos
    private Instant genExpirationDate(){
        // Horário do Amazonas/Mato Grosso
        return LocalDateTime.now().plusMinutes(5).toInstant(ZoneOffset.of("-04:00"));
    }
}