# MAGDA Mock

MAGDA Mock is a project by Digitaal Vlaanderen's MAGDA Platform to offer a mock environment for customers of its SOAP and REST services,
without real personally identifiable information and with stable and consistent test cases across services.

The MAGDA Mock is available as a Docker image and as a library.
The project also offers a REST/SOAP and Mock connector which can be used to connect to MAGDA Acceptance and Production environments after testing with the mock.

> **Warning**
> The MAGDA Mock is currently a preview release, it is provided as-is and without warranty or dedicated support.


## Table of Contents

- [Modules](#Modules)
- [Prerequisites](#prerequisites)
- [Endpoints](#endpoints)
- [Test data](#test-data)
- [Who do I talk to?](#who-do-i-talk-to)
- [Note on language](#note-on-language)


## Modules

The magdamock project has 3 Modules, each of them with their own purpose.

### magdamock-core

The core of the magdamock library, providing a Java interface to the mock engine.

To use this java interface, include the maven dependency:

```xml
<dependency>
    <groupId>be.vlaanderen.vip</groupId>
    <artifactId>magdamock-core</artifactId>
    <version>XXX</version>
    <scope>test</scope>
</dependency>
```

And then it's possible to include the MagdaMockConnection in another project.

```java
MagdaConnection connection = MagdaMockConnection.create(
    "path/to/rest/mockdata",
    "path/to/soap/mockdata",
    "path/to/xsd/data"
);
```


### magdamock-spring

Spring Boot wrapper around the core, provides a runnable standalone server and Docker image to build upon.

#### Configuration

All properties live under the `magda.magdamock` prefix in `application.yml` or `application.properties`.

The following properties are supported:

| Property                 | Default  | Description                                                              |
|--------------------------|----------|--------------------------------------------------------------------------|
| `rest-test-path`         | _(none)_ | Path to the REST mockdata root directory (absolute path recommended)     |
| `soap-test-path`         | _(none)_ | Path to the SOAP XML mockdata root directory (absolute path recommended) |
| `magda-xsd-path`         | _(none)_ | Path to the XSD directory; leave blank to disable schema validation      |
| `enable-timeout`         | `false`  | Whether to enable a random artificial timeout                            |
| `minimum-timeout-millis` | _(none)_ | Timeout configuration; the minimum milliseconds of timeout               |
| `maximum-timeout-millis` | _(none)_ | Timeout configuration; the maximum milliseconds of timeout               |


#### Magdamock with custom data

It is required to create a magdamock with custom data, 

To create a magdamock with custom data the following prerequisites are assumed:
- there is a directory `rest` with the REST testdata
- there is a directory `soap` with the SOAP testdata

Then the steps are:
- create a custom Dockerfile with accompanying `application.yml`, that copies aforementioned data
- build and run the Dockerfile that was just created

Dockerfile:
```dockerfile
FROM ghcr.io/informatievlaanderen/magda-mock-v2:x.y.z

COPY ./application.yml application.yml
COPY ./rest           /data/rest
COPY ./soap           /data/soap

EXPOSE 8080
EXPOSE 8081
```

With the accompanying `application.yml`:
```yaml
spring:
  application:
    name: custom-magdamock

magda:
  magdamock:
    rest-test-path: "/data/rest"
    soap-test-path: "/data/soap"
    magda-xsd-path: "/data/magda.xsd" # note, these files are provided in the magdamock base image
```

Build and run the magdamock docker image
```bash
# in the folder containing Dockerfile, application.yml and the rest and soap folders
docker build -t custom-magdamock .
docker run -p 8080:8080 custom-magdamock
```

### magdamock-demo

Example showing how to use the Docker image with custom test data.
A broader demo, with the same concept and containing more data can be found in https://github.com/Informatievlaanderen/MAGDA-Mock-Community-v2 .


## Prerequisites

| Requirement | Version |
|-------------|---------|
| Java        | 25      |
| Maven       | 3.9+    |
| Docker      | 20+     |


## Endpoints

There are 2 endpoints, one for SOAP (`/soap`) and one for REST (`/rest`).
The SOAP service to be mocked is matched based on the envelope fields `//Naam` and `//Versie`.
The REST service to be mocked is matched based on the path/method combination.

## Test data

For the format of test data, see https://vlaamseoverheid.atlassian.net/wiki/spaces/MG/pages/8652521541/MAGDA+Mock+-+Mockdata .
For the expected location of test data, see https://vlaamseoverheid.atlassian.net/wiki/spaces/MG/pages/8567914962/MAGDA+Mock+-+Overzicht+diensten .

## Who do I talk to?

- **For external users:** Raise a technical issue (bugs, inconsistencies in test cases) via the project's
  issue tracker on Github.
- **For internal users:** Contact the MAGDA Mock team.

## Note on language

Given the technical nature of the project, English is used wherever possible. However, as this project
originated within the Flemish government, Dutch-language comments, names and documentation are still
present in places and will be replaced with English equivalents over time.

The exception is when a word is a **reference** to a term that appears verbatim in an XML document or
domain concept without a natural English equivalent (e.g. `Uitzondering`, `hoedanigheidscode`,
`Antwoord`). Where such terms appear, they are treated as proper names and are not translated.
