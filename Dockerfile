FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY app.jar app.jar

# Mode headless pour éviter QuantumRenderer
CMD ["java", "-Dprism.order=sw", "-Djava.awt.headless=true", "-jar", "app.jar"]
