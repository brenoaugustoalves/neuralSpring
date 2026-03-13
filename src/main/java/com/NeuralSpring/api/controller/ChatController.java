package com.NeuralSpring.api.controller;

import com.NeuralSpring.api.AiService.OllamaService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

public class ChatController {
    private final OllamaService ollamaService;

    // Injeção via construtor (Boa prática: facilita testes e garante imutabilidade)
    public ChatController(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    @GetMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamOllama(@RequestParam String prompt) {
        // Timeout de 3 minutos (IA pode ser lenta no primeiro carregamento)
        SseEmitter emitter = new SseEmitter(180_000L);

        // Chamamos o serviço que retorna um Flux (fluxo de dados reativo)
        Flux<String> stream = ollamaService.analisarStream(prompt);

        // Conectamos o fluxo do Ollama ao emissor do navegador
        stream.subscribe(
                token -> {
                    try {
                        // Envia cada pedaço de texto assim que chega do Ollama
                        emitter.send(SseEmitter.event().data(token));
                    } catch (Exception e) {
                        emitter.completeWithError(e);
                    }
                },
                emitter::completeWithError, // Caso o Ollama dê erro (ex: 404, 500)
                emitter::complete            // Quando o Ollama terminar de falar
        );

        return emitter;
    }
}