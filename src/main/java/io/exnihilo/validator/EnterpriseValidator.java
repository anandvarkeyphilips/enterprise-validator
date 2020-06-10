package io.exnihilo.validator;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * This is the entry point for spring boot admin server application. There are lots of
 * customizations available.
 *
 * @author Anand Varkey Philips
 * @since 2.0.6.RELEASE
 */
@EnableSwagger2
@Slf4j
@SpringBootApplication
public class EnterpriseValidator {

  private String applicationName;
  private static final ZoneId ZONE_ID = ZoneId.of("Asia/Kolkata");
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd:MMM:YYY hh:mm:ss:SSS a");

  @Value("${application.name}")
  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  public static void main(String[] args) {
    SpringApplicationBuilder app = new SpringApplicationBuilder(EnterpriseValidator.class);
    app.build().addListeners(new ApplicationPidFileWriter());
    app.run();
  }

  @EventListener(ApplicationReadyEvent.class)
  void logApplicationStart() {
    log.info("{} Application is started at Indian Standard Time: {}", applicationName,
        ZonedDateTime.now(ZONE_ID).format(DATE_TIME_FORMATTER));
  }

  @PreDestroy
  void logApplicationExit() {
    log.info("{} Application is being gracefully stopped at Indian Standard Time: {}", applicationName,
        ZonedDateTime.now(ZONE_ID).format(DATE_TIME_FORMATTER));
  }
}
