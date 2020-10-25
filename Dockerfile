FROM openjdk:11-jdk
EXPOSE 8901
ADD build/libs/wownetwork-0.0.1-SNAPSHOT.jar wownetwork.jar
ENTRYPOINT ["java","-jar","/wownetwork.jar"]