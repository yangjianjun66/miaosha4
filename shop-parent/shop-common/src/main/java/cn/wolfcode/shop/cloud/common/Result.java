package cn.wolfcode.shop.cloud.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    public static Integer DEFAULI_SUCCESS_CODE = 200;
    public static String DEFAULI_SUCCESS_MSG = "操作成功";
    public static Integer DEFAULI_ERROR_CODE = 500000;
    public static String DEFAULI_ERROR_MSG = "服务器繁忙,请稍后重试";
    private Integer code;
    private String msg;
    private T data;
    public static <T> Result<T> success(T data){
        return new Result<>(DEFAULI_SUCCESS_CODE,DEFAULI_SUCCESS_MSG,data);
    }
    public static <T> Result<T> success(String msg,T data){
        return new Result<>(DEFAULI_SUCCESS_CODE,msg,data);
    }
    public static <T> Result<T> defaultError(){
        return new Result<>(DEFAULI_ERROR_CODE,DEFAULI_ERROR_MSG,null);
    }
    public static <T> Result<T> error(CodeMsg codeMsg){
        return new Result<>(codeMsg.getCode(),codeMsg.getMsg(),null);
    }
    public boolean hasError(){
        return !this.code.equals(DEFAULI_SUCCESS_CODE);
    }
}
