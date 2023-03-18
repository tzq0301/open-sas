package cn.tzq0301.opensasopenmind.controller;

import cn.tzq0301.http.rest.result.Result;
import cn.tzq0301.http.rest.result.ResultCodeEnum;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<?> handleException(Exception e) {
        return Result.error(ResultCodeEnum.SYSTEM_ERROR, e.getMessage());
    }
}
