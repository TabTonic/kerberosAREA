## Default Plugins ##

The following list of maven plugins is installed by default.

* Reporting
	* [maven-project-info-reports-plugin](http://maven.apache.org/plugins/maven-project-info-reports-plugin/)
		* Generates basic project reports
		* mailing list report disabled
		* dependency reports disabled because they slow down the build process 
	* [maven-checkstyle-plugin](http://maven.apache.org/plugins/maven-checkstyle-plugin/)
		* Code style checking
		* Configured via src/build/conf/checkStyle.xml
		* [Project Site at Sourceforge](http://checkstyle.sourceforge.net/)
	* [maven-surefire-report-plugin](http://maven.apache.org/surefire/maven-surefire-report-plugin)	
		* Generates reports from automated tests
	*  [maven-javadoc-plugin](http://maven.apache.org/plugins/maven-javadoc-plugin/)
		* Generates javadoc pages for site  
	*  [maven-jxr-plugin](http://maven.apache.org/jxr/maven-jxr-plugin/)
		* Generates referencable line-number enabled pages for referencing code  
	*  [maven-pmd-plugin](http://maven.apache.org/plugins/maven-pmd-plugin/)
		*  PMD is a source code analyzer. It finds common programming flaws like unused variables, empty catch blocks, unnecessary object creation, and so forth.
		* [At sourceforge](http://pmd.sourceforge.net/) 
	*  [maven-changelog-plugin](http://maven.apache.org/plugins/maven-changelog-plugin/)
		* Generates changelog from source code management repository
	* [findbugs-maven-plugin](http://mojo.codehaus.org/findbugs-maven-plugin)  
		* code analysis
		* [At sourceforge](http://findbugs.sourceforge.net/factSheet.html) 
* Build
	* [maven-compiler-plugin](http://maven.apache.org/plugins/maven-compiler-plugin/)
		* necessary to levarage a java version other than 1.5
		* configured to 1.6  
	* [maven-site-plugin](http://maven.apache.org/plugins/maven-site-plugin/)
		* for generating this site
	* [buildnumber-maven-plugin](http://mojo.codehaus.org/buildnumber-maven-plugin)
		* for auto-incrementing build numbers
	* [build-helper-maven-plugin](http://mojo.codehaus.org/build-helper-maven-plugin) 
		* brings in version variables that can be used elsewhere in the build process
	* [maven-jar-plugin](http://maven.apache.org/plugins/maven-jar-plugin/)
		* for generating the jar file and customizing the output
	* [maven-shade-plugin](http://maven.apache.org/plugins/maven-shade-plugin/)
		* generates the uber-jar including the dependencies
	* [maven-release-plugin](http://maven.apache.org/maven-release/maven-release-plugin/)
		* manages version numbers and tags SCM releases      