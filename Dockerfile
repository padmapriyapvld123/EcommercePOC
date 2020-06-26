From openjdk:8
FROM maven:3.5-jdk-8 AS build  
COPY src /usr/src/app/src  
COPY pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean package
copy ./target/orderservice.jar orderservice.jar
CMD ["java","-jar","orderservice.jar"]

