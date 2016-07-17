#mvn install:install-file  -DgroupId=com.object0r.toortools -DartifactId=toortools -Dversion=1.0.2 -Dpackaging=jar -Dfile=target/toortools-1.0.2-jar-with-dependencies.jar
#mvn install:install-file -DgroupId=ftp4j -DartifactId=ftp4j -Dversion=1.7.2 -Dpackaging=jar -Dfile=lib/ftp4j/ftp4j/1.7.2/ftp4j-1.7.2.jar

mvn install -DperformRelease=true -DcreateChecksum=true