package com.hundanli.gulimall.product.exception;

import com.hundanli.common.exception.BiCodeEnum;
import com.hundanli.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author li
 * @version 1.0
 * @date 2020-05-14 20:48
 * 控制层统一异常处理类
 **/
@Slf4j
@RestControllerAdvice(basePackages = {"com.hundanli.gulimall.product.controller"})
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleException(MethodArgumentNotValidException e) {
        log.error(e.getClass().getName(), Arrays.toString(e.getStackTrace()));
        Map<String, Object> errorMap = new HashMap<>(8);
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            String field = fieldError.getField();
            String message = fieldError.getDefaultMessage();
            errorMap.put(field, message);
        });
        return R.error(BiCodeEnum.VALID_EXCEPTION.getCode(), BiCodeEnum.VALID_EXCEPTION.getMsg())
                .put("data", errorMap);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public R handleException(HttpMessageNotReadableException e) {
        log.error(e.getClass().getName(), Arrays.toString(e.getStackTrace()));
        return R.error(10004, "请求数据格式错误").put("data", "请求数据无法解析");
    }

    @ExceptionHandler(value = BadSqlGrammarException.class)
    public R handleException(BadSqlGrammarException e) {
        log.error(e.getClass().getName(), Arrays.toString(e.getStackTrace()));
        return R.error(10002, "请求无效").put("data", "无效请求");
    }

    @ExceptionHandler(Throwable.class)
    public R handleException(Throwable t) {
        log.error(t.getClass().getName(), Arrays.toString(t.getStackTrace()));
        return R.error(BiCodeEnum.UNKNOWN_EXCEPTION.getCode(), BiCodeEnum.UNKNOWN_EXCEPTION.getMsg())
                .put("data", "未知错误");
    }
}
