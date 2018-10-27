
# Welcome to Enterprise YAML Validator

[comment]: # (REPLACE ME: Add Brief description of your service)

Latest version as of 27-10-2018 is 2.0.1.RELEASE

### Getting Started

[comment]: # (REPLACE ME: Add a Getting Started guide)

Enterprise YAML validator is better than [http://www.yamllint.com] http://www.yamllint.com
Enterprise YAML Validator follows YAML spec, support multiple doc feature which is not supported my YAML Lint.
Also, now you dont have to use https://www.base64decode.org/,
This app also supports Decode from and to Base64 format.

Here's the working version of the Application hosted in Microsoft Azure Cloud:
[http://varkeys-rhel-jenkins.westus.cloudapp.azure.com:8090/validator/forEditor] (http://varkeys-rhel-jenkins.westus.cloudapp.azure.com:8090/validator/forEditor)

![Alt text](enterprise-yaml-validator-image.PNG?raw=true "Enterprise YAML Validator")

### Recipe Ingredients
 * spring-boot-starter-web
 * spring-boot-starter-freemarker
 * springfox-swagger2, springfox-swagger-ui
 * snakeyaml, lombok, jackson-databind, spring-boot-test
[comment]: # (REPLACE ME: Add Initial requirements like JDK version, IDE, etc)

### How to?
[comment]: # (REPLACE ME: Add your confluence page in below format)
Click on "BASE 64Encode" to encode to Base64 format.
Click on "BASE 64Encode" to decode from Base64 format.
Click on "Validate YAML" to validate YAML data.
Click on "Share YAML" to share the YAML data through Outlook Mail.


### Jira
[comment]: # (REPLACE ME: Add your Jira EPIC page in below format)
* [Epic](https://jira.global.atlassian.com/browse/<JIRA-ID>) - Work in Progress

### For help and support,
Please contact: [Anand Varkey Philips] (anandvarkeyphilips@gmail.com) (Anand Varkey Philips)

### Change Log
[comment]: # (REPLACE ME: Add the changelog in below format)

* Version 2.*-RELEASE

> Released *[27/10/2018]* : 2.0.0.RELEASE
>
> Changes includes the following:
>
> 1. Added front end-web page for Enterprise YAML Validator.
> 2. Made it a complete standalone microservie app.

* Version 1.*-RELEASE

> Released *[08/10/2018]* : 1.0.0.RELEASE
>
> Changes includes the following:
>
> 1. Initial Commit with only YAML validation service.
> 2. Enabled Jenkins setup and one touch deployment.