FROM maven:3-jdk-12 AS MAVEN_TOOL_CHAIN
WORKDIR /tmp/
RUN mkdir db
COPY pom.xml /tmp/
RUN mvn dependency:go-offline
COPY src /tmp/src/
RUN mvn package

FROM adoptopenjdk/openjdk8:debian-jre
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/todo.jar /opt/app/app.jar

WORKDIR /opt/app/

RUN chmod 777 app.jar

RUN mkdir db

EXPOSE 8080

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar

# HEALTHCHECK --interval=1m --timeout=3s CMD wget --quiet --tries=1 --spider http://localhost:8080/swagger-ui.html || exit 1