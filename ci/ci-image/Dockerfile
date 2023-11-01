FROM maven:3.9.5-eclipse-temurin-17-focal

RUN apt-get update
RUN apt-get install -y mariadb-server-10.3
RUN apt-get install -y netcat

COPY files/launch_mysql.sh /bin/
