FROM docker.io/eclipse-temurin:25.0.5.1_2-jre

VOLUME /tmp

# These volumes are required for deployment in argocd
VOLUME /secrets
VOLUME /vault/secrets
VOLUME /config

COPY target/*.jar /target/

RUN ln -s $(find target -name "*.jar" ! -name "*-plain.jar" | head -n 1) /app.jar

COPY src/main/resources/magda.xsd /data/magda.xsd
COPY src/main/resources/scripts /data/scripts

EXPOSE 8080:8080

RUN apt-get update && apt-get install -y --no-install-recommends git

CMD ["/data/scripts/entrypoint.sh"]
