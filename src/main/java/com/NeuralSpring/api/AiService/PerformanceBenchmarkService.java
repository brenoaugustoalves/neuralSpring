package com.NeuralSpring.api.AiService;


import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class PerformanceBenchmarkService {

    private final WebClient webClient;

    public PerformanceBenchmarkService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:11434").build();
    }

    public void testModelSpeed(String modelName) {
        String prompt = "Responda apenas com a palavra: OK"; // Teste curto para medir latência
        long startTime = System.currentTimeMillis();

        System.out.println(">>> Iniciando Benchmark para o modelo: " + modelName);

        webClient.post()
                .uri("/api/generate")
                .bodyValue(Map.of(
                        "model", modelName,
                        "prompt", prompt,
                        "stream", false // Pegamos o tempo total da resposta fechada
                ))
                .retrieve()
                .bodyToMono(Map.class)
                .doOnSuccess(response -> {
                    long endTime = System.currentTimeMillis();
                    double totalSeconds = (endTime - startTime) / 1000.0;

                    System.out.println("-----------------------------------------");
                    System.out.println("RESULTADO DO BENCHMARK:");
                    System.out.println("Tempo Total: " + totalSeconds + " segundos");
                    System.out.println("Modelo Utilizado: " + modelName);
                    System.out.println("-----------------------------------------");
                })
                .doOnError(e -> System.err.println("ERRO NO TESTE: " + e.getMessage()))
                .subscribe();
    }
}

