# Working with Maven #

## POM Variables ##
The following variables are available to be used in the pom.xml file.

* Project Information
 * `${project.build.finalName}`
 * `${project.basedir}` base directory
 * `${project.build.directory}` target/
 * `${project.build.outputDirectory}` target/classes  
* Version Information
 * `${project.version}` OOTB version 
 * `${buildNumber}` Provided by the [buildnumber-maven-plugin](http://mojo.codehaus.org/buildnumber-maven-plugin/usage.html).
 * `${parsedVersion.majorVersion}`  Provided by the [build-helper-maven-plugin](http://mojo.codehaus.org/build-helper-maven-plugin/usage.html).
 * `${parsedVersion.minorVersion}`
 * `${parsedVersion.incrementalVersion}`
 * `${parsedVersion.qualifier}`
 * `${parsedVersion.buildNumber}`

## Adding Git to PATH ##
    D:\maven\apache-maven-3.0.4\bin\mvnreleasepath.cmd

Contains the following:
    
    set PATH=%PATH%;D:\Program Files (x86)\Git\cmd
    

## Common Maven Commands ##
    mvn site #generates the site and reports
    mvn site:deploy #pushes the site files 

    mvn package #generates package
    mvn release:prepare #preps a release
    mvn release:release #executes a release 

    mvn clean #clean the target directory and erase the build files

    mvn test #execute tests