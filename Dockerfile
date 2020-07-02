FROM alpine/git as clone 
WORKDIR /app3
RUN git clone https://github.com/padmapriyapvld123/EcommercePOC.git

FROM maven:3.5-jdk-8-alpine as build
WORKDIR /app3
COPY --from=0 /app3/EcommercePOC /app3 
RUN mvn install

FROM openjdk:8-jre-alpine
WORKDIR /app3
COPY --from=build /app3/target/orderservice.jar /app3
EXPOSE 8081
ENTRYPOINT ["sh", "-c"]

CMD ["java -jar orderservice.jar"]




