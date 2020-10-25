package com.matejko.wownetwork.api;

import com.matejko.wownetwork.exceptions.NoEntityExistsException;
import com.matejko.wownetwork.exceptions.SelfFollowingException;
import com.matejko.wownetwork.exceptions.UserAlreadyFollowedException;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@ResponseBody
public class ControllerSupervisor {

    @ExceptionHandler(NoEntityExistsException.class)
    public ResponseEntity<String> noEntityExceptionHandler(final NoEntityExistsException exception) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(exception.getMessage());
    }

    @ExceptionHandler(UserAlreadyFollowedException.class)
    public ResponseEntity<String> userAlreadyFollowedExceptionHandler(final UserAlreadyFollowedException exception) {
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(exception.getMessage());
    }

    @ExceptionHandler(SelfFollowingException.class)
    public ResponseEntity<String> selfFollowingHandler(final SelfFollowingException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> hibernateValidatorsExceptionsHandler(final MethodArgumentNotValidException exception) {
        return exception.getBindingResult().getAllErrors()
                .stream()
                .filter(error -> error instanceof FieldError)
                .map(error -> (FieldError) error)
                .map(error -> {
                    final var fieldName = error.getField();
                    final var errorMessage = error.getDefaultMessage();

                    return Pair.of(fieldName, errorMessage);
                })
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }
}
