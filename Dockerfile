# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

# Add bash to minimal alpine image
RUN apk add --no-cache bash

# Add Maintainer Info
LABEL maintainer="anandvarkeyphilips@gmail.com"

# The application's jar file
ARG JAR_FILE=target/enterprise-validator*.jar

# Add a volume pointing to /tmp
VOLUME /packages

# Set Working Directory
WORKDIR /packages

# Run any commands like folder creations
RUN mkdir config config/enterprise-validator apps apps/enterprise-validator

# Add the application's jar to the container
ADD ${JAR_FILE} apps/enterprise-validator/enterprise-validator.jar

# Make port 80 available to the world outside this container
EXPOSE 80

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","apps/enterprise-validator/enterprise-validator.jar"]