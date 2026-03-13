package com.NeuralSpring.api.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
public class HealthController {

    // Controle de estado da aplicação (ex: muda para false no Graceful Shutdown)
    private final AtomicBoolean isAlive = new AtomicBoolean(true);

    /**
     * LIVENESS PROBE: "O processo Java travou?"
     * Deve ser o mais simples possível. Se não responder, o K8s reinicia o Pod.
     */
    @GetMapping("/liveness")
    public ResponseEntity<Void> liveness() {
        // Se o código chegou aqui, a CPU não está travada e a JVM está operando.
        return ResponseEntity.ok().build();
    }

    /**
     * READINESS PROBE: "Posso receber tráfego agora?"
     * Usa sua lógica inteligente para dizer ao Load Balancer se deve enviar clientes.
     */
    @GetMapping("/healthz")
    public ResponseEntity<Map<String, Object>> readiness() {
        boolean healthy = isAlive.get();

        Map<String, Object> body = Map.of(
                "status", healthy ? "UP" : "DOWN",
                "timestamp", System.currentTimeMillis(),
                "message", healthy ? "Pronto para receber requisições" : "Sinalizando encerramento ou instabilidade"
        );

        return ResponseEntity
                .status(healthy ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE)
                .body(body);
    }
}


