package com.NeuralSpring.api.controller;


import com.NeuralSpring.api.AiService.PerformanceBenchmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private PerformanceBenchmarkService benchmarkService;

    @GetMapping("/test-speed")
    public String runTest() {
        // Use o nome exato do modelo leve que você baixou
        benchmarkService.testModelSpeed("llama3:8b-instruct-q2_K");
        return "Teste de velocidade iniciado! Verifique o console da IDE.";
    }
}




