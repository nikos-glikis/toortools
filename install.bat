
rem mvn install:install-file  -DgroupId=com.object0r -DartifactId=toortools -Dversion=1.0.2 -Dpackaging=jar -Dfile=target/toortools-1.0.2-jar-with-dependencies.jar
mvn install -DperformRelease=true -DcreateChecksum=true
