package com.studentManagementSystem.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CsrfException.class)
    public String csrfExceptionHandler(CsrfException csrfException,RedirectAttributes redirectAttributes) {
        log.warn("Csrf validation failed", csrfException.getMessage());
        redirectAttributes.addFlashAttribute("message", "Session expired. Please log in again.");
        return "redirect:/login";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String genericExceptionHandler(Exception exception) {
        log.warn("Something went wrong", exception.getMessage());
        return "500";
    }
}
