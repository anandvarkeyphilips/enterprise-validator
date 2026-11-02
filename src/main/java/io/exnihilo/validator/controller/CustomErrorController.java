package io.exnihilo.validator.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;



@Slf4j
@Hidden
@Controller
public class CustomErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

  @GetMapping("/error")
  public ModelAndView handleError(HttpServletRequest request) {
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    if (null != status && Integer.valueOf(status.toString()) == HttpStatus.NOT_FOUND.value()) {
      log.error("error-404: An attempt to search for {}", request.getRequestURL());
      return new ModelAndView("/errors/404");
    } else {
      log.error("Internal error occurred for {}", request.getRequestURL());
      return new ModelAndView("/errors/500");
    }
  }
}
