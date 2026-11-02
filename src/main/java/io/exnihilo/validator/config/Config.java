package io.exnihilo.validator.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

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
  public OpenAPI api() {
    final String securitySchemeName = "JWT";
    List<Server> servers = new ArrayList<>();
    for (String protocol : protocols) {
      servers.add(new Server().url(protocol.trim() + "://" + host).description(applicationName + " server"));
    }
    return new OpenAPI()
        .info(new Info().title(applicationName).description(applicationDescription).version(applicationVersion)
            .termsOfService(termsOfServiceUrl)
            .contact(new Contact().name(contactName).url(contactUrl).email(contactEmail))
            .license(new License().name("License of API").url(licenseUrl)))
        .servers(servers).addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
        .components(new Components().addSecuritySchemes(securitySchemeName,
            new SecurityScheme().name(HttpHeaders.AUTHORIZATION).type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER).description("JWT authentication token")))
        .tags(List.of(new Tag().name("Validator Service")
            .description("Validator APIs for YAML, JSON and XML formats and Base64 encoding and decoding")));
  }
}
