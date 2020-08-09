package cn.wolfcode.shop.cloud.web.advice;

import cn.wolfcode.shop.cloud.common.CommonControllerAdvice;
import cn.wolfcode.shop.cloud.common.Result;
import cn.wolfcode.shop.cloud.msg.MemberServerCodeMsg;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class MemberServerControllerAdvice extends CommonControllerAdvice{
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Result hanlderDefaultException(BindException e){
        e.printStackTrace();
        String msg = e.getAllErrors().get(0).getDefaultMessage();
        //CodeMsg codeMsg = MemberServerCodeMsg.PARAN_ERROR;
        //codeMsg.setMsg(MessageFormat.format(codeMsg.getMsg(),msg));
        return Result.error(MemberServerCodeMsg.PARAN_ERROR.fillArgs(msg));
    }

}
