FROM bellsoft/liberica-openjdk-alpine:18.0.2-10
ENV ARTIFACT_NAME=beavers-server-commander-*.jar
COPY ./build/libs/$ARTIFACT_NAME /tmp/bsc.jar
WORKDIR /tmp
ENTRYPOINT ["java","-jar","bsc.jar"]