package io.exnihilo.validator;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import io.exnihilo.validator.config.MemoryAppender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests the bean context configurations
 *
 * @author Anand Varkey Philips
 * @since 2.0.6.RELEASE
 */
@ActiveProfiles("test")
@SpringBootTest
public class EnterpriseValidatorTest {

  private MemoryAppender memoryAppender;
  private static final String LOGGER_NAME = "io.exnihilo.validator";

  @BeforeEach
  void setup() {
    Logger logger = (Logger) LoggerFactory.getLogger(LOGGER_NAME);
    memoryAppender = new MemoryAppender();
    memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
    logger.setLevel(Level.DEBUG);
    logger.addAppender(memoryAppender);
    memoryAppender.start();
  }

  @Test
  void contextLoads() {
    assertTrue(true);
  }

  @Test
  void testApplicationStartAndExitLogs() {
    EnterpriseValidator enterpriseValidator = new EnterpriseValidator();
    enterpriseValidator.logApplicationStart();
    enterpriseValidator.logApplicationExit();

    Assertions.assertThat(memoryAppender.countEventsForLogger(LOGGER_NAME)).isEqualTo(2);
    Assertions.assertThat(memoryAppender.search("Application is started at Indian Standard Time", Level.INFO))
        .hasSize(1);
    Assertions
        .assertThat(
            memoryAppender.search("Application is being gracefully stopped at Indian Standard Time", Level.INFO))
        .hasSize(1);
    Assertions
        .assertThat(
            memoryAppender.contains("Application is being gracefully stopped at Indian Standard Time", Level.TRACE))
        .isFalse();
  }
}
