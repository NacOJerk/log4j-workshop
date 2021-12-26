FROM maven as build

COPY . /ldapserver
WORKDIR /ldapserver

RUN mvn clean package

FROM openjdk:18-alpine

COPY --from=build /ldapserver/target/ldapServer-jar-with-dependencies.jar /ldapserver.jar

ENTRYPOINT ["java", "-jar", "/ldapserver.jar", "0.0.0.0", "8081"]
