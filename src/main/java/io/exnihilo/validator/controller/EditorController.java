package io.exnihilo.validator.controller;

import io.exnihilo.validator.entity.DiffEntity;
import io.exnihilo.validator.entity.ValidationEntity;
import io.exnihilo.validator.service.IValidatorService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
// CORS is intentionally open: this is a stateless public validation API with no authentication,
// cookies or sensitive data, and enterprise deployments must be able to call it from any origin.
@SuppressWarnings("java:S5122")
@CrossOrigin(maxAge = 3600)
@RestController
@Tag(name = "Validator Service")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Successfully connected and validated with API Validator"),
    @ApiResponse(responseCode = "401", description = "You are not authenticated properly to view the resource!"),
    @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden!"),
    @ApiResponse(responseCode = "404",
        description = "Validator Service page you are looking for is not available right now!"),})
public class EditorController {

  private final IValidatorService validatorService;

  public EditorController(IValidatorService validatorService) {
    this.validatorService = validatorService;
  }

  @Hidden
  @GetMapping("/")
  public ModelAndView index() {
    return new ModelAndView("redirect:/editor");
  }

  @Hidden
  @GetMapping("/editor")
  public ModelAndView editor() {
    log.info("Inside editor");
    return new ModelAndView("editor");
  }

  /**
   * Splits yaml data in case of multiple documents "---" and validates each part, and then returns
   * error message, line and column numbers in case of failure.
   *
   * @param validationEntity Model entity for all api requests and responses
   * @return validation result
   */
  @PostMapping("/yaml")
  @Operation(summary = "API for Validating the YAML Data", description = "This API validates YAML data input.")
  public ValidationEntity validateYamlController(@RequestBody ValidationEntity validationEntity) {
    log.debug("Calling validateYamlController...");
    return validatorService.validateYamlService(validationEntity);
  }

  /**
   * Validates the format of json data string and returns error details in case of failure.
   *
   * @param validationEntity Model entity for all api requests and responses
   * @return validation result
   */
  @PostMapping("/json")
  @Operation(summary = "API for Validating the JSON Data", description = "This API validates JSON data input.")
  public ValidationEntity validateJsonController(@RequestBody ValidationEntity validationEntity) {
    log.debug("Calling validateJsonController...");
    return validatorService.validateJsonService(validationEntity);
  }

  /**
   * Validates the format of json data string and returns formatted json along with error details in
   * case of failure.
   *
   * @param validationEntity Model entity for all api requests and responses
   * @return validation result
   */
  @PostMapping("/formatJson")
  @Operation(summary = "API for formatting the JSON Data", description = "This API formats JSON data input.")
  public ValidationEntity formatJsonController(@RequestBody ValidationEntity validationEntity) {
    log.debug("Calling formatJsonController...");
    return validatorService.formatJsonService(validationEntity);
  }

  /**
   * Validates the format of xml data string and returns error details in case of failure.
   *
   * @param validationEntity Model entity for all api requests and responses
   * @return validation result
   */
  @PostMapping("/formatXml")
  @Operation(summary = "API for formatting the XML Data", description = "This API formats XML data input.")
  public ValidationEntity formatXmlController(@RequestBody ValidationEntity validationEntity) {
    log.debug("Calling formatXmlController...");
    return validatorService.formatXmlService(validationEntity);
  }

  /**
   * Encodes data in Base64 format and returns error details in case of failure.
   *
   * @param validationEntity Model entity for all api requests and responses
   * @return validation result
   */
  @PostMapping("/base64Encode")
  @Operation(summary = "API for Base64 encoding Data", description = "This API encodes data input.")
  public ValidationEntity base64EncodeController(@RequestBody ValidationEntity validationEntity) {
    log.debug("Calling base64EncodeController...");
    return validatorService.encodeBase64(validationEntity);
  }

  /**
   * Decodes data from Base64 format and returns error details in case of failure.
   *
   * @param validationEntity Model entity for all api requests and responses
   * @return validation result
   */
  @PostMapping("/base64Decode")
  @Operation(summary = "API for Base64 decoding Data", description = "This API decodes data input.")
  public ValidationEntity base64DecodeController(@RequestBody ValidationEntity validationEntity) {
    log.debug("Calling base64DecodeController...");
    return validatorService.decodeBase64(validationEntity);
  }

  /**
   * Compares two json documents by keys and reports added, removed and changed key paths.
   *
   * @param diffEntity holds the base document (inputMessage) and the comparison document
   *        (compareMessage)
   * @return diff result with the list of differences
   */
  @PostMapping("/diffJson")
  @Operation(summary = "API for a key-based diff of two JSON documents",
      description = "This API compares the base document with the comparison document by keys.")
  public DiffEntity diffJsonController(@RequestBody DiffEntity diffEntity) {
    log.debug("Calling diffJsonController...");
    return validatorService.diffJsonService(diffEntity);
  }

  /**
   * Compares two yaml documents by keys and reports added, removed and changed key paths.
   *
   * @param diffEntity holds the base document (inputMessage) and the comparison document
   *        (compareMessage)
   * @return diff result with the list of differences
   */
  @PostMapping("/diffYaml")
  @Operation(summary = "API for a key-based diff of two YAML documents",
      description = "This API compares the base document with the comparison document by keys.")
  public DiffEntity diffYamlController(@RequestBody DiffEntity diffEntity) {
    log.debug("Calling diffYamlController...");
    return validatorService.diffYamlService(diffEntity);
  }
}
