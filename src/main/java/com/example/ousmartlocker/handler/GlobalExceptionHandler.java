package com.example.ousmartlocker.handler;

import com.example.ousmartlocker.exception.OuSmartLockerBadRequestApiException;
import com.example.ousmartlocker.exception.OuSmartLockerUnauthorizedApiException;
import com.example.ousmartlocker.dto.ErrorDetailDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(OuSmartLockerBadRequestApiException.class)
    public ResponseEntity<ErrorDetailDto> handleReservationApiException(
            OuSmartLockerBadRequestApiException exception,
            WebRequest request
    ) {
        final ErrorDetailDto errorDetailDto = new ErrorDetailDto();
        errorDetailDto.setErrorMessage(exception.getLocalizedMessage());
        errorDetailDto.setDevErrorMessage(request.getDescription(false));
        errorDetailDto.setTimestamp(System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetailDto);
    }

    @ExceptionHandler(OuSmartLockerUnauthorizedApiException.class)
    public ResponseEntity<ErrorDetailDto> handleUnauthorizedApiException(
            OuSmartLockerUnauthorizedApiException exception,
            WebRequest request
    ) {
        final ErrorDetailDto errorDetailDto = new ErrorDetailDto();
        errorDetailDto.setErrorMessage(exception.getLocalizedMessage());
        errorDetailDto.setDevErrorMessage(request.getDescription(false));
        errorDetailDto.setTimestamp(System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetailDto);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetailDto> handleAccessDeniedException(
            AccessDeniedException exception,
            WebRequest request
    ) {
        final ErrorDetailDto errorDetailDto = new ErrorDetailDto();

        errorDetailDto.setErrorMessage(exception.getLocalizedMessage());
        errorDetailDto.setDevErrorMessage(request.getDescription(false));
        errorDetailDto.setTimestamp(System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetailDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetailDto> handleGlobalException(
            Exception exception,
            WebRequest request
    ) {
        final ErrorDetailDto errorDetailDto = new ErrorDetailDto();
        errorDetailDto.setErrorMessage(exception.getLocalizedMessage());
        errorDetailDto.setDevErrorMessage(request.getDescription(false));
        errorDetailDto.setTimestamp(System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetailDto);
    }

}
