cat > Dockerfile <<'EOF'
FROM amazoncorretto:17-alpine

WORKDIR /app

COPY presentacion/target/presentacion-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
EOF