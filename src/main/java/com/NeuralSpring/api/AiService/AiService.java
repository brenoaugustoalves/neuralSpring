package com.NeuralSpring.api.AiService;

import com.NeuralSpring.api.model.AnaliseReview;
import com.NeuralSpring.api.repository.AnaliseRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final ChatClient chatClient;
    private final AnaliseRepository repository;

    public AiService(ChatClient.Builder builder, AnaliseRepository repository) {
        this.chatClient = builder.build();
        this.repository = repository;
    }

    public AnaliseReview analisarESalvar(String texto) {
        String respostaIA = chatClient.prompt()
                .system("Você é um analista de e-commerce. Responda o sentimento (POSITIVO/NEGATIVO) e uma sugestão.")
                .user(texto)
                .call()
                .content();

        AnaliseReview analise = new AnaliseReview();
        analise.setComentarioOriginal(texto);
        analise.setSentimento(respostaIA.contains("POSITIVO") ? "POSITIVO" : "NEGATIVO");
        analise.setSugestaoResposta(respostaIA);

        return repository.save(analise);
    }
}

