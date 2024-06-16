package xyz.ontip.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.ontip.pojo.ResultEntity;

@Slf4j
@RestControllerAdvice
@RestController
public class GlobalExceptionHandle {

    @ExceptionHandler(value = RuntimeException.class)
    public ResultEntity<String> runtimeExceptionHandle(RuntimeException e){
        log.warn(e.getMessage());
       return ResultEntity.failure(404,e.getMessage());
    }
}
