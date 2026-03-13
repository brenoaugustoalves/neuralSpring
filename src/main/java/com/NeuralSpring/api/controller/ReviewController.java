package com.NeuralSpring.api.controller;

import com.NeuralSpring.api.AiService.OllamaService;
import com.NeuralSpring.api.AiService.PerformanceBenchmarkService;
import com.NeuralSpring.api.model.AnaliseReview;
import com.NeuralSpring.api.repository.AnaliseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/reviews") // Base da URL: /api/reviews
public class ReviewController {

    private final Logger log = LoggerFactory.getLogger(ReviewController.class);

    // Injeção de todos os serviços via construtor (O jeito certo)
    private final OllamaService ollamaService;
    private final PerformanceBenchmarkService benchmarkService;
    private final AnaliseRepository repository; // Adicione o seu Repository aqui

    public ReviewController(OllamaService ollamaService,
                            PerformanceBenchmarkService benchmarkService,
                            AnaliseRepository repository) {
        this.ollamaService = ollamaService;
        this.benchmarkService = benchmarkService;
        this.repository = repository;
    }

    @PostMapping(value = "/analisar", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter analisarReview(@RequestBody Map<String, String> payload) {
        SseEmitter emitter = new SseEmitter(120_000L); // 2 minutos para IA lenta
        String comentario = payload.get("comentario");

        // Acumulador para salvar no banco depois
        StringBuilder respostaCompleta = new StringBuilder();

        log.info("Iniciando análise via Virtual Thread para comentário: {}", comentario);

        // Java 21: Virtual Thread para não bloquear o servidor
        Thread.ofVirtual().start(() -> {
            ollamaService.analisarStream(comentario).subscribe(
                    token -> {
                        try {
                            respostaCompleta.append(token); // Acumula
                            emitter.send(SseEmitter.event().data(token)); // Envia
                        } catch (Exception e) {
                            log.error("Erro ao enviar token SSE", e);
                        }
                    },
                    error -> {
                        log.error("Erro no fluxo do Ollama", error);
                        emitter.completeWithError(error);
                    },
                    () -> {
                        // Quando a IA termina de "falar", salvamos no Postgres
                        salvarResultadoNoBanco(comentario, respostaCompleta.toString());
                        emitter.complete();
                        log.info("Análise finalizada e salva no banco.");
                    }
            );
        });

        return emitter;
    }

    private void salvarResultadoNoBanco(String original, String respostaIA) {
        // Lógica de salvamento (Certifique-se de ter a Entity AnaliseReview)
        AnaliseReview analise = new AnaliseReview();
        analise.setComentarioOriginal(original);
        analise.setSugestaoResposta(respostaIA);
        analise.setSentimento(respostaIA.toUpperCase().contains("POSITIVO") ? "POSITIVO" : "NEGATIVO");
        repository.save(analise);
    }

    @GetMapping("/historico")
    public List<AnaliseReview> buscarHistorico() {
        // Retorna as últimas 5 análises ordenadas por ID decrescente
        return repository.findAll()
                .stream()
                .sorted((a, b) -> b.getId().compareTo(a.getId()))
                .limit(5)
                .toList();
    }


    // --- Endpoints de Teste/Debug ---

    @GetMapping("/test-speed")
    public String runTest() {
        benchmarkService.testModelSpeed("llama3:8b-instruct-q2_K");
        return "Benchmark iniciado! Verifique o log do IntelliJ.";
    }

    @GetMapping("/kill")
    public void quebrarAplicacao() {
        log.warn("SIMULANDO ERRO FATAL...");
        System.exit(1);
    }
}
