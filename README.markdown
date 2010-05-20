Certificate Authority Service BAsed on HTTP
===========================================

The CASBaH project aims at creating a web service for browsing, issuing and revoking SSL
certificates. 

### To compile the code ###

To build the CASBaH you'll need the following:

* a copy of the source code, either from a distribution tarball or from the git repository
* a Java 6 compliant compiler
* Maven 2.1.0 or above

From the `casbah-pom` directory

`mvn clean install` will build all the project submodules a create a WAR file ready for deployment.
The war file can be found either from your local maven repository or from the `target` directory of
the `casbah-ui` sub-module.

### To use CASBaH ###

#### As a Demo Server from WebStart ####

You can run the demo server as a [WebStart application](http://sandbox.harhaanjohtaja.com/casbah.jnlp).
The server will automatically create a directory called `.casbah` under your home directory
and autoconfigure itself. If you prefer to have the data somewhere else have an environment variable
called `CASBAH_HOME` point at your desired location.

You will still need:

* a Java Runtime Environment 6 or higher
* OpenSsl installed on your machine and available on the path.
* A reasonably new browser


#### As a Demo Server from Compiled Code ####

Alternatively, if you have built the code on your machine, you just run

`java -jar casbah-embedded-0.0.1-SNAPSHOT.jar`

from a terminal window/command prompt. The requirements are the same as above.

#### As a real server ####

To run CASBah you'll simply need a Java Servlet Container (tomcat, jetty) up and running on
your machine. Just deploy the casbah-ui.war to the container in any way you prefer and you should be up and
running. Again data will end up in the `.casbah` directory of the user under which the 
servlet container runs, if you prefer (and you should) explicitly specify the directory where
casbah stores its data, define the `CASBAH_HOME` environment variable.

