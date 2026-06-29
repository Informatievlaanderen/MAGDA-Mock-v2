<h1 align="center">MAGDA Mock</h1>

MAGDA Mock is a project by Digitaal Vlaanderen's MAGDA Platform to offer a mock environment for customers of its SOAP and REST services,
without real personally identifiable information and with stable and consistent test cases across services.

The MAGDA Mock is available as a Docker image and as a library.

> **Warning**
> The MAGDA Mock is currently a preview release, it is provided as-is and without warranty or dedicated support.

---

## Table of Contents

- [Introduction](#introduction)
- [Prerequisites](#prerequisites)
- [Quick start with Docker](#quick-start-with-docker)
- [Using as a Java library](#using-as-a-java-library)
- [Configuration reference](#configuration-reference)
- [Endpoints](#endpoints)
- [Test data](#test-data)
- [Adding test cases](#adding-test-cases)
- [Repository structure](#repository-structure)
- [Building from source](#building-from-source)
- [Who do I talk to?](#who-do-i-talk-to)
- [Note on language](#note-on-language)

---

## Introduction

This repository is the **technical foundation to run a MAGDA Mock instance**. It bundles the mock engine,
a runnable server, and the SOAP/REST endpoints needed to stand up a self-contained MAGDA Mock environment —
either as a Docker image or embedded as a Java library in your tests.

It does **not** ship a complete catalogue of test cases by itself: it provides the runtime, while the
service-specific test fixtures and querying behaviour are documented separately (see [Test data](#test-data)).

---

## Prerequisites

| Requirement | Version |
|-------------|---------|
| Java        | 25      |
| Maven       | 3.9+    |
| Docker      | 20+     |

---

## Quick start with Docker

The easiest way to run MAGDA Mock is with the published Docker image.

```bash
docker run -p 8080:8080 -p 8081:8081 \
  vlaamseoverheid-magda-docker-releases.jfrog.io/magda/magda.magdamock:latest
```

- **Port 8080** - SOAP and REST service endpoints
- **Port 8081** - Spring Boot Actuator / health endpoint

### Custom test data

The `magdamock-demo` directory shows how to extend the base image with your own fixture data:

```dockerfile
ARG MAGDAMOCK_VERSION="latest"
ARG DOCKER_URL="vlaamseoverheid-magda-docker-releases.jfrog.io"

FROM ${DOCKER_URL}/magda/magda.magdamock:${MAGDAMOCK_VERSION}

COPY ./application.yml application.yml
COPY ./wiremock-data  /data/wiremock
COPY ./soap           /data/soap

EXPOSE 8080
EXPOSE 8081
```

With the accompanying `application.yml`:

```yaml
magda:
  magdamock:
    magda-mock-test-path: "/data/wiremock"
    soap-test-path: "/data/soap"
```

---

## Using as a Java library

Add the `magdaconnector-test` SDK module to your project to use the mock directly in tests - no running server required.
This library module also allows you to set up mock scenarios in-flight using just a mock connection builder
where you set up requests and responses, without needing any real files.

**Maven dependency:**

```xml
<dependency>
    <groupId>be.vlaanderen.vip</groupId>
    <artifactId>magdamock-core</artifactId>
    <version>XXX</version>
    <scope>test</scope>
</dependency>
```

**Creating a `MagdaMockConnection`:**

```java
MagdaConnection connection = MagdaMockConnection.create(
    "src/test/resources/wiremock",
    "src/test/resources/soap",
    "src/main/resources/magda.xsd"
);
```

The `MagdaConnection` instance can then be injected wherever a real MAGDA connection would be used.

---

## Configuration reference

All properties live under the `magda.magdamock` prefix in `application.yml` or `application.properties`.

The following properties are supported:

| Property               | Default     | Description                                                          |
|------------------------|-------------|----------------------------------------------------------------------|
| `magda-mock-test-path` | `wiremock`  | Path to the WireMock data root directory (relative or absolute)      |
| `soap-test-path`       | _(none)_    | Path to the SOAP XML fixture root directory (absolutely recommended) |
| `magda-xsd-path`       | _(none)_    | Path to the XSD directory; leave blank to disable schema validation  |

The management / health endpoint runs on port **8081** by default (configurable via `management.server.port`).

---

## Endpoints

### SOAP

| Path    | Method | Content-Type                   |
|---------|--------|--------------------------------|
| `/soap` | POST   | `text/xml` / `application/xml` |

Internally the request is routed using XPath matching on two envelope fields:

- `//Naam` — the service name (e.g. `GeefPersoon`)
- `//Versie` — the service version (e.g. `02.02.0000`)

Plus one or more key fields (e.g. `//INSZ`) whose values drive the file lookup.  
Each service/version combination and its keys are registered in `SoapStubRegistrar.SoapStubDefinitions`.

### REST

| Path       | Methods                       | Content-Type       |
|------------|-------------------------------|--------------------|
| `/rest/**` | DELETE, GET, PATCH, POST, PUT | `application/json` |

REST responses are served from WireMock `mappings/` + `__files/` files under the configured `magda-mock-test-path`.

### Health / Actuator

```
GET http://localhost:8081/actuator/health
```

---

## Test data

> **Important**
> The **supported querying mechanism for every service**, as well as the **path where its test fixtures
> must be placed**, is documented per service on Confluence:
> [MAGDA Mock - Documentatie](https://vlaamseoverheid.atlassian.net/wiki/spaces/MG/pages/8574697520/MAGDA+Mock+-+Documentatie).

> **Tip**
> The [MAGDA Mock Community](https://github.com/Informatievlaanderen/MAGDA-Mock-Community-v2) repository
> provides a set of predefined test cases you can use as-is or layer on top of the Docker image
> (see the **Custom test data** section under **Quick start with Docker**).

### Test numbers

To avoid conflicts with real citizen identifiers, the MAGDA Mock uses **Test numbers** for test cases
relating to personal information of citizens (i.e. wherever an INSZ number is required).

Test numbers have the same 11-digit format as a regular INSZ number (`^[0-9]{11}$`) and pass the
[modulo-97 checksum](https://vlaamseoverheid.atlassian.net/wiki/spaces/MG/pages/6208324471/Validatiemethodes).  
The key difference: the third and fourth digits - which in a real national registry number encode the birth month
(range `01`–`12`) - are deliberately placed in the range **`61`–`72`**.  
This prevents any accidental extraction of a real date of birth and avoids collisions with genuine citizen identifiers.

### SOAP fixture structure

SOAP responses are stored as plain XML files. The directory layout follows the pattern:

```
<soap-test-path>/
└── {Domain}/
    └── {ServiceName}/
        └── {Version}/              # e.g. 02.02.0000
            ├── {key}.xml           # matched when the request key equals the filename (without .xml)
            └── default.xml         # fallback — returned when no specific key file matches
```

Example (single-key service, keyed by INSZ):

```
soap/
└── Persoon/
    └── GeefPersoon/
        └── 02.02.0000/
            ├── 00600099536.xml   ← returned when INSZ = 00600099536
            └── default.xml       ← returned for any other INSZ
```

For services with **multiple keys**, the file is nested in subdirectories - one per key value except the last,
which becomes the filename. For example, `GeefJaarrekeningen` is keyed on `//Ondernemingsnummer` and `//Boekjaar`:

```
soap/
└── Onderneming/
    └── GeefJaarrekeningen/
        └── 02.00.0000/
            └── 0200065765/       ← subdirectory for first key (ondernemingsnummer)
                └── 2018.xml      ← file for second key (boekjaar)
```

### REST fixture structure

REST responses are stored as JSON files. The directory layout follows the pattern:

```
<magda-mock-test-path>/
├── rest/
│   └── {Version}/{Service}/{HttpMethod}/
│       └── {key}.json
```

Example for a REST `GET` with a query parameter:

**`mobility/registrations/get/plateNr/1ABC123.json`**
```json
{
  "response": {
    "status": 200,
    "headers": {
      "Content-Type": "application/json"
    },
    "jsonBody": {
      "@context": "https://vlaamseoverheid.atlassian.net/wiki/download/attachments/1693680273/Mobility-context.jsonld",
      "self": "/registrations?plateNr=1ABC123",
      "items": [
        {
          "license": {
            "plateNr": "1ABC123",
            "plateUID": "6148372095",
            "plateType": {
              "code": "NM",
              "description": "Normaal"
            }
          }
        }
      ]
    }
  }
}
```

This sample response was truncated for brevity.

---

## Adding test cases

#### Step 1 — Create the XML fixture files

Place a valid MAGDA response XML file at:

```
<soap-test-path>/{Domain}/{ServiceName}/{Version}/{key-value}.xml
```

For a **multi-key** service, nest subdirectories for all keys except the last one:

```
<soap-test-path>/{Domain}/{ServiceName}/{Version}/{key1-value}/{key2-value}.xml
```

The XML must be a complete, well-formed MAGDA service response. This response should not include the
SOAP envelope — only the inner body content as returned by the service.
The following fields in the response are **automatically overwritten** at call time,
so their values in the file do not matter:

| Field (XPath in response)              | Value injected                              |
|----------------------------------------|---------------------------------------------|
| `//Ontvanger/Referte`                  | Copied from `//Afzender/Referte` in request |
| `//Antwoord/Referte`                   | Copied from `//Afzender/Referte` in request |
| `//Ontvanger/Identificatie`            | Copied from `//Afzender/Identificatie`      |
| `//Ontvanger/Hoedanigheid`             | Copied from `//Afzender/Hoedanigheid`       |
| `//Ontvanger/Gebruiker`                | Copied from `//Afzender/Gebruiker`          |
| `//Context/Bericht/Tijdstip/Datum`     | Current date                                |
| `//Context/Bericht/Tijdstip/Tijd`      | Current time                                |
| `//Afzender/Referte`                   | Fresh UUID                                  |
| `//Afzender/Identificatie`             | `kb.vlaanderen.be/aiv/magda-mock-server`    |
| `//Afzender/Naam`                      | `Magda Mock Server`                         |

> Some services have additional patching logic (e.g. `GeefAanslagbiljetPersonenbelasting` copies the
> requested `Inkomensjaar` from the request).

WireMock [Handlebars templating](https://wiremock.org/docs/response-templating/) is also supported in
XML fixture files. The `Date` header (derived from the request timestamp) is available as `request.headers.Date`.
See the demo fixture `GeefPersoon/02.02.0000/00600099734.xml` for an example using `dateMath`.

#### Step 2 — Add a default fallback (optional but recommended)

If no specific file matches, the mock returns a 404 to the caller. If instead you want to return a proper
MAGDA fallback response, place a file named `default.xml` in the version directory:

```
<soap-test-path>/{Domain}/{ServiceName}/{Version}/default.xml
```

This file is registered at **lower priority** than key-specific files, so it only fires when nothing else matches.

---

## Repository structure

```
magda.magdamock/
├── magdamock-core/        # Framework-agnostic mock engine (WireMock-based, published as a library)
├── magdamock-spring/      # Spring Boot wrapper — runnable standalone server and Docker image
└── magdamock-demo/        # Example showing how to extend the Docker image with custom test data
```

### magdamock-core

Contains the mock engine built on top of [WireMock](https://wiremock.org/).  
Key class: `MagdaMockConnection` — implements `MagdaConnection` and can be used directly in integration tests
without starting an HTTP server.  
SOAP responses are loaded from a configurable directory of XML fixture files.  
REST responses are loaded from standard WireMock `mappings/` + `__files/` directories.  
Optional XSD validation can be enabled for both SOAP requests and responses.

### magdamock-spring

Spring Boot application that wraps `magdamock-core` and exposes the MAGDA SOAP and REST endpoints over HTTP.  
Can be run as a fat-jar or packaged as a Docker image.

### magdamock-demo

Demonstrates how to layer custom test data on top of the published Docker image using a simple `Dockerfile`.

---

## Building from source

```bash
# Build all modules
mvn clean install

# Build and run the Spring Boot application locally
mvn -pl magdamock-spring spring-boot:run -Dspring-boot.run.profiles=local

# Build a fat-jar
mvn -pl magdamock-spring package -P fat-jar

# Build the Docker image (requires Docker)
mvn -pl magdamock-spring package -P docker

# Generate aggregated Javadoc
mvn javadoc:aggregate
```

The local Spring profile (`-Dspring-boot.run.profiles=local`) enables more verbose console logging
via `logback-dev.xml`.

---

## Who do I talk to?

- **For external users:** Raise a technical issue (bugs, ...) via the project's
  issue tracker on Github.
- **For internal users:** Contact the MAGDA Mock team.

---

## Note on language

Given the technical nature of the project, English is used wherever possible. However, as this project
originated within the Flemish government, Dutch-language comments, names and documentation are still
present in places and will be replaced with English equivalents over time.

The exception is when a word is a **reference** to a term that appears verbatim in an XML document or
domain concept without a natural English equivalent (e.g. `Uitzondering`, `hoedanigheidscode`,
`Antwoord`). Where such terms appear, they are treated as proper names and are not translated.
