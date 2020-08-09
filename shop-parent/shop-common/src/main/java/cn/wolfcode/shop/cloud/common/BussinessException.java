package cn.wolfcode.shop.cloud.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BussinessException extends RuntimeException{
    private CodeMsg codeMsg;
    public BussinessException(CodeMsg codeMsg){
        this.codeMsg = codeMsg;
    }
}
