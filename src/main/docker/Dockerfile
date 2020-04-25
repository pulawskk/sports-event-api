FROM openjdk:11.0

VOLUME /tmp
ADD maven/${project.artifactId}-${project.version}.jar myapp.jar
RUN sh -c 'touch myapp.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/myapp.jar"]