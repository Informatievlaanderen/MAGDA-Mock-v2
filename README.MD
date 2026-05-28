<h1 align="center">MAGDA Mock</h1>

MAGDA Mock is a project by Digitaal Vlaanderen's MAGDA Platform to offer a mock environment for customers of its SOAP and REST services,
without real personally identifiable information and with stable and consistent test cases across services.

The MAGDA Mock is available as a Docker image and as a library, and the project also offers a REST/SOAP and Mock connector which
can be used to connect to MAGDA Acceptance and Production environments after testing with the mock.

> **Warning**
> The MAGDA Mock is currently a preview release, it is provided as-is and without warranty or dedicated support.

---

## Table of Contents

- [Repository structure](#repository-structure)
- [Prerequisites](#prerequisites)
- [Quick start with Docker](#quick-start-with-docker)
- [Using as a Java library](#using-as-a-java-library)
- [Configuration reference](#configuration-reference)
- [Endpoints](#endpoints)
- [Test data](#test-data)
- [Adding test cases](#adding-test-cases)
- [Supported services](#supported-services)
- [Adding a service](#adding-a-service)
- [Building from source](#building-from-source)
- [Who do I talk to?](#who-do-i-talk-to)
- [Note on language](#note-on-language)

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

| Path                            | Method | Content-Type                  |
|---------------------------------|--------|-------------------------------|
| `/Magda-02.00/soap/WebService`  | POST   | `text/xml` / `application/xml`|

Internally the request is routed using XPath matching on two envelope fields:

- `//Naam` — the service name (e.g. `GeefPersoon`)
- `//Versie` — the service version (e.g. `02.02.0000`)

Plus one or more key fields (e.g. `//INSZ`) whose values drive the file lookup.  
Each service/version combination and its keys are registered in `SoapStubRegistrar.SoapStubDefinitions`.

### REST

| Path                  | Methods                              | Content-Type       |
|-----------------------|--------------------------------------|--------------------|
| `/Magda-v1/rest/**`   | DELETE, GET, PATCH, POST, PUT        | `application/json` |

REST responses are served from WireMock `mappings/` + `__files/` files under the configured `magda-mock-test-path`.

### Health / Actuator

```
GET http://localhost:8081/actuator/health
```

---

## Test data

### Test numbers

To avoid conflicts with real citizen identifiers, the MAGDA Mock uses **Test numbers** for test cases
relating to personal information of citizens (i.e. wherever an INSZ-number is required).

Test numbers have the same 11-digit format as a regular INSZ-number (`^[0-9]{11}$`) and pass the
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
            ├── notfound.xml        # fallback — returned when no specific key file matches (cannot be present together with success.xml)
            └── success.xml         # fallback — returned for write/action services (cannot be present together with notfound.xml)
```

Example (single-key service, keyed by INSZ):

```
soap/
└── Persoon/
    └── GeefPersoon/
        └── 02.02.0000/
            ├── 00600099536.xml   ← returned when INSZ = 00600099536
            └── notfound.xml      ← returned for any other INSZ
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

REST responses follow the standard [WireMock file structure](https://wiremock.org/docs/stubbing/):

```
<magda-mock-test-path>/
├── mappings/               # JSON stub definitions (URL + response metadata)
│   └── {service}/
│       └── {key}.json
└── __files/                # Response body files referenced from mappings
    └── {service}/
        └── {key}.json
```

Example for a REST `GET` with a query parameter:

**`mappings/mobility/registrations/1ABC123.json`**
```json
{
  "request": {
    "method": "GET",
    "urlPath": "/v1/mobility/registrations",
    "queryParameters": {
      "plateNr": { "equalTo": "1ABC123" }
    }
  },
  "response": {
    "status": 200,
    "headers": { "Content-Type": "application/json" },
    "bodyFileName": "mobility/registrations/1ABC123.json"
  }
}
```

The `bodyFileName` is resolved relative to `__files/`.

---

## Adding test cases

### Adding a SOAP service

#### Step 1 - Identify the service definition

Open `SoapStubRegistrar.SoapStubDefinitions` in `magdamock-core` and find the entry for the service you want to add data for.
The entry tells you which keys (XPath expressions) are used to look up the fixture file. For example:

```java
subDir("GeefPersoon", VERSION_02_02, KEY_INSZ)
// → key resolved from xpath //INSZ
// → file path: {Domain}/GeefPersoon/02.02.0000/{insz-value}.xml
```

#### Step 2 — Create the XML fixture files

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
The following fields in the response are **automatically overwritten** by `BasicSoapResponsePatcher` at call time, 
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
> requested `Inkomensjaar` from the request). See `SoapResponsePatcherImpl` for the full list.

WireMock [Handlebars templating](https://wiremock.org/docs/response-templating/) is also supported in 
XML fixture files. The `Date` header (derived from the request timestamp) is available as `request.headers.Date`.
See the demo fixture `GeefPersoon/02.02.0000/00600099734.xml` for an example using `dateMath`.

#### Step 3 — Add a "not found" or "success" fallback (optional but recommended)

If no specific file matches, the mock returns a 404 to the caller. If instead you want to return a proper
MAGDA "not found" response, place a file named `notfound.xml` in the version directory:

```
<soap-test-path>/{Domain}/{ServiceName}/{Version}/notfound.xml
```

Similarly, one can add a file ´success.xml´ instead of ´notfound.xml´ for services where the positive response has no specific data payload.

This file is registered at **lower priority** than key-specific files, so it only fires when nothing else matches.

#### Step 4 — Register the case in soap-services.json (for automated testing)

Test cases in the built-in dataset used during testing, are defined in:

```
magdamock-core/src/test/resources/soap/soap-services.json
```

Each entry specifies a request scenario and an expected XPath assertion on the response:

```json
{
  "naam": "GeefPersoon",
  "versie": "02.02.0000",
  "requestFields": [
    { "path": "INSZ", "value": "00600099536" }
  ],
  "xpathExpression": "//Gemeente/Naam",
  "expectedValue": "Kortenberg"
}
```

The `requestFields` entries are injected into the request envelope at the given relative XPath `path`.

#### Step 5 - Adding the new service/version to the registry

```java
// Single key (INSZ)
subDir("GeefIetsNieuwService", VERSION_02_00, KEY_INSZ),

// Multiple keys
subDir("ZoekIetsNieuwService", VERSION_02_00, "//Criteria/Veld1", "//Criteria/Veld2"),
```

Then follow steps 1–4 above to add the fixture files.

### Adding a REST test case

#### Step 1 - Create the WireMock mapping

Create a JSON stub mapping in `<magda-mock-test-path>/mappings/`:

```json
{
  "request": {
    "method": "GET",
    "urlPath": "/Magda-v1/rest/my-service/resource",
    "queryParameters": {
      "id": { "equalTo": "12345" }
    }
  },
  "response": {
    "status": 200,
    "headers": { "Content-Type": "application/json" },
    "bodyFileName": "my-service/resource/12345.json"
  }
}
```

All standard [WireMock matching strategies](https://wiremock.org/docs/request-matching/) are supported
(URL patterns, headers, body patterns, query parameters, etc.).

#### Step 2 - Create the response body file

Place the JSON response body at the path specified in `bodyFileName`, resolved relative to `__files/`:

```
<magda-mock-test-path>/__files/my-service/resource/12345.json
```

#### Step 3 - Add a mapping for the "unknown" case (optional)

Add a lower-priority mapping (add `"priority": 10` to match-all, vs the default `5`) that returns a 404
or an appropriate error body for requests that don't match any known identifier.

---

### GeefPasfoto - special SOAP case

`GeefPasfoto` does not use a key-based filename lookup. Instead, the INSZ from the request is used to
determine a binary gender (odd sequence number = male), and a passport photo XML is selected pseudo-randomly
from the corresponding gender subdirectory:

```
<soap-test-path>/Persoon/GeefPasfoto/02.00.0000/
├── {insz}.xml          ← if present, returned directly for that specific INSZ
├── mannen/             ← pool of passport photos for male INSZs
│   └── *.xml
├── vrouwen/            ← pool of passport photos for female INSZs
│   └── *.xml
└── notfound.xml
```

The selection is deterministic per INSZ (uses `random.nextInt(fileCount)` seeded from birth date), so the
same INSZ always gets the same photo within a single server run.  
The passport photo XML contains base64-encoded image data in the `//Pasfoto` element — it is not an actual
image file.

---

## Supported services

The following MAGDA service domains are included in the built-in test dataset.
Key fields used for file lookup per service are listed in parentheses.

| Domain          | Service (version)                                           | Key(s)                                                             |
|-----------------|-------------------------------------------------------------|--------------------------------------------------------------------|
| **Persoon**     | GeefPersoon (02.02)                                         | `//INSZ`                                                           |
|                 | GeefHistoriekPersoon (02.00, 02.02)                         | `//INSZ`                                                           |
|                 | GeefPasfoto (02.00)                                         | `//INSZ` (gender-based pool, _special_)                           |
|                 | GeefGezinssamenstelling (02.00, 02.02)                      | `//INSZ`                                                           |
|                 | GeefHistoriekGezinssamenstelling (02.02)                    | `//INSZ`                                                           |
|                 | GeefAttest (02.00)                                          | `//INSZ`                                                           |
|                 | ZoekPersoonOpAdres (02.02)                                  | `//Bron` / `//Postcode` / `//Straatcode` / `//Huisnummer` / `//EnkelReferentiepersoon` |
|                 | ZoekPersoonOpNaam (02.02)                                   | `//Bron` / `//Achternaam` / `//Geboortedatum`                     |
|                 | GeefPersoonMutatiesNotificaties (02.00)                     | `//Inhoud/Ontvangstreferte`                                        |
|                 | RaadpleegLeerkredietsaldo (01.00)                           | `//Inhoud/Ontvangstreferte`                                        |
| **Gezin**       | GeefKindVoordelen (02.00)                                   | `//INSZ`                                                           |
| **Inkomen**     | GeefAanslagbiljetPersonenbelasting (02.00)                  | `//INSZ`                                                           |
| **Kadaster**    | GeefCadNetTransacties (01.00)                               | `//INSZ`                                                           |
|                 | GeefHistoriekEigendomstoestand (03.00)                      | `//EigendomstoestandId`                                            |
|                 | GeefEigendomstoestanden (02.00)                             | `//EigendomId`                                                     |
|                 | ZoekEigendomstoestanden (02.00)                             | `//INSZ`                                                           |
|                 | ZoekPerceel (02.00)                                         | `//KadastraleAfdeling` / `//Sectie` / `//Grondnummer`             |
|                 | ZoekVerkoopprijzen (03.00)                                  | `//Provincie` / `//Gemeente` / `//TypeInschrijving` / `//CodeAard` |
|                 | GeefTransacties (03.00)                                     | `//INSZ`                                                           |
| **Onderneming** | GeefOnderneming, GeefOndernemingVKBO (02.00)                | `//Ondernemingsnummer`                                             |
|                 | ZoekOnderneming (02.00)                                     | `//Basisgegevens` / `//Resultaten`                                |
|                 | GeefFuncties (02.00)                                        | `//INSZ`                                                           |
|                 | GeefJaarrekeningen (02.00)                                  | `//Ondernemingsnummer` / `//Boekjaar`                              |
|                 | GeefAdressenLocaties, GeefBeschikbareJaarrekeningen, GeefDeelnemingen, GeefFiscaleSchuld, GeefSocialeSchuld, GeefTewerkstelling, GeefPCenTW, GeefOndernemingSignalen (02.00) | `//Ondernemingsnummer`                                             |
|                 | GeefFiscaleInhoudingsplicht (02.01)                         | `//Ondernemingsnummer`                                             |
|                 | GeefKadastraleAfdelingenOpKBO (01.00)                       | `//Ondernemingsnummer`                                             |
| **Onderwijs**   | GeefHistoriekInschrijving (02.01)                           | `//INSZ`                                                           |
| **Repertorium** | RegistreerInschrijving (02.00)                              | `//INSZ`                                                           |
|                 | RegistreerInschrijving (02.01)                              | `//Subject/Type` / `//Subject/Sleutel`                             |
|                 | RegistreerUitschrijving (02.00)                             | `//INSZ`                                                           |
|                 | GeefPersoonMutatiesNotificaties (02.00)                     | `//Inhoud/Ontvangstreferte`                                        |
| **SocEcon**     | GeefStatusRechtOndersteuningen (02.00)                      | `//INSZ`                                                           |
| **SocZek**      | GeefBetalingenHandicap, GeefDossierHandicap (03.00)         | `//ssin`                                                           |
|                 | GeefVolledigDossierHandicap (03.00)                         | `//rrnr`                                                           |
|                 | GeefSociaalStatuut, GeefLeefloonbedragen (03.00 / 02.00)    | `//INSZ`                                                           |
| **LED**         | AnnuleerBewijs, GeefBewijs, RegistreerBewijs, RegistreerMutatieBewijs (02.00) | `//INSZ`                                                           |
|                 | GeefHistoriekMutatiedossier (03.00)                         | `//Dossiernummer`                                                  |
| **Vastgoed**    | GeefEpc (02.01)                                             | `//Postcode` / `//Straat` / `//Huisnummer`                        |
| **Werk**        | GeefDmfaVoorWerknemer (03.00)                               | `//INSZ`                                                           |
|                 | GeefLoopbaanARZA (02.01)                                    | `//INSZ`                                                           |
|                 | GeefLoopbaanonderbrekingen, GeefWerkrelaties (02.00)        | `//INSZ`                                                           |

Test INSZs and other request keys for all built-in cases are listed in
`magdamock-core/src/test/resources/soap/soap-services.json`.

All supported SOAP services should have a basic test case in the magdamock-core/src/test/resources/soap/soap-services.json file.

---

## Adding a service

This section describes how to add a **new service** (not only a new case for an existing service).

### Add a new SOAP service

1. **Register the service in code**

   Add a definition to `SoapStubRegistrar.SoapStubDefinitions.allDefinitions()` in:
   `magdamock-core/src/main/java/be/vlaanderen/vip/magda/magdamock/client/soap/SoapStubRegistrar.java`

   Use one of these patterns:

   - Single-key lookup:
     ```java
     subDir("GeefNieuweService", VERSION_02_00, KEY_INSZ),
     ```
   - Multi-key lookup:
     ```java
     subDir("ZoekNieuweService", VERSION_02_00, "//Criteria/Veld1", "//Criteria/Veld2"),
     ```
   - Special handler (only if needed, e.g. like `GeefPasfoto`):
     implement a `SoapStubHandler` and register it via a custom factory.

2. **Create fixture directories and responses**

   Under `magdamock-core/src/test/resources/soap`, add:

   ```text
   {Domain}/{ServiceName}/{Version}/
   ```

   Include at minimum:

   - one success response file keyed by request value(s)
   - `notfound.xml` fallback
   - `success.xml` fallback (for actions where the positive response has no specific data)

   The success fallback and the notfound fallback cannot be present together, as they are mutually exclusive.

3. **Add regression entries to `soap-services.json`**

   Update:
   `magdamock-core/src/test/resources/soap/soap-services.json`

   Add at least one success and one failure/not-found scenario for the new service/version.

4. **(Optional) Add response patching logic**

   If the service requires dynamic values copied from request to response, implement a new patcher and register it in:
   `magdamock-core/src/main/java/be/vlaanderen/vip/magda/magdamock/client/soap/SoapResponsePatcherImpl.java`

5. **Run tests/build locally**

   ```bash
   mvn clean install
   ```

### Add a new REST service

1. **Add WireMock mappings**

   Under `mappings/`, add request matchers for the endpoint/method/query/body.

2. **Add response bodies**

   Under `__files/`, add referenced JSON payloads and link them via `bodyFileName` in mapping files.

3. **Cover unknown/error paths**

   Add explicit mappings for unknown identifiers (typically 404) to keep behavior deterministic.

4. **Verify with a local run**

   ```bash
   mvn -pl magdamock-spring spring-boot:run -Dspring-boot.run.profiles=local
   ```

### Service contribution checklist

- [ ] Service registered (for SOAP) in `SoapStubRegistrar`
- [ ] Fixture files added in correct domain/service/version path
- [ ] `notfound.xml` included (SOAP) or unknown mapping included (REST)
- [ ] `soap-services.json` updated for SOAP regression coverage
- [ ] Local build/test executed successfully

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

- **For external users:** Raise a technical issue (bugs, inconsistencies in test cases) via the project's
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
