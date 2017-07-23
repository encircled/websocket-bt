FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD websocket-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT ["java", "-jar", "/app.jar"]
CMD ["--activemq.url=ec2-18-220-50-158.us-east-2.compute.amazonaws.com,--activemq.port=61613"]