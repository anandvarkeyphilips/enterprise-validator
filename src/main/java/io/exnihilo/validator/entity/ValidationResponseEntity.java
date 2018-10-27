package io.exnihilo.validator.entity;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
/**
 * Validation Response Entity has the response details for all configured validator methods.
 *
 * @author Anand Varkey Philips
 * @date 27/10/2018
 * @since 2.0.0.RELEASE
 */
@Component
@Data
@Scope(value = "prototype", proxyMode = ScopedProxyMode.INTERFACES)
public class ValidationResponseEntity {
    private int lineNumber;
    private int columnNumber;
    private String validationMessage;
}
