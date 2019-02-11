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
     * Splits yaml data in case of multiple documents "---" and validates each part,
     * and then returns error message, line and column numbers in case of failure.
     *
     * @param validationEntity
     * @return validation result
     */
    @PostMapping("/yaml")
    @ApiOperation(
            value = "API for Validating the YAML Data",
            notes = "This API validates YAML data input.")
    public ResponseEntity<?> validateYamlController(@RequestBody ValidationEntity validationEntity) {
        log.debug("Calling validateYamlController...");
        return new ResponseEntity<Object>(validatorService.validateYamlService(validationEntity), HttpStatus.OK);
    }

    /**
     * Validates the format of json data string and returns error details in case of failure.
     *
     * @param validationEntity
     * @return validation result
     */
    @PostMapping("/json")
    @ApiOperation(
            value = "API for Validating the JSON Data",
            notes = "This API validates JSON data input.")
    public ResponseEntity<?> validateJsonController(@RequestBody ValidationEntity validationEntity) {
        log.debug("Calling validateJsonController...");
        return new ResponseEntity<Object>(validatorService.validateJsonService(validationEntity), HttpStatus.OK);
    }

    /**
     * Validates the format of json data string and returns formatted json
     * along with error details in case of failure.
     *
     * @param validationEntity
     * @return validation result
     */
    @PostMapping("/formatJson")
    @ApiOperation(
            value = "API for formatting the JSON Data",
            notes = "This API formats JSON data input.")
    public ResponseEntity<?> formatJsonController(@RequestBody ValidationEntity validationEntity) {
        log.debug("Calling formatJsonController...");
        return new ResponseEntity<Object>(validatorService.formatJsonService(validationEntity), HttpStatus.OK);
    }

    /**
     * Validates the format of xml data string and returns  error details in case of failure.
     *
     * @param validationEntity
     * @return validation result
     */
    @PostMapping("/formatXml")
    @ApiOperation(
            value = "API for formatting the XML Data",
            notes = "This API formats XML data input.")
    public ResponseEntity<?> formatXmlController(@RequestBody ValidationEntity validationEntity) {
        log.debug("Calling formatXmlController...");
        return new ResponseEntity<Object>(validatorService.formatXmlService(validationEntity), HttpStatus.OK);
    }

    /**
     * Encodes data in Base64 format and returns error details in case of failure.
     *
     * @param validationEntity
     * @return validation result
     */
    @PostMapping("/base64Encode")
    @ApiOperation(
            value = "API for Base64 encoding Data",
            notes = "This API encodes data input.")
    public ResponseEntity<?> base64EncodeController(@RequestBody ValidationEntity validationEntity) {
        log.debug("Calling base64EncodeController...");
        return new ResponseEntity<Object>(validatorService.encodeBase64(validationEntity), HttpStatus.OK);
    }

    /**
     * Decodes data from Base64 format and returns error details in case of failure.
     *
     * @param validationEntity
     * @return validation result
     */
    @PostMapping("/base64Decode")
    @ApiOperation(
            value = "API for Base64 decoding Data",
            notes = "This API decodes data input.")
    public ResponseEntity<?> base64DecodeController(@RequestBody ValidationEntity validationEntity) {
        log.debug("Calling base64DecodeController...");
        return new ResponseEntity<Object>(validatorService.decodeBase64(validationEntity), HttpStatus.OK);
    }
}