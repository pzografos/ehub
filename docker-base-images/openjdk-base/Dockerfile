FROM azul/zulu-openjdk-alpine:11

ENV APPLICATION_HOME=/opt/application

WORKDIR ${APPLICATION_HOME}

# Default command to run
ENTRYPOINT exec java $JAVA_OPTS -jar application.jar


