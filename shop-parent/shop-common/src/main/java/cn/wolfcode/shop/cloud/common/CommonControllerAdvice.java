package cn.wolfcode.shop.cloud.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class CommonControllerAdvice {
    @ExceptionHandler(BussinessException.class)
    @ResponseBody
    public Result hanlderDefaultException(BussinessException e){
        e.printStackTrace();
        return Result.error(e.getCodeMsg());
    }
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result hanlderException(Exception e){
        e.printStackTrace();
        return Result.defaultError();
    }
}
