package com.NeuralSpring.api.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "analises_reviews")
public class AnaliseReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT") // TEXT permite comentários longos
    private String comentarioOriginal;

    private String sentimento;

    @Column(columnDefinition = "TEXT")
    private String sugestaoResposta;

    private LocalDateTime dataCriacao = LocalDateTime.now();

    // --- Getters e Setters (Obrigatórios para o JPA) ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComentarioOriginal() {
        return comentarioOriginal;
    }

    public void setComentarioOriginal(String comentarioOriginal) {
        this.comentarioOriginal = comentarioOriginal;
    }

    public String getSentimento() {
        return sentimento;
    }

    public void setSentimento(String sentimento) {
        this.sentimento = sentimento;
    }

    public String getSugestaoResposta() {
        return sugestaoResposta;
    }

    public void setSugestaoResposta(String sugestaoResposta) {
        this.sugestaoResposta = sugestaoResposta;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
}

