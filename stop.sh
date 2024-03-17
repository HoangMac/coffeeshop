#!/bin/bash

# Stop and remove Docker containers
echo "Stopping and removing Docker containers..."
docker-compose -f project_setup.yml down

echo "All Docker containers stopped."
