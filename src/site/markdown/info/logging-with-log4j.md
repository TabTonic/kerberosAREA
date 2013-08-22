## log4j.xml ##
The log4j.xml file is used to configure logging output for [log4j](http://logging.apache.org/log4j/1.2/).  Log4J is used by many libraries, including the Remedy API.

Basics

Every xml file follows a set of standards.  Every element has an opening and a closing tag.

    <configuration>something goes in there</configuration>

Optionally, elements can have attributes that define the element more specifically.  Attributes are specified within the opening tag following a simple name=value convention.


    <appender name="STDOUT">something here</appender>

The basic format of log4j.xml is as follows:

    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">
    <!--^^^ DOCTYPE and xml declaration come first in the file -->
  
    <!-- Create the log4j configuration -->
    <log4j:configuration xmlns:log4j="http://jakarta.apache.org/log4j/">
  
    <!-- An appender will log either to the console or to a file -->
    <!--  Multiple file appenders can be specified -->
      <appender name="console" class="org.apache.log4j.ConsoleAppender"> 
        <param name="Target" value="System.out"/>
          <layout class="org.apache.log4j.PatternLayout"> 
              <param name="ConversionPattern" value="%d{HH:mm:ss.SSS} [%thread] %-5p %C - %m%n" />      
          </layout> 
        </appender>   
  
    <!--Any class specific log configuration -->
      <logger name="my.class.name"><level value="off|all|debug|fatal|info|trace"></level></logger>
  
    <!--The default log level and appender -->
      <root> 
          <priority value="trace"></priority> 
          <appender-ref ref="console" /> 
          </root>  
    <!--Close the configuration element-->
    </log4j:configuration>

### Where To Send Log Output ###
Multiple outputs can be specified by simply including more than one appender element under a given configuration.

####Logging to File####

In order to log to file, you need to specify a File Appender.

    <appender name="default.file" class="org.apache.log4j.FileAppender">
        <param name="file" value="/path/to/log/mylogfile.log" />
        <param name="append" value="false" />
        <param name="threshold" value="debug" />
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%d{ISO8601} %-5p [%c{1}] - %m%n" />
        </layout>
    </appender>

####Logging to STDOUT####

In order to send log data to STDOUT, you need to specify a Console Appender.

    <appender name="console" class="org.apache.log4j.ConsoleAppender"> 
      <param name="Target" value="System.out"/>
        <layout class="org.apache.log4j.PatternLayout"> 
            <param name="ConversionPattern" value="%d{HH:mm:ss.SSS} [%thread] %-5p %C - %m%n" />      
        </layout> 
      </appender>   

### Output patterns ###

Reference for log layout patterns is available in the [Log4j JavaDoc](http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/PatternLayout.html). 

### Loglevels ###
  
-     all - all information
-     trace - includes trace, debug, info, warning, error, fatal
-     debug - includes debug, info, warning, error, fatal
-     info - includes info, warning, error, and fatal
-     warn - includes warning, error, and fatal
-     error - includes error and fatal
-     fatal - includes fatal only
-     off - no messages

### Per Class Configuration ###

the logger name is usually the class name.  Options for levels are: TRACE, DEBUG, INFO, WARN, ERROR, ALL or OFF.  Levels are case insensitive

    <logger name="com.mycompany.LogsLikeCrazy"  level="OFF"/>
    <logger name="com.mycompany.NeedsDebugging" level="DEBUG"/>
    <logger name="com.mycompany.JustErrors"   level="ERROR"/>


