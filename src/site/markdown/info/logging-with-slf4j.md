## A little bit about SLF4J ##

The Simple Logging Facade for Java (SLF4J) serves as a simple facade or abstraction for various logging frameworks (e.g. java.util.logging, logback, log4j) allowing the end user to plug in the desired logging framework at deployment time. 

* [The SLF4J Website](http://www.slf4j.org/)
* [The SLF4J Manual](http://www.slf4j.org/manual.html)

Here is a basic usage scenario

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

    public class HelloWorld {
      final Logger logger = LoggerFactory.getLogger(HelloWorld.class);

      public static void main(String[] args) {
        logger.info("Hello World");
      }
    }

You can call logger.info, .error, .fatal, .warn etc.

When adding variables to logging entries:

    logger.debug("Temperature set to {}. Old temperature was {}.", t, oldT);

SLF4J will only build the string in this case when debugging is turned on.