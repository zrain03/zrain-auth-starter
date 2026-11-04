/*
 * Fail / File: src/main/java/dev/zrain/authstarter/common/ApiExceptionHandler.java
 * Pembangun / Developer: Zrain
 * Tarikh dicipta / Created: 2026-10-26 (bukti: Git initial commit fe0ad6dd39)
 * Tarikh dikemas kini / Last updated: 2026-11-04
 * BM: Memetakan kegagalan API kepada respons yang stabil tanpa pendedahan stack trace atau butiran dalaman.
 * EN: Maps API failures to stable responses without exposing stack traces or internal details.
 */
package dev.zrain.authstarter.common;

import dev.zrain.authstarter.auth.EmailAlreadyRegisteredException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
    /* BM: Memetakan kegagalan API kepada respons yang stabil tanpa pendedahan stack trace atau butiran dalaman.
       EN: Maps API failures to stable responses without exposing stack traces or internal details. */
public class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    /* BM: Pulangkan ralat validasi generik tanpa menyenaraikan data input sensitif.
       EN: Return a generic validation error without repeating sensitive input. */
    public ResponseEntity<ApiError> validation(HttpServletRequest req) {
        /* BM: Bina struktur ralat seragam bersama URI permintaan sebenar.
           EN: Build a consistent error structure using the actual request URI. */
        return error(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Check the supplied fields.", req);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    /* BM: Bezakan JSON rosak daripada masalah autorisasi dengan HTTP 400.
       EN: Distinguish malformed JSON from authorization failures with HTTP 400. */
    public ResponseEntity<ApiError> malformed(HttpServletRequest req) {
        /* BM: Bina struktur ralat seragam bersama URI permintaan sebenar.
           EN: Build a consistent error structure using the actual request URI. */
        return error(HttpStatus.BAD_REQUEST, "MALFORMED_JSON", "Request JSON is missing or invalid.", req);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    /* BM: Petakan precondition perkhidmatan yang dilanggar kepada HTTP 400.
       EN: Map violated service preconditions to HTTP 400. */
    public ResponseEntity<ApiError> argument(IllegalArgumentException ex, HttpServletRequest req) {
        /* BM: Bina struktur ralat seragam bersama URI permintaan sebenar.
           EN: Build a consistent error structure using the actual request URI. */
        return error(HttpStatus.BAD_REQUEST, "INVALID_INPUT", ex.getMessage(), req);
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    /* BM: Pulangkan HTTP 409 untuk emel yang telah didaftarkan.
       EN: Return HTTP 409 for an already registered email. */
    public ResponseEntity<ApiError> duplicate(HttpServletRequest req) {
        /* BM: Bina struktur ralat seragam bersama URI permintaan sebenar.
           EN: Build a consistent error structure using the actual request URI. */
        return error(HttpStatus.CONFLICT, "EMAIL_ALREADY_REGISTERED", "Email is already registered.", req);
    }

    @ExceptionHandler(BadCredentialsException.class)
    /* BM: Pulangkan HTTP 401 dengan mesej generik yang tidak mendedahkan kewujudan akaun.
       EN: Return HTTP 401 with a generic message that does not reveal account existence. */
    public ResponseEntity<ApiError> invalidCredentials(HttpServletRequest req) {
        /* BM: Bina struktur ralat seragam bersama URI permintaan sebenar.
           EN: Build a consistent error structure using the actual request URI. */
        return error(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "Invalid email or password.", req);
    }

    @ExceptionHandler(ResponseStatusException.class)
    /* BM: Pulangkan status akaun yang selamat dan elakkan mesej exception dalaman bocor.
       EN: Return a safe account status and avoid leaking exception internals. */
    public ResponseEntity<ApiError> status(ResponseStatusException ex, HttpServletRequest req) {
        HttpStatus http = HttpStatus.resolve(ex.getStatusCode().value());
        // BM: Gunakan status 500 apabila status exception tidak tersenarai dalam enum HttpStatus.
        // EN: Fall back to status 500 when the exception status is not represented in HttpStatus.
        if (http == null) http = HttpStatus.INTERNAL_SERVER_ERROR;
        /* BM: Bina struktur ralat seragam bersama URI permintaan sebenar.
           EN: Build a consistent error structure using the actual request URI. */
        return error(http, "ACCOUNT_UNAVAILABLE", "Account is unavailable.", req);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    /* BM: Bezakan sumber yang tidak dijumpai daripada kegagalan aplikasi.
       EN: Distinguish an absent resource from an application failure. */
    public ResponseEntity<ApiError> missing(HttpServletRequest req) {
        /* BM: Bina struktur ralat seragam bersama URI permintaan sebenar.
           EN: Build a consistent error structure using the actual request URI. */
        return error(HttpStatus.NOT_FOUND, "NOT_FOUND", "Resource not found.", req);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    /* BM: Gunakan HTTP 405 apabila method permintaan tidak dibenarkan.
       EN: Use HTTP 405 when the requested HTTP method is not supported. */
    public ResponseEntity<ApiError> method(HttpServletRequest req) {
        /* BM: Bina struktur ralat seragam bersama URI permintaan sebenar.
           EN: Build a consistent error structure using the actual request URI. */
        return error(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED", "HTTP method not allowed.", req);
    }

    @ExceptionHandler(DataAccessException.class)
    /* BM: Peta kegagalan akses data kepada ralat perkhidmatan sementara tanpa butiran SQL.
       EN: Map data-access failures to a temporary service error without SQL details. */
    public ResponseEntity<ApiError> database(HttpServletRequest req) {
        /* BM: Bina struktur ralat seragam bersama URI permintaan sebenar.
           EN: Build a consistent error structure using the actual request URI. */
        return error(HttpStatus.SERVICE_UNAVAILABLE, "DATABASE_UNAVAILABLE", "Service temporarily unavailable.", req);
    }

    @ExceptionHandler(Exception.class)
    /* BM: Gunakan fallback HTTP 500 yang tidak membocorkan exception dalaman.
       EN: Use an HTTP 500 fallback that does not disclose internal exceptions. */
    public ResponseEntity<ApiError> unknown(HttpServletRequest req) {
        /* BM: Bina struktur ralat seragam bersama URI permintaan sebenar.
           EN: Build a consistent error structure using the actual request URI. */
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Unexpected server error.", req);
    }

    /* BM: Bina struktur ralat seragam bersama URI permintaan sebenar.
       EN: Build a consistent error structure using the actual request URI. */
    private ResponseEntity<ApiError> error(HttpStatus status, String code, String message, HttpServletRequest req) {
        return ResponseEntity.status(status).body(new ApiError(status.value(), code, message, req.getRequestURI()));
    }
}
