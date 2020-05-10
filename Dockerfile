FROM maven:3-jdk-12 AS MAVEN_TOOL_CHAIN
WORKDIR /tmp/
RUN mkdir db
RUN mkdir secret
COPY pom.xml /tmp/
RUN mvn dependency:go-offline
COPY src /tmp/src/
RUN export MAVEN_OPTS="-Duser.timezone=UTC"
RUN mvn package

FROM adoptopenjdk/openjdk8:debian-jre
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/todo.jar /opt/app/app.jar
WORKDIR /opt/app/
RUN chmod +x app.jar
RUN mkdir db
RUN mkdir secret
ENV TODO_OPTS=""

EXPOSE 8080

COPY entrypoint.sh /opt/app/
RUN chmod +x entrypoint.sh
ENTRYPOINT ["sh", "-c", "cd /opt/app; sh entrypoint.sh"]

# HEALTHCHECK --interval=1m --timeout=3s CMD wget --quiet --tries=1 --spider http://localhost:8080/swagger-ui.html || exit 1