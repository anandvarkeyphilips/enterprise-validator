
# Welcome to the Enterprise Validator

![GitHub release](https://img.shields.io/github/release/anandvarkeyphilips/enterprise-validator.svg)
[![Gitter chat](https://badges.gitter.im/gitterHQ/gitter.png)](https://gitter.im/enterprise-validator/Lobby)
[![GitHub issues](https://img.shields.io/github/issues/anandvarkeyphilips/enterprise-validator.svg)](https://github.com/anandvarkeyphilips/enterprise-validator/issues)
[![GitHub forks](https://img.shields.io/github/forks/anandvarkeyphilips/enterprise-validator.svg)](https://github.com/anandvarkeyphilips/enterprise-validator/network)
[![Build Status](http://varkeys-rhel-jenkins.westus.cloudapp.azure.com:8080/buildStatus/icon?job=pipeline-job)](http://varkeys-rhel-jenkins.westus.cloudapp.azure.com:8080/job/pipeline-job/)

### Getting Started

This Spring Boot Fully executable stand alone application is made for enterprises which cannot access/use public
websites for data validation and manipulation. This app provides,

YAML validator which is better than http://www.yamllint.com as this app follows YAML spec, support multiple doc feature
which is not supported my YAML Lint. JSON validator and formatter follows the JSON spec at http://json.org/. Also, now 
you dont have to use https://www.base64decode.org/ as this app also provides options for Decode from and to Base64 format.

![Alt text](https://github.com/anandvarkeyphilips/enterprise-validator/blob/master/.github/README-IMAGES/enterprise-validator.png?raw=true?raw=true)

Here's the Docker container version of the Application hosted in Microsoft Azure Cloud:  
**http://enterprise-validator.centralus.azurecontainer.io:8090/enterprise-validator/editor**  
Here's the fatty jar Application running in Microsoft Azure RHEL VM(May Have Issues):  
**http://varkeys-rhel-jenkins.westus.cloudapp.azure.com/enterprise-validator/editor**  
Here's the Openshift version of the Application hosted in Redhat Openshift(May Have Issues):  
**http://enterprise-validator-enterprise-validator.7e14.starter-us-west-2.openshiftapps.com/editor**  

![Alt text](https://github.com/anandvarkeyphilips/enterprise-validator/blob/master/.github/README-IMAGES/enterprise-validator.png?raw=true?raw=true "Enterprise Validator Suite")

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
