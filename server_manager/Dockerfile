FROM maven as build

ARG SERVER_IP
ENV SERVER_IP $SERVER_IP

ARG CLIENT_IMAGE
ENV CLIENT_IMAGE $CLIENT_IMAGE

COPY . /login_manager
WORKDIR /login_manager

RUN mvn clean package



FROM ubuntu:latest

RUN apt update && apt install -y curl openjdk-17-jdk
RUN curl https://get.docker.com/ -o getDocker.sh
RUN chmod +x getDocker.sh
RUN ./getDocker.sh
RUN rm getDocker.sh

WORKDIR /server
RUN curl https://papermc.io/api/v2/projects/paper/versions/1.18.1/builds/101/downloads/paper-1.18.1-101.jar -o server.jar
RUN echo "eula=true" > eula.txt
COPY server.properties server.properties
RUN mkdir plugins

COPY --from=build /login_manager/target/server_manager-1.0-SNAPSHOT.jar /server/plugins/manager.jar

ENTRYPOINT ["java", "-Xmx4G", "-jar", "server.jar"]
