package io.exnihilo.validator.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * Validation Response Entity has the response details for all configured editor methods.
 *
 * @author Anand Varkey Philips
 * @date 27/10/2018
 * @since 2.0.0.RELEASE
 */
@Data
@Builder
@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.INTERFACES)
public class ValidationEntity {
    private boolean isValid;
    private int lineNumber;
    private int columnNumber;
    private String validationMessage;
    private String inputMessage;
}
