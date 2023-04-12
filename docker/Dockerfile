FROM gradle:jdk17-alpine
WORKDIR /payment-command-service

COPY src /payment-command-service/src/
COPY build.gradle .
COPY lombok.config .

RUN gradle clean build -x test

ENTRYPOINT ["java", "-jar", "./build/libs/payment-command-service-1.0.0.jar"]