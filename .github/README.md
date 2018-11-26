
# Welcome to the Enterprise Validator

[![Build Status](http://varkeys-rhel-jenkins.westus.cloudapp.azure.com:8080/buildStatus/icon?job=pipeline-job)](http://varkeys-rhel-jenkins.westus.cloudapp.azure.com:8080/job/pipeline-job/)
[![Gitter chat](https://badges.gitter.im/gitterHQ/gitter.png)](https://gitter.im/enterprise-validator/Lobby)
[![GitHub issues](https://img.shields.io/github/issues/anandvarkeyphilips/enterprise-validator.svg)](https://github.com/anandvarkeyphilips/enterprise-validator/issues)
[![GitHub forks](https://img.shields.io/github/forks/anandvarkeyphilips/enterprise-validator.svg)](https://github.com/anandvarkeyphilips/enterprise-validator/network)
![GitHub release](https://img.shields.io/github/release/anandvarkeyphilips/enterprise-validator.svg)

### Getting Started

YAML validator is better than http://www.yamllint.com.
YAML Validator follows YAML spec, support multiple doc feature which is not supported my YAML Lint.
JSON validator and formatter follows the JSON spec at http://json.org/.
Also, now you dont have to use https://www.base64decode.org/,
This app also supports Decode from and to Base64 format.

Here's the working version of the Application hosted in Microsoft Azure Cloud:<br />
**http://varkeys-rhel-jenkins.westus.cloudapp.azure.com/enterprise-validator/editor**

![Alt text](README-IMAGES/enterprise-validator.png?raw=true "Enterprise Validator Suite")

### Recipe Ingredients
 * spring-boot-starter-web, spring-boot-starter-freemarker, spring-boot-starter-actuator
 * springfox-swagger2, springfox-swagger-ui
 * org.json,snakeyaml, lombok, jackson-databind, spring-boot-test


### How to?
* Click on "BASE 64Encode" to encode to Base64 format.
* Click on "BASE 64Encode" to decode from Base64 format.
* Click on "Validate YAML" to validate YAML data.
* Click on "Validate JSON" to validate JSON data.
* Click on "Format YAML" to format JSON data.
* Click on "Share Data" to share the data through configured Mailing App.


### Jira
* [Epic](https://jira.global.atlassian.com/browse/<JIRA-ID>) - Work in Progress

### For help and support,
Please contact: [Join the Gitter Chat](https://gitter.im/enterprise-validator/Lobby/)

### Change Log
 
 * You can view the changes in each releases [here](https://github.com/anandvarkeyphilips/enterprise-validator/releases).
 
### Want to Contribute?
  * We love participation. We are ready to listen and discuss about changing underlying technologies or anything..
    You can check the document [here](CONTRIBUTING.md).