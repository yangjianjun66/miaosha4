package cn.wolfcode.shop.cloud.msg;

import cn.wolfcode.shop.cloud.common.CodeMsg;

import java.text.MessageFormat;

public class MemberServerCodeMsg extends CodeMsg{
    private MemberServerCodeMsg(Integer code,String msg){
        super(code,msg);
    }
    public static final MemberServerCodeMsg OP_ERROR = new MemberServerCodeMsg(500101,"非法的操作");
    public static final MemberServerCodeMsg LOGIN_ERROR = new MemberServerCodeMsg(500102,"用户名或密码错误");
    public static final MemberServerCodeMsg PARAN_ERROR = new MemberServerCodeMsg(500103,"参数校验:{0}");
    public MemberServerCodeMsg fillArgs(Object... args){
        MemberServerCodeMsg codeMsg = new MemberServerCodeMsg(this.getCode(),this.getMsg());
        codeMsg.setMsg(MessageFormat.format(codeMsg.getMsg(),args));
        return codeMsg;
    }
}