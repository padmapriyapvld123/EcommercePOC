From openjdk:8
copy ./target/orderservice.jar orderservice.jar
CMD ["java","-jar","orderservice.jar"]

