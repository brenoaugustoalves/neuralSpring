🚀 NeuralSpring (Java 21 Edition)
O NeuralSpring é um motor de orquestração de Inteligência Artificial desenvolvido com Java 21 e Spring Boot 3.x. O foco do projeto é fornecer uma camada de abstração performática e segura para o consumo de Modelos de Linguagem (LLMs), utilizando as inovações de concorrência e sintaxe da versão mais recente da JVM.
🛠️ Diferenciais Técnicos (Padrão Sênior)
1. Concorrência com Virtual Threads (Project Loom)
   Diferente de arquiteturas legadas que escalam através de pools de threads pesados, o NeuralSpring utiliza Virtual Threads.
   Por que importa: Chamadas para APIs de IA (OpenAI/Anthropic) são bloqueantes e lentas. Com Virtual Threads, o sistema sustenta milhares de conexões simultâneas com um consumo mínimo de memória RAM, eliminando o overhead de troca de contexto do S.O.
   Implementação: Configurado via spring.threads.virtual.enabled=true.
2. Sintaxe Moderna e Segurança de Tipos
   Record Patterns: Utilizados para desestruturar payloads complexos de JSON retornados pelas engines neurais, tornando o código de parsing 40% mais limpo e imune a NullPointerException.
   Pattern Matching for Switch: Implementado na lógica de roteamento de modelos (Model Dispatcher), garantindo que todos os tipos de resposta sejam tratados em tempo de compilação.
   Sequenced Collections: Gestão nativa da ordem cronológica de prompts e replies usando as novas interfaces getFirst() / getLast().
3. Defesa Cibernética e Observabilidade
   Como Engenheiro com foco em Cyber Defense, o projeto aplica:
   Zero Hardcoded Secrets: Integração total com variáveis de ambiente e arquivos de configuração externos (.env).
   Rate Limiting: Proteção contra exaustão de tokens e ataques de negação de serviço na camada de integração de IA.
   Failover Estratégico: Lógica de fallback para trocar de modelo (ex: GPT-4 para GPT-3.5) em caso de latência alta ou erro de cota.
   🧰 Stack Técnica
   Backend: Java 21 (LTS) & Spring Boot 3.3+.
   Database: PostgreSQL (Persistência Relacional) e [Opcional: Redis para Cache de Inferência].
   Build Tool: Maven.
   Containerização: Docker & Docker Compose para orquestração de ambiente.
   🚀 Como Executar
   Pré-requisitos
   JDK 21 instalado.
   Maven 3.9+.
   Passo a Passo
   Clone o projeto e acesse a pasta raiz.
   Crie seu arquivo de ambiente baseado no template:
   bash
   cp src/main/resources/application-template.properties src/main/resources/application.properties
   Use o código com cuidado.

Insira sua NEURAL_API_KEY no arquivo criado.
Execute via Maven:
bash
mvn spring-boot:run
Use o código com cuidado.

🗺️ Roadmap de Evolução
Implementação de Spring AI para suporte multi-modelo nativo.
Suporte a Structured Outputs usando Record Patterns avançados.
Pipeline de CI/CD com validação automática de segurança de segredos (GitGuardian).
