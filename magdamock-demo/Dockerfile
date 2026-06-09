ARG MAGDAMOCK_VERSION="latest"
ARG DOCKER_URL="vlaamseoverheid-magda-docker-releases.jfrog.io"

FROM ${DOCKER_URL}/magda/magda.magdamock:${MAGDAMOCK_VERSION}

COPY ./application.yml application.yml

COPY ./soap /data/soap
COPY ./rest /data/rest

EXPOSE 8080
EXPOSE 8081