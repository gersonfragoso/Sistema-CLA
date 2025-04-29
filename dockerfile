FROM maven:3.9.7-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copia apenas os arquivos necessários para baixar dependências
COPY pom.xml ./
RUN mvn dependency:go-offline 

# Copia o código-fonte e compila o projeto
COPY src ./src
RUN mvn package -DskipTests

# Etapa 2: Imagem final para rodar a aplicação
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia o JAR da etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta da aplicação
EXPOSE 8080

# Comando para rodar a aplicação
CMD ["java", "-jar", "app.jar"]
