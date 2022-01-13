# dwp-jsa-jsaps-adapter
DWP NSJSA JSAPS Adapter

## About 

Microservice for converting requests to and from the JSAPS/PXP Format

## Pre-requisites

* Java 8
* Maven

### Installing

`mvn install`

## PublicKey

In application.properties, the services.publicKey needs to be populated with a good RSA key.
To create this, and set it, run ./createPublicKey.sh.  This is a one time operation.  Please take
care not to check this change in.



`java -jar target/jsaps-adapter-service-${version}.jar`

## Running the tests
 `mvn test -Dspring.profiles.active=local_test,mockredis`

# Dependencies

This service requires nsjsa-commons to build.
