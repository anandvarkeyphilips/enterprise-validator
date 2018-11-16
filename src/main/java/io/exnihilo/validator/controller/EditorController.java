package io.exnihilo.validator.controller;

import io.exnihilo.validator.entity.ValidationEntity;
import io.exnihilo.validator.service.ValidatorService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully connected and validated with API Validator"),
        @ApiResponse(code = 401, message = "You are not authenticated properly to view the resource!"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden!"),
        @ApiResponse(code = 404, message = "Validator Service not available right now!"),
})
public class EditorController {

    @Autowired
    private ValidatorService validatorService;

    @RequestMapping("/editor")
    public ModelAndView editor() {
        log.info("Inside editor");
        return new ModelAndView("editor");
    }

    /**
     * A pre-configured sample REST endpoint to demonstrate the use of Request Parameter.
     *
     * @param validationEntity
     * @return validation result
     */
    @PostMapping("/yaml")
    @ApiOperation(
            value = "API for Validating the YAML Data",
            notes = "This API validates YAML data input.The API is in beta phase..")
    public ResponseEntity<?> validateYamlController(@RequestBody ValidationEntity validationEntity) {
        log.debug("Calling validateYamlController...");
        return new ResponseEntity<Object>(validatorService.validateYamlService(validationEntity.getInputMessage()), HttpStatus.OK);
    }

    /**
     * A pre-configured sample REST endpoint to demonstrate the use of Request Parameter.
     *
     * @param validationEntity
     * @return validation result
     */
    @PostMapping("/json")
    @ApiOperation(
            value = "API for Validating the JSON Data",
            notes = "This API validates JSON data input.The API is in beta phase..")
    public ResponseEntity<?> validateJsonController(@RequestBody ValidationEntity validationEntity) {
        log.debug("Calling validateJsonController...");
        return new ResponseEntity<Object>(validatorService.validateJsonService(validationEntity.getInputMessage()), HttpStatus.OK);
    }

    /**
     * A pre-configured sample REST endpoint to demonstrate the use of Request Parameter.
     *
     * @param validationEntity
     * @return validation result
     */
    @PostMapping("/formatJson")
    @ApiOperation(
            value = "API for formatting the JSON Data",
            notes = "This API formats JSON data input.The API is in beta phase..")
    public ResponseEntity<?> formatJsonController(@RequestBody ValidationEntity validationEntity) {
        log.debug("Calling formatJsonController...");
        return new ResponseEntity<Object>(validatorService.formatJsonService(validationEntity.getInputMessage()), HttpStatus.OK);
    }

    /**
     * A pre-configured sample REST endpoint to demonstrate the use of Request Parameter.
     *
     * @param validationEntity
     * @return validation result
     */
    @PostMapping("/formatXml")
    @ApiOperation(
            value = "API for formatting the XML Data",
            notes = "This API formats XML data input.The API is in beta phase..")
    public ResponseEntity<?> formatXmlController(@RequestBody ValidationEntity validationEntity) {
        log.debug("Calling formatXmlController...");
        return new ResponseEntity<Object>(validatorService.formatXmlService(validationEntity.getInputMessage()), HttpStatus.OK);
    }
}