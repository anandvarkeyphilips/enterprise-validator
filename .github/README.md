# Enterprise Validator

![GitHub release](https://img.shields.io/github/release/anandvarkeyphilips/enterprise-validator.svg)
[![CI](https://github.com/anandvarkeyphilips/enterprise-validator/actions/workflows/ci.yml/badge.svg)](https://github.com/anandvarkeyphilips/enterprise-validator/actions/workflows/ci.yml)
[![GitHub issues](https://img.shields.io/github/issues/anandvarkeyphilips/enterprise-validator.svg)](https://github.com/anandvarkeyphilips/enterprise-validator/issues)
[![GitHub forks](https://img.shields.io/github/forks/anandvarkeyphilips/enterprise-validator.svg)](https://github.com/anandvarkeyphilips/enterprise-validator/network)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A self-contained Spring Boot application for enterprises that cannot use public websites for data
validation and manipulation — everything runs locally, no external services are called.

**Try it live:** https://enterprise-validator.azurewebsites.net/editor

![Enterprise Validator Suite](https://github.com/anandvarkeyphilips/enterprise-validator/blob/master/.github/README-IMAGES/enterprise-validator.png?raw=true)

## Features

* **YAML validation** following the YAML spec, including multi-document (`---`) support that
  http://www.yamllint.com does not offer — with line and column markers on the failing key.
* **JSON validation and formatting** per the JSON spec at http://json.org/.
* **XML formatting** with pretty printing.
* **Base64 encoding and decoding** — no need for https://www.base64decode.org/.
* **Share** the editor content by copying it to the clipboard or opening a pre-filled email.
* **OpenAPI / Swagger UI** documentation of every endpoint at `/swagger-ui.html`.

## How to use

* Click **Validate YAML** / **Validate JSON** to validate the editor content.
* Click **Format JSON** / **Format XML** to pretty print the editor content.
* Click **BASE64 Encode** / **BASE64 Decode** to convert the editor content.
* Click **Share Data** to copy the data to the clipboard or open it in your mail client.

## Under the hood

* Java 25 (LTS, Azul Zulu) — pinned via `.sdkmanrc` (`sdk env` selects it automatically)
* Spring Boot 3.5.16, Maven 3.9.16 via the wrapper (`./mvnw clean verify`)
* spring-boot-starter-web, spring-boot-starter-freemarker, spring-boot-starter-actuator
* springdoc-openapi, org.json, snakeyaml, lombok, jackson-databind
* CI/CD on GitHub Actions: build + coverage/mutation gates, Playwright end-to-end tests,
  OIDC deploy to Azure App Service (Java 25 runtime), releases published to Maven Central
* Docker base image: `azul/zulu-openjdk-alpine:25-jre`

## Develop locally

```bash
sdk env                       # select Zulu JDK 25 (or install: sdk install java 25.0.5.0+8-zulu)
./mvnw clean verify           # build, tests, JaCoCo + PIT mutation gates
./mvnw -DskipTests package && java -jar target/enterprise-validator-*.jar --server.port=8080
cd e2e && npx playwright test # browser end-to-end suite against the packaged jar
```

<details>
<summary>Deprecated deployments (no longer active, kept for reference)</summary>

Here's the Docker container version of the Application hosted in Microsoft Azure Container Instances (DEPRECATED — no longer active):
**http://enterprise-validator.centralus.azurecontainer.io:8090/enterprise-validator/editor**
Here's the fatty jar Application running in Microsoft Azure RHEL VM (DEPRECATED — no longer active):
**http://varkeys-rhel-jenkins.westus.cloudapp.azure.com/enterprise-validator/editor**
Here's the Openshift version of the Application hosted in Redhat Openshift (DEPRECATED — no longer active):
**http://enterprise-validator-enterprise-validator.7e14.starter-us-west-2.openshiftapps.com/editor**
</details>

## Report an issue

Found a bug or have an idea? Please [open a GitHub issue](https://github.com/anandvarkeyphilips/enterprise-validator/issues) —
include the input data and the validation message whenever possible.

## Help and support

Join the [Gitter chat](https://gitter.im/enterprise-validator/Lobby/).

## Change log

See the changes in each release [here](https://github.com/anandvarkeyphilips/enterprise-validator/releases).

## Want to contribute?

We love participation and are ready to discuss changing underlying technologies or anything else.
See [CONTRIBUTING.md](CONTRIBUTING.md).
