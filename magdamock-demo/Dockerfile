ARG MAGDAMOCK_VERSION="latest"

FROM vlaamseoverheid-magda-docker-releases.jfrog.io/magda/magda.magdamock:${MAGDAMOCK_VERSION}

COPY ./application.yml /application.yml

COPY ./wiremock-data /data/wiremock

EXPOSE 8080
EXPOSE 8081