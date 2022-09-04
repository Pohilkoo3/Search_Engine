package com.example.SearchEngine.controlers.errors;

import com.example.SearchEngine.exceptions.IllegalSearchQueryParamException;
import com.example.SearchEngine.models.errors.InterruptedCustomError;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionsController {

   @ExceptionHandler(InterruptedException.class)
    public String getErrorsInterruptedException(InterruptedException ex, RedirectAttributes redirectAttributes, Model model){
       model.addAttribute("searchError", ex);
       return "404";
   }

    @ExceptionHandler(InterruptedCustomError.class)
    public String getErrorsInterruptedException(InterruptedCustomError ex){
        String text = "InterruptedCustomError. InterruptedCustomError. InterruptedCustomError. InterruptedCustomError.";
        System.out.println(text);
        return "404";
    }

    @ExceptionHandler(IllegalSearchQueryParamException.class)
    public String handlerEmptySearchException(IllegalSearchQueryParamException e, RedirectAttributes redirectAttributes, Model model) {
        model.addAttribute("searchError", e);
        return "404";
    }

    @ExceptionHandler(RuntimeException.class)
    public String getErrorsRuntimeExceptionException(RuntimeException ex, RedirectAttributes redirectAttributes, Model model){
        model.addAttribute("searchError", ex);
        return "404";
    }




}
