package io.exnihilo.validator.controller;

import io.exnihilo.validator.entity.ValidationEntity;
import io.exnihilo.validator.service.IValidatorService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@CrossOrigin(maxAge = 3600)
@RestController
@ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully connected and validated with API Validator"),
    @ApiResponse(code = 401, message = "You are not authenticated properly to view the resource!"),
    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden!"),
    @ApiResponse(code = 404, message = "Validator Service not available right now!"),})
public class EditorController {

  @Autowired
  private IValidatorService validatorService;

  @GetMapping("/editor")
  public ModelAndView editor() {
    log.info("Inside editor");
    return new ModelAndView("editor");
  }

  /**
   * Splits yaml data in case of multiple documents "---" and validates each part, and then returns
   * error message, line and column numbers in case of failure.
   *
   * @param validationEntity
   * @return validation result
   */
  @PostMapping("/yaml")
  @ApiOperation(value = "API for Validating the YAML Data", notes = "This API validates YAML data input.")
  public ValidationEntity validateYamlController(@RequestBody ValidationEntity validationEntity) {
    log.debug("Calling validateYamlController...");
    return validatorService.validateYamlService(validationEntity);
  }

  /**
   * Validates the format of json data string and returns error details in case of failure.
   *
   * @param validationEntity
   * @return validation result
   */
  @PostMapping("/json")
  @ApiOperation(value = "API for Validating the JSON Data", notes = "This API validates JSON data input.")
  public ValidationEntity validateJsonController(@RequestBody ValidationEntity validationEntity) {
    log.debug("Calling validateJsonController...");
    return validatorService.validateJsonService(validationEntity);
  }

  /**
   * Validates the format of json data string and returns formatted json along with error details in
   * case of failure.
   *
   * @param validationEntity
   * @return validation result
   */
  @PostMapping("/formatJson")
  @ApiOperation(value = "API for formatting the JSON Data", notes = "This API formats JSON data input.")
  public ValidationEntity formatJsonController(@RequestBody ValidationEntity validationEntity) {
    log.debug("Calling formatJsonController...");
    return validatorService.formatJsonService(validationEntity);
  }

  /**
   * Validates the format of xml data string and returns error details in case of failure.
   *
   * @param validationEntity
   * @return validation result
   */
  @PostMapping("/formatXml")
  @ApiOperation(value = "API for formatting the XML Data", notes = "This API formats XML data input.")
  public ValidationEntity formatXmlController(@RequestBody ValidationEntity validationEntity) {
    log.debug("Calling formatXmlController...");
    return validatorService.formatXmlService(validationEntity);
  }

  /**
   * Encodes data in Base64 format and returns error details in case of failure.
   *
   * @param validationEntity
   * @return validation result
   */
  @PostMapping("/base64Encode")
  @ApiOperation(value = "API for Base64 encoding Data", notes = "This API encodes data input.")
  public ValidationEntity base64EncodeController(@RequestBody ValidationEntity validationEntity) {
    log.debug("Calling base64EncodeController...");
    return validatorService.encodeBase64(validationEntity);
  }

  /**
   * Decodes data from Base64 format and returns error details in case of failure.
   *
   * @param validationEntity
   * @return validation result
   */
  @PostMapping("/base64Decode")
  @ApiOperation(value = "API for Base64 decoding Data", notes = "This API decodes data input.")
  public ValidationEntity base64DecodeController(@RequestBody ValidationEntity validationEntity) {
    log.debug("Calling base64DecodeController...");
    return validatorService.decodeBase64(validationEntity);
  }
}
