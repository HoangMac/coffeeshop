FROM amazoncorretto:21

# Set the working directory inside the container
WORKDIR /app

CMD ["./gradlew", "clean", "build"]

# Copy the executable JAR file from the build directory into the container
COPY build/libs/coffee-shop-0.0.1-SNAPSHOT.jar /app/coffee-shop-0.0.1-SNAPSHOT.jar

# Expose the port that your Spring Boot application listens on
EXPOSE 8080

# Specify the command to run your Spring Boot application when the container starts
CMD ["java", "-jar", "coffee-shop-0.0.1-SNAPSHOT.jar"]