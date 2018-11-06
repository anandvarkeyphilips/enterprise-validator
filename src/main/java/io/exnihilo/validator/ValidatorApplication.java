package io.exnihilo.validator;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * This is the entry point for spring boot admin server application.
 * There are lots of customizations available.
 *
 * @author Anand Varkey Philips
 * @version 2.0.2.RELEASE
 */
@EnableSwagger2
@SpringBootApplication
public class ValidatorApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder app = new SpringApplicationBuilder(ValidatorApplication.class);
        app.build().addListeners(new ApplicationPidFileWriter());
        app.run();
    }
}