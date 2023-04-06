#!/usr/bin/env bash
#export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/
#export JAVA_HOME=/home/nikos/.jdks/corretto-11.0.13/
#mvn install:install-file  -DgroupId=com.object0r.toortools -DartifactId=toortools -Dversion=1.0.2 -Dpackaging=jar -Dfile=target/toortools-1.0.2-jar-with-dependencies.jar
#mvn install:install-file -DgroupId=ftp4j -DartifactId=ftp4j -Dversion=1.7.2 -Dpackaging=jar -Dfile=lib/ftp4j/ftp4j/1.7.2/ftp4j-1.7.2.jar

mvn install -DperformRelease=true -DcreateChecksum=true -Dgpg.passphrase=password
#mvn deploy -DperformRelease=true -DcreateChecksum=true -Dgpg.passphrase=password