FROM openjdk:8-jdk-alpine

VOLUME /tmp

EXPOSE 8080

ARG JAR_FILE=target/websocket-demo-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} angerona-web.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/angerona-web.jar"]
