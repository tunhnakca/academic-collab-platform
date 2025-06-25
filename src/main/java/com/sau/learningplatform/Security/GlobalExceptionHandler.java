package com.sau.learningplatform.Security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {

        log.warn("Error: " + e.getMessage());

        model.addAttribute("errorMessage", "Something went wrong. Please try again later.");

        return "custom-error";
    }
}