#Start with a base image containing Java runtime
FROM openjdk:21-jdk-slim

#Information around who maintains the image
MAINTAINER sandesh-jain.com

# Add the application's jar to the image
#COPY target/sandeshjain-tutorials-spring-boot-mongodb-reactive-0.0.1-SNAPSHOT.jar tutorial.jar
ADD target/*.jar tutorial.jar

EXPOSE 8080

#CMD [ "org.h2.tools.GUIConsole", "-webAllowOthers" ]

# execute the application
ENTRYPOINT ["java","-jar","tutorial.jar"]