package org.catcom.classreserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.catcom.classreserver.exceptions.reservations.ReservationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public @ResponseBody Map<String, Object> handleMismatchArgumentType(MethodArgumentTypeMismatchException ex, HttpServletResponse res)
    {

        res.setStatus(HttpStatus.BAD_REQUEST.value());
        var paramName = ex.getParameter().getParameterName();
        if (paramName == null) paramName = "null";

        var requiredParamTypeName = ex.getParameter().getParameterType().getSimpleName();
        var actualParamTypeName = ex.getValue() == null ? "null" : ex.getValue().getClass().getSimpleName();
        var reason = "Cannot cast argument {" + paramName + "} from type [" + actualParamTypeName + "] to [" + requiredParamTypeName + "] for value: " + ex.getValue().toString();

        return Map.of(
                "reason", reason,
                "argument", paramName,
                "value", ex.getValue(),
                "expected_type", requiredParamTypeName,
                "actual_type", actualParamTypeName
        );

    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public @ResponseBody Map<String, Object> handleMissingParameterException(MissingServletRequestParameterException ex, HttpServletResponse res)
    {
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        var reason = "Missing argument {" + ex.getParameterName() + "} of type [" + ex.getParameterType() + "]";

        return Map.of(
                "reason", reason,
                "argument", ex.getParameterName(),
                "type", ex.getParameterType()
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public @ResponseBody Map<String, Object> handleGenericResponseException(ResponseStatusException ex, HttpServletResponse res)
    {
        res.setStatus(ex.getStatusCode().value());
        var reason = ex.getReason();
        if (reason == null) reason = ex.getMessage();
        return Map.of("reason", reason);
    }

    @ExceptionHandler(ReservationException.class)
    public @ResponseBody Map<String, Object> handleReservationException(ReservationException ex, HttpServletResponse res)
    {
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        var reason = ex.getMessage() == null ? "null" : ex.getMessage();
        return Map.of("reason", reason);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public @ResponseBody Map<String, Object> handleUnreadableRequest(HttpMessageNotReadableException ex, HttpServletResponse res)
    {
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        return Map.of(
                "reason", ex.getMessage()
        );
    }

}
