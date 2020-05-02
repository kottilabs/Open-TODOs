FROM maven:3.5.2-jdk-8-alpine AS MAVEN_TOOL_CHAIN
WORKDIR /tmp/
COPY pom.xml /tmp/
RUN mvn verify clean --fail-never
COPY src /tmp/src/
RUN mvn package

FROM adoptopenjdk/openjdk8:debian-jre
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/todo.jar /opt/app/app.jar

WORKDIR /opt/app/

RUN chmod 777 app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]

# HEALTHCHECK --interval=1m --timeout=3s CMD wget --quiet --tries=1 --spider http://localhost:8080/swagger-ui.html || exit 1