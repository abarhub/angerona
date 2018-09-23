#FROM openjdk:8-jdk-alpine
FROM openjdk:10-jdk

VOLUME /tmp

EXPOSE 8080

ARG JAR_FILE=target/web-0.0.1-SNAPSHOT.jar

ARG REP=/home/angerona

RUN mkdir -p ${REP}

WORKDIR ${REP}

ADD ${JAR_FILE} ${REP}/angerona-web.jar

#ADD target/data/coffrefort.zip data/coffrefort.zip

#ADD target/data/coffrefort.asc data/coffrefort.asc

#ADD target/config.properties config.properties

#RUN echo "dir=data" > config.properties
RUN echo "dir=data" > ./target/config.properties
ADD target/config.properties ${REP}/config.properties

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","./angerona-web.jar"]
