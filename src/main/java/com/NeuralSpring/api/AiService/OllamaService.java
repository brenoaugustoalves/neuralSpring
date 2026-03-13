package com.NeuralSpring.api.AiService;


import com.NeuralSpring.api.dto.OllamaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Map;

@Service
public class OllamaService {

    private final WebClient webClient;

    // 1. Lê a URL do application.properties (localhost na IDE, ollama no Docker)
    public OllamaService(WebClient.Builder builder, @Value("${spring.ai.ollama.base-url}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    public Flux<String> analisarStream(String comentario) {
        // 2. Prompt "Curto e Grosso" para economizar tempo da GPU MX450
        String promptIncisivo = """
    Analise o comentário abaixo. 
    Responda EXATAMENTE neste formato, sem explicações extras:
    SENTIMENTO: [POSITIVO, NEGATIVO ou NEUTRO]
    SUGESTÃO: [Uma frase curta e profissional de resposta].
    
    Comentário: "%s"
    """.formatted(comentario);


        return webClient.post()
                .uri("/api/generate")
                .bodyValue(Map.of(
                        "model", "llama3.2:1b", // Versão leve de ~2GB
                        "prompt", promptIncisivo,
                        "stream", true
                ))
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                // Trata erros de conexão ou 500 do Ollama
                .bodyToFlux(OllamaResponse.class)
                .map(OllamaResponse::response)
                .onErrorReturn("Erro: Não foi possível processar a análise no momento.");
    }
}