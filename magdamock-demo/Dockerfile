# Use the specified image as the base
FROM vlaamseoverheid-magda-docker-snapshots.jfrog.io/magda/magda.magdamock:latest

# Copy the application.yml file into the root of the container
COPY ./application.yml /application.yml

# Copy the wiremock-data directory into the container
COPY ./wiremock-data /data/wiremock

# Document that the container listens on port 8080
EXPOSE 8080