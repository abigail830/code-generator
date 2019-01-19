FROM java:openjdk-8-jdk
ADD ./build/libs/code-generator-1.0-SNAPSHOT.jar code-generator.jar
ADD ./template template
RUN chmod a+x code-generator.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "./code-generator.jar"]