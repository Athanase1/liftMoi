
FROM openjdk:17-ea-slim AS build
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src/ ./src
RUN ./mvnw package -DskipTests


FROM openjdk:17-ea-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
EXPOSE 5005

ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "app.jar"]