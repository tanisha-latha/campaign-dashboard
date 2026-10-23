package com.capstone.dashboard;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.capstone.dashboard.campaign.CampaignNotFoundException;

// Global error handling: @RestControllerAdvice catches exceptions thrown by
// any controller and turns them into clean JSON errors the frontend can show,
// instead of Spring's default error page.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Unknown id -> 404 { "message": "Campaign not found: ..." }
    @ExceptionHandler(CampaignNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleNotFound(CampaignNotFoundException ex) {
        return Map.of("message", ex.getMessage());
    }

    // @Valid failed -> 400 { "message": "...", "errors": { "name": "Name is required" } }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.putIfAbsent(error.getField(), error.getDefaultMessage()));
        return Map.of("message", "Please fix the highlighted fields.", "errors", errors);
    }

    // Body isn't valid JSON, or a value has the wrong type (e.g. an unknown status) -> 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleUnreadable(HttpMessageNotReadableException ex) {
        return Map.of("message", "Request body is missing or has the wrong format.");
    }
}
