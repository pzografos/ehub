FROM openjdk:11-slim

# Default payara ports to expose
EXPOSE 8080

# Configure environment variables
ENV PAYARA_HOME=/opt/payara\
    DEPLOY_DIR=/opt/payara/deployments

WORKDIR ${PAYARA_HOME}

# Download specific
ARG PAYARA_VERSION="5.192"
ENV PAYARA_VERSION="$PAYARA_VERSION"
RUN apt-get -yy update && \
    apt-get -yy install wget && \
    wget --no-verbose -O ${PAYARA_HOME}/payara-micro.jar http://central.maven.org/maven2/fish/payara/extras/payara-micro/${PAYARA_VERSION}/payara-micro-${PAYARA_VERSION}.jar

# Default command to run
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=90.0", "-Xdebug", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000", "-jar", "payara-micro.jar" ]
CMD ["--deploymentDir", "/opt/payara/deployments"]
