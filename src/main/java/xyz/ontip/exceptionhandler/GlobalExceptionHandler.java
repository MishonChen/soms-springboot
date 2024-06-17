package xyz.ontip.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.ontip.exception.ForbiddenException;
import xyz.ontip.pojo.ResultEntity;

@Slf4j
@RestControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ForbiddenException.class)
    public ResultEntity<?> ForbiddenExceptionHandler(ForbiddenException e) {
        log.warn(e.getMessage());
        return ResultEntity.failure(404, e.getMessage());
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResultEntity<?> RuntimeExceptionHandle(RuntimeException e) {
        log.warn(e.getMessage());
        return ResultEntity.failure(404, e.getMessage());
    }
}
