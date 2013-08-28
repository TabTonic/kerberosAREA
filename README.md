kerberosAREA
============

This is an AREA plugin for authenticating [Remedy Action Request System](http://www.bmc.com/products/product-listing/remedy-action-request-system.html) logins against a kerberos environment.

Documentation can be found at the [kerberosAREA project page](http://tabtonic.github.io/kerberosAREA/).

AREA (short for AR External Authentication) is a proprietary standard for delegating end user Authentication to an external resource.  General documentation surrounding setting up AREA authentication within a Remedy Action Request System environment can be found at [BMC's support website](http://www.bmc.com/support), under the Remedy product documentation.  While we do provide installation documentation, it is still important to reference the official BMC documentation, and preferably talk with an AR System expert about AREA authentication configuration.  

[BMC Communities](http://communities.bmc.com) and the [ARS List](http://www.rbugs.com/cgi-bin/wa.exe?SUBED1=ARSLIST&A=1) ([searchable archive](http://www.mail-archive.com/arslist@arslist.org/)) are strong resources for community based expertise on Remedy related issues.  Support for this plugin is available through [TabTonic Support](http://www.tabtonic.com/support/) and through the [GitHub issues tracker](https://github.com/TabTonic/kerberosAREA/issues).

## INSTALLATION ##

1) Build project with mvn package or download release from: https://bintray.com/tabtonic/kerberosAREA/Releases

2) Unzip contents into installation directory.

3) Edit kerberosAREA.config, log4j_pluginsvr.xml, and pluginsvr_config.xml appropriately for your environment.

4) Merge log4j_pluginsvr.xml with the log4j_pluginsvr.xml in the java plugin server installation directory.

5) Merge pluginsvr_config.xml with the pluginsvr_config.xml file in the java plugin server installation directory.

* Note - you may want to comment out existing AREA plugins

6) Configure remedy to use AREA

* use External Authentication Server RPC Program Number: 390695
* Authentication Chaining Mode is typically AREA - ARS to use ARS authentication as a fallback