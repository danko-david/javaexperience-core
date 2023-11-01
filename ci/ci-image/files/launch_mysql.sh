#!/bin/bash

service mysql start

while ! nc -z 127.0.0.1 3306 ;
do
  echo "Waiting mysql for getting ready."
  sleep 3;
done

mysql << 'EOF'

CREATE DATABASE test;
CREATE USER 'test'@'localhost' IDENTIFIED BY 'test';
GRANT ALL PRIVILEGES ON test.* TO 'test'@'localhost';
FLUSH PRIVILEGES;

EOF
