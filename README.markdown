Certificate Authority Service BAsed on HTTP
===========================================

The CASBaH project aims at creating a web service for browsing, issuing and revoking SSL
certificates. 

### To compile the code ###

To build the CASBaH you'll need the following:

* a copy of the source code, either from a distribution tarball or from the git repository
* a Java 5 compliant compiler
* Maven 2.0.10 or above

From the `casbah-pom` directory

`mvn clean install` will build all the project submodules a create a WAR file ready for deployment.
The war file can be found either from your local maven repository or from the `target` directory of
the `casbah-ui` sub-module.

### To use CASBaH ###

To run CASBah you'll simply need a Java Servlet Container (tomcat, jetty) up and running on
your machine. Just deploy the war to the container in any way you prefer and you should be up and
running.  
