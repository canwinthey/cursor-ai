#!/bin/bash

# Build script for Docker image
# This script builds the Maven project and then creates the Docker image

echo "Building Maven project..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "Maven build failed!"
    exit 1
fi

echo "Building Docker image..."
docker build -t cursor-crud-product:latest .

if [ $? -ne 0 ]; then
    echo "Docker build failed!"
    exit 1
fi

echo "Docker image 'cursor-crud-product:latest' built successfully!"
echo "To run the container, use: docker run -d -p 9090:9090 --name cursor-crud-product-container cursor-crud-product:latest"

