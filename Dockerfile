FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY /build/libs/service.jar /app/documentService.jar
ENTRYPOINT ["java", "-jar", "documentService.jar"]