package io.exnihilo.validator.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.exnihilo.validator.service.ValidatorService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Slf4j
@RestController
public class ValidatorController {

    @Autowired
    private ValidatorService validatorService;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build().apiInfo(apiInfo());
    }


    private ApiInfo apiInfo() {
        return new ApiInfo(
                "This REST API",
                "Validates yaml, json and xml files. Hi!!",
                "API TOS",
                "Terms of service",
                new Contact("Anand Varkey Philips", "about.me/anandvarkeyphilips", "anandvarkey.philips@gmail.com"),
                "License of API", "API license URL", Collections.emptyList());
    }


    @RequestMapping("/forEditor")
    public ModelAndView forEditor() {
        log.info("Inside forEditor");
        return new ModelAndView("yaml-editor");
    }

    /**
     * A pre-configured sample REST endpoint to demonstrate the use of Request Parameter.
     *
     * @param yamlData
     * @return validation result
     */
    @PostMapping("/yaml")
    @ApiOperation(
            value = "API for Validating the YAML Data",
            notes = "This API validates YAML data input.The API is in beta phase..")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully connected and validated with API Validator"),
            @ApiResponse(code = 401, message = "You are not authenticated properly to view the resource!"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden!"),
            @ApiResponse(code = 404, message = "Validator Service not available right now!"),
    })
    public ResponseEntity<?> validateYAMLController(@RequestBody ObjectNode yamlData) {
        log.debug("Calling validateYAMLController...");
        return new ResponseEntity<Object>(validatorService.validateYAMLService(yamlData.get("yamlData").asText()), HttpStatus.OK);
    }
}
