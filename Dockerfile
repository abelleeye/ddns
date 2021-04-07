FROM openjdk:11-jre-slim-buster
ENV TZ=Asia/Shanghai

COPY ./build/libs/ddns.jar /app.jar
CMD ["java", "-jar", "/app.jar", "/config.json"]