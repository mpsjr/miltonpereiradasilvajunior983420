package br.gov.mt.seplag.lista_api.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter implements Filter {

    // Armazena um Bucket para cada IP que acessa a API
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

	// Whitelist - prefixos que serão ignorados para não consumir tokens
    private static final String[] WHITELIST_URLS = {
            "/swagger-ui",
            "/v3/api-docs",
			"/api-docs",
            "/swagger-resources",
            "/webjars",
            "/favicon.ico",
            "/actuator",
			"/error"
    };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getRequestURI();

        // Se for uma URL da Whitelist, passa direto sem checar o bucket
        if (isExcluded(path)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // Lógica de Rate Limit para endpoints de negócio (/v1/...)
        String ip = request.getRemoteAddr();
        Bucket bucket = cache.computeIfAbsent(ip, this::createNewBucket);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            response.setStatus(429);
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("Limite de requisições excedido (10 req/min). Aguarde um momento.");
        }
    }

    private boolean isExcluded(String path) {
        for (String excluded : WHITELIST_URLS) {
            if (path.startsWith(excluded)) {
                return true;
            }
        }
        return false;
    }

    private Bucket createNewBucket(String key) {
        // Regra: 10 requisições por minuto
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }
}