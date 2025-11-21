package alik.leverxfinalproject.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationException(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() +  " " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(","));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDTO("Invalid-request", errorMessage));
    }

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<ErrorDTO> handleInvalidLoginException(InvalidLoginException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorDTO("Invalid-login", exception.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ErrorDTO> handleEmailAlreadyInUseException(EmailAlreadyInUseException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorDTO("Email-in-use", exception.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyVerifiedException.class)
    public ResponseEntity<ErrorDTO> handleEmailAlreadyVerifiedException(EmailAlreadyVerifiedException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorDTO("Email-verified", exception.getMessage()));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorDTO> handleInvalidTokenException(InvalidTokenException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDTO("Invalid-token", exception.getMessage()));
    }

    @ExceptionHandler(InvalidResetCodeException.class)
    public ResponseEntity<ErrorDTO> handleInvalidResetCodeException(InvalidResetCodeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDTO("Invalid-reset-code", exception.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleUserNotFoundException(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDTO("User-not-found", exception.getMessage()));
    }

    @ExceptionHandler(GameObjectNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleGameObjectNotFoundException(GameObjectNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDTO("Game-object-not-found", exception.getMessage()));
    }

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleGameNotFoundException(GameNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDTO("Game-not-found", exception.getMessage()));
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleCommentNotFoundException(CommentNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDTO("Comment-not-found", exception.getMessage()));
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<ErrorDTO> handleUnauthorizedActionException(UnauthorizedActionException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorDTO("Unauthorized-action", exception.getMessage()));
    }



}
