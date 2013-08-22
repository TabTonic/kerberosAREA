## logback.xml ##
The logback.xml file is used to configure logging output for [logback-classic](http://logback.qos.ch/).  Logback-classic is one of the potential outputs for [slf4j](http://www.slf4j.org/).

Basics

Every xml file follows a set of standards.  Every element has an opening and a closing tag.

	<configuration>something goes in there</configuration>

Optionally, elements can have attributes that define the element more specifically.  Attributes are specified within the opening tag following a simple name=value convention.

	<appender name="STDOUT">something here</appender>

The basic format of logback.xml is as follows:

	configuration
	|
	--appender name=...  class=...
	|	|
	|	--encoder
	|		|
	|		--pattern
	|
	--root level=(trace,info,warn,debug)
	|	|
	|	--appender-ref ref=...
	|
	--logger name=... level=(TRACE,INFO,WARN,ERROR)

### Where To Send Log Output ###
Multiple outputs can be specified by simply including more than one appender element under a given configuration.

Logging to File

	<configuration>
		<appender name="FILE" class="ch.qos.logback.core.FileAppender">
		<file>LogFile.log</file>
		<encoder>
			<pattern>%date %level [%thread] %logger{10} [%file:%line] %msg%n</pattern>
		</encoder>
	</configuration>

Logging to STDOUT

	<configuration>
		<appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
		<encoder>
			<pattern>%date %level [%thread] %logger{10} [%file:%line] %msg%n</pattern>
		</encoder>
	</configuration>

### Output patterns ###
Output patterns are laid out in detail [here](http://logback.qos.ch/manual/layouts.html).
 
	%logger{10} 	mainPackage.sub.sample.Bar 	m.s.s.Bar
	%date|%d		date, optionally formatted {dd MMM YYYY ; HH:mm:ss.SSS}
	%level|le|p		the level of the output
	%thread			the name of the thread generating the output
	%file			the nane of the java source file generating the output
	%line			the line of the java source
	%msg			the message itself
	%n				platform independent line separator ("\n","\r\n")

### Loglevels ###
	
	trace
	debug
	info
	warn
	error
	fatal

### Per Class Configuration ###

the logger name is usually the class name.  Options for levels are: TRACE, DEBUG, INFO, WARN, ERROR, ALL or OFF.  Levels are case insensitive

	<logger name="com.mycompany.LogsLikeCrazy"  level="OFF"/>
	<logger name="com.mycompany.NeedsDebugging" level="DEBUG"/>
	<logger name="com.mycompany.JustErrors"  	level="ERROR"/>


