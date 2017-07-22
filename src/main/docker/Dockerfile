FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD websocket-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
#ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
ENTRYPOINT ["java", "-jar", "/app.jar"]
CMD ["--activemq.url=ec2-18-220-50-158.us-east-2.compute.amazonaws.com"]