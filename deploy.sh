#!/bin/bash

# Build the Spring Boot application using Gradle
echo "Building Spring Boot application..."
./gradlew build

# Build the Docker image for the Spring Boot application
echo "Building Docker image for the Spring Boot application..."
docker build -t coffee-shop-image .

# Start the Docker containers using Docker Compose
echo "Starting Docker containers..."
docker-compose -f project_setup.yml up -d

echo "Application deployment complete."
