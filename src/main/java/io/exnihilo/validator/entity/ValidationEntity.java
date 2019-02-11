package io.exnihilo.validator.entity;

import lombok.*;
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
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.INTERFACES)
public class ValidationEntity {
    private boolean valid;
    private int lineNumber;
    private int columnNumber;
    private String validationMessage;
    private String inputMessage;

    public static ValidationEntityBuilder builder(String inputMessage) {
        return new ValidationEntityBuilder().inputMessage(inputMessage);
    }
}