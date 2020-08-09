package cn.wolfcode.shop.cloud.web.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Getter
@Setter
public class LoginVO implements Serializable{
    @Pattern(regexp = "1[3456789]\\d{9}",message = "手机号码格式不正确")
    private String username;
    @NotEmpty(message = "密码不能为空")
    private String password;
}
