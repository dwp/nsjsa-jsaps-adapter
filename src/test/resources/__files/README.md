# Wiremock

## Getting Started

### Prerequisites

* Wiremock standalone setup (directory with wiremock JAR)
* BASH shell
* Environment variable WIREMOCK_HOME (no trailing / on path)

### Running wiremock locally

Run the following command from the shell and from the WIREMOCK_HOME directory

```java -jar wiremock-standalone-2.19.0.jar --local-response-templating```

### Deploying wiremock services

The directories below this README contains all the available 
options for each mock endpoint. They are organised as follows:

* /service/endpoint/testVariation

To deploy an endpoint, start a BASH shell in this diretory and run the following command

```./deployWiremockService.sh```

This will lead you through the service, endpoint and variation to deploy to your local wiremock standalone install.

Once deployed, start the service as above.

