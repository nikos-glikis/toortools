toortools
=========

Collection of small tools and classes I regularly use in my projects. I also created wrappers for lots and lots of small snippets of code that I find my self googling all the time.

It is created based on my needs and my programming style. I have tried to create it as parameterizable and static as possible.

It is build as a maven project. That means that you must install it in your local repository before you use it, or build it as a jar. 

.idea folder is included, this is not related to Java, but its the intellij files a cloned repository is ready for development.

If you have any suggestions please let me know.

Some things included:

* Solid Customized HTTP Library to create a request (Cookies, headers, parameters, timeouts, POST/GET methods etc).
* Quick helper classes to do various things like: 
    - Quickly download a URL with Cookie,
    - Disable https protections in java.
* Lightning fast LevelDB Key-Value Database Implementation.
* Define common datatypes along projects (ProxyInfo)
* OsHelper:
    - Quick and easy Os Detector (Windows/Linux).
    - Print with Colors for console Helper.
* Logger
* 2 implementations of FTP
* File download helper
* Simple POST request helper
* Basic String manipulation
* String to MD5
* Get External Ip
* Torify traffic (Windows and Linux)

Installation
------------

There are 2 ways to use this repository:

* As a maven repository
* As a jar file added to your project's classpath.


* Install Maven:

Windows:

    - Download maven binaries from: https://maven.apache.org/download.cgi (apache-maven-3.3.9-bin.zip or similar should be ok)
    - Extract somewhere and add the bin/ directory to the systems PATH.
    - You know when maven is installed when you run mvn and you don't get a not exists error.
    
Linux:

    sudo apt-get install maven
    
* Create a single jar with all dependencies packed:

    mvn clean compile assembly:single
    
Jar is created in target/ subfolder in the directory (toortools-1.0.2-jar-with-dependencies.jar)

At this point you can use the jar as any other jar.

If you want to use it in a maven project you will have to install the jar in the local maven repository (install.sh):
    
    mvn install:install-file  -DgroupId=com.object0r -DartifactId=toortools -Dversion=1.0.2 -Dpackaging=jar -Dfile=target/toortools-1.0.2-jar-with-dependencies.jar

To use it add below in your pom.xml:    
    
                <dependency>
                    <groupId>com.object0r</groupId>
                    <artifactId>toortools</artifactId>
                    <version>1.0.2</version>
                </dependency>
            
Install maven 3 ubuntu:
=======================

http://sysads.co.uk/2014/05/install-apache-maven-3-2-1-ubuntu-14-04/

    sudo apt-get install gdebi
    
    wget http://ppa.launchpad.net/natecarlson/maven3/ubuntu/pool/main/m/maven3/maven3_3.2.1-0~ppa1_all.deb
    
    sudo gdebi maven3_3.2.1-0~ppa1_all.deb
    
    sudo ln -s /usr/share/maven3/bin/mvn /usr/bin/maven