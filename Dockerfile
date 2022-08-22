FROM openjdk:18
ENV ARTIFACT_NAME=beavers-server-commander-*.jar
COPY ./build/libs/$ARTIFACT_NAME /tmp/bsc.jar
WORKDIR /tmp
ENTRYPOINT ["java","-jar","bsc.jar"]