package io.exnihilo.validator.config;

import com.google.common.base.Predicates;
import io.swagger.annotations.ApiKeyAuthDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import javax.servlet.ServletContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Slf4j
@Configuration
public class Config {
  @Value("${application.name}")
  private String applicationName;
  @Value("${application.description}")
  private String applicationDescription;
  @Value("${application.version}")
  private String applicationVersion;

  @Value("${swagger.contactName}")
  private String contactName;
  @Value("${swagger.contactUrl}")
  private String contactUrl;
  @Value("${swagger.contactEmail}")
  private String contactEmail;
  @Value("${swagger.licenseUrl}")
  private String licenseUrl;
  @Value("${swagger.termsOfServiceUrl}")
  private String termsOfServiceUrl;
  @Value("${swagger.host}")
  private String host;
  @Value("${swagger.protocols}")
  private String[] protocols;

  @Bean
  public Docket api(ServletContext servletContext) {
    return new Docket(DocumentationType.SWAGGER_2)
        .securitySchemes(Collections.singletonList(
            new ApiKey("JWT", HttpHeaders.AUTHORIZATION, ApiKeyAuthDefinition.ApiKeyLocation.HEADER.name())))
        .securityContexts(
            Collections.singletonList(SecurityContext.builder()
                .securityReferences(Collections.singletonList(
                    SecurityReference.builder().reference("JWT").scopes(new AuthorizationScope[0]).build()))
                .build()))
        .select().apis(RequestHandlerSelectors.basePackage("io.exnihilo"))
        .paths(Predicates.not(PathSelectors.ant("/actuator/**"))).build().apiInfo(apiInfo()).host(host)
        .protocols(new HashSet<>(Arrays.asList(protocols))).tags(new Tag("Validator Service",
            "Validator APISs for YAML, JSON and XML formats and Base64 encoding and decoding"));
  }

  private ApiInfo apiInfo() {
    return new ApiInfo(applicationName, applicationDescription, applicationVersion, termsOfServiceUrl,
        new Contact(contactName, contactUrl, contactEmail), "License of API", licenseUrl, Collections.emptyList());
  }
}
