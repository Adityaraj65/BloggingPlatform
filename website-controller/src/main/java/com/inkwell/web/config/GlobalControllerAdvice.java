package com.inkwell.web.config;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllErrors(Exception ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "Microservice communication failed. Please check Eureka.");
        return mav;
    }
}