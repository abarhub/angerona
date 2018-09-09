FROM openjdk:8-jdk-alpine

VOLUME /tmp

EXPOSE 8080

ARG JAR_FILE=target/web-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} angerona-web.jar

ADD target/data/coffrefort.zip data/coffrefort.zip

ADD target/config.properties config.properties

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/angerona-web.jar"]
