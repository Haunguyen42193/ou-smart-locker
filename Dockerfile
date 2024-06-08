FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY $JAR_FILE app.jar
COPY src/main/resources/keystore.p12 /usr/local/share/keystore/
RUN keytool -importkeystore -srckeystore /usr/local/share/keystore/keystore.p12 -srcstorepass ${KEYSTORE_PASS} -srcstoretype pkcs12 -destkeystore /usr/java/openjdk-17/lib/security/cacerts -storepass changeit
CMD ["java", "-jar", "/app.jar"]
