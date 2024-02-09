# Используем образ OpenJDK
FROM openjdk:17-jdk-alpine

# Устанавливаем переменную окружения для указания порта, который будет слушать ваше Spring Boot приложение
ENV PORT=8080

# Копируем скомпилированные Java-файлы в контейнер
COPY target/DeutschTrainer-0.0.1-SNAPSHOT.jar /app/DeutschTrainer.jar

# Устанавливаем рабочую директорию
WORKDIR /app

# Запускаем приложение
CMD ["java", "-jar", "DeutschTrainer.jar"]



