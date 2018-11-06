
# Welcome to Enterprise Validator Suite

[comment]: # (REPLACE ME: Add Brief description of your service)

Latest version as of 27-10-2018 is 2.0.4.RELEASE

### Getting Started

[comment]: # (REPLACE ME: Add a Getting Started guide)

YAML validator is better than http://www.yamllint.com.
YAML Validator follows YAML spec, support multiple doc feature which is not supported my YAML Lint.
JSON validator and formatter follows the JSON spec at http://json.org/.
Also, now you dont have to use https://www.base64decode.org/,
This app also supports Decode from and to Base64 format.

Here's the working version of the Application hosted in Microsoft Azure Cloud:
  http://varkeys-rhel-jenkins.westus.cloudapp.azure.com:9090/validator/editor

![Alt text](enterprise-validator-suite-image.PNG?raw=true "Enterprise Validator Suite")

### Recipe Ingredients
 * spring-boot-starter-web
 * spring-boot-starter-freemarker
 * springfox-swagger2, springfox-swagger-ui
 * org.json,snakeyaml, lombok, jackson-databind, spring-boot-test


### How to?
[comment]: # (REPLACE ME: Add your confluence page in below format)
* Click on "BASE 64Encode" to encode to Base64 format.
* Click on "BASE 64Encode" to decode from Base64 format.
* Click on "Validate YAML" to validate YAML data.
* Click on "Validate JSON" to validate JSON data.
* Click on "Format YAML" to format JSON data.
* Click on "Share Data" to share the data through configured Mailing App.


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
> 3. Added JSON validation and Formatting capabilities.

* Version 1.*-RELEASE

> Released *[08/10/2018]* : 1.0.0.RELEASE
>
> Changes includes the following:
>
> 1. Initial Commit with only YAML validation service.
> 2. Enabled Jenkins setup and one touch deployment.