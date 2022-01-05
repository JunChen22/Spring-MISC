FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=*.jar
EXPOSE 8080/tcp
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]