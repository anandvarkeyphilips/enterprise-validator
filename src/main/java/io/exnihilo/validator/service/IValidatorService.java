package io.exnihilo.validator.service;

import io.exnihilo.validator.entity.ValidationEntity;
import org.springframework.stereotype.Service;

/**
 * Validator Service Interface for proving custom implementations of configured validators if
 * required.
 *
 * @author Anand Varkey Philips
 * @since 2.0.0.RELEASE
 */
@Service
public interface IValidatorService {

  /**
   * Splits yaml data in case of multiple documents "---" and validates each part, and then returns
   * error message, line and column numbers in case of failure.
   *
   * @param validationEntity Model entity for all api requests and responses
   * @return validationEntity
   */
  ValidationEntity validateYamlService(ValidationEntity validationEntity);

  /**
   * Validates the format of json data string and returns error details in case of failure.
   *
   * @param validationEntity Model entity for all api requests and responses
   * @return validationEntity
   */
  ValidationEntity validateJsonService(ValidationEntity validationEntity);

  /**
   * Validates the format of json data string and returns formatted json along with error details in
   * case of failure.
   *
   * @param validationEntity Model entity for all api requests and responses
   * @return validationEntity
   */
  ValidationEntity formatJsonService(ValidationEntity validationEntity);

  /**
   * Validates the format of xml data string and returns error details in case of failure.
   *
   * @param validationEntity Model entity for all api requests and responses
   * @return validationEntity
   */
  ValidationEntity formatXmlService(ValidationEntity validationEntity);

  /**
   * Encodes data in Base64 format and returns error details in case of failure.
   *
   * @param validationEntity Model entity for all api requests and responses
   * @return validationEntity
   */
  ValidationEntity encodeBase64(ValidationEntity validationEntity);

  /**
   * Decodes data from Base64 format and returns error details in case of failure.
   *
   * @param validationEntity Model entity for all api requests and responses
   * @return validationEntity
   */
  ValidationEntity decodeBase64(ValidationEntity validationEntity);
}
