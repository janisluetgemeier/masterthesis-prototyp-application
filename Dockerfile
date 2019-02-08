FROM maven:3.2-jdk-8 AS build
RUN mkdir -p /build
COPY . /build/
WORKDIR /build
RUN mvn clean package -DskipTests


FROM openjdk:8-jdk-slim

EXPOSE 8090
EXPOSE 8091

RUN mkdir -p /app

COPY --from=build /build/target/*.jar /app/application.jar

CMD ["sh","-c","java $JAVA_OPTS -jar /app/application.jar -server --server.port=8090"]
