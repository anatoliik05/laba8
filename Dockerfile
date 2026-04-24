# ЭТАП 1: Сборка
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ЭТАП 2: Подготовка слоев
FROM eclipse-temurin:17-jdk-jammy AS prepare
WORKDIR /app
COPY --from=build /app/target/*.war app.jar
# Распаковываем приложение специальным инструментом Spring Boot
RUN java -Djarmode=layertools -jar app.jar extract

# ЭТАП 3: Запуск
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
# Копируем слои по отдельности
COPY --from=prepare /app/dependencies/ ./
COPY --from=prepare /app/spring-boot-loader/ ./
COPY --from=prepare /app/snapshot-dependencies/ ./
COPY --from=prepare /app/application/ ./

EXPOSE 8080
# Запуск через специальный Launcher, который правильно поднимет Tomcat и JSP
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.WarLauncher"]