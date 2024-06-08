FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY $JAR_FILE app.jar
COPY keystore.p12 /usr/local/share/ca-certificates/
RUN keytool -importkeystore -srckeystore /usr/local/share/keystore/keystore.p12 -srcstoretype pkcs12 -destkeystore $JAVA_HOME/lib/security/cacerts -storepass changeit
CMD ["java", "-jar", "/app.jar"]

