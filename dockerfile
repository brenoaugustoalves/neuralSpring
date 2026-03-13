# 1. Use uma imagem do Java (Certifique-se de que é a 21 para as Virtual Threads)
FROM eclipse-temurin:21-jdk-alpine

# 2. Define o diretório de trabalho
WORKDIR /app

# 3. Copia o seu arquivo .jar (certifique-se de que o nome está correto após o build)
COPY target/*.jar app.jar

# 4. Expõe a porta que definimos no application.properties
EXPOSE 8081

# 5. Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]





