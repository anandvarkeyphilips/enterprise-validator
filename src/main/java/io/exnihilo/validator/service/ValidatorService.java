package io.exnihilo.validator.service;

import io.exnihilo.validator.entity.ValidationResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validator Service Class handles the functional aspects of all configured validators.
 *
 * @author Anand Varkey Philips
 * @date 27/10/2018
 * @since 2.0.0.RELEASE
 */
@Slf4j
@Service
public class ValidatorService {

    @Lookup
    public ValidationResponseEntity getPrototypeBean() {
        return null;
    }


    /**
     * Splitting of yaml data in case of "---", then validating each part separately,
     * and then returning success data or line and column numbers in case of failure.
     *
     * @param yamlData
     * @return validation result
     */
    public ValidationResponseEntity validateYAMLService(String yamlData) {
        ValidationResponseEntity validationResponseEntity = getPrototypeBean();
        String validationMessage = "Valid YAML!!!";
        try {
            Yaml yaml = new Yaml();
            Map<String, Object> obj = yaml.load(yamlData.replace("---", ""));
            log.debug("Value obtained successfully: {}", obj.toString());
            validationResponseEntity.setValidationMessage(validationMessage);
        } catch (Exception e) {
            validationMessage = e.getMessage();
            validationResponseEntity.setValidationMessage(validationMessage);
            log.error("Exception occurred in validation: ", e);
            if (validationMessage.contains("line ")) {
                String pattern1 = "line ";
                String pattern2 = ",";
                Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
                Matcher m = p.matcher(validationMessage);
                while (m.find()) {
                    validationResponseEntity.setLineNumber(Integer.parseInt(m.group(1)));
                }
            }
            if (validationMessage.contains("line ")) {
                String pattern1 = "column ";
                String pattern2 = ":";
                Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
                Matcher m = p.matcher(validationMessage);
                while (m.find()) {
                    validationResponseEntity.setColumnNumber(Integer.parseInt(m.group(1)));
                }
            }
        } finally {
            return validationResponseEntity;
        }
    }
}