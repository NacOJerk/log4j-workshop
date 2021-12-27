FROM maven as build

COPY . /client
WORKDIR /client

RUN mvn clean package

FROM openjdk:18-alpine

WORKDIR /client

COPY --from=build /client/target/Client-jar-with-dependencies.jar client.jar

RUN echo "ctf{GoodJobM8}" > flag.txt

ENTRYPOINT ["java", "-jar", "-Dcom.sun.jndi.ldap.object.trustURLCodebase=true", "client.jar"]
